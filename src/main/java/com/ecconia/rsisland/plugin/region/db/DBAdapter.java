package com.ecconia.rsisland.plugin.region.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import com.ecconia.rsisland.plugin.region.FormattedLogger;
import com.ecconia.rsisland.plugin.region.elements.Region;
import com.ecconia.rsisland.plugin.region.elements.Room;

public class DBAdapter extends GenericDBAdapter
{
	private final DBHelpers h;
	
	public DBAdapter(String jdbcURL, FormattedLogger logger, String prefix) throws SQLException
	{
		super(jdbcURL, logger, prefix);
		
		h = new DBHelpers(logger, prefix);
		h.setupConnection(connection);
	}
	
	@Override
	protected void createTables() throws SQLException
	{
		//TODO: add version handling
		try(Statement stmt = connection.createStatement())
		{
			stmt.executeUpdate(
				  "CREATE TABLE IF NOT EXISTS " + prefix + "_worlds"
				+ "("
				+ "    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"
				+ "    name TEXT NOT NULL"
				+ ");"
			);

			stmt.executeUpdate(
				  "CREATE TABLE IF NOT EXISTS " + prefix + "_players"
				+ "("
				+ "    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"
				+ "    uuid_l BIGINT NOT NULL,"
				+ "    uuid_h BIGINT NOT NULL,"
				+ "    UNIQUE KEY " + prefix + "_ui_players_uuid (uuid_l, uuid_h)"
				+ ");"
			);

			stmt.executeUpdate(
				  "CREATE TABLE IF NOT EXISTS " + prefix + "_regions"
				+ "("
				+ "    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"
				+ "    world INT NOT NULL,"
				+ "    name VARCHAR(256) NOT NULL,"
				+ "    CONSTRAINT " + prefix + "_fk_region_to_world FOREIGN KEY (world) REFERENCES " + prefix + "_worlds (id) ON DELETE CASCADE ON UPDATE CASCADE"
				+ ");"
			);

			stmt.executeUpdate(
				  "CREATE TABLE IF NOT EXISTS " + prefix + "_rooms"
				+ "("
				+ "    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"
				+ "    region INT NOT NULL,"
				+ "    min_x INT NOT NULL,"
				+ "    min_y INT NOT NULL,"
				+ "    min_z INT NOT NULL,"
				+ "    max_x INT NOT NULL,"
				+ "    max_y INT NOT NULL,"
				+ "    max_z INT NOT NULL,"
				+ "    CONSTRAINT " + prefix + "_fk_room_to_region FOREIGN KEY (region) REFERENCES " + prefix + "_regions (id) ON DELETE CASCADE ON UPDATE CASCADE"
				+ ");"
			);

			stmt.executeUpdate(
				  "CREATE TABLE IF NOT EXISTS " + prefix + "_region_owner"
				+ "("
				+ "	   id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"
				+ "    region INT NOT NULL,"
				+ "    owner INT NOT NULL,"
				+ "    CONSTRAINT " + prefix + "_fk_region_owner_to_player FOREIGN KEY (owner) REFERENCES " + prefix + "_players (id) ON DELETE CASCADE ON UPDATE CASCADE,"
				+ "    CONSTRAINT " + prefix + "_fk_region_owner_to_region FOREIGN KEY (region) REFERENCES " + prefix + "_regions (id) ON DELETE CASCADE ON UPDATE CASCADE"
				+ ");"
			);

			stmt.executeUpdate(
				  "CREATE TABLE IF NOT EXISTS " + prefix + "_region_member"
				+ "("
				+ "	   id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"
				+ "    region INT NOT NULL,"
				+ "    member INT NOT NULL,"
				+ "    CONSTRAINT " + prefix + "_fk_region_member_to_player FOREIGN KEY (member) REFERENCES " + prefix + "_players (id) ON DELETE CASCADE ON UPDATE CASCADE,"
				+ "    CONSTRAINT " + prefix + "_fk_region_member_to_region FOREIGN KEY (region) REFERENCES " + prefix + "_regions (id) ON DELETE CASCADE ON UPDATE CASCADE"
				+ ");"
			);

			stmt.executeUpdate(
				  "CREATE TABLE IF NOT EXISTS " + prefix + "_data"
				+ "("
				+ "    region INT NOT NULL,"
				+ "    `key` VARCHAR(256) NOT NULL,"
				+ "    value VARCHAR(256),"
				+ "    CONSTRAINT " + prefix + "_fk_data_to_region FOREIGN KEY (region) REFERENCES " + prefix + "_regions (id) ON DELETE CASCADE ON UPDATE CASCADE,"
				+ "    PRIMARY KEY (region, `key`)"
				+ ");"
			);
		}
	}
	
