package es.uvigo.ei.sing.yacli;


public class Option {

	private String paramName;
	private String shortName;
	private String description;
	private boolean optional;
	private boolean requiresValue;
	private boolean isMultiple;

	public Option(
			String paramName, 
			String shortName, 
			String description,
			boolean optional, 
			boolean requiresValue,
			boolean isMultiple) {
		super();
		this.paramName = paramName;
		this.shortName = shortName;
		this.description = description;
		this.optional = optional;
		this.requiresValue = requiresValue;
		this.isMultiple = isMultiple;
	}

	public Option(
			String paramName, 
			String shortName, 
			String description,
			boolean optional, 
			boolean requiresValue) {
		this(paramName, shortName, description, optional, requiresValue, false);
	}
	public String getParamName() {
		return paramName;
	}

	public String getShortName() {
		return shortName;
	}

	public String getDescription() {
		return description;
	}

	public boolean isOptional() {
		return optional;
	}

	public boolean requiresValue() {
		return requiresValue;
	}
	
	public boolean isMultiple() {
		return isMultiple;
	}
	
	

}
