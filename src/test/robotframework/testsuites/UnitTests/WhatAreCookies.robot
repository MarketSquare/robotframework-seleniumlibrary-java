*** Settings ***
Resource          ../../settings/Settings.robot
Library    String

*** Test Cases ***
Cookies
    Open Browser    http://www.whatarecookies.com/cookietest.asp    ${browser}    mainbrowser
    Handle Cookie Consent    cookieconsent_btn
    ${all_cookies}    Get Cookies
    ${first cookie name}   Split String    ${all_cookies}    =
    ${test}    Get Cookie Value    ${first cookie name}[0]
    Close Browser 