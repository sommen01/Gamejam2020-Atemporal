package com.roguelike.constants;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.entities.Entity;
import com.roguelike.entities.Monster;
import com.roguelike.entities.NPC;
import com.roguelike.entities.Player;
import com.roguelike.entities.Projectile;
import com.roguelike.entities.Prop;
import com.roguelike.entities.Treasure;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;
import com.roguelike.world.GameMap;
import com.roguelike.world.Respawn;

public class Constant
{
	public static final int ITEMSLOT_LEFT = 32;
	public static final int ITEMSLOT_RIGHT = 33;
	public static final int ITEMSLOT_HEAD = 34;
	public static final int ITEMSLOT_BODY = 35;
	public static final int ITEMSLOT_SPECIAL1 = 36;
	public static final int ITEMSLOT_SPECIAL2 = 37;
	public static final int ITEMSLOT_SPECIAL3 = 38;
	public static final int ITEMSLOT_SPECIAL4 = 39;
	public static final int ITEMSLOT_MAX = ITEMSLOT_SPECIAL4;
	public static final int ITEMSLOT_OFFSET = ITEMSLOT_LEFT;
	public static final int ITEMSLOT_FAKE_HOTBAR = 100;

	public static final int ANIMATION_STAND = 0;
	public static final int ANIMATION_WALKING = 1;
	public static final int ANIMATION_INVENTORY = 2;
	public static final int ANIMATION_ATTACKING = 3;
	public static final int ANIMATION_ROLL = 4;
	public static final int ANIMATION_DANCE = 5;

	public static final int DIRECTION_LEFT = -1;
	public static final int DIRECTION_RIGHT = 1;

	public static final int QUALITY_COMMON = 0;
	public static final int QUALITY_UNCOMMON = 1;
	public static final int QUALITY_RARE = 2;
	public static final int QUALITY_EPIC = 3;
	public static final int QUALITY_LEGENDARY = 4;
	public static final int QUALITY_IMAGINARY = 5;

	public static final int ITEMTYPE_LEFT = 0;
	public static final int ITEMTYPE_RIGHT = 1;
	public static final int ITEMTYPE_HEAD = 2;
	public static final int ITEMTYPE_BODY = 3;
	public static final int ITEMTYPE_SPECIAL = 4;

	public static final int DAMAGETYPE_PHYSICAL = 0;
	public static final int DAMAGETYPE_POISON = 1;
	public static final int DAMAGETYPE_FIRE = 2;
	public static final int DAMAGETYPE_DARKNESS = 3;
	public static final int DAMAGETYPE_ICE = 4;
	public static final int DAMAGETYPE_NATURE = 5;
	public static final int DAMAGETYPE_HOLY = 6;
	public static final int DAMAGETYPE_AIR = 7;
	public static final int DAMAGETYPE_ENERGY = 8;
	public static final int DAMAGETYPE_BLOOD = 9;
	public static final int DAMAGETYPE_GOOD = 10;
	public static final int DAMAGETYPE_DEATH = 11;

	public static final int OVERRIDEATTACK_OVERRIDE = 0;
	public static final int OVERRIDEATTACK_SWING = 1;
	public static final int OVERRIDEATTACK_ROTATE = 2;
	public static final int OVERRIDEATTACK_BIGSWING = 3;
	public static final int OVERRIDEATTACK_BIGROTATE = 4;

	public static final int OVERRIDESTAND_OVERRIDE = 0;
	public static final int OVERRIDESTAND_HILT = 1;
	public static final int OVERRIDESTAND_MIDDLE = 2;
	public static final int OVERRIDESTAND_HEAD = 3;
	public static final int OVERRIDESTAND_BODY = 4;
	public static final int OVERRIDESTAND_FRONT = 5;
	public static final int OVERRIDESTAND_ORB = 6;
	
	public static final int SKILLSLOT_WEAPON = 10;

	public static final int AICOLLISIONDIRECTION = 1;

