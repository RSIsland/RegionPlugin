package com.ecconia.rsisland.plugin.region.regionstorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecconia.rsisland.framework.commonelements.Area;
import com.ecconia.rsisland.framework.commonelements.Point;
import com.ecconia.rsisland.plugin.region.elements.Region;

public class LinearRegionContainer implements RegionContainer
{
	private final Map<String, Region> regions;
	
	public LinearRegionContainer()
	{
		regions = new HashMap<>();
	}
	
	@Override
	public void add(Region region)
	{
		regions.put(region.getName(), region);
	}

	@Override
	public void remove(Region region)
	{
		regions.remove(region.getName());
	}

	@Override
	public List<Region> getRegions(Point point)
	{
		List<Region> foundRegions = new ArrayList<>();
		
		for(Region region : regions.values())
		{
			if(region.contains(point))
			{
				foundRegions.add(region);
			}
		}
		
		return foundRegions;
	}

	@Override
	public List<Region> getRegions(Area area)
	{
		//TODO: Needed for EditPlugins
		return Collections.emptyList();
	}

	@Override
	public Region getRegion(String name)
	{
		return regions.get(name);
	}

	@Override
	public void add(List<Region> regions)
	{
		for(Region r : regions)
		{
			this.regions.put(r.getName(), r);
		}
	}

	@Override
	public Collection<Region> getAllRegions()
	{
		return regions.values();
	}
}
