package com.ecconia.rsisland.plugin.region.commands;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ecconia.rsisland.framework.cofami.Subcommand;
import com.ecconia.rsisland.plugin.region.RegionPlugin;
import com.ecconia.rsisland.plugin.region.exception.RegionNotExistingException;

public class CommandDelete extends Subcommand
{
	private final RegionPlugin plugin;
	
	public CommandDelete(RegionPlugin plugin)
	{
		super("delete");
		this.plugin = plugin;
	}
	
	@Override
	public void exec(CommandSender sender, String[] arguments)
	{
		String regionName = null;
		World world = null;
		
		if(arguments.length == 1)
		{
			if(sender instanceof Player)
			{
				Player p = (Player) sender;
				world = p.getWorld();
			}
			else
			{
				die("Usage: %v <%v> <%v>", path, "world", "region");
			}
			
			regionName = arguments[0];
		}
		else if(arguments.length == 2)
		{
			world = plugin.getServer().getWorld(arguments[0]);
			if(world == null)
			{
				die("No such world %v.", arguments[0]);
			}
			
			regionName = arguments[1];
		}
		else
		{
			//TODO: If player get region he is in.
			die("Usage: %v [%v] <%v>", path, "world", "region");
		}
		
		try
		{
			plugin.deleteRegion(world, regionName);
		}
		catch (RegionNotExistingException e)
		{
			die("Region %v does not exist in %v.", regionName, world.getName());
		}
		
		f.n(sender, "Region %v in %v deleted.", regionName, world.getName());
	}
}
