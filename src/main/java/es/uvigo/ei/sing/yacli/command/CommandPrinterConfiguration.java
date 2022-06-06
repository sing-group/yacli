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
