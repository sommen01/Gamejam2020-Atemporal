package com.roguelike.world;

import java.time.Instant;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.Constant;
import com.roguelike.entities.Monster;
import com.roguelike.entities.Player;
import com.roguelike.game.Main;

public class Respawn
{
	public int type = 0;
	public int cooldown = 0;
	public long nextProc = 0;
	public Vector2 position = Vector2.Zero.cpy();
	public int myMapX = -1;
	public int myMapY = -1;
	public int uid = -1;
	
	public Respawn(int monsterId, int cooldown, Vector2 position)
	{
		this.type = monsterId;
		this.cooldown = cooldown;
		this.position = position;
		this.uid = -1;
		if(Main.debug)
		{
			//System.out.println("New respawn for monster " + monsterId + " in " + position.toString());
		}
	}
	
	public Respawn() {}
	
	public boolean shouldProc()
	{
		if(Instant.now().getEpochSecond() >= nextProc)
		{
			for(Iterator<Monster> i = Constant.getMonsterList(true).iterator();i.hasNext();)
			{
				Monster m = i.next();
				if(m.uid == this.uid)
				{
					//if(Main.debug)
						//System.out.println("Respawn can't proc because monster is still alive.");
					
					return false;
				}
			}

			float distance = 2400f;
			boolean noPlayer = true;
			for(int i = 0;i < Constant.getPlayerList().length;i++)
			{
				Player pe = Constant.getPlayerList()[i];
				if(pe.active && pe.myMapX == this.myMapX && pe.myMapY == this.myMapY && (pe.Center().dst(new Vector2(this.position.x, this.position.y)) < distance))
				{
					noPlayer = false;
					break;
				}
			}
			if(!noPlayer)
			{
				distance = 1800f;
				for(int i = 0;i < Constant.getPlayerList().length;i++)
				{
					Player pe = Constant.getPlayerList()[i];
					if(pe.active && pe.myMapX == this.myMapX && pe.myMapY == this.myMapY && (pe.Center().dst(new Vector2(this.position.x, this.position.y)) < distance))
					{
						noPlayer = true;
						break;
					}
				}
			}
			if(noPlayer)
			{
				//if(Main.debug)
					//System.out.println("Respawn can't proc because there is no player in the respawn area.");
				
				return false;
			}
			return true;
		}
		else
		{
			//if(Main.debug)
				//System.out.println("Respawn can't proc because it's in cooldown. (" + Instant.now().getEpochSecond() + "/" + this.nextProc + ")");
			return false;
		}
	}
	
	public void Proc()
	{
		Monster.Create(position.cpy(), this.type, this.myMapX, this.myMapY, this.uid);
		this.nextProc = Instant.now().getEpochSecond() + this.cooldown;
	}

	public void setNextProc()
	{
		this.nextProc = Instant.now().getEpochSecond() + this.cooldown;
	}
}