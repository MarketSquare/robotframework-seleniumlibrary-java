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
    Wait Until Page Contains Element    //*[contains(@class,'gLFyf')]
    Execute Javascript    window.document.my_element = window.document.getElementsByClassName('gLFyf')[0];
    ${className}=    Execute Javascript    return window.document.my_element.className;
    Should Contain    ${className}    gLFyf
    
Get Name Of Active Element With JavaScript
    Input Text    q    Robotframework Selenium2Library
    ${activeElementId}=    Execute JavaScript    return window.document.activeElement.name;
