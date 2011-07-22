package me.matterz.supernaturals.listeners;

import java.util.ArrayList;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class SNBlockListener extends BlockListener{

	private SupernaturalsPlugin plugin;
	
	public SNBlockListener(SupernaturalsPlugin instance){
		this.plugin = instance;
	}
	
	@Override
	public void onBlockBreak(BlockBreakEvent event){
		Block eventBlock = event.getBlock();
		ArrayList<Block> blocks = new ArrayList<Block>();
		for(Block block : plugin.getDemonManager().getWebs().keySet()){
			if(block.equals(eventBlock)){
				event.setCancelled(true);
				block.setType(Material.AIR);
				blocks.add(block);
				plugin.getDemonManager().removeWeb(block);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log("Removed web block through destruction.");
				return;
			}
		}
	}
}
