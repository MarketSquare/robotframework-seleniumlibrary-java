package com.github.markusbernhardt.seleniumlibrary;

import com.github.markusbernhardt.seleniumlibrary.keywords.BrowserManagement;
import com.github.markusbernhardt.seleniumlibrary.keywords.Logging;
import com.github.markusbernhardt.seleniumlibrary.keywords.Robot;
import org.openqa.selenium.WebDriver;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Field;

/**
 * For extends of custom keywords of WebDriver actions
 */
public class CustomRobotDriverElement {

    private static SeleniumLibrary s;
    private static BrowserManagement b;
    protected Robot robot = new Robot();
    protected Logging logging = new Logging();

    public CustomRobotDriverElement() throws NoSuchFieldException, IllegalAccessException {
        try {
            CustomRobotDriverElement.s = getLibraryInstance();
        } catch (ScriptException e) {
            throw new SeleniumLibraryNonFatalException("Cannot create SeleniumLibrary instance.", e);
        }
        Field bmField = SeleniumLibrary.class.getDeclaredField("bm");
        bmField.setAccessible(true);
        b = (BrowserManagement) bmField.get(s);
        bmField.setAccessible(false);
    }

    private static SeleniumLibrary getLibraryInstance() throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("python");
        engine.put("library", "SeleniumLibrary");
        engine.eval("from robot.libraries.BuiltIn import BuiltIn");
        engine.eval("instance = BuiltIn().get_library_instance(library)");
        return (SeleniumLibrary) engine.get("instance");
    }

    protected WebDriver getCurrentBrowser() {
        return b.getCurrentWebDriver();
    }
}
