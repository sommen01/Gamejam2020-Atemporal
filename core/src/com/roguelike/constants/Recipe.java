package com.roguelike.constants;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.roguelike.entities.Player;
import com.roguelike.game.Main;

public enum Recipe
{
	SLIMEHELMET(67, new int[] {113, 20}, 1, Bundle.SLIME),
	SLIMEPLATE(68, new int[] {113, 25}, 1, Bundle.SLIME),
	SLIMEWAND(178, new int[] {113, 20}, 1, Bundle.SLIME),
	LEAFMASK(61, new int[] {114, 20}, 1, Bundle.LEAF),
	LEAFCOVER(62, new int[] {114, 25}, 1, Bundle.LEAF),
	BEESWORD(63, new int[] {125, 5, 126, 20}, 1, Bundle.BEE),
	BEEDAGGER(64, new int[] {125, 5, 126, 20}, 1, Bundle.BEE),
	BEESTAFF(65, new int[] {127, 1, 126, 20}, 1, Bundle.BEE),
	STONEHELMET(85, new int[] {83, 20}, 1, Bundle.STONE),
	STONEPLATE(84, new int[] {83, 25}, 1, Bundle.STONE),
	STONEHAMMER(86, new int[] {83, 25}, 1, Bundle.STONE),
	BEARHOOD(128, new int[] {130, 20}, 1, Bundle.BEAR),
	BEARPLATE(129, new int[] {130, 25}, 1, Bundle.BEAR),
	PANTHERHOOD(132, new int[] {135, 12}, 1, Bundle.PANTHER),
	PANTHERPLATE(133, new int[] {135, 15}, 1, Bundle.PANTHER),
	FAIRYORB(179, new int[] {185, 15, 121, 1}, 1, Bundle.FAIRY),
	MACAWMASK(180, new int[] {186, 20, 187, 3}, 1, Bundle.MACAW),
	MACAWCOSTUME(181, new int[] {186, 25}, 1, Bundle.MACAW),
	MAKBOW(182, new int[] {186, 15, 187, 3}, 1, Bundle.MACAW),
	_IRONHELMET(69, new int[] {78, 20}, 1, Bundle.IRON),
	_IRONBREASTPLATE(70, new int[] {78, 25}, 1, Bundle.IRON),
	_IRONBROADSWORD(71, new int[] {78, 25}, 1, Bundle.IRON),
	_IRONSHIELD(73, new int[] {78, 15}, 1, Bundle.IRON),
	_IRONSHORTSWORD(74, new int[] {78, 15}, 1, Bundle.IRON),
	_IRONBOW(72, new int[] {78, 15}, 1, Bundle.IRON),
	_IRONDAGGER(75, new int[] {78, 10}, 1, Bundle.IRON),
	SAPPHIRESTAFF(153, new int[] {106, 5, 78, 10}, 1),
	AMETHYSTSTAFF(154, new int[] {105, 5, 78, 10}, 1),
	EMERALDSTAFF(155, new int[] {107, 5, 78, 10}, 1),
	RUBYSTAFF(156, new int[] {110, 5, 78, 10}, 1),
	VANADITESTAFF(157, new int[] {108, 5, 78, 10}, 1),
	DIAMONDSTAFF(158, new int[] {109, 5, 78, 10}, 1),
	SPIDERHELMET(138, new int[] {140, 20}, 1, Bundle.SPIDER),
	SPIDERPLATE(139, new int[] {140, 25}, 1, Bundle.SPIDER),
	_GHOSTSWORD(177, new int[] {168, 20, 174, 2, 78, 10}, 1),
	_SILVERPLATE(101, new int[] {120, 25}, 1, Bundle.SILVER),
	_SILVERHELMET(102, new int[] {120, 20}, 1, Bundle.SILVER),
	_SILVERBROADSWORD(103, new int[] {120, 20}, 1, Bundle.SILVER),
	_SILVERSHORTSWORD(104, new int[] {120, 15}, 1, Bundle.SILVER),
	_SILVERSHIELD(183, new int[] {120, 20, 188, 5}, 1, Bundle.SILVER),
	RAINBOWSTAFF(159, new int[] {153, 1, 154, 1, 155, 1, 156, 1, 157, 1, 158, 1, 82, 20}, 1),
	_GOLDENHELMET(96, new int[] {82, 20}, 1, Bundle.GOLDEN),
	_GOLDENBREASTPLATE(97, new int[] {82, 25}, 1, Bundle.GOLDEN),
	_GOLDENBROADSWORD(98, new int[] {82, 25}, 1, Bundle.GOLDEN),
	_GOLDENSHIELD(99, new int[] {82, 15}, 1, Bundle.GOLDEN),
	_GOLDENSHORTSWORD(100, new int[] {82, 15}, 1, Bundle.GOLDEN),
	ANTLERS(122, new int[] {142, 5, 123, 2}, 1, Bundle.REINDEER),
	REINDEERCOST(141, new int[] {142, 25}, 1, Bundle.REINDEER),
	ELFHAT(146, new int[] {144, 20}, 1, Bundle.ELF),
	ELFSUIT(145, new int[] {144, 25}, 1, Bundle.ELF),
	FOXHAT(149, new int[] {150, 20}, 1, Bundle.FOX),
	FOXCOST(148, new int[] {150, 25}, 1, Bundle.FOX),
	_GHOSTAURA(175, new int[] {121, 1, 168, 15, 174, 1}, 1),
	_MAGICESSENCE1(174, new int[] {105, 1}, 2, Bundle.GENERIC),
	_MAGICESSENCE2(174, new int[] {106, 1}, 2, Bundle.GENERIC),
	_MAGICESSENCE3(174, new int[] {107, 1}, 2, Bundle.GENERIC),
	_MAGICESSENCE4(174, new int[] {108, 1}, 2, Bundle.GENERIC),
	_MAGICESSENCE5(174, new int[] {109, 1}, 2, Bundle.GENERIC),
	_MAGICESSENCE6(174, new int[] {110, 1}, 2, Bundle.GENERIC),
	_MAGICESSENCE7(174, new int[] {111, 1}, 2, Bundle.GENERIC),
	_MAGICESSENCE8(174, new int[] {112, 1}, 2, Bundle.GENERIC);
	
