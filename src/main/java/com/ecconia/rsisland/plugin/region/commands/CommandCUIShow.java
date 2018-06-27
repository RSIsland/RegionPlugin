package com.ecconia.rsisland.plugin.region.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ecconia.rsisland.framework.cofami.Subcommand;
import com.ecconia.rsisland.plugin.region.RegionPlugin;
import com.ecconia.rsisland.plugin.region.elements.Region;
import com.ecconia.rsisland.plugin.region.elements.Room;
import com.ecconia.rsisland.plugin.region.exception.NoSelectionPluginException;
import com.ecconia.rsisland.plugin.selection.api.CUIArea;
import com.ecconia.rsisland.plugin.selection.api.ISelPlayer;
import com.ecconia.rsisland.plugin.selection.api.SelectionAPI;

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
		
		List<CUIArea> cuiRegions = new ArrayList<>();
		
		Random r = new Random();
		
		for(Region region : plugin.getStorage().getWorldContainer(player.getWorld()).getAllRegions())
		{
			List<CUIArea.Room> rooms = new ArrayList<>();
			
			for(Room room : region.getRooms())
			{
				rooms.add(new CUIArea.Room(room.getMin(), room.getMax()));
			}
			
			StringBuilder sb = new StringBuilder("#");
			sb.append(Integer.toString(r.nextInt(255*255*255), 16));
			sb.append("ff");
			
			CUIArea area = new CUIArea(rooms);
			area.setColor(CUIArea.Color.validateColor("#ffffffff", sb.toString(), "#00000000", "#00000000"));
			
			cuiRegions.add(area);
		}
		
		selPlayer.setCUIAreas(cuiRegions);
		
		f.n(sender, "Sent %v regions of world %v to cui.", cuiRegions.size(), player.getWorld().getName());
	}
}
