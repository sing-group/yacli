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
