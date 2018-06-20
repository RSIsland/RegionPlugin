package com.ecconia.rsisland.plugin.region.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.ecconia.rsisland.plugin.region.FormattedLogger;

public class DBHelpers
{
	private final FormattedLogger logger;
	private final String prefix;
	
	private Connection connection;
	
	//ID's
	private Map<String, Integer> worldIDs = new HashMap<>(); 
	
	public DBHelpers(FormattedLogger logger, String prefix)
	{
		this.logger = logger;
		this.prefix = prefix;
	}
	
	public void setupConnection(Connection connection) throws SQLException
	{
		this.connection = connection;
	}
	
	public Integer getWorldID(String worldName) throws SQLException
	{
		Integer id = worldIDs.get(worldName);
		
		if(id == null)
		{
			String getID = "SELECT ID FROM " + prefix + "_worlds WHERE name='" + worldName + "'";
			ResultSet rs = returnQuery(getID);
			if(rs.first())
			{
				id = rs.getInt(1);
				worldIDs.put(worldName, id);
			}
		}
		
		return id;
	}
	
	public int getOrSetWorldID(String worldName) throws SQLException
	{
		Integer id = getWorldID(worldName);
		
		if(id == null)
		{
			try
			{
				String insert = "INSERT INTO " + prefix + "_worlds (name) VALUES ('" + worldName + "');";
				sendQuery(insert);
				ResultSet rs = returnQuery("SELECT LAST_INSERT_ID();");
				rs.first();
				id = rs.getInt(1);
			}
			catch (SQLException e)
			{
				logger.error("Error while inserting world into DB.");
				throw e;
			}
		}
		
		return id;
	}
	
	public ResultSet returnQuery(String query) throws SQLException
	{
		Statement stmt = null;
		
		try
		{
			stmt = connection.createStatement();
			return stmt.executeQuery(query);
		}
		catch (SQLException e)
		{
			logger.error("Return query failed: ", query);
			throw e;
		}
	}
	
	public void sendQuery(String query) throws SQLException
	{
		Statement stmt = null;
		
		try
		{
			stmt = connection.createStatement();
			stmt.execute(query);
		}
		catch (SQLException e)
		{
			logger.error("Query failed: ", query);
			throw e;
		}
	}
}
