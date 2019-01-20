package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.Dimension;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.locators.WindowManager;
import com.github.markusbernhardt.seleniumlibrary.utils.WebDriverCache.SessionIdAliasWebDriverTuple;

@RobotKeywords
public class Window extends RunOnFailureKeywordsAdapter {
    
    @Autowired
    protected BrowserManagement browserManagement;
    
    @Autowired
    protected Logging logging;
    
    @RobotKeyword("Selects the window identified by ``locator`` as the context of actions.\r\n" + 
            "\r\n" + 
            "If the window is found, all subsequent commands use that window, until this keyword is used again. If the window is not found, this keyword fails.\r\n" + 
            "\r\n" + 
            "By default, when a ``locator`` value is provided, it is matched against the title of the window and the javascript name of the window. If multiple windows with same identifier are found, the first one is selected.\r\n" + 
            "\r\n" + 
            "The special locator *MAIN* (default) can be used to select the main window.\r\n" + 
            "\r\n" + 
            "Example:\r\n" + 
            "\r\n" + 
            " | Click Link | popup_link | # opens new window | \r\n" + 
            " | Select Window | popupName |  | \r\n" + 
            " | Title Should Be | Popup Title |  | \r\n" + 
            " | Select Window |  | # Chooses the main window again |\r\n" + 
            "\r\n" + 
            "It is also possible to specify the approach SeleniumLibrary should take to find a window by specifying a locator strategy. See Introduction for details about locators:\r\n" + 
            "\r\n" + 
            " | = Strategy = | = Example = | = Description = | \r\n" + 
            " | title | Select Window | title:My Document | Matches by window title | \r\n" + 
            " | name | Select Window | name:${name} | Matches by window javascript name | \r\n" + 
            " | url | Select Window | url:http://google.com | Matches by window's current URL |")
    @ArgumentNames({ "locator=MAIN" })
    public void selectWindow(String locator) {
        WindowManager.select(browserManagement.getWebDriverCache().getCurrent(), locator);
    }
    
    @RobotKeyword("Closes the currently open pop-up window.")
    public void closeWindow() {
        browserManagement.getWebDriverCache().getCurrent().close();
    }
    
    @RobotKeyword("Returns the id attributes of all windows known to the current browser instance.")
    public List<String> getWindowIdentifiers() {
        return toList(WindowManager.getWindowIds(browserManagement.getWebDriverCache().getCurrent()), "Window Id");
    }
    
    @RobotKeyword("Returns the names of all windows known to the current browser instance.")
    public List<String> getWindowNames() {
        List<String> windowNames = WindowManager.getWindowNames(browserManagement.getWebDriverCache().getCurrent());
        if (windowNames.size() != 0 && windowNames.get(0).equals("undefined")) {
            windowNames.set(0, "selenium_main_app_window");
        }
        return toList(windowNames, "Window Name");
    }
    
    @RobotKeyword("Returns the titles of all windows known to the current browser instance.")
    public List<String> getWindowTitles() {
        return toList(WindowManager.getWindowTitles(browserManagement.getWebDriverCache().getCurrent()), "Window Title");
    }
    
    @RobotKeyword("Returns and logs URLs of all known browser windows.")
    public List<String> getLocations() {
        List<String> locations = new ArrayList<String>();
        for (SessionIdAliasWebDriverTuple sessionIdAliasWebdriverTuple : browserManagement.getWebDriverCache().getWebDrivers()) {
            locations.add(sessionIdAliasWebdriverTuple.webDriver.getCurrentUrl());
        }
        logging.info(ArrayUtils.toString(locations));
        return toList(locations, "Locations");
    }
    
    @RobotKeyword("Maximizes current browser window.")
    public void maximizeBrowserWindow() {
        browserManagement.getWebDriverCache().getCurrent().manage().window().maximize();
    }
    
    @RobotKeyword("Returns current window width and height as integers.\r\n" + 
            "\r\n" + 
            "See also `Set Window Size`.\r\n" + 
            "\r\n" + 
            "Example:\r\n" + 
            "\r\n" + 
            " | ${width} | ${height}= | Get Window Size | ")
    public Object[] getWindowSize() {
        Dimension size = browserManagement.getWebDriverCache().getCurrent().manage().window().getSize();
        return new Object[] { Integer.toString(size.width), Integer.toString(size.height) };
    }
    
    @RobotKeyword("Sets current windows size to given `` width`` and ``height``.\r\n" + 
            "\r\n" + 
            "Values can be given using strings containing numbers or by using actual numbers. See also `Get Window Size`.\r\n" + 
            "\r\n" + 
            "Browsers have a limit how small they can be set. Trying to set them smaller will cause the actual size to be bigger than the requested size.\r\n" + 
            "\r\n" + 
            "Example:\r\n" + 
            "\r\n" + 
            " | Set Window Size | 800 | 600 |")
    @ArgumentNames({ "width", "height" })
    public void setWindowSize(String width, String height) {
        browserManagement.getWebDriverCache().getCurrent().manage().window()
                .setSize(new Dimension(Integer.parseInt(width), Integer.parseInt(height)));
    }
    
    
    protected List<String> toList(List<String> items) {
        return toList(items, "item");
    }

    protected List<String> toList(List<String> items, String what) {
        List<String> msg = new ArrayList<String>();
        msg.add(String.format("Altogether %d %s%s.\n", items.size(), what, items.size() == 1 ? "" : "s"));
        for (int index = 0; index < items.size(); index++) {
            msg.add(String.format("%d: %s", index + 1, items.get(index)));
        }
        return items;
    }

}
