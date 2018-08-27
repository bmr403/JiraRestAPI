package com.bpa.jira;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.DelegatingPromise;

import java.io.Writer;
import java.net.URI;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;

public class JiraRestAPIMain {

	static Logger logger = Logger.getLogger("JiraRestAPIEx");
	static JiraRestClient client = null;

	public static void main(String[] args) {

		final String JIRA_URL = "URL";
		final String JIRA_ADMIN_USERNAME = "user Name";
		final String JIRA_ADMIN_PASSWORD = "Password";
		final String PROJECT_NAME = "PROJECT";
		final String UNIQUE_LABEL = "UNIQUE LABEL NAME";
		final String COMMENT_BODY = "MOVED FROM OPEN TO DEV using JIRA REST API";
		final int OPEN_TO_DEV_TRANSISTION = 1; // Transition id (Id specifies where this transition should get moved)
		URI uri;
		try {
			if (JIRA_URL != null) {
				// Construct the JRJC client
				String.format("Logging in to %s with username '%s' and password '%s'", JIRA_URL,
						JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
				uri = new URI(JIRA_URL);
				JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
				client = factory.createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
				TreeSet<String> set = new TreeSet<String>();
				set.add("*all");
				DelegatingPromise<SearchResult> searchJqlPromise = (DelegatingPromise<SearchResult>) client
						.getSearchClient().searchJql("project = " + PROJECT_NAME + " AND labels = " + UNIQUE_LABEL
								+ " ORDER BY priority DESC, updated DESC", 100, 0, set);

				SearchResult searchResult = searchJqlPromise.get();
				Iterator<Issue> isssue = searchResult.getIssues().iterator();
				while (isssue.hasNext()) {
					Issue jiraTicket = isssue.next();
					System.out.println(jiraTicket.getKey());
					TransitionInput tinput = new TransitionInput(OPEN_TO_DEV_TRANSISTION);
					client.getIssueClient().transition(jiraTicket, tinput).claim();
					JiraRestAPIMain.addComment(jiraTicket, COMMENT_BODY);

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	public static void addComment(Issue issue, String commentBody) {
		client.getIssueClient().addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
	}

}
