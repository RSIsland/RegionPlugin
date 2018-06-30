package com.ecconia.rsisland.plugin.region;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.ecconia.rsisland.framework.cofami.CommandHandler;
import com.ecconia.rsisland.framework.cofami.Feedback;
import com.ecconia.rsisland.framework.cofami.GroupSubcommand;
import com.ecconia.rsisland.framework.commonelements.Cuboid;
import com.ecconia.rsisland.plugin.region.commands.CommandAppend;
import com.ecconia.rsisland.plugin.region.commands.CommandCUIShow;
import com.ecconia.rsisland.plugin.region.commands.CommandCreate;
import com.ecconia.rsisland.plugin.region.commands.CommandDelete;
import com.ecconia.rsisland.plugin.region.commands.CommandList;
import com.ecconia.rsisland.plugin.region.commands.CommandRemove;
import com.ecconia.rsisland.plugin.region.db.DBAdapter;
import com.ecconia.rsisland.plugin.region.elements.Region;
import com.ecconia.rsisland.plugin.region.elements.Room;
import com.ecconia.rsisland.plugin.region.exception.DatabaseException;
import com.ecconia.rsisland.plugin.region.exception.NoSelectionPluginException;
import com.ecconia.rsisland.plugin.region.exception.RegionExistingException;
import com.ecconia.rsisland.plugin.region.exception.RegionNotExistingException;
import com.ecconia.rsisland.plugin.region.regionstorage.RegionStorage;
import com.ecconia.rsisland.plugin.selection.api.SelectionAPI;

public class RegionPlugin extends JavaPlugin
{
	private DBAdapter dba;
	private SelectionAPI selectAPI;
	private RegionStorage storage;
	private FormattedLogger logger;
	
	@Override
	public void onEnable()
	{
		Feedback f = new Feedback(Feedback.simplePrefix(ChatColor.WHITE, ChatColor.GOLD, "Region"));
		logger = new FormattedLogger(f, getServer().getConsoleSender());
		
		try
		{
			String jdbcString = "jdbc:mysql://localhost/{Database}?user={username}&password={password}&useSSL=false";
			jdbcString = jdbcString.replace("{Database}", "");
			jdbcString = jdbcString.replace("{username}", "");
			jdbcString = jdbcString.replace("{password}", "");
			dba = new DBAdapter(jdbcString, logger, "region");
		}
		catch (SQLException e)
		{
			logger.error("Could not connect to database, aborting plugin load.");
			e.printStackTrace();
			return;
		}
		
		{
			RegisteredServiceProvider<SelectionAPI> provider = getServer().getServicesManager().getRegistration(SelectionAPI.class);
			if(provider != null)
			{
				selectAPI = (SelectionAPI) provider.getProvider();
			}
		}
		
		storage = new RegionStorage();
		
		//Load regions.
		for(World world : getServer().getWorlds())
		{
			storage.addWorldRegions(dba.getAllRegionsInWorld(world));
		}
		
		initCommand(f);
	}
	
	private void initCommand(Feedback f)
	{
		new CommandHandler(this, f, new GroupSubcommand("reg"
			,new CommandCreate(this)
			,new CommandDelete(this)
			,new CommandAppend(this)
			,new CommandRemove(this)
			,new CommandList(this)
			,new CommandCUIShow(this)
		));
	}

	public SelectionAPI getSelectAPI() throws NoSelectionPluginException
	{
		if(selectAPI == null)
		{
			throw new NoSelectionPluginException();
		}
		return selectAPI;
	}

	public void createRegion(World world, Cuboid cuboid, String name)
	{
		if(storage.hasRegion(world, name))
		{
			throw new RegionExistingException();
		}
		
		Region region = new Region(name, world, new Room(cuboid));
		
		if(dba.updateRegion(region))
		{
			storage.add(region);
		}
		else
		{
			throw new DatabaseException();
		}
	}
	
	public RegionStorage getStorage()
	{
		return storage;
	}

	public void deleteRegion(World world, String name)
	{
		Region region = storage.getWorldContainer(world).getRegion(name);
		
		if(region == null)
		{
			throw new RegionNotExistingException();
		}
		
		if(dba.deleteRegion(region))
		{
			storage.getWorldContainer(world).remove(region);
		}
		else
		{
			throw new DatabaseException();
		}
	}
}
