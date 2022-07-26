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

import static es.uvigo.ei.sing.yacli.command.CommandPrinter.printCommandOptionsExtended;
import static java.lang.System.arraycopy;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.uvigo.ei.sing.yacli.command.Command;
import es.uvigo.ei.sing.yacli.command.CommandPrinterConfiguration;
import es.uvigo.ei.sing.yacli.command.option.DefaultValuedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.DefaultParameters;
import es.uvigo.ei.sing.yacli.command.parameter.MultipleParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.ParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;
import es.uvigo.ei.sing.yacli.command.parameter.SingleParameterValue;

public abstract class CLIApplication {
	private final Map<String, Command> commandsByName;

	protected abstract List<Command> buildCommands(); // factory method

	protected abstract String getApplicationName();

	protected abstract String getApplicationVersion();

	protected abstract String getApplicationCommand();
	
  protected CommandPrinterConfiguration getCommandPrinterConfiguration() {
    return new CommandPrinterConfiguration();
  };

	private boolean showApplicationCommandInHelp;
	private boolean ignoreUnrecognizedOptions;
  private CommandPrinterConfiguration commandPrinterConfiguration;

	public CLIApplication() {
		this(true, true);
	}

	public CLIApplication(boolean showApplicationCommandInHelp) {
		this(showApplicationCommandInHelp, true);
	}

	protected CLIApplication(boolean showApplicationCommandInHelp,
		boolean preloadCommands
	) {
		this(showApplicationCommandInHelp, preloadCommands, false);
	}

	protected CLIApplication(boolean showApplicationCommandInHelp,
		boolean preloadCommands, boolean ignoreUnrecognizedOptions
	) {
		this.showApplicationCommandInHelp = showApplicationCommandInHelp;
		this.commandsByName = new LinkedHashMap<>();
		this.ignoreUnrecognizedOptions = ignoreUnrecognizedOptions;
		if (preloadCommands) {
			this.loadCommands();
		}
		this.commandPrinterConfiguration = getCommandPrinterConfiguration();
	}

	protected void loadCommands() {
		for (Command c : buildCommands()) {
			commandsByName.put(c.getName().toUpperCase(), c);
		}
	}

	protected List<Command> listCommands() {
		return new ArrayList<>(this.commandsByName.values());
	}

	public boolean isShowApplicationCommandInHelp() {
		return showApplicationCommandInHelp;
	}

	public void setShowApplicationCommandInHelp(boolean showApplicationCommandInHelp) {
		this.showApplicationCommandInHelp = showApplicationCommandInHelp;
	}

	protected String getDescription() {
		return "";
	}

	public void run(String[] args) throws CLIApplicationException {
		PrintStream out = System.err;
		if (args.length == 0) {
			printHelp(out);
		} else if (isVersionArgument(args[0])) {
			out.println(getApplicationVersion());
		} else if (isHelpArgument(args[0])) {
			if (args.length >= 2) {
				final Command command = commandsByName
					.get(args[1].toUpperCase());

				if (command != null) {
					printWelcome(out);
					printCommandHelp(command, out, commandPrinterConfiguration);
				} else {
					out.println("Command " + args[1] + " not found");
					printHelp(out);
				}
			} else {
				printHelp(out);
			}
		} else {
			final Command command = commandsByName.get(args[0].toUpperCase());

			if (command != null) {
				final String[] noFirst = new String[args.length - 1];
				arraycopy(args, 1, noFirst, 0, noFirst.length);

				try {
					final Map<Option<?>, ParameterValue<?>> values = 
						parseCommand(command, noFirst);

					final Parameters parameters = new DefaultParameters(values);
					command.execute(parameters);
				} catch (Exception e) {
					handleCommandException(new CLIApplicationCommandException(command, e), out);
				}
			} else {
				handleException(new CLIApplicationException(new IllegalArgumentException("Command " + args[0] + " not found")), out);
			}

		}
	}

