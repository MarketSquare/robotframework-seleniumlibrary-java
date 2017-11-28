package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;

@RobotKeywords
public class FormElement extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated Element keyword bean
	 */
	@Autowired
	protected Element element;

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
	public void submitForm() {
		submitForm(null);
	}

    @RobotKeyword("Submit the form identified by ``locator``.\r\n" + 
            "\r\n" + 
            "If the locator is empty, the first form in the page will be submitted.\r\n" + 
            "\r\n" + 
            "Key attributes for forms are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator=NONE" })
	public void submitForm(String locator) {
		logging.info(String.format("Submitting form '%s'.", locator));
		if (locator == null) {
			locator = "xpath=//form";
		}
		List<WebElement> webElements = element.elementFind(locator, true, true, "form");
		webElements.get(0).submit();
	}

    @RobotKeyword("Verify the checkbox identified by ``locator``is selected/checked.\r\n" + 
            "\r\n" + 
            "Key attributes for checkboxes are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public void checkboxShouldBeSelected(String locator) {
		logging.info(String.format("Verifying checkbox '%s' is selected.", locator));
		WebElement element = getCheckbox(locator);
		if (!element.isSelected()) {
			throw new SeleniumLibraryNonFatalException(String.format("Checkbox '%s' should have been selected.",
					locator));
		}
	}

    @RobotKeyword("Verify the checkbox identified by ``locator``is not selected/checked.\r\n" + 
            "\r\n" + 
            "Key attributes for checkboxes are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public void checkboxShouldNotBeSelected(String locator) {
		logging.info(String.format("Verifying checkbox '%s' is selected.", locator));
		WebElement element = getCheckbox(locator);
		if (element.isSelected()) {
			throw new SeleniumLibraryNonFatalException(String.format("Checkbox '%s' should not have been selected.",
					locator));
		}
	}

    @RobotKeyword("Verify the checkbox identified by ``locator``is found on the current page.\r\n" + 
            "\r\n" + 
            "Key attributes for checkboxes are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldContainCheckbox(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		element.pageShouldContainElement(locator, "checkbox", message, logLevel);
	}

    @RobotKeyword("Verify the checkbox identified by ``locator``is not found on the current page.\r\n" + 
            "\r\n" + 
            "Key attributes for checkboxes are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldNotContainCheckbox(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		element.pageShouldNotContainElement(locator, "checkbox", message, logLevel);
	}

    @RobotKeyword("Select the checkbox identified by ``locator``.\r\n" + 
            "\r\n" +
            "Does nothing, if the checkbox is already selected.\r\n" + 
            "\r\n" + 
            "Key attributes for checkboxes are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public void selectCheckbox(String locator) {
		logging.info(String.format("Selecting checkbox '%s'.", locator));
		WebElement element = getCheckbox(locator);
		if (!element.isSelected()) {
			element.click();
		}
	}

    @RobotKeyword("Unselect the checkbox identified by ``locator``.\r\n" + 
            "\r\n" +
            "Does nothing, if the checkbox is not selected.\r\n" + 
            "\r\n" + 
            "Key attributes for checkboxes are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public void unselectCheckbox(String locator) {
		logging.info(String.format("Selecting checkbox '%s'.", locator));
		WebElement element = getCheckbox(locator);
		if (element.isSelected()) {
			element.click();
		}
	}

    @RobotKeyword("Verify the radio button identified by ``locator``is found on the current page.\r\n" + 
            "\r\n" + 
            "Key attributes for radio buttons are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldContainRadioButton(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		element.pageShouldContainElement(locator, "radio button", message, logLevel);
	}

    @RobotKeyword("Verify the radio button identified by ``locator``is not found on the current page.\r\n" + 
            "\r\n" + 
            "Key attributes for radio buttons are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldNotContainRadioButton(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		element.pageShouldNotContainElement(locator, "radio button", message, logLevel);
	}

    @RobotKeyword("Verify the radio button group identified by ``groupName``has its selection set to ``value``.\r\n" + 
            "\r\n" + 
            "See `Select Radio Button` for details about locating radio buttons.")
	@ArgumentNames({ "groupName", "value" })
	public void radioButtonShouldBeSetTo(String groupName, String value) {
		logging.info(String.format("Verifying radio button '%s' has selection '%s'.", groupName, value));
		List<WebElement> elements = getRadioButtons(groupName);
		String actualValue = getValueFromRadioButtons(elements);
		if (actualValue == null || !actualValue.equals(value)) {
			throw new SeleniumLibraryNonFatalException(String.format(
					"Selection of radio button '%s' should have been '%s' but was '%s'", groupName, value, actualValue));
		}
	}

    @RobotKeyword("Verify the radio button group identified by ``groupName``has no selection.\r\n" + 
            "\r\n" + 
            "See `Select Radio Button` for details about locating radio buttons.")
	@ArgumentNames({ "groupName" })
	public void radioButtonShouldNotBeSelected(String groupName) {
		logging.info(String.format("Verifying radio button '%s' has no selection.", groupName));
		List<WebElement> elements = getRadioButtons(groupName);
		String actualValue = getValueFromRadioButtons(elements);
		if (actualValue != null) {
			throw new SeleniumLibraryNonFatalException(String.format(
					"Radio button group '%s' should not have had selection, but '%s' was selected", groupName,
					actualValue));
		}
	}

	@RobotKeyword("Sets the selection of the radio button group identified by ``groupName`` to ``value``.\r\n" + 
	        "\r\n" + 
	        "Example:\r\n" + 
	        " | Select Radio Button | size | XL | # Matches HTML like <input type=\"radio\" name=\"size\" value=\"XL\">XL</input> | \r\n" + 
	        " | Select Radio Button | size | sizeXL | # Matches HTML like <input type=\"radio\" name=\"size\" value=\"XL\" id=\"sizeXL\">XL</input> |")
	@ArgumentNames({ "groupName", "value" })
	public void selectRadioButton(String groupName, String value) {
		logging.info(String.format("Selecting '%s' from radio button '%s'.", value, groupName));
		WebElement element = getRadioButtonWithValue(groupName, value);
		if (!element.isSelected()) {
			element.click();
		}
	}

	@RobotKeyword("Types the given ``filePath`` into the input field identified by ``locator``.\r\n" + 
	        "\r\n" + 
	        "This keyword is most often used to input files into upload forms. The file specified with filePath must be available on the same host where the Selenium Server is running.\r\n" + 
	        "\r\n" + 
	        "Example:\r\n" + 
	        "| Choose File | my_upload_field | /home/user/files/trades.csv |\r\n" + 
	        "Key attributes for input fields are id and name. See Introduction for details about locators.")
	@ArgumentNames({ "locator", "filePath" })
	public void chooseFile(String locator, String filePath) {
		if (!new File(filePath).isFile()) {
			logging.info(String.format("File '%s' does not exist on the local file system", filePath));
		}
		element.elementFind(locator, true, true).get(0).sendKeys(filePath);
	}

    @RobotKeyword("Types the given ``text`` into the password field identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Key attributes for input fields are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text" })
	public void inputPassword(String locator, String text) {
		logging.info(String.format("Typing password into text field '%s'", locator));
		inputTextIntoTextField(locator, text);
	}

    @RobotKeyword("Types the given ``text`` into the text field identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Key attributes for input fields are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text" })
	public void inputText(String locator, String text) {
		logging.info(String.format("Typing text '%s' into text field '%s'", text, locator));
		inputTextIntoTextField(locator, text);
	}

    @RobotKeyword("Verify the text field identified by ``locator`` is found on the current page.\r\n" + 
            "\r\n" + 
            "Key attributes for input fields are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldContainTextfield(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		element.pageShouldContainElement(locator, "text field", message, logLevel);
	}

    @RobotKeyword("Verify the text field identified by ``locator`` is not found on the current page.\r\n" + 
            "\r\n" + 
            "Key attributes for input fields are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldNotContainTextfield(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		element.pageShouldNotContainElement(locator, "text field", message, logLevel);
	}

    @RobotKeyword("Verify the text field identified by ``locator`` is exactly ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for input fields are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textfieldValueShouldBe(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
		String actual = element.getValue(locator, "text field");
		if (!actual.contains(text)) {
			if (message == null) {
				message = String.format("Value of text field '%s' should have been '%s' but was '%s'", locator, text,
						actual);
			}
			throw new SeleniumLibraryNonFatalException(message);
		}
		logging.info(String.format("Content of text field '%s' is '%s'.", locator, text));
	}

    @RobotKeyword("Verify the text field identified by ``locator`` is not exactly ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for input fields are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textfieldValueShouldNotBe(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
		String actual = element.getValue(locator, "text field");
		if (actual.contains(text)) {
			if (message == null) {
				message = String.format("Value of text field '%s' should not have been '%s' but was '%s'", locator,
						text, actual);
			}
			throw new SeleniumLibraryNonFatalException(message);
		}
		logging.info(String.format("Content of text field '%s' is '%s'.", locator, text));
	}

    @RobotKeyword("Verify the text field identified by ``locator`` contains ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for input fields are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textfieldShouldContain(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
		String actual = element.getValue(locator, "text field");
		if (!actual.contains(text)) {
			if (message == null) {
				message = String.format("Text field '%s' should have contained text '%s', but was '%s'", locator, text,
						actual);
			}
			throw new SeleniumLibraryNonFatalException(message);
		}
		logging.info(String.format("Text field '%s' contains text '%s'.", locator, text));
	}

    @RobotKeyword("Verify the text field identified by ``locator`` does not contain ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for input fields are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textfieldShouldNotContain(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
		String actual = element.getValue(locator, "text field");
		if (actual.contains(text)) {
			if (message == null) {
				message = String.format("Text field '%s' should not have contained text '%s', but was '%s'", locator,
						text, actual);
			}
			throw new SeleniumLibraryNonFatalException(message);
		}
		logging.info(String.format("Text field '%s' contains text '%s'.", locator, text));
	}

    @RobotKeyword("Verify the text area identified by ``locator`` contains ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for text areas are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textareaShouldContain(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
		String actual = element.getValue(locator, "text area");
		if (!actual.contains(text)) {
			if (message == null) {
				message = String.format("Text area '%s' should have contained text '%s', but was '%s'", locator, text,
						actual);
			}
			throw new SeleniumLibraryNonFatalException(message);
		}
		logging.info(String.format("Text field '%s' contains text '%s'.", locator, text));
	}

    @RobotKeyword("Verify the text area identified by ``locator`` does not contain ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for text areas are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textareaShouldNotContain(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
		String actual = element.getValue(locator, "text area");
		if (!actual.contains(text)) {
			if (message == null) {
				message = String.format("Text area '%s' should not have contained text '%s', but was '%s'", locator, text,
						actual);
			}
			throw new SeleniumLibraryNonFatalException(message);
		}
		logging.info(String.format("Text field '%s' contains text '%s'.", locator, text));
	}

    @RobotKeyword("Verify the text area identified by ``locator`` is exactly ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for text areas are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textareaValueShouldBe(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
		String actual = element.getValue(locator, "text area");
		if (!actual.contains(text)) {
			if (message == null) {
				message = String.format("Value of text area '%s' should have been '%s' but was '%s'", locator, text,
						actual);
			}
			throw new SeleniumLibraryNonFatalException(message);
		}
		logging.info(String.format("Content of text area '%s' is '%s'.", locator, text));
	}

    @RobotKeyword("Verify the text area identified by ``locator`` is not exactly ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for text areas are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "text", "message=NONE" })
	public void textareaValueShouldNotBe(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
		String actual = element.getValue(locator, "text area");
		if (actual.contains(text)) {
			if (message == null) {
				message = String.format("Value of text area '%s' should not have been '%s' but was '%s'", locator,
						text, actual);
			}
			throw new SeleniumLibraryNonFatalException(message);
		}
		logging.info(String.format("Content of text area '%s' is '%s'.", locator, text));
	}

    @RobotKeyword("Click on the button identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Key attributes for buttons are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator" })
	public void clickButton(String locator) {
		logging.info(String.format("Clicking button '%s'.", locator));
		List<WebElement> elements = element.elementFind(locator, true, false, "input");
		if (elements.size() == 0) {
			elements = element.elementFind(locator, true, true, "button");
		}
		elements.get(0).click();
	}

    @RobotKeyword("Verify the button identified by ``locator`` is found on the current page.\r\n" + 
            "\r\n" + 
            "Key attributes for buttons are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldContainButton(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		try {
			element.pageShouldContainElement(locator, "input", message, logLevel);
		} catch (SeleniumLibraryNonFatalException e) {
			element.pageShouldContainElement(locator, "button", message, logLevel);
		}
	}

    @RobotKeyword("Verify the button identified by ``locator`` is not found on the current page.\r\n" + 
            "\r\n" + 
            "Key attributes for buttons are id and name. See `Introduction` for details about locators.") 
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldNotContainButton(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		element.pageShouldNotContainElement(locator, "input", message, logLevel);
		element.pageShouldNotContainElement(locator, "button", message, logLevel);
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected WebElement getCheckbox(String locator) {
		return element.elementFind(locator, true, true, "input").get(0);
	}

	protected List<WebElement> getRadioButtons(String groupName) {
		String xpath = String.format("xpath=//input[@type='radio' and @name='%s']", groupName);
		logging.debug("Radio group locator: " + xpath);
		return element.elementFind(xpath, false, true);
	}

	protected WebElement getRadioButtonWithValue(String groupName, String value) {
		String xpath = String.format("xpath=//input[@type='radio' and @name='%s' and (@value='%s' or @id='%s')]",
				groupName, value, value);
		logging.debug("Radio group locator: " + xpath);
		return element.elementFind(xpath, true, true).get(0);
	}

	protected String getValueFromRadioButtons(List<WebElement> elements) {
		for (WebElement element : elements) {
			if (element.isSelected()) {
				return element.getAttribute("value");
			}
		}
		return null;
	}

	protected void inputTextIntoTextField(String locator, String text) {
		WebElement webElement = element.elementFind(locator, true, true).get(0);
		webElement.clear();
		webElement.sendKeys(text);
	}

	protected boolean isFormElement(WebElement element) {
		if (element == null) {
			return false;
		}
		String tag = element.getTagName().toLowerCase();
		return "input".equals(tag) || "select".equals(tag) || "textarea".equals(tag) || "button".equals(tag) || "option".equals(tag);
	}

}
