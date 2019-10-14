/**
 * @author Jp
 *
 */
package com.radiant.microservices.email;

import static com.radiant.microservices.common.Constants.COMMA;
import static com.radiant.microservices.common.Constants.SMTP_AUTH_PWD;
import static com.radiant.microservices.common.Constants.SMTP_AUTH_USER;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.radiant.microservices.util.StringUtil;

public class EmailManager {
  protected transient final Log log = LogFactory.getLog(getClass());
  public static Properties properties = null;
  private static EmailManager emailManager = null;
  private static String smtphost = null;
  private static Authenticator auth = null;
  private static Session session = null;
  private static String from = null;
  private static Transport transport = null;
  
  //========================================================================
  
  // Making this class as a single-ton class.
  private EmailManager() {}
  
  //========================================================================
  
  public static synchronized EmailManager getInstance() {
    
    if(emailManager == null) {
      emailManager = new EmailManager();
      
      if(init()) {
        return emailManager;
      } else {
        return null;
      }
    } 
    return emailManager;
  }
  
  //========================================================================
  
  private static boolean init() {
    InputStream inputStream = null;
    
    try {
      // Load the properties file
      properties = new Properties();
      inputStream = EmailManager.class.getClassLoader().getResourceAsStream("mail.properties");
      properties.load(inputStream);
      from = properties.getProperty("mail.login.username");
      smtphost = properties.getProperty("mail.smtp.host");
      SMTP_AUTH_USER = (String) properties.get("mail.login.username");
      SMTP_AUTH_PWD = (String) properties.get("mail.login.password");
      
      auth = new SMTPAuthenticator();
      session = Session.getDefaultInstance(properties, auth);
      transport = session.getTransport("smtp");
      transport.connect(smtphost, SMTP_AUTH_USER, SMTP_AUTH_PWD);
    } catch (Exception e) {
      //log.error(e);
      return false;
    } finally {
      if(inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          //log.error(e);
        }
      }
    }
    return true;
  }

  //========================================================================
  
  public boolean postMail(String recipients, String subject, String message) throws MessagingException {
    log.info("START of the method postMail");
    boolean debug = false;
    boolean mailStatus = false;
    Message msg = null;
    Address fromAddress = null;
    Address[] toAddresses = null;
    String[] toAddressesArray = null;
    Address toAddress = null;
    
    try {
      if (properties != null && recipients != null && subject != null && message != null) {
        
        if(session == null) {
          session = Session.getDefaultInstance(properties, auth);
        }
        
        if(transport == null) {
          transport = session.getTransport("smtp");
             transport.connect(smtphost, SMTP_AUTH_USER, SMTP_AUTH_PWD);
          }
        
        if(transport != null && !(transport.isConnected())) {
          transport.connect(smtphost, SMTP_AUTH_USER, SMTP_AUTH_PWD);
        }
        
        if(session != null && transport != null && transport.isConnected()) {
          session.setDebug(debug);
          // create a message
          msg = new MimeMessage(session);
          // set the from and to address
          fromAddress = new InternetAddress(from);
          
          if(fromAddress != null) {
            msg.setFrom(fromAddress);
            toAddressesArray = recipients.split(COMMA);
            
            if(toAddressesArray != null && toAddressesArray.length > 0) {
              toAddresses = new Address[toAddressesArray.length];
              int toAddressCount = 0;
              
              for (int i = 0; i < toAddressesArray.length; i++) {
                if(StringUtil.isNotNull(toAddressesArray[i])) {
                  toAddress = new InternetAddress(toAddressesArray[i].trim());
                  toAddresses[toAddressCount] = toAddress;
                  toAddressCount++;
                }
              }
              msg.setRecipients(RecipientType.TO, toAddresses);

              // Setting the Subject and Content Type
              msg.setSubject(subject);
              msg.setText(message);
              
              try {
                msg.saveChanges();   // don't forget this
                transport.sendMessage(msg, msg.getAllRecipients());
                mailStatus = true;
              } catch (Exception e) {
                log.error(e);
              }
            }
          } 
        } else {
          log.error("Session is null");
        }
      } else {
        log.error("Props are null");
      }
    } catch (Exception e) {
      log.error("PROBLEM in the method postMail");
      log.error(e);
    }
    log.info("END of the method postMail");
    return mailStatus;
  }

  //========================================================================
  
  //SimpleAuthenticator is used to do simple authentication when the SMTP server requires it.
  private static class SMTPAuthenticator extends javax.mail.Authenticator {
    public PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
    }
  }
  
  //========================================================================
  
}