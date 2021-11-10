/**
 * Created On 09-Jan-2014
 * Copyright 2010 by Primeleaf Consulting (P) Ltd.,
 * #29,784/785 Hendre Castle,
 * D.S.Babrekar Marg,
 * Gokhale Road(North),
 * Dadar,Mumbai 400 028
 * India
 * 
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Primeleaf Consulting (P) Ltd. ("Confidential Information").  
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Primeleaf Consulting (P) Ltd.
 */

package com.primeleaf.krystal.web.view.cpanel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */
public class ControlPanelView extends WebView {

	public ControlPanelView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printDomains();
		template.generateFooter();
	}

	private void printDomains() throws Exception{
		try{
			
			out.println("<div class=\"card\">");
			out.println("<div class=\"card-header h5\">");
			out.println("<i class=\"bi text-primary bi-speedometer2\"></i> Control Panel");
			out.println("</div>");
			
			out.println("<div class=\"card-body\"><div class=\"row\">");
			
			out.print("<div class=\"col-sm-4\">");
			out.println("<a href=\"/cpanel/users\" class=\"card p-3 pt-4\">");
			out.println("<h5><i class=\"bi text-primary bi-person\"></i> Manage Users</h5>");
			out.println("</a>");
			out.print("</div>");

			out.print("<div class=\"col-sm-4\">");
			out.println("<a href=\"/cpanel/managedocumentclasses\"class=\"card p-3 pt-4\">");
			out.println("<h5><i class=\"bi text-primary bi-folder2-open\"></i> Manage Document Classes</h5>");
			out.println("</a>");
			out.println("</div>");

			out.print("<div class=\"col-sm-4\">");
			out.println("<a href=\"/cpanel/managecheckouts\"  class=\"card p-3 pt-4\">");
			out.println("<h5><i class=\"bi text-primary bi-lock\"></i> Manage Checkouts</h5>");
			out.println("</a>");
			out.println("</div>");

			out.print("<div class=\"col-sm-4\">");
			out.println("<a href=\"/cpanel/recyclebin\"  class=\"card p-3 pt-4\">");
			out.println("<h5><i class=\"bi text-primary bi-trash\"></i> Recycle Bin</h5>");
			out.println("</a>");
			out.println("</div>");

			out.print("<div class=\"col-sm-4\">");
			out.println("<a href=\"/cpanel/reports\" class=\"card p-3 pt-4\">");
			out.println("<h5><i class=\"bi text-primary bi-bar-chart\"></i> System Reports</h5>");
			out.println("</a>");
			out.println("</div>");
			
			out.println("</div></div>");//list-group
			out.println("</div>");//panel
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

