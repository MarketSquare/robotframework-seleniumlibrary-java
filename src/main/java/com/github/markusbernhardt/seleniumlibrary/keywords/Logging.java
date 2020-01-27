package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;

@RobotKeywords
public class Logging extends RunOnFailureKeywordsAdapter {

    protected final static Map<String, String[]> VALID_LOG_LEVELS;
    protected static String logDir = null;

    static {
        VALID_LOG_LEVELS = new HashMap<>();
        VALID_LOG_LEVELS.put("debug", new String[] { "debug", "" });
        VALID_LOG_LEVELS.put("html", new String[] { "info", ", True, False" });
        VALID_LOG_LEVELS.put("info", new String[] { "info", "" });
        VALID_LOG_LEVELS.put("trace", new String[] { "trace", "" });
        VALID_LOG_LEVELS.put("warn", new String[] { "warn", "" });
        VALID_LOG_LEVELS.put("error", new String[] { "error", "" });
    }

    /**
     * Instantiated BrowserManagement keyword bean
     */
    @Autowired
    protected BrowserManagement browserManagement;
    
    @Autowired 
    protected Window window;
    
    @Autowired
    protected Robot robot;

    // ##############################
    // Keywords
    // ##############################

    @RobotKeyword("Logs and returns the id attributes of all windows known to the current browser instance.")
    @ArgumentNames({ "logLevel=INFO" })
    public List<String> logWindowIdentifiers(String logLevel) {
        List<String> windowIdentifiers = window.getWindowIdentifiers();
        for (String windowIdentifier : windowIdentifiers) {
            log(windowIdentifier, logLevel);
        }
        return windowIdentifiers;
    }

    @RobotKeyword("Logs and returns the names of all windows known to the current browser instance.\r\n" + 
            "\r\n" + 
            "See `Introduction` for details about the ``logLevel``.")
    @ArgumentNames({ "logLevel=INFO" })
    public List<String> logWindowNames(String logLevel) {
        List<String> windowIdentifiers = window.getWindowNames();
        for (String windowIdentifier : windowIdentifiers) {
            log(windowIdentifier, logLevel);
        }
        return windowIdentifiers;
    }

    @RobotKeyword("Logs and returns the titles of all windows known to the current browser instance.\r\n" + 
            "\r\n" + 
            "See `Introduction` for details about the ``logLevel``.")
    @ArgumentNames({ "logLevel=INFO" })
    public List<String> logWindowTitles(String logLevel) {
        List<String> windowIdentifiers = window.getWindowTitles();
        for (String windowIdentifier : windowIdentifiers) {
            log(windowIdentifier, logLevel);
        }
        return windowIdentifiers;
    }
    
    @RobotKeyword("Logs and returns the location of current browser instance.\r\n" + 
            "\r\n" + 
            "See `Introduction` for details about the ``logLevel``.")
    @ArgumentNames({ "logLevel=INFO" })
    public String logLocation(String logLevel) {
        String actual = browserManagement.getLocation();
        log(actual, logLevel);
        return actual;
    }

    @RobotKeyword("Logs and returns the entire html source of the current page or frame.\r\n" + 
            "\r\n" + 
            "See `Introduction` for details about the ``logLevel``.")
    @ArgumentNames({ "logLevel=INFO" })
    public String logSource(String logLevel) {
        String actual = browserManagement.getSource();
        log(actual, logLevel);
        return actual;
    }

    @RobotKeyword("Logs and returns the title of current page.\r\n" + 
            "\r\n" + 
            "See `Introduction` for details about the ``logLevel``.")
    @ArgumentNames({ "logLevel=INFO" })
    public String logTitle(String logLevel) {
        String actual = browserManagement.getTitle();
        log(actual, logLevel);
        return actual;
    }

    @RobotKeyword("Logs and returns basic system information about the execution environment.\r\n" + 
            "\r\n" + 
            "See `Introduction` for details about the ``logLevel``.")
    @ArgumentNames({ "logLevel=INFO" })
    public String logSystemInfo(String logLevel) {
        String actual = browserManagement.getSystemInfo();
        log(actual, logLevel);
        return actual;
    }

    @RobotKeyword("Returns the actually supported capabilities of the remote browser instance.\r\n" + 
            "\r\n" + 
            "Not all server implementations will support every WebDriver feature. Therefore, the client and server should use JSON objects with the properties listed below when describing which features a user requests that a session support. *If a session cannot support a capability that is requested in the desired capabilities, no error is thrown*; a read-only capabilities object is returned that indicates the capabilities the session actually supports. For more information see: [https://github.com/SeleniumHQ/selenium/wiki/DesiredCapabilities|DesiredCapabilities]\r\n" + 
            "\r\n" + 
            "See `Introduction` for details about the ``logLevel``.")
    @ArgumentNames({ "logLevel=INFO" })
    public String logRemoteCapabilities(String logLevel) {
        String actual = browserManagement.getRemoteCapabilities();
        log(actual, logLevel);
        return actual;
    }

