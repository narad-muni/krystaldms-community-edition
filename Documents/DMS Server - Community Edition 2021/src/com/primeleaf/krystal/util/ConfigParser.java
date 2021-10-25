package com.primeleaf.krystal.util;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.primeleaf.krystal.constants.ServerConstants;
import com.primeleaf.krystal.security.PasswordService;

public class ConfigParser {
	
	static JSONObject jObject = null;
	static String KRYSTAL_HOME = null;
	
	public static void init() {
		KRYSTAL_HOME = System.getProperty("krystal.home");
		if (KRYSTAL_HOME == null) {
			KRYSTAL_HOME = System.getProperty("user.dir");
			System.setProperty("krystal.home", KRYSTAL_HOME);
		}
		try {
			jObject = (JSONObject) new JSONParser().parse(new FileReader(KRYSTAL_HOME+"/settings/config.json"));
		}catch(Exception e) {
			System.out.println(e);
		}
		ServerConstants.SYSTEM_MASTER_PASSWORD = PasswordService.getInstance().encrypt((String)jObject.get("MASTER_PASSWORD"));
	}
	
	public static int getPort() {
		try {
			return Integer.valueOf((String) jObject.get("PORT"));
		}catch(Exception e){
			return 8080;
		}
	}
	
	public static Boolean multiLogin() {
		try {
			if(((String)jObject.get("MULTI_LOGIN")).equals("y")) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	public static Boolean PasswordReset() {
		try {
			if(((String)jObject.get("ALLOW_PASSWORD_RESET")).equals("y")) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	public static Boolean AllowMasterPassword() {
		try {
			if(((String)jObject.get("MASTER_PASSWORD")).equals("y")) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	public static int InactiveTimeout() {
		try {
			return Integer.valueOf((String) jObject.get("INACTIVE_TIMEOUT"))*60;
		}catch(Exception e){
			return 300;
		}
	}
	
	public static String getSmtpPort() {
		try {
			return (String) jObject.get("SMTP_PORT");
		}catch(Exception e){
			return "587";
		}
	}
	
	public static Boolean getSmtpTls() {
		try {
			if(((String)jObject.get("SMTP_TLS")).equals("y")) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e){
			return true;
		}
	}
	
	public static String getSmtpHost() {
		try {
			return (String) jObject.get("SMTP_HOST");
		}catch(Exception e){
			return "smtp.gmail.com";
		}
	}
	
	public static String getSmtpUsername() {
		try {
			return (String) jObject.get("SMTP_USERNAME");
		}catch(Exception e){
			return "yourgmail@gmail.com";
		}
	}
	
	public static String getSmtpPassword() {
		try {
			return (String) jObject.get("SMTP_PASSWORD");
		}catch(Exception e){
			return "SeceretePassword";
		}
	}
}


//Default Settings
//{
//	"PORT":"8085",
//	"MULTI_LOGIN":"y",
//	"ALLOW_MASTER_PASSWORD":"n",
//	"INACTIVE_TIMEOUT":"5",
//	"MASTER_PASSWORD":"KRYSTALDMS",
//	"ALLOW_PASSWORD_RESET":"y",
//	
//	"SMTP_HOST":"smtp.gmail.com",
//	"SMTP_TLS":"y",
//	"SMTP_PORT":"587",
//	"SMTP_USERNAME":"saumilsunilshahtechguru@gmail.com",
//	"SMTP_PASSWORD":"hbgsmhgvdqubsyey"
//}

//for smtp password generate app password for google and enable 2 factor authentication
//or disable less secure apps and use normal password

