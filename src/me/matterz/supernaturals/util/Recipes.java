package me.matterz.supernaturals.util;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class Recipes{
	public Map<Material, Integer> materialQuantities;
	
	public void removeFromPlayer(Player player){
		Inventory inventory = player.getInventory();
		for(Material material: this.materialQuantities.keySet()){
			inventory.removeItem(new ItemStack(material.getId(), this.materialQuantities.get(material)));
		}
		inventory.notifyAll();
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
