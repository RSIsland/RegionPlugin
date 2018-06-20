package com.ecconia.rsisland.plugin.region.commands;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.ecconia.rsisland.framework.cofami.Subcommand;
import com.ecconia.rsisland.plugin.region.RegionPlugin;
import com.ecconia.rsisland.plugin.region.elements.Region;
import com.ecconia.rsisland.plugin.region.regionstorage.RegionContainer;

public class CommandList extends Subcommand
{
	private final RegionPlugin plugin;
	
	public CommandList(RegionPlugin plugin)
	{
		super("list");
		
		this.plugin = plugin;
	}

	@Override
	public void exec(CommandSender sender, String[] arguments)
	{
		if(arguments.length != 0)
		{
			die("Usage: " + path);
		}
		
		f.n(sender, "Worlds:");
		for(Entry<World, RegionContainer> worldContainer : plugin.getStorage().getWorldContainers().entrySet())
		{
			f.n(sender, "> %v:", worldContainer.getKey().getName());
			for(Region region : worldContainer.getValue().getAllRegions())
			{
				f.n(sender, "   - Region %v parts %v", region.getName(), region.getRooms().size());
			}
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args)
	{
		return Collections.emptyList();
	}
}
