package com.roguelike.online.interpretations;

public class MapRequest
{
	public int globalX;
	public int globalY;
	public int cGX;
	public int cGY;
	public int cLX;
	public int cLY;
	public int expected;
	
	public MapRequest(int globalX, int globalY, int cGX, int cGY, int cLX, int cLY, int expected)
	{
		this.globalX = globalX;
		this.globalY = globalY;
		this.cGX = cGX;
		this.cGY = cGY;
		this.cLX = cLX;
		this.cLY = cLY;
		this.expected = expected;
	}
	
	public MapRequest() {}
}