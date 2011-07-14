package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SNCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SNCommandBurnTimes extends SNCommand
{
	public SNCommandBurnTimes()
	{
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		senderMustBeSupernatural = false;
		permissions = "supernatural.admin.command.burntime";

		requiredParameters.add("from");
		requiredParameters.add("to");
	}
	
	@Override
	public void perform()
	{
		
		Player player = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(player, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		String fromTime = parameters.get(0);
		String toTime = parameters.get(1);
		
		SNConfigHandler.vampireCombustFromTime = Integer.parseInt(fromTime);
		SNConfigHandler.vampireCombustToTime = Integer.parseInt(toTime);
		SupernaturalsPlugin.instance.getConfigManager().saveConfig();
		
		this.sendMessage("Day time set from " + ChatColor.WHITE + fromTime + ChatColor.RED + " to " 
				+ ChatColor.WHITE + toTime + ChatColor.RED + ". Vampires will burn during this time.");
	}
}
