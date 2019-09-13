package com.techelevator.model;

import java.time.LocalDateTime;

public class TimeStamp {
	/*
	 *  Member Data
	 */
	
	private int id;
	private LocalDateTime stamp;
	private String username;
	private boolean isIn;
	
	/*
	 *  Methods: Getters & Setters
	 */
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public LocalDateTime getStamp() {
		return stamp;
	}
	
	public void setStamp(LocalDateTime stamp) {
		this.stamp = stamp;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public boolean isIsIn() {
		return isIn;
	}
	
	public void setIsIn(boolean isIn) {
		this.isIn = isIn;
	}

}
