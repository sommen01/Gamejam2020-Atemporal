package com.roguelike.entities;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.Buff;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Item;
import com.roguelike.constants.Skill;
import com.roguelike.game.Content;
import com.roguelike.game.DJ;
import com.roguelike.game.Event;
import com.roguelike.game.SaveInfos;
import com.roguelike.online.AHS;
import com.roguelike.world.GameMap;
import com.roguelike.game.Main;
import com.roguelike.constants.ArkaClass;

public class Projectile extends Entity
{
	public int type;
	public boolean collidable;
	public int whoAmI = 0;
	public int frames = 1;
	public int currentFrame = 0;
	public int currentFrameTicks = 0;
	public int ticks = 0;
	public Vector2[] lastPosition = new Vector2[10];
	public boolean rotateByDirection = false;
	public boolean rotateByVelocity = false;
	public double rotationOffset = 0;
	public boolean flipped = false;
	public boolean mirrored = false;
	public boolean useAI = true;
	public boolean drawBack = false;
	public float timeLeft = 0f;
	public boolean ranged = false;
	public int extraUpdates = 1;
	public boolean hostile = false;
	public int collisionWaitFrames = 2;
	public boolean lights = false;
	public int lightSize = 256;
	public Color lightColor = Color.WHITE;
	public float lightStrength = 1;
	public int projectileclass = Constant.PROJECTILETYPE_ANY;
	public Buff[] buffs = new Buff[30];
	public boolean ricochet = false;
	public int damage;
	public float collisionY;
	public Entity owner;
	public boolean recent;
	public float alpha;
	public boolean regOnHit = true;
	public ArrayList<Integer> alreadyCollided = new ArrayList<Integer>();
	public Entity target = null;
	public boolean targeting = false;
	public boolean destroyOnTargetLoss = false;
	public float[] ai = new float[10];
	public transient Texture customTexture;
	public transient Lighting queueLight;
	public transient DJ summonAudio;
	public transient DJ destroyAudio;
	public transient float summonPitch = 1f;
	public transient float destroyPitch = 1f;

	public void SetDefaults()
	{
		this.SetDefaults(true);
	}
	
