package es.uvigo.ei.sing.yacli;


import java.util.List;

public interface Command {

	public String getName();
	public String getDescription();
	public List<Option> getOptions();
	
	public void execute(Parameters parameters) throws Exception;
	
	
}
