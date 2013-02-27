package org.robotframework.selenium2library;

import java.io.File;
import java.util.ResourceBundle;

import org.robotframework.selenium2library.keywords.Selenium2LibraryExtension;

/**
 * Robotframework Library. All public methods are keywords.
 */
public class Selenium2Library extends Selenium2LibraryExtension {

	/**
	 * This means the same instance of this class is used throughout the
	 * lifecycle of a Robot Framework test execution.
	 */
	public static String ROBOT_LIBRARY_SCOPE = "GLOBAL";

	/**
	 * Documentation is in text format.
	 */
	public static String ROBOT_LIBRARY_DOC_FORMAT = "TEXT";

	/**
	 * The actual version of this library. Loaded from maven project.
	 */
	public static String ROBOT_LIBRARY_VERSION;

	static {
		/**
		 * Load the version from file
		 */
		try {
			ROBOT_LIBRARY_VERSION = ResourceBundle.getBundle(
					Selenium2Library.class.getCanonicalName().replace(".",
							File.separator)).getString("version");
		} catch (RuntimeException e) {
			ROBOT_LIBRARY_VERSION = "unknown";
		}
	}

	public Selenium2Library() {
		this(5.0);
	}

	public Selenium2Library(double timeout) {
		this(timeout, 0);
	}

	public Selenium2Library(double timeout, double implicitWait) {
		this(timeout, implicitWait, "Capture Page Screenshot");
	}

	public Selenium2Library(double timeout, double implicitWait,
			String runOnFailureKeyword) {
		this.timeout = timeout;
		this.implicitWait = implicitWait;
		this.runOnFailureKeyword = runOnFailureKeyword;
	}

	/**
	 * This method is called by the
	 * org.robotframework.selenium2library.aspects.RunOnFailureAspect in case a
	 * exception is thrown in one of the public methods of a keyword class.
	 */
	public void runOnFailureByAspectJ() {
		runOnFailure();
	}

}