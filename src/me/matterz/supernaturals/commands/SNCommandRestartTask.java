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

import me.matterz.supernaturals.SupernaturalsPlugin;

import org.bukkit.entity.Player;

public class SNCommandRestartTask extends SNCommandReload {
	
	public SNCommandRestartTask() {
		permissions = "supernatural.admin.command.task";
		helpNameAndParams = "";
		helpDescription = "Restarts the task timer.";
	}
	
	@Override
	public void perform()
	{
		Player senderPlayer = (Player) sender;
		if(!SupernaturalsPlugin.hasPermissions(senderPlayer, permissions)){
			this.sendMessage("You do not have permissions to use this command.");
			return;
		}
		SupernaturalsPlugin.restartTask();
		this.sendMessage("Task Timer has been restarted.");
	}
}
