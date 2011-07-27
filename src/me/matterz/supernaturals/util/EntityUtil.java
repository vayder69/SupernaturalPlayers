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

package me.matterz.supernaturals.util;

import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;

public class EntityUtil{
	
	public static CreatureType creatureTypeFromEntity(Entity entity){
		if(!(entity instanceof Creature)){
			return null;
		}
		
		String name = entity.getClass().getSimpleName();
		name = name.substring(5); // Remove "Craft"
		
		return CreatureType.fromName(name);
	}
	
	public static String creatureNameFromEntity(Entity entity){
		if(!(entity instanceof Creature)){
			return null;
		}
		
		String name = entity.getClass().getSimpleName();
		name = name.substring(5); // Remove "Craft"
		
		return name;
	}
}
