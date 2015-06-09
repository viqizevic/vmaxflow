package util;


/**
 * The Class Util.
 */
public class Util {
	
	/**
	 * Gets a text in singular or plural version dependent on the count.
	 * The plural version is in this case only added with "s" at the end.
	 *
	 * @param text the text
	 * @param n the n
	 * @return the string
	 */
	public static String nText(String text, int n) {
		return nText(text, text + "s", n);
	}
	
	/**
	 * Gets a text in singular or plural version dependent on the count.
	 *
	 * @param text the text in singular form
	 * @param pluralText the text in plural form
	 * @param n the count number
	 * @return the result of String.format() with text and count as variables if count is 1, otherwise with the plural text
	 */
	public static String nText(String text, String pluralText, int n) {
		return String.format((1==n ? text : pluralText), n);
	}
	
}
