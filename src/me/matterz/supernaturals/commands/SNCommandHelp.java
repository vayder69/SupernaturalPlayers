package me.matterz.supernaturals.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;

public class SNCommandHelp extends SNCommand{
	private static List<String> helpMessages = new ArrayList<String>();
	
	public SNCommandHelp(){
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		senderMustBeSupernatural = false;
		permissions = "supernatural.command.help";
	}
	
	static{
		helpMessages.add("*** "+ChatColor.WHITE+"Supernatural Help "+ChatColor.RED+"***");
		helpMessages.add("/sn power "+ChatColor.WHITE+"- Show current power level.");
		helpMessages.add("/sn list "+ChatColor.WHITE+"- List supernaturals on the server.");
		helpMessages.add("/sn classes "+ChatColor.WHITE+"- Show the list of available Super-classes");
	}
	
	@Override
	public void perform()
	{
		String permissions2 = "supernatural.command.adminhelp";
		Player senderPlayer = (Player) sender;
		
		if(SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions2)){
			if(helpMessages.size()==4){
				helpMessages.add("/sn admin "+ChatColor.WHITE+"- Show list of admin-only commands");
			}
		}
		
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}		
		this.sendMessage(helpMessages);
	}
}
