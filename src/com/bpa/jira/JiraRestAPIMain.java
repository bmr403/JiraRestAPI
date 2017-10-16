package com.bpa.jira;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

public class JiraRestAPIMain {
	
	static Logger logger = Logger.getLogger("JiraRestAPIEx");
	
	public static void main(String[] args) {
		
		  final String JIRA_URL = args[0]; 
		  final String JIRA_ADMIN_USERNAME =args[1]; 
		  final String JIRA_ADMIN_PASSWORD = args[2]; 
		  String JIRA_USER_SEARCH = args[3];
		  int START_AT = Integer.parseInt(args[4]); 
		  int MAX_RESULTS = Integer.parseInt(args[5]); 
		  String FILE_PATH = args[6]; 
		  String FILE_NAME = args[7];

		URI uri;
		User user;
		Writer fstream = null;
		JiraRestClient client = null;
		try {
			if(StringUtils.isNotBlank(JIRA_URL) || StringUtils.isNotEmpty(JIRA_URL)){
				// Construct the JRJC client
				logger.info(String.format("Logging in to %s with username '%s' and password '%s'", JIRA_URL,
						JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD));
				uri = new URI(JIRA_URL);
				JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

				client = factory.createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME,
						JIRA_ADMIN_PASSWORD);
			}
			
			// Invoke the JRJC Client
			Promise<User> promise = client.getUserClient().getUser(JIRA_ADMIN_USERNAME);
			user = promise.claim();

			TreeSet<String> set = new TreeSet<String>();
			set.add("*all");
			ArrayList<String> userEmailList = new ArrayList<String>();
			userEmailList.add(user.getEmailAddress());
			
			// JIRA_USER_SEARCH is search user on behalf of the user.
			// User X can search his results or User Y assigned results.
			Promise<SearchResult> searchJqlPromise = client.getSearchClient().searchJql(
					"assignee in ("+JIRA_USER_SEARCH+") ORDER BY priority DESC, updated DESC", MAX_RESULTS, START_AT, set);
			fstream = new OutputStreamWriter(new FileOutputStream(FILE_PATH+"\\"+FILE_NAME), StandardCharsets.UTF_8);
			logger.info(searchJqlPromise.claim().getTotal() + "");
			for (Issue issue : searchJqlPromise.claim().getIssues()) {
				logger.info(issue.getKey() + " : " + issue.getId());
				fstream.write(issue.getKey() + ":" + issue.getId());
				fstream.write(System.getProperty( "line.separator" ));
				fstream.flush();
			}
			if (null != fstream) {
				fstream.close();
			}
			logger.info(String.format("Your admin user's email address is: %s\r\n",
					user.getEmailAddress() + " : " + " : " + user.getDisplayName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

}
