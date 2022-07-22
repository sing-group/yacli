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
package es.uvigo.ei.sing.yacli.command.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.uvigo.ei.sing.yacli.command.option.Option;

public class DefaultParameters implements Parameters {
	private final Map<Option<?>, ParameterValue<?>> optionValues;
	
	public DefaultParameters(Map<Option<?>, ParameterValue<?>> values) {
		this(values, true);
	}
	
	public DefaultParameters(Map<Option<?>, ParameterValue<?>> values, boolean checkValues) {
		if (checkValues) {
			for (Map.Entry<Option<?>, ParameterValue<?>> value : values.entrySet()) {
				if (value.getKey().isMultiple() && !(value.getValue() instanceof MultipleParameterValue)) {
					throw new IllegalArgumentException("Option %s/%s is multiple but single value was assigned");
				} else if (!value.getKey().isMultiple() && !(value.getValue() instanceof SingleParameterValue)) {
					throw new IllegalArgumentException("Option %s/%s is single but multiple value was assigned");
				}
			}
		}
		
		this.optionValues = values;
	}

	protected <T> SingleParameterValue getSingleParameterValue(Option<T> option) {
		if (option.isMultiple()) {
			throw new IllegalArgumentException(
				String.format(
					"Option %s/%s is multiple. Use getAllValues, instead",
					option.getParamName(),
					option.getShortName()
				)
			);
		}

		if (!this.hasOption(option)) {
			throw new IllegalArgumentException("Missing option: " + option.getParamName());
		}
		
		return (SingleParameterValue) this.optionValues.get(option);
	}

	protected <T> MultipleParameterValue getMultipleParameterValue(Option<T> option) {
		if (!option.isMultiple()) {
			throw new IllegalArgumentException(
				String.format(
					"Option %s/%s is not multiple. Use getSingleValue, instead",
					option.getParamName(),
					option.getShortName()
				)
			);
		}
		
		if (!this.hasOption(option)) {
			throw new IllegalArgumentException("Missing option: " + option.getParamName());
		}
		
		return (MultipleParameterValue) this.optionValues.get(option);
	}

	@Override
	public <T> T getSingleValue(Option<T> option) {
		final T value = option.getConverter().convert(
			this.getSingleParameterValue(option)
		);
		
		if (value == null && !option.isOptional()) {
			throw new RuntimeException("Missing value for non-optional option: " + option.getShortName());
		} else {
			return value;
		}
	}

	@Override
	public String getSingleValueString(Option<?> option) {
		return this.getSingleParameterValue(option).getValue();
	}
	
	@Override
	public <T> List<T> getAllValues(Option<T> option)
	throws IllegalArgumentException {
		return option.getConverter().convert(this.getMultipleParameterValue(option));
	}
	
	@Override
	public List<String> getAllValuesString(Option<?> option) {
		return this.getMultipleParameterValue(option).getValue();
	}

	@Override
	public boolean hasFlag(Option<?> option) {
		if (option.requiresValue()) {
			throw new IllegalArgumentException(
				String.format(
					"Option %s/%s is not a flag. Use getSingleValue, instead",
					option.getParamName(),
					option.getShortName()
				)
			);
		}
		
		return optionValues.containsKey(option);
	}

	@Override
	public boolean hasOption(Option<?> option) {
		return optionValues.containsKey(option);
	}
	
	@Override
	public List<Option<?>> listOptions() {
		return new ArrayList<>(this.optionValues.keySet());
	}
}