	public static final int AIDIRECTION_DOWN = 1;
	public static final int AIDIRECTION_UP = 2;
	public static final int AIDIRECTION_LEFT = 3;
	public static final int AIDIRECTION_RIGHT = 4;
	public static final int AIDIRECTION_NONE = 0;

	public static final float MAPZOOM_CLOSE = 0.5f;
	public static final float MAPZOOM_NORMAL = 1f;
	public static final float MAPZOOM_OUTSIDE = 1.33f;
	public static final float MAPZOOM_BIG = 2f;
	public static final float MAPZOOM_HUGE = 4f;

	public static final int ITEMCLASS_MATERIAL = 0;
	public static final int ITEMCLASS_SHORTSWORD = 1;
	public static final int ITEMCLASS_RANGED = 2;
	public static final int ITEMCLASS_MAGIC = 3;
	public static final int ITEMCLASS_PASSIVE = 4;
	public static final int ITEMCLASS_HAT = 5;
	public static final int ITEMCLASS_BODYGUARD = 6;
	public static final int ITEMCLASS_SHIELD = 7;
	public static final int ITEMCLASS_SCROLL = 8;
	public static final int ITEMCLASS_LONGSWORD = 9;
	public static final int ITEMCLASS_CLUB = 10;
	public static final int ITEMCLASS_HAMMER = 11;
	public static final int ITEMCLASS_HEAVYHAMMER = 12;
	public static final int ITEMCLASS_HEAVYCLUB = 13;
	public static final int ITEMCLASS_AXE = 14;
	public static final int ITEMCLASS_HEAVYAXE = 15;
	public static final int ITEMCLASS_DAGGER = 16;
	public static final int ITEMCLASS_LANCE = 17;
	public static final int ITEMCLASS_BROADSWORD = 18;
	public static final int ITEMCLASS_USABLE = 19;
	public static final int ITEMCLASS_ACCESSORY = 20;
	public static final int ITEMCLASS_ORB = 21;
	
	public static final int PROJECTILETYPE_ANY = 0;
	public static final int PROJECTILETYPE_ARROW = 1;
	public static final int PROJECTILETYPE_BULLET = 2;

	public static final int NPCID_BRAGUS = 3;
	public static final int NPCID_VICTOR = 4;
	public static final int NPCID_LEONIDAS = 5;
	public static final int NPCID_LUSHMAEL = 7;
	public static final int NPCID_LEWIN = 8;

	public static final int LEWINFEELING_EMPTYNOTHINGTOFEELLIFEISNOTSPECIAL = 0;
	public static final int LEWINFEELING_SADNESS = 1;
	public static final int LEWINFEELING_ANGRY = 2;
	public static final int LEWINFEELING_HAPPINESS = 3;
	
	public static final int PROPID_DIALOG = 1;
	public static final int PROPID_TELEPORT = 2;

	public static final int PROPDIALOG_DIALOGID = 0;
	
	public static final int DIALOG_ITSLOCKED = 1000;
	
	public static final int PROPTELEPORT_GLOBALMAPX = 0;
	public static final int PROPTELEPORT_GLOBALMAPY = 1;
	public static final int PROPTELEPORT_LOCALMAPX = 2;
	public static final int PROPTELEPORT_LOCALMAPY = 3;
	public static final int PROPTELEPORT_SPECIALGEN = 4;
	
	public static final int MAPFLAG_RANDOM = 1337;

	public static final Color[] QUALITYCOLOR = new Color[6];
	public static final String[] QUALITYCOLORNAME = new String[6];
	public static final Color[] ELEMENTCOLOR = new Color[12];
	public static final Color[] JEWELCOLOR = new Color[8];
	public static final Color[] ELEMENTBGCOLOR = new Color[12];
	public static final String[] ELEMENTCOLORNAME = new String[12];
	public static final Color[] EXPLOSION_DEFAULT_SCHEME = new Color[4];
	public static final Color EXPLOSION_DEFAULT_SMOKE = new Color(0.35f, 0.35f, 0.35f, 1f);
	
