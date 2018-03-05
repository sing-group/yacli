package es.uvigo.ei.sing.yacli.command.parameter;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class MultipleParameterValue implements ParameterValue<List<String>> {
	private final List<String> values;

	public MultipleParameterValue(List<String> values) {
		this.values = unmodifiableList(new ArrayList<>(values));
	}
	
	@Override
	public List<String> getValue() {
		return values;
	}
	
	@Override
	public String toString() {
		return this.getValue().toString();
	}
}
