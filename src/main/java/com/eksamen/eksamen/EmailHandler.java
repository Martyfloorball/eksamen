package com.eksamen.eksamen;

import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailHandler {
  private final String username = "developerteam1234@gmail.com";
  private final String password = "Test1379";
  Session session;

  public EmailHandler() {


    Properties props = new Properties();

    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.sfmtp.port", "587");

    session = Session.getInstance(props,
      new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });
  }

  public void message() {
    try {

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress("from-email@gmail.com"));
      message.setRecipients(Message.RecipientType.TO,
        InternetAddress.parse("developerteam1234@gmail.com"));
      message.setSubject("spam");
      message.setText("Dear Mail Crawler,"
        + "\n\n No spam to my email, please! HEj martin");

      Transport.send(message);


      System.out.println("Done");

    } catch (MessagingException e) {
      // throw new RuntimeException(e);
    }
  }
}

