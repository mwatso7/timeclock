package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcRecipientDao implements RecipientDao{
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JdbcRecipientDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private Recipient mapRowToRecipient(SqlRowSet row) {
		Recipient recipient = new Recipient();
		recipient.setId(row.getInt("id"));
		recipient.setEmail(row.getString("email"));
		recipient.setUsername(row.getString("username"));
		return recipient;
	}

	@Override
	public List<Recipient> getRecipientsByUsername(String username) {
		List<Recipient> recipients = new ArrayList<Recipient>();
		String sqlSelectAllRecipients = "SELECT * FROM recipients WHERE username = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectAllRecipients, username);
		
		while (results.next()) {
			Recipient recipient = mapRowToRecipient(results);
			recipients.add(recipient);
		}
		return recipients;
	}

	@Override
	public boolean saveRecipient(Recipient recipient) {
		try{
			String sqlInsertRecipient = "INSERT INTO recipients(email, username) VALUES (?,?)";
			jdbcTemplate.update(sqlInsertRecipient, recipient.getEmail(), recipient.getUsername());
		} catch (Exception e){
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteRecipient(int id) {
		try {
			String deleteFromRecipients = "DELETE FROM recipients WHERE id = ?";
			jdbcTemplate.update(deleteFromRecipients, id);
		}catch (Exception e) {
			return false;
		}
		return true;
	}

}
