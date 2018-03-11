package es.uvigo.ei.sing.yacli.command.option;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import es.uvigo.ei.sing.yacli.command.option.StringConstructedOption.StringConstructedConverter;

public class DefaultValuedStringConstructedOption<T> extends DefaultValuedOption<T> {
	public DefaultValuedStringConstructedOption(
		String paramName,
		String shortName,
		String description,
		String defaultValue
	) {
		super(paramName, shortName, description, defaultValue, 
			new StringConstructedOption.StringConstructedConverter<T>()
		);

		this.getConverter().setTargetClass(this.getParamClass());
	}
	public DefaultValuedStringConstructedOption(
			List<OptionCategory> categories,
			String paramName,
			String shortName,
			String description,
			String defaultValue
			) {
		super(categories, paramName, shortName, description, defaultValue, 
				new StringConstructedOption.StringConstructedConverter<T>()
				);
		
		this.getConverter().setTargetClass(this.getParamClass());
	}
	
	@Override
	public StringConstructedConverter<T> getConverter() {
		return (StringConstructedConverter<T>) super.getConverter();
	}
	
	@SuppressWarnings("unchecked")
	private final Class<T> getParamClass() {
		return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
