package com.roguelike.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.entities.Entity;

public enum DJ
{
	_CAVE("data/sounds/music/cave.mp3", "data/sounds/music/cave.mp3", 90f, 90f),
	_PFIELDS("data/sounds/music/pfields2.mp3", "data/sounds/music/pfields2.mp3", 96.3f, 96.3f),
	LIFEISADREAM("data/sounds/music/pfields.mp3", "data/sounds/music/pfields.mp3", 141f, 141f),
	_FOREST("data/sounds/music/forest.mp3", "data/sounds/music/forest.mp3", 128f, 128f),
	HOPE2("data/sounds/music/hope2.mp3", "data/sounds/music/hope2.mp3", 138.1f, 138.1f),
	_DAWNSILENCE("data/sounds/music/dawnsilence.mp3", "data/sounds/music/dawnsilence.mp3", 90f, 90f),
	CAMPFIRE("data/sounds/fx/campfire.mp3", 4.8f),
	PHYSICALHIT("data/sounds/fx/physhit.mp3", 0.25f),
	ICEHIT("data/sounds/fx/icehit.mp3", 0.62f),
	SHADOWHIT("data/sounds/fx/shadowhit.mp3", 0.72f),
	DEATHHIT("data/sounds/fx/deathhit.mp3", 1.3f),
	BLOODHIT("data/sounds/fx/bloodhit.mp3", 1.3f),
	DEATH("data/sounds/fx/death.mp3", 1.5f),
	ENERGYHIT("data/sounds/fx/energyhit.mp3", 0.62f),
	FIREHIT("data/sounds/fx/firehit.mp3", 0.35f),
	SLASH("data/sounds/fx/slash.mp3", 0.35f),
	HEAVYSLASH("data/sounds/fx/heavyslash.mp3", 0.35f);
	
	public static DJ[] music = new DJ[2];
	public static boolean[] playing = new boolean[] {false, false};
	public static long[] handleId = new long[] {-1, -1};
	public static float[] volumes = new float[] {0f, 0f};
	public static ArrayList<WorldSound> worldSounds = new ArrayList<WorldSound>();
	
	public String audioDir;
	public String loopDir;
	public Sound sound;
	public Sound soundLoop;
	public boolean loopable;
	public static int cListen = 1;
	public float loopTime = 0f;
	public float loopLoopTime = 0f;
	private long startedPlaying;
	private boolean looped = false;
	public boolean loaded = false;
	public static boolean forcingMusic = false;
	public static boolean oldForcingMusic = false;
	
	DJ(String audioDir, String loopDir, float loopTime, float loopLoopTime)
	{
		this.audioDir = audioDir;
		this.loopDir = loopDir;
		this.loopTime = loopTime;
		this.loopLoopTime = loopLoopTime;
		this.loopable = true;
	}
	
	DJ(String audioDir, float loopTime)
	{
		this.audioDir = audioDir;
		this.loopTime = loopTime;
		this.loopDir = null;
		this.loopable = false;
	}
	
	public static void loadFiles()
	{
		for(DJ d : DJ.values())
		{
			if(d.name().charAt(0) != '_')
			{
				loadSound(d);
			}
		}
	}
	
	public static void loadSound(DJ d)
	{
		d.sound = Gdx.audio.newSound(Gdx.files.internal(d.audioDir));
		if(d.loopable)
		{
			d.soundLoop = Gdx.audio.newSound(Gdx.files.internal(d.loopDir));
		}
		d.loaded = true;
	}
	
	public static void disposeFiles()
	{
		for(DJ d : DJ.values())
		{
			if(!d.loaded)
				continue;
			
			d.sound.dispose();
			if(d.soundLoop != null)
			{
				d.soundLoop.dispose();
			}
		}
	}
	
