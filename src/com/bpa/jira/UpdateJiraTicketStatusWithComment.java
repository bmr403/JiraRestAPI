package com.bpa.jira;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.DelegatingPromise;
import com.atlassian.util.concurrent.Nullable;

public class UpdateJiraTicketStatusWithComment {
	static Logger logger = Logger.getLogger("JiraRestAPIEx");
	static JiraRestClient client = null;
	static String JIRA_URL = "URL";
	static String JIRA_ADMIN_USERNAME = "user Name";
	static String JIRA_ADMIN_PASSWORD = "Password";
	static String PROJECT_NAME = "PROJECT";
	static String UNIQUE_LABEL = "UNIQUE LABEL NAME";
	static String COMMENT_BODY = "MOVED STATUS using JIRA REST API";
	static int TRANSISTION_ID = 241; // Transition id (Id specifies where this transition should get
													// moved)
	static int MAX_RESULTS = 100;
	static URI uri = null;

	public static void main(String[] args) {

		try {
			if (JIRA_URL != null) {
				JIRA_URL = args[0];
				JIRA_ADMIN_USERNAME = args[1];
				JIRA_ADMIN_PASSWORD = args[2];
				PROJECT_NAME = args[3];
				UNIQUE_LABEL = args[4].toString();
				TRANSISTION_ID = Integer.parseInt(args[5]);
				
				// Construct the JRJC client
				client = getJiraRestClientAccess();
				Iterator<Issue> isssue = getIssuesByProjectAndLabel(PROJECT_NAME, UNIQUE_LABEL, MAX_RESULTS).getIssues().iterator();
				while (isssue.hasNext()) {
					Issue jiraTicket = isssue.next();
					logger.info(jiraTicket.getKey());
					TransitionInput tinput = new TransitionInput(TRANSISTION_ID);
					client.getIssueClient().transition(jiraTicket, tinput).claim();
					addComment(jiraTicket, COMMENT_BODY);
					logger.info(jiraTicket.getKey() + " status has been updated");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static JiraRestClient getJiraRestClientAccess() {
		try {
			String.format("Logging in to %s with username '%s' and password '%s'", JIRA_URL, JIRA_ADMIN_USERNAME,
					JIRA_ADMIN_PASSWORD);
			uri = new URI(JIRA_URL);
			client = new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME,
					JIRA_ADMIN_PASSWORD);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return client;
	}

	public static SearchResult getIssuesByProjectAndLabel(String PROJECT_NAME, String UNIQUE_LABEL, int maxResults) {
			TreeSet<String> set = new TreeSet<String>();
			set.add("*all");
			try {
				return client.getSearchClient().searchJql("project = " + PROJECT_NAME + " AND labels = "
						+ UNIQUE_LABEL + " ORDER BY priority DESC, updated DESC", 100, 0, set).get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

	}
	
	public static void addComment(Issue issue, String commentBody) {
		client.getIssueClient().addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
	}
}
