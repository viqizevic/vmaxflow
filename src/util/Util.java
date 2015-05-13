package util;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

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
	 * Gets the file extension.
	 *
	 * @param filePath the file path
	 * @return the file extension
	 */
	public static String getFileExtension(String filePath) {
		String fileName = new File(filePath).getName();
		if (fileName.contains(".")) {
			return fileName.substring(fileName.lastIndexOf("."));
		}
		return "";
	}
	
	/**
	 * Gets the file extension.
	 *
	 * @param filePaths the file paths
	 * @return the file extension or null if extension is not unique
	 */
	public static String getFileExtension(String[] filePaths) {
		String ext = null;
		for (String s : filePaths) {
			if (null == ext) {
				ext = getFileExtension(s);
			} else {
				if (!ext.equals(getFileExtension(s))) {
					return null;
				}
			}
		}
		return ext;
	}
	
	/**
	 * Removes the file extension.
	 *
	 * @param filePath the file path
	 * @return the string
	 */
	public static String removeFileExtension(String filePath) {
		return filePath.replaceAll(getFileExtension(filePath), "");
	}
	
	/**
	 * Gets the folder path.
	 *
	 * @param filePath the file path
	 * @return the folder path
	 */
	public static String getFolderPath(String filePath) {
		String folderPath = new File(filePath).getParent();
		if (null == folderPath) {
			folderPath = "";
		} else if (!folderPath.isEmpty() && !folderPath.endsWith("/")){
			folderPath += "/";
		}
		return folderPath;
	}
	
	/**
	 * Capitalize as title case.
	 *
	 * @param text the text
	 * @return the string
	 */
	public static String capitalizeAsTitleCase(String text) {
		if (null == text || text.isEmpty()) {
			return text;
		}
		String[] s = text.toLowerCase().split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i=0; i < s.length; i++) {
			sb.append(Character.toUpperCase(s[i].charAt(0))).append(s[i].substring(1)).append(" ");
		}
		return sb.toString().trim();
	}
	
	/**
	 * String is in array.
	 *
	 * @param target the target
	 * @param array the array
	 * @return true, if successful
	 */
	public static boolean stringIsInArray(String target, String[] array) {
		for (String s : array) {
			if (s.equals(target)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Using windows system.
	 *
	 * @return true, if successful
	 */
	public static boolean usingWindowsSystem() {
		String osName = System.getProperty("os.name");
		return osName.startsWith("Windows");
	}
	
	/**
	 * Adds the numbers at the end of each element.
	 *
	 * @param array the array
	 * @return the string[]
	 */
	public static String[] addNumbers(String[] array) {
		int n = array.length;
		String[] s = new String[n];
		for (int i=0; i<n; i++) {
			int k = i+1;
			s[i] = array[i] + String.format("[%d]", k);
		}
		return s;
	}
	
	/**
	 * Gets the power set.
	 *
	 * @param set the set
	 * @return the power set
	 */
	public static Vector<HashSet<Integer>> getPowerSet(Collection<Integer> set) {
		Vector<HashSet<Integer>> powerSet = new Vector<HashSet<Integer>>();
		for (Integer element : set) {
			HashSet<Integer> single = new HashSet<Integer>();
			single.add(element);
			Vector<HashSet<Integer>> prevs = new Vector<HashSet<Integer>>();
			prevs.addAll(powerSet);
			for (HashSet<Integer> prev : prevs) {
				HashSet<Integer> newset = new HashSet<Integer>();
				newset.addAll(prev);
				newset.addAll(single);
				powerSet.add(newset);
			}
			powerSet.add(single);
		}
		return powerSet;
	}
	
	/**
	 * Creates the constant array.
	 *
	 * @param size the size
	 * @param constantValue the constant value
	 * @return the double[]
	 */
	public static double[] createConstantArray(int size, double constantValue) {
		double[] constantArray = new double[size];
		for (int i=0; i < size; i++) {
			constantArray[i] = constantValue;
		}
		return constantArray;
	}
	
}
