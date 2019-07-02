package com.github.markusbernhardt.seleniumlibrary.keywords;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.util.PythonInterpreter;
import org.robotframework.javalib.annotation.RobotKeywords;

import com.google.gson.Gson;

@RobotKeywords
public class Robot {

    protected Logging logging = new Logging();

	@SuppressWarnings("unchecked")
	public <T> T getParamsValue(String[] params, int index, T defaultValue) {
		T value = defaultValue;
		String givenValue = null;
		if (params.length > index) {
			givenValue = params[index];
			logging.debug("Value from arguments: " + givenValue);
		}

		if (givenValue != null && !givenValue.equals("None") && givenValue.length() > 0) {
			if (defaultValue instanceof Map) {
				value = (T) parseRobotDictionary(givenValue);
			} else if (defaultValue instanceof String || defaultValue == null) {
				value = (T) givenValue;
			} else if (defaultValue instanceof List) {
				value = (T) parseRobotList(givenValue);
			} else if (Boolean.valueOf(givenValue)) {
				value = (T) Boolean.valueOf(givenValue);
			}
		}
		return value;
	}

	@SuppressWarnings({ "unchecked", "resource" })
	public Map<String, Object> parseRobotDictionary(String dictionary) {
		logging.debug("Dictionary going to be parsed to Map: " + dictionary);
		Map<String, Object> json = new HashMap<String, Object>();
		try {
		PythonInterpreter py = new PythonInterpreter();
		py.exec("import json");
		json = new Gson().fromJson(py.eval("json.dumps(" + dictionary + ")").toString(), Map.class);
		} catch (RuntimeException e)  {
			logging.error(String.format("Parsing of dictionary %s failed.", dictionary));
			throw e;
		}
		
		return json;
	}

	@SuppressWarnings("unchecked")
	public List<String> parseRobotList(String list) {
		logging.debug("List going to be parsed: " + list);
		return new Gson().fromJson(list.replace("u'", "'"), List.class);
	}

	public Boolean isDictionary(String testedString) {
		try {
			new Gson().fromJson(testedString, Map.class);
			return !testedString.contains("\"");
		} catch (Exception e) {
			logging.debug(String.format("%s is tested for being dictionary, and result is: %s", testedString, (testedString.contains("{") && testedString.contains("}"))));
			return (testedString.contains("{") && testedString.contains("}"));
		}
	}

}