	public static final int MOVEMENTAI_CHARGEANDRUN = 1;
	public static final int MOVEMENTAI_FLYAWAYANDSHOOT = 2;
	public static final int MOVEMENTAI_FLYINGKNIGHT = 3;
	public static final int MOVEMENTAI_CHARGEDASH = 4;
	public static final int MOVEMENTAI_FLYINGDUMMY = 5;
	public static final int MOVEMENTAI_WALKINGDUMMY = 6;
	public static final int MOVEMENTAI_SHOOTINGSENTRY = 7;
	public static final int MOVEMENTAI_MARKSMAN2K18LUL = 8;
	
	public static final int AI1_STOPTIME = 3;
	public static final int AI1_MAXSPEED = 4;
	public static final int AI1_STOPDISTANCE = 5;
	public static final int AI1_SCAREDISTANCE = 6;
	
	public static final int AI2_SPEEDDIV = 3;
	public static final int AI2_DISTANCE = 4;
	public static final int AI2_MAXSPEED = 5;
	public static final int AI2_PROJECTILEID = 6;
	public static final int AI2_PROJECTILECOOLDOWN = 7;
	public static final int AI2_PROJECTILESPEED = 8;
	public static final int AI2_PROJECTILEDAMAGE = 9;
	public static final int AI2_MAXDISTANCE = 10;

	public static final int AI3_DASHSPEED = 3;
	public static final int AI3_KEEPHEIGHT = 4;
	public static final int AI3_FLYSPEED = 5;
	
	public static final int AI4_CHARGETIME = 3;
	public static final int AI4_DASHSPEED = 4;
	public static final int AI4_DASHDISACCEL = 5;
	public static final int AI4_WALKSPEED = 6;
	public static final int AI4_DISTTOCHARGE = 7;
	public static final int AI4_CHARGEFINALFRAME = 8;
	public static final int AI4_WALKFINALFRAME = 9;
	public static final int AI4_COOLDOWN = 10;

	public static final int AI5_FLYMAXSPEED = 3;
	public static final int AI5_ACCELERATION = 4;
	public static final int AI5_MAXFRAMECOUNTER = 5;
	public static final int AI5_SHOULDROTATE = 6;
	
	public static final int AI6_MAXSPEED = 3;
	public static final int AI6_ACCELERATION = 4;
	public static final int AI6_JUMPFORCE = 5;
	public static final int AI6_MAXFRAMECOUNTER = 6;
	
	public static final int AI7_PROJECTILEID = 3;
	public static final int AI7_PROJECTILECOOLDOWN = 4;
	public static final int AI7_PROJECTILESPEED = 5;
	public static final int AI7_PROJECTILEDAMAGE = 6;
	public static final int AI7_SHOOTRANGE = 7;
	
	public static final int AI8_PROJECTILEID = 3;
	public static final int AI8_PROJECTILECOOLDOWN = 4;
	public static final int AI8_PROJECTILESPEED = 5;
	public static final int AI8_PROJECTILEDAMAGE = 6;
	public static final int AI8_MAXSPEED = 7;
	public static final int AI8_MONSTEREXTRAID = 8;
	public static final int AI8_MONSTEREXTRAFRAMES = 9;
	public static final int AI8_KEEPDISTANCE = 10;
	public static final int AI8_CAITMAXSPEED = 11;

	public static final int LANGUAGE_ENGLISH = 0;
	public static final int LANGUAGE_PORTUGUESE = 1;

	public static final int TAGSYMBOL_NONE = 0;
	public static final int TAGSYMBOL_EXCLAMATION = 1;
	public static final int TAGSYMBOL_INTERROGATIONGREEN = 2;
	public static final int TAGSYMBOL_INTERROGATIONGRAY = 3;
	
	public static final float BACKWEAPON_TIME = 10f;
	
	public static final int FBO_SIZE = 1024;

	public static final int RANDOMWALK_NONE = 0;
	public static final int RANDOMWALK_NORMAL = 1;
	public static final int RANDOMWALK_PAUSED = 2;
	
