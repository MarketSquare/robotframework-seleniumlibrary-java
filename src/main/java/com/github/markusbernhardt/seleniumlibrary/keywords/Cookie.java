package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.util.ArrayList;
import java.util.Date;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.github.markusbernhardt.seleniumlibrary.RunOnFailureKeywordsAdapter;
import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;

@RobotKeywords
public class Cookie extends RunOnFailureKeywordsAdapter {

	/**
	 * Instantiated BrowserManagement keyword bean
	 */
	@Autowired
	protected BrowserManagement browserManagement;
	
	@Autowired
	protected Robot robot;

	// ##############################
	// Keywords
	// ##############################

	@RobotKeyword("Deletes all cookies.")
	public void deleteAllCookies() {
		browserManagement.getCurrentWebDriver().manage().deleteAllCookies();
	}

	@RobotKeyword("Deletes cookie matching ``name``.\n\r"
	        + "\n\r"
	        + "If the cookie is not found, nothing happens.")
	@ArgumentNames({ "name" })
	public void deleteCookie(String name) {
		browserManagement.getCurrentWebDriver().manage().deleteCookieNamed(name);
	}

	@RobotKeyword("Returns all cookies of the current page.")
	public String getCookies() {
		StringBuilder ret = new StringBuilder();

		ArrayList<org.openqa.selenium.Cookie> cookies = new ArrayList<>(browserManagement
				.getCurrentWebDriver().manage().getCookies());
		for (int i = 0; i < cookies.size(); i++) {
			ret.append(cookies.get(i).getName()).append('=').append(cookies.get(i).getValue());
			if (i != cookies.size() - 1) {
				ret.append("; ");
			}
		}

		return ret.toString();
	}

	@RobotKeyword("Returns value of cookie found with ``name``.\n\r"
	        + "\n\r"
	        + "If no cookie is found with name, this keyword fails.")
	@ArgumentNames({ "name" })
	public String getCookieValue(String name) {
		org.openqa.selenium.Cookie cookie = browserManagement.getCurrentWebDriver().manage().getCookieNamed(name);

		if (cookie != null) {
			return cookie.getValue();
		} else {
			throw new SeleniumLibraryNonFatalException(String.format("Cookie with name %s not found.", name));
		}
	}

	@RobotKeyword("Adds a cookie to your current session.")
	@ArgumentNames({ "name", "value", "path=NONE", "domain=NONE", "secure=NONE"})
	public void addCookie(String name, String value, String path, String domain, String secure) {
	    Date expiry = null;
		org.openqa.selenium.Cookie cookie = new org.openqa.selenium.Cookie(name, value, domain, path, expiry,
				"true".equals(secure.toLowerCase()));
		browserManagement.getCurrentWebDriver().manage().addCookie(cookie);
	}
}
