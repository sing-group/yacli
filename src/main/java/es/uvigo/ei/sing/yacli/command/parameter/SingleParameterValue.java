package es.uvigo.ei.sing.yacli.command.parameter;

public class SingleParameterValue implements ParameterValue<String> {
	private final String value;

	public SingleParameterValue(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.getValue();
	}
}
