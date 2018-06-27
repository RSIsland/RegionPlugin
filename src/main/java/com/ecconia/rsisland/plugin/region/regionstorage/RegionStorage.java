package com.ecconia.rsisland.plugin.region.regionstorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;

import com.ecconia.rsisland.plugin.region.elements.Region;

public class RegionStorage
{
	private Map<World, RegionContainer> containers;
	
	public RegionStorage()
	{
		containers = new HashMap<>();
	}
	
	public void addWorldRegions(List<Region> regions)
	{
		if(regions == null || regions.isEmpty())
		{
			return;
		}
		
		World world = regions.get(0).getWorld();
		RegionContainer container = containers.get(world);
		
		if(container == null)
		{
			container = new LinearRegionContainer();
			containers.put(world, container);
		}
		
		container.add(regions);
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

	public boolean hasRegion(World world, String name)
	{
		return getWorldContainer(world).getRegion(name) != null;
	}

	public void add(Region region)
	{
		getWorldContainer(region.getWorld()).add(region);
	}
	
	public Map<World, RegionContainer> getWorldContainers()
	{
		return containers;
	}
}
