package com.github.markusbernhardt.seleniumlibrary.locators;

import com.github.markusbernhardt.seleniumlibrary.SeleniumLibraryNonFatalException;
import com.github.markusbernhardt.seleniumlibrary.keywords.Element;
import com.github.markusbernhardt.seleniumlibrary.utils.Python;
import org.openqa.selenium.*;
import org.python.util.PythonInterpreter;

import java.util.*;
import java.util.Map.Entry;

public class ElementFinder {

	protected final static Hashtable<String, CustomStrategy> registeredLocationStrategies = new Hashtable<>();

	protected enum KeyAttrs {
		DEFAULT("@id,@name"),
		A("@id,@name,@href,normalize-space(descendant-or-self::text())"),
		IMG("@id,@name,@src,@alt"),
		INPUT("@id,@name,@value,@src"),
		BUTTON("@id,@name,@value,normalize-space(descendant-or-self::text())");

		protected String[] keyAttrs;

		KeyAttrs(String keyAttrs) {
			this.keyAttrs = keyAttrs.split(",");
		}

		public String[] getKeyAttrs() {
			return keyAttrs;
		}
	}

	protected interface Strategy {
		List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates);
	}

	protected enum StrategyEnum implements Strategy {
		DEFAULT {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {
				if (findByCoordinates.criteria.startsWith("//")) {
					return XPATH.findBy(webDriver, findByCoordinates);
				}
				return findByKeyAttrs(webDriver, findByCoordinates);
			}
		},
		IDENTIFIER {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {
				List<WebElement> elements = webDriver.findElements(By.id(findByCoordinates.criteria));
				elements.addAll(webDriver.findElements(By.name(findByCoordinates.criteria)));
				return filterElements(elements, findByCoordinates);
			}
		},
		ID {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By.id(findByCoordinates.criteria)), findByCoordinates);
			}
		},
		NAME {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By.name(findByCoordinates.criteria)), findByCoordinates);
			}
		},
		XPATH {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By.xpath(findByCoordinates.criteria)), findByCoordinates);
			}
		},
		DOM {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {
				Object result = ((JavascriptExecutor) webDriver).executeScript(String.format("return %s;",
						findByCoordinates.criteria));
				return filterElements(toList(result), findByCoordinates);
			}
		},
		LINK {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By.linkText(findByCoordinates.criteria)),
						findByCoordinates);
			}
		},
		CSS {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By.cssSelector(findByCoordinates.criteria)),
						findByCoordinates);
			}
		},
		TAG {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {
				return filterElements(webDriver.findElements(By.tagName(findByCoordinates.criteria)), findByCoordinates);
			}
		},
		JQUERY {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {

				return findByJQuerySizzle(webDriver, findByCoordinates);
			}

		},
		SIZZLE {

			@Override
			public List<WebElement> findBy(WebDriver webDriver, FindByCoordinates findByCoordinates) {

				return findByJQuerySizzle(webDriver, findByCoordinates);
			}

		};

	}

	protected static List<WebElement> findByJQuerySizzle(WebDriver webDriver, FindByCoordinates findByCoordinates) {
		String js = String.format("return jQuery('%s').get();", findByCoordinates.criteria.replace("'", "\\'"));

		Object o = ((JavascriptExecutor) webDriver).executeScript(js);
		List<WebElement> list = toList(o);
		return filterElements(list, findByCoordinates);
	}

	protected static List<WebElement> filterElements(List<WebElement> elements, FindByCoordinates findByCoordinates) {
		if (findByCoordinates.tag == null) {
			return elements;
		}

		List<WebElement> result = new ArrayList<>();
		for (WebElement element : elements) {
			if (elementMatches(element, findByCoordinates)) {
				result.add(element);
			}
		}
		return result;
	}

	protected static boolean elementMatches(WebElement element, FindByCoordinates findByCoordinates) {
		if (!element.getTagName().toLowerCase().equals(findByCoordinates.tag)) {
			return false;
		}

		if (findByCoordinates.constraints != null) {
			for (String name : findByCoordinates.constraints.keySet()) {
				if (!element.getAttribute(name).equals(findByCoordinates.constraints.get(name))) {
					return false;
				}
			}
		}

		return true;
	}

	protected static List<WebElement> findByKeyAttrs(WebDriver webDriver, FindByCoordinates findByCoordinates) {
		KeyAttrs keyAttrs = KeyAttrs.DEFAULT;
		if (findByCoordinates.tag != null) {
			try {
				keyAttrs = KeyAttrs.valueOf(findByCoordinates.tag.trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				// No special keyAttrs available for this tag
			}
		}
		String xpathCriteria = Element.escapeXpathValue(findByCoordinates.criteria);
		String xpathTag = findByCoordinates.tag;
		if (findByCoordinates.tag == null) {
			xpathTag = "*";
		}
		List<String> xpathConstraints = new ArrayList<>();
		if (findByCoordinates.constraints != null) {
			for (Entry<String, String> entry : findByCoordinates.constraints.entrySet()) {
				xpathConstraints.add(String.format("@%s='%s'", entry.getKey(), entry.getValue()));
			}
		}
		List<String> xpathSearchers = new ArrayList<>();
		for (String attr : keyAttrs.getKeyAttrs()) {
			xpathSearchers.add(String.format("%s=%s", attr, xpathCriteria));
		}
		xpathSearchers.addAll(getAttrsWithUrl(webDriver, keyAttrs, findByCoordinates.criteria));
		String xpath = String.format("//%s[%s(%s)]", xpathTag, Python.join(" and ", xpathConstraints)
				+ (xpathConstraints.size() > 0 ? " and " : ""), Python.join(" or ", xpathSearchers));

		return webDriver.findElements(By.xpath(xpath));
	}

	protected static List<String> getAttrsWithUrl(WebDriver webDriver, KeyAttrs keyAttrs, String criteria) {
		List<String> attrs = new ArrayList<>();
		String url = null;
		String xpathUrl = null;
		String[] srcHref = { "@src", "@href" };
		for (String attr : srcHref) {
			for (String keyAttr : keyAttrs.getKeyAttrs()) {
				if (attr.equals(keyAttr)) {
					if (url == null || xpathUrl == null) {
						url = getBaseUrl(webDriver) + "/" + criteria;
						xpathUrl = Element.escapeXpathValue(url);
					}
					attrs.add(String.format("%s=%s", attr, xpathUrl));
				}
			}
		}
		return attrs;
	}

	protected static String getBaseUrl(WebDriver webDriver) {
		String url = webDriver.getCurrentUrl();
		int lastIndex = url.lastIndexOf('/');
		if (lastIndex != -1) {
			url = url.substring(0, lastIndex);
		}
		return url;
	}

	public static void addLocationStrategy(String strategyName, String functionDefinition, String delimiter) {
		registeredLocationStrategies.put(strategyName.toUpperCase(), new CustomStrategy(functionDefinition, delimiter));
	}

	public static List<WebElement> find(WebDriver webDriver, String locator) {
		return find(webDriver, locator, null);
	}

	public static List<WebElement> find(WebDriver webDriver, String locator, String tag) {
		if (webDriver == null) {
			throw new SeleniumLibraryNonFatalException("ElementFinder.find: webDriver is null.");
		}
		if (locator == null) {
			throw new SeleniumLibraryNonFatalException("ElementFinder.find: locator is null.");
		}

		FindByCoordinates findByCoordinates = new FindByCoordinates();
		Strategy strategy = parseLocator(findByCoordinates, locator);
		parseTag(findByCoordinates, tag);
		return strategy.findBy(webDriver, findByCoordinates);
	}

	protected static ThreadLocal<PythonInterpreter> loggingPythonInterpreter = ThreadLocal.withInitial(() -> {
		PythonInterpreter pythonInterpreter = new PythonInterpreter();
		pythonInterpreter.exec("from robot.libraries.BuiltIn import BuiltIn; from robot.api import logger;");
		return pythonInterpreter;
	});

	protected static void warn(String msg) {
		loggingPythonInterpreter.get().exec(
				String.format("logger.warn('%s');", msg.replace("'", "\\'").replace("\n", "\\n")));
	}

	protected static Strategy parseLocator(FindByCoordinates findByCoordinates, String locator) {
		String prefix = null;
		String criteria = locator;
		if (!locator.startsWith("//")) {
			String[] locatorParts = locator.split("[=:]", 2);
			if (locatorParts.length == 2) {
			    if (locator.charAt(locatorParts[0].length()) == "=".charAt(0)) {
			        System.out.println("*WARN* '=' is deprecated as locator strategy separator. ':' should be used instead" );
			    }
				prefix = locatorParts[0].trim().toUpperCase();
				criteria = locatorParts[1].trim();
			}
		}

		Strategy strategy = StrategyEnum.DEFAULT;
		if (prefix != null) {
			try {
				strategy = StrategyEnum.valueOf(prefix);
			} catch (IllegalArgumentException e) {
				// No standard locator type. Look for custom strategy
				CustomStrategy customStrategy = registeredLocationStrategies.get(prefix);
				if (customStrategy != null) {
					strategy = customStrategy;
				}
			}
		}
		findByCoordinates.criteria = criteria;
		return strategy;
	}

	protected static void parseTag(FindByCoordinates findByCoordinates, String tag) {
		if (tag == null) {
			return;
		}

		tag = tag.toLowerCase();
		Map<String, String> constraints = new TreeMap<>();
        switch (tag) {
            case "link":
                tag = "a";
                break;
            case "image":
                tag = "img";
                break;
            case "list":
                tag = "select";
                break;
            case "text area":
                tag = "textarea";
                break;
            case "radio button":
                tag = "input";
                constraints.put("type", "radio");
                break;
            case "checkbox":
                tag = "input";
                constraints.put("type", "checkbox");
                break;
            case "text field":
                tag = "input";
                constraints.put("type", "text");
                break;
            case "file upload":
                tag = "input";
                constraints.put("type", "file");
                break;
        }
		findByCoordinates.tag = tag;
		findByCoordinates.constraints = constraints;
	}

	@SuppressWarnings("unchecked")
	protected static List<WebElement> toList(Object o) {
		if (o instanceof List<?>) {
			return (List<WebElement>) o;
		}
		List<WebElement> list = new ArrayList<>();
		if (o instanceof WebElement) {
			list.add((WebElement) o);
			return list;
		}
		return list;
	}

	protected static class FindByCoordinates {

		String criteria;
		String tag;
		Map<String, String> constraints;
	}

	protected static class CustomStrategy implements Strategy {

		protected String functionDefinition;

		protected String delimiter;

		public CustomStrategy(String functionDefinition, String delimiter) {
			this.functionDefinition = functionDefinition;
			this.delimiter = delimiter;
		}

		@Override
		public List<WebElement> findBy(final WebDriver webDriver, final FindByCoordinates findByCoordinates) {
			return filterElements(webDriver.findElements(new By() {

				@Override
				public List<WebElement> findElements(SearchContext context) {
					Object[] arguments;
					if (delimiter == null) {
						arguments = new Object[1];
						arguments[0] = findByCoordinates.criteria;
					} else {
						String[] splittedCriteria = findByCoordinates.criteria.split(delimiter);
						arguments = new Object[splittedCriteria.length];
						System.arraycopy(splittedCriteria, 0, arguments, 0, splittedCriteria.length);
					}
					Object o = ((JavascriptExecutor) webDriver).executeScript(functionDefinition, arguments);
					return toList(o);
				}

			}), findByCoordinates);
		}
	}
}
