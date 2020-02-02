package com.roguelike.online.interpretations;

import com.roguelike.world.GameMap;

public class MapInfos
{
	public GameMap map;
	public int globalX;
	public int globalY;
	
	public MapInfos(GameMap map, int globalX, int globalY)
	{
		this.map = map;
		this.globalX = globalX;
		this.globalY = globalY;
	}
	
	public MapInfos() {}
}