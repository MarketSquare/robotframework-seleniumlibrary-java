package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;
import com.github.markusbernhardt.seleniumlibrary.utils.Python;

@RobotKeywords
public class JavaScript extends RunOnFailureKeywordsAdapter {

	protected boolean acceptOnNextConfirmationDefault = true;
	protected boolean acceptOnNextConfirmation = true;

	/**
	 * Instantiated BrowserManagement keyword bean
	 */
	@Autowired
	protected BrowserManagement browserManagement;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	protected Logging logging;
	
	@Autowired
	protected Robot robot;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeywordOverload
	public void alertShouldBePresent() {
		alertShouldBePresent("");
	}

	@RobotKeyword("Verify an alert is present and dismiss it.\r\n" + 
	        "\r\n" + 
	        "If ``text`` is a non-empty string, then it is also verified that the message of the alert equals to text.\r\n" + 
	        "\r\n" + 
	        "Will fail if no alert is present. Note that following keywords will fail unless the alert is confirmed by this keyword or another like `Confirm Action`.")
	@ArgumentNames({ "text=NONE" })
	public void alertShouldBePresent(String...params) {
        String text = robot.getParamsValue(params, 0, null);
		String alertText = confirmAction();
		if (text != null && !alertText.equals(text)) {
			throw new SeleniumLibraryNonFatalException(String.format("Alert text should have been '%s' but was '%s'",
					text, alertText));
		}
	}

	@RobotKeyword("Cancel will be selected the next time a confirmation dialog appears.\r\n" + 
	        "\r\n" + 
	        "Note that every time a confirmation comes up, it must be confirmed by the keywords `Alert Should Be Present` or `Confirm Action`. Otherwise all following operations will fail.")
	public void chooseCancelOnNextConfirmation() {
		acceptOnNextConfirmation = false;
	}

	@RobotKeyword("OK will be selected the next time a confirmation dialog appears.\r\n" + 
	        "\r\n" + 
	        "Note that every time a confirmation comes up, it must be confirmed by the keywords `Alert Should Be Present` or `Confirm Action`. Otherwise all following operations will fail.")
	public void chooseOkOnNextConfirmation() {
		acceptOnNextConfirmation = true;
	}

    @RobotKeyword("Cancel will as default be selected from now on every time a confirmation dialog appears.\r\n" + 
            "\r\n" + 
            "Note that every time a confirmation comes up, it must be confirmed by the keywords `Alert Should Be Present` or `Confirm Action`. Otherwise all following operations will fail.")
	public void chooseCancelOnConfirmation() {
		acceptOnNextConfirmationDefault = false;
		acceptOnNextConfirmation = false;
	}

    @RobotKeyword("OK will as default be selected from now on every time a confirmation dialog appears.\r\n" + 
            "\r\n" + 
            "Note that every time a confirmation comes up, it must be confirmed by the keywords `Alert Should Be Present` or `Confirm Action`. Otherwise all following operations will fail.")
	public void chooseOkOnConfirmation() {
		acceptOnNextConfirmationDefault = true;
		acceptOnNextConfirmation = true;
	}

	@RobotKeyword("Dismisses currently shown confirmation dialog and returns its message.\r\n" + 
	        "\r\n" + 
	        "By default, this keyword chooses 'OK' option from the dialog. If 'Cancel' needs to be chosen, keyword `Choose Cancel On Next Confirmation` must be called before the action that causes the confirmation dialog to be shown.\r\n" + 
	        "\r\n" + 
	        "Example:\r\n" + 
	        " | Click Button | Send |  | # Shows a confirmation dialog | \r\n" + 
	        " | ${message}= | Confirm Action |  | # Chooses Ok | \r\n" + 
	        " | Should Be Equal | ${message} | Are your sure? | # Check dialog message | \r\n" + 
	        " | Choose Cancel On Next Confirmation |  |  | # Choose cancel on next Confirm Action | \r\n" + 
	        " | Click Button | Send |  | # Shows a confirmation dialog | \r\n" + 
	        " | Confirm Action |  |  | # Chooses Cancel |")
	public String confirmAction() {
		try {
			Alert alert = browserManagement.getCurrentWebDriver().switchTo().alert();
			String text = alert.getText().replace("\n", "");
			if (acceptOnNextConfirmation) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			acceptOnNextConfirmation = acceptOnNextConfirmationDefault;
			return text;
		} catch (WebDriverException wde) {
			throw new SeleniumLibraryNonFatalException("There were no alerts");
		}
	}

