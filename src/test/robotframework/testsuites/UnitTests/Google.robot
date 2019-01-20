*** Settings ***
Suite Setup       Google.Site.Open
Suite Teardown    Google.Site.Close
Test Setup        Google.Site.Init
Resource          ../../settings/Settings.robot
Resource          ../../imports/Google.txt

*** Test cases ***
Open And Close Google Site
    Capture Page Screenshot
    
Store Web Element In JavaScript
    Wait Until Page Contains Element    //*[contains(@class,'gsfi')]
    Execute Javascript    window.document.my_element = window.document.getElementsByClassName('gsfi')[0];
    ${className}=    Execute Javascript    return window.document.my_element.className;
    Should Contain    ${className}    gsfi

Search Robotframework Selenium2Library
    Google.Search.Search String    Robotframework Selenium2Library Java
    
Search With JavaScript Locator
    SeleniumLibrary.Add Location Strategy    elementByName    return window.document.getElementsByName(arguments[0])[0];
    Input Text    elementByName:q    Robotframework Selenium2Library Java
    Press Key    elementByName:q    \\13
    Wait Until Element Is Visible    xpath://a[contains(.,'MarkusBernhardt')]
    Capture Page Screenshot

Search Without Locator Type
    Input Text    q    Robotframework Selenium2Library Java
    Press Key    q    \\13
    Wait Until Element Is Visible    //a[contains(.,'MarkusBernhardt')]
    
Get Name Of Active Element With JavaScript
    Input Text    q    Robotframework Selenium2Library
    ${activeElementId}=    Execute JavaScript    return window.document.activeElement.name;
