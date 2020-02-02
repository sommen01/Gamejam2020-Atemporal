package com.roguelike.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Story;
import com.roguelike.world.Background;
import com.roguelike.world.Tile;

public class Content
{
	public static int TILECOUNT = Tile.tileMap.size();
	public static int BGCOUNT = Background.tileMap.size();
	public static int MONSTERCOUNT = 0;
	public static int EXTRACOUNT = 0;
	public static int ITEMCOUNT = 0;
	public static int PARTICLECOUNT = 0;
	public static int PROJECTILECOUNT = 0;
	public static int NPCCOUNT = 0;
	public static int SKILLCOUNT = 0;
	public static int BUFFCOUNT = 0;
	public static int BBGCOUNT = 1;
	public static int PSACOUNT = 0;

	public static Texture playerStand;
	public static Texture playerHandStand;
	public static Texture playerBHandStand;
	public static Texture playerHeadStand;
	public static Texture playerRoll;
	public static Texture playerHeadRoll;
	public static Texture playerHandRoll;
	public static Texture playerBHandRoll;
	public static Texture playerWalking;
	public static Texture playerHeadWalking;
	public static Texture playerHandWalking;
	public static Texture playerBHandWalking;
	public static Texture playerInventory;
	public static Texture playerBagInventory;
	public static Texture playerHeadInventory;
	public static Texture playerHandInventory;
	public static Texture playerBHandInventory;
	public static Texture playerDance;
	public static Texture playerDash;
	public static Texture playerHeadDash;

	public static Texture cursor;
	public static Texture hudStats;
	public static Texture healthBar;
	public static Texture staminaBar;
	public static Texture xpBar;
	public static Texture manaBar;
	public static Texture channelBar;
	public static Texture slot;
	public static Texture seb;
	public static Texture cover;
	public static Texture pressx;
	public static Texture moon;
	public static Texture sun;
	public static Texture skystar;
	public static Texture exclamation;
	public static Texture interrogation;
	public static Texture interrogation2;
	public static Texture pressz;
	public static Texture black;
	public static Texture white;
	public static Texture light;
	public static Texture light2;
	public static Texture light3;
	public static Texture p17l;
	public static Texture inventoryslot;
	public static Texture blank;

	public static Texture[] tiles = new Texture[TILECOUNT];
	public static Texture[] bg = new Texture[BGCOUNT];
	public static Texture[] monsters = new Texture[1000];
	public static Texture[] extras = new Texture[1000];
	public static Texture[] particles = new Texture[1000];
	public static Texture[] projectiles = new Texture[1000];
	public static Texture[] buffs = new Texture[1000];
	public static Texture[] skills = new Texture[1000];
	public static Texture[] items = new Texture[1000];
	public static Texture[] hitems = new Texture[1000];
	public static Texture[] aitems = new Texture[1000];
	public static Texture[] witems = new Texture[1000];
	public static Texture[] heads = new Texture[1000];
	public static Texture[] headw = new Texture[1000];
	public static Texture[] headr = new Texture[1000];
	public static Texture[] dheads = new Texture[1000];
	public static Texture[] dheadw = new Texture[1000];
	public static Texture[] dheadr = new Texture[1000];
	public static Texture[] npc = new Texture[1000];
	public static Texture[] psa = new Texture[1000];

	public static Texture[] BBG = new Texture[BBGCOUNT];

	// Secondaries
	public static Texture[] darkrai = new Texture[3];
	public static Texture gslegs;
	public static Texture woodentreasure;
	public static Texture playerportrait;
	public static Texture playerHead;
	public static Texture lewinBall;

	public static Texture sfH;
	public static Texture sfA;
	public static Texture ptH;
	public static Texture sbA;
	public static Texture fbA;
	public static Texture seH;
	public static Texture seA;
	public static Texture atkr;
	public static Texture defr;

	public static Texture nebula;
	
	public static Texture scheye;
	
	public static Texture mask;

	public static Texture[] arcanecircle = new Texture[4];
	public static Texture[] arcanecirclew = new Texture[4];
	public static Texture[] arcanecirclesymbols = new Texture[Constant.ELEMENTCOLOR.length];
	public static Texture[] arcanecirclesymbolsw = new Texture[Constant.ELEMENTCOLOR.length];
	public static Texture ragingDash;
	public static Texture handPointing;
	public static Texture handLiftedUp;
	public static Texture dartThrow;
	public static Texture dartThrowMark;
	public static Texture bonice;
	public static Texture star0;
	public static Texture star1;
	public static Texture stunmark;
	public static Texture blinkEye;
	public static Texture barrier;
	public static Texture dialogBox;
	public static Texture dialogBoxF;
	public static Texture charBox;
	public static Texture charBoxF;
	public static Texture inventoryBox;
	public static Texture dialogBoxF2;
	public static Texture genericBox;
	public static Texture genericBox2;
	public static Texture genericBox3;
	public static Texture[] ghostcloud = new Texture[5];
	public static Texture loading;
	public static Texture arkaHero;

