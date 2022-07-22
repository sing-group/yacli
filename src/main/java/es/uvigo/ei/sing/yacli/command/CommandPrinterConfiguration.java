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

public class CommandPrinterConfiguration {

  public static final boolean DEFAULT_PRINT_CATEGORY_NAME = true;
  public static final String DEFAULT_MULTIPLE_PARAMETER_STRING = ". This option can be specified multiple times";
  public static final String DEFAULT_UNGROUPED_OPTIONS_STRING = "General options:";
  public static final int DEFAULT_BREAK_LINES = 0;

  private boolean printCategoryName;
  private String multipleParameterString;
  private String ungroupedOptionsMessage;
  private int breakLines;

  public CommandPrinterConfiguration() {
    this(
      DEFAULT_PRINT_CATEGORY_NAME, DEFAULT_MULTIPLE_PARAMETER_STRING, DEFAULT_UNGROUPED_OPTIONS_STRING,
      DEFAULT_BREAK_LINES
    );
  }

  public CommandPrinterConfiguration(
    boolean printCategoryName, String multipleParameterString, String ungroupedOptionsMessage, int breakLines
  ) {
    this.printCategoryName = printCategoryName;
    this.multipleParameterString = multipleParameterString;
    this.ungroupedOptionsMessage = ungroupedOptionsMessage;
    this.breakLines = breakLines;
  }

  public boolean isPrintCategoryName() {
    return printCategoryName;
  }

  public String getMultipleParameterString() {
    return multipleParameterString;
  }

  public String getUngroupedOptionsMessage() {
    return ungroupedOptionsMessage;
  }

  public int getBreakLines() {
    return breakLines;
  }
}
