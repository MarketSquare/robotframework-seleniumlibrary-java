*** Settings ***
Resource          ../../settings/Settings.robot
Default Tags      htmlunit    htmlunitwithjs

*** Test Cases ***
Cookies
    Open Browser    http://www.whatarecookies.com/cookietest.asp    ${browser}    mainbrowser
    Handle Cookie Consent    cookieconsent_btn
    ${all_cookies}=    Get Cookies
    ${test}=    Get Cookie Value    cookieconsent
    Close Browser 