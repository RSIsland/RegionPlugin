package com.ecconia.rsisland.plugin.region.regionstorage;

import java.util.Collection;
import java.util.List;

import com.ecconia.rsisland.framework.commonelements.Area;
import com.ecconia.rsisland.framework.commonelements.Point;
import com.ecconia.rsisland.plugin.region.elements.Region;

public interface RegionContainer
{
	public void add(Region region);
	
	public void remove(Region region);
	
	public List<Region> getRegions(Point location);
	
	public List<Region> getRegions(Area area);
	
	public Region getRegion(String name);

	public void add(List<Region> regions);

	public Collection<Region> getAllRegions();
}
