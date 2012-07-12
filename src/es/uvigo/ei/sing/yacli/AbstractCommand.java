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
	protected abstract List<Option> createOptions(); //factory method

}
