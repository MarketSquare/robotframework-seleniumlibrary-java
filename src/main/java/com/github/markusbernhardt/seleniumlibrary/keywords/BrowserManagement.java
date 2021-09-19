package com.github.markusbernhardt.seleniumlibrary.keywords;

import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.selendroid.client.SelendroidDriver;

import java.io.File;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryFatalException;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;
import com.github.markusbernhardt.seleniumlibrary.locators.ElementFinder;
import com.github.markusbernhardt.seleniumlibrary.utils.Robotframework;
import com.github.markusbernhardt.seleniumlibrary.utils.WebDriverCache;
import com.github.markusbernhardt.seleniumlibrary.utils.WebDriverCache.SessionIdAliasWebDriverTuple;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;

@RobotKeywords
public class BrowserManagement extends RunOnFailureKeywordsAdapter {

    public String remoteWebDriverProxyHost = "";
    public String remoteWebDriverProxyPort = "";
    public String remoteWebDriverProxyUser = "";
    public String remoteWebDriverProxyPassword = "";
    public String remoteWebDriverProxyDomain = "";
    public String remoteWebDriverProxyWorkstation = "";

    /**
     * Cache for all open browsers.
     */
    protected WebDriverCache webDriverCache = new WebDriverCache();

    /**
     * Timeout in milliseconds
     */
    protected double timeout = 5.0;

    /**
     * Implicit wait in milliseconds
     */
    protected double implicitWait = 0;

    /**
     * Instantiated Logging keyword bean
     */
    @Autowired
    protected Logging logging;

    /**
     * Instantiated Element keyword bean
     */
    @Autowired
    protected Element element;

    @Autowired
    private Robot robot;

    // ##############################
    // Getter / Setter
    // ##############################

    public WebDriverCache getWebDriverCache() {
        return webDriverCache;
    }

    public WebDriver getCurrentWebDriver() {
        return webDriverCache.getCurrent();
    }

    public double getTimeout() {
        return timeout;
    }

    // ##############################
    // Keywords
    // ##############################

    @RobotKeyword("Registers a JavaScript function as locator with the specified strategy name.\r\n" +
            "\r\n" +
            "The registered function has to return a WebElement, a List of WebElements or null. Optionally a delimiter can be given to split the value of the locator in multiple arguments when executing the JavaScript function. \r\n" +
            "\r\n" +
            "Example:\r\n" +
            " | Add Location Strategy | byId | return window.document.getElementById(arguments[0]); | \r\n" +
            " | Input Text | byId:firstName | Max |\r\n" +
            "\r\n" +
            "Example with delimiter:\r\n" +
            " | Add Location Strategy | byClassname | return window.document.getElementsByClassName(arguments[0])[arguments[1]]; | , | \r\n" +
            " | Input Text | byClassname:input,3 | Max | ")
    @ArgumentNames({ "strategyName", "functionDefinition", "delimiter=NONE" })
    public void addLocationStrategy(String strategyName, String functionDefinition, String delimiter) {
        ElementFinder.addLocationStrategy(strategyName, functionDefinition, delimiter);
    }

    @RobotKeyword("Closes the current browser instance.")
    public void closeBrowser() {
        if (webDriverCache.getCurrentSessionId() != null) {
            logging.debug(String.format("Closing browser with session id %s", webDriverCache.getCurrentSessionId()));
            webDriverCache.close();
        }
    }

