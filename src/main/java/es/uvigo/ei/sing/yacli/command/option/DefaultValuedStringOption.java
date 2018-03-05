package es.uvigo.ei.sing.yacli.command.option;


public class DefaultValuedStringOption extends DefaultValuedOption<String> {
	public DefaultValuedStringOption(
		String paramName, 
		String shortName,
		String description, 
		String defaultValue
	) {
		super(paramName, shortName, description, defaultValue, new StringOption.StringOptionConverter());
	}
}
