package com.roguelike.constants;

import com.roguelike.entities.NPC;
import com.roguelike.entities.Player;
import com.roguelike.game.Main;
import com.roguelike.game.SaveInfos;

public class Cutscene {
	// new add
	public int id;
	public int ticks;
	
	public Cutscene(int id)
	{
		this.id = id;
		this.ticks = 0;
	}
	
	public void onStart(Player reference)
	{
		if(this.id == 1)
		{
			NPC lushmael = SaveInfos.findNPCWithID(Constant.NPCID_LUSHMAEL, reference);
			if(lushmael != null)
			{
				lushmael.velocity.x = -400;
			}
		}
		else if(this.id == 2)
		{
			NPC lildwa = SaveInfos.findNPCWithID(14, reference);
			if(lildwa != null)
			{
				lildwa.velocity.x = 400;
			}
		}
		else if(this.id == 3)
		{
			NPC ashley = SaveInfos.findNPCWithID(29, reference);
			NPC forge = SaveInfos.findNPCWithID(22, reference);
			if(ashley != null)
			{
				ashley.velocity.x = 400 * Main.directionFromTo(ashley, forge);
				reference.velocity.x = 400 * Main.directionFromTo(reference, forge);
				reference.walking = true;
			}
		}
	}
	
	public void onUpdate(float deltaTime, Player reference)
	{
		if(this.id == 1)
		{
			NPC lushmael = SaveInfos.findNPCWithID(Constant.NPCID_LUSHMAEL);
			if(lushmael != null)
			{
				if(lushmael.Center().x <= 700)
				{
					this.onEnd();
				}
			}
		}
		else if(this.id == 2)
		{
			NPC lildwa = SaveInfos.findNPCWithID(14);
			if(lildwa != null)
			{
				lildwa.direction = 1;
				if(lildwa.position.x >= Main.worldMap.width * 64)
				{
					lildwa.active = false;
					this.onEnd();
				}
			}
		}
		else if(this.id == 3)
		{
			NPC ashley = SaveInfos.findNPCWithID(29, reference);
			NPC forge = SaveInfos.findNPCWithID(22, reference);
			if(ashley != null)
			{
				ashley.direction = Main.directionFromTo(ashley, forge);
				ashley.velocity.x = 400 * Main.directionFromTo(ashley, forge);
				reference.velocity.x = 400 * Main.directionFromTo(reference, forge);
				reference.direction =  Main.directionFromTo(reference, forge);
				reference.walking = true;
				boolean ash = false;
				boolean ref = false;
				if(ashley.Center().dst(forge.Center()) < 100)
				{
					ashley.velocity.x = 0;
					ash = true;
				}
				if(reference.Center().dst(forge.Center()) < 50)
				{
					reference.velocity.x = 0;
					ref = true;
				}
				if(ref && ash)
					this.onEnd();
			}
		}
		this.ticks++;
	}
	
	public void onEnd()
	{
		if(this.id == 1)
		{

			NPC lushmael = SaveInfos.findNPCWithID(Constant.NPCID_LUSHMAEL);
			if(lushmael != null)
			{
				lushmael.velocity.x = 0;
			}
		}
		else if(this.id == 3)
		{
			NPC ashley = SaveInfos.findNPCWithID(29);
			if(ashley != null)
			{
				ashley.showDialog(313);
			}
		}
		Main.cutscene = null;
	}
	
	public static void Start(int id, Player reference)
	{
		if(!Main.displayDialog && Main.cutscene == null)
		{
			Main.cutsceneBordersTicks = 0;
		}
		Main.cutscene = new Cutscene(id);
		Main.cutscene.onStart(reference);
	}
}
