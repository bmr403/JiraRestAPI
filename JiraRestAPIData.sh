#!/bin/bash

#
# Jira Ticket details
#

# CURL command to get User tickets 
JIRA_HOST=$1
JIRA_USER=$2
JIRA_PWD=$3
JIRA_USER_SERACH=$4
START_AT=$5
MAX_RESULTS=$6
JSON_PATH=`pwd`
JSON_FILE_NAME=JiraRestAPI.txt
JARNAME=JiraRestAPI

usage() {
  echo "Usage: $0 JIRA_HOST JIRA_USER JIRA_PWD JIRA_USER_SERACH(Which user details you want to search - ID) START_AT MAX_RESULTS"
  exit 1
}

if [ -z "$JIRA_HOST" ]; then
  usage;
fi

if [ -z "$JIRA_USER" ]; then
  usage;
fi

if [ -z "$JIRA_PWD" ]; then 
  usage;
fi

if [ -z "$JIRA_USER_SERACH" ]; then
  usage;
fi

if [ -z "$START_AT" ]; then
  usage;
fi

if [ -z "$MAX_RESULTS" ]; then
  usage;
fi

java -jar "$JSON_PATH\\$JARNAME.jar" $JIRA_HOST $JIRA_USER $JIRA_PWD $JIRA_USER_SERACH $START_AT $MAX_RESULTS $JSON_PATH $JSON_FILE_NAME &&

echo "JIRA REST API done..."

pushd $JSON_PATH

if [ -s "$JSON_FILE_NAME" ]
then
	echo "$JSON_FILE_NAME found."
	while IFS=":" read -r KEY ID
do 
    echo "$KEY-$ID"
	#Working CURL to get Jira Ticket Dev status
	URL="${JIRA_HOST}/rest/dev-status/1.0/issue/detail?applicationType=bitbucket&dataType=repository&issueId=${ID}"
	FILENAME="$KEY-$ID.json"
	echo "${URL}"
	curl -u $JIRA_USER:$JIRA_PWD ${URL%$'\r'} >> $FILENAME
done < $JSON_FILE_NAME
echo "DONEEEEEE....."
else
	echo "$JSON_FILE_NAME not found."
fi
