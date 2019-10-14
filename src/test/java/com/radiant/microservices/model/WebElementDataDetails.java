/**
 * @author Jp
 *
 */
package com.radiant.microservices.model;

import java.io.Serializable;
import java.util.List;

public class WebElementDataDetails implements Serializable {
  private static final long serialVersionUID = -1475018287372497267L;
  private int dataSetNo;
  private boolean execute;
  private List<String> dataSet;

  public int getDataSetNo() {
    return dataSetNo;
  }

  public void setDataSetNo(int dataSetNo) {
    this.dataSetNo = dataSetNo;
  }

  public boolean isExecute() {
    return execute;
  }

  public void setExecute(boolean execute) {
    this.execute = execute;
  }

  public List<String> getDataSet() {
    return dataSet;
  }

  public void setDataSet(List<String> dataSet) {
    this.dataSet = dataSet;
  }

}
