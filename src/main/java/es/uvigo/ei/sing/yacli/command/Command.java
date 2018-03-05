package es.uvigo.ei.sing.yacli.command;

import java.util.List;

import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public interface Command {
	public String getName();

	public String getDescriptiveName();

	public String getDescription();

	public List<Option<?>> getOptions();
	
	public Option<?> getOption(String name);

	public void execute(Parameters parameters) throws Exception;
}