	protected void handleCommandException(CLIApplicationCommandException exception, PrintStream out) {
		if (exception.getCause() instanceof ParsingException) {
			out.println("Error parsing command: " + exception.getCause().getMessage());
			printCommandHelp(exception.getCommand(), out, commandPrinterConfiguration);
			throw exception;
		}
		out.println("Error during execution: " + exception.getMessage());					
		throw exception;
	}

	protected void handleException(CLIApplicationException exception, PrintStream out) {
		out.println(exception.getMessage());
		printHelp(out);
		throw exception;
	}
	
	protected static boolean isVersionArgument(String arg) {
		return arg.equalsIgnoreCase("-v") || arg.equalsIgnoreCase("--version");
	}

	protected static boolean isHelpArgument(String arg) {
		return arg.equalsIgnoreCase("help") || arg.equalsIgnoreCase("-h")
			|| arg.equalsIgnoreCase("--help");
	}

	@SuppressWarnings("unchecked")
	private Map<Option<?>, ParameterValue<?>> parseCommand(Command command,
		String[] arguments
	) throws ParsingException {
		final Map<Option<?>, Object> values = new HashMap<Option<?>, Object>();

		command.getOptions();
		Option<?> currentOption = null;
		
		final Option<?> IGNORE_OPTION = new Option<String>(
			"--ignore--", "--ignore--", "--ignore--", false, false, null);
		
		for (String token : arguments) {
			if (token.startsWith("-")) {
				if (currentOption != null && !values.containsKey(currentOption) && currentOption.requiresValue()) {
					throw new IllegalArgumentException(
							String.format("option %s requires a value", currentOption.getParamName()));
				} else if (currentOption != null && !values.containsKey(currentOption)
						&& !currentOption.requiresValue()) {
					values.put(currentOption, null);
				}

				String optionName = null;
				if (token.charAt(1) == '-') {
					// starts with --
					optionName = token.substring(2);
				} else {
					// starts with -
					optionName = token.substring(1);
				}

				final Option<?> option = command.getOption(optionName);
				if (option == null) {
					if (ignoreUnrecognizedOptions) {
						currentOption = IGNORE_OPTION;
					} else {
						throw new ParsingException(String.format("option %s not found", optionName));
					}
				} else {
					currentOption = option;
				}
			} else {
				if (currentOption == null) {
					throw new ParsingException("unable to parse. You should specify an option before a value");
				} else {
					if (ignoreUnrecognizedOptions && currentOption == IGNORE_OPTION) {
						continue;
					}
					if (values.containsKey(currentOption)) {
						if (currentOption.isMultiple()) {
							List<String> valuesList = (List<String>) values.get(currentOption);
							valuesList.add(token);
						} else {
							throw new ParsingException(
									"option " + currentOption.getParamName() + " was already specified");
						}
					} else {
						if (currentOption.isMultiple()) {
							List<String> toAddValues = new ArrayList<>();
							toAddValues.add(token);
							values.put(currentOption, toAddValues);
						} else {
							values.put(currentOption, token);
						}
					}
				}
			}
		}

		// if there is the last option with no required value
		if (currentOption != null && !values.containsKey(currentOption) && currentOption.requiresValue()) {
			throw new ParsingException("option " + currentOption.getParamName() + " requires a value");
		} else if (currentOption != null && !values.containsKey(currentOption) && !currentOption.requiresValue()) {
			values.put(currentOption, null);
		}

		// validate mandatory arguments and put defaults
		for (Option<?> option : command.getOptions()) {
			if (!option.isOptional() && !values.containsKey(option) && !(option instanceof DefaultValuedOption)) {
				throw new ParsingException("option " + option.getParamName() + " is mandatory");
			}

			// put default-values if not specified before
			if (!values.containsKey(option) && ((option instanceof DefaultValuedOption))) {
				values.put(option, ((DefaultValuedOption<?>) option).getDefaultValue());
			}
		}

		final Map<Option<?>, ParameterValue<?>> paramValues = new HashMap<Option<?>, ParameterValue<?>>();
		for (Map.Entry<Option<?>, Object> parameterValue : values.entrySet()) {
			final Object value = parameterValue.getValue();
			final Option<?> key = parameterValue.getKey();
			paramValues.put(key, key.isMultiple() ? new MultipleParameterValue((List<String>) value)
					: new SingleParameterValue((String) value));
		}

		return paramValues;
	}

