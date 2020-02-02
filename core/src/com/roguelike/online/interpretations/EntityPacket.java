package com.roguelike.online.interpretations;

import com.roguelike.entities.Entity;

public class EntityPacket
{
	public String message;
	public Entity entity;
	
	public EntityPacket() {}
	
	public EntityPacket(Entity e, String pac)
	{
		this.entity = e;
		this.message = pac;
	}
}
