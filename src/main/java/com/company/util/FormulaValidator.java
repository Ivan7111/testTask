package com.company.util;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormulaValidator {
	private static final String LITERAL_REGEX = "[A-Za-zА-Яа-я]";

	public static boolean checkFormula(String formula) {
		Pattern pattern = Pattern.compile(LITERAL_REGEX);
		Matcher matcher = pattern.matcher(formula);
		if (matcher.find()){
			return false;
		}
		try {
			Expression exp = new ExpressionBuilder(formula).build();
			return exp.validate().isValid();
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
