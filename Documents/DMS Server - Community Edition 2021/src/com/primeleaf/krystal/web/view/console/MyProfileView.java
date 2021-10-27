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

package com.primeleaf.krystal.web.view.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.vo.HitList;
import com.primeleaf.krystal.util.StringHelper;
import com.primeleaf.krystal.web.view.WebPageTemplate;
import com.primeleaf.krystal.web.view.WebView;


/**
 * @author Rahul Kubadia
 *
 */
public class MyProfileView extends WebView {

	public MyProfileView (HttpServletRequest request, HttpServletResponse response) throws Exception{
		init(request, response);
	}

	public void render() throws Exception{
		WebPageTemplate template = new WebPageTemplate(request,response);
		template.generateHeader();
		printMyProfle();
		template.generateFooter();
	}
	private void printBreadCrumbs() throws Exception{
		out.println("<ol class=\"breadcrumb\">");
		out.println("<li class=\"breadcrumb-item\"><a href=\"/console\">My Workspace</a></li>");
		out.println("<li class=\"breadcrumb-item active\">My Profile</li>");
		out.println("</ol>");
	}
	
	private void printMyProfle() throws Exception{
		printBreadCrumbs();

		if(request.getAttribute(HTTPConstants.REQUEST_ERROR) != null){
			printErrorDismissable((String) request.getAttribute(HTTPConstants.REQUEST_ERROR));
		}
		if(request.getAttribute(HTTPConstants.REQUEST_MESSAGE) != null){
			printSuccessDismissable((String) request.getAttribute(HTTPConstants.REQUEST_MESSAGE));
		}
		String showThumbNail = loggedInUser.getShowThumbNail();

		out.println("<ul class=\"nav nav-tabs\">");
		out.println("<li class=\"nav-item\"><button class=\"nav-link p-2 active\" href=\"#profile\" data-bs-toggle=\"tab\"><i class=\"h6 bi bi-person-badge me-1 text-primary\"></i>My Profile</button></li>");
		out.println("<li class=\"nav-item\"><button class=\"nav-link p-2\" href=\"#preferences\" data-bs-toggle=\"tab\"><i class=\"h6 bi bi-sliders me-1 text-primary\"></i>Preferences</button></li>");
		out.println("<li class=\"nav-item\"><button class=\"nav-link p-2\" href=\"#changepassword\" data-bs-toggle=\"tab\"><i class=\"h6 bi bi-key me-1 text-primary\"></i>Change Password</button></li>");
		out.println("</ul>");

		out.println("<div class=\"tab-content\">"); 

		out.println("<div class=\"tab-pane fade ms-5 ps-5 active show\" id=\"profile\">");
		out.println("<p>&nbsp;</p>");
		out.println("<form action=\"/console/updateprofilepicture\" method=\"post\" id=\"frmMyProfile\" class=\"form-horizontal\" enctype=\"multipart/form-data\">");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label class=\"col-sm-3 col-form-label fw-lighter\">User Name </label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<p class=\"form-control-plaintext fw-bold\">"+loggedInUser.getUserName()+"</p>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label class=\"col-sm-3 col-form-label fw-lighter\">Real Name </label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<p class=\"form-control-plaintext fw-bold\">"+StringEscapeUtils.escapeHtml4(loggedInUser.getRealName())+"</p>");
		out.println("</div>");
		out.println("</div>");
		
		
		out.println("<div class=\"row\">");
		out.println("<label class=\"col-sm-3 col-form-label fw-lighter\">My Profile Picture</label>");
		out.println("<div class=\"col-sm-1\">");
		out.println("<div class=\"profile\">");
		out.println("<a href=\"javascript:void(0);\" id=\"profilePic\">");
		out.println("<img src=\"/console/profilepicture?username="+loggedInUser.getUserName()+"\" class=\"img-thumbnail darkMode\"/>");
		out.println("<span class=\"overlay d-flex align-items-center\"><span id=\"profilepiccontent\" style=\"text-align:center\">Change Profile Picture</span></span>");
		out.println("</a>");
		out.println("</div>");
		out.println("<input class=\"btn-check\"  type=\"file\" name=\"profilepicture\" id=\"profilepicture\" class=\"required\" title=\"Select Profile Picture\" style=\"display:none;\">");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label class=\"col-sm-3 col-form-label fw-lighter\">Email ID</label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<p class=\"form-control-plaintext fw-bold\">"+loggedInUser.getUserEmail()+"</p>");
		out.println("</div>");
		out.println("</div>");
		
		String lastLogin = loggedInUser.getLastLoginDate()!=null?StringHelper.formatDate(loggedInUser.getLastLoginDate()):"0000-00-00 00:00:00";

		out.println("<div class=\"mb-3 row\">");
		out.println("<label class=\"col-sm-3 col-form-label fw-lighter\">Last Login Date</label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<p class=\"form-control-plaintext fw-bold\">"+lastLogin+"</p>");
		out.println("</div>");
		out.println("</div>");

		out.println("</form>");
		out.println("</div>");


		out.println("<div class=\"tab-pane fade in\" id=\"preferences\">");
		out.println("<p>&nbsp;</p>");
		out.println("<div id=\"resultPreferences\"></div>");
		out.println("<form action=\"/console/preferences\" method=\"post\" id=\"frmPreferences\" form-type=\"ajax\" datatarget=\"#resultPreferences\" onsubmit=\"updateMode()\" class=\"form-horizontal\">");
		out.println("<div class=\"card   \">");
		out.println("<div class=\"card-header\"><h4>Hitlist Preferences</h4></div>");
		out.println("<div class=\"card-body\">");
		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"cmbHitListSize\" class=\"col-sm-3 col-form-label\">No of Hits per page</label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<select class=\"form-control\" name=\"cmbHitListSize\">");
		out.println("<option value=\"5\""); 	if(loggedInUser.getHitlistSize() == 5 ){ out.print(" selected");} out.println(">5</option>");
		out.println("<option value=\"10\""); 	if(loggedInUser.getHitlistSize() == 10 ){ out.print(" selected");} out.println(">10</option>");
		out.println("<option value=\"25\""); 	if(loggedInUser.getHitlistSize() == 25 ){ out.print(" selected");} out.println(">25</option>");
		out.println("<option value=\"50\""); 	if(loggedInUser.getHitlistSize() == 50 ){ out.print(" selected");} out.println(">50</option>");
		out.println("<option value=\"100\""); 	if(loggedInUser.getHitlistSize() == 100 ){ out.print(" selected");} out.println(">100</option>");
		out.println("</select>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"radCreatedOn\" class=\"col-sm-3 col-xs-2 col-form-label\">Created On</label>");
		out.println("<div class=\"btn-group col me-5\">");
		out.println("<input class=\"btn-check\" type=\"radio\" id=\"radCreatedOn1\" name=\""+HitList.META_CREATED+"\"  value=\"TRUE\"");if(loggedInUser.getMetaPreferences().isCreatedVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"radCreatedOn1\" class=\"btn btn-outline-dark\">Yes</label>");
		out.println("<input class=\"btn-check\" type=\"radio\" id=\"radCreatedOn2\" name=\""+HitList.META_CREATED+"\"  value=\"FALSE\"");if(!loggedInUser.getMetaPreferences().isCreatedVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"radCreatedOn2\" class=\"btn btn-outline-dark\">No</label>");
				
		out.println("</div>");

		out.println("<label for=\"radActive\" class=\"col-sm-3 col-xs-2 col-form-label\">Created By</label>");
		out.println("<div class=\"btn-group col me-5\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"btn-check\" type=\"radio\" id=\"radActive1\" name=\""+HitList.META_CREATEDBY+"\" value=\"TRUE\"");if(loggedInUser.getMetaPreferences().isCreatedByVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"radActive1\" class=\"btn btn-outline-dark \">Yes</label>");
		out.println("<input class=\"btn-check\" type=\"radio\" id=\"radActive2\" name=\""+HitList.META_CREATEDBY+"\"  value=\"FALSE\"");if(!loggedInUser.getMetaPreferences().isCreatedByVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"radActive2\" class=\"btn btn-outline-dark \">No</label>");
		out.println("</div>");
		
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"modifiedOn\" class=\"col-sm-3 col-xs-2 col-form-label\">Last Modified On</label>");
		out.println("<div class=\"btn-group col me-5\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"modifiedOn1\" name=\""+HitList.META_MODIFIED+"\" value=\"TRUE\"");if(loggedInUser.getMetaPreferences().isModifiedVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"modifiedOn1\" class=\"btn btn-outline-dark \">Yes</label>");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"modifiedOn2\" name=\""+HitList.META_MODIFIED+"\"  value=\"FALSE\"");if(!loggedInUser.getMetaPreferences().isModifiedVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"modifiedOn2\" class=\"btn btn-outline-dark \">No</label>");
		out.println("</div>");

		out.println("<label for=\"modifiedBy\" class=\"col-sm-3 col-xs-2 col-form-label\">Last Modified By</label>");
		out.println("<div class=\"btn-group col me-5\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"modifiedBy1\" name=\""+HitList.META_MODIFIEDBY+"\" value=\"TRUE\"");if(loggedInUser.getMetaPreferences().isModifiedByVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"modifiedBy1\" class=\"btn btn-outline-dark \">Yes</label>");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"modifiedBy2\" name=\""+HitList.META_MODIFIEDBY+"\"  value=\"FALSE\"");if(!loggedInUser.getMetaPreferences().isModifiedByVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"modifiedBy2\" class=\"btn btn-outline-dark \">No</label>");
		out.println("</div>");
		out.println("</div>");

		
		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"fileSize\" class=\"col-sm-3 col-xs-2 col-form-label\">File Size</label>");
		out.println("<div class=\"btn-group col me-5\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"fileSize1\" name=\""+HitList.META_LENGTH+"\" value=\"TRUE\"");if(loggedInUser.getMetaPreferences().isFileSizeVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"fileSize1\" class=\"btn btn-outline-dark \">Yes</label>");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"fileSize2\" name=\""+HitList.META_LENGTH+"\"  value=\"FALSE\"");if(!loggedInUser.getMetaPreferences().isFileSizeVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"fileSize2\" class=\"btn btn-outline-dark \">No</label>");
		out.println("</div>");
		
		/*
		 * Dark Mode Added By Saumil Shah
		 * on 25 August 2021
		 * */
		
		out.println("<label for=\"Mode\" class=\"col-sm-3 col-xs-2 col-form-label\">Dark Mode</label>");
		out.println("<div class=\"btn-group col me-5\">");
		out.println("<input class=\"btn-check\" type=\"radio\" id=\"darkMode\" name=\"Mode\" value=\"TRUE\">");
		out.println("<label for=\"darkMode\" class=\"btn btn-outline-dark\">Yes</label>");
		out.println("<input class=\"btn-check\" type=\"radio\" id=\"lightMode\" name=\"Mode\"  value=\"FALSE\">");
		out.println("<label for=\"lightMode\" class=\"btn btn-outline-dark\">No</label>");
		out.println("</div>");
		out.println("</div>");


		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"documentId\" class=\"col-sm-3 col-xs-2 col-form-label\">Document ID</label>");
		out.println("<div class=\"btn-group col me-5\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"documentId1\" name=\""+HitList.META_DOCUMENTID+"\" value=\"TRUE\"");if(loggedInUser.getMetaPreferences().isDocumentIdVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"documentId1\" class=\"btn btn-outline-dark \">Yes</label>");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"documentId2\" name=\""+HitList.META_DOCUMENTID+"\"  value=\"FALSE\"");if(!loggedInUser.getMetaPreferences().isDocumentIdVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"documentId2\" class=\"btn btn-outline-dark \">No</label>");
		out.println("</div>");

		out.println("<label for=\"revisionId\" class=\"col-sm-3 col-xs-2 col-form-label\">Revision ID</label>");
		out.println("<div class=\"btn-group col me-5\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"revisionId1\" name=\""+HitList.META_REVISIONID+"\" value=\"TRUE\"");if(loggedInUser.getMetaPreferences().isRevisionIdVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"revisionId1\" class=\"btn btn-outline-dark \">Yes</label>");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"revisionId2\" name=\""+HitList.META_REVISIONID+"\"  value=\"FALSE\"");if(!loggedInUser.getMetaPreferences().isRevisionIdVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"revisionId2\" class=\"btn btn-outline-dark \">No</label>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"expiryOn\" class=\"col-sm-3 col-xs-2 col-form-label\">Expiry On</label>");
		out.println("<div class=\"btn-group col me-5\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"expiryOn1\" name=\""+HitList.META_EXPIRYON+"\" value=\"TRUE\"");if(loggedInUser.getMetaPreferences().isExpiryOnVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"expiryOn1\" class=\"btn btn-outline-dark \">Yes</label>");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"expiryOn2\" name=\""+HitList.META_EXPIRYON+"\"  value=\"FALSE\"");if(!loggedInUser.getMetaPreferences().isExpiryOnVisible()) {out.print(" checked");}out.print(">");
		out.println("<label for=\"expiryOn2\" class=\"btn btn-outline-dark \">No</label>");
		out.println("</div>");

		out.println("<label for=\"radThumbNail\" class=\"col-sm-3 col-xs-2 col-form-label\">Show Thumbnail in Viewer</label>");
		out.println("<div class=\"btn-group col me-5\" data-bs-toggle=\"buttons\">");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"radThumbNail1\" name=\"radThumbNail\" value=\"TRUE\"");if( "TRUE".equalsIgnoreCase(showThumbNail)) {out.print(" checked");}out.print(">");
		out.println("<label for=\"radThumbNail1\" class=\"btn btn-outline-dark \">Yes</label>");
		out.println("<input class=\"btn-check\"  type=\"radio\" id=\"radThumbNail2\" name=\"radThumbNail\"  value=\"FALSE\"");if("FALSE".equalsIgnoreCase(showThumbNail)) {out.print(" checked");}out.print(">");
		out.println("<label for=\"radThumbNail2\" class=\"btn btn-outline-dark \">No</label>");
		out.println("</div>");
		out.println("</div>");
		
		out.println("<hr/>");
		out.println("<div class=\"mb-3 row\">");
		out.println("<div class=\"col-sm-offset-3 col-sm-9\">");
		out.println("<input class=\"btn btn-sm btn-dark\" type=\"submit\"  value=\"Submit\" name=\"btnSubmit\">&nbsp;");
		out.println("</div>");
		out.println("</div>");
		
		out.println("</div>");//card-body
		out.println("</div>");//panel
		
		out.println("</form>");
		out.println("</div>");


		out.println("<div class=\"tab-pane fade in\" id=\"changepassword\">");
		out.println("<p>&nbsp;</p>");
		out.println("<div id=\"resultChangePassword\"></div>");
		out.println("<form action=\"/console/changepassword\" method=\"post\" id=\"frmChangePassword\" form-type=\"ajax\" datatarget=\"#resultChangePassword\" class=\"form-horizontal\">");
		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"txtOldPassword\" class=\"col-sm-3 col-form-label\">Current Password <span style='color:red'>*</span></label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<div class=\"form-floating\">");
		out.println("<input class=\"form-control required\"  type=\"password\" maxlength=\"30\" name=\"txtOldPassword\" id=\"txtOldPassword\" class=\"form-control required\" autocomplete=\"off\" placeholder=\"Current Password\" title=\"Please enter password\"><label for=\"txtOldPassword\">Current Password</label>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"txtNewPassword\" class=\"col-sm-3 col-form-label\">New Password <span style='color:red'>*</span></label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<div class=\"form-floating\">");
		out.println("<input class=\"form-control required\"  type=\"password\" maxlength=\"30\" name=\"txtNewPassword\" id=\"txtNewPassword\" class=\"form-control required complexPassword\" placeholder=\"New Password\" minlength=\"8\" autocomplete=\"off\"><label for=\"txtNewPassword\">New Password</label>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");

		out.println("<div class=\"mb-3 row\">");
		out.println("<label for=\"txtConfirmPassword\" class=\"col-sm-3 col-form-label\">Confirm Password <span style='color:red'>*</span></label>");
		out.println("<div class=\"col-sm-9\">");
		out.println("<div class=\"form-floating\">");
		out.println("<input  type=\"password\" maxlength=\"30\" name=\"txtConfirmPassword\" id=\"txtConfirmPassword\" class=\"form-control required\" equalTo= \"#txtNewPassword\" placeholder=\"Confirm Password\" autocomplete=\"off\" title=\"Password must match\"><label for=\"txtConfirmPassword\">Confirm Password</label>");
		out.println("</div>");
		out.println("</div>");
		out.println("</div>");

		out.println("<hr/>");
		out.println("<div class=\"mb-3 row\">");
		out.println("<div class=\"col-sm-offset-3 col-sm-9\">");
		out.println("<input class=\"btn btn-sm btn-dark\" type=\"submit\"  value=\"Submit\" name=\"btnSubmit\">&nbsp;");
		out.println("</div>");
		out.println("</div>");
		out.println("</form>");
		out.println("</div>");
		out.println("</div>");
	}
}

