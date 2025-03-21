*** Settings ***
Resource          ../../settings/Settings.robot
Default Tags      htmlunit    htmlunitwithjs
Library    String

*** Test Cases ***
Cookies
    Open Browser    http://www.whatarecookies.com/cookietest.asp    ${browser}    mainbrowser
    Handle Cookie Consent    xpath://*[@id="cookieconsent_btn"]
    ${all_cookies}    Get Cookies
    ${first cookie name}   Split String    ${all_cookies}    =
    ${test}    Get Cookie Value    ${first cookie name}[0]
    Close Browser 