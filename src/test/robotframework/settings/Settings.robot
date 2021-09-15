*** Settings ***
Documentation     A resource file to setup all variables.
Resource          profiles/${Profile}.txt
Library           SeleniumLibrary

*** Variables ***
${Profile}    Local
${browser}    jbrowser
${downloadWebDriver}    ${False}
${Selenium.Browser.Name}    ${browser}
${Selenium.Browser.Version}    17
${Selenium.Timeout}        30.0

*** Keywords ***
Test
    Op

Handle Cookie Consent
    [Arguments]    ${consent click xpath}
    Comment    Consent search causes stack overflow to jbrowser, so tagging tescases using this keyword
    Set Tags    jbrowser
    ${count}    Get Matching Xpath Count    ${consent click xpath}
    IF    ${count} > 0
    Click Element    ${consent click xpath}
    END