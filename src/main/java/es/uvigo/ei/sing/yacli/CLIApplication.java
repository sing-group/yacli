package es.uvigo.ei.sing.yacli;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.uvigo.ei.sing.yacli.command.Command;
import es.uvigo.ei.sing.yacli.command.option.DefaultValuedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.DefaultParameters;
import es.uvigo.ei.sing.yacli.command.parameter.MultipleParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.ParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;
import es.uvigo.ei.sing.yacli.command.parameter.SingleParameterValue;

public abstract class CLIApplication {
	private final Map<String, Command> commandsByName;
	
	protected abstract List<Command> buildCommands(); //factory method
	protected abstract String getApplicationName();
	protected abstract String getApplicationCommand();
	
	private boolean showApplicationCommandInHelp;
	
	public CLIApplication() {
		this(true, true);
	}
	
	public CLIApplication(boolean showApplicationCommandInHelp) {
		this(showApplicationCommandInHelp, true);
	}
	
	protected CLIApplication(boolean showApplicationCommandInHelp, boolean preloadCommands) {
		this.showApplicationCommandInHelp = showApplicationCommandInHelp;
		this.commandsByName = new LinkedHashMap<>();
		
		if (preloadCommands) {
			this.loadCommands();
		}
	}
	
	protected void loadCommands() {
		for (Command c : buildCommands()){
			commandsByName.put(c.getName().toUpperCase(), c);
		}		
	}
	
	protected List<Command> listCommands() {
		return new ArrayList<>(this.commandsByName.values());
	}
	
	public boolean isShowApplicationCommandInHelp() {
		return showApplicationCommandInHelp;
	}
	
	public void setShowApplicationCommandInHelp(boolean showApplicationCommandInHelp) {
		this.showApplicationCommandInHelp = showApplicationCommandInHelp;
	}
	
	protected String getDescription() {
		return "";
	}
	
