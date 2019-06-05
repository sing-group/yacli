package es.uvigo.ei.sing.yacli.command;

public class CommandPrinterConfiguration {

  public static boolean DEFAULT_PRINT_CATEGORY_NAME = true;

  private boolean printCategoryName;

  public CommandPrinterConfiguration() {
    this(DEFAULT_PRINT_CATEGORY_NAME);
  }

  public CommandPrinterConfiguration(boolean printCategoryName) {
    this.printCategoryName = printCategoryName;
  }

  public boolean isPrintCategoryName() {
    return printCategoryName;
  }
}
