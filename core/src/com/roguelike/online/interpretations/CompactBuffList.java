package com.roguelike.online.interpretations;

import com.roguelike.constants.Buff;
import com.roguelike.entities.Monster;
import com.roguelike.entities.Player;
import com.roguelike.entities.Projectile;

public class CompactBuffList
{
	public int[] buffs = new int[30];
	public float[] timeleft = new float[30];
	public float[] maxtimeleft = new float[30];
	public int[] stacks = new int[30];
	public int[] originEntity = new int[30];
	public String[] originInfo = new String[30];
	
	public Buff[] toBuffList()
	{
		Buff[] buffs = new Buff[30];
		for(int i = 0;i < 30;i++)
		{
			if(this.buffs[i] != -1)
			{
				buffs[i] = new Buff();
				buffs[i].SetInfos(this.buffs[i], null);
				buffs[i].originUid = this.originEntity[i];
				buffs[i].originInfo = this.originInfo[i];
				buffs[i].id = this.buffs[i];
				buffs[i].timeLeft = this.timeleft[i];
				buffs[i].lastTimeLeft = this.maxtimeleft[i];
				buffs[i].stacks = this.stacks[i];
			}
			else
			{
				buffs[i] = null;
			}
		}
		return buffs;
	}
	
	public static CompactBuffList fromPlayer(Player p)
	{
		CompactBuffList cb = new CompactBuffList();
		for(int i = 0;i < 30;i++)
		{
			if(p.buffs[i] != null)
			{
				cb.buffs[i] = p.buffs[i].id;
				cb.timeleft[i] = p.buffs[i].timeLeft;
				cb.maxtimeleft[i] = p.buffs[i].lastTimeLeft;
				cb.stacks[i] = p.buffs[i].stacks;
				cb.originEntity[i] = p.buffs[i].originUid;
				cb.originInfo[i] = p.buffs[i].originInfo;
			}
			else
			{
				cb.buffs[i] = -1;
				cb.timeleft[i] = 0;
				cb.stacks[i] = -1;
				cb.originEntity[i] = -1;
				cb.originInfo[i] = "";
			}
		}
		return cb;
	}
	
	public static CompactBuffList fromMonster(Monster m)
	{
		CompactBuffList cb = new CompactBuffList();
		for(int i = 0;i < 30;i++)
		{
			if(m.buffs[i] != null)
			{
				cb.buffs[i] = m.buffs[i].id;
				cb.timeleft[i] = m.buffs[i].timeLeft;
				cb.maxtimeleft[i] = m.buffs[i].lastTimeLeft;
				cb.stacks[i] = m.buffs[i].stacks;
				cb.originEntity[i] = m.buffs[i].originUid;
				cb.originInfo[i] = m.buffs[i].originInfo;
			}
			else
			{
				cb.buffs[i] = -1;
				cb.timeleft[i] = 0;
				cb.stacks[i] = -1;
				cb.originEntity[i] = -1;
				cb.originInfo[i] = "";
			}
		}
		return cb;
	}
	
	public static CompactBuffList fromProjectile(Projectile m)
	{
		CompactBuffList cb = new CompactBuffList();
		for(int i = 0;i < 30;i++)
		{
			if(m.buffs[i] != null)
			{
				cb.buffs[i] = m.buffs[i].id;
				cb.timeleft[i] = m.buffs[i].timeLeft;
				cb.maxtimeleft[i] = m.buffs[i].lastTimeLeft;
				cb.stacks[i] = m.buffs[i].stacks;
				cb.originEntity[i] = m.buffs[i].originUid;
				cb.originInfo[i] = m.buffs[i].originInfo;
			}
			else
			{
				cb.buffs[i] = -1;
				cb.timeleft[i] = 0;
				cb.stacks[i] = -1;
				cb.originEntity[i] = -1;
				cb.originInfo[i] = "";
			}
		}
		return cb;
	}
}