	@Override
	protected void setupConnection() throws SQLException
	{
		if(h != null)
		{
			h.setupConnection(connection);
		}
	}
	
	public List<Region> getAllRegionsInWorld(World world)
	{
		try
		{
			valid();
			
			Integer worldID = h.getWorldID(world.getName());
			if(worldID == null)
			{
				//No regions in given world.
				return null;
			}
			
			String get_regions = 
					  "SELECT * "
					+ "FROM " + prefix + "_regions "
					+ "WHERE world = " + worldID + ";";
			ResultSet rs_regions = h.returnQuery(get_regions);
			
			List<Region> regions = new ArrayList<>();
			
			while(rs_regions.next())
			{
				int id = rs_regions.getInt("id");
				String name = rs_regions.getString("name");
				Region region = new Region(id, name, world);
				
				String get_rooms = 
						  "SELECT * "
						+ "FROM " + prefix + "_rooms "
						+ "WHERE region = " + id + ";";
				ResultSet rs_rooms = h.returnQuery(get_rooms);
				
				while(rs_rooms.next())
				{
					region.expand(new Room(
						rs_rooms.getInt("id"),
						rs_rooms.getInt("min_x"),
						rs_rooms.getInt("min_y"),
						rs_rooms.getInt("min_z"),
						rs_rooms.getInt("max_x"),
						rs_rooms.getInt("max_y"),
						rs_rooms.getInt("max_z")));
				}
				
				regions.add(region);
			}
			
			if(regions.isEmpty())
			{
				return null;
			}
			
			return regions;
		}
		catch(SQLException e)
		{
			logger.error("Error loading regions for ", world.getName());
		}
		
		return null;
	}
	
	public boolean updateRegion(Region region)
	{
		Integer id = region.getDBId();
		if(id == null)
		{ //Insert Region
			try
			{
				valid();
				
				int worldID = h.getOrSetWorldID(region.getWorld().getName());
				String name = region.getName();
				List<Room> rooms = region.getRooms();
				
				String insert = "INSERT INTO " + prefix + "_regions (world, name) VALUES (" + worldID + ", '" + name + "');";
				h.sendQuery(insert);
				ResultSet rs = h.returnQuery("SELECT LAST_INSERT_ID();");
				rs.first();
				region.setDBId(rs.getInt(1));
				
				for(Room room : rooms)
				{
					insert = "INSERT INTO " + prefix + "_rooms (region, min_x, min_y, min_z, max_x, max_y, max_z) VALUES ("
								+ region.getDBId() + ", "
								+ room.getMin().getX() + ", "
								+ room.getMin().getY() + ", "
								+ room.getMin().getZ() + ", "
								+ room.getMax().getX() + ", "
								+ room.getMax().getY() + ", "
								+ room.getMax().getZ() + ");";
					h.sendQuery(insert);
					rs = h.returnQuery("SELECT LAST_INSERT_ID();");
					rs.first();
					room.setDBId(rs.getInt(1));
				}
			}
			catch (SQLException e)
			{
				logger.error("Could not save region.");
				e.printStackTrace();
				return false;
			}
		}
		else
		{ //Update Region
			
		}
		return true;
	}

	public boolean deleteRegion(Region region)
	{
		try
		{
			valid();
			
			if(region.getDBId() == null)
			{
				logger.error("Attempted to delete region %v in %v which has not been inserted into the DB yet (no ID).", region.getName(), region.getWorld());
				//Maybe query by raw data?
				return true;
			}
			
			Integer worldID = h.getWorldID(region.getWorld().getName());
			if(worldID == null)
			{
				logger.error("Attempted to delete region %v which cannot exist in the DB since world %v is not present.", region.getName(), region.getWorld());
				return true;
			}
			
			String delete_region = "DELETE FROM " + prefix + "_regions WHERE id = " + region.getDBId() + ";";
			h.sendQuery(delete_region);
			return true;
		}
		catch (SQLException e)
		{
			logger.error("SQL error while deleting region %v in %v.", region.getName(), region.getWorld().getName());
			e.printStackTrace();
			return false;
		}
	}
}
