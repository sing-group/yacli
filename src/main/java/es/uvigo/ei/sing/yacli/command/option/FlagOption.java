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

public class FlagOption extends Option<Object> {

  public FlagOption(
    String paramName, String shortName, String description
  ) {
    super(paramName, shortName, description, true, false, false, new FlagOptionConverter());
  }

  public FlagOption(
    List<OptionCategory> categories, String paramName, String shortName, String description
  ) {
    super(categories, paramName, shortName, description, true, false, false, new FlagOptionConverter());
  }

  protected static class FlagOptionConverter extends AbstractOptionConverter<Object> {

    @Override
    public Class<Object> getTargetClass() {
      return Object.class;
    }

    @Override
    public boolean canConvert(SingleParameterValue value) {
      return false;
    }

    @Override
    public Object convert(SingleParameterValue spv) {
      throw new RuntimeException("A FlagOption can't have a value");
    }
  }
}
