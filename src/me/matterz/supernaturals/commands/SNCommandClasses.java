package me.matterz.supernaturals.commands;

import java.util.ArrayList;
import java.util.List;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SNCommandClasses extends SNCommand{
	private static List<String> classMessages = new ArrayList<String>();
	
	public SNCommandClasses(){
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		senderMustBeSupernatural = false;
		permissions = "supernatural.command.classes";
	}
	
	static{
		classMessages.add("*** "+ChatColor.WHITE+"Supernatural Classes "+ChatColor.RED+"***");
		classMessages.add("Human: "+ChatColor.WHITE+"- Your standard run of the mill person.");
		classMessages.add("Priest: "+ChatColor.WHITE+"- A person with significant power over the unholy.");
		classMessages.add("Vampire: "+ChatColor.WHITE+"- No they don't sparkle!");
		classMessages.add("Ghoul: "+ChatColor.WHITE+"- Slow and very durable.");
		classMessages.add("Werewolf: "+ChatColor.WHITE+"- Gain significant powers at night.");
		classMessages.add("WitchHunter: "+ChatColor.WHITE+"- Expert at bows and stealth.");
		classMessages.add("Demon: "+ChatColor.WHITE+"- Possesses an unholy union with fire.");
	}
	
	@Override
	public void perform()
	{
		Player senderPlayer = (Player) sender;
		
		if(!SupernaturalsPlugin.hasPermissions(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		this.sendMessage(classMessages);
	}

}