    @RobotKeyword("Logs and returns the session id of the remote browser instance.\r\n" + 
            "\r\n" + 
            "See `Introduction` for details about the ``logLevel``.")
    @ArgumentNames({ "logLevel=INFO" })
    public String logRemoteSessionId(String logLevel) {
        String actual = browserManagement.getRemoteSessionId();
        log(actual, logLevel);
        return actual;
    }

    @RobotKeyword("Set the ``logDirectory``, where captured screenshots are stored, to some custom path.\r\n" + 
            "\r\n" + 
            "Fails, if either the given path does not exist, is no directory or is not writable.")
    @ArgumentNames({ "logDirectory" })
    public void setLogDirectory(String logDirectory) throws Exception {
        File file = new File(logDirectory);

        if (file.exists() && file.isDirectory() && file.canWrite()) {
            Logging.setLogDir(file.getAbsolutePath());
        } else {
            throw new Exception(
                    "Location given as parameter: " + logDirectory + " must exist and must be a writeable directory!");
        }
    }

    // ##############################
    // Internal Methods
    // ##############################

    protected void trace(String msg) {
        log(msg, "trace");
    }

    protected void debug(String msg) {
        log(msg, "debug");
    }

    protected void info(String msg) {
        log(msg, "info");
    }

    protected void html(String msg) {
        log(msg, "html");
    }

    protected void warn(String msg) {
        log(msg, "warn");
    }

    protected void error(String msg) {
        log(msg, "error");
    }

    protected void log(String msg, String logLevel) {
        String[] methodParameters = VALID_LOG_LEVELS.get(logLevel.toLowerCase());
        if (methodParameters != null) {
            log0(msg, methodParameters[0], methodParameters[1]);
        } else {
            throw new SeleniumLibraryNonFatalException(String.format("Given log level %s is invalid.", logLevel));
        }
    }

    protected void log0(String msg, String methodName, String methodArguments) {
        msg = String.valueOf(msg);
        if (msg.length() > 1024) {
            // Message is too large.
            // There is a hard limit of 100k in the Jython source code parser
            try {
                // Write message to temp file
                File tempFile = File.createTempFile("SeleniumLibrary-", ".log");
                tempFile.deleteOnExit();
                FileWriter writer = new FileWriter(tempFile);
                writer.write(msg);
                writer.close();

                // Read the message in Python back and log it.
                loggingPythonInterpreter.get()
                        .exec(String.format(
                                "from __future__ import with_statement\n" + "\n" + "with open('%s', 'r') as msg_file:\n"
                                        + "    msg = msg_file.read()\n" + "    logger.%s(msg%s)",
                                tempFile.getAbsolutePath().replace("\\", "\\\\"), methodName, methodArguments));

            } catch (IOException e) {
                throw new SeleniumLibraryNonFatalException("Error in handling temp file for long log message.", e);
            }
        } else {
            // Message is small enough to get parsed by Jython
            loggingPythonInterpreter.get().exec(String.format("logger.%s('%s'%s)", methodName,
                    msg.replace("\\", "\\\\").replace("'", "\\'").replace("\n", "\\n"), methodArguments));
        }
    }

    protected File getLogDir() {
        if (logDir == null
                && !loggingPythonInterpreter.get().eval("EXECUTION_CONTEXTS.current").toString().equals("None")) {
            PyString logDirName = (PyString) loggingPythonInterpreter.get()
                    .eval("BuiltIn().get_variables()['${LOG FILE}']");
            if (logDirName != null && !(logDirName.asString().toUpperCase().equals("NONE"))) {
                return new File(logDirName.asString()).getParentFile();
            }
            logDirName = (PyString) loggingPythonInterpreter.get().eval("BuiltIn().get_variables()['${OUTPUTDIR}']");
            return new File(logDirName.asString()).getParentFile();
        } else {
            return new File(logDir);
        }
    }

    public static void setLogDir(String logDirectory) {
        logDir = logDirectory;
    }

    protected static ThreadLocal<PythonInterpreter> loggingPythonInterpreter = ThreadLocal.withInitial(() -> {
        PythonInterpreter pythonInterpreter = new PythonInterpreter();
        pythonInterpreter.exec(
                "from robot.libraries.BuiltIn import BuiltIn; from robot.running.context import EXECUTION_CONTEXTS; from robot.api import logger;");
        return pythonInterpreter;
    });
}
