package com.roguelike.online.interpretations;

import com.roguelike.constants.Constant;
import com.roguelike.entities.Entity;
import com.roguelike.entities.Monster;
import com.roguelike.entities.Player;
import com.roguelike.entities.Projectile;

public class CompactProjectile extends Entity
{
	public int type;
	public float timeleft;
	public int damage;
	public CompactBuffList buffs;
	public int owner;
	public String ownerInfos;
	
	public static CompactProjectile fromProjectile(Projectile p)
	{
		CompactProjectile cp = new CompactProjectile();
		cp.active = p.active;
		cp.position = p.position;
		cp.rotation = p.rotation;
		cp.velocity = p.velocity;
		cp.scale = p.scale;
		cp.type = p.type;
		cp.timeleft = p.timeLeft;
		cp.damage = p.damage;
		cp.buffs = CompactBuffList.fromProjectile(p);
		cp.myMapX = p.myMapX;
		cp.myMapY = p.myMapY;
		if(p.owner != null)
		{
			if(p.owner.getClass() == Monster.class)
			{
				Monster m = (Monster)p.owner;
				cp.owner = m.uid;
				cp.ownerInfos = "monster";
			}
			else if(p.owner.getClass() == Player.class)
			{
				Player pl = (Player)p.owner;
				cp.owner = pl.whoAmI;
				cp.ownerInfos = "player";
			}
		}
		return cp;
	}
	
	public Projectile toProjectile()
	{
		Projectile p = new Projectile();
		p.type = this.type;
		p.buffs = this.buffs.toBuffList();
		if(this.ownerInfos.equalsIgnoreCase("monster"))
		{
			for(Monster m : Constant.getMonsterList(false))
			{
				if(m.active && m.uid == this.owner)
				{
					p.owner = m;
					break;
				}
			}
		}
		else if(this.ownerInfos.equalsIgnoreCase("player"))
		{
			p.owner = Constant.getPlayerList()[this.owner];
		}
		else
		{
			p.owner = null;
		}
		p.SetDefaults();
		p.active = this.active;
		p.position = this.position;
		p.rotation = this.rotation;
		p.velocity = this.velocity;
		p.scale = this.scale;
		p.timeLeft = this.timeleft;
		p.damage = this.damage;
		p.myMapX = this.myMapX;
		p.myMapY = this.myMapY;
		for(int i = 0;i < 10;i++)
		{
			p.ai[i] = 0;
		}
		p.resetCollisions();
		return p;
	}
	
	public CompactProjectile() {}

	@Override
	public void update(float delta) {}
}
