/*
 * Supernatural Players Plugin for Bukkit
 * Copyright (C) 2011  Matt Walker <mmw167@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package me.matterz.supernaturals.commands;

import java.util.ArrayList;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.manager.HunterManager;
import me.matterz.supernaturals.manager.SNCommand;
import me.matterz.supernaturals.manager.SuperNManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SNCommandRmTarget extends SNCommand {
	public SNCommandRmTarget() {
		super();
		requiredParameters = new ArrayList<String>();
		optionalParameters = new ArrayList<String>();
		senderMustBePlayer = false;
		optionalParameters.add("playername");
		permissions = "supernatural.admin.command.rmtarget";
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
			SuperNPlayer snplayer = SuperNManager.get(senderPlayer);
			if(HunterManager.removeBounty(snplayer)){
				this.sendMessage("You were removed from the target list!");
				return;
			}else{
				this.sendMessage("You are not an active target.");
				return;
			}
		}else{
			String playername = parameters.get(0);
			SuperNPlayer snplayer = SuperNManager.get(playername);
			
			if (snplayer == null) {
				this.sendMessage("Player not found.");
				return;
			}
			
			if(HunterManager.removeBounty(snplayer)){
				this.sendMessage(ChatColor.WHITE+snplayer.getName()+ChatColor.RED+" was removed from the target list!");
				HunterManager.addBounty();
				return;
			}else{
				this.sendMessage(ChatColor.WHITE+snplayer.getName()+ChatColor.RED+" is not an active target.");
				return;
			}
		}
	}
}