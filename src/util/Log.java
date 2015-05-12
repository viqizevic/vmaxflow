package util;

/**
 * The Class Log.
 */
public class Log {
	
	/** The print log on. */
	private static boolean PRINT_LOG_ON_ = true;
	
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
		turnOnPrintLog();
		p(message);
		PRINT_LOG_ON_ = ps;
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
	}
	
}
