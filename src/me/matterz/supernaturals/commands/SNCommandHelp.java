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
		helpMessages.add("/sn version "+ChatColor.WHITE+"- Show plugin version.");
	}
	
	@Override
	public void perform()
	{
		String permissions2 = "supernatural.command.adminhelp";
		Player senderPlayer = (Player) sender;
		
		if(SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions2)){
			helpMessages.add("/sn cure [PlayerName] "+ChatColor.WHITE+"- Cure a player.");
			helpMessages.add("/sn convert [PlayerName]  [SupernaturalType] "+ChatColor.WHITE+"- Turn a player into vampire.");
			helpMessages.add("/sn powergain [Playername] [Power] "+ChatColor.WHITE+"- Give power to a player.");
			helpMessages.add("/sn save "+ChatColor.WHITE+"- Save data to disk.");
			helpMessages.add("/sn reload "+ChatColor.WHITE+"- Reload data from disk.");
			helpMessages.add("/sn list "+ChatColor.WHITE+"- List supernaturals on the server.");
			helpMessages.add("/sn setchurch "+ChatColor.WHITE+"- Sets your current location as the priests' church.");
			helpMessages.add("/sn burntime [From] [To] "+ChatColor.WHITE+"- Set time during which vampires will burn.");
		}
		
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		this.sendMessage(helpMessages);
	}
}
