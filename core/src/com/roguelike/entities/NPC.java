package com.roguelike.entities;

import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Dialogs;
import com.roguelike.constants.Objective;
import com.roguelike.constants.Quest;
import com.roguelike.game.Content;
import com.roguelike.game.DJ;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;
import com.roguelike.world.GameMap;
import com.roguelike.world.Tile;

public class NPC extends Entity
{
	public int id;
	public Vector2 initialPosition = null;
	public transient int currentFrame = 0;
	public transient int currentFrameCounter = 0;
	public transient float alpha = 1f;
	public transient int frames = 1;
	public int useDialog = -1;
	public boolean temporary = false;
	public transient Entity lookingTo;
	public transient Vector2 drawOffset;
	public transient boolean displayName = true;
	public int[] infos = new int[5];
	public transient boolean collisionsEnabled = true;
	public transient int fixLookDirection = 0;
	public transient String title;
	public transient int randomlywalk;
	public transient boolean goSleep = false;
	public transient boolean sleeping = false;

	public static NPC CreateBase(Vector2 position, int id)
	{
		NPC n = new NPC();
		n.SetInfos(id);
		n.position = position.cpy();
		n.active = true;
		n.infos = new int[]{0,0,0,0,0};
		n.initialPosition = position.cpy();
		return n;
	}
	
	public void initialize()
	{
		this.SetInfos(this.id, false);
		if(this.initialPosition == null)
		{
			this.initialPosition = new Vector2(this.Center().x, this.position.y);
		}
	}
	
	public void SetInfos(int id)
	{
		SetInfos(id, true);
	}
	
