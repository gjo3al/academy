package com.wei.email;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class GmailService implements EmailService {

	private final String CONTEXT_PATH = "http://localhost:8080/academy";

	private final String VERIFY_SUBJECT = "選課系統電子信箱驗證";
	
	private final String RESET_SUBJECT = "選課系統密碼更改";
	
	@Autowired
	private Environment environment;
	
	private String mailUser;
    private String mailPassword;
    private Properties props = new Properties();
	
	@PostConstruct
	void init() {
		mailUser = environment.getProperty("mail.mailUser");
		mailPassword = environment.getProperty("mail.mailPassword");
		props.put("mail.smtp.host", environment.getProperty("mail.smtp.host"));
        props.put("mail.smtp.auth", environment.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", environment.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.port", environment.getProperty("mail.smtp.port"));
	}
	
	@Override
	public void validationLink(String email, String token) {
		
		try {
			String link = CONTEXT_PATH + "/register/verify" + 
					String.format("?email=%s&token=%s", email, token);
			
			String anchor = String.format("<a href='%s'>驗證郵件</a>", link);
			
			String html = String.format(
			        "請按%s或複製連結至網址列:<br><br>%s", anchor, link);
			
			javax.mail.Message message = createMessage(
					mailUser, email, VERIFY_SUBJECT, html);
			
			Transport.send(message);
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void passwordResetLink(String email, String token, String password) {
		try {
			String link = CONTEXT_PATH + "/users/reset" + 
					String.format("?email=%s&token=%s", email, token);
			
			String anchor = String.format("<a href='%s'>變更密碼</a>", link);
			
			String html = String.format(
					"以下是你的新密碼:" +
					"<br>%s<br>" +
			        "為確保帳號安全，請登入後重設新密碼，按%s或複製連結至網址列前往:<br><br>%s", 
			        password, anchor, link);
			
			javax.mail.Message message = createMessage(
					mailUser, email, RESET_SUBJECT, html);
			
			Transport.send(message);
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private javax.mail.Message createMessage(
            String from, String to, String subject, String text)
                              throws MessagingException, IOException {
        Session session = Session.getDefaultInstance(props, new Authenticator() {  
            protected PasswordAuthentication getPasswordAuthentication() {  
                return new PasswordAuthentication(mailUser, mailPassword);
            }} 
        );  
        
        Multipart multiPart = multiPart(text);
        
        javax.mail.Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setContent(multiPart);
        
        return message;
    }
	
	private Multipart multiPart(String text) throws MessagingException {
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(text, "text/html;charset=UTF-8");
        
        Multipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(htmlPart);
        
        return multiPart;
    }

}
