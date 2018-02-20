package com.ecconia.rsisland.plugin.region;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.ecconia.rsisland.plugin.region.command.framework.CommandHandler;
import com.ecconia.rsisland.plugin.region.command.framework.Subcommand;

public class RegionPlugin extends JavaPlugin
{
	public static final String prefix = ChatColor.WHITE + "[" + ChatColor.GOLD + "Region" + ChatColor.WHITE + "] ";
	
	@Override
	public void onEnable()
	{
		initCommand();
	}
	
	private void initCommand()
	{
		getCommand("reg").setExecutor(new CommandHandler(this, new Subcommand[]{
				
		}));
	}
}
