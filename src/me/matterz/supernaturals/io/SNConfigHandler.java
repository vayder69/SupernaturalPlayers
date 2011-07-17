package me.matterz.supernaturals.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.CreatureType;
import org.bukkit.util.config.Configuration;

import me.matterz.supernaturals.SupernaturalsPlugin;
import me.matterz.supernaturals.util.Recipes;

public class SNConfigHandler {

	public static SupernaturalsPlugin plugin;
	
	//Config variables
	public static Configuration config;
	public static boolean debugMode;
	public static boolean vampireKillSpreadCurse;
	public static boolean ghoulKillSpreadCurse;
	public static boolean wereKillSpreadCurse;
	public static boolean vampireBurnInSunlight;
	public static boolean wolfTruce;
	public static double spreadChance;
	public static double vampireDamageFactor;
	public static double ghoulDamageFactor;
	public static double woodFactor;
	public static double vampireDamageReceivedFactor;
	public static double ghoulDamageReceivedFactor;
	public static double jumpDeltaSpeed;
	public static double dashDeltaSpeed;
	public static double ghoulMoveSpeed;
	public static double ghoulHealthGained;
	public static double wereHealthGained;
	public static double vampireAltarInfectMaterialRadius;
	public static double vampireAltarCureMaterialRadius;
	public static double vampireTimePowerGained;
	public static double vampireTimeHealthGained;
	public static double vampireHealthCost;
	public static double wereDamageFall;
	public static double wereDamageFactor;
	public static double wereDamageReceivedFactor;
	public static double priestDamageFactorAttackSuper;
	public static double priestDamageFactorAttackHuman;
	public static double priestDrainFactor;
	public static int jumpBloodCost;
	public static int dashBloodCost;
	public static int vampireHealingPowerMin;
	public static int truceBreakTime;
	public static int maxPower;
	public static int vampireAltarInfectMaterialSurroundCount;
	public static int vampireAltarCureMaterialSurroundCount;
	public static int vampirePowerStart;
	public static int ghoulPowerStart;
	public static int ghoulDamageWater;
	public static int werePowerStart;
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
	public static int vampireTeleportCost;
	public static int priestLightRadius;
	public static int priestLightIntensity;
	public static int priestPowerBanish;
	public static int priestPowerHeal;
	public static int priestPowerCure;
	public static int priestPowerExorcise;
	public static int priestPowerDrain;
	public static int priestHealAmount;
	public static int priestPowerStart;
	public static int priestPowerDonation;
	public static int werePowerSummonMin;
	public static int werePowerSummonCost;
	public static int werePowerFood;
	public static int ghoulPowerSummonMin;
	public static int ghoulPowerSummonCost;
	public static String vampireAltarInfectMaterial;
	public static String vampireAltarCureMaterial;
	public static String vampireAltarInfectMaterialSurround;
	public static String vampireAltarCureMaterialSurround;
	public static String priestAltarMaterial;
	public static String wolfMaterial;
	public static String wolfbaneMaterial;
	public static String ghoulMaterial;
	public static String vampireMaterial;
	public static String jumpMaterial;
	public static String dashMaterial;
	public static Location vampireTeleportLocation;
	public static Location priestChurchLocation;
	public static Location priestBanishLocation;
	public static List<String> supernaturalTypes = new ArrayList<String>();
	
	public static List<Material> woodMaterials = new ArrayList<Material>();
	public static List<CreatureType> vampireTruce = new ArrayList<CreatureType>();
	public static List<Material> foodMaterials = new ArrayList<Material>();
	public static List<Material> ghoulWeapons = new ArrayList<Material>();
	public static List<Material> ghoulWeaponImmunity = new ArrayList<Material>();
	public static List<CreatureType> ghoulTruce = new ArrayList<CreatureType>();
	public static List<Material> priestSpellMaterials = new ArrayList<Material>();
	public static List<Material> priestDonationMaterials = new ArrayList<Material>();
	
	public static String vampireTeleportWorld;
	public static int vampireTeleportLocationX;
	public static int vampireTeleportLocationY;
	public static int vampireTeleportLocationZ;
	public static String priestChurchWorld;
	public static int priestChurchLocationX;
	public static int priestChurchLocationY;
	public static int priestChurchLocationZ;
	public static String priestBanishWorld;
	public static int priestBanishLocationX;
	public static int priestBanishLocationY;
	public static int priestBanishLocationZ;
	
