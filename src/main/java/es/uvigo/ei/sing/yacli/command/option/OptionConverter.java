package es.uvigo.ei.sing.yacli.command.option;

import java.util.List;

import es.uvigo.ei.sing.yacli.command.parameter.MultipleParameterValue;
import es.uvigo.ei.sing.yacli.command.parameter.SingleParameterValue;

public interface OptionConverter<T> {
	public Class<T> getTargetClass();
	public boolean canConvert(SingleParameterValue value);
	public boolean canConvert(MultipleParameterValue value);
	
	public T convert(SingleParameterValue value);
	public List<T> convert(MultipleParameterValue value);
}
