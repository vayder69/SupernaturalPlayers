package me.matterz.supernaturals.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.util.config.Configuration;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.util.Recipes;

public class SNConfigHandler {

	public static SupernaturalsPlugin plugin;
	
	//Config variables
	public Configuration config;
	public static boolean debugMode;
	public static boolean vampireKillSpreadCurse;
	public static boolean ghoulKillSpreadCurse;
	public static boolean wereKillSpreadCurse;
	public static boolean vampireBurnInSunlight;
	public static double vampireDamageFactor;
	public static double ghoulDamageFactor;
	public static double woodFactor;
	public static double vampireDamageReceivedFactor;
	public static double ghoulDamageReceivedFactor;
	public static double jumpDeltaSpeed;
	public static double dashDeltaSpeed;
	public static int jumpBloodCost;
	public static int dashBloodCost;
	public static double vampireAltarInfectMaterialRadius;
	public static double vampireAltarCureMaterialRadius;
	public static double vampireTimePowerGained;
	public static double vampireTimeHealthGained;
	public static double vampireHealthCost;
	public static int vampireHealingPowerMin;
	public static int truceBreakTime;
	public static int maxPower;
	public static int vampireAltarInfectMaterialSurroundCount;
	public static int vampireAltarCureMaterialSurroundCount;
	public static int vampireAltarInfectStartingPower;
	public static int vampireDeathPowerPenalty;
	public static int ghoulDeathPowerPenalty;
	public static int wereDeathPowerPenalty;
	public static int priestDeathPowerPenalty;
	public static int vampireKillPowerCreatureGain;
	public static int ghoulKillPowerCreatureGain;
	public static int wereKillPowerCreatureGain;
	public static int vampireKillPowerPlayerGain;
	public static int ghoulKillPowerPlayerGain;
	public static int wereKillPowerPlayerGain;
	public static int vampireCombustFireTicks;
	public static int vampireDrowningPowerMin;
	public static int vampireDrowningCost;
	public static int ghoulDrowningPowerMin;
	public static int priestChurchLocationX;
	public static int priestChurchLocationY;
	public static int priestChurchLocationZ;
	public static int priestLightRadius;
	public static int priestLightIntensity;
	public static int werePowerSummonMin;
	public static int werePowerSummonCost;
	public static String vampireAltarInfectMaterial;
	public static String vampireAltarCureMaterial;
	public static String vampireAltarInfectMaterialSurround;
	public static String vampireAltarCureMaterialSurround;
	public static String wolfMaterial;
	public static List<String> supernaturalTypes = new ArrayList<String>();
	
	public static List<Material> woodMaterials = new ArrayList<Material>();
	public static List<CreatureType> vampireTruce = new ArrayList<CreatureType>();
	public static List<Material> foodMaterials = new ArrayList<Material>();
	public static List<Material> jumpMaterials = new ArrayList<Material>();
	public static List<Material> ghoulWeapons = new ArrayList<Material>();
	
	private static List<String> ghoulWeaponsString = new ArrayList<String>();
	private static List<String> woodMaterialsString = new ArrayList<String>();
	private static List<String> vampireTruceString = new ArrayList<String>();
	private static List<String> foodMaterialsString = new ArrayList<String>();
	private static List<String> jumpMaterialsString = new ArrayList<String>();
	private static List<String> vampireAltarInfectMaterialsString = new ArrayList<String>();
	private static List<String> vampireAltarCureMaterialsString = new ArrayList<String>();
	private static List<Integer> vampireAltarCureQuantities = new ArrayList<Integer>();
	private static List<Integer> vampireAltarInfectQuantities = new ArrayList<Integer>();
	
	public static Map<Material,Double> materialOpacity = new HashMap<Material,Double>();
	
	public static Recipes vampireAltarInfectRecipe = new Recipes();
	public static Recipes vampireAltarCureRecipe = new Recipes();
	
	static{
		materialOpacity.put(Material.AIR, 0D);
		materialOpacity.put(Material.SAPLING, 0.3D);
		materialOpacity.put(Material.LEAVES, 0.3D);
		materialOpacity.put(Material.GLASS, 0.5D);
		materialOpacity.put(Material.YELLOW_FLOWER, 0.1D);
		materialOpacity.put(Material.RED_ROSE, 0.1D);
		materialOpacity.put(Material.BROWN_MUSHROOM, 0.1D);
		materialOpacity.put(Material.RED_MUSHROOM, 0.1D);
		materialOpacity.put(Material.TORCH, 0.1D);
		materialOpacity.put(Material.FIRE, 0D);
		materialOpacity.put(Material.MOB_SPAWNER, 0.3D);
		materialOpacity.put(Material.REDSTONE_WIRE, 0D);
		materialOpacity.put(Material.CROPS, 0.2D);
		materialOpacity.put(Material.SIGN, 0.1D);
		materialOpacity.put(Material.SIGN_POST, 0.2D);
		materialOpacity.put(Material.LEVER, 0.1D);
		materialOpacity.put(Material.STONE_PLATE, 0D);
		materialOpacity.put(Material.WOOD_PLATE, 0D);
		materialOpacity.put(Material.REDSTONE_TORCH_OFF, 0.1D);
		materialOpacity.put(Material.REDSTONE_TORCH_ON, 0.1D);
		materialOpacity.put(Material.STONE_BUTTON, 0D);
		materialOpacity.put(Material.SUGAR_CANE_BLOCK, 0.3D);
		materialOpacity.put(Material.FENCE, 0.2D);
		materialOpacity.put(Material.DIODE_BLOCK_OFF, 0D);
		materialOpacity.put(Material.DIODE_BLOCK_ON, 0D);
	}
	
