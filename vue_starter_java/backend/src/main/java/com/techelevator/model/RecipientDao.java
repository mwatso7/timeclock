package com.techelevator.model;

import java.util.List;

public interface RecipientDao {
	
	public List<Recipient> getRecipientsByUsername(String username);
	
	public boolean saveRecipient(Recipient recipient);
	
	public boolean deleteRecipient(int id);
}
