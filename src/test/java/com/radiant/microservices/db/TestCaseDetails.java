/**
 * @author Jp
 */
package com.radiant.microservices.db;
import java.sql.Timestamp;
import static com.radiant.microservices.common.Constants.FAIL;

public class TestCaseDetails implements java.io.Serializable {
  private static final long serialVersionUID = 4209909580462307220L;
  private long testCaseDetailsId;
  private String testCaseName;
  private String methodName;
  private String status = FAIL;
  private String errorMessage;
  private String customMessage;
  private String screenShotLocation;
  private Timestamp dateTime;
  private long testSuiteDetailsId;
  
  public long getTestCaseDetailsId() {
    return testCaseDetailsId;
  }
  public void setTestCaseDetailsId(long testCaseDetailsId) {
    this.testCaseDetailsId = testCaseDetailsId;
  }
  public String getTestCaseName() {
    return testCaseName;
  }
  public void setTestCaseName(String testCaseName) {
    this.testCaseName = testCaseName;
  }
  public String getMethodName() {
    return methodName;
  }
  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  
  public String getCustomMessage() {
    return customMessage;
  }
  public void setCustomMessage(String customMessage) {
    this.customMessage = customMessage;
  }
  public String getErrorMessage() {
    return errorMessage;
  }
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
  public String getScreenShotLocation() {
    return screenShotLocation;
  }
  public void setScreenShotLocation(String screenShotLocation) {
    this.screenShotLocation = screenShotLocation;
  }
  public Timestamp getDateTime() {
    return dateTime;
  }
  public void setDateTime(Timestamp dateTime) {
    this.dateTime = dateTime;
  }
  
  public long getTestSuiteDetailsId() {
    return testSuiteDetailsId;
  }
  
  public void setTestSuiteDetailsId(long testSuiteDetailsId) {
    this.testSuiteDetailsId = testSuiteDetailsId;
  }
}