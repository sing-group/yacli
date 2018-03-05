package es.uvigo.ei.sing.yacli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandLine implements Runnable {
	protected String getExitCommand() {
		return "quit";
	}
	
	protected abstract Class<? extends CLIApplication> getCLIApplication();
	
	@Override
	public void run() {
		final Class<? extends CLIApplication> cli = this.getCLIApplication();
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String line;
		
		try {
			// System.err to System.out redirection
			System.setErr(System.out);
			
			final CLIApplication application = cli.newInstance();
			application.setShowApplicationCommandInHelp(false);
			
			System.out.println("Welcome to " + application.getApplicationName());
			System.out.print("> ");
			while ((line = br.readLine()) != null && !line.equals(this.getExitCommand())) {
				final String[] tokens = tokenize(line);
				
				application.run(tokens);
				
				System.out.print("\n> ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Taken from JSAP library
	 * 
	 * Goofy internal utility to avoid duplicated code. If the specified
	 * StringBuffer is not empty, its contents are appended to the resulting
	 * array (temporarily stored in the specified List). The StringBuffer
	 * is then emptied in order to begin storing the next argument.
	 * @param builder
	 *            the StringBuffer storing the current argument.
	 * @param result
	 *            the List temporarily storing the resulting argument array.
	 */
	private static void addToken(StringBuilder builder, List<String> result) {
		if (builder.length() > 0) {
			result.add(builder.toString());
			builder.setLength(0);
		}
	}

	/**
	 * Taken from JSAP library
	 * 
	 * Parses the specified command line into an array of individual arguments.
	 * Arguments containing spaces should be enclosed in quotes. Quotes that
	 * should be in the argument string should be escaped with a preceding
	 * backslash ('\') character. Backslash characters that should be in the
	 * argument string should also be escaped with a preceding backslash
	 * character.
	 * 
	 * @param commandLine
	 *            the command line to parse
	 * @return an argument array representing the specified command line.
	 */
	private static String[] tokenize(String commandLine) {
		final List<String> tokens = new LinkedList<>();

		if (commandLine != null) {
			final int commandLength = commandLine.length();
			boolean insideQuotes = false;
			
			final StringBuilder builder = new StringBuilder();

			for (int i = 0; i < commandLength; ++i) {
				final char c = commandLine.charAt(i);
				
				if (c == '"') {
					addToken(builder, tokens);
					insideQuotes = !insideQuotes;
				} else if (c == '\\') {
					if ((commandLength > i + 1) && ((commandLine.charAt(i + 1) == '"') || 
						(commandLine.charAt(i + 1) == '\\'))
					) {
						builder.append(commandLine.charAt(i + 1));
						++i;
					} else {
						builder.append("\\");
					}
				} else {
					if (insideQuotes) {
						builder.append(c);
					} else {
						if (Character.isWhitespace(c)) {
							addToken(builder, tokens);
						} else {
							builder.append(c);
						}
					}
				}
			}
			addToken(builder, tokens);
		}

		return tokens.toArray(new String[tokens.size()]);
	}
}
