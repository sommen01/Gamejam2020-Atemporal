package com.roguelike.constants;

import java.util.ArrayList;

public enum Warp
{
	OLDGAVE("Old Gave", -1, 0, 5936, 384),
	AECIA("Aecia Village", 0, 10000, 4540, 320);
	
	public String mapName;
	public int mapX;
	public int mapY;
	public int localMapX;
	public int localMapY;
	public static ArrayList<Warp> allWarps;
	
	private Warp(String mapName, int mapX, int mapY, int localMapX, int localMapY)
	{
		this.mapName = mapName;
		this.mapX = mapX;
		this.mapY = mapY;
		this.localMapX = localMapX;
		this.localMapY = localMapY;
	}
	
	static
	{
		allWarps = new ArrayList<Warp>();
		for(Warp w : Warp.values())
		{
			allWarps.add(w);
		}
	}
}