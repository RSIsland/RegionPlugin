package com.ecconia.rsisland.plugin.region.elements;

import org.bukkit.World;

import com.ecconia.rsisland.framework.commonelements.Area;
import com.ecconia.rsisland.framework.commonelements.Point;

public class Region
{
	private Area area;
	private String name;
	
	public Region(String name, Area area)
	{
		this.area = area;
		this.name = name;
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
		return area.getWorld();
	}
}
