package me.matterz.supernaturals.manager;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.EnumSkyBlock;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class PriestManager {
	
private SupernaturalsPlugin plugin;

	private Map<SuperNPlayer, String> playerList = new HashMap<SuperNPlayer, String>();
	
	public PriestManager(SupernaturalsPlugin plugin) {
		this.plugin=plugin;
	}
	
	// -------------------------------------------- //
	// 					Spells						//
	// -------------------------------------------- //
	
	public void banish(Player player, Player victim){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		if(snplayer.getPower() > SNConfigHandler.priestPowerBanish){
			SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerBanish, "Banished "+victim.getName());
			victim.teleport(player.getWorld().getSpawnLocation());
			ItemStack item = player.getItemInHand();
			if(item.getAmount()==1){
				player.setItemInHand(null);
			}else{
				item.setAmount(player.getItemInHand().getAmount()-1);
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to banish.");
		}
	}
	
	// -------------------------------------------- //
	// 					Damage						//
	// -------------------------------------------- //
	
	public float priestAttack(Player priest, Entity victim, float damage){
		if(victim instanceof Animals){
			damage = 0;
		}else if(victim instanceof Player){
			Player pVictim = (Player) victim;
			SuperNPlayer snvictim = SupernaturalManager.get(pVictim);
			if(snvictim.isSuper()){
				pVictim.setFireTicks(pVictim.getMaxFireTicks());
				damage *= SNConfigHandler.priestDamageFactorAttackSuper;
			}else{
				damage *= SNConfigHandler.priestDamageFactorAttackHuman;
			}
		}
		return damage;
	}
	
	// -------------------------------------------- //
	// 					Lighting					//
	// -------------------------------------------- //
	
	public void priestLight(){
		// Adjust each Priest
		for(SuperNPlayer snplayer : SupernaturalManager.getSupernaturals()) {
			if(snplayer.isPriest()){
				int radius = SNConfigHandler.priestLightRadius;
				int LocationX;
				int LocationY;
				int LocationZ;
				int currentInt;
				int newInt;
				//Light Removal
				if(playerList.containsKey(snplayer)){
					String locationString = playerList.get(snplayer);
					String[] locationStrings = locationString.split(":");
					LocationX = Integer.parseInt(locationStrings[1]);
					LocationY = Integer.parseInt(locationStrings[2]);
					LocationZ = Integer.parseInt(locationStrings[3]);
					CraftWorld world = (CraftWorld) SupernaturalsPlugin.instance.getServer().getWorld(locationStrings[0]);
					for(int x = -radius; x <= radius; x++){
						for(int y = -radius; y <= radius; y++){
							for(int z = -radius; z <= radius; z++){
								
								Vector origin = new Vector(LocationX, LocationY, LocationZ);
								Vector v = new Vector(LocationX + x, LocationY + y, LocationZ + z);

								if(v.isInSphere(origin, radius)){
									currentInt = world.getHandle().getLightLevel(LocationX+x, LocationY+y, LocationZ+z);
									newInt = (currentInt - SNConfigHandler.priestLightIntensity);
									if(newInt<0)
										newInt=0;
									world.getHandle().b(EnumSkyBlock.BLOCK, LocationX+x, LocationY+y, LocationZ+z, newInt);
								}
							}
						}
					}
					playerList.remove(snplayer);
				}
				//Light Addition
				Player player = plugin.getServer().getPlayer(snplayer.getName());
				if(player==null){
					continue;
				}
				Location location = player.getLocation();
				LocationX = (int)location.getX();
				LocationY = (int)location.getY();
				LocationZ = (int)location.getZ();
				CraftWorld world = (CraftWorld) player.getWorld();
				String locationString = (world.getName()+":"+LocationX+":"+LocationY+":"+LocationZ);
				for(int x = -radius; x <= radius; x++){
					for(int y = -radius; y <= radius; y++){
						for(int z = -radius; z <= radius; z++){
							
							Vector origin = new Vector(LocationX, LocationY, LocationZ);
							Vector v = new Vector(LocationX + x, LocationY + y, LocationZ + z);

							if(v.isInSphere(origin, radius)){
								currentInt = world.getHandle().getLightLevel(LocationX+x, LocationY+y, LocationZ+z);
								newInt = (currentInt + SNConfigHandler.priestLightIntensity);
								if(newInt>15)
									newInt=15;
								world.getHandle().b(EnumSkyBlock.BLOCK, LocationX+x, LocationY+y, LocationZ+z, newInt);
							}
						}
					}
					playerList.put(snplayer,locationString);
				}
			}
		}
	}
	
	// -------------------------------------------- //
	// 					Armor						//
	// -------------------------------------------- //
	
	public void armorCheck(Player player){
		PlayerInventory inv = player.getInventory();
		ItemStack helmet = inv.getHelmet();
		ItemStack chest = inv.getChestplate();
		ItemStack leggings = inv.getLeggings();
		ItemStack boots = inv.getBoots();
		
		if(helmet.getTypeId()!=0){
			SupernaturalManager.sendMessage(SupernaturalManager.get(player), "Priests cannot wear armor!");
			inv.setHelmet(null);
			Item newItem = player.getWorld().dropItem(player.getLocation(), helmet);
			newItem.setItemStack(helmet);
		}
		if(chest.getTypeId()!=0){
			SupernaturalManager.sendMessage(SupernaturalManager.get(player), "Priests cannot wear armor!");
			inv.setChestplate(null);
			Item newItem = player.getWorld().dropItem(player.getLocation(), chest);
			newItem.setItemStack(chest);
		}
		if(leggings.getTypeId()!=0){
			SupernaturalManager.sendMessage(SupernaturalManager.get(player), "Priests cannot wear armor!");
			inv.setLeggings(null);
			Item newItem = player.getWorld().dropItem(player.getLocation(), leggings);
			newItem.setItemStack(leggings);
		}
		if(boots.getTypeId()!=0){
			SupernaturalManager.sendMessage(SupernaturalManager.get(player), "Priests cannot wear armor!");
			inv.setBoots(null);
			Item newItem = player.getWorld().dropItem(player.getLocation(), boots);
			newItem.setItemStack(boots);
		}
	}

}
