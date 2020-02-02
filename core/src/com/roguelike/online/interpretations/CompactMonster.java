package com.roguelike.online.interpretations;

import com.roguelike.entities.Entity;
import com.roguelike.entities.Monster;

public class CompactMonster extends Entity
{
	public int type;
	public int health;
	public int maxHealth;
	public int actualFrame;
	public int[] ai;
	public CompactBuffList buffs;
	public int target;
	public int lastHitter;
	public int uid;
	public int lastHitTick;
	
	public static CompactMonster fromMonster(Monster m)
	{
		CompactMonster cm = new CompactMonster();
		cm.type = m.id;
		cm.position = m.position;
		cm.width = m.width;
		cm.height = m.height;
		cm.velocity = m.velocity;
		cm.name = m.name;
		cm.myMapX = m.myMapX;
		cm.myMapY = m.myMapY;
		cm.scale = m.scale;
		cm.health = m.health;
		cm.maxHealth = m.maxHealth;
		cm.actualFrame = m.actualFrame;
		cm.ai = m.ai;
		cm.buffs = CompactBuffList.fromMonster(m);
		cm.target = m.target;
		cm.lastHitter = m.lastHitter;
		cm.uid = m.uid;
		cm.lastHitTick = m.lastHitTick;
		return cm;
	}
	
	public Monster toMonster()
	{
		Monster m = new Monster();
		m.id = this.type;
		m.Reset(false);
		m.position = this.position;
		m.width = this.width;
		m.height = this.height;
		m.velocity = this.velocity;
		m.name = this.name;
		m.myMapX = this.myMapX;
		m.myMapY = this.myMapY;
		m.scale = this.scale;
		m.health = this.health;
		m.maxHealth = this.maxHealth;
		m.actualFrame = this.actualFrame;
		m.ai = this.ai;
		m.buffs = buffs.toBuffList();
		m.target = this.target;
		m.lastHitter = this.lastHitter;
		m.uid = this.uid;
		m.lastHitTick = this.lastHitTick;
		m.active = true;
		return m;
	}
	
	public CompactMonster() {}

	@Override
	public void update(float delta) {}
}