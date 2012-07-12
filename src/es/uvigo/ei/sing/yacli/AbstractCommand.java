package es.uvigo.ei.sing.yacli;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractCommand implements Command {

	protected List<Option> options = new LinkedList<Option>();
	
	public AbstractCommand() {
		this.options = createOptions();
	}
	
	@Override
	public List<Option> getOptions() {
		return Collections.unmodifiableList(options);
	}

	
	protected Option findOption(String name){
		for (Option option: this.getOptions()){
			if (option.getParamName().equalsIgnoreCase(name) || option.getShortName().equalsIgnoreCase(name)){
				return option;
			}
		}
		return null;			
	}
	protected String getSingleValue(Map<Option, Object> parameters, String optionName){
		Option o = findOption(optionName);
		if (o.isMultiple()){
			throw new IllegalArgumentException("Option "+optionName+" is multiple. Use getAllValues, instead");
		}
		return (parameters.get(o)==null)?null:parameters.get(o).toString();
	}
	
	protected List<String> getAllValues(Map<Option, Object> parameters, String optionName){
		Option o = findOption(optionName);
		if (!o.isMultiple()){
			throw new IllegalArgumentException("Option "+optionName+" is not multiple. Use getSingleValue, instead");
		}
		return (List<String>) parameters.get(o);
	}
	
	protected boolean hasFlag(Map<Option, Object> parameters, String optionName){
		Option o = findOption(optionName);
		if (o.requiresValue()){
			throw new IllegalArgumentException("Option "+optionName+" is not a flag. Use getSingleValue, instead");
		}
		return parameters.containsKey(o);
		
	}
	
	protected boolean hasOption(Map<Option, Object> parameters, String optionName){
		Option o = findOption(optionName);		
		return parameters.containsKey(o);
		
	}
	
	protected abstract List<Option> createOptions(); //factory method

}
