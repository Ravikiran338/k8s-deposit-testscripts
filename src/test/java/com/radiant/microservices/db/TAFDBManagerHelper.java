/**
 * @author Jp
 */
package com.radiant.microservices.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.radiant.microservices.util.StringUtil;

public class TAFDBManagerHelper {
  protected transient final Log log = LogFactory.getLog(getClass());
  private static TAFDBManagerHelper tafDBManagerHelper;
  
  //========================================================================
  
  // Making this class as a single-ton class.
  private TAFDBManagerHelper() {}
  
  //========================================================================
  
  public static TAFDBManagerHelper getInstance() {
    if(tafDBManagerHelper == null) {
      return new TAFDBManagerHelper();
    } else {
      return tafDBManagerHelper;
    }
  }
  
  //========================================================================
  
  public TestSuiteDetails saveTestSuiteDetails(String testSuiteName) {
    log.info("START of the method saveTestSuiteDetails");
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    String insertQuery = null;
    TAFDBConnectionManager dbConnectionManager = null;
    long timeStamp = 0;
    String testSuiteUnqId = null;
    TestSuiteDetails testSuiteDetails = null;
    
    try {
      if(StringUtil.isNotNull(testSuiteName)) {
        dbConnectionManager = TAFDBConnectionManager.getInstance();
            
        if(dbConnectionManager != null) {
          connection = dbConnectionManager.getPooledConnection();
                
          if(connection != null) {
            insertQuery = "insert into test_suite_details(test_suite_unq_id, test_suite_name, date_time ,iteration_details_id )";
            insertQuery = insertQuery.concat(" values(?, ?, ? , ?)");
            preparedStatement = connection.prepareStatement(insertQuery);
                    
            if(preparedStatement != null) {
              timeStamp = System.currentTimeMillis();
              testSuiteUnqId = "TS_" + timeStamp;
              preparedStatement.setString(1, testSuiteUnqId);
              preparedStatement.setString(2, testSuiteName);
              preparedStatement.setTimestamp(3, new Timestamp(timeStamp));
              preparedStatement.setInt(4, 1);
              int noOfRecordsEffected = preparedStatement.executeUpdate();
                       
              if(noOfRecordsEffected > 0) {
                log.info("Test Suite details saved successfully");
                testSuiteDetails = getTestSuiteDetails(0, testSuiteUnqId);
              } 
            } else {
              log.info("preparedStatement object is null");
            }
          } else {
            log.info("connection object is null");
          }
        } else {
          log.info("dbConnectionManager object is null");
        }
      } else {
        log.info("testSuiteName object is null");
      }
    } catch (Exception e) {
      log.error("PROBLEM in the method: saveTestSuiteDetails in the TestMateDBConnectionManager Class");
      log.error(e);
    } finally {
      try {
        if(preparedStatement != null) {
          preparedStatement.close();
        }
        if(connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        log.error(e);
      }
    }
    log.info("END of the method saveTestSuiteDetails");
    return testSuiteDetails;
  }
  
  //========================================================================
  
  public TestSuiteDetails getTestSuiteDetails(long testSuiteDetailsId, String testSuiteUnqId) {
    log.info("START of the method getTestSuiteDetails");
    TestSuiteDetails testSuiteDetails = null;
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    String selectQuery = null;
    TAFDBConnectionManager dbConnectionManager = null;
    
    try {
      dbConnectionManager = TAFDBConnectionManager.getInstance();
    
      if(dbConnectionManager != null) {
        connection = dbConnectionManager.getPooledConnection();
        
        if(connection != null) {
          statement = connection.createStatement();
            
          if(statement != null) {
            /*select * from test_suite_details as tsd where 
            tsd.test_suite_details_id=1;
            tsd.test_suite_unq_id='TS_1405331011162';*/
            selectQuery = "SELECT * FROM test_suite_details as tsd where ";
            if(StringUtil.isNotNull(testSuiteUnqId)) {
              selectQuery = selectQuery.concat(" tsd.test_suite_unq_id='"+testSuiteUnqId+"'");
            } else {
              selectQuery = selectQuery.concat(" tsd.test_suite_details_id="+testSuiteDetailsId);    
            }
            resultSet = statement.executeQuery(selectQuery);
                
            if(resultSet != null) {
              while (resultSet.next()) {
                testSuiteDetails = new TestSuiteDetails();
                testSuiteDetails.setTestSuiteDetailsId(resultSet.getLong("test_suite_details_id"));
                testSuiteDetails.setTestSuiteUnqName(resultSet.getString("test_suite_unq_id"));
                testSuiteDetails.setTestSuiteName(resultSet.getString("test_suite_name"));
                testSuiteDetails.setDateTime(resultSet.getTimestamp("date_time"));
              }
            } else {
              log.info("result set is null");
            }
          } else {
            log.info("Statement is null");
          }
        } else {
          log.info("Unable to get the pooled connection");
        }
      } else {
        log.info("Unable to get the dbConnectionManager object");
      }
    } catch (Exception e) {
      log.error("PROBLEM in the method: getTestSuiteDetails in the TestMateDBConnectionManager Class");
      log.error(e);
    } finally {
      try {
        if(resultSet != null) {
          resultSet.close();
        }
        if(statement != null) {
          statement.close();
        }
        if(connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        log.error(e);
      }
    }
    log.info("END of the method getTestSuiteDetails");
    System.out.println(testSuiteDetails.getTestSuiteDetailsId());
    return testSuiteDetails;
  }
  
  //========================================================================
  
  public boolean saveTestCaseDetails(TestCaseDetails testCaseDetails) {
    log.info("START of the method saveTestCaseDetails");
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    String insertQuery = null;
    TAFDBConnectionManager dbConnectionManager = null;
    long timeStamp = 0;
    boolean isTestCaseDetailsSaved = false;
    String errorMessage = null;
    String customMessage = null;
    
    try {
      if(areTestCaseDetailsMandatoryFieldsProvided(testCaseDetails)) {
        dbConnectionManager = TAFDBConnectionManager.getInstance();
            
        if(dbConnectionManager != null) {
          connection = dbConnectionManager.getPooledConnection();
                
          if(connection != null) {
            insertQuery = "insert into test_case_details(test_case_name, method_name, status, error_message, custom_message, screen_shot_location, date_time, test_suite_details_id )";
            insertQuery = insertQuery.concat(" values(?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement = connection.prepareStatement(insertQuery);
                    
            if(preparedStatement != null) {
              timeStamp = System.currentTimeMillis();
              preparedStatement.setString(1, testCaseDetails.getTestCaseName());
              preparedStatement.setString(2, testCaseDetails.getMethodName());
              preparedStatement.setString(3, testCaseDetails.getStatus());
              errorMessage = testCaseDetails.getErrorMessage();
              errorMessage = StringUtil.getSubString(errorMessage, 250);
              preparedStatement.setString(4, errorMessage);
              customMessage = testCaseDetails.getCustomMessage();
              customMessage = StringUtil.getSubString(customMessage, 250);
              preparedStatement.setString(5, customMessage);
              preparedStatement.setString(6, testCaseDetails.getScreenShotLocation());
              preparedStatement.setTimestamp(7, new Timestamp(timeStamp));
              preparedStatement.setLong(8, testCaseDetails.getTestSuiteDetailsId());
              int noOfRecordsEffected = preparedStatement.executeUpdate();
                       
              if(noOfRecordsEffected > 0) {
                log.info("Test case details saved successfully");
                isTestCaseDetailsSaved = true;
              } 
            } else {
              log.info("preparedStatement object is null");
            }
          } else {
            log.info("connection object is null");
          }
        } else {
          log.info("dbConnectionManager object is null");
        }
      } else {
        log.info("TestCaseDetails object is null");
      }
    } catch (Exception e) {
      log.error("PROBLEM in the method: saveTestCaseDetails in the TestMateDBConnectionManager Class");
      log.error(e);
    } finally {
      try {
        if(preparedStatement != null) {
          preparedStatement.close();
        }
        if(connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        log.error(e);
      }
    }
    log.info("END of the method saveTestCaseDetails");
    return isTestCaseDetailsSaved;
  }
  
  //========================================================================
  
  private boolean areTestCaseDetailsMandatoryFieldsProvided(TestCaseDetails tsd) {
    boolean status = false;
    
    if(tsd != null) {
      if(StringUtil.isNotNull(tsd.getTestCaseName()) && StringUtil.isNotNull(tsd.getMethodName()) && StringUtil.isNotNull(tsd.getStatus())) {
        status = true;
      }
    }
    return status;
  }
  
  //========================================================================
  
  public List<TestCaseDetails> getTestCases(long testSuiteDetailsId) {
    log.info("START of the method getTestCases");
    TestCaseDetails tesCaseDetails = null;
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    String selectQuery = null;
    TAFDBConnectionManager dbConnectionManager = null;
    List<TestCaseDetails> testCases = null;
    
    try {
      dbConnectionManager = TAFDBConnectionManager.getInstance();
    
      if(dbConnectionManager != null) {
        connection = dbConnectionManager.getPooledConnection();
        
        if(connection != null) {
          statement = connection.createStatement();
            
          if(statement != null) {
            /*SELECT * FROM test_case_details as tcd where tcd.test_suite_details_id=1;*/
            selectQuery = "SELECT * FROM test_case_details as tcd WHERE tcd.test_suite_details_id="+testSuiteDetailsId;
            resultSet = statement.executeQuery(selectQuery);
                
            if(resultSet != null) {
              testCases = new ArrayList<TestCaseDetails>();
              
              while (resultSet.next()) {
                tesCaseDetails = new TestCaseDetails();
                tesCaseDetails.setTestCaseDetailsId(resultSet.getLong("test_case_details_id"));
                tesCaseDetails.setTestCaseName(resultSet.getString("test_case_name"));
                tesCaseDetails.setMethodName(resultSet.getString("method_name"));
                tesCaseDetails.setStatus(resultSet.getString("status"));
                tesCaseDetails.setErrorMessage(resultSet.getString("error_message"));
                tesCaseDetails.setScreenShotLocation(resultSet.getString("screen_shot_location"));
                tesCaseDetails.setDateTime(resultSet.getTimestamp("date_time"));
                tesCaseDetails.setTestSuiteDetailsId(resultSet.getLong("test_suite_details_id"));
                testCases.add(tesCaseDetails);
              }
            } else {
              log.info("result set is null");
            }
          } else {
            log.info("Statement is null");
          }
        } else {
          log.info("Unable to get the pooled connection");
        }
      } else {
        log.info("Unable to get the dbConnectionManager object");
      }
    } catch (Exception e) {
      log.error("PROBLEM in the method: getTestCases in the TestMateDBConnectionManager Class");
      log.error(e);
    } finally {
      try {
        if(resultSet != null) {
          resultSet.close();
        }
        if(statement != null) {
          statement.close();
        }
        if(connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        log.error(e);
      }
    }
    log.info("END of the method getTestCases");
    return testCases;
  }
  
  //========================================================================
}