	public static final int QUESTSTATE_NEVER = 0;
	public static final int QUESTSTATE_PROGRESS = 1;
	public static final int QUESTSTATE_READYTOCOMPLETE = 2;
	public static final int QUESTSTATE_COMPLETED = 3;

	public static final int SCREEN_START = 0;
	public static final int SCREEN_CHARSCREEN = 1;
	public static final int SCREEN_GAME = 2;

	public static final int DEVSCREEN_WIDTH = 1366;
	public static final int DEVSCREEN_HEIGHT = 768;
	
	public static ArrayList<ShaderProgram> safeShaders;
	
	public static Color skinColor = new Color(255f/255f, 194f/255f, 81f/255f, 1f);

	static{
		
		QUALITYCOLOR[QUALITY_COMMON] = Color.WHITE;
		QUALITYCOLOR[QUALITY_UNCOMMON] = new Color(0f, 1f, 1f, 1f);
		QUALITYCOLOR[QUALITY_RARE] = new Color(0f, 1f, 0f, 1f);
		QUALITYCOLOR[QUALITY_EPIC] = new Color(1f, 0.8f, 0f, 1f);
		QUALITYCOLOR[QUALITY_LEGENDARY] = new Color(1f, 0f, 0f, 1f);
		QUALITYCOLOR[QUALITY_IMAGINARY] = new Color(1f, 0f, 1f, 1f);
	}
	{

		JEWELCOLOR[0] = new Color(1f, 0.1f, 1f, 1f);
		JEWELCOLOR[1] = new Color(0.05f, 0.1f, 0.95f, 1f);
		JEWELCOLOR[2] = new Color(0.05f, 0.75f, 0.05f, 1f);
		JEWELCOLOR[3] = new Color(0.95f, 0.18f, 0.18f, 1f);
		JEWELCOLOR[4] = new Color(0.05f, 0.95f, 0.95f, 1f);
		JEWELCOLOR[5] = new Color(1f, 0.05f, 0.05f, 1f);
		JEWELCOLOR[6] = new Color(0.3f, 0.3f, 0.3f, 1f);
		JEWELCOLOR[7] = new Color(1f, 0.8f, 0.05f, 1f);
	}
	{
		EXPLOSION_DEFAULT_SCHEME[0] = Color.WHITE;
		EXPLOSION_DEFAULT_SCHEME[1] = new Color(0.98f, 0.93f, 0.22f, 1f);
		EXPLOSION_DEFAULT_SCHEME[2] = new Color(0.86f, 0.46f, 0.17f, 1f);
		EXPLOSION_DEFAULT_SCHEME[3] = new Color(0.8f, 0.4f, 0.15f, 1f);
	}
	{
		
		QUALITYCOLORNAME[QUALITY_COMMON] = "COMMON";
		QUALITYCOLORNAME[QUALITY_UNCOMMON] = "UNCOMMON";
		QUALITYCOLORNAME[QUALITY_RARE] = "RARE";
		QUALITYCOLORNAME[QUALITY_EPIC] = "EPIC";
		QUALITYCOLORNAME[QUALITY_LEGENDARY] = "LEGENDARY";
		QUALITYCOLORNAME[QUALITY_IMAGINARY] = "IMAGINARY";
	}
	{
		ELEMENTCOLOR[DAMAGETYPE_PHYSICAL] = new Color(0.75f, 0.75f, 0.75f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_POISON] = new Color(0f, 0.5f, 0f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_FIRE] = new Color(1f, 0.56f, 0.17f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_DARKNESS] = new Color(0.5f, 0f, 0.5f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_ICE] = new Color(0f, 0.75f, 1f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_NATURE] = new Color(0f, 0.5f, 0.25f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_HOLY] = new Color(1f, 1f, 0.25f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_AIR] = new Color(1f, 1f, 0.6f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_ENERGY] = new Color(0f, 0.69f, 1f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_BLOOD] = new Color(1f, 0f, 0f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_GOOD] = new Color(0f, 1f, 0.25f, 1f);
		ELEMENTCOLOR[DAMAGETYPE_DEATH] = new Color(0.1f, 0.1f, 0.1f, 1f);
	}
	{
		ELEMENTBGCOLOR[DAMAGETYPE_PHYSICAL] = new Color(0.5f, 0.5f, 0.5f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_POISON] = new Color(0f, 0.5f, 0f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_FIRE] = new Color(1f, 0.56f, 0.17f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_DARKNESS] = new Color(0.5f, 0f, 0.5f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_ICE] = new Color(0f, 0.75f, 1f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_NATURE] = new Color(0f, 0.5f, 0.25f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_HOLY] = new Color(1f, 1f, 0.25f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_AIR] = new Color(1f, 1f, 0.6f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_ENERGY] = new Color(0f, 0.69f, 1f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_BLOOD] = new Color(1f, 0f, 0f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_GOOD] = new Color(0f, 1f, 0.25f, 1f);
		ELEMENTBGCOLOR[DAMAGETYPE_DEATH] = new Color(0.1f, 0.1f, 0.1f, 1f);
	}
	{
		ELEMENTCOLORNAME[DAMAGETYPE_PHYSICAL] = "PHYSICAL";
		ELEMENTCOLORNAME[DAMAGETYPE_POISON] = "POISON";
		ELEMENTCOLORNAME[DAMAGETYPE_FIRE] = "FIRE";
		ELEMENTCOLORNAME[DAMAGETYPE_DARKNESS] = "DARKNESS";
		ELEMENTCOLORNAME[DAMAGETYPE_ICE] = "ICE";
		ELEMENTCOLORNAME[DAMAGETYPE_NATURE] = "NATURE";
		ELEMENTCOLORNAME[DAMAGETYPE_HOLY] = "HOLY";
		ELEMENTCOLORNAME[DAMAGETYPE_AIR] = "AIR";
		ELEMENTCOLORNAME[DAMAGETYPE_ENERGY] = "ENERGY";
		ELEMENTCOLORNAME[DAMAGETYPE_BLOOD] = "BLOOD";
		ELEMENTCOLORNAME[DAMAGETYPE_GOOD] = "GOOD";
		ELEMENTCOLORNAME[DAMAGETYPE_DEATH] = "DEATH";
	}
	
