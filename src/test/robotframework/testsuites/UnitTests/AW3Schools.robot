*** Settings ***
Suite Teardown    Close All Browsers
Resource          ../../settings/Settings.robot
Default Tags      htmlunit    htmlunitwithjs

*** Variables ***
${URL Application}    http://www.w3schools.com

*** Test Cases *** 
Select
    [Tags]    jbrowser
    Open Browser    https://interactive-examples.mdn.mozilla.net/pages/tabbed/select.html    ${browser}    mainbrowser
    Wait Until Page Contains Element    output-iframe
    Select Frame    output-iframe
    Wait Until Page Contains Element    xpath://select[@name="pets"]
    Select From List    xpath://select[@name="pets"]   Spider
    ${label}    Get Selected List Label    xpath://select[@name="pets"]
    Should Be Equal    ${label}    Spider
    List Selection Should Be    xpath://select[@name="pets"]    Spider
    
Multiple Browsers
    Open Browser    ${URL Application}    ${browser}    mainbrowser1
    Open Browser    ${URL Application}    ${browser}    mainbrowser2
    Close All Browsers

No Browser Alias
    Open Browser    ${URL Application}    ${browser}
    Close Browser

Close All Browsers reset the browser index to 1
    [Documentation]    Ensure that the Browser count is reset to 0 when Close All Browsers is used.
    ${id}    Open Browser    ${URL Application}    ${browser}    mainbrowser1
    Should Be Equal    ${id}    1    
    ${id}    Open Browser    ${URL Application}    ${browser}    mainbrowser2
    Should Be Equal    ${id}    2    
    Close All Browsers
    ${id}    Open Browser    ${URL Application}    ${browser}    mainbrowser1
    Should Be Equal    ${id}    1
    Close All Browsers

Open Browser after Close Browser creates unique browser id
    [Documentation]    Ensure that the Close Browser keyword doesn't reset the count, rather that it decrements.
    ${id}    Open Browser    ${URL Application}    ${browser}    mainbrowser1
    Should Be Equal    ${id}    1    
    ${id}    Open Browser    ${URL Application}    ${browser}    mainbrowser2
    Should Be Equal    ${id}    2    
    Close browser
    ${id}    Open Browser    ${URL Application}    ${browser}    mainbrowser2
    Should Be Equal    ${id}    2    
    ${id}    Open Browser    ${URL Application}    ${browser}    mainbrowser3
    Should Be Equal    ${id}    3    
    Close browser
    ${id}    Open Browser    ${URL Application}    ${browser}    mainbrowser4
    Should Be Equal    ${id}    3
    Switch Browser    2    
    Close browser
    ${id}    Open Browser    ${URL Application}    ${browser}    mainbrowser2
    Should Be Equal    ${id}    2    
    Close All Browsers