	protected void printCommandHelp(Command command, PrintStream out, CommandPrinterConfiguration configuration) {
		out.println("Command " + command.getName());
		printCommandUsage(command, out);
		printCommandOptionsExtended(command, out, configuration);
	}

	protected void printCommandUsage(Command command, PrintStream out) {
		printCommandOptions(command, out);
		out.println();
	}

  protected void printCommandOptions(Command command, PrintStream out) {
    StringBuilder sb = new StringBuilder("usage: ");

    sb.append(this.getApplicationCommand());
    sb.append(" ");
    sb.append(command.getName());

    for (Option<?> option : command.getOptions()) {
      if (option.isOptional()) {
        sb.append(" [");
      } else {
        sb.append(" ");
      }

      sb.append("-" + option.getShortName());
      if (option.requiresValue()) {
        sb.append(" <" + option.getParamName() + ">");
      }

      if (option.isOptional()) {
        sb.append("]");
      }
    }

    if (this.commandPrinterConfiguration.getBreakLines() == 0) {
      out.print(sb.toString());
    } else {
      out.print(breakString(sb.toString(), this.commandPrinterConfiguration.getBreakLines(), "\t"));
    }
  }

  private static String breakString(String text, int breakLines, String indent) {
    List<String> toret = new ArrayList<String>();

    StringBuilder currentString = new StringBuilder();
    StringBuilder currentOption = new StringBuilder();

    for (int i = 0; i < text.length(); i++) {
      char current = text.charAt(i);
      if (current != ' ') {
        currentOption.append(current);
      } else {
        if (i + 1 < text.length() && text.charAt(i + 1) == '<') {
          currentOption.append(current);
        } else {
          if (currentString.length() + currentOption.length() + 1 < breakLines) {
            currentString.append(currentOption).append(" ");
            currentOption = new StringBuilder();
          } else {
            toret.add(currentString.toString());
            currentString = new StringBuilder(indent);
            currentString.append(currentOption).append(" ");
            currentOption = new StringBuilder();
          }
        }
      }
    }

    if (currentString.length() + currentOption.length() + 1 < breakLines) {
      currentString.append(currentOption).append(" ");
    } else {
      toret.add(currentString.toString());
      currentString = new StringBuilder(indent);
      currentString.append(currentOption).append(" ");
    }
    if (currentString.length() > 0) {
      toret.add(currentString.toString());
    }

    return toret.stream().collect(Collectors.joining("\n"));
  }

  protected void printHelp(PrintStream out) {
    printWelcome(out);
    out.println("usage: " + this.getApplicationCommand() + " <command> [options]");

    printCommandsInHelp(out, listCommands());

    if (this.isShowApplicationCommandInHelp()) {
      out.println("Write '" + this.getApplicationCommand() + " help <command>' to see command-specific help");
    } else {
      out.println("Write 'help <command>' to see command-specific help");
    }
  }

  protected void printCommandsInHelp(PrintStream out, List<Command> commands) {
    out.println("where <command> is one of:");
    for (Command command : listCommands()) {
      printCommandInHelp(out, command);
    }
  }

  protected void printCommandInHelp(PrintStream out, Command command) {
    out.println("\t" + command.getName() + "\n\t\t" + command.getDescription());
  }

	protected void printWelcome(PrintStream out) {
		out.println("Welcome to " + this.getApplicationName());
		out.println(this.getDescription());
	}
}
