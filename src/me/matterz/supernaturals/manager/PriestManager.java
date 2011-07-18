package me.matterz.supernaturals.manager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;

public class PriestManager {

//	private Map<SuperNPlayer, String> playerList = new HashMap<SuperNPlayer, String>();
	
	// -------------------------------------------- //
	// 					Church						//
	// -------------------------------------------- //
	
	@SuppressWarnings("deprecation")
	public void useAltar(Player player){
		Location location = player.getLocation();
		World world = location.getWorld();
		int locX = location.getBlockX();
		int locY = location.getBlockY();
		int locZ = location.getBlockZ();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		int amount = 0;
		int delta = 0;
		if(world.getName().equalsIgnoreCase(SNConfigHandler.priestChurchWorld)){
			if(Math.abs(locX-SNConfigHandler.priestChurchLocationX) <= 10){
				if(Math.abs(locY-SNConfigHandler.priestChurchLocationY) <= 10){
					if(Math.abs(locZ-SNConfigHandler.priestChurchLocationZ) <= 10){
						if(snplayer.isPriest()){
							if(player.getItemInHand().getType().equals(Material.COAL)){
								SupernaturalManager.sendMessage(snplayer, "The Church excommunicates you!");
								SupernaturalManager.cure(snplayer);
							}else{
								PlayerInventory inv = player.getInventory();
								ItemStack[] items = inv.getContents();
								for(Material mat : SNConfigHandler.priestDonationMap.keySet()){
									for(ItemStack itemStack : items){
										if(itemStack!=null){
											if(itemStack.getType().equals(mat)){
												amount += itemStack.getAmount();
											}
										}
									}
									delta += (amount * SNConfigHandler.priestDonationMap.get(mat));
									amount = 0;
								}
								for(Material mat: SNConfigHandler.priestDonationMap.keySet()){
									inv.remove(mat);
								}
								player.updateInventory();
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
									if(random<(SNConfigHandler.spreadChance-0.1)){
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
		if(!SupernaturalsPlugin.instance.getPvP(victim)){
			SupernaturalManager.sendMessage(snplayer, "Cannot cast in a non-PvP zone.");
			return;
		}
		if(snplayer.getPower() > SNConfigHandler.priestPowerBanish){
			if(snvictim.isSuper()){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerBanish, "Banished "+victim.getName());
				SupernaturalManager.sendMessage(snvictim, "You were banished by "+ChatColor.WHITE+snplayer.getName()+ChatColor.RED+"!");
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
				SupernaturalManager.sendMessage(snvictim, "You were healed by "+ChatColor.WHITE+snplayer.getName()+ChatColor.RED+"!");
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
		if(!SupernaturalsPlugin.instance.getPvP(victim)){
			SupernaturalManager.sendMessage(snplayer, "Cannot cast in a non-PvP zone.");
			return;
		}
		if(snplayer.getPower() > SNConfigHandler.priestPowerExorcise){
			if(snvictim.isSuper()){
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerExorcise, "Exorcised "+victim.getName());
				SupernaturalManager.sendMessage(snvictim, "You were exorcised by "+ChatColor.WHITE+snplayer.getName()+ChatColor.RED+"!");
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
			if(snvictim.isSuper()){
				if(victim.getItemInHand().getType().equals(material)){
					SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerCure, "Cured "+victim.getName());
					SupernaturalManager.sendMessage(snvictim, ChatColor.WHITE+snplayer.getName()+ChatColor.RED+" has restored your humanity");
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
				}else{
					SupernaturalManager.sendMessage(snplayer, ChatColor.WHITE+snvictim.getName()+ChatColor.RED
							+" is not holding "+ChatColor.WHITE+material.toString()+ChatColor.RED+".");
				}
			}else{
				SupernaturalManager.sendMessage(snplayer, "You can only cure supernatural players.");
			}
		}else{
			SupernaturalManager.sendMessage(snplayer, "Not enough power to banish.");
		}
	}
	
	public void drainPower(Player player, Player victim){
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		SuperNPlayer snvictim = SupernaturalManager.get(victim);
		if(!SupernaturalsPlugin.instance.getPvP(victim)){
			SupernaturalManager.sendMessage(snplayer, "Cannot cast in a non-PvP zone.");
			return;
		}
		if(snplayer.getPower() > SNConfigHandler.priestPowerDrain){
			if(snvictim.isSuper()){
				double power = snvictim.getPower();
				power *= SNConfigHandler.priestDrainFactor;
				SupernaturalManager.alterPower(snplayer, -SNConfigHandler.priestPowerDrain, "Drained  "+snvictim.getName()+"'s power!");
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
		if((victim instanceof Animals) && !(victim instanceof Wolf)){
			damage = 0;
		}else if(victim instanceof Player){
			Player pVictim = (Player) victim;
			if(!SupernaturalsPlugin.instance.getPvP(pVictim))
				return damage;
			SuperNPlayer snvictim = SupernaturalManager.get(pVictim);
			if(snvictim.isSuper()){
				pVictim.setFireTicks(pVictim.getMaxFireTicks());
				damage += damage *  SupernaturalManager.get(priest).scaleAttack(SNConfigHandler.priestDamageFactorAttackSuper);
			}else{
				damage += damage *  SupernaturalManager.get(priest).scaleAttack(SNConfigHandler.priestDamageFactorAttackHuman);
			}
		}
		return damage;
	}
	
	// -------------------------------------------- //
	// 					Lighting					//
	// -------------------------------------------- //
	
//	public void priestLight(){
//		// Adjust each Priest
//		for(SuperNPlayer snplayer : SupernaturalManager.getSupernaturals()) {
//			if(snplayer.isPriest()){
////				int radius = SNConfigHandler.priestLightRadius;
//				int LocationX;
//				int LocationY;
//				int LocationZ;
//				int currentInt;
//				int newInt;
//				//Light Removal
//				if(playerList.containsKey(snplayer)){
//					String locationString = playerList.get(snplayer);
//					String[] locationStrings = locationString.split(":");
//					LocationX = Integer.parseInt(locationStrings[1]);
//					LocationY = Integer.parseInt(locationStrings[2]);
//					LocationZ = Integer.parseInt(locationStrings[3]);
//					CraftWorld world = (CraftWorld) SupernaturalsPlugin.instance.getServer().getWorld(locationStrings[0]);
//					currentInt = world.getHandle().getLightLevel(LocationX, LocationY, LocationZ);
//					newInt = (currentInt - SNConfigHandler.priestLightIntensity);
//					if(newInt<0)
//						newInt=0;
//					world.getHandle().b(EnumSkyBlock.BLOCK, LocationX, LocationY, LocationZ, newInt);
////					for(int x = -radius; x <= radius; x++){
////						for(int y = -radius; y <= radius; y++){
////							for(int z = -radius; z <= radius; z++){
////								
////								Vector origin = new Vector(LocationX, LocationY, LocationZ);
////								Vector v = new Vector(LocationX + x, LocationY + y, LocationZ + z);
////
////								if(v.isInSphere(origin, radius)){
////									currentInt = world.getHandle().getLightLevel(LocationX+x, LocationY+y, LocationZ+z);
////									newInt = (currentInt - SNConfigHandler.priestLightIntensity);
////									if(newInt<0)
////										newInt=0;
////									world.getHandle().b(EnumSkyBlock.BLOCK, LocationX+x, LocationY+y, LocationZ+z, newInt);
////								}
////							}
////						}
////					}
//					playerList.remove(snplayer);
//				}
//				//Light Addition
//				Player player = plugin.getServer().getPlayer(snplayer.getName());
//				if(player==null){
//					continue;
//				}
//				Location location = player.getLocation();
//				LocationX = (int)location.getX();
//				LocationY = (int)location.getY();
//				LocationZ = (int)location.getZ();
//				CraftWorld world = (CraftWorld) player.getWorld();
//				String locationString = (world.getName()+":"+LocationX+":"+LocationY+":"+LocationZ);
//				currentInt = world.getHandle().getLightLevel(LocationX, LocationY, LocationZ);
//				newInt = (currentInt + SNConfigHandler.priestLightIntensity);
//				if(newInt>15)
//					newInt=15;
//				world.getHandle().b(EnumSkyBlock.BLOCK, LocationX, LocationY, LocationZ, newInt);
////				for(int x = -radius; x <= radius; x++){
////					for(int y = -radius; y <= radius; y++){
////						for(int z = -radius; z <= radius; z++){
////							
////							Vector origin = new Vector(LocationX, LocationY, LocationZ);
////							Vector v = new Vector(LocationX + x, LocationY + y, LocationZ + z);
////
////							if(v.isInSphere(origin, radius)){
////								currentInt = world.getHandle().getLightLevel(LocationX+x, LocationY+y, LocationZ+z);
////								newInt = (currentInt + SNConfigHandler.priestLightIntensity);
////								if(newInt>15)
////									newInt=15;
////								world.getHandle().b(EnumSkyBlock.BLOCK, LocationX+x, LocationY+y, LocationZ+z, newInt);
////							}
////						}
////					}
//					playerList.put(snplayer,locationString);
////				}
//			}
//		}
//	}
	
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