	public static ShaderProgram blackS;
	public static ShaderProgram whiteS;
	public static ShaderProgram redS;
	public static ShaderProgram pcS;
	public static ShaderProgram empowerBladeS;
	public static ShaderProgram stellarAttackS;
	public static ShaderProgram colorS;
	public static ShaderProgram shinyS;
	public static ShaderProgram maskIncS;
	public static ShaderProgram maskExcS;
	public static ShaderProgram maskAllS;
	
	public static Texture goblinWithStatue;
	public static Texture[] plains = new Texture[3];
	public static Texture[] forest = new Texture[3];
	public static Texture[] caves = new Texture[3];
	public static Texture[] invcaves = new Texture[3];
	
	public static Texture loadingParticle;

	private static boolean loadedThisFrame = false;

	public static Texture[] menuBg = new Texture[3];
	public static Texture[] birdBg = new Texture[5];
	public static Texture[] brokenGround = new Texture[1];
	
	public static HashMap<String, Texture> map = new HashMap<String, Texture>();
	
	public static void loadBasicContent()
	{
		loading = new Texture("data/others/loading.png");
		loadingParticle = new Texture("data/Particle_10.png");
	}
	
	public static boolean loadShaders()
	{
		loadedThisFrame = false;
		Main.loadingProgress = Main.lv("Hiring expressionists", "Contratando expressionistas");
		blackS = Content.loadBaseShader("black"); if(loadedThisFrame) return false;
		whiteS = Content.loadBaseShader("white"); if(loadedThisFrame) return false;
		redS = Content.loadBaseShader("red"); if(loadedThisFrame) return false;
		pcS = Content.loadBaseShader("playercolor"); if(loadedThisFrame) return false;
		empowerBladeS = Content.loadBaseShader("empowerblade"); if(loadedThisFrame) return false;
		stellarAttackS = Content.loadBaseShader("stellarattack"); if(loadedThisFrame) return false;
		colorS = Content.loadBaseShader("color"); if(loadedThisFrame) return false;
		shinyS = Content.loadBaseShader("shiny"); if(loadedThisFrame) return false;

		maskIncS = Content.loadBaseShader("maskinc");
		maskIncS.begin();
		maskIncS.setUniformi("u_mask", 1);
		maskIncS.end();
		maskExcS = Content.loadBaseShader("maskexc");
		maskExcS.begin();
		maskExcS.setUniformi("u_mask", 1);
		maskExcS.end();
		maskAllS = Content.loadBaseShader("maskall");
		maskAllS.begin();
		maskAllS.setUniformi("u_mask", 1);
		maskAllS.end();

		Constant.safeShaders = new ArrayList<ShaderProgram>();
		Constant.safeShaders.add(pcS);
		Constant.safeShaders.add(Main.basicShader);
		Constant.safeShaders.add(shinyS);
		Constant.safeShaders.add(maskIncS);
		Constant.safeShaders.add(maskExcS);
		Constant.safeShaders.add(maskAllS);
		if(loadedThisFrame) return false;
		return true;
	}
	
