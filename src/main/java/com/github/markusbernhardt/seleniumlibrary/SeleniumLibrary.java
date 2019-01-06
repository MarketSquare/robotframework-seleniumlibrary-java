package com.github.markusbernhardt.seleniumlibrary;

import java.io.File;
import java.util.ResourceBundle;

import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.library.AnnotationLibrary;

import com.github.markusbernhardt.seleniumlibrary.keywords.BrowserManagement;
import com.github.markusbernhardt.seleniumlibrary.keywords.RunOnFailure;
import com.github.markusbernhardt.seleniumlibrary.keywords.Screenshot;

public class SeleniumLibrary extends AnnotationLibrary {

    /**
     * The list of keyword patterns for the AnnotationLibrary
     */
    public static final String KEYWORD_PATTERN = "com/github/markusbernhardt/seleniumlibrary/keywords/**/*.class";

    /**
     * The scope of this library is global.
     */
    public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

    /**
     * The actual version of this library. Loaded from maven project.
     */
    public static final String ROBOT_LIBRARY_VERSION = loadRobotLibraryVersion();

    private static String loadRobotLibraryVersion() {
        try {
            return ResourceBundle.getBundle(SeleniumLibrary.class.getCanonicalName().replace(".", File.separator))
                    .getString("version");
        } catch (RuntimeException e) {
            return "unknown";
        }
    }

    public SeleniumLibrary() {
        this("5.0");
    }

    public SeleniumLibrary(String timeout) {
        this(timeout, "0.0");
    }

    public SeleniumLibrary(String timeout, String implicitWait) {
        this(timeout, implicitWait, "Capture Page Screenshot");
    }

    public SeleniumLibrary(String timeout, String implicitWait, String keywordToRunOnFailure) {
        this(timeout, implicitWait, keywordToRunOnFailure, "");
    }
    
    public SeleniumLibrary(String timeout, String implicitWait, String keywordToRunOnFailure, String screenshotPath) {
        super();
        addKeywordPattern(KEYWORD_PATTERN);
        //Enable annotations
        createKeywordFactory();
        bm.setSeleniumTimeout(timeout);
        bm.setSeleniumImplicitWait(implicitWait);
        rof.registerKeywordToRunOnFailure(keywordToRunOnFailure);
        if (!screenshotPath.isEmpty()) {
            screenshot.setScreenshotDirectory(screenshotPath);
        }
        
    }

    // ##############################
    // Autowired References
    // ##############################
    
    @Autowired
    BrowserManagement bm;
    
    @Autowired
    RunOnFailure rof;
    
    @Autowired
    Screenshot screenshot;


