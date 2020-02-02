package com.roguelike.constants;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.entities.Monster;
import com.roguelike.entities.Player;
import com.roguelike.game.Content;
import com.roguelike.game.Event;
import com.roguelike.game.Main;

public enum ArkaClass
{
	NONE("Adventurer", new String[] {
			"Adventurer",
			"Adventurer",
			"Adventurer",
			"Adventurer",
			"Adventurer",
			"The Adventurer",},
			new int[] {},
			ArkaClass.ROOTCLASS_KNIGHT, 3, "yourself"),
	KNIGHT("Knight", new String[] {
			"Knight Recruit",
			"Army Leader",
			"Army Destroyer",
			"Arthur's Warrior",
			"Centurion Knight",
			"The Avalon Protector"},
			new int[] {
					26, 8, 58, 12, 21, 13, 22, 25, 24, 59, 60
			},
			ArkaClass.ROOTCLASS_KNIGHT, 4, "Leonidas"),
	ROGUE("Rogue", new String[] {
			"Stealer",
			"Sneaky Dagger",
			"Assassin",
			"Hunted Killer",
			"Invisible Blade",
			"The Deathbringer"},
			new int[] {
					28, 14, 11, 29, 30, 31, 127, 33, 34, 35, 36
			},
			ArkaClass.ROOTCLASS_ROGUE, 5, "Lushmael"),
	RANGER("Ranger", new String[] {
			"Shooter",
			"Sharpshooter",
			"Lethal Bulseye",
			"Death Pointer",
			"One-shot Assassin",
			"The Aether Hunter"},
			new int[] {
					37, 10, 15, 6, 128, 39, 40, 41, 42, 126, 44},
			ArkaClass.ROOTCLASS_RANGER, 4, "Victor"),
	MAGE("Mage", new String[] {
			"Magic Apprentice",
			"Spellcaster",
			"Sorcerer",
			"Master Sorcerer",
			"Warlock",
			"The Archmage"},
			new int[] {45, 47, 116, 46, 49, 51, 53, 54, 55, 93, 94, 114, 115, 117, 118, 119},
			ArkaClass.ROOTCLASS_MAGE, 8, "Lewin"),
	HEMOMANCER("Hemomancer", new String[] {
			"Life Stealer",
			"Vampire",
			"Blood Taker",
			"Blood Controller",
			"Blood Wrath",
			"The Blood Warlock"},
			new int [] {64, 65, 66, 108, 109, 110, 111, 112, 113},
			ArkaClass.ROOTCLASS_MAGE, 6, "Trot"),
	SAMURAI("Samurai", new String[] {
			"Ronin",
			"Exiled Warrior",
			"Redeemed Warrior",
			"Musashi's Inheritor",
			"Lonely Blade",
			"The Forgotten Blade"},
			new int [] {67, 19, 68, 69, 71, 70, 76, 72, 73, 74, 75},
			ArkaClass.ROOTCLASS_KNIGHT, 6, "Musashi"),
	LANCER("Lancer", new String[] {
			"Army Lancer",
			"Deadly Tip",
			"Lifetaker",
			"Dancing Flux",
			"Agile Motion",
			"The Swifting Blade"},
			new int[] {86, 77, 85, 78, 83, 79, 80, 81, 87, 84, 82},
			ArkaClass.ROOTCLASS_KNIGHT, 6, "Louis"),
	CASSINOGAMBLER("Cassino Gambler", new String[] {
			"Cassino Gambler",
			"Cassino Gambler",
			"Cassino Gambler",
			"Cassino Gambler",
			"Cassino Gambler",
			"The Cassino Gambler"},
			new int[] {122, 88, 89, 90, 91, 92},
			ArkaClass.ROOTCLASS_MAGE, 6, "Devole B."),
	ARCANETRICKSTER("Arcane Trickster", new String[] {
			"Magic Apprentice",
			"Enchanted Dagger",
			"Blade Summoner",
			"Arcane Shredder",
			"Arcane Annihilator",
			"The Magic Inquisitor"},
			new int[] {106, 11, 95, 96, 97, 98, 103, 104, 105, 107, 102},
			ArkaClass.ROOTCLASS_ROGUE, 6, "Nelirem"),
	PYROMANCER("Pyromancer", new String[] {
			"Pyromancer",
			"Pyromancer",
			"Pyromancer",
			"Pyromancer",
			"Pyromancer",
			"Pyromancer"},
			new int[] {54, 100, 101},
			ArkaClass.ROOTCLASS_MAGE, 6, "Kil"),
	REAPER("Reaper", new String[] {
			"Scythe",
			"Death Cloak",
			"Death Note",
			"Gravedigger",
			"Soul Reaper",
			"The Disciple of Death"},
			new int[] {121, 120, 11},
			ArkaClass.ROOTCLASS_ROGUE, 6, "Lushmael");
	
