Java port of the Python based SeleniumLibrary for Robot Framework
==========================================================================

Introduction
------------

SeleniumLibrary is a web testing library for Robot Framework that leverages
the Selenium libraries from the [Selenium](http://docs.seleniumhq.org) project.

Version number follows Selenium's version numbers (first 3 numbers), and 4th number is reserved for e.g. issue fixes.

This Java version of SeleniumLibrary exists because of easier dependency management when Robot Framework is used in Java-projects. Library is a quite direct alternative to the Python SeleniumLibrary.

Usage, Selenium 2
-----------------
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
[jar-with-dependencies](http://search.maven.org/remotecontent?filepath=com/github/hi-fi/robotframework-seleniumlibrary/2.53.1.1/robotframework-seleniumlibrary-2.53.1.1-jar-with-dependencies.jar),
which contains all required libraries.

* More information about this library can be found in the
  [Keyword Documentation](http://search.maven.org/remotecontent?filepath=com/github/hi-fi/robotframework-seleniumlibrary/2.53.1.1/robotframework-seleniumlibrary-2.53.1.1.html).
* For keyword completion in RIDE you can download this
  [Library Specs](http://search.maven.org/remotecontent?filepath=com/github/hi-fi/robotframework-seleniumlibrary/2.53.1.1/robotframework-seleniumlibrary-2.53.1.1.xml)
  and place it in your PYTHONPATH.


Usage, Selenium 3
-----------------

If you are using the robotframework-maven-plugin you can
use this library by adding the following dependency to 
your pom.xml:

    <dependency>
        <groupId>com.github.hi-fi</groupId>
        <artifactId>robotframework-seleniumlibrary</artifactId>
        <version>3.9.1.0</version>
        <scope>test</scope>
    </dependency>

If you cannot use the robotframework-maven-plugin you can use the
[jar-with-dependencies](http://search.maven.org/remotecontent?filepath=com/github/hi-fi/robotframework-seleniumlibrary/3.9.1.0/robotframework-seleniumlibrary-3.9.1.0-jar-with-dependencies.jar),
which contains all required libraries.

* More information about this library can be found in the
  [Keyword Documentation](http://search.maven.org/remotecontent?filepath=com/github/hi-fi/robotframework-seleniumlibrary/3.9.1.0/robotframework-seleniumlibrary-3.9.1.0.html).
* For keyword completion in RIDE you can download this
  [Library Specs](http://search.maven.org/remotecontent?filepath=com/github/hi-fi/robotframework-seleniumlibrary/3.9.1.0/robotframework-seleniumlibrary-3.9.1.0.xml)
  and place it in your PYTHONPATH.

Differences
-----------

* Some keyword differences between this and [Python version](https://github.com/robotframework/SeleniumLibrary) exists. (Same) keywords should be aligned in upcoming versions.


Browser drivers
---------------

After installing the library, you still need to install browser and
operating system specific browser drivers for all those browsers you
want to use in tests. These are the exact same drivers you need to use with
Selenium also when not using SeleniumLibrary. More information about
drivers can be found from `Selenium documentation`__.

The general approach to install a browser driver is downloading a right
driver, such as ``chromedriver`` for Chrome, and placing it into
a directory that is in PATH__. Drivers for different browsers
can be found via Selenium documentation or by using your favorite
search engine with a search term like ``selenium chrome browser driver``.
New browser driver versions are released to support features in
new browsers, fix bug, or otherwise, and you need to keep an eye on them
to know when to update drivers you use.

Drivers can also be fetched with [https://github.com/Ardesco/selenium-standalone-server-plugin|Selenium Driver Binary Downloader plugin].

__ https://seleniumhq.github.io/selenium/docs/api/py/index.html#drivers
__ https://en.wikipedia.org/wiki/PATH_(variable)

Demo
----

This is a maven project. You can execute the integration tests (using [jBrowser](https://github.com/machinepublishers/jbrowserdriver)) with:

    mvn integration-test
    
Other browsers are behind profiles (require browser installation with Firefox and Google Chrome, but not driver):

* Firefox: mvn integration-test -Pfirefox
* Google Chrome: mvn integration-test -Pgooglechrome
* PhantomJS: mvn integration-test -Pphantomjs 

Getting Help
------------

The [user group for Robot Framework](https://groups.google.com/forum/#!forum/robotframework-users)
is the best place to get help. Consider including in the post:
* Full description of what you are trying to do and expected outcome
* Version number of SeleniumLibrary, Robot Framework
* StackTraces or other debug output containing error information
