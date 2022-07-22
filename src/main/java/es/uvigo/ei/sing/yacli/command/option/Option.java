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

import java.util.Arrays;
import java.util.List;

import es.uvigo.ei.sing.yacli.command.parameter.MultipleParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.ParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.SingleParameterValue;

public class Option<T> {
	private final String paramName;
	private final String shortName;
	private final String description;
	private final boolean optional;
	private final boolean requiresValue;
	private final boolean isMultiple;
	private final OptionConverter<T> converter;
	private final List<OptionCategory> categories;
	
	public Option(
			List<OptionCategory> categories,
			String paramName,
			String shortName,
			String description,
			boolean optional,
			boolean requiresValue,
			boolean isMultiple,
			OptionConverter<T> converter
	) {
		super();
		this.categories = categories;
		this.paramName = paramName;
		this.shortName = shortName;
		this.description = description;
		this.optional = optional;
		this.requiresValue = requiresValue;
		this.isMultiple = isMultiple;
		this.converter = converter;
	}

	public Option(
			List<OptionCategory> categories,
			String paramName,
			String shortName,
			String description,
			boolean optional,
			boolean requiresValue,
			OptionConverter<T> converter
	) {
		this(categories, paramName, shortName, description, optional, requiresValue, false, converter);
	}

	public Option(
			String paramName,
			String shortName,
			String description,
			boolean optional,
			boolean requiresValue,
			boolean isMultiple,
			OptionConverter<T> converter
	) {
		this(Arrays.asList(OptionCategory.DEFAULT_CATEGORY), paramName, shortName, description, optional, requiresValue, isMultiple, converter);
	}

	public Option(
			String paramName,
			String shortName,
			String description,
			boolean optional,
			boolean requiresValue,
			OptionConverter<T> converter
	) {
		this(Arrays.asList(OptionCategory.DEFAULT_CATEGORY), paramName, shortName, description, optional, requiresValue, converter);
	}
	
	public List<OptionCategory> getCategories() {
		return categories;
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

	public boolean canConvert(ParameterValue<?> value) {
		if (value instanceof SingleParameterValue) {
			return this.getConverter().canConvert((SingleParameterValue) value);
		} else if (value instanceof MultipleParameterValue) {
			return this.getConverter().canConvert((MultipleParameterValue) value);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return this.getShortName();
	}
}
