package es.uvigo.ei.sing.yacli.command.option;

import java.util.List;

import es.uvigo.ei.sing.yacli.command.parameter.MultipleParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.SingleParameterValue;

public class StringOption extends Option<String> {
	public StringOption(
		String paramName, 
		String shortName, 
		String description,
		boolean optional, 
		boolean requiresValue, 
		boolean isMultiple
	) {
		super(paramName, shortName, description, optional, requiresValue, isMultiple, new StringOptionConverter());
	}

	public StringOption(
		String paramName, 
		String shortName, 
		String description,
		boolean optional, 
		boolean requiresValue
	) {
		super(paramName, shortName, description, optional, requiresValue, new StringOptionConverter());
	}
	
	@Override
	public StringOptionConverter getConverter() {
		return (StringOptionConverter) super.getConverter();
	}

	public static final class StringOptionConverter implements OptionConverter<String> {
		StringOptionConverter() {}
		
		@Override
		public Class<String> getTargetClass() {
			return String.class;
		}
		
		@Override
		public String convert(SingleParameterValue value) {
			return value == null ? null : value.getValue();
		}
		
		@Override
		public List<String> convert(MultipleParameterValue values) {
			return values.getValue();
		}
		
		@Override
		public boolean canConvert(SingleParameterValue value) {
			return true;
		}
		
		@Override
		public boolean canConvert(MultipleParameterValue value) {
			return true;
		}
	}
}
