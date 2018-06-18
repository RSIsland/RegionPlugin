package com.ecconia.rsisland.plugin.region.regionstorage;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;

import com.ecconia.rsisland.plugin.region.elements.Region;

public class RegionStorage
{
	private Map<String, Region> regionsByName;
	private Map<World, RegionContainer> containers;
	
	public RegionStorage()
	{
		containers = new HashMap<>();
		regionsByName = new HashMap<>();
	}
	
	public RegionContainer getWorldContainer(World world)
	{
		RegionContainer container = containers.get(world);
		if(container == null)
		{
			container = new LinearRegionContainer();
			containers.put(world, container);
		}
		return container;
	}

	public boolean hasRegion(String name)
	{
		return regionsByName.containsKey(name);
	}

	public void add(Region region)
	{
		regionsByName.put(region.getName(), region);
		getWorldContainer(region.getWorld()).add(region);
	}
}
