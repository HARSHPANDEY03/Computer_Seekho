package com.example.dto;

public class ClosureReasonRequest {
	private String closureReasonDesc;

	public ClosureReasonRequest() {
	}

	public ClosureReasonRequest(String closureReasonDesc) {
		this.closureReasonDesc = closureReasonDesc;
	}

	public String getClosureReasonDesc() {
		return closureReasonDesc;
	}

	public void setClosureReasonDesc(String closureReasonDesc) {
		this.closureReasonDesc = closureReasonDesc;
	}
}