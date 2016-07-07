package com.phoneutils.crosspromotion;

import java.util.ArrayList;

public class ResponseModel {
	private boolean result;
	private String message;
	private ArrayList<AppModel> apps;

	public ResponseModel() {
	}

	public ArrayList<AppModel> getApps() {
		return apps;
	}
	public void setApps(ArrayList<AppModel> apps) {
		this.apps = apps;
	}
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
