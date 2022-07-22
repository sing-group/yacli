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
package es.uvigo.ei.sing.yacli.command;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import es.uvigo.ei.sing.yacli.command.option.Option;

public abstract class AbstractCommand implements Command {
  protected final List<Option<?>> options;

  public AbstractCommand() {
    this.options = createOptions();
  }

  @Override
  public List<Option<?>> getOptions() {
    return unmodifiableList(options);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Option<?> getOption(String name) {
    for (Option<?> option : this.getOptions()) {
      if (option.hasName(name))
        return option;
    }

    return null;
  }

  protected abstract List<Option<?>> createOptions();
}
