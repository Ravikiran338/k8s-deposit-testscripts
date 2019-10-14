/**
 * @author Jp
 *
 */
package com.radiant.microservices.reports;

import static com.radiant.microservices.common.Constants.DATE_FORMAT;
import static com.radiant.microservices.common.Constants.DATE_TIME;
import static com.radiant.microservices.common.Constants.PASS;
import static com.radiant.microservices.common.Constants.TEST_REPORT_MAIL_MESSAGE_CONTENT;
import static com.radiant.microservices.common.Constants.TEST_REPORT_MAIL_RECIPIENTS;
import static com.radiant.microservices.common.Constants.TEST_REPORT_MAIL_SUBJECT;
import static com.radiant.microservices.common.Constants.TEST_REPORT_MAIL_TEMPLATE_PROPS_FILE;
import static com.radiant.microservices.common.Constants.TEST_SUITE_NAME;
import static com.radiant.microservices.common.Constants.TOTAL_NO_OF_TEST_CASES_FAILED;
import static com.radiant.microservices.common.Constants.TOTAL_NO_OF_TEST_CASES_PASSED;
import static com.radiant.microservices.common.Constants.TOTAL_NO_OF_TEST_CASES_RUN;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.radiant.microservices.db.TestCaseDetails;
import com.radiant.microservices.db.TAFDBManagerHelper;
import com.radiant.microservices.db.TestSuiteDetails;
import com.radiant.microservices.email.EmailManager;

public class ReportsManager {
  protected transient final Log log = LogFactory.getLog(getClass());
  private static ReportsManager manager = null;
  
  //==========================================================================
  
  public static synchronized ReportsManager getInstance() {
    if(manager == null) {
      manager = new ReportsManager();
    }
    return manager;
  }
  
  //==========================================================================
  
  private ReportsManager() { }

  //==========================================================================
  
  public void sendTestReport(TestSuiteDetails testSuiteDetails) {
    log.info("START of the method sendTestReport");
    List<TestCaseDetails> testCases = null;
    String subject = null;
    String messageContent = null;
    int testCasesRun = 0;
    int testCasesPass = 0;
    int testCasesFail = 0;
    MailTemplateDetails mailTemplateDetails = null;
    String dateTime = null;
    DateFormat dateFormat = null;
    
    try {
      if(testSuiteDetails != null) {
        testCases = TAFDBManagerHelper.getInstance().getTestCases(testSuiteDetails.getTestSuiteDetailsId());
        
        if(testCases != null && testCases.size() > 0) {
          testCasesRun = testCases.size();
         
          for(TestCaseDetails testCaseDetails : testCases) {
            if(testCaseDetails.getStatus().equalsIgnoreCase(PASS)) {
              testCasesPass = testCasesPass + 1;
            } else {
              testCasesFail = testCasesFail + 1;
            }
          }
        }
        mailTemplateDetails = getMailTemplateDetails();
        
        if(mailTemplateDetails != null) {
          dateFormat = new SimpleDateFormat(DATE_FORMAT);
          dateTime = dateFormat.format(testSuiteDetails.getDateTime());
          subject = mailTemplateDetails.getSubject();
          subject = subject.replaceAll(TEST_SUITE_NAME, testSuiteDetails.getTestSuiteName());
          messageContent = mailTemplateDetails.getMessageContent();
          messageContent = messageContent.replaceAll(DATE_TIME, dateTime);
          messageContent = messageContent.replaceAll(TOTAL_NO_OF_TEST_CASES_RUN, String.valueOf(testCasesRun));
          messageContent = messageContent.replaceAll(TOTAL_NO_OF_TEST_CASES_PASSED, String.valueOf(testCasesPass));
          messageContent = messageContent.replaceAll(TOTAL_NO_OF_TEST_CASES_FAILED, String.valueOf(testCasesFail));
          
          EmailManager.getInstance().postMail(mailTemplateDetails.getRecipients(), subject, messageContent);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    log.info("END of the method sendTestReport");
  }
  
  //==========================================================================
  
  class MailTemplateDetails {
    private String recipients;
    private String subject;
    private String messageContent;
    
    public String getRecipients() {
      return recipients;
    }
    public void setRecipients(String recipients) {
      this.recipients = recipients;
    }
    public String getSubject() {
      return subject;
    }
    public void setSubject(String subject) {
      this.subject = subject;
    }
    public String getMessageContent() {
      return messageContent;
    }
    public void setMessageContent(String messageContent) {
      this.messageContent = messageContent;
    }
  }
  
  //==========================================================================
  
  private MailTemplateDetails getMailTemplateDetails() {
    MailTemplateDetails mailTemplateDetails = new MailTemplateDetails();
    ResourceBundle rb = null;
    
    try {
      rb = ResourceBundle.getBundle(TEST_REPORT_MAIL_TEMPLATE_PROPS_FILE);
      mailTemplateDetails.setRecipients(rb.getString(TEST_REPORT_MAIL_RECIPIENTS));
      mailTemplateDetails.setSubject(rb.getString(TEST_REPORT_MAIL_SUBJECT));
      mailTemplateDetails.setMessageContent(rb.getString(TEST_REPORT_MAIL_MESSAGE_CONTENT));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mailTemplateDetails;
  }
  
  //===========================================================================
}
