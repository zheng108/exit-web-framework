package org.exitsoft.project.vcsadmin.service.account;

public enum RememberMeType {
	
	Day(1 * 24 * 60 * 60 * 60),
	Week(7 * 24 * 60 * 60 * 60),
	Month(30 * 24 * 60 * 60 * 60),
	Year(365 * 24 * 60 * 60 * 60);
	
	RememberMeType(int value) {
		this.value = value;
	}
	
	private int value;

	public int getValue() {
		return value;
	}
	
	
}
