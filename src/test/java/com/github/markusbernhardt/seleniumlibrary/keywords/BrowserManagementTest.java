package com.github.markusbernhardt.seleniumlibrary.keywords;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class BrowserManagementTest {
    
    private static BrowserManagement bm;
    
    @BeforeClass
    public static void initialize() {
        bm = new BrowserManagement();
        bm.logging = new Logging();
    }

    @Test
    public void testOpenBrowser() {
        BrowserManagement bm = mock(BrowserManagement.class);
        FirefoxDriver wdMock = mock(FirefoxDriver.class);
        String desired = "{\"platform\":\"WINDOWS\",\"browserName\":\"firefox\",\"version\":\"\"}";
        String[] arguments = { "unitTestUrl", "desiredCapabilities=" + desired, "browserOptions={}" };
        try {
            when(bm.createLocalWebDriver(eq("firefox"), any(DesiredCapabilities.class))).thenReturn(wdMock);
            bm.openBrowser("about:blank", arguments);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateDesiredCapabilities() {
        String desired = "{\"platform\":\"WINDOWS\",\"browserName\":\"firefox\",\"version\":\"\"}";
        String browserOptions = "{\"preferences\": {\"network.proxy.type\": 1, \"network.proxy.http\": \"localhost\", \"network.proxy.http_port\": 73571}}";
        Capabilities dc = bm.createCapabilities("firefox", desired, browserOptions);
        FirefoxProfile profile = (FirefoxProfile) dc.getCapability("firefox_profile");
        assertTrue(dc.getCapability("platform").toString().equals("WINDOWS"));
        assertTrue(profile.getStringPreference("network.proxy.http", "wrong") != "wrong");
    }
    
    @Test
    public void testCreateDesiredCapabilitiesWithoutBrowserOptions() {
        String desired = "{\"platform\":\"WINDOWS\",\"browserName\":\"firefox\",\"version\":\"\"}";
        Capabilities dc = bm.createCapabilities("firefox", desired, null);
        FirefoxProfile profile = (FirefoxProfile) dc.getCapability("firefox_profile");
        assertTrue(dc.getCapability("platform").toString().equals("WINDOWS"));
    }
    
    @Test
    public void testCreateDesiredCapabilitiesWithOnlyBrowserOptions() {
        String browserOptions = "{\"preferences\": {\"network.proxy.type\": 1, \"network.proxy.http\": \"localhost\", \"network.proxy.http_port\": 73571}}";
        Capabilities dc = bm.createCapabilities("firefox", null, browserOptions);
        FirefoxProfile profile = (FirefoxProfile) dc.getCapability("firefox_profile");
        assertTrue(profile.getStringPreference("network.proxy.http", "wrong") != "wrong");
    }
    
    @Test
    public void parseChromeBrowserOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        String browserOptions = "{\"args\":[\"start-maximized\"],\"extensions\":[],\"prefs\":{\"intl.accept_languages\":\"de-AT\"}}";
        bm.parseBrowserOptionsChrome(browserOptions, chromeOptions);
        assertTrue(chromeOptions.getCapability("goog:chromeOptions").toString().contains("de-AT"));
    }
}
