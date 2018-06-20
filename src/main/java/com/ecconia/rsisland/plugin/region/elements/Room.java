package com.ecconia.rsisland.plugin.region.elements;

import com.ecconia.rsisland.framework.commonelements.Area;
import com.ecconia.rsisland.framework.commonelements.Point;

public class Room
{
	private Integer id;
	
	private final Point min;
	private final Point max;
	
	public Room(int id, int min_x, int min_y, int min_z, int max_x, int max_y, int max_z)
	{
		this.id = id;
		
		min = new Point(min_x, min_y, min_z);
		max = new Point(max_x, max_y, max_z);
	}
	
	public Room(Area area)
	{
		min = area.getMin();
		max = area.getMax();
	}

	public Point getMin()
	{
		return min;
	}

	public Point getMax()
	{
		return max;
	}
	
	public void setDBId(int id)
	{
		this.id = id;
	}
	
	public Integer getId()
	{
		return id;
	}
}