	public static boolean loadContent()
	{
		loadedThisFrame = false;
		//system.out.println("-------- Loading content..."); if(loadedThisFrame) return false;
		//System.out.println("Loading content");

		Main.loadingProgress = Main.lv("Loading generic textures", "Carregando texturas genéricas");
		playerStand = lt("data/player/stand.png"); if(loadedThisFrame) return false;
		playerHeadStand = lt("data/player/stand_he.png"); if(loadedThisFrame) return false;
		playerRoll = lt("data/player/roll.png"); if(loadedThisFrame) return false;
		playerHeadRoll = lt("data/player/roll_he.png"); if(loadedThisFrame) return false;
		playerDash = lt("data/player/dash.png"); if(loadedThisFrame) return false;
		playerHeadDash = lt("data/player/dash_he.png"); if(loadedThisFrame) return false;
		playerWalking = lt("data/player/walking.png"); if(loadedThisFrame) return false;
		playerHeadWalking = lt("data/player/walking_he.png"); if(loadedThisFrame) return false;
		playerInventory = lt("data/player/inventory.png"); if(loadedThisFrame) return false;
		playerBagInventory = lt("data/player/inventory_bag.png"); if(loadedThisFrame) return false;
		playerHeadInventory = lt("data/player/inventory_he.png"); if(loadedThisFrame) return false;
		playerHandStand = lt("data/player/stand_h.png"); if(loadedThisFrame) return false;
		playerBHandStand = lt("data/player/stand_bh.png"); if(loadedThisFrame) return false;
		playerHandRoll = lt("data/player/roll_h.png"); if(loadedThisFrame) return false;
		playerBHandRoll = lt("data/player/roll_bh.png"); if(loadedThisFrame) return false;
		playerHandWalking = lt("data/player/walking_h.png"); if(loadedThisFrame) return false;
		playerBHandWalking = lt("data/player/walking_bh.png"); if(loadedThisFrame) return false;
		playerHandInventory = lt("data/player/inventory_h.png"); if(loadedThisFrame) return false;
		dialogBox = lt("data/others/layout/dialogback.png"); if(loadedThisFrame) return false;
		inventoryBox = lt("data/others/layout/inventoryback.png"); if(loadedThisFrame) return false;
		dialogBoxF = lt("data/others/layout/dialogfront.png"); if(loadedThisFrame) return false;
		charBox = lt("data/others/layout/characterback.png"); if(loadedThisFrame) return false;
		genericBox = lt("data/others/layout/genericBox.png"); if(loadedThisFrame) return false;
		genericBox2 = lt("data/others/layout/genericBox2.png"); if(loadedThisFrame) return false;
		genericBox3 = lt("data/others/layout/genericBox3.png"); if(loadedThisFrame) return false;
		charBoxF = lt("data/others/layout/characterfront.png"); if(loadedThisFrame) return false;
		dialogBoxF2 = lt("data/others/layout/dialogfront2.png"); if(loadedThisFrame) return false;
		playerBHandInventory = lt("data/player/inventory_bh.png"); if(loadedThisFrame) return false;
		playerHead = lt("data/player/head.png"); if(loadedThisFrame) return false;
		mask = lt("data/mask.png"); if(loadedThisFrame) return false;
		playerDance = lt("data/player/dance.png"); if(loadedThisFrame) return false;
		cursor = lt("data/cursor2.png"); if(loadedThisFrame) return false;
		hudStats = lt("data/hudStats.png"); if(loadedThisFrame) return false;
		nebula = lt("data/others/nebula.png"); if(loadedThisFrame) return false;
		healthBar = lt("data/healthBar.png"); if(loadedThisFrame) return false;
		xpBar = lt("data/xpBar.png"); if(loadedThisFrame) return false;
		staminaBar = lt("data/staminaBar.png"); if(loadedThisFrame) return false;
		channelBar = lt("data/channelBar.png"); if(loadedThisFrame) return false;
		manaBar = lt("data/manaBar.png"); if(loadedThisFrame) return false;
		slot = lt("data/slot.png"); if(loadedThisFrame) return false;
		cover = lt("data/cover.png"); if(loadedThisFrame) return false;
		pressx = lt("data/pressx.png"); if(loadedThisFrame) return false;
		exclamation = lt("data/exclamation.png"); if(loadedThisFrame) return false;
		interrogation = lt("data/interrogation.png"); if(loadedThisFrame) return false;
		interrogation2 = lt("data/interrogation2.png"); if(loadedThisFrame) return false;
		pressz = lt("data/pressz.png"); if(loadedThisFrame) return false;
		black = lt("data/black.png"); if(loadedThisFrame) return false;
		white = lt("data/white.png"); if(loadedThisFrame) return false;
		light = lt("data/light.png"); if(loadedThisFrame) return false;
		light2 = lt("data/light2.png"); if(loadedThisFrame) return false;
		light3 = lt("data/light3.png"); if(loadedThisFrame) return false;
		p17l = lt("data/Particle_17_light.png"); if(loadedThisFrame) return false;
		playerportrait = lt("data/player/portrait.png"); if(loadedThisFrame) return false;
		inventoryslot = lt("data/others/inventoryslot.png"); if(loadedThisFrame) return false;
		woodentreasure = lt("data/others/woodentreasure.png"); if(loadedThisFrame) return false;
		ragingDash = lt("data/others/ragingdash.png"); if(loadedThisFrame) return false;
		handPointing = lt("data/others/ragingdashhand.png"); if(loadedThisFrame) return false;
		handLiftedUp = lt("data/others/windslashhand.png"); if(loadedThisFrame) return false;
		dartThrow = lt("data/others/dartthrow.png"); if(loadedThisFrame) return false;
		barrier = lt("data/others/barrier.png"); if(loadedThisFrame) return false;
		dartThrowMark = lt("data/others/dartthrowmark.png"); if(loadedThisFrame) return false;
		lewinBall = lt("data/others/lewinBall.png"); if(loadedThisFrame) return false;
		goblinWithStatue = lt("data/others/goblinwithstatue.png"); if(loadedThisFrame) return false;
		star0 = lt("data/others/star0.png"); if(loadedThisFrame) return false;
		star1 = lt("data/others/star1.png"); if(loadedThisFrame) return false;
		stunmark = lt("data/others/stunmark.png"); if(loadedThisFrame) return false;
		seb = lt("data/others/skillElementBase.png"); if(loadedThisFrame) return false;
		bonice = lt("data/others/BONICEpeww.png"); if(loadedThisFrame) return false;
		blinkEye = lt("data/others/blinkeye.png"); if(loadedThisFrame) return false;
		atkr = lt("data/others/atk-.png"); if(loadedThisFrame) return false;
		defr = lt("data/others/def-.png"); if(loadedThisFrame) return false;
		moon = lt("data/others/moon.png"); if(loadedThisFrame) return false;
		sun = lt("data/others/sun.png"); if(loadedThisFrame) return false;
		skystar = lt("data/others/star.png"); if(loadedThisFrame) return false;
		sfH = lt("data/HItem_11S.png"); if(loadedThisFrame) return false;
		ptH = lt("data/HItem_46S.png"); if(loadedThisFrame) return false;
		seH = lt("data/HItem_48S.png"); if(loadedThisFrame) return false;
		sfA = lt("data/AItem_11S.png"); if(loadedThisFrame) return false;
		sbA = lt("data/others/sba.png"); if(loadedThisFrame) return false;
		seA = lt("data/AItem_48S.png"); if(loadedThisFrame) return false;
		fbA = lt("data/others/fba.png"); if(loadedThisFrame) return false;
		for(int i = 0;i < 3;i++)
		{
			darkrai[i] = lt("data/others/darkrai_" + i + ".png"); if(loadedThisFrame) return false;
		}
		for(int i = 0;i < brokenGround.length;i++)
		{
			brokenGround[i] = lt("data/others/maps/brokenground" + i + ".png"); if(loadedThisFrame) return false;
		}
		gslegs = lt("data/others/gslegs.png"); if(loadedThisFrame) return false;
		scheye = lt("data/others/scheye.png"); if(loadedThisFrame) return false;
		for(int i = 0;i < ghostcloud.length;i++)
		{
			ghostcloud[i] = lt("data/others/maps/ghostcloud" + (i+1) + ".png"); if(loadedThisFrame) return false;
		}
		for(int i = 0;i < 4;i++)
		{
			arcanecircle[i] = lt("data/others/arcanecircle/" + (i+1) + ".png"); if(loadedThisFrame) return false;
			arcanecirclew[i] = lt("data/others/arcanecircle/a" + (i+1) + ".png"); if(loadedThisFrame) return false;
		}
		for(int i = 0;i < arcanecirclesymbols.length;i++)
		{
			arcanecirclesymbols[i] = lt("data/others/arcanecircle/e" + i + ".png"); if(loadedThisFrame) return false;
			arcanecirclesymbolsw[i] = lt("data/others/arcanecircle/e" + i + ".png"); if(loadedThisFrame) return false;
		}
		Main.loadingProgress = Main.lv("Searching for pretty floors", "Buscando pisos bonitos");
		for(int i = 0; i < TILECOUNT;i++)
		{
			tiles[i] = lt("data/Tile_" + i + ".png"); if(loadedThisFrame) return false;
		}
		Main.loadingProgress = Main.lv("Loading antigravitational walls", "Carregando paredes antigravitacionais");
		for(int i = 0; i < BGCOUNT;i++)
		{
			bg[i] = lt("data/Background_" + i + ".png");
			if(bg[i].getWidth() > 64 || bg[i].getHeight() > 64)
			{
				Background.values()[i].highPriority = true;
			}
			if(loadedThisFrame) return false;
		}
		Main.loadingProgress = Main.lv("Sharpening swords and shining hats", "Afiando espadas e lustrando chapéus");
		boolean found = true;
		while(found)
		{
			FileHandle file = Gdx.files.local("data/Item_" + ITEMCOUNT + ".png");
			if(file.exists())
			{
				items[ITEMCOUNT] = lt("data/Item_" + ITEMCOUNT + ".png");
			}
			else
			{
				found = false;
				break;
			}
			
			file = Gdx.files.local("data/HItem_" + ITEMCOUNT + ".png");
			boolean haveh = false;
			if(file.exists())
			{
				hitems[ITEMCOUNT] = lt("data/HItem_" + ITEMCOUNT + ".png");
				haveh = true;
			}

			file = Gdx.files.local("data/AItem_" + ITEMCOUNT + ".png");
			if(file.exists())
			{
				aitems[ITEMCOUNT] = lt("data/AItem_" + ITEMCOUNT + ".png");
			}

			file = Gdx.files.local("data/WItem_" + ITEMCOUNT + ".png");
			if(file.exists())
			{
				witems[ITEMCOUNT] = lt("data/WItem_" + ITEMCOUNT + ".png");
			}
			else if(haveh)
			{
				//system.out.println("-> Warning: Item " + ITEMCOUNT + " have a (H)and sprite but not a (W)orld sprite."); if(loadedThisFrame) return false;
			}

			file = Gdx.files.local("data/armor/HatS_" + ITEMCOUNT + ".png");
			boolean havehats = false;
			if(file.exists())
			{
				heads[ITEMCOUNT] = lt("data/armor/HatS_" + ITEMCOUNT + ".png");
				havehats = true;
			}

			file = Gdx.files.local("data/armor/HatW_" + ITEMCOUNT + ".png");
			boolean havehatw = false;
			if(file.exists())
			{
				headw[ITEMCOUNT] = lt("data/armor/HatW_" + ITEMCOUNT + ".png");
				havehatw = true;
			}
			
			file = Gdx.files.local("data/armor/HatR_" + ITEMCOUNT + ".png");
			boolean havehatr = false;
			if(file.exists())
			{
				headr[ITEMCOUNT] = lt("data/armor/HatR_" + ITEMCOUNT + ".png");
				havehatr = true;
			}
			//else if(havehats)
				//system.out.println("-> Warning: Item " + ITEMCOUNT + " have a (HatS)tand sprite but not a (HatR)oll sprite."); if(loadedThisFrame) return false;
			
			file = Gdx.files.local("data/armor/DHatS_" + ITEMCOUNT + ".png");
			boolean dyable = false;
			if(file.exists())
			{
				dheads[ITEMCOUNT] = lt("data/armor/DHatS_" + ITEMCOUNT + ".png");
				dyable = true;
			}

			file = Gdx.files.local("data/armor/DHatW_" + ITEMCOUNT + ".png");
			if(file.exists())
			{
				dheadw[ITEMCOUNT] = lt("data/armor/DHatW_" + ITEMCOUNT + ".png");
			}
			//else if(dyable && havehatw)
				//system.out.println("-> Warning: Item " + ITEMCOUNT + " is lefting the (D)yed(HatW)alking sprite."); if(loadedThisFrame) return false;
			
			file = Gdx.files.local("data/armor/DHatR_" + ITEMCOUNT + ".png");
			if(file.exists())
			{
				dheadr[ITEMCOUNT] = lt("data/armor/DHatR_" + ITEMCOUNT + ".png");
			}
			//else if(dyable && havehatr)
				//system.out.println("-> Warning: Item " + ITEMCOUNT + " is lefting the (D)yed(HatR)oll sprite."); if(loadedThisFrame) return false;
			
			ITEMCOUNT++;
			if(loadedThisFrame) return false;
		}
		//system.out.println("Loaded " + ITEMCOUNT + " items."); if(loadedThisFrame) return false;
		Main.loadingProgress = Main.lv("Loading beauty effects", "Carregando efeitos de beleza");
		found = true;
		while(found)
		{
			FileHandle file = Gdx.files.local("data/Particle_" + PARTICLECOUNT + ".png");
			if(file.exists())
			{
				particles[PARTICLECOUNT] = lt("data/Particle_" + PARTICLECOUNT + ".png"); if(loadedThisFrame) return false;
			}
			else
			{
				found = false;
				break;
			}
				
			
			PARTICLECOUNT++;
		}

		//system.out.println("Loaded " + PARTICLECOUNT + " particles."); if(loadedThisFrame) return false;
		Main.loadingProgress = Main.lv("Filling quivers and reloading guns", "Preenchendo aljavas e recarregando armas");
		found = true;
		while(found)
		{
			FileHandle file = Gdx.files.local("data/Projectile_" + PROJECTILECOUNT + ".png");
			if(file.exists())
			{
				projectiles[PROJECTILECOUNT] = lt("data/Projectile_" + PROJECTILECOUNT + ".png"); if(loadedThisFrame) return false;
			}
			else
			{
				found = false;
				break;
			}
			PROJECTILECOUNT++;
		}
		//system.out.println("Loaded " + PROJECTILECOUNT + " projectiles."); if(loadedThisFrame) return false;
		Main.loadingProgress = Main.lv("Waking up NPCs", "Acordando os NPCs");
		found = true;
		while(found)
		{
			FileHandle file = Gdx.files.local("data/NPC_" + NPCCOUNT + ".png");
			if(file.exists())
			{
				npc[NPCCOUNT] = lt("data/NPC_" + NPCCOUNT + ".png"); if(loadedThisFrame) return false;
			}
			else
			{
				found = false;
				break;
			}
			NPCCOUNT++;
		}
		//system.out.println("Loaded " + NPCCOUNT + " NPCs."); if(loadedThisFrame) return false;
		Main.loadingProgress = Main.lv("Calling out creatures", "Chamando as criaturas");
		found = true;
		while(found)
		{
			FileHandle file = Gdx.files.local("data/Monster_" + MONSTERCOUNT + ".png");
			if(file.exists())
			{
				monsters[MONSTERCOUNT] = lt("data/Monster_" + MONSTERCOUNT + ".png"); if(loadedThisFrame) return false;
			}
			else
			{
				found = false;
				break;
			}
			MONSTERCOUNT++;
		}
		//system.out.println("Loaded " + MONSTERCOUNT + " monsters."); if(loadedThisFrame) return false;
		Main.loadingProgress = Main.lv("Loading totally unnecessary files", "Carregando arquivos totalmente desnecessários");
		found = true;
		while(found)
		{
			FileHandle file = Gdx.files.local("data/Extra_" + EXTRACOUNT + ".png");
			if(file.exists())
			{
				extras[EXTRACOUNT] = lt("data/Extra_" + EXTRACOUNT + ".png"); if(loadedThisFrame) return false;
			}
			else
			{
				found = false;
				break;
			}
			EXTRACOUNT++;
		}
		//system.out.println("Loaded " + EXTRACOUNT + " extra sprites."); if(loadedThisFrame) return false;
		Main.loadingProgress = Main.lv("Loading deceptive advertisements", "Carregando propagandas enganosas");
		found = true;
		while(found)
		{
			FileHandle file = Gdx.files.local("data/Skill_" + SKILLCOUNT + ".png");
			if(file.exists())
			{
				skills[SKILLCOUNT] = lt("data/Skill_" + SKILLCOUNT + ".png"); if(loadedThisFrame) return false;
			}
			else
			{
				found = false;
				break;
			}
			SKILLCOUNT++;
		}
		//system.out.println("Loaded " + SKILLCOUNT + " skills."); if(loadedThisFrame) return false;
		Main.loadingProgress = Main.lv("Summoning annoying fairies", "Invocando fadas irritantes");
		found = true;
		while(found)
		{
			FileHandle file = Gdx.files.local("data/Buff_" + BUFFCOUNT + ".png");
			if(file.exists())
			{
				buffs[BUFFCOUNT] = lt("data/Buff_" + BUFFCOUNT + ".png"); if(loadedThisFrame) return false;
			}
			else
			{
				found = false;
				break;
			}
			BUFFCOUNT++;
		}
		//system.out.println("Loaded " + BUFFCOUNT + " buffs."); if(loadedThisFrame) return false;
		Main.loadingProgress = Main.lv("Preparing show offs", "Preparando show offs");
		found = true;
		while(found)
		{
			FileHandle file = Gdx.files.local("data/PSA_" + PSACOUNT + ".png");
			if(file.exists())
			{
				psa[PSACOUNT] = lt("data/PSA_" + PSACOUNT + ".png"); if(loadedThisFrame) return false;
			}
			else
			{
				found = false;
				break;
			}
			PSACOUNT++;
		}
		//system.out.println("Loaded " + PSACOUNT + " player special animations."); if(loadedThisFrame) return false;
		Main.loadingProgress = Main.lv("Starting up the matrix", "Iniciando a matrix");
		for(int i = 0;i < 3;i++)
		{
			plains[i] = lt("data/bg/Plains_"+i+".png"); if(loadedThisFrame) return false;
			forest[i] = lt("data/bg/Forest_"+i+".png"); if(loadedThisFrame) return false;
		}
		arkaHero = lt("data/others/menu/arkahero.png"); if(loadedThisFrame) return false;
		caves[0] = lt("data/bg/Caves_0.png"); if(loadedThisFrame) return false;
		caves[1] = lt("data/bg/Caves_1.png"); if(loadedThisFrame) return false;
		caves[2] = lt("data/bg/Caves_0.png"); if(loadedThisFrame) return false;
		invcaves[0] = lt("data/bg/Caves_2.png"); if(loadedThisFrame) return false;
		invcaves[1] = lt("data/bg/Caves_3.png"); if(loadedThisFrame) return false;
		invcaves[2] = lt("data/bg/Caves_2.png"); if(loadedThisFrame) return false;
		menuBg[0] = lt("data/others/menu/bg0.png"); if(loadedThisFrame) return false;
		menuBg[1] = lt("data/others/menu/bg1.png"); if(loadedThisFrame) return false;
		menuBg[2] = lt("data/others/menu/bg2.png"); if(loadedThisFrame) return false;
		birdBg[0] = lt("data/others/menu/bird0.png"); if(loadedThisFrame) return false;
		birdBg[1] = lt("data/others/menu/bird1.png"); if(loadedThisFrame) return false;
		birdBg[2] = lt("data/others/menu/bird2.png"); if(loadedThisFrame) return false;
		birdBg[3] = lt("data/others/menu/bird3.png"); if(loadedThisFrame) return false;
		birdBg[4] = lt("data/others/menu/bird4.png"); if(loadedThisFrame) return false;
		for(Story s : Story.values())
		{
			s.loadFile(); if(loadedThisFrame) return false;
		}
		blank = lt("data/blank.png");
		System.out.println("Textures loaded");
		return true;
	}

