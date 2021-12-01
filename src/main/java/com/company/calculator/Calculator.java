package com.company.calculator;

import com.company.exception.CalculationException;

import java.util.ArrayDeque;
import java.util.Deque;

/*
* This class calculates prefix notation mathematical formula in form of Deque with leftmost symbol on top
* Formula may contain negative and positive numbers, divider (.) and operation signs (+, -, *, /)
* Formula must not contain variables
* Result is represented as double
*/

public class Calculator {

	private static final String OPERATOR_REGEX = "[\\+\\-\\*\\/]";
	private static final String OPERAND_REGEX = "\\-?\\d+(\\.\\d+)?";

	public static double calculate(Deque<String> formula) throws CalculationException {
		double result;
		Deque<String> in = new ArrayDeque<>(formula);
		Deque<String> buff = new ArrayDeque<>();
		// Unloading first three elements from input stack
		String supposedlyOperator = in.pollFirst();
		String supposedlyOperand1 = in.pollFirst();
		String supposedlyOperand2 = in.pollFirst();
		while (supposedlyOperand1 != null) {
			if (in.isEmpty() && supposedlyOperand2 == null) {
				throw new CalculationException("Invalid formula!");
			}
			// Checking if unloaded elements form mathematical expression in prefix form
			if (supposedlyOperator.matches(OPERATOR_REGEX)
					  && supposedlyOperand1.matches(OPERAND_REGEX) && supposedlyOperand2.matches(OPERAND_REGEX)) {
				double res;
				switch (supposedlyOperator) {
					case "+" -> {
						// Calculating
						res = Double.parseDouble(supposedlyOperand1) + Double.parseDouble(supposedlyOperand2);
						// Returning result to the input stack
						in.addFirst(Double.toString(res));
					}
					case "-" -> {
						// Calculating
						res = Double.parseDouble(supposedlyOperand1) - Double.parseDouble(supposedlyOperand2);
						// Returning result to the input stack
						in.addFirst(Double.toString(res));
					}
					case "*" -> {
						// Calculating
						res = Double.parseDouble(supposedlyOperand1) * Double.parseDouble(supposedlyOperand2);
						// Returning result to the input stack
						in.addFirst(Double.toString(res));
					}
					case "/" -> {
						// Calculating
						res = Double.parseDouble(supposedlyOperand1) / Double.parseDouble(supposedlyOperand2);
						// Returning result to the input stack
						in.addFirst(Double.toString(res));
					}
				}
				// Unloading buffer stack contents back into input stack
				while (!buff.isEmpty()) {
					in.addFirst(buff.removeFirst());
				}
				// Starting again
				supposedlyOperator = in.pollFirst();
				supposedlyOperand1 = in.pollFirst();
				supposedlyOperand2 = in.pollFirst();

			} else {
				// If unloaded elements don't form expression, putting first element into buff stack, moving other elements,
				// and getting new element from input stack
				buff.addFirst(supposedlyOperator);
				supposedlyOperator = supposedlyOperand1;
				supposedlyOperand1 = supposedlyOperand2;
				supposedlyOperand2 = in.pollFirst();
			}

		}
		result = Double.parseDouble(supposedlyOperator);
		return result;
	}
}
