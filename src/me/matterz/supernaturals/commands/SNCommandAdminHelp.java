package me.matterz.supernaturals.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;

public class SNCommandAdminHelp extends SNCommand{
	private static List<String> adminHelpMessages = new ArrayList<String>();
	
	public SNCommandAdminHelp(){
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		senderMustBeSupernatural = false;
		permissions = "supernatural.command.adminhelp";
	}
	
	static{
		adminHelpMessages.add("*** "+ChatColor.WHITE+"Supernatural Admin Help"+ChatColor.RED+" ***");
		adminHelpMessages.add("/sn cure [PlayerName] "+ChatColor.WHITE+"- Cure a player.");
		adminHelpMessages.add("/sn convert [PlayerName]  [SupernaturalType] "+ChatColor.WHITE+"- Turn a player into vampire.");
		adminHelpMessages.add("/sn powergain [Playername] [Power] "+ChatColor.WHITE+"- Give power to a player.");
		adminHelpMessages.add("/sn save "+ChatColor.WHITE+"- Save data to disk.");
		adminHelpMessages.add("/sn reload "+ChatColor.WHITE+"- Reload data from disk.");
		adminHelpMessages.add("/sn list "+ChatColor.WHITE+"- List supernaturals on the server.");
		adminHelpMessages.add("/sn setchurch "+ChatColor.WHITE+"- Sets your current location as the priests' church.");
		adminHelpMessages.add("/sn burntime [From] [To] "+ChatColor.WHITE+"- Set time during which vampires will burn.");
	}
	
	@Override
	public void perform()
	{
		Player player = (Player) sender;
		if(SupernaturalsPlugin.permissionHandler.has(player, permissions)){
			this.sendMessage(adminHelpMessages);
		} else {
			this.sendMessage("You do not have permissions to use this command.");
		}	
	}
}