	public static void switchMusic(DJ djAudio, boolean forcing)
	{
		if(!djAudio.loaded)
		{
			loadSound(djAudio);
		}
		if(forcing)
		{
			forcingMusic = true;
			if(music[cListen] != null && music[cListen].sound == djAudio.sound && volumes[cListen] > 0)
			{
				playing[cListen] = true;
				return;
			}
			if(music[1-cListen] != null && music[1-cListen].sound == djAudio.sound && volumes[1-cListen] > 0)
			{
				cListen = 1-cListen;
				playing[cListen] = true;
				System.out.println("Resuming music: " + djAudio.name());
				return;
			}
		}
		
		if(music[cListen] == null || djAudio.sound != music[cListen].sound)
		{
			cListen = 1-cListen;
			if(music[cListen] != null && volumes[cListen] <= 0)
			{
				music[cListen].stop();
			}
			handleId[cListen] = DJ.playSound(djAudio.sound, 0f, 1f);
			music[cListen] = djAudio;
			volumes[cListen] = 0f;
			playing[cListen] = true;
			music[cListen].startedPlaying = System.currentTimeMillis();
			music[cListen].looped = false;
			System.out.println("Playing music: " + djAudio.name());
		}
	}
	
	public static void switchMusic(DJ djAudio)
	{
		switchMusic(djAudio, false);
	}
	
	public static void update(float delta)
	{
		if(oldForcingMusic && !forcingMusic)
		{
			cListen = 1-cListen;
		}
		if(playing[1-cListen])
		{
			if(volumes[1-cListen] > 0f)
			{
				DJ.pushToVolume(1-cListen, 0f, 0.005f * Settings.musicVolume);
			}

			if(music[1-cListen].loopable && music[1-cListen].getPlayTime() > music[1-cListen].getLoopTime())
			{
				handleId[1-cListen] = music[1-cListen].soundLoop.play(volumes[1-cListen], 1f, 0f);
				music[1-cListen].startedPlaying = System.currentTimeMillis();
				music[1-cListen].looped = true;
			}
		}
		
		if(playing[cListen])
		{
			if(Main.playing)
			{
				if(Main.blackScreenProgress() < 1f)
				{
					DJ.pushToVolume(cListen, 0.2f * Settings.musicVolume, 0.016f * Settings.musicVolume);
				}
				else if(Main.dialog != null && Main.currentStory != null)
				{
					DJ.pushToVolume(cListen, 0.1f * Settings.musicVolume, 0.016f * Settings.musicVolume);
				}
				else if(Main.pause)
				{
					DJ.pushToVolume(cListen, 0.2f * Settings.musicVolume, 0.005f * Settings.musicVolume);
				}
				else
				{
					DJ.pushToVolume(cListen, Settings.musicVolume, 0.005f * Settings.musicVolume);
				}
			}
			else if(Roguelike.loadingTextures)
			{
				DJ.pushToVolume(cListen, 0f, 1f);
			}
			else
			{
				DJ.pushToVolume(cListen, Settings.musicVolume, 0.005f * Settings.musicVolume);
			}

			if(music[cListen].loopable && music[cListen].getPlayTime() > music[cListen].getLoopTime())
			{
				handleId[cListen] = music[cListen].soundLoop.play(volumes[cListen], 1f, 0f);
				music[cListen].startedPlaying = System.currentTimeMillis();
				music[cListen].looped = true;
			}
		}

		if(!forcingMusic && volumes[1-cListen] <= 0f && playing[1-cListen])
		{
			music[1-cListen].stop();
			playing[1-cListen] = false;
		}

		if(Main.worldMap != null)
		{
			for(Iterator<WorldSound> it = worldSounds.iterator();it.hasNext();)
			{
				WorldSound w = it.next();
				if(w.myMapX != Main.worldMap.globalX || w.myMapY != Main.worldMap.globalY || w.getPlayTime() > w.loopTime || (w.hadParent && (w.parent == null || !w.parent.active)))
				{
					if(w.myMapX != Main.worldMap.globalX || w.myMapY != Main.worldMap.globalY || (w.hadParent && (w.parent == null || !w.parent.active)))
						w.sound.stop(w.handle);

					it.remove();
				}
				else
				{
					w.sound.setPan(w.handle, DJ.getPanByPosition(w.getPosition()), DJ.getVolumeMultByDistance(w.getPosition()));
				}
			}
		}
		else
		{
			for(Iterator<WorldSound> it = worldSounds.iterator();it.hasNext();)
			{
				WorldSound w = it.next();
				w.sound.stop(w.handle);
				it.remove();
			}
		}
	}

