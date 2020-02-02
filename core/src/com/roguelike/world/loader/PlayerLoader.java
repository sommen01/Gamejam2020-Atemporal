package com.roguelike.world.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.ArkaClass;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Item;
import com.roguelike.constants.Skill;
import com.roguelike.entities.Player;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;

public class PlayerLoader
{
	public static Player loadPlayer(String name)
	{
		String directory = "saves/" + name + "/";
		
		Gdx.files.local(directory).file().mkdirs();
		FileHandle file = Gdx.files.local(directory+"player.plr");
		if (file.exists())
		{
			System.out.println("Player "+name+" found, loading...");
			Player data = Main.json.fromJson(Player.class, file.readString());
			data.name = name;
			data.initialize();
			return data;
		}
		else
		{
			System.out.println("Player "+name+" not found, creating..");
			Player gen = new Player();
			gen.name = name;
			generateNewPlayerInfos(gen);
			gen.initialize();
			savePlayer(gen);
			return gen;
		}
	}
	
	public static void generateNewPlayerInfos(Player p)
	{
		p.position = new Vector2(300, 300);
		p.classType = ArkaClass.NONE;
		if(p.classType.rootClass == ArkaClass.ROOTCLASS_KNIGHT)
		{
			p.inventory[0] = new Item().SetInfos(76);
			p.inventory[1] = new Item().SetInfos(76);
			p.inventory[2] = new Item().SetInfos(77);
			p.inventory[3] = new Item().SetInfos(31);
			p.inventory[4] = new Item().SetInfos(163);
			p.inventory[5] = new Item().SetInfos(151);
			p.inventory[6] = new Item().SetInfos(164);
			p.inventory[7] = new Item().SetInfos(165);
			p.inventory[8] = new Item().SetInfos(166);
		}
		else if(p.classType.rootClass == ArkaClass.ROOTCLASS_MAGE)
		{
			p.inventory[Constant.ITEMSLOT_LEFT] = new Item().SetInfos(160);
			p.inventory[Constant.ITEMSLOT_BODY] = new Item().SetInfos(161);
			p.inventory[0] = new Item().SetInfos(163);
			p.inventory[1] = new Item().SetInfos(151);
			p.inventory[2] = new Item().SetInfos(164);
			p.inventory[3] = new Item().SetInfos(165);
			p.inventory[4] = new Item().SetInfos(166);
		}
		else if(p.classType.rootClass == ArkaClass.ROOTCLASS_ROGUE)
		{
			p.inventory[0] = new Item().SetInfos(15);
			p.inventory[1] = new Item().SetInfos(163);
			p.inventory[2] = new Item().SetInfos(151);
			p.inventory[3] = new Item().SetInfos(164);
			p.inventory[4] = new Item().SetInfos(165);
			p.inventory[5] = new Item().SetInfos(166);
		}
		else if(p.classType.rootClass == ArkaClass.ROOTCLASS_RANGER)
		{
			p.inventory[0] = new Item().SetInfos(3);
			p.inventory[1] = new Item().SetInfos(163);
			p.inventory[2] = new Item().SetInfos(151);
			p.inventory[3] = new Item().SetInfos(164);
			p.inventory[4] = new Item().SetInfos(165);
			p.inventory[5] = new Item().SetInfos(166);
		}
		p.changeClass(p.classType);
		p.health = 100;
	}
	
	public static void savePlayer (Player player)
	{
		String directory = "saves/" + player.name + "/";
		
		Gdx.files.local(directory).file().mkdirs();
		FileHandle file = Gdx.files.local(directory + "player.plr");
		file.writeString(Main.json.prettyPrint(player), false);
	}
}
