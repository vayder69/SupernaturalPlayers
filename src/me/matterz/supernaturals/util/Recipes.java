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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Recipes{
	public Map<Material, Integer> materialQuantities = new HashMap<Material, Integer>();
	
	@SuppressWarnings("deprecation")
	public void removeFromPlayer(Player player){
		Inventory inventory = player.getInventory();
		for(Material material: this.materialQuantities.keySet()){
			inventory.removeItem(new ItemStack(material.getId(), this.materialQuantities.get(material)));
		}
		player.updateInventory();
	}
	
	public boolean playerHasEnough(Player player){
		Inventory inventory = player.getInventory();
		for(Material material: this.materialQuantities.keySet()){
			if(getMaterialCountFromInventory(material, inventory) < this.materialQuantities.get(material)){
				return false;
			}
		}
		return true;
	}
	
	public static int getMaterialCountFromInventory(Material material, Inventory inventory){
		int count = 0;
		for(ItemStack stack : inventory.all(material).values()){
			count += stack.getAmount();
		}
		return count;
	}
	
	public String getRecipeLine(){
		ArrayList<String> lines = new ArrayList<String>();
		for (Entry<Material, Integer> item : SortUtil.entriesSortedByValues(this.materialQuantities)) {
			lines.add(""+item.getValue()+" "+TextUtil.getMaterialName(item.getKey()));
		}
		return TextUtil.implode(lines, ", ");
	}
}
