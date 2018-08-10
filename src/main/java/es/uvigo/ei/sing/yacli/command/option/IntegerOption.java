/*
 * #%L
 * BDBM CLI
 * %%
 * Copyright (C) 2014 - 2015 Miguel Reboiro-Jato, Critina P. Vieira, Hugo López-Fdez, Noé Vázquez González, Florentino Fdez-Riverola and Jorge Vieira
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

import es.uvigo.ei.sing.yacli.command.parameter.SingleParameterValue;

public class IntegerOption extends Option<Integer> {

	public IntegerOption(
		String paramName,
		String shortName,
		String description,
		boolean optional,
		boolean isMultiple
	) {
		super(paramName, shortName, description, optional, true, isMultiple, new IntegerConverter());
	}

	public IntegerOption(
		String paramName,
		String shortName,
		String description,
		boolean optional
	) {
		super(paramName, shortName, description, optional, true, new IntegerConverter());
	}
	
	public IntegerOption(
		List<OptionCategory> categories,
		String paramName,
		String shortName,
		String description,
		boolean optional,
		boolean isMultiple
	) {
		super(categories, paramName, shortName, description, optional, true, isMultiple, new IntegerConverter());
	}
	
	public IntegerOption(
		List<OptionCategory> categories,
		String paramName,
		String shortName,
		String description,
		boolean optional
	) {
		super(categories, paramName, shortName, description, optional, true, new IntegerConverter());
	}
	
	protected static class IntegerConverter extends AbstractOptionConverter<Integer> {
		@Override
		public Class<Integer> getTargetClass() {
			return Integer.class;
		}

		@Override
		public boolean canConvert(SingleParameterValue value) {
			return true;
		}

		@Override
		public Integer convert(SingleParameterValue spv) {
			final String value = spv.getValue();

			return Integer.valueOf(value);
		}
	}
}
