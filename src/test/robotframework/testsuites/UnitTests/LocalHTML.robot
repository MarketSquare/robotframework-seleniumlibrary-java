*** Settings ***
Suite Teardown    Close All Browsers
Resource          ../../settings/Settings.robot

*** Variables ***
${URL Application}    http://www.w3schools.com

*** Test Cases *** 
Check Form
    Open Browser    file:///${testHTMLDirectory}/formtest.html    ${browser}
    Textfield Value Should Be    name:firstname    Mickey
    Textfield Value Should Be    name:homepage    http://example.com