    @RobotKeyword("Opens a new browser instance to the given ``url``.\r\n" +
            "\r\n" +
            "The ``browser`` argument specifies which browser to use, and the supported browser are listed in the table below. The browser names are case-insensitive and some browsers have multiple supported names.\r\n" +
            "|    = Browser =    | = Name(s) =       |\r\n" +
            "| Firefox   | firefox, ff      |\r\n" +
            "| Firefox (headless)   | firefoxheadless, ffheadless      |\r\n" +
            "| Google Chrome     | googlechrome, chrome, gc |\r\n" +
            "| Google Chrome (headless)    | googlechromeheadless, chromeheadless, gcheadless |\r\n" + 
            "| Internet Explorer | internetexplorer, ie     |\r\n" + 
            "| Edge      | edge      |\r\n" + 
            "| Safari    | safari    |\r\n" + 
            "| Android   | android   |\r\n" + 
            "| Iphone    | iphone    |\r\n" + 
            "| JBrowser  | jbrowser  |\r\n" + 
            "\r\n" + 
            "To be able to actually use one of these browsers, you need to have a matching Selenium browser driver available. See the [https://github.com/Hi-Fi/robotframework-seleniumlibrary-java#browser-drivers|project documentation] for more details.\r\n" + 
            "\r\n" + 
            "Optional ``alias`` is an alias given for this browser instance and it can be used for switching between browsers. An alternative approach for switching is using an index returned by this keyword. These indices start from 1, are incremented when new browsers are opened, and reset back to 1 when `Close All Browsers` is called. See `Switch Browser` for more information and examples.\r\n" + 
            "\r\n" + 
            "Optional ``remote_url`` is the URL for a remote Selenium server. If you specify a value for a remote, you can also specify ``desired_capabilities`` to configure, for example, a proxy server for Internet Explorer or a browser and operating system when using [http://saucelabs.com|Sauce Labs]. Desired capabilities can be given as a dictionary. [https://github.com/SeleniumHQ/selenium/wiki/Capabilities| Selenium documentation] lists possible capabilities that can be enabled.\r\n" + 
            "\r\n" + 
            "Optional ``ff_profile_dir`` is the path to the Firefox profile directory if you wish to overwrite the default profile Selenium uses. Notice that prior to SeleniumLibrary 3.0, the library contained its own profile that was used by default.\r\n" + 
            "\r\n" + 
            "Examples:\r\n" + 
            "| `Open Browser` | http://example.com | Chrome  |\r\n" + 
            "| `Open Browser` | http://example.com | Firefox | alias=Firefox |\r\n" + 
            "| `Open Browser` | http://example.com | Edge    | remote_url=http://127.0.0.1:4444/wd/hub |\r\n" + 
            "\r\n" + 
            "If the provided configuration options are not enough, it is possible to use `Create Webdriver` to customize browser initialization even more.")
    @ArgumentNames({ "url", "browserName=firefox", "alias=None", "remoteUrl=None", "desiredCapabilities=None",
            "browserOptions=None", "isWebDriverManager=false" })
    public String openBrowser(String url, String browserName, String alias, String remoteUrl, String desiredCapabilities, String browserOptions, boolean isWebDriverManager) {
        if (isWebDriverManager) {
            webDriverManagerSetup(browserName);
        }

        try {
            logging.info("browserName: " + browserName);

            WebDriver webDriver = createWebDriver(browserName, desiredCapabilities, remoteUrl, browserOptions);
            webDriver.get(url);
            String sessionId = webDriverCache.register(webDriver, alias);
            logging.debug(String.format("Opened browser with session id %s", sessionId));
            return sessionId;
        } catch (Throwable t) {
            if (remoteUrl != null && !remoteUrl.equalsIgnoreCase("FALSE") && !remoteUrl.equalsIgnoreCase("NONE")) {
                logging.warn(String.format("Opening browser '%s' to base url '%s' through remote server at '%s' failed",
                        browserName, url, remoteUrl));
            } else {
                logging.warn(String.format("Opening browser '%s' to base url '%s' failed", browserName, url));
            }
            throw new SeleniumLibraryFatalException(t);
        }
    }

    @RobotKeyword("Switches between active browser instances using an index or an alias.\r\n" +
            "\r\n" +
            "The index is returned from Open Browser and an alias can be given to it.\r\n" +
            "\r\n" +
            "Example:\r\n" +
            "\r\n" +
            " | Open Browser | http://google.com | ff |  | \r\n" +
            " | Location Should Be | http://google.com |  |  | \r\n" +
            " | Open Browser | http://yahoo.com | ie | 2nd conn | \r\n" +
            " | Location Should Be | http://yahoo.com |  |  | \r\n" +
            " | Switch Browser | 1 | # index |  | \r\n" +
            " | Page Should Contain | I'm feeling lucky |  |  | \r\n" +
            " | Switch Browser | 2nd conn | # alias |  | \r\n" +
            " | Page Should Contain | More Yahoo! |  |  | \r\n" +
            " | Close All Browsers |  |  | \r\n" +
            "\r\n" +
            "The above example expects that there was no other open browsers when opening the first one because it used index '1' when switching to it later. If you aren't sure about that you can store the index into a variable as below.\r\n" +
            "\r\n" +
            " | ${id} = | Open Browser | http://google.com | \r\n" +
            " | # Do something ... |  |  | \r\n" +
            " | Switch Browser | ${id} |")
    @ArgumentNames({ "indexOrAlias" })
    public void switchBrowser(String indexOrAlias) {
        try {
            webDriverCache.switchBrowser(indexOrAlias);
            logging.debug(String.format("Switched to browser with Selenium session id %s",
                    webDriverCache.getCurrentSessionId()));
        } catch (Throwable t) {
            throw new SeleniumLibraryFatalException(
                    String.format("No browser with index or alias '%s' found.", indexOrAlias));
        }
    }

    @RobotKeyword("Closes all open browsers and resets the browser cache.\r\n" +
            "\r\n" +
            "After this keyword new indexes returned from `Open Browser` keyword are reset to 1.\r\n" +
            "\r\n" +
            "This keyword should be used in test or suite teardown to make sure all browsers are closed.")
    public void closeAllBrowsers() {
        logging.debug("Closing all browsers");
        webDriverCache.closeAll();
    }


