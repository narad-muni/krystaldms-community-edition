package com.primeleaf.krystal.web.controller;

/**
 * @author Saumil Shah
 *
 */

import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.model.dao.UserDAO;
import com.primeleaf.krystal.model.vo.User;

public class ConnectionManagerController implements  HttpSessionBindingListener {

	public void valueBound(HttpSessionBindingEvent event) {
		User user;
		try {
			user = UserDAO.getInstance().readUser(event.getName());
			try {
		    	Connection connection = ConnectionPoolManager.getInstance().getConnection();
				Statement stmt = connection.createStatement();
				StringBuffer hitListQuery =  new StringBuffer("Update USERS set LOGGEDIN='Y' Where USERID="+user.getUserId());
				stmt.execute(hitListQuery.toString());
				connection.close();
				user.setLoggedIn(false);
		    	}catch(Exception e){
		    		System.out.println(e);
		    	}
			user.setLoggedIn(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void valueUnbound(HttpSessionBindingEvent event) {
		User user;
		try {
			user = UserDAO.getInstance().readUser(event.getName());
			try {
		    	Connection connection = ConnectionPoolManager.getInstance().getConnection();
				Statement stmt = connection.createStatement();
				StringBuffer hitListQuery =  new StringBuffer("Update USERS set LOGGEDIN='N' Where USERID="+user.getUserId());
				stmt.execute(hitListQuery.toString());
				connection.close();
				user.setLoggedIn(false);
		    	}catch(Exception e){
		    		System.out.println(e);
		    	}
			user.setLoggedIn(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	}