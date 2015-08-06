package util;

import java.util.HashMap;

/**
 * The Class Timer.
 */
public class Timer {
	
	/** The timers. */
	private static HashMap<String, Long> timer_;
	
	private static int lastUsedId_;
	
	private static String PREFIX_ = "timer";
	
	/**
	 * Start new timer.
	 *
	 * @return the string corresponds to the new created timer.
	 * Needed to get the elapsed time from {@link #getTime(String, boolean) getTime} method.
	 */
	public static String startNewTimer() {
		if (null == timer_) {
			timer_ = new HashMap<String, Long>();
		}
		int k = lastUsedId_;
		while (timer_.containsKey(PREFIX_+k)) {
			k = k+1;
		}
		String id = PREFIX_ + k;
		long startTime = System.currentTimeMillis();
		timer_.put(id, startTime);
		lastUsedId_ = k;
		return id;
	}
	
	/**
	 * Stop timer and print log.
	 *
	 * @param startTimerId the start timer id received from {@link #startNewTimer() startNewTimer} method.
	 * @return the total time in milliseconds
	 */
	public static long stopTimerAndPrintLog(String startTimerId) {
		return getTime(startTimerId, true, null, false);
	}
	
	/**
	 * Stop timer and print log.
	 *
	 * @param startTimerId the start timer id received from {@link #startNewTimer() startNewTimer} method.
	 * @param shouldPrintLog boolean which tells if this method should print a message over log
	 * @param description the description of what the timer is for
	 * @return the total time in milliseconds
	 */
	public static long stopTimerAndPrintLog(String startTimerId, String description) {
		return getTime(startTimerId, true, description, false);
	}
	
	/**
	 * Stop timer.
	 *
	 * @param startTimerId the start timer id received from {@link #startNewTimer() startNewTimer} method.
	 * @return the total time in milliseconds
	 */
	public static long stopTimer(String startTimerId) {
		return getTime(startTimerId, false, null, false);
	}
	
	/**
	 * Gets the time.
	 *
	 * @param startTimerId the start timer id received from {@link #startNewTimer() startNewTimer} method.
	 * @return the time in milliseconds
	 */
	public static long getTime(String startTimerId) {
		return getTime(startTimerId, false, null, true);
	}
	
	/**
	 * Gets the time.
	 *
	 * @param startTimerId the start timer id received from {@link #startNewTimer() startNewTimer} method.
	 * @param shouldPrintLog boolean which tells if this method should print a message over log
	 * @param description the description of what the timer is for
	 * @return the total time in milliseconds
	 */
	public static long getTime(String startTimerId, boolean shouldPrintLog, String description) {
		return getTime(startTimerId, shouldPrintLog, description, true);
	}
	
	/**
	 * Gets the time.
	 *
	 * @param startTimerId the start timer id received from {@link #startNewTimer() startNewTimer} method
	 * @param shouldPrintLog boolean which tells if this method should print a message over log
	 * @param description the description of what the timer is for
	 * @param keepTimer boolean which tells if this method should keep the timer or delete it from memory
	 * @return the total time in milliseconds
	 */
	public static long getTime(String startTimerId, boolean shouldPrintLog, String description, boolean keepTimer) {
		if (!timer_.containsKey(startTimerId)) {
			Log.e("Request for non-existent timer: " + startTimerId);
			return 0;
		}
		long t = System.currentTimeMillis() - timer_.get(startTimerId);
		if (shouldPrintLog) {
			String print = getFriendlyString(t) + " elapsed time.";
			if (null != description && !description.isEmpty()) {
				print = description + ": " + print;
			}
			Log.p(print);
		}
		if (!keepTimer) {
			removeTimer(startTimerId);
		}
		return t;
	}
	
	/**
	 * Gets the friendly string.
	 *
	 * @param timeInMillis the time in millis
	 * @return the friendly string
	 */
	public static String getFriendlyString(long timeInMillis) {
		String print = timeInMillis+"ms";
		int h = (int) timeInMillis / (60*60*1000); timeInMillis -= h*60*60*1000;
		int m = (int) timeInMillis / (60*1000);    timeInMillis -= m*60*1000;
		int s = (int) timeInMillis / 1000;
		if (h > 0) {
			print = String.format("%dh %dm", h, m);
		} else if (m > 0) {
			print = String.format("%dm %ds", m, s);
		} else if (s > 0) {
			print = String.format("%.2fs", timeInMillis/1000.0);
		} else {
			print = String.format("%dms", timeInMillis);
		}
		return print;
	}
	
	/**
	 * Removes the timer.
	 *
	 * @param startTimerId the start timer id
	 */
	public static void removeTimer(String startTimerId) {
		if (timer_.containsKey(startTimerId)) {
			timer_.remove(startTimerId);
			int k = Integer.parseInt(startTimerId.replace(PREFIX_, ""));
			lastUsedId_ = k;
		}
	}
	
}
