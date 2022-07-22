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
