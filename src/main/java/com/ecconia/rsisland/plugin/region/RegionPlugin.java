package com.ecconia.rsisland.plugin.region;

import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.ecconia.rsisland.framework.cofami.CommandHandler;
import com.ecconia.rsisland.framework.cofami.Feedback;
import com.ecconia.rsisland.framework.cofami.GroupSubcommand;
import com.ecconia.rsisland.plugin.selection.api.SelectionAPI;

public class RegionPlugin extends JavaPlugin
{
	public static final String prefix = ChatColor.WHITE + "[" + ChatColor.GOLD + "Region" + ChatColor.WHITE + "] ";
	private SelectionAPI selectAPI;
	
	@Override
	public void onEnable()
	{
		{
			RegisteredServiceProvider<SelectionAPI> provider = getServer().getServicesManager().getRegistration(SelectionAPI.class);
			if(provider != null)
			{
				selectAPI = (SelectionAPI) provider.getProvider();
			}
		}
		
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

	public SelectionAPI getSelectAPI() throws NoSelectionPluginException
	{
		if(selectAPI == null)
		{
			throw new NoSelectionPluginException();
		}
		return selectAPI;
	}
}
