package es.uvigo.ei.sing.yacli;


import java.util.HashMap;
import java.util.List;

public abstract class CLIApplication {

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
					HashMap<Option, String> values = parseCommand(command, noFirst);
					command.execute(values);
				}catch(ParsingException e){
					System.err.println("Error parsing command: "+e.getMessage());
					System.err.println("Please type '"+this.getApplicationCommand()+" help "+command.getName()+"' for more info");
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
	
	private HashMap<Option, String> parseCommand(Command command, String[] arguments) throws ParsingException {
		HashMap<Option, String> values = new HashMap<Option, String>();
		
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
						throw new ParsingException("option "+currentOption.getParamName()+" was already specified");
					}else{
						values.put(currentOption, token);
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
					+((option instanceof DefaultValuedOption)?" (default: "+((DefaultValuedOption)option).getDefaultValue()+")":""));
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
