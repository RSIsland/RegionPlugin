package com.ecconia.rsisland.plugin.region.regionstorage;

import java.util.Collection;
import java.util.List;

import org.bukkit.World;

import com.ecconia.rsisland.framework.commonelements.Cuboid;
import com.ecconia.rsisland.framework.commonelements.Point;
import com.ecconia.rsisland.plugin.region.elements.Region;

public interface RegionContainer
{
	public void add(Region region);
	
	public void remove(Region region);
	
	public List<Region> getRegions(Point location);
	
	public List<Region> getRegions(World world, Cuboid area);
	
	public Region getRegion(String name);

	public void add(List<Region> regions);

	public Collection<Region> getAllRegions();
}
