package me.matterz.supernaturals.commands;

import java.util.ArrayList;
import java.util.List;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SupernaturalManager;
import me.matterz.supernaturals.util.TextUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class SNCommandList extends SNCommand {
	
	public SNCommandList() {
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		permissions = "supernatural.command.list";
		helpNameAndParams = "list";
		helpDescription = "List supernaturals on the server.";
	}
	
	@Override
	public void perform()
	{
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		List<String> vampires = new ArrayList<String>();
		List<String> werewolves = new ArrayList<String>();
		List<String> ghouls = new ArrayList<String>();
		List<String> priests = new ArrayList<String>();
		List<String> hunters = new ArrayList<String>();
		List<String> demons = new ArrayList<String>();
		
		for (SuperNPlayer snplayer : SupernaturalManager.findAllOnline()) {
			if (snplayer.isVampire()) {
				vampires.add(snplayer.getName());
			}else if(snplayer.isPriest()){
				priests.add(snplayer.getName());
			}else if(snplayer.isWere()){
				werewolves.add(snplayer.getName());
			}else if(snplayer.isGhoul()){
				ghouls.add(snplayer.getName());
			}else if(snplayer.isHunter()){
				hunters.add(snplayer.getName());
			}else if(snplayer.isDemon()){
				demons.add(snplayer.getName());
			}
		}
		
		// Create Messages
		List<String> messages = new ArrayList<String>();
		messages.add("*** "+ChatColor.WHITE +"Online Supernatural Players "+ChatColor.RED +"***");
		messages.add("Vampires: "+ ChatColor.WHITE + TextUtil.implode(vampires, ", "));
		messages.add("Werewolves: "+ ChatColor.WHITE + TextUtil.implode(werewolves, ", "));
		messages.add("Ghouls: "+ ChatColor.WHITE + TextUtil.implode(ghouls, ", "));
		messages.add("Priests: "+ ChatColor.WHITE + TextUtil.implode(priests, ", "));
		messages.add("WitchHunters: "+ ChatColor.WHITE + TextUtil.implode(hunters, ", "));
		messages.add("Demons: "+ ChatColor.WHITE + TextUtil.implode(demons, ", "));
		
		// Send them
		this.sendMessage(messages);
	}
}
