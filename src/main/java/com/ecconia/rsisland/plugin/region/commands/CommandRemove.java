package com.ecconia.rsisland.plugin.region.commands;

import org.bukkit.command.CommandSender;

import com.ecconia.rsisland.framework.cofami.Subcommand;
import com.ecconia.rsisland.plugin.region.RegionPlugin;

public class CommandRemove extends Subcommand
{
	private final RegionPlugin plugin;
	
	public CommandRemove(RegionPlugin plugin)
	{
		super("remove");
		this.plugin = plugin;
	}
	
	@Override
	public void exec(CommandSender sender, String[] arguments)
	{
	}
}
