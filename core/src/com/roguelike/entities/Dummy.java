package com.roguelike.entities;

public class Dummy extends Entity {

	@Override
	public void update(float delta) {}

	public Dummy copy(Entity e)
	{
		this.position = e.position.cpy();
		this.velocity = e.velocity.cpy();
		this.width = e.width;
		this.height = e.height;
		this.rotation = e.rotation;
		this.myMapX = e.myMapX;
		this.myMapY = e.myMapY;
		return this;
	}
}
