import com.company.calculator.Calculator;
import com.company.converter.InfToPrefConverter;
import com.company.exception.CalculationException;
import com.company.exception.ConversionException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ConverterTest {
	private static final Logger LOGGER = LogManager.getLogger();

	@Test
	public void simpleFormulaTest() throws ConversionException {
		String formula = "2 + 2";
		Deque<String> converted = new ArrayDeque<>();
		converted.addFirst("2");
		converted.addFirst("2");
		converted.addFirst("+");
		LOGGER.log(Level.INFO, "Testing if \"{}\" converts to {}", formula, converted);
		assertEquals(converted.toString(), InfToPrefConverter.convert(formula).toString());
	}

	@Test
	public void complicatedFormulaTest() throws ConversionException {
		String formula = "( 2 + 3 ) * 6 / ( 8 - 6 )";
		Deque<String> converted = new ArrayDeque<>();
		converted.addFirst("6");
		converted.addFirst("8");
		converted.addFirst("-");
		converted.addFirst("6");
		converted.addFirst("/");
		converted.addFirst("3");
		converted.addFirst("2");
		converted.addFirst("+");
		converted.addFirst("*");
		LOGGER.log(Level.INFO, "Testing if \"{}\" converts to {}", formula, converted);
		assertEquals(converted.toString(), InfToPrefConverter.convert(formula).toString());
	}

	@Test
	public void formulaWithNegetivesAndFractionsTest() throws ConversionException {
		String formula = "-2 + 2.5";
		Deque<String> converted = new ArrayDeque<>();
		converted.addFirst("2.5");
		converted.addFirst("-2");
		converted.addFirst("+");
		LOGGER.log(Level.INFO, "Testing if \"{}\" converts to {}", formula, converted);
		assertEquals(converted.toString(), InfToPrefConverter.convert(formula).toString());
	}

	@Test
	public void invalidInputTest() throws ConversionException {
		String invalidFormula = "( 2 2 + 2";
		LOGGER.log(Level.INFO, "Testing if {} throws an exception", invalidFormula);
		Exception e = assertThrows(ConversionException.class, () -> InfToPrefConverter.convert(invalidFormula));
		assertEquals("Invalid formula!", e.getMessage());
	}

	@Test
	public void testFromFile() throws ConversionException {
		final String DIVIDER_REGEX = "\\:";
		final String SPACE_REGEX = "\\s";
		try {
			FileReader input = new FileReader("src/test/resources/converterTestInput.txt");
			BufferedReader reader = new BufferedReader(input);
			String line = reader.readLine();
			String formula;
			String resultString;
			Deque<String> dequeFormula = new ArrayDeque<>();
			while (line != null) {
				formula = line.split(DIVIDER_REGEX)[0];
				resultString = line.split(DIVIDER_REGEX)[1];
				dequeFormula.addAll(Arrays.asList(resultString.split(SPACE_REGEX)));
				LOGGER.log(Level.INFO, "Testing if \"{}\" converts to {}", formula, dequeFormula);
				try {
					assertEquals("Error occurred converting ", dequeFormula.toString(),
							  InfToPrefConverter.convert(formula).toString());
				} catch (ConversionException e) {
					LOGGER.log(Level.WARN, "Wrong input: {}, message: {}",dequeFormula, e.getMessage());
				}
				dequeFormula.clear();
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.ERROR, e.getMessage());
		} catch (IOException e) {
			LOGGER.log(Level.ERROR, e.getMessage());
		}
	}
}
