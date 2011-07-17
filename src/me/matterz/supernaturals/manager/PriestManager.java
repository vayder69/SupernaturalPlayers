package me.matterz.supernaturals.manager;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.server.EnumSkyBlock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
	// 					Church						//
	// -------------------------------------------- //
	
	public void useAltar(Player player){
		Location location = player.getLocation();
		World world = location.getWorld();
		int locX = location.getBlockX();
		int locY = location.getBlockY();
		int locZ = location.getBlockZ();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		int amount = 0;
		if(world.getName().equalsIgnoreCase(SNConfigHandler.priestChurchWorld)){
			if(Math.abs(locX-SNConfigHandler.priestChurchLocationX) <= 10){
				if(Math.abs(locY-SNConfigHandler.priestChurchLocationY) <= 10){
					if(Math.abs(locZ-SNConfigHandler.priestChurchLocationZ) <= 10){
						if(snplayer.isPriest()){
							if(player.getItemInHand().getType().equals(Material.COAL)){
								SupernaturalManager.sendMessage(snplayer, "The Church expels you from it's ranks!");
								SupernaturalManager.cure(snplayer);
							}else{
								PlayerInventory inv = player.getInventory();
								ItemStack[] items = inv.getContents();
								for(Material mat : SNConfigHandler.priestDonationMaterials){
									for(ItemStack itemStack : items){
										if(itemStack.getType().equals(mat)){
											amount += itemStack.getAmount();
										}
									}
								}
								for(Material mat: SNConfigHandler.priestDonationMaterials){
									inv.remove(mat);
								}
								int delta = amount * SNConfigHandler.priestPowerDonation;
								SupernaturalManager.sendMessage(snplayer, "The Church accepts your gracious donations.");
								SupernaturalManager.alterPower(snplayer, delta, "Donations!");
							}
						}else{
							SupernaturalManager.sendMessage(snplayer, "The Church Altar radiates holy power.");
							if(!snplayer.isHuman()) {
								SupernaturalManager.sendMessage(snplayer, "The holy power of the Church tears you asunder!");
								player.setHealth(0);
								if(snplayer.isGhoul()){
								double random = Math.random();
									if(random>SNConfigHandler.spreadChance){
										SupernaturalManager.cure(snplayer);
									}
								}
								return;
							}
							if(SNConfigHandler.priestAltarRecipe.playerHasEnough(player)) {
								SupernaturalManager.sendMessage(snplayer, "You donate these items to the Church:");
								SupernaturalManager.sendMessage(snplayer, SNConfigHandler.priestAltarRecipe.getRecipeLine());
								SupernaturalManager.sendMessage(snplayer, "The Church recognizes your holy spirit and accepts you into the priesthood.");
								SNConfigHandler.priestAltarRecipe.removeFromPlayer(player);
								SupernaturalManager.curse(snplayer, "priest", SNConfigHandler.priestPowerStart);
							}else{
								SupernaturalManager.sendMessage(snplayer, "The Church judges your intended donate insufficient.  You must gather the following: ");
								SupernaturalManager.sendMessage(snplayer, SNConfigHandler.priestAltarRecipe.getRecipeLine());
							}
						}
					}
				}
			}
		}
	}
	
	// -------------------------------------------- //
	// 					Spells						//
	// -------------------------------------------- //
	
	public void banish(Player player, Player victim){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(snplayer.getPower() > SNConfigHandler.priestPowerBanish){
			if(snvictim.isSuper()){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerBanish, "Banished "+victim.getName());
				victim.teleport(SNConfigHandler.priestBanishLocation);
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to banish.");
		}
	}
	
	public void heal(Player player, Player victim){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(snplayer.getPower() > SNConfigHandler.priestPowerHeal){
			if(!snvictim.isSuper() && victim.getHealth()<20 && !victim.isDead()){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerHeal, "Healed "+victim.getName());
				int health = victim.getHealth()+SNConfigHandler.priestHealAmount;
				if(health>20)
					health=20;
				victim.setHealth(health);
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
			}else{
				SupernaturalManager.sendMessage(snplayer, "Player cannot be healed.");
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to heal.");
		}
	}
	
	public void exorcise(Player player, Player victim){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(snplayer.getPower() > SNConfigHandler.priestPowerExorcise){
			if(snvictim.isSuper()){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerExorcise, "Exorcised "+victim.getName());
				SupernaturalManager.cure(snvictim);
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
			}else{
				SupernaturalManager.sendMessage(snplayer, "Only supernatural players can be exorcised.");
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to exorcise.");
		}
	}
	
	public void cure(Player player, Player victim, Material material){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(snplayer.getPower() > SNConfigHandler.priestPowerCure){
			if(snvictim.isSuper() && victim.getItemInHand().getType().equals(material)){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerCure, "Cured "+victim.getName());
				SupernaturalManager.sendMessage(snvictim, snplayer.getName()+" has restored your humanity");
				SupernaturalManager.cure(snvictim);
				ItemStack item = player.getItemInHand();
				ItemStack item2 = victim.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
				if(item2.getAmount()==1){
					victim.setItemInHand(null);
				}else{
					item2.setAmount(victim.getItemInHand().getAmount()-1);
				}
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to banish.");
		}
	}
	
	public void drainPower(Player player, Player victim){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(snplayer.getPower() > SNConfigHandler.priestPowerDrain){
			if(snvictim.isSuper()){
				double power = snvictim.getPower();
				power *= SNConfigHandler.priestDrainFactor;
				SupernaturalManager.alterPower(snplayer, (power - SNConfigHandler.priestPowerDrain), "Drained "+victim.getName());
				SupernaturalManager.alterPower(snvictim, -power, "Drained by "+snplayer.getName());
				ItemStack item = player.getItemInHand();
				if(item.getAmount()==1){
					player.setItemInHand(null);
				}else{
					item.setAmount(player.getItemInHand().getAmount()-1);
				}
			}else{
				SupernaturalManager.sendMessage(snplayer, "Only supernatural players can be power drained.");
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to drain power.");
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