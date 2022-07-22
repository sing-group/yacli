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

import java.util.ArrayList;
import java.util.List;

import es.uvigo.ei.sing.yacli.command.parameter.MultipleParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.SingleParameterValue;

public abstract class AbstractOptionConverter<T> implements OptionConverter<T> {
	
	@Override
	public List<T> convert(MultipleParameterValue mpv) {
		if (mpv == null) {
			return null;
		} else {
			final List<String> values = mpv.getValue();
			final List<T> typedValues = new ArrayList<>(values.size());

			for (String value : values) {
				typedValues.add(this.convert(new SingleParameterValue(value)));
			}

			return typedValues;
		}
	}

	@Override
	public boolean canConvert(MultipleParameterValue mpv) {
		if (mpv == null) {
			return true;
		} else {
			final List<String> values = mpv.getValue();

			for (String value : values) {
				if (value == null || !this.canConvert(new SingleParameterValue(value)))
					return false;
			}

			return true;
		}
	}
}
