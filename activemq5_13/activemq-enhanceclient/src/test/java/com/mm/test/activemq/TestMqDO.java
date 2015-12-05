package com.mm.test.activemq;

import java.io.Serializable;

public class TestMqDO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private int age;

	
	public TestMqDO(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return super.toString() + "___" + this.getName() + "___"
				+ this.getAge();
	}

}
