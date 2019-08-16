package com.zc.erpext.entity;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 4325129064406258324L;
	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
