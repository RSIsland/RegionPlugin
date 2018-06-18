package com.ecconia.rsisland.plugin.region;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.ecconia.rsisland.framework.cofami.CommandHandler;
import com.ecconia.rsisland.framework.cofami.Feedback;
import com.ecconia.rsisland.framework.cofami.GroupSubcommand;

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
			,new CommandCreate(this)
			,new CommandDelete(this)
			,new CommandAppend(this)
			,new CommandRemove(this)
		));
	}
}
