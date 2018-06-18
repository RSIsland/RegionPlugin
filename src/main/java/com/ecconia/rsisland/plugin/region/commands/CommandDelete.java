package com.ecconia.rsisland.plugin.region.commands;

import org.bukkit.command.CommandSender;

import com.ecconia.rsisland.framework.cofami.Subcommand;
import com.ecconia.rsisland.plugin.region.RegionPlugin;

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
	}
}
