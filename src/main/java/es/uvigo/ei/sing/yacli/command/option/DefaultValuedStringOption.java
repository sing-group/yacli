package es.uvigo.ei.sing.yacli.command.option;

import java.util.List;

public class DefaultValuedStringOption extends DefaultValuedOption<String> {
	public DefaultValuedStringOption(
		String paramName, 
		String shortName,
		String description, 
		String defaultValue
	) {
		super(paramName, shortName, description, defaultValue, new StringOption.StringOptionConverter());
	}
	
	public DefaultValuedStringOption(
			List<OptionCategory> categories,
			String paramName, 
			String shortName,
			String description, 
			String defaultValue
			) {
		super(categories, paramName, shortName, description, defaultValue, new StringOption.StringOptionConverter());
	}
}