	public int itemid;
	public int[] materials;
	public Bundle bundleid;
	public int craftStation;
	public boolean unlockable;
	
	Recipe(int itemid, int[] materials, int craftStation, Bundle bundleid)
	{

		if(materials.length%2!=0)
		{
			System.out.println("[!!!RECIPE!!!] Invalid materials for " + this.name());
			return;
		}
		this.itemid = itemid;
		this.materials = materials;
		this.craftStation = craftStation;
		this.bundleid = bundleid;
		boolean unlockable = true;
		if(this.name().charAt(0) == '_')
			unlockable = false;

		this.unlockable = unlockable;
	}
	
	Recipe(int itemid, int[] materials, int craftStation)
	{
		this(itemid, materials, craftStation, null);
	}
	
	public static Recipe getRecipeForItem(int id)
	{
		Recipe r = null;
		for(Recipe r2 : Recipe.values())
		{
			if(r2.itemid == id)
			{
				r = r2;
				break;
			}
		}
		return r;
	}
	
	public ArrayList<Integer> getMaterials()
	{
		if(this.materials.length%2!=0 || this.materials.length<=0)
		{
			System.out.println("[!!!RECIPE!!!] Bad materials for " + this.name());
			return null;
		}
		ArrayList<Integer> ret = new ArrayList<Integer>();
		for(int i = 0;i < materials.length;i+=2)
		{
			ret.add(this.materials[i]);
		}
		return ret;
	}
	
	public static Recipe getUnlockableItem(Player player, int tier)
	{
		ArrayList<Recipe> list = new ArrayList<Recipe>();
		for(Recipe r : Recipe.values())
		{
			Item item = new Item().SetInfos(r.itemid);
			if(r.unlockable && r.bundleid != Bundle.GENERIC && !player.learnedRecipes.contains(r) && item.quality <= tier)
			{
				list.add(r);
			}
		}
		if(list.size() > 0)
			return list.get(MathUtils.random(list.size()-1));
		else
			return null;
	}
	
	public int getMaterialCount(int itemid)
	{
		if(this.materials.length%2!=0)
		{
			System.out.println("[!!!RECIPE!!!] Bad recipe for " + this.name());
			return -1;
		}
		for(int i = 0;i < materials.length;i+=2)
		{
			if(this.materials[i] == itemid)
			{
				return this.materials[i+1];
			}
		}
		return 0;
	}
}
