package com.primeleaf.krystal.web.action;

/**
 * @author Saumil Shah
 *
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;

import com.primeleaf.krystal.model.ConnectionPoolManager;
import com.primeleaf.krystal.security.PasswordService;
import com.primeleaf.krystal.util.ConfigParser;
import com.primeleaf.krystal.util.EmailMessage;
import com.primeleaf.krystal.web.view.Error404View;
import com.primeleaf.krystal.web.view.ForgotPasswordView;
import com.primeleaf.krystal.web.view.InvalidUserView;
import com.primeleaf.krystal.web.view.NewPasswordView;
import com.primeleaf.krystal.web.view.WebView;

public class ForgotPasswordAction implements Action {

	public WebView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(ConfigParser.PasswordReset()) {
			if(request.getMethod().equalsIgnoreCase("POST")){//if post request then reset password using parameters
				Connection connection = ConnectionPoolManager.getInstance().getConnection();
				String query = "Select Count(*) as IsUser from USERS where USERNAME = ? and USEREMAIL = ?";
				PreparedStatement myStmt = connection.prepareStatement(query);
	        	myStmt.setString(1, request.getParameter("txtLoginId").toUpperCase());
	        	myStmt.setString(2, request.getParameter("txtEmail"));
	 
	        	ResultSet rSet = myStmt.executeQuery();
	        	rSet.next();
	        	
				if(rSet.getInt("IsUser")==1) {
					StringBuilder pwd = new StringBuilder(RandomStringUtils.random( 10, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$&" ));
					pwd.setCharAt(new Random().nextInt(10),'@');
					query = "Update USERS SET PASSWORD = ? where USERNAME = ? and USEREMAIL = ?";
					myStmt = connection.prepareStatement(query);
		        	myStmt.setString(2, request.getParameter("txtLoginId").toUpperCase());
		        	myStmt.setString(3, request.getParameter("txtEmail"));
		        	myStmt.setString(1, PasswordService.getInstance().encrypt(pwd.toString()));
		        	int res = myStmt.executeUpdate();
		        	
		        	if(res>0) {
		        	
						EmailMessage emailMessage = new EmailMessage();
						emailMessage.setSubject("Password Reset");
						emailMessage.setFrom(ConfigParser.getSmtpUsername());
		
						StringBuffer message = new StringBuffer();
						message.append("Hello,");
						emailMessage.setTo(request.getParameter("txtEmail"));
						message.append("<h1>Here Is your New password</h1>");
						message.append("<p>Please Don't Disclose your password.</p><br/>");
						
						message.append("<p>Message from : SYSTEM </p>");
						message.append("<p><i> Your New Password is : "+pwd+" <br/> use This to Login and  and change your password from My Profile</i></p>");
		
						message.append("<p>Thankyou,<br/>DMS Administrator</p>");
						message.append("<small>If you didn't reset the password , then kindlly contact Administrator immediately,<br/>DMS Administrator</small>");
						emailMessage.setMessage(message.toString());
						emailMessage.send();	
						connection.close();
						return new NewPasswordView(request, response);
		        	}else {
		        		connection.close();
		        		return new Error404View(request, response);
		        	}
				}else {
					connection.close();
					return new InvalidUserView(request, response);
				}
			}else {//if not post request then send to view
				return (new ForgotPasswordView(request, response));
			}
		}else {
			return new Error404View(request, response);
		}
	}

}
