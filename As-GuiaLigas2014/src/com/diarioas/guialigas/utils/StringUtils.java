package com.diarioas.guialigas.utils;

import java.util.HashMap;

public class StringUtils {

	private static final HashMap<Character, Character> accents = new HashMap<Character, Character>() {
		{
			put('á', 'a');
			put('é', 'e');
			put('í', 'i');
			put('ó', 'o');
			put('ú', 'u');
			put('ñ', 'n');

			put('Á', 'A');
			put('É', 'E');
			put('Í', 'I');
			put('Ó', 'O');
			put('Ú', 'U');
			put('Ñ', 'N');

			// put('Ą', 'A');
			// put('Ę', 'E');
			// put('Ć', 'C');
			// put('Ł', 'L');
			// put('Ń', 'N');
			// put('Ó', 'O');
			// put('Ś', 'S');
			// put('Ż', 'Z');
			// put('Ź', 'Z');
			//
			// put('ą', 'a');
			// put('ę', 'e');
			// put('ć', 'c');
			// put('ł', 'l');
			// put('ń', 'n');
			// put('ó', 'o');
			// put('ś', 's');
			// put('ż', 'z');
			// put('ź', 'z');
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
