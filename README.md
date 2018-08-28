# JiraRestAPI
Performs the issue transition with transition id along with adding comment for the ticket.

This code will pull the assigned tickets of the specified user in Jira.
  It will return the Jira key and issue ID. The results may be usefull for future reference for reporting.
  
To Pull the commits of specifed Jira issue changes made by developers. 
We can use this API to get the issueId and pass the issue id to the provided "CURL" command to get all the onformation of JIRA Commits on the reposotory as JSON.

Just run the "JiraRestAPIData.sh" file and make the Jar and dependencies availiable before running. Pass the arguments to the script. It will give you all the commits happened on the specified Jira ticket.
