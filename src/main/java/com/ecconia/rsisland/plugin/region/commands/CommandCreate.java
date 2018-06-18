package com.ecconia.rsisland.plugin.region.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ecconia.rsisland.framework.cofami.Subcommand;
import com.ecconia.rsisland.plugin.region.RegionPlugin;
import com.ecconia.rsisland.plugin.region.exception.NoSelectionPluginException;
import com.ecconia.rsisland.plugin.region.exception.RegionExistingException;
import com.ecconia.rsisland.plugin.selection.api.ISelection;
import com.ecconia.rsisland.plugin.selection.api.SelectionAPI;

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
		Player player = getPlayer(sender);
		SelectionAPI selectAPI = null;
		
		try
		{
			selectAPI = plugin.getSelectAPI();
		}
		catch(NoSelectionPluginException e)
		{
			die("You can only use this command with SelectionPlugin installed and enabled.");
		}
		
		if(args.length < 1)
		{
			die("Usage: %v", path + " <name>");
		}
		
		ISelection selection = selectAPI.getPlayer(player).getSelectionOrCurrent("plugin:region");
		if(selection == null)
		{
			die("You do not have a selection %v nor a last used selection.", "plugin:region");
		}
		if(selection.isIncomplete())
		{
			die("Your selection is not complete, it cannot be used");
		}
		
		try
		{
			plugin.createRegion(selection.getArea(), args[0]);
		}
		catch(RegionExistingException e)
		{
			die("Region %v exists already.", args[0]);
		}
		
		f.n(sender, "Region %v has been created.", args[0]);
	}
}
