/*
 * #%L
 * Yast Another Command Line Interface library
 * %%
 * Copyright (C) 2015 - 2022 Daniel Glez-Peña, Hugo López-Fernández, and Miguel Reboiro-Jato
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package es.uvigo.ei.sing.yacli.command.option;

import java.lang.reflect.ParameterizedType;
import java.util.List;

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
	
	public StringConstructedOption(
			List<OptionCategory> categories,
			String paramName, 
			String shortName, 
			String description,
			boolean optional, 
			boolean requiresValue, 
			boolean isMultiple
		) {
			super(
				categories,
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
			List<OptionCategory> categories,
			String paramName, 
			String shortName, 
			String description,
			boolean optional, 
			boolean requiresValue
		) {
			super(
				categories,
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
