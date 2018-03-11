package es.uvigo.ei.sing.yacli.command.option;

import java.util.List;

public class DefaultValuedOption<T> extends Option<T> {
	protected final String defaultValue;

	public DefaultValuedOption(
			String paramName,
			String shortName,
			String description,
			String defaultValue,
			OptionConverter<T> converter
	) {
		this(paramName, shortName, description, defaultValue, true, true, false, converter);
	}

	public DefaultValuedOption(
			String paramName,
			String shortName,
			String description,
			String defaultValue,
			boolean optional,
			boolean requiresValue,
			boolean isMultiple,
			OptionConverter<T> converter
	) {
		super(paramName, shortName, description, optional, requiresValue, isMultiple, converter);

		this.defaultValue = defaultValue;
	}
	public DefaultValuedOption(
			List<OptionCategory> categories,
			String paramName,
			String shortName,
			String description,
			String defaultValue,
			OptionConverter<T> converter
			) {
		this(categories, paramName, shortName, description, defaultValue, true, true, false, converter);
	}
	
	public DefaultValuedOption(
			List<OptionCategory> categories,
			String paramName,
			String shortName,
			String description,
			String defaultValue,
			boolean optional,
			boolean requiresValue,
			boolean isMultiple,
			OptionConverter<T> converter
			) {
		super(categories, paramName, shortName, description, optional, requiresValue, isMultiple, converter);
		
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
}
