package com.ecconia.rsisland.plugin.region.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ecconia.rsisland.framework.cofami.Subcommand;
import com.ecconia.rsisland.framework.commonelements.Cuboid;
import com.ecconia.rsisland.plugin.region.RegionPlugin;
import com.ecconia.rsisland.plugin.region.elements.Region;
import com.ecconia.rsisland.plugin.region.elements.Room;
import com.ecconia.rsisland.plugin.region.exception.NoSelectionPluginException;
import com.ecconia.rsisland.plugin.selection.api.ISelPlayer;
import com.ecconia.rsisland.plugin.selection.api.SelectionAPI;
import com.ecconia.rsisland.plugin.selection.api.cui.CUIColor;
import com.ecconia.rsisland.plugin.selection.api.cui.CUICuboidConstruct;

public class CommandCUIShow extends Subcommand
{
	private final RegionPlugin plugin;
	
	public CommandCUIShow(RegionPlugin plugin)
	{
		super("cuishow");
		onlyPlayer();
		
		this.plugin = plugin;
	}
	
	@Override
	public void exec(CommandSender sender, String[] arguments)
	{
		Player player = getPlayer(sender);
		
		SelectionAPI sel = null;
		
		try
		{
			sel = plugin.getSelectAPI();
		}
		catch(NoSelectionPluginException e)
		{
			die("You can only use this command when %v is installed.", "SelectionPlugin");
		}
		
		ISelPlayer selPlayer = sel.getPlayer(player);
		
		if(!selPlayer.hasCUI())
		{
			die("You can only use this command with the clientmod WECUI for LiteLoader.");
		}
		
		if(selPlayer.hasCuboidConstructs(plugin))
		{
			selPlayer.setCUIAreas(plugin, Collections.emptyList());
			
			f.n(sender, "Removed all regions from cui.");
		}
		else
		{
			List<CUICuboidConstruct> cuiRegions = new ArrayList<>();
			
			Random r = new Random();
			
			for(Region region : plugin.getStorage().getWorldContainer(player.getWorld()).getAllRegions())
			{
				List<Cuboid> cuboids = new ArrayList<>();
				
				for(Room room : region.getRooms())
				{
					cuboids.add(new Cuboid(room.getMin(), room.getMax()));
				}
				
				StringBuilder sb = new StringBuilder("#");
				sb.append(Integer.toString(r.nextInt(255*255*255), 16));
				sb.append("ff");
				
				CUICuboidConstruct area = new CUICuboidConstruct(cuboids);
				area.setColor(CUIColor.validateColor("#ffffffff", sb.toString(), "#00000000", "#00000000"));
				
				cuiRegions.add(area);
			}
			
			selPlayer.setCUIAreas(plugin, cuiRegions);
			
			f.n(sender, "Sent %v regions of world %v to cui.", cuiRegions.size(), player.getWorld().getName());
		}
	}
}
