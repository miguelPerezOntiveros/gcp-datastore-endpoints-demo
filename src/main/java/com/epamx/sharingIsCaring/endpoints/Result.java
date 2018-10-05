package com.epamx.sharingIsCaring.endpoints;

public class Result {
	public Result() {}
	public Result(String result) { this.result = result;}

	String result;
	public String getResult() { return this.result; }
	public void setResult(String result) { this.result = result; }
}
