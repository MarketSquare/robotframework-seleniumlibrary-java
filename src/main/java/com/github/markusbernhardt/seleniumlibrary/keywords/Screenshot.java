package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.utils.Robotframework;

@RobotKeywords
public class Screenshot extends RunOnFailureKeywordsAdapter {

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
	
	@Autowired Robot robot;
	
	private static File screenshotDir = null;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeyword("Sets the directory for captured screenshots.\r\n" + 
	        "\r\n" + 
	        "``path`` argument specifies the absolute path to a directory where the screenshots should be written to. If the directory does not exist, it will be created. The directory can also be set when `importing` the library. If it is not configured anywhere, screenshots are saved to the same directory where Robot Framework's log file is written.\r\n" + 
	        "\r\n" + 
	        "The previous value is returned and can be used to restorethe original value later if needed.")
	@ArgumentNames({"path"})
	public String setScreenshotDirectory(String path) {
	    String oldDir = screenshotDir != null ? screenshotDir.getAbsolutePath() : logging.getLogDir().getAbsolutePath();
	    screenshotDir = new File(path);
	    return oldDir;
	}
	
	@RobotKeyword("Take a screenshot of the current page and embed it into the log.\r\n" + 
	        "\r\n" + 
	        "The ``filename`` argument specifies the name of the file to write the screenshot into. If no filename is given, the screenshot is saved into file selenium-screenshot-<counter>.png under the directory where the Robot Framework log file is written into. The filename is also considered relative to the same directory, if it is not given in absolute format.\r\n" + 
	        "\r\n" + 
	        "A CSS can be used to modify how the screenshot is taken. By default the background color is changed to avoid possible problems with background leaking when the page layout is somehow broken.")
	@ArgumentNames({ "filename=NONE" })
	public void capturePageScreenshot(String...params) {
	    String filename = robot.getParamsValue(params, 0, null);
		File logdir = screenshotDir != null ? screenshotDir : logging.getLogDir();
		File path = new File(logdir, normalizeFilename(filename));
		String link = Robotframework.getLinkPath(path, logdir);
		WebDriver currentWebDriver = browserManagement.getCurrentWebDriver();

		if (currentWebDriver.getClass().toString().contains("HtmlUnit")) {
		    logging.warn("HTMLunit is not supporting screenshots.");
		    return;
		} else {
		    try {
		        TakesScreenshot takesScreenshot = ((TakesScreenshot) currentWebDriver);
		        byte[] png = takesScreenshot.getScreenshotAs(OutputType.BYTES);
	            writeScreenshot(path, png);
	    
	            logging.html(String.format(
	                    "</td></tr><tr><td colspan=\"3\"><a href=\"%s\"><img src=\"%s\" width=\"800px\"></a>", link, link));
		    } catch (NullPointerException e) {
	            logging.warn("Can't take screenshot. No open browser found");
                return;    
	        }    	
		}
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected int screenshotIndex = 0;

	protected void writeScreenshot(File path, byte[] png) {
		FileOutputStream fos = null;
		try {
		    path.getParentFile().mkdirs();
			fos = new FileOutputStream(path);
			fos.write(png);
			fos.flush();
		} catch (IOException e) {
			logging.warn(String.format("Can't write screenshot '%s'", path.getAbsolutePath()));
			logging.debug(ExceptionUtils.getStackTrace(e));
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logging.warn("Can't even close stream");
				}
			}
		}
	}

	protected String normalizeFilename(String filename) {
		if (filename == null) {
			screenshotIndex++;
			filename = String.format("selenium-screenshot-%d.png", screenshotIndex);
		} else if(filename.contains("{index}")) {
			screenshotIndex++;
			filename = filename.replace("{index}", "%d");
			filename = String.format(filename, screenshotIndex);
		} else {
			filename = filename.replace('/', File.separatorChar);
		}
		return filename;
	}

}
