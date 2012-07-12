package es.uvigo.ei.sing.yacli;


import java.util.List;
import java.util.Map;

public interface Command {

	public String getName();
	public String getDescription();
	public List<Option> getOptions();
	
	public void execute(Parameters parameters) throws Exception;
	
	
}
