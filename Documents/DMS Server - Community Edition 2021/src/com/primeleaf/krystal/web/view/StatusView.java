package com.primeleaf.krystal.web.view;

/**
 * @author Saumil Shah
 *
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StatusView extends WebView {
	
	public StatusView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	
	public void render() throws Exception{
		printLogout();
	}
		
	private void printLogout() throws Exception{
		
		out.print("{"
				+ "\"USERS\":"+request.getAttribute("USERS")
				+ ",\"DOCUMENTS\":"+request.getAttribute("DOCS")
				+ "}");
	}



}
