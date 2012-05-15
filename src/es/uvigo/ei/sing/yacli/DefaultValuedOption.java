package es.uvigo.ei.sing.yacli;


public final class DefaultValuedOption extends Option {

	private String defaultValue;

	public DefaultValuedOption(String paramName, String shortName,
			String description, String defaultValue) {		
		super(paramName, shortName, description, true, true);
		this.defaultValue = defaultValue;
		
	}
	public String getDefaultValue() {
		return defaultValue;
	}

}