    @RobotKeyword("Sets frame identified by ``locator`` as the current frame.\r\n" +
            "\r\n" +
            "See the `Locating elements` section for details about the locator syntax.\r\n" +
            "\r\n" +
            "Works both with frames and iframes. Use `Unselect Frame` to cancel the frame selection and return to the main frame.\r\n" +
            "\r\n" +
            "Example:\r\n" +
            "\r\n" +
            " | Select Frame | top-frame | # Select frame with id or name 'top-frame' | \r\n" +
            " | Click Link | example | # Click link 'example' in the selected frame | \r\n" +
            " | Unselect Frame |  | # Back to main frame. | \r\n" +
            " | Select Frame | //iframe[@name='xxx'] | # Select frame using xpath | ")
    @ArgumentNames({ "locator" })
    public void selectFrame(String locator) {
        logging.info(String.format("Selecting frame '%s'.", locator));
        List<WebElement> elements = element.elementFind(locator, true, true);
        webDriverCache.getCurrent().switchTo().frame(elements.get(0));
    }

    @RobotKeyword("Selects the top frame as the current frame.")
    public void unselectFrame() {
        webDriverCache.getCurrent().switchTo().defaultContent();
    }

    @RobotKeyword("Returns the current browser URL.")
    public String getLocation() {
        return webDriverCache.getCurrent().getCurrentUrl();
    }

    @RobotKeyword("Returns the entire HTML source of the current page or frame.")
    public String getSource() {
        return webDriverCache.getCurrent().getPageSource();
    }

    @RobotKeyword("Returns the title of current page.")
    public String getTitle() {
        return webDriverCache.getCurrent().getTitle();
    }

    @RobotKeyword("     * Returns the actually supported capabilities of the remote browser instance.\r\n" +
            "     * \r\n" +
            "Not all server implementations will support every WebDriver feature. " +
            "Therefore, the client and server should use JSON objects with the properties " +
            "listed below when describing which features a user requests that a session " +
            "support. <b>If a session cannot support a capability that is requested in the " +
            "desired capabilities, no error is thrown;</b> a read-only capabilities object " +
            "is returned that indicates the capabilities the session actually supports. " +
            "For more information see:" +
            "[http://code.google.com/p/selenium/wiki/DesiredCapabilities|DesiredCapabilities]")
    public String getRemoteCapabilities() {
        // Null returned from jbrowserdriver
        if (getCurrentWebDriver() instanceof RemoteWebDriver
                && ((RemoteWebDriver) getCurrentWebDriver()).getCapabilities() != null) {
            logging.info(String.format("Capabilities supported by current browser:\n%s", getCurrentWebDriver().toString()));
            return ((RemoteWebDriver) getCurrentWebDriver()).getCapabilities().toString();
        } else {
            return "No remote session id or capabilities available";
        }
    }

    @RobotKeyword("Returns the session id of the remote browser instance.")
    public String getRemoteSessionId() {
        if (getCurrentWebDriver() instanceof RemoteWebDriver) {
            return ((RemoteWebDriver) getCurrentWebDriver()).getSessionId().toString();
        } else {
            return "No remote session id";
        }
    }

    @RobotKeyword("Returns basic system information about the execution environment.")
    public String getSystemInfo() {
        return String.format("      os.name: '%s'\n      os.arch: '%s'\n   os.version: '%s'\n java.version: '%s'",
                System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("os.version"),
                System.getProperty("java.version"));
    }

