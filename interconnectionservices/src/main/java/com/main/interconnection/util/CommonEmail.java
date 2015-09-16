package com.main.interconnection.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;

import org.springframework.beans.factory.annotation.Value;



public class CommonEmail extends Authenticator {
	
	@Value("${mail.username}")
	String mailUserName;
	
	@Value("${mail.password}")
	String mailPassword;
	
	Properties props = null;
	
	public Properties  getProPerty(){
		props = new Properties();		
		props.put("mail.smtp.host", "mail.miracleglobal.com");
	//	props.put("mail.smtp.socketFactory.port", "465");
	//	props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		return props;
	}
	
	
public void sendEmail(String emailTemplatePath, HashMap<String, String>params , String subject , String toEmail){
	try{
		
		Message message = new MimeMessage(this.session);
		
		message.setFrom(new InternetAddress("no-reply@Waspit.com"));
		message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse(toEmail));
		message.setSubject(subject);
		
		message.setContent(this.getEmailBody(emailTemplatePath, params),"text/html");
		
		Transport.send(message);
		
		
	}catch (Exception e) {
		e.printStackTrace();
	}
}
	
	
	
	Session session = Session.getDefaultInstance(this.getProPerty(),	new javax.mail.Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(mailUserName,mailPassword);
				}
			});
	
	
	public String getEmailBody(String emailTemplatePath, HashMap<String, String> param)
			throws IOException {
		String emailBody = null;
		File file = null;			
		
		file = new File(emailTemplatePath);				
		FileInputStream fin = new FileInputStream(file);
		byte[] bytes = new byte[(int) file.length()];
		fin.read(bytes);
		emailBody = new String(bytes);
		for(Map.Entry<String, String> entry : param.entrySet()){
			emailBody = emailBody.replaceAll(entry.getKey(), entry.getValue());
		}					
		return emailBody;
	}	
}
