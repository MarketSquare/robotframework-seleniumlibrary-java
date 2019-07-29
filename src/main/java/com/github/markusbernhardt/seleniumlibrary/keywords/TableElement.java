package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;
import com.github.markusbernhardt.seleniumlibrary.locators.TableElementFinder;

@RobotKeywords
public class TableElement extends RunOnFailureKeywordsAdapter {

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
	
	@Autowired
	protected Robot robot;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeyword("Returns the content of the table cell at the coordinates ``row`` and ``column`` of the table identified by ``tableLocator``.\r\n" + 
	        "\r\n" + 
	        "Row and column number start from 1. Header and footer rows are included in the count. That way also cell content from header or footer rows can be obtained with this keyword.\r\n" + 
	        "\r\n" + 
	        "Key attributes for tables are id and name. See `Introduction` for details about locators and log levels.")
	@ArgumentNames({ "tableLocator", "row", "column", "logLevel=INFO" })
	public String getTableCell(String tableLocator, int row, int column, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
		int rowIndex = row - 1;
		int columnIndex = column - 1;
		WebElement table = TableElementFinder.find(browserManagement.getCurrentWebDriver(), tableLocator);
		if (table != null) {
			List<WebElement> rows = table.findElements(By.xpath("./thead/tr"));
			if (rowIndex >= rows.size()) {
				rows.addAll(table.findElements(By.xpath("./tbody/tr")));
			}
			if (rowIndex >= rows.size()) {
				rows.addAll(table.findElements(By.xpath("./tfoot/tr")));
			}
			if (rowIndex < rows.size()) {
				List<WebElement> columns = rows.get(rowIndex).findElements(By.tagName("th"));
				if (columnIndex >= columns.size()) {
					columns.addAll(rows.get(rowIndex).findElements(By.tagName("td")));
				}
				if (columnIndex < columns.size()) {
					return columns.get(columnIndex).getText();
				}
			}
		}
		logging.logSource(logLevel);
		throw new SeleniumLibraryNonFatalException(String.format(
				"Cell in table %s in row #%d and column #%d could not be found.", tableLocator, row, column));
	}

    @RobotKeyword("Verify the content of the table cell at the coordinates ``row`` and ``column`` of the table identified by ``tableLocator`` contains ``text``.\r\n" + 
            "\r\n" + 
            "Row and column number start from 1. Header and footer rows are included in the count. That way also cell content from header or footer rows can be obtained with this keyword.\r\n" + 
            "\r\n" + 
            "Key attributes for tables are id and name. See `Introduction` for details about locators and log levels.")
	@ArgumentNames({ "tableLocator", "row", "column", "text", "logLevel=INFO" })
	public void tableCellShouldContain(String tableLocator, int row, int column, String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
		String message = String.format("Cell in table '%s' in row #%d and column #%d should have contained text '%s'.",
				tableLocator, row, column, text);

		String content;
		try {
			content = getTableCell(tableLocator, row, column, logLevel);
		} catch (SeleniumLibraryNonFatalException e) {
			logging.info(e.getMessage());
			throw new SeleniumLibraryNonFatalException(message);
		}

		logging.info(String.format("Cell contains %s.", content));
		if (!content.contains(text)) {
			logging.logSource(logLevel);
			throw new SeleniumLibraryNonFatalException(message);
		}
	}

	@RobotKeyword("Verify the content of any table cells of the table ``column`` of the table identified by ``tableLocator`` contains ``text``.\r\n" + 
	        "\r\n" + 
	        "Key attributes for tables are id and name. See `Introduction` for details about locators and log levels.\r\n" + 
	        "\r\n" + 
	        "The first leftmost column is column number 1. If the table contains cells that span multiple columns, those merged cells count as a single column. For example both tests below work, if in one row columns A and B are merged with colspan=\"2\", and the logical third column contains \"C\".\r\n" + 
	        "\r\n" + 
	        "Example:\r\n" + 
	        " | Table Column Should Contain | tableId | 3 | C | \r\n" + 
	        " | Table Column Should Contain | tableId | 2 | C |")
	@ArgumentNames({ "tableLocator", "col", "text", "logLevel=INFO" })
	public void tableColumnShouldContain(String tableLocator, int col, String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
		WebElement element = TableElementFinder.findByCol(browserManagement.getCurrentWebDriver(), tableLocator, col,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new SeleniumLibraryNonFatalException(String.format(
					"Column #%d in table identified by '%s' should have contained text '%s'.", col, tableLocator, text));
		}
	}

    @RobotKeyword("Verify the content of any table footer cells of the table identified by ``tableLocator`` contains ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for tables are id and name. See `Introduction` for details about locators and log levels.")
	@ArgumentNames({ "tableLocator", "text", "logLevel=INFO" })
	public void tableFooterShouldContain(String tableLocator, String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
		WebElement element = TableElementFinder.findByFooter(browserManagement.getCurrentWebDriver(), tableLocator,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new SeleniumLibraryNonFatalException(String.format(
					"Footer in table identified by '%s' should have contained text '%s'.", tableLocator, text));
		}
	}

    @RobotKeyword("Verify the content of any table header cells of the table identified by ``tableLocator`` contains ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for tables are id and name. See `Introduction` for details about locators and log levels.")
	@ArgumentNames({ "tableLocator", "text", "logLevel=INFO" })
	public void tableHeaderShouldContain(String tableLocator, String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
		WebElement element = TableElementFinder.findByHeader(browserManagement.getCurrentWebDriver(), tableLocator,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new SeleniumLibraryNonFatalException(String.format(
					"Header in table identified by '%s' should have contained text '%s'.", tableLocator, text));
		}
	}

	@RobotKeyword("Verify the content of any table cells of the table ``row`` of the table identified by ``tableLocator`` contains ``text``.\r\n" + 
	        "\r\n" + 
	        "Key attributes for tables are id and name. See `Introduction` for details about locators and log levels.\r\n" + 
	        "\r\n" + 
	        "The uppermost row is row number 1. For tables that are structured with thead, tbody and tfoot, only the tbody section is searched. Please use Table Header Should Contain or Table Footer Should Contain for tests against the header or footer content.\r\n" + 
	        "\r\n" + 
	        "If the table contains cells that span multiple rows, a match only occurs for the uppermost row of those merged cells.")
	@ArgumentNames({ "tableLocator", "row", "text", "logLevel=INFO" })
	public void tableRowShouldContain(String tableLocator, int row, String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
		WebElement element = TableElementFinder.findByRow(browserManagement.getCurrentWebDriver(), tableLocator, row,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new SeleniumLibraryNonFatalException(String.format(
					"Row #%d in table identified by '%s' should have contained text '%s'.", row, tableLocator, text));
		}
	}

    @RobotKeyword("Verify the content of any table cells of the table identified by ``tableLocator`` contains ``text``.\r\n" + 
            "\r\n" + 
            "Key attributes for tables are id and name. See `Introduction` for details about locators and log levels.")
	@ArgumentNames({ "tableLocator", "text", "logLevel=INFO" })
	public void tableShouldContain(String tableLocator, String text, String...params) {
        String logLevel = robot.getParamsValue(params, 0, "INFO");
		WebElement element = TableElementFinder.findByContent(browserManagement.getCurrentWebDriver(), tableLocator,
				text);
		if (element == null) {
			logging.logSource(logLevel);
			throw new SeleniumLibraryNonFatalException(String.format(
					"Table identified by '%s' should have contained text '%s'.", tableLocator, text));
		}
	}

}
