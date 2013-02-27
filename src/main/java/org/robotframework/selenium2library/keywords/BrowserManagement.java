package org.robotframework.selenium2library.keywords;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.robotframework.selenium2library.locators.WindowManager;
import org.robotframework.selenium2library.utils.Robotframework;
import org.robotframework.selenium2library.utils.WebDriverCache;

import com.opera.core.systems.OperaDriver;

public abstract class BrowserManagement extends Robotframework {

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

	// =================================================================
	// SECTION: BROWSERMANAGEMENT - PUBLIC KEYWORDS
	// =================================================================

	public void closeBrowser() {
		if (webDriverCache.getCurrentSessionId() != null) {
			debug(String.format("Closing browser with session id %s",
					webDriverCache.getCurrentSessionId()));
			webDriverCache.close();
		}
	}

	public String openBrowser(String url) throws Throwable {
		return openBrowser(url, "firefox");
	}

	public String openBrowser(String url, String browserName) throws Throwable {
		return openBrowser(url, browserName, null);
	}

	public String openBrowser(String url, String browserName, String alias)
			throws Throwable {
		return openBrowser(url, browserName, alias, null);
	}

	public String openBrowser(String url, String browserName, String alias,
			String remoteUrl) throws Throwable {
		return openBrowser(url, browserName, alias, remoteUrl, null);
	}

	public String openBrowser(String url, String browserName, String alias,
			String remoteUrl, String desiredCapabilities) throws Throwable {
		return openBrowser(url, browserName, alias, remoteUrl,
				desiredCapabilities, null);
	}

	public String openBrowser(String url, String browserName, String alias,
			String remoteUrl, String desiredCapabilities, String ffProfileDir)
			throws Throwable {
		try {
			info("browserName: " + browserName);
			if (remoteUrl != null) {
				info(String
						.format("Opening browser '%s' to base url '%s' through remote server at '%s'",
								browserName, url, remoteUrl));
			} else {
				info(String.format("Opening browser '%s' to base url '%s'",
						browserName, url));
			}

			WebDriver webDriver = createWebDriver(browserName,
					desiredCapabilities, ffProfileDir, remoteUrl);
			webDriver.get(url);
			String sessionId = webDriverCache.register(webDriver, alias);
			debug(String.format("Opened browser with session id %s", sessionId));
			return sessionId;
		} catch (Throwable t) {
			if (remoteUrl != null) {
				warn(String
						.format("Opening browser '%s' to base url '%s' through remote server at '%s' failed",
								browserName, url, remoteUrl));
			} else {
				warn(String.format(
						"Opening browser '%s' to base url '%s' failed",
						browserName, url));
			}
			throw t;
		}
	}

	public void switchBrowser(String indexOrAlias) {
		try {
			webDriverCache.switchBrowser(indexOrAlias);
			debug(String.format(
					"Switched to browser with Selenium session id %s",
					webDriverCache.getCurrentSessionId()));
		} catch (Throwable t) {
			throw new IllegalArgumentException(String.format(
					"No browser with index or alias '%s' found.", indexOrAlias));
		}
	}

	public void closeAllBrowsers() {
		debug("Closing all browsers");
		webDriverCache.closeAll();
	}

	public void closeWindow() {
		webDriverCache.getCurrent().close();
	}

	public List<String> getWindowIdentifiers() {
		return logList(WindowManager.getWindowIds(webDriverCache.getCurrent()),
				"Window Id");
	}

	public List<String> getWindowNames() {
		List<String> windowNames = WindowManager.getWindowNames(webDriverCache
				.getCurrent());
		if (windowNames.size() != 0 && windowNames.get(0).equals("undefined")) {
			windowNames.set(0, "selenium_main_app_window");
		}
		return logList(windowNames, "Window Name");
	}

	public List<String> getWindowTitles() {
		return logList(
				WindowManager.getWindowTitles(webDriverCache.getCurrent()),
				"Window Title");
	}

	public void maximizeBrowserWindow() {
		webDriverCache.getCurrent().manage().window().maximize();
	}

