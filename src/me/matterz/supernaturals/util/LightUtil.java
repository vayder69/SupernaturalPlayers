package me.matterz.supernaturals.util;

import java.util.HashMap;
import java.util.Map;

import me.matterz.supernaturals.io.SNConfigHandler;
import net.minecraft.server.EnumSkyBlock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LightUtil {
	
	private static Map<String, Integer> lightMap = new HashMap<String, Integer>();
	
	public static void illuminate(Player player, Location location){
		int LocationX = location.getBlockX();
		int LocationY = location.getBlockY();
		int LocationZ = location.getBlockZ();
		
		int radius = SNConfigHandler.priestLightRadius;
		int intensity = SNConfigHandler.priestLightIntensity;
		
		int falloff = intensity / radius;
		
		CraftWorld world = (CraftWorld) player.getWorld();
		
		for (int x = -(radius+2); x <= (radius+2); x++){
			for (int y = -(radius+2); y <= (radius+2); y++){
				for (int z = -(radius+2); z <= (radius+2); z++){
					
					if(Math.abs(x)<radius && Math.abs(y)<radius && Math.abs(z)<radius){
						Vector origin = new Vector(LocationX, LocationY, LocationZ);
						Vector v = new Vector(LocationX + x, LocationY + y, LocationZ + z);
						
						int newIntensity;
						int currentInt;
						
						Location blockLocation = new Location(world, LocationX+x, LocationY+y, LocationZ+z);
						String locationString = (world.getName()+":"+blockLocation.getBlockX()+":"+blockLocation.getBlockY()+":"+blockLocation.getBlockZ());
						if(!lightMap.containsKey(locationString)) {
							currentInt = world.getHandle().getLightLevel(LocationX+x, LocationY+y, LocationZ+z);
							lightMap.put(locationString, currentInt);
						} else {
							currentInt = lightMap.get(locationString);
						}
						
						if (v.isInSphere(origin, radius)){
							double distanceSq = v.distanceSquared(origin);
							newIntensity = (int)(((intensity - Math.sqrt(distanceSq) * falloff) * 100.0D + 0.5D) / 100.0D);
						 }else{
							 newIntensity = currentInt;
						}
						int worldIntensity = world.getHandle().getLightLevel(LocationX + x, LocationY + y, LocationZ + z);
						if (newIntensity > worldIntensity) {
							world.getHandle().b(EnumSkyBlock.BLOCK, LocationX + x, LocationY + y, LocationZ + z, newIntensity);
						}
					} else {
						Location blockLocation = new Location(world, LocationX+x, LocationY+y, LocationZ+z);
						String locationString = (world.getName()+":"+blockLocation.getBlockX()+":"+blockLocation.getBlockY()+":"+blockLocation.getBlockZ());
						if(lightMap.containsKey(locationString)){
							world.getHandle().b(EnumSkyBlock.BLOCK, LocationX + x, LocationY + y, LocationZ + z, lightMap.remove(locationString));
						}
					}
				}
			}
		}
	}
}
