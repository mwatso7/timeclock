package com.techelevator.model;

import java.util.List;

public interface TimeStampDao {
	
	public List<TimeStamp> getAllTimeStamps();
	
	public TimeStamp getTimeStampById(int id);
	
	public List<TimeStamp> getTimeStampsByUsername(String username);
	
	public TimeStamp getLastTimeStampByUsername(String username);
	
	public boolean saveTimeStamp(TimeStamp timeStamp);
	
}
