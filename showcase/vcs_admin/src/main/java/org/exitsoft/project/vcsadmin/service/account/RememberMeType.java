package org.exitsoft.project.vcsadmin.service.account;

public enum RememberMeType {
	
	Day(60 * 60 * 24 * 1),
	Week(60 * 60 * 24 * 7),
	Month(60 * 60 * 24 * 30),
	Year(60 * 60 * 24 * 365);
	
	RememberMeType(int value) {
		this.value = value;
	}
	
	private int value;

	public int getValue() {
		return value;
	}
	
	
}
