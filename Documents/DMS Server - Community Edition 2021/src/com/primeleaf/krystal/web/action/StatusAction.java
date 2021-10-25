package com.primeleaf.krystal.web.action;

/**
 * @author Saumil Shah
 *
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.web.view.Error404View;
import com.primeleaf.krystal.web.view.StatusView;
import com.primeleaf.krystal.web.view.WebView;

public class StatusAction implements Action {

	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
			if(request.getMethod().equalsIgnoreCase("POST")){
				Connection connection = ConnectionPoolManager.getInstance().getConnection();
				ResultSet rsHits = null;
				Statement stmt = connection.createStatement();
				StringBuffer hitListQuery =  new StringBuffer("SELECT COUNT(*) as noOfDoc from DOCUMENTS");
				rsHits = stmt.executeQuery(hitListQuery.toString());
				rsHits.next();
				request.setAttribute("DOCS",rsHits.getInt("noOfDoc"));
				hitListQuery =  new StringBuffer("SELECT COUNT(*) as noOfUser from USERS WHERE LOGGEDIN='Y'");
				rsHits = stmt.executeQuery(hitListQuery.toString());
				rsHits.next();
				request.setAttribute("USERS",rsHits.getInt("noOfuser"));
				connection.close();
				return (new StatusView(request, response));
			}else {
				return (new Error404View(request, response));
			}
	}

}
