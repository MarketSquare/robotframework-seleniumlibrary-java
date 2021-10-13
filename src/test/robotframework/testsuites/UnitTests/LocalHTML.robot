*** Settings ***
Suite Teardown    Close All Browsers
Resource          ../../settings/Settings.robot
Default Tags      jbrowser

*** Test Cases *** 
Check Form
    Open Browser    file:///${testHTMLDirectory}/formtest.html    ${browser}
    Textfield Value Should Be    name:firstname    Mickey
    Textfield Value Should Be    name:homepage    http://example.com

Check Table
    Open Browser    file:///${testHTMLDirectory}/tabletest.html    ${browser}
    Table Footer Should Contain    test_table    Totals
    Table Footer Should Contain    test_table    21,000
    Table Header Should Contain    test_table    Items