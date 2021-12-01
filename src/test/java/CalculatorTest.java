import com.company.calculator.Calculator;
import com.company.exception.CalculationException;
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

public class CalculatorTest {
	private static final Logger LOGGER = LogManager.getLogger();

	@Test
	public void simpleCalculationTest() throws CalculationException {
		// +22 ( 2+2 )
		Deque<String> supposedlyFour = new ArrayDeque<>();
		supposedlyFour.addFirst("2");
		supposedlyFour.addFirst("2");
		supposedlyFour.addFirst("+");
		int four = 4;
		LOGGER.log(Level.INFO, "Testing if {} = {}", supposedlyFour, four);
		assertEquals(four, (int) Calculator.calculate(supposedlyFour));
	}

	@Test
	public void divisionTest() throws CalculationException {
		// /+482 ( (4+8)/2 )
		Deque<String> supposedlySix = new ArrayDeque<>();
		supposedlySix.addFirst("2");
		supposedlySix.addFirst("8");
		supposedlySix.addFirst("4");
		supposedlySix.addFirst("+");
		supposedlySix.addFirst("/");
		int six = 6;
		LOGGER.log(Level.INFO, "Testing if {} = {}", supposedlySix, six);
		assertEquals(six, (int) Calculator.calculate(supposedlySix));
	}

	@Test
	public void invalidInputTest() {
		Deque<String> invalidInput = new ArrayDeque<>();
		invalidInput.addFirst("2");
		invalidInput.addFirst("+");
		invalidInput.addFirst("+");
		LOGGER.log(Level.INFO, "Testing if {} throws an exception", invalidInput);
		Exception e = assertThrows(CalculationException.class, () -> Calculator.calculate(invalidInput));
		assertEquals("Invalid formula!", e.getMessage());
	}

	@Test
	public void wrongOrderInputTest() {
		Deque<String> invalidInput = new ArrayDeque<>();
		invalidInput.addFirst("2");
		invalidInput.addFirst("+");
		invalidInput.addFirst("2");
		LOGGER.log(Level.INFO, "Testing if {} throws an exception", invalidInput);
		Exception e = assertThrows(CalculationException.class, () -> Calculator.calculate(invalidInput));
		assertEquals("Invalid formula!", e.getMessage());
	}

	@Test
	public void testFromFile() {
		final String DIVIDER_REGEX = "\\:";
		final String SPACE_REGEX = "\\s";
		try {
			FileReader input = new FileReader("src/test/resources/calculatorTestInput.txt");
			BufferedReader reader = new BufferedReader(input);
			String line = reader.readLine();
			String formula;
			int result;
			Deque<String> dequeFormula = new ArrayDeque<>();
			while (line != null) {
				formula = line.split(DIVIDER_REGEX)[0];
				result = Integer.parseInt(line.split(DIVIDER_REGEX)[1]);
				dequeFormula.addAll(Arrays.asList(formula.split(SPACE_REGEX)));
				LOGGER.log(Level.INFO, "Testing if {} = {}", dequeFormula, result);
				try {
					assertEquals("Error occurred calculating ", result, (int) Calculator.calculate(dequeFormula));
				} catch (CalculationException e) {
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
