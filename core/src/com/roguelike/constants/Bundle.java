package com.roguelike.constants;

import com.roguelike.game.Main;

public enum Bundle
{
	GENERIC("Generic"),
	SLIME("Slime"), LEAF(Main.lv("Leaf", "Folhas")), BEE(Main.lv("Bee", "Abelha")), STONE(Main.lv("Stone", "Pedra")), 
	BEAR(Main.lv("Bear", "Urso")), PANTHER(Main.lv("Panther", "Pantera")), IRON(Main.lv("Iron", "Ferro")),
	SPIDER(Main.lv("Spider", "Aranha")), SILVER(Main.lv("Silver", "Prata")), GOLDEN(Main.lv("Golden", "Ouro")), 
	REINDEER(Main.lv("Reindeer", "Rena")), ELF(Main.lv("Elf", "Elfo")), FOX(Main.lv("Fox", "Raposa")),
	FAIRY(Main.lv("Fairy", "Fada")), MACAW(Main.lv("Macaw", "Arara"));
	
	public String name;
	
	Bundle(String name)
	{
		this.name = name;
	}
}