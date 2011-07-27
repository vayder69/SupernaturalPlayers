package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SuperNManager;

import org.bukkit.entity.Player;

public class SNCommandReset extends SNCommand {

	public SNCommandReset() {	
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		optionalParameters.add("playername");
		permissions = "supernatural.admin.command.reset";
		helpNameAndParams = "reset | reset [playername]";
		helpDescription = "Reset a player's power to zero";
	}
	
	@Override
	public void perform(){
		
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.hasPermissions(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		if(parameters.isEmpty()){
			SuperNPlayer snplayer = SuperNManager.get(senderPlayer);
			SuperNManager.alterPower(snplayer, -10000, "Admin");
		}else{
			String playername = parameters.get(0);
			Player player = SupernaturalsPlugin.instance.getServer().getPlayer(playername);
	
			if (player == null){
				this.sendMessage("Player not found!");
				return;
			}			
			SuperNPlayer snplayer = SuperNManager.get(player);
			SuperNManager.alterPower(snplayer, -10000, "Admin");
			this.sendMessage("Power reset for player: "+snplayer.getName());
		}
	}
}
