package es.uvigo.ei.sing.yacli.command.option;

import java.util.ArrayList;
import java.util.List;

import es.uvigo.ei.sing.yacli.command.parameter.MultipleParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.SingleParameterValue;

public abstract class AbstractOptionConverter<T> implements OptionConverter<T> {
	@Override
	public List<T> convert(MultipleParameterValue mpv) {
		if (mpv == null) {
			return null;
		} else {
			final List<String> values = mpv.getValue();
			final List<T> typedValues = new ArrayList<>(values.size());

			for (String value : values) {
				typedValues.add(this.convert(new SingleParameterValue(value)));
			}

			return typedValues;
		}
	}

	@Override
	public boolean canConvert(MultipleParameterValue mpv) {
		if (mpv == null) {
			return true;
		} else {
			final List<String> values = mpv.getValue();

			for (String value : values) {
				if (value == null || !this.canConvert(new SingleParameterValue(value)))
					return false;
			}

			return true;
		}
	}
}
