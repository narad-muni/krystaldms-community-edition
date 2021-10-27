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

package com.primeleaf.krystal.web.action.console;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.GenericValidator;

import com.primeleaf.krystal.constants.HTTPConstants;
import com.primeleaf.krystal.model.AccessControlManager;
import com.primeleaf.krystal.model.AuditLogManager;
import com.primeleaf.krystal.model.DocumentManager;
import com.primeleaf.krystal.model.dao.DocumentClassDAO;
import com.primeleaf.krystal.model.vo.AuditLogRecord;
import com.primeleaf.krystal.model.vo.DocumentClass;
import com.primeleaf.krystal.model.vo.DocumentRevision;
import com.primeleaf.krystal.model.vo.IndexDefinition;
import com.primeleaf.krystal.model.vo.User;
import com.primeleaf.krystal.security.ACL;
import com.primeleaf.krystal.util.FileUploadProgressListener;
import com.primeleaf.krystal.web.action.Action;
import com.primeleaf.krystal.web.view.WebView;
import com.primeleaf.krystal.web.view.console.NewDocumentView;

/**
 * Author Rahul Kubadia
 */

public class NewDocumentAction implements Action {
	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User loggedInUser = (User)session.getAttribute(HTTPConstants.SESSION_KRYSTAL);
		String classId = request.getParameter("classid")!=null? request.getParameter("classid"):"0";
		if(request.getMethod().equalsIgnoreCase("POST")){
			Part filePart = request.getPart("fileDocument");
			try{
				String userName = loggedInUser.getUserName();
				String sessionid = (String)session.getId();
				String tempFilePath = System.getProperty("java.io.tmpdir");
				if ( !(tempFilePath.endsWith("/") || tempFilePath.endsWith("\\")) ){
					tempFilePath += System.getProperty("file.separator");
				}
				tempFilePath+=  userName+"_"+sessionid;
				//variables
				String fileName="",ext="",comments="";
				FileUploadProgressListener listener = new FileUploadProgressListener();
				session.setAttribute("LISTENER", listener);
				session.setAttribute("UPLOAD_ERROR", null);
				session.setAttribute("UPLOAD_PERCENT_COMPLETE", Long.valueOf(0));

				DocumentClass documentClass = null;

				Hashtable<String,String> indexRecord = new Hashtable<String,String>();
				String name="";
				String value="";
				
				classId = request.getParameter("classid")!=null? request.getParameter("classid"):"0";
				comments = request.getParameter("txtNote");
				fileName = filePart.getSubmittedFileName();
				ext = fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();	
				InputStream tempIS = filePart.getInputStream();
//				java.nio.file.Files.copy(
//					      tempIS, 
//					      file.toPath(), 
//					      StandardCopyOption.REPLACE_EXISTING);
				if( tempIS.available() <= 0 )	{ //code for checking minimum size of file
					session.setAttribute("UPLOAD_ERROR","Zero length document");
					return null;
				}
				documentClass = DocumentClassDAO.getInstance().readDocumentClassById(Integer.parseInt(classId));
				if(documentClass == null){
					session.setAttribute("UPLOAD_ERROR","Invalid document class");
					return null;
				}
				AccessControlManager aclManager = new AccessControlManager();
				ACL acl = aclManager.getACL(documentClass, loggedInUser);

				if(! acl.canCreate()){
					session.setAttribute("UPLOAD_ERROR","Access Denied");
					return null;
				}

				String indexValue="";
				String indexName="";
				session.setAttribute("UPLOAD_PERCENT_COMPLETE",  Long.valueOf(50));
				
				Collection<Part> indexParts = request.getParts();

				for(IndexDefinition indexDefinition : documentClass.getIndexDefinitions()){
					indexName=indexDefinition.getIndexColumnName();
					for(Part iter1 : indexParts) {
						Part item1 = iter1;
						if (item1.getName()!="classid" && item1.getName()!="fileDocument"){
							name = item1.getName();
							value = IOUtils.toString(item1.getInputStream(),HTTPConstants.CHARACTER_ENCODING);
							if(name.equals(indexName)){
								indexValue=value;
								String errorMessage = "";
								if(indexValue != null){
									if(indexDefinition.isMandatory()){
										if(indexValue.trim().length() <=0){
											errorMessage ="Invalid input for "  + indexDefinition.getIndexDisplayName();
											session.setAttribute("UPLOAD_ERROR",errorMessage);
											return null;
										}
									}
									if(IndexDefinition.INDEXTYPE_NUMBER.equalsIgnoreCase(indexDefinition.getIndexType())){
										if(indexValue.trim().length() > 0){
											if(!GenericValidator.matchRegexp(indexValue, HTTPConstants.NUMERIC_REGEXP)){
												errorMessage ="Invalid input for "  + indexDefinition.getIndexDisplayName();
												session.setAttribute("UPLOAD_ERROR",errorMessage);
												return null;
											}
										}
									}else if(IndexDefinition.INDEXTYPE_DATE.equalsIgnoreCase(indexDefinition.getIndexType())){
										if(indexValue.trim().length() > 0){
											if(!GenericValidator.isDate(indexValue, "yyyy-MM-dd",true)){
												errorMessage = "Invalid input for "  + indexDefinition.getIndexDisplayName();
												session.setAttribute("UPLOAD_ERROR",errorMessage);
												return null;
											}
										}
									}
									if (indexValue.trim().length() > indexDefinition.getIndexMaxLength()){ //code for checking index field length
										 errorMessage = 	"Document index size exceeded for " +
												"Index Name : " +
												indexDefinition.getIndexDisplayName() + " [ " +
												"Index Length : " + indexDefinition.getIndexMaxLength() + " , " +
												"Actual Length : " + indexValue.length() + " ]" ;
										session.setAttribute("UPLOAD_ERROR",errorMessage);
										return null;
									}
								}
								indexRecord.put(indexName,indexValue);
							}
						}
					}
				}//while indexCfgList
				session.setAttribute("UPLOAD_PERCENT_COMPLETE", Long.valueOf(75));

				DocumentRevision documentRevision = new DocumentRevision();
				documentRevision.setClassId(documentClass.getClassId());
				documentRevision.setDocumentId(0);
				documentRevision.setRevisionId("1.0");
				documentRevision.setDocumentFile(tempIS);
				documentRevision.setUserName(loggedInUser.getUserName());
				documentRevision.setIndexRecord(indexRecord);
				documentRevision.setComments(comments);

				DocumentManager documentManager = new DocumentManager();
				documentManager.storeDocument(documentRevision, documentClass,ext);

				//Log the entry to audit logs 
				AuditLogManager.log(new AuditLogRecord(
						documentRevision.getDocumentId(),
						AuditLogRecord.OBJECT_DOCUMENT,
						AuditLogRecord.ACTION_CREATED,
						userName,
						request.getRemoteAddr(),
						AuditLogRecord.LEVEL_INFO,
						"",
						"Document created"));

				session.setAttribute("UPLOAD_PERCENT_COMPLETE",  Long.valueOf(100));
			}catch (Exception e) {
				e.printStackTrace(System.out);
			}
			return null;
		}else{
			try{
				ArrayList<DocumentClass> availableDocumentClasses = DocumentClassDAO.getInstance().readDocumentClasses(" ACTIVE = 'Y'");
				ArrayList<DocumentClass> documentClasses = new ArrayList<DocumentClass>();
				AccessControlManager aclManager = new AccessControlManager();
				for(DocumentClass documentClass : availableDocumentClasses){
					ACL acl = aclManager.getACL(documentClass, loggedInUser);
					if(acl.canCreate()){
						documentClasses.add(documentClass);
					}
				}
				int documentClassId = 0;
				try{
					documentClassId = Integer.parseInt(classId);
				}catch(Exception ex){
					request.setAttribute(HTTPConstants.REQUEST_ERROR, "Invalid input");
					return (new NewDocumentView(request, response));
				}
				if(documentClassId > 0 ){
					DocumentClass selectedDocumentClass = DocumentClassDAO.getInstance().readDocumentClassById(documentClassId);
					request.setAttribute("DOCUMENTCLASS", selectedDocumentClass);
				}
				request.setAttribute("CLASSID", documentClassId);
				request.setAttribute("CLASSLIST", documentClasses);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return (new NewDocumentView(request, response));
	}
}