	private static List<String> ghoulWeaponsString = new ArrayList<String>();
	private static List<String> ghoulWeaponImmunityString = new ArrayList<String>();
	private static List<String> woodMaterialsString = new ArrayList<String>();
	private static List<String> vampireTruceString = new ArrayList<String>();
	private static List<String> foodMaterialsString = new ArrayList<String>();
	private static List<String> ghoulTruceString = new ArrayList<String>();
	private static List<String> vampireAltarInfectMaterialsString = new ArrayList<String>();
	private static List<String> vampireAltarCureMaterialsString = new ArrayList<String>();
	private static List<Integer> vampireAltarCureQuantities = new ArrayList<Integer>();
	private static List<Integer> vampireAltarInfectQuantities = new ArrayList<Integer>();
	private static List<String> priestMaterialsString = new ArrayList<String>();
	private static List<String> priestAltarMaterialsString = new ArrayList<String>();
	private static List<Integer> priestAltarQuantities = new ArrayList<Integer>();
	private static List<String> priestDonationMaterialsString = new ArrayList<String>();
	private static List<String> wereWolfbaneMaterialsString = new ArrayList<String>();
	private static List<Integer> wereWolfbaneQuantities = new ArrayList<Integer>();
	
	public static Map<Material,Double> materialOpacity = new HashMap<Material,Double>();
	
	public static Recipes vampireAltarInfectRecipe = new Recipes();
	public static Recipes vampireAltarCureRecipe = new Recipes();
	public static Recipes priestAltarRecipe = new Recipes();
	public static Recipes wereWolfbaneRecipe = new Recipes();
	
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
	
	public static void getConfiguration(){ 
		config = plugin.getConfiguration();
		loadValues(config);
	}
	
