package com.github.markusbernhardt.seleniumlibrary.keywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

    /**
     * Sort list of strings.
     *
     * @param jsonStringOfList  json string of list strings
     * @param params            sorting order
     * @return sorting list of strings
     */
    @RobotKeyword("Returns sorting list of strings by ``order``.")
    @ArgumentNames({"list", "order=ascending"})
    public List<String> sortStrings(String jsonStringOfList, String... params) {
        List<String> listOfStrings = robot.parseRobotList(jsonStringOfList);
        String order = robot.getParamsValue(params, 0, ASCENDING);
        List<String> sortedList = new ArrayList<>(listOfStrings);
        if (order.equalsIgnoreCase(DESCENDING)) {
            listOfStrings.sort(Collections.reverseOrder());
        } else {
            Collections.sort(listOfStrings);
        }
        logging.info(String.format("Sorted list '%s' by %s", sortedList, order.toUpperCase(Locale.ENGLISH)));
        return sortedList;
    }
}
