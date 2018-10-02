package com.epamx.sharingIsCaring.endpoints;

public class Result {

  private String result;

  public Result(String result) {
  	this.result = result;
  }

  public Result() {
  }

  public String getResult() {
    return this.result;
  }

  public void setResult(String result) {
    this.result = result;
  }
}