	public void selectFrame(String locator) {
		info(String.format("Selecting frame '%s'.", locator));
		List<WebElement> elements = elementFind(locator, true, true);
		webDriverCache.getCurrent().switchTo().frame(elements.get(0));
	}

	public void selectWindow() {
		selectWindow(null);
	}

	public void selectWindow(String locator) {
		WindowManager.select(webDriverCache.getCurrent(), locator);
	}

	public void unselectFrame() {
		webDriverCache.getCurrent().switchTo().defaultContent();
	}

	public String getLocation() {
		return webDriverCache.getCurrent().getCurrentUrl();
	}

	public String getSource() {
		return webDriverCache.getCurrent().getPageSource();
	}

	public String getTitle() {
		return webDriverCache.getCurrent().getTitle();
	}

	public void locationShouldBe(String url) {
		String actual = getLocation();
		if (!actual.equals(url)) {
			throw new AssertionError(String.format(
					"Location should have been '%s' but was '%s'", url, actual));
		}
		info(String.format("Current location is '%s'.", url));
	}

	public void locationShouldContain(String url) {
		String actual = getLocation();
		if (!actual.contains(url)) {
			throw new AssertionError(String.format(
					"Location should have been '%s' but was '%s'", url, actual));
		}
		info(String.format("Current location is '%s'.", url));
	}

	public String logLocation() {
		String actual = getLocation();
		info(actual);
		return actual;
	}

	public String logSource() {
		return logSource("INFO");
	}

	public String logSource(String logLevel) {
		String actual = getSource();
		log(actual, logLevel);
		return actual;
	}

	public String logTitle() {
		String actual = getTitle();
		info(actual);
		return actual;
	}

	public void titleShouldBe(String title) {
		String actual = getTitle();
		if (!actual.equals(title)) {
			throw new AssertionError(String.format(
					"Title should have been '%s' but was '%s'", title, actual));
		}
		info(String.format("Page title is '%s'.", title));
	}

	public void goBack() {
		webDriverCache.getCurrent().navigate().back();
	}

	public void goTo(String url) {
		info(String.format("Opening url '%s'", url));
		webDriverCache.getCurrent().get(url);
	}

	public void reloadPage() {
		webDriverCache.getCurrent().navigate().refresh();
	}

	public String getSeleniumSpeed() {
		return secsToTimestr(0);
	}

	public String getSeleniumTimeout() {
		return secsToTimestr(timeout);
	}

	public String getSeleniumImplicitWait() {
		return secsToTimestr(implicitWait);
	}

	public String setSeleniumSpeed(String timestr) {
		return "0s";
	}

	public String setSeleniumTimeout(String timestr) {
		String oldWait = getSeleniumTimeout();
		timeout = timestrToSecs(timestr);

		for (WebDriver webDriver : webDriverCache.getWebDrivers()) {
			webDriver
					.manage()
					.timeouts()
					.setScriptTimeout((int) (timeout * 1000.0),
							TimeUnit.MILLISECONDS);
		}
		return oldWait;
	}

	public String setSeleniumImplicitWait(String timestr) {
		String oldWait = getSeleniumTimeout();
		implicitWait = timestrToSecs(timestr);

		for (WebDriver webDriver : webDriverCache.getWebDrivers()) {
			webDriver
					.manage()
					.timeouts()
					.implicitlyWait((int) (implicitWait * 1000.0),
							TimeUnit.MILLISECONDS);
		}
		return oldWait;
	}

	public String setBrowserImplicitWait(String timestr) {
		String oldWait = getSeleniumTimeout();
		implicitWait = timestrToSecs(timestr);
		webDriverCache
				.getCurrent()
				.manage()
				.timeouts()
				.implicitlyWait((int) (implicitWait * 1000.0),
						TimeUnit.MILLISECONDS);
		return oldWait;
	}

	// =================================================================
	// SECTION: BROWSERMANAGEMENT - PROTECTED HELPERS
	// =================================================================

