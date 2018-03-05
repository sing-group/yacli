package es.uvigo.ei.sing.yacli.command.parameter;

import java.util.List;

import es.uvigo.ei.sing.yacli.command.option.Option;

public interface Parameters {
	
	public String getSingleValue(Option option);
	
	public List<String> getAllValues(Option option);
	
	public boolean hasFlag(Option option);
	
	public boolean hasOption(Option option);
}