    @RobotKeyword("Verify the current page URL is exactly ``url``.")
    @ArgumentNames({ "url" })
    public void locationShouldBe(String url) {
        String actual = getLocation();
        if (!actual.equals(url)) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Location should have been '%s', but was '%s'", url, actual));
        }
        logging.info(String.format("Current location is '%s'.", url));
    }

    @RobotKeyword(" Verify the current page URL contains ``url``")
    @ArgumentNames({ "url" })
    public void locationShouldContain(String url) {
        String actual = getLocation();
        if (!actual.contains(url)) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Location should have contained '%s', but was '%s'", url, actual));
        }
        logging.info(String.format("Current location is '%s'.", url));
    }

    @RobotKeyword("Verify the current page title is exactly ``title``")
    @ArgumentNames({ "title" })
    public void titleShouldBe(String title) {
        String actual = getTitle();
        if (!actual.equals(title)) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Title should have been '%s', but was '%s'", title, actual));
        }
        logging.info(String.format("Page title is '%s'.", title));
    }

    @RobotKeyword("Verify the current page title is not exactly ``title``.")
    @ArgumentNames({ "title" })
    public void titleShouldNotBe(String title) {
        String actual = getTitle();
        if (actual.equals(title)) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Title should not have been '%s', but was '%s'", title, actual));
        }
        logging.info(String.format("Page title is '%s'.", title));
    }

    @RobotKeyword("Verify the current page title contains ``title``.")
    @ArgumentNames({ "title" })
    public void titleShouldContain(String title) {
        String actual = getTitle();
        if (!actual.contains(title)) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Title should have contained '%s', but was '%s'", title, actual));
        }
        logging.info(String.format("Page title is '%s'.", title));
    }

    @RobotKeyword("Verify the current page title does not contain ``title``.")
    @ArgumentNames({ "title" })
    public void titleShouldNotContain(String title) {
        String actual = getTitle();
        if (actual.contains(title)) {
            throw new SeleniumLibraryNonFatalException(
                    String.format("Title should not have contained '%s', but was '%s'", title, actual));
        }
        logging.info(String.format("Page title is '%s'.", title));
    }

    @RobotKeyword("Simulates the user clicking the \"back\" button on their browser.")
    public void goBack() {
        webDriverCache.getCurrent().navigate().back();
    }

    @RobotKeyword("Navigates the active browser instance to the provided URL.")
    @ArgumentNames({ "url" })
    public void goTo(String url) {
        logging.info(String.format("Opening url '%s'", url));
        webDriverCache.getCurrent().get(url);
    }

    @RobotKeyword("Simulates user reloading page.")
    public void reloadPage() {
        webDriverCache.getCurrent().navigate().refresh();
    }

    @RobotKeyword("*(NOT IMPLEMENTED)*\r\n\r\nReturns the delay in seconds that is waited after each Selenium command.")
    public String getSeleniumSpeed() {
        return Robotframework.secsToTimestr(0);
    }

    @RobotKeyword("*(NOT IMPLEMENTED)* \r\n\r\nSets the delay in seconds that is waited after each Selenium command.")
    @ArgumentNames({ "timestr" })
    public String setSeleniumSpeed(String timestr) {
        return "0s";
    }

    @RobotKeyword("Returns the timeout in seconds that is used by various keywords.")
    public String getSeleniumTimeout() {
        return Robotframework.secsToTimestr(timeout);
    }

    @RobotKeyword("Sets and returns the timeout in seconds that is used by various keywords.\r\n" +
            "\r\n" +
            "There are several Wait ... keywords that take a timeout as an argument. All of these timeout arguments are optional. The timeout used by all of them can be set globally using this keyword. See `Introduction` for more information about timeouts.\r\n" +
            "\r\n" +
            "The previous timeout value is returned by this keyword and can be used to set the old value back later. The default timeout is 5 seconds, but it can be altered in importing the library.\r\n" +
            "\r\n" +
            "Example:\r\n" +
            " | ${orig timeout} = | Set Selenium Timeout | 15 seconds | \r\n" +
            " | # Open page that loads slowly |  |  | \r\n" +
            " | Set Selenium Timeout | ${orig timeout} | # Reset to old value |")
    @ArgumentNames({ "timestr" })
    public String setSeleniumTimeout(String timestr) {
        String oldWait = getSeleniumTimeout();
        timeout = Robotframework.timestrToSecs(timestr);

        for (SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple : webDriverCache.getWebDrivers()) {
            sessionIdAliasWebDriverTuple.webDriver.manage().timeouts().setScriptTimeout((int) (timeout * 1000.0),
                    TimeUnit.MILLISECONDS);
        }
        return oldWait;
    }

    @RobotKeyword("Returns the implicit wait time in seconds that is used by Selenium.")
    public String getSeleniumImplicitWait() {
        return Robotframework.secsToTimestr(implicitWait);
    }

    @RobotKeyword("Sets and returns the implicit wait time in seconds that is used by all Selenium 2 WebDriver instances. This affects all currently open and from now on opened instances.\r\n" +
            "\r\n" +
            "From selenium 2 function: _Sets a sticky timeout to implicitly wait for an element to be found, or a command to complete. This method only needs to be called one time per session._\r\n" +
            "\r\n" +
            "Example:\r\n" +
            " | ${orig wait} = | Set Selenium Implicit Wait | 10 seconds | \r\n" +
            " | # Perform AJAX call that is slow |  |  | \r\n" +
            " | Set Selenium Implicit Wait | ${orig wait} | # Reset to old value |")
    @ArgumentNames({ "timestr" })
    public String setSeleniumImplicitWait(String timestr) {
        String oldWait = getSeleniumTimeout();
        implicitWait = Robotframework.timestrToSecs(timestr);

        for (SessionIdAliasWebDriverTuple sessionIdAliasWebDriverTuple : webDriverCache.getWebDrivers()) {
            sessionIdAliasWebDriverTuple.webDriver.manage().timeouts().implicitlyWait((int) (implicitWait * 1000.0),
                    TimeUnit.MILLISECONDS);
        }
        return oldWait;
    }

    @RobotKeyword("Sets and returns the implicit wait time in seconds that is used by the current Selenium 2 WebDriver instance.\r\n" +
            "\r\n" +
            "From selenium 2 function: _Sets a sticky timeout to implicitly wait for an element to be found, or a command to complete. This method only needs to be called one time per session._\r\n" +
            "\r\n" +
            "Example:\r\n" +
            " | ${orig wait} = | Set Browser Implicit Wait | 10 seconds | \r\n" +
            " | # Perform AJAX call that is slow |  |  | \r\n" +
            " | Set Browser Implicit Wait | ${orig wait} | # Reset to old value |")
    @ArgumentNames({ "timestr" })
    public String setBrowserImplicitWait(String timestr) {
        String oldWait = getSeleniumTimeout();
        implicitWait = Robotframework.timestrToSecs(timestr);
        webDriverCache.getCurrent().manage().timeouts().implicitlyWait((int) (implicitWait * 1000.0),
                TimeUnit.MILLISECONDS);
        return oldWait;
    }

    @RobotKeyword("Configures proxy handling for remote WebDriver instances.\r\n" +
            "\r\n" +
            "This is needed to connect to an external Selenium 2 WebDriver rid through a local HTTP proxy. This implementation handles BASIC, DIGEST and NTLM based authentication schemes correctly.\r\n" +
            "\r\n" +
            "The given configuration will be used for all subsequent calls of `Open Browser`. To remove the proxy call:\r\n" +
            "| Set Remote Web Driver Proxy | ${EMPTY} | ${EMPTY} |\r\n" +
            "\r\n" +
            "Some additional info:\r\n" +
            " - If no ``username`` is provided, it looks for a username at the Java property http.proxyUser and the environment variables HTTP_PROXY and http_proxy. If a username is found, it is only used, if the host and port also match.\r\n" +
            " - If no ``password`` is provided, it looks for a password at the Java property http.proxyUser and the environment variables HTTP_PROXY and http_proxy. If a password is found, it is only used, if the host, port and username also match.\r\n" +
            " - If a ``domain`` is provided, NTLM based authentication is used\r\n" +
            " - If no ``workstation`` is provided and NTLM based authentication is used, the hostname is used as workstation name.")
    @ArgumentNames({ "host", "port", "username=None", "password=None", "domain=None", "workstation=None" })
    public void setRemoteWebDriverProxy(String host, String port, String username, String password, String domain, String workstation) {
        if (host.length() == 0 || port.length() == 0) {
            // No host and port given as proxy
            remoteWebDriverProxyHost = "";
            remoteWebDriverProxyPort = "";
            remoteWebDriverProxyUser = "";
            remoteWebDriverProxyPassword = "";
            remoteWebDriverProxyDomain = "";
            remoteWebDriverProxyWorkstation = "";
            return;
        }

        URL proxyUrl = null;
        try {
            String httpProxy = System.getenv().get("http_proxy");
            if (httpProxy != null) {
                proxyUrl = new URL(httpProxy);
            } else {
                httpProxy = System.getenv().get("HTTP_PROXY");
                if (httpProxy != null) {
                    proxyUrl = new URL(httpProxy);
                }
            }
        } catch (MalformedURLException e) {
            throw new SeleniumLibraryNonFatalException(e.getMessage());
        }

        if (username.length() == 0) {
            // look for a username from properties
            if (System.getProperty("http.proxyHost", "").equals(host)
                    && System.getProperty("http.proxyPort", "").equals(port)) {
                username = System.getProperty("http.proxyUser", "");
            }
            // look for a username from environment
            if (username.length() == 0) {
                if (proxyUrl != null && proxyUrl.getHost().equals(host)
                        && Integer.toString(proxyUrl.getPort()).equals(port)) {
                    username = getUserFromURL(proxyUrl);
                }
            }
        }

        if (password.length() == 0) {
            // look for a password from properties
            if (System.getProperty("http.proxyHost", "").equals(host)
                    && System.getProperty("http.proxyPort", "").equals(port)
                    && System.getProperty("http.proxyUser", "").equals(username)) {
                password = System.getProperty("http.proxyPassword", "");
            }
            // look for a password from environment
            if (password.length() == 0) {
                if (proxyUrl != null && proxyUrl.getHost().equals(host)
                        && Integer.toString(proxyUrl.getPort()).equals(port)
                        && getUserFromURL(proxyUrl).equals(username)) {
                    password = getPasswordFromURL(proxyUrl);
                }
            }
        }

        if (domain.length() != 0 && workstation.length() == 0) {
            try {
                workstation = InetAddress.getLocalHost().getHostName().split("\\.")[0];
            } catch (UnknownHostException e) {
                logging.warn("No workstation name found");
            }
        }

        remoteWebDriverProxyHost = host;
        remoteWebDriverProxyPort = port;
        remoteWebDriverProxyUser = username;
        remoteWebDriverProxyPassword = password;
        remoteWebDriverProxyDomain = domain;
        remoteWebDriverProxyWorkstation = workstation;
    }

    // ##############################
    // Internal Methods
    // ##############################

    protected String getUserFromURL(URL url) {
        String auth = url.getUserInfo();
        int index = auth.indexOf(':');
        if (index == -1) {
            return auth;
        }
        return auth.substring(0, index);
    }

    protected String getPasswordFromURL(URL url) {
        String auth = url.getUserInfo();
        int index = auth.indexOf(':');
        if (index == -1) {
            return "";
        }
        return auth.substring(index + 1);
    }

    protected WebDriver createWebDriver(String browserName, String desiredCapabilitiesString, String remoteUrlString,
            String browserOptions) throws MalformedURLException {
        browserName = browserName.toLowerCase().replace(" ", "");
        Capabilities desiredCapabilities = createCapabilities(browserName, desiredCapabilitiesString,
                browserOptions);

        WebDriver webDriver;
        if (remoteUrlString != null && !remoteUrlString.equalsIgnoreCase("FALSE") && !remoteUrlString.equalsIgnoreCase("NONE")) {
            logging.info(String.format("Opening browser '%s' through remote server at '%s'",
                    browserName, remoteUrlString));
            webDriver = createRemoteWebDriver(desiredCapabilities, new URL(remoteUrlString));
        } else {
            logging.info(String.format("Opening browser '%s'", browserName));
            webDriver = createLocalWebDriver(browserName, desiredCapabilities);
        }

        webDriver.manage().timeouts().setScriptTimeout((int) (timeout * 1000.0), TimeUnit.MILLISECONDS);
        webDriver.manage().timeouts().implicitlyWait((int) (implicitWait * 1000.0), TimeUnit.MILLISECONDS);

        return webDriver;
    }

    protected WebDriver createLocalWebDriver(String browserName, Capabilities desiredCapabilities) {
        switch (browserName.toLowerCase()) {
            case "ff":
            case "firefox":
            case "ffheadless":
            case "firefoxheadless":
                return new FirefoxDriver((FirefoxOptions)desiredCapabilities);
            case "ie":
            case "internetexplorer":
                return new InternetExplorerDriver((InternetExplorerOptions)desiredCapabilities);
            case "edge":
                return new EdgeDriver((EdgeOptions)desiredCapabilities);
            case "gc":
            case "chrome":
            case "googlechrome":
            case "gcheadless":
            case "chromeheadless":
            case "googlechromeheadless":
                return new ChromeDriver((ChromeOptions)desiredCapabilities);
            case "safari":
                return new SafariDriver(new SafariOptions().merge(desiredCapabilities));
            case "jbrowser":
                return new JBrowserDriver(Settings.builder().build());
            case "android":
                try {
                    return new SelendroidDriver(desiredCapabilities);
                } catch (Exception e) {
                    throw new SeleniumLibraryFatalException(e);
                }
            case "ipad":
            case "iphone":
                try {
                    return new IOSDriver<>(new URL(""), desiredCapabilities);
                } catch (Exception e) {
                    throw new SeleniumLibraryFatalException("Creating " + browserName + " instance failed.", e);
                }
            case "htmlunit":
                return new HtmlUnitDriver(desiredCapabilities);
            case "htmlunitwithjs":
                HtmlUnitDriver driver = new HtmlUnitDriver(desiredCapabilities);
                driver.setJavascriptEnabled(true);
                return driver;
            default:
                throw new SeleniumLibraryFatalException(browserName + " is not a supported browser.");
        }
    }

    @RobotKeyword("WebDriver Manager Setup")
    @ArgumentNames({"browserName=firefox"})
    public void webDriverManagerSetup(String browserName) {
        initWebDriver(browserName);
        logging.info(String.format("Init WebDriver Manager for '%s' browser", browserName));
    }

    private void initWebDriver(String browserName) {
        switch (browserName.toLowerCase()) {
            case "ff":
            case "firefox":
            case "ffheadless":
            case "firefoxheadless":
                WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();
                break;
            case "ie":
            case "internetexplorer":
                WebDriverManager.getInstance(DriverManagerType.IEXPLORER).setup();
                break;
            case "edge":
                WebDriverManager.getInstance(DriverManagerType.EDGE).setup();
                break;
            case "gc":
            case "chrome":
            case "googlechrome":
            case "gcheadless":
            case "chromeheadless":
            case "googlechromeheadless":
                WebDriverManager.getInstance(DriverManagerType.CHROME).setup();
                break;
            default:
                throw new SeleniumLibraryFatalException(browserName + " is not a supported browser for WebDriver Manager.");
        }
    }

    protected WebDriver createRemoteWebDriver(Capabilities desiredCapabilities, URL remoteUrl) {
        HttpCommandExecutor httpCommandExecutor = new HttpCommandExecutor(remoteUrl);
        setRemoteWebDriverProxy(httpCommandExecutor);
        return new Augmenter().augment(new RemoteWebDriver(httpCommandExecutor, desiredCapabilities));
    }

    protected Capabilities createCapabilities(String browserName, String desiredCapabilitiesString,
            String browserOptions) {
        MutableCapabilities desiredCapabilities;
        switch (browserName.toLowerCase()) {
        case "ff":
        case "firefox":
            desiredCapabilities = new FirefoxOptions();
            parseBrowserOptionsFirefox(browserOptions, desiredCapabilities);
            break;
        case "ffheadless":
        case "firefoxheadless":
            desiredCapabilities = new FirefoxOptions();
            parseBrowserOptionsFirefox(browserOptions, desiredCapabilities);
            ((FirefoxOptions)desiredCapabilities).setHeadless(true);
            break;
        case "ie":
        case "internetexplorer":
            desiredCapabilities = new InternetExplorerOptions();
            break;
        case "edge":
            desiredCapabilities = new EdgeOptions();
            break;
        case "gc":
        case "chrome":
        case "googlechrome":
            desiredCapabilities = new ChromeOptions();
            logging.debug("Parsing chrome options: "+browserOptions);
            parseBrowserOptionsChrome(browserOptions, desiredCapabilities);
            break;
        case "gcheadless":
        case "chromeheadless":
        case "googlechromeheadless":
            desiredCapabilities = new ChromeOptions();
            logging.debug("Parsing chrome options: "+browserOptions);
            parseBrowserOptionsChrome(browserOptions, desiredCapabilities);
            ((ChromeOptions)desiredCapabilities).setHeadless(true);
            break;
        case "safari":
            desiredCapabilities = new SafariOptions();
            break;
        case "jbrowser":
            desiredCapabilities = new DesiredCapabilities("jbrowser", "1", Platform.ANY);
            break;
        case "htmlunit":
        case "htmlunitwithjs":
            desiredCapabilities = DesiredCapabilities.htmlUnit();
            ((DesiredCapabilities) desiredCapabilities).setBrowserName("htmlunit");
            break;
        default:
            throw new SeleniumLibraryFatalException(browserName + " is not a supported browser.");
        }

        if (desiredCapabilitiesString != null && !"None".equals(desiredCapabilitiesString)) {
            JSONObject jsonObject = (JSONObject) JSONValue.parse(desiredCapabilitiesString);
            if (jsonObject != null) {
                // Valid JSON
                for (Object o : jsonObject.entrySet()) {
                    Entry<?, ?> entry = (Entry<?, ?>) o;
                    desiredCapabilities.setCapability(entry.getKey().toString(), entry.getValue());
                }
            } else {
                // Invalid JSON. Old style key-value pairs
                for (String capability : desiredCapabilitiesString.split(",")) {
                    String[] keyValue = capability.split(":");
                    if (keyValue.length == 2) {
                        desiredCapabilities.setCapability(keyValue[0], keyValue[1]);
                    } else {
                        logging.warn("Invalid desiredCapabilities: " + desiredCapabilitiesString);
                    }
                }
            }
        }
        return desiredCapabilities;
    }

    protected void parseBrowserOptionsChrome(String browserOptions, Capabilities desiredCapabilities) {
        if (browserOptions != null && !"NONE".equalsIgnoreCase(browserOptions)) {
            JSONObject jsonObject = (JSONObject) JSONValue.parse(browserOptions);
            if (jsonObject != null) {
                // Check all properties for translation to ChromeOptions
                for (Object o : jsonObject.keySet()) {
                    String key = (String) o;
                    switch (key) {
                        case "args": {
                            // args is a list of strings
                            List<String> args = new ArrayList<>();
                            for (Object arg : (JSONArray) jsonObject.get(key)) {
                                args.add("--" + arg.toString().replace("--", ""));
                            }
                            ((ChromeOptions) desiredCapabilities).addArguments(args);
                            break;
                        }
                        case "extensions": {
                            List<File> extensions = new ArrayList<>();
                            for (Object extension : (JSONArray) jsonObject.get(key)) {
                                extensions.add(new File(extension.toString().replace('/', File.separatorChar)));
                            }
                            ((ChromeOptions) desiredCapabilities).addExtensions(extensions);
                            break;
                        }
                        case "disable-extensions":
                            // change casing
                            ((ChromeOptions) desiredCapabilities).setExperimentalOption("useAutomationExtension", false);
                            break;
                        default:
                            // all unknonw properties are passed as is
                            ((ChromeOptions) desiredCapabilities).setExperimentalOption(key, jsonObject.get(key));
                            break;
                    }
                }
            } else {
                logging.warn("Invalid browserOptions: " + browserOptions);
            }
        }
    }

    protected void parseBrowserOptionsFirefox(String browserOptions, Capabilities desiredCapabilities) {
        if (browserOptions != null && !"NONE".equalsIgnoreCase(browserOptions)) {
            JSONObject jsonObject = (JSONObject) JSONValue.parse(browserOptions);
            if (jsonObject != null) {
                FirefoxProfile firefoxProfile = new FirefoxProfile();
                for (Object o1 : jsonObject.entrySet()) {
                    Entry<?, ?> entry = (Entry<?, ?>) o1;
                    String key = entry.getKey().toString();
                    if (key.equals("preferences")) {
                        // Preferences
                        JSONObject preferences = (JSONObject) entry.getValue();
                        for (Object o : preferences.entrySet()) {
                            Entry<?, ?> entryPreferences = (Entry<?, ?>) o;
                            Object valuePreferences = entryPreferences.getValue();
                            logging.debug(String.format("Adding property: %s with value: %s",
                                    entryPreferences.getKey().toString(), valuePreferences));
                            if (valuePreferences instanceof Number) {
                                firefoxProfile.setPreference(entryPreferences.getKey().toString(),
                                        ((Number) valuePreferences).intValue());
                            } else if (valuePreferences instanceof Boolean) {
                                firefoxProfile.setPreference(entryPreferences.getKey().toString(),
                                        (Boolean) valuePreferences);
                            } else {
                                firefoxProfile.setPreference(entryPreferences.getKey().toString(),
                                        valuePreferences.toString());
                            }
                        }
                    } else if (key.equals("extensions")) {
                        // Extensions
                        JSONArray extensions = (JSONArray) entry.getValue();
                        for (Object extension : extensions) {
                            File file = new File(extension.toString().replace('/', File.separatorChar));
                            firefoxProfile.addExtension(file);
                        }
                    } else {
                        logging.warn("Unknown browserOption: " + key + ":" + entry.getValue());
                    }
                }
                ((FirefoxOptions) desiredCapabilities).setCapability(FirefoxDriver.PROFILE, firefoxProfile);
            } else {
                logging.warn("Invalid browserOptions: " + browserOptions);
            }
        }
    }

    protected void setRemoteWebDriverProxy(HttpCommandExecutor httpCommandExecutor) {
        if (remoteWebDriverProxyHost.length() == 0) {
            return;
        }

        String fieldName = "<unknown>";
        String className = "<unknown>";
        try {
            // Get access to the client instance
            fieldName = "client";
            className = "DefaultHttpClient";
            Field field = HttpCommandExecutor.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            HttpClientBuilder clientBuilder = HttpClientBuilder.create();
            //DefaultHttpClient client = (DefaultHttpClient) field.get(httpCommandExecutor);

            // set the credentials for the proxy
            AuthScope authScope = new AuthScope(remoteWebDriverProxyHost, Integer.parseInt(remoteWebDriverProxyPort));
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            if (remoteWebDriverProxyDomain.length() == 0) {
                // BASIC Authentication
                credsProvider.setCredentials(authScope,
                        new UsernamePasswordCredentials(remoteWebDriverProxyUser, remoteWebDriverProxyPassword));
                clientBuilder.setDefaultCredentialsProvider(credsProvider);
            } else {
                // NTLM Authentication
            	credsProvider.setCredentials(authScope, new NTCredentials(remoteWebDriverProxyUser,
                        remoteWebDriverProxyPassword, remoteWebDriverProxyWorkstation, remoteWebDriverProxyDomain));
            	clientBuilder.setDefaultCredentialsProvider(credsProvider);
            }

            // Set the RoutePlanner back to something that handles
            // proxies correctly.
            HttpHost proxy = new HttpHost(remoteWebDriverProxyHost, Integer.parseInt(remoteWebDriverProxyPort));
            clientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy));
            clientBuilder.setProxy(proxy);
        } catch (SecurityException e) {
            throw new SeleniumLibraryFatalException(
                    String.format("The SecurityManager does not allow us to lookup to the %s field.", fieldName));
        } catch (NoSuchFieldException e) {
            throw new SeleniumLibraryFatalException(
                    String.format("The RemoteWebDriver dose not declare the %s field any more.", fieldName));
        } catch (IllegalArgumentException e) {
            throw new SeleniumLibraryFatalException(
                    String.format("The field %s does not belong to the given object.", fieldName));
        } catch (ClassCastException e) {
            throw new SeleniumLibraryFatalException(
                    String.format("The %s field does not contain a %s.", fieldName, className));
        }
    }
}
