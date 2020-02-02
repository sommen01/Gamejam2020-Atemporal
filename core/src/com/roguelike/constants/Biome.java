package com.roguelike.constants;

import java.util.ArrayList;

import com.roguelike.game.Main;

public enum Biome
{
	PLAINS("Plains", 0, 6, 0, 1, new int[] {2}, false),
	CAVES("Caves", 5, 9, 0, 5, new int[] {140}, true),
	CAVES2("Caves", 0, 4, 0, 5, new int[] {140}, true),
	FOREST("Forest", 4, 9, 0, 10, new int[] {130}, false),
	FORESTCAVES("Forest Caves", 10, 13, 0, 20, new int[] {150}, true),
	HARDPLAINS("Hard Plains", 10, 16, 0, 1, new int[] {160}, false),
	HARDCAVES("Hard Caves", 14, 18, 0, 20, new int[] {180}, true),
	HARDFOREST("Hard Forest", 14, 20, 0, 25, new int[] {170}, false),
	HARDFORESTCAVES("Hard Forest Caves", 17, 20, 0, 20, new int[] {150}, true),
	SNOWUPLANDS("Snow Uplands", 10, 13, 1000, 35, new int[] {101}, false),
	SNOWPLAINS("Snow Plains", 10, 13, 1000, 35, new int[] {102}, false);
	
	public String name;
	public int[] maps;
	public int minGlobalX;
	public int maxGlobalX;
	public int mapLayer;
	public int recommendedLevel;
	public boolean underground;
	
	private Biome(String name, int minGlobalX, int maxGlobalX, int mapLayer, int recommendedLevel, int[] groundMaps, boolean underground)
	{
		this.name = name;
		this.maps = groundMaps;
		this.minGlobalX = minGlobalX;
		this.maxGlobalX = maxGlobalX;
		this.mapLayer = mapLayer;
		this.recommendedLevel = recommendedLevel;
		this.underground = underground;
	}
	
	public static float getRelativeDifficulty(int mapId, int myX)
	{
		boolean right = false;
		for(Biome b : Biome.values())
		{
			for(int i : b.maps)
			{
				if(i == mapId)
				{
					right = true;
					break;
				}
			}
			if(right)
			{
				float val = (float)((float)Math.abs(myX)-(float)b.minGlobalX)/((float)b.maxGlobalX-(float)b.minGlobalX);
				return Main.clamp(0, val, 1);
			}
		}
		return 0f;
	}
	
	public static ArrayList<Integer> getPossibleMaps(int x, int y)
	{
		ArrayList<Integer> ret = new ArrayList<Integer>();

		for(Biome b : Biome.values())
		{
			if(Math.abs(x) >= b.minGlobalX && Math.abs(x) <= b.maxGlobalX)
			{
				if(Math.abs(y - b.mapLayer) < 20)
				{
					int actualLayer = y - b.mapLayer;
					if(actualLayer < 0)
					{
						if(b.underground)
						{
							for(Integer i : b.maps)
							{
								ret.add(i);
							}
						}
					}
					else if(actualLayer < 20)
					{
						if(!b.underground)
						{
							for(Integer i : b.maps)
							{
								ret.add(i);
							}
						}
					}
				}
			}
		}
		if(ret.size() < 1)
		{
			ret.add(1);
			System.out.println("Not found any valid map for x " + x);
		}
		return ret;
	}
}