	public static final int ROOTCLASS_KNIGHT = 1;
	public static final int ROOTCLASS_ROGUE = 2;
	public static final int ROOTCLASS_MAGE = 3;
	public static final int ROOTCLASS_RANGER = 4;
	
	public String name;
	public String[] titles;
	public int[] skillkit;
	public int rootClass;
	public int skillSlots;
	public String master;
	
	private ArkaClass(String name, String[] titles, int[] skillkit, int rootClass, int skillSlots, String master)
	{
		this.name = name;
		this.titles = titles;
		this.skillkit = skillkit;
		this.rootClass = rootClass;
		this.skillSlots = skillSlots;
		this.master = master;
	}
	
	public static void doLancerAnimation(final Player player, final Texture sheet1, final Texture sheet2, final int frames, final int skipper, final boolean shouldRotate, final boolean blockMove, final boolean blockAttack, final Vector2 centered)
	{
		player.playAnim2(Content.psa[18], 7, 1, false, blockMove, true);
		player.playAnim(Content.psa[19], 7, 1, false, blockMove, true);
		Event e = new Event(14) {
			@Override
			public void function()
			{
				if(sheet1 != null)
					player.playAnim(sheet1, frames, skipper, shouldRotate, blockMove, blockAttack, centered);
				
				if(sheet2 != null)
					player.playAnim2(sheet2, frames, skipper, shouldRotate, blockMove, blockAttack, centered);
			}
		};
		Main.scheduledTasks.add(e);
	}
	
	public static void doReaperAnimation(final Player player, final Texture sheet1, final Texture sheet2, final int frames, final int skipper, final boolean shouldRotate, final boolean blockMove, final boolean blockAttack, final Vector2 centered)
	{
		player.playAnim2(Content.psa[28], 9, 1, false, blockMove, true);
		player.playAnim(Content.psa[29], 9, 1, false, blockMove, true);
		Event e = new Event(18) {
			@Override
			public void function()
			{
				if(sheet1 != null)
					player.playAnim(sheet1, frames, skipper, shouldRotate, blockMove, blockAttack, centered);
				
				if(sheet2 != null)
					player.playAnim2(sheet2, frames, skipper, shouldRotate, blockMove, blockAttack, centered);
			}
		};
		Main.scheduledTasks.add(e);
	}
	
	public static int getMaxCGCards(Player player)
	{
		int cards = 2;
		Skill s = player.getSkill(122);
		if(s != null)
		{
			cards = s.getInfoValueI(player, 0);
		}
		return cards;
	}
	
	public static int getCGFreeCards(Player player)
	{
		return ArkaClass.getMaxCGCards(player)-ArkaClass.getCGUsingCards(player);
	}
	
	public static int getNextCGCard(Player player)
	{
		int slot = -1;
		for(int i = 0;i < ArkaClass.getMaxCGCards(player);i++)
		{
			if(player.isCardAvailable[i] && player.ticksSinceCardUse[i] > 90)
			{
				slot = i;
				break;
			}
		}
		return slot;
	}
	
	public static Vector2 getCGCardPosition(Player player, int slot)
	{
		int sin = (int) ((Constant.gameTick() + (360/ArkaClass.getMaxCGCards(player)) * slot) % 360);
		double sinV = Math.sin(sin * Math.PI/180);
		int cSin = Math.abs(180 - sin);
		float offsetY = ((180 - cSin) * 32)/180;
		Vector2 pos = player.Center().add((float) (sinV * 64 - 12), -32 + offsetY + 16);
		return pos;
	}
	
	public static boolean isCGCardBack(Player player, int slot)
	{
		return (ArkaClass.getCGCardPosition(player, slot).sub(player.Center()).y > 0);
	}
	
	public static int getCGUsingCards(Player player)
	{
		int count = ArkaClass.getMaxCGCards(player);
		int totalCards = count;
		for(int i = 0;i < totalCards;i++)
		{
			if(player.isCardAvailable[i])
				count--;
		}
		return count;
	}
	
	public static void applyArcaneMark(Monster m, Player player)
	{
		m.addBuff(56, 3f, player);
	}
	
	public static boolean hasEoB(Player player)
	{
		return player.haveBuff(63) != -1;
	}
}
