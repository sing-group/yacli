package es.uvigo.ei.sing.yacli.command;

import java.util.Collections;
import java.util.List;

import es.uvigo.ei.sing.yacli.command.option.Option;

public abstract class AbstractCommand implements Command {
	protected final List<Option<?>> options;

	public AbstractCommand() {
		this.options = createOptions();
	}

	@Override
	public List<Option<?>> getOptions() {
		return Collections.unmodifiableList(options);
	}
	
	@Override
	public Option<?> getOption(String name) {
		for (Option<?> option : this.getOptions()) {
			if (option.hasName(name))
				return option;
		}
		
		return null;
	}

	protected abstract List<Option<?>> createOptions(); // factory method
}