	public SNConfigHandler(SupernaturalsPlugin instance){
		SNConfigHandler.plugin = instance;
	}
	
	public void getConfiguration(){ 
		config = plugin.getConfiguration();
		this.loadValues(config);
	}
	
	public void loadValues(Configuration config){
		debugMode = config.getBoolean("DebugMode", true);
		truceBreakTime = config.getInt("Supernatural.Truce.BreakTime", 30000);
		maxPower = config.getInt("Supernatural.MaxAllowedPower", 10000);
		supernaturalTypes = config.getStringList("Supernatural.Types", null);
		
		woodMaterialsString = config.getStringList("Vampire.Materials.Wooden", null);
		foodMaterialsString = config.getStringList("Vampire.Materials.Food", null);
		
		jumpMaterialsString = config.getStringList("Vampire.Jump.Materials", null);
		
		jumpDeltaSpeed = config.getDouble("Vampire.Jump.Delta", 1.5);
		jumpBloodCost = config.getInt("Vampire.Power.Jump.Cost", 10);
		dashBloodCost = config.getInt("Vampire.Power.Dash.Cost", 10);
		dashDeltaSpeed = config.getDouble("Vampire.Dash.Delta", 3);
		
		vampireKillSpreadCurse = config.getBoolean("Vampire.Kill.SpreadCurse",true);
		vampireKillPowerCreatureGain = config.getInt("Vampire.Power.Kill.CreatureGain", 10);
		vampireKillPowerPlayerGain = config.getInt("Vampire.Power.Kill.PlayerGain", 100);
		vampireDeathPowerPenalty = config.getInt("Vampire.Power.Death.Penalty", 200);
		vampireDamageFactor = config.getDouble("Vampire.Damage.Factor", 1.1);
		vampireDamageReceivedFactor = config.getDouble("Vampire.Damage.ReceivedFactor", 0.9);
		woodFactor = config.getDouble("Vampire.Damage.WoodFactor", 1.5);
		vampireBurnInSunlight = config.getBoolean("Vampire.Burn.InSunlight", true);
		vampireCombustFireTicks = config.getInt("Vampire.Burn.FireTicks", 1);
		
		vampireTruceString = config.getStringList("Vampire.Truce.Creatures", null);
		vampireTimePowerGained = config.getDouble("Vampire.Time.PowerGained", 1.0);
		vampireTimeHealthGained = config.getDouble("Vampire.Time.HealthGained", 0.1);
		vampireHealthCost = config.getDouble("Vampire.Power.Healing.Cost",3);
		vampireHealingPowerMin = config.getInt("Vampire.Power.Healing.Min", 1000);
		vampireDrowningPowerMin = config.getInt("Vampire.Power.Drowning.Min", 1000);
		vampireDrowningCost = config.getInt("Vampire.Power.Drowning.Cost", 1);
		vampireAltarInfectStartingPower = config.getInt("Vampire.Altar.Infect.StartingPower", 1000);
		
		vampireAltarInfectMaterial = config.getString("Vampire.Altar.Infect.Material","GOLD_BLOCK");
		vampireAltarInfectMaterialSurround = config.getString("Vampire.Altar.Infect.Surrounding.Material","OBSIDIAN");
		vampireAltarInfectMaterialRadius = config.getDouble("Vampire.Altar.Infect.Surrounding.Radius",7D);
		vampireAltarInfectMaterialSurroundCount = config.getInt("Vampire.Altar.Infect.Surrounding.Count",20);
		vampireAltarInfectMaterialsString = config.getStringList("Vampire.Altar.Infect.Recipe.Materials", null);
		vampireAltarInfectQuantities = config.getIntList("Vampire.Altar.Infect.Recipe.Quantities", null);
		
		vampireAltarCureMaterial = config.getString("Vampire.Altar.Cure.Material","LAPIS_BLOCK");
		vampireAltarCureMaterialSurround = config.getString("Vampire.Altar.Cure.Surrounding.Material","GLOWSTONE");
		vampireAltarCureMaterialRadius = config.getDouble("Vampire.Altar.Cure.Surrounding.Radius", 7D);
		vampireAltarCureMaterialSurroundCount = config.getInt("Vampire.Altar.Cure.Surrounding.Count",20);
		vampireAltarCureMaterialsString = config.getStringList("Vampire.Altar.Cure.Recipe.Materials", null);
		vampireAltarCureQuantities = config.getIntList("Vampire.Altar.Cure.Recipe.Quantities", null);
		
		priestChurchLocationX = config.getInt("Priest.Church.Location.X", 0);
		priestChurchLocationY = config.getInt("Priest.Church.Location.Y", 80);
		priestChurchLocationZ = config.getInt("Priest.Church.Location.Z", 0);
		
		priestLightRadius = config.getInt("Priest.Light.Radius", 2);
		priestLightIntensity = config.getInt("Priest.Light.Intensity", 8);
		priestDeathPowerPenalty = config.getInt("Priest.Death.PowerPenalty", 200);
		
		ghoulKillSpreadCurse = config.getBoolean("Ghoul.Kill.SpreadCurse", true);
		ghoulKillPowerCreatureGain = config.getInt("Ghoul.Power.Kill.CreatureGain", 10);
		ghoulKillPowerPlayerGain = config.getInt("Ghoul.Power.Kill.PlayerGain", 100);
		ghoulDeathPowerPenalty = config.getInt("Ghoul.Power.Death.Penalty", 200);
		ghoulDrowningPowerMin = config.getInt("Ghoul.Power.Drowning.Min", 1000);
		ghoulDamageReceivedFactor = config.getDouble("Ghoul.Damage.RecievedFactor", 0.5);
		ghoulWeaponsString = config.getStringList("Ghoul.WeaponsDisabled", null);
		ghoulDamageFactor = config.getDouble("Ghoul.Damage.Factor", 2);
		
		wereDeathPowerPenalty = config.getInt("Were.Power.Death.Penalty", 200);
		wereKillPowerCreatureGain = config.getInt("Were.Power.Kill.CreatureGain", 10);
		wereKillPowerPlayerGain = config.getInt("Were.Power.Kill.PlayerGain", 100);
		werePowerSummonMin = config.getInt("Were.Power.Summon.Min", 1000);
		wereKillSpreadCurse = config.getBoolean("Were.Kill.SpreadCurse", true);
		wolfMaterial = config.getString("Were.Summon.Material", "PORK");
		werePowerSummonCost = config.getInt("Were.Power.Summon.Cost", 200);
		
		
		if(supernaturalTypes.size() == 0){
			supernaturalTypes.add("human");
			supernaturalTypes.add("vampire");
			supernaturalTypes.add("werewolf");
			supernaturalTypes.add("ghoul");
			supernaturalTypes.add("priest");
			config.setProperty("Supernatural.Types", supernaturalTypes);
		}
		
		if(woodMaterialsString.size() == 0){
			woodMaterialsString.add("STICK");
			woodMaterialsString.add("WOOD_AXE");
			woodMaterialsString.add("WOOD_HOE");
			woodMaterialsString.add("WOOD_PICKAXE");
			woodMaterialsString.add("WOOD_SPADE");
			woodMaterialsString.add("WOOD_SWORD");
			woodMaterialsString.add("BOW");
			config.setProperty("Vampire.Materials.Wooden", woodMaterialsString);
		}
		
		if(foodMaterialsString.size() == 0){
			foodMaterialsString.add("APPLE");
			foodMaterialsString.add("BREAD");
			foodMaterialsString.add("COOKED_FISH");
			foodMaterialsString.add("GRILLED_PORK");
			foodMaterialsString.add("GOLDEN_APPLE");
			foodMaterialsString.add("MUSHROOM_SOUP");
			foodMaterialsString.add("RAW_FISH");
			foodMaterialsString.add("PORK");
			config.setProperty("Vampire.Materials.Food", foodMaterialsString);
		}
		
		if(jumpMaterialsString.size() == 0){
			jumpMaterialsString.add("RED_ROSE");
			config.setProperty("Vampire.Jump.Materials", jumpMaterialsString);
		}
		
		if(vampireTruceString.size() == 0){
			vampireTruceString.add("CREEPER");
			vampireTruceString.add("GHAST");
			vampireTruceString.add("SKELETON");
			vampireTruceString.add("SPIDER");
			vampireTruceString.add("ZOMBIE");
			config.setProperty("Vampire.Truce.Creatures", vampireTruceString);
		}
		
		if(vampireAltarInfectMaterialsString.size() == 0){
			vampireAltarInfectMaterialsString.add("MUSHROOM_SOUP");
			vampireAltarInfectMaterialsString.add("BONE");
			vampireAltarInfectMaterialsString.add("SULPHUR");
			vampireAltarInfectMaterialsString.add("REDSTONE");
			config.setProperty("Vampire.Altar.Infect.Recipe.Materials", vampireAltarInfectMaterialsString);
		}
		
		if(vampireAltarInfectQuantities.size() == 0){
			vampireAltarInfectQuantities.add(1);
			vampireAltarInfectQuantities.add(10);
			vampireAltarInfectQuantities.add(10);
			vampireAltarInfectQuantities.add(10);
			config.setProperty("Vampire.Altar.Infect.Recipe.Quantities",vampireAltarInfectQuantities);
		}
		
		if(vampireAltarCureMaterialsString.size() == 0){
			vampireAltarCureMaterialsString.add("WATER_BUCKET");
			vampireAltarCureMaterialsString.add("DIAMOND");
			vampireAltarCureMaterialsString.add("SUGAR");
			vampireAltarCureMaterialsString.add("WHEAT");
			config.setProperty("Vampire.Altar.Cure.Recipe.Materials", vampireAltarCureMaterialsString);
		}
		
		if(vampireAltarCureQuantities.size() == 0){
			vampireAltarCureQuantities.add(1);
			vampireAltarCureQuantities.add(1);
			vampireAltarCureQuantities.add(20);
			vampireAltarCureQuantities.add(20);
			config.setProperty("Vampire.Altar.Cure.Recipe.Quantities",vampireAltarCureQuantities);
		}
		
		if(ghoulWeaponsString.size() == 0){
			ghoulWeaponsString.add("BOW");
			ghoulWeaponsString.add("STICK");
			ghoulWeaponsString.add("WOOD_SWORD");
			ghoulWeaponsString.add("WOOD_PICKAXE");
			ghoulWeaponsString.add("WOOD_SPADE");
			ghoulWeaponsString.add("WOOD_AXE");
			ghoulWeaponsString.add("WOOD_HOE");
			ghoulWeaponsString.add("STONE_SWORD");
			ghoulWeaponsString.add("STONE_PICKAXE");
			ghoulWeaponsString.add("STONE_SPADE");
			ghoulWeaponsString.add("STONE_AXE");
			ghoulWeaponsString.add("STONE_HOE");
			ghoulWeaponsString.add("IRON_SWORD");
			ghoulWeaponsString.add("IRON_PICKAXE");
			ghoulWeaponsString.add("IRON_SPADE");
			ghoulWeaponsString.add("IRON_AXE");
			ghoulWeaponsString.add("IRON_HOE");
			ghoulWeaponsString.add("GOLD_SWORD");
			ghoulWeaponsString.add("GOLD_PICKAXE");
			ghoulWeaponsString.add("GOLD_SPADE");
			ghoulWeaponsString.add("GOLD_AXE");
			ghoulWeaponsString.add("GOLD_HOE");
			ghoulWeaponsString.add("DIAMOND_SWORD");
			ghoulWeaponsString.add("DIAMOND_PICKAXE");
			ghoulWeaponsString.add("DIAMOND_SPADE");
			ghoulWeaponsString.add("DIAMOND_AXE");
			ghoulWeaponsString.add("DIAMOND_HOE");
		}

		config.save();
		
		for(String wood : woodMaterialsString){
			woodMaterials.add(Material.getMaterial(wood));
		}
		
		for(String food : foodMaterialsString){
			foodMaterials.add(Material.getMaterial(food));
		}
		
		for(String creature : vampireTruceString){
			vampireTruce.add(CreatureType.fromName(creature));
		}
		
		for(String jump : jumpMaterialsString){
			jumpMaterials.add(Material.getMaterial(jump));
		}
		
		for(String weapon : ghoulWeaponsString){
			ghoulWeapons.add(Material.getMaterial(weapon));
		}
		
		for(int i=0; i<vampireAltarInfectMaterialsString.size();i++){
			Material material = Material.getMaterial(vampireAltarInfectMaterialsString.get(i));
			int quantity=1;
			try{
				quantity = vampireAltarInfectQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Vampire Infect Altar Quantities!");
			}				
			vampireAltarInfectRecipe.materialQuantities.put(material,quantity);
		}
		
		for(int i=0; i<vampireAltarCureMaterialsString.size();i++){
			Material material = Material.getMaterial(vampireAltarCureMaterialsString.get(i));
			int quantity=1;
			try{
				quantity = vampireAltarCureQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Vampire Cure Altar Quantities!");
			}
			vampireAltarCureRecipe.materialQuantities.put(material,quantity);
		}
	}
	
	public void saveConfig(){
		config.save();
	}
	
	public void reloadConfig(){
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Reloaded configuration file");
		}
		config.load();
		this.loadValues(config);
	}
}
