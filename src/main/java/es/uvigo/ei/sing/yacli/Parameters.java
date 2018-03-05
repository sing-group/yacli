package es.uvigo.ei.sing.yacli;

import java.util.List;

public interface Parameters {
	
	public String getSingleValue(Option option);
	
	public List<String> getAllValues(Option option);
	
	public boolean hasFlag(Option option);
	
	public boolean hasOption(Option option);
}
