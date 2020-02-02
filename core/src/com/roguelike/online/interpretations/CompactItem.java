package com.roguelike.online.interpretations;

import com.roguelike.constants.Item;
import com.roguelike.entities.Entity;

public class CompactItem extends Entity
{
	public int id;
	public int level;
	public boolean inWorld;
	
	public static CompactItem fromItem(Item i)
	{
		CompactItem ci = new CompactItem();
		ci.position = i.position;
		ci.velocity = i.velocity;
		ci.width = i.width;
		ci.height = i.height;
		ci.name = i.name;
		ci.myMapX = i.myMapX;
		ci.myMapY = i.myMapY;
		ci.id = i.id;
		ci.level = i.level;
		ci.inWorld = i.inWorld;
		return ci;
	}
	
	public Item toItem()
	{
		Item i = new Item();
		i.position = this.position;
		i.velocity = this.velocity;
		i.width = this.width;
		i.height = this.height;
		i.name = this.name;
		i.myMapX = this.myMapX;
		i.myMapY = this.myMapY;
		i.id = this.id;
		i.level = this.level;
		i.inWorld = this.inWorld;
		i.reforge(i.level);
		return i;
	}
	
	@Override
	public void update(float delta) {}
}
