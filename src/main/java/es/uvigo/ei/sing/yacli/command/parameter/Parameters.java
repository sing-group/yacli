package es.uvigo.ei.sing.yacli.command.parameter;

import java.util.List;

import es.uvigo.ei.sing.yacli.command.option.Option;

public interface Parameters {
	public <T> T getSingleValue(Option<T> option);

	public String getSingleValueString(Option<?> option);

	public <T> List<T> getAllValues(Option<T> option);

	public List<String> getAllValuesString(Option<?> option);

	public boolean hasFlag(Option<?> option);

	public boolean hasOption(Option<?> option);
}
