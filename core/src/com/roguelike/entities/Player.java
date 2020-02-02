package com.roguelike.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.ArkaClass;
import com.roguelike.constants.Biome;
import com.roguelike.constants.Buff;
import com.roguelike.constants.Bundle;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Item;
import com.roguelike.constants.Objective;
import com.roguelike.constants.Quest;
import com.roguelike.constants.Recipe;
import com.roguelike.constants.Set;
import com.roguelike.constants.Skill;
import com.roguelike.game.Content;
import com.roguelike.game.DJ;
import com.roguelike.game.Event;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;
import com.roguelike.online.interpretations.SomeRequest;
import com.roguelike.world.Tile;
import com.roguelike.world.Background;
import com.roguelike.world.GameMap;
import com.roguelike.game.SaveInfos;
import com.roguelike.game.SavedWorld;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Player extends Entity implements Cloneable {
	public transient Skill recallSkill;
	public int whoAmI;
	public transient float acceleration = 120f;
	public transient float disacceleration = 0.8f;
	public transient float maxSpeed = 700f;
	public transient float jumpHeight = 900f;
	public transient float maxHealth = 100;
	public float health;
	public transient int attackDir = 1;
	public transient int animFrame = 0;
	public transient float animFrameCounter = 0;
	public transient int animFrame2 = 0;
	public transient float animFrameCounter2 = 0;
	public transient boolean walking = false;
	public transient boolean grounded = false;
	public Item[] inventory = new Item[Constant.ITEMSLOT_MAX + 1];
	public Item[] vanity = new Item[4];
	public transient boolean douchebag = true;
	public transient boolean attacking = false;
	public transient boolean attacking2 = false;
	public transient boolean skillCasting = false;
	public transient boolean invertedSwing = false;
	public transient boolean invertedSwing2 = false;
	public Skill[] skills = new Skill[11];
	public ArrayList<Skill> learnedSkills = new ArrayList<Skill>();
	public transient Vector2 atkMouseDif = Vector2.Zero.cpy();
	public Buff[] buffs = new Buff[30];
	public transient Vector2 mouseOnAttack;
	public transient Vector2 posOnAttack;
	public transient boolean superVision;
	public transient float invincible = 0f;
	public transient boolean rolling = false;
	public transient float attackSpeed = 5f;
	public transient int resetGameTick = 0;
	public transient boolean dead = false;
	public transient boolean dancing = false;
	public transient boolean controllingGeas = false;
	public transient float criticalChance = 5f;
	public transient int lastHitTick = 0;
	public int skillSlotAvailable = 8;
	public transient float oldCriticalChance = 5f;

	public transient Texture customAnim = null;

	public transient int customAnimFrames = 1;
	public transient int customAnimSkipper = 0;
	public transient boolean customAnimBlockMove = false;
	public transient boolean customAnimBlockAttack = false;
	public transient boolean customAnimShouldRotate = false;
	public transient int customAnimFrame = 0;
	public transient Vector2 customAnimOffset = Vector2.Zero.cpy();
	public transient int customAnimFrameCounter = 0;
	public transient boolean customAnimShouldFreeze = true;
	public transient float customAnimRotation = 0f;

	public transient Texture customAnim2 = null;
	public transient int customAnim2Frames = 1;
	public transient int customAnim2Skipper = 0;
	public transient boolean customAnim2BlockMove = false;
	public transient boolean customAnim2BlockAttack = false;
	public transient boolean customAnim2ShouldRotate = false;
	public transient int customAnim2Frame = 0;
	public transient Vector2 customAnim2Offset = Vector2.Zero.cpy();
	public transient int customAnim2FrameCounter = 0;
	public transient boolean customAnim2ShouldFreeze = true;
	public transient float customAnim2Rotation = 0f;

	public transient int skillTicksLeft = 0;
	public transient int skillOnCast = -1;
	public transient float lifeSteal = 0f;
	public int stamina = 20;
	public float timeForStamina = 0f;
	public transient int maxStamina = 20;
	public float mana = 100;
	public transient int maxMana = 100;
	public transient float healthRegen = 1;
	public transient float manaRegen = 1;
	public transient float timeSinceLastAttack = 9999;
	public SaveInfos saveInfo = new SaveInfos();
	public int gold = 0;
	public transient boolean checkingInventory;
	public transient Vector2 mousePos = Vector2.Zero.cpy();
	
	public transient float cooldown = 0f;

	public transient float exhaust = 0;

	public int strength = 0;
	public int intelligence = 0;
	public int lethality = 0;
	public int agility = 0;
	public int luck = 0;
	public int vitality = 0;

	public transient int estrength = 0;
	public transient int eintelligence = 0;
	public transient int elethality = 0;
	public transient int eagility = 0;
	public transient int eluck = 0;
	public transient int evitality = 0;

	public transient float maxHealthPerc = 1f;
	public transient float critMult = 2f;

	public int level = 1;
	public int experience = 0;

	public transient float cooldownReduction = 1f;

	public int characterPoints = 5;

	public ArkaClass classType = ArkaClass.NONE;

	public transient int ticksFromLastSkill = 9999;

	public List<Quest> quests = new ArrayList<Quest>();
	public int displayQuest = -1;

	public int cameFromX = 0;
	public int cameFromY = 0;
	public int cameFromLX = 0;
	public int cameFromLY = 0;

	public List<Integer> questsCompleted = new ArrayList<Integer>();

	public transient boolean[] pressingButtons = new boolean[4];
	public transient boolean[] oldPressingButtons = new boolean[4];

	public transient boolean loadingMapOnline = false;

	public transient float untargetable = -1f;
	public transient float customDisaccelTime = -1f;
	public transient float customDisaccel = 1f;
	public transient float attackBlockTime = 0f;
	public transient float movementBlockTime = 0f;

	// Others
	public transient Vector2[][] myBand = new Vector2[2][25];
	public transient float[] bandTick = { -5, 5 };
	public transient boolean[] bandGo = { true, false };
	public transient ArrayList<Integer> contacting = new ArrayList<Integer>();
	public transient ArrayList<Integer> attacked = new ArrayList<Integer>();

	public transient boolean carryingGoblin = false;

	// Skills and passives
	public transient boolean doubleOnHit = false;
	public transient int arcaneCircle = 0;
	public transient int arcaneCircleElement = Constant.DAMAGETYPE_PHYSICAL;
	public transient int impactReduction = 0;
	public transient boolean rollBlocksProjectiles = false;
	public transient int ragingDash = 0;
	public transient boolean onlineWorking = false;
	public transient int lastMonsterHitTicks = 999999;

	public transient float[] extraElementDamage = new float[Constant.ELEMENTCOLORNAME.length];
	public transient float overallMagicDamage = 1f;
	public transient float extraMPperc = 1f;
	public transient float extraPPperc = 1f;
	public transient float extraMP = 0f;
	public transient float extraPP = 0f;
	public transient float manaCostMult = 1f;
	
	public transient int ticksFromLastAnimation1 = 999999;
	public transient int ticksFromLastAnimation2 = 999999;

	public int[] hotBar = new int[3];
	public transient float cooldownAcceleration = 1f;
	public transient int[] ticksSinceCardUse = new int[20];
	public transient boolean[] isCardAvailable = new boolean[20];
	public transient int forceDashPosture;
	
	public transient int maxMobs = 5;
	public transient int lastMobSpawn = 0;

	public ArrayList<Recipe> learnedRecipes = new ArrayList<Recipe>();
	private transient float prevMaxHP;
	public boolean updatedSkinColor = false;
	public float[] lastSkinColor = new float[3];
	public boolean standingOnPlatform = false;

	public void initialize() {
		ArrayList<Item> items = new ArrayList<Item>();
		items.addAll(Arrays.asList(this.inventory));
		items.addAll(Arrays.asList(this.vanity));

		for (Item i : items) {
			if (i != null)
			{
				i.initialize();
				i.holder = this;
			}
		}
		if(this.getRight() != null)
		{
			this.getRight().equippedHand = 1;
		}

		ArrayList<Buff> buffs = new ArrayList<Buff>();
		buffs.addAll(Arrays.asList(this.buffs));

		for (Buff b : buffs) {
			if (b != null)
				b.initialize(this);
		}
		this.learnedSkills.clear();
		this.skills = new Skill[11];
		Skill s2 = new Skill(19);
		this.learnedSkills.add(s2);
		this.skills[0] = s2;
		s2 = new Skill(94);
		this.learnedSkills.add(s2);
		this.skills[1] = s2;
		s2 = new Skill(55);
		this.learnedSkills.add(s2);
		this.skills[2] = s2;
		
		ArrayList<Skill> skills = new ArrayList<Skill>();
		skills.addAll(this.learnedSkills);

		for (int i = 0; i < this.skills.length; i++) {
			if (this.skills[i] != null)
				this.skills[i] = this.getSkill(this.skills[i].id);
		}

		for (Skill s : skills) {
			if (s != null)
				s.initialize();
		}
		this.updateRecipes();
		this.active = true;
	}
	
	public void updateRecipes()
	{
		ArrayList<Recipe> newList = new ArrayList<Recipe>();
		for(Recipe r : Recipe.values())
		{
			if(learnedRecipes.contains(r) || r.bundleid == Bundle.GENERIC)
			{
				newList.add(r);
			}
		}
		learnedRecipes = newList;
	}

	public Player() {
		super();
		Skill s = new Skill(19);
		this.learnedSkills.add(s);
		this.skills[0] = s;
		this.recallSkill = new Skill(62);
		this.width = 52;
		this.height = 72;
		contacting = new ArrayList<Integer>();
		attacked = new ArrayList<Integer>();
		this.mouseOnAttack = Vector2.Zero.cpy();
		this.posOnAttack = Vector2.Zero.cpy();
		this.extraElementDamage = new float[Constant.ELEMENTCOLORNAME.length];

		for (int i = 0; i < 4; i++) {
			pressingButtons[i] = false;
			oldPressingButtons[i] = false;
		}

		for (int i = 0; i < 25; i++) {
			for (int x = 0; x < 2; x++) {
				myBand[x][i] = new Vector2();
			}
		}
		for (int i = 0; i < 20; i++) {
			this.isCardAvailable[i] = true;
		}
		for (int i = 0; i < 11; i++) {
			this.skills[i] = null;
		}
		for (int i = 0; i < 3; i++) {
			this.hotBar[i] = -1;
		}
		// this.inventory[Constant.ITEMSLOT_LEFT] = new Item().SetInfos(2);
		this.prevMaxHP = 999999;
	}

	@Override
	public void update(float delta) {
		this.updatedSkinColor = false;
		this.getPCShaderArray();
		ResetStats(delta);
		this.inventory[Constant.ITEMSLOT_HEAD] = new Item().SetInfos(172);
		this.inventory[Constant.ITEMSLOT_BODY] = new Item().SetInfos(173);
		if(this.skills[0] == null)
			this.skills[0] = new Skill(19);
		
		if(this.skills[1] == null)
			this.skills[1] = new Skill(94);
		
		for (int i = 0; i <= Constant.ITEMSLOT_MAX; i++) {
			if (this.inventory[i] != null && this.inventory[i].stacks < 1) {
				this.inventory[i] = null;
			}
		}
		for (int i = Constant.ITEMSLOT_OFFSET; i <= Constant.ITEMSLOT_MAX; i++) {
			if (this.inventory[i] != null) {
				this.inventory[i].updateEquip(this, i);
			}
		}
		for(int i : this.hotBar)
		{
			if(i > 0)
			{
				Item item = this.getItem(i);
				if(item.itemclass == Constant.ITEMCLASS_ACCESSORY)
				{
					item.updateEquip(this, Constant.ITEMSLOT_FAKE_HOTBAR);
				}
			}
		}

		if (this.vanity[3] == null) {
			if (this.inventory[Constant.ITEMSLOT_BODY] != null)
				this.inventory[Constant.ITEMSLOT_BODY].updateVanity(this);
		} else
			this.vanity[3].updateVanity(this);

		if (this.vanity[2] == null) {
			if (this.inventory[Constant.ITEMSLOT_HEAD] != null)
				this.inventory[Constant.ITEMSLOT_HEAD].updateVanity(this);
		} else
			this.vanity[2].updateVanity(this);

		for (int i = 0; i < 30; i++) {
			if (this.buffs[i] != null) {
				this.applyBuffEffects(this.buffs[i]);
			}
		}
		this.extraUpdates();
		if (Gdx.input.isKeyPressed(Keys.O)) {
			if (!this.dancing)
				this.resetAnim();

			this.dancing = true;
		} else {
			this.dancing = false;
		}
		if (this.inventory[Constant.ITEMSLOT_LEFT] != null) {
			if (this.inventory[Constant.ITEMSLOT_LEFT].specialSkill != null) {
				this.skills[10] = this.inventory[Constant.ITEMSLOT_LEFT].specialSkill;
			} else {
				this.skills[10] = null;
			}
		}
		if (this.inventory[Constant.ITEMSLOT_LEFT] != null && this.inventory[Constant.ITEMSLOT_RIGHT] != null
				&& (this.inventory[Constant.ITEMSLOT_LEFT].specialSkill != null
				|| this.inventory[Constant.ITEMSLOT_RIGHT].specialSkill != null)) {
			this.skills[10] = new Skill(9);
		}
		for (int i = 0; i < 11; i++) {
			if (this.skills[i] != null) {
				this.skills[i].update(delta, this);
			}
		}
		for(Skill s : this.learnedSkills)
		{
			if(s.classPassive)
				s.update(delta, this);
		}
		this.recallSkill.update(delta, this);
		// Afterstats
		if(this.getEquippedItem(166) != null)
		{
			int mp = this.getMP();
			int pp = this.getPP();
			this.extraPP = -pp/this.extraPPperc + mp;
			this.extraMP = -mp/this.extraMPperc + pp;
		}
		
		this.maxHealth = (int) (Math.max(this.maxHealth * this.maxHealthPerc, 1));
		this.healSilent(this.healthRegen * delta);
		this.mana += this.manaRegen * delta * (this.inBattle() ? 1 : 3);

		this.maxMana = 100;

		if (this.mana > this.maxMana)
			this.mana = this.maxMana;

		if (this.health > this.maxHealth)
			this.health = this.maxHealth;

		Item item = this.inventory[Constant.ITEMSLOT_LEFT];

		if (item != null) {
			boolean attackFrame = this.animFrame == item.hurtFrame;
			for (int num : item.extraHits) {
				if (this.animFrame == num) {
					attackFrame = true;
					break;
				}
			}
			if (this.attacking && attackFrame && this.animFrameCounter == 0) {
				Sprite sprite = new Sprite(item.getAttackTexture());
				int width = (int) (sprite.getWidth());
				int height = (int) (sprite.getHeight() / item.attackFrames);
				float x = this.position.x + (this.direction == 1 ? 16 : this.width - 16 - width);
				float y = this.position.y - 104;
				if (item.overrideAttack == Constant.OVERRIDEATTACK_SWING && !item.dontAttack) {
					Rectangle wHitBox = new Rectangle(x, y, width, height);
					for (Monster m : Constant.getMonsterList(true)) {
						Rectangle mHitBox = m.hitBox();
						if (m.active && wHitBox.overlaps(mHitBox)) {
							for (int j = Constant.ITEMSLOT_OFFSET; j <= Constant.ITEMSLOT_MAX; j++) {
								if (this.inventory[j] != null && j != Constant.ITEMSLOT_RIGHT) {
									this.inventory[j].onHit(m, this);
									if (this.doubleOnHit)
										this.inventory[j].onHit(m, this);
								}
							}

							if(this.haveBuff(76) != -1 && this.haveBuff(77) == -1)
							{
								Skill s = this.getSkill(128);
								if(s != null)
								{
									for(int i = 0;i < 4;i++)
									{
										Vector2 pos = this.Center().add(new Vector2(100f, 0f).setAngle(i * 90 + Constant.gameTick()));
										Projectile.Summon(126, pos, new Vector2(1000f, 0f).setAngle(Main.angleBetween(pos, m.Center())), s.getInfoValueI(this, 0), 5f, this).setTarget(m).destroyOnTargetLoss = true;
									}
									this.addBuff(77, 0.1f, this);
								}
							}
							m.hurtDmgVar(item.damage, (this.Center().x > m.Center().x ? -1 : 1), 1f,
									this.canCritical(item, m), item.damageType, item.itemclass, this);
						}
					}
				}
				if (item.overrideAttack == Constant.OVERRIDEATTACK_ROTATE && !item.dontAttack) {
					x = this.Center().x;
					y = this.Center().y - height / 2;
					Polygon wHitBox = new Polygon(new float[] { 0, 0, width, 0, width, height, 0, height });

					Vector2 pos = new Vector2(x, y);
					Vector2 atkToP = Vector2.Zero.cpy();
					atkToP = item.offsetRotateAttack.cpy();
					atkToP.rotate(item.rotation);
					pos.add(atkToP);
					wHitBox.setPosition(pos.x, pos.y);
					wHitBox.setOrigin(0, height / 2);
					wHitBox.rotate(item.rotation);
					for (Monster m : Constant.getMonsterList(true)) {
						Polygon mHitBox = m.perfectHitBox();
						if (m.active && Intersector.overlapConvexPolygons(wHitBox, mHitBox)) {
							for (int j = Constant.ITEMSLOT_OFFSET; j <= Constant.ITEMSLOT_MAX; j++) {
								if (this.inventory[j] != null && j != Constant.ITEMSLOT_RIGHT) {
									this.inventory[j].onHit(m, this);
									if (this.doubleOnHit)
										this.inventory[j].onHit(m, this);
								}
							}


							if(this.haveBuff(76) != -1 && this.haveBuff(77) == -1)
							{
								Skill s = this.getSkill(128);
								if(s != null)
								{
									for(int i = 0;i < 4;i++)
									{
										Vector2 pos2 = this.Center().add(new Vector2(100f, 0f).setAngle(i * 90 + Constant.gameTick()));
										Projectile.Summon(126, pos2, new Vector2(1000f, 0f).setAngle(Main.angleBetween(pos, m.Center())), s.getInfoValueI(this, 0), 5f, this).setTarget(m).destroyOnTargetLoss = true;
									}
									this.addBuff(77, 0.1f, this);
								}
							}
							m.hurtDmgVar(item.damage, this.direction, 1f,
									this.canCritical(item, m), item.damageType, item.itemclass, this);
						}
					}
				}
				if (item.overrideAttack == Constant.OVERRIDEATTACK_BIGSWING && !item.dontAttack) {
					x = this.Center().x - (this.direction == -1 ? width / 2 : 0);
					y = this.Center().y - height / 2;
					Rectangle wHitBox = new Rectangle(x, y, width / 2, height);
					for (Monster m : Constant.getMonsterList(true)) {
						Rectangle mHitBox = m.hitBox();
						if (m.active && wHitBox.overlaps(mHitBox)) {
							for (int j = Constant.ITEMSLOT_OFFSET; j <= Constant.ITEMSLOT_MAX; j++) {
								if (this.inventory[j] != null && j != Constant.ITEMSLOT_RIGHT) {
									this.inventory[j].onHit(m, this);
									if (this.doubleOnHit)
										this.inventory[j].onHit(m, this);
								}
							}


							if(this.haveBuff(76) != -1 && this.haveBuff(77) == -1)
							{
								Skill s = this.getSkill(128);
								if(s != null)
								{
									for(int i = 0;i < 4;i++)
									{
										Vector2 pos = this.Center().add(new Vector2(100f, 0f).setAngle(i * 90 + Constant.gameTick()));
										Projectile.Summon(126, pos, new Vector2(1000f, 0f).setAngle(Main.angleBetween(pos, m.Center())), s.getInfoValueI(this, 0), 5f, this).setTarget(m).destroyOnTargetLoss = true;
									}
									this.addBuff(77, 0.1f, this);
								}
							}
							m.hurtDmgVar(item.damage, this.direction, 1f,
									this.canCritical(item, m), item.damageType, item.itemclass, this);
						}
					}
				}
				for (int i = Constant.ITEMSLOT_OFFSET; i <= Constant.ITEMSLOT_MAX; i++) {
					if (this.inventory[i] != null && i != Constant.ITEMSLOT_RIGHT) {
						this.inventory[i].onHurtFrame(sprite, this);
						if (this.inventory[i].shootProj > 0 && this.animFrame == this.inventory[i].hurtFrame
								&& this.attacking) {
							Vector2 pos = this.Center();
							Vector2 mouseNor = this.mouseOnAttack.cpy().sub(pos);
							mouseNor.nor();
							mouseNor.scl(this.inventory[i].shootSpeed);
							mouseNor.rotate(MathUtils.random(-this.inventory[i].shootAccuracy / 2f,
									this.inventory[i].shootAccuracy / 2f));
							Vector2 spawnPos = this.Center();
							if(this.inventory[i].overrideAttack != Constant.OVERRIDEATTACK_BIGROTATE)
							{
								Vector2 atkToP = Vector2.Zero.cpy();
								atkToP = this.inventory[i].spawnProjectileOffset.cpy();
								atkToP.x += this.inventory[i].spriteWidth;
								atkToP.rotate(mouseNor.angle());
								spawnPos.add(atkToP);
							}
							else
							{
								Vector2 atkToP = Vector2.Zero.cpy();
								atkToP = this.inventory[i].spawnProjectileOffset.cpy();
								atkToP.rotate(mouseNor.angle());
								spawnPos.add(atkToP);
							}
							if (item.id != 47 || !Projectile.Exists(item.shootProj)) {
								Projectile proj = Projectile.Summon(this.inventory[i].shootProj, spawnPos, mouseNor,
										this.inventory[i].damage, 6f, this);
								this.inventory[i].onShoot(proj, this);
							}
						}
					}
				}

			}
		}

		item = this.inventory[Constant.ITEMSLOT_RIGHT];
		if (item != null) {
			boolean attackFrame = this.animFrame2 == item.hurtFrame;
			for (int num : item.extraHits) {
				if (this.animFrame2 == num) {
					attackFrame = true;
					break;
				}
			}
			if (this.attacking2 && attackFrame && this.animFrameCounter2 == 0) {
				Sprite sprite = new Sprite(item.getAttackTexture());
				int width = (int) (sprite.getWidth());
				int height = (int) (sprite.getHeight() / item.attackFrames);
				float x = this.position.x + (this.direction == 1 ? 16 : this.width - 16 - width);
				float y = this.position.y - 104;
				if (item.overrideAttack == Constant.OVERRIDEATTACK_SWING && !item.dontAttack) {
					Rectangle wHitBox = new Rectangle(x, y, width, height);
					for (Monster m : Constant.getMonsterList(true)) {
						Rectangle mHitBox = m.hitBox();
						if (m.active && wHitBox.overlaps(mHitBox)) {
							for (int j = Constant.ITEMSLOT_OFFSET; j <= Constant.ITEMSLOT_MAX; j++) {
								if (this.inventory[j] != null && j != Constant.ITEMSLOT_LEFT) {
									this.inventory[j].onHit(m, this);
									if (this.doubleOnHit)
										this.inventory[j].onHit(m, this);
								}
							}


							if(this.haveBuff(76) != -1 && this.haveBuff(77) == -1)
							{
								Skill s = this.getSkill(128);
								if(s != null)
								{
									for(int i = 0;i < 4;i++)
									{
										Vector2 pos = this.Center().add(new Vector2(100f, 0f).setAngle(i * 90 + Constant.gameTick()));
										Projectile.Summon(126, pos, new Vector2(1000f, 0f).setAngle(Main.angleBetween(pos, m.Center())), s.getInfoValueI(this, 0), 5f, this).setTarget(m).destroyOnTargetLoss = true;
									}
									this.addBuff(77, 0.1f, this);
								}
							}
							m.hurtDmgVar(item.damage, this.direction, 1f,
									this.canCritical(item, m), item.damageType, item.itemclass, this);
						}
					}
				}
				if (item.overrideAttack == Constant.OVERRIDEATTACK_ROTATE && !item.dontAttack) {
					x = this.Center().x;
					y = this.Center().y - height / 2;
					Polygon wHitBox = new Polygon(new float[] { 0, 0, width, 0, width, height, 0, height });
					Vector2 pos = new Vector2(x, y);
					Vector2 atkToP = Vector2.Zero.cpy();
					atkToP = item.offsetRotateAttack.cpy();
					atkToP.rotate(item.rotation);
					pos.add(atkToP);
					wHitBox.setPosition(pos.x, pos.y);
					wHitBox.setOrigin(0, height / 2);
					wHitBox.rotate(item.rotation);
					for (Monster m : Constant.getMonsterList(true)) {
						Polygon mHitBox = m.perfectHitBox();
						mHitBox.setPosition(m.position.x, m.position.y);
						if (m.active && Intersector.overlapConvexPolygons(wHitBox, mHitBox)) {
							for (int j = Constant.ITEMSLOT_OFFSET; j <= Constant.ITEMSLOT_MAX; j++) {
								if (this.inventory[j] != null && j != Constant.ITEMSLOT_LEFT) {
									this.inventory[j].onHit(m, this);
									if (this.doubleOnHit)
										this.inventory[j].onHit(m, this);
								}
							}


							if(this.haveBuff(76) != -1 && this.haveBuff(77) == -1)
							{
								Skill s = this.getSkill(128);
								if(s != null)
								{
									for(int i = 0;i < 4;i++)
									{
										Vector2 pos2 = this.Center().add(new Vector2(100f, 0f).setAngle(i * 90 + Constant.gameTick()));
										Projectile.Summon(126, pos2, new Vector2(1000f, 0f).setAngle(Main.angleBetween(pos, m.Center())), s.getInfoValueI(this, 0), 5f, this).setTarget(m).destroyOnTargetLoss = true;
									}
									this.addBuff(77, 0.1f, this);
								}
							}
							m.hurtDmgVar(item.damage, this.direction, 1f,
									this.canCritical(item, m), item.damageType, item.itemclass, this);
						}
					}
				}
				if (item.overrideAttack == Constant.OVERRIDEATTACK_BIGSWING && !item.dontAttack) {
					x = this.Center().x - (this.direction == -1 ? width / 2 : 0);
					y = this.Center().y - height / 2;
					Rectangle wHitBox = new Rectangle(x, y, width / 2, height);
					for (Monster m : Constant.getMonsterList(true)) {
						Rectangle mHitBox = m.hitBox();
						if (m.active && wHitBox.overlaps(mHitBox)) {
							for (int j = Constant.ITEMSLOT_OFFSET; j <= Constant.ITEMSLOT_MAX; j++) {
								if (this.inventory[j] != null && j != Constant.ITEMSLOT_LEFT) {
									this.inventory[j].onHit(m, this);
									if (this.doubleOnHit)
										this.inventory[j].onHit(m, this);
								}
							}


							if(this.haveBuff(76) != -1 && this.haveBuff(77) == -1)
							{
								Skill s = this.getSkill(128);
								if(s != null)
								{
									for(int i = 0;i < 4;i++)
									{
										Vector2 pos = this.Center().add(new Vector2(100f, 0f).setAngle(i * 90 + Constant.gameTick()));
										Projectile.Summon(126, pos, new Vector2(1000f, 0f).setAngle(Main.angleBetween(pos, m.Center())), s.getInfoValueI(this, 0), 5f, this).setTarget(m).destroyOnTargetLoss = true;
									}
									this.addBuff(77, 0.1f, this);
								}
							}
							m.hurtDmgVar(item.damage, this.direction, 1f,
									this.canCritical(item, m), item.damageType, item.itemclass, this);
						}
					}
				}
				int i = Constant.ITEMSLOT_RIGHT;
				if (this.inventory[i] != null) {
					if (this.inventory[i].shootProj > 0 && this.animFrame2 == this.inventory[i].hurtFrame
							&& this.attacking2) {
						Vector2 pos = this.Center();
						Vector2 mouseNor = this.mouseOnAttack.cpy().sub(pos);
						mouseNor.nor();
						mouseNor.scl(this.inventory[i].shootSpeed);
						mouseNor.rotate(MathUtils.random(-this.inventory[i].shootAccuracy / 2f,
								this.inventory[i].shootAccuracy / 2f));
						Vector2 spawnPos = this.Center();
						if(this.inventory[i].overrideAttack != Constant.OVERRIDEATTACK_BIGROTATE)
						{
							Vector2 atkToP = Vector2.Zero.cpy();
							atkToP = this.inventory[i].spawnProjectileOffset.cpy();
							atkToP.x += this.inventory[i].spriteWidth;
							atkToP.x += 8;
							atkToP.y += 8 * this.direction;
							atkToP.rotate(mouseNor.angle());
							spawnPos.add(atkToP);
						}
						else
						{
							Vector2 atkToP = Vector2.Zero.cpy();
							atkToP = this.inventory[i].spawnProjectileOffset.cpy();
							atkToP.x += 8;
							atkToP.y += 8 * this.direction;
							atkToP.rotate(mouseNor.angle());
							spawnPos.add(atkToP);
						}
						if (item.id != 47 || !Projectile.Exists(item.shootProj)) {
							Projectile proj = Projectile.Summon(this.inventory[i].shootProj, spawnPos, mouseNor,
									this.inventory[i].damage, 6f, this);
							this.inventory[i].onShoot(proj, this);
						}
					}
				}

			}
		}

		float ms = this.maxSpeed;
		float mult = 1f;
		if (this.attacking) {
			if ((this.inventory[Constant.ITEMSLOT_LEFT] != null
							&& this.inventory[Constant.ITEMSLOT_LEFT].isMelee())) {
				mult = Math.min(0.75f, mult);
			} else {
				mult = Math.min(0.25f, mult);
			}
		}
		if (this.attacking2) {
			if (this.inventory[Constant.ITEMSLOT_RIGHT] != null && this.inventory[Constant.ITEMSLOT_RIGHT].isMelee()) {
				mult = Math.min(0.75f, mult);
			} else {
				mult = Math.min(0.25f, mult);
			}
		}

		for (Projectile i : Constant.getProjectileList()) {
			if (i.active && i.type == 12 && i.owner == this) {
				mult = Math.min(0.5f, mult);
				break;
			}
		}

		ms *= mult;

		if (!AHS.isUp && this.whoAmI == Main.me) {
			if (Gdx.input.isKeyPressed(Keys.A)) {
				this.pressingButtons[Constant.AIDIRECTION_LEFT - 1] = true;
			} else {
				this.pressingButtons[Constant.AIDIRECTION_LEFT - 1] = false;
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				this.pressingButtons[Constant.AIDIRECTION_RIGHT - 1] = true;
			} else {
				this.pressingButtons[Constant.AIDIRECTION_RIGHT - 1] = false;
			}
			if (Gdx.input.isKeyPressed(Keys.W)) {
				this.pressingButtons[Constant.AIDIRECTION_UP - 1] = true;
			} else {
				this.pressingButtons[Constant.AIDIRECTION_UP - 1] = false;
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				this.pressingButtons[Constant.AIDIRECTION_DOWN - 1] = true;
			} else {
				this.pressingButtons[Constant.AIDIRECTION_DOWN - 1] = false;
			}

			if (Main.shouldNet()) {
				SomeRequest request = new SomeRequest();
				request.text.add("KeyPress " + this.whoAmI + " " + this.pressingButtons[0] + " "
						+ this.pressingButtons[1] + " " + this.pressingButtons[2] + " " + this.pressingButtons[3]);

				request.text.add("mousePos " + this.whoAmI + " " + Main.mouseInWorld().x + " " + Main.mouseInWorld().y);
				Main.client.client.sendUDP(request);
			}
		}

		if ((this.pressingButtons[Constant.AIDIRECTION_LEFT - 1]) && this.canMove()) {
			this.velocity.x = Math.max(-ms, this.velocity.x - this.acceleration);
			this.walking = true;
			if (this.customAnim == null && this.customAnim2 == null)
				this.direction = -1;
		} else if ((this.pressingButtons[Constant.AIDIRECTION_RIGHT - 1]) && this.canMove()) {
			this.velocity.x = Math.min(ms, this.velocity.x + this.acceleration);
			this.walking = true;
			if (this.customAnim == null && this.customAnim2 == null)
				this.direction = 1;
		} else {
			this.velocity.x *= this.disacceleration;
		}

		boolean atkToDir = false;
		if ((this.attacking || this.attacking2) && !this.rolling)
			atkToDir = true;

		if (this.whoAmI == Main.me) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)
					&& (this.inventory[Constant.ITEMSLOT_LEFT] != null)
					&& this.canUseAttack() && this.getLeft().isWeapon() && this.inventory[Constant.ITEMSLOT_LEFT].cdf <= 0)
				atkToDir = true;

			if (Gdx.input.isButtonPressed(Buttons.RIGHT)
					&& (this.inventory[Constant.ITEMSLOT_RIGHT] != null)
					&& this.canUseAttack() && this.getRight().isWeapon() && this.inventory[Constant.ITEMSLOT_RIGHT].cdf <= 0)
				atkToDir = true;
		}

		if (atkToDir)
			this.direction = this.attackDir;
		else {
			if (this.timeSinceLastAttack < 2 && ((this.inventory[Constant.ITEMSLOT_LEFT] != null
					&& this.inventory[Constant.ITEMSLOT_LEFT].rotateAfterAttack)
					|| (this.inventory[Constant.ITEMSLOT_RIGHT] != null
					&& this.inventory[Constant.ITEMSLOT_RIGHT].rotateAfterAttack))) {
				Vector2 dif = this.mousePos.cpy().sub(this.Center());
				int cDir = (dif.x < 0 ? -1 : 1);
				this.direction = cDir;
			}
		}

		if (this.whoAmI == Main.me && Main.debug && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			if (Gdx.input.isKeyPressed(Keys.V) && !Main.displayInventory) {
				int cX = (int) (this.mousePos.cpy().x / Tile.TILE_SIZE);
				int cY = (int) (this.mousePos.cpy().y / Tile.TILE_SIZE);

				if (cX >= 0 && cX < Constant.getPlayerMap(this.whoAmI).width && cY >= 0
						&& cY < Constant.getPlayerMap(this.whoAmI).height) {
					Constant.getPlayerMap(this.whoAmI).map[cY][cX] = Tile.getTileTypeById(Main.myBlock);
				}
			}
			if (Gdx.input.isKeyPressed(Keys.C) && !Main.displayInventory) {
				int cX = (int) (this.mousePos.cpy().x / Tile.TILE_SIZE);
				int cY = (int) (this.mousePos.cpy().y / Tile.TILE_SIZE);

				if (cX >= 0 && cX < Constant.getPlayerMap(this.whoAmI).width && cY >= 0
						&& cY < Constant.getPlayerMap(this.whoAmI).height) {
					Constant.getPlayerMap(this.whoAmI).map[cY][cX] = null;
				}
			}
			if (Gdx.input.isKeyPressed(Keys.X) && !Main.displayInventory) {
				int cX = (int) (this.mousePos.cpy().x / Tile.TILE_SIZE);
				int cY = (int) (this.mousePos.cpy().y / Tile.TILE_SIZE);

				if (cX >= 0 && cX < Constant.getPlayerMap(this.whoAmI).width && cY >= 0
						&& cY < Constant.getPlayerMap(this.whoAmI).height) {
					Constant.getPlayerMap(this.whoAmI).bg[cY][cX][0] = Background.getTileTypeById(Main.myBlock);
				}
			}
			if (Gdx.input.isKeyPressed(Keys.Z) && !Main.displayInventory) {
				int cX = (int) (this.mousePos.cpy().x / Tile.TILE_SIZE);
				int cY = (int) (this.mousePos.cpy().y / Tile.TILE_SIZE);

				if (cX >= 0 && cX < Constant.getPlayerMap(this.whoAmI).width && cY >= 0
						&& cY < Constant.getPlayerMap(this.whoAmI).height) {
					Constant.getPlayerMap(this.whoAmI).bg[cY][cX][0] = null;
				}
			}
		}

		if (this.inventory[Constant.ITEMSLOT_LEFT] != null && this.attacking
				&& this.animFrame >= this.inventory[Constant.ITEMSLOT_LEFT].attackFrames - 1) {
			for (int i = Constant.ITEMSLOT_OFFSET; i <= Constant.ITEMSLOT_MAX; i++) {
				if (this.inventory[i] != null)
					this.inventory[i].onAttack(this);
			}
			this.attacking = false;
			this.attacked.clear();
		}
		if (this.inventory[Constant.ITEMSLOT_RIGHT] != null && this.attacking2
				&& this.animFrame2 >= this.inventory[Constant.ITEMSLOT_RIGHT].attackFrames - 1) {
			for (int i = Constant.ITEMSLOT_OFFSET; i <= Constant.ITEMSLOT_MAX; i++) {
				if (this.inventory[i] != null)
					this.inventory[i].onAttack(this);
			}
			this.attacking2 = false;
			this.attacked.clear();
		}

		boolean canUpdate = true;
		if (Main.readyForNextmap && !this.dead) {
			this.velocity.x = 0;
			this.velocity.y = 0;
			canUpdate = false;
		}

		if (canUpdate) {
			if (this.haveCollisions() && Constant.getPlayerMap(this.whoAmI).doesRectCollideWithMap2(this.position.x,
					this.position.y, this.width, this.height) && !this.standingOnPlatform) {
				this.position = Constant.getPlayerMap(this.whoAmI).getNextestFreeSpace(this.position.x, this.position.y,
						this.width, this.height, this.direction, 1).scl(64f);
			}

			if (this.haveBuff(26) == -1)
				this.velocity.y += Main.gravity;

			if (this.haveBuff(7) != -1) {
				this.velocity.y = 0f;
				this.velocity.x = 0f;
				this.rolling = false;
			}

			if (this.haveBuff(25) != -1) {
				Monster target = null;
				for (Monster m : Constant.getMonsterList(false)) {
					int slot = m.haveBuff(45);
					if (slot != -1) {
						Buff b = m.buffs[slot];
						if (b.getOrigin() == this) {
							target = m;
							break;
						}
					}
				}
				if (target != null) {
					if (target.Center().dst(this.Center()) > 60f)
						this.velocity.setLength(3500f);
					else
						this.velocity.setLength(500f);
					this.redirectVelocity(target.Center());
				} else {
					this.removeBuff(25);
				}
			}
			
			if (this.haveBuff(68) != -1) {
				Monster target = null;
				for (Monster m : Constant.getMonsterList(false)) {
					int slot = m.haveBuff(69);
					if (slot != -1) {
						Buff b = m.buffs[slot];
						if (b.getOrigin() == this) {
							target = m;
							break;
						}
					}
				}
				if (target != null) {
					if (target.Center().dst(this.Center()) > 60f)
						this.velocity.setLength(5000f);
					else
						this.velocity.setLength(500f);
					this.redirectVelocity(target.Center());
				} else {
					this.removeBuff(68);
				}
			}

			float addAmountX = this.velocity.x * delta;
			float newX = this.position.x + addAmountX;
			float addAmountY = this.velocity.y * delta;
			float newY = this.position.y + addAmountY;

			this.grounded = false;
			if (!this.haveCollisions() || !Constant.getPlayerMap(this.whoAmI).doesRectCollideWithMap2(this.position.x,
					newY, this.width, this.height)) {
				this.position.y = newY;
			} else {
				if (this.velocity.y < 0f) {
					if(!this.standingOnPlatform)
						this.position.y = (float) (Math.floor(this.position.y)
								- Math.floor(this.position.y) % Tile.TILE_SIZE);
					this.grounded = true;
				} else if (this.velocity.y > 0f) {
					if(!this.standingOnPlatform)
						this.position.y = (float) (Math.floor(this.position.y)
							+ (Tile.TILE_SIZE - Math.floor(this.position.y) % Tile.TILE_SIZE)
							- (this.height - Tile.TILE_SIZE));
				}
				this.velocity.y = 0f;
			}

			if (!this.haveCollisions() || !Constant.getPlayerMap(this.whoAmI).doesRectCollideWithMap2(newX,
					this.position.y, this.width, this.height)) {
				this.position.x = newX;
			} else {
				if (this.velocity.x < 0f) {
					if(!this.standingOnPlatform)
						this.position.x = (float) (Math.floor(this.position.x)
								- Math.floor(this.position.x) % Tile.TILE_SIZE);
				} else if (this.velocity.x > 0f) {
					if(!this.standingOnPlatform)
					{
						this.position.x = (float) (Math.floor(this.position.x)
								- (Tile.TILE_SIZE - Math.floor(this.position.x) % Tile.TILE_SIZE));
						this.position.x += (float) (Tile.TILE_SIZE - this.position.x % Tile.TILE_SIZE)
								- (this.width - Tile.TILE_SIZE);
					}
				}
				if (this.rolling) {
					this.rolling = false;
					Item bow = this.getEquippedItem(17);
					if (bow != null) {
						this.addBuff(5, bow.getInfoValueI(0), bow.getInfoValueI(0), 3f, this);
					}
					this.velocity.x = 0;
				}
			}

			if (this.whoAmI == Main.me && !this.loadingMapOnline && !Main.readyForNextmap && Main.getLayerY(this.myMapY) < 3) {
				if (this.position.x + this.width < 0
						&& Constant.getPlayerMap(this.whoAmI).expected[Constant.AIDIRECTION_LEFT - 1] != -1) {
					Main.SaveMap(Constant.getPlayerMap(this.whoAmI), this.myMapX, this.myMapY);
					this.updateCameMap();
					this.myMapX--;
					Main.SwitchMap(Constant.getPlayerMap(this.whoAmI).expected[Constant.AIDIRECTION_LEFT - 1], true,
							false, null);
					this.onChangeMap();
					System.out.println("Switching maps cause screen exit.");
				} else if (this.position.x > Constant.getPlayerMap(this.whoAmI).width * Tile.TILE_SIZE
						&& Constant.getPlayerMap(this.whoAmI).expected[Constant.AIDIRECTION_RIGHT - 1] != -1) {
					Main.SaveMap(Constant.getPlayerMap(this.whoAmI), this.myMapX, this.myMapY);
					this.updateCameMap();
					this.myMapX++;
					Main.SwitchMap(Constant.getPlayerMap(this.whoAmI).expected[Constant.AIDIRECTION_RIGHT - 1], true,
							false, null);
					this.onChangeMap();
					System.out.println("Switching maps cause screen exit.");
				} else if (this.position.y + this.height < 0
						&& Constant.getPlayerMap(this.whoAmI).expected[Constant.AIDIRECTION_DOWN - 1] != -1) {
					Main.SaveMap(Constant.getPlayerMap(this.whoAmI), this.myMapX, this.myMapY);
					this.updateCameMap();
					this.myMapY--;
					Main.SwitchMap(Constant.getPlayerMap(this.whoAmI).expected[Constant.AIDIRECTION_DOWN - 1], false,
							true, null);
					this.onChangeMap();
					System.out.println("Switching maps cause screen exit.");
				} else if (this.position.y > Constant.getPlayerMap(this.whoAmI).height * Tile.TILE_SIZE
						&& Constant.getPlayerMap(this.whoAmI).expected[Constant.AIDIRECTION_UP - 1] != -1) {
					Main.SaveMap(Constant.getPlayerMap(this.whoAmI), this.myMapX, this.myMapY);
					this.updateCameMap();
					this.myMapY++;
					Main.SwitchMap(Constant.getPlayerMap(this.whoAmI).expected[Constant.AIDIRECTION_UP - 1], false,
							true, null);
					this.onChangeMap();
					System.out.println("Switching maps cause screen exit.");
				}
			}
		} else {
			this.velocity = Vector2.Zero.cpy();
		}

		float max = 3;
		if (this.getAnim() == Constant.ANIMATION_ATTACKING) {
			max = 1.99f;
			if (this.attacking)
			{
				this.animFrameCounter += (this.getAttackSpeed() >= 2f ? 2 : 1);
			}
			if (this.attacking2)
				this.animFrameCounter2 += (this.getAttackSpeed() >= 2f ? 2 : 1);
		} else {
			this.animFrameCounter++;
		}

		if (this.getAnim() == Constant.ANIMATION_INVENTORY)
			max = 2;
		if (this.getAnim() == Constant.ANIMATION_ROLL)
			max = 2;
		if (this.getAnim() == Constant.ANIMATION_DANCE)
			max = 8;

		if (this.animFrameCounter > max) {
			this.animFrame++;
			this.animFrameCounter = 0;
			if(this.attacking && this.getLeft() != null && this.getLeft().attackSounds.containsKey(this.animFrame))
			{
				DJ.playSound(this.getLeft().attackSounds.get(this.animFrame), DJ.getVolumeMultByDistance(this.Center()), MathUtils.random(-0.075f, 0.075f) + this.getLeft().attackSoundPitch);
			}
			
			if (this.getAnim() == Constant.ANIMATION_ROLL && this.grounded) {
				this.rolling = false;
				Item bow = this.getEquippedItem(17);
				if (bow != null) {
					this.addBuff(5, bow.getInfoValueI(0), bow.getInfoValueI(0), 3f, this);
				}
			}
		}
		if (this.animFrameCounter2 > max) {
			this.animFrame2++;
			this.animFrameCounter2 = 0;
			if(this.attacking2 && this.getRight() != null && this.getRight().attackSounds.containsKey(this.animFrame2))
			{
				DJ.playSound(this.getRight().attackSounds.get(this.animFrame2), DJ.getVolumeMultByDistance(this.Center()), MathUtils.random(-0.075f, 0.075f) + this.getRight().attackSoundPitch);
			}
		}

		if (this.customAnim != null) {
			max = this.customAnimSkipper;
			this.customAnimFrameCounter++;
			if (this.customAnimFrameCounter > max) {
				this.customAnimFrame++;
				this.customAnimFrameCounter = 0;
				if (this.customAnimFrame >= this.customAnimFrames) {
					this.customAnim = null;
					this.ticksFromLastAnimation1 = 0;
				}
			}
		}

		if (this.customAnim2 != null) {
			max = this.customAnim2Skipper;
			this.customAnim2FrameCounter++;
			if (this.customAnim2FrameCounter > max) {
				this.customAnim2Frame++;
				this.customAnim2FrameCounter = 0;
				if (this.customAnim2Frame >= this.customAnim2Frames) {
					this.customAnim2 = null;
					this.ticksFromLastAnimation2 = 0;
				}
			}
		}

		if (this.whoAmI == Main.me && Gdx.input.isKeyJustPressed(Keys.W) && this.canMove()) {
			if (Main.shouldNet()) {
				SomeRequest request = new SomeRequest("Jump " + this.whoAmI);
				Main.client.client.sendTCP(request);
			}
			this.executeJump();
		}

		if (this.whoAmI == Main.me
				&& (Gdx.input.isKeyPressed(Keys.E)
						|| ((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) && this.direction == 1))
				&& this.canMove() && this.canRoll() && this.stamina >= 2) {
			if (Main.shouldNet()) {
				SomeRequest request = new SomeRequest("Roll 1 " + this.whoAmI);
				Main.client.client.sendTCP(request);
			}
			this.executeRoll(1);
		} else if (this.whoAmI == Main.me
				&& (Gdx.input.isKeyPressed(Keys.Q)
						|| ((Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) && this.direction == -1))
				&& this.canMove() && this.canRoll() && this.stamina >= 2) {
			if (Main.shouldNet()) {
				SomeRequest request = new SomeRequest("Roll 0 " + this.whoAmI);
				Main.client.client.sendTCP(request);
			}
			this.executeRoll(0);
		}
		if (this.stamina < 0)
			this.stamina = 0;

		for (Monster m : Constant.getMonsterList(false)) {
			if (this.hitBox().overlaps(m.hitBox())) {
				if (this.contacting.indexOf(m.uid) == -1)
					this.onEnterHitBox(m);

				this.contacting.add(m.uid);
			} else {
				if (this.contacting.indexOf(m.uid) != -1) {
					this.contacting.remove(this.contacting.indexOf(m.uid));
				}
			}
		}

		for (int i = 0; i < 2; i++) {
			if (bandGo[i]) {
				bandTick[i] += MathUtils.random() * 2;
				if (i == 0)
					bandTick[i] += 0.9f;
				else
					bandTick[i] += 0.7f;

				if (bandTick[i] >= 5) {
					bandGo[i] = false;
				}

			} else {
				bandTick[i] -= MathUtils.random() * 2;
				if (bandTick[i] <= -5) {
					bandGo[i] = true;
				}
			}
		}

		for (Quest q : this.quests) {
			for (Objective o : q.objectives) {
				o.update(delta);
			}
		}
		this.prevMaxHP = this.maxHealth;
		this.oldPressingButtons = this.pressingButtons;
		this.standingOnPlatform = false;
	}

	public void extraUpdates() {
		if ((Set.playerItemSetCount(this, Set.PUNISHMENT)) >= 3) {
			this.attackSpeed += 0.3f;
		}
		if ((Set.playerItemSetCount(this, Set.VAMPIRICLORD)) >= 3) {
			this.lifeSteal += 0.05f;
		}

		Skill s = null;

		if ((s = this.getSkill(63)) != null) {
			this.extraMPperc += (this.health / this.maxHealth) / 2f;
			this.healthRegen *= 1 + (2 * this.level * ((this.maxHealth - this.health) / this.maxHealth));
		}
		if ((s = this.getSkill(67)) != null) {
			Item leftitem = this.inventory[Constant.ITEMSLOT_LEFT];
			Item rightitem = this.inventory[Constant.ITEMSLOT_RIGHT];
			if (leftitem != null
					&& (leftitem.itemclass == Constant.ITEMCLASS_BROADSWORD
							|| leftitem.itemclass == Constant.ITEMCLASS_SHORTSWORD
							|| leftitem.itemclass == Constant.ITEMCLASS_LONGSWORD)
					&& rightitem == null && this.vanity[0] == null && this.vanity[1] == null) {
				this.extraPPperc += s.getInfoValueF(this, 0) / 100f;
			}
		}
		if((s = this.getSkill(122)) != null)
		{
			this.extraMPperc += (s.getInfoValueF(this, 1)/100f)*ArkaClass.getCGFreeCards(this);
		}
	}

	public void executeJump() {
		if (this.haveBuff(7) != -1) {
			// this.removeBuff(7);
		} else {
			if (this.grounded) {
				this.velocity.y = this.jumpHeight;
				for(int i = 0;i < 3;i++)
				{
					try
					{
						Vector2 pos = new Vector2(this.position.x + 8 + Main.vectorNoise(4).x, this.position.y + Main.vectorNoise(4).y);
						GameMap g = Constant.getPlayerMap(this.whoAmI);
						Color color = g.map[Main.clamp(0, (int)Math.floor((pos.y/64f)-1), g.height-1)][Main.clamp(0, (int)Math.floor((pos.x/64f)), g.width-1)].color;
						Particle p = Particle.Create(pos,
								new Vector2(this.velocity.x * -0.05f, 60f + Main.vectorNoise(10).y), 2, color, -2f, 1f, 1f);
						p.drawBack = true;
					} catch(Exception ex) {}
				}
				for(int i = 0;i < 3;i++)
				{
					try
					{
						Vector2 pos = new Vector2(this.position.x + this.width - 8 + Main.vectorNoise(4).x, this.position.y + Main.vectorNoise(4).y);
						GameMap g = Constant.getPlayerMap(this.whoAmI);
						Color color = g.map[Main.clamp(0, (int)Math.floor((pos.y/64f)-1), g.height-1)][Main.clamp(0, (int)Math.floor((pos.x/64f)), g.width-1)].color;
						Particle p = Particle.Create(pos,
								new Vector2(this.velocity.x * -0.05f, 60f + Main.vectorNoise(10).y), 2, color, -2f, 1f, 1f);
						p.drawBack = true;
					} catch(Exception ex) {}
				}
			}
		}
	}

	public void executeRoll(int rollDir) {
		if (rollDir == 1) {
			this.velocity.x = this.maxSpeed * 1.5f;
			this.velocity.y = 500;
			this.rolling = true;
			this.direction = 1;
			this.resetAnim();
			this.attacking = false;
			this.attacking2 = false;
			this.stamina -= (this.maxStamina * Math.min((0.1f * Math.max(this.exhaust, 1)), 0.3f));
			this.timeForStamina = 3f;
			this.exhaust++;
		} else {
			this.velocity.x = -this.maxSpeed * 1.5f;
			this.velocity.y = 500;
			this.rolling = true;
			this.direction = -1;
			this.resetAnim();
			this.attacking = false;
			this.attacking2 = false;
			this.stamina -= (this.maxStamina * Math.min((0.1f * Math.max(this.exhaust, 1)), 0.3f));
			this.timeForStamina = 3f;
			this.exhaust++;
		}
		for(int i = 0;i < 3;i++)
		{
			try
			{
				Vector2 pos = new Vector2(this.position.x + 8 + Main.vectorNoise(4).x, this.position.y + Main.vectorNoise(4).y);
				GameMap g = Constant.getPlayerMap(this.whoAmI);
				Color color = g.map[Main.clamp(0, (int)Math.floor((pos.y/64f)-1), g.height-1)][Main.clamp(0, (int)Math.floor((pos.x/64f)), g.width-1)].color;
				Particle p = Particle.Create(pos,
						new Vector2(this.velocity.x * -0.05f, 60f + Main.vectorNoise(10).y), 2, color, -2f, 1f, 1f);
				p.drawBack = true;
			} catch(Exception ex) {}
		}
		for(int i = 0;i < 3;i++)
		{
			try
			{
				Vector2 pos = new Vector2(this.position.x + this.width - 8 + Main.vectorNoise(4).x, this.position.y + Main.vectorNoise(4).y);
				GameMap g = Constant.getPlayerMap(this.whoAmI);
				Color color = g.map[Main.clamp(0, (int)Math.floor((pos.y/64f)-1), g.height-1)][Main.clamp(0, (int)Math.floor((pos.x/64f)), g.width-1)].color;
				Particle p = Particle.Create(pos,
						new Vector2(this.velocity.x * -0.05f, 60f + Main.vectorNoise(10).y), 2, color, -2f, 1f, 1f);
				p.drawBack = true;
			} catch(Exception ex) {}
		}
		this.timeSinceLastAttack = 9999f;
		this.addBuff(21, 4f, this);
	}

	public boolean canRoll() {
		if (!this.canMove())
			return false;

		if (!this.grounded)
			return false;

		if (this.carryingGoblin)
			return false;

		int buff;
		if ((buff = this.haveBuff(21)) != -1 && this.buffs[buff].stacks >= 3)
			return false;

		return true;
	}

	public void onEnterHitBox(Monster m) {
		if (this.haveBuff(68) >= 0 && m.haveBuff(69) >= 0) {
			Skill s = this.getSkill(69);
			float damage = 0;
			if (s == null)
				return;

			damage = s.getInfoValueF(this, 0);
			boolean kill = m.hurtDmgVar((int) damage, this.direction, 0f, this.canCritical(m),
					Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_DAGGER, this);
			m.velocity.x = this.directionByVelocity() * 1800f;
			m.velocity.y = 1000f;
			m.Stun(1f, this);
			if(kill)
			{
				s.cdf = 0f;
			}
			this.removeBuff(68);
			Main.doEarthquake(15);
			this.removeBuff(26);
			this.setCustomDisacceleration(0.25f, 0f);
			this.blockAttack(0f);
			this.blockMovement(0f);
			this.setInvincible(1.5f);
			this.playAnim2(Content.psa[15], 5, 1, false, false, true);
			this.playAnim(Content.psa[14], 5, 1, false, false, true);

			this.velocity = Vector2.Zero.cpy();
		}
		if (this.haveBuff(25) >= 0 && m.haveBuff(45) >= 0) {
			Skill s = this.getSkill(30);
			float damage = 0;
			if (s == null)
				return;

			damage = s.getInfoValueF(this, 0);
			m.hurtDmgVar((int) damage, this.direction, 3f, this.canCritical(m),
					Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_DAGGER, this);
			this.removeBuff(25);
			Main.doEarthquake(15);
			this.removeBuff(26);
			this.setCustomDisacceleration(0.25f, 0f);
			this.blockAttack(0f);
			this.blockMovement(0f);
			this.setInvincible(1.5f);
			for(int i = 0;i < 25;i++)
			{
				float angle = this.velocity.angle() + (i-12.5f + MathUtils.random(-1f, 1f)) * 5f;
				float sin = (float)Math.sin(angle * Math.PI/180f);
				float cos = (float)Math.cos(angle * Math.PI/180f);
				float s2 = MathUtils.random(150, 250);
				Vector2 vel = new Vector2(cos * s2, sin * s2);
				Vector2 origin = this.velocity.cpy().nor().scl(48f).add(this.Center());
				Particle p = Particle.Create(origin.cpy().add(MathUtils.random(-10, 10), MathUtils.random(-10, 10)), vel, 2, new Color(0f, 0f, 0f, 0f), 0f, 1f, MathUtils.random()/2 + 1f);
				p.setLight(32, Color.PURPLE);
			}
			if (this.inventory[Constant.ITEMSLOT_LEFT] != null && this.inventory[Constant.ITEMSLOT_LEFT].isMelee()) {
				this.playAnim(this.inventory[Constant.ITEMSLOT_LEFT].getAttackTexture(),
						this.inventory[Constant.ITEMSLOT_LEFT].attackFrames, 1, (this.getLeft().overrideAttack == Constant.OVERRIDEATTACK_ROTATE || this.getLeft().overrideAttack == Constant.OVERRIDEATTACK_BIGROTATE), false, true);
				this.customAnimFrame = this.getLeft().hurtFrame;
			} else
				this.playAnim(Content.psa[3], 6, 1, false, false, true);

			this.velocity = Vector2.Zero.cpy();
		}
		if (this.haveBuff(40) != -1) {
			Skill s = this.getSkill(12);
			if (s != null) {
				this.removeBuff(40);
				this.velocity.x = 0;
				Vector2 posOffset = s.mouseOnCast.cpy().sub(s.posOnCast.cpy());
				float angle = Main.angleBetween(Vector2.Zero.cpy(), posOffset);

				float dmg = s.getInfoValueI(this, 0);
				boolean hitSomeone = false;
				Polygon shieldBox = new Polygon(new float[] { 0, 0, 80, 0, 80, this.height + 24, 0, this.height + 24 });
				shieldBox.setOrigin(0, (this.height + 24) / 2);
				shieldBox.setPosition(this.Center().x, this.position.y - 12);
				shieldBox.rotate(angle);
				for (Monster me : Constant.getMonsterList(false)) {
					if (me.sameMapAs(this)) {
						if (Intersector.overlapConvexPolygons(shieldBox, Main.rectToPoly(me.hitBox()))) {
							me.hurtDmgVar((int) (dmg), this.direction, 2f, this.canCritical(me),
									Constant.DAMAGETYPE_PHYSICAL, this);
							float time = s.valueByLevel(1, 1, 1, 1, 2);
							me.Stun(time, this);
							hitSomeone = true;
						}
					}
				}
				if (hitSomeone) {
					for (int i = 0; i < 45; i++) {
						Vector2 pos = new Vector2(32, 0).rotate(angle).add(this.Center());
						Vector2 vel = new Vector2(MathUtils.random(100, 300), MathUtils.random(-100, 100));
						vel.rotate(angle);
						Particle.Create(pos, vel, 6, Color.GRAY, 0f, 1f, MathUtils.random(0.5f, 1f));
					}
				}
			}
		}
		if (this.haveBuff(49) != -1) {
			m.hurtDmgVar(1000, 0, 0, this.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, this);
			this.velocity.x = 0f;
			this.removeBuff(49);
			m.velocity.x = 0;
			m.velocity.y = 1500;
			m.Stun(3f, this);
		}
		if (this.haveBuff(51) != -1) {
			m.hurtDmgVar(1000, this.directionByVelocity(), 1f, this.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, this);
			m.Stun(1f, this);
		}

		if (this.ragingDash > 0) {
			Skill s = this.getSkill(13);
			if (s == null)
				return;

			int damage = s.getInfoValueI(this, 1) + s.getInfoValueI(this, 0);
			m.hurtDmgVar(damage, (this.Center().x > m.Center().x ? -1 : 1), 1f, false, Constant.DAMAGETYPE_PHYSICAL,
					this);
		}
	}

	public void applyBuffEffects(Buff buff) {
		if (buff.id == 8) {
			this.attackSpeed += 0.02f * buff.stacks;
		} else if (buff.id == 9 && buff.stacks >= 5) {
			this.doubleOnHit = true;
		} else if (buff.id == 10) {
			Skill s = this.getSkill(10);
			if(s != null)
			{
				this.attackSpeed += s.getInfoValueF(this, 0)/100f;
			}
		} else if (buff.id == 19) {
			Item item = this.getEquippedItem(52);
			if (item != null)
				this.attackSpeed += (item.getInfoValueF(0) / 100f) * buff.stacks;
		} else if (buff.id == 23) {
			if (buff.ticks % 60 == 0) {
				Monster target = null;
				float lowestPercent = 99999f;
				for (Monster m : Constant.getMonsterList(false)) {
					float perc = m.health / m.maxHealth;
					if (m.active && m.sameMapAs(this) && m.Center().dst(this.Center()) < 1000f
							&& perc < lowestPercent) {
						target = m;
					}
				}
				if (target != null) {
					Projectile p = Projectile.Summon(29, target.Center().add(0, 200), Vector2.Zero, 0, 5f, this);
					if (MathUtils.randomBoolean())
						p.mirrored = true;

					Skill s = this.getSkill(24);
					float dmg = 0f;
					if (s != null) {
						dmg = s.valueByLevel(80, 120, 150, 200, 250) + this.getMP() * 0.75f + this.getPP() * 0.1f;
					}
					target.hurtDmgVar((int) dmg, 0, 0f, false, Constant.DAMAGETYPE_ENERGY, this);
				}
			}
		} else if (buff.id == 36) {
			Projectile dest = null;
			for (Projectile p : Constant.getProjectileList()) {
				if (p.type == 45 && p.active && p.owner == this) {
					dest = p;
					break;
				}
			}
			if (dest != null) {
				this.addBuff(26, 0.01f, this);
				this.setCustomDisacceleration(0.01f, 1f);
				this.blockMovement(0.01f);
				float angle = Main.angleBetween(this.Center(), dest.Center());
				Vector2 vel = new Vector2(3000f, 0f).rotate(angle);
				this.velocity = vel;
				this.direction = this.directionByVelocity();
			}
		} else if (buff.id == 40) {
			this.setCustomDisacceleration(0.02f, 1f);
			this.velocity.x = this.maxSpeed * this.direction * 2;
		} else if (buff.id == 41) {
			this.attackSpeed += 0.1f * buff.stacks;
		} else if (buff.id == 42) {
			this.attackSpeed += 0.3f * buff.stacks;
		} else if (buff.id == 44) {
			Skill s = this.getSkill(60);
			if (s != null) {
				float ats = s.valueByLevel("0.6/0.7/0.8/0.9/1");
				float mvs = s.valueByLevel("0.3/0.35/0.4/0.45/0.5");
				this.attackSpeed += ats;
				this.maxSpeed += this.maxSpeed * mvs;
			}
		} else if (buff.id == 46) {
			Skill s = this.getSkill(72);
			if (s != null) {
				this.attackSpeed += s.getInfoValueF(this, 0) / 100f;
				this.maxSpeed += this.maxSpeed * (s.getInfoValueF(this, 1) / 100f);
				this.lifeSteal += s.getInfoValueF(this, 2) / 100f;
				this.cooldownAcceleration += 1f;
			}
		} else if (buff.id == 49) {
			this.velocity.x = this.maxSpeed * 2 * this.direction;
		} else if (buff.id == 54) {
			this.cooldownAcceleration += buff.stacks;
		} else if (buff.id == 55) {
			for (int i = 0; i < Constant.ELEMENTCOLORNAME.length; i++)
				this.extraElementDamage[i] += 0.77f;
		}
		else if(buff.id == 57)
		{
			this.attackSpeed += 0.8f;
		}
		else if(buff.id == 62)
		{
			Skill s = this.getSkill(113);
			if(s != null)
			{
				this.cooldownAcceleration += s.getInfoValueF(this, 0)/100f;
				this.maxSpeed += (this.maxSpeed * s.getInfoValueF(this, 1) / 100f);
			}
		}
		else if(buff.id == 65)
		{
			Skill s = this.getSkill(115);
			if(s != null)
			{
				this.cooldownAcceleration += 1f;
				this.manaRegen += s.getInfoValueF(this, 0);
				this.addBuff(63, 1f, this);
			}
		}
		else if(buff.id == 66)
		{
			Item i = this.getEquippedItem(165);
			if(i != null)
			{
				this.manaRegen *= (1 + (buff.stacks*i.getInfoValueF(0)/100f));
			}
		}
		else if(buff.id == 70)
		{
			this.healthRegen += this.maxHealth/10f;
			this.manaRegen += this.maxMana/10f;
		}
		else if(buff.id == 71)
		{
			Skill s = this.getSkill(10);
			if(s != null)
				this.attackSpeed += s.getInfoValueF(this, 0)/100f;
		}
		if (buff != null)
			buff.ticks++;
	}

	public void preDraw(SpriteBatch batch) {
		if (this.classType == ArkaClass.CASSINOGAMBLER) {
			for (int i = 0; i < ArkaClass.getMaxCGCards(this); i++) {
				if (this.isCardAvailable[i]) {
					int sin = (int) ((Constant.gameTick() + (360 / ArkaClass.getMaxCGCards(this)) * i) % 360);
					double sinV = Math.sin(sin * Math.PI / 180);
					if (sin >= 90 && sin <= 270) {
						Sprite sprite = new Sprite(Content.psa[27]);
						float spawnHeight = 0;
						int timeCard = this.ticksSinceCardUse[i];
						if (timeCard < 120) {
							// float s = (float)Math.sin((timeCard*0.75f) * Math.PI/180f);
							float s = (float) Math.pow(0.95f, timeCard);
							spawnHeight = (s) * 1000;
						}
						Vector2 pos = ArkaClass.getCGCardPosition(this, i);
						sprite.setPosition(pos.x, pos.y + spawnHeight - 16);
						sprite.setSize(24, 32);
						int frame = Math.min((int) ((Math.abs(sinV)) * 10), 9);
						sprite.setRegion(0, frame * 32, 24, 32);
						sprite.setOriginCenter();
						sprite.rotate((float) (sinV * 10));
						sprite.draw(batch);
					}
				}
			}
		}
		
		if(this.getAnim() == Constant.ANIMATION_ATTACKING && this.inventory[Constant.ITEMSLOT_LEFT] != null)
		{
			boolean attacking = !this.isWearingShield() ? this.attacking : this.attacking2;
			if(attacking && this.inventory[Constant.ITEMSLOT_LEFT].isDoubleAnimation())
			{
				this.inventory[Constant.ITEMSLOT_LEFT].runningDiagAttack = true;
				this.inventory[Constant.ITEMSLOT_LEFT].draw(batch, this, 0);
				this.inventory[Constant.ITEMSLOT_LEFT].runningDiagAttack = false;
			}
		}
		if(this.getAnim() == Constant.ANIMATION_ATTACKING && this.inventory[Constant.ITEMSLOT_RIGHT] != null)
		{
			boolean attacking = !this.isWearingShield() ? this.attacking2 : this.attacking;
			if(attacking && this.inventory[Constant.ITEMSLOT_RIGHT].isDoubleAnimation())
			{
				this.inventory[Constant.ITEMSLOT_LEFT].runningDiagAttack = true;
				this.inventory[Constant.ITEMSLOT_RIGHT].draw(batch, this, 1);
				this.inventory[Constant.ITEMSLOT_LEFT].runningDiagAttack = false;
			}
		}

		if (carryingGoblin) {
			Sprite sprite = new Sprite(Content.goblinWithStatue);
			sprite.setPosition(this.Center().x - sprite.getWidth() / 2 - 44 * this.direction,
					this.getHeadPosition().y - 2);
			sprite.setFlip(this.direction == -1, false);
			sprite.draw(batch);
		}
	}

	public void postDraw(SpriteBatch batch) {
		if (ragingDash > 0) {
			Sprite sprite = new Sprite(Content.ragingDash);
			int width = (int) sprite.getWidth();
			int height = (int) sprite.getHeight() / 12;
			sprite.setRegion(0, (Constant.gameTick() / 3 % 12) * 160, width, height);
			sprite.setSize(width, height);
			sprite.setPosition(this.Center().x - width / 2, this.Center().y - height / 2);
			sprite.flip(this.direction == -1, false);
			sprite.setOriginCenter();
			sprite.draw(batch);
		}
		if (this.classType == ArkaClass.CASSINOGAMBLER) {
			for (int i = 0; i < ArkaClass.getMaxCGCards(this); i++) {
				if (this.isCardAvailable[i]) {
					int sin = (int) ((Constant.gameTick() + (360 / ArkaClass.getMaxCGCards(this)) * i) % 360);
					double sinV = Math.sin(sin * Math.PI / 180);
					if (!(sin >= 90 && sin <= 270)) {
						Sprite sprite = new Sprite(Content.psa[27]);
						float spawnHeight = 0;
						int timeCard = this.ticksSinceCardUse[i];
						if (timeCard < 120) {
							// float s = (float)Math.sin((timeCard*0.75f) * Math.PI/180f);
							float s = (float) Math.pow(0.95f, timeCard);
							spawnHeight = (s) * 1000;
						}
						Vector2 pos = ArkaClass.getCGCardPosition(this, i);
						sprite.setPosition(pos.x, pos.y + spawnHeight - 16);
						sprite.setSize(24, 32);
						int frame = Math.min((int) ((Math.abs(sinV)) * 10), 9);
						sprite.setRegion(0, frame * 32, 24, 32);
						sprite.setOriginCenter();
						sprite.rotate((float) (sinV * 10));
						sprite.draw(batch);
					}
				}
			}
		}
		float multiplier = Main.camera.zoom;
		if (Main.shouldNet()) {
			Main.prettyFontDraw(batch, this.name, this.Center().x,
					this.position.y + this.height + 48 * multiplier, 0.5f * multiplier, Color.WHITE, Color.BLACK, 1f,
					true, 0);
			int colorId = Math.min(this.level / 20, 5);
			Main.prettyFontDraw(batch, "« " + this.getClassName() + " »", this.Center().x,
					this.position.y + this.height + 32 * multiplier, 0.5f * multiplier, Constant.QUALITYCOLOR[colorId],
					Color.BLACK, 1f, true, 0);
		}
		boolean channeling = false;
		int channelTicks = 0;
		int channelMax = 0;
		int channelMaxMax = 0;
		for (Skill s : this.skills) {
			if (s != null && s.channeling) {
				channeling = true;
				channelTicks = s.channelTicks;
				channelMax = s.maxChannelTime;
				channelMaxMax = s.maxChannelHoldTime;
			}
		}
		if (!channeling) {
			Skill s = this.recallSkill;
			if (s != null && s.channeling) {
				channeling = true;
				channelTicks = s.channelTicks;
				channelMax = s.maxChannelTime;
				channelMaxMax = s.maxChannelHoldTime;
			}
		}
		if (channeling) {
			Sprite sprite = new Sprite(Content.black);
			sprite.setPosition(this.position.x - 2, this.position.y - 14 * multiplier);
			sprite.setSize(this.width + 4f, 12f);
			sprite.draw(batch);
			sprite = new Sprite(Content.channelBar);
			sprite.setPosition(this.position.x, this.position.y - 12 * multiplier);
			sprite.setSize(((float) Math.min(channelTicks, channelMax) / (float) channelMax) * this.width, 8f);
			sprite.draw(batch);
			if (channelTicks > channelMax) {
				sprite = new Sprite(Content.channelBar);
				sprite.setPosition(this.position.x, this.position.y - 12 * multiplier);
				sprite.setColor(new Color(1f, 0f, 0f, 1f));
				sprite.setSize(((float) Math.min(channelTicks - channelMax, channelMaxMax - channelMax)
						/ (float) (channelMaxMax - channelMax)) * this.width, 8f);
				sprite.draw(batch);
			}
		}
	}

	private void ResetStats(float delta) {
		if(!this.active)
			return;
		
		for (int i = 0; i < 30; i++) {
			if (this.buffs[i] != null) {
				this.buffs[i].timeLeft -= delta;
				if (this.buffs[i].timeLeft <= 0f || (this.buffs[i].stacks <= 0 && this.buffs[i].canStack)) {
					this.buffs[i].onEnd(this);
					this.buffs[i] = null;
				}
			}
		}

		this.healthRegen = 1.2f + 0.05f * this.getVitality();
		this.manaRegen = 1.2f + 0.1f * this.getIntelligence();
		this.critMult = 2f + this.getLethality() * 0.0025f;
		this.lastMonsterHitTicks++;

		this.timeForStamina -= delta;
		this.customDisaccelTime -= delta;
		this.untargetable -= delta;
		this.extraPPperc = 1f;
		this.extraMPperc = 1f;
		this.extraPP = 0;
		this.forceDashPosture--;
		this.extraMP = 0;
		this.manaCostMult = 1f;
		this.maxMobs = 5;
		this.cooldown-=delta;
		
		int trigger = 600;
		if(this.myMapY < 0)
		{
			trigger = 180+(int) (420*(this.Center().y/(Constant.getPlayerMap(this.whoAmI).height*Tile.TILE_SIZE)));
		}
		trigger *= (1-Biome.getRelativeDifficulty(Constant.getPlayerMap(this.whoAmI).id, this.myMapX))*0.75f + 0.25f;
		
		boolean dialogRestricted = false;
		if(this.nearNPC())
		{
			dialogRestricted = true;
			this.lastMobSpawn = 0;
		}

		int targetingMonsters = 0;
		for(Monster m : Constant.getMonsterList(false))
		{
			if(m.getTarget() == this)
			{
				targetingMonsters++;
			}
		}
		
		if(!dialogRestricted)
		{
			this.lastMobSpawn++;
			if(this.velocity.len() >= 200)
				lastMobSpawn++;
		}
		
		if(targetingMonsters < this.maxMobs && !dialogRestricted && this.lastMobSpawn > trigger || (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.J)))
		{
			ArrayList<Integer> list = Constant.getPlayerMap(this.whoAmI).randomrespawns;
			if(list.size() > 0)
			{
				boolean spawned = false;
				int tests = 0;
				do
				{
					tests++;
					int id = list.get(MathUtils.random(0, list.size()-1));
					Monster t = new Monster();
					t.id = id;
					t.Reset(true);
					if((!t.boss || MathUtils.random(100f) <= 3f) && t.canSpawn())
					{
						float extraX = 2000f * MathUtils.randomSign();
						Vector2 pos = this.Center().add(new Vector2(extraX, 0f));
						Monster m = Monster.Create(pos, id, this.myMapX, this.myMapY);
						m.preUpdate(delta);
						if(m.canFly)
						{
							m.position.y += 400;
						}
						m.position = Constant.getPlayerMap(this.whoAmI).getNextestFreeSpace(m.position.x, m.position.y, m.width, m.height, 1, 1).scl(64f);
						if(m.Center().dst(this.Center()) < 1900)
						{
							extraX *= -1f;
							pos = this.Center().add(new Vector2(extraX, 0f));
							m.position = Constant.getPlayerMap(this.whoAmI).getNextestFreeSpace(pos.x, pos.y, m.width, m.height, 1, 1).scl(64f);
						}
						m.myMapX = this.myMapX;
						m.myMapY = this.myMapY;
						m.target = this.whoAmI;
						spawned = true;
						if(Main.debug)
							System.out.println("Spawned a " + m.name);
					}
				}while(!spawned && tests < 3);
			}
			this.lastMobSpawn = 0;
		}
		this.cooldownAcceleration = 1f;
		for (int i = 0; i < 20; i++) {
			this.ticksSinceCardUse[i]++;
		}
		this.attackBlockTime -= delta;
		this.movementBlockTime -= delta;
		this.skillSlotAvailable = this.classType.skillSlots;
		this.ticksFromLastAnimation1++;
		this.ticksFromLastAnimation2++;
		if (this.timeForStamina <= 0f) {
			this.stamina++;
			this.exhaust = 0;
		}

		if (this.stamina > this.maxStamina)
			this.stamina = this.maxStamina;

		if (this.stamina < 0)
			this.stamina = 0;

		this.maxStamina = 30 + (int) Math.floor(this.getAgility() / 3);
		this.maxHealthPerc = 1f + this.getVitality() * 0.002f;
		this.lifeSteal = 0f;
		this.superVision = (Main.var[5] == 4);
		this.invincible -= delta;
		this.attackSpeed = 1f + Main.var[6] * 0.1f + this.getAgility() * 0.0075f;
		this.overallMagicDamage = 1f;
		for (int i = 0; i < this.extraElementDamage.length; i++) {
			this.extraElementDamage[i] = 0f;
		}
		this.acceleration = 120f;
		this.disacceleration = 0.8f;
		this.ticksFromLastSkill++;

		this.timeSinceLastAttack += delta;

		boolean alreadyUp = false;
		while (this.experience >= this.nextLevelExp() && this.level < 100) {
			this.levelUp(alreadyUp);
			alreadyUp = true;
		}
		if(this.level == 100)
		{
			this.experience = this.nextLevelExp();
		}
		if (customDisaccelTime < 0f) {
			if (!this.grounded && !this.canMove())
				this.disacceleration = 0.985f;
			else if (this.dead)
				this.disacceleration = 0.95f;
			if (this.rolling || this.ragingDash > 0)
				this.disacceleration = 1f;
		} else {
			this.disacceleration = this.customDisaccel;
		}

		this.criticalChance = 5f + (this.getLuck() * 0.4f) + this.getLethality() * 0.15f;
		this.maxSpeed = 700f + this.getAgility();
		this.jumpHeight = 1000f;
		this.walking = false;
		this.maxHealth = 1;

		this.estrength = 0;
		this.eagility = 0;
		this.elethality = 0;
		this.eintelligence = 0;
		this.eluck = 0;
		this.evitality = this.getStrenght() / 4;

		if (this.classType == ArkaClass.KNIGHT)
			this.impactReduction = (int) (this.level * 0.4);

		this.rollBlocksProjectiles = false;
		if (this.getSkill(37) != null)
			this.rollBlocksProjectiles = true;

		this.ragingDash--;
		if(this.ragingDash > 0)
		{
			if(this.blocksAhead() > 0)
			{
				this.ragingDash = 0;
				this.customAnim = null;
				this.customAnim2 = null;
			}
		}

		this.skills[10] = null;

		this.skillTicksLeft--;
		if (this.skillTicksLeft <= 0) {
			this.skillCasting = false;
			this.skillOnCast = -1;
		}

		for (int i = Constant.ITEMSLOT_OFFSET; i <= Constant.ITEMSLOT_MAX; i++) {
			if (this.inventory[i] != null)
				this.inventory[i].cdf--;
		}

		if (this.health <= 0 && !this.dead) {
			die();
		}

		for (int i = 0; i < 3; i++) {
			if (this.hotBar[i] > 0 && this.getItemQuantity(this.hotBar[i]) <= 0) {
				this.hotBar[i] = -1;
			}
		}

		this.doubleOnHit = false;
		this.cooldownReduction = 1f;
		if (Main.var[8] > 10) {
			this.cooldownReduction = 0.05f;
		}

		if (this.whoAmI == Main.me)
			this.mousePos = Main.mouseInWorld().cpy();
	}

	public void die() {
		this.velocity.y = 600;
		this.velocity.x = -800 * this.direction;
		this.dead = true;
		Main.dialog = null;
		this.onDeath();
		this.resetAnim();
	}

	private void levelUp(boolean alreadyUp) {
		LivingText.Create(this.Center(), new Vector2(MathUtils.random(-300, 300), MathUtils.random(500, 1000)),
				"LEVEL UP!", new Color(0.5f, 1f, 1f, 1f), 2f, 4, true, true);
		this.experience -= this.nextLevelExp();
		this.level++;
		this.characterPoints += 5;
		if (!alreadyUp) {
			for (int j = 0; j < 2; j++) {
				Color c = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);
				for (int i = -2; i <= 2; i++) {
					Particle p = Particle.Create(new Vector2(this.Center().x + i * 8, this.position.y + 16),
							Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
		}
		int newcplevel = (int) Main.clamp(1, (this.level/20)+1, 5);
		for (Skill s : this.learnedSkills) {
			if (s.classPassive)
				s.level = newcplevel;
		}
		for (Item i : this.inventory) {
			if (i != null && i.itemclass != Constant.ITEMCLASS_MATERIAL)
				i.reforge(this.level);
		}
		for (Item i : this.vanity) {
			if (i != null && i.itemclass != Constant.ITEMCLASS_MATERIAL)
				i.reforge(this.level);
		}
	}

	public void heal(float quant) {
		this.healSilent(quant);

		if (quant > 1) {
			LivingText.Create(this.position.cpy().add(this.width / 2, this.height),
					new Vector2(MathUtils.random(-100, 100), MathUtils.random(200, 600)), String.valueOf((int) quant),
					Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_GOOD], 0.5f, 1.5f, true, false, this);
		}
	}

	public void healSilent(float quant) {
		this.health += quant;
		if (this.health > this.getMaxHealthEx())
			this.health = this.getMaxHealthEx();
	}
	
	public void healEx(final float quant)
	{
		final Player player = this;
		Event e = new Event(1) {
			@Override
			public void function()
			{
				player.heal(quant);
			}
		};
		Main.scheduledTasks.add(e);
	}

	public void onDeath() {
		if (this.whoAmI == Main.me) {
			Main.SwitchMap(0, false, false, null);
			Main.projectile.clear();
			Main.particle.clear();
			this.skills[0].casts = 0;
			this.skills[0].cdf = 0.1f;
			this.skills[1].cdf = 0.1f;
			SavedWorld.saved = false;
		}
	}

	public void resetAnim2() {
		this.animFrame2 = 0;
		this.animFrameCounter2 = 0;
		if (this.inventory[Constant.ITEMSLOT_RIGHT] != null) {
			this.invertedSwing2 = (this.inventory[Constant.ITEMSLOT_RIGHT].canFlipAttack
					&& MathUtils.random(0, 1) == 0);
			if (Main.var[5] == 3) {
				int calcDir = (this.mousePos.cpy().x > this.Center().x ? 1 : -1);
				this.invertedSwing2 = (calcDir == -1 ? true : false);
			}
		}
		this.atkMouseDif = this.mousePos.cpy().sub(this.Center());
		this.resetGameTick = Constant.gameTick();
	}

	public void resetCustomAnim() {
		this.customAnim = null;
		this.customAnimBlockAttack = false;
		this.customAnimBlockMove = false;
		this.customAnimFrames = 1;
		this.customAnimSkipper = 0;
		this.customAnimShouldRotate = false;
		this.customAnimFrame = 0;
		this.customAnimFrameCounter = 0;
		this.customAnimShouldFreeze = true;
	}

	public void resetCustomAnim2() {
		this.customAnim2 = null;
		this.customAnim2BlockAttack = false;
		this.customAnim2BlockMove = false;
		this.customAnim2Frames = 1;
		this.customAnim2Skipper = 0;
		this.customAnim2ShouldRotate = false;
		this.customAnim2Frame = 0;
		this.customAnim2FrameCounter = 0;
		this.customAnim2ShouldFreeze = true;
	}

	public void resetAnim() {
		this.animFrame = 0;
		this.animFrameCounter = 0;
		if (this.inventory[Constant.ITEMSLOT_LEFT] != null) {
			this.invertedSwing = (this.inventory[Constant.ITEMSLOT_LEFT].canFlipAttack
					&& MathUtils.random(0, 1) == 0);
			if (Main.var[5] == 3) {
				int calcDir = (this.mousePos.cpy().x > this.Center().x ? 1 : -1);
				this.invertedSwing = (calcDir == -1 ? true : false);
				if (Main.var[4] == 1) {
					this.invertedSwing = this.attackDir == 1 ? false : true;
				}
			}
		}
		this.atkMouseDif = this.mousePos.cpy().sub(this.Center());
		this.resetGameTick = Constant.gameTick();
		this.customAnim = null;
		this.customAnimBlockAttack = false;
		this.customAnimBlockMove = false;
		this.customAnimFrames = 1;
		this.customAnimSkipper = 0;
		this.customAnimShouldRotate = false;
		this.customAnim2 = null;
		this.customAnim2BlockAttack = false;
		this.customAnim2BlockMove = false;
		this.customAnim2Frames = 1;
		this.customAnim2Skipper = 0;
		this.customAnim2ShouldRotate = false;
	}

	public int getAnim() {
		int a = 0;
		if (this.walking) {
			a = 1;
		}
		if (this.attacking || this.attacking2) {
			a = 3;
		}
		if (this.checkingInventory) {
			a = 2;
		}
		if (this.rolling) {
			a = 4;
		}
		if (this.dancing) {
			a = 5;
		}
		return a;
	}

	public Vector2 getHandPosition() {
		Vector2 fP = new Vector2(this.Center().x + (this.direction == 1 ? -18 : 18), this.position.y + 20);
		if (this.walking) {
			float extraY = 0;
			switch (this.animFrame % 5) {
			case 1: {
				extraY = 4;
				break;
			}
			case 2: {
				extraY = 8;
				break;
			}
			case 3: {
				extraY = 4;
				break;
			}
			case 4: {
				extraY = 4;
				break;
			}
			}
			fP.y += extraY;
		}
		return fP;
	}

	public Vector2 getItemHandOffset() {
		Vector2 fP = Vector2.Zero.cpy();
		Item item = this.inventory[Constant.ITEMSLOT_LEFT];

		if (item != null && (!item.standDrawHand || item.overrideStand == Constant.OVERRIDESTAND_OVERRIDE)) {
			fP.x -= (item.offsetX);
			fP.y -= (item.offsetY);
		}
		return fP;
	}

	public Vector2 getBackHandPosition() {
		Vector2 fP = new Vector2(this.Center().x + (this.direction == -1 ? -18 : 18), this.position.y + 20);
		if (this.walking) {
			float extraY = 0;
			switch (this.animFrame % 5) {
			case 1: {
				extraY = 4;
				break;
			}
			case 2: {
				extraY = 8;
				break;
			}
			case 3: {
				extraY = 4;
				break;
			}
			case 4: {
				extraY = 4;
				break;
			}
			}
			fP.y += extraY;
		}
		return fP;
	}

	public Vector2 getBodyPosition() {
		Vector2 fP = new Vector2(this.Center().x + (this.direction == 1 ? -18 : -22), this.position.y + 8);
		if (this.ragingDash > 0) {
			fP.x -= 4 * this.direction;
		} else if (this.walking) {
			float extraY = 0;
			switch (this.animFrame % 5) {
			case 0: {
				extraY = 4;
				break;
			}
			case 1: {
				extraY = 4;
				break;
			}
			case 2: {
				extraY = 4;
				break;
			}
			case 3: {
				extraY = 0;
				break;
			}
			case 4: {
				extraY = 0;
				break;
			}
			}
			fP.y += extraY;
		}
		return fP;
	}

	public float getYAnimationOffset() {
		float extraY = 0;
		if (this.walking) {
			switch (this.animFrame % 5) {
			case 0: {
				extraY = 4;
				break;
			}
			case 1: {
				extraY = 4;
				break;
			}
			case 2: {
				extraY = 4;
				break;
			}
			case 3: {
				extraY = 0;
				break;
			}
			case 4: {
				extraY = 0;
				break;
			}
			}
		}
		return extraY;
	}

	public Vector2 getHeadPosition() {
		Vector2 fP = new Vector2(this.Center().x + (this.direction == 1 ? -18 : -22), this.position.y + 36);
		if (this.shouldDashPosture()) {
			fP.y -= 8;
			fP.x = this.Center().x - 10 * this.direction;
			if (this.direction == -1)
				fP.x -= 40;
		} else if (this.rolling) {
			float extraY = 0;
			int frame = this.animFrame;
			if (this.animFrame > 1)
				frame = 1;

			switch (frame) {
			case 0: {
				extraY = -4;
				break;
			}
			case 1: {
				extraY = -8;
				break;
			}
			}
			fP.y += extraY;
		} else if (this.getAnim() == Constant.ANIMATION_INVENTORY) {

			float extraY = 0;
			int frame = this.animFrame;
			if (this.animFrame > 6)
				frame = 6;

			if (frame == 1)
				extraY = -4;
			else if (frame > 1)
				extraY = -8;

			fP.y += extraY;
		} else if (this.walking) {
			float extraY = 0;
			switch (this.animFrame % 5) {
			case 0: {
				extraY = 4;
				break;
			}
			case 1: {
				extraY = 4;
				break;
			}
			case 2: {
				extraY = 4;
				break;
			}
			case 3: {
				extraY = 0;
				break;
			}
			case 4: {
				extraY = 0;
				break;
			}
			}
			fP.y += extraY;
		}
		return fP;
	}

	public float offSetByDirection(float caseLeft, float caseRight) {
		if (this.direction == Constant.DIRECTION_LEFT)
			return caseLeft;
		else
			return caseRight;
	}

	public boolean canMove() {
		return canMove(true);
	}

	public boolean canMove(boolean shallowCheck) {
		if (shallowCheck) {
			if (!AHS.isUp && this.whoAmI == Main.me && (this.checkingInventory || Main.displayDialog || Main.cutscene != null || Main.displayWarps || Main.displayCrafting))
				return false;

			if (this.whoAmI == Main.me && ((Main.blackScreenTime >= -2f && Main.blackScreenTime <= 1f))
					&& Main.isOnline)
				return false;
		}

		if (this.dead)
			return false;

		if (this.rolling)
			return false;

		if (this.isUntargetable())
			return false;

		if (this.customAnim != null && this.customAnimBlockMove)
			return false;

		if (this.customAnim2 != null && this.customAnim2BlockMove)
			return false;

		if (this.haveBuff(7) != -1)
			return false;

		if (this.movementBlockTime > 0f)
			return false;

		if (this.ragingDash > 0)
			return false;

		return true;
	}

	public boolean canAttack() {
		if (!AHS.isUp && this.whoAmI == Main.me && (this.checkingInventory || Main.displayCrafting || Main.displayCharacter || Main.displayDialog || Main.displaySkill))
			return false;

		// System.out.println("No menus open...");

		if (this.dead)
			return false;

		// System.out.println("Player alive...");

		// System.out.println("Valid weapon...");

		if (this.skillCasting)
			return false;

		if (this.channelingSkillBlocksAttack())
			return false;

		// System.out.println("No skills casting...");

		if (this.isUntargetable())
			return false;

		// System.out.println("Player targetable...");

		if (this.attacking && this.animFrame <= this.inventory[Constant.ITEMSLOT_LEFT].hurtFrame)
			return false;

		// System.out.println("Player not attacking...");

		if (this.inventory[Constant.ITEMSLOT_RIGHT] != null
				&& this.inventory[Constant.ITEMSLOT_RIGHT].id == this.inventory[Constant.ITEMSLOT_LEFT].id) {
			int limit = (int) (3 * this.inventory[Constant.ITEMSLOT_RIGHT].cooldownFrames
					/ (this.getAttackSpeed()));
			if (this.inventory[Constant.ITEMSLOT_RIGHT].cdf > limit / 2) {
				return false;
			}
		}

		// System.out.println("Weapon cooled down...");

		if (this.rolling)
			return false;

		// System.out.println("Player not rolling...");

		if (this.inventory[Constant.ITEMSLOT_LEFT].cdf > 0)
			return false;

		// System.out.println("Weapon ready...");

		if (this.customAnim != null && this.customAnimBlockAttack)
			return false;

		if (this.customAnim2 != null && this.customAnim2BlockAttack)
			return false;

		if (this.attackBlockTime > 0f)
			return false;

		// System.out.println("No custom animations...");

		for (Projectile p : Constant.getProjectileList()) {
			if (p.active && p.type == 12) {
				return false;
			}
		}

		// System.out.println("No shield existing...");

		return true;
	}

	public boolean canAttack2() {
		if (this.checkingInventory || (!AHS.isUp && (Main.displayCharacter || Main.displayDialog)))
			return false;

		if (this.dead)
			return false;

		if (this.inventory[Constant.ITEMSLOT_RIGHT] == null
				|| this.inventory[Constant.ITEMSLOT_RIGHT].itemclass == Constant.ITEMCLASS_SHIELD)
			return false;

		if (this.skillCasting)
			return false;

		if (this.channelingSkillBlocksAttack())
			return false;

		if (this.isUntargetable())
			return false;

		if (this.attacking2 && this.animFrame2 <= this.inventory[Constant.ITEMSLOT_RIGHT].hurtFrame)
			return false;

		if (this.inventory[Constant.ITEMSLOT_LEFT] != null
				&& this.inventory[Constant.ITEMSLOT_RIGHT].id == this.inventory[Constant.ITEMSLOT_LEFT].id) {
			int limit = (int) (3 * this.inventory[Constant.ITEMSLOT_LEFT].cooldownFrames / (this.getAttackSpeed()));
			if (this.inventory[Constant.ITEMSLOT_LEFT].cdf > limit / 2) {
				return false;
			}
		}

		if (this.rolling)
			return false;

		if (this.inventory[Constant.ITEMSLOT_RIGHT].cdf > 0)
			return false;

		if (this.customAnim != null && this.customAnimBlockAttack)
			return false;

		if (this.customAnim2 != null && this.customAnim2BlockAttack)
			return false;

		if (this.attackBlockTime > 0f)
			return false;

		for (Projectile p : Constant.getProjectileList()) {
			if (p.active && p.type == 12) {
				return false;
			}
		}

		return true;
	}

	public boolean canUseAttack() {
		if (this.checkingInventory || (!AHS.isUp && (Main.displayCharacter || Main.displayDialog)))
			return false;

		if (this.dead)
			return false;

		if (this.skillCasting)
			return false;

		if (this.channelingSkillBlocksAttack())
			return false;

		if (this.isUntargetable())
			return false;

		if (this.attacking && this.animFrame <= this.inventory[Constant.ITEMSLOT_LEFT].hurtFrame)
			return false;

		if (this.rolling)
			return false;

		if (this.customAnim != null && this.customAnimBlockAttack)
			return false;

		if (this.customAnim2 != null && this.customAnim2BlockAttack)
			return false;

		if (this.attackBlockTime > 0f)
			return false;

		for (Projectile p : Constant.getProjectileList()) {
			if (p.active && p.type == 12) {
				return false;
			}
		}

		return true;
	}

	public boolean canCast(Skill skill) {
		if (this.checkingInventory || (!AHS.isUp && (Main.displayCharacter || Main.displayDialog || Main.displaySkill)))
			return false;

		if (this.dead)
			return false;

		if (skill.id == 8)
			return true;

		if (this.skillCasting)
			return false;

		if (this.isUntargetable())
			return false;
		
		if (skill.waitForBuff > -1 && this.haveBuff(skill.waitForBuff) != -1)
			return false;

		if (skill.freeUse && skill.casts > 0)
			return true;

		if ((this.attacking || this.attacking2)
				&& ((this.inventory[Constant.ITEMSLOT_LEFT] != null
				&& this.animFrame <= this.inventory[Constant.ITEMSLOT_LEFT].hurtFrame)
						|| (this.inventory[Constant.ITEMSLOT_RIGHT] != null
						&& this.animFrame2 <= this.inventory[Constant.ITEMSLOT_RIGHT].hurtFrame))
				&& skill.attackRestricted)
			return false;

		if (this.rolling && skill.attackRestricted)
			return false;

		if (this.customAnim != null && this.customAnimBlockAttack)
			return false;

		if (this.customAnim2 != null && this.customAnim2BlockAttack)
			return false;

		if (this.attackBlockTime > 0f)
			return false;

		if (skill.id == 75 && skill.casts <= 0) {
			Monster target = null;
			for (Monster m : Constant.getMonstersInRange(this, 1500)) {
				if (m.isStunned() && !m.grounded) {
					target = m;
					break;
				}
			}

			if (target == null)
				return false;
		}

		return true;
	}

	public boolean drawBackHand() {
		if (this.customAnim2 != null) {
			return true;
		}

		if (this.getAnim() != Constant.ANIMATION_INVENTORY) {
			if(this.timeSinceLastAttack <= Constant.BACKWEAPON_TIME)
			{
				if(!this.isWearingShield())
				{
					if (this.inventory[Constant.ITEMSLOT_LEFT] != null) {
						if (this.inventory[Constant.ITEMSLOT_LEFT].dualHandedAttack
								&& this.inventory[Constant.ITEMSLOT_LEFT].rotateAfterAttack
								&& this.timeSinceLastAttack < 2) {
							return false;
						}

						if (this.getAnim() == Constant.ANIMATION_ATTACKING) {
							if (this.inventory[Constant.ITEMSLOT_LEFT].dualHandedAttack) {
								return false;
							}
						} else {
							if (this.inventory[Constant.ITEMSLOT_LEFT].dualHanded) {
								return false;
							}
						}
					}
					if (this.inventory[Constant.ITEMSLOT_RIGHT] != null) {
						if (!this.inventory[Constant.ITEMSLOT_RIGHT].standDrawHand
								&& (this.getAnim() == Constant.ANIMATION_STAND
								|| this.getAnim() == Constant.ANIMATION_WALKING)) {
							return false;
						}
					}
				}
			}
			else
			{
				if(!this.isWearingShield())
				{
					if(this.inventory[Constant.ITEMSLOT_LEFT] != null && this.inventory[Constant.ITEMSLOT_LEFT].dualHanded)
					{
						return this.inventory[Constant.ITEMSLOT_LEFT].canGoBack ? true : this.inventory[Constant.ITEMSLOT_LEFT].standDrawHand;
					}
					if(this.inventory[Constant.ITEMSLOT_RIGHT] != null)
					{
						return this.inventory[Constant.ITEMSLOT_RIGHT].canGoBack ? true : this.inventory[Constant.ITEMSLOT_RIGHT].standDrawHand;
					}
				}
				else
				{
					if(this.inventory[Constant.ITEMSLOT_LEFT] != null)
					{
						return this.inventory[Constant.ITEMSLOT_LEFT].canGoBack ? true : this.inventory[Constant.ITEMSLOT_LEFT].standDrawHand;
					}
				}
			}
		}

		if (this.ragingDash > 0) {
			return false;
		}

		if (this.getAnim() == Constant.ANIMATION_DANCE)
			return false;

		return true;
	}

	public boolean drawBHBack() {
		if (this.rolling)
			return false;

		return true;
	}

	public boolean drawHand() {
		if (this.dancing)
			return false;

		if (this.customAnim != null)
			return true;

		if (this.getAnim() != Constant.ANIMATION_INVENTORY) {
			if(this.timeSinceLastAttack < Constant.BACKWEAPON_TIME)
			{
				if (this.isWearingShield())
					return false;

				if (this.inventory[Constant.ITEMSLOT_LEFT] != null) {
					if (this.inventory[Constant.ITEMSLOT_LEFT].rotateAfterAttack && this.timeSinceLastAttack < 2) {
						return false;
					}

					if ((this.getAnim() == Constant.ANIMATION_ATTACKING
							&& this.inventory[Constant.ITEMSLOT_LEFT].overrideAttack == Constant.OVERRIDEATTACK_OVERRIDE)) {
						if (this.inventory[Constant.ITEMSLOT_LEFT].dualHandedAttack) {
							return false;
						}
					} else {
						if (this.inventory[Constant.ITEMSLOT_LEFT].dualHanded
								&& this.inventory[Constant.ITEMSLOT_LEFT].overrideStand == Constant.OVERRIDESTAND_OVERRIDE) {
							return false;
						}
						if (!this.inventory[Constant.ITEMSLOT_LEFT].standDrawHand && !this.attacking) {
							return false;
						}
						if (this.inventory[Constant.ITEMSLOT_LEFT].overrideStand == Constant.OVERRIDESTAND_FRONT
								&& !this.attacking) {
							return false;
						}
					}
				}
			}
			else
			{
				if(this.isWearingShield())
					return false;
				
				if(this.inventory[Constant.ITEMSLOT_LEFT] != null)
				{
					return this.inventory[Constant.ITEMSLOT_LEFT].canGoBack ? true : this.inventory[Constant.ITEMSLOT_LEFT].standDrawHand;
				}
			}
		}

		return true;
	}

	public void UpdateBand() {
		for (int x = 0; x < 2; x++) {
			float interpBT = bandTick[x];
			if (x == 0) {
				interpBT *= -1;
			}
			Vector2 dif = this.position.cpy().sub(this.lastPosition);
			this.myBand[x][0] = new Vector2(this.Center().x + (x == 0 ? -8 : 8) + dif.x,
					this.getHeadPosition().y + 32 + dif.y);
			for (int i = 24; i >= 1; i--) {
				this.myBand[x][i].y += ((interpBT * 2) / i);
				this.myBand[x][i].x += (-10 + Math.abs(this.velocity.x / this.maxSpeed * 10f)) * this.direction;
				this.myBand[x][i] = this.myBand[x][i - 1];
			}
		}
	}

	public int haveBuff(int id) {
		for (int i = 0; i < 30; i++) {
			if (this.buffs[i] != null && this.buffs[i].id == id) {
				return i;
			}
		}
		return -1;
	}

	public int getBuffStacks(int id) {
		int slot = this.haveBuff(id);
		int stacks = 0;
		if (slot != -1) {
			stacks = this.buffs[slot].stacks;
		}
		return stacks;
	}

	public int addBuff(int id, float timeLeft, Entity origin) {
		return addBuff(id, 1, timeLeft, origin);
	}

	public int addBuff(int id, int stacks, float timeLeft, Entity origin) {
		int slot = this.haveBuff(id);
		if (slot != -1) {
			this.buffs[slot].timeLeft = timeLeft;
			this.buffs[slot].lastTimeLeft = timeLeft;
			if (this.buffs[slot].canStack) {
				this.buffs[slot].stacks += stacks;
				if ((this.buffs[slot].maxStacks > 0 && this.buffs[slot].stacks > this.buffs[slot].maxStacks)) {
					this.buffs[slot].stacks = this.buffs[slot].maxStacks;
				}
			}
			if (origin.getClass() == Player.class) {
				Player player = (Player) origin;
				this.buffs[slot].originUid = player.whoAmI;
				this.buffs[slot].originInfo = "player";
			} else if (origin.getClass() == Monster.class) {
				Monster m = (Monster) origin;
				this.buffs[slot].originUid = m.uid;
				this.buffs[slot].originInfo = "monster";
			}
			else if(origin.getClass() == NPC.class) 
			{
				NPC n = (NPC) origin;
				this.buffs[slot].originUid = n.id;
				this.buffs[slot].originInfo = "npc";
			}
		} else {
			for (int i = 0; i < 30; i++) {
				if (this.buffs[i] == null) {
					this.buffs[i] = new Buff();
					this.buffs[i].SetInfos(id, origin);
					this.buffs[i].timeLeft = timeLeft;
					this.buffs[i].lastTimeLeft = timeLeft;
					this.buffs[i].stacks = stacks;
					if ((this.buffs[i].maxStacks > 0 && this.buffs[i].stacks > this.buffs[i].maxStacks)) {
						this.buffs[i].stacks = this.buffs[i].maxStacks;
					}
					if (origin.getClass() == Player.class) {
						Player player = (Player) origin;
						this.buffs[i].originUid = player.whoAmI;
						this.buffs[i].originInfo = "player";
					} else if (origin.getClass() == Monster.class) {
						Monster m = (Monster) origin;
						this.buffs[i].originUid = m.uid;
						this.buffs[i].originInfo = "monster";
					}
					else if(origin.getClass() == NPC.class) 
					{
						NPC n = (NPC) origin;
						this.buffs[i].originUid = n.id;
						this.buffs[i].originInfo = "npc";
					}
					slot = i;
					break;
				}
			}
		}
		return slot;
	}

	public int addBuff(int id, int stacks, int customStackLimit, float timeLeft, Entity origin) {
		int slot = this.haveBuff(id);
		if (slot != -1) {
			this.buffs[slot].timeLeft = timeLeft;
			this.buffs[slot].lastTimeLeft = timeLeft;
			if (this.buffs[slot].canStack) {
				this.buffs[slot].stacks += stacks;
				if ((customStackLimit > 0 && this.buffs[slot].stacks > customStackLimit)) {
					this.buffs[slot].stacks = customStackLimit;
				}
			}
			if (origin.getClass() == Player.class) {
				Player player = (Player) origin;
				this.buffs[slot].originUid = player.whoAmI;
				this.buffs[slot].originInfo = "player";
			} else if (origin.getClass() == Monster.class) {
				Monster m = (Monster) origin;
				this.buffs[slot].originUid = m.uid;
				this.buffs[slot].originInfo = "monster";
			}
			else if(origin.getClass() == NPC.class) 
			{
				NPC n = (NPC) origin;
				this.buffs[slot].originUid = n.id;
				this.buffs[slot].originInfo = "npc";
			}
		} else {
			for (int i = 0; i < 30; i++) {
				if (this.buffs[i] == null) {
					this.buffs[i] = new Buff();
					this.buffs[i].SetInfos(id, origin);
					this.buffs[i].timeLeft = timeLeft;
					this.buffs[i].lastTimeLeft = timeLeft;
					this.buffs[i].stacks = stacks;
					if ((customStackLimit > 0 && this.buffs[i].stacks > customStackLimit)) {
						this.buffs[i].stacks = customStackLimit;
					}
					if (origin.getClass() == Player.class) {
						Player player = (Player) origin;
						this.buffs[i].originUid = player.whoAmI;
						this.buffs[i].originInfo = "player";
					} else if (origin.getClass() == Monster.class) {
						Monster m = (Monster) origin;
						this.buffs[i].originUid = m.uid;
						this.buffs[i].originInfo = "monster";
					}
					else if(origin.getClass() == NPC.class) 
					{
						NPC n = (NPC) origin;
						this.buffs[i].originUid = n.id;
						this.buffs[i].originInfo = "npc";
					}
					slot = i;
					break;
				}
			}
		}
		return slot;
	}

	public void removeBuff(int id) {
		for (int i = 0; i < 30; i++) {
			if (this.buffs[i] != null && this.buffs[i].id == id) {
				this.buffs[i] = null;
			}
		}
	}

	public void hurt(int damage, int direction, float scalar, Entity attacker) {
		if (this.isUntargetable() || Main.var[8] == 3)
			return;

		float initialHealth = this.health;

		// Damage Reduction Here
		if (this.haveBuff(43) != -1) {
			Buff b = this.buffs[this.haveBuff(43)];
			b.stacks += damage;
			Skill s = this.getSkill(60);
			if (s != null) {
				damage = (int) (damage * (1f - s.getInfoValueF(this, 0) / 100f));
			}
		}
		Skill s = this.getEquippedSkill(8);
		if(s != null && this.inventory[Constant.ITEMSLOT_RIGHT] != null && this.inventory[Constant.ITEMSLOT_RIGHT].itemclass == Constant.ITEMCLASS_SHIELD)
		{
			boolean buffed = false;
			for(Projectile p : Constant.getProjectileList())
			{
				if(p.active && p.type == 12 && p.owner == this)
				{
					buffed = true;
					s.totalCasts++;
					s.level = Skill.levelFromCasts(s.totalCasts);
					break;
				}
			}
			damage = (int)(damage * (1f - s.getInfoValueF(this, buffed ? 2 : 1)/100f));
		}
		// ---------------------------------------

		// Post Damage Reduction
		if((this.health - damage)/this.maxHealth < 0.25f && this.haveBuff(74) == -1)
		{
			Item i = this.getEquippedItem(189);
			if(i != null)
			{
				this.addBuff(73, i.getInfoValueI(0), 5f, this);
				this.addBuff(74, 150, this);
				Particle.Create(this.Center(), Vector2.Zero, 23, new Color(0.9f, 0.93f, 0.8f, 1f), 0f, 1f, 1f);
			}
		}
		// ---------------------------------------
		
		int remainingDamage = damage;
		int shieldDamage = 0;
		int healthDamage = 0;
		while (remainingDamage > 0) {
			Buff b = this.getNextShield();
			if (b != null) {
				if (b.stacks > remainingDamage) {
					b.stacks -= remainingDamage;
					shieldDamage += remainingDamage;
					remainingDamage = 0;
				} else {
					remainingDamage -= b.stacks;
					shieldDamage += b.stacks;
					b.stacks = 0;
				}
			} else {
				this.health -= remainingDamage;
				healthDamage += remainingDamage;
				remainingDamage = 0;
			}
		}

		if ((s = this.getEquippedSkill(71)) != null && this.health < this.maxHealth / 2
				&& initialHealth > this.maxHealth / 2) {
			s.applyCast(this);
		}

		if (direction != 0 && this.canMove(false)) {
			this.velocity = new Vector2(400 * direction * scalar, 400);
		}
		if (direction != 0 && attacker.getClass() == Monster.class) {
			Monster m = (Monster) attacker;
			m.addAggroDelay(this, 180);
			this.setInvincible(1f / 3f);
		}
		this.lastHitTick = Constant.gameTick();
	}

	public Buff getNextShield() {
		Buff b = null;
		for (Buff buff : this.buffs) {
			if (buff != null && buff.shield && buff.stacks > 0) {
				b = buff;
				break;
			}
		}
		return b;
	}

	public void hurt2(int damage2, int direction, float scalar, Entity attacker) {
		float damage = damage2 * MathUtils.random(0.875f, 1.125f);
		if (attacker.getClass() == Monster.class) {
			Monster m = (Monster) attacker;
			if (m.haveBuff(28) != -1) {
				Buff buff = m.buffs[m.haveBuff(28)];
				if (buff.originUid >= 0 && buff.originInfo.equalsIgnoreCase("player")) {
					if (((Player) buff.getOrigin()).getSkill(31) != null) {
						damage *= ((Player) buff.getOrigin()).getSkill(31).valueByLevel("0.7/0.65/0.6/0.55/0.5");
					}
				}
			}
		}
		hurt((int) damage, direction, scalar, attacker);
	}

	public boolean canBeHit() {
		if (this.invincible > 0f)
			return false;

		if (this.ragingDash > 0)
			return false;

		if (this.haveBuff(51) != -1 || this.haveBuff(36) != -1 || this.haveBuff(25) != -1)
			return false;

		return true;
	}

	public int lastResetTicks() {
		return Constant.gameTick() - this.resetGameTick;
	}

	public int lastHurtTicks() {
		return Constant.gameTick() - this.lastHitTick;
	}
	
	public void forceCritical()
	{
		this.oldCriticalChance = this.criticalChance;
		this.criticalChance = 1337;
	}
	
	public void restoreCritical()
	{
		this.criticalChance = this.oldCriticalChance;
	}

	public boolean canCritical(Monster m) {
		return this.canCritical(null, -1, m);
	}
	
	public boolean canCritical(int specialFlag, Monster m)
	{
		return this.canCritical(null, specialFlag, m);
	}

	public boolean canCritical(Item item, Monster m)
	{
		return this.canCritical(item, -1, m);
	}
	
	public boolean canCritical(Item item, int specialFlag, Monster m)
	{
		if (item != null) {
			if (item.id == 79) {
				return (m.getActualHealth(true) * 100f) < item.getInfoValueF(0);
			}
		}
		
		int chance = MathUtils.random(0, 100);

		if (this.classType.rootClass == ArkaClass.ROOTCLASS_ROGUE) {
			if ((m.target != this.whoAmI && this.direction != m.direction) || this.haveBuff(27) != -1 || specialFlag == 1) {
				if(this.classType == ArkaClass.ROGUE)
				{
					Skill s = this.getSkill(28);
					if(s != null)
						this.critMult += s.getInfoValueF(this, 0)/100f;
				}
				else if(this.classType == ArkaClass.ARCANETRICKSTER)
				{
					this.addBuff(60, 1f, this);
					for(Skill s : this.skills)
					{
						if(s != null)
						{
							s.cdf -= 2f;
						}
					}
				}
				else if(this.classType == ArkaClass.REAPER)
				{
					Skill s = this.getSkill(121);
					if(s != null)
					{
						if(m.getActualHealth(true) <= s.getInfoValueF(this, 0)/100f)
							m.hurt(m.health, 0, 0, true, Constant.DAMAGETYPE_DEATH, this);
					}
				}
				return true;
			}
		}

		if (chance <= this.criticalChance || this.criticalChance == 1337)
			return true;

		return false;
	}

	public void playAnim(Texture sheet, int frames, int skipper, boolean shouldRotate, boolean blockMove,
			boolean blockAttack, Vector2 centered) {
		this.resetCustomAnim();
		this.customAnim = sheet;
		this.customAnimFrames = frames;
		this.customAnimFrame = 0;
		this.customAnimSkipper = skipper;
		this.customAnimBlockMove = blockMove;
		this.customAnimBlockAttack = blockAttack;
		this.customAnimShouldRotate = shouldRotate;
		this.customAnimOffset = centered.cpy();
		this.customAnimRotation = this.mousePos.cpy().sub(this.Center()).angle();
		this.invertedSwing = false;
		this.timeSinceLastAttack = 9999f;
		if (blockMove && blockAttack) {
			this.setInvincible(Main.ticksToSeconds(frames * (skipper + 1)));
		}
	}

	public void playAnim(Texture sheet, int frames, int skipper, boolean shouldRotate, boolean blockMove,
			boolean blockAttack) {
		playAnim(sheet, frames, skipper, shouldRotate, blockMove, blockAttack, Vector2.Zero.cpy());
	}

	public void playAnim2(Texture sheet, int frames, int skipper, boolean shouldRotate, boolean blockMove,
			boolean blockAttack, Vector2 centered) {
		this.resetCustomAnim2();
		this.customAnim2 = sheet;
		this.customAnim2Frames = frames;
		this.customAnim2Frame = 0;
		this.customAnim2Skipper = skipper;
		this.customAnim2BlockMove = blockMove;
		this.customAnim2BlockAttack = blockAttack;
		this.customAnim2ShouldRotate = shouldRotate;
		this.customAnim2Offset = centered.cpy();
		this.customAnim2Rotation = this.mousePos.cpy().sub(this.Center()).angle();
		this.invertedSwing2 = false;
		this.timeSinceLastAttack = 9999f;
		if (blockMove && blockAttack) {
			this.setInvincible(Main.ticksToSeconds(frames * skipper));
		}
	}

	public void playAnim2(Texture sheet, int frames, int skipper, boolean shouldRotate, boolean blockMove,
			boolean blockAttack) {
		playAnim2(sheet, frames, skipper, shouldRotate, blockMove, blockAttack, Vector2.Zero.cpy());
	}

	public int getStrenght() {
		return this.strength + this.estrength;
	}

	public int getAgility() {
		return this.agility + this.eagility;
	}

	public int getLuck() {
		return this.luck + this.eluck;
	}

	public int getLethality() {
		return this.lethality + this.elethality;
	}

	public int getIntelligence() {
		return this.intelligence + this.eintelligence;
	}

	public int getVitality() {
		return this.vitality + this.evitality;
	}

	public float getAttackSpeed() {
		return Main.clamp(0.01f, (this.haveBuff(42) != -1 ? 1000f : 3f), this.attackSpeed);
	}

	public int nextLevelExp() {
		return nextLevelExp(this.level);
	}

	public int nextLevelExp(int i) {
		return 10000000;
	}

	public String getClassName() {
		int titleid = 0;
		if (this.level >= 100)
			titleid = 5;
		else if (this.level > 80)
			titleid = 4;
		else if (this.level > 60)
			titleid = 3;
		else if (this.level > 40)
			titleid = 2;
		else if (this.level > 20)
			titleid = 1;

		return classType.titles[titleid];
	}

	public boolean tryingToAttack() {
		return (Gdx.input.isButtonPressed(Buttons.LEFT) && this.canUseAttack());
	}

	public boolean tryingToAttack2() {
		return (Gdx.input.isButtonPressed(Buttons.RIGHT) && this.canUseAttack());
	}

	public boolean canBeTargeted() {
		if (this.haveBuff(11) != -1)
			return false;

		return true;
	}

	public int weaponsDamage() {
		float dmg = 0;
		int div = 0;
		if (this.inventory[Constant.ITEMSLOT_LEFT] != null && this.inventory[Constant.ITEMSLOT_LEFT].damage > 0) {
			dmg += this.inventory[Constant.ITEMSLOT_LEFT].damage;
			div++;
		}
		if (this.inventory[Constant.ITEMSLOT_RIGHT] != null && this.inventory[Constant.ITEMSLOT_RIGHT].damage > 0) {
			dmg += this.inventory[Constant.ITEMSLOT_RIGHT].damage;
			div++;
		}

		dmg /= div;

		return (int) dmg;
	}

	public boolean canReach(Vector2 dest) {
		return true;
	}

	public Objective haveObjective(int objid) {
		Objective have = null;
		for (Quest q : this.quests) {
			for (Objective o : q.objectives) {
				if (o.id == objid) {
					have = o;
					break;
				}
			}
			if (have != null)
				break;
		}
		return have;
	}

	public Objective haveKillObjective(int monsterid) {
		Objective have = null;
		for (Quest q : this.quests) {
			for (Objective o : q.objectives) {
				if (o.id == 1 && o.infos[0] == monsterid) {
					have = o;
					break;
				}
			}
			if (have != null)
				break;
		}
		return have;
	}

	public Objective haveCollectObjective(int itemid) {
		Objective have = null;
		for (Quest q : this.quests) {
			for (Objective o : q.objectives) {
				if (o.id == 2 && o.infos[0] == itemid) {
					have = o;
					break;
				}
			}
			if (have != null)
				break;
		}
		return have;
	}

	public Quest addQuest(int id, boolean showAsMain) {
		Quest q = new Quest().setInfos(id);
		this.quests.add(q);
		if (showAsMain)
			this.displayQuest = quests.indexOf(q);

		Main.lastQuest = q;
		Main.lastQuestTicks = 0;
		Main.lastQuestText = q.name + " has been added to your quest log";
		return q;
	}

	public void updateObjectives(Quest q) {
		Main.lastQuest = q;
		Main.lastQuestTicks = 0;
		Main.lastQuestText = q.name + " objectives were updated";
		q.step++;
	}

	public void accomplishQuest(int id) {
		boolean fine = false;
		for (Iterator<Quest> j = this.quests.iterator(); j.hasNext();) {
			Quest q = j.next();
			if (q.id == id) {
				this.experience += q.rewardXP;
				this.gold += q.rewardGold;
				q.onComplete(this);
				LivingText.Create(this.position.cpy().add(this.width / 2, this.height + 16), new Vector2(0f, 100f),
						"+" + q.rewardXP + " XP", Color.WHITE, 1f, 1.5f, true, false);
				LivingText.Create(this.position.cpy().add(this.width / 2, this.height + 48), new Vector2(0f, 100f),
						"+" + q.rewardGold + " Gold", Color.YELLOW, 1f, 1.5f, true, false);
				fine = true;
				if (!haveCompletedQuest(id))
					this.questsCompleted.add(id);

				j.remove();
			}
		}
		if (fine) {
			if (quests.size() > 0) {
				this.displayQuest = 0;
			}
		}
	}

	public boolean haveCompletedQuest(int questId) {
		for (int q : this.questsCompleted) {
			if (q == questId) {
				return true;
			}
		}
		return false;
	}

	public boolean readyToAccomplish(int questId) {
		if(this.getQuest(questId) == null)
			return false;
		
		for (Quest q : this.quests) {
			if (q.id == questId) {
				return q.isConcluded();
			}
		}
		return true;
	}

	public boolean hasQuest(int id) {
		for (Quest q : this.quests) {
			if (q.id == id) {
				return true;
			}
		}
		return false;
	}

	public Quest getQuest(int id) {
		for (Quest q : this.quests) {
			if (q.id == id) {
				return q;
			}
		}
		return null;
	}

	public float getPhysicalDamageMult() {
		return 1f + this.extraElementDamage[Constant.DAMAGETYPE_PHYSICAL];
	}

	public float getElementalDamageMult() {
		int extraIntelligence = (int) Math.floor(this.getIntelligence() / 5);
		return this.overallMagicDamage + extraIntelligence * 0.02f;
	}

	public float getMeleeDamageMult() {
		return 1f + this.getStrenght() * 0.004f;
	}

	public float getRangedDamageMult() {
		return 1f + this.getStrenght() * 0.004f;
	}

	public float getMagicDamageMult() {
		return 1f + this.getIntelligence() * 0.004f;
	}

	public void executeAttack(int handtype, Vector2 vector2) {
		double mult = 1f-((Math.pow(this.getAttackSpeed(), 2f)-1f)/9f);
		if (handtype == 0)
		{
			if(this.inventory[Constant.ITEMSLOT_LEFT] != null && this.canAttack() && this.getLeft().isWeapon()) {
				int finalcdf = (int) (this.inventory[Constant.ITEMSLOT_LEFT].cooldownFrames * mult);
				this.attacking = true;
				this.timeSinceLastAttack = 0;
				this.inventory[Constant.ITEMSLOT_LEFT].onSwing(this);
				this.mouseOnAttack = vector2.cpy();
				this.posOnAttack = this.Center().cpy();
				this.resetAnim();
				if (vector2.x < this.Center().x) {
					this.attackDir = -1;
				} else {
					this.attackDir = 1;
				}
				this.inventory[Constant.ITEMSLOT_LEFT].cdf = finalcdf;
				this.inventory[Constant.ITEMSLOT_LEFT].lastcdf = finalcdf;
			}
		}
		else {
			if (this.inventory[Constant.ITEMSLOT_RIGHT] != null && this.canAttack2() && this.getRight().isWeapon()) {
				int finalcdf = (int) (this.inventory[Constant.ITEMSLOT_RIGHT].cooldownFrames * mult);
				this.attacking2 = true;
				this.timeSinceLastAttack = 0;
				this.inventory[Constant.ITEMSLOT_RIGHT].onSwing(this);
				this.mouseOnAttack = vector2.cpy();
				this.posOnAttack = this.Center().cpy();
				this.resetAnim2();
				if (vector2.x < this.Center().x) {
					this.attackDir = -1;
				} else {
					this.attackDir = 1;
				}
				this.inventory[Constant.ITEMSLOT_RIGHT].cdf = finalcdf;
				this.inventory[Constant.ITEMSLOT_RIGHT].lastcdf = finalcdf;
			}
		}
	}

	public boolean inBattle() {
		if (this.lastHurtTicks() < 300)
			return true;

		if (this.lastMonsterHitTicks < 300)
			return true;

		for (Monster m : Main.monster) {
			if (m.sameMapAs(this) && m.target == this.whoAmI && m.updateRelevant() && m.Center().dst(this.Center()) < 2400) {
				return true;
			}
		}

		return false;
	}

	public Item getEquippedItem(int id) {
		for (int i = Constant.ITEMSLOT_OFFSET; i <= Constant.ITEMSLOT_MAX; i++) {
			if (this.inventory[i] != null && this.inventory[i].id == id) {
				return this.inventory[i];
			}
		}
		return null;
	}

	public Item getItem(int id) {
		for (int i = 0; i <= Constant.ITEMSLOT_MAX; i++) {
			if (this.inventory[i] != null && this.inventory[i].id == id) {
				return this.inventory[i];
			}
		}
		for(int i = 0;i < 4;i++)
		{
			if(this.vanity[i] != null && this.vanity[i].id == id)
			{
				return this.vanity[i];
			}
		}
		if(!AHS.isUp)
		{
			if(Main.holdingItem != null && Main.holdingItem.id == id)
			{
				return Main.holdingItem;
			}
		}
		return null;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public int getPP() {
		int pp = (int) (this.getStrenght() * 0.5f + this.getLethality() * 0.5f);
		if (this.inventory[Constant.ITEMSLOT_LEFT] != null && (this.inventory[Constant.ITEMSLOT_LEFT].isMelee()
				|| this.inventory[Constant.ITEMSLOT_LEFT].isRanged()))
			pp += this.inventory[Constant.ITEMSLOT_LEFT].damage/2f;

		if (this.inventory[Constant.ITEMSLOT_RIGHT] != null && (this.inventory[Constant.ITEMSLOT_RIGHT].isMelee()
				|| this.inventory[Constant.ITEMSLOT_RIGHT].isRanged()))
			pp += this.inventory[Constant.ITEMSLOT_RIGHT].damage/2f;

		pp *= (1f + (this.getLethality() * 0.0075f));

		pp += this.extraPP;

		return (int) (pp * this.extraPPperc);
	}

	public int getMP() {
		int mp = (int) (this.getIntelligence() * 0.5f + this.getLethality() * 0.5f);
		if (this.inventory[Constant.ITEMSLOT_LEFT] != null && this.inventory[Constant.ITEMSLOT_LEFT].isMagic())
			mp += this.inventory[Constant.ITEMSLOT_LEFT].damage/2f;

		if (this.inventory[Constant.ITEMSLOT_RIGHT] != null && this.inventory[Constant.ITEMSLOT_RIGHT].isMagic())
			mp += this.inventory[Constant.ITEMSLOT_RIGHT].damage/2f;

		mp *= (1f + (this.getLethality() * 0.0075f));

		mp += this.extraMP;

		return (int) (mp * this.extraMPperc);
	}

	public void changeClass(ArkaClass newClass) {
		this.classType = newClass;
		this.learnedSkills.clear();
		for(int i = 0;i < this.skills.length;i++)
		{
			this.skills[i] = null;
		}
		for (int i : newClass.skillkit) {
			Skill s = new Skill(i);
			this.learnedSkills.add(s);
			System.out.println("Learned skill: " + s.name);
		}
	}

	/**
	 * @return the untargetable
	 */
	public boolean isUntargetable() {
		return untargetable > 0f;
	}

	/**
	 * @param untargetable the untargetable to set
	 */
	public void setUntargetable(float untargetable) {
		this.untargetable = untargetable;
	}

	public void setCustomDisacceleration(float time, float mult) {
		this.customDisaccelTime = time;
		this.customDisaccel = mult;
	}

	public Skill getEquippedSkill(int id) {
		for (int i = 0; i < this.skills.length; i++) {
			if (this.skills[i] != null && this.skills[i].id == id) {
				return this.skills[i];
			}
		}
		return null;
	}

	public Skill getSkill(int id) {
		for (Skill s : this.learnedSkills) {
			if (s.id == id) {
				return s;
			}
		}
		return null;
	}

	public void setInvincible(float invincible) {
		this.invincible = Math.max(this.invincible, invincible);
	}

	public void blockMovement(float time) {
		this.movementBlockTime = time;
	}

	public void blockAttack(float time) {
		this.attackBlockTime = time;
	}

	public boolean haveCollisions() {
		if (this.haveBuff(25) != -1 || this.haveBuff(36) != -1 || this.haveBuff(68) != -1)
			return false;

		return true;
	}

	public void playThrowAnim(boolean blockAttack) {
		this.playAnim(Content.dartThrow, 6, 3, true, false, blockAttack, new Vector2(-64, 0));
	}

	public void playThrowAnim() {
		this.playThrowAnim(true);
	}

	public boolean channelingSkillBlocksAttack() {
		for (Skill s : this.skills) {
			if (s != null && s.channeling) {
				return true;
			}
		}
		return false;
	}

	public boolean channelingSkillBlocksMove() {
		return false;
	}

	public void playMageHandAnimation(int ticks, boolean blockAttack) {
		this.playAnim(Content.handPointing, 1, ticks, true, false, blockAttack, new Vector2(-48, 0));
	}

	public int getTotalShield() {
		int shield = 0;
		for (Buff b : this.buffs) {
			if (b != null && b.shield) {
				shield += b.stacks;
			}
		}
		return shield;
	}

	public boolean dualEquipping() {
		Item left = this.inventory[Constant.ITEMSLOT_LEFT];
		Item right = this.inventory[Constant.ITEMSLOT_RIGHT];
		if (left == null || right == null) {
			return false;
		}
		if((left.itemclass == Constant.ITEMCLASS_MAGIC && right.itemclass != Constant.ITEMCLASS_MAGIC) || 
				(right.itemclass == Constant.ITEMCLASS_MAGIC && left.itemclass != Constant.ITEMCLASS_MAGIC))
			return false;
		
		return right.type == Constant.ITEMTYPE_LEFT;
	}

	public void resetWeaponCdf() {
		if (this.inventory[Constant.ITEMSLOT_LEFT] != null) {
			this.inventory[Constant.ITEMSLOT_LEFT].cdf = 0;
		}
		if (this.inventory[Constant.ITEMSLOT_RIGHT] != null) {
			this.inventory[Constant.ITEMSLOT_RIGHT].cdf = 0;
		}
	}

	public void removeItem(int id, int quant) {
		int left = quant;
		for (int i = 0; i <= Constant.ITEMSLOT_MAX; i++) {
			if (left <= 0) {
				break;
			}
			if (this.inventory[i] != null && this.inventory[i].id == id) {
				if (!this.inventory[i].canStack) {
					this.inventory[i].stacks--;
					left--;
				} else {
					if (left > this.inventory[i].stacks) {
						left -= this.inventory[i].stacks;
						this.inventory[i].stacks = 0;
					} else {
						this.inventory[i].stacks -= left;
						left = 0;
					}
				}
			}
		}
	}

	public int getItemQuantity(int id) {
		int stacks = 0;
		for (int i = 0; i <= Constant.ITEMSLOT_MAX; i++) {
			if (this.inventory[i] != null && this.inventory[i].id == id) {
				stacks += this.inventory[i].stacks;
			}
		}
		if(!AHS.isUp)
		{
			if(Main.holdingItem != null && Main.holdingItem.id == id)
			{
				stacks += Main.holdingItem.stacks;
			}
		}
		return stacks;
	}

	public void removeItem(int id) {
		removeItem(id, 1);
	}

	public void updateCameMap() {
		this.cameFromX = this.myMapX;
		this.cameFromY = this.myMapY;
		this.cameFromLX = (int) this.position.x;
		this.cameFromLY = (int) this.position.y;
	}

	public boolean shouldDashPosture() {
		if (this.forceDashPosture > 0)
			return true;

		return (this.ragingDash > 0f || this.haveBuff(36) != -1 || this.haveBuff(40) != -1 || this.haveBuff(25) != -1
				|| this.isMultiCasting(75) || this.haveBuff(49) != -1 || this.haveBuff(50) != -1
				|| this.haveBuff(51) != -1 || this.haveBuff(53) != -1 || this.haveBuff(68) != -1);
	}

	public float getLostHealth(boolean perc) {
		if (!perc)
			return this.getMaxHealthEx() - this.health;
		else
			return ((float) (this.getMaxHealthEx() - this.health) / this.maxHealth);
	}
	
	public float getMaxHealthEx()
	{
		return this.prevMaxHP;
	}

	public float getActualHealth(boolean perc) {
		if (!perc)
			return this.health;
		else
			return (float) this.health / this.maxHealth;
	}

	public boolean isMultiCasting(int skill) {
		Skill s = this.getSkill(skill);
		if (s != null && s.casts > 0 && s.casts < s.multiCasts) {
			return true;
		}
		return false;
	}

	public void resetCollisions() {
		this.contacting.clear();
	}

	public void fixY() {
		/*if (Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, this.position.y, this.width,
				this.height)) {
			this.position.y = Constant.tryGetMapForEntity(this).getNextestFreeSpace(this.position.x, this.position.y,
					this.width, this.height, this.direction, 1).y * 64;
		}*/
	}

	public boolean isWearingShield() {
		return (this.inventory[Constant.ITEMSLOT_RIGHT] != null
				&& this.inventory[Constant.ITEMSLOT_RIGHT].itemclass == Constant.ITEMCLASS_SHIELD);
	}
	
	public void resetAttackTime()
	{
		this.timeSinceLastAttack = 0f;
	}

	public boolean randomChance(float i)
	{
		float chance = i + this.luck * 0.005f;
		return MathUtils.randomBoolean(chance);
	}
	
	public boolean nearNPC()
	{
		for(NPC n : Constant.getNPCList())
		{
			if(n.active && n.sameMapAs(this) && n.Center().dst(this.Center()) < 1000)
			{
				return true;
			}
		}
		return false;
	}
	
	public Item getLeft()
	{
		return this.inventory[Constant.ITEMSLOT_LEFT];
	}
	public Item getRight()
	{
		return this.inventory[Constant.ITEMSLOT_RIGHT];
	}
	public Item getHead()
	{
		return this.inventory[Constant.ITEMSLOT_HEAD];
	}
	public Item getBody()
	{
		return this.inventory[Constant.ITEMSLOT_BODY];
	}
	
	public float[] getPCShaderArray()
	{
		if(!this.updatedSkinColor)
		{
			float[] sc = new float[] {Constant.skinColor.r, Constant.skinColor.g, Constant.skinColor.b};
			if(this.vanity[3] != null)
			{
				if(this.vanity[3].armorColor != null)
				{
					Color c = this.vanity[3].armorColor;
					if(c != null && this.vanity[3].dyed && this.vanity[3].dyeArmorColor)
						c = this.vanity[3].dye;
					
					sc = new float[] {c.r, c.g, c.b};
				}
			}
			else if(this.inventory[Constant.ITEMSLOT_BODY] != null && this.inventory[Constant.ITEMSLOT_BODY].armorColor != null)
			{
				Color c = this.inventory[Constant.ITEMSLOT_BODY].armorColor;
				if(c != null && this.inventory[Constant.ITEMSLOT_BODY].dyed && this.inventory[Constant.ITEMSLOT_BODY].dyeArmorColor)
					c = this.inventory[Constant.ITEMSLOT_BODY].dye;
				
				sc = new float[] {c.r, c.g, c.b};
			}
			this.lastSkinColor = sc;
			return sc;
		}
		else
		{
			return this.lastSkinColor;
		}
	}
	
	public Color getPCShaderColor()
	{
		float[] sc = this.getPCShaderArray();
		return new Color(sc[0], sc[1], sc[2], 1f);
	}
	
	@Override
	protected String makeShaderReplacements(String str, Sprite sprite)
	{
		return super.makeShaderReplacements(str, sprite)
				.replaceAll("scr", ""+this.getPCShaderArray()[0])
				.replaceAll("scg", ""+this.getPCShaderArray()[1])
				.replaceAll("scb", ""+this.getPCShaderArray()[2]);
	}
	
	public int getQuestState(int questID)
	{
		if(!this.haveCompletedQuest(questID))
		{
			if(this.hasQuest(questID))
				return this.readyToAccomplish(questID) ? Constant.QUESTSTATE_READYTOCOMPLETE : Constant.QUESTSTATE_PROGRESS;
			else
				return Constant.QUESTSTATE_NEVER;
		}
		else
		{
			return Constant.QUESTSTATE_COMPLETED;
		}
	}

	public void onChangeMap()
	{
		for(int i = 0;i < this.isCardAvailable.length;i++)
		{
			if(!this.isCardAvailable[i])
			{
				this.isCardAvailable[i] = true;
				this.ticksSinceCardUse[i] = 0;
			}
		}
	}
	
	@Override
	public String toString()
	{
		return this.name + " (" + this.whoAmI + ", " + this.active + ")";
	}
}