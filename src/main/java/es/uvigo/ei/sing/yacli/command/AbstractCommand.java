package es.uvigo.ei.sing.yacli.command;

import static java.util.Collections.unmodifiableList;

import java.util.List;

import es.uvigo.ei.sing.yacli.command.option.Option;

public abstract class AbstractCommand implements Command {
  protected final List<Option<?>> options;

  public AbstractCommand() {
    this.options = createOptions();
  }

  @Override
  public List<Option<?>> getOptions() {
    return unmodifiableList(options);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Option<?> getOption(String name) {
    for (Option<?> option : this.getOptions()) {
      if (option.hasName(name))
        return option;
    }

    return null;
  }

  protected abstract List<Option<?>> createOptions();
}
