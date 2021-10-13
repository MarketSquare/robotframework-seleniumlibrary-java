# Java port of the Python based SeleniumLibrary for Robot Framework

## Introduction

SeleniumLibrary is a web testing library for Robot Framework that leverages
the Selenium libraries from the [Selenium](http://docs.seleniumhq.org) project.

Version number follows Selenium's version numbers (first 3 numbers), and 4th number is reserved for e.g. issue fixes.

This Java version of SeleniumLibrary exists because of easier dependency management when Robot Framework is used in Java-projects. Library is a quite direct alternative to the Python SeleniumLibrary.

## Usage, Selenium 4

If you are using the robotframework-maven-plugin you can
use this library by adding the following dependency to
your pom.xml:

    <dependency>
        <groupId>com.github.marketsquare</groupId>
        <artifactId>robotframework-seleniumlibrary</artifactId>
        <version>4.0.0.0</version>
        <scope>test</scope>
    </dependency>

If you cannot use the robotframework-maven-plugin you can use the
[jar-with-dependencies](https://repo1.maven.org/maven2/com/github/marketsquare/robotframework-seleniumlibrary/4.0.0.0/robotframework-seleniumlibrary-4.0.0.0-jar-with-dependencies.jar),
which contains all required libraries except ios/android ones (Appium and Selendroid). Running of tests with this can be done with command `java -jar robotframework-seleniumlibrary-4.0.0.0-jar-with-dependencies.jar <test location>`.

- More information about this library can be found in the
  [Keyword Documentation](https://repo1.maven.org/maven2/com/github/marketsquare/robotframework-seleniumlibrary/4.0.0.0/robotframework-seleniumlibrary-4.0.0.0.html).
- For keyword completion in RIDE you can download this
  [Library Specs](https://repo1.maven.org/maven2/com/github/marketsquare/robotframework-seleniumlibrary/4.0.0.0/robotframework-seleniumlibrary-4.0.0.0.xml)
  and place it in your PYTHONPATH.

## Old versions

### Usage, Selenium 2

If you are using the robotframework-maven-plugin you can
use this library by adding the following dependency to
your pom.xml:

    <dependency>
        <groupId>com.github.hi-fi</groupId>
        <artifactId>robotframework-seleniumlibrary</artifactId>
        <version>2.53.1.1</version>
        <scope>test</scope>
    </dependency>

If you cannot use the robotframework-maven-plugin you can use the
[jar-with-dependencies](https://repo1.maven.org/maven2/com/github/hi-fi/robotframework-seleniumlibrary/2.53.1.1/robotframework-seleniumlibrary-2.53.1.1-jar-with-dependencies.jar),
which contains all required libraries.

- More information about this library can be found in the
  [Keyword Documentation](https://repo1.maven.org/maven2/com/github/hi-fi/robotframework-seleniumlibrary/2.53.1.1/robotframework-seleniumlibrary-2.53.1.1.html).
- For keyword completion in RIDE you can download this
  [Library Specs](https://repo1.maven.org/maven2/com/github/hi-fi/robotframework-seleniumlibrary/2.53.1.1/robotframework-seleniumlibrary-2.53.1.1.xml)
  and place it in your PYTHONPATH.

### Usage, Selenium 3

If you are using the robotframework-maven-plugin you can
use this library by adding the following dependency to
your pom.xml:

    <dependency>
        <groupId>com.github.hi-fi</groupId>
        <artifactId>robotframework-seleniumlibrary</artifactId>
        <version>3.141.59.26535</version>
        <scope>test</scope>
    </dependency>

If you cannot use the robotframework-maven-plugin you can use the
[jar-with-dependencies](https://repo1.maven.org/maven2/com/github/hi-fi/robotframework-seleniumlibrary/3.141.59.26535/robotframework-seleniumlibrary-3.141.59.26535-jar-with-dependencies.jar),
which contains all required libraries. Running of tests with this can be done with command `java -jar robotframework-seleniumlibrary-3.141.59.26535-jar-with-dependencies.jar <test location>`.

- More information about this library can be found in the
  [Keyword Documentation](https://repo1.maven.org/maven2/com/github/hi-fi/robotframework-seleniumlibrary/3.141.59.26535/robotframework-seleniumlibrary-3.141.59.26535.html).
- For keyword completion in RIDE you can download this
  [Library Specs](https://repo1.maven.org/maven2/com/github/hi-fi/robotframework-seleniumlibrary/3.141.59.26535/robotframework-seleniumlibrary-3.141.59.26535.xml)
  and place it in your PYTHONPATH.

## Testing IOS/Android browsers with library

Pre-Selenium 4 fat-jar contains Appium and Selendroid that allow testing of IOS and Android browsers. Those are dropped in Selenium 4 -versions,
so dependencies needs to be handles self with either Maven/Gradle or manually. If there's need to have fat-jar with those, please create issue about that.

## Differences

- Some keyword differences between this and [Python version](https://github.com/robotframework/SeleniumLibrary) exists. (Same) keywords should be aligned in upcoming versions.
- Older version of the library was imported as `Library Selenium2Library` (both Java and Python versions).
  Since 2.53.1.1 (and later major versions) import is done as `Library SeleniumLibrary`

## Browser drivers

After installing the library, you still need to install browser and
operating system specific browser drivers for all those browsers you
want to use in tests. These are the exact same drivers you need to use with
Selenium also when not using SeleniumLibrary. More information about
drivers can be found from [Selenium documentation](https://seleniumhq.github.io/selenium/docs/api/py/index.html#drivers).

The general approach to install a browser driver is downloading a right
driver, such as `chromedriver` for Chrome, and placing it into
a directory that is in [PATH](<https://en.wikipedia.org/wiki/PATH_(variable)>). Drivers for different browsers
can be found via Selenium documentation or by using your favorite
search engine with a search term like `selenium chrome browser driver`.
New browser driver versions are released to support features in
new browsers, fix bug, or otherwise, and you need to keep an eye on them
to know when to update drivers you use.

Drivers can also be fetched with [Selenium Driver Binary Downloader plugin](https://github.com/Ardesco/selenium-standalone-server-plugin).

After version 3.141.59.265 [WebdriverManager](https://github.com/bonigarcia/webdrivermanager) is taken to use, so handling of drivers can also be done with standalone JAR from tests itself.

## Demo

This is a maven project. You can execute the integration tests (using [jBrowser](https://github.com/machinepublishers/jbrowserdriver)) with:

    mvn integration-test

Other browsers are behind profiles
(require browser installation with Firefox and Google Chrome, but not driver. Driver downloaded directly from Internet, so runner needs to have access to it.):

- Firefox: mvn integration-test -Pfirefox
- Google Chrome: mvn integration-test -Pgooglechrome
- PhantomJS: mvn integration-test -Pphantomjs

## Getting Help

The [user group for Robot Framework](https://groups.google.com/forum/#!forum/robotframework-users)
is the best place to get help. Consider including in the post:

- Full description of what you are trying to do and expected outcome
- Version number of SeleniumLibrary, Robot Framework
- StackTraces or other debug output containing error information
