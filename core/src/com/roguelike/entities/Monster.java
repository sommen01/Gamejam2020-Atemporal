package com.roguelike.entities;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.ArkaClass;
import com.roguelike.constants.Buff;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Item;
import com.roguelike.constants.Objective;
import com.roguelike.constants.Quest;
import com.roguelike.constants.Skill;
import com.roguelike.game.Content;
import com.roguelike.game.DJ;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;
import com.roguelike.online.interpretations.DamageRequest;
import com.roguelike.world.GameMap;
import com.roguelike.world.Respawn;
import com.roguelike.world.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.*;

public class Monster extends Entity
{
	public int id;
	public transient int health;
	public transient int maxHealth;
	public transient int frames;
	public transient int actualFrame = 0;
	public transient int frameCounter = 0;
	public transient boolean collide = false;
	public transient boolean grounded = false;
	public int[] ai = new int[15];
	public int[] oldai = new int[15];
	public transient boolean boss = false;
	public Buff[] buffs = new Buff[30];
	public transient HashMap<Integer, Integer> aggros = new HashMap<Integer, Integer>();
	public transient int lazyUpdate;
	public transient int whoAmI;
	public transient int impactDamage;
	public transient float uncapped = 0f;
	public transient boolean overrideDraw = false;
	public transient ArrayList<Color> rColor = new ArrayList<Color>();
	public transient int target;
	public transient boolean knockbackImmune = false;
	public transient boolean shouldGravity = true;
	public transient int lastHitter;
	public int uid;
	public transient boolean natural;
	public transient int lastHitTick;
	public transient boolean centeredOrigin = false;
	public transient int movementAi;
	public transient boolean canFly = false;
	public transient boolean ranged = false;
	public transient boolean fleshandblood = false;
	public boolean swordTombSummon = false;
	public transient HashMap<Integer, Float> drops = new HashMap<Integer, Float>();
	public transient int lastHitElement = Constant.DAMAGETYPE_PHYSICAL;
	public transient int recommendedLevel = 1;

	// -------------------------------------------------------------------------------
	// Infos
	// -------------------------------------------------------------------------------

	public Monster()
	{
		super();
		for(int i = 0;i < 15;i++)
		{
			this.ai[i] = 0;
		}
		this.lazyUpdate = 90;
		this.buffs = new Buff[30];
		this.aggros = new HashMap<Integer, Integer>();
	}

	public void initialize()
	{
		this.Reset(false);
	}

	public void Reset()
	{
		this.Reset(true);
	}

