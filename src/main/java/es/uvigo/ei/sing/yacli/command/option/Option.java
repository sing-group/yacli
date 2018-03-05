package es.uvigo.ei.sing.yacli.command.option;


public class Option<T> {
	private final String paramName;
	private final String shortName;
	private final String description;
	private final boolean optional;
	private final boolean requiresValue;
	private final boolean isMultiple;
	private final OptionConverter<T> converter;

	public Option(
			String paramName,
			String shortName,
			String description,
			boolean optional,
			boolean requiresValue,
			boolean isMultiple,
			OptionConverter<T> converter
	) {
		super();
		this.paramName = paramName;
		this.shortName = shortName;
		this.description = description;
		this.optional = optional;
		this.requiresValue = requiresValue;
		this.isMultiple = isMultiple;
		this.converter = converter;
	}

	public Option(
			String paramName,
			String shortName,
			String description,
			boolean optional,
			boolean requiresValue,
			OptionConverter<T> converter
	) {
		this(paramName, shortName, description, optional, requiresValue, false, converter);
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

	public OptionConverter<T> getConverter() {
		return converter;
	}

	public boolean hasName(String name) {
		return this.paramName.equalsIgnoreCase(name) ||
				this.shortName.equalsIgnoreCase(name);
	}

	@Override
	public String toString() {
		return this.getShortName();
	}
}
