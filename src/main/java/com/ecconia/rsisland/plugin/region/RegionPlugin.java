package com.ecconia.rsisland.plugin.region;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;


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
		new CommandHandler(this, new Feedback(prefix), new GroupSubcommand("reg"
		));
	}
}
