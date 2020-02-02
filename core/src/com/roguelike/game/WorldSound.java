package com.roguelike.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.entities.Entity;

public class WorldSound
{
	public String audioDir;
	public String loopDir;
	public Sound sound;
	public Sound soundLoop;
	public boolean loopable;
	public float loopTime = 0f;
	public float loopLoopTime = 0f;
	public long startedPlaying;
	public boolean looped = false;
	public Vector2 soundPosition = null;
	public Entity parent = null;
	public boolean hadParent = false;
	public long handle;
	public int myMapX;
	public int myMapY;
	
	public WorldSound(DJ d)
	{
		this.audioDir = d.audioDir;
		this.loopDir = d.loopDir;
		this.loopTime = d.loopTime;
		this.loopLoopTime = d.loopLoopTime;
		this.loopable = d.loopable;
		this.sound = d.sound;
		this.soundLoop = d.soundLoop;
	}
	
	public Vector2 getPosition()
	{
		if(hadParent && parent != null)
			return this.parent.Center();
		else
			return this.soundPosition;
	}

	public double getPlayTime()
	{
		return (System.currentTimeMillis()-this.startedPlaying)/1000.0;
	}
}
