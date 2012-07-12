package es.uvigo.ei.sing.yacli;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class CLIApplication {

	public class DefaultParameters implements Parameters {
		
		private HashMap<Option, Object> parameters;
		public DefaultParameters(HashMap<Option, Object> values) {
			this.parameters = values;
		}

		@Override
		public String getSingleValue(Option option) {
			if (option.isMultiple()){
				throw new IllegalArgumentException("Option "+option.getParamName()+"/"+option.getShortName()+" is multiple. Use getAllValues, instead");
			}
			return (parameters.get(option)==null)?null:parameters.get(option).toString();
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<String> getAllValues(Option option) {
			if (!option.isMultiple()){
				throw new IllegalArgumentException("Option "+option.getParamName()+"/"+option.getShortName()+" is not multiple. Use getSingleValue, instead");
			}
			return (List<String>) parameters.get(option);
	
		}

		@Override
		public boolean hasFlag(Option option) {
			if (option.requiresValue()){
				throw new IllegalArgumentException("Option "+option.getParamName()+"/"+option.getShortName()+" is not a flag. Use getSingleValue, instead");
			}
			return parameters.containsKey(option);
		}

		@Override
		public boolean hasOption(Option option) {
			return parameters.containsKey(option);
		}

	}
	protected List<Command> commands = buildCommands();
	private HashMap<String, Command> commandsByName = new HashMap<String, Command>();
	protected abstract List<Command> buildCommands(); //factory method
	protected abstract String getApplicationName();
	protected abstract String getApplicationCommand();
	
	protected String getDescription(){
		return "";
	}
	
	public CLIApplication() {
		for (Command c : commands){
			commandsByName.put(c.getName().toUpperCase(), c);
		}
	}
	
	public void run(String[] args){
		if (args.length==0){
			printHelp();
		}else if (args[0].equalsIgnoreCase("help")){
			printWellcome();
			if (args.length>=2){
				Command command = commandsByName.get(args[1].toUpperCase());
				if (command!=null) 
					printCommandHelp(command);
				else{
					System.err.println("Command "+args[1]+" not found");
					printHelp();
				}					
			}
		}else{ //run the command
			Command command = commandsByName.get(args[0].toUpperCase());
			
			if (command != null){
				String[] noFirst = new String[args.length-1];
				System.arraycopy(args, 1, noFirst, 0, noFirst.length);
				try{
					HashMap<Option, Object> values = parseCommand(command, noFirst);
					Parameters parameters = new DefaultParameters(values);
					command.execute(parameters);
				}catch(ParsingException e){
					System.err.println("Error parsing command: "+e.getMessage());
					printCommandHelp(command);
				}catch(Exception e){
					System.err.println("Error during execution: "+e.getMessage());
					e.printStackTrace();
				}
			}else{
				System.err.println("Command "+args[0]+" not found");
				printHelp();
			}
		
		}	
	}

	private Option findOption(Command command, String name){
		for (Option option: command.getOptions()){
			if (option.getParamName().equalsIgnoreCase(name) || option.getShortName().equalsIgnoreCase(name)){
				return option;
			}
		}
		return null;			
	}
	
	private HashMap<Option, Object> parseCommand(Command command, String[] arguments) throws ParsingException {
		HashMap<Option, Object> values = new HashMap<Option, Object>();
		
		command.getOptions();
		Option currentOption = null;
		
		for (String token : arguments){
			if (token.startsWith("-")){
				
				if (currentOption!=null && !values.containsKey(currentOption) && currentOption.requiresValue()){
					throw new IllegalArgumentException("option "+currentOption.getParamName()+" requires a value");
				}else if (currentOption!=null && !values.containsKey(currentOption) && !currentOption.requiresValue()){
					values.put(currentOption, null);
				}
				
				String optionName = null;
				if (token.charAt(1)=='-'){
					//starts with --
					optionName = token.substring(2);
				}else{
					//starts with -
					optionName = token.substring(1);
				}
				Option option = findOption(command, optionName);
				if (option == null){
					throw new ParsingException("option "+optionName+" not found");
				}else{
					currentOption = option;
				}
			}else{
				if (currentOption==null){
					throw new ParsingException("unable to parse. You should specify an option before a value");
				}else{
					if (values.containsKey(currentOption)){
						if (currentOption.isMultiple()){
							@SuppressWarnings("unchecked")
							List<String> valuesList = (List<String>) values.get(currentOption);
							valuesList.add(token);
						}else{
							throw new ParsingException("option "+currentOption.getParamName()+" was already specified");
						}
					}else{
						if (currentOption.isMultiple()){
							List<String> valuesList = new LinkedList<String>();
							valuesList.add(token);
							values.put(currentOption, valuesList);
						}else{
							values.put(currentOption, token);
						}
					}
				}
			}
			
		}
		//if there is the last option with no required value
		if (currentOption!=null && !values.containsKey(currentOption) && currentOption.requiresValue()){
			throw new ParsingException("option "+currentOption.getParamName()+" requires a value");
		}else if (currentOption!=null && !values.containsKey(currentOption) && !currentOption.requiresValue()){
			values.put(currentOption, null);
		}
		
		//validate mandatory arguments and put defaults
		for (Option option : command.getOptions()){
			if (!option.isOptional() && !values.containsKey(option) && (! (option instanceof DefaultValuedOption))){
				throw new ParsingException("option "+option.getParamName()+" is mandatory");
			}
			
			//put default-values if not specified before
			if (!values.containsKey(option) && ( (option instanceof DefaultValuedOption))){
				values.put(option, ((DefaultValuedOption) option).getDefaultValue());
			}
		}
		return values;
	}
	private void printCommandHelp(Command command) {
		System.err.println("Command "+command.getName());
		printUsage(command);
		for (Option option : command.getOptions()){
			System.err.println(
					"\t--"+option.getParamName()+"/-"+option.getShortName()
					+"\n\t\t"+option.getDescription()
					+((option instanceof DefaultValuedOption)?" (default: "+((DefaultValuedOption)option).getDefaultValue()+")":"")
					+((option.isMultiple())?". This option can be specified multiple times":""));
		}		
	}
	
	private void printUsage(Command command) {
		System.err.print("usage: "+this.getApplicationCommand()+" "+command.getName());
		for (Option option: command.getOptions()){
			if (option.isOptional()){
				System.err.print(" [");
			}else{
				System.err.print(" ");
			}
			System.err.print("-"+option.getShortName());
			if (option.requiresValue()){
				System.err.print(" <"+option.getParamName()+">");
			}
			
			if (option.isOptional()){
				System.err.print("]");
			}
		}
		System.err.println();
		
	}
	private void printHelp() {
		printWellcome();
		System.err.println("usage: "+this.getApplicationCommand()+" <command> [options]");
		
		System.err.println("where <command> is one of:");
		for (Command option: commands){
			System.err.println("\t"+option.getName()+"\n\t\t"+option.getDescription());
		}
		
		System.err.println("Write '"+this.getApplicationCommand()+" help <command>' to see command-specific help");
	}
	private void printWellcome() {
		System.err.println("Welcome to "+this.getApplicationName());
		System.err.println(this.getDescription());
	}
}

@SuppressWarnings("serial")
class ParsingException extends Exception{
	public ParsingException(String message) {
		super(message);
	}
}
