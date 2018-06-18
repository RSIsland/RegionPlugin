package com.ecconia.rsisland.plugin.region.commands;

import org.bukkit.command.CommandSender;

import com.ecconia.rsisland.framework.cofami.Subcommand;
import com.ecconia.rsisland.plugin.region.RegionPlugin;

public class CommandCreate extends Subcommand
{
	private final RegionPlugin plugin;
	
	public CommandCreate(RegionPlugin plugin)
	{
		super("create");
		this.plugin = plugin;
	}
	
	@Override
	public void exec(CommandSender sender, String[] args)
	{
	}
}
