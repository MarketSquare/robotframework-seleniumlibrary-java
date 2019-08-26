*** Settings ***
Resource          ../../settings/Settings.robot

*** Test Cases ***
Cookies
    Open Browser    http://www.whatarecookies.com/cookietest.asp    ${browser}    mainbrowser
    ${all_cookies}=    Get Cookies
    ${test}=    Get Cookie Value    _ga
    Close Browser 