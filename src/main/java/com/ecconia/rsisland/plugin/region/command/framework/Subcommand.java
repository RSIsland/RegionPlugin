package com.ecconia.rsisland.plugin.region.command.framework;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

public abstract class Subcommand implements CommandCompleter
{
	private final String name;
	
	public Subcommand(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args)
	{
		return Collections.emptyList();
	}

	public abstract void exec(CommandSender sender, String[] args);
}
