*** Settings ***
Resource          ../../settings/Settings.robot
Default Tags      jbrowser

*** Test Cases ***
Get Inner Element Id test
    Open Browser    https://www.google.co.in/#q=table+with+inner+element    ${browser}    mainbrowser
    Wait Until Page Contains Element    //*[@id="gsr"]
    ${innerelementid}=    Get Inner Element Id  //*[@id="gsr"]    doc   0
    Log Variables
    Close Browser 