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
	public StringOption(
			List<OptionCategory> categories,
			String paramName, 
			String shortName, 
			String description,
			boolean optional, 
			boolean requiresValue, 
			boolean isMultiple
			) {
		super(categories, paramName, shortName, description, optional, requiresValue, isMultiple, new StringOptionConverter());
	}
	
	public StringOption(
			List<OptionCategory> categories,
			String paramName, 
			String shortName, 
			String description,
			boolean optional, 
			boolean requiresValue
			) {
		super(categories, paramName, shortName, description, optional, requiresValue, new StringOptionConverter());
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
