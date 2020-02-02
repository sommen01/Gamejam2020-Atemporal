package com.roguelike.constants;

import com.roguelike.entities.Monster;
import com.roguelike.entities.Player;
import com.roguelike.game.Main;

public class Objective
{
	public int id;
	public String text;
	public int[] infos;
	public boolean concluded;
	
	public Objective(int id, int[] infos)
	{
		this.id = id;
		this.infos = infos;
		this.concluded = false;
	}
	
	public Objective(int id)
	{
		this.id = id;
		this.infos = new int[1];
		this.concluded = false;
	}
	
	public Objective()
	{ }
	
	public void update(float delta)
	{
		if(this.id == 1)
		{
			/*
			 * infos[0]: ID monster to kill
			 * infos[1]: How many to kill
			 * infos[2]: How much were killed*/
			Monster m = new Monster();
			m.id = this.infos[0];
			m.Reset();
			this.text = Main.lv("Kill ", "Mate ") + Math.min(this.infos[2], this.infos[1]) + "/" + this.infos[1] + " " + m.name;
			if(this.infos[2] >= this.infos[1])
			{
				this.concluded = true;
			}
			else
			{
				this.concluded = false;
			}
		}
		else if(this.id == 2)
		{
			/*
			 * infos[0]: ID item to collect
			 * infos[1]: How many to collect
			 * infos[2]: How much were collected*/
			Item i = new Item().SetInfos(this.infos[0]);
			String name = i.name;
			Player p = Constant.getPlayerList()[Main.me];
			int quant = p.getItemQuantity(this.infos[0]);
			this.infos[2] = quant;
			if(this.infos[1] != 1)
			{
				name = Main.pluralOf(i.name);
			}
			this.text = Main.lv("Collect ", "Consiga ") + Math.min(this.infos[2], this.infos[1]) + "/" + this.infos[1] + " " + name;
			if(this.infos[2] >= this.infos[1])
			{
				this.concluded = true;
			}
			else
			{
				this.concluded = false;
			}
		}
		else if(this.id == 3)
		{
			/*
			 * infos[0]: Nothing*/
			this.text = "Find the creature that is killing the Farmer sheeps. (At the Forests)";
		}
		else if(this.id == 4)
		{
			this.text = "Keep going to the right, you are free now!";
		}
		else if(this.id == 5)
		{
			this.text = "Listen to Bragus and the class masters.";
		}
		else if(this.id == 6)
		{
			this.text = "Go to the Farm at the right and act naturally as you pass by the Farmer.";
		}
		else if(this.id == 7)
		{
			if(Main.worldMap.id == 21)
				this.concluded = true;
			else
				this.concluded = false;
			
			this.text = "Go to the top of the Volcano at the right.";
		}
		else if(this.id == 8)
		{
			this.text = "Ask the Farmer for informations about the goblin.";
		}
		else if(this.id == 9)
		{
			this.text = "Go to the basement of the Farmer's house and take the statue from the goblin.";
		}
		else if(this.id == 10)
		{
			this.text = "Climb down the right side of the volcano.";
		}
		else if(this.id == 11)
		{
			this.text = "Receive the gift from Lildwa's father.";
		}
	}
}