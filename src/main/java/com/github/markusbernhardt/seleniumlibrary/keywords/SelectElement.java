package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;
import com.github.markusbernhardt.seleniumlibrary.utils.Python;

@RobotKeywords
public class SelectElement extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated Element keyword bean
	 */
	@Autowired
	protected Element element;

	/**
	 * Instantiated Logging keyword bean
	 */
	@Autowired
	protected Logging logging;
	
	@Autowired
	protected Robot robot;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeyword("Returns the values in the select list identified by ``locator``.\r\n" + 
	        "\r\n" + 
	        "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public List<String> getListItems(String locator) {
		List<WebElement> options = getSelectListOptions(locator);

		return getLabelsForOptions(options);
	}

	@RobotKeyword("Returns the visible label of the first selected element from the select list identified by ``locator``.\r\n" + 
	        "\r\n" + 
	        "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public String getSelectedListLabel(String locator) {
		Select select = getSelectList(locator);

		return select.getFirstSelectedOption().getText();
	}

    @RobotKeyword("Returns the visible labels of the first selected element from the select list identified by ``locator``.\r\n" + 
            "\r\n" +
            "Fails if there is no selection.\r\n" + 
            "\r\n" + 
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public List<String> getSelectedListLabels(String locator) {
		List<WebElement> options = getSelectListOptionsSelected(locator);

		if (options.size() == 0) {
			throw new SeleniumLibraryNonFatalException(String.format(
					"Select list with locator '%s' does not have any selected values.", locator));
		}

		return getLabelsForOptions(options);
	}

    @RobotKeyword("Returns the value of the first selected element from the select list identified by ``locator``.\r\n" + 
            "\r\n" +
            "The return value is read from the value attribute of the selected element.\r\n" + 
            "\r\n" + 
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public String getSelectedListValue(String locator) {
		Select select = getSelectList(locator);

		return select.getFirstSelectedOption().getAttribute("value");
	}

    @RobotKeyword("Returns the values of the first selected element as a list from the select list identified by ``locator``.\r\n" + 
            "\r\n" +
            "Fails if there is no selection. The return values are read from the value attribute of the selected element.\r\n" + 
            "\r\n" + 
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public List<String> getSelectedListValues(String locator) {
		List<WebElement> options = getSelectListOptionsSelected(locator);

		if (options.size() == 0) {
			throw new SeleniumLibraryNonFatalException(String.format(
					"Select list with locator '%s' does not have any selected values.", locator));
		}

		return getValuesForOptions(options);
	}

    @RobotKeyword("Verify the selection of the select list identified by ``locator``.\r\n" + 
            "\r\n" +
            "If you want to verify no option is selected, simply give no items.\r\n" + 
            "\r\n" + 
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "*items" })
	public void listSelectionShouldBe(String locator, String... items) {
		String itemList = items.length != 0 ? String.format("option(s) [ %s ]", Python.join(" | ", items))
				: "no options";
		logging.info(String.format("Verifying list '%s' has %s selected.", locator, itemList));

		pageShouldContainList(locator);

		List<WebElement> options = getSelectListOptionsSelected(locator);
		List<String> selectedLabels = getLabelsForOptions(options);
		String message = String.format("List '%s' should have had selection [ %s ] but it was [ %s ].", locator,
				Python.join(" | ", items), Python.join(" | ", selectedLabels));
		if (items.length != options.size()) {
			throw new SeleniumLibraryNonFatalException(message);
		} else {
			List<String> selectedValues = getValuesForOptions(options);

			for (String item : items) {
				if (!selectedValues.contains(item) && !selectedLabels.contains(item)) {
					throw new SeleniumLibraryNonFatalException(message);
				}
			}
		}
	}

    @RobotKeyword("Verify the select list identified by ``locator`` has no selections.\r\n" + 
            "\r\n" +
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public void listShouldHaveNoSelections(String locator) {
		logging.info(String.format("Verifying list '%s' has no selection.", locator));

		List<WebElement> options = getSelectListOptionsSelected(locator);
		if (!options.isEmpty()) {
			List<String> selectedLabels = getLabelsForOptions(options);
			String items = Python.join(" | ", selectedLabels);
			throw new SeleniumLibraryNonFatalException(String.format(
					"List '%s' should have had no selection (selection was [ %s ]).", locator, items));
		}
	}

    @RobotKeyword("Verify the select list identified by ``locator`` is found on the current page.\r\n" + 
            "\r\n" +
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldContainList(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		element.pageShouldContainElement(locator, "list", message, logLevel);
	}

    @RobotKeyword("Verify the select list identified by ``locator`` is not found on the current page.\r\n" + 
            "\r\n" +
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "message=NONE", "logLevel=INFO" })
	public void pageShouldNotContainList(String locator, String...params) {
        String message = robot.getParamsValue(params, 0, "");
        String logLevel = robot.getParamsValue(params, 1, "INFO");
		element.pageShouldNotContainElement(locator, "list", message, logLevel);
	}

    @RobotKeyword("Select all values of the multi-select list identified by ``locator``.\r\n" + 
            "\r\n" +
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator" })
	public void selectAllFromList(String locator) {
		logging.info(String.format("Selecting all options from list '%s'.", locator));

		Select select = getSelectList(locator);
		if (!isMultiselectList(select)) {
			throw new SeleniumLibraryNonFatalException(
					"Keyword 'Select all from list' works only for multiselect lists.");
		}

		for (int i = 0; i < select.getOptions().size(); i++) {
			select.selectByIndex(i);
		}
	}

	@RobotKeyword("Select the given ``*items`` of the multi-select list identified by ``locator``.\r\n" + 
	        "\r\n" + 
	        "An exception is raised for a single-selection list if the last value does not exist in the list and a warning for all other non-existing items. For a multi-selection list, an exception is raised for any and all non-existing values.\r\n" + 
	        "\r\n" + 
	        "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "*items" })
	public void selectFromList(String locator, String... items) {
		String itemList = items.length != 0 ? String.format("option(s) [ %s ]", Python.join(" | ", items))
				: "all options";
		logging.info(String.format("Selecting %s from list '%s'.", itemList, locator));

		Select select = getSelectList(locator);

		// If no items given, select all values (of in case of single select
		// list, go through all values)
		if (items.length == 0) {
			for (int i = 0; i < select.getOptions().size(); i++) {
				select.selectByIndex(i);
			}
			return;
		}

		boolean lastItemFound = false;
		List<String> nonExistingItems = new ArrayList<>();
		for (String item : items) {
			lastItemFound = true;
			try {
				select.selectByValue(item);
			} catch (NoSuchElementException e1) {
				try {
					select.selectByVisibleText(item);
				} catch (NoSuchElementException e2) {
					nonExistingItems.add(item);
					lastItemFound = false;
					continue;
				}
			}
		}

		if (nonExistingItems.size() != 0) {
			// multi-selection list => throw immediately
			if (select.isMultiple()) {
				throw new SeleniumLibraryNonFatalException(String.format("Options '%s' not in list '%s'.",
						Python.join(", ", nonExistingItems), locator));
			}

			// single-selection list => log warning with not found items
			logging.warn(String.format("Option%s '%s' not found within list '%s'.", nonExistingItems.size() == 0 ? ""
					: "s", Python.join(", ", nonExistingItems), locator));

			// single-selection list => throw if last item was not found
			if (!lastItemFound) {
				throw new SeleniumLibraryNonFatalException(String.format("Option '%s' not in list '%s'.",
						nonExistingItems.get(nonExistingItems.size() - 1), locator));
			}
		}
	}

	@RobotKeyword("Select the given ``*indexes`` of the multi-select list identified by ``locator``.\r\n" + 
	        "\r\n" + 
	        "Tries to select by value AND by label. It's generally faster to use 'by index/value/label' keywords.\r\n" + 
	        "\r\n" + 
	        "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "*indexes" })
	public void selectFromListByIndex(String locator, String... indexes) {
		if (indexes.length == 0) {
			throw new SeleniumLibraryNonFatalException("No index given.");
		}

		List<String> tmp = new ArrayList<>();
		Collections.addAll(tmp, indexes);
		String items = String.format("index(es) '%s'", Python.join(", ", tmp));
		logging.info(String.format("Selecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);
		for (String index : indexes) {
			select.selectByIndex(Integer.parseInt(index));
		}
	}

    @RobotKeyword("Select the given ``*values`` of the multi-select list identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "*values" })
	public void selectFromListByValue(String locator, String... values) {
		if (values.length == 0) {
			throw new SeleniumLibraryNonFatalException("No value given.");
		}

		String items = String.format("value(s) '%s'", Python.join(", ", values));
		logging.info(String.format("Selecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);
		for (String value : values) {
			select.selectByValue(value);
		}
	}

    @RobotKeyword("Select the given ``*labels`` of the multi-select list identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "*labels" })
	public void selectFromListByLabel(String locator, String... labels) {
		if (labels.length == 0) {
			throw new SeleniumLibraryNonFatalException("No value given.");
		}

		String items = String.format("label(s) '%s'", Python.join(", ", labels));
		logging.info(String.format("Selecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);
		for (String label : labels) {
			select.selectByVisibleText(label);
		}
	}

	@RobotKeyword("Unselect the given ``*items`` of the multi-select list identified by ``locator``.\r\n" + 
	        "\r\n" + 
	        "As a special case, giving an empty *items list will remove all selections.\r\n" + 
	        "\r\n" + 
	        "Tries to unselect by value AND by label. It's generally faster to use 'by index/value/label' keywords.\r\n" + 
	        "\r\n" + 
	        "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "*items" })
	public void unselectFromList(String locator, String... items) {
		String itemList = items.length != 0 ? String.format("option(s) [ %s ]", Python.join(" | ", items))
				: "all options";
		logging.info(String.format("Unselecting %s from list '%s'.", itemList, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new SeleniumLibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		if (items.length == 0) {
			select.deselectAll();

			return;
		}

		for (String item : items) {
			select.deselectByValue(item);
			select.deselectByVisibleText(item);
		}
	}

    @RobotKeyword("Unselect the given ``*indexes`` of the multi-select list identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "*indexes" })
	public void unselectFromListByIndex(String locator, Integer... indexes) {
		if (indexes.length == 0) {
			throw new SeleniumLibraryNonFatalException("No index given.");
		}

		List<String> tmp = new ArrayList<>();
		for (Integer index : indexes) {
			tmp.add(index.toString());
		}
		String items = String.format("index(es) '%s'", Python.join(", ", tmp));
		logging.info(String.format("Unselecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new SeleniumLibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		for (int index : indexes) {
			select.deselectByIndex(index);
		}
	}

    @RobotKeyword("Unselect the given ``*values`` of the multi-select list identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "*values" })
	public void unselectFromListByValue(String locator, String... values) {
		if (values.length == 0) {
			throw new SeleniumLibraryNonFatalException("No value given.");
		}

		String items = String.format("value(s) '%s'", Python.join(", ", values));
		logging.info(String.format("Unselecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new SeleniumLibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		for (String value : values) {
			select.deselectByValue(value);
		}
	}

    @RobotKeyword("Unselect the given ``*labels`` of the multi-select list identified by ``locator``.\r\n" + 
            "\r\n" + 
            "Select list keywords work on both lists and combo boxes. Key attributes for select lists are id and name. See `Introduction` for details about locators.")
	@ArgumentNames({ "locator", "*labels" })
	public void unselectFromListByLabel(String locator, String... labels) {
		if (labels.length == 0) {
			throw new SeleniumLibraryNonFatalException("No value given.");
		}

		String items = String.format("label(s) '%s'", Python.join(", ", labels));
		logging.info(String.format("Unselecting %s from list '%s'.", items, locator));

		Select select = getSelectList(locator);

		if (!isMultiselectList(select)) {
			throw new SeleniumLibraryNonFatalException(
					"Keyword 'Unselect from list' works only for multiselect lists.");
		}

		for (String label : labels) {
			select.deselectByVisibleText(label);
		}
	}

	// ##############################
	// Internal Methods
	// ##############################

	protected List<String> getLabelsForOptions(List<WebElement> options) {
		List<String> labels = new ArrayList<>();

		for (WebElement option : options) {
			labels.add(option.getText());
		}

		return labels;
	}

	protected Select getSelectList(String locator) {
		List<WebElement> webElements = element.elementFind(locator, true, true, "select");

		return new Select(webElements.get(0));
	}

	protected List<WebElement> getSelectListOptions(Select select) {
		return new ArrayList<>(select.getOptions());
	}

	protected List<WebElement> getSelectListOptions(String locator) {
		Select select = getSelectList(locator);

		return getSelectListOptions(select);
	}

	protected List<WebElement> getSelectListOptionsSelected(String locator) {
		Select select = getSelectList(locator);

		return new ArrayList<>(select.getAllSelectedOptions());
	}

	protected List<String> getValuesForOptions(List<WebElement> options) {
		ArrayList<String> labels = new ArrayList<>();

		for (WebElement option : options) {
			labels.add(option.getAttribute("value"));
		}

		return labels;
	}

	protected boolean isMultiselectList(Select select) {
		return select.isMultiple();
	}

}
