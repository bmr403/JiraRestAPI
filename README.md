# JiraRestAPI
Performs the issue transition with transition id along with adding comment for the ticket.

This code will pull the tickets of the specified unique label and chnage the status to the transition which you specified.

Clone the development branch and create runnanble JAR using eclipse and select UpdateJiraTicketStatusWithComment as main class to run the project.

Just run the "UpdateJiraTicketStatus.sh" file and make the Runnable Jar next to the script file and dependencies availiable before running. Pass the arguments to the script as requested JIRA_HOST JIRA_USER JIRA_PWD PROJECT_NAME UNIQUE_LABEL TRANSISTION_ID.

This script will move the tickets into current status to TRANSITION STATUS which TRANSITION ID* you mentioned as arguments.

NOTE: You may get 404 or permission issues, please check the current ticket status have the permission to move into specified transition. 
