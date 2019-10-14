/**
 * @author Jp
 *
 */
package com.radiant.microservices.exceptions;

import static com.radiant.microservices.common.Constants.ERROR;
import static com.radiant.microservices.common.Constants.FAIL;
import static com.radiant.microservices.common.Constants.SCREEN_SHOTS_LOCATION;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.radiant.microservices.db.TestCaseDetails;
import com.radiant.microservices.screenshots.ScreenShotManager;

public class TAFException extends Throwable {
  protected transient final Log log = LogFactory.getLog(getClass());
  private static final long serialVersionUID = 2957585198043190934L;
  @SuppressWarnings("rawtypes")
  private Set m_Descriptions = new HashSet(5);
  private Exception m_Exception;

  // ==========================================================================
  
  public TAFException() {
    super();
  }

  // ==========================================================================
  
  public TAFException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  // ==========================================================================
  
  public TAFException(Throwable arg0) {
    super(arg0);
  }

  // ==========================================================================
  
  public TAFException(PrintWriter out,Throwable exp) {
    exp.printStackTrace(out); 
  }
   
  // ==========================================================================
  
  @SuppressWarnings("unchecked")
  public void addDescription(String description) {
    m_Descriptions.add(description);
  }

  // ==========================================================================
  
  @SuppressWarnings("unchecked")
  public String[] getDescriptions() {
    String[] desc = new String[m_Descriptions.size()];
    return (String[]) m_Descriptions.toArray(desc);
  }

  // ==========================================================================
  
  @SuppressWarnings("rawtypes")
  public Iterator iterator() {
    return m_Descriptions.iterator();
  }

  // ==========================================================================
  
  public TAFException setException(Exception ex) {
    m_Exception = ex;
    return this;
  }

  // ==========================================================================
  
  public boolean hasException() {
    return (m_Exception != null);
  }

  // ==========================================================================
  
  public String getExceptionTrace() {
    if (hasException()) {
      StringWriter trace = new StringWriter();
      m_Exception.printStackTrace(new PrintWriter(trace));
      return trace.toString();
    } else {
      return "";
    }
  }

  // ==========================================================================
  
  public Exception getException() {
    return m_Exception;
  }

  // ==========================================================================

  public TestCaseDetails handleException(Throwable e, TestCaseDetails testCaseDetails, String customMessage) {
    log.error("PROBLEM in the method "+testCaseDetails.getMethodName());
    testCaseDetails.setErrorMessage(e.getMessage());
    testCaseDetails.setStatus(FAIL);
    testCaseDetails.setCustomMessage(customMessage);
    
    //Local Machine Configuration
    /*String screenShotLocation = ScreenShotManager.getInstance().saveScreenShot(testCaseDetails.getMethodName(), ERROR);
    
    if(StringUtil.isNotNull(screenShotLocation) && screenShotLocation.contains("\\")) {
      screenShotLocation = screenShotLocation.replace('\\', '/');
    }*/
    
    //Server Configuration (DevOps Statging server)
    //String screenShotName = ScreenShotManager.getInstance().saveScreenShot(testCaseDetails.getMethodName(), ERROR);
    // screenShotLocation = SCREEN_SHOTS_LOCATION.concat(screenShotName);
    //testCaseDetails.setScreenShotLocation(screenShotLocation);
    e.printStackTrace();
    return testCaseDetails;
  }

  // ==========================================================================
}
