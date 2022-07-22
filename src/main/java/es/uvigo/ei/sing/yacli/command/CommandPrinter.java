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

import static es.uvigo.ei.sing.yacli.command.option.OptionCategory.DEFAULT_CATEGORY;
import static java.util.stream.Collectors.joining;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import es.uvigo.ei.sing.yacli.command.option.DefaultValuedOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.option.OptionCategory;

public class CommandPrinter {
  public static void printCommandOptionsExtended(
    Command command, PrintStream out, CommandPrinterConfiguration configuration
  ) {
    Map<OptionCategory, List<Option<?>>> optionsByCategory = new HashMap<>();
    command.getOptions().forEach((option) -> {
      option.getCategories().forEach((category) -> {
        if (!optionsByCategory.containsKey(category)) {
          optionsByCategory.put(category, new LinkedList<>());
        }
        optionsByCategory.get(category).add(option);
      });
    });

    if (optionsByCategory.keySet().size() == 1 && optionsByCategory.keySet().contains(DEFAULT_CATEGORY)) {
      // do not categorize options
      for (Option<?> option : command.getOptions()) {
        printOption(option, out, configuration);
      }
    } else {
      // print options by category, first uncategorized ones (if any)
      if (optionsByCategory.containsKey(DEFAULT_CATEGORY)) {
        out.print("\t");
        out.println(configuration.getUngroupedOptionsMessage());
        for (Option<?> option : optionsByCategory.get(DEFAULT_CATEGORY)) {
          printOption(option, out, configuration);
        }
        out.println();
      }
      optionsByCategory.remove(DEFAULT_CATEGORY);

      optionsByCategory.forEach((category, options) -> {
        if (configuration.isPrintCategoryName()) {
          out.println("\t" + category.getName() + ":");
        }
        for (Option<?> option : options) {
          printOption(option, out, configuration);
        }
        out.println();
      });
    }
  }

  protected static void printOption(Option<?> option, PrintStream out, CommandPrinterConfiguration configuration) {
    StringBuilder sb = new StringBuilder();

    sb.append("\t--").append(option.getParamName()).append("/-").append(option.getShortName());

    sb.append("\n");

    StringBuilder optionDescription = new StringBuilder(option.getDescription());

    if (option instanceof DefaultValuedOption) {
      optionDescription.append(" (default: ").append(((DefaultValuedOption<?>) option).getDefaultValue()).append(")");
    }

    if (option.isMultiple()) {
      optionDescription.append(configuration.getMultipleParameterString());
    }

    if (configuration.getBreakLines() == 0) {
      sb.append("\t\t").append(optionDescription.toString());
    } else {
      sb.append(breakDescription(optionDescription.toString(), configuration.getBreakLines(), "\t\t"));
    }

    out.println(sb.toString());
  }

  protected static String breakDescription(String description, int breakLines, String indent) {
    if (description.contains("\n")) {
      String[] descriptionSplit = description.split("\n");
      StringBuilder toret = new StringBuilder();
      for (int i = 0; i < descriptionSplit.length; i++) {
        String line = descriptionSplit[i];
        int firstNonTabCharIndex = findFirstNonTabCharIndex(line);
        String newIndent = indent + IntStream.range(0, firstNonTabCharIndex).mapToObj(x -> "\t").collect(joining());
        toret.append(breakDescription(line.substring(firstNonTabCharIndex), breakLines, newIndent));
        if (i < descriptionSplit.length - 1) {
          toret.append("\n");
        }
      }
      return toret.toString();
    } else {
      String[] wordSplit = description.split(" ");
      StringBuilder currentLine = new StringBuilder(indent);
      List<String> toret = new ArrayList<String>();
      for (String word : wordSplit) {
        if (currentLine.length() + word.length() + 1 <= breakLines) {
          if (currentLine.length() > indent.length()) {
            currentLine.append(" ");
          }
          currentLine.append(word);
        } else {
          toret.add(currentLine.toString());
          currentLine = new StringBuilder(indent);
          currentLine.append(word);
        }
      }
      if (currentLine.length() > 0) {
        toret.add(currentLine.toString());
      }
      return toret.stream().collect(joining("\n"));
    }
  }

  protected static int findFirstNonTabCharIndex(String string) {
    for (int i = 0; i < string.length(); i++) {
      if (string.charAt(i) != '\t') {
        return i;
      }
    }
    return 0;
  }
}