	protected WebDriver createWebDriver(String browserName,
			String desiredCapabilitiesString, String profileDirectory,
			String remoteUrlString) throws MalformedURLException {
		browserName = browserName.toLowerCase().replace(" ", "");
		DesiredCapabilities desiredCapabilities = createDesiredCapabilities(
				browserName, desiredCapabilitiesString, profileDirectory);

		WebDriver webDriver;
		if (remoteUrlString != null && !"False".equals(remoteUrlString)) {
			webDriver = createRemoteWebDriver(desiredCapabilities, new URL(
					remoteUrlString));
		} else {
			webDriver = createLocalWebDriver(browserName, desiredCapabilities);
		}

		webDriver
				.manage()
				.timeouts()
				.setScriptTimeout((int) (timeout * 1000.0),
						TimeUnit.MILLISECONDS);
		webDriver
				.manage()
				.timeouts()
				.implicitlyWait((int) (implicitWait * 1000.0),
						TimeUnit.MILLISECONDS);

		return webDriver;
	}

	protected WebDriver createLocalWebDriver(String browserName,
			DesiredCapabilities desiredCapabilities) {
		if ("ff".equals(browserName) || "firefox".equals(browserName)) {
			return new FirefoxDriver(desiredCapabilities);

		} else if ("ie".equals(browserName)
				|| "internetexplorer".equals(browserName)) {
			return new InternetExplorerDriver(desiredCapabilities);
		} else if ("gc".equals(browserName) || "chrome".equals(browserName)
				|| "googlechrome".equals(browserName)) {
			return new ChromeDriver(desiredCapabilities);
		} else if ("opera".equals(browserName)) {
			new OperaDriver(desiredCapabilities);
		} else if ("htmlunit".equals(browserName)) {
			return new HtmlUnitDriver(desiredCapabilities);
		} else if ("htmlunitwithjs".equals(browserName)) {
			HtmlUnitDriver driver = new HtmlUnitDriver(desiredCapabilities);
			driver.setJavascriptEnabled(true);
			return driver;
		}

		throw new IllegalArgumentException(browserName
				+ " is not a supported browser.");
	}

	protected WebDriver createRemoteWebDriver(
			DesiredCapabilities desiredCapabilities, URL remoteUrl) {
		return new Augmenter().augment(new RemoteWebDriver(remoteUrl,
				desiredCapabilities));
	}

	protected DesiredCapabilities createDesiredCapabilities(String browserName,
			String desiredCapabilitiesString, String profileDirectory) {
		DesiredCapabilities desiredCapabilities;
		if ("ff".equals(browserName) || "firefox".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.firefox();
			if (profileDirectory != null) {
				FirefoxProfile profile = new FirefoxProfile(new File(
						profileDirectory));
				desiredCapabilities.setCapability(FirefoxDriver.PROFILE,
						profile);
			}
		} else if ("ie".equals(browserName)
				|| "internetexplorer".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.internetExplorer();
		} else if ("gc".equals(browserName) || "chrome".equals(browserName)
				|| "googlechrome".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.chrome();
		} else if ("opera".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.opera();
		} else if ("htmlunit".equals(browserName)
				|| "htmlunitwithjs".equals(browserName)) {
			desiredCapabilities = DesiredCapabilities.htmlUnit();
		} else {
			throw new IllegalArgumentException(browserName
					+ " is not a supported browser.");
		}

		if (desiredCapabilitiesString != null
				&& !"None".equals(desiredCapabilitiesString)) {
			for (String capability : desiredCapabilitiesString.split(",")) {
				String[] keyValue = capability.split(":");
				desiredCapabilities.setCapability(keyValue[0], keyValue[1]);
			}
		}
		return desiredCapabilities;
	}

	// =================================================================
	// SECTION: BROWSERMANAGEMENT - FORWARD DECLARATIONS
	// =================================================================

	protected abstract List<WebElement> elementFind(String locator,
			boolean firstOnly, boolean required);

	protected abstract void log(String msg, String logLevel);

	protected abstract void trace(String msg);

	protected abstract void debug(String msg);

	protected abstract void info(String msg);

	protected abstract void html(String msg);

	protected abstract void warn(String msg);

	protected abstract List<String> logList(List<String> items);

	protected abstract List<String> logList(List<String> items, String what);

	protected abstract File getLogDir();
}