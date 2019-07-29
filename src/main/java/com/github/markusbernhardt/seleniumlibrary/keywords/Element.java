package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;
import com.github.markusbernhardt.seleniumlibrary.locators.ElementFinder;
import com.github.markusbernhardt.seleniumlibrary.utils.Python;

@RobotKeywords
public class Element extends RunOnFailureKeywordsAdapter {

    /**
     * Instantiated BrowserManagement keyword bean
     */
    @Autowired
    protected BrowserManagement browserManagement;

    /**
     * Instantiated FormElement keyword bean
     */
    @Autowired
    protected FormElement formElement;

    /**
     * Instantiated Logging keyword bean
     */
    @Autowired
    protected Logging logging;

    @Autowired
    protected Robot robot;

    // ##############################
    // Keywords - Element Lookups
    // ##############################

    @RobotKeyword("Verify the current frame contains ``text``.\n\r"
            + "\n\r"
            + "See `Introduction` for details about log levels.")
    @ArgumentNames({ "text", "logLevel=INFO" })
    public void currentFrameContains(String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
        if (!isTextPresent(text)) {
            logging.log(String.format("Current Frame Contains: %s => FAILED", text), logLevel);
            throw new SeleniumLibraryNonFatalException(
                    String.format("Page should have contained text '%s', but did not.", text));
        } else {
            logging.log(String.format("Current Frame Contains: %s => OK", text), logLevel);
        }
    }

