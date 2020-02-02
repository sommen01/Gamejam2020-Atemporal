package com.roguelike.online.interpretations;

public class DamageRequest
{
	public int damage;
	public int direction;
	public float scalar;
	public boolean critical;
	public int attackerid;
	public int damageType;
	public int damageClass;
	public int monsteruid;
	
	public DamageRequest(int damage, int direction, float scalar, boolean critical, int attackerid, int damageType,
			int damageClass, int monsteruid)
	{
		this.damage = damage;
		this.direction = direction;
		this.scalar = scalar;
		this.critical = critical;
		this.attackerid = attackerid;
		this.damageType = damageType;
		this.damageClass = damageClass;
		this.monsteruid = monsteruid;
	}
	
	public DamageRequest() {}
}