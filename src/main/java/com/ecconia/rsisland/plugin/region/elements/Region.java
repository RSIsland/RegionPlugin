package com.ecconia.rsisland.plugin.region.elements;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;

import com.ecconia.rsisland.framework.commonelements.Point;

public class Region
{
	//DB:
	private Integer id; //If null object has no been inserted yet.
	
	private List<Room> rooms = new ArrayList<>();
	private String name;
	private World world;
	
	public Region(int id, String name, World world)
	{
		this.id = id;
		this.name = name;
		this.world = world;
	}
	
	public Region(String name, World world, Room room)
	{
		this.name = name;
		this.world = world;
		
		rooms.add(room);
	}

	public String getName()
	{
		return name;
	}

	public boolean contains(Point point)
	{
		return false;
	}

	public World getWorld()
	{
		return world;
	}
	
	public Integer getDBId()
	{
		return id;
	}
	
	public List<Room> getRooms()
	{
		return rooms;
	}
	
	public void expand(Room room)
	{
		rooms.add(room);
	}

	public void setDBId(int id)
	{
		this.id = id;
	}
}
