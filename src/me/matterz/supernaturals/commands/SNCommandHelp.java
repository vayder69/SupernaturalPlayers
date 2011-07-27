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
		helpMessages.add("/sn Power "+ChatColor.WHITE+"- Show current power level.");
		helpMessages.add("/sn List "+ChatColor.WHITE+"- List supernaturals on the server.");
		helpMessages.add("/sn Classes "+ChatColor.WHITE+"- Show the list of available Super-classes.");
		helpMessages.add("/sn KillList "+ChatColor.WHITE+"- Show the list of current WitchHunter targets.");
	}
	
	@Override
	public void perform()
	{
		String permissions2 = "supernatural.command.adminhelp";
		Player senderPlayer = (Player) sender;
		
		if(!SupernaturalsPlugin.hasPermissions(senderPlayer, permissions2)){
			if(helpMessages.size()==5){
				helpMessages.add("/sn admin "+ChatColor.WHITE+"- Show list of admin-only commands");
			}
		}
		
		if(!SupernaturalsPlugin.hasPermissions(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}		
		this.sendMessage(helpMessages);
	}
}
