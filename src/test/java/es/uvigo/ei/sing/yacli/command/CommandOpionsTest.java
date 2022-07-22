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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import es.uvigo.ei.sing.yacli.command.option.BigDecimalDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.BigDecimalOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerDefaultValuedStringConstructedOption;
import es.uvigo.ei.sing.yacli.command.option.IntegerOption;
import es.uvigo.ei.sing.yacli.command.option.Option;
import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class CommandOpionsTest {

	@Test
	public void testIntegerOptions() throws UnsupportedEncodingException {
		Command c = new TestOptionCommand() {
			@Override
			protected List<Option<?>> createOptions() {
				return asList(
					new IntegerDefaultValuedStringConstructedOption("int1", "i1", "test integer option 1", 1),
					new IntegerOption("int2", "i2", "test integer option 2", false),
					new IntegerOption("int3", "i3", "test integer option 3", false, false),
					new IntegerOption("int4", "i4", "test integer option 4", false, true)
				);
			}
		};

		String expectedPrintCommandOptionsString = toString(asList(
			optionLine("--int1/-i1", 1),
			optionLine("test integer option 1 (default: 1)", 2),
			optionLine("--int2/-i2", 1),
			optionLine("test integer option 2", 2),
			optionLine("--int3/-i3", 1),
			optionLine("test integer option 3", 2),
			optionLine("--int4/-i4", 1),
			lastOptionLine("test integer option 4. This option can be specified multiple times", 2)
		));
		
		String actual = printCommandOptionsExtended(c);

		assertEquals(expectedPrintCommandOptionsString, actual);
	}
	
	@Test
	public void testBigDecimalOptions() throws UnsupportedEncodingException {
		Command c = new TestOptionCommand() {
			@Override
			protected List<Option<?>> createOptions() {
				return asList(
					new BigDecimalDefaultValuedStringConstructedOption("big-decimal-1", "bd1", "test big decimal option 1", new BigDecimal(1)),
					new BigDecimalOption("big-decimal-2", "bd2", "test big decimal option 2", false),
					new BigDecimalOption("big-decimal-3", "bd3", "test big decimal option 3", false, false),
					new BigDecimalOption("big-decimal-4", "bd4", "test big decimal option 4", false, true)
				);
			}
		};
		
		String expectedPrintCommandOptionsString = toString(asList(
			optionLine("--big-decimal-1/-bd1", 1),
			optionLine("test big decimal option 1 (default: 1)", 2),
			optionLine("--big-decimal-2/-bd2", 1),
			optionLine("test big decimal option 2", 2),
			optionLine("--big-decimal-3/-bd3", 1),
			optionLine("test big decimal option 3", 2),
			optionLine("--big-decimal-4/-bd4", 1),
			lastOptionLine("test big decimal option 4. This option can be specified multiple times", 2)
		));
		
		String actual = printCommandOptionsExtended(c);
		
		assertEquals(expectedPrintCommandOptionsString, actual);
	}
	
	private static abstract class TestOptionCommand extends AbstractCommand {
		@Override
		public String getName() {
			return "Test";
		}

		@Override
		public String getDescriptiveName() {
			return "Test command";
		}

		@Override
		public String getDescription() {
			return "Test command implementation";
		}

		@Override
		public void execute(Parameters parameters) throws Exception {
		}
	}

	private static final String lastOptionLine(String option, int indents) {
		return optionLine(option + "\n", indents);
	}

	private static final String optionLine(String option, int indents) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indents; i++) {
			sb.append("\t");
		}
		sb.append(option);

		return sb.toString();
	}

	private static final String toString(List<String> lines) {
		return lines.stream().collect(Collectors.joining("\n"));
	}
	
	private static final String printCommandOptionsExtended(Command c)
		throws UnsupportedEncodingException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
    CommandPrinter.printCommandOptionsExtended(c, ps, new CommandPrinterConfiguration());

		return os.toString("UTF8");
	}
}