	public void SetDefaults(boolean fullReset)
	{
		if(fullReset)
			this.resetStats();
		
		if(this.type == 1)
		{
			this.collidable = true;
			this.width = 20;
			this.height = 64;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.rotationOffset = 90;
			this.projectileclass = Constant.PROJECTILETYPE_ARROW;
		}
		else if(this.type == 2)
		{
			this.collidable = true;
			this.width = 8;
			this.height = 8;
			this.ranged = true;
			this.extraUpdates = 10;
		}
		else if(this.type == 3)
		{
			this.collidable = false;
			this.width = 60;
			this.height = 60;
			this.ranged = true;
			this.extraUpdates = 3;
			//this.rotationOffset = 315;
			//this.rotateByVelocity = true;
		}
		else if(this.type == 4)
		{
			this.collidable = true;
			this.width = 32;
			this.height = 32;
			this.ranged = true;
			this.rotateByDirection = true;
			this.hostile = true;
		}
		else if(this.type == 5)
		{
			this.collidable = true;
			this.width = 180;
			this.height = 46;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.lights = true;
			this.lightStrength = 5;
			//this.lightColor = new Color(1f, 1f, 0.5f, 1f);
		}
		else if(this.type == 6)
		{
			this.collidable = true;
			this.width = 16;
			this.height = 16;
			this.ranged = true;
			this.rotateByDirection = true;
			this.collisionWaitFrames = 9;
			this.lights = true;
			//this.lightColor = new Color(1f, 1f, 0.5f, 1f);
		}
		else if(this.type == 7)
		{
			this.collidable = false;
			this.width = 64;
			this.height = 64;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.frames = 2;
		}
		else if(this.type == 8)
		{
			this.collidable = true;
			this.width = 8;
			this.height = 8;
			this.ranged = true;
			this.extraUpdates = 50;
		}
		else if(this.type == 9)
		{
			this.collidable = true;
			this.width = 16;
			this.height = 16;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.projectileclass = Constant.PROJECTILETYPE_ARROW;
		}
		else if(this.type == 10)
		{
			this.collidable = true;
			this.width = 48;
			this.height = 48;
			this.ranged = true;
			this.rotateByDirection = true;
		}
		else if(this.type == 11)
		{
			this.collidable = true;
			this.width = 48;
			this.height = 48;
			this.frames = 3;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.collisionWaitFrames = 15;
			this.projectileclass = Constant.PROJECTILETYPE_ARROW;
		}
		else if(this.type == 12)
		{
			this.collidable = false;
			this.width = 16;
			this.height = 64;
			this.ranged = false;
		}
		else if(this.type == 13)
		{
			this.collidable = true;
			this.width = 16;
			this.height = 16;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.projectileclass = Constant.PROJECTILETYPE_BULLET;
			this.extraUpdates = 5;
		}
		else if(this.type == 14)
		{
			this.collidable = true;
			this.width = 61;
			this.height = 10;
			this.ranged = true;
			this.rotateByVelocity = true;
		}
		else if(this.type == 15)
		{
			this.collidable = false;
			this.width = 136*2;
			this.height = 256*2;
			this.ranged = false;
			this.frames = 21;
		}
		else if(this.type == 16)
		{
			this.collidable = true;
			this.width = 36;
			this.height = 64;
			this.rotateByDirection = true;
			this.ranged = true;
		}
		else if(this.type == 17)
		{
			this.collidable = false;
			this.width = 197;
			this.height = 206;
			this.rotateByDirection = true;
			this.ranged = false;
		}
		else if(this.type == 18)
		{
			this.collidable = false;
			this.width = 284;
			this.height = 284;
			this.rotateByDirection = true;
			this.ranged = false;
		}
		else if(this.type == 19)
		{
			this.collidable = true;
			this.width = 16;
			this.height = 16;
			this.rotateByDirection = true;
			this.ranged = true;
			this.hostile = true;
		}
		else if(this.type == 20)
		{
			this.collidable = true;
			this.width = 10;
			this.height = 8;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.hostile = true;
			this.rotationOffset = -45f;
		}
		else if(this.type == 21)
		{
			this.collidable = true;
			this.width = 48;
			this.height = 52;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.hostile = true;
		}
		else if(this.type == 22)
		{
			this.collidable = true;
			this.width = 22;
			this.height = 24;
			this.rotateByDirection = true;
			this.ranged = true;
			this.hostile = true;
			this.regOnHit = false;
		}
		else if(this.type == 23)
		{
			this.collidable = false;
			this.width = 4;
			this.height = 4;
			this.ranged = false;
			this.extraUpdates = 200;
			this.regOnHit = false;
		}
		else if(this.type == 24)
		{
			this.collidable = true;
			this.width = 36;
			this.height = 36;
			this.ranged = true;
			this.extraUpdates = 12;
			this.ricochet = true;
			this.regOnHit = false;
		}
		else if(this.type == 25)
		{
			this.collidable = false;
			this.width = 256;
			this.height = 256;
			this.frames = 4;
			this.ranged = false;
			this.regOnHit = false;
		}
		else if(this.type == 26)
		{
			this.collidable = false;
			this.ranged = false;
			this.width = 80;
			this.height = 50;
			this.regOnHit = false;
		}
		else if(this.type == 27)
		{
			this.collidable = true;
			this.ranged = true;
			this.width = 14;
			this.height = 40;
			this.regOnHit = false;
		}
		else if(this.type == 28)
		{
			this.collidable = false;
			this.ranged = true;
			this.width = 80;
			this.height = 200;
			this.regOnHit = false;
			this.setLight(512, new Color(1f, 0.1f, 0.1f, 1f), 10);
			this.extraUpdates = 5;
		}
		else if(this.type == 29)
		{
			this.collidable = false;
			this.width = 160;
			this.height = 480;
			this.ranged = false;
			this.frames = 5;
			this.regOnHit = false;
		}
		else if(this.type == 30)
		{
			this.collidable = false;
			this.width = 100;
			this.height = 200;
			this.ranged = false;
			this.frames = 5;
			this.regOnHit = false;
			this.rotateByVelocity = true;
		}
		else if(this.type == 31)
		{
			this.collidable = false;
			this.width = 64;
			this.height = 36;
			this.ranged = false;
			this.regOnHit = false;
		}
		else if(this.type == 32)
		{
			this.collidable = false;
			this.width = 128;
			this.height = 128;
			this.ranged = false;
			this.regOnHit = false;
		}
		else if(this.type == 33)
		{
			this.collidable = false;
			this.width = 56;
			this.height = 60;
			this.ranged = false;
			this.regOnHit = false;
		}
		else if(this.type == 34)
		{
			this.collidable = false;
			this.width = 36;
			this.height = 60;
			this.ranged = false;
			this.regOnHit = false;
		}
		else if(this.type == 35)
		{
			this.collidable = false;
			this.width = 128;
			this.height = 128;
			this.ranged = false;
			this.regOnHit = false;
		}
		else if(this.type == 36)
		{
			this.collidable = true;
			this.width = 79;
			this.height = 22;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.extraUpdates = 2;
		}
		else if(this.type == 37)
		{
			this.collidable = true;
			this.width = 4;
			this.height = 4;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.extraUpdates = 10;
			this.projectileclass = Constant.PROJECTILETYPE_BULLET;
		}
		else if(this.type == 38)
		{
			this.collidable = true;
			this.frames = 4;
			this.width = 36;
			this.height = 76;
			this.rotateByVelocity = true;
			this.rotationOffset = 90;
			this.ranged = true;
			this.projectileclass = Constant.PROJECTILETYPE_ARROW;
		}
		else if(this.type == 39)
		{
			this.collidable = true;
			this.width = 60;
			this.height = 16;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.collisionWaitFrames = 0;
		}
		else if(this.type == 40)
		{
			this.collidable = true;
			this.width = 60;
			this.height = 16;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.collisionWaitFrames = 0;
		}
		else if(this.type == 41)
		{
			this.collidable = false;
			this.width = 32;
			this.height = 32;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.extraUpdates = 400;
			this.projectileclass = Constant.PROJECTILETYPE_ARROW;
		}
		else if(this.type == 42)
		{
			this.collidable = false;
			this.ranged = false;
			this.width = 128;
			this.height = 128;
			this.frames = 22;
			this.regOnHit = false;
		}
		else if(this.type == 43)
		{
			this.collidable = false;
			this.width = 134;
			this.height = 412;
			this.ranged = false;
			this.frames = 6;
			this.regOnHit = false;
		}
		else if(this.type == 44)
		{
			this.collidable = false;
			this.width = 56;
			this.height = 44;
			this.rotateByVelocity = true;
			this.ranged = true;
		}
		else if(this.type == 45)
		{
			this.collidable = false;
			this.width = 4;
			this.height = 4;
			this.ranged = false;
			this.regOnHit = false;
			this.summonAudio = DJ.SHADOWHIT;
			this.summonPitch = 0.5f;
			this.destroyAudio = DJ.SHADOWHIT;
			this.destroyPitch = 1.5f;
		}
		else if(this.type == 46)
		{
			this.collidable = true;
			this.width = 80;
			this.height = 32;
			this.frames = 5;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.setLight(256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE], 20);
			this.summonAudio = DJ.FIREHIT;
			this.summonPitch = 0.5f;
			this.destroyAudio = DJ.FIREHIT;
			this.destroyPitch = 0.8f;
		}
		else if(this.type == 47)
		{
			this.collidable = true;
			this.frames = 2;
			this.width = 12;
			this.height = 9;
			this.ranged = false;
		}
		else if(this.type == 48)
		{
			this.collidable = true;
			this.width = 100;
			this.height = 72;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.frames = 2;
		}
		else if(this.type == 49)
		{
			this.collidable = true;
			this.width = 10;
			this.height = 8;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.rotationOffset = -45f;
		}
		else if(this.type == 50)
		{
			this.collidable = false;
			this.width = 1;
			this.height = 1;
			this.ranged = false;
		}
		else if(this.type == 51)
		{
			this.collidable = true;
			this.width = 64;
			this.height = 20;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.projectileclass = Constant.PROJECTILETYPE_ARROW;
		}
		else if(this.type == 52)
		{
			this.collidable = false;
			this.width = 400;
			this.height = 400;
			this.ranged = false;
			this.drawBack = true;
		}
		else if(this.type == 53)
		{
			this.collidable = false;
			this.width = 64;
			this.height = 64;
			this.ranged = true;
			this.rotateByDirection = true;
			this.frames = 3;
		}
		else if(this.type == 54)
		{
			this.collidable = false;
			this.width = 160;
			this.height = 40;
			this.ranged = false;
		}
		else if(this.type == 55)
		{
			this.collidable = true;
			this.frames = 2;
			this.width = 8;
			this.height = 8;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.projectileclass = Constant.PROJECTILETYPE_BULLET;
			this.extraUpdates = 5;
		}
		else if(this.type == 56)
		{
			this.collidable = false;
			this.width = 72;
			this.height = 72;
			this.rotationOffset = 45;
			this.ranged = true;
			this.extraUpdates = 5;
			this.rotateByDirection = true;
		}
		else if(this.type == 57)
		{
			this.regOnHit = false;
			this.collidable = false;
			this.width = 220;
			this.height = 220;
			this.ranged = false;
			this.drawBack = true;
		}
		else if(this.type == 58)
		{
			this.collidable = true;
			this.frames = 11;
			this.width = 112;
			this.height = 34;
			this.rotateByVelocity = true;
			this.ranged = true;
		}
		else if(this.type == 59)
		{
			this.collidable = false;
			this.ranged = false;
			this.width = 128;
			this.height = 128;
			this.frames = 22;
			this.regOnHit = false;
		}
		else if(this.type == 60)
		{
			this.collidable = false;
			this.width = 16;
			this.height = 16;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.extraUpdates = 3;
		}
		else if(this.type == 61)
		{
			this.collidable = false;
			this.width = 12;
			this.height = 12;
			this.ranged = true;
			this.extraUpdates = 3;
		}
		else if(this.type == 62)
		{
			this.collidable = false;
			this.frames = 6;
			this.width = 256;
			this.height = 256;
			this.ranged = false;
			this.drawBack = true;
		}
		else if(this.type == 63)
		{
			this.regOnHit = false;
			this.collidable = false;
			this.width = 600;
			this.height = 600;
			this.ranged = false;
			this.drawBack = true;
		}
		else if(this.type == 64)
		{
			this.regOnHit = false;
			this.collidable = false;
			this.width = 128;
			this.height = 128;
			this.ranged = false;
			this.drawBack = true;
		}
		else if(this.type == 65)
		{
			this.regOnHit = false;
			this.collidable = false;
			this.width = 200;
			this.height = 200;
			this.ranged = false;
			this.drawBack = false;
		}
		else if(this.type == 67)
		{
			this.regOnHit = false;
			this.collidable = false;
			this.width = 180;
			this.height = 42;
			this.frames = 5;
			this.ranged = false;
			this.drawBack = false;
		}
		else if(this.type == 68)
		{
			this.collidable = false;
			this.width = 92;
			this.height = 92;
			this.ranged = true;
			this.frames = 2;
		}
		else if(this.type == 69)
		{
			this.width = 3072;
			this.height = 928;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.setLight(4096, new Color(0f, 0.69f, 1f, 1f), 0f);
		}
		else if(this.type == 70)
		{
			this.collidable = true;
			this.width = 124;
			this.height = 34;
			this.ranged = true;
			this.rotateByVelocity = true;
		}
		else if(this.type == 71)
		{
			this.collidable = false;
			this.width = 149;
			this.height = 41;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.extraUpdates = 10;
			this.setLight(256, new Color(0f, 0.69f, 1f, 1f), 50f);
		}
		else if(this.type == 72)
		{
			this.collidable = true;
			this.width = 149;
			this.height = 41;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.extraUpdates = 4;
			this.setLight(256, new Color(0f, 0.69f, 1f, 1f), 50f);
		}
		else if(this.type == 73)
		{
			this.collidable = true;
			this.width = 149;
			this.height = 41;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.extraUpdates = 2;
			this.setLight(256, new Color(0f, 0.69f, 1f, 1f), 50f);
		}
		else if(this.type == 74)
		{
			this.collidable = false;
			this.width = 149;
			this.height = 122;
			this.ranged = true;
			this.frames = 9;
			this.drawBack = true;
			this.setLight(512, new Color(0f, 0.69f, 1f, 1f), 50f);
		}
		else if(this.type == 75)
		{
			this.collidable = true;
			this.width = 149;
			this.height = 41;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.setLight(256, new Color(0f, 0.69f, 1f, 1f), 50f);
		}
		else if(this.type == 76)
		{
			this.collidable = false;
			this.width = 149;
			this.height = 41;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.setLight(256, new Color(0f, 0.69f, 1f, 1f), 50f);
		}
		else if(this.type == 77)
		{
			this.width = 860;
			this.height = 860;
			this.regOnHit = false;
			this.drawBack = true;
			this.setLight(2048, new Color(0f, 0.69f, 1f, 1f), 0f);
		}
		else if(this.type == 78)
		{
			this.width = 24;
			this.height = 32;
			this.frames = 10;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.rotationOffset = 90;
		}
		else if(this.type == 79)
		{
			this.width = 24;
			this.height = 32;
			this.frames = 10;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.rotationOffset = 90;
			this.setLight(256, new Color(1f, 0.8f, 0.2f, 1f), 10f);
		}
		else if(this.type == 80)
		{
			//this.rotateByVelocity = true;
			this.width = 384;
			this.height = 512;
			this.frames = 1;
			this.rotationOffset = 90;
		}
		else if(this.type == 81)
		{
			//this.rotateByVelocity = true;
			this.width = 384;
			this.height = 512;
			this.frames = 6;
			this.rotationOffset = 90;
			this.setLight(512, new Color(1f, 0.8f, 0.2f, 1f), 30f);
		}
		else if(this.type == 82)
		{
			this.width = 32;
			this.height = 24;
			this.frames = 48;
		}
		else if(this.type == 83)
		{
			this.rotateByDirection = true;
			this.width = 24;
			this.height = 32;
			this.frames = 6;
			this.rotationOffset = 90;
			this.collidable = true;
			this.setLight(128, new Color(1f, 0.8f, 0.2f, 1f), 10f);
		}
		else if(this.type == 84)
		{
			this.width = 24;
			this.height = 32;
			this.frames = 10;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.rotationOffset = 90;
			this.setLight(128, new Color(1f, 0.8f, 0.2f, 1f), 10f);
		}
		else if(this.type == 85)
		{
			this.width = 24;
			this.height = 32;
			this.frames = 10;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.rotationOffset = 90;
		}
		else if(this.type == 86)
		{
			this.width = 24;
			this.height = 32;
			this.frames = 10;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.rotationOffset = 90;
		}
		else if(this.type == 87)
		{
			this.width = 132;
			this.height = 120;
			this.frames = 2;
			this.collidable = true;
		}
		else if(this.type == 88)
		{
			this.width = 132;
			this.height = 120;
			this.frames = 10;
			this.collidable = true;
		}
		else if(this.type == 89)
		{
			this.width = 16;
			this.height = 16;
			this.collidable = false;
			this.ranged = true;
			this.setLight(256, new Color(0.1f, 1f, 1f, 1f), 50f);
		}
		else if(this.type == 90)
		{
			this.width = 56;
			this.height = 60;
			this.frames = 6;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.summonAudio = DJ.FIREHIT;
			this.summonPitch = 0.5f;
			this.destroyAudio = DJ.FIREHIT;
			this.destroyPitch = 0.8f;
		}
		else if(this.type == 91)
		{
			this.width = 40;
			this.height = 60;
			this.collidable = false;
			this.drawBack = true;
			this.frames = 1;
		}
		else if(this.type == 92)
		{
			this.width = 60;
			this.height = 22;
			this.frames = 4;
			this.rotateByVelocity = true;
			this.drawBack = true;
			this.extraUpdates = 4;
			this.setLight(256, new Color(0.1f, 0.6f, 1f, 1f), 20f);
		}
		else if(this.type == 93)
		{
			this.width = 224;
			this.height = 192;
			this.frames = 18;
			this.collidable = false;
			this.setLight(512, new Color(0.1f, 0.6f, 1f, 1f), 50f);
		}
		else if(this.type == 94)
		{
			this.width = 60;
			this.height = 60;
			this.frames = 15;
			this.collidable = false;
			this.setLight(512, new Color(0.9f, 0.6f, 0.1f, 1f), 20f);
		}
		else if(this.type == 95)
		{
			this.collidable = true;
			this.width = 80;
			this.height = 24;
			this.collisionWaitFrames = 60;
			this.frames = 5;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.setLight(256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE], 20);
		}
		else if(this.type == 96)
		{
			this.collidable = false;
			this.width = 64;
			this.height = 64;
			this.ranged = false;
		}
		else if(this.type == 97)
		{
			this.collidable = false;
			this.width = 60;
			this.height = 56;
			this.ranged = false;
			this.drawBack = true;
			this.setLight(1024, new Color(0.1f, 0.6f, 0.7f, 1f), 20f);
		}
		else if(this.type == 98)
		{
			this.collidable = true;
			this.width = 80;
			this.height = 32;
			this.frames = 5;
			this.rotateByVelocity = true;
			this.extraUpdates = 2;
			this.ranged = true;
			this.setLight(256, new Color(1f, 0.1f, 0.1f, 1f), 20);
		}
		else if(this.type == 99)
		{
			this.collidable = false;
			this.width = 232;
			this.height = 232;
			this.frames = 63;
			this.drawBack = true;
			this.setLight(512, new Color(1f, 0.1f, 0.1f, 1f), 20f);
		}
		else if(this.type == 100)
		{
			this.width = 16;
			this.height = 16;
			this.ranged = true;
			this.collidable = true;
			this.setLight(256, new Color(0.25f, 0.25f, 1f, 1f), 30f);
			this.extraUpdates = 3;
		}
		else if(this.type == 101)
		{
			this.width = 16;
			this.height = 16;
			this.ranged = true;
			this.collidable = true;
			this.setLight(256, new Color(0.5f, 0.15f, 0.5f, 1f), 30f);
			this.extraUpdates = 3;
		}
		else if(this.type == 102)
		{
			this.width = 16;
			this.height = 16;
			this.ranged = true;
			this.collidable = true;
			this.setLight(256, new Color(0.25f, 1f, 0.25f, 1f), 30f);
			this.extraUpdates = 3;
		}
		else if(this.type == 103)
		{
			this.width = 28;
			this.height = 24;
			this.ranged = true;
			this.collidable = true;
			this.setLight(256, new Color(0.8f, 0.15f, 0.15f, 1f), 30f);
			this.extraUpdates = 3;
		}
		else if(this.type == 104)
		{
			this.width = 32;
			this.height = 16;
			this.ranged = true;
			this.collidable = true;
			this.setLight(256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE], 30f);
			this.extraUpdates = 3;
			this.rotateByVelocity = true;
			this.frames = 3;
		}
		else if(this.type == 105)
		{
			this.width = 32;
			this.height = 16;
			this.ranged = true;
			this.collidable = true;
			this.setLight(256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ICE], 30f);
			this.extraUpdates = 3;
			this.rotateByDirection = true;
		}
		else if(this.type == 106)
		{
			this.width = 16;
			this.height = 16;
			this.ranged = true;
			this.collidable = true;
			this.setLight(256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE], 30f);
			this.rotateByVelocity = true;
		}
		else if(this.type == 107)
		{
			this.width = 70;
			this.height = 74;
			this.collidable = false;
			this.drawBack = true;
			this.setLight(256, new Color(0.5f, 0f, 0.5f, 1f), 40f);
		}
		else if(this.type == 108)
		{
			this.collidable = false;
			this.ranged = false;
			this.width = 128;
			this.height = 128;
			this.frames = 22;
			this.regOnHit = false;
			this.setLight(256, new Color(0f, 0.4f, 0.9f, 1f), 40);
		}
		else if(this.type == 109)
		{
			this.collidable = true;
			this.ranged = true;
			this.width = 32;
			this.height = 32;
			this.extraUpdates = 100;
			this.rotateByVelocity = true;
			this.summonAudio = DJ.ENERGYHIT;
			this.summonPitch = 0.5f;
			this.destroyAudio = DJ.FIREHIT;
			this.destroyPitch = 0.8f;
		}
		else if(this.type == 110)
		{
			this.collidable = false;
			this.ranged = true;
			this.width = 32;
			this.height = 32;
			this.extraUpdates = 100;
			this.rotateByVelocity = true;
		}
		else if(this.type == 111)
		{
			this.collidable = true;
			this.ranged = true;
			this.width = 32;
			this.height = 32;
			this.extraUpdates = 100;
			this.rotateByVelocity = true;
		}
		else if(this.type == 112)
		{
			this.collidable = false;
			this.width = 4;
			this.height = 4;
			this.ranged = false;
			this.extraUpdates = 200;
			this.regOnHit = false;
		}
		else if(this.type == 114)
		{
			this.collidable = false;
			this.width = 512;
			this.height = 512;
			this.ranged = false;
			this.frames = 10;
			this.drawBack = true;
			this.setLight(512, Color.WHITE, 50);
		}
		else if(this.type == 115)
		{
			this.collidable = false;
			this.frames = 5;
			this.width = 256;
			this.height = 256;
			this.ranged = false;
		}
		else if(this.type == 116)
		{
			this.collidable = true;
			this.width = 20;
			this.height = 64;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.rotationOffset = 90;
			this.projectileclass = Constant.PROJECTILETYPE_ARROW;
			this.setLight(256, new Color(0.92f, 0.97f, 0.82f, 1f), 16f);
			this.collisionWaitFrames = 999;
		}
		else if(this.type == 117)
		{
			this.collidable = false;
			this.frames = 1;
			this.width = 100;
			this.height = 100;
			this.rotateByDirection = true;
			this.ranged = false;
		}
		else if(this.type == 118)
		{
			this.collidable = false;
			this.frames = 22;
			this.width = 800;
			this.height = 256;
			this.setLight(768, new Color(0.92f, 0.98f, 0.8f, 1f), 50f);
		}
		else if(this.type == 119)
		{
			this.collidable = true;
			this.width = 36;
			this.height = 20;
			this.frames = 3;
			this.rotateByVelocity = true;
			this.ranged = true;
		}
		else if(this.type == 120)
		{
			this.collidable = true;
			this.width = 20;
			this.height = 64;
			this.ranged = true;
			this.rotateByVelocity = true;
			this.rotationOffset = 90;
			this.projectileclass = Constant.PROJECTILETYPE_ARROW;
		}
		else if(this.type == 121)
		{
			this.collidable = true;
			this.width = 36;
			this.height = 20;
			this.frames = 3;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.hostile = true;
		}
		else if(this.type == 122)
		{
			this.collidable = true;
			this.width = 36;
			this.height = 20;
			this.frames = 3;
			this.rotateByVelocity = true;
			this.ranged = true;
		}
		else if(this.type == 123)
		{
			this.collidable = false;
			this.width = 128;
			this.height = 128;
			this.setLight(256, new Color(0.92f, 0.96f, 0.6f, 1f), 50);
		}
		else if(this.type == 124)
		{
			this.collidable = true;
			this.width = 80;
			this.height = 30;
			this.frames = 3;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.projectileclass = Constant.PROJECTILETYPE_BULLET;
			this.extraUpdates = 5;
			this.setLight(256, new Color(0.92f, 0.96f, 0.6f, 1f), 50);
		}
		else if(this.type == 125)
		{
			this.extraUpdates = 2;
			this.collidable = false;
			this.frames = 1;
			this.width = 64;
			this.height = 64;
			this.rotateByDirection = true;
			this.ranged = true;
			this.setLight(512, new Color(0.7f, 0.4f, 0.7f, 1f), 16);
		}
		else if(this.type == 126)
		{
			this.collidable = false;
			this.width = 16;
			this.height = 16;
			this.rotateByVelocity = true;
			this.ranged = true;
			this.setLight(128, new Color(0.92f, 0.96f, 0.8f, 1f), 0);
		}
		else if(this.type == 127)
		{
			this.collidable = false;
			this.drawBack = true;
			this.frames = 10;
			this.hostile = true;
			this.width = 44;
			this.height = 52;
			this.ranged = false;
			this.setLight(256, new Color(0.75f, 0.5f, 0.75f, 1f), 16);
		}
		else if(this.type == 128)
		{
			this.collidable = true;
			this.width = 80;
			this.height = 32;
			this.hostile = true;
			this.frames = 5;
			this.rotateByVelocity = true;
			this.collisionWaitFrames = 13;
			this.ranged = true;
			this.setLight(256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_DARKNESS], 20);
			this.summonAudio = DJ.FIREHIT;
			this.summonPitch = 0.5f;
			this.destroyAudio = DJ.SHADOWHIT;
			this.destroyPitch = 0.8f;
		}
		else if(this.type == 129)
		{
			this.collidable = true;
			this.width = 64;
			this.height = 64;
			this.hostile = true;
			this.frames = 1;
			this.rotateByVelocity = true;
			this.collisionWaitFrames = 12;
			this.ranged = true;
			this.setLight(256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_DARKNESS], 20);
			this.summonAudio = DJ.FIREHIT;
			this.summonPitch = 0.5f;
			this.destroyAudio = DJ.SHADOWHIT;
			this.destroyPitch = 0.8f;
		}
		
		/*
		public int type;
		public boolean collidable;
		public int frames = 1;
		public int width;
		public int height;
		public boolean rotateByDirection = false;
		public boolean rotateByVelocity = false;
		public double rotationOffset = 0;
		public boolean useAI = true;
		public boolean drawBack = false;
		public float timeLeft = 0f;
		public boolean ranged = false;
		public int actionTick = 0;
		public int extraUpdates = 1;
		public boolean hostile = false;
		public int collisionWaitFrames = 2;
		public boolean lights = false;
		public int lightSize = 256;
		public Color lightColor = Color.WHITE;
		public boolean regOnHit = true;
		public float lightStrenght = 1;
		public int projectileclass = Constant.PROJECTILETYPE_ANY;
		*/
		this.postGen(this.owner);
	}
	
	public void setLight(int i, Color color2, float vibrancy)
	{
		Lighting l = Lighting.Prepare(this.Center(), i, new Color(color2.r*0.75f+0.25f, color2.g*0.75f+0.25f, color2.b*0.75f+0.25f, color2.a), this.timeLeft);
		l.vibrancy = vibrancy;
		this.queueLight = l;
	}

	public void postGen(Entity ent)
	{
		if(this.hostile || ent == null)
			return;

		if(ent.getClass() == Player.class)
		{
			Player player = (Player)ent;
			if(player.haveBuff(6) != -1 && (this.projectileclass == Constant.PROJECTILETYPE_ARROW || this.projectileclass == Constant.PROJECTILETYPE_BULLET))
			{
				player.removeBuff(6);
				this.addBuff(6, this.timeLeft, player);
			}
			int starshooter = player.haveBuff(5);
			if(starshooter != -1 && (this.type == 9))
			{
				this.addBuff(5, player.buffs[starshooter].stacks, player.buffs[starshooter].stacks, this.timeLeft, player);
				player.removeBuff(5);
			}
			
			if(player.haveBuff(31) != -1)
			{
				player.removeBuff(31);
				this.addBuff(31, this.timeLeft, player);
			}
			
			if(player.haveBuff(32) != -1)
			{
				player.removeBuff(32);
				this.addBuff(32, this.timeLeft, player);
			}
			
			if(player.haveBuff(33) != -1)
			{
				this.extraUpdates *= 2;
			}
		}
	}

	private void resetStats()
	{
		this.ticks = 0;
		this.currentFrameTicks = 0;
		this.currentFrame = 0;
		this.rotation = 0;
		this.flipped = false;
		this.mirrored = false;
		this.useAI = true;
		this.rotateByDirection = false;
		this.rotateByVelocity = false;
		this.rotationOffset = 0;
		this.scale = 1f;
		this.ranged = false;
		this.extraUpdates = 1;
		this.hostile = false;
		this.collisionWaitFrames = 2;
		this.collisionY = -1;
		this.lights = false;
		this.lightSize = 256;
		this.targeting = false;
		this.target = null;
		this.lightColor = Color.WHITE;
		this.lightStrength = 1;
		this.projectileclass = Constant.PROJECTILETYPE_ANY;
		this.alpha = 1f;
		this.regOnHit = true;
		this.queueLight = null;
		this.summonAudio = null;
		this.destroyAudio = null;
		this.summonPitch = 1f;
		this.destroyPitch = 1f;
		for(int i = 0;i < 30;i++)
		{
			this.buffs[i] = null;
		}
	}

	@Override
	public void update(float deltaTime)
	{
		if(!this.active)
			return;
		
		if(!AHS.isUp && this.queueLight != null)
		{
			this.queueLight.center = this.Center();
			this.queueLight.timeLeft = this.timeLeft;
			this.queueLight.parent = this;
			this.queueLight.hadParent = true;
			Main.lighting.add(this.queueLight);
			this.queueLight = null;
		}

		//this.velocity.y += NinjaBlock.gravity;

		if(this.rotateByVelocity)
		{
			this.rotation = (float)Math.toDegrees((Math.atan2(this.velocity.y, this.velocity.x)));
		}
		timeLeft -= deltaTime;
		if(timeLeft <= 0f)
		{
			this.destroy();
			return;
		}
		
		if(this.target != null && this.target instanceof Monster)
		{
			Monster m = (Monster)this.target;
			if(!m.active)
			{
				this.targeting = false;
				this.target = null;
			}
		}
		
		if(this.target == null && this.destroyOnTargetLoss)
		{
			this.destroy();
		}

		for(int j = 0;j < this.extraUpdates;j++)
		{
			boolean collision = false;
			if(this.ticks > this.collisionWaitFrames && (this.collisionY <= 0 || this.Center().y < this.collisionY))
				collision = this.collidable;
			
			if(!this.active)
				return;

			// Collision detection
			boolean collided = false;

			Monster who = null;

			boolean timestopped = false;
			Vector2 originalVelocity = this.velocity.cpy();
			Vector2 originalPosition = this.position.cpy();
			int originalFrame = this.currentFrame;
			float originalRotation = this.rotation;
			for(Projectile p : Constant.getProjectileList())
			{
				if(p != this && p.type == 91)
				{
					Circle c = new Circle(p.Center(), 400);
					if(Main.circleContainsPolygon(c, this.perfectHitBox()))
					{
						timestopped = true;
						j = this.extraUpdates;
						break;
					}
				}
			}
			
			if(this.useAI && !timestopped)
			{
				if(this.targeting)
				{
					this.redirectVelocity(this.target.Center());
				}
				
				float addAmountX = this.velocity.x * deltaTime;
				float newX = this.position.x + addAmountX;
				float addAmountY = this.velocity.y * deltaTime;
				float newY = this.position.y + addAmountY;
				if(!this.hostile)
				{
					for(Monster m : Constant.getMonsterList(false))
					{
						if(m.active && this.hitBox().overlaps(m.hitBox()) && this.canCollideWith(m))
						{
							collided = true;
							who = m;
							break;
						}
					}
				}
				else
				{
					for(Player p : Constant.getPlayerList())
					{
						if(this.hitBox().overlaps(p.hitBox()) && p.canBeHit() && !(p.rolling && p.rollBlocksProjectiles))
						{
							this.CollidePlayer(p);
						}
					}
				}
				this.ai[1] = 0;
				if(collision && !collided)
				{
					Polygon wHitBox = new Polygon(new float[]{0, 0, 
							width, 0, 
							width, height, 
							0, height});
					wHitBox.setOrigin(width/2, height/2);
					wHitBox.setPosition(this.position.x, newY);
					wHitBox.rotate((float) (this.rotation + this.rotationOffset));
					if(!Constant.tryGetMapForEntity(this).doesPolygonCollideWithMap(wHitBox))
					{
						this.position.y = newY;
					}
					else
					{
						if(this.velocity.y < 0f)
						{
							//this.position.y = (float) (Math.floor(this.position.y) - Math.floor(this.position.y) % Tile.TILE_SIZE);
							this.ai[1] = 1;
						}
						else if(this.velocity.y > 0f)
						{
							//this.position.y = (float) (Math.floor(this.position.y) + (Tile.TILE_SIZE - Math.floor(this.position.y) % Tile.TILE_SIZE) - Math.abs((this.height - Tile.TILE_SIZE)));
							this.ai[1] = 2;
						}

						if(this.ricochet)
							this.velocity.y *= -1f;
						collided = true;
					}

					wHitBox.setPosition(newX, this.position.y);
					if(!Constant.tryGetMapForEntity(this).doesPolygonCollideWithMap(wHitBox))
					{
						this.position.x = newX;
					}
					else
					{
						if(this.velocity.x < 0f)
						{
							//this.position.x = (float) (Math.floor(this.position.x) - Math.floor(this.position.x) % Tile.TILE_SIZE);
							this.ai[1] = 3;
						}
						else if(this.velocity.x > 0f)
						{
							//this.position.x = (float) (Math.floor(this.position.x) - (Tile.TILE_SIZE - Math.floor(this.position.x) % Tile.TILE_SIZE));
							//this.position.x += (float) (Tile.TILE_SIZE - this.position.x % 32) - (this.width-Tile.TILE_SIZE);
							this.ai[1] = 4;
						}
						if(this.ricochet)
							this.velocity.x *= -1f;
						collided = true;
					}
				}
				else
				{
					this.position = new Vector2(newX, newY);
				}
				if(collided)
				{
					this.Collide(who, this.owner);
				}
				if(this.velocity.x < 0)
				{
					this.direction = -1;
				}
				else
				{
					this.direction = 1;
				}
			}

			//AI
			Player player = null;
			if(this.owner.getClass() == Player.class)
			{
				player = (Player) this.owner;

				if(this.haveBuff(6) != -1)
				{
					Lighting.Create(this.Center(), 512, Main.getLightingColor(new Color(0.96f, 0.92f, 0.6f, 1f), 0.3f), 1f, 0.8f);
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 10, new Color(1f, 1f, MathUtils.random(), 1f), 0f, 1f, 1f).setLight(16, new Color(0.92f, 0.96f, 0.6f, 1f));
					p.drawFront = true;
				}
				if(this.haveBuff(31) != -1)
				{
					float dist = Math.max(this.width, this.height)/2f+10;
					int quant = 0;
					Skill s = player.getSkill(39);
					if(s != null)
					{
						quant = s.getInfoValueI(player, 0);
					}
					for(int i = 0;i < quant;i++)
					{
						float angle = 360/quant * i + Constant.gameTick()*2;
						Vector2 pos = new Vector2(dist, 0f).setAngle(angle);
						Vector2 center = this.Center();
						pos.rotate(this.rotation);
						pos.add(center);
						Particle p = Particle.Create(pos, Vector2.Zero, 16, new Color(0.96f, 0.92f, 0.6f, 1f), 0f, 0.5f, 1f).setLight(16, new Color(0.92f, 0.96f, 0.6f, 1f));
						p.drawFront = true;
						p.rotation = angle;
					}
					
					Lighting.Create(this.Center(), 512, Main.getLightingColor(new Color(0.96f, 0.92f, 0.6f, 1f), 0.3f), 1f, 0.7f);
				}
			}

			if(this.type == 1)
			{
				this.velocity.y -= 500f * deltaTime;
			}
			else if(this.type == 2)
			{
				for(int i = 0;i < 10;i++)
				{
					Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-5, 5), MathUtils.random(-5, 5)), 2, Color.CYAN, 0f, 0.5f, MathUtils.random(0.5f, 1f));
				}
			}
			else if(this.type == 3)
			{
				Vector2 center = player.Center();
				if(player.controllingGeas)
				{
					center.x = this.ai[7];
					center.y = this.ai[8];
				}
				Vector2 spCenter = this.Center();
				float angle = (float)Math.toDegrees(Math.atan2(spCenter.y - center.y, spCenter.x - center.x));
				this.rotation = angle + 135;
				if(this.ai[5] != 1)
				{
					this.velocity.x *= 0.95f;
					this.velocity.y *= 0.975f;
					if(this.velocity.len() < 200f)
					{
						this.ai[5] = 1;
						this.resetCollisions();
					}
				}
				else
				{
					Vector2 dif = new Vector2(center.x - this.Center().x, center.y - this.Center().y);
					dif.nor();
					float sp = player.attackSpeed/2;
					if(sp < 1)
						sp = 1;
					dif.scl(30 * sp);
					this.velocity.x *= 0.985f;
					this.velocity.y *= 0.985f;
					this.velocity.add(dif);
					if(this.Center().dst(player.Center()) < 50)
					{
						this.destroy();
					}
				}

				if(this.ai[6] == 1)
				{
					this.position.x = player.Center().x - this.width/2;
				}
			}
			else if(this.type == 4)
			{
				this.rotation += 10;
				for(int i = 0;i < 3;i++)
				{
					float r = MathUtils.random(0.7f,1f);
					Color color = new Color(r,r,r,1);
					Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 2, color, 0f, 1f, 1f);
				}
			}
			else if(this.type == 5)
			{
				for(int i = 0;i < 30;i++)
				{
					float height2 = this.height;
					Vector2 pos = new Vector2(this.position.x + MathUtils.random(this.width), this.position.y + MathUtils.random(this.height));
					height2 = (this.height/2) - Math.abs(pos.cpy().y - this.position.y - this.height/2);
					pos.sub(this.Center());
					pos.rotate(this.rotation);
					pos.add(this.Center());
					//Color color = new Color(1f,0.5f + MathUtils.random()*0.3f,0f,1);
					Color color = new Color((height2/(this.height/2)), 0.5f + MathUtils.random()*0.3f + (height2/(this.height/2)), 1f, 1f);
					Particle p = Particle.Create(pos, new Vector2(this.velocity.x * -0.5f + MathUtils.random(-50f, 50f), this.velocity.y * -0.5f + MathUtils.random(-50f, 50f)), 2, color, 0f, 1f, 1+MathUtils.random());
					p.setLight(32, Color.CYAN);
				}
			}
			else if(this.type == 6)
			{
				this.velocity.y -= 800f * deltaTime;
				this.rotation += 10;
				for(int i = 0;i < 4;i++)
				{
					float height2 = this.height;
					Vector2 pos = new Vector2(this.position.x + MathUtils.random(this.width), this.position.y + MathUtils.random(this.height));
					height2 = (this.height/2) - Math.abs(pos.cpy().y - this.position.y - this.height/2);
					pos.sub(this.Center());
					pos.rotate(this.rotation);
					pos.add(this.Center());
					Color color = new Color((height2/(this.height/2)), 0.5f + MathUtils.random()*0.3f + (height2/(this.height/2)), 1f, 1f);
					Particle p = Particle.Create(pos, new Vector2(this.velocity.x * -0.5f + MathUtils.random(-50f, 50f), this.velocity.y * -0.5f + MathUtils.random(-50f, 50f)), 2, color, 0f, 1f, 1+MathUtils.random());
					p.setLight(16, Color.CYAN);
				}
			}
			else if(this.type == 7)
			{
				for(int i = 0;i < 12;i++)
				{
					Vector2 pos = this.randomHitBoxPosition();
					float val = MathUtils.random()*0.5f;
					Color color = new Color(val, 0f, val, 1f);
					Particle p = Particle.Create(pos, Vector2.Zero.cpy(), 6, color, 0f, 1f, (MathUtils.random()+1f)*0.75f);
					p.drawFront = true;
					p.setLight(32, new Color(0.4f, 0f, 0.4f, 1f));
				}
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrameTicks = 0;
					this.currentFrame = (this.currentFrame+1)%2;
				}
			}
			else if(this.type == 8)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 2, Color.RED, 0f, 0.1f, 1f);
				p.setLight(32, p.color);
			}
			else if(this.type == 9)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero.cpy(), 8, new Color(1f,0.85f,0f,1f), 0f, 0.1f, 0.5f);
				p.rotation = this.rotation;
				p.width = 64;
			}
			else if(this.type == 10)
			{
				this.velocity.y -= 500f * deltaTime;
				for(int i = 0;i < 8;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(this.velocity.x * -0.5f + MathUtils.random(-50f, 50f), this.velocity.y * -0.5f + MathUtils.random(-50f, 50f)), 2, new Color(1f, 0.5f + MathUtils.random()/2f, 0f, 1f), 0f, 1f, 0.75f + MathUtils.random()/2f);
					p.drawFront = true;
				}
				this.rotation += 10f;
			}
			else if(this.type == 11)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 3)
				{
					this.currentFrameTicks = 0;
					this.currentFrame = (this.currentFrame + 1) % 3;
				}
				Particle p = Particle.Create(this.Center(), Vector2.Zero.cpy(), 8, new Color(0.5f,0f,0.5f,1f), 0f, 0f, 0.5f);
				p.rotation = this.rotation;
				p.width = 64;
			}
			else if(this.type == 12)
			{
				Lighting.Create(this.Center(), 512, new Color(0.96f, 0.92f, 0.75f, 1f), 1f, 0.8f);
				Item item = player.inventory[Constant.ITEMSLOT_RIGHT];
				if(item != null)
				{
					player.playAnim(item.getAttackTexture(), item.attackFrames, 3, true, false, true, new Vector2(24, 0));
					player.mouseOnAttack = player.mousePos.cpy();
					player.posOnAttack = player.Center().cpy();
					player.customAnim2Frame = item.attackFrames-1;
				}
				//Vector2 dif = player.position.cpy().sub(player.lastPosition);
				if(player.mouseOnAttack.x - player.posOnAttack.x < 0)
					this.flipped = true;

				float angle = Main.angleBetween(player.Center(), player.mousePos);
				Vector2 mouseNor = player.mousePos.cpy().sub(player.Center());
				mouseNor.nor();
				mouseNor.scl(50f);
				Vector2 pos = new Vector2(player.Center().x + mouseNor.x - this.width/2f, player.position.y + mouseNor.y);
				this.position = pos;
				this.rotation = angle;
				player.direction = Main.directionFromTo(player.Center(), player.mousePos);
				if(player.direction == Constant.DIRECTION_LEFT)
					this.flipped = true;
				else
					this.flipped = false;
				
				Polygon wHitBox = new Polygon(new float[]{0, 0, 
						width, 0, 
						width, height, 
						0, height});
				wHitBox.setOrigin(width/2, height/2);
				wHitBox.setPosition(this.position.x, this.position.y);
				wHitBox.rotate(this.rotation);
				for(Projectile p : Constant.getProjectileList())
				{
					if(p.active && p.hostile)
					{
						Polygon pHitBox = new Polygon(new float[]{0, 0, 
								p.width, 0, 
								p.width, p.height, 
								0, p.height});
						pHitBox.setOrigin(p.width/2, p.height/2);
						pHitBox.setPosition(p.position.x, p.position.y);
						pHitBox.rotate(p.rotation);

						if(Intersector.overlapConvexPolygons(pHitBox, wHitBox))
						{
							p.destroy();
						}
					}
				}
				Skill s = player.getSkill(8);
				if(s == null)
				{
					this.destroy();
				}
				else
				{
					int maxTicks = (int) (60 * s.getInfoValueF(player, 0));
					if(this.ticks >= maxTicks)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 13)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero.cpy(), 8, new Color(0f,1f,1f,1f), 0f, 0f, 0.5f);
				p.rotation = this.rotation;
				p.frameCounterTicks = -1;
				p.width = 64;
			}
			else if(this.type == 14)
			{
				Vector2 vel = this.velocity.cpy().scl(-1f).nor().scl(MathUtils.random(50f, 150f));
				Particle p = Particle.Create(this.randomHitBoxPosition().add(MathUtils.random(-10, 10), MathUtils.random(-10, 10)), vel, 2, new Color(0f, 0f, 0f, 0f), 0f, 0.5f, 1f);
				p.rotation = this.velocity.angle();
				p.setLight(32, Color.PURPLE);
			}
			else if(this.type == 15)
			{
				this.currentFrameTicks++;
				int max = 3;
				if(currentFrame < 8)
					max = 1;
				
				if(currentFrame >= 15 && currentFrame <= 18)
					max = 5;
				
				if(this.currentFrameTicks >= max)
				{
					this.currentFrameTicks = 0;
					this.currentFrame++;
					if(this.currentFrame > 21)
						this.destroy();
				}
			}
			else if(this.type == 16)
			{
				this.rotation += 10;
				this.velocity.y -= 600 * deltaTime;
			}
			else if(this.type == 17)
			{
				if(this.ticks % ((int)(10 / player.getAttackSpeed())) == 0)
				{
					this.resetCollisions();
				}
				this.rotation += 35;
				if(this.ai[2] != 1)
				{
					this.velocity.scl(0.95f);
					if(this.velocity.len() < 40)
					{
						this.ai[2] = 1;
						this.ai[3] = 0;
					}
				}
				else
				{
					this.ai[3] += 20;
					Vector2 dif = new Vector2(player.Center().x - this.Center().x, player.Center().y - this.Center().y);
					dif.nor().scl(30f + ai[3]);
					this.velocity = dif;
					if(this.Center().dst(player.Center()) < 50)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 18)
			{
				if(this.ticks % ((int)(10 / player.getAttackSpeed())) == 0)
				{
					this.resetCollisions();
				}
				this.rotation += 35;
				Vector2 center = player.Center();
				if(this.ai[2] != 1)
				{
					this.velocity.scl(0.95f);
					if(this.velocity.len() < 40)
					{
						this.ai[2] = 1;
					}
				}
				else
				{
					Vector2 dif = new Vector2(center.x - this.Center().x, center.y - this.Center().y);
					dif.nor();
					float sp = player.attackSpeed/2;
					if(sp < 1)
						sp = 1;
					dif.scl(130 * sp);
					this.velocity.x *= 0.9f;
					this.velocity.y *= 0.9f;
					this.velocity.add(dif);
					if(this.Center().dst(player.Center()) < 50)
					{
						this.destroy();
					}
				}
				
				for(int i = 0;i < 3;i++)
				{
					float ran = MathUtils.random(360);
					float cos2 = (float)Math.cos((ran) * Math.PI/180f);
					float sin2 = (float)Math.sin((ran) * Math.PI/180f);
					float dis = MathUtils.random(70, 90);
					Particle p = Particle.Create(new Vector2(this.Center().x + cos2 * dis, this.Center().y + sin2 * dis), Vector2.Zero, 2, new Color(1f, 0.5f+MathUtils.random()/2f, 0f, 1f), 2f, 1.5f, 1f);
					p.setLight(16, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE]);
					p.drawFront = true;
				}
			}
			else if(this.type == 19)
			{
				for(int i = 0;i < 2;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.2f), 2, new Color(0f, 1f, 1f, 1f), 0f, 0.5f, 1f);
					p.setLight(16, p.color);
				}
			}
			else if(this.type == 21)
			{
				for(int i = 0;i < 3;i++)
					Particle.Create(this.randomHitBoxPosition(), Vector2.Zero.cpy(), 6, new Color(0f, 0.9f, 0f, 1f), 0f, 2f, 1f);
			}
			else if(this.type == 22)
			{
				this.rotation += 10f;
			}
			else if(this.type == 23)
			{
				Vector2 dest = new Vector2(this.ai[7], this.ai[8]);
				if(this.Center().dst(dest) < 40 || this.ticks > 100000)
				{
					this.destroy();
				}
				
				if(this.ticks % 10 == 0)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 26, new Color(1f, 1f, 1f, 1f),  0f, 1f, 1f);
					p.rotation=  this.velocity.angle();
				}
			}
			else if(this.type == 24)
			{
				this.rotation += 3;
				if(this.velocity.len() > 40)
				{
					Particle p = Particle.Create(this.Center(), Vector2.Zero, 8, Color.WHITE, 0f, 0f, 0.5f);
					p.rotation = this.velocity.angle();
				}
				if(this.ai[5] != 1)
				{
					this.velocity.scl(0.993f);
					if(this.velocity.len() < 10)
					{
						this.ai[5] = 1;
						if(this.ai[7] != 1)
							this.damage *= 3;
						
						this.collidable = false;
						this.resetCollisions();
					}
				}
				else
				{
					this.ai[6]++;
					this.velocity = this.owner.Center().sub(this.Center()).nor().scl(5 + this.ai[6]);
					if(this.hitBox().overlaps(this.owner.hitBox()))
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 25)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrameTicks = 0;
					this.currentFrame++;
					if(this.currentFrame >= 4)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 26)
			{
				for(int i = 0;i < 6;i++)
				{
					float r = 0.33f + (MathUtils.random()/2f);
					Particle.Create(this.randomHitBoxPosition(), Vector2.Zero.cpy(), 6, new Color(r, r, r, 1f), 0f, 1f, 1f);
				}
				if(this.ticks < 120)
				{
					if(this.ticks % 5 == 0)
					{
						Skill s = player.getSkill(23);
						float dmg = 0f;
						if(s == null)
							dmg = 0f;
						else
						{
							dmg = s.valueByLevel(0.05f, 0.075f, 0.1f, 0.125f, 0.15f) * player.getPP();
						}
						Projectile.Summon(27, this.randomHitBoxPosition(), new Vector2(0f, -1000f), (int) (dmg), 5f, player);
					}
				}
				else if(this.ticks == 120)
				{
					Skill s = player.getSkill(23);
					float dmg = 0f;
					if(s == null)
						dmg = 0f;
					else
					{
						dmg = s.valueByLevel(1.9f, 2.05f, 2.2f, 2.35f, 2.5f) * player.getPP();
					}
					Projectile.Summon(28, this.Center(), new Vector2(0f, -1800f), (int)dmg, 5f, player);
				}
			}
			else if(this.type == 27)
			{
				this.velocity.y -= 500 * deltaTime;
				this.drawBack = true;
			}
			else if(this.type == 28)
			{
				if(this.velocity.len() > 100)
				for(int i = 0;i < 2;i++)
				{
					Color color = new Color(1f, 0.1f, 0.1f, 1f);
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 2, color, -2f, 2f, MathUtils.random(1f, 2f));
					p.setLight(32, p.color);
					p.drawBack = true;
				}
				this.drawBack = true;
				if(!this.collidable)
				{
					if(this.position.y + this.height < this.ai[6] && Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, this.position.y + 100, this.width, this.height - 100))
					{
						this.collidable = true;
						Main.doEarthquake(30);
						this.velocity.y = 0f;
						for(int i = 0;i < 100;i++)
						{
							Color color = new Color(1f, 0.1f, 0.1f, 1f);
							Particle p = Particle.Create(new Vector2(this.position.x + MathUtils.random(this.width), this.position.y + 120), new Vector2(MathUtils.random(-150, 150), MathUtils.random(20, 300)), 2, color, -2f, 2.5f, MathUtils.random(1f, 2f));
							p.setLight(32, p.color);
							p.collisions = true;
						}
					}
				}
				if(this.timeLeft < 1)
				{
					this.alpha = this.timeLeft;
				}
			}
			else if(this.type == 29)
			{
				for(int i = 0;i < 25;i++)
				{
					float r = 0.1f + (MathUtils.random()/3f);
					Particle.Create(new Vector2(this.position.x + MathUtils.random(this.width), this.position.y + this.height + MathUtils.random(-50, 10)), Vector2.Zero.cpy(), 6, new Color(r, r, r, 1f), 0f, 1f, 2f);
				}
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrameTicks = 0;
					this.currentFrame++;
					if(this.currentFrame >= this.frames)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 30)
			{
				for(int i = 0;i < this.velocity.len()/300f;i++)
				{
					Vector2 spawnPos = this.randomHitBoxPosition();
					Vector2 extraVel = new Vector2(spawnPos.cpy().sub(this.Center())).rotate(Main.angleBetween(this.Center(), spawnPos));
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().nor().scl(-300f).add(extraVel), 2, Color.WHITE, 0f, 0.5f, 1f);
					p.setLight(16, Color.WHITE);
				}
				this.velocity.scl(0.85f);
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 4)
				{
					this.currentFrameTicks = 0;
					this.currentFrame++;
					if(this.currentFrame >= this.frames)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 32 || this.type == 33 || this.type == 31 || this.type == 34 || this.type == 35)
			{
				this.scale += 0.02f;
				if(this.timeLeft < 0.5f)
				{
					this.alpha = this.timeLeft*2;
				}
			}
			else if(this.type == 37)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero.cpy(), 8, new Color(0f,1f,1f,1f), 0f, 0f, 0.1f);
				p.rotation = this.rotation;
				p.frameCounterTicks = -1;
				p.width = 4;
			}
			else if(this.type == 38)
			{
				float r = MathUtils.random()/3f;
				Color c = new Color(r, r, r, 1f);
				Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.2f), 2, c, 0f, 0.5f, 1f);
			}
			else if(this.type == 39)
			{
				this.velocity.y -= 300 * deltaTime;
				if(this.ai[0] != 1)
				{
					if(this.ai[1] == 1)
					{
						this.timeLeft = 8f;
						this.ai[0] = 1;
						this.velocity.x = 0f;
						this.rotateByVelocity = false;
						this.rotation = 0f;
						this.position.y = Constant.tryGetMapForEntity(this).getNextestFreeSpace(this.position.x, this.position.y
								, this.width, this.height, 1, 1).scl(64f).y;
						for(int i = 0;i < 8;i++)
						{
							float r = MathUtils.random()/2f+0.5f;
							Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 6, new Color(r, 0f, r, 1f), 0f, 1f, 1f);
						}
					}
				}
				else
				{
					this.rotation = 0f;
				}
			}
			else if(this.type == 40)
			{
				this.velocity.y -= 300 * deltaTime;
				if(this.ai[0] != 1)
				{
					if(this.ai[1] == 1)
					{
						this.timeLeft = 8f;
						this.ai[0] = 1;
						this.velocity.x = 0f;
						this.rotateByVelocity = false;
						this.rotation = 0f;
						this.position.y = Constant.tryGetMapForEntity(this).getNextestFreeSpace(this.position.x, this.position.y
								, this.width, this.height, 1, 1).scl(64f).y;
						for(int i = 0;i < 8;i++)
						{
							float r = MathUtils.random()/2f+0.25f;
							Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 6, new Color(1f, 1f, r, 1f), 0f, 1f, 1f);
						}
					}
				}
				else
				{
					this.rotation = 0f;
				}
			}
			else if(this.type == 41)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero.cpy(), 8, new Color(1f,1f,0.5f,1f), 0f, 0f, 1f);
				p.rotation = this.rotation;
				p.frameCounterTicks = -1;
				p.width = 32;
				
				if(this.position.x < -100 || this.position.x > Constant.tryGetMapForEntity(this).width * 64f + 100 ||
						this.position.y < -100 || this.position.y > Constant.tryGetMapForEntity(this).height * 64f + 100)
				{
					this.destroy();
				}
			}
			else if(this.type == 42 || this.type == 59 || this.type == 108)
			{
				if(this.target != null)
				{
					this.position = this.target.Center().sub((this.width)/2f, (this.height)/2f);
				}
				
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 1)
				{
					this.currentFrame = Math.min(this.currentFrame + 1, this.frames-1);
				}
				
				if(this.currentFrame >= this.frames-1)
				{
					this.ai[0]++;
				}
				if(this.timeLeft < 1)
				{
					this.alpha = this.timeLeft;
				}
			}
			else if(this.type == 43)
			{
				if(this.ai[5] != 1)
				{
					for(int i = 0;i < 25;i++)
					{
						float r = 0.1f + (MathUtils.random()/3f);
						Particle p = Particle.Create(this.Center().add(MathUtils.random(-this.getScaledWidth()/2f-20, this.getScaledWidth()/2f+20), this.getScaledHeight()/2f+MathUtils.random(-30*this.scale, 30*this.scale)), Vector2.Zero.cpy(), 6, new Color(r, r, r, 1f), 0f, 1f, 2f*this.scale);
						p.drawFront = !this.drawBack;
						p.frameCounterTicks = 2;
					}
				}
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrameTicks = 0;
					this.currentFrame++;
					if(this.currentFrame >= this.frames)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 44)
			{
				this.drawBack = true;
				if(!this.collidable)
				{
					if(Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, this.position.y + 10, this.width, this.height - 10))
					{
						this.collidable = true;
						this.velocity.y = -1f;
						for(int i = 0;i < 50;i++)
						{
							float r = .75f + MathUtils.random()/4f;
							Color color = new Color(0f, r, r, 1f);
							Particle.Create(new Vector2(this.position.x + MathUtils.random(this.width), this.position.y + 20), new Vector2(MathUtils.random(-80, 80), MathUtils.random(200, 400)), 13, color, -4f, 2f, 1f);
						}
					}
				}
				if(this.ticks > 120)
				{
					this.alpha -= 0.05f;
					if(this.alpha <= 0f)
					{
						this.destroy();
					}
				}
				for(int i = 0;i < 2;i++)
				{
					float fc = MathUtils.random()/4f + 0.75f;
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 13, new Color(0f, fc, fc, 1f), -0.5f, 1f, 1f);
					p.drawBack = true;
				}
			}
			else if(this.type == 45)
			{
				boolean eob = this.haveBuff(63) != -1;
				for(int k= 0;k < 2;k++)
				{
					for(int i = 0;i < 4;i++)
					{
						double angle = (4 * Constant.gameTick() + i * 90) * Math.PI/180f;
						float sin = (float)Math.sin(angle);
						float cos = (float)Math.cos(angle);
						float fc = MathUtils.random()/4f + 0.3f;
						Particle p = Particle.Create(new Vector2(cos * (60 + k * 36), sin * (60 + k * 36)).add(this.Center()).add(MathUtils.random(-4, 4), MathUtils.random(-4, 4)), new Vector2(0f, 0f), 2, new Color(fc, 0f, fc, 1f), 0.5f, 1f, 1f);
						p.drawBack = true;
						p.setLight(16, new Color(1f, 0f, 1f, 1f));
					}
				}

				for(int i = 0;i < 5;i++)
				{
					double angle = 72 * i * Math.PI/180f;
					float direction = (float) (Math.toDegrees(angle) + 144);
					float sin = (float)Math.sin(angle);
					float cos = (float)Math.cos(angle);
					float fc = MathUtils.random()/4f + 0.3f;
					Vector2 pos = new Vector2(cos * 96, sin * 96).add(this.Center()).add(new Vector2(this.ticks*2, 0f).rotate(direction)).add(MathUtils.random(-4, 4), MathUtils.random(-4, 4));
					Particle p = Particle.Create(pos, new Vector2(0f, 0f), 2, new Color(fc, 0f, fc, 1f), 0.5f, 1f, 1f);
					p.drawBack = true;
					p.setLight(16, new Color(1f, 0f, 1f, 1f));
				}
				
				if(this.Center().dst(this.owner.Center()) < 50f)
				{
					if(eob)
						player.removeBuff(63);
					Skill s = player.getSkill(53);
					if(s != null && s.casts > 0)
					{
						s.casts = 4;
						s.applyCast(player);
					}
					owner.position.x = this.Center().x - owner.width/2f;
					owner.position.y = this.Center().y - owner.height/2f;
					owner.unstuck();
					owner.velocity.x = 0f;
					owner.velocity.y = 0f;
					Main.doEarthquake(eob ? 30 : 15);
					for(int i = 0;i < 180;i++)
					{
						double angle = (i * 2) * Math.PI/180f;
						float sin = (float)Math.sin(angle);
						float cos = (float)Math.cos(angle);
						float fc = MathUtils.random()/4f + 0.3f;
						Vector2 pos = new Vector2(cos * 96, sin * 96).add(this.Center()).add(MathUtils.random(-4, 4), MathUtils.random(-4, 4));
						Vector2 vel = new Vector2(100f, 0f).rotate(Main.angleBetween(this.Center(), pos));
						Particle p = Particle.Create(pos, vel, 2, new Color(fc, 0f, fc, 1f), 0.5f, 1f, 1f);
						p.drawBack = true;
						p.setLight(16, new Color(1f, 0f, 1f, 1f));
					}
					Color[] quadcolors = new Color[4];
					quadcolors[0] = new Color(0.5f, 0.05f, 0.5f, 1f);
					quadcolors[1] = new Color(0.4f, 0.05f, 0.4f, 1f);
					quadcolors[2] = new Color(0.3f, 0.05f, 0.3f, 1f);
					quadcolors[3] = new Color(0.2f, 0.05f, 0.2f, 1f);
					Color smokeColor = new Color(1f, 0f, 1f, 1f);
					Main.createCustomExplosion(this.Center(), eob ? 280 : 180, 1, false, quadcolors, smokeColor);
					for(Monster m : Constant.getMonstersInRange(this.myMapX, this.myMapY, this.Center(), eob ? 196 : 96))
					{
						float dmg = 0f;
						if(this.owner instanceof Player)
						{
							if(s != null)
							{
								dmg = s.getInfoValueF(player, 0) + (eob ? s.getInfoValueF(player, 1) : 0);
							}
						}
						m.hurtDmgVar((int) dmg, 0, 0f, false, Constant.DAMAGETYPE_DARKNESS, player);
					}
					if(this.owner instanceof Player)
					{
						Player p = (Player)this.owner;
						p.removeBuff(36);
						p.customAnim2 = null;
					}
					this.destroy();
				}
			}
			else if(this.type == 46)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrame = (this.currentFrame + 1) % this.frames;
					this.currentFrameTicks = 0;
				}
				for(int i = 0;i < 3;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.05f), 24,  new Color(0.8f, 0.4f, 0.15f, 1f), 0f, 1f, 2f);
					p.setLight(32, new Color(0.8f, 0.4f, 0.15f, 1f));
					Lighting.Create(p.position, 128, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE], 1f);
				}
			}
			else if(this.type == 47)
			{
				if(this.timeLeft < 0.5f)
				{
					this.alpha = this.timeLeft * 2f;
				}
				
				this.currentFrame = (this.currentFrame + 1 ) % this.frames;
				
				if(this.ticks > 0 && this.ticks % 60 == 0)
				{
					Vector2 vel = new Vector2(1000f, 0f).rotate(this.rotation);
					Projectile.Summon(49, this.Center(), vel, this.damage/2, 5f, this.owner);
				}
			}
			else if(this.type == 48)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 2)
				{
					this.currentFrame = (this.currentFrame + 1 ) % this.frames;
					this.currentFrameTicks = 0;
				}
				for(int i = 0;i < 2;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 15, new Color(0.9f, 0.7f, 0.05f, 1f), 0f, 1f, 1f);
					p.drawFront = true;
				}
			}
			else if(this.type == 50)
			{
				if(this.ticks % 20 == 0)
				{
					for(int i = 0;i < 4;i++)
					{
						Vector2 vel = new Vector2(MathUtils.random(-400, 400), -1000f);
						Projectile p = Projectile.Summon(48, this.Center().add(MathUtils.random(-400, 400), 1500f), vel, this.damage, 5f, this.owner);
						p.collisionY = this.Center().y;
					}
				}
			}
			else if(this.type == 51)
			{
				this.velocity.y -= 500f * deltaTime;
			}
			else if(this.type == 52)
			{
				if(this.timeLeft < 1)
					this.alpha = this.timeLeft;
			}
			else if(this.type == 53)
			{
				Main.doEarthquake(4);
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrameTicks = 0;
					this.currentFrame = (this.currentFrame + 1) % this.frames;
				}
				for(int i = 0;i < 2;i++)
				{
					float r = MathUtils.random(0.9f, 1f);
					Particle pa = Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(100, 300) * this.direction, MathUtils.random(0, 200)), 2, new Color(r, r, r, 1f), -2.5f, 1.5f, 1f);
					pa.drawFront = true;
					pa.lights = true;
					pa.lightSize = 16;
				}
				if((this.velocity.x > 0 && this.Center().x > this.ai[5]) || (this.velocity.x < 0 && this.Center().x < this.ai[5]))
				{
					this.destroy();
				}
			}
			else if(this.type == 54)
			{
				this.alpha = Math.min(1f, this.timeLeft);
			}
			else if(this.type == 55)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.2f), 2, new Color(1f, 0f, 1f, 1f), 0f, 1f, 1f);
				p.setLight(32, new Color(1f, 0.2f, 1f, 1f));
			}
			else if(this.type == 56)
			{
				Lighting l = Lighting.Create(this.Center(), 512, new Color(0.5f, 0.8f, 1f, 1f), 1f);
				l.setParent(this);
				this.rotation -= 10;
				for(int i = 0;i < 6;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.1f), 2, new Color(0f, 1f, 1f, 1f), 0f, 0.5f, 1f);
					p.rotation = this.rotation - 45f + (MathUtils.randomBoolean() ? 180 : 0);
					p.setLight(32, p.color);
					p.drawFront = true;
				}
				if(this.Center().dst(this.owner.Center()) > 3000f && this.ai[5] != 1)
				{
					Vector2 velNor = this.velocity.cpy().nor();
					this.position.sub(velNor.scl(6000f));
					this.resetCollisions();
					this.ai[5] = 1;
					this.setTarget(player);
				}
				if(this.ai[5] == 1 && this.hitBox().overlaps(player.hitBox()))
				{
					this.destroy();
					Color color = new Color(0f, 1f, 1f, 1f);
					Color[] quad = new Color[] {color, color, color, color};
					Main.createCustomExplosion(this.Center(), 130, 1, false, quad, quad[0]);
					Main.doEarthquake(25);
					for(int i = 0;i < 200;i++)
					{
						Particle p = Particle.Create(this.Center().add(new Vector2(1f, 0f).scl(MathUtils.random(35f, 150f)).rotate(MathUtils.random(360f))), Vector2.Zero, 2, new Color(0f, 1f, 1f, 1f), 0f, 0.5f, 1f);
						p.rotation = Main.angleBetween(this.Center(), p.Center()) + 45 + (MathUtils.randomBoolean() ? 180 : 0);
						p.drawFront = true;
						p.setLight(32, p.color);
					}
					for(Monster m : Constant.getMonstersInRange(player.myMapX, player.myMapY, player.Center(), 175f))
					{
						m.hurtDmgVar((int) (this.damage * 2f), Main.directionFromTo(player, m), 1f, player.canCritical(m), Constant.DAMAGETYPE_ENERGY, player);
						m.Stun(1f, player);
					}
				}
			}
			else if(this.type == 57)
			{
				if(this.timeLeft < 1f && this.ai[5] == 1)
				{
					this.alpha = this.timeLeft;
				}
				if(this.ai[5] != 1)
				{
					this.position = this.owner.Center().sub(this.getScaledWidth()/2f, this.getScaledHeight()/2f);
				}
			}
			else if(this.type == 58)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 2)
				{
					this.currentFrameTicks = 0;
					this.currentFrame = (this.currentFrame + 1) % this.frames;
				}
				this.velocity.y -= 200 * deltaTime;
			}
			else if(this.type == 60)
			{
				for(int i = 0;i < 3;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 2, new Color(1f, 0f, 0f, (this.ai[5] == 1 ? 0.3f : 0.2f)), 0f, 0.75f, 1f);
					p.setLight(this.ai[5]==1?32:16, new Color(1f, 0.1f, 0.1f, 1f));
				}
				
				if(this.owner.getClass() == Player.class)
				{
					Vector2 dif = new Vector2(player.Center().x - this.Center().x, player.Center().y - this.Center().y);
					dif.nor();
					dif.scl(115);
					this.velocity.x *= 0.91f;
					this.velocity.y *= 0.91f;
					this.velocity.add(dif);
					
					if(this.hitBox().overlaps(player.hitBox()))
					{
						player.heal(this.damage * (this.ai[5] == 1 ? 2 : 1));
						this.destroy();
					}
				}
			}
			else if(this.type == 61)
			{
				float b = MathUtils.random(0f, 0.15f);
				Particle p = Particle.Create(this.Center(), this.velocity.cpy().scl(0.1f), 2, new Color(1f, b, b, 1f), 0f, 1f, 1f);
				p.drawBack = true;
				p.setLight(16, new Color(1f, 0.1f, 0.1f, 1f));
				if(this.target != null)
				{
					Vector2 dif = new Vector2(this.target.Center().x - this.Center().x, this.target.Center().y - this.Center().y);
					dif.nor();
					dif.scl(115);
					this.velocity.x *= 0.91f;
					this.velocity.y *= 0.91f;
					this.velocity.add(dif);
				}
				else if(this.target == null || !this.target.active)
				{
					this.destroy();
				}
			}
			else if(this.type == 62)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 2)
				{
					this.currentFrameTicks = 0;
					this.currentFrame++;
					if(this.currentFrame >= this.frames)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 63)
			{
				if(this.owner != null)
				{
					this.position = this.owner.Center().sub(this.width/2f, this.height/2f);
				}
				this.alpha = 1f;
				if(this.timeLeft < 1)
				{
					this.alpha = this.timeLeft;
					this.velocity.y = 20f;
				}
			}
			else if(this.type == 64)
			{
				if(this.target != null)
				{
					this.position = this.target.Center().sub((this.width)/2f, (this.height)/2f);
				}
				this.alpha = 1f;
				if(this.timeLeft < 1)
				{
					this.alpha = this.timeLeft;
				}
			}
			else if(this.type == 65)
			{
				if(this.target != null)
				{
					this.position = this.target.Center().sub((this.width)/2f, (this.height)/2f);
				}
				this.alpha = 1f;
				if(this.timeLeft < 1)
				{
					this.alpha = this.timeLeft;
				}
			}
			else if(this.type == 66)
			{
				Particle p = Particle.Create(this.Center(), this.velocity.cpy().scl(0.1f), 2, new Color(1f, 1f, 1f, 1f), 0f, 1f, 1f);
				p.setLight(32, Color.WHITE);
				if(this.target != null)
				{
					Vector2 dif = new Vector2(this.target.Center().x - this.Center().x, this.target.Center().y - this.Center().y);
					dif.nor();
					dif.scl(90f);
					this.velocity.x *= 0.975f;
					this.velocity.y *= 0.975f;
					this.velocity.add(dif);
				}
				else if(this.target == null || !this.target.active)
				{
					this.destroy();
				}
			}
			else if(this.type == 67)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 1)
				{
					this.currentFrameTicks = 0;
					this.currentFrame++;
					if(this.currentFrame >= this.frames)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 68)
			{
				this.rotation += Math.min(15 + (this.ticks/2), 78);
				
				if(this.ticks % 6 == 0)
				{
					this.resetCollisions();
				}
				if(this.currentFrame == 1)
				{
					for(int i = 0;i < 4;i++)
					{
						float ran = MathUtils.random(360);
						float cos2 = (float)Math.cos((ran) * Math.PI/180f);
						float sin2 = (float)Math.sin((ran) * Math.PI/180f);
						float dis = MathUtils.random(26, 46);
						Particle p = Particle.Create(new Vector2(this.Center().x + cos2 * dis, this.Center().y + sin2 * dis), Vector2.Zero, 2, Color.WHITE, 2f, 1.5f, 1f);
						p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
						Lighting.Create(p.position, 256, new Color(0f, 0.69f, 1f, 1f), 1.5f);
						p.drawFront = true;
					}
				}
				
				if(this.ai[5] != 1)
				{
					this.velocity.scl(0.94f);
					if(this.velocity.len() < 30)
					{
						this.ai[5] = 1;
						this.ticks = 4;
						this.currentFrame = 1;
						this.setLight(256, new Color(0f, 0.69f, 1f, 1f), 50f);
						Main.doEarthquake(20);
						Particle p = Particle.Create(this.Center(), Vector2.Zero, 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 5.5f);
						p.drawFront = true;
						p.frameCounterTicks = 1;
						p.fixAlpha = true;
						p.alpha = 0.5f;
						p = Particle.Create(this.Center(), Vector2.Zero, 11, Color.WHITE, 0f, 1f, 5f);
						p.drawFront = true;
						p.frameCounterTicks = 1;
					}
				}
				else
				{
					this.velocity = new Vector2(this.ticks*10, 0f).rotate(Main.angleBetween(this.Center(), this.owner.Center()));

					/*Vector2 center = player.Center();
					Vector2 dif = new Vector2(center.x - this.Center().x, center.y - this.Center().y);
					dif.nor();
					dif.scl(130);
					this.velocity.x *= 0.9f;
					this.velocity.y *= 0.9f;
					this.velocity.add(dif);*/
					if(this.Center().dst(this.owner.Center()) < 20)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 69)
			{
				if(this.ticks % 12 == 0)
				{
					this.resetCollisions();
				}
				Main.doEarthquake(Math.min(this.ticks/4, 40));
				for(int i = 0;i < 100;i++)
				{
					Particle p = Particle.Create(this.Center().add(MathUtils.random(this.getScaledWidth()/2f), MathUtils.random(-this.getScaledHeight()/2f, this.getScaledHeight()/2f)).rotate(this.rotation), Vector2.Zero, 2, Color.WHITE, 0f, 1f, 2f);
					p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
					Lighting.Create(p.position, 256, new Color(0f, 0.69f, 1f, 1f), 1f);
				}
			}
			else if(this.type == 70)
			{
				for(int i = 0;i < (this.ai[5] == 1 ? 1 : 10);i++)
				{
					Particle p = Particle.Create(this.Center().add(new Vector2(this.getScaledWidth()/2f, 0f).rotate(this.rotation)), new Vector2(MathUtils.random(-120, -80), MathUtils.random(-150, 150)).rotate(this.rotation), 2, Color.WHITE, 0f, 0.5f, 1f);
					p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
				}
			}
			else if(this.type == 71)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero, 8, new Color(0f, 0.69f, 1f, 1f), 0f, 0f, 1f);
				p.rotation = this.rotation;
				p.width = this.width - 8;
				p.height += 8;
				p.alpha = 0.75f;
				p.fixAlpha = true;
				p.drawBack = true;
				p = Particle.Create(this.Center(), Vector2.Zero, 8, Color.WHITE, 0f, 0f, 1f);
				p.rotation = this.rotation;
				p.width = this.width - 16;
				p.drawBack = true;
				for(int i = 0;i < 1;i++)
				{
					p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 18, new Color(0f, 0.69f, 1f, 1f), 0f, 2f, 0.5f);
					p.rotation = this.rotation - 45f + (MathUtils.randomBoolean() ? 180 : 0);
					p.drawFront = true;
				}
				Main.doEarthquake(5);
			}
			else if(this.type == 72)
			{
				for(int i = 0;i < 10;i++)
				{
					Particle p = Particle.Create(this.Center().add(new Vector2(this.getScaledWidth()/2f, 0f).rotate(this.rotation)), new Vector2(MathUtils.random(-120, -80), MathUtils.random(-150, 150)).rotate(this.rotation), 2, Color.WHITE, 0f, 0.5f, 1f);
					p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
				}
			}
			else if(this.type == 73)
			{
				for(int i = 0;i < 2;i++)
				{
					Particle p = Particle.Create(this.Center().add(new Vector2(MathUtils.random(this.getScaledWidth()/2f), 0f).rotate(this.rotation)), new Vector2(MathUtils.random(-120, -80), MathUtils.random(-150, 150)).rotate(this.rotation), 2, Color.WHITE, 0f, 0.5f, 1f);
					p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
				}
			}
			else if(this.type == 74)
			{
				for(int i = 0;i < 2;i++)
				{
					Vector2 pos = this.Center().add(MathUtils.random(-26, 26), MathUtils.random(-36, 36));
					float rotation = MathUtils.random(360f);
					for(int k = 0;k < 2;k++)
					{
						Particle p = Particle.Create(pos, new Vector2(0f, 20f), 2, Color.WHITE, 0f, 1f, 1.25f);
						p.drawFront = true;
						p.rotation = rotation;
						p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
					}
				}
				if(Math.abs(this.velocity.x) > 50)
				{
					for(int i = 0;i < 15;i++)
					{
						Vector2 pos = this.Center().add(new Vector2(this.direction * MathUtils.random(60f, 100f), -8f).rotate(this.rotation));
						Vector2 vel = new Vector2(this.direction * MathUtils.random(-120, -80), MathUtils.random(-300, 300)).rotate(this.rotation);
						float rotation = MathUtils.random(360f);
						for(int k = 0;k < 2;k++)
						{
							Particle p = Particle.Create(pos, vel, 2, Color.WHITE, 0f, 0.3f, 1f);
							p.drawFront = true;
							p.rotation = rotation;
							p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
						}
					}
				}
				if(this.ticks > 30)
				{
					if(this.currentFrame < 8)
					{
						this.currentFrameTicks++;
						if(this.currentFrameTicks > 2)
						{
							this.currentFrame++;
							this.currentFrameTicks = 0;
						}
					}
					else
					{
						this.drawBack = false;
						this.extraUpdates = 3;
						this.rotation = Main.angleBetween(this.Center(), this.owner.Center());
						this.velocity = new Vector2(1000f, 0f).rotate(Main.angleBetween(this.Center(), this.owner.Center()));
						if(this.directionByVelocity() == -1)
						{
							this.rotation += 180;
						}
						if(this.Center().dst(this.owner.Center()) < 10)
						{
							player.removeBuff(51);
							player.customAnim = null;
							player.customAnim2 = null;
							player.setInvincible(0.33f);
							this.destroy();
						}
					}
				}
			}
			else if(this.type == 75)
			{
				for(int i = 0;i < 2;i++)
				{
					Particle p = Particle.Create(this.Center().add(new Vector2(MathUtils.random(this.getScaledWidth()/2f), 0f).rotate(this.rotation)), new Vector2(MathUtils.random(-120, -80), MathUtils.random(-150, 150)).rotate(this.rotation), 2, Color.WHITE, 0f, 0.5f, 1f);
					p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
				}
			}
			else if(this.type == 76)
			{
				if(j == 0)
				{
					for(int i = 0;i < 2;i++)
					{
						Particle p = Particle.Create(this.Center().add(new Vector2(MathUtils.random(this.getScaledWidth()/2f), 0f).rotate(this.rotation)), new Vector2(MathUtils.random(-120, -80), MathUtils.random(-150, 150)).rotate(this.rotation).nor().scl(this.velocity.len()/5f + 100), 2, Color.WHITE, 0f, 0.5f, 1f);
						p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
						p.drawBack = true;
					}
				}
				if(this.Center().y < this.ai[5])
				{
					this.velocity = Vector2.Zero;
					this.rotateByVelocity = false;
				}
				if(this.timeLeft < 1f)
				{
					this.alpha = this.timeLeft;
				}
			}
			else if(this.type == 77)
			{
				if(this.ticks <= 60)
				{
					this.alpha = (this.ticks)/60f;
				}
				else if(this.timeLeft < 1f)
				{
					this.alpha = this.timeLeft;
				}
			}
			else if(this.type == 78 || this.type == 79)
			{
				if(this.ticks > 15 && this.ai[8] > 0)
				{
					Particle p = Particle.Create(this.Center(), Vector2.Zero, 11, new Color(1f, 1f, 0.8f, 1f), 0f, 1f, 3f);
					p.frameCounterTicks = 0;
					this.type = 79;
					this.damage *= 3;
					this.SetDefaults(false);
					this.ai[8] = 0;
				}
				if(this.ticks == 0)
				{
					this.ai[4] = this.velocity.angle();
					this.ai[3] = -90 * (this.flipped ? -1 : 1);
				}
				if(this.Center().dst(this.owner.Center()) > 64)
				{
					this.drawBack = false;
				}
				if(this.ai[2] != 1)
				{
					this.ai[3] += 5;
					if(this.ai[3] >= 90)
					{
						this.ai[2] = 1;
					}
				}
				else
				{
					this.ai[3] -= 5;
					if(this.ai[3] <= -90)
					{
						this.ai[2] = 0;
					}
				}
				float sin = (float)Math.sin(this.ai[3] * Math.PI/180f);
				this.currentFrame = 10-(int) (Math.abs(sin)*10);
				this.velocity = new Vector2(this.velocity.len(), 0f).rotate(this.ai[4] + (this.ai[6] != 1 ? sin * (this.ai[7]*40) : 0));
				
				if(this.type == 79)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 16, Color.WHITE, 0f, 0.5f, 1f);
					p.setLight(32, Color.WHITE);
				}
			}
			else if(this.type == 80 || this.type == 81)
			{
				if(this.Center().y > this.collisionY)
				{
					this.velocity = Vector2.Zero.cpy();
					this.rotateByVelocity = false;
				}
				else
				{
					Main.doEarthquake(10);
				}
				
				if(this.type == 81)
				{
					this.currentFrameTicks++;
					if(this.currentFrameTicks > 2)
					{
						this.currentFrameTicks = 0;
						this.currentFrame = (this.currentFrame+1)%this.frames;
					}
					for(int i = 0;i < 3;i++)
					{
						Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 16, Color.WHITE, 0f, 0.5f, 1f);
						p.setLight(32, Color.WHITE);
						p.drawBack = true;
					}
				}
				
				if(this.timeLeft < 1f)
				{
					this.velocity.y = -1000 * (1-this.timeLeft);
				}
				if(this.timeLeft < 0.25f)
					this.alpha = this.timeLeft*4f;
			}
			else if(this.type == 82)
			{
				if(this.Center().y < this.collisionY && this.ai[5] != 1)
				{
					this.velocity = Vector2.Zero.cpy();
					this.rotateByVelocity = false;
					Main.doEarthquake((int) (5 * this.scale/2f));
					this.ai[5] = 1;
					this.ai[2] = 1;
				}
				if(this.ai[2] == 1)
				{
					this.ai[3]++;
				}
				
				if(this.ai[4] == 1 && this.ai[3] > 120)
				{
					this.currentFrameTicks++;
					Main.doEarthquake(10);
					if(this.currentFrameTicks > 2)
					{
						this.currentFrame = (this.currentFrame+1)%this.frames;
						this.currentFrameTicks = 0;
					}
					if(this.currentFrame == this.frames-1)
					{
						Main.doEarthquake(20);
						this.destroy();
						for(int i = 0;i < 30;i++)
						{
							Vector2 vel = new Vector2(MathUtils.random(-300, 300), MathUtils.random(400, 600));
							Vector2 pos = new Vector2(
									MathUtils.random(this.width * this.scale / 2f),
									MathUtils.random(-this.height * scale / 2f, this.height * this.scale / 2f)).
									rotate(this.rotation)
											.add(this.Center());
							Projectile p = Projectile.Summon(83, pos, vel, 1000, 6f, player);
							p.collisionWaitFrames = 30;
						}
					}
				}
				if(this.timeLeft < 1f && this.ai[4] != 1)
				{
					this.velocity.y = -1000 * (1-this.timeLeft);
					Main.doEarthquake(20);
				}
				
				if(this.timeLeft < 0.25f)
					this.alpha = this.timeLeft*4f;
			}
			else if(this.type == 83)
			{
				this.rotation += 15;
				this.velocity.y -= 600 * deltaTime;
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 2)
				{
					this.currentFrameTicks = 0;
					this.currentFrame = (this.currentFrame+1)%this.frames;
				}
				for(int i = 0;i < 3;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 16, Color.WHITE, 0f, 0.5f, 1f);
					p.setLight(32, Color.WHITE);
					p.drawBack = true;
				}
			}
			else if(this.type == 84 || this.type == 85)
			{
				float sin = (float)Math.sin(this.ticks);
				this.currentFrame = 10-(int) (Math.abs(sin)*10);
				if(this.ticks > 15 && this.ai[8] > 0)
				{
					Particle p = Particle.Create(this.Center(), Vector2.Zero, 11, new Color(1f, 1f, 0.8f, 1f), 0f, 1f, 3f);
					p.frameCounterTicks = 0;
					this.type = 84;
					this.damage *= 3;
					this.SetDefaults(false);
					this.ai[8] = 0;
				}
				Monster m = (Monster)this.target;
				if(m != null && m.health > 0)
				{
					Vector2 center = m.Center();
					Vector2 dif = new Vector2(center.x - this.Center().x, center.y - this.Center().y);
					dif.nor();
					dif.scl(250);
					this.velocity.scl(0.95f);
					this.velocity.add(dif);
				}
				else
				{
					float nextest = 99999f;
					Monster victim = null;
					for(Monster m2 : Constant.getMonstersInRange(this, 300))
					{
						if(this.Center().dst(m2.Center()) < nextest)
						{
							nextest = this.Center().dst(m2.Center());
							victim = m2;
						}
					}
					this.target = victim;
				}
				if(this.type == 84)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 16, Color.WHITE, 0f, 0.5f, 1f);
					p.setLight(32, Color.WHITE);
				}
			}
			else if(this.type == 87)
			{
				if(this.ai[5] == 1)
				{
					this.ai[4]++;
				}
				if(this.ai[4] == 60)
				{
					this.ai[6] = MathUtils.random(1, 4);
					if(Main.var[4] > 0)
						this.ai[6] = Main.var[4];
				}
				else if(this.ai[4] == 120)
				{
					this.ai[7] = MathUtils.random(1, 4);
					if(Main.var[4] > 0)
						this.ai[7] = Main.var[4];
				}
				else if(this.ai[4] == 180)
				{
					this.ai[8] = MathUtils.random(1, 4);
					if(Main.var[4] > 0)
						this.ai[8] = Main.var[4];
				}
				
				if(this.ai[6] > 0)
				{
					Particle p = Particle.Create(this.Center().add(-48, this.getScaledHeight()/2f), Vector2.Zero, 2, Color.WHITE, 0f, Main.ticksToSeconds(2), 1f);
					p.fixAlpha = true;
					p.setCustomTexture(Content.extras[(int) (6+this.ai[6])], 1);
				}
				if(this.ai[7] > 0)
				{
					Particle p = Particle.Create(this.Center().add(0, this.getScaledHeight()/2f), Vector2.Zero, 2, Color.WHITE, 0f, Main.ticksToSeconds(2), 1f);
					p.fixAlpha = true;
					p.setCustomTexture(Content.extras[(int) (6+this.ai[7])], 1);
				}
				if(this.ai[8] > 0)
				{
					Particle p = Particle.Create(this.Center().add(48, this.getScaledHeight()/2f), Vector2.Zero, 2, Color.WHITE, 0f, Main.ticksToSeconds(2), 1f);
					p.fixAlpha = true;
					p.setCustomTexture(Content.extras[(int) (6+this.ai[8])], 1);
					this.ai[9]++;
					
					if((this.ai[6] == this.ai[7] || this.ai[6] == this.ai[8] || this.ai[7] == this.ai[8]) && this.ai[9] == 1)
					{
						Color color = Color.GREEN;
						int quant = 50;
						if(this.ai[6] == this.ai[7] && this.ai[7] == this.ai[8])
						{
							quant = 100;
							color = Color.YELLOW;
						}
						for(int i = 0;i < quant;i++)
						{
							Dummy ent = new Dummy();
							ent.width = 144;
							ent.height = 36;
							ent.position = this.Center().add(-48, this.getScaledHeight()/2f);
							p = Particle.Create(ent.randomHitBoxPosition(), new Vector2(MathUtils.random(-150, 150), MathUtils.random(0, 120)), 2, color, -2f, 2f, 1f);
							p.drawFront = true;
							p.setLight(16, color);
						}
					}
				}
				if(this.ai[9] == 60)
				{
					float majority = 0;
					boolean all2 = false;
					if(this.ai[6] == this.ai[7] && this.ai[7] == this.ai[8])
					{
						all2 = true;
					}
					if(this.ai[6] == this.ai[7])
					{
						majority = this.ai[6];
					}
					else if(this.ai[6] == this.ai[8])
					{
						majority = this.ai[6];
					}
					else if(this.ai[7] == this.ai[8])
					{
						majority = this.ai[7];
					}

					final boolean all = all2;
					final Skill s = player.getSkill(92);

					if(s != null)
					{
						if(majority == 1)
						{
							Projectile p = Projectile.Summon(31, this.Center(), Vector2.Zero, 0, 1f, player);
							p.scale = 2f;
							p.setCustomTexture(Content.extras[11], 1);
							Particle.Create(this.Center(), Vector2.Zero, 22, new Color(1f, 0.25f, 0.25f, 1f), 0f, all ? 6f : 4f, all ? 4f : 2f);
							final Projectile jack = this;
							final Player playerf = player;
							for(int i = 0;i < (all ? 5 : 3);i++)
							{
								final int loop = i;
								Event e = new Event((i+1)*60) {
									@Override
									public void function()
									{
										Particle.Create(jack.Center(), Vector2.Zero, 23, new Color(1f, 0.25f, 0.25f, 1f), 0f, 1f, (all ? 4f : 2f));
										for(Player pl : Constant.getPlayersInRange(jack, all ? 400 : 200))
										{
											if(s != null)
												pl.heal(s.getInfoValueF(playerf, 2));
										}
										if(loop == (all ? 4 : 2))
										{
											jack.ai[3] = 1;
										}
									}
								};
								Main.scheduledTasks.add(e);
							}
						}
						else if(majority == 2)
						{
							Projectile p = Projectile.Summon(88, this.Center(), Vector2.Zero, s.getInfoValueI(player, 1), this.timeLeft, player);
							p.ai[5] = all ? 1 : 0;
							p = Projectile.Summon(31, this.Center(), Vector2.Zero, 0, 1f, player);
							p.scale = 2f;
							p.setCustomTexture(Content.extras[12], 1);
							this.destroy();
						}
						else if(majority == 3)
						{
							Projectile p = Projectile.Summon(31, this.Center(), Vector2.Zero, 0, 1f, player);
							p.scale = 2f;
							p.setCustomTexture(Content.extras[13], 1);
							Particle.Create(this.Center(), Vector2.Zero, 22, new Color(1f, 0f, 0f, 1f), 0f, 4f, all ? 4f : 2f);
							final Projectile jack = this;
							for(int i = 0;i < 3;i++)
							{
								final Player origin = player;
								final int loop = i;
								Event e = new Event((i+1)*60) {
									@Override
									public void function()
									{
										Particle.Create(jack.Center(), Vector2.Zero, 23, new Color(1f, 0f, 0f, 1f), 0f, 1f, (all ? 4f : 2f));
										for(Player pl : Constant.getPlayersInRange(jack, all ? 400 : 200))
										{
											pl.addBuff(55, s.getInfoValueF(origin, 4) * (all ? 7 : 1), origin);
										}
										if(loop == 2)
										{
											jack.ai[3] = 1;
										}
									}
								};
								Main.scheduledTasks.add(e);
							}
						}
						else if(majority == 4)
						{
							Projectile p = Projectile.Summon(31, this.Center(), Vector2.Zero, 0, 1f, player);
							p.scale = 2f;
							p.setCustomTexture(Content.extras[14], 1);
							Particle.Create(this.Center(), Vector2.Zero, 22, new Color(0.1f, 1f, 0.1f, 1f), 0f, 4f, all ? 4f : 2f);
							final Projectile jack = this;
							for(int i = 0;i < 3;i++)
							{
								final Player origin = player;
								final int loop = i;
								Event e = new Event((i+1)*60) {
									@Override
									public void function()
									{
										Particle.Create(jack.Center(), Vector2.Zero, 23, new Color(0.1f, 1f, 0.1f, 1f), 0f, 1f, (all ? 4f : 2f));
										for(Player pl : Constant.getPlayersInRange(jack, all ? 400 : 200))
										{
											pl.buffs[pl.addBuff(54, s.getInfoValueF(origin, 3), origin)].stacks = all ? 2 : 1;
										}
										if(loop == 2)
										{
											jack.ai[3] = 1;
										}
									}
								};
								Main.scheduledTasks.add(e);
							}
						}
						else
						{
							this.ai[3] = 1;
						}
					}
				}
				if(this.ai[3] > 0)
				{
					this.alpha -= 0.05f;
					if(this.alpha < 0)
					{
						this.destroy();
					}
				}
				else
				{
					Main.forceShowOff(0.1f);
				}
			}
			else if(this.type == 88)
			{
				boolean all = this.ai[5] == 1;
				if(this.currentFrame < this.frames-1)
				{
					this.currentFrameTicks++;
					if(this.currentFrameTicks > 1)
					{
						this.currentFrameTicks = 0;
						this.currentFrame = this.currentFrame + 1;
					}
				}
				else
				{
					Main.createExplosion(this.Center(), all ? 400 : 200, all ? 2 : 1, false);
					for(Monster m : Constant.getMonstersInRange(this, all ? 400 : 200))
					{
						m.hurtDmgVar(all ? this.damage*2 : this.damage, Main.directionFromTo(this, m), all ? 2f : 1f, player.canCritical(m), Constant.DAMAGETYPE_FIRE, player);
					}
					Main.doEarthquake(all ? 30 : 15);
					this.destroy();
				}
			}
			else if(this.type == 89)
			{
				for(int i = 0;i < 2;i++)
				{
					float angle = ((this.ticks*5)+i*180)%360;
					float sin = (float)Math.sin(angle*Math.PI/180f);
					Vector2 pos = this.Center().add(new Vector2(0f, sin*32).rotate(this.velocity.angle()));
					Particle p = Particle.Create(pos, Vector2.Zero, 27, Color.CYAN, 0f, 0.5f, 0.5f);
					p.setLight(32, new Color(0.1f, 1f, 1f, 1f));
					p.rotation = this.velocity.angle() + (float)Math.sin(angle*2*Math.PI/180f)*32;
					p = Particle.Create(pos, Vector2.Zero, 4, Color.CYAN, 0f, 0.1f, 1f);
					p.setLight(64, new Color(0.1f, 1f, 1f, 1f));
					p.drawBack = !(angle > 90 && angle < 270);
				}
			}
			else if(this.type == 90)
			{
				Main.forceShowOff(0.5f);
				for(int i = 0;i < 20;i++)
				{
					float r = MathUtils.random(0.04f, 0.07f);
					Color color = new Color(r, r, r, 1f);
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.1f), 6, color, 0f, 0.5f, 1f);
					p.setLight(32, p.color);
					p.drawFront = true;
					p.frameCounterTicks = 0;
				}
				for(int i = 0;i < 20;i++)
				{
					float r = MathUtils.random(0.04f, 0.07f);
					Color color = new Color(r, r, r, 1f);
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.1f), 6, color, 0f, 0.5f, 1f);
					p.setLight(32, p.color);
					p.drawFront = true;
				}
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 1)
				{
					this.currentFrameTicks = 0;
					this.currentFrame = (this.currentFrame+1)%this.frames;
				}
			}
			else if(this.type == 91)
			{
				Color color = new Color(0f, 0.8f, 0.75f, 1f);
				if(this.ticks == 1)
				{
					Particle p = Particle.Create(this.Center(), Vector2.Zero, 22, color, 0f, this.ai[5]+0.5f, 4f);
					p.drawBack = true;
				}
				if(this.ticks % 60 == 0)
				{
					Particle p = Particle.Create(this.Center(), Vector2.Zero, 23, color, 0f, 1f, 4f);
					p.drawBack = true;
				}
				if(this.ticks < 60)
					this.alpha = this.ticks/60f;
				else
					this.alpha = 1f;
				
				this.rotation = 360f * (float)Math.pow(0.9f, this.ticks);
				if(Main.secondsToTicks(this.ai[5]) < this.ticks)
				{
					this.destroy();
				}
			}
			else if(this.type == 92)
			{
				if((this.velocity.len() < 10 && j == 0 && this.ticks % 5 == 0) || (this.velocity.len() >= 10))
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 2, new Color(0f, 0.8f, 1f, 1f), 3f, Math.min(1f, this.timeLeft), 1f);
					p.rotation = MathUtils.random(360f);
					p.setLight(32, new Color(0f, 0.6f, 1f, 1f));
					p.drawBack = true;
				}
				if(this.velocity.len() > 10)
				{
					/*Particle p = Particle.Create(this.Center(), Vector2.Zero, 28, new Color(0.1f, 0.6f, 1f, 1f), 0f, 0f, 1f);
					p.rotation = this.rotation;
					p.width *= 2;
					p.drawBack = true;*/
					if((this.collisionY <= 10 || this.Center().y < this.collisionY))
					{
						if((this.ai[5] == 1 && this.ai[1] > 0) || (this.ai[5] != 1 && Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.Center().x-10, this.Center().y-10, 20, 20)))
						{
							this.velocity = Vector2.Zero.cpy();
							this.rotateByVelocity = false;
							this.timeLeft = 1;
						}
					}
				}
				if(this.timeLeft < 1f)
				{
					this.alpha = this.timeLeft;
				}
			}
			else if(this.type == 93)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks>1 && this.currentFrame<this.frames-1)
				{
					this.currentFrame = this.currentFrame+1;
					this.currentFrameTicks = 0;
				}
				this.scale = 1f+(float) (Math.pow(0.9f, 1+(ticks/1.5f)));
				if(this.ticks<30)
				{
					this.alpha = this.ticks/30f;
				}
				if(this.timeLeft < 1f)
				{
					this.alpha = this.timeLeft;
				}
			}
			else if(this.type == 94)
			{
				this.rotation += 5;
				for(int i = 0;i < 3;i++)
				{
					Color color = new Color(0.8f + (0.2f * (float)this.currentFrame/this.frames), 0.4f + (0.6f * (float)this.currentFrame/this.frames), 0.15f + (0.85f * (float)this.currentFrame/this.frames), 1f);
					Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-32, 32), MathUtils.random(-32, 32)), 24,  color, 0f, 0.5f, 1f);
					//p.setLight(32, new Color(0.8f, 0.4f, 0.15f, 1f));
					p.drawBack = true;
				}
				ArrayList<Monster> monsters = Constant.getMonstersInRange(this, 200);
				ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
				for(Projectile p : Constant.getProjectileList())
				{
					Circle c = new Circle(this.Center(), 400);
					if(p.sameMapAs(this) && Intersector.overlaps(c, p.hitBox()) && p.type == this.type)
					{
						Vector2 center = this.Center().add(p.Center()).scl(0.5f);
						Color color = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE].cpy();
						Particle p2 = Particle.Create(center, Vector2.Zero, 2, color, 0f, 0.1f, 1f);
						p2.height = 2;
						p2.width = (int) this.Center().dst(p.Center());
						p2.rotation = Main.angleBetween(p.Center(), this.Center());
						p2.drawBack = true;
						p2.fixAlpha = true;
						p2.alpha = 0.5f;
						projectiles.add(p);
					}
				}
				if((monsters.size() > 0 || this.ai[6] == 1))
				{
					this.ai[4] = 1;
				}
				
				if(this.ai[4] == 1)
				{
					this.ai[5]++;
				}
				
				if(this.ai[5] > 0)
				{
					this.currentFrameTicks++;
					if(this.currentFrameTicks > 1 && this.currentFrame < this.frames-1)
					{
						this.currentFrameTicks = 0;
						this.currentFrame = this.currentFrame+1;
					}
				}
				for(int i = 0;i < 36;i++)
				{
					float sin = (float)Math.sin((i * 10 + this.ticks) * Math.PI/180);
					float cos = (float)Math.cos((i * 10 + this.ticks) * Math.PI/180);
					Vector2 pos = new Vector2(this.Center().x + cos * 200, this.Center().y + sin * 200);
					Particle p = Particle.Create(pos, Vector2.Zero, 2, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE], 0f, 0.1f, 1f);
					p.rotation = this.ticks * 2;
				}
				if(this.ai[5] >= 60)
				{
					Main.createExplosion(this.Center(), 200, 1f, false);
					Main.doEarthquake(20);
					for(Monster m : monsters)
					{
						m.hurtDmgVar(this.damage, 0, 0f, player.canCritical(m), Constant.DAMAGETYPE_FIRE, player);
					}
					for(Projectile p : projectiles)
					{
						p.ai[6] = 1;
						p.ai[5] = Math.max(30, p.ai[5]);
					}
					this.destroy();
				}
			}
			else if(this.type == 95)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrame = (this.currentFrame + 1) % this.frames;
					this.currentFrameTicks = 0;
				}
				for(int i = 0;i < 2;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.05f), 24,  new Color(0.8f, 0.4f, 0.15f, 1f), 0f, 0.5f, 2f);
					p.setLight(32, new Color(0.8f, 0.4f, 0.15f, 1f));
				}
				
				if(this.target != null && !this.target.active)
				{
					this.target = null;
				}
				
				if(this.target == null)
				{
					float distance = 1500f;
					ArrayList<Monster> poss = new ArrayList<Monster>();
					for(Monster m : Constant.getMonsterList(false))
					{
						if(m.Center().dst(this.Center()) < distance)
						{
							poss.add(m);
						}
					}
					if(poss.size() > 0)
					{
						Collections.shuffle(poss);
						this.target = poss.get(0);
					}
				}
				
				if(this.ticks > 5 && this.target != null)
				{
					Vector2 center = this.target.Center();
					Vector2 dif = new Vector2(center.x - this.Center().x, center.y - this.Center().y);
					dif.nor();
					dif.scl(300);
					this.velocity.x *= 0.9f;
					this.velocity.y *= 0.9f;
					this.velocity.add(dif);
				}
			}
			else if(this.type == 96)
			{
				this.rotation += 3;
				ArrayList<Monster> m2 = Constant.getMonstersInRange(this, 150);
				if(m2.size() > 0)
				{
					Color[] quadcolors = new Color[4];
					quadcolors[0] = new Color(0.95f, 0.975f, 1f, 1f);
					quadcolors[1] = new Color(0.3f, 0.4f, 0.8f, 1f);
					quadcolors[2] = new Color(0.2f, 0.3f, 0.7f, 1f);
					quadcolors[3] = new Color(0.15f, 0.25f, 0.6f, 1f);
					Main.createCustomExplosion(this.Center(), 300, 1, false, quadcolors, quadcolors[2]);
					for(Monster m : Constant.getMonstersInRange(this, 300))
					{
						m.hurtDmgVar(1000, 0, 0f, player.canCritical(m), Constant.DAMAGETYPE_ENERGY, player);
					}
					Main.doEarthquake(30);
					this.destroy();
				}
				
				if(this.ticks > 30 && this.ticks <= 60)
				{
					this.alpha = 1f-(this.ticks-30f)/60f;
				}
				else if(this.timeLeft < 0.5f)
				{
					this.alpha = this.timeLeft;
				}
			}
			else if(this.type == 97)
			{
				Color color = new Color(0.1f, 0.6f, 0.75f, 1f);
				if(this.ticks == 1)
				{
					Particle p = Particle.Create(this.Center(), Vector2.Zero, 22, color, 0f, this.ai[5]+0.5f, 4f);
					p.drawBack = true;
				}
				if(this.ticks % 60 == 0)
				{
					Particle p = Particle.Create(this.Center(), Vector2.Zero, 23, color, 0f, 1f, 4f);
					p.drawBack = true;
				}
				if(this.ticks < 60)
					this.alpha = this.ticks/60f;
				else
					this.alpha = 1f;
				
				this.rotation = 360f * (float)Math.pow(0.9f, this.ticks);
				if(Main.secondsToTicks(this.ai[5]) < this.ticks)
				{
					this.destroy();
				}
				else if(Main.secondsToTicks(this.ai[5]) < this.ticks+60)
				{
					this.alpha = (Main.secondsToTicks(this.ai[5])-this.ticks)/60f;
				}
				
				this.ai[6]--;
			}
			else if(this.type == 98)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrame = (this.currentFrame + 1) % this.frames;
					this.currentFrameTicks = 0;
				}
				for(int i = 0;i < 3;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.05f), 24,  new Color(1f, 0.1f, 0.1f, 1f), 0f, 1f, 2f);
					p.setLight(32, new Color(1f, 0.1f, 0.1f, 1f));
					Lighting.Create(p.position, 128, p.color, 1f);
				}
			}
			else if(this.type == 99)
			{
				if(this.owner != null)
				{
					this.position = owner.Center().sub(this.width/2f, this.height/2f);
				}
				
				if(this.currentFrame < this.frames-1)
				{
					this.currentFrame++;
				}
				this.alpha = Math.min(this.timeLeft, 1f);
			}
			else if(this.type == 100)
			{
				if(j == 0)
				{
					this.velocity.y -= 200 * deltaTime;
				}
				for(int i = 0;i < 2;i++)
				{
					float angle = ((this.ticks*5)+i*180)%360;
					float sin = (float)Math.sin(angle*Math.PI/180f);
					Vector2 pos = this.Center().add(new Vector2(0f, sin*16).rotate(this.velocity.angle()));
					Particle p = Particle.Create(pos, Vector2.Zero, 2, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ENERGY], 0f, 0.3f, 1f);
					p.setLight(32, new Color(0f, 0f, 1f, 1f));
					p.rotation = angle;
					p.drawBack = !(angle > 90 && angle < 270);
				}
			}
			else if(this.type == 101)
			{
				if(j == 0)
				{
					this.velocity.y -= 200 * deltaTime;
				}
				Particle.Create(this.Center(), Vector2.Zero, 4, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_DARKNESS], 0f, 0.3f, 1.5f);
			}
			else if(this.type == 102)
			{
				if(j == 0)
				{
					this.velocity.y -= 200 * deltaTime;
				}
				for(int i = 0;i < 2;i++)
				{
					Particle.Create(this.Center().add(MathUtils.random(-8f, 8f), MathUtils.random(-8f, 8f)), Vector2.Zero, 10, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_NATURE], 0f, 0.3f, 1.5f);
				}
			}
			else if(this.type == 103)
			{
				if(j == 0)
				{
					this.velocity.y -= 200 * deltaTime;
				}
				for(int i = 0;i < 2;i++)
				{
					Particle.Create(this.Center().add(MathUtils.random(-8f, 8f), MathUtils.random(-8f, 8f)), Vector2.Zero, 16, new Color(1f, 0.15f, 0.15f, 1f), 0f, 0.3f, 1f);
				}
			}
			else if(this.type == 104)
			{
				if(j == 0)
				{
					this.velocity.y -= 200 * deltaTime;
				}
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrame = (this.currentFrame + 1) % this.frames;
					this.currentFrameTicks = 0;
				}
				Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.05f), 24,  new Color(0.8f, 0.4f, 0.15f, 1f), 0f, 1f, 1f);
				p.setLight(32, new Color(0.8f, 0.4f, 0.15f, 1f));
			}
			else if(this.type == 105)
			{
				if(j == 0)
				{
					this.velocity.y -= 200 * deltaTime;
				}
				this.rotation -= 3;
				Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.05f), 24,  Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ICE], 0f, 1f, 1f);
				p.setLight(32, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ICE]);
			}
			else if(this.type == 106)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 12, new Color(1f, 0.8f, 0.2f, 1f), 0f, 1f, 0.25f);
				p.setLight(16, p.color);
			}
			else if(this.type == 107)
			{
				this.alpha = 0.5f;
				if(this.timeLeft < 0.5f)
				{
					this.alpha = this.timeLeft;
				}
				if(this.ticks % 3 == 0)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-1f), 2, new Color(0f, 0f, 0f, 1f), 0f, 1f, 1f);
					p.setLight(16, new Color(0.5f, 0f, 0.5f, 1f));
				}
			}
			else if(this.type == 109)
			{
				if(this.ai[5] <= 0)
					this.ai[5] = Constant.gameTick();
				
				this.ai[5]++;
				if(this.ticks % 3 == 0)
				{
					float h = (this.ai[5]) % 360;
					Color color = new Color(1f, 1f, 1f, 1f).fromHsv(h, 1f, 1f);
					Particle p = Particle.Create(this.Center(), Vector2.Zero, 8, Color.WHITE.cpy().fromHsv(h, 0.6f, 1f), 0f, 0f, 1f);
					p.drawBack = true;
					p.rotation = this.rotation;
					for(int i = 0;i < 5;i++)
					{
						p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.5f), 2, new Color(1f,1f,1f,0f), 0f, 0.5f, 1f);
						p.setLight(32, color);
					}
				}
			}
			else if(this.type == 110)
			{
				if(this.ai[5] <= 0)
					this.ai[5] = Constant.gameTick();
				
				this.ai[5]++;
				if(this.ticks % 3 == 0)
				{
					float h = (this.ai[5]) % 360;
					Color color = new Color(1f, 1f, 1f, 1f).fromHsv(h, 1f, 1f);
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-1f).setLength(100f), 2, Color.WHITE, 0f, MathUtils.random(0.5f, 0.8f), MathUtils.random(1f, 1.5f));
					p.drawBack = true;
					p.rotation = MathUtils.random(360f);
					p.setCustomTexture(Content.particles[31], 1);
					for(int i = 0;i < 3;i++)
					{
						p = Particle.Create(this.Center().add(MathUtils.random(-4, 4), MathUtils.random(-4, 4)), this.velocity.cpy().scl(-0.5f), 2, new Color(1f,1f,1f,0f), 0f, 0.8f, 1f);
						p.setLight(24, color);
					}
				}
				if(j == this.extraUpdates-1)
				{
					if(this.target == null || !this.target.active || !this.canCollideWith((Monster)this.target))
					{
						Monster nextest = null;
						float nextestDist = 600f;
						for(Monster m : Constant.getMonstersInRange(this, nextestDist))
						{
							if(this.canCollideWith(m) && m.Center().dst(this.Center()) < nextestDist)
							{
								nextestDist = m.Center().dst(this.Center());
								nextest = m;
							}
						}
						this.target = nextest;
					}
					if(this.target == null || !this.target.active || !this.canCollideWith((Monster)this.target))
					{
						//this.destroy();
					}
					else
					{
						this.velocity = this.velocity.cpy().setAngle(Main.angleBetween(this.Center(), this.target.Center()));
						this.ai[6] = 1;
					}
				}
				if(this.ticks > 500) {
					this.destroy();
				}
			}
			else if(this.type == 111)
			{
				if(this.ai[5] <= 0)
					this.ai[5] = Constant.gameTick();
				
				this.ai[5]++;
				if(this.ticks % 5 == 0)
				{
					float h = (this.ai[5]) % 360;
					Color color = new Color(1f, 1f, 1f, 1f).fromHsv(h, 1f, 1f);
					Particle p = Particle.Create(this.Center(), Vector2.Zero, 8, Color.WHITE.cpy().fromHsv(h, 0.6f, 1f), 0f, 0f, 1f);
					p.drawBack = true;
					p.rotation = this.rotation;
					for(int i = 0;i < 5;i++)
					{
						p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.5f), 2, new Color(1f,1f,1f,0f), 0f, 0.5f, 1f);
						p.setLight(32, color);
					}
				}
				if(this.ticks >= 100)
				{
					this.destroy();
				}
			}
			else if(this.type == 112)
			{
				Vector2 dest = new Vector2(this.ai[7], this.ai[8]);
				if(this.Center().dst(dest) < 40 || this.ticks > 100000)
				{
					this.destroy();
				}
				
				for(int i = 0;i < 2;i++)
				{
					Vector2 vel = this.velocity.cpy().scl(-1f).nor().scl(MathUtils.random(50f, 150f));
					Particle p = Particle.Create(this.randomHitBoxPosition().add(MathUtils.random(-10, 10), MathUtils.random(-10, 10)), vel, 2, new Color(0f, 0f, 0f, 0f), 0f, 0.5f, 1f);
					p.rotation = this.velocity.angle();
					p.setLight(32, Color.PURPLE);
				}
			}
			else if(this.type == 114)
			{
				this.alpha = 1f-(float)Math.pow(0.9f, 1+this.ticks*1.5f);
				this.currentFrameTicks++;
				if(this.currentFrame < this.frames-1 && this.currentFrameTicks >= 2)
				{
					this.currentFrame++;
					this.currentFrameTicks = 0;
					if(this.currentFrame == 6)
					{
						if(this.currentFrame == 6)
						{
							player.forceCritical();
							for(Monster m : Constant.getMonstersInArea(this.myMapX, this.myMapY, this.Center().x - (this.mirrored ? 256 : 0), this.position.y, 256, 512))
							{
								m.hurtDmgVar(damage, this.mirrored ? -1 : 1, 2.5f, player.canCritical(1, m), Constant.DAMAGETYPE_DEATH, player);
							}
							player.restoreCritical();
						}
						Main.doEarthquake(20);
						int extra = this.mirrored ? - 200: 200;
						for(int i = -50 + extra;i <= 50 + extra;i++)
						{
							try
							{
								Vector2 pos = new Vector2(player.Center().x,player.position.y - 8).add(i + MathUtils.random(-4, 4),  MathUtils.random(-16, 16));
								GameMap g = Constant.getPlayerMap(player.whoAmI);
								Color color = g.map[Main.clamp(0, (int)Math.floor((pos.y/64f)-1), g.height-1)][Main.clamp(0, (int)Math.floor((pos.x/64f)), g.width-1)].color;
								Particle p = Particle.Create(pos, new Vector2(MathUtils.random(0, 50), MathUtils.random(100, 400)), 2, color, -3f, 3f, 1f);
								p.drawBack = true;
							}
							catch(Exception ex)
							{continue;}
						}
					}
				}
				if(this.timeLeft < 0.5f)
				{
					this.alpha = this.timeLeft*2f;
				}
				if(this.currentFrame < 6)
				{
					this.position = player.Center().sub(this.width/2f, this.height/2f);
				}
			}
			else if(this.type == 115)
			{
				this.velocity.scl(0.99f);
				if(this.currentFrame < this.frames-1)
				{
					this.currentFrameTicks++;
					if(this.currentFrameTicks > 2)
					{
						this.currentFrame++;
						this.currentFrameTicks = 0;
					}
				}
				else
				{
					this.alpha -= 0.04f;
					if(this.alpha <= 0f)
					{
						this.destroy();
					}
				}
			}
			else if(this.type == 116)
			{
				this.velocity.y -= 500f * deltaTime;
				if(Constant.gameTick() % 2 == 0)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.2f), 2, new Color(), 0f, 0.5f, 1f);
					p.setLight(32, this.lightColor);
				}
			}
			else if(this.type == 117)
			{
				this.rotation += 30f + this.ticks/3;
				if(this.target != null && this.target.active && this.ai[5] != 1)
				{
					try
					{
						Vector2 center = this.target.Center().add(175 * (((Projectile)this.target).mirrored ? 1 : -1), 0f);
						this.velocity = center.cpy().sub(this.Center()).scl(12f);
					} catch(Exception ex) {}
				}
				if(this.ticks % 5 == 0)
					this.resetCollisions();
			}
			else if(this.type == 118)
			{
				if(this.ticks < 15)
				{
					this.alpha = Main.ticksToSeconds(this.ticks) * 2;
				}
				
				if(this.ticks == 15)
				{
					this.alpha = 1f;
				}
				if(this.ticks == 25)
				{
					if(this.target != null && this.target.active)
					{
						try
						{
							((Projectile)this.target).destroy();
						} catch(Exception ex) {}
					}
				}
				if(this.ticks >= 25)
				{
					if(this.currentFrame < this.frames-1-4)
					{
						this.currentFrameTicks++;
						if(this.currentFrameTicks > 1)
						{
							this.currentFrame++;
							this.currentFrameTicks = 0;
							if(this.currentFrame == 1)
							{
								Main.doEarthquake(10);
							}
							else if(this.currentFrame == 11)
							{
								this.resetCollisions();
								Main.doEarthquake(25);
							}
						}
					}
					
					if(this.currentFrame >= 15)
					{
						this.alpha -= 0.05f;
						this.timeLeft = this.alpha;
						if(this.alpha <= 0f)
						{
							this.destroy();
						}
					}
				}
				Main.forceShowOff(0.1f);
			}
			else if(this.type == 119)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 1)
				{
					this.currentFrame = (this.currentFrame + 1) % this.frames;
					this.currentFrameTicks = 0;
				}
				Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 15, new Color(0f, 0.75f, 0f, 0.75f), 0f, 1f, MathUtils.random(0.5f, 0.75f));
			}
			else if(this.type == 120)
			{
				this.velocity.y -= 250f * deltaTime;
				if(this.ticks % (this.haveBuff(72) != -1 ? 3 : 5) == 0)
				{
					Color color = new Color(1f, 0f, 0f, 1f);
					if(MathUtils.randomBoolean(1/3f))
					{
						color = MathUtils.randomBoolean() ? new Color(1f, 1f, 0f, 1f) : new Color(0f, 0f, 1f, 1f);
					}
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.1f), 14, color, -4f, 1f, 0.5f);
					p.loseSpeed = true;
					p.loseSpeedMult = 0.9f;
					p.rotation = MathUtils.random(360f);
				}
			}
			else if(this.type == 121)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 1)
				{
					this.currentFrame = (this.currentFrame + 1) % this.frames;
					this.currentFrameTicks = 0;
				}
				Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.2f), 2, new Color(0.1f, 0.1f, 0.1f, 1f), 0f, 1f, 1f).setLight(24, new Color(0.1f, 0.1f,0.1f, 1f)).drawBack = true;
			}
			else if(this.type == 122)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 1)
				{
					this.currentFrame = (this.currentFrame + 1) % this.frames;
					this.currentFrameTicks = 0;
				}
				if(this.ticks % 3 == 0)
					Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.2f), 2, new Color(0.1f, 0.1f, 0.1f, 1f), 0f, 1f, 1f).setLight(24, new Color(0.1f, 0.1f,0.1f, 1f)).drawBack = true;
			}
			else if(this.type == 123)
			{
				this.rotation += 35f;
				this.velocity.y -= 500 * deltaTime;
			}
			else if(this.type == 124)
			{
				Particle.Create(this.Center(), Vector2.Zero, 8, Color.WHITE, 0f, 0.5f, this.scale/2f).rotation = this.rotation;
				Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.2f), 16, new Color(0.92f, 0.96f, 0.6f, 1f), 0f, 1f, 1f).setLight(32, new Color(0.92f, 0.96f, 0.6f, 1f));
				this.currentFrameTicks++;
				if(this.currentFrameTicks > 1)
				{
					this.currentFrame = (this.currentFrame+1)%this.frames;
					this.currentFrameTicks=0;
				}
			}
			else if(this.type == 125)
			{
				this.flipped = this.directionByVelocity() == -1;
				this.rotation -= 33;
				for(int i = 0;i < 5;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 2, new Color(0f, 0f, 0f, 0f), 0f, 0.5f, 1f);
					p.setLight(64, Color.PURPLE);
				}
			}
			else if(this.type == 126)
			{
				Particle.Create(this.Center(), Vector2.Zero, 16, new Color(0.92f, 0.96f, 0.8f, 1f), 0f, 0.5f, 1f).setLight(16, new Color(0.92f, 0.96f, 0.8f, 1f));
			}
			else if(this.type == 127)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks> 1)
				{
					this.currentFrame = (this.currentFrame+1)%this.frames;
					this.currentFrameTicks = 0;
				}
				if(this.timeLeft < 1f)
				{
					this.position.y -= 1f;
				}
			}
			else if(this.type == 128)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 2)
				{
					this.currentFrame = (this.currentFrame + 1) % this.frames;
					this.currentFrameTicks = 0;
				}
				for(int i = 0;i < 3;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.05f), 24,  new Color(0.5f, 0f, 0.5f, 1f), 0f, 1f, 2f);
					p.setLight(32, new Color(0.8f, 0.4f, 0.15f, 1f));
					Lighting.Create(p.position, 128, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_DARKNESS], 1f);
				}
			}
			else if(this.type == 129)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.05f), 24,  new Color(0.5f, 0f, 0.5f, 1f), 0f, 1f, 2f);
				p.setLight(32, new Color(0.8f, 0.4f, 0.15f, 1f));
				Lighting.Create(p.position, 128, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_DARKNESS], 1f);
			}


			if(this.type == 32 && this.ai[5] != 1)
			{
				Projectile p = Projectile.Summon(32, new Vector2(this.position.x + this.width/2f, this.position.y + this.height/2f), Vector2.Zero.cpy(), 0, 0.5f, player);
				p.ai[5] = 1;
			}
			else if(this.type == 35)
			{
				for(int i = 0;i < 35;i++)
				{
					float range = MathUtils.random(0, 225);
					float angle = (float) (MathUtils.random() * Math.PI * 2f);
					float sin = (float) Math.sin(angle);
					float cos = (float) Math.cos(angle);
					Vector2 pos = new Vector2(cos * range, sin * range).add(this.Center());

					float fc = MathUtils.random(0.2f, 0.5f);
					Color color = new Color(fc, 0f, fc, 1f);
					Particle p = Particle.Create(pos, Vector2.Zero.cpy(), 6, color, 0f, 1f, 1f);
					p.drawBack = true;
				}
			}
			else if(this.type == 36)
			{
				this.velocity.y -= 400 * deltaTime;
			}
			this.ticks++;
			if(timestopped)
			{
				this.position = originalPosition;
				this.velocity = originalVelocity;
				this.rotation = originalRotation;
				this.currentFrame = originalFrame;
			}
		}
		if(this.rotateByVelocity)
		{
			this.rotation = (float)Math.toDegrees((Math.atan2(this.velocity.y, this.velocity.x)));
		}

	}

	public void resetCollisions()
	{
		this.alreadyCollided.clear();
	}

	private void CollidePlayer(Player player)
	{
		if(player.haveBuff(17) != -1)
		{
			player.removeBuff(17);
			player.invincible = 1.5f;
			return;
		}
		if(this.type == 4)
		{
			player.hurt2(this.damage, (player.Center().x < this.Center().x ? -1 : 1), 1f, this.owner);
			this.destroy();
		}
		else if(this.type == 19)
		{
			player.hurt2(this.damage, Main.directionFromTo(this.Center(), player.Center()), 1f, this.owner);
			this.destroy();
		}
		else if(this.type == 20)
		{
			player.hurt2(this.damage, Main.directionFromTo(this.Center(), player.Center()), 1f, this.owner);
			this.destroy();
		}
		else if(this.type == 21)
		{
			player.hurt2(this.damage, Main.directionFromTo(this.Center(), player.Center()), 1f, this.owner);
			this.destroy();
		}
		else if(this.type == 22)
		{
			player.hurt2(this.damage, Main.directionFromTo(this.Center(), player.Center()), 1f, this.owner);
			this.destroy();
		}
		else if(this.type == 121)
		{
			player.hurt2(this.damage, Main.directionFromTo(this.Center(), player.Center()), 1f, this.owner);
			this.destroy();
		}
		else if(this.type == 127)
		{
			player.hurt2(this.damage, Main.directionFromTo(this.Center(), player.Center()), 1f, this.owner);
		}
		else if(this.type == 128)
		{
			player.hurt2(this.damage, Main.directionFromTo(this.Center(), player.Center()), 1f, this.owner);
			Color[] color = new Color[4];
			color[0] = new Color(1f, 1f, 1f, 1f);
			color[1] = new Color(0.8f, 0.4f, 0.8f, 1f);
			color[2] = new Color(0.5f, 0.2f, 0.5f, 1f);
			color[3] = new Color(0.35f, 0.05f, 0.35f, 1f);
			Main.createCustomExplosion(this.Center(), 150, 1f, false, color, color[3]);
			Main.doEarthquake(15);
			this.destroy();
		}
		else if(this.type == 129)
		{
			player.hurt2(this.damage, Main.directionFromTo(this.Center(), player.Center()), 1f, this.owner);
			Color[] color = new Color[4];
			color[0] = new Color(1f, 1f, 1f, 1f);
			color[1] = new Color(0.8f, 0.4f, 0.8f, 1f);
			color[2] = new Color(0.5f, 0.2f, 0.5f, 1f);
			color[3] = new Color(0.35f, 0.05f, 0.35f, 1f);
			Main.createCustomExplosion(this.Center(), 200, 1f, false, color, color[3]);
			Main.doEarthquake(20);
			this.destroy();
		}
	}

	public static Projectile Summon(int iType, Vector2 vPos, Vector2 vVel, int pdamage, float timeLeft2, Entity owner)
	{
		if(AHS.isUp || !Main.isOnline)
		{
			Projectile p = new Projectile();
			p.type = iType;
			p.owner = owner;
			p.SetDefaults();
			p.timeLeft = timeLeft2;
			p.position = vPos.cpy().sub(new Vector2(p.width/2, p.height/2));
			p.velocity = vVel.cpy();
			p.damage = pdamage;
			p.active = true;
			p.ticks = 0;
			p.rotation = 0f;

			p.resetCollisions();
			for(int i = 0;i < 10;i++)
			{
				p.lastPosition[i] = new Vector2();
			}
			p.recent = true;
			Constant.getProjectileExList().add(p);
			p.whoAmI = Constant.getProjectileExList().indexOf(p);
			p.myMapX = owner.myMapX;
			p.myMapY = owner.myMapY;
			if(p.summonAudio != null)
			{
				DJ.playSound(p.summonAudio, 1f, p.summonPitch, p.Center());
			}
			return p;
		}
		return new Projectile();
	}

	public void Collide(final Monster victim, Entity owner2)
	{
		if(!this.active)
			return;

		Player player2 = null;
		Item item2 = null;
		if(owner2.getClass() == Player.class)
		{
			player2 = (Player)owner2;
			if(player2.inventory[Constant.ITEMSLOT_LEFT] != null && player2.inventory[Constant.ITEMSLOT_LEFT].shootProj == this.type)
				item2 = player2.inventory[Constant.ITEMSLOT_LEFT];
			else if(player2.inventory[Constant.ITEMSLOT_RIGHT] != null && player2.inventory[Constant.ITEMSLOT_RIGHT].shootProj == this.type)
				item2 = player2.inventory[Constant.ITEMSLOT_RIGHT];
		}

		final Player player = player2;
		final Item item = item2;

		if(victim != null)
		{
			if(player != null)
			{
				if(this.haveBuff(31) != -1)
				{
					int count = 0;
					if(this.owner instanceof Player)
					{
						if(player.getSkill(39) != null)
						{
							count = (int) player.getSkill(39).getInfoValueI(player, 0);
						}
					}
					for(int i = 0;i < count;i++)
					{
						float len = this.velocity.len();
						Vector2 pos = this.Center().cpy().add(MathUtils.random(-500, 500), 1000 + i * 200);
						float angle = Main.angleBetween(pos, this.Center());
						Vector2 vel = new Vector2(len, 0).rotate(angle + MathUtils.random(-7.5f, 7.5f));
						Projectile p = Projectile.Summon(this.type, pos, vel, this.damage, 5f, this.owner);
						p.collisionY = this.Center().y + this.height * 2f;
						p.setTarget(victim);
					}
					this.removeBuff(31);
				}
				
				if(this.haveBuff(32) != -1)
				{
					this.removeBuff(32);
					Projectile p = Projectile.Summon(29, victim.Center().add(0, 200), Vector2.Zero, 0, 5f, this.owner);
					if(MathUtils.randomBoolean())
						p.mirrored = true;

					Skill s = player.getSkill(40);
					float dmg = 0f;
					if(s != null)
					{
						dmg = s.getInfoValueF(player, 0);
					}
					victim.hurtDmgVar((int) dmg, 0, 0f, false, Constant.DAMAGETYPE_ENERGY, player);
					Main.doEarthquake(15);
				}
				
				if(this.type != 126 && player.haveBuff(76) != -1 && player.haveBuff(77) == -1)
				{
					Skill s = player.getSkill(128);
					if(s != null)
					{
						for(int i = 0;i < 4;i++)
						{
							Vector2 pos = owner.Center().add(new Vector2(100f, 0f).setAngle(i * 90 + Constant.gameTick()));
							Projectile.Summon(126, pos, new Vector2(1000f, 0f).setAngle(Main.angleBetween(pos, victim.Center())), s.getInfoValueI(player, 0), 5f, player).setTarget(victim).destroyOnTargetLoss = true;
						}
						player.addBuff(77, 0.1f, player);
					}
				}
			}

			if(this.haveBuff(6) !=- 1 && (this.projectileclass == Constant.PROJECTILETYPE_ARROW || this.projectileclass == Constant.PROJECTILETYPE_BULLET))
			{
				if(player != null)
				{
					Skill s = player.getSkill(6);
					if(s != null)
						damage *= (1f + s.getInfoValueF(player, 0)/100f);
				}
			}


			if(this.type == 1)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_RANGED, player);
				this.destroy();
			}
			else if(this.type == 2)
			{
				for(int i = 0;i < 72;i++)
				{
					float sin = (float)Math.sin(Math.PI / 180 * i * 5);
					float cos = (float)Math.cos(Math.PI / 180 * i * 5);
					for(int j = 0;j < 4;j++)
					{
						Particle.Create(victim.Center(), new Vector2(cos * MathUtils.random(100f, 255f), sin * MathUtils.random(100f, 255f)), 2, Color.CYAN, 0f, 1.5f, 1f);
					}
				}
				victim.hurtDmgVar(player.inventory[Constant.ITEMSLOT_LEFT].damage, (this.Center().x > victim.Center().x ? -1 : 1), 1f, player.canCritical(victim), Constant.DAMAGETYPE_ICE, Constant.ITEMCLASS_MAGIC, player);
			}
			else if(this.type == 3 && this.canCollideWith(victim))
			{
				double mult = (this.velocity.len() * this.extraUpdates) / 200;
				if(mult < 1)
					mult = 1f;

				this.addCollision(victim);
				victim.hurtDmgVar((int)(player.attackSpeed * damage * mult * (11 - this.timeLeft)), (this.Center().x > victim.Center().x ? -1 : 1), 1f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_RANGED, player);
			}
			else if(this.type == 5)
			{
				boolean crit = player.canCritical(victim);
				victim.hurtDmgVar(damage, (this.Center().x > victim.Center().x ? -1 : 1), 1f, crit, Constant.DAMAGETYPE_HOLY, Constant.ITEMCLASS_RANGED, player);
				for(int i = 0;i < 72;i++)
				{
					Color color = new Color(0f,0.5f + MathUtils.random()*0.3f,1f,1);
					float force = MathUtils.random(400, 550);
					Particle.Create(victim.Center(), new Vector2(MathUtils.random() * force, MathUtils.random() * force), 2, color, 0f, 1.5f, 1f + MathUtils.random());
				}
				this.addCollision(victim);
			}
			else if(this.type == 6)
			{
				victim.hurtDmgVar((int) (damage*0.5f), (this.Center().x > victim.Center().x ? -1 : 1), 1f, false, Constant.DAMAGETYPE_HOLY, Constant.ITEMCLASS_RANGED, player);
			}
			else if(this.type == 7)
			{
				boolean killed = victim.hurtDmgVar((int)(damage), (this.Center().x > victim.Center().x ? -1 : 1), 1f, player.canCritical(victim), Constant.DAMAGETYPE_DARKNESS, player);
				for(int i = 0;i < 200;i++)
				{
					float val = MathUtils.random()*0.5f;
					Color color = new Color(val, 0f, val, 1f);
					Vector2 vel = new Vector2(MathUtils.random(500, 1500), MathUtils.random(-100, 400));
					if(this.Center().x > victim.Center().x)
						vel.x *= -1f;
					Particle p = Particle.Create(this.Center(), vel, 6, color, -10f, 1.5f, (MathUtils.random()+1f)*0.75f);
					p.drawFront = true;
				}
				if(killed)
				{
					Skill s = player.getEquippedSkill(3);
					if(s != null)
						s.setCooldown(0f);
				}
			}
			else if(this.type == 8)
			{
				for(int i = 0;i < 36;i++)
				{
					float sin = (float)(Math.sin(i * 10 * Math.PI/180));
					float cos = (float)(Math.cos(i * 10 * Math.PI/180));
					float size = Math.max(victim.width, victim.height);
					Particle p = Particle.Create(new Vector2(cos * size + MathUtils.random(-size*0.05f,size*0.05f), sin * size + MathUtils.random(-size*0.05f,size*0.05f)), Vector2.Zero, 2, Color.RED, 0f, 0.1f, Math.max(1f, size/100f));
					p.position.add(victim.Center());
					p.setLight(32, p.color);
					p.parent = victim;
				}
				victim.hurtDmgVar(damage, 0, 0f, player.canCritical(victim), Constant.DAMAGETYPE_DARKNESS, player.inventory[Constant.ITEMSLOT_LEFT].itemclass, player);
				player.heal(damage * 0.1f);
			}
			else if(this.type == 9)
			{
				int extra = 0;
				int ss = this.haveBuff(5);
				if(ss != -1)
				{
					extra = (int) (damage*(this.buffs[ss].stacks/100f));
					player.removeBuff(5);
					Vector2 pos = new Vector2(this.position.x + this.width, this.position.y + this.height/2);
					pos.sub(this.Center());
					pos.rotate(this.rotation);
					pos.add(this.Center());
					Particle p = Particle.Create(pos, Vector2.Zero, 9, new Color(0.9f, 0.75f, 0f, 1f), 0f, 0.5f, 5f);
					p.rotation = MathUtils.random(360);
				}
				victim.hurtDmgVar(damage + extra, (player.Center().x > victim.Center().x ? -1 : 1), 1f, player.canCritical(victim), Constant.DAMAGETYPE_HOLY, Constant.ITEMCLASS_RANGED, player);
			}
			else if(this.type == 10)
			{
				int spelldmg = 100;
				victim.hurtDmgVar(spelldmg, (player.Center().x > victim.Center().x ? -1 : 1), 1f, player.canCritical(victim), Constant.DAMAGETYPE_FIRE, player);
			}
			else if(this.type == 11)
			{
				victim.hurtDmgVar(damage, (player.Center().x > victim.Center().x ? -1 : 1), 0.25f, player.canCritical(victim), Constant.DAMAGETYPE_DARKNESS, Constant.ITEMCLASS_RANGED, player);
			}
			else if(this.type == 13)
			{
				victim.hurtDmgVar(damage, (player.Center().x > victim.Center().x ? -1 : 1), 0.25f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, Constant.ITEMCLASS_RANGED, player);
			}
			else if(this.type == 14)
			{
				int stacks = victim.getBuffStacks(12);
				if(stacks < 3)
				{
					victim.hurtDmgVar(damage, (player.Center().x > victim.Center().x ? -1 : 1), 0.25f, false, Constant.DAMAGETYPE_PHYSICAL, player);
					victim.addBuff(12, 3f, player);
				}
			}
			else if(this.type == 15)
			{
				if(this.currentFrame >= 15 && this.currentFrame <= 18 && this.canCollideWith(victim))
				{
					victim.hurtDmgVar(damage, (player.Center().x > victim.Center().x ? -1 : 1), 0.25f, player.canCritical(victim), Constant.DAMAGETYPE_AIR, player);
					this.addCollision(victim);
				}
			}
			else if(this.type == 17)
			{
				if(this.canCollideWith(victim))
				{
					this.addCollision(victim);
					victim.hurtDmgVar(damage, (player.Center().x > victim.Center().x ? -1 : 1), 1f, player.canCritical(victim), Constant.DAMAGETYPE_FIRE, Constant.ITEMCLASS_HEAVYAXE, player);
					for(int i = 0;i < 20;i++)
					{
						Particle p = Particle.Create(victim.randomHitBoxPosition(), new Vector2((player.Center().x > victim.Center().x ? -1 : 1) * MathUtils.random(400f, 500f), MathUtils.random(-100, 200)), 2, new Color(1f, MathUtils.random()/2f + 0.5f, 0f, 1f), -2f, 2f, MathUtils.random(0.75f, 1.25f));
						p.setLight(16, p.color);
					}
				}
			}
			else if(this.type == 18)
			{
				if(this.canCollideWith(victim))
				{
					this.addCollision(victim);
					victim.hurtDmgVar((int) (damage * 1.5f), (player.Center().x > victim.Center().x ? -1 : 1), 1f, player.canCritical(victim), Constant.DAMAGETYPE_FIRE, Constant.ITEMCLASS_HEAVYAXE, player);
					for(int i = 0;i < 60;i++)
					{
						Particle p = Particle.Create(victim.randomHitBoxPosition(), new Vector2((player.Center().x > victim.Center().x ? -1 : 1) * MathUtils.random(400f, 500f), MathUtils.random(-100, 200)), 2, new Color(1f, MathUtils.random()/2f + 0.5f, 0f, 1f), -2f, 2f, MathUtils.random(0.75f, 1.25f));
						p.setLight(16, p.color);
					}
				}
			}
			else if(this.type == 23)
			{
				if(this.canCollideWith(victim))
				{
					this.addCollision(victim);
					victim.hurt(damage, Main.directionFromTo(victim.Center().x, player.Center().x), 1f, false, Constant.DAMAGETYPE_PHYSICAL, player);
					Projectile p2 = Projectile.Summon(67, victim.Center(), Vector2.Zero, 0, 5f, player);
					p2.rotation = this.velocity.angle()+(this.ai[5] % 2 == 0 ? 20 : -20);
					p2.scale = 2f;
					Particle p3 = Particle.Create(victim.Center(), Vector2.Zero, 26, Color.WHITE, 0f, 1f, 1f);
					p3.rotation = this.velocity.angle()+MathUtils.random(-30, 30);
					p3.scale = 4f;
					this.flipped = (this.ai[5] % 2 == 0);
					this.ai[5]++;
					Main.doEarthquake(15);
				}
			}
			else if(this.type == 24)
			{
				if(this.canCollideWith(victim))
				{
					this.addCollision(victim);
					if(this.ai[5] != 1)
						this.ai[7] = 1;
					victim.hurtDmgVar(damage, Main.directionFromTo(this, victim), 1f, false, Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_RANGED, player);
				}
			}
			else if(this.type == 25)
			{
				if(this.canCollideWith(victim) && this.currentFrame == 0)
				{
					this.addCollision(victim);
					victim.hurtDmgVar(damage, (this.mirrored ? -1 : 1), 1f, false, Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_SHORTSWORD, player);
					victim.hurtDmgVar(damage, (this.mirrored ? -1 : 1), 1f, false, Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_SHORTSWORD, player);
				}
			}
			else if(this.type == 27)
			{
				victim.hurtDmgVar(damage, 0, 0f, false, Constant.DAMAGETYPE_PHYSICAL, player);
			}
			else if(this.type == 28)
			{
				if(this.canCollideWith(victim) && this.velocity.len() > 10)
				{
					this.addCollision(victim);
					victim.hurtDmgVar(damage, 0, 0f, false, Constant.DAMAGETYPE_BLOOD, player);
					victim.Stun(2f, player);
				}
			}
			else if(this.type == 30)
			{
				if(this.canCollideWith(victim))
				{
					this.addCollision(victim);
					float dmgMult = 0f;
					Skill s = player.getSkill(25);
					if(s != null)
						dmgMult = 1f + victim.getLostHealth(true) * (s.getInfoValueF(player, 1)/100f);
					
					victim.hurtDmgVar((int) (this.damage * dmgMult), 0, 0f, false, Constant.DAMAGETYPE_AIR, player);
				}
			}
			else if(this.type == 36)
			{
				if(this.canCollideWith(victim))
				{
					this.addCollision(victim);
					victim.hurtDmgVar((int) (this.damage), (this.velocity.x > 0 ? 1 : -1), 1f, false, Constant.DAMAGETYPE_PHYSICAL, player);
				}
			}
			else if(this.type == 37)
			{
				victim.hurtDmgVar((int)this.damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
			}
			else if(this.type == 38)
			{
				if(this.canCollideWith(victim))
				{
					victim.hurtDmgVar((int)this.damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_AIR, Constant.ITEMCLASS_RANGED, player);
					this.addCollision(victim);
				}
			}
			else if(this.type == 39)
			{
				Color[] quadcolors = new Color[4];
				quadcolors[0] = new Color(0.95f, 0.1f, 0.95f, 1f);
				quadcolors[1] = new Color(0.7f, 0.08f, 0.7f, 1f);
				quadcolors[2] = new Color(0.4f, 0.06f, 0.4f, 1f);
				quadcolors[3] = new Color(0.2f, 0.04f, 0.2f, 1f);
				Main.doEarthquake(15);
				Main.createCustomExplosion(this.Center(), 250, 1f, false, quadcolors, quadcolors[0]);
				for(Monster m : Constant.getMonstersInRange(this, 250))
				{
					float time = 0f;
					if(player != null)
					{
						Skill s = player.getSkill(41);
						if(s != null)
						{
							time = s.getInfoValueF(player, 0);
							m.hurtDmgVar(s.getInfoValueI(player, 1), Main.directionFromTo(this, m), 1f, player.canCritical(m), Constant.DAMAGETYPE_DARKNESS, player);
						}
					}
					m.Stun(time, this.owner);
				}
				this.destroy();
			}
			else if(this.type == 40)
			{
				Main.doEarthquake(15);
				for(int i = 0;i < 150;i++)
				{
					Vector2 vel = new Vector2(0f, MathUtils.random(0f, 1400f));

					Color color = new Color(0.96f, 0.92f, 0.6f, 1f);
					Particle p = Particle.Create(this.Center().add(MathUtils.random(-250f, 250f), 0f), vel, 16, color, 0f, 1f, 1f).setLight(32, color);
					p.drawBack = true;
					p.loseSpeed = true;
					p.loseSpeedMult = 0.9f;
				}
				for(int i = -250;i <= 250;i++)
				{
					try
					{
						Vector2 pos = new Vector2(this.Center().x,this.position.y - 8).add(i + MathUtils.random(-4, 4),  MathUtils.random(-16, 16));
						GameMap g = Constant.tryGetMapForEntity(this);
						Color color = g.map[Main.clamp(0, (int)Math.floor((pos.y/64f)-1), g.height-1)][Main.clamp(0, (int)Math.floor((pos.x/64f)), g.width-1)].color;
						Particle p = Particle.Create(pos, new Vector2(MathUtils.random(0, 50), MathUtils.random(100, 400)), 2, color, -3f, 3f, 1f);
						p.drawBack = true;
					}
					catch(Exception ex)
					{continue;}
				}
				Rectangle hitbox = new Rectangle(this.Center().x-250, this.Center().y-250, 500, 550);
				for(Monster m : Constant.getMonsterList(false))
				{
					if(m.hitBox().overlaps(hitbox))
					{
						if(player != null)
						{
							Skill s = player.getSkill(42);
							if(s != null)
							{
								m.hurtDmgVar(s.getInfoValueI(player, 0), Main.directionFromTo(this, m), 1f, player.canCritical(m), Constant.DAMAGETYPE_AIR, player);
							}
						}
						m.velocity.x = 0f;
						m.velocity.y = 1800f;
						m.Stun(1f, this.owner);
					}
				}
				this.destroy();
			}
			else if(this.type == 41)
			{
				if(this.canCollideWith(victim))
				{
					victim.hurtDmgVar((int)this.damage, this.directionByVelocity(), 1f, false, Constant.DAMAGETYPE_HOLY, Constant.ITEMCLASS_RANGED, player);
					Particle p = Particle.Create(victim.Center(), Vector2.Zero, 11, new Color(1f, 1f, 0.5f, 1f), 0f, 1f, 2f);
					p.drawFront = true;
					for(int i = 0;i < 5;i++)
					{
						p = Particle.Create(victim.randomHitBoxPosition(), Vector2.Zero, 11, new Color(1f, 1f, 0.5f, 1f), 0f, 1f, 2f);
						p.drawFront = true;
					}
					this.addCollision(victim);
				}
				if(this.ai[0] != 1)
				{
					this.destroy();
				}
			}
			else if(this.type == 44)
			{
				if(this.canCollideWith(victim) && this.collidable)
				{
					victim.hurtDmgVar((int)this.damage, 0, 0f, false, Constant.DAMAGETYPE_ICE, player);
					this.addCollision(victim);
				}
			}
			else if(this.type == 48)
			{
				victim.hurtDmgVar((int)this.damage, 0, 0f, false, Constant.DAMAGETYPE_NATURE, player);
			}
			else if(this.type == 49)
			{
				victim.hurtDmgVar((int)this.damage, 0, 0f, false, Constant.DAMAGETYPE_NATURE, player);
			}
			else if(this.type == 51)
			{
				victim.hurtDmgVar(damage, (this.Center().x > victim.Center().x ? -1 : 1), 1f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_RANGED, player);
				this.destroy();
			}
			else if(this.type == 53)
			{
				if(this.canCollideWith(victim))
				{
					victim.hurtDmgVar((int) (this.damage * 0.2f), 0, 0f, false, Constant.DAMAGETYPE_PHYSICAL, player);
					victim.Stun(0.5f, player);
					victim.velocity.y = 600f;
					this.addCollision(victim);
				}
			}
			else if(this.type == 55)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(item, victim), Constant.DAMAGETYPE_DARKNESS, Constant.ITEMCLASS_RANGED, player);
				player.addBuff(41, 2f, player);
			}
			else if(this.type == 56)
			{
				if(this.canCollideWith(victim))
				{
					this.addCollision(victim);
					victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
					victim.Stun(1f, player);
					Projectile p = Projectile.Summon(43, victim.Center().add(0, 200), Vector2.Zero, 0, 5f, player);
					Main.doEarthquake(10);
					Particle p2 = Particle.Create(victim.grounded ? new Vector2(victim.Center().x, victim.position.y) : victim.Center(), Vector2.Zero, victim.grounded ? 21 : 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 5.5f);
					p2.drawFront = true;
					p2.frameCounterTicks = 1;
					p2.fixAlpha = true;
					p2.alpha = 0.5f;
					p2 = Particle.Create(victim.grounded ? new Vector2(victim.Center().x, victim.position.y) : victim.Center(), Vector2.Zero, victim.grounded ? 21 : 11, Color.WHITE, 0f, 1f, 5f);
					p2.drawFront = true;
					p2.frameCounterTicks = 1;
					if(MathUtils.randomBoolean())
						p.mirrored = true;
				}
			}
			else if(this.type == 58)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_RANGED, player);
			}
			else if(this.type == 61)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 0.05f, player.canCritical(victim), Constant.DAMAGETYPE_BLOOD, player);
				this.destroy();
			}
			else if(this.type == 68)
			{
				if(this.canCollideWith(victim))
				{
					if(this.ai[5] != 1)
					{
						victim.hurtDmgVar(this.damage, this.directionByVelocity(), 0.5f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
					}
					else
					{
						victim.hurtDmgVar(this.damage*2, this.directionByVelocity(), 0.5f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
					}
					this.addCollision(victim);
				}
			}
			else if(this.type == 69)
			{
				if(this.canCollideWith(victim))
				{
					victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
					this.addCollision(victim);
				}
			}
			else if(this.type == 70)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 2f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
			}
			else if(this.type == 71)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 2f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
				this.destroy();
			}
			else if(this.type == 72)
			{
				victim.Stun(3f, player);
			}
			else if(this.type == 73)
			{
				if(this.ai[5] != 1 || Main.var[8] == 5)
				{
					victim.hurtDmgVar(damage, this.directionByVelocity(), 0.2f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
					Projectile p = Projectile.Summon(73, victim.Center().add(new Vector2(2000f, 0f).rotate(MathUtils.random(360f))), Vector2.Zero, damage, 1f, player);
					p.velocity = victim.Center().sub(p.Center()).nor().scl(3000f);
					p.collidable = false;
					this.ai[5] = 1;
					if(this.ai[4] == 1)
					{
						Skill s = player.getSkill(81);
						if(s != null)
						{
							s.cdf = 0f;
						}
					}
				}
			}
			else if(this.type == 74)
			{
				if(this.canCollideWith(victim) && this.currentFrame >= 8)
				{
					victim.hurtDmgVar((int) (victim.maxHealth * 0.5f), this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
					this.addCollision(victim);
				}
			}
			else if(this.type == 75)
			{
				victim.hurtDmgVar(damage, 0, 0f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
			}
			else if(this.type == 78)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 0.25f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
				this.destroy();
			}
			else if(this.type == 79)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 0.25f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
				this.destroy();
			}
			else if(this.type == 80 || this.type == 81)
			{
				if(this.velocity.y > 10 && this.canCollideWith(victim))
				{
					victim.hurtDmgVar(damage, this.directionByVelocity(), 2f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
					if(this.type == 81)
					{
						victim.Stun(2f, player);
					}
					this.addCollision(victim);
				}
			}
			else if(this.type == 82)
			{
				if(this.velocity.y < -1500f && this.canCollideWith(victim))
				{
					victim.hurtDmgVar(damage, 1, 0.05f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
					victim.velocity.x = 0;
					this.addCollision(victim);
				}
			}
			else if(this.type == 83)
			{
				victim.hurtDmgVar(damage, 0, 0, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
			}
			else if(this.type == 85 || this.type == 84)
			{
				victim.hurtDmgVar(damage, 0, 0, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
				this.destroy();
			}
			else if(this.type == 87)
			{
				if(this.velocity.len() > 10)
				{
					victim.hurtDmgVar(damage, 0, 0, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
				}
			}
			else if(this.type == 89)
			{
				if(victim != this.target)
					return;
				
				victim.addBuff(37, 5.5f, player);
				for(Monster m : Constant.getMonsterList(false))
				{
					if(m != null && m != victim && m.haveBuff(37) != -1 && m.buffs[victim.haveBuff(37)] != null && m.buffs[victim.haveBuff(37)].getOrigin() == player)
					{
						final Monster t = m;
						Event e = new Event(30) {
							@Override
							public void function()
							{
								if(t.health <= 0 || victim.health <= 0)
									return;
								
								float damage = 0;
								if(player.getSkill(55) != null)
								{
									damage = player.getSkill(55).getInfoValueF(player, 0);
								}
								t.hurtDmgVar((int) damage, 0, 0f, false, Constant.DAMAGETYPE_ENERGY, player);
								victim.hurtDmgVar((int) damage, 0, 0f, false, Constant.DAMAGETYPE_ENERGY, player);
								t.Stun(1f, player);
								victim.Stun(1f, player);
								t.removeBuff(37);
								victim.removeBuff(37);
								float angle = Main.angleBetween(t.Center(), victim.Center());
								Vector2 vel = new Vector2(1300f, 0f).rotate(angle);
								t.velocity = vel.cpy();
								victim.velocity = vel.cpy().scl(-1f);

								Vector2 pos = t.Center().add(victim.Center()).scl(0.5f);
								
								float scale = (t.Center().dst(victim.Center()))/206f;
								Particle p = Particle.Create(pos, Vector2.Zero, 26, new Color(0f, 1f, 1f, 1f), 0f, 1f, scale);
								p.rotation = Main.angleBetween(victim.Center(), t.Center());
							}
						};
						Main.scheduledTasks.add(e);
						break;
					}
				}
				Particle.Create(this.Center(), Vector2.Zero, 23, new Color(0.1f, 1f, 1f, 1f), 0f, 1f, 2f);
				this.destroy();
			}
			else if(this.type == 90)
			{
				if(victim != this.target)
					return;
				
				victim.hurtDmgVar(damage, this.directionByVelocity(), 2f, player.canCritical(victim), Constant.DAMAGETYPE_DEATH, player);
				Color[] quadcolors = new Color[4];
				quadcolors[0] = new Color(0.07f, 0.07f, 0.07f, 1f);
				quadcolors[1] = new Color(0.06f, 0.06f, 0.06f, 1f);
				quadcolors[2] = new Color(0.05f, 0.05f, 0.05f, 1f);
				quadcolors[3] = new Color(0.04f, 0.04f, 0.04f, 1f);
				Main.createCustomExplosion(this.Center(), 600f, 1, false, quadcolors, quadcolors[3]);
				Main.doEarthquake(30);
				DJ.playSound(DJ.DEATHHIT, 1f, 0.5f, this.Center());
				this.destroy();
			}
			else if(this.type == 92)
			{
				if(this.velocity.len()>10 && this.canCollideWith(victim))
				{
					victim.hurtDmgVar(damage, 0, 1f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
					this.addCollision(victim);
				}
			}
			else if(this.type == 100)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_ENERGY, player);
			}
			else if(this.type == 101)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_DARKNESS, player);
			}
			else if(this.type == 102)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_NATURE, player);
			}
			else if(this.type == 103)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_BLOOD, player);
			}
			else if(this.type == 104)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_FIRE, player);
			}
			else if(this.type == 105)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_ICE, player);
			}
			else if(this.type == 106)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_FIRE, player);
			}
			else if(this.type == 110)
			{
				if(this.canCollideWith(victim))
				{
					victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_FIRE, player);
					for(int i = 0;i < 50;i++)
					{
						float angle = MathUtils.random(360f);
						float sin = (float)Math.sin(angle * Math.PI/180f);
						float cos = (float)Math.cos(angle * Math.PI/180f);
						float range = MathUtils.random(64f);
						float rangeVel = MathUtils.random(200f);
						Vector2 pos = new Vector2(cos * range, sin * range).add(victim.Center());
						Particle p = Particle.Create(pos, new Vector2(cos * rangeVel, sin * rangeVel), i % 2 == 0 ? 32 : 2, Color.WHITE, 0f, MathUtils.random(0.5f, 1f), MathUtils.random(1f, 1.5f));
						if(i%2==0)
							p.setCustomTexture(Content.particles[31], 1);
						Color color = Color.WHITE.cpy().fromHsv(this.ai[5]%360, 1f, 1f);
						p.setLight(64, color);
					}
					this.addCollision(victim);
				}
			}
			else if(this.type == 115)
			{
				if(this.currentFrame < 3 && this.canCollideWith(victim))
				{
					victim.hurtDmgVar(damage, 0, 0f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_LONGSWORD, player);
					this.addCollision(victim);
				}
			}
			else if(this.type == 116)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_RANGED, player);
				this.destroy();
			}
			else if(this.type == 117)
			{
				if(this.canCollideWith(victim))
				{
					victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
					this.addCollision(victim);
				}
			}
			else if(this.type == 118)
			{
				if(this.canCollideWith(victim) && ((this.currentFrame >= 11 && this.currentFrame <= 13) || (victim.hitBox().overlaps(new Rectangle(this.Center().x - 400 * (this.mirrored ? 0 : 1), this.position.y, 400, 256)) && this.currentFrame >= 1 && this.currentFrame <= 3)))
				{
					victim.hurtDmgVar(damage, this.mirrored ? -1 : 1, 1.5f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, player);
					victim.Stun(1.5f, player);
					this.addCollision(victim);
				}
			}
			else if(this.type == 119)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_POISON, Constant.ITEMCLASS_MAGIC, player);
			}
			else if(this.type == 120)
			{
				int b = this.haveBuff(72);
				player.oldCriticalChance = player.criticalChance;
				if(b != -1)
				{
					player.criticalChance += this.getBuffStacks(72);
				}
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_RANGED, player);
				player.criticalChance = player.oldCriticalChance;
			}
			else if(this.type == 122)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_DEATH, Constant.ITEMCLASS_MAGIC, player);
			}
			else if(this.type == 125)
			{
				victim.hurtDmgVar(damage, this.directionByVelocity(), 1f, player.canCritical(victim), Constant.DAMAGETYPE_DARKNESS, player);
				victim.addBuff(75, 1f, player);
				for(int i = 0;i < 30;i++)
				{
					Vector2 vel = new Vector2(1200f, 0f).setAngle(this.velocity.angle()).rotate(MathUtils.random(-15f, 15f));
					Particle p = Particle.Create(this.randomHitBoxPosition(), vel, 2, new Color(0f, 0f, 0f, 0f), 0f, 0.5f, 1f);
					p.setLight(64, Color.PURPLE);
					p.loseSpeed = true;
					p.loseSpeedMult = 0.9f;
				}
				this.destroy();
			}
			else if(this.type == 126)
			{
				victim.hurtDmgVar(damage, 0, 0f, player.canCritical(victim), Constant.DAMAGETYPE_HOLY, player);
				this.destroy();
			}

			int i = Constant.ITEMSLOT_LEFT;
			int idA = -1;
			if(this.regOnHit)
			{
				if(player.inventory[i] != null && player.inventory[i].shootProj == this.type)
				{
					player.inventory[i].onHit(victim, player);
					idA = player.inventory[i].id;
					if(player.doubleOnHit)
						player.inventory[i].onHit(victim, player);
				}
	
				i = Constant.ITEMSLOT_RIGHT;
				if(player.inventory[i] != null && player.inventory[i].shootProj == this.type && idA != player.inventory[i].id)
				{
					player.inventory[i].onHit(victim, player);
					if(player.doubleOnHit)
						player.inventory[i].onHit(victim, player);
				}
	
				for(i = Constant.ITEMSLOT_OFFSET + 2;i <= Constant.ITEMSLOT_MAX;i++)
				{
					if(player.inventory[i] != null)
					{
						player.inventory[i].onHit(victim, player);
						if(player.doubleOnHit)
							player.inventory[i].onHit(victim, player);
					}
				}
			}
		} // end of victim collision
		else
		{
			if(this.type == 36)
			{
				this.owner.position = this.Center().sub(owner.width/2f, 0f);
				if(Constant.tryGetMapForEntity(this.owner).doesRectCollideWithMap(this.owner.position.x, this.owner.position.y, this.owner.width, this.owner.height))
					this.owner.position = Constant.tryGetMapForEntity(this.owner).getNextestFreeSpace(this.owner.position.x, this.owner.position.y, this.owner.width, this.owner.height, (this.velocity.x > 0 ? 1 : -1), (this.velocity.y > 0 ? 1 : -1)).scl(64f);
				
				for(int i = 0;i < 6;i++)
				{
					Vector2 vel = this.velocity.cpy().rotate(MathUtils.random(-20, 20)).scl(MathUtils.random(0.8f, 1.2f));
					Particle.Create(this.randomHitBoxPosition(), vel, 2, new Color(0.75f, 0.75f, 0.75f, 1f), -2f, 1f, 1f);
				}
				this.destroy();
			}
			else if(this.type == 38)
			{
				this.destroy();
			}
			else if(this.type == 87)
			{
				this.currentFrame = 1;
				this.ai[5] = 1;
				this.position.y = Constant.tryGetMapForEntity(this).getNotFreeY((int)this.Center().x/64, (int)this.position.y/64)*64;
				for(int i = 0;i < 8;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-100, 100), MathUtils.random(80, 150)), 9, Color.WHITE, -2.5f, 2f, 1f);
					p.setCustomTexture(Content.extras[5], 1);
					p.collisions = true;
					p.drawFront = true;
				}
				this.collidable = false;
				this.velocity = Vector2.Zero;
				for(int i = 0;i <= 2;i++)
				{
					Particle p = Particle.Create(this.Center().add(-48 + 48*i, this.height/2f), Vector2.Zero, 2, Color.WHITE, 0f, i+1, 1f);
					p.fixAlpha = true;
					p.drawFront = true;
					p.setCustomTexture(Content.extras[6], 13);
				}
			}
		} // end of terrain collision

		if(this.type == 1)
		{
			this.destroy();
		}
		else if(this.type == 2)
		{
			this.destroy();
		}
		else if(this.type == 4)
		{
			this.destroy();
		}
		else if(this.type == 5)
		{
			this.destroy();
		}
		else if(this.type == 6)
		{
			this.destroy();
		}
		else if(this.type == 7)
		{
			this.destroy();
		}
		else if(this.type == 8)
		{
			this.destroy();
		}
		else if(this.type == 9)
		{	
			Vector2 pos = new Vector2(this.position.x + this.width, this.position.y + this.height/2);
			pos.sub(this.Center());
			pos.rotate(this.rotation);
			pos.add(this.Center());
			Particle p = Particle.Create(pos, Vector2.Zero, 7, new Color(1f, 0.85f, 0f, 1f), 0f, 1/3f, 3f);	
			p.frameCounterTicks-=2;
			p.drawFront = true;
			p.rotation = MathUtils.random(360);
			this.destroy();
		}
		else if(this.type == 10)
		{
			for(int i = 0;i < 100;i++)
			{
				Color color = new Color(1f, 0.5f + MathUtils.random()/2, 0f, 1f);
				Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-900, 900), MathUtils.random(-900, 900)), 2, color, -30f, 1.5f, 0.75f + MathUtils.random()/2f);
				p.drawFront = true;
				p.killOnCollision = true;
			}
			this.destroy();
		}
		else if(this.type == 11)
		{
			this.destroy();
		}
		else if(this.type == 13)
		{	
			this.destroy();
		}
		else if(this.type == 14)
		{
			this.destroy();
		}
		else if(this.type == 16)
		{
			NPC n = SaveInfos.findNPCWithID(Constant.NPCID_VICTOR);
			if(n != null)
				n.showDialog(29);
			
			this.destroy();
		}
		else if(this.type == 19)
		{
			this.destroy();
		}
		else if(this.type == 20)
		{
			this.destroy();
		}
		else if(this.type == 21)
		{
			this.destroy();
		}
		else if(this.type == 22)
		{
			this.destroy();
		}
		else if(this.type == 27)
		{
			this.destroy();
		}
		else if(this.type == 37)
		{
			Particle.Create(this.Center(), Vector2.Zero, 6, new Color(0f, 1f, 1f, 1f), 0f, 1f, 1f);
			for(int i = 0;i < 3;i++)
			{
				Vector2 vel = this.velocity.cpy()
						.scl(-1f)
						.rotate(MathUtils.random(-5f, 5f));
				Particle.Create(this.Center(), vel, 2, new Color(0f, 1f, 1f, 1f), 0f, 1f, 1f);
			}
			this.destroy();
		}
		else if(this.type == 46)
		{
			boolean eob = this.haveBuff(63) != -1;
			for(Monster m : Constant.getMonstersInRange(this, eob ? 250 : 150))
			{
				m.hurtDmgVar((int)this.damage, 0, 0f, false, Constant.DAMAGETYPE_FIRE, player);
				if(eob)
				{
					if(player.getSkill(54) != null)
					{
						float burndamage = player.getSkill(54).getInfoValueF(player, 2);
						m.Burn((int)burndamage, 3f, player);
					}
				}
			}
			/*for(int i = 0;i < 250;i++)
			{
				float range = MathUtils.random(0, 120);
				float angle = (float) (MathUtils.random() * Math.PI * 2f);
				float sin = (float) Math.sin(angle);
				float cos = (float) Math.cos(angle);
				Vector2 pos = new Vector2(cos * range, sin * range).add(this.Center());
				Particle p = Particle.Create(pos, Vector2.Zero, 11, new Color(1f, 0.6f + MathUtils.random(0.2f), 0f, 1f), 0f, 1f, 1f);
				
				Particle.Create(p.position, Vector2.Zero.cpy(), 6, Color.DARK_GRAY, 2f, 1f, 1f);
			}*/
			Main.createExplosion(this.Center(), eob ? 250 : 150, 1f, false);
			Main.doEarthquake(eob ? 30 : 15);
			this.destroy();
		}
		else if(this.type == 95)
		{
			for(Monster m : Constant.getMonstersInRange(this, 100))
			{
				m.hurtDmgVar((int)this.damage, 0, 0f, false, Constant.DAMAGETYPE_FIRE, player);
			}
			/*for(int i = 0;i < 250;i++)
			{
				float range = MathUtils.random(0, 120);
				float angle = (float) (MathUtils.random() * Math.PI * 2f);
				float sin = (float) Math.sin(angle);
				float cos = (float) Math.cos(angle);
				Vector2 pos = new Vector2(cos * range, sin * range).add(this.Center());
				Particle p = Particle.Create(pos, Vector2.Zero, 11, new Color(1f, 0.6f + MathUtils.random(0.2f), 0f, 1f), 0f, 1f, 1f);
				
				Particle.Create(p.position, Vector2.Zero.cpy(), 6, Color.DARK_GRAY, 2f, 1f, 1f);
			}*/
			Main.createExplosion(this.Center(), 100, 0.05f, false);
			Main.doEarthquake(10);
			this.destroy();
		}
		else if(this.type == 48)
		{
			for(int i = 0;i < 10;i++)
			{
				Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 11, new Color(0.9f, 0.7f, 0.05f, 1f), 0f, 1f, 1f);
			}
			for(int i = 0;i < 30;i++)
			{
				Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-300, 300), MathUtils.random(200f, 400f)),
						2, new Color(0.9f, 0.7f, 0.05f, 1f), -2f, 2f, 1f);
			}
			this.destroy();
		}
		else if(this.type == 49)
		{
			this.destroy();
		}
		else if(this.type == 51)
		{
			this.destroy();
		}
		else if(this.type == 55)
		{
			Particle.Create(this.Center(), Vector2.Zero, 6, new Color(1f, 0f, 1f, 1f), 0f, 1f, 1f);
			for(int i = 0;i < 3;i++)
			{
				Vector2 vel = this.velocity.cpy()
						.scl(-1f)
						.rotate(MathUtils.random(-5f, 5f));
				Particle.Create(this.Center(), vel, 4, new Color(1f, 0f, 1f, 1f), 0f, 1f, 1f);
			}
			this.destroy();
		}
		else if(this.type == 58)
		{
			this.destroy();
		}
		else if(this.type == 70)
		{
			this.destroy();
		}
		else if(this.type == 72)
		{
			for(int i = 0;i < 100;i++)
			{
				Projectile p = Projectile.Summon(75, this.Center().add(-2500 * this.directionByVelocity(), 2500), Vector2.Zero, this.damage, 6f, player);
				p.velocity = this.Center().sub(p.Center()).nor().scl(2000f);
				p.position.add(MathUtils.random(-500, 500), MathUtils.random(-500, 500));
				p.collisionY = this.Center().y - 32;
				if(victim != null)
				{
					p.setTarget(victim);
					p.destroyOnTargetLoss = true;
				}
				p.ai[5] = 1;
			}
			this.destroy();
		}
		else if(this.type == 73 && this.ai[4] == 1)
		{
			this.destroy();
		}
		else if(this.type == 75)
		{
			this.destroy();
		}
		else if(this.type == 83)
		{
			this.destroy();
		}
		else if(this.type == 87)
		{
			if(Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, this.position.y, this.width, this.height))
			{
				this.currentFrame = 1;
				this.ai[5] = 1;
				this.position.y = Constant.tryGetMapForEntity(this).getNotFreeY((int)this.Center().x/64, (int)this.position.y/64)*64;
				for(int i = 0;i < 8;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-100, 100), MathUtils.random(80, 150)), 9, Color.WHITE, -2.5f, 2f, 1f);
					p.setCustomTexture(Content.extras[5], 1);
					p.collisions = true;
					p.drawFront = true;
				}
				this.collidable = false;
				this.velocity = Vector2.Zero;
				for(int i = 0;i <= 2;i++)
				{
					Particle p = Particle.Create(this.Center().add(-48 + 48*i, this.height/2f), Vector2.Zero, 2, Color.WHITE, 0f, i+1, 1f);
					p.fixAlpha = true;
					p.drawFront = true;
					p.setCustomTexture(Content.extras[6], 13);
				}
			}
		}
		else if(this.type == 98)
		{
			for(Monster m : Constant.getMonstersInRange(this, this.ai[5]))
			{
				m.hurtDmgVar((int)this.damage, 0, 0f, player.canCritical(m), Constant.DAMAGETYPE_BLOOD, player);
			}
			/*for(int i = 0;i < 250;i++)
			{
				float range = MathUtils.random(0, 120);
				float angle = (float) (MathUtils.random() * Math.PI * 2f);
				float sin = (float) Math.sin(angle);
				float cos = (float) Math.cos(angle);
				Vector2 pos = new Vector2(cos * range, sin * range).add(this.Center());
				Particle p = Particle.Create(pos, Vector2.Zero, 11, new Color(1f, 0.6f + MathUtils.random(0.2f), 0f, 1f), 0f, 1f, 1f);
				
				Particle.Create(p.position, Vector2.Zero.cpy(), 6, Color.DARK_GRAY, 2f, 1f, 1f);
			}*/
			Color[] quadColors = new Color[4];
			quadColors[0] = new Color(1f, 0.4f, 0.4f, 1f);
			quadColors[1] = new Color(1f, 0.1f, 0.1f, 1f);
			quadColors[2] = new Color(0.7f, 0.1f, 0.1f, 1f);
			quadColors[3] = new Color(0.4f, 0.02f, 0.02f, 1f);
			Main.createBackCustomExplosion(this.Center(), this.ai[5], 0.5f, false, quadColors, quadColors[1]);
			for(int i = 0;i < this.ai[5];i++)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-this.ai[5], this.ai[5]), MathUtils.random(-this.ai[5], this.ai[5])), 2, quadColors[MathUtils.random(1, 3)], -4f, 3f, 1.5f);
				p.collisions = true;
				p.loseSpeedMult = 0.985f;
				p.drawFront = true;
				p.setLight(32, quadColors[1]);
			}
			Main.doEarthquake(15);
			this.destroy();
		}
		else if(this.type == 100)
		{
			for(int i = 0;i < 5;i++)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero, 11, Color.WHITE, 0f, 0.5f, 4f);
				p.frameCounterTicks--;
				p.setLight(256, new Color(0f, 0f, 1f, 1f));
			}
			this.destroy();
		}
		else if(this.type == 101)
		{
			for(int i = 0;i < 5;i++)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero, 11, new Color(0.25f, 0.01f, 0.25f, 1f), 0f, 0.5f, 4f);
				p.frameCounterTicks--;
				p.setLight(256, new Color(0.15f, 0.02f, 0.15f, 1f));
			}
			this.destroy();
		}
		else if(this.type == 102)
		{
			for(int i = 0;i < 2;i++)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero, 11, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_NATURE], 0f, 0.5f, 4f);
				p.frameCounterTicks--;
				p.setLight(256, new Color(0.2f, 1f, 0.2f, 1f));
			}
			this.destroy();
		}
		else if(this.type == 103)
		{
			for(int i = 0;i < 2;i++)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero, 11, new Color(1f, 0.15f, 0.15f, 1f), 0f, 0.5f, 4f);
				p.frameCounterTicks--;
				p.setLight(256, new Color(1f, 0.15f, 0.15f, 1f));
			}
			this.destroy();
		}
		else if(this.type == 104)
		{
			for(int i = 0;i < 2;i++)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero, 11, new Color(1f, 0.8f, 0.2f, 1f), 0f, 0.5f, 4f);
				p.frameCounterTicks--;
				p.setLight(256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE]);
			}
			this.destroy();
		}
		else if(this.type == 105)
		{
			for(int i = 0;i < 2;i++)
			{
				Particle p = Particle.Create(this.Center(), Vector2.Zero, 11, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ICE], 0f, 0.5f, 4f);
				p.frameCounterTicks--;
				p.setLight(256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ICE]);
			}
			this.destroy();
		}
		else if(this.type == 106)
		{
			for(int i = 0;i < 20;i++)
			{
				Vector2 vel = new Vector2(this.velocity.len(), 0f).rotate(this.velocity.angle() + MathUtils.random(-10, 10));
				Particle p = Particle.Create(this.randomHitBoxPosition(), vel, 12, new Color(1f, 0.8f, 0.2f, 1f), 0f, 1f, 0.25f);
				p.setLight(16, p.color);
				p.loseSpeed = true;
				p.loseSpeedMult = 0.93f;
			}
			this.destroy();
		}
		else if(this.type == 109)
		{
			Color[] quadcolors = new Color[4];
			for(int i = 0;i < 4;i++)
			{
				quadcolors[i] = Color.WHITE.cpy().fromHsv(this.ai[5] % 360, 1f, 1f);
				this.ai[5] += 10;
			}
			Color smokeColor = Color.WHITE.cpy().fromHsv(this.ai[5] % 360, 1f, 1f);
			Main.createCustomExplosion(this.Center(), 200, 1f, false, quadcolors, smokeColor);
			Main.doEarthquake(20);
			for(Monster m : Constant.getMonstersInRange(this, 200))
			{
				m.hurtDmgVar(this.damage, 0, 0f, player.canCritical(m), Constant.DAMAGETYPE_FIRE, player);
			}
			this.destroy();
		}
		else if(this.type == 111)
		{
			this.destroy();
		}
		else if(this.type == 116)
		{
			this.destroy();
		}
		else if(this.type == 119)
		{
			this.destroy();
		}
		else if(this.type == 120)
		{
			this.destroy();
		}
		else if(this.type == 121)
		{
			this.destroy();
		}
		else if(this.type == 122)
		{
			this.destroy();
		}
		else if(this.type == 124)
		{
			this.destroy();
		}
		else if(this.type == 128)
		{
			Color[] color = new Color[4];
			color[0] = new Color(1f, 1f, 1f, 1f);
			color[1] = new Color(0.8f, 0.4f, 0.8f, 1f);
			color[2] = new Color(0.5f, 0.2f, 0.5f, 1f);
			color[3] = new Color(0.35f, 0.05f, 0.35f, 1f);
			Main.createCustomExplosion(this.Center(), 150, 1f, false, color, color[3]);
			Main.doEarthquake(15);
			this.destroy();
		}
		else if(this.type == 129)
		{
			Color[] color = new Color[4];
			color[0] = new Color(1f, 1f, 1f, 1f);
			color[1] = new Color(0.8f, 0.4f, 0.8f, 1f);
			color[2] = new Color(0.5f, 0.2f, 0.5f, 1f);
			color[3] = new Color(0.35f, 0.05f, 0.35f, 1f);
			Main.createCustomExplosion(this.Center(), 200, 1f, false, color, color[3]);
			Main.doEarthquake(20);
			this.destroy();
		}
	}


	public void destroy()
	{
		Player player = null;
		if(this.owner instanceof Player)
			player = (Player)this.owner;
		if(this.haveBuff(6) != -1)
		{
			for(int i = 0;i < 40;i++)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(1100f, 0f).scl(MathUtils.random(0.5f, 2f)).setAngle(this.velocity.angle()+MathUtils.random(-20f, 20f)), 10, new Color(1f, 1f, MathUtils.random(), 1f), 0f, 1f, 1f).setLight(16, new Color(0.92f, 0.96f, 0.6f, 1f));
				p.loseSpeed = true;
				p.loseSpeedMult = 0.9f;
				p.rotation = MathUtils.random(360f);
			}
		}
		if(this.haveBuff(32) != -1)
		{
			this.removeBuff(32);
			Projectile p = Projectile.Summon(29, this.Center().add(0, 200), Vector2.Zero, 0, 5f, this.owner);
			Main.doEarthquake(15);
			if(MathUtils.randomBoolean())
				p.mirrored = true;
		}

		if(this.haveBuff(31) != -1)
		{
			int count = 0;
			if(this.owner instanceof Player)
			{
				if(player.getSkill(39) != null)
				{
					count = (int) player.getSkill(39).getInfoValueI(player, 0);
				}
			}
			for(int i = 0;i < count;i++)
			{
				float len = this.velocity.len();
				Vector2 pos = this.Center().cpy().add(MathUtils.random(-500, 500), 1000 + i * 200);
				float angle = Main.angleBetween(pos, this.Center());
				Vector2 vel = new Vector2(len, 0).rotate(angle + MathUtils.random(-7.5f, 7.5f));
				Projectile p = Projectile.Summon(this.type, pos, vel, this.damage, 5f, this.owner);
				p.collisionY = this.Center().y + this.height * 2f;
			}
			this.removeBuff(31);
		}

		if(this.type == 1)
		{
			for(int i = 0;i < 16;i++)
			{
				Color color = new Color(0.5f, 0.5f, 0.5f, 1f);
				if(MathUtils.random(1) == 0)
				{
					color = new Color(0.6f, 0.2f, 0f, 1f);
				}
				Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(0.1f).rotate(MathUtils.random(-10, 10)), 2, color, -5f, 1.5f, 1f).collisions = true;
			}
		}
		else if(this.type == 2)
		{
			for(int i = 0;i < 72;i++)
			{
				float sin = (float)Math.sin(Math.PI / 180 * i * 5);
				float cos = (float)Math.cos(Math.PI / 180 * i * 5);
				for(int j = 0;j < 4;j++)
				{
					Particle.Create(this.Center(), new Vector2(cos * MathUtils.random(100f, 255f), sin * MathUtils.random(100f, 255f)), 2, Color.CYAN, 0f, 1.5f, 1f);
				}
			}
		}
		else if(this.type == 5)
		{
			for(int i = 0;i < 50;i++)
			{
				Color color = new Color(0f,0.5f + MathUtils.random()*0.3f,1f,1);
				Particle.Create(this.Center(), new Vector2(MathUtils.random(-500, 500), MathUtils.random(-500, 500)), 2, color, 0f, 1f, 2+MathUtils.random());
			}
			for(int i = 0;i < 4;i++)
			{
				Vector2 vel = this.velocity.cpy().scl(-0.2f);
				float addX = 0;
				if(this.velocity.x > 0)
					addX = MathUtils.random(-300, 0);
				else
					addX = MathUtils.random(0, 300);

				float addY = 0;
				if(this.velocity.y > Math.abs(this.velocity.x*2) && this.velocity.y > 0)
					addY = MathUtils.random(-300, 0);
				else
					addY = MathUtils.random(300, 600);

				vel.add(addX, addY);
				Projectile.Summon(6, this.Center(), vel, this.damage, 6f, owner);
			}
		}
		else if(this.type == 6)
		{
			for(int i = 0;i < 50;i++)
			{
				Color color = new Color(0f,0.5f + MathUtils.random()*0.3f,1f,1);
				Particle.Create(this.Center(), new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100)), 2, color, 0f, 1f, 1f);
			}
		}
		else if(this.type == 11)
		{
			Vector2 pos = new Vector2(this.position.x + this.width, this.position.y + this.height/2);
			pos.sub(this.Center());
			pos.rotate(this.rotation);
			pos.add(this.Center());
			float r = MathUtils.random()/2;
			Particle p = Particle.Create(pos, Vector2.Zero, 12, new Color(r, 0f, r, 1f), 0f, 1/3f, 3f);	
			p.frameCounterTicks-=2;
			p.drawFront = true;
			p.rotation = MathUtils.random(360);
		}
		else if(this.type == 12)
		{
			if(player != null)
			{
				Skill s = player.getSkill(8);
				if(s != null && s.casts == 1)
				{
					s.applyCast(player);
				}
			}
		}
		else if(this.type == 13)
		{
			Vector2 pos = new Vector2(this.position.x + this.width + 32, this.position.y + this.height/2);
			pos.sub(this.Center());
			pos.rotate(this.rotation);
			pos.add(this.Center());
			Particle p = Particle.Create(pos, Vector2.Zero, 11, new Color(0f, 1f, 1f, 1f), 0f, 1f, 3f);	
			p.frameCounterTicks--;
			p.drawFront = true;
			p.rotation = MathUtils.random(360);
		}
		else if(this.type == 19)
		{
			for(int i = 0;i < 10;i++)
			{
				int ang = MathUtils.random((int) (Math.PI*2));
				float sin = (float)Math.sin(ang);
				float cos = (float)Math.cos(ang);
				Particle p = Particle.Create(this.Center(), new Vector2(cos * 100f, sin * 100f), 2, Color.CYAN, 0f, 1f, 1f);
				p.setLight(16, p.color);
			}
		}
		else if(this.type == 21)
		{
			for(int i = 0;i < 20;i++)
			{
				Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100)), 6, new Color(0f, 0.9f, 0f, 1f), 0f, 1f, 1f);
			}
		}
		else if(this.type == 27)
		{
			for(int i = 0;i < 3;i++)
			{
				float r = 0.33f + (MathUtils.random()/2f);
				Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-50, 50), MathUtils.random(100, 200)), 2, new Color(r, r, r, 1f), -2f, 1f, 1f);
			}
		}
		else if(this.type == 51)
		{
			for(int i = 0;i < 16;i++)
			{
				Color color = new Color(0.6f, 0.6f, 0.6f, 1f);
				if(MathUtils.random(1) == 0)
				{
					color = new Color(1f, 0.8f, 0f, 1f);
				}
				Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-64, 64), MathUtils.random(-64, 64)), 2, color, -5f, 1.5f, 1f);
			}
		}
		else if(this.type == 53)
		{
			Main.doEarthquake(40);
			Projectile p = Projectile.Summon(54, new Vector2(this.Center().x, this.position.y + 20), Vector2.Zero, 0, 1f, this.owner);
			for(Monster m : Constant.getMonstersInArea(this.myMapX, this.myMapY, this.Center().x - 80, this.position.y, 160, 160))
			{
				m.hurtDmgVar(this.damage, 0, 0f, false, Constant.DAMAGETYPE_PHYSICAL, player);
				m.Stun(1.25f, player);
				m.velocity.y = 1300f;
			}
			for(int i = 0;i < 150;i++)
			{
				float r = MathUtils.random(0.9f, 1f);
				Particle pa = Particle.Create(p.randomHitBoxPosition(), new Vector2(MathUtils.random(-100, 100), MathUtils.random(0, 200)), 2, new Color(r, r, r, 1f), -2f, 1.25f, 1f);
				pa.drawFront = true;
				pa.lights = true;
				pa.lightSize = 16;
			}
		}
		else if(this.type == 58)
		{
			for(int i = 0;i < 32;i++)
			{
				Color color = new Color(0.7f, 0.7f, 0.7f, 1f);
				if(MathUtils.random(1) == 0)
				{
					color = new Color(0.6f, 0.1f, 0.1f, 1f);
				}
				Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-64, 64), MathUtils.random(-64, 64)), 2, color, -5f, 1.5f, 1f);
			}
		}
		else if(this.type == 61)
		{
			Particle.Create(this.Center(), Vector2.Zero, 15, new Color(1f, 0.1f, 0.1f, 1f), 0f, 1f, 1f);
		}
		else if(this.type == 70)
		{
			for(int i = 0;i < (this.ai[5] == 1 ? 1 : 25);i++)
			{
				Color color = new Color(0.7f, 0.7f, 0.7f, 1f);
				if(MathUtils.randomBoolean(0.2f))
				{
					color = new Color(1f, 0.8f, 0f, 1f);
				}
				Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(0.25f, 0.4f), 2, color, -2f, 1f, 1f);
				p.collisions = true;
			}
		}
		else if(this.type == 71)
		{
			for (int i = 0; i < 120; i++) {
				float extraRotation = MathUtils.random(-15, 15);
				Particle p = Particle.Create(
						new Vector2(this.Center()
								.add(new Vector2(MathUtils.random(this.getScaledWidth()), 0f).rotate(this.rotation))),
						this.velocity.cpy().scl(MathUtils.random(0.7f, 1.4f)).rotate(extraRotation), 2,
						new Color(0f, 0.69f, 1f, 1f), 0f, 2f, 1.5f);
				p.setLight(16, p.color);
				p.rotation = MathUtils.random(360f);
				p.drawFront = true;
				p.frameCounterTicks++;
			}
			Main.doEarthquake(40);
		}
		else if(this.type == 75)
		{
			for(int i = 0;i < 1;i++)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(0.25f, 0.4f), 2, Color.WHITE, -2f, 1f, 1f);
				p.collisions = true;
				p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
			}
		}
		else if(this.type == 78 || this.type == 85)
		{
			for(int i = 0;i < 6;i++)
			{
				Color color = new Color(0.15f, 0.15f, 0.15f, 1f);
				if(MathUtils.randomBoolean(0.2f))
				{
					color = new Color(1f, 0.8f, 0.1f, 1f);
				}
				Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(0.25f, 0.4f), 2, color, -2f, 1f, 1f);
				p.collisions = true;
			}
		}
		else if(this.type == 79 || this.type == 84)
		{
			for(int i = 0;i < 6;i++)
			{
				Color color = new Color(1f, 1f, 0.2f, 1f);
				if(MathUtils.randomBoolean(0.2f))
				{
					color = new Color(1f, 1f, 1f, 1f);
				}
				Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(0.25f, 0.4f), 2, color, -2f, 1f, 1f);
				p.collisions = true;
				p.setLight(32, Color.WHITE);
			}
		}
		else if(this.type == 83)
		{
			for(int i = 0;i < 6;i++)
			{
				Color color = new Color(1f, 1f, 0.2f, 1f);
				if(MathUtils.randomBoolean(0.2f))
				{
					color = new Color(1f, 1f, 1f, 1f);
				}
				Particle p = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(0.25f, 0.4f), 2, color, -2f, 1f, 1f);
				p.collisions = true;
				p.setLight(32, Color.WHITE);
			}
		}
		else if(this.type == 111)
		{
			Color[] quadcolors = new Color[4];
			for(int i = 0;i < 4;i++)
			{
				quadcolors[i] = Color.WHITE.cpy().fromHsv(this.ai[5] % 360, 1f, 1f);
				this.ai[5] += 10;
			}
			Color smokeColor = Color.WHITE.cpy().fromHsv(this.ai[5] % 360, 1f, 1f);
			Main.createCustomExplosion(this.Center(), 200, 0.1f, false, quadcolors, smokeColor);
			Main.doEarthquake(20);
			for(Monster m : Constant.getMonstersInRange(this, 200))
			{
				m.hurtDmgVar(this.damage, 0, 0f, player.canCritical(m), Constant.DAMAGETYPE_FIRE, player);
			}
		}
		else if(this.type == 116)
		{
			for(int i = 0;i < 15;i++)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-128f, 128f), MathUtils.random(-128f, 128f)), 2, new Color(), 0f, 0.5f, 1f);
				p.setLight(32, this.lightColor);
			}
		}
		else if(this.type == 119)
		{
			for(int i = 0;i < 20;i++)
			{
				Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-200, 200), MathUtils.random(-200, 200)), 2, new Color(0f, 0.75f, 0f, 0.75f), -2f, 1f, 1f).collisions = true;
			}
		}
		else if(this.type == 120)
		{
			for(int i = 0;i < 16;i++)
			{
				Color color = new Color(0.9f, 0.9f, 0.9f, 1f);
				if(MathUtils.random(1) == 0)
				{
					color = new Color(0.95f, 0f, 0f, 1f);
				}
				Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(0.1f).rotate(MathUtils.random(-10, 10)), 2, color, -5f, 1.5f, 1f).collisions = true;
			}
		}
		else if(this.type == 121)
		{
			Color[] quadcolors = new Color[4];
			quadcolors[0] = new Color(0.4f, 0.4f, 0.4f, 1f);
			quadcolors[1] = new Color(0.3f, 0.3f, 0.3f, 1f);
			quadcolors[2] = new Color(0.2f, 0.2f, 0.2f, 1f);
			quadcolors[3] = new Color(0.1f, 0.1f, 0.1f, 1f);
			Main.createCustomExplosion(this.Center(), 48, 1f, false, quadcolors, quadcolors[0]);
		}
		else if(this.type == 122)
		{
			Color[] quadcolors = new Color[4];
			quadcolors[0] = new Color(0.4f, 0.4f, 0.4f, 1f);
			quadcolors[1] = new Color(0.3f, 0.3f, 0.3f, 1f);
			quadcolors[2] = new Color(0.2f, 0.2f, 0.2f, 1f);
			quadcolors[3] = new Color(0.1f, 0.1f, 0.1f, 1f);
			Main.createCustomExplosion(this.Center(), 48, 0.4f, false, quadcolors, quadcolors[0]);
		}
		else if(this.type == 123)
		{
			Color[] quadcolors = new Color[4];
			quadcolors[0] = new Color(1f, 1f, 1f, 1f);
			quadcolors[1] = new Color(0.92f, 0.96f, 0.6f, 1f);
			quadcolors[2] = quadcolors[1];
			quadcolors[3] = new Color(0.92f, 0.8f, 0.4f, 1f);
			Main.createCustomExplosion(this.Center(), 64, 0.5f, false, quadcolors, quadcolors[1]);
		}
		else if(this.type == 124)
		{
			Color[] quadcolors = new Color[4];
			quadcolors[0] = new Color(1f, 1f, 1f, 1f);
			quadcolors[1] = new Color(0.92f, 0.96f, 0.6f, 1f);
			quadcolors[2] = quadcolors[1];
			quadcolors[3] = new Color(0.92f, 0.8f, 0.4f, 1f);
			Main.createCustomExplosion(this.Center(), 128*this.scale, 0.5f, false, quadcolors, quadcolors[1]);
			Main.doEarthquake((int)(10*this.scale));
			for(Monster m : Constant.getMonstersInRange(this, 128*this.scale))
			{
				m.hurtDmgVar(damage, Main.directionFromTo(this, m), 1f, player.canCritical(m), Constant.DAMAGETYPE_HOLY, player);
			}
		}
		else if(this.type == 126)
		{
			Color[] quadcolors = new Color[4];
			quadcolors[0] = new Color(1f, 1f, 1f, 1f);
			quadcolors[1] = new Color(0.92f, 0.96f, 0.6f, 1f);
			quadcolors[2] = quadcolors[1];
			quadcolors[3] = new Color(0.92f, 0.8f, 0.4f, 1f);
			Main.createCustomExplosion(this.Center(), 32, 0.15f, false, quadcolors, quadcolors[1]);
		}
		
		this.active = false;
		
		if(this.type == 78 || this.type == 85 || this.type == 79 || this.type == 84 || this.type == 82 || this.type == 81 || this.type == 80)
		{
			if(this.owner instanceof Player)
			{
				int slot = 0;
				for(int i = 0;i < ArkaClass.getMaxCGCards(player);i++)
				{
					if(!player.isCardAvailable[i])
					{
						slot = i;
						break;
					}
				}
				((Player)this.owner).ticksSinceCardUse[slot] = 0;
				((Player)this.owner).isCardAvailable[slot] = true;
			}
		}
		if(this.destroyAudio != null)
		{
			DJ.playSound(this.destroyAudio, 1f, this.destroyPitch, this.Center());
		}
	}

	public Texture getTexture()
	{
		if(this.customTexture != null)
			return this.customTexture;
		return Content.projectiles[this.type - 1];
	}

	public int haveBuff(int id)
	{
		for(int i = 0;i < 30;i++)
		{
			if(this.buffs[i] != null && this.buffs[i].id == id)
			{
				return i;
			}
		}
		return -1;
	}

	public int getBuffStacks(int id)
	{
		int slot = this.haveBuff(id);
		int stacks = 0;
		if(slot != -1)
		{
			stacks = this.buffs[slot].stacks;
		}
		return stacks;
	}

	public int addBuff(int id, float timeLeft, Entity origin)
	{
		return addBuff(id, 1, timeLeft, origin);
	}
	
	public int addBuff(int id, int stacks, int customStackLimit, float timeLeft, Entity origin)
	{
		int slot = this.haveBuff(id);
		if(slot != -1)
		{
			this.buffs[slot].timeLeft = timeLeft;
			this.buffs[slot].lastTimeLeft = timeLeft;
			if(this.buffs[slot].canStack)
			{
				this.buffs[slot].stacks += stacks;
				if((customStackLimit > 0 && this.buffs[slot].stacks > customStackLimit))
				{
					this.buffs[slot].stacks = customStackLimit;
				}
			}
			if(origin.getClass() == Player.class)
			{
				Player player = (Player)origin;
				this.buffs[slot].originUid = player.whoAmI;
				this.buffs[slot].originInfo = "player";
			}
			else if(origin.getClass() == Monster.class)
			{
				Monster m = (Monster)origin;
				this.buffs[slot].originUid = m.uid;
				this.buffs[slot].originInfo = "monster";
			}
		}
		else
		{
			for(int i = 0;i < 30;i++)
			{
				if(this.buffs[i] == null)
				{
					this.buffs[i] = new Buff();
					this.buffs[i].SetInfos(id, origin);
					this.buffs[i].timeLeft = timeLeft;
					this.buffs[i].lastTimeLeft = timeLeft;
					this.buffs[i].stacks = stacks;
					if((customStackLimit > 0 && this.buffs[i].stacks > customStackLimit))
					{
						this.buffs[i].stacks = customStackLimit;
					}
					if(origin.getClass() == Player.class)
					{
						Player player = (Player)origin;
						this.buffs[i].originUid = player.whoAmI;
						this.buffs[i].originInfo = "player";
					}
					else if(origin.getClass() == Monster.class)
					{
						Monster m = (Monster)origin;
						this.buffs[i].originUid = m.uid;
						this.buffs[i].originInfo = "monster";
					}
					slot = i;
					break;
				}
			}
		}
		return slot;
	}
	
	public int addBuff(int id, int stacks, float timeLeft, Entity origin)
	{
		int slot = this.haveBuff(id);
		if(slot != -1)
		{
			this.buffs[slot].timeLeft = timeLeft;
			this.buffs[slot].lastTimeLeft = timeLeft;
			if(this.buffs[slot].canStack)
			{
				this.buffs[slot].stacks += stacks;
				if((this.buffs[slot].maxStacks > 0 && this.buffs[slot].stacks > this.buffs[slot].maxStacks))
				{
					this.buffs[slot].stacks = this.buffs[slot].maxStacks;
				}
			}
			if(origin.getClass() == Player.class)
			{
				Player player = (Player)origin;
				this.buffs[slot].originUid = player.whoAmI;
				this.buffs[slot].originInfo = "player";
			}
			else if(origin.getClass() == Monster.class)
			{
				Monster m = (Monster)origin;
				this.buffs[slot].originUid = m.uid;
				this.buffs[slot].originInfo = "monster";
			}
		}
		else
		{
			for(int i = 0;i < 30;i++)
			{
				if(this.buffs[i] == null)
				{
					this.buffs[i] = new Buff();
					this.buffs[i].SetInfos(id, owner);
					this.buffs[i].timeLeft = timeLeft;
					this.buffs[i].lastTimeLeft = timeLeft;
					this.buffs[i].stacks = Math.min(stacks, this.buffs[i].maxStacks);
					if(origin.getClass() == Player.class)
					{
						Player player = (Player)origin;
						this.buffs[i].originUid = player.whoAmI;
						this.buffs[i].originInfo = "player";
					}
					else if(origin.getClass() == Monster.class)
					{
						Monster m = (Monster)origin;
						this.buffs[i].originUid = m.uid;
						this.buffs[i].originInfo = "monster";
					}
					slot = i;
					break;
				}
			}
		}
		return slot;
	}
	
	public static boolean Exists(int id)
	{
		for(Projectile p : Constant.getProjectileList())
		{
			if(p.active && p.type == id)
			{
				return true;
			}
		}
		return false;
	}

	public void removeBuff(int id)
	{
		for(int i = 0;i < 30;i++)
		{
			if(this.buffs[i] != null && this.buffs[i].id == id)
			{
				this.buffs[i] = null;
			}
		}
	}
	
	@Override
	public Vector2 Center()
	{
		return new Vector2(this.position.x + this.width/2f, this.position.y + this.height/2f);
	}
	
	public boolean canCollideWith(Monster m)
	{
		if(this.alreadyCollided.indexOf(m.uid) == -1)
			return true;
		
		return false;
	}
	
	public void addCollision(Monster m)
	{
		this.alreadyCollided.add(m.uid);
	}
	
	public Projectile setTarget(Entity e)
	{
		if(e != null)
		{
			this.target = e;
			this.targeting = true;
		}
		return this;
	}
	
	public void rescale(float scale)
	{
		this.scale *= scale;
		this.width *= scale;
		this.height *= scale;
	}
	
	public void setCustomTexture(Texture texture, int frames)
	{
		this.position.sub(this.getScaledWidth()/2f, this.getScaledHeight()/2f);
		this.customTexture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight()/frames;
		this.frames = frames;
	}
	
	@Override
	public Vector2 randomHitBoxPosition()
	{
		Vector2 pos = new Vector2(MathUtils.random(-this.width*this.scale/2f, this.width*this.scale/2f), MathUtils.random(-this.height*this.scale/2f, this.height*this.scale/2f));
		pos.rotate((float) (this.rotation + this.rotationOffset));
		pos.add(this.Center());
		return pos;
	}

}