    @Override
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__")) {
            return "SeleniumLibrary is a web testing library for the Robot Framework and was originally written in Python. This is the Java port of the SeleniumLibrary for Robot Framework (https://github.com/robotframework/SeleniumLibrary). It uses the Selenium libraries internally to control a web browser. See [http://seleniumhq.org/docs/03_webdriver.html|WebDriver] for more information on Selenium (2) and WebDriver. It runs tests in a real browser instance and should work with most modern browsers and can be used with the Jython interpreter or any other Java application.\r\n" + 
                    "\r\n" + 
                    "== Before running tests ==\r\n" + 
                    "\r\n" + 
                    "Prior to running test cases using SeleniumLibrary, the library must be imported into your Robot Framework test suite (see importing section), and the `Open Browser` keyword must be used to open a browser to the desired location.\r\n" + 
                    "\r\n" + 
                    "= Locating elements =\r\n" + 
                    "\r\n" + 
                    "All keywords in SeleniumLibrary that need to find an element on the page take an locator argument.\r\n" + 
                    "\r\n" + 
                    "== Key attributes ==\r\n" + 
                    "\r\n" + 
                    "By default, when a locator value is provided, it is matched against the key attributes of the particular element type. The attributes ``id`` and ``name`` are key attributes to all elements.\r\n" + 
                    "\r\n" + 
                    "List of key attributes:\r\n" + 
                    "\r\n" + 
                    " | = Element Type = | = Key Attributes = | \r\n" + 
                    " | A | @id,@name,@href,text | \r\n" + 
                    " | IMG | @id,@name,@src,@alt | \r\n" + 
                    " | INPUT | @id,@name,@value,@src | \r\n" + 
                    " | BUTTON | @id,@name,@value,text | \r\n" + 
                    " | * | @id,@name | \r\n" + 
                    " \r\n" + 
                    "Example:\r\n" + 
                    " | Click Element | my_element |\r\n" + 
                    "\r\n" + 
                    "== Locator strategies ==\r\n" + 
                    "\r\n" + 
                    "It is also possible to specify the approach SeleniumLibrary should take to find an element by specifying a locator strategy with a locator prefix.\r\n" + 
                    "\r\n" + 
                    "Supported strategies are:\r\n" + 
                    "\r\n" + 
                    " | = Strategy = | = Example = | = Description = | \r\n" + 
                    " | identifier | Click Element | identifier:my_element | Matches by @id or @name attribute | \r\n" + 
                    " | id | Click Element | id:my_element | Matches by @id attribute | \r\n" + 
                    " | name | Click Element | name:my_element | Matches by @name attribute | \r\n" + 
                    " | xpath | Click Element | xpath://div[@id='my_element'] | Matches by arbitrary XPath expression | \r\n" + 
                    " | dom | Click Element | dom:document.images[56] | Matches by arbitrary DOM expression | \r\n" + 
                    " | link | Click Element | link:My Link | Matches by the link text | \r\n" + 
                    " | css | Click Element | css:div.my_class | Matches by CSS selector | \r\n" + 
                    " | jquery | Click Element | jquery:div.my_class | Matches by jQuery/sizzle selector | \r\n" + 
                    " | sizzle | Click Element | sizzle:div.my_class | Matches by jQuery/sizzle selector | \r\n" + 
                    " | tag | Click Element | tag:div | Matches by HTML tag name | \r\n" + 
                    " \r\n" + 
                    "== Locating tables ==\r\n" + 
                    "\r\n" + 
                    "Table related keywords, such as `Table Should Contain`, work differently. By default, when a table locator value is provided, it will search for a table with the specified id attribute.\r\n" + 
                    "\r\n" + 
                    "Example:\r\n" + 
                    "\r\n" + 
                    " | Table Should Contain | my_table | text | \r\n" + 
                    " \r\n" + 
                    "More complex table locator strategies:\r\n" + 
                    "\r\n" + 
                    " | = Strategy = | = Example = | = Description = | \r\n" + 
                    " | xpath | Table Should Contain | xpath://table/[@name=\"my_table\"] | text | Matches by arbitrary XPath expression | \r\n" + 
                    " | css | Table Should Contain | css:table.my_class | text | Matches by CSS selector | \r\n" + 
                    "\r\n" + 
                    "== Custom location strategies ==\r\n" + 
                    "\r\n" + 
                    "It is also possible to register custom location strategies. See `Add Location Strategy` for details about custom location strategies.<br>\r\n" + 
                    "\r\n" + 
                    "Example:\r\n" + 
                    "\r\n" + 
                    " | Add Location Strategy | custom | return window.document.getElementById(arguments[0]); | \r\n" + 
                    " | Input Text | custom=firstName | Max | \r\n" + 
                    " \r\n" + 
                    "= Timeouts =\r\n" + 
                    "\r\n" + 
                    "There are several Wait ... keywords that take ``timeout`` as an argument. All of these timeout arguments are optional. The timeout used by all of them can be set globally using the `Set Selenium Timeout keyword`.\r\n" + 
                    "\r\n" + 
                    "All timeouts can be given as numbers considered seconds (e.g. ``0.5`` or ``42``) or in Robot Framework's time syntax (e.g. ``1.5 seconds`` or ``1 min 30 s``). See [http://robotframework.org/robotframework/latest/RobotFrameworkUserGuide.html#time-format|Robot Framework User Guide] for details about the time syntax.\r\n" + 
                    "\r\n" + 
                    "== Log Level ==\r\n" + 
                    "There are several keywords that take ``log level`` as an argument. All of these log level arguments are optional. The default is usually INFO.\r\n" + 
                    "\r\n" + 
                    " | = Log Level = | = Description = | \r\n" + 
                    " | DEBUG |  | \r\n" + 
                    " | INFO |  | \r\n" + 
                    " | HTML | Same as INFO, but message is in HTML format | \r\n" + 
                    " | TRACE |  | \r\n" + 
                    " | WARN |  | ";
        } else if (keywordName.equals("__init__")) {
            return "SeleniumLibrary can be imported with several optional arguments.\r\n" + 
                    "        - ``timeout``:\r\n" + 
                    "          Default value for `timeouts` used with ``Wait ...`` keywords.\r\n" + 
                    "        - ``implicit_wait``:\r\n" + 
                    "          Default value for `implicit wait` used when locating elements.\r\n" + 
                    "        - ``run_on_failure``:\r\n" + 
                    "          Default action for the `run-on-failure functionality`.\r\n" + 
                    "        - ``screenshot_root_directory``:\r\n" + 
                    "          Location where possible screenshots are created. If not given,\r\n" + 
                    "          the directory where the log file is written is used.";
        } else {
            try {
                return super.getKeywordDocumentation(keywordName);
            }
            catch (Exception e) {
                System.out.println(keywordName);
            }
        }
        return keywordName;

    }
}