	public void SetInfos(int id, boolean fullReset)
	{
		this.id = id;
		if(fullReset)
		{
			this.resetStats();
		}
		Player player = new Player();
		if(!AHS.isUp && Main.player != null && Main.me!=-1 && Main.player[Main.me]!=null)
			player = Main.player[Main.me];
		
		this.randomlywalk = Constant.RANDOMWALK_NORMAL;
		this.drawOffset = Vector2.Zero.cpy();
		if(this.id == 1)
		{
			this.name = "Test NPC";
			this.title = Main.lv("the Tester", "o Testador");
			this.width = 52;
			this.height = 72;
			this.useDialog = 1;
		}
		else if(this.id == 2)
		{
			this.name = "Hooded Stranger";
			this.title = Main.lv("a Rogue Apprentice", "o Aprendiz de Ladino");
			this.width = 52;
			this.height = 72;
		}
		else if(this.id == 3)
		{
			this.name = "Bragus";
			this.title = Main.lv("the Bardo Master", "o Mestre dos Bardos");
			this.width = 56;
			this.height = 76;
			this.frames = 5;
			this.goSleep = true;
		}
		else if(this.id == 4)
		{
			this.name = "Victor";
			this.title = Main.lv("the Stor Gave Ranger Master", "o Mestre dos Atiradores de Stor Gave");
			this.width = 60;
			this.height = 72;
		}
		else if(this.id == 5)
		{
			this.name = "Leonidas";
			this.title = Main.lv("the Stor Gave Knight Master", "o Mestre dos Guerreiros de Stor Gave");
			this.width = 104;
			this.height = 100;
			this.frames = 5;
			this.useDialog = 406;
		}
		else if(this.id == 6) // Casual
		{
			this.name = "Lushmael";
			this.title = Main.lv("the Stor Gave Rogue Master", "o Mestre dos Ladinos de Stor Gave");
			this.width = 64;
			this.height = 96;
			this.frames = 5;
		}
		else if(this.id == 7) // Original
		{
			this.name = "Lushmael";
			this.title = Main.lv("the Stor Gave Rogue Master", "o Mestre dos Ladinos de Stor Gave");
			this.width = 60;
			this.height = 84;
			this.drawOffset = new Vector2(0, -4);
		}
		else if(this.id == 8)
		{
			this.name = "Lewin";
			this.title = Main.lv("the Stor Gave Mage Master", "o Mestre dos Magos de Stor Gave");
			this.width = 52;
			this.height = 76;
			this.frames = 5;
			this.useDialog = 398;
		}
		else if(this.id == 9)
		{
			this.name = Main.lv("Farmer", "Fazendeiro");
			this.width = 56;
			this.height = 84;
		}
		else if(this.id == 10)
		{
			this.name = Main.lv("Volcano Cultist", "Cultista Vulcânico");
			this.width = 56;
			this.height = 84;
			this.frames = 9;
		}
		else if(this.id == 11)
		{
			this.name = Main.lv("Volcano Cult Master", "Mestre do Culto do Vulcão");
			this.title = Main.lv("the Intuitive Name", "o Nome Intuitivo");
			this.width = 71;
			this.height = 126;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
		}
		else if(this.id == 12)
		{
			this.name = "Goblin";
			this.width = 30;
			this.height = 38;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
		}
		else if(this.id == 13)
		{
			this.name = Main.lv("Volcano Cult Statue", "Estátua do Culto do Vulcão");
			this.width = 96;
			this.height = 128;
			this.displayName = false;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
		}
		else if(this.id == 14)
		{
			this.name = "Lildwa";
			this.width = 62;
			this.height = 72;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
		}
		else if(this.id == 15)
		{
			this.name = "Maf";
			this.title = Main.lv("the Blacksmith", "o Ferreiro");
			this.width = 60;
			this.height = 76;
			this.useDialog = 344;
			this.frames = 5;
		}
		else if(this.id == 16)
		{
			this.name = Main.lv("Anvil", "Bigorna");
			this.width = 64;
			this.height = 40;
			this.displayName = false;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
		}
		else if(this.id == 17)
		{
			this.name = "Helena Skat";
			this.title = Main.lv("the Task Manager", "a Gerenciadora de Tarefas");
			this.width = 60;
			this.height = 76;
			this.useDialog = 224;
			this.frames = 5;
			this.goSleep = true;
		}
		else if(this.id == 18)
		{
			this.name = "Viton";
			this.width = 100;
			this.height = 92;
		}
		else if(this.id == 19)
		{
			this.name = "Warp";
			this.width = 180;
			this.height = 160;
			this.useDialog = 230;
			this.fixLookDirection = 1;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
		}
		else if(this.id == 20)
		{
			this.name = Main.lv("Stor Gave Guard", "Guarda de Stor Gave");
			this.title = Main.lv("the Defender", "o Defensor");
			this.width = 80;
			this.height = 80;
			this.useDialog = 246;
			this.frames = 11;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
		}
		else if(this.id == 21)
		{
			this.name = Main.lv("Old Wade", "Velho Wade");
			this.title = Main.lv("the Speculator", "o Especulador");
			this.width = 60;
			this.height = 76;
			this.frames = 5;
			this.useDialog = 240;
			this.goSleep = true;
		}
		else if(this.id == 22)
		{
			this.name = Main.lv("Forge", "Forja");
			this.displayName = false;
			this.width = 160;
			this.height = 72;
			this.frames = 5;
			this.useDialog = 295;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
			this.fixLookDirection = 1;
		}
		else if(this.id == 23)
		{
			this.name = Main.lv("Shovel", "Pá");
			this.displayName = false;
			this.width = 84;
			this.height = 68;
			this.frames = 1;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
		}
		else if(this.id == 24)
		{
			if(!player.saveInfo.metBuckTheFarmer)
			{
				this.name = Main.lv("Farmer", "Fazendeiro");
				this.useDialog = 260;
			}
			else
			{
				this.name = "Buck";
				this.useDialog = 262;
				this.title = Main.lv("the Farmer", "o Fazendeiro");
			}
			this.width = 64;
			this.height = 80;
			this.frames = 5;
			this.goSleep = true;
		}
		else if(this.id == 25)
		{
			this.name = "Tobias";
			this.title = Main.lv("the Baker", "o Padeiro");
			this.width = 52;
			this.height = 104;
			this.frames = 5;
			this.goSleep = true;
		}
		else if(this.id == 26)
		{
			this.name = "Hank";
			this.title = Main.lv("the Farmer", "o Fazendeiro");
			this.width = 56;
			this.height = 76;
			this.frames = 5;
			this.useDialog = 278;
			this.goSleep = true;
		}
		else if(this.id == 27)
		{
			this.name = "Garry";
			this.title = Main.lv("the HUMAN Farmer", "o Fazendeiro HUMANO");
			this.width = 56;
			this.height = 80;
			this.frames = 5;
			this.useDialog = 281;
			this.goSleep = true;
		}
		else if(this.id == 28)
		{
			this.name = "Soul Tomb";
			this.width = 80;
			this.height = 84;
			this.frames = 2;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
			this.displayName = false;
		}
		else if(this.id == 29)
		{
			this.name = "Ashley";
			this.title = Main.lv("the Blacksmith", "a Ferreira");
			this.width = 60;
			this.height = 88;
			this.frames = 5;
			this.useDialog = 289;
			this.goSleep = true;
		}
		else if(this.id == 30)
		{
			this.name = Main.lv("Campfire", "Fogueira");
			this.displayName = false;
			this.width = 64;
			this.height = 72;
			this.frames = 10;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
			this.fixLookDirection = 1;
			this.useDialog = 296;
			this.mapDisplayIndex = 33;
		}
		else if(this.id == 31)
		{
			this.name = "???";
			this.width = 92;
			this.height = 84;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
			this.useDialog = 334;
			this.collisionsEnabled = false;
		}
		else if(this.id == 32)
		{
			this.name = "Dill Skat";
			this.width = 60;
			this.height = 76;
			this.frames = 5;
			this.useDialog = 337;
		}
		else if(this.id == 33)
		{
			this.name = Main.lv("Captain Sparrot", "Capitão Sparrot");
			this.title = Main.lv("the Ghost Captain", "o Capitão Fantasma");
			this.width = 72;
			this.height = 100;
			this.frames = 5;
			this.useDialog = 356;
		}
		else if(this.id == 34)
		{
			this.name = Main.lv("Campfire", "Fogueira");
			this.displayName = false;
			this.width = 64;
			this.height = 72;
			this.frames = 10;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
			this.fixLookDirection = 1;
			this.useDialog = 296;
			this.mapDisplayIndex = 33;
		}
		else if(this.id == 35)
		{
			this.name = Main.lv("Void Picture", "Quadro do Vazio");
			this.displayName = false;
			this.width = 128;
			this.height = 64;
			this.frames = 1;
			this.randomlywalk = Constant.RANDOMWALK_NONE;
			this.fixLookDirection = 1;
			this.collisionsEnabled = false;
			
			this.customShader = Content.maskIncS;
			this.customShaderUValues.put("u_offset", new String[] {"(((cx*0.011 % sw)/(sw/2)) + (ticks*0.0005)) - floor((((cx*0.011 % sw)/(sw/2)) + (ticks*0.0005)))", "(((cy*0.011 % sh)/(sh/2)) + (ticks*0.00025)) - floor((((cy*0.011 % sh)/(sh/2)) + (ticks*0.00025)))"});
			this.customShaderUValues.put("u_maskScale", new String[] {Content.nebula.getWidth()+"/128", Content.nebula.getHeight()+"/64"});
			this.customShaderUValues.put("u_frames", new String[] {"1"});
			this.customShaderUTextures.put(1, Content.nebula);
		}
		else if(this.id == 36)
		{
			this.name = "Tonny";
			this.width = 64;
			this.height = 84;
			this.frames = 5;
			this.title = Main.lv("the Retired Adventurer", "o Aventureiro Aposentado");
			this.randomlywalk = Constant.RANDOMWALK_PAUSED;
			this.useDialog = 448;
			this.goSleep = true;
		}
		else if(this.id == 37)
		{
			this.name = "Siren";
			this.width = 68;
			this.height = 76;
			this.frames = 5;
			this.title = Main.lv("the Forest Guardian", "o Guardião da Floresta");
			this.useDialog = 496;
		}
		else if(this.id == 38)
		{
			this.name = Main.lv("Lost Ghost", "Fantasma Perdido");
			this.width = 76;
			this.height = 84;
			this.frames = 5;
			this.useDialog = 515;
		}
		else if(this.id == 39)
		{
			this.name = Main.lv("Lost Ghost", "Fantasma Perdida");
			this.width = 80;
			this.height = 84;
			this.frames = 5;
			this.useDialog = 526;
		}
		
		/*
	public int id;
	public String name;
	public float width;
	public float height;
	public transient int frames = 1;
	public int useDialog = -1;
	public transient Vector2 drawOffset;
	public transient boolean displayName = true;
	public int[] infos = new int[5];
	public transient boolean collisionsEnabled = true;
	public transient int fixLookDirection = 0;
	public transient String title;
	public transient int randomlywalk;*/
	}
	
