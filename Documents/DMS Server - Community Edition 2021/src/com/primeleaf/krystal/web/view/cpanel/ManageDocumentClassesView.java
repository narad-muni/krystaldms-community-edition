/**
 * Created On 05-Jan-2014
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

/**
 * Created on 05-Jan-2014
 *
 * Copyright 2003-09 by Primeleaf Consulting (P) Ltd.,
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

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;

/**
 * @author Rahul Kubadia
 *
 */

public class ManageDocumentClassesView extends WebView {
	public ManageDocumentClassesView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}
	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printDocumentClasses();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/cpanel\">Control Panel</a></li>");
		out.println("<li class=\"breadcrumb-item active\">Manage Document Classes</li>");
		out.println("</ol>");
	}
	@SuppressWarnings("unchecked")
	private void printDocumentClasses() throws Exception{
		printBreadCrumbs();
		
		out.println("<div class=\"card   \">");
		out.println("<div class=\"card-header\">");
		out.println("<div class=\"d-flex justify-content-between\">");
		out.println("<div class=\"col-xs-6\">");
		out.println("<i class=\"bi text-primary bi-folder2-open \"></i> Manage Document Classes");
		out.println("</div>");
		out.println("<div class=\"col-xs-6 text-end\">");
		out.println("<a href=\"/cpanel/newdocumentclass\">Add Document Class</a>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
		out.println("<div class=\"card-body\">");
		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String)request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String)request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		
		try {
			ArrayList<DocumentClass> documentClassList = (ArrayList<DocumentClass>) request.getAttribute("CLASSLIST");
			if(documentClassList.size() > 0 ){
				int count = 0;
				out.println("<div class=\"row\">");
				for (DocumentClass documentClass : documentClassList) {
					count++;
					out.println("<div class=\"col-sm-4\">");
					out.println("<div class=\"card\">");
					out.println("<div class=\"card-body\">");
					out.println("<h4 class=\"text-primary\">" + StringEscapeUtils.escapeHtml4(documentClass.getClassName()) + "</h4>");
					out.println("<h5>" + StringEscapeUtils.escapeHtml4(documentClass.getClassDescription()) + "</h5>");
					out.println("<p>");
					out.println("Active ");
					if(documentClass.isVisible()){
						out.println("&nbsp;&nbsp; <i class=\"bi bi-flag text-success\"></i>");
					}else{
						out.println("&nbsp;&nbsp; <i class=\"bi bi-flag \"></i>");
					}
					out.println("&nbsp;&nbsp;"); 
					out.println("Version Control");
					if(documentClass.isRevisionControlEnabled()){
						out.println("&nbsp;&nbsp; <i class=\"bi bi-flag  text-success\"></i>");
					}else{
						out.println("&nbsp;&nbsp; <i class=\"bi bi-flag \"></i>");
					}
					out.println("</p>");
					
					out.println("</div>"); //card-body
					out.println("<div class=\"card-footer\">");
					out.println("<a href=\""+HTTPConstants.BASEURL+"/cpanel/editdocumentclass?classid="+ documentClass.getClassId()+"\">Edit</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/cpanel/deletedocumentclass?classid="+ documentClass.getClassId()+"\"  title=\"Are you sure, you want to permanently delete this Document Class?\" class=\"confirm\">Delete</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/cpanel/classindexes?classid="+ documentClass.getClassId() + "\" title=\"Manage Indexes\">Manage Indexes</a>");
					out.println(" | <a href=\""+HTTPConstants.BASEURL+"/cpanel/permissions?classid="+ documentClass.getClassId() + "\" title=\"Manage Permissions\">Manage Permissions</a>");
					out.println("</div>"); //card-footer
					out.println("</div>"); //panel
					out.println("</div>");//col-sm-4
					
					if(count % 3 == 0){
						out.println("</div><div class=\"row\">");//row
					}
				}// for
				out.println("</div>");
			}else{
				out.println("Currently there are no document classes available in the system.");
			}
			out.println("</div>");//card-body
			out.println("</div>");//panel
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