	public Polygon rectToPoly(Rectangle r)
	{
		Polygon poly = new Polygon(new float[]{0, 0, 
				r.width, 0, 
				r.width, r.height, 
				0, r.height});
		poly.setPosition(r.getX(), r.getY());
		return poly;
	}

	public static ArrayList<Monster> getMonsterList(boolean addNaturals)
	{
		ArrayList<Monster> list = new ArrayList<Monster>();
		list.addAll(Main.monster);
		if(addNaturals)
			list.addAll(Main.natural);
		return list;
	}

	public static ArrayList<Monster> getNaturalList()
	{
		return Main.natural;
	}
	
	public static ArrayList<Monster> getMonsterExList()
	{
		return Main.monsterex;
	}
	
	public static ArrayList<Monster> getNaturalExList()
	{
		return Main.naturalex;
	}
	
	public static ArrayList<Treasure> getTreasureList()
	{
		return Main.treasure;
	}
	
	public static ArrayList<Item> getItemsList()
	{
		return Main.items;
	}
	
	public static ArrayList<NPC> getNPCList()
	{
		return Main.npc;
	}
	
	public static ArrayList<Projectile> getProjectileList()
	{
		return Main.projectile;
	}
	
	public static ArrayList<Projectile> getProjectileExList()
	{
		return Main.projectileex;
	}
	
	public static ArrayList<Prop> getPropList()
	{
		return Main.prop;
	}
	
	public static ArrayList<Respawn> getRespawnsList()
	{
		return Main.respawns;
	}
	
	public static Player[] getPlayerList()
	{
		return Main.player;
	}
	
