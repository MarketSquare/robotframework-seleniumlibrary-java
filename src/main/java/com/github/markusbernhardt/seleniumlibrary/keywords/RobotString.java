package com.github.markusbernhardt.seleniumlibrary.keywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import org.apache.commons.lang3.StringUtils;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class RobotString extends RunOnFailureKeywordsAdapter {

    /**
     * Instantiated Logging keyword bean
     */
    @Autowired
    protected Logging logging;

    @RobotKeyword("Set System Property")
    @ArgumentNames({"key", "value"})
    public void setSystemProperty(String key, String value) {
        System.setProperty(key, value);
    }

    @RobotKeyword("Get System Property")
    @ArgumentNames({"key"})
    public String getSystemProperty(String key) {
        return System.getProperty(key);
    }

    @RobotKeyword("Is Contain String Ignore Case")
    @ArgumentNames({"str", "searchStr"})
    public boolean isContainStringIgnoreCase(String str, String searchStr) {
        logging.info(String.format("Is Contain String Ignore Case: Value - '%s'; String - '%s'", str, searchStr));
        return StringUtils.containsIgnoreCase(str, searchStr);
    }
}