	public void resetStats()
	{
		this.name = "???";
		this.currentFrame = 0;
		this.currentFrameCounter = 0;
		this.alpha = 1f;
		this.frames = 1;
		this.useDialog = -1;
		this.displayName = true;
		this.infos = new int[]{0,0,0,0,0};
		this.randomlywalk = Constant.RANDOMWALK_NORMAL;
		this.collisionsEnabled = true;
		this.title = "";
	}

	@Override
	public void update(float delta)
	{
		Player player = Constant.getPlayerList()[Main.me];
		// AI
		this.tagSymbol = 0;
		for(Quest q : player.quests)
		{
			if(q.npcQuesterId == this.id)
			{
				this.tagSymbol = 2;
				for(Objective o : q.objectives)
				{
					if(!o.concluded)
					{
						this.tagSymbol = 3;
						break;
					}
				}
				break;
			}
		}

		if(this.id == 2) // Hooded Stranger
		{
			if(this.Center().dst(player.Center()) < 2500f)
			{
				this.velocity.x = -1600f;
				this.direction = Constant.DIRECTION_LEFT;
			}

			if(this.hitBox().overlaps(player.hitBox()))
			{
				for(int i = 0;i < Constant.ITEMSLOT_MAX;i++)
				{
					player.inventory[i] = null;
				}
			}
			if(this.Center().x < 100)
			{
				this.active = false;
			}
		}
		else if(this.id == 3) // Bragus
		{
			if(player.haveObjective(4) != null)
			{
				this.tagSymbol = 2;
			}
			if(Main.player[Main.me].Center().dst(this.Center()) < 300)
				DJ.switchMusic(DJ.LIFEISADREAM, true);
		}
		else if(this.id == 9) // Farmer
		{
			if(this.Center().dst(player.Center()) < 100 && !player.saveInfo.chapter2helpedFarmer && !Main.displayDialog)
			{
				this.showDialog(127);
				this.lookingTo = player;
			}
			
			if(!player.saveInfo.chapter2helpedFarmer)
			{
				this.tagSymbol = 1;
			}
			else
			{
				this.tagSymbol = 0;
			}
		}
		else if(this.id == 11)
		{
			if(!player.saveInfo.chapter2helpedCultist)
			{
				this.tagSymbol = 1;
			}
			else
			{
				this.tagSymbol = 0;
			}
		}
		else if(this.id == 15)
		{
			if(!player.haveCompletedQuest(9))
			{
				if(!player.hasQuest(9))
				{
					this.tagSymbol = Constant.TAGSYMBOL_EXCLAMATION;
				}
			}
		}
		else if(this.id == 17)
		{
			if(this.tagSymbol == 0)
				this.tagSymbol = 1;
		}
		else if(this.id == 32)
		{
			if(this.tagSymbol == 0)
				this.tagSymbol = 1;
		}
		else if(this.id == 19)
		{
			this.direction = 1;
		}
		else if(this.id == 20)
		{
			this.currentFrameCounter++;
			if(this.currentFrameCounter > 1)
			{
				this.currentFrame = (this.currentFrame + 1) % this.frames;
				this.currentFrameCounter = 0;
			}
			
			if(!player.hasQuest(6) && !player.haveCompletedQuest(6))
			{
				this.tagSymbol = 1;
			}
			
			if(this.myMapX < 0 && Main.dialogEntity != this)
				this.direction = -1;
			else if(this.myMapX > 0 && Main.dialogEntity != this)
				this.direction = 1;
		}
		else if(this.id == 22)
		{
			this.currentFrameCounter++;
			if(this.currentFrameCounter > 1)
			{
				this.currentFrame = (this.currentFrame + 1) % this.frames;
				this.currentFrameCounter = 0;
			}
			
			if(Constant.gameTick() % 10 == 0)
			{
				Lighting.Create(this.Center().add(10+MathUtils.random(-16, 16), MathUtils.random(-16,16)), 512, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE], 1f);
			}
		}
		else if(this.id == 28)
		{
			this.tagSymbol = Constant.TAGSYMBOL_EXCLAMATION;
			if(this.currentFrame == 1)
			{
				this.tagSymbol = this.infos[0] >= this.infos[1] ? Constant.TAGSYMBOL_INTERROGATIONGREEN : Constant.TAGSYMBOL_INTERROGATIONGRAY;
				if(this.infos[0] >= this.infos[1])
				{
					this.useDialog = 288;
				}
			}
			else
			{
				this.useDialog = 286;
			}
		}
		else if(this.id == 29)
		{
			if(!player.haveCompletedQuest(7))
			{
				if(!player.hasQuest(7))
				{
					this.tagSymbol = Constant.TAGSYMBOL_EXCLAMATION;
				}
			}
		}
		else if(this.id == 30 || this.id == 34)
		{
			DJ.addWorldSound(this, DJ.CAMPFIRE);
			this.currentFrameCounter++;
			if(this.currentFrameCounter > 4)
			{
				this.currentFrame = (this.currentFrame + 1) % this.frames;
				this.currentFrameCounter = 0;
			}
			Lighting.Create(this.Center().add(10+MathUtils.random(-16, 16), MathUtils.random(-16,16)), 512, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE], 1f, true);
			for(Player p : Constant.getPlayersInRange(this, 700))
			{
				if(!player.inBattle())
				{
					p.addBuff(70, 0.1f, this);
					if(p.whoAmI == Main.me)
					{
						DJ.switchMusic(DJ.HOPE2, true);
					}
				}
			}
			if(this.id == 34)
			{
				this.infos[0]--;
				if(this.infos[0] <= 0)
				{
					this.active = false;
				}
			}
		}
		else if(this.id == 31)
		{
			if(this.infos[0] != 1)
			{
				this.velocity.y = 0;
				this.position.y = Constant.tryGetMapForEntity(this).height * 64f + 100;
				boolean awake = false;
				for(Player p : Constant.getPlayerList())
				{
					if(p != null && p.active && p.sameMapAs(this) && Math.abs(p.Center().x-this.Center().x) < 500)
					{
						awake = true;
						break;
					}
				}
				if(awake)
				{
					this.position.y = (Constant.tryGetMapForEntity(this).getFreeY((int) (this.Center().x/64))-1) * 64;
					Projectile p = Projectile.Summon(43, Vector2.Zero, Vector2.Zero, 0, 2f, this);
					p.scale = 2f;
					p.position = new Vector2(this.Center().x - p.width/2f, this.position.y + p.height/2f);
					p = Projectile.Summon(43, Vector2.Zero, Vector2.Zero, 0, 2f, this);
					p.scale = 2f;
					p.position = new Vector2(this.Center().x - p.width/2f, this.position.y + p.height/2f);
					p.mirrored = true;
					
					Vector2 pos =  new Vector2(this.Center().x, this.Center().y);
					Particle p2 = Particle.Create(pos, Vector2.Zero, 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 5.5f);
					p2.drawFront = true;
					p2.frameCounterTicks = 0;
					p2.fixAlpha = true;
					p2.alpha = 0.5f;
					p2 = Particle.Create(pos, Vector2.Zero, 11, Color.WHITE, 0f, 1f, 5f);
					p2.drawFront = true;
					p2.frameCounterTicks = 0;
					Main.doEarthquake(15);
					
					this.infos[0] = 1;
				}
			}
			else
			{
				Lighting.Create(this.Center(), 1024, new Color(0f, 0.69f, 1f, 1f), 1f, true);
				boolean awake = false;
				for(Player p : Constant.getPlayerList())
				{
					if(p != null && p.active && p.sameMapAs(this) && Math.abs(p.Center().x-this.Center().x) < 500)
					{
						awake = true;
						break;
					}
				}
				if(!awake)
				{
					Projectile p = Projectile.Summon(43, Vector2.Zero, Vector2.Zero, 0, 2f, this);
					p.scale = 2f;
					p.position = new Vector2(this.Center().x - p.width/2f, this.position.y + p.height/2f);
					p = Projectile.Summon(43, Vector2.Zero, Vector2.Zero, 0, 2f, this);
					p.scale = 2f;
					p.position = new Vector2(this.Center().x - p.width/2f, this.position.y + p.height/2f);
					p.mirrored = true;
					
					Vector2 pos =  new Vector2(this.Center().x, this.Center().y);
					Particle p2 = Particle.Create(pos, Vector2.Zero, 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 5.5f);
					p2.drawFront = true;
					p2.frameCounterTicks = 0;
					p2.fixAlpha = true;
					p2.alpha = 0.5f;
					p2 = Particle.Create(pos, Vector2.Zero, 11, Color.WHITE, 0f, 1f, 5f);
					p2.drawFront = true;
					p2.frameCounterTicks = 0;
					Main.doEarthquake(15);

					this.position.y = Constant.tryGetMapForEntity(this).height * 64f + 100;
					this.infos[0] = 0;
				}
			}
		}
		else if(this.id == 35)
		{
			Lighting.Create(this.Center().add(10+MathUtils.random(-16, 16), MathUtils.random(-16,16)), 512, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_DARKNESS], 1f, true);
		}
		else if(this.id == 37)
		{
			if(!Main.isAtDay())
			{
				Lighting.Create(this.Center(), 256, new Color(0.5f, 0.95f, 0.59f, 1f), 1f, 1f, true);
			}
		}
		else if(this.id == 38)
		{
			this.tagSymbol = NPC.questStateToTagSymbol(player, 11);
			Lighting l = Lighting.Create(this.Center(), 512f, new Color(0.92f, 0.97f, 0.8f, 1f), 1f);
			l.setParent(this);
		}
		else if(this.id == 39)
		{
			this.tagSymbol = NPC.questStateToTagSymbol(player, 12);
			Lighting l = Lighting.Create(this.Center(), 512f, new Color(0.92f, 0.97f, 0.8f, 1f), 1f);
			l.setParent(this);
		}
		
		this.sleeping = false;
		if(this.goSleep && !Main.isAtDay())
		{
			this.sleeping = true;
		}
		
		if(this.randomlywalk != Constant.RANDOMWALK_NONE)
		{
			this.infos[0]--;
			if(this.infos[0] == 0)
			{
				this.direction = Main.directionFromTo(this.Center(), this.initialPosition);
				
				GameMap m = Constant.tryGetMapForEntity(this);
				if(m.doesRectCollideWithMap(this.position.x - 100, this.position.y, this.width + 100, this.height))
					this.direction = 1;
				else if(m.doesRectCollideWithMap(this.position.x, this.position.y, this.width + 100, this.height))
					this.direction = -1;
				
				this.infos[1] = 1;
				this.currentFrame = 1;
			}
			else if(this.infos[0] < -60)
			{
				this.infos[0] = MathUtils.random(90, 120);
				this.infos[1] = 0;
				this.velocity.x = 0f;
			}
			if(this.infos[1] == 1)
			{
				this.velocity.x = this.direction * 100f;
			}
			if((Main.dialogEntity == this && Main.displayDialog) || (Main.displayCrafting && Main.displayCraftOrigin == this))
			{
				this.infos[1] = 0;
				this.infos[0] = MathUtils.random(-75, -45);
				this.velocity.x = 0f;
			}
			
			if(this.velocity.x != 0f)
			{
				this.currentFrameCounter++;
				if(this.currentFrameCounter > 5)
				{
					this.currentFrame++;
					if(this.currentFrame >= this.frames)
					{
						this.currentFrame=(this.randomlywalk == Constant.RANDOMWALK_PAUSED ? 0 : 1);
					}
					this.currentFrameCounter = 0;
				}
			}
			else
			{
				this.currentFrame = 0;
			}			
		}
		
		if(Main.dialogEntity == this && Main.displayDialog)
		{
			this.direction = Main.directionFromTo(this, player);
		}
		
		// Updates
		if(this.collisionsEnabled)
			this.velocity.y += Main.gravity;

		float addAmountX = this.velocity.x * delta;
		float newX = this.position.x + addAmountX;
		float addAmountY = this.velocity.y * delta;
		float newY = this.position.y + addAmountY;

		if(!Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, newY, this.width, this.height) || !this.collisionsEnabled)
		{
			this.position.y = newY;
		}
		else
		{
			/*if(this.velocity.y < 0f)
			{
				this.position.y -= Math.floor(this.position.y) % Tile.TILE_SIZE;
			}
			else if(this.velocity.y > 0f)
			{
				this.position.y = this.position.y;
			}*/
			if(this.velocity.y < 0f)
			{
				this.position.y = (float) (Math.floor(this.position.y/Tile.TILE_SIZE)*Tile.TILE_SIZE);
			}
			this.velocity.y = 0f;
		}

		if(!Constant.tryGetMapForEntity(this).doesRectCollideWithMap(newX, this.position.y, this.width, this.height) || !this.collisionsEnabled)
		{
			this.position.x = newX;
		}
		else
		{
			/*if(this.velocity.x < 0f)
			{
				this.position.x -= Math.floor(this.position.x) % Tile.TILE_SIZE;
			}
			else if(this.velocity.x > 0f)
			{
				this.position.x = this.position.x;
			}*/
			if(this.randomlywalk != Constant.RANDOMWALK_NONE)
				this.infos[1] = 0;
			
			this.velocity.x = 0;
		}
		
		if(this.collisionsEnabled && Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, this.position.y, this.width, this.height))
		{
			this.position = Constant.tryGetMapForEntity(this).getNextestFreeSpace(this.position.x, this.position.y, this.width, this.height, 1, 1).scl(64f);
		}


		boolean closest = true;
		if(this.useDialog != -1 && !this.sleeping)
		{
			for(NPC i : Constant.getNPCList())
			{
				if(!i.equals(this) && i.Center().dst(player.Center()) < this.Center().dst(player.Center()) && i.useDialog != -1 && !this.sleeping && this.myMapX ==  Constant.getPlayerList()[Main.me].myMapX && this.myMapY ==  Constant.getPlayerList()[Main.me].myMapY)
				{
					closest = false;
					break;
				}
			}
			for(Prop i : Constant.getPropList())
			{
				if(i.Center().dst(player.Center()) < this.Center().dst(player.Center()) && i.usable && this.myMapX == Constant.getPlayerList()[Main.me].myMapX && this.myMapY == Constant.getPlayerList()[Main.me].myMapY)
				{
					closest = false;
					break;
				}
			}
			if(Gdx.input.isKeyJustPressed(Keys.Z) && closest && this.Center().dst(player.Center()) < 120 && !Main.displayDialog && !Main.readyForNextmap)
			{
				player.direction = (player.Center().x < this.Center().x ? 1 : -1);
				this.direction = (player.Center().x < this.Center().x ? -1 : 1);
				this.showDialog(this.useDialog);
			}
		}
		
		if(this.lookingTo != null && this.id != 19)
		{
			this.direction = Main.directionFromTo(this.Center(), this.lookingTo.Center());
		}
		
		if(this.fixLookDirection != 0)
		{
			this.direction = this.fixLookDirection;
		}
	}

	public void showDialog(int dialogId)
	{
		Dialogs dialog = new Dialogs().setInfos(dialogId);
		if(!dialog.actionOnly && !Main.displayDialog && Main.cutscene == null)
		{
			Main.cutsceneBordersTicks = 0;
		}
		Main.displayDialog = true;
		Main.dialog = dialog;
		dialog.text = dialog.text.replaceAll(Pattern.quote ("{PLAYER}"), Main.saveName);
		String upperName = Main.saveName.toUpperCase();
		dialog.text = dialog.text.replaceAll(Pattern.quote ("{UPLAYER}"), upperName);
		Main.dialogTicks = 0;
		Main.dialogEntity = this;
		Main.dialogFullDrawn = false;
		Main.dialog.onOpen(Constant.getPlayerList()[Main.me]);
	}

	public void draw(float delta, SpriteBatch batch)
	{
		if(this.sleeping)
			return;
		
		if(batch != Main.batch || Main.getScreen().overlaps(this.hitBox()))
		{
			Sprite sprite;
			
			boolean closest = true;
			if(this.useDialog != -1)
			{
				for(NPC i : Main.npc)
				{
					if(!i.equals(this) && i.Center().dst(Main.player[Main.me].Center()) < this.Center().dst(Main.player[Main.me].Center()) && i.useDialog != -1 && this.myMapX == Main.player[Main.me].myMapX && this.myMapY == Main.player[Main.me].myMapY)
					{
						closest = false;
						break;
					}
				}
				for(Prop i : Main.prop)
				{
					if(i.Center().dst(Main.player[Main.me].Center()) < this.Center().dst(Main.player[Main.me].Center()) && i.usable && this.myMapX == Main.player[Main.me].myMapX && this.myMapY == Main.player[Main.me].myMapY)
					{
						closest = false;
						break;
					}
				}
				if(closest && this.Center().dst(Main.player[Main.me].Center()) < 120 && this.useDialog != -1 && !Main.displayDialog)
				{
					batch.setShader(Content.whiteS);
					
					sprite = new Sprite(Content.npc[this.id-1]);
					sprite.setRegion(0, this.currentFrame*this.height, this.width, this.height);
					sprite.setSize(this.width, this.height);
					sprite.setPosition(this.position.x + this.drawOffset.x - 2, this.position.y + this.drawOffset.y - 2);
					sprite.setAlpha(this.alpha);
					sprite.flip(this.direction == -1, false);
					sprite.rotate(this.rotation);
					sprite.draw(batch);
					
					sprite = new Sprite(Content.npc[this.id-1]);
					sprite.setRegion(0, this.currentFrame*this.height, this.width, this.height);
					sprite.setSize(this.width, this.height);
					sprite.setPosition(this.position.x + this.drawOffset.x + 2, this.position.y + this.drawOffset.y - 2);
					sprite.setAlpha(this.alpha);
					sprite.flip(this.direction == -1, false);
					sprite.rotate(this.rotation);
					sprite.draw(batch);
					
					sprite = new Sprite(Content.npc[this.id-1]);
					sprite.setRegion(0, this.currentFrame*this.height, this.width, this.height);
					sprite.setSize(this.width, this.height);
					sprite.setPosition(this.position.x + this.drawOffset.x - 2, this.position.y + this.drawOffset.y + 2);
					sprite.setAlpha(this.alpha);
					sprite.flip(this.direction == -1, false);
					sprite.rotate(this.rotation);
					sprite.draw(batch);

					sprite = new Sprite(Content.npc[this.id-1]);
					sprite.setRegion(0, this.currentFrame*this.height, this.width, this.height);
					sprite.setSize(this.width, this.height);
					sprite.setPosition(this.position.x + this.drawOffset.x + 2, this.position.y + this.drawOffset.y + 2);
					sprite.setAlpha(this.alpha);
					sprite.flip(this.direction == -1, false);
					sprite.rotate(this.rotation);
					sprite.draw(batch);

					batch.setShader(Main.basicShader);
				}
			}
			
			sprite = new Sprite(Content.npc[this.id-1]);
			sprite.setRegion(0, this.currentFrame*this.height, this.width, this.height);
			sprite.setSize(this.width, this.height);
			sprite.setPosition(this.position.x + this.drawOffset.x, this.position.y + this.drawOffset.y);
			sprite.setAlpha(this.alpha);
			sprite.flip(this.direction == -1, false);
			sprite.rotate(this.rotation);
			this.drawEnt(batch, sprite);
			
			if(this.id == Constant.NPCID_LEWIN && this.infos[0] != 1)
			{
				sprite = new Sprite(Content.lewinBall);
				Vector2 frameOffset = new Vector2((this.currentFrame == 0 || this.currentFrame == 1 || this.currentFrame == 3 ? 0 : (this.currentFrame == 2 ? -4 : 4)), (this.currentFrame == 2 || this.currentFrame == 4 ? 4 : 0));
				sprite.setPosition(this.position.x + (this.direction == Constant.DIRECTION_RIGHT ? -2 : -30 + this.width) + frameOffset.x, this.position.y + 28 + frameOffset.y);
				sprite.setRotation(Main.gameTick % 360);
				sprite.draw(batch);
				
				if(Main.gameTick % 3 == 0)
				{
					Vector2 particlePos = new Vector2(sprite.getX() + MathUtils.random(sprite.getWidth()), sprite.getY() + MathUtils.random(sprite.getHeight()));
					
					Particle p = Particle.Create(particlePos, Vector2.Zero, 2, new Color(0f, 1f, 1f, 1f), 1f, 1f + MathUtils.random(0.25f), 1f);
					p.lights = true;
					p.lightSize = 16;
					p.lightColor = Color.CYAN;
				}
			}
			else if(this.id == 37)
			{
				sprite = new Sprite(Content.extras[Main.isAtDay() ? 37 : 36]);
				sprite.setPosition(this.Center().x - 34, this.position.y -8);
				sprite.setRegion(0, this.currentFrame*84, 68, 84);
				sprite.setAlpha(this.alpha);
				sprite.setSize(68, 84);
				sprite.flip(this.direction == -1, false);
				sprite.rotate(this.rotation);
				sprite.draw(batch);
			}
		}
	}

	public void postDraw(SpriteBatch batch)
	{
		if(this.sleeping)
			return;
		
		float posX = this.Center().x;
		float posY = this.position.y + this.height + 8 + Main.FONT_SIZE;
		if(this.displayName)
		{
			Main.prettyFontDraw(batch, this.name, posX, posY, 1f, Color.GREEN, Color.BLACK, 1f, true, -1);
		}
		if(this.id == 34)
		{
			float timeleft = Main.ticksToSeconds(this.infos[0]);
			String str = String.format("%.1fs", timeleft);
			Main.prettyFontDraw(batch, str, posX, posY, 1f, Color.GREEN, Color.BLACK, 1f, true, -1);
		}

		boolean closest = true;
		if(this.useDialog != -1)
		{
			for(NPC i : Main.npc)
			{
				if(!i.equals(this) && i.Center().dst(Main.player[Main.me].Center()) < this.Center().dst(Main.player[Main.me].Center()) && i.useDialog != -1)
				{
					closest = false;
					break;
				}
			}
			for(Prop i : Main.prop)
			{
				if(i.Center().dst(Main.player[Main.me].Center()) < this.Center().dst(Main.player[Main.me].Center()) && i.usable)
				{
					closest = false;
					break;
				}
			}
		}
		if(closest && this.Center().dst(Main.player[Main.me].Center()) < 120 && this.useDialog != -1 && !Main.displayDialog)
		{
			Sprite pressx = new Sprite(Content.pressz);
			pressx.setPosition(this.position.x + this.width/2 - pressx.getWidth()/2, this.position.y + this.height + 8);
			Main.prettySpriteDraw(pressx, batch);
		}
		
		if(this.id == 28 && this.currentFrame == 1)
		{
			Color color = (this.infos[0] >= this.infos[1] ? Color.GREEN : Color.WHITE);
			Main.prettyFontDraw(batch, this.infos[0]+"/"+this.infos[1], this.Center().x, this.position.y+this.height+16, 1f, color, Color.BLACK, 1f, true, -1);
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
			if(closest && this.Center().dst(Main.player[Main.me].Center()) < 120 && this.useDialog != -1 && !Main.displayDialog)
				bonusY = 48;

			float sin = (float)Math.abs(Math.sin(((Main.gameTick * 5) % 360) * Math.PI/180));
			if(exc != null)
			{
				exc.setPosition(this.Center().x - exc.getWidth()/2, this.position.y + this.height + 16 + bonusY + sin * 16);
				exc.draw(batch);
			}
		}
	}
	
	public static NPC Create(Vector2 position, int mapX, int mapY, int type)
	{
		NPC npcs = new NPC();
		npcs.SetInfos(type, true);
		npcs.position = position;
		npcs.initialPosition = position;
		npcs.active = true;
		npcs.myMapX = mapX;
		npcs.myMapY = mapY;
		Invoke(npcs);
		return npcs;
	}
	
	public static int questStateToTagSymbol(Player player, int questID)
	{
		if(!player.haveCompletedQuest(questID))
		{
			if(player.hasQuest(questID))
				return player.readyToAccomplish(questID) ? Constant.TAGSYMBOL_INTERROGATIONGREEN : Constant.TAGSYMBOL_INTERROGATIONGRAY;
			else
				return Constant.TAGSYMBOL_EXCLAMATION;
		}
		else
		{
			return Constant.TAGSYMBOL_NONE;
		}
	}

	public static void Invoke(NPC n)
	{
		Constant.getNPCList().add(n);
	}
}