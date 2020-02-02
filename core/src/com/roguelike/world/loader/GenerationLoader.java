package com.roguelike.world.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.roguelike.world.Generation;

public class GenerationLoader
{
	private static Json json = new Json();

	public static GenerationData loadPlayer ()
	{
		Gdx.files.local("maps/").file().mkdirs();
		FileHandle file = Gdx.files.local("maps/generation.dat");
		if (file.exists())
		{
			GenerationData data = json.fromJson(GenerationData.class, file.readString());
			return data;
		}
		else
		{
			return null;
		}
	}
	
	public static void saveGen()
	{
		GenerationData data = new GenerationData();
		data.lastId = Generation.lastId;
		Gdx.files.local("maps/").file().mkdirs();
		FileHandle file = Gdx.files.local("maps/generation.dat");
		file.writeString(json.prettyPrint(data), false);
	}

}
