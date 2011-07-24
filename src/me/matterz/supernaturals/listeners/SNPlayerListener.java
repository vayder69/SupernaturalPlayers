package me.matterz.supernaturals.listeners;

import me.matterz.supernaturals.SuperNPlayer;
import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.manager.SupernaturalManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.material.Door;

public class SNPlayerListener extends PlayerListener{

	public static SupernaturalsPlugin plugin;
	private String permissions = "supernatural.player.shrineuse";
	private String permissions2 = "supernatural.player.wolfbane";
	
	public SNPlayerListener(SupernaturalsPlugin instance){
		SNPlayerListener.plugin = instance;
	}
	
//	@Override
//	public void onPlayerToggleSneak(PlayerToggleSneakEvent event){
//		Player player = event.getPlayer();
//		SuperNPlayer snplayer = SupernaturalManager.get(player);
//		if(snplayer.isHunter()){
//			player.setSneaking(true);
//			event.setCancelled(true);
//		}
//	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event){
		Action action = event.getAction();		
		
		if(!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_AIR)) && event.isCancelled()){
			return;
		}
		
		Player player = event.getPlayer();
		SuperNPlayer snplayer = SupernaturalManager.get(player);
		boolean cancelled = false;
		Material itemMaterial = event.getMaterial();
		
		if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)){
			if(player.getItemInHand()==null){
				return;
			}
			
			if(snplayer.isVampire()){
				if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
					SupernaturalManager.jump(player, SNConfigHandler.jumpDeltaSpeed, true);
					event.setCancelled(true);
					return;
				}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.vampireMaterial)){
					plugin.getVampireManager().teleport(player);
					event.setCancelled(true);
					return;
				}
			}else if(snplayer.isWere()){
					if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.wolfMaterial)){
						if(SupernaturalManager.worldTimeIsNight(player)){
							plugin.getWereManager().summon(player);
							event.setCancelled(true);
							return;
						}else{
							SupernaturalManager.sendMessage(snplayer, "Cannot use this ability during the day.");
							return;
						}
					}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.wolfbaneMaterial)){
						if(!SupernaturalsPlugin.hasPermissions(player, permissions2)){
							return;
						}
						if(SupernaturalManager.worldTimeIsNight(player)){
							SupernaturalManager.sendMessage(snplayer, "Cannot cure lycanthropy during the night.");
							return;
						}else{
							plugin.getWereManager().wolfbane(player);
							event.setCancelled(true);
							return;
						}
					}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.dashMaterial)){
						if(SupernaturalManager.worldTimeIsNight(player)){
							SupernaturalManager.jump(event.getPlayer(), SNConfigHandler.dashDeltaSpeed, false);
							event.setCancelled(true);
							return;
						}else{
							SupernaturalManager.sendMessage(snplayer, "Cannot use this ability during the day.");
							return;
						}
					}
			}else if(snplayer.isGhoul()){
				if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.ghoulMaterial)){
					plugin.getGhoulManager().summon(player);
					event.setCancelled(true);
					return;
				}
			}else if(snplayer.isPriest()){
				if(SNConfigHandler.priestSpellMaterials.contains(itemMaterial)){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(snplayer.getName() + " is attempting to cast a spell...");
					Player victim = plugin.getSuperManager().getTarget(player);
					if(victim == null)
						return;
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(victim.getName()+" is targetted by spell.");
					if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(0))){
						plugin.getPriestManager().banish(player, victim);
						cancelled = false;
					}else if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(1))){
						plugin.getPriestManager().exorcise(player, victim);
						cancelled = false;
					}else if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(2))){
						cancelled = plugin.getPriestManager().cure(player, victim, itemMaterial);
					}else if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(3))){
						cancelled = plugin.getPriestManager().heal(player, victim);
					}else if(itemMaterial.equals(SNConfigHandler.priestSpellMaterials.get(4))){
						plugin.getPriestManager().drainPower(player, victim);
						cancelled = false;
					}
					if(!event.isCancelled())
						event.setCancelled(cancelled);
					return;
				}
			}else if(snplayer.isDemon()){
				if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.demonMaterial)){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(player.getName()+" is casting FIREBALL with "+itemMaterial.toString());
					cancelled = plugin.getDemonManager().fireball(player);
					if(!event.isCancelled() && cancelled)
						event.setCancelled(true);
					return;
				}else if(itemMaterial.toString().equalsIgnoreCase(SNConfigHandler.demonSnareMaterial)){
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(player.getName()+" is casting SNARE with "+itemMaterial.toString());
					Player target = plugin.getSuperManager().getTarget(player);
					cancelled = plugin.getDemonManager().snare(player, target);
					if(!event.isCancelled() && cancelled)
						event.setCancelled(true);
					return;
				}
			}else if(snplayer.isHunter()){
				if(player.getItemInHand().getType().equals(Material.BOW)){
					plugin.getHunterManager().changeArrowType(snplayer);
					return;
				}
			}
		}
		
		if(!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))){
			return;
		}
		
		if(snplayer.isHunter()){
			if(player.getItemInHand().getType().equals(Material.BOW)){
				if(player.getInventory().contains(Material.ARROW)){
					cancelled = plugin.getHunterManager().shoot(player);
					if(cancelled){
						event.setUseInteractedBlock(Event.Result.DENY);
						event.setCancelled(true);
					}
					return;
				}else{
					return;
				}
			}
		}
		
		if(action.equals(Action.RIGHT_CLICK_AIR)){
			if(SNConfigHandler.foodMaterials.contains(itemMaterial)){
				if(snplayer.isVampire())
				{
					if(SNConfigHandler.debugMode)
						SupernaturalsPlugin.log(snplayer.getName() + " attempted to eat " + itemMaterial.toString());
					SupernaturalManager.sendMessage(snplayer, "Vampires can't eat food. You must drink blood instead.");
					event.setCancelled(true);
					return;
				}else if(snplayer.isWere()){
					if(itemMaterial.equals(Material.BREAD)){
						SupernaturalManager.sendMessage(snplayer, "Werewolves do not gain power from Bread.");
						return;
					}else{
						SupernaturalManager.alterPower(snplayer, SNConfigHandler.werePowerFood, "Eating!");
						if(SNConfigHandler.debugMode)
							SupernaturalsPlugin.log(snplayer.getName() + " ate " + itemMaterial.toString() + " to gain " + SNConfigHandler.werePowerFood + " power!");
						return;
					}
				}
				return;
			}
			return;
		}else if(!(action.equals(Action.RIGHT_CLICK_BLOCK))){
			return;
		}
		
		Material blockMaterial = event.getClickedBlock().getType();
		
		Block block = event.getClickedBlock();
		if(block.getType().equals(Material.IRON_DOOR_BLOCK) || block.getType().equals(Material.WOODEN_DOOR)){
			Location blockLoc = block.getLocation();
			for(int x = blockLoc.getBlockX()-2; x < blockLoc.getBlockX()+3; x++){
				for(int y = blockLoc.getBlockY()-2; y < blockLoc.getBlockY()+3; y++){
					for(int z = blockLoc.getBlockZ()-2; z < blockLoc.getBlockZ()+3; z++){
						Location newLoc = new Location(block.getWorld(), x, y, z);
						Block newBlock = newLoc.getBlock();
						if(newBlock.getType().equals(Material.SIGN_POST)){
							Sign sign = (Sign) newBlock;
							String[] text = sign.getLines();
							for(int i = 0; i < text.length; i++){
								if(text[i].contains("WitchHunters' Hall")){
									Door door = (Door) block;
									if(snplayer.isHuman()){
										boolean open = plugin.getHunterManager().join(snplayer);
										event.setCancelled(open);
										return;
									}else if(snplayer.isHunter()){
										door.setOpen(true);
										event.setCancelled(true);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
		
		if(!SupernaturalsPlugin.hasPermissions(player, permissions)){
			return;
		}
		
		if(blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarInfectMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggered a Vampire Infect Altar.");
			plugin.getVampireManager().useAltarInfect(player, event.getClickedBlock());
		}else if(blockMaterial == Material.getMaterial(SNConfigHandler.vampireAltarCureMaterial)) {
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggered a Vampire Cure Altar.");
			plugin.getVampireManager().useAltarCure(player, event.getClickedBlock());
		}else if(blockMaterial == Material.getMaterial(SNConfigHandler.priestAltarMaterial)){
			if(SNConfigHandler.debugMode)
				SupernaturalsPlugin.log(snplayer.getName() + " triggered a Priest Altar.");
			plugin.getPriestManager().useAltar(player);
		}
	}
	
	@Override
	public void onPlayerKick(PlayerKickEvent event) {
		if(event.isCancelled()){
			return;
		}
		if ((event.getLeaveMessage().contains("Flying")) || (event.getReason().contains("Flying"))) {
			SuperNPlayer snplayer = SupernaturalManager.get(event.getPlayer());
			if(snplayer.isVampire()&& event.getPlayer().getItemInHand().getType().toString().equalsIgnoreCase(SNConfigHandler.jumpMaterial)){
				event.setCancelled(true);
				if(SNConfigHandler.debugMode)
					SupernaturalsPlugin.log(event.getPlayer().getName() + " was not kicked for flying as a vampire.");
			} 
		}
	}
}