	@RobotKeyword("Execute the given JavaScript ``code``.\r\n" + 
	        "\r\n" + 
	        "The given code may contain multiple lines of code, but must contain a return statement (with the value to be returned) at the end.\r\n" + 
	        "\r\n" + 
	        "The given code may be divided into multiple cells in the test data. In that case, the parts are concatenated together without adding spaces. If the given code is an absolute path to an existing file, the JavaScript to execute will be read from that file. Forward slashes work as a path separator on all operating systems.\r\n" + 
	        "\r\n" + 
	        "Note that by default the code will be executed in the context of the Selenium object itself, so *this* will refer to the Selenium object. Use *window* to refer to the window of your application, e.g. _window.document.getElementById('foo')_.\r\n" + 
	        "\r\n" + 
	        "Example:\r\n" + 
	        " | Execute JavaScript | return window.my_js_function('arg1', 'arg2'); | # Directly execute the JavaScript | \r\n" + 
	        " | Execute JavaScript | ${CURDIR}/js_to_execute.js | # Load the JavaScript to execute from file |")
	@ArgumentNames({ "*code" })
	public Object executeJavascript(String... code) {
		String js = getJavascriptToExecute(Python.join("", Arrays.asList(code)));
		logging.html(String.format("Executing JavaScript:\n%s", js));
		return ((JavascriptExecutor) browserManagement.getCurrentWebDriver()).executeScript(js);
	}

    @RobotKeyword("Execute the given JavaScript ``code`` asynchronously.\r\n" + 
            "\r\n" + 
            "The given code may contain multiple lines of code, but must contain a return statement (with the value to be returned) at the end.\r\n" + 
            "\r\n" + 
            "The given code may be divided into multiple cells in the test data. In that case, the parts are concatenated together without adding spaces. If the given code is an absolute path to an existing file, the JavaScript to execute will be read from that file. Forward slashes work as a path separator on all operating systems.\r\n" + 
            "\r\n" + 
            "Note that by default the code will be executed in the context of the Selenium object itself, so *this* will refer to the Selenium object. Use *window* to refer to the window of your application, e.g. _window.document.getElementById('foo')_.\r\n" + 
            "\r\n" + 
            "Example:\r\n" + 
            " | Execute Async JavaScript | return window.my_js_function('arg1', 'arg2'); | # Directly execute the JavaScript | \r\n" + 
            " | Execute Async JavaScript | ${CURDIR}/js_to_execute.js | # Load the JavaScript to execute from file |")
	@ArgumentNames({ "*code" })
	public Object executeAsyncJavascript(String... code) {
		String js = getJavascriptToExecute(Python.join("", Arrays.asList(code)));
		logging.html(String.format("Executing JavaScript:\n%s", js));
		return ((JavascriptExecutor) browserManagement.getCurrentWebDriver()).executeAsyncScript(js);
	}

    @RobotKeyword("Returns the text of current JavaScript alert.\r\n" + 
            "\r\n" + 
            "This keyword will fail if no alert is present. Note that following keywords will fail unless the alert is confirmed by the keywords `Alert Should Be Present` or `Confirm Action`.")
	public String getAlertMessage() {
		try {
			Alert alert = browserManagement.getCurrentWebDriver().switchTo().alert();
			return alert.getText().replace("\n", "");
		} catch (WebDriverException wde) {
			throw new SeleniumLibraryNonFatalException("There were no alerts");
		}
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected static String readFile(String path) throws IOException {
		try (FileInputStream stream = new FileInputStream(new File(path))) {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		}
	}

	protected String getJavascriptToExecute(String code) {
		String codepath = code.replace('/', File.separatorChar);
		if (!new File(codepath).isFile()) {
			return code;
		}
		logging.html(String.format("Reading JavaScript from file <a href=\"file://%s\">%s</a>.",
				codepath.replace(File.separatorChar, '/'), codepath));
		try {
			return readFile(codepath);
		} catch (IOException e) {
			throw new SeleniumLibraryNonFatalException("Cannot read JavaScript file: " + codepath);
		}
	}

}
