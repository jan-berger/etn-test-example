package com.etnetera.hr.rest;

/**
 * General error in JSON response.
 *
 * @author Berger
 */
public class SimpleError {

	private String message;

	private String detail;

	public SimpleError(String message) {
		this.message = message;
	}

	public SimpleError(String message, String detail) {
		this.message = message;
		this.detail = detail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