	public static void disposeContent()
	{
		playerStand.dispose();
		playerWalking.dispose();
		playerDash.dispose();
		loadingParticle.dispose();
		moon.dispose();
		sun.dispose();
		skystar.dispose();
		playerHeadDash.dispose();
		channelBar.dispose();
		playerInventory.dispose();
		playerBagInventory.dispose();
		playerHeadStand.dispose();
		playerHeadWalking.dispose();
		playerHeadInventory.dispose();
		playerHeadRoll.dispose();
		playerHandStand.dispose();
		playerHandWalking.dispose();
		playerHandInventory.dispose();
		playerBHandStand.dispose();
		playerBHandWalking.dispose();
		seb.dispose();
		playerBHandInventory.dispose();
		playerRoll.dispose();
		blank.dispose();
		playerHandRoll.dispose();
		playerBHandRoll.dispose();
		playerDance.dispose();
		cursor.dispose();
		hudStats.dispose();
		mask.dispose();
		healthBar.dispose();
		slot.dispose();
		pcS.dispose();
		empowerBladeS.dispose();
		cover.dispose();
		nebula.dispose();
		pressx.dispose();
		exclamation.dispose();
		interrogation.dispose();
		interrogation2.dispose();
		pressz.dispose();
		black.dispose();
		white.dispose();
		gslegs.dispose();
		light.dispose();
		light2.dispose();
		light3.dispose();
		p17l.dispose();
		playerHead.dispose();
		ragingDash.dispose();
		handPointing.dispose();
		seH.dispose();
		handLiftedUp.dispose();
		arkaHero.dispose();
		xpBar.dispose();
		woodentreasure.dispose();
		lewinBall.dispose();
		dartThrow.dispose();
		bonice.dispose();
		loading.dispose();
		dartThrowMark.dispose();
		goblinWithStatue.dispose();
		barrier.dispose();
		for(int i = 0;i < brokenGround.length;i++)
		{
			brokenGround[i].dispose();
		}
		star0.dispose();
		star1.dispose();
		stunmark.dispose();
		for(int i = 0;i < ghostcloud.length;i++)
		{
			ghostcloud[i].dispose();
		}
		for(int i = 0;i < TILECOUNT;i++)
		{
			tiles[i].dispose();
		}
		for(int i = 0;i < BGCOUNT;i++)
		{
			bg[i].dispose();
		}
		for(int i = 0;i < MONSTERCOUNT;i++)
		{
			monsters[i].dispose();
		}
		sfH.dispose();
		sfA.dispose();
		seA.dispose();
		sbA.dispose();
		fbA.dispose();
		atkr.dispose();
		defr.dispose();
		blinkEye.dispose();
		charBox.dispose();
		charBoxF.dispose();
		scheye.dispose();
		for(int i = 0;i < ITEMCOUNT;i++)
		{
			items[i].dispose();
			if(hitems[i] != null)
				hitems[i].dispose();

			if(aitems[i] != null)
				aitems[i].dispose();

			if(heads[i] != null)
				heads[i].dispose();

			if(headw[i] != null)
				headw[i].dispose();
			
			if(headr[i] != null)
				headr[i].dispose();
		}

		for(int i = 0;i < PARTICLECOUNT;i++)
		{
			particles[i].dispose();
		}
		for(int i = 0;i < PROJECTILECOUNT;i++)
		{
			projectiles[i].dispose();
		}
		for(int i = 0;i < SKILLCOUNT;i++)
		{
			skills[i].dispose();
		}
		for(int i = 0;i < BUFFCOUNT;i++)
		{
			buffs[i].dispose();
		}
		/*for(int i = 0;i < BBGCOUNT;i++)
		{
			BBG[i].dispose();
		}*/
		for(int i = 0;i < 3;i++)
		{
			darkrai[i].dispose();
		}
		playerportrait.dispose();
		for(int i = 0;i < 4;i++)
		{
			arcanecircle[i].dispose();
		}
		blackS.dispose();
		inventoryBox.dispose();
		redS.dispose();
		genericBox.dispose();
		genericBox2.dispose();
		genericBox3.dispose();
		dialogBox.dispose();
		dialogBoxF.dispose();
		dialogBoxF2.dispose();
		whiteS.dispose();
		for(int i = 0;i < 3;i++)
		{
			plains[i].dispose();
			forest[i].dispose();
			caves[i].dispose();
			invcaves[i].dispose();
		}
		maskIncS.dispose();
		maskExcS.dispose();
		menuBg[0].dispose();
		menuBg[1].dispose();
		menuBg[2].dispose();
		birdBg[0].dispose();
		birdBg[1].dispose();
		birdBg[2].dispose();
		birdBg[3].dispose();
		birdBg[4].dispose();
		shinyS.dispose();
		for(Story s : Story.values())
		{
			s.disposeFile();
		}
	}
	