	public static void loadValues(Configuration config){
		debugMode = config.getBoolean("DebugMode", true);
		truceBreakTime = config.getInt("Supernatural.Truce.BreakTime", 30000);
		maxPower = config.getInt("Supernatural.MaxAllowedPower", 10000);
		supernaturalTypes = config.getStringList("Supernatural.Types", null);
		spreadChance = config.getDouble("Supernatural.SpreadChance", 0.6);
		
		woodMaterialsString = config.getStringList("Vampire.Materials.Wooden", null);
		foodMaterialsString = config.getStringList("Vampire.Materials.Food", null);
		
		jumpMaterial = config.getString("Vampire.Materials.Jump", "RED_ROSE");
		dashMaterial = config.getString("Were.Materials.Dash", "FEATHER");
		
		jumpDeltaSpeed = config.getDouble("Vampire.Jump.Delta", 1.5);
		jumpBloodCost = config.getInt("Vampire.Power.Jump.Cost", 10);
		dashBloodCost = config.getInt("Were.Power.Dash.Cost", 10);
		dashDeltaSpeed = config.getDouble("Were.Dash.Delta", 3);
		
		vampireKillSpreadCurse = config.getBoolean("Vampire.Kill.SpreadCurse",true);
		vampireKillPowerCreatureGain = config.getInt("Vampire.Power.Kill.CreatureGain", 10);
		vampireKillPowerPlayerGain = config.getInt("Vampire.Power.Kill.PlayerGain", 100);
		vampireDeathPowerPenalty = config.getInt("Vampire.Power.Death.Penalty", 200);
		vampireDamageFactor = config.getDouble("Vampire.DamageFactor.Attack", 1.1);
		vampireDamageReceivedFactor = config.getDouble("Vampire.DamageFactor.Defense", 0.9);
		woodFactor = config.getDouble("Vampire.DamageFactor.Wood", 1.5);
		vampireBurnInSunlight = config.getBoolean("Vampire.Burn.InSunlight", true);
		vampireCombustFireTicks = config.getInt("Vampire.Burn.FireTicks", 1);
		
		vampireTruceString = config.getStringList("Vampire.Truce.Creatures", null);
		vampireTimePowerGained = config.getDouble("Vampire.Time.PowerGained", 1.0);
		vampireTimeHealthGained = config.getDouble("Vampire.Time.HealthGained", 0.1);
		vampireHealthCost = config.getDouble("Vampire.Power.Healing.Cost",3);
		vampireHealingPowerMin = config.getInt("Vampire.Power.Healing.Min", 1000);
		vampireDrowningPowerMin = config.getInt("Vampire.Power.Drowning.Min", 1000);
		vampireDrowningCost = config.getInt("Vampire.Power.Drowning.Cost", 1);
		vampirePowerStart = config.getInt("Vampire.Power.Start", 1000);
		vampireMaterial = config.getString("Vampire.Spell.Material", "BOOK");
		vampireTeleportCost = config.getInt("Vampire.Power.TeleportCost", 1000);
		
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
		
		vampireTeleportWorld = config.getString("Vampire.Teleport.World", "world");
		vampireTeleportLocationX = config.getInt("Vampire.Teleport.Location.X", 0);
		vampireTeleportLocationY = config.getInt("Vampire.Teleport.Location.Y", 80);
		vampireTeleportLocationZ = config.getInt("Vampire.Teleport.Location.Z", 0);
		
		priestChurchWorld = config.getString("Priest.Church.World", "world");
		priestChurchLocationX = config.getInt("Priest.Church.Location.X", 0);
		priestChurchLocationY = config.getInt("Priest.Church.Location.Y", 80);
		priestChurchLocationZ = config.getInt("Priest.Church.Location.Z", 0);
		priestBanishWorld = config.getString("Priest.Banish.World", "world");
		priestBanishLocationX = config.getInt("Priest.Banish.Location.X", 0);
		priestBanishLocationY = config.getInt("Priest.Banish.Location.Y", 80);
		priestBanishLocationZ = config.getInt("Priest.Banish.Location.Z", 0);
		
		priestLightRadius = config.getInt("Priest.Light.Radius", 2);
		priestLightIntensity = config.getInt("Priest.Light.Intensity", 8);
		priestDeathPowerPenalty = config.getInt("Priest.Death.PowerPenalty", 200);
		priestDamageFactorAttackSuper = config.getDouble("Priest.DamageFactor.AttackSuper", 3.0);
		priestDamageFactorAttackHuman = config.getDouble("Priest.DamageFactor.AttackHuman", 0.5);
		priestPowerDonation = config.getInt("Priest.Power.Donations", 10);
		priestPowerBanish = config.getInt("Priest.Power.Banish", 1000);
		priestPowerHeal = config.getInt("Priest.Power.Heal", 500);
		priestPowerExorcise = config.getInt("Priest.Power.Exorcise", 2000);
		priestPowerCure = config.getInt("Priest.Power.Cure", 500);
		priestPowerDrain = config.getInt("Priest.Power.Drain", 100);
		priestDrainFactor = config.getDouble("Priest.Spell.DrainFactor", 0.1);
		priestHealAmount = config.getInt("Priest.Spell.HealAmount", 5);
		priestPowerStart = config.getInt("Priest.Power.StartingAmount", 1000);
		priestAltarMaterial = config.getString("Priest.Church.Altar.Material","DIAMOND_BLOCK");
		priestMaterialsString = config.getStringList("Priest.Spell.Material", null);
		priestAltarMaterialsString = config.getStringList("Priest.Church.Recipe.Materials", null);
		priestAltarQuantities = config.getIntList("Priest.Church.Recipe.Quantities", null);
		priestDonationMaterialsString = config.getStringList("Priest.Church.DonationMaterials", null);
		
		ghoulPowerStart = config.getInt("Ghoul.Power.Start", 1000);
		ghoulKillSpreadCurse = config.getBoolean("Ghoul.Kill.SpreadCurse", true);
		ghoulKillPowerCreatureGain = config.getInt("Ghoul.Power.Kill.CreatureGain", 10);
		ghoulKillPowerPlayerGain = config.getInt("Ghoul.Power.Kill.PlayerGain", 100);
		ghoulDeathPowerPenalty = config.getInt("Ghoul.Power.Death.Penalty", 200);
		ghoulDamageReceivedFactor = config.getDouble("Ghoul.DamageFactor.Defense", 0.5);
		ghoulWeaponsString = config.getStringList("Ghoul.Weapon.Disabled", null);
		ghoulTruceString = config.getStringList("Ghoul.TruceString", null);
		ghoulDamageFactor = config.getDouble("Ghoul.DamageFactor.Attack", 3);
		ghoulDamageWater = config.getInt("Ghoul.WaterDamage", 3);
		ghoulMoveSpeed = config.getDouble("Ghoul.Move.Speed", 0.9);
		ghoulHealthGained = config.getDouble("Ghoul.Time.HealthGained", 0.5);
		ghoulMaterial = config.getString("Ghoul.Summon.Material", "PORK");
		ghoulPowerSummonMin = config.getInt("Ghoul.Power.Summon.Min", 5000);
		ghoulPowerSummonCost = config.getInt("Ghoul.Power.Summon.Cost", 1000);
		ghoulWeaponImmunityString = config.getStringList("Ghoul.Weapon.Immunity", null);
		
		werePowerStart = config.getInt("Were.Power.Start", 1000);
		wereDeathPowerPenalty = config.getInt("Were.Power.Death.Penalty", 200);
		wereKillPowerCreatureGain = config.getInt("Were.Power.Kill.CreatureGain", 10);
		wereKillPowerPlayerGain = config.getInt("Were.Power.Kill.PlayerGain", 10);
		werePowerSummonMin = config.getInt("Were.Power.Summon.Min", 2000);
		wereKillSpreadCurse = config.getBoolean("Were.Kill.SpreadCurse", true);
		wolfMaterial = config.getString("Were.Summon.Material", "PORK");
		wolfbaneMaterial = config.getString("Were.Wolfbane.Trigger", "BOWL");
		werePowerSummonCost = config.getInt("Were.Power.Summon.Cost", 1000);
		wolfTruce = config.getBoolean("Were.WolfTruce", true);
		werePowerFood = config.getInt("Were.Power.Food", 20);
		wereDamageFall = config.getDouble("Were.DamageFactor.Fall", 0.5);
		wereDamageFactor = config.getDouble("Were.DamageFactor.Attack", 1.3);
		wereDamageReceivedFactor = config.getDouble("Were.DamageFactor.Defense", 0.7);
		wereHealthGained = config.getDouble("Were.Time.HealthGained", 0.7);
		wereWolfbaneMaterialsString = config.getStringList("Were.Wolfbane.Materials", null);
		wereWolfbaneQuantities = config.getIntList("Were.Wolfbane.Quantities", null);
		
		
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
		
		if(vampireTruceString.size() == 0){
			vampireTruceString.add("CREEPER");
			vampireTruceString.add("SKELETON");
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
		
		if(priestAltarMaterialsString.size() == 0){
			priestAltarMaterialsString.add("DIAMOND");
			priestAltarMaterialsString.add("GOLD_INGOT");
			priestAltarMaterialsString.add("GLOWSTONE_DUST");
			priestAltarMaterialsString.add("REDSTONE");
			config.setProperty("Priest.Church.Recipe.Materials", priestAltarMaterialsString);
		}
		
		if(priestAltarQuantities.size() == 0){
			priestAltarQuantities.add(2);
			priestAltarQuantities.add(4);
			priestAltarQuantities.add(4);
			priestAltarQuantities.add(8);
			config.setProperty("Priest.Church.Recipe.Quantities", priestAltarQuantities);
		}
		
		if(priestMaterialsString.size() == 0){
			priestMaterialsString.add("INK_SACK");
			priestMaterialsString.add("SUGAR_CANE");
			priestMaterialsString.add("COAL");
			priestMaterialsString.add("PAPER");
			priestMaterialsString.add("BOOK");
			config.setProperty("Priest.Spell.Material", priestMaterialsString);
		}
		
		if(priestDonationMaterialsString.size() == 0){
			priestDonationMaterialsString.add("RAW_FISH");
			priestDonationMaterialsString.add("COOKED_FISH");
			priestDonationMaterialsString.add("GRILLED_PORK");
			config.setProperty("Priest.Church.DonationMaterials", priestDonationMaterialsString);
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
			config.setProperty("Ghoul.Weapon.Disabled", ghoulWeaponsString);
		}
		
		if(ghoulWeaponImmunityString.size() == 0){
			ghoulWeaponImmunityString.add("BOW");
			ghoulWeaponImmunityString.add("STONE_SWORD");
			ghoulWeaponImmunityString.add("STONE_PICKAXE");
			ghoulWeaponImmunityString.add("STONE_SPADE");
			ghoulWeaponImmunityString.add("STONE_AXE");
			ghoulWeaponImmunityString.add("STONE_HOE");
			ghoulWeaponImmunityString.add("IRON_SWORD");
			ghoulWeaponImmunityString.add("IRON_PICKAXE");
			ghoulWeaponImmunityString.add("IRON_SPADE");
			ghoulWeaponImmunityString.add("IRON_AXE");
			ghoulWeaponImmunityString.add("IRON_HOE");
			config.setProperty("Ghoul.Weapon.Immunity", ghoulWeaponImmunityString);
		}
		
		if(ghoulTruceString.size() == 0){
			ghoulTruceString.add("CREEPER");
			ghoulTruceString.add("SKELETON");
			ghoulTruceString.add("ZOMBIE");
			ghoulTruceString.add("PIG_ZOMBIE");
			ghoulTruceString.add("GIANT");
			config.setProperty("Ghoul.Truce.Creatures", ghoulTruceString);
		}
		
		if(wereWolfbaneMaterialsString.size() == 0){
			wereWolfbaneMaterialsString.add("YELLOW_FLOWER");
			wereWolfbaneMaterialsString.add("RED_ROSE");
			wereWolfbaneMaterialsString.add("RED_MUSHROOM");
			wereWolfbaneMaterialsString.add("BROWN_MUSHROOM");
			wereWolfbaneMaterialsString.add("BOWL");
			config.setProperty("Were.Wolfbane.Materials", wereWolfbaneMaterialsString);
		}
		
		if(wereWolfbaneQuantities.size() == 0){
			wereWolfbaneQuantities.add(30);
			wereWolfbaneQuantities.add(10);
			wereWolfbaneQuantities.add(10);
			wereWolfbaneQuantities.add(30);
			wereWolfbaneQuantities.add(1);
			config.setProperty("Were.Wolfbane.Quantities", wereWolfbaneQuantities);
		}

		config.save();
		
		for(String wood : woodMaterialsString){
			woodMaterials.add(Material.getMaterial(wood));
		}
		
		for(String food : foodMaterialsString){
			foodMaterials.add(Material.getMaterial(food));
		}
		
		for(String creature : vampireTruceString){
			CreatureType cType = CreatureType.valueOf(creature);
			if(cType!=null)
				vampireTruce.add(cType);
		}
		
		for(String material : priestMaterialsString){
			priestSpellMaterials.add(Material.getMaterial(material));
		}
		
		for(String material : priestDonationMaterialsString){
			priestDonationMaterials.add(Material.getMaterial(material));
		}
		
		for(String creature : ghoulTruceString){
			CreatureType cType = CreatureType.valueOf(creature);
			if(cType!=null)
				ghoulTruce.add(cType);
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
		
		for(int i=0; i<priestAltarMaterialsString.size();i++){
			Material material = Material.getMaterial(priestAltarMaterialsString.get(i));
			int quantity=1;
			try{
				quantity = priestAltarQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Priest Altar Quantities!");
			}
			priestAltarRecipe.materialQuantities.put(material,quantity);
		}
		
		for(int i=0; i<wereWolfbaneMaterialsString.size();i++){
			Material material = Material.getMaterial(wereWolfbaneMaterialsString.get(i));
			int quantity=1;
			try{
				quantity = wereWolfbaneQuantities.get(i);
			}catch(Exception e){
				e.printStackTrace();
				SupernaturalsPlugin.log("Invalid Wolfbane Quantities!");
			}
			wereWolfbaneRecipe.materialQuantities.put(material,quantity);
		}
		
		priestChurchLocation = new Location(plugin.getServer().getWorld(priestChurchWorld), priestChurchLocationX, priestChurchLocationY, priestChurchLocationZ);
		priestBanishLocation = new Location(plugin.getServer().getWorld(priestBanishWorld), priestBanishLocationX, priestBanishLocationY, priestBanishLocationZ);
		vampireTeleportLocation = new Location(plugin.getServer().getWorld(vampireTeleportWorld), vampireTeleportLocationX, vampireTeleportLocationY, vampireTeleportLocationZ);
	}
	
	public static void saveConfig(){
		config.save();
	}
	
	public static void reloadConfig(){
		if(SNConfigHandler.debugMode){
			SupernaturalsPlugin.log("Reloaded configuration file");
		}
		config.load();
		loadValues(config);
	}
}
