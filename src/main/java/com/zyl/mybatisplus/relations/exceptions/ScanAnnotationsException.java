package com.zyl.mybatisplus.relations.exceptions;

public class ScanAnnotationsException extends RuntimeException {
	private String msg;

	public ScanAnnotationsException(String msg) {
		this.msg = msg;
	}

	public ScanAnnotationsException(String msg, Object object) {
		super(object.toString());
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}
}
