package util;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The Class FileOrganizer.
 */
public class FileOrganizer {
	
	/**
	 * Write file.
	 *
	 * @param fileName the file name
	 * @param content the content
	 */
	public static void writeFile(String fileName, String content) {
		FileWriter fw = FileOrganizer.getNewFileWriter(fileName);
		FileOrganizer.appendToFile(fw, content);
		FileOrganizer.closeFileWriter(fw);
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
