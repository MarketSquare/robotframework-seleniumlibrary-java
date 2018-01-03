public class SeleniumLibrary extends com.github.markusbernhardt.seleniumlibrary.SeleniumLibrary {
	public SeleniumLibrary() {
        super("5.0");
    }

    public SeleniumLibrary(String timeout) {
        super(timeout);
    }

    public SeleniumLibrary(String timeout, String implicitWait) {
        super(timeout, implicitWait);
    }

    public SeleniumLibrary(String timeout, String implicitWait, String keywordToRunOnFailure) {
        super(timeout, implicitWait, keywordToRunOnFailure);
    }
    
    public SeleniumLibrary(String timeout, String implicitWait, String keywordToRunOnFailure, String screenshotPath) {
		super(timeout, implicitWait, keywordToRunOnFailure, screenshotPath);
	}
}
