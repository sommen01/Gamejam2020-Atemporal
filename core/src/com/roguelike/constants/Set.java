package com.roguelike.constants;

import com.roguelike.entities.Player;
import com.roguelike.game.Bonus;

public enum Set
{
	PUNISHMENT("Punishment Set", new int[]{50, 51, 53}, 
			new Bonus[] {new Bonus(2, "[EPIC]Punishment:[] Weapon attacks causes 8% of enemy health as bonus holy damage (4% for ranged and magic weapons) with 0.5 seconds cooldown."),
					new Bonus(3, "+30% attack speed"),
					new Bonus(4, "The [EPIC]Punishment[] passive has no cooldown.")}),
	VAMPIRICLORD("Vampire Lord Set", new int[]{28, 29, 30},
			new Bonus[] {new Bonus(3, "+5% life steal"),
					new Bonus(4, "Any life steal you receive is shared between nearby friends.")}),
	LEAF("Bush Disguise", new int[] {61, 62},
			new Bonus[] {new Bonus(2, "The skill [EPIC]Invisibility[] has it's cooldown reduced by 30%.")}),
	STONE("Stone Set", new int[]{84, 85},
			new Bonus[] {new Bonus(2, "The wearer is immune to knockback and burning effects.")
			});
	
	public String name;
	public int[] items;
	public Bonus[] bonus;
	
	private Set(String name, int[] items, Bonus[] bonus)
	{
		this.name = name;
		this.items = items;
		this.bonus = bonus;
	}
	
	public static int playerItemSetCount(Player player, Set set)
	{
		int count = 0;
		for(int i = Constant.ITEMSLOT_OFFSET;i <= Constant.ITEMSLOT_MAX;i++)
		{
			if(player.inventory[i] != null && isInArray(set.items, player.inventory[i].id))
			{
				count++;
			}
		}
		return count;
	}
	
	public static boolean isInArray(int[] array, int item)
	{
		for(int z : array)
		{
			if(item == z)
			{
				return true;
			}
		}
		return false;
	}
}