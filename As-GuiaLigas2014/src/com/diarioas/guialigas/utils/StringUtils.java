package com.diarioas.guialigas.utils;

import java.util.HashMap;

public class StringUtils {

	private static final HashMap<Character, Character> accents = new HashMap<Character, Character>() {
		{
			put('‡', 'a');
			put('', 'e');
			put('’', 'i');
			put('—', 'o');
			put('œ', 'u');
			put('–', 'n');

			put('ç', 'A');
			put('ƒ', 'E');
			put('ê', 'I');
			put('î', 'O');
			put('ò', 'U');
			put('„', 'N');

			// put('Ä„', 'A');
			// put('Ä˜', 'E');
			// put('Ä†', 'C');
			// put('Å', 'L');
			// put('Åƒ', 'N');
			// put('Ã“', 'O');
			// put('Åš', 'S');
			// put('Å»', 'Z');
			// put('Å¹', 'Z');
			//
			// put('Ä…', 'a');
			// put('Ä™', 'e');
			// put('Ä‡', 'c');
			// put('Å‚', 'l');
			// put('Å„', 'n');
			// put('Ã³', 'o');
			// put('Å›', 's');
			// put('Å¼', 'z');
			// put('Åº', 'z');
		}
	};

	public static String removeAccents(String string) {

		char[] result = string.toCharArray();
		for (int i = 0; i < result.length; i++) {
			Character replacement = accents.get(result[i]);
			if (replacement != null)
				result[i] = replacement;
		}
		return new String(result);
	}

	public static String getNormalizeText(String string) {
		return getNormalizeText(string, true, true, true);
	}

	public static String getNormalizeText(String string, boolean toLower,
			boolean withOutBlankets, boolean withOutAccents) {
		// To lowerCase
		if (toLower)
			string = string.toLowerCase();
		// Replace blanckets for _
		if (withOutBlankets)
			string = string.replaceAll(" ", "_");
		if (withOutAccents)
			// Delete accents
			string = removeAccents(string);

		return string;
	}

}