	public void Reset(boolean fullReset)
	{
		if(fullReset)
			this.resetStats();

		this.target = -1;
		this.aggros = new HashMap<Integer, Integer>();
		this.rColor.clear();
		if(this.id == 1)
		{
			this.name = Main.lv("Spider", "Aranha");
			this.maxHealth = 1;
			this.health = 1;
			this.frames = 5;
			this.width = 72;
			this.height = 52;
			this.collide = true;
			this.boss = false;
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 20;
			this.ai[Constant.AI6_JUMPFORCE] = 900;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 700;
			this.impactDamage = 25;
			this.rColor.add(new Color(0.2f, 0.2f, 0.2f, 1f));
			this.rColor.add(new Color(0.3f, 0.3f, 0.3f, 1f));
			this.fleshandblood = true;
			this.recommendedLevel = 10;
		}
		else if(this.id == 2)
		{
			this.name = Main.lv("Giant Dummy", "Boneco de Testes Gigante");
			this.maxHealth = 10000000;
			this.health = 10000000;
			this.frames = 5;
			this.width = 360;
			this.height = 260;
			this.collide = true;
			this.boss = true;
			this.ai[1] = 700;
			this.impactDamage = 0;
			this.rColor.add(new Color(0.2f, 0.2f, 0.2f, 1f));
			this.rColor.add(new Color(0.3f, 0.3f, 0.3f, 1f));
			this.fleshandblood = true;
			this.shouldGravity = true;
			this.recommendedLevel = 100;
		}
		else if(this.id == 3)
		{
			this.name = Main.lv("Dummy", "Boneco de Testes");
			this.maxHealth = 100000;
			this.health = 100000;
			this.frames = 5;
			this.width = 72;
			this.height = 52;
			this.collide = true;
			this.ai[1] = 700;
			this.impactDamage = 10;
			this.rColor.add(new Color(0.2f, 0.2f, 0.2f, 1f));
			this.rColor.add(new Color(0.3f, 0.3f, 0.3f, 1f));
			this.fleshandblood = true;
			//this.knockbackImmune = true;
			this.shouldGravity = true;
			this.recommendedLevel = 100;
		}
		else if(this.id == 4)
		{
			this.name = Main.lv("Spider Queen", "Aranha Rainha");
			this.maxHealth = 5000;
			this.health = 5000;
			this.frames = 5;
			this.width = 360;
			this.height = 260;
			this.collide = true;
			this.boss = true;
			this.ai[1] = 99999;
			this.impactDamage = 15;
			this.rColor.add(new Color(0.2f, 0.2f, 0.2f, 1f));
			this.rColor.add(new Color(0.3f, 0.3f, 0.3f, 1f));
			this.fleshandblood = true;
			this.recommendedLevel = 35;
		}
		else if(this.id == 5)
		{
			this.name = Main.lv("Schizophrenia", "Esquizofrenia");
			this.maxHealth = 80000;
			this.health = 80000;
			this.frames = 1;
			this.width = 200;
			this.height = 200;
			this.overrideDraw = true;
			this.collide = false;
			this.boss = false;
			this.ai[1] = 1000;
			this.impactDamage = 270;
			this.rColor.add(new Color(0.1f, 0.1f, 0.1f, 1f));
			this.rColor.add(new Color(0.3f, 0.0f, 0.3f, 1f));
			this.recommendedLevel = 95;
		}
		else if(this.id == 6)
		{
			this.name = Main.lv("Insanity", "Insanidade");
			this.maxHealth = 60000;
			this.health = 60000;
			this.frames = 4;
			this.width = 108;
			this.height = 112;
			this.impactDamage = 200;
			this.ai[1] = 1000;
			this.rColor.add(new Color(0.5f, 0f, 0.5f, 1f));
			this.rColor.add(new Color(0.4f, 0f, 0.4f, 1f));
			this.recommendedLevel = 90;
		}
		else if(this.id == 7)
		{
			this.name = Main.lv("Lycanthrope", "Licantropo");
			this.maxHealth = 150;
			this.health = 150;
			this.frames = 5;
			this.width = 360;
			this.height = 260;
			this.collide = true;
			this.boss = true;
			this.ai[1] = 99999;
			this.impactDamage = 20;
			this.rColor.add(new Color(0.2f, 0.2f, 0.2f, 1f));
			this.rColor.add(new Color(0.3f, 0.3f, 0.3f, 1f));
			this.fleshandblood = true;
			this.recommendedLevel = 8;
		}
		else if(this.id == 8)
		{
			this.name = Main.lv("Wisp", "Fogo Fátuo");
			this.health = 22;
			this.maxHealth = 22;
			this.width = 48;
			this.height = 32;
			this.frames = 3;
			this.impactDamage = 3;
			this.shouldGravity = false;
			this.collide = false;
			this.movementAi = Constant.MOVEMENTAI_FLYINGDUMMY;
			this.ai[Constant.AI5_ACCELERATION] = 20;
			this.ai[Constant.AI5_FLYMAXSPEED] = 500;
			this.ai[Constant.AI5_SHOULDROTATE] = 1;
			this.ai[Constant.AI5_MAXFRAMECOUNTER] = 3;
			this.centeredOrigin = true;
			this.rColor.add(new Color(0.49f, 0.49f, 0.49f, 1f));
			this.rColor.add(new Color(0.68f, 0.68f, 0.68f, 1f));
			this.drops.put(121, 0.05f);
			this.recommendedLevel = 1;
		}
		else if(this.id == 9)
		{
			this.name = Main.lv("Fox", "Raposa");
			this.health = 1133;
			this.maxHealth = 1133;
			this.width = 84;
			this.height = 80;
			this.frames = 7;
			this.collide = true;
			this.impactDamage = 64;
			this.rColor.add(new Color(0.49f, 0.49f, 0.49f, 1f));
			this.rColor.add(new Color(0.68f, 0.68f, 0.68f, 1f));
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 35;
			this.ai[Constant.AI6_JUMPFORCE] = 900;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 950;
			this.fleshandblood = true;
			this.drops.put(150, 2f);
			this.recommendedLevel = 25;
		}
		else if(this.id == 10)
		{
			this.name = Main.lv("Bee", "Abelha");
			this.health = 18;
			this.maxHealth = 18;
			this.width = 44;
			this.height = 64;
			this.frames = 2;
			this.collide = false;
			this.impactDamage = 3;
			this.shouldGravity = false;
			this.rColor.add(new Color(1f, 0.8f, 0f, 1f));
			this.rColor.add(new Color(0.08f, 0.08f, 0.08f, 1f));
			this.movementAi = Constant.MOVEMENTAI_FLYAWAYANDSHOOT;
			this.ai[Constant.AI2_DISTANCE] = 500;
			this.ai[Constant.AI2_MAXDISTANCE] = 900;
			this.ai[Constant.AI2_MAXSPEED] = 300;
			this.ai[Constant.AI2_SPEEDDIV] = 3;
			this.ai[Constant.AI2_PROJECTILEID] = 20;
			this.ai[Constant.AI2_PROJECTILECOOLDOWN] = 150;
			this.ai[Constant.AI2_PROJECTILESPEED] = 1000;
			this.ai[Constant.AI2_PROJECTILEDAMAGE] = 3;
			this.fleshandblood = true;
			this.drops.put(125, 0.2f);
			this.drops.put(126, 0.6f);
			this.recommendedLevel = 1;
		}
		else if(this.id == 11)
		{
			this.name = Main.lv("Bull", "Touro");
			this.health = 60;
			this.maxHealth = 60;
			this.width = 200;
			this.height = 120;
			this.frames = 11;
			this.collide = true;
			this.impactDamage = 7;
			this.movementAi = Constant.MOVEMENTAI_CHARGEANDRUN;
			this.ai[Constant.AI1_MAXSPEED] = 700;
			this.ai[Constant.AI1_STOPDISTANCE] = 400;
			this.ai[Constant.AI1_STOPTIME] = 120;
			this.ai[Constant.AI1_SCAREDISTANCE] = 200;
			this.rColor.add(new Color(0.52f, 0.38f, 0.2f, 1f));
			this.rColor.add(new Color(0.45f, 0.31f, 0.15f, 1f));
			this.fleshandblood = true;
			this.recommendedLevel = 3;
		}
		else if(this.id == 12)
		{
			this.name = Main.lv("Reindeer", "Rena");
			this.health = 1800;
			this.maxHealth = 1800;
			this.width = 180;
			this.height = 220;
			this.frames = 12;
			this.collide = true;
			this.impactDamage = 90;
			this.ai[Constant.AI1_STOPTIME] = 90;
			this.ai[Constant.AI1_MAXSPEED] = 1200;
			this.ai[Constant.AI1_STOPDISTANCE] = 400;
			this.ai[Constant.AI1_SCAREDISTANCE] = 200;
			this.rColor.add(new Color(0.52f, 0.38f, 0.2f, 1f));
			this.rColor.add(new Color(0.45f, 0.31f, 0.15f, 1f));
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEANDRUN;
			this.fleshandblood = true;
			this.drops.put(123, 0.1f);
			this.drops.put(142, 0.6f);
			this.recommendedLevel = 30;
		}
		else if(this.id == 13)
		{
			this.name = Main.lv("Mr. Spooky", "Mr. Sustos");
			this.health = 1800;
			this.maxHealth = 1800;
			this.width = 120;
			this.height = 200;
			this.frames = 9;
			this.collide = true;
			this.impactDamage = 81;
			this.movementAi = Constant.MOVEMENTAI_CHARGEANDRUN;
			this.ai[Constant.AI1_STOPTIME] = 30;
			this.ai[Constant.AI1_MAXSPEED] = 700;
			this.ai[Constant.AI1_STOPDISTANCE] = 400;
			this.ai[Constant.AI1_SCAREDISTANCE] = 100;
			this.rColor.add(new Color(0.9f, 0.9f, 0.9f, 1f));
			this.knockbackImmune = false;
			this.shouldGravity = true;
			this.drops.put(143, 0.01f);
			this.drops.put(147, 0.6f);
			this.recommendedLevel = 30;
		}
		else if(this.id == 14)
		{
			this.name = Main.lv("Snowman", "Boneco de Neve");
			this.health = 100;
			this.maxHealth = 100;
			this.width = 104;
			this.height = 120;
			this.frames = 1;
			this.collide = true;
			this.impactDamage = 0;
			this.rColor.add(new Color(0.9f, 0.9f, 0.9f, 1f));
			this.knockbackImmune = true;
			this.shouldGravity = true;
			this.drops.put(143, 0.01f);
			this.drops.put(147, 0.6f);
		}
		else if(this.id == 15)
		{
			this.name = " ";
			this.health = 4;
			this.maxHealth = 4;
			this.width = 434;
			this.height = 960;
			this.frames = 1;
			this.collide = false;
			this.boss = false;
			this.impactDamage = 0;
			this.overrideDraw = false;
			this.rColor.add(new Color(0.35f, 0.25f, 0.05f, 1f));
			this.knockbackImmune = true;
			this.shouldGravity = true;
			this.natural = true;
		}
		else if(this.id == 16)
		{
			this.name = Main.lv("Ice Spectrum", "Espectro do Gelo");
			this.health = 618;
			this.maxHealth = 618;
			this.width = 64;
			this.height = 64;
			this.frames = 2;
			this.collide = false;
			this.boss = false;
			this.impactDamage = 64;
			this.movementAi = Constant.MOVEMENTAI_FLYAWAYANDSHOOT;
			this.ai[Constant.AI2_DISTANCE] = 500;
			this.ai[Constant.AI2_MAXDISTANCE] = 900;
			this.ai[Constant.AI2_SPEEDDIV] = 2;
			this.ai[Constant.AI2_MAXSPEED] = 600;
			this.ai[Constant.AI2_PROJECTILEID] = 19;
			this.ai[Constant.AI2_PROJECTILECOOLDOWN] = 120;
			this.ai[Constant.AI2_PROJECTILESPEED] = 1300;
			this.ai[Constant.AI2_PROJECTILEDAMAGE] = 64;
			this.rColor.add(new Color(0f, 1f, 1f, 0.3f));
			this.knockbackImmune = true;
			this.shouldGravity = false;
			this.centeredOrigin = true;
			this.recommendedLevel = 25;
		}
		else if(this.id == 17)
		{
			this.name = " ";
			this.health = 4;
			this.maxHealth = 4;
			this.width = 150;
			this.height = 740;
			this.frames = 1;
			this.collide = false;
			this.boss = false;
			this.impactDamage = 0;
			this.overrideDraw = false;
			this.rColor.add(new Color(0.35f, 0.25f, 0.05f, 1f));
			this.knockbackImmune = true;
			this.shouldGravity = true;
			this.natural = true;
			this.drops.put(87, 0.01f);
			this.drops.put(127, 0.05f);
			this.drops.put(137, 0.4f);
		}
		else if(this.id == 18)
		{
			this.name = Main.lv("Macaw", "Arara");
			this.health = 75;
			this.maxHealth = 75;
			this.width = 96;
			this.height = 60;
			this.frames = 3;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 13;
			this.movementAi = Constant.MOVEMENTAI_FLYINGKNIGHT;
			this.ai[Constant.AI3_DASHSPEED] = 780;
			this.ai[Constant.AI3_FLYSPEED] = 400;
			this.ai[Constant.AI3_KEEPHEIGHT] = 650;
			this.rColor.add(Color.RED);
			this.rColor.add(Color.RED);
			this.rColor.add(Color.RED);
			this.rColor.add(Color.BLUE);
			this.shouldGravity = false;
			this.centeredOrigin = true;
			this.fleshandblood = true;
			this.recommendedLevel = 5;
			this.drops.put(186, 0.6f);
			this.drops.put(187, 0.05f);
		}
		else if(this.id == 19)
		{
			this.name = Main.lv("Bat", "Morcego");
			this.health = 156;
			this.maxHealth = 156;
			this.width = 112;
			this.height = 56;
			this.frames = 4;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 24;
			this.overrideDraw = false;
			this.rColor.add(new Color(0.4f, 0.3f, 0.3f, 1f));
			this.knockbackImmune = false;
			this.shouldGravity = false;
			this.movementAi = Constant.MOVEMENTAI_FLYINGDUMMY;
			this.ai[Constant.AI5_ACCELERATION] = 30;
			this.ai[Constant.AI5_FLYMAXSPEED] = 700;
			this.ai[Constant.AI5_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI5_SHOULDROTATE] = 0;
			this.fleshandblood = true;
			this.recommendedLevel = 8;
		}
		else if(this.id == 20)
		{
			this.name = Main.lv("Ogre", "Ogro");
			this.maxHealth = 152;
			this.health = 152;
			this.width = 148;
			this.height = 144;
			this.frames = 20;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 17;
			this.rColor.add(new Color(0f, 0.5f, 0.2f, 1f));
			this.rColor.add(new Color(0f, 0.5f, 0.1f, 1f));
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGETIME] = 20;
			this.ai[Constant.AI4_DASHDISACCEL] = 96;
			this.ai[Constant.AI4_DASHSPEED] = 2100;
			this.ai[Constant.AI4_DISTTOCHARGE] = 150;
			this.ai[Constant.AI4_WALKSPEED] = 600;
			this.ai[Constant.AI4_WALKFINALFRAME] = 9;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 16;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.fleshandblood = true;
			this.recommendedLevel = 8;
		}
		else if(this.id == 21)
		{
			this.name = Main.lv("Stone Golemite", "Golemita de Pedra");
			this.health = 229;
			this.maxHealth = 229;
			this.width = 80;
			this.height = 80;
			this.frames = 17;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 24;
			this.rColor.add(new Color(0.2f, 0.2f, 0.2f, 1f));
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 11;
			this.ai[Constant.AI4_CHARGETIME] = 11;
			this.ai[Constant.AI4_DASHDISACCEL] = 95;
			this.ai[Constant.AI4_DASHSPEED] = 2400;
			this.ai[Constant.AI4_DISTTOCHARGE] = 100;
			this.ai[Constant.AI4_WALKFINALFRAME] = 8;
			this.ai[Constant.AI4_WALKSPEED] = 800;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.recommendedLevel = 13;
		}
		else if(this.id == 22)
		{
			this.name = Main.lv("Minotaur", "Minotauro");
			this.health = 229;
			this.maxHealth = 229;
			this.width = 80;
			this.height = 100;
			this.frames = 8;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 24;
			this.rColor.add(new Color(0.65f, 0.3f, 0.1f, 1f));
			this.knockbackImmune = false;
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 30;
			this.ai[Constant.AI6_JUMPFORCE] = 1000;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 1000;
			this.fleshandblood = true;
			this.recommendedLevel = 13;
			this.drops.put(188, 0.1f);
		}
		else if(this.id == 23)
		{
			this.name = Main.lv("Scarecrow", "Espantalho");
			this.health = 46;
			this.maxHealth = 46;
			this.width = 104;
			this.height = 100;
			this.frames = 7;
			this.collide = true;
			this.impactDamage = 5;
			this.rColor.add(new Color(0.5f, 0.3f, 0f, 1f));
			this.rColor.add(new Color(0.8f, 0.6f, 0.3f, 1f));
			this.rColor.add(new Color(1f, 0f, 0f, 1f));
			this.knockbackImmune = false;
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEANDRUN;
			this.ai[Constant.AI1_MAXSPEED] = 500;
			this.ai[Constant.AI1_STOPDISTANCE] = 400;
			this.ai[Constant.AI1_STOPTIME] = 100;
			this.ai[Constant.AI1_SCAREDISTANCE] = 153;
			this.drops.put(136, 0.01f);
			this.recommendedLevel = 1;
		}
		else if(this.id == 24)
		{
			this.name = Main.lv("Harpy", "Harpia");
			this.health = 1400;
			this.maxHealth = 1400;
			this.width = 140;
			this.height = 92;
			this.frames = 4;
			this.collide = true;
			this.impactDamage = 180;
			this.rColor.add(new Color(0.6f, 0.3f, 0.15f, 1f));
			this.rColor.add(new Color(0.65f, 0.45f, 0.25f, 1f));
			this.shouldGravity = false;
			this.movementAi = Constant.MOVEMENTAI_FLYINGKNIGHT;
			this.ai[Constant.AI3_KEEPHEIGHT] = 800;
			this.ai[Constant.AI3_FLYSPEED] = 600;
			this.ai[Constant.AI3_DASHSPEED] = 900;
			this.centeredOrigin = true;
			this.fleshandblood = true;
			this.recommendedLevel = 35;
		}
		else if(this.id == 25)
		{
			this.name = Main.lv("Slime", "Slime");
			this.health = 33;
			this.maxHealth = 33;
			this.width = 72;
			this.height = 64;
			this.frames = 8;
			this.collide = true;
			this.impactDamage = 3;
			this.rColor.add(new Color(0f, 0.8f, 0f, 1f));
			this.knockbackImmune = false;
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 20;
			this.ai[Constant.AI6_JUMPFORCE] = 900;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 600;
			this.drops.put(113, 0.6f);
			this.recommendedLevel = 1;
		}
		else if(this.id == 26)
		{
			this.name = Main.lv("Bear", "Urso");
			this.health = 130;
			this.maxHealth = 130;
			this.width = 208;
			this.height = 116;
			this.frames = 8;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 15;
			this.rColor.add(new Color(0.6f, 0.4f, 0.1f, 1f));
			this.knockbackImmune = true;
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 40;
			this.ai[Constant.AI6_JUMPFORCE] = 1000;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 3;
			this.ai[Constant.AI6_MAXSPEED] = 600;
			this.fleshandblood = true;
			this.drops.put(130, 0.6f);
			this.recommendedLevel = 7;
		}
		else if(this.id == 27)
		{
			this.name = Main.lv("Fairy", "Fada");
			this.health = 75;
			this.maxHealth = 75;
			this.width = 64;
			this.height = 64;
			this.frames = 2;
			this.collide = true;
			this.impactDamage = 13;
			this.rColor.add(new Color(0.9f, 0.9f, 0.9f, 1f));
			this.shouldGravity = false;
			this.centeredOrigin = true;
			this.movementAi = Constant.MOVEMENTAI_FLYINGDUMMY;
			this.ai[Constant.AI5_ACCELERATION] = 30;
			this.ai[Constant.AI5_FLYMAXSPEED] = 700;
			this.ai[Constant.AI5_MAXFRAMECOUNTER] = 1;
			this.ai[Constant.AI5_SHOULDROTATE] = 1;
			this.recommendedLevel = 5;
			this.drops.put(185, 0.6f);
		}
		else if(this.id == 28)
		{
			this.name = Main.lv("Magic Leaf Golemite", "Golemita de Folhas Mágicas");
			this.health = 111;
			this.maxHealth = 111;
			this.width = 80;
			this.height = 80;
			this.frames = 16;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 13;
			this.rColor.add(new Color(0f, 0.8f, 0f, 1f));
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 11;
			this.ai[Constant.AI4_CHARGETIME] = 11;
			this.ai[Constant.AI4_DASHDISACCEL] = 95;
			this.ai[Constant.AI4_DASHSPEED] = 2100;
			this.ai[Constant.AI4_DISTTOCHARGE] = 100;
			this.ai[Constant.AI4_WALKFINALFRAME] = 8;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_WALKSPEED] = 700;
			this.recommendedLevel = 5;
		}
		else if(this.id == 29)
		{
			this.name = Main.lv("Black Panther", "Pantera Negra");
			this.health = 130;
			this.maxHealth = 130;
			this.width = 208;
			this.height = 128;
			this.frames = 16;
			this.collide = true;
			this.impactDamage = 15;	
			this.rColor.add(new Color(0.08f, 0.08f, 0.1f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 11;
			this.ai[Constant.AI4_CHARGETIME] = 25;
			this.ai[Constant.AI4_DASHDISACCEL] = 96;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_DASHSPEED] = 2100;
			this.ai[Constant.AI4_DISTTOCHARGE] = 200;
			this.ai[Constant.AI4_WALKFINALFRAME] = 8;
			this.ai[Constant.AI4_WALKSPEED] = 800;
			this.fleshandblood = true;
			this.drops.put(135, 0.6f);
			this.recommendedLevel = 5;
		}
		else if(this.id == 30)
		{
			this.name = Main.lv("Orc Berserker", "Orc Furioso");
			this.health = 229;
			this.maxHealth = 229;
			this.width = 68;
			this.height = 100;
			this.frames = 17;
			this.collide = true;
			this.impactDamage = 24;
			this.rColor.add(new Color(0.1f, 0.6f, 0.15f, 1f));
			this.rColor.add(new Color(0.6f, 0.3f, 0.1f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 12;
			this.ai[Constant.AI4_CHARGETIME] = 15;
			this.ai[Constant.AI4_DASHDISACCEL] = 95;
			this.ai[Constant.AI4_DASHSPEED] = 1900;
			this.ai[Constant.AI4_DISTTOCHARGE] = 100;
			this.ai[Constant.AI4_WALKFINALFRAME] = 7;
			this.ai[Constant.AI4_WALKSPEED] = 800;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.fleshandblood = true;
			this.drops.put(131, 0.01f);
			this.recommendedLevel = 10;
		}
		else if(this.id == 31)
		{
			this.name = Main.lv("Naja", "Naja");
			this.health = 300;
			this.maxHealth = 300;
			this.width = 62;
			this.height = 76;
			this.frames = 7;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 40;
			this.knockbackImmune = true;
			this.shouldGravity = true;
			this.rColor.add(new Color(0.65f, 0.6f, 0.5f, 1f));
			this.rColor.add(new Color(0.65f, 0.6f, 0.5f, 1f));
			this.rColor.add(new Color(0.85f, 0.65f, 0.4f, 1f));
			this.movementAi = Constant.MOVEMENTAI_SHOOTINGSENTRY;
			this.ai[Constant.AI7_PROJECTILECOOLDOWN] = 90;
			this.ai[Constant.AI7_PROJECTILEDAMAGE] = 55;
			this.ai[Constant.AI7_PROJECTILEID] = 21;
			this.ai[Constant.AI7_PROJECTILESPEED] = 1300;
			this.ai[Constant.AI7_SHOOTRANGE] = 1000;
			this.fleshandblood = true;
			this.recommendedLevel = 35;
		}
		else if(this.id == 32)
		{
			this.name = Main.lv("Goblin Assassin", "Goblin Assassino");
			this.health = 697;
			this.maxHealth = 697;
			this.width = 30;
			this.height = 50;
			this.frames = 4;
			this.collide = true;
			this.impactDamage = 49;
			this.rColor.add(new Color(0f, 0.3f, 0f, 1f));
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 40;
			this.ai[Constant.AI6_JUMPFORCE] = 1000;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 750;
			this.fleshandblood = true;
			this.recommendedLevel = 20;
		}
		else if(this.id == 33)
		{
			this.name = Main.lv("Void Spectrum", "Espectro do Vazio");
			this.health = 200;
			this.maxHealth = 200;
			this.width = 64;
			this.height = 64;
			this.frames = 1;
			this.collide = false;
			this.impactDamage = 22;
			this.rColor.add(new Color(0.02f, 0.03f, 0.05f, 1f));
			this.knockbackImmune = true;
			this.shouldGravity = false;
			this.centeredOrigin = true;
			this.overrideDraw = true;
			this.movementAi = Constant.MOVEMENTAI_FLYINGDUMMY;
			this.ai[Constant.AI5_ACCELERATION] = 50;
			this.ai[Constant.AI5_FLYMAXSPEED] = 700;
			this.ai[Constant.AI5_MAXFRAMECOUNTER] = 1;
			this.ai[Constant.AI5_SHOULDROTATE] = 0;
			this.recommendedLevel = 10;
		}
		else if(this.id == 34)
		{
			this.name = Main.lv("Christmas Elf", "Elfo de Natal");
			this.health = 1300;
			this.maxHealth = 1300;
			this.width = 30;
			this.height = 65;
			this.frames = 4;
			this.collide = true;
			this.impactDamage = 81;
			this.rColor.add(new Color(1f, 0.7f, 0.4f, 1f));
			this.rColor.add(new Color(0.1f, 0.7f, 0.1f, 1f));
			this.rColor.add(new Color(0.9f, 0f, 0f, 1f));
			this.movementAi = Constant.MOVEMENTAI_MARKSMAN2K18LUL;
			this.ai[Constant.AI8_MAXSPEED] = 750;
			this.ai[Constant.AI8_MONSTEREXTRAFRAMES] = 8;
			this.ai[Constant.AI8_PROJECTILECOOLDOWN] = 120;
			this.ai[Constant.AI8_PROJECTILEDAMAGE] = 81;
			this.ai[Constant.AI8_PROJECTILEID] = 22;
			this.ai[Constant.AI8_PROJECTILESPEED] = 1000;
			this.ai[Constant.AI8_MONSTEREXTRAID] = 0;
			this.ai[Constant.AI8_KEEPDISTANCE] = 500;
			this.ai[Constant.AI8_CAITMAXSPEED] = 270;
			this.fleshandblood = true;
			this.drops.put(144, 2f);
			this.recommendedLevel = 30;
		}
		else if(this.id == 35)
		{
			this.name = Main.lv("Cyclops", "Ciclópe");
			this.health = 229;
			this.maxHealth = 229;
			this.width = 120;
			this.height = 160;
			this.frames = 15;
			this.collide = true;
			this.impactDamage = 30;
			this.rColor.add(new Color(1f, 0.75f, 0.4f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 10;
			this.ai[Constant.AI4_CHARGETIME] = 5;
			this.ai[Constant.AI4_DASHDISACCEL] = 96;
			this.ai[Constant.AI4_DASHSPEED] = 1900;
			this.ai[Constant.AI4_DISTTOCHARGE] = 150;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_WALKFINALFRAME] = 7;
			this.ai[Constant.AI4_WALKSPEED] = 800;
			this.fleshandblood = true;
			this.drops.put(124, 0.01f);
			this.recommendedLevel = 13;
		}
		else if(this.id == 36)
		{
			this.name = Main.lv("Wandering Bush", "Arbusto Errante");
			this.health = 33;
			this.maxHealth = 33;
			this.width = 64;
			this.height = 56;
			this.frames = 4;
			this.collide = true;
			this.impactDamage = 3;
			this.rColor.add(new Color(0f, 0.5f, 0f, 1f));
			this.rColor.add(new Color(0f, 0.65f, 0f, 1f));
			this.rColor.add(new Color(0f, 0.8f, 0f, 1f));
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 20;
			this.ai[Constant.AI6_JUMPFORCE] = 900;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 600;
			this.drops.put(114, 0.6f);
			this.recommendedLevel = 1;
		}
		else if(this.id == 37)
		{
			this.name = Main.lv("Goblin in a Tire", "Goblin em um Pneu");
			this.health = 849;
			this.maxHealth = 849;
			this.width = 30;
			this.height = 50;
			this.frames = 4;
			this.collide = true;
			this.impactDamage = 55;
			this.rColor.add(new Color(0f, 0.8f, 0f, 1f));
			this.rColor.add(new Color(0.2f, 0.2f, 0.2f, 1f));
			this.knockbackImmune = true;
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 20;
			this.ai[Constant.AI6_JUMPFORCE] = 1000;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 1000;
			this.fleshandblood = true;
			this.recommendedLevel = 20;
		}
		else if(this.id == 38)
		{
			this.name = Main.lv("Celestial Knight", "Cavaleiro Celestial");
			this.health = 19500;
			this.maxHealth = 19500;
			this.impactDamage = 450;
			this.width = 136;
			this.height = 236;
			this.frames = 11;
			this.collide = true;
			this.rColor.add(new Color(0f, 0.9f, 0.8f, 1f));
			this.rColor.add(new Color(0.9f, 0.9f, 0.9f, 1f));
			this.knockbackImmune = true;
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 36;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_JUMPFORCE] = 1060;
			this.ai[Constant.AI6_MAXSPEED] = 1000;
			this.recommendedLevel = 60;
		}
		else if(this.id == 39)
		{
			this.name = Main.lv("Sand Golemite", "Golemita de Areia");
			this.width = 80;
			this.height = 72;
			this.health = 987;
			this.maxHealth = 987;
			this.impactDamage = 140;
			this.frames = 16;
			this.collide = true;
			this.rColor.add(new Color(1f, 0.95f, 0.6f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 11;
			this.ai[Constant.AI4_CHARGETIME] = 15;
			this.ai[Constant.AI4_DASHDISACCEL] = 96;
			this.ai[Constant.AI4_DASHSPEED] = 2020;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_DISTTOCHARGE] = 160;
			this.ai[Constant.AI4_WALKFINALFRAME] = 7;
			this.ai[Constant.AI4_WALKSPEED] = 800;
			this.recommendedLevel = 35;
		}
		else if(this.id == 40)
		{
			this.name = Main.lv("Ice Golemite", "Golemita de Gelo");
			this.width = 80;
			this.height = 72;
			this.health = 1500;
			this.maxHealth = 1500;
			this.impactDamage = 75;
			this.frames = 16;
			this.collide = true;
			this.rColor.add(new Color(0f, 0.85f, 1f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 11;
			this.ai[Constant.AI4_CHARGETIME] = 20;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_DASHDISACCEL] = 96;
			this.ai[Constant.AI4_DASHSPEED] = 1940;
			this.ai[Constant.AI4_DISTTOCHARGE] = 170;
			this.ai[Constant.AI4_WALKFINALFRAME] = 7;
			this.ai[Constant.AI4_WALKSPEED] = 750;
			this.recommendedLevel = 28;
		}
		else if(this.id == 41)
		{
			this.name = Main.lv("Sheriff Cactus", "Xerife Cacto");
			this.health = 1000;
			this.maxHealth = 1000;
			this.width = 88;
			this.height = 132;
			this.frames = 9;
			this.collide = true;
			this.impactDamage = 140;
			this.rColor.add(new Color(0f, 0.6f, 0f, 1f));
			this.knockbackImmune = false;
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEANDRUN;
			this.ai[Constant.AI1_MAXSPEED] = 1000;
			this.ai[Constant.AI1_STOPDISTANCE] = 100;
			this.ai[Constant.AI1_STOPTIME] = 380;
			this.ai[Constant.AI1_SCAREDISTANCE] = 270;
			this.recommendedLevel = 35;
		}
		else if(this.id == 42)
		{
			this.name = Main.lv("Shaman Goblin", "Goblin Xamã");
			this.health = 507;
			this.maxHealth = 507;
			this.width = 50;
			this.height = 56;
			this.frames = 4;
			this.collide = true;
			this.impactDamage = 49;
			this.rColor.add(new Color(0f, 0.6f, 0f, 1f));
			this.rColor.add(new Color(0.35f, 0f, 0.45f, 1f));
			this.movementAi = Constant.MOVEMENTAI_MARKSMAN2K18LUL;
			this.ai[Constant.AI8_KEEPDISTANCE] = 490;
			this.ai[Constant.AI8_MAXSPEED] = 700;
			this.ai[Constant.AI8_MONSTEREXTRAFRAMES] = 8;
			this.ai[Constant.AI8_MONSTEREXTRAID] = 3;
			this.ai[Constant.AI8_PROJECTILECOOLDOWN] = 120;
			this.ai[Constant.AI8_PROJECTILEDAMAGE] = 35;
			this.ai[Constant.AI8_PROJECTILEID] = 121;
			this.ai[Constant.AI8_PROJECTILESPEED] = 1200;
			this.ai[Constant.AI8_CAITMAXSPEED] = 200;
			this.fleshandblood = true;
			this.recommendedLevel = 20;
			this.drops.put(184, 0.01f);
		}
		else if(this.id == 43)
		{
			this.name = Main.lv("Hellfire Demon Kitty", "Gatinho Inocente Demoníaco de Fogo");
			this.health = 4480;
			this.maxHealth = 4480;
			this.impactDamage = 240;
			this.width = 32;
			this.height = 28;
			this.frames = 6;
			this.collide = true;
			this.rColor.add(new Color(1f, 0.85f, 0f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 4;
			this.ai[Constant.AI4_CHARGETIME] = 5;
			this.ai[Constant.AI4_DASHDISACCEL] = 96;
			this.ai[Constant.AI4_DASHSPEED] = 2000;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_DISTTOCHARGE] = 100;
			this.ai[Constant.AI4_WALKFINALFRAME] = 3;
			this.ai[Constant.AI4_WALKSPEED] = 1000;
			this.recommendedLevel = 70;
		}
		else if(this.id == 44)
		{
			this.name = Main.lv("Ranger Goblin", "Goblin Atirador");
			this.health = 507;
			this.maxHealth = 507;
			this.width = 50;
			this.height = 56;
			this.frames = 4;
			this.collide = true;
			this.impactDamage = 49;
			this.rColor.add(new Color(0.65f, 0.3f, 0.15f, 1f));
			this.rColor.add(new Color(0.35f, 0f, 0.45f, 1f));
			this.movementAi = Constant.MOVEMENTAI_MARKSMAN2K18LUL;
			this.ai[Constant.AI8_KEEPDISTANCE] = 550;
			this.ai[Constant.AI8_MAXSPEED] = 700;
			this.ai[Constant.AI8_MONSTEREXTRAFRAMES] = 6;
			this.ai[Constant.AI8_MONSTEREXTRAID] = 4;
			this.ai[Constant.AI8_PROJECTILECOOLDOWN] = 150;
			this.ai[Constant.AI8_PROJECTILEDAMAGE] = 49;
			this.ai[Constant.AI8_PROJECTILEID] = 20;
			this.ai[Constant.AI8_PROJECTILESPEED] = 2500;
			this.ai[Constant.AI8_CAITMAXSPEED] = 200;
			this.fleshandblood = true;
			this.recommendedLevel = 20;
		}
		else if(this.id == 45)
		{
			this.name = Main.lv("Bubble Golemite", "Golemita de Bolhas");
			this.width = 80;
			this.height = 72;
			this.health = 800;
			this.maxHealth = 800;
			this.impactDamage = 220;
			this.frames = 16;
			this.collide = true;
			this.rColor.add(new Color(0.95f, 0.95f, 0.95f, 1f));
			this.rColor.add(new Color(0f, 0.4f, 1f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 11;
			this.ai[Constant.AI4_CHARGETIME] = 23;
			this.ai[Constant.AI4_DASHDISACCEL] = 95;
			this.ai[Constant.AI4_DASHSPEED] = 2100;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_DISTTOCHARGE] = 150;
			this.ai[Constant.AI4_WALKFINALFRAME] = 7;
			this.ai[Constant.AI4_WALKSPEED] = 850;
			this.recommendedLevel = 30;
		}
		else if(this.id == 46)
		{
			this.name = " ";
			this.health = 2;
			this.maxHealth = 2;
			this.width = 72;
			this.height = 44;
			this.frames = 1;
			this.collide = false;
			this.boss = false;
			this.impactDamage = 0;
			this.overrideDraw = false;
			this.rColor.add(new Color(0.6f, 0.6f, 0.6f, 1f));
			this.rColor.add(new Color(0.7f, 0.7f, 0.7f, 1f));
			this.knockbackImmune = true;
			this.shouldGravity = true;
			this.natural = true;

			this.drops.put(83, 3f);
		}
		else if(this.id == 47)
		{
			this.name = Main.lv("Iron Ore", "Minério de Ferro");
			this.health = 5;
			this.maxHealth = 5;
			this.width = 72;
			this.height = 72;
			this.frames = 1;
			this.collide = true;
			this.impactDamage = 0;
			this.knockbackImmune = true;
			this.natural = true;
			this.rColor.add(new Color(0.14f, 0.14f, 0.14f, 1f));
			this.rColor.add(new Color(0.1f, 0.1f, 0.1f, 1f));
			this.rColor.add(new Color(0.14f, 0.14f, 0.14f, 1f));
			this.rColor.add(new Color(0.1f, 0.1f, 0.1f, 1f));
			this.rColor.add(new Color(0.6f, 0.6f, 0.6f, 1f));

			this.drops.put(78, 3f);
		}
		else if(this.id == 48)
		{
			this.name = Main.lv("Gold Ore", "Minério de Ouro");
			this.health = 8;
			this.maxHealth = 8;
			this.width = 72;
			this.height = 72;
			this.frames = 1;
			this.collide = true;
			this.impactDamage = 0;
			this.knockbackImmune = true;
			this.natural = true;
			this.rColor.add(new Color(0.14f, 0.14f, 0.14f, 1f));
			this.rColor.add(new Color(0.1f, 0.1f, 0.1f, 1f));
			this.rColor.add(new Color(0.14f, 0.14f, 0.14f, 1f));
			this.rColor.add(new Color(0.1f, 0.1f, 0.1f, 1f));
			this.rColor.add(new Color(1f, 1f, 0.2f, 1f));

			this.drops.put(82, 3f);
		}
		else if(this.id == 49)
		{
			this.name = Main.lv("Brotaur", "Manotouro");
			this.health = 687;
			this.maxHealth = 687;
			this.width = 100;
			this.height = 120;
			this.frames = 12;
			this.collide = true;
			this.impactDamage = 48;
			this.rColor.add(new Color(0.62f, 0.3f, 0.1f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 10;
			this.ai[Constant.AI4_CHARGETIME] = 10;
			this.ai[Constant.AI4_DASHDISACCEL] = 90;
			this.ai[Constant.AI4_DASHSPEED] = 5000;
			this.ai[Constant.AI4_DISTTOCHARGE] = 500;
			this.ai[Constant.AI4_WALKFINALFRAME] = 6;
			this.ai[Constant.AI4_WALKSPEED] = 1000;
			this.boss = true;
			this.recommendedLevel = 16;
		}
		else if(this.id == 50)
		{
			this.name = Main.lv("Scariercrow", "Espanto");
			this.health = 697;
			this.maxHealth = 697;
			this.width = 104;
			this.height = 100;
			this.frames = 7;
			this.collide = true;
			this.impactDamage = 49;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.knockbackImmune = false;
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEANDRUN;
			this.ai[Constant.AI1_MAXSPEED] = 875;
			this.ai[Constant.AI1_STOPDISTANCE] = 440;
			this.ai[Constant.AI1_STOPTIME] = 95;
			this.ai[Constant.AI1_SCAREDISTANCE] = 210;
			this.drops.put(168, 0.7f);
			this.recommendedLevel = 20;
		}
		else if(this.id == 51)
		{
			this.name = Main.lv("Ghost Bull", "Touro Fantasma");
			this.health = 849;
			this.maxHealth = 849;
			this.width = 200;
			this.height = 120;
			this.frames = 11;
			this.collide = true;
			this.impactDamage = 75;
			this.movementAi = Constant.MOVEMENTAI_CHARGEANDRUN;
			this.ai[Constant.AI1_MAXSPEED] = 906;
			this.ai[Constant.AI1_STOPDISTANCE] = 425;
			this.ai[Constant.AI1_STOPTIME] = 94;
			this.ai[Constant.AI1_SCAREDISTANCE] = 225;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.fleshandblood = true;
			this.drops.put(168, 0.7f);
			this.recommendedLevel = 23;
		}
		else if(this.id == 52)
		{
			this.name = "Apoidea";
			this.health = 380;
			this.maxHealth = 380;
			this.width = 90;
			this.height = 86;
			this.frames = 2;
			this.centeredOrigin = true;
			this.collide = false;
			this.impactDamage = 49;
			this.shouldGravity = false;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.movementAi = Constant.MOVEMENTAI_FLYAWAYANDSHOOT;
			this.ai[Constant.AI2_DISTANCE] = 600;
			this.ai[Constant.AI2_MAXDISTANCE] = 900;
			this.ai[Constant.AI2_MAXSPEED] = 600;
			this.ai[Constant.AI2_SPEEDDIV] = 5;
			this.ai[Constant.AI2_PROJECTILEID] = 20;
			this.ai[Constant.AI2_PROJECTILECOOLDOWN] = 150;
			this.ai[Constant.AI2_PROJECTILESPEED] = 1000;
			this.ai[Constant.AI2_PROJECTILEDAMAGE] = 55;
			this.fleshandblood = true;
			this.drops.put(168, 0.7f);
			this.recommendedLevel = 20;
		}
		else if(this.id == 53)
		{
			this.name = Main.lv("Wandering Spirit", "Espirito Errante");
			this.health = 697;
			this.maxHealth = 697;
			this.width = 64;
			this.height = 56;
			this.frames = 4;
			this.collide = true;
			this.impactDamage = 49;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 24;
			this.ai[Constant.AI6_JUMPFORCE] = 940;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 700;
			this.drops.put(168, 0.7f);
			this.recommendedLevel = 20;
		}
		else if(this.id == 54)
		{
			this.name = Main.lv("Ectoplasm Slime", "Slime de Ectoplasma");
			this.health = 697;
			this.maxHealth = 697;
			this.width = 72;
			this.height = 64;
			this.frames = 8;
			this.collide = true;
			this.impactDamage = 49;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.knockbackImmune = false;
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 24;
			this.ai[Constant.AI6_JUMPFORCE] = 940;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 700;
			this.drops.put(168, 0.7f);
			this.recommendedLevel = 20;
		}
		else if(this.id == 55)
		{
			this.name = Main.lv("Maw", "Ar");
			this.health = 772;
			this.maxHealth = 772;
			this.width = 96;
			this.height = 60;
			this.frames = 3;
			this.collide = true;
			this.impactDamage = 64;
			this.movementAi = Constant.MOVEMENTAI_FLYINGKNIGHT;
			this.ai[Constant.AI3_DASHSPEED] = 900;
			this.ai[Constant.AI3_FLYSPEED] = 400;
			this.ai[Constant.AI3_KEEPHEIGHT] = 700;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.shouldGravity = false;
			this.centeredOrigin = true;
			this.fleshandblood = true;
			this.drops.put(168, 1.5f);
			this.recommendedLevel = 25;
		}
		else if(this.id == 56)
		{
			this.name = Main.lv("Ghost Panther", "Pantera Fantasma");
			this.health = 1498;
			this.maxHealth = 1498;
			this.width = 208;
			this.height = 128;
			this.frames = 16;
			this.collide = true;
			this.impactDamage = 75;	
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 11;
			this.ai[Constant.AI4_CHARGETIME] = 25;
			this.ai[Constant.AI4_DASHDISACCEL] = 96;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_DASHSPEED] = 2100;
			this.ai[Constant.AI4_DISTTOCHARGE] = 175;
			this.ai[Constant.AI4_WALKFINALFRAME] = 8;
			this.ai[Constant.AI4_WALKSPEED] = 850;
			this.fleshandblood = true;
			this.drops.put(168, 1.5f);
			this.recommendedLevel = 30;
		}
		else if(this.id == 57)
		{
			this.name = Main.lv("Swamp Spirit", "Espírito do Pântano");
			this.maxHealth = 1798;
			this.health = 1798;
			this.width = 148;
			this.height = 144;
			this.frames = 20;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 90;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGETIME] = 20;
			this.ai[Constant.AI4_DASHDISACCEL] = 96;
			this.ai[Constant.AI4_DASHSPEED] = 2140;
			this.ai[Constant.AI4_DISTTOCHARGE] = 170;
			this.ai[Constant.AI4_WALKSPEED] = 750;
			this.ai[Constant.AI4_WALKFINALFRAME] = 9;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 16;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.fleshandblood = true;
			this.drops.put(168, 1.5f);
			this.recommendedLevel = 30;
		}
		else if(this.id == 58)
		{
			this.name = Main.lv("Leaf Guardian", "Guardião das Folhas");
			this.health = 1300;
			this.maxHealth = 1300;
			this.width = 80;
			this.height = 80;
			this.frames = 16;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 60;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 11;
			this.ai[Constant.AI4_CHARGETIME] = 11;
			this.ai[Constant.AI4_DASHDISACCEL] = 95;
			this.ai[Constant.AI4_DASHSPEED] = 2100;
			this.ai[Constant.AI4_DISTTOCHARGE] = 100;
			this.ai[Constant.AI4_WALKFINALFRAME] = 8;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_WALKSPEED] = 725;
			this.drops.put(168, 1.5f);
			this.recommendedLevel = 28;
		}
		else if(this.id == 59)
		{
			this.name = Main.lv("Spirit of Annoyance", "Espírito do Incômodo");
			this.health = 772;
			this.maxHealth = 772;
			this.width = 64;
			this.height = 64;
			this.frames = 2;
			this.collide = true;
			this.impactDamage = 75;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.shouldGravity = false;
			this.centeredOrigin = true;
			this.movementAi = Constant.MOVEMENTAI_FLYINGDUMMY;
			this.ai[Constant.AI5_ACCELERATION] = 30;
			this.ai[Constant.AI5_FLYMAXSPEED] = 725;
			this.ai[Constant.AI5_MAXFRAMECOUNTER] = 1;
			this.ai[Constant.AI5_SHOULDROTATE] = 1;
			this.drops.put(168, 1.5f);
			this.recommendedLevel = 28;
		}
		else if(this.id == 60)
		{
			this.name = Main.lv("Spirit of Patience", "Espírito da Paciência");
			this.health = 1133;
			this.maxHealth = 1133;
			this.width = 208;
			this.height = 116;
			this.frames = 8;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 75;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.knockbackImmune = true;
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 50;
			this.ai[Constant.AI6_JUMPFORCE] = 950;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 3;
			this.ai[Constant.AI6_MAXSPEED] = 725;
			this.fleshandblood = true;
			this.drops.put(168, 1.5f);
			this.recommendedLevel = 26;
		}
		else if(this.id == 61)
		{
			this.name = " ";
			this.health = 1000;
			this.maxHealth = 1000;
			this.width = 150;
			this.height = 740;
			this.frames = 1;
			this.collide = false;
			this.boss = false;
			this.impactDamage = 0;
			this.overrideDraw = false;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.rColor.add(Color.WHITE);
			this.knockbackImmune = true;
			this.shouldGravity = true;
			this.natural = true;
			this.drops.put(168, 1.5f);
		}
		else if(this.id == 62)
		{
			this.name = Main.lv("Stone Guardian", "Guardião das Pedras");
			this.health = 1798;
			this.maxHealth = 1798;
			this.width = 80;
			this.height = 80;
			this.frames = 17;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 90;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 11;
			this.ai[Constant.AI4_CHARGETIME] = 11;
			this.ai[Constant.AI4_DASHDISACCEL] = 95;
			this.ai[Constant.AI4_DASHSPEED] = 2400;
			this.ai[Constant.AI4_DISTTOCHARGE] = 100;
			this.ai[Constant.AI4_WALKFINALFRAME] = 8;
			this.ai[Constant.AI4_WALKSPEED] = 850;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.drops.put(168, 2.5f);
			this.recommendedLevel = 30;
		}
		else if(this.id == 63)
		{
			this.name = Main.lv("Minoghost", "Minotasma");
			this.health = 1798;
			this.maxHealth = 1798;
			this.width = 80;
			this.height = 100;
			this.frames = 8;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 90;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.knockbackImmune = false;
			this.shouldGravity = true;
			this.movementAi = Constant.MOVEMENTAI_WALKINGDUMMY;
			this.ai[Constant.AI6_ACCELERATION] = 30;
			this.ai[Constant.AI6_JUMPFORCE] = 1000;
			this.ai[Constant.AI6_MAXFRAMECOUNTER] = 2;
			this.ai[Constant.AI6_MAXSPEED] = 1050;
			this.fleshandblood = true;
			this.drops.put(168, 2.5f);
			this.recommendedLevel = 30;
		}
		else if(this.id == 64)
		{
			this.name = Main.lv("Vengeful Spirit", "Espírito Vingativo");
			this.health = 1500;
			this.maxHealth = 1500;
			this.width = 68;
			this.height = 100;
			this.frames = 17;
			this.collide = true;
			this.impactDamage = 94;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 12;
			this.ai[Constant.AI4_CHARGETIME] = 15;
			this.ai[Constant.AI4_DASHDISACCEL] = 95;
			this.ai[Constant.AI4_DASHSPEED] = 1940;
			this.ai[Constant.AI4_DISTTOCHARGE] = 100;
			this.ai[Constant.AI4_WALKFINALFRAME] = 7;
			this.ai[Constant.AI4_WALKSPEED] = 800;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.fleshandblood = true;
			this.drops.put(168, 2.5f);
			this.recommendedLevel = 28;
		}
		else if(this.id == 65)
		{
			this.name = Main.lv("Beholding Spirit", "Espírito da Vigilância");
			this.health = 1500;
			this.maxHealth = 1500;
			this.width = 120;
			this.height = 160;
			this.frames = 15;
			this.collide = true;
			this.impactDamage = 105;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 10;
			this.ai[Constant.AI4_CHARGETIME] = 5;
			this.ai[Constant.AI4_DASHDISACCEL] = 96;
			this.ai[Constant.AI4_DASHSPEED] = 1950;
			this.ai[Constant.AI4_DISTTOCHARGE] = 150;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_WALKFINALFRAME] = 7;
			this.ai[Constant.AI4_WALKSPEED] = 850;
			this.fleshandblood = true;
			this.drops.put(168, 2.5f);
			this.recommendedLevel = 30;
		}
		else if(this.id == 66)
		{
			this.name = Main.lv("Brospirit", "Manotasma");
			this.health = 5400;
			this.maxHealth = 5400;
			this.width = 100;
			this.height = 120;
			this.frames = 12;
			this.collide = true;
			this.impactDamage = 105;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.movementAi = Constant.MOVEMENTAI_CHARGEDASH;
			this.ai[Constant.AI4_COOLDOWN] = 300;
			this.ai[Constant.AI4_CHARGEFINALFRAME] = 10;
			this.ai[Constant.AI4_CHARGETIME] = 10;
			this.ai[Constant.AI4_DASHDISACCEL] = 90;
			this.ai[Constant.AI4_DASHSPEED] = 5000;
			this.ai[Constant.AI4_DISTTOCHARGE] = 400;
			this.ai[Constant.AI4_WALKFINALFRAME] = 6;
			this.ai[Constant.AI4_WALKSPEED] = 1000;
			this.boss = true;
			this.drops.put(168, 2.5f);
			this.recommendedLevel = 35;
		}
		else if(this.id == 67)
		{
			this.name = Main.lv("Static", "Estático");
			this.maxHealth = 1;
			this.health = 1;
			this.frames = 5;
			this.width = 72;
			this.height = 52;
			this.collide = true;
			this.boss = false;
			this.impactDamage = 90;
			this.rColor.add(new Color(0.82f, 0.85f, 0.75f, 1f));
			this.rColor.add(new Color(0.85f, 0.93f, 0.8f, 1f));
			this.rColor.add(new Color(0.8f, 0.84f, 0.77f, 1f));
			this.fleshandblood = true;
			this.recommendedLevel = 30;
		}
		else if(this.id == 68)
		{
			this.name = Main.lv("Silver Ore", "Minério de Prata");
			this.health = 7;
			this.maxHealth = 7;
			this.width = 72;
			this.height = 72;
			this.frames = 1;
			this.collide = true;
			this.impactDamage = 0;
			this.knockbackImmune = true;
			this.natural = true;
			this.rColor.add(new Color(0.14f, 0.14f, 0.14f, 1f));
			this.rColor.add(new Color(0.1f, 0.1f, 0.1f, 1f));
			this.rColor.add(new Color(0.14f, 0.14f, 0.14f, 1f));
			this.rColor.add(new Color(0.1f, 0.1f, 0.1f, 1f));
			this.rColor.add(new Color(0.85f, 0.85f, 0.85f, 1f));

			this.drops.put(120, 3f);
		}
		else if(this.id == 69)
		{
			this.name = "Ethereal";
			this.health = 1;
			this.maxHealth = 1;
			this.width = 52;
			this.height = 72;
			this.frames = 4;
			this.collide = true;
			this.impactDamage = 100;
			this.rColor.add(new Color(0.5f, 0f, 0.5f, 1f));
			this.knockbackImmune = true;
			this.customShader = Content.maskIncS;
			int width = Content.nebula.getWidth();
			int height = Content.nebula.getHeight();
			this.customShaderUValues.put("u_offset", new String[] {"(((ed*ex*0.011 % sw)/(sw/2)) + (ticks*0.0005)) - floor((((ed*ex*0.011 % sw)/(sw/2)) + (ticks*0.0005)))", "(((ed*ey*0.011 % sh)/(sh/2)) + (ticks*0.00025)) - floor((((ed*ey*0.011 % sh)/(sh/2)) + (ticks*0.00025)))"});
			this.customShaderUValues.put("u_maskScale", new String[] {width+"/sw", height+"/sh"});
			this.customShaderUValues.put("u_frames", new String[] {"1"});
			this.customShaderUTextures.put(1, Content.extras[38]);
		}
		else if(this.id == 70)
		{
			this.name = "Damon";
			this.health = 3;
			this.maxHealth = 3;
			this.width = 52;
			this.height= 72;
			this.frames = 5;
			this.collide = true;
			this.boss = true;
			this.impactDamage = 1;
			this.knockbackImmune = true;
		}

		/*
		public String name;
		public int health;
		public int maxHealth;
		public int width;
		public int height;
		public int frames;
		public boolean collide = false;
		public boolean boss = false;
		public int impactDamage;
		public boolean overrideDraw = false;
		public List<Color> rColor;
		public boolean knockbackImmune = false;
		public boolean shouldGravity = true;
		public boolean natural = false;
		public boolean centeredOrigin = false;
		public int movementAi = 0;
		public boolean ranged = false;
		public boolean canFly = false;
		 */
		if(this.rColor.size() < 1)
		{
			System.out.println(this.name + " was created without rColor, generating black & purple to avoid crash.");
			this.rColor.add(new Color(0f, 0f, 0f, 1f));
			this.rColor.add(new Color(1f, 0f, 1f, 1f));
		}

		if(fullReset)
		{
			this.postReset();
		}
	}

	private void postReset()
	{
		if(this.natural)
		{
			this.scale = MathUtils.random(0.8f, 1.2f);
		}

		if(this.movementAi == 1)
		{
			this.naturalize(Constant.AI1_MAXSPEED);
			this.naturalize(Constant.AI1_SCAREDISTANCE);
			this.naturalize(Constant.AI1_STOPDISTANCE);
			this.naturalize(Constant.AI1_STOPTIME);
		}
		else if(this.movementAi == 2)
		{
			this.naturalize(Constant.AI2_DISTANCE);
			this.naturalize(Constant.AI2_MAXDISTANCE);
			this.naturalize(Constant.AI2_MAXSPEED);
			this.naturalize(Constant.AI2_PROJECTILECOOLDOWN);
			this.naturalize(Constant.AI2_PROJECTILEDAMAGE);
			this.naturalize(Constant.AI2_PROJECTILESPEED);
		}
		else if(this.movementAi == 3)
		{
			this.naturalize(Constant.AI3_DASHSPEED);
			this.naturalize(Constant.AI3_FLYSPEED);
			this.naturalize(Constant.AI3_KEEPHEIGHT);
		}
		else if(this.movementAi == 4)
		{
			this.naturalize(Constant.AI4_CHARGETIME);
			this.naturalize(Constant.AI4_COOLDOWN);
			this.naturalize(Constant.AI4_DASHSPEED);
			this.naturalize(Constant.AI4_DISTTOCHARGE);
			this.naturalize(Constant.AI4_WALKSPEED);
		}
		else if(this.movementAi == 5)
		{
			this.naturalize(Constant.AI5_FLYMAXSPEED);
		}
		else if(this.movementAi == 6)
		{
			this.naturalize(Constant.AI6_MAXSPEED);
		}
		else if(this.movementAi == 7)
		{
			this.naturalize(Constant.AI7_PROJECTILECOOLDOWN);
			this.naturalize(Constant.AI7_SHOOTRANGE);
			this.naturalize(Constant.AI7_PROJECTILEDAMAGE);
		}
		else if(this.movementAi == 8)
		{
			this.naturalize(Constant.AI8_KEEPDISTANCE);
			this.naturalize(Constant.AI8_MAXSPEED);
			this.naturalize(Constant.AI8_PROJECTILEDAMAGE);
			this.naturalize(Constant.AI8_PROJECTILECOOLDOWN);
			this.naturalize(Constant.AI8_CAITMAXSPEED);
		}
	
		if(this.id == 21)
		{
			if(MathUtils.randomBoolean(0.1f))
			{
				this.ai[14] = MathUtils.random(1,8);
				this.drops.put(104+this.ai[14], 5f);
				this.maxHealth*=1.5f;
				this.health *= 1.5f;
				this.impactDamage*=1.5f;
				this.ai[Constant.AI4_WALKSPEED] += 50;
				this.recommendedLevel += 5;
			}
		}
	}

	public void naturalize(int index)
	{
		this.ai[index] *= MathUtils.random(0.8f, 1.2f);
	}

	public void resetStats()
	{
		for(int i = 0;i < 15;i++)
		{
			this.ai[i] = 0;
		}
		for(int i = 0;i < 30;i++)
		{
			this.buffs[i] = null;
		}
		this.knockbackImmune = false;
		this.lastHitTick = 0;
		this.natural = false;
		this.centeredOrigin = false;
		this.movementAi = 0;
		this.swordTombSummon = false;
	}

	@Override
	public void update(float delta)
	{
		if(!AHS.isUp)
		{
			Player player = Main.player[Main.me];
			if(player != null)
			{
				for(Quest q : player.quests)
				{
					for(Objective o : q.objectives)
					{
						if(o.id == 1 && o.infos[0] == this.id)
						{
							this.tagSymbol = Constant.TAGSYMBOL_EXCLAMATION;
						}
					}
				}
			}
		}
		if(Constant.tryGetMapForEntity(this) == null)
		{
			System.out.println("Could not find a map for " + this.name + " (" + this.myMapX + ", " + this.myMapY + ")");
			return;
		}
		if(this.aggros.size() > 0)
		{
			Iterator<Entry<Integer, Integer>> it = this.aggros.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Integer, Integer> pair = (Entry<Integer, Integer>)it.next();
				Integer i = pair.setValue(pair.getValue()-1);
				i--;
				if(i < 0)
				{
					it.remove();
				}
			}
		}
		this.lazyUpdate--;
		this.uncapped -= delta;
		for(int i = 0;i < 30;i++)
		{
			if(this.buffs[i] != null)
			{
				applyBuffEffects(i);
				this.buffs[i].timeLeft -= delta;
				if(this.buffs[i].timeLeft < 0)
				{
					this.buffs[i].onEnd(this);
					this.buffs[i] = null;
				}
			}
		}

		boolean timestopped = false;
		for(Projectile p : Constant.getProjectileList())
		{
			if(p.type == 91)
			{
				Circle c = new Circle(p.Center(), 400);
				if(Main.circleContainsRect(c, this.hitBox()))
				{
					timestopped = true;
					break;
				}
			}
		}
		if((this.shouldGravity || this.isStunned()) && !timestopped)
		{
			if(this.haveBuff(26) == -1)
				this.velocity.y += Main.gravity;

		}
		if(this.collide && Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, this.position.y, this.width, this.height))
		{
			this.position = Constant.tryGetMapForEntity(this).getNextestFreeSpace(this.position.x, this.position.y, this.width, this.height, 1, 1).scl(64f);
		}

		Player player = null;
		if(this.getTarget() != null)
			player = this.getTarget();

		if(!timestopped)
		{
			if(this.movementAi == Constant.MOVEMENTAI_CHARGEANDRUN)
			{
				this.ai[1]++;
				if(this.target >= 0 && !this.getTarget().canBeTargeted())
					this.target = -1;

				float maxspeed = this.ai[4];
				Player nextest = this.getNextestPlayer();
				if(!this.isStunned())
				{
					if(nextest != null 
							&& nextest.Center().dst(this.Center()) <= 1200 
							&& this.ai[1] >= this.ai[3] && this.ai[2] != 1
							&& (this.ai[7] > 0 || this.ai[Constant.AI1_SCAREDISTANCE] <= 0 || this.Center().dst(nextest.Center()) < this.ai[Constant.AI1_SCAREDISTANCE]))
					{
						this.target = nextest.whoAmI;
						player = nextest;
						this.ai[2] = 1;
						this.ai[1] = 0;
						this.velocity.x = maxspeed * Main.directionFromTo(this.Center(), player.Center());
						this.knockbackImmune = true;
					}
					if(this.ai[2] == 1)
					{
						if(player == null
								|| (this.velocity.x <= 0 && player.Center().x - this.Center().x >= this.ai[Constant.AI1_STOPDISTANCE])
								|| (this.velocity.x >= 0 && player.Center().x - this.Center().x <= -this.ai[Constant.AI1_STOPDISTANCE])
								|| (this.position.x < 20 || this.position.x + this.width > Constant.tryGetMapForEntity(this).width * Tile.TILE_SIZE - 20))
						{
							this.ai[2] = 0;
							this.ai[1] = 0;
							this.velocity.x = 0;
							this.knockbackImmune = false;
							this.target = -1;
						}
					}

					if(this.ai[2] != 1)
					{
						this.actualFrame = 0;
						this.velocity.x *= 0.96f;
					}
					else
					{
						this.actualFrame = (this.actualFrame + 1) % this.frames;
						this.direction = (this.velocity.x > 0 ? 1 : -1);
					}
				}
				else
				{
					this.velocity.x *= 0.95f;
					this.actualFrame = 0;
					this.ai[1] = 0;
				}

				if(Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, this.position.y, this.width, this.height))
				{
					this.position = Constant.tryGetMapForEntity(this).getNextestFreeSpace(this.position.x, this.position.y, this.width, this.height, 1, 1).scl(64f);
					//	this.ai[2] = 0;
				}
				if(this.target >= 0)
				{
					this.ai[7] = 1;
				}
				else
				{
					this.actualFrame = 0;
				}
			}
			else if(this.movementAi == Constant.MOVEMENTAI_FLYAWAYANDSHOOT)
			{
				this.ranged = true;
				this.canFly = true;
				this.ai[1]++;
				Player target = this.getNextestPlayer(this.ai[Constant.AI2_MAXDISTANCE] * 1.25f);
				if(this.getTarget() != null && (!this.getTarget().canBeTargeted() || !this.getTarget().sameMapAs(this)))
				{
					this.target = -1;
					target = null;
				}
				if(this.getTarget() == null && target != null)
				{
					this.target = target.whoAmI;
				}

				if(!this.isStunned())
				{
					if(target != null)
					{
						if(this.ai[1] >= this.ai[Constant.AI2_PROJECTILECOOLDOWN] && target.Center().dst(this.Center()) < this.ai[Constant.AI2_MAXDISTANCE])
						{
							this.ai[1] = 0;
							float angle = Main.angleBetween(this.Center(), target.Center());
							float sin = (float)Math.sin(angle * Math.PI/180f);
							float cos = (float)Math.cos(angle * Math.PI/180f);
							float min = Math.min(this.width, this.height);
							Projectile.Summon(this.ai[Constant.AI2_PROJECTILEID], new Vector2(this.Center().x + cos * min, this.Center().y + sin * min), new Vector2(cos * this.ai[Constant.AI2_PROJECTILESPEED], sin * this.ai[Constant.AI2_PROJECTILESPEED]), this.ai[Constant.AI2_PROJECTILEDAMAGE], 8f, this);
						}
						this.target = target.whoAmI;
						float angle = Main.angleBetween(this.Center(), new Vector2(target.Center().x, target.Center().y - 4));
						float sin = (float)Math.sin(angle * Math.PI/180);
						float cos = (float)Math.cos(angle * Math.PI/180);
						this.velocity.x -= (this.velocity.x - ((target.Center().x - cos*this.ai[Constant.AI2_DISTANCE]) - this.Center().x))/this.ai[Constant.AI2_SPEEDDIV];
						this.velocity.y -= (this.velocity.y - ((target.Center().y - sin*this.ai[Constant.AI2_DISTANCE]) - this.Center().y))/this.ai[Constant.AI2_SPEEDDIV];
						if(this.velocity.len() > this.ai[Constant.AI2_MAXSPEED])
						{
							this.velocity.nor().scl(this.ai[Constant.AI2_MAXSPEED]);
						}
						this.direction = Main.directionFromTo(this.Center(), target.Center());
					}
					else
					{
						this.rotation = 0f;
						this.target = -1;
						this.velocity.y *= 0.95f;
					}
					this.actualFrame = (this.actualFrame + 1) % this.frames;
				}
				else
				{
					this.velocity.x *= 0.98f;
					this.actualFrame = 0;
				}
			}
			else if(this.movementAi == Constant.MOVEMENTAI_FLYINGKNIGHT)
			{
				this.canFly = true;
				this.ai[1]++;
				Player target = this.getTarget();
				if(!this.isStunned())
				{
					if(this.getTarget() != null && (!this.getTarget().canBeTargeted() || !this.getTarget().sameMapAs(this)))
					{
						this.target = -1;
						target = null;
					}
					Player nextest = this.getNextestPlayer();
					if(this.getTarget() == null && nextest != null)
					{
						this.target = nextest.whoAmI;
						target = nextest;
					}

					if(target != null)
					{
						if(this.ai[2] == 1)
						{
							target = this.getTarget();
							if(target != null)
							{
								if(this.Center().dst(target.Center()) < 30)
								{
									this.ai[2] = 0;
									this.ai[1] = 0;
								}
								this.direction = 1;
								float angle = Main.angleBetween(this.Center(), target.Center());
								if(this.blocksAhead(1, false) != 0)
								{
									if(this.blocksAhead(4, false) < this.blocksAheadDown(4, false))
									{
										angle = 270;
									}
									else
									{
										angle = 90;
									}

									if(target.Center().x < this.Center().x)
									{
										angle += 45;
									}
									else
									{
										angle -= 45;
									}
								}
								this.rotation = angle;
								float sin = (float)Math.sin(angle * Math.PI/180f);
								float cos = (float)Math.cos(angle * Math.PI/180f);
								this.velocity.x = this.ai[Constant.AI3_DASHSPEED] * cos;
								this.velocity.y = this.ai[Constant.AI3_DASHSPEED] * sin;
							}
							else
							{
								this.ai[2] = 0;
							}
						}
						else
						{
							this.target = target.whoAmI;
							this.rotation = 0f;
							float dist = this.Center().dst(target.Center());
							if(dist >= this.ai[Constant.AI3_KEEPHEIGHT] || this.ai[1] > 120)
							{
								this.ai[2] = 1;
							}
							else
							{
								if(dist <= this.ai[Constant.AI3_KEEPHEIGHT])
								{
									this.direction = Main.directionFromTo(target.Center(), this.Center());
									this.velocity.x = 150 * this.direction;
									this.velocity.y = this.ai[Constant.AI3_FLYSPEED];
								}
							}
						}
					}

					if(this.ai[2] == 1 && this.ai[1] % 120 < 90)
					{
						this.actualFrame = 0;
					}
					else
					{
						this.frameCounter++;
						if(this.frameCounter > 3)
						{
							this.frameCounter = 0;
							this.actualFrame++;
							if(this.actualFrame >= this.frames)
							{
								this.actualFrame = 1;
							}
						}
					}
				}
				else
				{
					this.velocity.x *= 0.95f;
					this.actualFrame = 0;
					this.ai[1] = 0;
					this.rotation = 0;
				}
			}
			else if(this.movementAi == Constant.MOVEMENTAI_FLYINGDUMMY)
			{
				this.canFly = true;
				this.ai[1]++;
				Player target = this.getTarget();
				if(!this.isStunned())
				{
					if(this.getTarget() != null && (!this.getTarget().canBeTargeted() || !this.getTarget().sameMapAs(this)))
					{
						this.target = -1;
						target = null;
					}
					Player nextest = this.getNextestPlayer();
					if(this.getTarget() == null && nextest != null)
					{
						this.target = nextest.whoAmI;
						target = nextest;
					}

					if(target != null)
					{
						if((this.ai[1] > 180 || this.target < 0))
						{
							this.target = target.whoAmI;
							this.ai[1] = 0;
						}

						if(this.target >= 0)
						{
							if(this.ai[Constant.AI5_SHOULDROTATE] < 1)
							{
								this.direction = Main.directionFromTo(this.Center(), target.Center());
							}
							else
							{
								this.direction = 1;
								this.rotation = this.velocity.len() > 300 ? this.velocity.angle() : Main.angleBetween(this.Center(), target.Center());
							}
						}

						float angle = Main.angleBetween(this.Center(), target.Center().add(0, 32));
						boolean shouldRotate = this.ai[Constant.AI5_SHOULDROTATE] > 0;
						if(this.blocksAhead(1, shouldRotate) != 0)
						{
							if(this.blocksAhead(4, shouldRotate) < this.blocksAheadDown(4, shouldRotate))
							{
								angle = 270;
							}
							else
							{
								angle = 90;
							}

							if(target.Center().x < this.Center().x)
							{
								angle += 45;
							}
							else
							{
								angle -= 45;
							}
						}
						this.velocity
						.add(new Vector2(this.ai[Constant.AI5_ACCELERATION], 0f).setAngle(angle))
						.clamp(0f, this.ai[Constant.AI5_FLYMAXSPEED]);
					}
					else
					{
						this.velocity.y *= 0.965f;
						this.velocity.x *= 0.965f;
					}
					this.frameCounter++;
					if(this.frameCounter >= this.ai[Constant.AI5_MAXFRAMECOUNTER])
					{
						this.frameCounter = 0;
						this.actualFrame = (this.actualFrame + 1) % this.frames;
					}
				}
				else
				{
					this.velocity.x *= 0.95f;
					this.actualFrame = 0;
					this.ai[1] = 0;
				}
			}
			else if(this.movementAi == Constant.MOVEMENTAI_CHARGEDASH)
			{
				this.ai[1]++;
				this.ai[11]--;
				Player p = this.getNextestPlayer(1200);
				if(!this.isStunned())
				{
					if(p != null)
						this.target = p.whoAmI;

					if(this.target >= 0 && !this.getTarget().canBeTargeted())
						this.target = -1;

					if(this.getTarget() != null)
					{
						Player target = this.getTarget();
						if(this.ai[2] == 0)
						{
							this.knockbackImmune = false;
							this.direction = Main.directionFromTo(this.Center(), target.Center());
							this.velocity.x += 50 * this.direction;

							int ba = this.blocksAhead();
							if((target.position.y > this.position.y + this.height || ba == 1 || ba == 2) && this.grounded)
							{
								this.velocity.y = 915;
							}

							if(this.direction == Constant.DIRECTION_RIGHT && this.velocity.x > this.ai[Constant.AI4_WALKSPEED])
							{
								this.velocity.x = this.ai[Constant.AI4_WALKSPEED];
							}
							else if(this.direction == Constant.DIRECTION_LEFT && this.velocity.x < -this.ai[Constant.AI4_WALKSPEED])
							{
								this.velocity.x = -this.ai[Constant.AI4_WALKSPEED];
							}
							this.frameCounter++;
							if(this.frameCounter >= 2)
							{
								this.frameCounter = 0;
								this.actualFrame = (this.actualFrame + 1) % (this.ai[Constant.AI4_WALKFINALFRAME] + 1);
							}
							if(this.ai[11] <= 0 && this.Center().dst(target.Center()) < this.ai[Constant.AI4_DISTTOCHARGE] && target.position.y <= this.position.y && this.grounded)
							{
								this.ai[2] = 1;
								this.ai[1] = 0;
								this.velocity.x = 0;
								this.actualFrame = this.ai[Constant.AI4_WALKFINALFRAME] + 1;
							}

						}
						else if(this.ai[2] == 1)
						{
							this.knockbackImmune = true;
							this.frameCounter++;
							if(this.actualFrame < this.ai[Constant.AI4_CHARGEFINALFRAME] && this.frameCounter >= 2)
							{
								this.frameCounter = 0;
								this.actualFrame = (this.actualFrame + 1);
							}
							if(this.ai[1] >= this.ai[Constant.AI4_CHARGETIME])
							{
								this.ai[1] = 0;
								this.ai[2] = 2;
								this.velocity.x = this.ai[Constant.AI4_DASHSPEED] * Main.directionFromTo(this.Center(), target.Center());
								this.actualFrame = this.ai[Constant.AI4_CHARGEFINALFRAME] + 1;
								this.direction = Main.directionFromTo(this.Center(), target.Center());
							}
						}
						else if(this.ai[2] == 2)
						{
							this.frameCounter++;
							if(this.actualFrame < this.frames - 1 && this.frameCounter >= 2)
							{
								this.frameCounter = 0;
								this.actualFrame = (this.actualFrame + 1);
							}
							if(this.grounded)
							{
								this.velocity.scl(this.ai[Constant.AI4_DASHDISACCEL]/100f);
							}
							if(this.velocity.len() < 50)
							{
								this.ai[2] = 0;
								this.ai[1] = 0;
								this.velocity.x = 0;
								this.actualFrame = 0;
								this.ai[11] = this.ai[Constant.AI4_COOLDOWN];
							}
						}
					}
				}
				else
				{
					this.velocity.x *= 0.95f;
					this.actualFrame = 0;
					this.ai[1] = 0;
				}
			}
			else if(this.movementAi == Constant.MOVEMENTAI_WALKINGDUMMY)
			{
				this.ai[1]++;
				Player p = this.getNextestPlayer(1300f);
				if(!this.isStunned())
				{
					if(this.target >= 0 && !this.getTarget().canBeTargeted())
						this.target = -1;

					if(this.getTarget() != null && !this.sameMapAs(this.getTarget()))
					{
						this.target = -1;
					}
					if((this.ai[1] >= 240 || this.getTarget() == null) && p != null)
					{
						this.target = p.whoAmI;
						player = p;
						this.ai[1] = 0;
					}

					if(this.target >= 0)
					{
						this.velocity.x += this.ai[Constant.AI6_ACCELERATION] * Main.directionFromTo(this.Center(), player.Center());
						int ba = this.blocksAhead();
						if((player.position.y > this.position.y + this.height || ba == 1 || (this.ai[Constant.AI6_JUMPFORCE] > 910 && ba == 2)) && this.grounded)
						{
							this.velocity.y = this.ai[Constant.AI6_JUMPFORCE];
						}
						if(this.velocity.x > this.ai[Constant.AI6_MAXSPEED])
						{
							this.velocity.x = this.ai[Constant.AI6_MAXSPEED];
						}
						else if(this.velocity.x < -this.ai[Constant.AI6_MAXSPEED])
						{
							this.velocity.x = -this.ai[Constant.AI6_MAXSPEED];
						}
						this.direction = Main.directionFromTo(this.Center(), player.Center());
						this.frameCounter++;
						if(this.frameCounter >= this.ai[Constant.AI6_MAXFRAMECOUNTER])
						{
							this.frameCounter = 0;
							this.actualFrame = (this.actualFrame + 1) % this.frames;
						}
					}
					else
					{
						this.actualFrame = 0;
						this.velocity.x *= 0.965f;
					}
				}
				else
				{
					this.velocity.x *= 0.95f;
					this.actualFrame = 0;
					this.ai[1] = 0;
				}
			}
			else if(this.movementAi == Constant.MOVEMENTAI_SHOOTINGSENTRY)
			{
				this.ranged = true;
				this.ai[1]++;
				if(!this.isStunned())
				{
					Player target = this.getNextestPlayer();
					if(this.target >= 0 && !this.getTarget().canBeTargeted())
						this.target = -1;

					if((this.ai[1] >= 180 || this.target <= 0) && target != null && target.Center().dst(this.Center()) < this.ai[Constant.AI7_SHOOTRANGE])
					{
						if(target.whoAmI != this.target)
						{
							this.ai[1] = 0;
							this.target = target.whoAmI;
						}
					}
					if(this.target >= 0)
					{
						target = this.getTarget();
						if(this.ai[1] > this.ai[Constant.AI7_PROJECTILECOOLDOWN])
						{
							float angle = Main.angleBetween(this.Center(), target.Center());
							float sin = (float) Math.sin(angle * Math.PI/180f);
							float cos = (float) Math.cos(angle * Math.PI/180f);
							float min = Math.min(this.width, this.height);
							Projectile.Summon(this.ai[Constant.AI7_PROJECTILEID], new Vector2(this.Center().x + min * cos, this.Center().y + min * sin), new Vector2(cos, sin).scl(this.ai[Constant.AI7_PROJECTILESPEED]), this.ai[Constant.AI7_PROJECTILEDAMAGE], 600, this);
							this.ai[1] = 0;
						}
					}

					this.frameCounter++;
					if(this.frameCounter >= 3)
					{
						this.frameCounter = 0;
						this.actualFrame = (this.actualFrame + 1) % this.frames;
					}
				}
				else
				{
					this.velocity.x *= 0.95f;
					this.actualFrame = 0;
					this.ai[1] = 0;
				}
			}
			else if(this.movementAi == Constant.MOVEMENTAI_MARKSMAN2K18LUL)
			{
				this.ai[1]++;
				if(!this.isStunned())
				{
					this.ai[12]++;
					this.ranged = true;
					Player target = this.getNextestPlayer();
					if(this.target >= 0 && !this.getTarget().canBeTargeted())
						this.target = -1;

					if((this.ai[1] >= 180 || this.target <= 0) && target != null && target.Center().dst(this.Center()) < this.ai[Constant.AI7_SHOOTRANGE])
					{
						if(target.whoAmI != this.target)
						{
							this.ai[1] = 0;
							this.target = target.whoAmI;
						}
					}
					if(this.target >= 0)
					{
						target = this.getTarget();
						if(Math.abs(this.Center().dst(target.Center()) - this.ai[Constant.AI8_KEEPDISTANCE]) > 100)
						{
							this.direction = Main.directionFromTo(this.Center(), target.Center());
							int walkDir = Main.directionFromTo(this.Center(), target.Center());
							if(this.Center().dst(target.Center()) < this.ai[Constant.AI8_KEEPDISTANCE])
							{
								walkDir *= -1;
							}
							this.velocity.x += 40 * walkDir;
							float maxspeed = this.ai[Constant.AI8_MAXSPEED];
							if(walkDir != Main.directionFromTo(this, target))
								maxspeed = this.ai[Constant.AI8_CAITMAXSPEED];

							if(walkDir == 1 && this.velocity.x > maxspeed)
							{
								this.velocity.x = maxspeed;
							}
							else if(walkDir == -1 && this.velocity.x < -maxspeed)
							{
								this.velocity.x = -maxspeed;
							}
							if(((walkDir != this.direction && this.blocksBehind() == 1) || (walkDir == this.direction && this.blocksAhead() == 1)) && this.grounded)
							{
								this.velocity.y = 900;
								this.grounded = false;
							}
							else if(((walkDir != this.direction && this.blocksBehind() == 2) || (walkDir == this.direction && this.blocksAhead() == 2)) && this.grounded)
							{
								this.velocity.y = 1000;
								this.grounded = false;
							}
							this.frameCounter++;
							if(this.frameCounter > 2)
							{
								this.frameCounter = 0;
								this.actualFrame = (this.actualFrame + 1) % this.frames;
							}
						}
						else
						{
							this.velocity.x *= 0.95f;
							this.actualFrame = 0;
						}

						if(this.ai[1] >= this.ai[Constant.AI8_PROJECTILECOOLDOWN])
						{
							float angle = Main.angleBetween(this.Center(), target.Center());
							float sin = (float) Math.sin(angle * Math.PI/180f);
							float cos = (float) Math.cos(angle * Math.PI/180f);
							float min = Math.min(this.width, this.height);
							Projectile.Summon(this.ai[Constant.AI8_PROJECTILEID], new Vector2(this.Center().x + min * cos, this.Center().y + min * sin), new Vector2(cos, sin).scl(this.ai[Constant.AI8_PROJECTILESPEED]), this.ai[Constant.AI8_PROJECTILEDAMAGE], 600, this);
							this.ai[1] = 0;
							this.ai[12] = 0;
						}
					}
				}
				else
				{
					this.velocity.x *= 0.95f;
					this.actualFrame = 0;
					this.ai[1] = 0;
				}
			}


			/*if(this.type == 1 || this.type == 2)
		{
			float maxSpeed = 800f;
			frameCounter++;
			if(frameCounter > 3)
			{
				actualFrame = (actualFrame + 1) % 5;
				frameCounter = 0;
			}
			this.velocity.y += Main.gravity;

			float see = this.ai[1];
			if(this.target < 0 && this.boss)
			{
				see = 700;
			}
			else if(this.target >= 0)
				see = 1500;

			if(this.getNextestPlayer() != null)
				this.target = this.getNextestPlayer().whoAmI;
			else
				this.target = -1;

			player = this.getTarget();

			if(player != null && (player.Center().dst(this.Center()) <= see || (this.boss)))
			{
				if(player.Center().x < this.Center().x)
				{
					this.velocity.x -= 10;
					this.direction = 1;
					if(this.velocity.x > 0)
					{
						this.velocity.x *= 0.95f;
					}
					if(this.velocity.x < -maxSpeed)
					{
						this.velocity.x = -maxSpeed;
					}
				}
				else if(player.Center().x > this.Center().x)
				{
					this.velocity.x += 10;
					this.direction = -1;
					if(this.velocity.x < 0)
					{
						this.velocity.x *= 0.95f;
					}
					if(this.velocity.x > maxSpeed)
					{
						this.velocity.x = maxSpeed;
					}
				}
			}
			else
			{
				this.target = -1;
				player = null;
				boolean noBlock = false;
				int downBlock;
				int downY = (int) Math.floor(this.position.y/64);
				if(downY > 0 && this.ai[9] == 1)
				{
					if(this.velocity.x < 0)
					{
						downBlock = (int) (Math.floor(this.position.x)/64);
					}
					else
					{
						downBlock = (int) (Math.floor(this.position.x+this.width)/64);
					}
					if(downY >= 0 && downY < Constant.tryGetMapForEntity(this).height && downBlock >= 0 && downBlock < Constant.tryGetMapForEntity(this).width && Constant.tryGetMapForEntity(this).map[downY-1][downBlock] == null)
					{
						noBlock = true;
					}
				}
				if(this.lazyUpdate <= 0 || noBlock)
				{
					if(this.ai[9] == 0)
					{
						this.direction = (MathUtils.random(1) == 0 ? -1 : 1);
						this.ai[9] = 1;
					}
					else
					{
						this.velocity.x = 0f;
						this.ai[9] = 0;
					}
					this.lazyUpdate = MathUtils.random(90, 180);
				}
				if(this.ai[9] == 1)
				{
					this.velocity.x = -200 * this.direction;
				}
				else
				{
					this.actualFrame = 2;
				}
			}
		}
		else */if(this.id == 4)
		{
			frameCounter++;
			if(frameCounter > 3)
			{
				actualFrame = (actualFrame + 1) % 5;
				frameCounter = 0;
			}
			this.velocity.y += Main.gravity;

			float see = this.ai[1];
			if(this.target < 0 && this.boss)
			{
				see = 700;
			}

			if(this.getNextestPlayer() != null)
				this.target = this.getNextestPlayer().whoAmI;

			player = this.getTarget();
			if((player.Center().dst(this.Center()) <= see || (this.boss && player != null)))
			{
				this.ai[2]--;
				if(this.ai[2] <= 0)
				{
					int r = MathUtils.random(0, 100);
					if(r <= 50)
					{
						this.ai[4] = 0;
					}
					else if(r <= 80)
					{
						this.ai[4] = 1;
					}
					else
					{
						this.ai[4] = 2;
					}
					this.ai[2] = 90;
					this.ai[5] = (player.Center().x < this.Center().x ?  -1 : 1);
					this.direction = -this.ai[5];
				}
				else
				{
					if(this.ai[4] == 0)
					{
						this.velocity.x = 1200 * this.ai[5];
					}
					else if(this.ai[4] == 1)
					{
						this.velocity.x = 0f;
						if(this.ai[2] == 89)
							this.velocity.y = 2000f;
						else if(this.ai[2] == 60)
						{
							for(int i = 0;i < 50;i++)
							{
								Projectile.Summon(4, this.Center(), new Vector2(MathUtils.random(-1000, 1000), MathUtils.random(-1000, 1000)), 10, 6f, this);
							}
						}
					}
					else if(this.ai[4] == 2)
					{
						this.velocity.x = 0f;
						if(this.ai[2] % 40 == 0 || this.ai[2] == 89)
						{
							for(int i = 0;i < 3;i++)
							{
								int type = 1;
								Monster m = Monster.Create(this.randomHitBoxPosition(), type, this.myMapX, this.myMapY);
								m.position = Constant.tryGetMapForEntity(this).getNextestFreeSpace(m.position.x, m.position.y, m.width, m.height, 1, 1).scl(Tile.TILE_SIZE);
								m.velocity = new Vector2(MathUtils.random(-1200, 1200), MathUtils.random(200, 1000));
							}
						}
					}
				}
			}
			else
			{
				this.target = -1;
				player = null;
				boolean noBlock = false;
				int downBlock;
				int downY = (int) Math.floor(this.position.y/64);
				if(downY > 0 && this.ai[9] == 1)
				{
					if(this.velocity.x < 0)
					{
						downBlock = (int) (Math.floor(this.position.x)/64);
					}
					else
					{
						downBlock = (int) (Math.floor(this.position.x+this.width)/64);
					}
					if(downY >= 0 && downY < Constant.tryGetMapForEntity(this).height && downBlock >= 0 && downBlock < Constant.tryGetMapForEntity(this).width && Constant.tryGetMapForEntity(this).map[downY-1][downBlock] == null)
					{
						noBlock = true;
					}
				}
				if(this.lazyUpdate <= 0 || noBlock)
				{
					if(this.ai[9] == 0)
					{
						this.direction = (MathUtils.random(1) == 0 ? -1 : 1);
						this.ai[9] = 1;
					}
					else
					{
						this.velocity.x = 0f;
						this.ai[9] = 0;
					}
					this.lazyUpdate = MathUtils.random(90, 180);
				}
				if(this.ai[9] == 1)
				{
					this.velocity.x = -200 * this.direction;
				}
				else
				{
					this.actualFrame = 2;
				}
			}
			boolean canJump = (Constant.tryGetMapForEntity(this).getTileTypeByCoordinate((int)(this.Center().x + (this.width/2+4) * -this.direction)/64, (int) Math.floor(this.position.y/64)) != null && Constant.tryGetMapForEntity(this).getTileTypeByCoordinate((int)(this.Center().x + (this.width/2+4) * -this.direction)/64, (int)Math.floor(this.position.y)/64).isCollidable()) || player.Center().y - this.Center().y >= Tile.TILE_SIZE;
			if(player.Center().dst(this.Center()) < this.ai[1] && canJump && this.grounded)
			{
				this.velocity.y = 600f + ai[0] * 100;
				this.ai[0]++;
			}
			if(player.Center().dst(this.Center()) > this.ai[1] || !canJump)
			{
				this.ai[0] = 0;
			}
		}
		else if(this.id == 3 || this.id == 2)
		{
			this.velocity.x *= 0.95f;
			this.actualFrame = 2;
		}
		else if(this.id == 5)
		{
			this.ai[1] += 2f;
			this.frames++;
			this.target = this.getNextestPlayer().whoAmI;
			player = this.getTarget();
			if(player != null && player.Center().dst(this.Center()) <= this.ai[1])
			{
				//
			}
			else
			{
				this.target = -1;
				player = null;
			}

			if(this.target >= 0)
			{
				if(this.ai[2] < 1000)
					this.ai[2]+=3;

				float angle = (float)Math.toDegrees((Math.atan2(player.Center().y - this.Center().y, player.Center().x - this.Center().x)));
				float sin = (float) Math.sin(angle * Math.PI/180);
				float cos = (float) Math.cos(angle * Math.PI/180);
				this.velocity = new Vector2(cos * (300+this.ai[2]), sin * (300+this.ai[2]));
			}
			else
			{
				this.velocity = new Vector2(MathUtils.random(-1000, 1000), MathUtils.random(-1000, 1000));
			}
		}
		else if(this.id == 6)
		{
			this.ai[1] += 2;
			this.target = this.getNextestPlayer().whoAmI;
			player = this.getTarget();
			if(player != null && this.Center().dst(player.Center()) < this.ai[1])
			{
				//this.target = Main.player;
			}
			else
			{
				this.target = -1;
				player = null;
			}
			this.velocity.y += Main.gravity;
			if(this.target < 0)
			{
				this.velocity = new Vector2(MathUtils.random(-800, 800), 0);
			}
			else
			{
				if(Main.gameTick % 30 == 0)
				{
					Particle p = Particle.Create(new Vector2(this.Center().x + MathUtils.random(-100, 100), this.Center().y + MathUtils.random(-100, 100)), Vector2.Zero.cpy(), 5, new Color(0f,0.5f,0f,1f), 0f, MathUtils.random(1f, 2.5f), 1f);
					p.parent = this;
				}
				this.velocity.x = 1000f;
				this.direction = Constant.DIRECTION_RIGHT;
				this.frameCounter++;
				if(this.frameCounter > 2)
				{
					this.actualFrame = (this.actualFrame+1)%this.frames;
					this.frameCounter = 0;
				}
				this.ai[3]++;
				if(player.Center().x < this.Center().x)
				{
					this.velocity.x *= -1f;
					this.direction = Constant.DIRECTION_LEFT;
				}

				if(this.ai[3] % 240 == 0)
				{
					float newX = 1500f;
					if(this.direction == Constant.DIRECTION_LEFT)
					{
						newX *= -1f;
					}
					for(int i = 0;i < 150;i++)
					{
						float value = MathUtils.random()/2f;
						Particle p = Particle.Create(new Vector2(this.Center().x + MathUtils.random(-80, 80), this.Center().y + MathUtils.random(-80,80)), new Vector2(0, MathUtils.random(600, 1100)), 2, new Color(value, 0f, value, 1f), 0f, 1f, 1f);
						p.lights = true;
						p.lightSize = 32;
						p.lightColor = new Color(1f, 0f, 1f, 1f);
					}
					this.position = new Vector2(newX, MathUtils.random(-400, 400));
					this.position.add(player.Center());
					this.position = Constant.tryGetMapForEntity(this).getNextestFreeSpace(this.position.x, this.position.y, this.width, this.height, 1, 1).scl(Tile.TILE_SIZE);
					for(int i = 0;i < 150;i++)
					{
						float value = MathUtils.random()/2f;
						Particle p = Particle.Create(new Vector2(this.Center().x + MathUtils.random(-80, 80), this.Center().y + MathUtils.random(-80,80)), new Vector2(0, MathUtils.random(600, 1100)), 2, new Color(value, 0f, value, 1f), 0f, 1f, 1f);
						p.lights = true;
						p.lightSize = 32;
						p.lightColor = new Color(1f, 0f, 1f, 1f);
					}
				}
			}
		}
		else if(this.id == 8)
		{
			for(int i = 0;i < 3;i++)
			{
				Particle par = Particle.Create(this.randomHitBoxPosition(), this.velocity.cpy().scl(-0.2f), 2, new Color(0f, 0.69f, 1f, 0f), 0f, 1.5f, 1f);
				par.setLight(32, new Color(0f, 0.69f, 1f, 1f));
				par.rotation = MathUtils.random(360f);
			}
			Lighting l = Lighting.Create(this.Center(), 512, new Color(0.5f, 0.85f, 1f, 1f), 1f);
			l.hadParent = true;
			l.parent = this;
		}
		else if(this.id == 11)
		{
			this.ai[1]++;

			if(!this.isStunned())
			{
				float maxspeed = this.ai[4];
				Player nextest = this.getNextestPlayer(1500f);
				if(nextest != null && this.ai[1] >= this.ai[3] && this.ai[2] != 1)
				{
					this.target = nextest.whoAmI;
					player = nextest;
					this.ai[2] = 1;
					this.ai[1] = 0;
					this.velocity.x = maxspeed * Main.directionFromTo(this.Center(), player.Center());
					this.knockbackImmune = true;
				}
				if(this.ai[2] == 1)
				{
					if(player == null || (this.velocity.x <= 0 && player.Center().x - this.Center().x >= this.ai[Constant.AI1_STOPDISTANCE]) || (this.velocity.x >= 0 && player.Center().x - this.Center().x <= -this.ai[Constant.AI1_STOPDISTANCE]))
					{
						this.ai[2] = 0;
						this.ai[1] = 0;
						this.velocity.x = 0;
						this.knockbackImmune = false;
						this.target = -1;
					}
				}

				if(this.ai[2] != 1)
				{
					if(nextest != null)
					{
						this.direction = Main.directionFromTo(this.Center(), nextest.Center());
						this.frameCounter++;
						if(this.frameCounter >= 3)
						{
							this.frameCounter = 0;
							this.actualFrame = ((actualFrame+1) % 5);
							if(this.actualFrame == 3)
							{
								Particle.Create(new Vector2(this.Center().x + 20, this.position.y), new Vector2(-400f * this.direction, MathUtils.random(50f)), 6, new Color(1f, 1f, 0.5f, 1f), 0f, 1f, 1f);
								Particle.Create(new Vector2(this.Center().x - 20, this.position.y), new Vector2(-400f * this.direction, MathUtils.random(50f)), 6, new Color(1f, 1f, 0.5f, 1f), 0f, 1f, 1f);
							}
						}
					}
				}
				else
				{
					this.frameCounter++;
					if(this.frameCounter >= 2)
					{
						this.frameCounter = 0;
						this.actualFrame = 5 + ((actualFrame+1) % 5);
					}
					this.direction = (this.velocity.x > 0 ? 1 : -1);
				}

				if(this.target < 0)
					this.actualFrame = 0;
			}
			else
			{
				this.velocity.x *= 0.95f;
				this.ai[1] = 0;
				this.actualFrame = 0;
			}

			if(Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, this.position.y, this.width, this.height))
			{
				this.position = Constant.tryGetMapForEntity(this).getNextestFreeSpace(this.position.x, this.position.y, this.width, this.height, 1, 1).scl(64f);
			}
		}
		else if(this.id == 16)
		{
			for(int i = 0;i < 4;i++)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero.cpy(), 10, new Color(0f, 1f, 1f, 1f), 0f, 1.5f, 1f);
				p.drawFront = true;
				p.lights = true;
				p.lightSize = 16;
			}
		}
		else if(this.id == 29)
		{
			if(this.oldai[2] != 2 && this.ai[2] == 2)
			{
				this.velocity.y = 350;
			}
		}
		else if(this.id == 30 || this.id == 64)
		{
			if(this.health < this.maxHealth * 0.4f)
			{
				this.ai[Constant.AI4_CHARGETIME] = 5;
				this.ai[Constant.AI4_DISTTOCHARGE] = 100;
				this.ai[Constant.AI4_WALKSPEED] = 950 + (this.id == 64 ? 100 : 0);
				this.ai[Constant.AI4_COOLDOWN] = 240;
				for(int i = 0;i < 3;i++)
				{
					Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(this.direction * MathUtils.random(-200, -100), MathUtils.random(-50, 50)), 10, new Color(MathUtils.random()/2 + 0.5f, 0f, 0f, 1f), 0f, 1f, 1f);
					p.drawBack = true;
					p.lights = true;
					p.lightSize = 64;
					p.lightColor = new Color(1f, 0f, 0f, 1f);
				}
			}
		}
		else if(this.id == 33)
		{
			this.rotation += 5;
		}
		else if(this.id == 38)
		{
			float gb = MathUtils.random()/2+0.5f;
			Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 10, new Color(0f, gb, gb, 1f), 0f, 1f, 1f);
			p.drawBack = true;
		}
		else if(this.id == 43)
		{
			if(this.oldai[2] != 2 && this.ai[2] == 2)
			{
				this.velocity.y = 450;
			}
			for(int i = 0;i < 5 + (this.ai[2] == 2 ? 5 : 0);i++)
			{
				Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 2, new Color(1f, MathUtils.random()/2f+0.5f, 0f, 1f), 2f, 1f, 0.5f);
			}
		}
		else if(this.id == 67)
		{
			Player p = this.getNextestPlayer();
			if(p != null)
			{
				this.target = p.whoAmI;
			}
		}
		if(this.id == 69)
		{
			Player p = this.getNextestPlayer();
			if(p != null)
				this.target = p.whoAmI;
			this.frameCounter++;
			if(this.frameCounter > 2)
			{
				this.frameCounter = 0;
				this.actualFrame = (this.actualFrame + 1) % this.frames;
			}
			this.velocity.x = this.direction * 300;
			float posx = this.position.x+this.width/2f;
			if(this.direction == -1)
				posx -= this.width;
			
			boolean gapforwards = false;
			if(this.direction == 1)
			{
				int y = (int)Math.floor(this.position.y/64)-1;
				int x = (int)Math.ceil((this.position.x+this.width)/64);
				if(Main.worldMap.map[y][x] == null || !Main.worldMap.map[y][x].isCollidable())
				{
					gapforwards = true;
				}
			}
			else
			{
				int y = (int)Math.floor(this.position.y/64)-1;
				int x = (int)Math.floor((this.position.x)/64)-1;
				if(Main.worldMap.map[y][x] == null || !Main.worldMap.map[y][x].isCollidable())
				{
					gapforwards = true;
				}
			}
			if(gapforwards || Main.worldMap.doesRectCollideWithMap(posx, this.position.y, this.width, this.height))
			{
				this.direction *= -1f;
			}
		}
		else if(this.id == 70)
		{
			this.ai[0]++;
			player = this.getNextestPlayer(99999f);
			if(player != null)
			{
				this.target = player.whoAmI;
			}
			if(Main.displayDialog)
			{
				this.velocity.x = 0;
				this.actualFrame = 0;
			}
			else
			{
				if(this.ai[2] == 0)
				{
					this.actualFrame = 0;
					if(this.ai[0] > 150)
					{
						this.ai[2] = MathUtils.random(player.position.y < 576 ? 1 : 2, 3);
						this.ai[0] = 0;
						if(this.ai[2] == 2 || this.ai[2] == 3)
						{
							Color[] color = new Color[4];
							color[0] = new Color(1f, 1f, 1f, 1f);
							color[1] = new Color(0.8f, 0.4f, 0.8f, 1f);
							color[2] = new Color(0.5f, 0.2f, 0.5f, 1f);
							color[3] = new Color(0.35f, 0.05f, 0.35f, 1f);
							Main.createCustomExplosion(this.Center(), 120, 1f, true, color, color[3]);
							Vector2 pos = new Vector2(500f, 0f).setAngle(MathUtils.random(360f)).add(player.Center());
							if(Main.worldMap.doesRectCollideWithMap(pos.x, pos.y, this.width, this.height))
							{
								pos = Main.worldMap.getNextestFreeSpace(pos.x, pos.y, this.width, this.height, 1, 1).scl(64f);
							}
							this.position = pos;
							Main.createCustomExplosion(this.Center(), 120, 1f, true, color, color[3]);
						}
						else
						{
							Color[] color = new Color[4];
							color[0] = new Color(1f, 1f, 1f, 1f);
							color[1] = new Color(0.8f, 0.4f, 0.8f, 1f);
							color[2] = new Color(0.5f, 0.2f, 0.5f, 1f);
							color[3] = new Color(0.35f, 0.05f, 0.35f, 1f);
							Main.createCustomExplosion(this.Center(), 120, 1f, true, color, color[3]);
							Vector2 pos = new Vector2(MathUtils.randomBoolean() ? 576 : 2432, 576);
							if(Main.worldMap.doesRectCollideWithMap(pos.x, pos.y, this.width, this.height))
							{
								pos = Main.worldMap.getNextestFreeSpace(pos.x, pos.y, this.width, this.height, 1, 1).scl(64f);
							}
							this.position = pos;
							Main.createCustomExplosion(this.Center(), 120, 1f, true, color, color[3]);
							this.direction = Main.directionFromTo(this, player);
						}
					}
				}
				else if(this.ai[2] == 1)
				{
					if(this.grounded)
					{
						Projectile p = Projectile.Summon(127, Vector2.Zero, Vector2.Zero, 10, 2f, player);
						p.position = new Vector2(this.Center().x-p.width/2f, this.position.y);
					}
					this.frameCounter++;
					if(this.frameCounter > 1)
					{
						this.frameCounter = 0;
						this.actualFrame++;
						if(this.actualFrame > 3)
						{
							this.actualFrame = 0;
						}
					}
					this.velocity.x = this.direction * 1500;
					float posx = this.position.x+30;
					if(this.direction == -1)
						posx = this.position.x - 30;
					if(Main.worldMap.doesRectCollideWithMap(posx, this.position.y, this.width, this.height))
					{
						this.ai[2] = 0;
						this.ai[0] = 0;
					}
				}
				else if(this.ai[2] == 2)
				{
					this.direction = Main.directionFromTo(this, player);
					this.actualFrame = 0;
					this.velocity.y = 0;
					if(this.ai[0] == 90)
					{
						this.ai[0] = 0;
						this.ai[2] = 0;
					}
					else if(this.ai[0] > 30 && this.ai[0] % 15 == 0)
					{
						Color[] color = new Color[4];
						color[0] = new Color(1f, 1f, 1f, 1f);
						color[1] = new Color(0.8f, 0.4f, 0.8f, 1f);
						color[2] = new Color(0.5f, 0.2f, 0.5f, 1f);
						color[3] = new Color(0.35f, 0.05f, 0.35f, 1f);
						Main.createCustomExplosion(this.Center(), 80, 1f, false, color, color[3]);
						Vector2 vel = new Vector2(1600f, 0f).rotate(Main.angleBetween(this.Center(), player.Center()));
						Projectile.Summon(128, this.Center(), vel, 10, 5f, player);
					}
				}
				else if(this.ai[2] == 3)
				{
					this.actualFrame = 0;
					this.direction = Main.directionFromTo(this, player);
					this.velocity.y = 0;
					if(this.ai[0] % 15 == 0)
					{
						for(int i = 0;i < 72;i++)
						{
							Vector2 pos = new Vector2(1f, 0f).setAngle(i*5).setLength(50 + (240 - this.ai[0] * 4)).add(this.Center());
							Particle.Create(pos, Vector2.Zero, 2, new Color(0.5f, 0f, 0.5f, 1f), 0f, 0.5f, 1f).setLight(32, Color.PURPLE);
						}
					}
					
					if(this.ai[0] == 60)
					{
						Color[] color = new Color[4];
						color[0] = new Color(1f, 1f, 1f, 1f);
						color[1] = new Color(0.8f, 0.4f, 0.8f, 1f);
						color[2] = new Color(0.5f, 0.2f, 0.5f, 1f);
						color[3] = new Color(0.35f, 0.05f, 0.35f, 1f);
						Main.createCustomExplosion(this.Center(), 100, 1f, false, color, color[3]);
						Vector2 vel = new Vector2(2100f, 0f).rotate(Main.angleBetween(this.Center(), player.Center()));
						Projectile.Summon(129, this.Center(), vel, 10, 5f, player);
						this.ai[0] = 0;
						this.ai[2] = 0;
					}
				}
			}
		}
		}

		Player targetp = player;
		boolean canAggro = true;
		if((targetp != null && aggros != null && aggros.size() > 0 && aggros.containsKey(targetp.whoAmI)) || timestopped)
			canAggro = false;

		if(targetp != null && this.hitBox().overlaps(targetp.hitBox()) && this.impactDamage > 0 && canAggro)
		{
			if(targetp.haveBuff(17) != -1)
			{
				targetp.removeBuff(17);
				targetp.setInvincible(1.5f);
				return;
			}

			int damage = (int) (this.impactDamage);
			Skill s = targetp.getSkill(26);
			if(s != null)
			{
				float mult = 1f-(s.getInfoValueF(targetp, 0)/100f);

				damage = (int) (damage * mult);
			}

			if(targetp.haveBuff(35) != -1)
			{
				float dmg = 0f;
				Skill sk = targetp.getSkill(52);
				if(sk != null)
				{
					dmg = targetp.getMP() * sk.valueByLevel("2/2.1/2.2/2.3/2.4");
				}
				for(int i = 0;i < 70;i++)
				{
					Vector2 pos = this.randomHitBoxPosition();
					float r = MathUtils.random()/3f;
					Particle.Create(pos, new Vector2(0f, 50f), 6, new Color(r, r, r, 1f), 2f, 1f, 1f);
					Particle.Create(pos, Vector2.Zero, 11, new Color(1f, MathUtils.random()/3f + 0.5f, 0f, 1f), 0f, 1f, MathUtils.random(0.5f, 1f));
				}
				this.hurtDmgVar((int) dmg, 0, 0f, false, Constant.DAMAGETYPE_FIRE, targetp);
			}

			if(targetp.getRight() != null && targetp.getRight().id == 183)
			{
				this.hurtDmgVar(targetp.getRight().getInfoValueI(1), Main.directionFromTo(player, this), 0.5f, player.canCritical(this), Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_SHIELD, player);
			}

			targetp.hurt2(damage, (this.Center().x > targetp.Center().x ? -1 : 1), 1f, this);
		}

		/*if(this.haveBuff(15) != -1)
				this.velocity.x = 0f;

			float addAmountX = this.velocity.x * delta;
			float newX = this.position.x + addAmountX;
			float addAmountY = this.velocity.y * delta;
			float newY = this.position.y + addAmountY;

			if(!Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, newY, this.width, this.height))
			{
				this.position.y = newY;
				this.grounded = false;
			}
			else
			{
				if(this.velocity.y <= 0f)
				{
					//this.position.y = (float) (Math.floor(this.position.y) - Math.floor(this.position.y) % Tile.TILE_SIZE);
					this.position.y -= Math.floor(this.position.y) % Tile.TILE_SIZE;
					this.grounded = true;
				}
				this.velocity.y = 0f;
			}

			if(!Constant.tryGetMapForEntity(this).doesRectCollideWithMap(newX, this.position.y, this.width, this.height))
			{
				this.position.x = newX;
			}
			else
			{
				if(this.velocity.x < 0f)
				{
					//this.position.x = (float) (Math.floor(this.position.x) - Math.floor(this.position.x) % Tile.TILE_SIZE);
					this.position.x -= Math.floor(this.position.x) % Tile.TILE_SIZE;
				}
				else if(this.velocity.x > 0f)
				{
					//this.position.y = (float) (Math.floor(this.position.y) + (Tile.TILE_SIZE - Math.floor(this.position.y) % Tile.TILE_SIZE) - (this.height - Tile.TILE_SIZE));
					this.position.x = this.position.x;
				}

				this.velocity.x = 0;
			}*/

		if(this.haveBuff(15) != -1)
			this.velocity.x = 0f;

		if(this.haveBuff(7) != -1)
		{
			this.velocity.y = 0f;
			this.velocity.x = 0f;
		}

		float addAmountX = this.velocity.x * delta;
		float newX = this.position.x + addAmountX;
		float addAmountY = this.velocity.y * delta;
		float newY = this.position.y + addAmountY;

		boolean arenacollisionX = false;
		boolean arenacollisionY = false;
		if(this.haveBuff(52) != -1)
		{
			Buff b = this.buffs[this.haveBuff(52)];
			if(b.getOrigin()!=null)
			{
				Vector2 center = null;
				for(Projectile p : Constant.getProjectileList())
				{
					if(p.type == 77 && p.owner == player)
					{
						center = p.Center();
						break;
					}
				}
				if(center != null)
				{
					Vector2 newCenter = new Vector2(newX + this.width/2f, this.position.y + this.height/2f);
					if(newCenter.dst(center) > 560)
					{
						arenacollisionX = true;
					}
					newCenter = new Vector2(this.position.x+ this.width/2f, newY + this.height/2f);
					if(newCenter.dst(center) > 560)
					{
						arenacollisionY = true;
					}
				}
			}
		}

		if(!timestopped)
		{
			if(!Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, newY, (int)(this.width*this.scale), (int)(this.height*this.scale))&&!arenacollisionY)
			{
				this.position.y = newY;
				this.grounded = false;
			}
			else if(arenacollisionY)
			{
				this.velocity.y = 0f;
			}
			else
			{
				if(this.velocity.y < 0f)
				{
					//this.position.y = (float) (Math.floor(this.position.y) - Math.floor(this.position.y) % Tile.TILE_SIZE);
					this.position.y -= Math.floor(this.position.y) % Tile.TILE_SIZE;
					this.grounded = true;
				}
				else if(this.velocity.y > 0f)
				{
					this.position.y = this.position.y;
					this.grounded = false;
				}
				this.velocity.y = 0f;
			}
			if(!Constant.tryGetMapForEntity(this).doesRectCollideWithMap(newX, this.position.y, (int)(this.scale*this.width), (int)(this.height*this.scale))&&!arenacollisionX)
			{
				this.position.x = newX;
			}
			else if(arenacollisionX)
			{
				this.velocity.x = 0f;
			}
			else
			{
				if(this.velocity.x < 0f)
				{
					//this.position.x = (float) (Math.floor(this.position.x) - Math.floor(this.position.x) % Tile.TILE_SIZE);
					this.position.x -= Math.floor(this.position.x) % Tile.TILE_SIZE;
				}
				else if(this.velocity.x > 0f)
				{
					//this.position.y = (float) (Math.floor(this.position.y) + (Tile.TILE_SIZE - Math.floor(this.position.y) % Tile.TILE_SIZE) - (this.height - Tile.TILE_SIZE));
					this.position.x = this.position.x;
				}

				this.velocity.x = 0;
			}
		}
		this.oldai = this.ai.clone();
	}

	private void applyBuffEffects(int slot)
	{
		Player player = null;
		if(this.buffs[slot].getOrigin() instanceof Player)
		{
			player = (Player)this.buffs[slot].getOrigin();
		}
		int id = this.buffs[slot].id;
		Buff b = this.buffs[slot];
		if(id == 2 && player != null)
		{
			if(Constant.gameTick() % 15 == 0)
			{
				this.hurt(this.buffs[slot].stacks, 0, 0f, false, Constant.DAMAGETYPE_POISON, player);
			}
		}
		else if(id == 13 && player != null)
		{
			if(Constant.gameTick() % 15 == 0)
			{
				int damage = (int)((800f * 0.04f) * (1f + player.getIntelligence()*0.0075f + player.getStrenght()*0.0075f + player.getLethality()*0.01f));
				this.hurt(damage * this.buffs[slot].stacks, 0, 0f, false, Constant.DAMAGETYPE_HOLY, player);
			}
		}
		else if(id == 34 && player != null)
		{
			if(b.ticks % 30 == 0)
				this.hurt(b.stacks, 0, 0f, false, Constant.DAMAGETYPE_FIRE, player);
		}
		/*else if(id == 52 && player != null)
		{
			Vector2 center = null;
			for(Projectile p : Constant.getProjectileList())
			{
				if(p.type == 77 && p.owner == player)
				{
					center = p.Center();
					break;
				}
			}
			if(center != null)
			{
				if(this.Center().dst(center) > 375)
				{
					float angle = Main.angleBetween(center, this.Center());
					this.position = center.cpy().add(350f, 0f).rotate(angle).sub(this.width/2f, 0f);
					this.velocity = Vector2.Zero.cpy();
				}
			}
		}*/
		b.ticks++;
	}

	public void draw(float delta, SpriteBatch batch, boolean transp)
	{
		for(int i = 0;i < 30;i++)
		{
			try
			{
				if(this.buffs[i] != null)
				{
					this.buffs[i].backDraw(this, batch);
				}
			}
			catch(NullPointerException ex)
			{
				this.buffs = new Buff[30];
			}
		}

		boolean closest = false;

		if(Main.getMouseTarget() == this)
			closest = true;

		if(!transp)
		{
			if(this.framesSinceLastHit() <= 10)
			{
				int stage = Constant.gameTick()/5;
				if(stage % 2 == 0)
				{
					batch.setShader(Content.whiteS);
				}
				else
				{
					batch.setShader(Content.colorS);
					Color color = Constant.ELEMENTCOLOR[this.lastHitElement];
					if(this.lastHitElement == Constant.DAMAGETYPE_PHYSICAL)
						color = new Color(1f, 0f, 0f, 1f);

					Content.colorS.setUniformf("color", color.r, color.g, color.b);
				}
			}
		}

		float width = 0;
		float height = 0;
		if(this.id == 37)
		{
			Sprite sprite = new Sprite(Content.extras[1]);
			sprite.setPosition(this.Center().x-(int)(sprite.getWidth()/2), this.position.y);
			sprite.setFlip(this.direction == -1, false);
			sprite.setRotation(Constant.gameTick() * -5 * this.direction);
			if(transp)
				sprite.setAlpha(0.5f);
			sprite.draw(batch);
		}
		if(!this.overrideDraw && !this.natural)
		{
			Sprite sprite = new Sprite(Content.monsters[this.id-1]);
			int h = (int) (sprite.getHeight()/this.frames);
			int w = (int) sprite.getWidth();
			sprite.setRegion(0, this.actualFrame * h, w, h);
			sprite.setSize(w, h);
			sprite.setFlip(this.direction == -1, false);
			if(transp)
				sprite.setAlpha(0.5f);
			if(this.haveBuff(15) != -1)
			{
				sprite.setColor(new Color(0f, 0.75f, 1f, 1f));
			}
			if(this.id == 30 && this.health < this.maxHealth * 0.4f)
			{
				sprite.setColor(0.8f, 0.4f, 0.4f, 1f);
			}
			if(this.centeredOrigin)
			{
				sprite.setOriginCenter();
			}
			else
			{
				sprite.setOrigin(0, 0);
			}
			if((this.rotation >= 90 && this.rotation <= 270) || (this.rotation <= -90 && this.rotation >= -270))
				sprite.flip(false, true);

			sprite.setScale(this.scale);
			sprite.setRotation(this.rotation);
			sprite.setPosition(this.Center().x - (w*this.scale)/2f, this.position.y);
			if(transp)
			{
				sprite.setPosition((this.savedPosition.x+this.width/2)- (w*this.scale)/2f, this.savedPosition.y);
			}
			this.drawEnt(batch, sprite);
			width = sprite.getWidth();
			height = sprite.getHeight();
			/*x = sprite.getX();
			y = sprite.getY();*/
		}
		else if(!this.overrideDraw && this.natural)
		{
			Sprite sprite = new Sprite(Content.monsters[this.id-1]);
			width = (int) sprite.getWidth();
			sprite.setPosition(this.Center().x - (width*this.scale)/2f, this.position.y);
			if(transp)
			{
				sprite.setPosition((this.savedPosition.x+this.width/2)- (width*this.scale)/2f, this.savedPosition.y);
			}
			sprite.setFlip(this.direction == -1, false);
			if(this.framesSinceLastHit() <= 30)
			{
				int mult = MathUtils.randomBoolean() ? 1 : -1;
				sprite.setX(sprite.getX() + (30 - this.framesSinceLastHit()) * 16 / 16 * mult);
			}
			if(this.haveBuff(15) != -1)
			{
				sprite.setColor(new Color(0f, 0.75f, 1f, 1f));
			}
			if(this.centeredOrigin)
			{
				sprite.setOriginCenter();
			}
			else
			{
				sprite.setOrigin(0, 0);
			}

			sprite.setScale(this.scale);
			if(this.rotation % 360 >= 90 && this.rotation % 360 <= 180)
				sprite.setFlip(sprite.isFlipX(), !sprite.isFlipY());

			if(Main.usingSkill(Main.player[Main.me]))
			{
				float v = 1-Main.usingSkillMinus(Main.player[Main.me]);
				sprite.setColor(sprite.getColor().mul(v, v, v, 1f));
			}

			sprite.setRotation(this.rotation);
			if(transp)
				sprite.setAlpha(0.5f);
			sprite.draw(batch);
		}


		if(this.movementAi == Constant.MOVEMENTAI_MARKSMAN2K18LUL)
		{
			Sprite sprite = new Sprite(Content.extras[this.ai[Constant.AI8_MONSTEREXTRAID]]);
			int h = (int) (sprite.getHeight()/this.ai[Constant.AI8_MONSTEREXTRAFRAMES]);
			int w = (int) sprite.getWidth();
			sprite.setPosition(this.position.x - w/2f + this.width/2f, this.position.y);
			if(this.ai[12]/3 < this.ai[Constant.AI8_MONSTEREXTRAFRAMES]-this.frames)
				sprite.setRegion(0, (this.frames+this.ai[12]/3) * h, w, h);
			else
				sprite.setRegion(0, this.actualFrame * h, w, h);
			sprite.setSize(w, h);
			sprite.setFlip(this.direction == -1, false);
			sprite.setScale(this.scale);
			sprite.setRotation(this.rotation);
			if(transp)
				sprite.setAlpha(0.5f);
			sprite.draw(batch);
		}

		if(this.id == 5)
		{
			Player player = this.getTarget();
			float angle = (float)Math.toDegrees((Math.atan2(player.Center().y - this.Center().y, player.Center().x - this.Center().x)));
			float sin = (float) Math.sin(angle * Math.PI/180);
			float cos = (float) Math.cos(angle * Math.PI/180);
			Sprite sprite = new Sprite(Content.monsters[this.id-1]);
			sprite.setPosition(this.position.x, this.position.y);
			sprite.draw(batch);
			sprite = new Sprite(Content.scheye);
			if(transp)
				sprite.setAlpha(0.5f);
			sprite.setPosition(this.position.x + cos * 60 - sprite.getWidth()/2, this.position.y + sin * 60-sprite.getHeight()/2);
			sprite.draw(batch);
			for(int i = 0;i < 20;i++)
			{
				float value = (float) (MathUtils.random()*Math.PI*2);
				sin = (float) Math.sin(value);
				cos = (float) Math.cos(value);
				Particle p = Particle.Create(new Vector2(cos * MathUtils.random(150f), sin * MathUtils.random(150f)), Vector2.Zero, 4, new Color(value,0f,value,1f), 0f, 1f, (MathUtils.random()*0.5f+0.75f)*0.1f);
				p.position.add(this.Center());
				p.lights = true;
				p.lightColor = p.color;
				p.lightSize = 8;
				p.drawBack = true;
			}
			for(int j = 0;j < 4;j++)
			{
				for(int i = 0;i < 4;i++)
				{
					float e = 90*j;
					float value = (float) (MathUtils.random(40+e,50+e));
					sin = (float) Math.sin((Constant.gameTick()+value)*Math.PI/180);
					cos = (float) Math.cos((Constant.gameTick()+value)*Math.PI/180);
					float mult = 5-Math.abs((45+e)-value);
					Particle p = Particle.Create(new Vector2(cos * MathUtils.random(100 + mult * 20, 150 + mult * 10), sin * MathUtils.random(100 + mult * 20, 150 + mult * 10)), Vector2.Zero, 2, new Color(value,0f,value,1f), 0f, 1f, (MathUtils.random()*0.5f+0.75f));
					p.position.add(this.Center());
					p.lights = true;
					p.lightColor = p.color;
					p.lightSize = 32;
					p.drawBack = true;
				}
			}
		}
		else if(this.id == 33)
		{
			Sprite sprite = new Sprite(Content.monsters[32]);
			width = (int) (sprite.getWidth()/2);
			height = (int) (sprite.getHeight()/2);
			sprite.setPosition(this.Center().x-width, this.Center().y-height);
			sprite.setRotation(this.rotation);
			if(transp)
				sprite.setAlpha(0.5f);
			sprite.draw(batch);
			if(Constant.gameTick() % 5 == 0)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 16, new Color(1f, 0.8f, 0.45f, 1f), 0f, 0.5f, 1f);
				p.parent = this;
			}
		}

		if(this.id == 21 && this.ai[14] > 0)
		{
			Lighting l = Lighting.Create(this.Center(), 512, Constant.JEWELCOLOR[this.ai[14]-1], 1f, true);
			l.hadParent = true;
			l.parent = this;
			Sprite sprite = new Sprite(Content.extras[19+this.ai[14]]);
			int h = (int) (sprite.getHeight()/this.frames);
			int w = (int) sprite.getWidth();
			sprite.setRegion(0, this.actualFrame * h, w, h);
			sprite.setSize(w, h);
			if(transp)
				sprite.setAlpha(0.5f);
			sprite.setFlip(this.direction == -1, false);
			if(this.haveBuff(15) != -1)
			{
				sprite.setColor(new Color(0f, 0.75f, 1f, 1f));
			}
			if(this.id == 30 && this.health < this.maxHealth * 0.4f)
			{
				sprite.setColor(0.8f, 0.4f, 0.4f, 1f);
			}
			if(this.centeredOrigin)
			{
				sprite.setOriginCenter();
			}
			else
			{
				sprite.setOrigin(0, 0);
			}
			if((this.rotation >= 90 && this.rotation <= 270) || (this.rotation <= -90 && this.rotation >= -270))
				sprite.flip(false, true);

			sprite.setScale(this.scale);
			sprite.setRotation(this.rotation);
			if(!transp)
			{
				if(closest && this.framesSinceLastHit() > 30)
				{
					batch.setShader(Content.redS);
					sprite.setPosition(this.position.x - w/2f + this.width/2f + 2, this.position.y - 2);
					sprite.draw(batch);
					sprite.setPosition(this.position.x - w/2f + this.width/2f - 2, this.position.y + 2);
					sprite.draw(batch);
					sprite.setPosition(this.position.x - w/2f + this.width/2f + 2, this.position.y + 2);
					sprite.draw(batch);
					sprite.setPosition(this.position.x - w/2f + this.width/2f - 2, this.position.y - 2);
					sprite.draw(batch);
					batch.setShader(Main.basicShader);
				}
			}
			sprite.setPosition(this.position.x - w/2f + this.width/2f, this.position.y);
			sprite.draw(batch);
			width = sprite.getWidth();
			height = sprite.getHeight();
		}

		batch.setShader(Main.basicShader);

		if(this.haveBuff(15) != -1)
		{
			Sprite ice = new Sprite(Content.bonice);
			ice.setPosition(this.position.x - 8, this.position.y);
			ice.setSize(this.width + 16, this.height + 16);
			ice.draw(batch);
		}
	}

	private int framesSinceLastHit()
	{
		return Constant.gameTick() - this.lastHitTick;
	}

	public void postDraw(SpriteBatch batch)
	{
		if(this.id == 2 || this.id == 4)
		{
			Sprite sprite = new Sprite(Content.gslegs);
			sprite.setPosition(this.position.x, this.position.y);
			sprite.setRegion(0, this.actualFrame * this.height, this.width, this.height);
			sprite.setSize(this.width, this.height);
			sprite.setFlip(this.direction == -1, false);
			sprite.draw(batch);
		}
		else if(this.id == 37)
		{
			Sprite sprite = new Sprite(Content.extras[2]);
			sprite.setPosition(this.Center().x-(int)(sprite.getWidth()/2), this.position.y);
			sprite.setFlip(this.direction == -1, false);
			sprite.setRotation(Constant.gameTick() * -5 * this.direction);
			sprite.draw(batch);
		}
		GlyphLayout layout = Main.layout;
		Main.font.getData().setScale(Main.camera.zoom);
		layout.setText(Main.font, this.name);
		Sprite sprite = new Sprite(Content.monsters[this.id-1]);
		float posX = this.Center().x - layout.width/2f;
		float posY = this.position.y + this.height*this.scale + layout.height + 4;
		for(int i = 0;i < 30;i++)
		{
			try
			{
				if(this.buffs[i] != null)
				{
					this.buffs[i].draw(this, batch);
				}
			}
			catch(NullPointerException ex)
			{
				this.buffs = new Buff[30];
			}
		}
		String namedraw = this.name + "";
		if(Main.debug)
		{
			boolean isnatural = false;
			if(Constant.getNaturalList().indexOf(this) != -1)
			{
				isnatural = true;
			}
			namedraw += " [" + this.uid + "][" + isnatural + "]";
		}
		if(this.tagSymbol > 0)
		{
			Sprite exc = null;

			if(tagSymbol == Constant.TAGSYMBOL_EXCLAMATION)
				exc = new Sprite(Content.exclamation);
			else if(tagSymbol == Constant.TAGSYMBOL_INTERROGATIONGREEN)
				exc = new Sprite(Content.interrogation);
			else if(tagSymbol == Constant.TAGSYMBOL_INTERROGATIONGRAY)
				exc = new Sprite(Content.interrogation2);

			int bonusY = 0;
			float sin = (float)Math.abs(Math.sin(((Main.gameTick * 5) % 360) * Math.PI/180));
			if(exc != null)
			{
				exc.setPosition(this.Center().x - exc.getWidth()/2, this.position.y + this.height + 16 + bonusY + sin * 16);
				exc.draw(batch);
			}
		}
		Color color = Color.WHITE;
		int difference = this.recommendedLevel-Main.player[Main.me].level;

		if(difference < -10)
			color = new Color(0f, 1f, 1f, 1f);
		else if(difference > 20)
			color = new Color(0f, 0f, 0f, 1f);
		else if(difference > 10)
			color = new Color(1f, 0f, 0f, 1f);
		else if(difference > 5)
			color = new Color(0.95f, 0.75f, 0.05f, 1f);
		else if(difference > -5)
			color = new Color(1f, 1f, 0f, 1f);

		Main.prettyFontDraw(batch, namedraw, posX, posY, Main.camera.zoom, color, difference > 20 ? new Color(0.5f, 0f, 0f, 1f) : Color.BLACK, 1f, false, -1);

		if(!this.natural || this.health < this.maxHealth)
		{
			if(!this.boss)
			{
				sprite = new Sprite(Content.black);
				sprite.setPosition(this.Center().x - 32, this.position.y - 16);
				sprite.setSize(64, 8);
				sprite.draw(batch);

				Sprite bar = new Sprite(Content.healthBar);
				bar.setPosition(this.Center().x - 32, this.position.y - 16);
				bar.setSize(this.health * 64 / this.maxHealth, 8);
				bar.draw(batch);
			}
			else
			{
				sprite = new Sprite(Content.black);
				sprite.setPosition(this.Center().x - 64, this.position.y - 24);
				sprite.setSize(128, 16);
				sprite.draw(batch);

				Sprite bar = new Sprite(Content.healthBar);
				bar.setPosition(this.Center().x - 64, this.position.y - 24);
				bar.setSize(this.health * 128 / this.maxHealth, 16);
				bar.draw(batch);
			}
		}
	}

	public static Monster Create(GameMap g, Vector2 pos, int type)
	{
		Monster p = new Monster();
		for(int i = 0;i < 30;i++)
		{
			p.buffs[i] = null;
		}
		for(int i = 0;i < 15;i++)
		{
			p.ai[i] = 0;
		}
		p.id = type;
		p.boss = false;
		p.Reset();
		p.active = true;
		p.position = pos.cpy();
		p.velocity = Vector2.Zero.cpy();
		if(!p.natural)
			g.monsters.add(p);
		else
			g.naturals.add(p);

		p.uid = MathUtils.random(1, 9999999);
		p.whoAmI = g.monsters.indexOf(p);
		return p;
	}
	public static Monster Create(Vector2 pos, int type, int mapX, int mapY)
	{
		return Create(pos, type, mapX, mapY, MathUtils.random(1, 9999999));
	}

	public static Monster Create(Vector2 pos, int type, int mapX, int mapY, int uid)
	{
		if((Main.isOnline))
		{
			return new Monster();
		}
		else
		{
			Monster p = new Monster();
			for(int i = 0;i < 30;i++)
			{
				p.buffs[i] = null;
			}
			for(int i = 0;i < 15;i++)
			{
				p.ai[i] = 0;
			}
			p.id = type;
			p.boss = false;
			p.Reset();
			p.active = true;
			p.myMapX = mapX;
			p.myMapY = mapY;
			p.position = pos.cpy();
			p.velocity = Vector2.Zero.cpy();
			p.uid = uid;
			if(!p.natural)
				Constant.getMonsterExList().add(p);
			else
				Constant.getNaturalExList().add(p);
			return p;
		}
	}

	private boolean checkDeath(int direction2, float scalar, Entity attacker) {
		if(this.health <= 0 && this.active)
		{
			Player player = Constant.getPlayerList()[lastHitter];
			for(int i = Constant.ITEMSLOT_OFFSET;i <= Constant.ITEMSLOT_MAX;i++)
			{
				if(player.inventory[i] != null)
					player.inventory[i].onKill(this, player);
			}
			this.active = false;
			for(int i = 0;i < this.position.dst(new Vector2(this.position.x + this.width, this.position.y + this.height)) * this.scale;i++)
			{
				//Particle.Create(new Vector2(this.position.x + MathUtils.random(this.width), this.position.y + MathUtils.random(this.height)), new Vector2(MathUtils.random(-50, 50), MathUtils.random(-50, 50)), 3, Color.WHITE, 0f, 1.5f, MathUtils.random(0.5f, 1f));
				// eita porra Particle p = Particle.Create(new Vector2(this.position.x + MathUtils.random(this.width), this.position.y + MathUtils.random(this.height)), new Vector2(MathUtils.random(-50, 50), MathUtils.random(0, 100)), 2, this.rColor, 0f, 1.5f, MathUtils.random(0.5f, 1f));
				int max = this.rColor.size();
				if(max > 0)
				{
					Color c = this.rColor.get(MathUtils.random(max-1));
					Particle p = Particle.Create(new Vector2(this.position.x + MathUtils.random(this.width), this.position.y + MathUtils.random(this.height)), new Vector2(MathUtils.random(-100, 100), MathUtils.random(0, 400)), 2, c, -5f, 2.5f, MathUtils.random(1f, 2f));
					if(direction2 != 0)
						p.velocity.add(new Vector2(200 * scalar * direction2, 0f));

					p.collisions = true;
					p.loseSpeed = true;
				}
			}
			this.onDeath(attacker);
			this.active = false;
			return true;
		}
		return false;
	}

	public void onDeath(Entity attacker)
	{
		if(attacker != null && attacker instanceof Player && !this.natural)
		{
			Player player = (Player)attacker;
			Skill s = player.getEquippedSkill(34);
			if(s != null)
			{
				for(int i = 0;i < player.skills.length;i++)
				{
					if(player.skills[i] != null)
					{
						player.skills[i].cdf -= s.getInfoValueF(player, 0);
					}
				}
				Skill dt = player.getEquippedSkill(14);
				if(dt != null)
				{
					dt.casts = 0;
					dt.cdf = 0f;
					dt.ticksFromCast = 9999;
				}
				s.applyCast(player);
			}
			if(player.getEquippedItem(49) != null)
			{
				player.addBuff(42, 5f, player);
			}
			s = player.getSkill(120);
			if(s != null)
			{
				s.cdf = 0f;
			}
			if(player.getEquippedSkill(73) != null)
			{
				s = player.getSkill(73);
				float mult = s.getInfoValueF(player, 1)/100f;
				for(int i = 0;i < player.skillSlotAvailable;i++)
				{
					if(player.skills[i] != null && player.skills[i].casts == 0)
						player.skills[i].cdf -= (player.skills[i].getCooldown(player) * mult);
				}
				player.heal((s.getInfoValueF(player, 0)/100f) * player.maxHealth);
				s.applyCast(player);
			}
			s = player.getSkill(105);
			if(s != null)
			{
				for(int j = 0;j < Constant.getPlayerList().length;j++)
				{
					if(Constant.getPlayerList()[j].sameMapAs(player))
					{
						Constant.getPlayerList()[j].mana += player.maxMana;
						for(int i = 0;i < player.skillSlotAvailable;i++)
						{
							if(player.skills[i] != null && player.skills[i].casts == 0)
								player.skills[i].cdf -= s.getInfoValueF(player, 0);
						}
					}
				}
			}

			Item item = player.getEquippedItem(164);
			if(item != null)
			{
				player.heal(item.getInfoValueI(0));
			}
			item = player.getEquippedItem(165);
			if(item != null)
			{
				player.addBuff(67, 10f, player);
			}
		}
		if(Main.debug)
			System.out.println(this.name + ": " + this.xpdrop() + " experience calculated.");
		for(int i = 0;i < 4;i++)
		{
			Player player = Constant.getPlayerList()[i];
			if(!player.active)
				continue;

			player.experience += this.xpdrop();
			if(this.id == 3)
			{
				player.experience += 10000000;
			}
			if(this.id == 2)
			{
				player.experience += 100000000;
			}

			for(Quest q : player.quests)
			{
				for(Objective o: q.objectives)
				{
					if(o.id == 1 && o.infos[0] == this.id)
						o.infos[2]++;
				}
			}
		}

		if(this.drops.size() > 0)
		{
			for(int i : this.drops.keySet())
			{
				float mult = 1f;
				if(attacker instanceof Player)
				{
					mult += ((Player)attacker).getLuck()*0.02f;
				}
				float chance = this.drops.get(i)*mult;
				if(chance < 1f && MathUtils.randomBoolean(chance))
				{
					this.dropItem(i);
				}
				else if(chance >= 1f)
				{
					for(float x = 0;x < MathUtils.random(1, chance+1);x++)
					{
						this.dropItem(i);
					}
				}
			}
		}
		if(this.swordTombSummon)
		{
			NPC sword = null;
			float distance = 999999;
			for(NPC n : Constant.getNPCList())
			{
				if(n.id == 28 && n.sameMapAs(this) && n.Center().dst(this.Center()) < distance)
				{
					distance = n.Center().dst(this.Center());
					sword = n;
				}
			}
			if(sword != null)
			{
				sword.infos[0]++;
				for(int i = 0;i < Math.max(1, this.position.dst(new Vector2(this.position.x + this.width, this.position.y + this.height))/30);i++)
				{
					Projectile p = Projectile.Summon(66, this.randomHitBoxPosition(), new Vector2(1000f, 0f).rotate(MathUtils.random(360)), 0, 5f, attacker);
					p.target = sword;
				}

				boolean finished = true;
				NPC furthest = null;
				float maxX = 0;
				for(NPC n : Constant.getNPCList())
				{
					if(n.id == 28 && n.sameMapAs(this))
					{
						if(n.currentFrame == 0 || n.infos[0] < n.infos[1])
						{
							finished = false;
							break;
						}
						if(n.Center().x > maxX)
						{
							furthest = n;
							maxX = n.Center().x;
						}
					}
				}
				if(finished && furthest != null)
				{
					Treasure treasure = new Treasure(furthest.Center().sub(50, 0));
					treasure.myMapX = this.myMapX;
					treasure.myMapY = this.myMapY;
					treasure.content.addAll(this.getTreasureList());
					Constant.getTreasureList().add(treasure);
					for(NPC n : Constant.getNPCList())
					{
						n.active = false;
					}
				}

			}
		}
		for (Iterator<Respawn> j = Constant.getRespawnsList().iterator(); j.hasNext();) {
			Respawn p = j.next();
			if(this.uid == p.uid)
			{
				p.setNextProc();
			}
		}
		float pitch = Main.clamp(0.5f, (float) (MathUtils.random(-0.05f, 0.05f) + 1f/(Math.log(this.width * this.height)/8.2f)), 2f);
		DJ.playSound(DJ.DEATH, 1f, pitch, this.Center());
	}

	private ArrayList<Item> getTreasureList()
	{
		ArrayList<Item> ret = new ArrayList<Item>();
		GameMap gm = Constant.tryGetMapForEntity(this);
		if(gm != null)
		{
			if(gm.id == 141)
			{
				if(MathUtils.randomBoolean(0.8f))
				{
					Item item = new Item().SetInfos(115);
					ret.add(item);
				}
				for(int i = 0;i < MathUtils.random(1, 3);i++)
				{
					Item it = new Item().SetInfos(140);
					ret.add(it);
				}
				for(int i = 0;i < MathUtils.random(8, 10);i++)
				{
					Item it = new Item().SetInfos(78);
					ret.add(it);
				}
				for(int i = 0;i < MathUtils.random(3, 5);i++)
				{
					Item item = new Item().SetInfos(89);
					ret.add(item);
				}
			}
			else if(gm.id == 181)
			{
				if(MathUtils.randomBoolean(0.8f))
				{
					Item item = new Item().SetInfos(116);
					ret.add(item);
				}
			}
		}
		return ret;
	}

	public void dropItem(int itemid)
	{
		Item.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-350, 350), MathUtils.random(400, 600)), itemid, this.myMapX, this.myMapY, false);
	}

	public boolean hurt(int damage2, int direction, float scalar, boolean critical, int damageType, Player attacker)
	{
		return hurt(damage2, direction, scalar, critical, damageType, -1, attacker);
	}

	public boolean hurt(int damage, int direction, float scalar, boolean critical, int damageType, int damageClass, Player attacker)
	{
		if(attacker != null)
			this.lastHitter = attacker.whoAmI;

		if(critical)
		{
			damage *= attacker.critMult;
		}

		damage = this.afterDamageCalculations(damage, critical, damageType, damageClass, attacker);

		if(this.target == -1)
			this.target = attacker.whoAmI;
		return this.applyDamage(damage, direction, scalar, critical, attacker, damageType, damageClass, false);
	}

	public boolean applyDamage(int damage, int direction, float scalar, boolean critical, Player attacker, int damageType, int damageClass, boolean serverPermission)
	{
		if(Main.isOnline && !serverPermission)
			return false;

		if(AHS.isUp)
		{
			DamageRequest dr = new DamageRequest(damage, direction, scalar, critical, attacker.whoAmI, damageType,
					damageClass, this.uid);
		}
		damage = Math.min(damage, this.health);
		this.health -= damage;
		float scale = 0.5f + (damage * 1f)/1000f;
		scale = (scale > 1.5f ? 1.5f : scale);
		if(this.natural)
			scale = 1;

		LivingText l = LivingText.Create(this.position.cpy().add(this.width/2, this.height), new Vector2(MathUtils.random(-100, 100), MathUtils.random(200, 600)), String.valueOf(damage), Constant.ELEMENTCOLOR[damageType], scale, 1.5f, true, critical, this);
		if(critical)
			l.timeLeft += 0.5f;
		if(direction != 0 && !this.boss && !this.knockbackImmune)
		{
			this.velocity = new Vector2(500 * direction * MathUtils.random(scalar * 0.75f, scalar * 1.25f), (!this.grounded ? 0 : 400 * scalar));
			this.uncapped = 0f;
		}
		if(this.movementAi == Constant.MOVEMENTAI_FLYINGKNIGHT && this.ai[2] == 1)
		{
			this.ai[2] = 0;
			this.ai[1] = 0;
		}

		boolean ret = checkDeath(direction, scalar, attacker);
		this.lastHitTick = Constant.gameTick();
		this.onHitted(damage, damageType, critical);
		this.lastHitElement = damageType;
		if(attacker != null && !this.natural)
		{
			float steal = attacker.lifeSteal;
			if(this.haveBuff(61) != -1)
			{
				Entity origin = this.buffs[this.haveBuff(61)].getOrigin();
				if(origin instanceof Player)
				{
					Player player = (Player)origin;
					Skill s = player.getSkill(109);
					if(s != null)
					{
						steal += s.getInfoValueF(player, 0)/100f;
					}
				}
			}
			Skill s = attacker.getEquippedSkill(110);
			if(damageType == Constant.DAMAGETYPE_BLOOD && s != null)
			{
				steal += s.getInfoValueF(attacker, 0)/100f;
				s.applyCast(attacker);
			}
			attacker.heal(damage * steal);
			attacker.lastMonsterHitTicks = 0;
			if(attacker.classType == ArkaClass.ARCANETRICKSTER)
			{
				ArkaClass.applyArcaneMark(this, attacker);
			}
		}
		for(Projectile p : Constant.getProjectileList())
		{
			if(p.type == 97 && this.Center().dst(p.Center()) < 400 && p.ai[6] <= 0)
			{
				p.ai[6] = 1;
				Circle c = new Circle(p.Center(), 400);
				for(Monster m : Constant.getMonsterList(false))
				{
					if(m != this && Intersector.overlaps(c, m.hitBox()))
					{
						m.hurtDmgVar(damage, 0, 0, critical, damageType, attacker);
					}
				}
			}
		}
		return ret;
	}

	public void onHitted(int damage, int damageType, boolean critical)
	{
		if(this.uncapped < -0.25f && !this.knockbackImmune)
		{
			this.uncapped = 0.5f;
		}
		if(this.id == 15)
		{
			for(int i = 0;i < 30;i++)
			{
				Color color = new Color(1f, 1f, 1f, 1f);
				if(MathUtils.randomBoolean())
					color = new Color(0.9f, 1f, 1f, 1f);
				Particle p = Particle.Create(this.randomHitBoxPosition(), new Vector2(MathUtils.random(-8, 8), MathUtils.random(50, 200)), 14, color, -3f, 3f, this.scale);
				p.rotation = MathUtils.random(360);
			}
		}
		else if(this.id == 17)
		{
			for(int i = 0;i < 30;i++)
			{
				Color color = new Color(0f, 0.6f, 0.1f, 1f);
				if(MathUtils.randomBoolean())
					color = new Color(0f, 0.5f, 0.1f, 1f);
				Particle p = Particle.Create(new Vector2(this.position.x + MathUtils.random(-306, 406), this.position.y + 312 + MathUtils.random(500)), new Vector2(MathUtils.random(-8, 8), MathUtils.random(50, 200)), 14, color, -3f, 3f, this.scale/2f);
				p.rotation = MathUtils.random(360);
			}
		}
		else if(this.id == 61)
		{
			for(int i = 0;i < 30;i++)
			{
				Color color = new Color(1f, 0.95f, 0.8f, 1f);
				if(MathUtils.randomBoolean())
					color = new Color(1f, 1f, 1f, 1f);
				Particle p = Particle.Create(new Vector2(this.position.x + MathUtils.random(-306, 406), this.position.y + 312 + MathUtils.random(500)), new Vector2(MathUtils.random(-8, 8), MathUtils.random(50, 200)), 14, color, -3f, 3f, this.scale/2f);
				p.rotation = MathUtils.random(360);
			}
		}
		float pitch = MathUtils.random(0.9f, 1.1f);

		if(critical)
		{
			pitch += 0.15f;
		}
		switch(damageType)
		{
		case Constant.DAMAGETYPE_FIRE:
			DJ.playSound(DJ.FIREHIT, 1f, pitch, this.Center());
			break;
		case Constant.DAMAGETYPE_ICE:
			DJ.playSound(DJ.ICEHIT, 1f, pitch, this.Center());
			break;
		case Constant.DAMAGETYPE_DEATH:
			DJ.playSound(DJ.DEATHHIT, 1f, pitch, this.Center());
			break;
		case Constant.DAMAGETYPE_DARKNESS:
			DJ.playSound(DJ.SHADOWHIT, 1f, pitch, this.Center());
			break;
		case Constant.DAMAGETYPE_BLOOD:
			DJ.playSound(DJ.BLOODHIT, 1f, pitch, this.Center());
			break;
		case Constant.DAMAGETYPE_ENERGY:
			DJ.playSound(DJ.ENERGYHIT, 1f, pitch, this.Center());
			break;
		default:
			DJ.playSound(DJ.PHYSICALHIT, 1f, pitch, this.Center());
			break;
		}
	}

	private int afterDamageCalculations(int damage, boolean critical, int damageType, int damageClass, Player attacker)
	{
		if(this.getBuffStacks(12) >= 3)
		{
			float multiplier = 0f;
			Buff buff = this.buffs[this.haveBuff(12)];
			if(buff.originInfo.equalsIgnoreCase("player"))
			{
				Player player = (Player)buff.getOrigin();
				Skill s = player.getSkill(14);
				multiplier = s.valueByLevel("1.5/1.625/1.75/1.875/2.0");
			}
			damage *= multiplier;
			this.removeBuff(12);
		}
		if(this.haveBuff(29) != -1)
		{
			float multiplier = 0f;
			Buff buff = this.buffs[this.haveBuff(29)];
			if(buff.originInfo.equalsIgnoreCase("player"))
			{
				Player player = (Player)buff.getOrigin();
				Skill s = player.getSkill(31);
				multiplier = 1+ s.getInfoValueF(player, 0)/100f;
			}
			damage *= multiplier;
		}

		if(attacker != null && !this.natural)
		{
			Skill s = attacker.getSkill(120);
			if(s != null)
			{
				s.cdf -= 1f;
			}
			damage *= (1f + attacker.extraElementDamage[damageType]);
			if(attacker.haveBuff(30) != -1)
			{
				attacker.removeBuff(30);
				s = attacker.getSkill(33);
				if(s != null)
				{
					float multiplier = s.getInfoValueF(attacker, 0)/100f + this.getLostHealth(true) * (s.getInfoValueF(attacker, 1)-s.getInfoValueF(attacker, 0))/100f;
					this.hurt((int) (damage * multiplier), 0, 0f, false, Constant.DAMAGETYPE_BLOOD, attacker);
				}
			}
			if(attacker.haveBuff(39) != -1 && Main.isMelee(damageClass))
			{
				attacker.removeBuff(39);
				s = attacker.getSkill(58);
				if(s != null)
				{
					float min = s.getInfoValueF(attacker, 0)/100f;
					float max = s.getInfoValueF(attacker, 1)/100f;
					damage *= (1f + min + (max-min)*attacker.getLostHealth(true));
				}
				for(int i = 0;i < 30;i++)
				{
					Vector2 pos = this.randomHitBoxPosition();
					Vector2 vel = new Vector2(MathUtils.random(1000, 2500), MathUtils.random(-300, 300));
					vel.rotate(Main.angleBetween(attacker.Center(), this.Center()));
					Particle p = Particle.Create(pos, vel, 16, new Color(0.5f + MathUtils.random()/2f, 0f, 0f, 0f), 2f, 1f, 1f);
					p.drawFront = true;
					p.lights = true;
					p.lightColor = Constant.EXPLOSION_DEFAULT_SCHEME[3];
					p.lightSize = 32;
					p.loseSpeed = true;
					p.rotation = MathUtils.random(360);
					Lighting.Create(p.position, 256, Constant.EXPLOSION_DEFAULT_SCHEME[3], p.duration, 0.8f, true);
				}
			}
			if(attacker.haveBuff(47) != -1)
			{
				this.addBuff(48, 5f, attacker);
				float scale = 0.1f + Math.max(this.width, this.height)/128f;
				Projectile p = Projectile.Summon(64, this.Center(), Vector2.Zero, 0, 5f, attacker);
				p.target = this;
				p.rescale(scale);
				attacker.buffs[attacker.haveBuff(47)].timeLeft = 0.1f;
			}
			if(attacker.haveBuff(60) != -1)
			{
				s = attacker.getSkill(106);
				if(s != null)
				{
					attacker.removeBuff(60);
					this.hurt((int)s.getInfoValueF(attacker, 0), 0, 0f, false, Constant.DAMAGETYPE_ENERGY, attacker);
				}
			}

			s = attacker.getEquippedSkill(22);
			if(s != null)
			{
				s.cdf -= 0.5f;
			}

			if(this.haveBuff(48) != -1)
			{
				Buff b = this.buffs[this.haveBuff(48)];
				s = attacker.getSkill(74);
				if(s != null && b.getOrigin()!=null && b.getOrigin().getClass() == Player.class)
				{
					Player origin = (Player)b.getOrigin();
					damage += ((s.getInfoValueF(origin, 0) + s.getInfoValueF(origin, 1))/100f * this.maxHealth);
				}
			}

			if(this.haveBuff(61) != -1 && damageType == Constant.DAMAGETYPE_BLOOD)
			{
				Entity origin = this.buffs[this.haveBuff(61)].getOrigin();
				if(origin instanceof Player)
				{
					Player player = (Player)origin;
					s = player.getSkill(109);
					if(s != null)
					{
						damage += (damage * s.getInfoValueF(player, 1)/100f);
					}
				}
			}
		}
		Item item;
		if(Main.isMelee(damageClass) && (item = attacker.getEquippedItem(131)) != null)
		{
			damage += damage * (item.getInfoValueF(0)/100f)*attacker.getLostHealth(true);
		}

		if(this.natural)
		{
			damage = 1;
		}

		return damage;
	}

	public boolean hurtDmgVar(int damage2, int direction, float scalar, boolean critical, int damageType, int damageClass, Player player)
	{
		float damage = (int) (damage2);

		if(damageType == Constant.DAMAGETYPE_PHYSICAL)
			damage *= player.getPhysicalDamageMult();
		else
			damage *= player.getElementalDamageMult();

		if(Main.isMelee(damageClass) || damageClass == Constant.ITEMCLASS_SHIELD)
			damage *= player.getMeleeDamageMult();
		else if(damageClass == Constant.ITEMCLASS_RANGED)
			damage *= player.getRangedDamageMult();
		else if(damageClass == Constant.ITEMCLASS_MAGIC)
			damage *= player.getMagicDamageMult();

		damage *= MathUtils.random(0.875f, 1.125f);
		return hurt((int)damage, direction, scalar, critical, damageType, damageClass, player);
	}

	public boolean hurtDmgVar(int damage2, int direction, float scalar, boolean critical, int damageType, Player player)
	{
		return hurtDmgVar(damage2, direction, scalar, critical, damageType, -1, player);
	}

	public int haveBuff(int id)
	{
		for(int i = 0;i < 30;i++)
		{
			try
			{
				if(this.buffs[i] != null &&
						this.buffs[i].id == id)
				{
					return i;
				}
			}
			catch(NullPointerException ex)
			{
				this.buffs = new Buff[30];
			}
		}
		return -1;
	}

	public int addBuff(int id, float timeLeft, Entity origin)
	{
		return addBuff(id, 1, timeLeft, origin);
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
					this.buffs[i].SetInfos(id, origin);
					this.buffs[i].timeLeft = timeLeft;
					this.buffs[i].lastTimeLeft = timeLeft;
					this.buffs[i].stacks = stacks;
					if((this.buffs[i].maxStacks > 0 && this.buffs[i].stacks > this.buffs[i].maxStacks))
					{
						this.buffs[i].stacks = this.buffs[i].maxStacks;
					}
					slot = i;
					break;
				}
			}
		}
		return slot;
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

	public Player getNextestPlayer(float dist)
	{
		Player p = null;
		float distance = dist;
		for(int i = 0;i < Constant.getPlayerList().length;i++)
		{
			Player pe = Constant.getPlayerList()[i];
			if(pe.active && pe.myMapX == this.myMapX && pe.myMapY == this.myMapY && (pe.Center().dst(this.Center()) < distance) && pe.canBeTargeted())
			{
				if(this.myMapY < 0 && Math.abs(this.Center().y - pe.Center().y) > 320 && this.target != pe.whoAmI)
				{
					continue;
				}
				p = pe;
				distance = pe.Center().dst(this.Center());
			}
		}
		return p;
	}

	public Player getNextestPlayer()
	{
		return this.getNextestPlayer(1500f);
	}

	public boolean isStunned()
	{
		if(this.haveBuff(15) != -1)
			return true;

		if(this.haveBuff(24) != -1)
			return true;

		return false;
	}

	public Player getTarget()
	{
		if(this.target < 0)
			return null;

		return Constant.getPlayerList()[this.target];
	}

	public float xpdrop()
	{
		if(this.natural)
			return 0;

		float mediaLV = (float) (Math.pow(this.maxHealth/26f, 1.05f+(this.impactDamage/7777f)) + this.impactDamage/2);
		float aux = 1;

		if (boss)
		{
			aux = 3;
			if (this.canFly)
			{
				aux += 0.75f;
			}
			if (this.ranged)
			{
				aux += 0.75f;
			}
		}
		else
		{
			if (this.canFly)
			{
				aux += 0.5f;
			}
			if (this.knockbackImmune == true)
			{
				aux += 0.25f;
			}
			if (this.ranged)
			{
				aux += 0.5f;
			}
		}
		return 5+(mediaLV * aux);
	}

	public float getLostHealth(boolean perc)
	{
		if(!perc)
			return this.maxHealth - this.health;
		else
			return ((float)(this.maxHealth - this.health)/this.maxHealth);
	}

	public float getActualHealth(boolean perc)
	{
		if(!perc)
			return this.health;
		else
			return (float)this.health/this.maxHealth;
	}

	public void Stun(float time, Entity origin)
	{
		boolean canApply = false;
		if(this.haveBuff(24) != -1)
		{
			if(this.buffs[this.haveBuff(24)].timeLeft <= time)
			{
				canApply = true;
			}
		}
		else
		{
			canApply = true;
		}

		if(canApply)
			this.addBuff(24, time, origin);
	}

	public void Burn(int damage, float time, Entity origin)
	{
		boolean canApply = false;
		int totalDamage = (int) (damage * time * 2);
		if(this.haveBuff(34) != -1)
		{
			Buff b = this.buffs[this.haveBuff(34)];
			int actualDamageLeft = (int) (b.timeLeft * 2 * b.stacks);
			if(totalDamage > actualDamageLeft)
			{
				canApply = true;
			}
		}
		else
		{
			canApply = true;
		}
		if(canApply)
		{
			int slot = this.addBuff(34, damage, time, origin);
			this.buffs[slot].stacks = damage;
		}
	}

	public boolean updateRelevant()
	{
		return true;
	}

	public boolean canSpawn()
	{
		if((this.id == 8 || this.id == 29) && Main.isAtDay())
		{
			return false;
		}
		return true;
	}

	public void addAggroDelay(Player p, int ticks)
	{
		this.aggros.put(p.whoAmI, ticks);
	}

	@Override
	public Rectangle hitBox()
	{
		return new Rectangle(this.Center().x-(this.width * this.scale)/2f, this.Center().y-(this.height * this.scale)/2f, this.width * this.scale, this.height * this.scale);
	}

	@Override
	public Vector2 Center()
	{
		return new Vector2(this.position.x + (this.width * this.scale)/2, this.position.y + (this.height * this.scale)/2);
	}
}