	public static GameMap getPlayerMap(int id)
	{
		return Main.worldMap;
	}

	public static GameMap tryGetMapForEntity(Entity m)
	{
		return Main.worldMap;
	}
	
	public static int gameTick()
	{
		return Main.gameTick;
	}
	
	public static int getPossibleMapId(int mapX, int mapY)
	{
		int tier = Math.abs(mapX/2);
		ArrayList<Integer> mapslist = new ArrayList<Integer>();
		mapslist.add(2);
		if(tier >= 6)
		{
			mapslist.add(102);
			mapslist.add(101);
		}
		if(tier >= 8)
		{
			mapslist.add(110);
		}
		if(tier >= 12)
		{
			mapslist.add(120);
		}
		int selectedMap = mapslist.get(MathUtils.random(mapslist.size()));
		return selectedMap;
	}
	
	public static ArrayList<Monster> getMonstersInArea(int mapx, int mapy, float x, float y, float width, float height)
	{
		ArrayList<Monster> ret = new ArrayList<Monster>();
		ArrayList<Monster> tot = Constant.getMonsterList(false);
		for(Monster m : tot)
		{
			if(m.myMapX == mapx && m.myMapY == mapy)
			{
				Rectangle rect = new Rectangle(x, y, width, height);
				if(rect.overlaps(m.hitBox()))
				{
					ret.add(m);
				}
			}
		}
		return ret;
	}

	public static ArrayList<Monster> getMonstersInRange(int mapx, int mapy, Vector2 center, float radius)
	{
		ArrayList<Monster> ret = new ArrayList<Monster>();
		ArrayList<Monster> tot = Constant.getMonsterList(false);
		for(Monster m : tot)
		{
			Circle c = new Circle(center.x, center.y, radius);
			Rectangle r = m.hitBox();
			if(m.active && !m.natural && m.myMapX == mapx && m.myMapY == mapy && Intersector.overlaps(c, r))
			{
				ret.add(m);
			}
		}
		return ret;
	}
	
	public static ArrayList<Monster> getMonstersInRange(Entity e, float radius)
	{	
		return Constant.getMonstersInRange(e.myMapX, e.myMapY, e.Center(), radius);
	}
	
	public static ArrayList<Player> getPlayersInRange(int mapx, int mapy, Vector2 center, float radius)
	{
		ArrayList<Player> ret = new ArrayList<Player>();
		ArrayList<Player> tot = new ArrayList<Player>();
		tot.addAll(Arrays.asList(Constant.getPlayerList()));
		for(Player m : tot)
		{
			Circle c = new Circle(center.x, center.y, radius);
			Rectangle r = m.hitBox();
			if(m.active && m.myMapX == mapx && m.myMapY == mapy && Intersector.overlaps(c, r))
			{
				ret.add(m);
			}
		}
		return ret;
	}
	
	public static ArrayList<Player> getPlayersInRange(Entity e, float radius)
	{	
		return Constant.getPlayersInRange(e.myMapX, e.myMapY, e.Center(), radius);
	}
	
	public static ArrayList<Monster> getMonstersInCone(int mapx, int mapy, Vector2 center, float radius, float angle2, float width)
	{
		ArrayList<Monster> ret = new ArrayList<Monster>();
		float angle = (float) Math.abs((angle2 * Math.PI/180f) / (Math.PI / 180f));
		for(Monster m : Constant.getMonstersInRange(mapx, mapy, center, radius))
		{
			float mAngle = Math.abs(Main.angleBetween(center, m.Center()));
			if(Math.abs(angle - mAngle) < width/2f)
			{
				ret.add(m);
			}
		}
		return ret;
	}

	public static int getWorldTime()
	{
		if(!AHS.isUp)
			return (int)Main.player[Main.me].saveInfo.myWorldTime;
		else
			return Constant.gameTick();
	}
	
	public static int getCurrentDay()
	{
		return (int)Math.floor(getWorldTime()/86400);
	}
}