package com.announcify.text;

import java.util.Map;

public class Formatter {
	public static final String EVENT = "$0";
	public static final String ADDRESS = "$1";
	public static final String NAME = "$2";
	public static final String ADDRESS_TYPE = "$3";
	public static final String MESSAGE = "$4";

	public static String replaceExpressions(final Map<String, String> expressions, String text) {
		for (final String s : expressions.keySet()) {
			text = text.replace(s, expressions.get(s));
		}

		return text;
	}
}