	public static ShaderProgram loadBaseShader(String shaderName)
	{
		ShaderProgram ret;
		ShaderProgram.pedantic = false;

		FileHandle file = Gdx.files.local("shaders/" + shaderName + ".vert");
		String VERT = file.readString();

		file = Gdx.files.local("shaders/" + shaderName + ".frag");
		String FRAG = file.readString();
		
		ret = new ShaderProgram(VERT, FRAG);
		if (!ret.isCompiled()) {
			System.err.println(ret.getLog());
			//System.exit(0);
		}
				
		if (ret.getLog().length()!=0)
			System.out.println(ret.getLog());
		
		return ret;
	}

	public static void loadShader(String shaderName)
	{
		if(shaderName.length() < 1)
		{
			loadShader();
			return;
		}
		ShaderProgram.pedantic = false;

		FileHandle file = Gdx.files.local("shaders/" + shaderName + ".vert");
		String VERT = file.readString();

		file = Gdx.files.local("shaders/" + shaderName + ".frag");
		String FRAG = file.readString();
		
		Main.shader = new ShaderProgram(VERT, FRAG);
		if (!Main.shader.isCompiled()) {
			System.err.println(Main.shader.getLog());
			System.exit(0);
		}
				
		if (Main.shader.getLog().length()!=0)
			System.out.println(Main.shader.getLog());
		
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		
		Main.usingShader = shaderName;
	}
	
	public static void loadShader()
	{
		//Main.shader = SpriteBatch.createDefaultShader();
		//Main.usingShader = "";
		loadShader("sunset");
	}
	
	public static Texture lt(String str)
	{
		boolean loaded = map.containsKey(str);
		if(loaded)
		{
			return map.get(str);
		}
		Texture tex = new Texture(str);
		map.put(str, tex);
		//System.out.println("Loaded " + str + " #" + loopCounter);
		loadedThisFrame = true;
		return tex;
	}
	
	public static void setShaderTexture(ShaderProgram shader, Texture texture, int slot)
	{
		texture.bind(slot);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}
}