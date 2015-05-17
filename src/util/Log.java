package util;

import java.io.FileWriter;

/**
 * The Class Log.
 */
public class Log {
	
	/** The print log on. */
	private static boolean PRINT_LOG_ON_ = true;
	
	/** The write log on variable. */
	private static boolean WRITE_LOG_ON_ = true;
	
	/** The Constant LOG_FILE_NAME_. */
	private static final String LOG_FILE_NAME_ = "vmaxflow.log.txt";
	
	/** The log file writer_. */
	private static FileWriter logFileWriter_;
	
	/**
	 * Turn on print log.
	 */
	public static void turnOnPrintLog() {
		PRINT_LOG_ON_ = true;
	}
	
	/**
	 * Turn off print log.
	 */
	public static void turnOffPrintLog() {
		PRINT_LOG_ON_ = false;
	}
	
	/**
	 * Turn on log writer.
	 */
	public static void turnOnLogWriter() {
		WRITE_LOG_ON_ = true;
	}
	
	/**
	 * Turn off log writer.
	 */
	public static void turnOffLogWriter() {
		WRITE_LOG_ON_ = false;
	}
	
	/**
	 * Prints log.
	 *
	 * @param message the message
	 */
	public static void p(String message) {
		if (PRINT_LOG_ON_) {
			if (message.endsWith("\n")) {
				System.out.print(message);
			} else {
				System.out.println(message);
			}
		}
		addLogContent(message);
	}
	
	/**
	 * Prints log using String format.
	 *
	 * @param format the format
	 * @param args the args
	 */
	public static void ps(String format, Object... args) {
		Log.p(String.format(format, args));
	}
	
	/**
	 * Forces prints log.
	 *
	 * @param message the message
	 */
	public static void pf(String message) {
		boolean ps = PRINT_LOG_ON_;
		boolean ws = WRITE_LOG_ON_;
		turnOnPrintLog();
		turnOnLogWriter();
		p(message);
		PRINT_LOG_ON_ = ps;
		WRITE_LOG_ON_ = ws;
	}
	
	/**
	 * Prints warning log.
	 *
	 * @param message the message
	 */
	public static void w(String message) {
		String warningMessage = "WARNING: " + message;
		p(warningMessage);
	}
	
	/**
	 * Prints error log.
	 *
	 * @param ifCondition the if condition
	 * @param message the message
	 * @return true, if should print
	 */
	public static boolean eIf(boolean ifCondition, String message) {
		if (ifCondition) {
			e(message);
		}
		return ifCondition;
	}
	
	/**
	 * Prints error log.
	 *
	 * @param message the message
	 */
	public static void e(String message) {
		e(message, null);
	}
	
	/**
	 * Prints error log.
	 *
	 * @param message the message
	 * @param exception the exception
	 */
	public static void e(String message, Exception exception) {
		String errorMessage = "ERROR: " + message;
		if (PRINT_LOG_ON_) {
			System.err.println(errorMessage);
			if (exception != null) {
				exception.printStackTrace();
			}
		}
		addLogContent(errorMessage);
		if (exception != null) {
			addLogContent("EXCEPTION: " + exception.toString());
		}
	}
	
	/**
	 * Adds log content.
	 *
	 * @param content the content
	 */
	private static void addLogContent(String content) {
		if (WRITE_LOG_ON_) {
			appendToLogFile(content);
		}
	}
	
	/**
	 * Append to log file.
	 *
	 * @param content the content
	 */
	public static void appendToLogFile(String content) {
		if (null == content) {
			return;
		}
		if (null == logFileWriter_) {
			logFileWriter_ = Util.getNewFileWriter(LOG_FILE_NAME_);
		}
		if (!content.endsWith("\n")) {
			content += "\n";
		}
		Util.appendToFile(logFileWriter_, content);
	}
	
	/**
	 * Finish creating log file.
	 */
	public static void finishCreatingLogFile() {
		Util.closeFileWriter(logFileWriter_);
	}
	
}
