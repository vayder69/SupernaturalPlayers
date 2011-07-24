package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class SNCommandCure extends SNCommand {
	public SNCommandCure() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		optionalParameters.add("playername");
		permissions = "supernatural.admin.command.cure";
	}
	
	@Override
	public void perform()
	{
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.hasPermissions(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		
		if(parameters.isEmpty()){
			SuperNPlayer snplayer = SupernaturalManager.get(senderPlayer);
			SupernaturalManager.cure(snplayer);
		}else{
			String playername = parameters.get(0);
			Player player = SupernaturalsPlugin.instance.getServer().getPlayer(playername);
			if (player == null) {
				this.sendMessage("Player not found.");
				return;
			}
			this.sendMessage(ChatColor.WHITE + player.getDisplayName() + ChatColor.RED + " was cured of any curse!");
			SupernaturalsPlugin.instance.getSuperManager();
			SuperNPlayer snplayer = SupernaturalManager.get(player);
			SupernaturalManager.cure(snplayer);
		}
	}
}