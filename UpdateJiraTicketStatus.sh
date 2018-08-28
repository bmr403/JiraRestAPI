#!/bin/bash

#
# To Get Commits information of an Jira issue using REST API and updating STATUS with comment.

JIRA_HOST=$1
JIRA_USER=$2
JIRA_API_KEY=$3
PROJECT_NAME=$4
UNIQUE_LABEL=$5
TRANSISTION_ID=$6
JAR_PATH=`pwd`
JARNAME=UpdateJiraTicketStatusWithComment

usage() {
  echo "Usage: $0 JIRA_HOST JIRA_USER JIRA_API_KEY PROJECT_NAME UNIQUE_LABEL TRANSISTION_ID"
  exit 1
}

if [ -z "$JIRA_HOST" ]; then
  usage;
fi

if [ -z "$JIRA_USER" ]; then
  usage;
fi

if [ -z "$JIRA_API_KEY" ]; then 
  usage;
fi

if [ -z "$PROJECT_NAME" ]; then
  usage;
fi

if [ -z "$UNIQUE_LABEL" ]; then
  usage;
fi

if [ -z "$TRANSISTION_ID" ]; then
  usage;
fi

java -jar "$JAR_PATH/$JARNAME.jar" $JIRA_HOST $JIRA_USER $JIRA_API_KEY $PROJECT_NAME $UNIQUE_LABEL $TRANSISTION_ID &&

echo "JIRA REST API Changing status was COMPLETED..."
