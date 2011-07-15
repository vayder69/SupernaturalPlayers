package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class SNCommandPower extends SNCommand {
	
	public SNCommandPower() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = true;
		senderMustBeSupernatural = true;
		permissions = "supernatural.command.power";
		optionalParameters.add("playername");
		optionalParameters.add("power");
		helpNameAndParams = "";
		helpDescription = "See current power level";
	}
	
	@Override
	public void perform() {
		
		Player senderPlayer = (Player) sender;
		String permissions2 = "supernatural.admin.command.power";
		
		if(optionalParameters.isEmpty()){
			
			if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions)){
				this.sendMessage("You do not have permissions to use this command.");
				return;
			}
			
			SupernaturalsPlugin.instance.getSuperManager();
			SuperNPlayer snplayer = SupernaturalManager.get(senderPlayer);
					
			this.sendMessage("Your current power level is: " + (int) snplayer.getPower());
		} else {
			if(!SupernaturalsPlugin.permissionHandler.has(senderPlayer, permissions2)){
				this.sendMessage("You do not have permissions to use this command.");
				return;
			}
			
			String playername = parameters.get(0);
			Player player = SupernaturalsPlugin.instance.getServer().getPlayer(playername);
			if (player == null) {
				this.sendMessage("Player not found!");
				return;
			}
			double powerGain;
			
			try{
				powerGain = Double.parseDouble(parameters.get(1));
			} catch(NumberFormatException e) {
				e.printStackTrace();
				this.sendMessage("Invalid Number.");
				return;
			}
			if(powerGain>=10000D){
				powerGain=9999;
			}
			
			this.sendMessage(ChatColor.WHITE + player.getDisplayName() + ChatColor.RED + " has been powered up!");
			SupernaturalsPlugin.instance.getSuperManager();
			SuperNPlayer snplayer = SupernaturalManager.get(player);
			SupernaturalsPlugin.instance.getSuperManager().alterPower(snplayer, powerGain, "Admin boost!");
		}
	}
}