*** Settings ***
Resource          ../../settings/Settings.robot
Suite Setup    Handle WebDriver

*** Keywords ***
Handle WebDriver
    Run Keyword If    ${downloadWebDriver}    WebDriver Manager Setup    ${browser}