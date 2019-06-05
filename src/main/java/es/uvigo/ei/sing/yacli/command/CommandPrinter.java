package es.uvigo.ei.sing.yacli.command;

import static es.uvigo.ei.sing.yacli.command.option.OptionCategory.DEFAULT_CATEGORY;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.OptionCategory;

public class CommandPrinter {
  public static void printCommandOptionsExtended(Command command, PrintStream out) {
    printCommandOptionsExtended(command, out, new CommandPrinterConfiguration());
  }

  public static void printCommandOptionsExtended(
    Command command, PrintStream out, CommandPrinterConfiguration configuration
  ) {
		Map<OptionCategory, Set<Option<?>>> optionsByCategory = new HashMap<>();
		command.getOptions().forEach((option) -> {
			option.getCategories().forEach((category) -> {
				if (!optionsByCategory.containsKey(category)) {
					optionsByCategory.put(category, new HashSet<>());
				}
				optionsByCategory.get(category).add(option);
			});
		});

		if (optionsByCategory.keySet().size() == 1 && optionsByCategory.keySet()
			.contains(DEFAULT_CATEGORY)
		) {
			// do not categorize options
			for (Option<?> option : command.getOptions()) {
				printOption(option, out);
			}
		} else {
			// print options by category, first uncategorized ones (if any)
			if (optionsByCategory.containsKey(DEFAULT_CATEGORY)) {
				out.println("\tGeneral options:");
				for (Option<?> option : optionsByCategory
					.get(DEFAULT_CATEGORY)) {
					printOption(option, out);
				}
				out.println();
			}
			optionsByCategory.remove(DEFAULT_CATEGORY);

			optionsByCategory.forEach((category, options) -> {
        if (configuration.isPrintCategoryName()) {
          out.println("\t" + category.getName() + ":");
        }
				for (Option<?> option : options) {
					printOption(option, out);
				}
				out.println();
			});
		}
	}

	protected static void printOption(Option<?> option, PrintStream out) {
		out.println(
			"\t--" + option.getParamName() + "/-" + option.getShortName()
				+ "\n\t\t" + option.getDescription()
				+ ((option instanceof DefaultValuedOption) ? " (default: "
					+ ((DefaultValuedOption<?>) option).getDefaultValue() + ")"
					: "")
				+ ((option.isMultiple())
					? ". This option can be specified multiple times"
					: ""));
	}
}
