package com.github.markusbernhardt.seleniumlibrary.keywords;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;
import com.github.markusbernhardt.seleniumlibrary.utils.Robotframework;

@RobotKeywords
public class Waiting extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated BrowserManagement keyword bean
	 */
	@Autowired
	protected BrowserManagement browserManagement;

	/**
	 * Instantiated Element keyword bean
	 */
	@Autowired
	protected Element element;
	
	@Autowired
	protected Logging logging;
	
	@Autowired
	protected Robot robot;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeyword("Waits until the given JavaScript ``condition`` is true.\r\n" + 
	        "\r\n" + 
	        "Fails, if the timeout expires, before the condition gets true. \r\n" + 
	        "\r\n" + 
	        "The condition may contain multiple JavaScript statements, but the last statement must return a boolean. Otherwise this keyword will always hit the timeout.\r\n" + 
	        "\r\n" + 
	        "Note that by default the code will be executed in the context of the Selenium object itself, so *this* will refer to the Selenium object. Use *window* to refer to the window of your application, e.g. _window.document.getElementById('foo')_.\r\n" + 
	        "\r\n" + 
	        "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "condition", "timeout=NONE", "message=NONE" })
	public void waitForCondition(final String condition, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Condition '%s' did not become true in <TIMEOUT>", condition);
		}
		waitUntil(timeout, message, () -> Boolean.TRUE.equals(((JavascriptExecutor) browserManagement.getCurrentWebDriver())
				.executeScript(condition)));
	}

    @RobotKeyword("Waits until the current page contains ``text``.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the text appears. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "condition", "timeout=NONE", "message=NONE" })
	public void waitUntilPageContains(final String text, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Text '%s' did not appear in <TIMEOUT>", text);
		}
		waitUntil(timeout, message, () -> element.isTextPresent(text));
	}

    @RobotKeyword("Waits until the current page does not contain ``text``.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the text disappears. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "text", "timeout=NONE", "message=NONE" })
	public void waitUntilPageNotContains(final String text, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Text '%s' did not disappear in <TIMEOUT>", text);
		}
		waitUntil(timeout, message, () -> !element.isTextPresent(text));
	}

    @RobotKeyword("Waits until the current page does not contain ``text``.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the text disappears. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "text", "timeout=NONE", "message=NONE" })
	public void waitUntilPageDoesNotContain(final String text, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		waitUntilPageNotContains(text, timeout, message);
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is found on the current page.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element appears. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilPageContainsElement(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Element '%s' did not appear in <TIMEOUT>", locator);
		}
		waitUntil(timeout, message, () -> element.isElementPresent(locator));
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is not found on the current page.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element disappears. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilPageNotContainsElement(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Element '%s' did not disappear in <TIMEOUT>", locator);
		}
		waitUntil(timeout, message, () -> !element.isElementPresent(locator));
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is not found on the current page.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element disappears. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilPageDoesNotContainElement(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
	    waitUntilPageNotContainsElement(locator, timeout, message);
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is visible.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element gets visible. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilElementIsVisible(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Element '%s' not visible in <TIMEOUT>", locator);
		}
		waitUntil(timeout, message, () -> element.isVisible(locator));
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is not visible.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element gets invisible. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilElementIsNotVisible(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Element '%s' still visible in <TIMEOUT>", locator);
		}
		waitUntil(timeout, message, () -> !element.isVisible(locator));
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is clickable.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element gets clickable. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilElementIsClickable(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Element '%s' not clickable in <TIMEOUT>", locator);
		}
		waitUntil(timeout, message, () -> element.isClickable(locator));
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is not clickable.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element gets unclickable. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilElementIsNotClickable(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Element '%s' still clickable in <TIMEOUT>", locator);
		}
		waitUntil(timeout, message, () -> !element.isClickable(locator));
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is succesfully clicked on.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element gets clicked on. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilElementIsSuccessfullyClicked(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Element '%s' not successfully clicked in <TIMEOUT>", locator);
		}
		waitUntil(timeout, message, () -> {
			element.clickElement(locator);
			return true;
		});
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is selected.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element gets selected. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilElementIsSelected(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Element '%s' not selected in <TIMEOUT>", locator);
		}
		waitUntil(timeout, message, () -> element.isSelected(locator));
	}

    @RobotKeyword("Waits until the element identified by ``locator`` is not selected.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the element gets unselected. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "locator", "timeout=NONE", "message=NONE" })
	public void waitUntilElementIsNotSelected(final String locator, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Element '%s' still selected in <TIMEOUT>", locator);
		}
		waitUntil(timeout, message, () -> !element.isSelected(locator));
	}

    @RobotKeyword("Waits until the current page title contains ``title``.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the page title contains the given title. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "title", "timeout=NONE", "message=NONE" })
	public void waitUntilTitleContains(final String title, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Title '%s' did not appear in <TIMEOUT>", title);
		}
		waitUntil(timeout, message, () -> {
			String currentTitle = browserManagement.getTitle();
			return currentTitle != null && currentTitle.contains(title);
		});
	}

    @RobotKeyword("Waits until the current page title does not contain ``title``.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the page title does not contain the given title. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "title", "timeout=NONE", "message=NONE" })
	public void waitUntilTitleNotContains(final String title, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Title '%s' did not appear in <TIMEOUT>", title);
		}
		waitUntil(timeout, message, () -> {
			String currentTitle = browserManagement.getTitle();
			return currentTitle == null || !currentTitle.contains(title);
		});
	}

    @RobotKeyword("Waits until the current page title is exactly ``title``.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the page title matches the given title. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "title", "timeout=NONE", "message=NONE" })
	public void waitUntilTitleIs(final String title, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Title '%s' did not appear in <TIMEOUT>", title);
		}
		waitUntil(timeout, message, () -> {
			String currentTitle = browserManagement.getTitle();
			return currentTitle != null && currentTitle.equals(title);
		});
	}

    @RobotKeyword("Waits until the current page title is not exactly ``title``.\r\n" + 
            "\r\n" + 
            "Fails, if the timeout expires, before the page title does not match the given title. \r\n" + 
            "\r\n" + 
            "See `Introduction` for details about timeouts.")
	@ArgumentNames({ "title", "timeout=NONE", "message=NONE" })
	public void waitUntilTitleIsNot(final String title, String...params) {
        String timeout = robot.getParamsValue(params, 0, null);
        String message = robot.getParamsValue(params, 1, null);
		if (message == null) {
			message = String.format("Title '%s' did not appear in <TIMEOUT>", title);
		}
		waitUntil(timeout, message, () -> {
			String currentTitle = browserManagement.getTitle();
			return currentTitle == null || !currentTitle.equals(title);
		});
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected void waitUntil(String timestr, String message, WaitUntilFunction function) {
		double timeout = timestr != null ? Robotframework.timestrToSecs(timestr) : browserManagement.getTimeout();
		message = message.replace("<TIMEOUT>", Robotframework.secsToTimestr(timeout));
		long maxtime = System.currentTimeMillis() + (long) (timeout * 1000);
		Throwable exception = new Throwable();
		for (;;) {
			try {
				if (function.isFinished()) {
					break;
				}
			} catch (Throwable t) {
			    exception = t;
			}
			if (System.currentTimeMillis() > maxtime) {
			    logging.trace(ExceptionUtils.getStackTrace(exception));
				throw new SeleniumLibraryNonFatalException(message);
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException ignored) {
			}
		}
	}

	protected interface WaitUntilFunction {

		boolean isFinished();
	}

}
