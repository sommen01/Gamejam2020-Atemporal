package com.roguelike.world.loader;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.world.Background;
import com.roguelike.world.GameMap;
import com.roguelike.world.Respawn;
import com.roguelike.world.Tile;
import com.roguelike.constants.Biome;
import com.roguelike.constants.Item;
import com.roguelike.entities.Monster;
import com.roguelike.entities.NPC;
import com.roguelike.entities.Prop;
import com.roguelike.entities.Treasure;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;

public class GameMapLoader
{

	public static GameMap loadMap (int id, String mname, int[] specialGen, int globalX, int globalY, int comeGX, int comeGY, int comeLX, int comeLY)
	{
		String useName = Main.saveName;

		Gdx.files.local("maps/base/").file().mkdirs();
		FileHandle basefile = Gdx.files.local("maps/base/" + mname + ".map");
		if(basefile.exists())
		{
			GameMap data = Main.json.fromJson(GameMap.class, basefile.readString());
			for(Iterator<NPC> it = data.npcs.iterator();it.hasNext();)
			{
				NPC n = it.next();
				if(n.temporary)
					it.remove();
			}
			Vector2 xy = stringToXY(mname);
			if(xy != null)
			{
				data.globalX = (int) xy.x;
				data.globalY = (int) xy.y;
			}
			Main.newGen = true;
			return data;
		}
		return null;
	}

	private static Vector2 stringToXY(String str)
	{
		String[] broken = str.split("_");
		if(broken.length != 2)
			return null;

		return new Vector2(Integer.parseInt(broken[0]), Integer.parseInt(broken[1]));
	}

	private static int getNewMap(int x, int y, int origin)
	{
		ArrayList<Integer> maps = Biome.getPossibleMaps(x, y);
		int id = maps.get(MathUtils.random(maps.size()-1));
		if(id == 1)
			id = origin;

		return id;
	}

	public static GameMap generateRandomMap(int id, int[] specialFlag, int myX, int myY, int comeGX, int comeGY, int comeLX, int comeLY)
	{
		GameMap gen = GameMap.GenerateBaseMap(id, specialFlag, myX, myY, comeGX, comeGY, comeLX, comeLY, false);
		return gen;
	}

}