	public void run(String[] args){
		if (args.length == 0) {
			printHelp();
		} else if (args[0].equalsIgnoreCase("help")) {
			printWelcome();
			
			if (args.length >= 2) {
				final Command command = commandsByName.get(args[1].toUpperCase());
				
				if (command != null)
					printCommandHelp(command);
				else {
					System.err.println("Command " + args[1] + " not found");
					printHelp();
				}
			}
		} else { // run the command
			final Command command = commandsByName.get(args[0].toUpperCase());

			if (command != null) {
				final String[] noFirst = new String[args.length - 1];
				System.arraycopy(args, 1, noFirst, 0, noFirst.length);
				
				try {
					final Map<Option<?>, ParameterValue<?>> values = parseCommand(command, noFirst);
					
					final Parameters parameters = new DefaultParameters(values);
					command.execute(parameters);
				} catch (ParsingException e) {
					System.err.println("Error parsing command: " + e.getMessage());
					printCommandHelp(command);
				} catch (Exception e) {
					System.err.println("Error during execution: " + e.getMessage());
					e.printStackTrace();
				}
			} else {
				System.err.println("Command " + args[0] + " not found");
				
				printHelp();
			}

		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<Option<?>, ParameterValue<?>> parseCommand(Command command, String[] arguments)
	throws ParsingException {
		final Map<Option<?>, Object> values = new HashMap<Option<?>, Object>();
		
		command.getOptions();
		Option<?> currentOption = null;
		
		for (String token : arguments){
			if (token.startsWith("-")) {
				if (currentOption != null 
					&& !values.containsKey(currentOption)
					&& currentOption.requiresValue()
				) {
					throw new IllegalArgumentException(
						String.format("option %s requires a value", currentOption.getParamName())
					);
				} else if (currentOption != null
					&& !values.containsKey(currentOption)
					&& !currentOption.requiresValue()
				) {
					values.put(currentOption, null);
				}
				
				String optionName = null;
				if (token.charAt(1) == '-') {
					//starts with --
					optionName = token.substring(2);
				} else {
					//starts with -
					optionName = token.substring(1);
				}
				
				final Option<?> option = command.getOption(optionName);
				if (option == null) {
					throw new ParsingException(String.format("option % not found", optionName));
				}else{
					currentOption = option;
				}
			} else {
				if (currentOption == null) {
					throw new ParsingException("unable to parse. You should specify an option before a value");
				} else {
					if (values.containsKey(currentOption)) {
						if (currentOption.isMultiple()) {
							List<String> valuesList = (List<String>) values.get(currentOption);
							valuesList.add(token);
						} else {
							throw new ParsingException("option " + currentOption.getParamName() + " was already specified");
						}
					} else {
						if (currentOption.isMultiple()){
							values.put(currentOption, Arrays.asList(token));
						} else {
							values.put(currentOption, token);
						}
					}
				}
			}
		}
		
		//if there is the last option with no required value
		if (currentOption != null && !values.containsKey(currentOption) && currentOption.requiresValue()) {
			throw new ParsingException("option " + currentOption.getParamName() + " requires a value");
		} else if (currentOption != null && !values.containsKey(currentOption) && !currentOption.requiresValue()) {
			values.put(currentOption, null);
		}
		
		//validate mandatory arguments and put defaults
		for (Option<?> option : command.getOptions()){
			if (!option.isOptional() && !values.containsKey(option) && !(option instanceof DefaultValuedOption)) {
				throw new ParsingException("option "+option.getParamName()+" is mandatory");
			}
			
			//put default-values if not specified before
			if (!values.containsKey(option) && ( (option instanceof DefaultValuedOption))){
				values.put(option, ((DefaultValuedOption<?>) option).getDefaultValue());
			}
		}
		
		final Map<Option<?>, ParameterValue<?>> paramValues = new HashMap<Option<?>, ParameterValue<?>>();
		for (Map.Entry<Option<?>, Object> parameterValue : values.entrySet()) {
			final Object value = parameterValue.getValue();
			final Option<?> key = parameterValue.getKey();
			paramValues.put(key, 
				key.isMultiple()?
					new MultipleParameterValue((List<String>) value):
					new SingleParameterValue((String) value)
			);
		}
		
		return paramValues;
	}
	
	private void printCommandHelp(Command command) {
		System.err.println("Command " + command.getName());
		printUsage(command);
		for (Option<?> option : command.getOptions()){
			System.err.println(
				"\t--" + option.getParamName() + "/-" + option.getShortName()
				+ "\n\t\t" + option.getDescription()
				+ ((option instanceof DefaultValuedOption)?" (default: " + ((DefaultValuedOption<?>)option).getDefaultValue()+")":"")
				+ ((option.isMultiple())?". This option can be specified multiple times":"")
			);
		}		
	}
	
	private void printUsage(Command command) {
		System.err.print("usage: " + this.getApplicationCommand() + " " + command.getName());
		
		for (Option<?> option : command.getOptions()) {
			if (option.isOptional()) {
				System.err.print(" [");
			} else {
				System.err.print(" ");
			}

			System.err.print("-" + option.getShortName());
			if (option.requiresValue()) {
				System.err.print(" <" + option.getParamName() + ">");
			}

			if (option.isOptional()) {
				System.err.print("]");
			}
		}
		System.err.println();
		
	}
	private void printHelp() {
		printWelcome();
		System.err.println("usage: " + this.getApplicationCommand() + " <command> [options]");
		
		System.err.println("where <command> is one of:");
		for (Command option : listCommands()){
			System.err.println("\t" + option.getName() + "\n\t\t" + option.getDescription());
		}
		
		if (this.isShowApplicationCommandInHelp()) {
			System.err.println("Write '" + this.getApplicationCommand() + " help <command>' to see command-specific help");
		} else {
			System.err.println("Write 'help <command>' to see command-specific help");
		}
	}
	
	private void printWelcome() {
		System.err.println("Welcome to "+this.getApplicationName());
		System.err.println(this.getDescription());
	}
}