    @RobotKeyword("Verify the current frame does not contain ``text``.\n\r"
            + "\n\r"
            + "See `Introduction` for details about log levels.")
    @ArgumentNames({ "text", "logLevel=INFO" })
    public void currentFrameShouldNotContain(String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
        if (isTextPresent(text)) {
            logging.log(String.format("Current Frame Should Not Contain: %s => FAILED", text), logLevel);
            throw new SeleniumLibraryNonFatalException(
                    String.format("Page should have not contained text '%s', but did.", text));
        } else {
            logging.log(String.format("Current Frame Should Not Contain: %s => OK", text), logLevel);
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` contains ``text``.\n\r"
            + "\n\r"
            + "See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "text", "message=NONE", "ignore_case=False" })
    public void elementShouldContain(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, StringUtils.EMPTY);
        Boolean ignoreCase = robot.getParamsValue(params, 1, Boolean.FALSE);
        String actual = getText(locator);

        if (!textContains(actual, text, ignoreCase)) {
            if (StringUtils.isEmpty(message)) {
                message = String.format("Element should have contained text '%s', but its text was %s.", text, actual);
            }
            logging.info(String.format("Element Should Contain: %s => FAILED", text));
            throw new SeleniumLibraryNonFatalException(message);
        } else {
            logging.info(String.format("Element Should Contain: %s => OK", text));
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` does not contain ``text``.\r\n"
            + "\r\n"
            + "See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "text", "message=NONE", "ignore_case=False" })
    public void elementShouldNotContain(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, StringUtils.EMPTY);
        Boolean ignoreCase = robot.getParamsValue(params, 1, Boolean.FALSE);
        String actual = getText(locator);

        if (textContains(actual, text, ignoreCase)) {
            if (StringUtils.isEmpty(message)) {
                message = String.format("Element should not have contained text '%s', but its text was %s.", text, actual);
            }
            logging.info(String.format("Element Should Not Contain: %s => FAILED", text));
            throw new SeleniumLibraryNonFatalException(message);
        } else {
            logging.info(String.format("Element Should Not Contain: %s => OK", text));
        }
    }

    @RobotKeyword("Verify the frame identified by ``locator`` contains ``text``.\n\r"
            + "\n\r"
            + "See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "text", "logLevel=INFO" })
    public void frameShouldContain(String locator, String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
        if (!frameContains(locator, text)) {
            logging.log(String.format("Frame Should Contain: %s => FAILED", text), logLevel);
            throw new SeleniumLibraryNonFatalException(
                    String.format("Frame should have contained text '%s', but did not.", text));
        } else {
            logging.log(String.format("Frame Should Contain: %s => OK", text), logLevel);
        }
    }

    @RobotKeyword("Verify the frame identified by ``locator`` does not contain ``text``.\n\r"
            + "\n\r"
            + "See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "text", "logLevel=INFO" })
    public void frameShouldNotContain(String locator, String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
        if (frameContains(locator, text)) {
            logging.log(String.format("Frame Should Not Contain: %s => FAILED", text), logLevel);
            throw new SeleniumLibraryNonFatalException(
                    String.format("Frame should not have contained text '%s', but did.", text));
        } else {
            logging.log(String.format("Frame Should Not Contain: %s => OK", text), logLevel);
        }
    }

    @RobotKeyword("Verify the current page contains ``text``.\n\r"
            + "\n\r"
            + "See `Introduction` for details about log levels.")
    @ArgumentNames({ "text", "logLevel=INFO" })
    public void pageShouldContain(String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
        if (!pageContains(text)) {
            logging.log(String.format("Page Should Contain: %s => FAILED", text), logLevel);
            throw new SeleniumLibraryNonFatalException(
                    String.format("Page should have contained text '%s' but did not.", text));
        } else {
            logging.log(String.format("Page Should Contain: %s => OK", text), logLevel);
        }
    }

    @RobotKeyword("Verify the current page does not contain ``text``.\n\r"
            + "\n\r"
            + "See `Introduction` for details about log levels.")
    @ArgumentNames({ "text", "logLevel=INFO" })
    public void pageShouldNotContain(String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
        if (pageContains(text)) {
            logging.log(String.format("Page Should Not Contain: %s => FAILED", text), logLevel);
            throw new SeleniumLibraryNonFatalException(
                    String.format("Page should not have contained text '%s' but did.", text));
        } else {
            logging.log(String.format("Page Should Not Contain: %s => OK", text), logLevel);
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` is found on the current page\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about log levels and locators.")
    @ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
    public void pageShouldContainElement(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
        pageShouldContainElement(locator, null, message, logLevel);
    }

    protected void pageShouldContainElement(String locator, String tag, String message, String logLevel) {
        String name = tag != null ? tag : "element";
        if (!isElementPresent(locator, tag)) {
            if (message == null || message.equals("")) {
                message = String.format("Page should have contained %s '%s' but did not", name, locator);
            }
            logging.log(message, logLevel);
            throw new SeleniumLibraryNonFatalException(message);
        } else {
            logging.log(String.format("Current page contains %s '%s'.", name, locator), logLevel);
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` is not found on the current page\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about log levels and locators.")
    @ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
    public void pageShouldNotContainElement(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
        pageShouldNotContainElement(locator, null, message, logLevel);
    }

    protected void pageShouldNotContainElement(String locator, String tag, String message, String logLevel) {
        String name = tag != null ? tag : "element";
        if (isElementPresent(locator, tag)) {
            if (message == null || message.equals("")) {
                message = String.format("Page should not have contained %s '%s' but did", name, locator);
            }
            logging.log(message, logLevel);
            throw new SeleniumLibraryNonFatalException(message);
        } else {
            logging.log(String.format("Current page does not contain %s '%s'.", name, locator), logLevel);
        }
    }

    // ##############################
    // Keywords - Attributes
    // ##############################

    @RobotKeyword("Assigns a temporary identifier to the element identified by ``locator``.\r\n" +
            "\r\n" +
            "This is mainly useful, when the locator is a complicated and slow XPath expression. The identifier expires when the page is reloaded.\r\n" +
            "\r\n" +
            "Example:\r\n" +
            " | Assign ID to Element | xpath://div[@id=\\\"first_div\\\"] | my id | \r\n" +
            " | Page Should Contain Element | my id |")
    @ArgumentNames({ "locator", "id" })
    public void assignIdToElement(String locator, String id) {
        logging.info(String.format("Assigning temporary id '%s' to element '%s'", id, locator));
        List<WebElement> elements = elementFind(locator, true, true);

        ((JavascriptExecutor) browserManagement.getCurrentWebDriver())
                .executeScript(String.format("arguments[0].id = '%s';", id), elements.get(0));
    }

    @RobotKeyword("Verify the element identified by ``locator`` is enabled\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about log levels and locators.")
    @ArgumentNames({ "locator" })
    public void elementShouldBeEnabled(String locator) {
        if (!isEnabled(locator)) {
            throw new SeleniumLibraryNonFatalException(String.format("Element %s is disabled.", locator));
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` is disabled.\r\n" +
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void elementShouldBeDisabled(String locator) {
        if (isEnabled(locator)) {
            throw new SeleniumLibraryNonFatalException(String.format("Element %s is enabled.", locator));
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` is focused\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about log levels and locators.")
    @ArgumentNames({ "locator" })
    public void elementShouldBeFocused(String locator) {
        if (!isFocused(locator)) {
            throw new SeleniumLibraryNonFatalException(String.format("Element %s is not focused.", locator));
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` is selected.\r\n" +
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldBeSelected(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        logging.info(String.format("Verifying element '%s' is selected.", locator));
        boolean selected = isSelected(locator);

        if (!selected) {
            if (message == null || message.equals("")) {
                message = String.format("Element '%s' should be selected, but it is not.", locator);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` is not selected.\r\n" +
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldNotBeSelected(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        logging.info(String.format("Verifying element '%s' is not selected.", locator));
        boolean selected = isSelected(locator);

        if (selected) {
            if (message == null || message.equals("")) {
                message = String.format("Element '%s' should not be selected, but it is.", locator);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` is visible.\r\n" +
            "\r\n" +
            "Herein, visible means that the element is logically visible, not optically visible in the current browser viewport. For example, an element that carries display:none is not logically visible, so using this keyword on that element would fail.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldBeVisible(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        logging.info(String.format("Verifying element '%s' is visible.", locator));
        boolean visible = isVisible(locator);

        if (!visible) {
            if (message == null || message.equals("")) {
                message = String.format("Element '%s' should be visible, but it is not.", locator);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` is not visible.\r\n" + 
            "\r\n" + 
            "Herein, visible means that the element is logically visible, not optically visible in the current browser viewport. For example, an element that carries display:none is not logically visible, so using this keyword on that element would fail.\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldNotBeVisible(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        logging.info(String.format("Verifying element '%s' is not visible.", locator));
        boolean visible = isVisible(locator);

        if (visible) {
            if (message == null || message.equals("")) {
                message = String.format("Element '%s' should not be visible, but it is.", locator);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }
    }

    @RobotKeyword("Verify the element identified by ``locator`` is clickable.\r\n" + 
            "\r\n" +  
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldBeClickable(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        logging.info(String.format("Verifying element '%s' is clickable.", locator));
        boolean clickable = isClickable(locator);

        if (!clickable) {
            if (message == null || message.equals("")) {
                message = String.format("Element '%s' should be clickable, but it is not.", locator);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }
    }


    @RobotKeyword("Verify the element identified by ``locator`` is not clickable.\r\n" + 
            "\r\n" +  
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "message=NONE" })
    public void elementShouldNotBeClickable(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        logging.info(String.format("Verifying element '%s' is not clickable.", locator));
        boolean clickable = isClickable(locator);

        if (clickable) {
            if (message == null || message.equals("")) {
                message = String.format("Element '%s' should not be clickable, but it is.", locator);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }
    }

    @RobotKeyword("Verify the text of the element identified by ``locator`` is exactly ``text``.\r\n" +
            "\r\n" +
            "In contrast to `Element Should Contain`, this keyword does not try a substring match but an exact match on the element identified by locator.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "text", "message=NONE", "ignore_case=False" })
    public void elementTextShouldBe(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        Boolean ignoreCase = robot.getParamsValue(params, 1, Boolean.FALSE);
        List<WebElement> elements = elementFind(locator, true, true);
        String actual = elements.get(0).getText();
        if (!textIs(actual, text, ignoreCase)) {
            if (StringUtils.isEmpty(message)) {
                message = String.format("The text of element '%s' should have been '%s', but it was '%s'.", locator,
                        text, actual);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }
    }


    @RobotKeyword("Verify the text of the element identified by ``locator`` is not exactly ``text``.\r\n" + 
            "\r\n" + 
            "In contrast to `Element Should Not Contain`, this keyword does not try a substring match but an exact match on the element identified by locator.\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "text", "message=NONE", "ignore_case=False" })
    public void elementTextShouldNotBe(String locator, String text, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        List<WebElement> elements = elementFind(locator, true, true);
        Boolean ignoreCase = robot.getParamsValue(params, 1, Boolean.FALSE);
        String actual = elements.get(0).getText();
        if (textIs(actual, text, ignoreCase)) {
            if (StringUtils.isEmpty(message)) {
                message = String.format("The text of element '%s' should have been '%s', but it was '%s'.", locator,
                        text, actual);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }
    }

    @RobotKeyword("Returns the value of an element attribute.\r\n" +
            "\r\n" +
            "The ``attribute_locator`` consists of element locator followed by an @ sign and attribute name. Example: element_id@class\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.\r\n" +
            "\r\n" +
            "Passing attribute name as part of the locator was removed in SeleniumLibrary 3.2. The explicit attribute argument should be used instead.")
    @ArgumentNames({ "attributeLocator" })
    @Deprecated
    public String getElementAttribute(String attributeLocator) {
        String[] parts = parseAttributeLocator(attributeLocator);
        return getElementAttribute(parts[0], parts[1]);
    }

    @RobotKeyword("Returns value of attribute from element locator.\r\n" +
            "\r\n" +
            "See the `Locating elements` section for details about the locator syntax.\r\n" +
            "\r\n" +
            "Example: ${id}=    Get Element Attribute   css:h1  id\r\n" +
            "\r\n" +
            "Passing attribute name as part of the locator was removed in SeleniumLibrary 3.2. The explicit attribute argument should be used instead.")
    @ArgumentNames({ "locator", "attribute" })
    public String getElementAttribute(String locator, String attribute) {

        List<WebElement> elements = elementFind(locator, true, false);

        if (elements.size() == 0) {
            throw new SeleniumLibraryNonFatalException(String.format("Element '%s' not found.", locator));
        }
        return elements.get(0).getAttribute(attribute);
    }

    @RobotKeyword("Clears the text from element identified by ``locator``.\r\n" +
            "\r\n" +
            "This keyword does not execute any checks on whether or not the clear method has succeeded, so if any subsequent checks are needed, they should be executed using method `Element Text Should Be`.\r\n" +
            "\r\n" +
            "Also, this method will use WebDriver's internal _element.clear()_ method, i.e. it will not send any keypresses, and it will not have any effect whatsoever on elements other than input textfields or input textareas. Clients relying on keypresses should implement their own methods.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void clearElementText(String locator) {
        List<WebElement> elements = elementFind(locator, true, true);

        elements.get(0).clear();
    }

    @RobotKeyword("Returns inner element id by index```` of element identified by ``locator`` which is matched by ``matchid``.\r\n"
            + "\r\n"
            + "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "matchid", "index" })
    public String getInnerElementId(String locator, String matchid, int index) {
        List<WebElement> elements = elementFind(locator, true, true);

        if (elements.size() == 0) {
            throw new SeleniumLibraryNonFatalException(String.format("get Inner element '%s' not found.", locator));
        }

        String xpathId = ".//*[contains(@id," + matchid + ")]";

        List<WebElement> tmpe = elements.get(0).findElements((By.xpath(xpathId)));
        if (tmpe.size() == 0) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("No Inner element '%s' not found by '%s'", locator, matchid));
        }
        String eId = tmpe.get(index).getAttribute("id");

        logging.info(String.format("Found element ID: '%s'.", eId));

        return eId;

    }

    @RobotKeyword("Returns horizontal position of element identified by ``locator``.\r\n" +
            "\r\n" +
            "The position is returned in pixels off the left side of the page, as an integer. Fails if the matching element is not found.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public int getHorizontalPosition(String locator) {
        List<WebElement> elements = elementFind(locator, true, false);

        if (elements.size() == 0) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Could not determine position for '%s'.", locator));
        }

        return elements.get(0).getLocation().getX();
    }

    @RobotKeyword("Returns the value attribute of the element identified by ``locator``..\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public String getValue(String locator) {
        return getValue(locator, null);
    }

    protected String getValue(String locator, String tag) {
        List<WebElement> elements = elementFind(locator, true, false, tag);

        if (elements.size() == 0) {
            return null;
        }

        return elements.get(0).getAttribute("value");
    }

    @RobotKeyword("Returns the text of the element identified by ``locator``..\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public String getText(String locator) {
        List<WebElement> elements = elementFind(locator, true, true);

        if (elements.size() == 0) {
            return null;
        }

        return elements.get(0).getText();
    }

    @RobotKeyword("Returns vertical position of element identified by ``locator``.\r\n" + 
            "\r\n" + 
            "The position is returned in pixels off the left side of the page, as an integer. Fails if the matching element is not found.\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public int getVerticalPosition(String locator) {
        List<WebElement> elements = elementFind(locator, true, false);

        if (elements.size() == 0) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Could not determine position for '%s'.", locator));
        }

        return elements.get(0).getLocation().getY();
    }

    // ##############################
    // Keywords - Mouse Input/Events
    // ##############################

    @RobotKeyword("Click on the element identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void clickElement(String locator) {
        logging.info(String.format("Clicking element '%s'.", locator));
        List<WebElement> elements = elementFind(locator, true, true);

        elements.get(0).click();
    }

    @RobotKeyword("Click on the element identified by locator at the coordinates ``xOffset`` and ``yOffset``.\r\n" + 
            "\r\n" + 
            "The cursor is moved at the center of the element and the to the given x/y offset from that point. Both offsets are specified as negative (left/up) or positive (right/down) number.\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "xOffset", "yOffset" })
    public void clickElementAtCoordinates(String locator, String xOffset, String yOffset) {
        logging.info(String.format("Clicking element '%s'in coordinates '%s', '%s'.", locator, xOffset, yOffset));
        List<WebElement> elements = elementFind(locator, true, true);

        WebElement element = elements.get(0);
        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.moveToElement(element).moveByOffset(Integer.parseInt(xOffset), Integer.parseInt(yOffset)).perform();
    }

    @RobotKeyword("Double-Click on the element identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void doubleClickElement(String locator) {
        logging.info(String.format("Double clicking element '%s'.", locator));

        List<WebElement> elements = elementFind(locator, true, true);
        Actions action = new Actions(browserManagement.getCurrentWebDriver());

        action.doubleClick(elements.get(0)).perform();
    }

    @RobotKeyword("Set the focus to the element identified by ``locator``.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    @Deprecated
    public void focus(String locator) {
        setFocusToElement(locator);
    }

    @RobotKeyword("Set the focus to the element identified by ``locator``.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void setFocusToElement(String locator) {
        List<WebElement> elements = elementFind(locator, true, true);
        ((JavascriptExecutor) browserManagement.getCurrentWebDriver()).executeScript("arguments[0].focus();",
                elements.get(0));
    }

    @RobotKeyword("Drag the element identified by the locator ``source`` and move it on top of the element identified by the locator ``target``.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.\r\n" +
            "\r\n" +
            "Example:\r\n" +
            " | Drag And Drop | elem1 | elem2 | # Move elem1 over elem2 |")
    @ArgumentNames({ "source", "target" })
    public void dragAndDrop(String source, String target) {
        List<WebElement> sourceElements = elementFind(source, true, true);
        List<WebElement> targetElements = elementFind(target, true, true);

        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.dragAndDrop(sourceElements.get(0), targetElements.get(0)).perform();
    }

    @RobotKeyword("Drag the element identified by the locator ``source`` and move it on top of the element identified by the locator ``target``.\r\n" +
            "\r\n" +
            "Both offsets are specified as negative (left/up) or positive (right/down) number.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.\r\n" +
            "\r\n" +
            "Example:\r\n" +
            " | Drag And Drop By Offset | elem1 | 50 | 35 | # Move elem1 50px right and 35px down. |")
    @ArgumentNames({ "source", "xOffset", "yOffset" })
    public void dragAndDropByOffset(String source, int xOffset, int yOffset) {
        List<WebElement> elements = elementFind(source, true, true);

        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.dragAndDropBy(elements.get(0), xOffset, yOffset).perform();
    }

    @RobotKeyword("Simulates pressing the left mouse button on the element identified by ``locator``.\r\n" +
            "\r\n" +
            "The element is pressed without releasing the mouse button.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void mouseDown(String locator) {
        logging.info(String.format("Simulating Mouse Down on element '%s'.", locator));
        List<WebElement> elements = elementFind(locator, true, false);

        if (elements.size() == 0) {
            throw new SeleniumLibraryNonFatalException(String.format("ERROR: Element %s not found.", locator));
        }

        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.clickAndHold(elements.get(0)).perform();
    }

    @RobotKeyword("Simulates moving the mouse away from the element identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void mouseOut(String locator) {
        logging.info(String.format("Simulating Mouse Out on element '%s'.", locator));
        List<WebElement> elements = elementFind(locator, true, false);

        if (elements.size() == 0) {
            throw new SeleniumLibraryNonFatalException(String.format("ERROR: Element %s not found.", locator));
        }

        WebElement element = elements.get(0);
        Dimension size = element.getSize();
        int offsetX = size.getWidth() / 2 + 1;
        int offsetY = size.getHeight() / 2 + 1;

        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.moveToElement(element).moveByOffset(offsetX, offsetY).perform();
    }

    @RobotKeyword("Simulates moving the mouse over the element identified by ``locator``.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void mouseOver(String locator) {
        logging.info(String.format("Simulating Mouse Over on element '%s'.", locator));
        List<WebElement> elements = elementFind(locator, true, false);

        if (elements.size() == 0) {
            throw new SeleniumLibraryNonFatalException(String.format("ERROR: Element %s not found.", locator));
        }

        WebElement element = elements.get(0);
        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.moveToElement(element).perform();
    }

    @RobotKeyword("Simulates releasing the left mouse button on the element identified by ``locator``.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void mouseUp(String locator) {
        logging.info(String.format("Simulating Mouse Up on element '%s'.", locator));
        List<WebElement> elements = elementFind(locator, true, false);

        if (elements.size() == 0) {
            throw new SeleniumLibraryNonFatalException(String.format("ERROR: Element %s not found.", locator));
        }

        WebElement element = elements.get(0);
        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.release(element).perform();
    }

    @RobotKeyword("Opens the context menu on the element identified by ``locator``.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void openContextMenu(String locator) {
        List<WebElement> elements = elementFind(locator, true, true);

        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.contextClick(elements.get(0)).perform();
    }

    @RobotKeyword("Simulates the given ``event`` on the element identified by ``locator``.\r\n" +
            "\r\n" +
            "This keyword is especially useful, when the element has an OnEvent handler that needs to be explicitly invoked.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator", "event" })
    public void simulate(String locator, String event) {
        List<WebElement> elements = elementFind(locator, true, true);
        String script = "element = arguments[0];" + "eventName = arguments[1];" + "if (document.createEventObject) {"
                + "return element.fireEvent('on' + eventName, document.createEventObject());" + "}"
                + "var evt = document.createEvent(\"HTMLEvents\");" + "evt.initEvent(eventName, true, true);"
                + "return !element.dispatchEvent(evt);";

        ((JavascriptExecutor) browserManagement.getCurrentWebDriver()).executeScript(script, elements.get(0), event);
    }

    @RobotKeyword("Simulates pressing key on the element identified by ``locator``.\r\n" +
            "\r\n" +
            "Key is either a single character, or a numerical ASCII code of the key lead by '\\\\'.\r\n" +
            "\r\n" +
            "Key attributes for arbitrary elements are id and name. See `Introduction` for details about locators.\r\n" +
            "\r\n" +
            "Example:\r\n" +
            " | Press Key | text_field | q | # Press 'q' | \r\n" +
            " | Press Key | login_button | \\\\13 | # ASCII code for enter key |")
    @ArgumentNames({ "locator", "key" })
    public void pressKey(String locator, String key) {
        if (key.startsWith("\\") && key.length() > 1) {
            key = mapAsciiKeyCodeToKey(Integer.parseInt(key.substring(1))).toString();
        }
        List<WebElement> element = elementFind(locator, true, true);
        element.get(0).sendKeys(key);
    }

    // ##############################
    // Keywords - Links
    // ##############################


    @RobotKeyword("Click on the link identified by ``locator``.\r\n" +
            "\r\n" +
            "Key attributes for links are id, name, href and link text. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void clickLink(String locator) {
        logging.info(String.format("Clicking link '%s'.", locator));
        List<WebElement> elements = elementFind(locator, true, true, "a");

        elements.get(0).click();
    }

    @RobotKeyword("Returns a list containing ids of all links found in current page.\r\n" +
            "\r\n" +
            "If a link has no id, an empty string will be in the list instead.")
    public ArrayList<String> getAllLinks() {
        ArrayList<String> ret = new ArrayList<String>();

        List<WebElement> elements = elementFind("tag:a", false, false, "a");
        for (WebElement element : elements) {
            ret.add(element.getAttribute("id"));
        }

        return ret;
    }

    @RobotKeyword("Simulates pressing the left mouse button on the link identified by  ``locator``.\r\n" +
            "\r\n" +
            "The element is pressed without releasing the mouse button.\r\n" +
            "\r\n" +
            "Key attributes for links are id, name, href and link text. See `Introduction` for details about locators.\r\n" +
            "\r\n")
    @ArgumentNames({ "locator" })
    public void mouseDownOnLink(String locator) {
        List<WebElement> elements = elementFind(locator, true, true, "link");

        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.clickAndHold(elements.get(0)).perform();
    }

    @RobotKeyword("Verify the link identified by ``locator`` is found on the current page.\r\n" +
            "\r\n" +
            "Key attributes for links are id, name, href and link text. See `Introduction` for details about log levels and locators.")
    @ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
    public void pageShouldContainLink(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
        pageShouldContainElement(locator, "link", message, logLevel);
    }

    @RobotKeyword("Verify the link identified by ``locator`` is not found on the current page.\r\n" +
            "\r\n" +
            "Key attributes for links are id, name, href and link text. See `Introduction` for details about log levels and locators.")
    @ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
    public void pageShouldNotContainLink(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
        pageShouldNotContainElement(locator, "link", message, logLevel);
    }

    // ##############################
    // Keywords - Images
    // ##############################

    @RobotKeyword("Click on the image identified by ``locator``.\r\n" +
            "\r\n" +
            "Key attributes for images are id, src and alt. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void clickImage(String locator) {
        logging.info(String.format("Clicking image '%s'.", locator));

        List<WebElement> elements = elementFind(locator, true, false, "image");

        if (elements.size() == 0) {
            elements = elementFind(locator, true, true, "input");
        }
        WebElement element = elements.get(0);
        element.click();
    }

    @RobotKeyword("Simulates pressing the left mouse button on the image identified by ``locator``.\r\n" +
            "\r\n" +
            "The element is pressed without releasing the mouse button.\r\n" +
            "\r\n" +
            "Key attributes for images are id, src and alt. See `Introduction` for details about locators.")
    @ArgumentNames({ "locator" })
    public void mouseDownOnImage(String locator) {
        List<WebElement> elements = elementFind(locator, true, true, "image");

        Actions action = new Actions(browserManagement.getCurrentWebDriver());
        action.clickAndHold(elements.get(0)).perform();
    }

    @RobotKeyword("Verify the image identified by ``locator`` is found on the current page.\r\n" +
            "\r\n" +
            "Key attributes for images are id, src and alt. See `Introduction` for details about log levels and locators.")
    @ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
    public void pageShouldContainImage(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
        pageShouldContainElement(locator, "image", message, logLevel);
    }

    @RobotKeyword("Verify the image identified by ``locator`` is not found on the current page.\r\n" +
            "\r\n" +
            "Key attributes for images are id, src and alt. See `Introduction` for details about log levels and locators.")
    @ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
    public void pageShouldNotContainImage(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
        pageShouldNotContainElement(locator, "image", message, logLevel);
    }

    // ##############################
    // Keywords - Xpath
    // ##############################

    @RobotKeyword("Returns the number of elements located the given ``xpath``.\r\n" +
            " \r\n" +
            "If you wish to assert the number of located elements, use `Xpath Should Match X Times`.")
    @ArgumentNames({ "xpath" })
    public int getMatchingXpathCount(String xpath) {
        if (!xpath.startsWith("xpath=") && !xpath.startsWith("xpath:")) {
            xpath = "xpath:" + xpath;
        }
        List<WebElement> elements = elementFind(xpath, false, false);

        return elements.size();
    }

    @RobotKeyword("Verify that the page contains the ``expectedXpathCount`` of elements located by the given ``xpath``.")
    @ArgumentNames({ "xpath", "expectedXpathCount", "message=NONE", "logLevel=INFO" })
    public void xpathShouldMatchXTimes(String xpath, int expectedXpathCount, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
        if (!xpath.startsWith("xpath=") && !xpath.startsWith("xpath:")) {
            xpath = "xpath:" + xpath;
        }
        List<WebElement> elements = elementFind(xpath, false, false);
        int actualXpathCount = elements.size();

        if (actualXpathCount != expectedXpathCount) {
            if (message.isEmpty()) {
                message = String.format("Xpath %s should have matched %s times but matched %s times.", xpath,
                        expectedXpathCount, actualXpathCount);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }

        logging.log(String.format("Current page contains %s elements matching '%s'.", actualXpathCount, xpath),
                logLevel);
    }

    @RobotKeyword("Click to element from list elements by locator ``xpath``.")
    @ArgumentNames({"xpath", "index=0", "message=NONE"})
    public void clickElementByIndex(String xpath, String... params) {
        int index = robot.getParamsValue(params, 0, 0);
        String message = robot.getParamsValue(params, 1, "");
        List<WebElement> elements = elementFind(xpath, false, false);
        if (elements.isEmpty()) {
            if (message.isEmpty()) {
                message = String.format("The Element was not found by locator '%s' with index '%d'", xpath, index);
            }
            throw new SeleniumLibraryNonFatalException(message);
        }
        WebElement element = elements.get(index);
        element.click();
    }

    // ##############################
    // Internal Methods
    // ##############################

    protected List<WebElement> elementFind(String locator, boolean firstOnly, boolean required) {
        return elementFind(locator, firstOnly, required, null);
    }

    protected List<WebElement> elementFind(String locator, boolean firstOnly, boolean required, String tag) {
        List<WebElement> elements = ElementFinder.find(browserManagement.getCurrentWebDriver(), locator, tag);

        if (required && elements.size() == 0) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Element locator '%s' did not match any elements.", locator));
        }

        if (firstOnly) {
            if (elements.size() > 1) {
                List<WebElement> tmp = new ArrayList<WebElement>();
                tmp.add(elements.get(0));
                elements = tmp;
            }
        }

        return elements;
    }

    protected boolean frameContains(String locator, String text) {
        WebDriver current = browserManagement.getCurrentWebDriver();
        List<WebElement> elements = elementFind(locator, true, true);

        current.switchTo().frame(elements.get(0));
        logging.info(String.format("Searching for text from frame '%s'.", locator));
        boolean found = isTextPresent(text);
        current.switchTo().defaultContent();

        return found;
    }

    protected boolean isTextPresent(String text) {
        String locator = String.format("xpath://*[contains(., %s)]", escapeXpathValue(text));

        return isElementPresent(locator);
    }

    protected boolean isEnabled(String locator) {
        List<WebElement> elements = elementFind(locator, true, true);
        WebElement element = elements.get(0);

        if (!formElement.isFormElement(element)) {
            throw new SeleniumLibraryNonFatalException(String.format("ERROR: Element %s is not an input.", locator));
        }
        if (!element.isEnabled()) {
            return false;
        }
        String readonly = element.getAttribute("readonly");
        if (readonly != null && (readonly.equals("readonly") || readonly.equals("true"))) {
            return false;
        }

        return true;
    }

    protected boolean isFocused(String locator) {
        List<WebElement> elements = elementFind(locator, true, true);
        WebElement element = elements.get(0);
        WebDriver current = browserManagement.getCurrentWebDriver();
        WebElement focused = current.switchTo().activeElement();
        return element.equals(focused);
    }

    protected boolean isVisible(String locator) {
        List<WebElement> elements = elementFind(locator, true, false);
        if (elements.size() == 0) {
            return false;
        }
        WebElement element = elements.get(0);
        return element.isDisplayed();
    }

    protected boolean isClickable(String locator) {
        List<WebElement> webElements = elementFind(locator, true, false);
        if (webElements.size() == 0) {
            return false;
        }
        WebElement element = webElements.get(0);
        return element.isDisplayed() && element.isEnabled();
    }

    protected boolean isSelected(String locator) {
        List<WebElement> webElements = elementFind(locator, true, false);
        if (webElements.size() == 0) {
            return false;
        }
        WebElement element = webElements.get(0);
        return element.isSelected();
    }

    protected String[] parseAttributeLocator(String attributeLocator) {
        int index = attributeLocator.lastIndexOf('@');
        if (index <= 0) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Attribute locator '%s' does not contain an element locator.", attributeLocator));
        }
        if (index + 1 == attributeLocator.length()) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Attribute locator '%s' does not contain an attribute name.", attributeLocator));
        }
        String[] parts = new String[2];
        parts[0] = attributeLocator.substring(0, index);
        parts[1] = attributeLocator.substring(index + 1);

        return parts;
    }

    protected boolean isElementPresent(String locator) {
        return isElementPresent(locator, null);
    }

    protected boolean isElementPresent(String locator, String tag) {
        return elementFind(locator, true, false, tag).size() != 0;
    }

    protected boolean pageContains(String text) {
        WebDriver current = browserManagement.getCurrentWebDriver();
        current.switchTo().defaultContent();

        if (isTextPresent(text)) {
            return true;
        }

        List<WebElement> elements = elementFind("xpath://frame|//iframe", false, false);
        Iterator<WebElement> it = elements.iterator();
        while (it.hasNext()) {
            current.switchTo().frame(it.next());
            boolean found = isTextPresent(text);
            current.switchTo().defaultContent();
            if (found) {
                return true;
            }
        }

        return false;
    }

    protected CharSequence mapAsciiKeyCodeToKey(int keyCode) {
        switch (keyCode) {
        case 0:
            return Keys.NULL;
        case 8:
            return Keys.BACK_SPACE;
        case 9:
            return Keys.TAB;
        case 10:
            return Keys.RETURN;
        case 13:
            return Keys.ENTER;
        case 24:
            return Keys.CANCEL;
        case 27:
            return Keys.ESCAPE;
        case 32:
            return Keys.SPACE;
        case 42:
            return Keys.MULTIPLY;
        case 43:
            return Keys.ADD;
        case 44:
            return Keys.SEPARATOR;
        case 45:
            return Keys.SUBTRACT;
        case 56:
            return Keys.DECIMAL;
        case 57:
            return Keys.DIVIDE;
        case 59:
            return Keys.SEMICOLON;
        case 61:
            return Keys.EQUALS;
        case 127:
            return Keys.DELETE;
        default:
            return new StringBuffer((char) keyCode);
        }
    }

    public static String escapeXpathValue(String value) {
        if (value.contains("\"") && value.contains("'")) {
            String[] partsWoApos = value.split("'");
            return String.format("concat('%s')", Python.join("', \"'\", '", Arrays.asList(partsWoApos)));
        }
        if (value.contains("'")) {
            return String.format("\"%s\"", value);
        }
        return String.format("'%s'", value);
    }

    private boolean textContains(String actual, String text, boolean ignoreCase) {
        return ignoreCase
                ? StringUtils.containsIgnoreCase(actual, text)
                : StringUtils.contains(actual, text);
    }

	private boolean textIs(String actual, String text, boolean ignoreCase) {
        return ignoreCase
                ? StringUtils.equalsIgnoreCase(actual, text)
                : StringUtils.equals(actual, text);
    }

}
