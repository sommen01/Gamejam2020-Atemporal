package com.roguelike.game;

public class Event
{
	public int ticksLeft = 1;
	
	public void function() {}
	
	public Event(int ticks)
	{
		this.ticksLeft = ticks;
	}
}
