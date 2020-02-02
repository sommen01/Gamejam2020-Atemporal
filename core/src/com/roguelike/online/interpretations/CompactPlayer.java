package com.roguelike.online.interpretations;

import com.roguelike.constants.Constant;
import com.roguelike.constants.Item;
import com.roguelike.entities.Entity;
import com.roguelike.entities.Player;

public class CompactPlayer extends Entity
{
	public int[] inventory = new int[Constant.ITEMSLOT_MAX+1];
	public boolean checkingInventory;
	public int level;
	public int experience;
	public int whoAmI;
	public CompactBuffList buffs;

	@Override
	public void update(float delta)
	{}

	public static CompactPlayer fromPlayer(Player p2)
	{
		Player p = new Player();
		try
		{
			p = (Player) p2.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		CompactPlayer cp = new CompactPlayer();
		cp.active = p.active;
		cp.width = p.width;
		cp.height = p.height;
		cp.name = p.name;
		cp.position = p.position;
		cp.rotation = p.rotation;
		cp.velocity = p.velocity;
		cp.whoAmI = p.whoAmI;
		cp.myMapX = p.myMapX;
		cp.myMapY = p.myMapY;
		for(int i = 0;i < cp.inventory.length;i++)
		{
			if(p.inventory[i] == null)
				cp.inventory[i] = 0;
			else
			{
				cp.inventory[i] = p.inventory[i].id;
			}
		}
		cp.buffs = CompactBuffList.fromPlayer(p);
		cp.checkingInventory = p.checkingInventory;
		cp.level = p.level;
		cp.experience = p.experience;
		return cp;
	}

	public void toPlayer(Player p)
	{
		p.active = true;
		p.width = this.width;
		p.height = this.height;
		p.name = this.name;
		if(p.position.dst(this.position) > 30)
			p.position = this.position;
		
		p.rotation = this.rotation;
		p.velocity = this.velocity;
		for(int i = 0;i < this.inventory.length;i++)
		{
			if(this.inventory[i] <= 0)
				p.inventory[i] = null;
			else
			{
				Item item = new Item();
				item.SetInfos(this.inventory[i]);
				p.inventory[i] = new Item().SetInfos(this.inventory[i]);
			}
		}
		p.buffs = this.buffs.toBuffList();
		p.checkingInventory = this.checkingInventory;
		p.myMapX = this.myMapX;
		p.myMapY = this.myMapY;
		p.level = this.level;
		p.whoAmI = this.whoAmI;
		p.experience = this.experience;
	}
}