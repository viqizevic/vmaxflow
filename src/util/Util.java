package util;

import java.io.FileWriter;
import java.io.IOException;

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
	
	/**
	 * Write file.
	 *
	 * @param fileName the file name
	 * @param content the content
	 */
	public static void writeFile(String fileName, String content) {
		FileWriter fw = getNewFileWriter(fileName);
		appendToFile(fw, content);
		closeFileWriter(fw);
		Log.p("File is written: " + fileName);
	}
	
	/**
	 * Gets the new file writer.
	 *
	 * @param fileName the file name
	 * @return the new file writer
	 */
	public static FileWriter getNewFileWriter(String fileName) {
		if (Log.eIf(fileName == null, "Cannot write file. No file name.")) {
			return null;
		}
		try {
			FileWriter fw = new FileWriter(fileName);
			return fw;
		} catch (IOException e) {
			Log.e("Unable to write file: " + fileName, e);
		}
		return null;
	}
	
	/**
	 * Append to file.
	 *
	 * @param fileWriter the file writer
	 * @param text the text
	 */
	public static void appendToFile(FileWriter fileWriter, String text) {
		if (Log.eIf(null == fileWriter, "Cannot append to file. Empty file writer given.")) {
			return;
		}
		if (Log.eIf(null == text, "Cannot append text. Empty text given.")) {
			return;
		}
		try {
			fileWriter.append(text);
			fileWriter.flush();
		} catch (IOException e) {
			Log.e("Unable to append text to file: " + text, e);
		}
	}
	
	/**
	 * Close file writer.
	 *
	 * @param fileWriter the file writer
	 */
	public static void closeFileWriter(FileWriter fileWriter) {
		if (Log.eIf(null == fileWriter, "Cannot close file writer. Empty file writer is given.")) {
			return;
		}
		try {
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			Log.e("Unable to close file writer.", e);
		}
	}
	
}
