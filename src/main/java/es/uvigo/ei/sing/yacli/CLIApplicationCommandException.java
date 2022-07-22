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
package es.uvigo.ei.sing.yacli;

import es.uvigo.ei.sing.yacli.command.Command;

public class CLIApplicationCommandException extends CLIApplicationException {
	private static final long serialVersionUID = 1L;

	private Command command;
	public CLIApplicationCommandException(Command command, String message) {
		super(message);
		this.command = command;
	}
	public CLIApplicationCommandException(Command command, Throwable cause) {
		super(cause);
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}
}