	public static long playSound(Sound sound, float volume, float pitch)
	{
		return playSound(sound, volume, pitch, null);
	}
	
	public static long playSound(Sound sound, float volume, float pitch, Vector2 position)
	{
		float vol2 = volume * DJ.getVolumeMultByDistance(position) * Settings.fxVolume;
		return sound.play(vol2, pitch, DJ.getPanByPosition(position));
	}
	
	public static long playSound(DJ sound, float volume, float pitch)
	{
		return playSound(sound.sound, volume, pitch, null);
	}
	
	public static long playSound(DJ sound, float volume, float pitch, Vector2 position)
	{
		return playSound(sound.sound, volume, pitch, position);
	}
	
	public static WorldSound addWorldSound(Entity e, DJ audio)
	{
		for(WorldSound w : worldSounds)
		{
			if(w.parent == e && w.sound == audio.sound)
			{
				return null;
			}
		}
		WorldSound w = addWorldSound(e.myMapX, e.myMapY, e.Center(), audio);
		if(w != null)
		{
			w.parent = e;
			w.hadParent = true;
		}
		
		return w;
	}
	
	public static WorldSound addWorldSound(int mapX, int mapY, Vector2 position, DJ audio)
	{
		if(mapX != Main.worldMap.globalX || mapY != Main.worldMap.globalY)
			return null;
		
		WorldSound w = new WorldSound(audio);
		w.soundPosition = position;
		w.myMapX = mapX;
		w.myMapY = mapY;
		float vol = 1f;
		if(position != null)
			vol = getVolumeMultByDistance(position);
		
		long handle = audio.sound.play(Settings.fxVolume * vol);
		w.handle = handle;
		w.startedPlaying = System.currentTimeMillis();
		worldSounds.add(w);
		return w;
	}
	
	public static float getPanByPosition(Vector2 position)
	{
		float pan = 0f;
		if(position != null)
		{
			float maxDist = Gdx.graphics.getWidth()*1.5f;
			pan = Main.clamp(-1f, (position.x-Main.camera.position.x)/maxDist, 1f);
		}
		return pan;
	}
	
	public static float getVolumeMultByDistance(Vector2 position)
	{
		if(position != null)
		{
			float dist = new Vector2(Main.camera.position.x, Main.camera.position.y).dst(position);
			dist = Math.max(dist-300f, 0f);
			float len = new Vector2(Gdx.graphics.getWidth()*2, Gdx.graphics.getHeight()*2).len();
			float vol = 1f-(dist/len);
			return Main.clamp(0f, vol, 1f);
		}
		else
		{
			return 1f;
		}
	}
	
	public static void setVolume(int index, float volume)
	{
		if(index == 0 || index == 1)
		{
			int id = index;
			volumes[id] = volume;
			if(!music[id].looped)
				music[id].sound.setVolume(handleId[id], volume);
			else
				music[id].soundLoop.setVolume(handleId[id], volume);
		}
	}
	
	public static void pushToVolume(int index, float volume, float pushStep)
	{
		if(volumes[index] < volume)
		{
			DJ.setVolume(index, Main.clamp(0f, volumes[index]+pushStep, volume));
		}
		else if(volumes[index] > volume)
		{
			DJ.setVolume(index, Main.clamp(volume, volumes[index]-pushStep, 1f));
		}
	}
	
	public float getLoopTime()
	{
		if(!this.looped)
			return this.loopTime;
		else
			return this.loopLoopTime;
	}
	
	public void stop()
	{
		if(!this.looped)
			this.sound.stop();
		else
			this.soundLoop.stop();
	}
	
	public double getPlayTime()
	{
		return (System.currentTimeMillis()-this.startedPlaying)/1000.0;
	}
}
