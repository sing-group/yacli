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