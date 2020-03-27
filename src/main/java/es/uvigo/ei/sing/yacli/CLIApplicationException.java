package es.uvigo.ei.sing.yacli;

public class CLIApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CLIApplicationException(String message) {
		super(message);
	}
	public CLIApplicationException(Throwable cause) {
		super(cause);
	}
}