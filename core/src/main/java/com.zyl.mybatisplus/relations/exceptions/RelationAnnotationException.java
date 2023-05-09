package com.zyl.mybatisplus.relations.exceptions;

public class RelationAnnotationException extends RuntimeException {
	private String msg;

	public RelationAnnotationException(String msg) {
		this.msg = msg;
	}

	public RelationAnnotationException(String msg, Object object) {
		super(object.toString());
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}
}
