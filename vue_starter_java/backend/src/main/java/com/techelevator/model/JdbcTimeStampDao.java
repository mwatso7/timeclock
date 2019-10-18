package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


@Component
public class JdbcTimeStampDao implements TimeStampDao{
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JdbcTimeStampDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private TimeStamp mapRowToTimeStamp(SqlRowSet row) {
		TimeStamp timeStamp = new TimeStamp();
		timeStamp.setId(row.getInt("id"));
		timeStamp.setStamp(row.getTimestamp("stamp").toLocalDateTime());
		timeStamp.setUsername(row.getString("username"));
		timeStamp.setIsIn(row.getBoolean("isin"));
		return timeStamp;
	}

	@Override
	public List<TimeStamp> getAllTimeStamps() {
		List<TimeStamp> theTimeStamps = new ArrayList<TimeStamp>();

		String sqlGetAllTimeStamps = "SELECT * FROM timestamps ORDER BY id DESC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllTimeStamps);

		while (results.next()) {
			TimeStamp theTimeStamp = mapRowToTimeStamp(results);
			theTimeStamps.add(theTimeStamp);
		}
		return theTimeStamps;
	}

	@Override
	public TimeStamp getTimeStampById(int id) {
		TimeStamp theTimeStamp = new TimeStamp();
		String sqlSelectAllTimeStamps = "SELECT * FROM timestamps WHERE id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectAllTimeStamps, id);
		if (results.next()) {
		theTimeStamp = mapRowToTimeStamp(results);
		}
		return theTimeStamp;
	}

	@Override
	public List<TimeStamp> getTimeStampsByUsername(String username) {
		List<TimeStamp> theTimeStamps = new ArrayList<TimeStamp>();
		String sqlSelectAllTimeStamps = "SELECT * FROM timestamps WHERE username = ? ORDER BY id DESC";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectAllTimeStamps, username);
		
		while (results.next()) {
			TimeStamp theTimeStamp = mapRowToTimeStamp(results);
			theTimeStamps.add(theTimeStamp);
		}
		return theTimeStamps;
	}

	@Override
	public boolean saveTimeStamp(TimeStamp timeStamp) {
		try{
			String sqlInsertTimeStamp = "INSERT INTO timestamps(username, isin) VALUES (?,?)";
			jdbcTemplate.update(sqlInsertTimeStamp, timeStamp.getUsername(), timeStamp.isIsIn());
		} catch (Exception e){
			return false;
		}
		return true;
	}

	@Override
	public TimeStamp getLastTimeStampByUsername(String username) {
		TimeStamp theTimeStamp = new TimeStamp();
		String sqlSelectTimeStamp = "SELECT * FROM timestamps WHERE username = ? ORDER BY id DESC LIMIT 1";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSelectTimeStamp, username);
		
		if (results.next()) {
			theTimeStamp = mapRowToTimeStamp(results);
		}
		return theTimeStamp;
	}
}
