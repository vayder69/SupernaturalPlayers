package me.matterz.supernaturals.util;

import java.util.ArrayList;
import java.util.List;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.io.SNConfigHandler;
import me.matterz.supernaturals.light.LightMap;
import net.minecraft.server.EnumSkyBlock;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LightUtil {
	
	public static void illuminate(Player player, Location location){
		int LocationX = location.getBlockX();
		int LocationY = location.getBlockY();
		int LocationZ = location.getBlockZ();
		
		int radius = SNConfigHandler.priestLightRadius;
		int intensity = SNConfigHandler.priestLightIntensity;
		
		int falloff = intensity / radius;
		
		CraftWorld world = (CraftWorld) player.getWorld();
		
		for (int x = -radius; x <= radius; x++){
			for (int y = -radius; y <= radius; y++){
				for (int z = -radius; z <= radius; z++){
					
					int currentInt = world.getHandle().getLightLevel(LocationX+x, LocationY+y, LocationZ+z);
					Vector origin = new Vector(LocationX, LocationY, LocationZ);
					Vector v = new Vector(LocationX + x, LocationY + y, LocationZ + z);
					
					int newIntensity;
					
					if (v.isInSphere(origin, radius)){
						double distanceSq = v.distanceSquared(origin);
						newIntensity = (int)(((intensity - Math.sqrt(distanceSq) * falloff) * 100.0D + 0.5D) / 100.0D);
					 }else{
						 newIntensity = currentInt;
					}
					Location blockLocation = new Location(world, LocationX + x, LocationY + y, LocationZ + z);
					int worldIntensity = world.getHandle().getLightLevel(LocationX + x, LocationY + y, LocationZ + z);
					if (newIntensity > worldIntensity) {
						world.getHandle().b(EnumSkyBlock.BLOCK, LocationX + x, LocationY + y, LocationZ + z, newIntensity);
					}
					//SupernaturalsPlugin.instance.getLightManager().addLightMap(player, blockLocation, (newIntensity - currentInt));
				}
			}
		}
	}
	
	public static void deluminate(Player player){
		CraftWorld world = (CraftWorld) player.getWorld();
		List<LightMap> removeMaps = new ArrayList<LightMap>();
		for(LightMap lightmap : SupernaturalsPlugin.instance.getLightManager().getLightMaps()){
			if(lightmap.getPlayer().equals(player)){
				Location blockLocation = lightmap.getLocation();
				int currentInt = world.getHandle().getLightLevel(blockLocation.getBlockX(), blockLocation.getBlockY(), blockLocation.getBlockZ());
				int changeInt = lightmap.getIntensity();
				int newInt = currentInt - changeInt;
				world.getHandle().b(EnumSkyBlock.BLOCK, blockLocation.getBlockX(), blockLocation.getBlockY(), blockLocation.getBlockZ(), newInt);
				removeMaps.add(lightmap);
			}
		}
		
		for(LightMap lightmap : removeMaps){
			SupernaturalsPlugin.instance.getLightManager().removeLightMap(lightmap);
		}

	}
}
