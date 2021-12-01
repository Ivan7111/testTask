package com.company.converter;

import com.company.exception.ConversionException;
import com.company.util.FormulaValidator;

import java.util.ArrayDeque;
import java.util.Deque;

/*
 * This class converts mathematical formula in infix notation to prefix notation
 * Using these steps:
 * 1. Rewriting formula right to left
 * 2. Converting to postfix notation
 * 3. Rewriting resulting postfix notation formula right to left
 *
 * Input formula must be in String and may contain positive and negative numbers, divider (.) and operation signs (+, -, *, /)
 * Input formula must NOT contain "$" sign
 * Operands, operators and brackets must be divided by whitespace, like this: "( 2 + 3 ) * 3"
 * For negative numbers: - sign and number itself must be written together, like this: "( 2 + 3 ) * -3"
 * For
 * Output formula is in prefix notation, represented as Deque with leftmost symbol on top
 */

public class InfToPrefConverter {

	private static final String SPACE_REGEX = "\\s";

	/*
	* This method receives mathematical formula in infix notation and returns the same formula in prefix notation
	*/

	public static Deque<String> convert(String formula) throws ConversionException {
		if (!FormulaValidator.checkFormula(formula)) {
			throw new ConversionException("Invalid formula!");
		}
		String result;
		result = mirror(formula);
		result = convertToPost(result);
		result = mirror(result);
		return stringToDeque(result);
	}

	/*
	* This method receives string and returns this string rewritten right to left
	*/

	private static String mirror(String formula) {
		StringBuilder result = new StringBuilder();
		String[] splitted = formula.split(SPACE_REGEX);
		result.append(" ");
		for (int i = splitted.length - 1; i >= 0; i--) {
			if (splitted[i].equals(")")) {
				result.append("(").append(" ");
			} else if (splitted[i].equals("(")) {
				result.append(")").append(" ");
			} else {
				result.append(splitted[i]).append(" ");
			}
		}
		return result.toString();
	}

	/*
	* This method receives mathematical formula in infix notation and returns the same formula in postfix notation
	*/

	private static String convertToPost(String formula) throws ConversionException {
		StringBuilder result = new StringBuilder();
		Deque<String> in = new ArrayDeque<>();
		Deque<String> out = new ArrayDeque<>();
		Deque<String> buff = new ArrayDeque<>();
		String[] splitted = formula.split(SPACE_REGEX);
		for (String s: splitted){
			in.addLast(s);
		}
		in.addLast("$");
		buff.addFirst("$");
		while (!in.isEmpty()){
			String temp = in.removeFirst();
			String firstBuff = buff.peekFirst();
			if (temp.equals("$")) {
				// End of formula sign - unloading buffer stack contents into the out stack
				if (firstBuff.equals("$")) {
					// Bottom of the buffer stack sign - conversion complete
					break;
				} else if (firstBuff.equals("(")) {
					// Open bracket in buff stack at this point means that input formula was invalid
					throw new ConversionException("Invalid formula!");
				} else if (firstBuff.equals("+") || firstBuff.equals("-") || firstBuff.equals("*") || firstBuff.equals("/")) {
					// At this point firsBuff may only be an operator sign, if statement remains for clarity
					out.addFirst(buff.removeFirst()); // Unloading first element from buffer stack to out stack
					in.addFirst(temp); // Putting end sign back in case buffer stack contains something else
				}
			} else if (temp.equals("(")){
				// Open bracket always goes straight to the buffer stack
				buff.addFirst(temp);
			} else if (temp.equals("+") || temp.equals("-")) {
				// Same priority operators, same actions
				if (firstBuff.equals("$") || firstBuff.equals("(")) {
					// In case of open bracket or end sign on top of buffer stack, temp goes straight to buffer
					buff.addFirst(temp);
				} else if (firstBuff.equals("+") || firstBuff.equals("-") || firstBuff.equals("*") || firstBuff.equals("/")) {
					// In case of same priority operations in input and in buffer, operator from buffer is unloaded to output
					// Same thing applies in case of + or - in input and * or / in buffer
					out.addFirst(buff.removeFirst()); // Unloading first element from buffer stack to out stack
					in.addFirst(temp); // Putting end sign back in case buffer stack contains more same priority operations
				}
			} else if (temp.equals("*") || temp.equals("/")) {
				// Same priority operators, same actions
				if (firstBuff.equals("$") || firstBuff.equals("(")) {
					// In case of open bracket or end sign on top of buffer stack, temp goes straight to buffer
					buff.addFirst(temp);
				} else if (firstBuff.equals("+") || firstBuff.equals("-")) {
					// In case of + or - on top of buffer stack temp goes straight to buffer
					buff.addFirst(temp);
				} else if (firstBuff.equals("*") || firstBuff.equals("/")) {
					// In case of same priority operations in input and in buffer operator from buffer is unloaded to output
					out.addFirst(buff.removeFirst()); // Unloading first element from buffer stack to out stack
					in.addFirst(temp); // Putting end sign back in case buffer stack contains more same priority operations
				}
			} else if (temp.equals(")")) {
				if (firstBuff.equals("$")) {
					// Closed bracket in buff stack at this point means that input formula was invalid
					throw new ConversionException("Invalid Formula!");
				} else if (firstBuff.equals("(")) {
					// Brackets disappear
					buff.removeFirst();
				} else if (firstBuff.equals("+") || firstBuff.equals("-") || firstBuff.equals("*") || firstBuff.equals("/")) {
					// In case of operator sign on top of the buffer stack, operator from buffer is unloaded to output
					out.addFirst(buff.removeFirst()); // Unloading first element from buffer stack to out stack
					in.addFirst(temp); // Putting end sign back in case buffer stack contains more operators
				}
			} else {
				// Remaining cases are either numbers or variables, in this case temp goes straight to out
				out.addFirst(temp);
			}
		}
		while (!out.isEmpty()) {
			result.append(out.removeLast()).append(" ");
		}
		return result.toString();
	}

	/*
	* This method converts formula in string to deque
	*/

	private static Deque<String> stringToDeque(String string) {
		Deque<String> result = new ArrayDeque<>();
		String[] splitted = string.split(SPACE_REGEX);
		for (String s: splitted) {
			if (!s.equals("")) {
				result.addLast(s);
			}
		}
		return result;
	}
}
