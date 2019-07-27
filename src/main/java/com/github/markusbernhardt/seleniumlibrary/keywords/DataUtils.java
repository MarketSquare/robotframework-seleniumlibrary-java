package com.github.markusbernhardt.seleniumlibrary.keywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RobotKeywords
public class DataUtils extends RunOnFailureKeywordsAdapter {

    private static final String ASCENDING = "ascending";
    private static final String DESCENDING = "descending";

    @Autowired
    protected Robot robot;

    /**
     * Instantiated Logging keyword bean
     */
    @Autowired
    protected Logging logging;

    // ##############################
    // Keywords
    // ##############################

    @RobotKeyword
    @ArgumentNames({"list", "order=ascending"})
    public List<String> sortStringList(String listToString, String... params) {
        List<String> listOfStrings = robot.parseRobotList(listToString);
        String order = robot.getParamsValue(params, 0, ASCENDING);
        List<String> sortedList = new ArrayList<>(listOfStrings);
        if (order.equalsIgnoreCase(DESCENDING)) {
            listOfStrings.sort(Collections.reverseOrder());
        } else {
            Collections.sort(listOfStrings);
        }
        logging.info(String.format("Sorted list '%s' by %s", sortedList, order.toUpperCase()));
        return sortedList;
    }

    @RobotKeyword
    @ArgumentNames({"text", "regExpPattern", "groupIndex"})
    public String getStringByRegexpGroup(String text, String regExpPattern, int groupIndex) {
        Matcher matcher = Pattern.compile(regExpPattern).matcher(text);
        matcher.find();
        String matchedText = matcher.group(groupIndex);
        logging.info(String.format("Matched text '%s' by pattern '%s' and group index '%d' from the text '%s'", matchedText, regExpPattern, groupIndex, text));
        return matchedText;
    }

    @RobotKeyword
    @ArgumentNames({"text", "regExpPattern"})
    public boolean isTextMatchPattern(String text, String regExpPattern) {
        return Pattern.compile(regExpPattern).matcher(text).matches();
    }
}
