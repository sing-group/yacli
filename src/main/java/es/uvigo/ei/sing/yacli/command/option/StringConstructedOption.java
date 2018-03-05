package es.uvigo.ei.sing.yacli.command.option;

import java.lang.reflect.ParameterizedType;

import es.uvigo.ei.sing.yacli.command.parameter.SingleParameterValue;

public abstract class StringConstructedOption<T> extends Option<T> {
	public StringConstructedOption(
		String paramName, 
		String shortName, 
		String description,
		boolean optional, 
		boolean requiresValue, 
		boolean isMultiple
	) {
		super(
			paramName, 
			shortName, 
			description, 
			optional, 
			requiresValue, 
			isMultiple, 
			new StringConstructedConverter<T>()
		);

		this.getConverter().setTargetClass(this.getParamClass());
	}

	public StringConstructedOption(
		String paramName, 
		String shortName, 
		String description,
		boolean optional, 
		boolean requiresValue
	) {
		super(
			paramName, 
			shortName, 
			description, 
			optional, 
			requiresValue, 
			new StringConstructedConverter<T>()
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
	
	public static final class StringConstructedConverter<T>
	extends AbstractOptionConverter<T> {
		private Class<T> targetClass;

		StringConstructedConverter() {}
		
		void setTargetClass(Class<T> clazz) {
			this.targetClass = clazz;
		}
		
		@Override
		public Class<T> getTargetClass() {
			return this.targetClass;
		}

		@Override
		public T convert(SingleParameterValue value) {
			try {
				if (value == null) {
					return null;
				} else {
					return this.targetClass.getConstructor(String.class).newInstance(value.getValue());
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Error converting value: " + value.getValue(), e);
			}
		}

		@Override
		public boolean canConvert(SingleParameterValue value) {
			try {
				this.convert(value);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}
}
