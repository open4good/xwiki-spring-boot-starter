package org.open4goods.xwiki.config;


/**
 * handles path for endpoints
 * 
 * @author Thierry.Ledan
 */
public class XWikiResourcesPath {

	public static final String WIKIS_PATH = "wikis";
	public static final String SPACES_PATH = "spaces";
	public static final String PAGES_PATH = "pages";
	public static final String OBJECTS_PATH = "objects";
	public static final String URI_SEPARATOR = "/";
	
	public static final String ADMIN_SPACE = "XWiki";
	
	// classes
	public static final String USERS_CLASNAME = "XWiki.XWikiUsers";
	public static final String GROUPS_CLASNAME = "XWiki.XWikiGroups";
	public static final String RIGHTS_CLASNAME = "XWiki.XWikiRights";
	// query
	public static final String QUERY_USERS = "query?q=object:" + USERS_CLASNAME;
	public static final String QUERY_GROUPS = "query?q=object:" + GROUPS_CLASNAME;	
		

	
	private String baseUrl;
	private String apiEntryPoint;
	private String wikiName;
	
	public XWikiResourcesPath(String baseUrl, String apiEntryPoint, String wikiName) {
		this.baseUrl = baseUrl;
		this.apiEntryPoint = apiEntryPoint;
		this.wikiName = wikiName;
	}
	
	/**
	 * Return endpoint to wiki
	 * returned path ends with '/' 
	 * @return
	 */
	public String getWikisEndpoint() {
		return this.apiEntryPoint +
				URI_SEPARATOR +
				WIKIS_PATH +
				URI_SEPARATOR +
				this.wikiName +
				URI_SEPARATOR;
	}

	/**
	 * path to 'space'. 
	 * returned path ends with '/' 
	 * @param space
	 * @return
	 */
	public String getSpacesEndpoint() {
		return getWikisEndpoint() +
				SPACES_PATH +
				URI_SEPARATOR;
	}
	
	/**
	 * path to 'space'. 
	 * returned path ends with '/' 
	 * @param space
	 * @return
	 */
	public String getSpaceEndpoint( String space ) {
		return getSpacesEndpoint() +
				space +
				URI_SEPARATOR;
	}
	

	/**
	 * path to 'page' related to 'space'. 
	 * returned path ends with '/' 
	 * @param space
	 * @param page
	 * @return
	 */
	public String getPagesEndpoint( String space ) {
		return getSpaceEndpoint(space) +
				PAGES_PATH +
				URI_SEPARATOR ;
	}
	
	/**
	 * path to 'page' related to 'space'. 
	 * returned path ends with '/' 
	 * @param space
	 * @param page
	 * @return
	 */
	public String getPageEndpoint( String space, String page ) {
		return getPagesEndpoint(space) +
				page +
				URI_SEPARATOR;
	}
	

	public String getGroupsEndpoint() {
		return this.apiEntryPoint +
				URI_SEPARATOR +
				WIKIS_PATH +
				URI_SEPARATOR +
				QUERY_GROUPS;
	}
	
	public String getGroupUsers(String groupPageName) {
		return getPageEndpoint(ADMIN_SPACE, groupPageName) + OBJECTS_PATH;
	}
	
	public String getUsersEndpoint() {
		return this.apiEntryPoint +
				URI_SEPARATOR +
				WIKIS_PATH +
				QUERY_USERS;
	}
	
	public String getUserEndpoint( String userName ) {
		return getPageEndpoint(ADMIN_SPACE, userName);
	}
	
}
