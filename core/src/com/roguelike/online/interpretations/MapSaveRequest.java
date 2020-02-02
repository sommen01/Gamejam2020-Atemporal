package com.roguelike.online.interpretations;

import com.roguelike.world.GameMap;

public class MapSaveRequest
{
	public int[] expected = new int[4];
	public int[] infos = new int[10];
	public GameMap map;
	
	public int globalX;
	public int globalY;
	
	public MapSaveRequest(GameMap map, int mx, int my)
	{
		this.map = map;
		this.globalX = mx;
		this.globalY = my;
	}
	
	public MapSaveRequest() {}
}