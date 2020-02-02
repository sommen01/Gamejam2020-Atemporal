package com.roguelike.constants;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.entities.Entity;
import com.roguelike.entities.Lighting;
import com.roguelike.entities.Monster;
import com.roguelike.entities.NPC;
import com.roguelike.entities.Particle;
import com.roguelike.entities.Player;
import com.roguelike.entities.Projectile;
import com.roguelike.game.Content;
import com.roguelike.game.Main;

public class Buff
{
	public transient String name;
	public transient String description;
	public float timeLeft;
	public int stacks;
	public transient boolean canStack;
	public transient int maxStacks;
	public int id;
	public transient boolean debuff;
	public float lastTimeLeft;
	public int originUid;
	public String originInfo;
	public int ticks;
	public transient boolean shield;
	
	public void initialize(Entity owner)
	{
		float time = this.timeLeft;
		float maxtime = this.lastTimeLeft;
		int ticks = this.ticks;
		int stacks = this.stacks;
		this.SetInfos(this.id, owner);
		this.timeLeft = time;
		this.lastTimeLeft = maxtime;
		this.ticks = ticks;
		this.stacks = stacks;
	}
	
	public Buff SetInfos(int id, Entity origin)
	{
		ResetStats();
		this.id = id;
		if(this.id == 1)
		{
			this.name = "Phase Rage";
			this.description = "Laser Saber's damage is increased";
			this.canStack = true;
			this.maxStacks = 20;
		}
		else if(this.id == 2)
		{
			this.name = "Poisoned";
			this.description = "Taking damage over time";
			this.canStack = true;
			this.maxStacks = -1;
			this.debuff = true;
		}
		else if(this.id == 3)
		{
			this.name = "Darkrai Mark";
			this.description = "Run!";
			this.canStack = true;
			this.maxStacks = 3;
			this.debuff = true;
		}
		else if(this.id == 4)
		{
			this.name = "Souls";
			this.description = "Tasty";
			this.canStack = true;
			this.maxStacks = 5;
		}
		else if(this.id == 5)
		{
			this.name = "Starshooter Roll";
			this.description = "Next attack deals 200% increased damage.";
			this.canStack = true;
			this.maxStacks = -1;
		}
		else if(this.id == 6)
		{
			this.name = "Triumph";
			this.description = "Next ranged attack deals increased damage.";
		}
		else if(this.id == 7)
		{
			this.name = "Air Time";
			this.description = "Gravity effects won't work in you.";
		}
		else if(this.id == 8)
		{
			this.name = "Pretty Plasma Pocket Pistol Perfection";
			this.description = "Attack speed and damage increased.";
			this.maxStacks = 100;
			this.canStack = true;
		}
		else if(this.id == 9)
		{
			this.name = "Time Glitch";
			this.description = "Clocks are useless now.";
			this.maxStacks = 5;
			this.canStack = true;
		}
		else if(this.id == 10)
		{
			this.name = "Guiding Star";
			this.description = "Attack speed increased.";
		}
		else if(this.id == 11)
		{
			this.name = "Invisible";
			this.description = "Enemies can't target you.";
		}
		else if(this.id == 12)
		{
			this.name = "Dart Marked";
			this.description = "Three of these and you're a dead man.";
			this.debuff = true;
			this.maxStacks = 3;
			this.canStack = true;
		}
		else if(this.id == 13)
		{
			this.name = "Holy Judgement";
			this.description = "Death to the guilty ones";
			this.debuff = true;
			this.maxStacks = 25;
			this.canStack = true;
		}
		else if(this.id == 14)
		{
			this.name = "Fire Mark Activated";
			this.description = "Phoenix's Tear attacks are powered.";
		}
		else if(this.id == 15)
		{
			this.name = "Frozen";
			this.description = "I can move my hands, what about you?";
			this.debuff = true;
		}
		else if(this.id == 16)
		{
			this.name = "Cold Tiredness";
			this.description = "You can't be frozen.";
		}
		else if(this.id == 17)
		{
			this.name = "Plasma Shield";
			this.description = "The next projectile or enemy contact will be mitigated.";
		}
		else if(this.id == 18)
		{
			this.name = "Dragon Mode";
			this.description = "Sanity's Epilogue is in Dragon mode.";
		}
		else if(this.id == 19)
		{
			this.name = "Hand Frenesis";
			this.description = "Your attack speed is highly increased for a tiny amount of time.";
			this.canStack = true;
			this.maxStacks = 10;
		}
		else if(this.id == 20)
		{
			this.name = "Punishment";
			this.description = "Punishment is cooling down.";
		}
		else if(this.id == 21)
		{
			this.name = "Flipping Dizziness";
			this.description = "Three of these and you can't flip.";
			this.canStack = true;
			this.maxStacks = 3;
		}
		else if(this.id == 22)
		{
			this.name = "Arthur's Prologue";
			this.description = "This target was already attacked by the Arthur's Prologue.";
		}
		else if(this.id == 23)
		{
			this.name = "Olympus Rage";
			this.description = "Zeus is angry with you but he is a little drunk to successfully hit you.";
		}
		else if(this.id == 24)
		{
			this.name = "Stun";
			this.description = "You can't move or attack. Dizzy wizzy.";
		}
		else if(this.id == 25)
		{
			this.name = "Leaping";
			this.description = "Rawr.";
		}
		else if(this.id == 26)
		{
			this.name = "Ungravitational";
			this.description = "'Holy apples' - Isaac Newton";
		}
		else if(this.id == 27)
		{
			this.name = "Sneaky";
			this.description = "Your next hit will be a guaranteed Sneaky Attack.";
		}
		else if(this.id == 28)
		{
			this.name = "Attack Deficit";
			this.description = "Attack damage reduced.";
		}
		else if(this.id == 29)
		{
			this.name = "Defense Deficit";
			this.description = "Defenses decreased.";
		}
		else if(this.id == 30)
		{
			this.name = "Bloodthirsty Dagger";
			this.description = "Your next attack will deal increased damage.";
		}
		else if(this.id == 31)
		{
			this.name = "Unexpected Shot";
			this.description = "Your next attack will summon extra projectiles when it collides.";
		}
		else if(this.id == 32)
		{
			this.name = "Judgement Arrow";
			this.description = "Your next attack will summon a lightning upon the enemy it hits.";
		}
		else if(this.id == 33)
		{
			this.name = "Power Arrow";
			this.description = "Your next arrow or bullet will have increased speed.";
		}
		else if(this.id == 34)
		{
			this.name = "Burn";
			this.description = "'I can not believe I'm on fire' - Spy";
		}
		else if(this.id == 35)
		{
			this.name = "Nuclear Body";
			this.description = "Enemies who causes contact damage to you are exploded.";
		}
		else if(this.id == 36)
		{
			this.name = "Space Distortion";
			this.description = "Stay at your seat.";
		}
		else if(this.id == 37)
		{
			this.name = "Soul Chain";
			this.description = "Ready to be pulled.";
		}
		else if(this.id == 38)
		{
			this.name = "Tera Protection";
			this.description = "You are protected.";
			this.shield = true;
			this.canStack = true;
			this.maxStacks = -1;
		}
		else if(this.id == 39)
		{
			this.name = "Empowered Blade";
			this.description = "Your next weapon attack is going to cause extra damage.";
		}
		else if(this.id == 40)
		{
			this.name = "Dashing n' Bashing";
			this.description = "GET OUT THE WAY!";
		}
		else if(this.id == 41)
		{
			this.name = "Sanity Loss";
			this.description = "If you already shot once, why not shoot twice?";
			this.canStack = true;
			this.maxStacks = 3;
		}
		else if(this.id == 42)
		{
			this.name = "Adrenaline Overdose";
			this.description = "If you already killed once, why not kill twice?";
			this.canStack = true;
			this.maxStacks = -1;
		}
		else if(this.id == 43)
		{
			this.name = "Brave Counterstrike";
			this.description = "Accumulating damage.";
			this.canStack = true;
			this.maxStacks = -1;
		}
		else if(this.id == 44)
		{
			this.name = "Bravery";
			this.description = "Attack speed and movement speed increased.";
			this.canStack = false;
		}
		else if(this.id == 45)
		{
			this.name = "Leap Target";
			this.description = "Coming out from nowhere.";
			this.canStack = false;
		}
		else if(this.id == 46)
		{
			this.name = "Undying Spirit";
			this.description = "You filled your determination.";
			this.canStack = false;
		}
		else if(this.id == 47)
		{
			this.name = "Focusing";
			this.description = "Your next attack is going to apply Focus to stroken enemies.";
		}
		else if(this.id == 48)
		{
			this.name = "Focused";
			this.description = "Avoid the Samurai.";
		}
		else if(this.id == 49)
		{
			this.name = "Engaging";
			this.description = "Yo, get out the way.";
		}
		else if(this.id == 50)
		{
			this.name = "Posture";
			this.description = "Cool postures.";
		}
		else if(this.id == 51)
		{
			this.name = "Shadow-ing";
			this.description = "Peter Pan attack.";
		}
		else if(this.id == 52)
		{
			this.name = "Death Gladiator";
			this.description = "You say run.";
		}
		else if(this.id == 53)
		{
			this.name = "Lightning Exhaust";
			this.description = "Wooosh.";
		}
		else if(this.id == 54)
		{
			this.name = "Dollar Sign";
			this.description = "Your cooldowns are regenerating faster.";
			this.canStack = true;
			this.maxStacks = 2;
		}
		else if(this.id == 55)
		{
			this.name = "Blazing Sevens";
			this.description = "Your attack is increased by 77%.";
		}
		else if(this.id == 56)
		{
			this.name = "Arcane Mark";
			this.canStack = false;
			this.description = "ur ded";
		}
		else if(this.id == 57)
		{
			this.name = "Berserk";
			this.description = "Grrr";
			this.canStack = false;
		}
		else if(this.id == 58)
		{
			this.name = "Lamp";
			this.description = ":OO";
			this.canStack = false;
		}
		else if(this.id == 59)
		{
			this.name = "Champion";
			this.description = "Empowered enemy.";
		}
		else if(this.id == 60)
		{
			this.name = "Sneaky Attack: Mana";
			this.description = "Ded.";
		}
		else if(this.id == 61)
		{
			this.name = "Vampiric Target";
			this.description = "So bad.";
		}
		else if(this.id == 62)
		{
			this.name = "Ritual";
			this.description = "Increased cooldown acceleration and movement speed.";
		}
		else if(this.id == 63)
		{
			this.name = "Elemental Overbreak";
			this.description = "Your skills are improved.";
		}
		else if(this.id == 64)
		{
			this.name = "Elemental Overbreak";
			this.description = "You are fullfilling your energy...";
			this.canStack = true;
			this.maxStacks = 6;
		}
		else if(this.id == 65)
		{
			this.name = "Arcane Elevation";
			this.description = "The full transcedence.";
		}
		else if(this.id == 66)
		{
			this.name = "Life Bubble";
			this.description = "Protect this bubble with your life.";
			this.shield = true;
			this.canStack = true;
			this.maxStacks = -1;
		}
		else if(this.id == 67)
		{
			this.name = "Electric Current";
			this.description = "Your veins are like 'bzzzzz'";
			this.canStack = true;
			this.maxStacks = 5;
		}
		else if(this.id == 68)
		{
			this.name = "Breaking It Down";
			this.description = "OMAE WA MOU SHINDEIRU";
		}
		else if(this.id == 69)
		{
			this.name = "Broken Down";
			this.description = "NANI?";
			this.canStack = false;
		}
		else if(this.id == 70)
		{
			this.name = "Campfire Heat";
			this.description = "Greatly increased health regeneration.";
			this.canStack = false;
		}
		else if(this.id == 71)
		{
			this.name = "Guiding Star";
			this.description = "Attack speed increased.";
		}
		else if(this.id == 72)
		{
			this.name = "Macaw Critical";
			this.description = "Critical chance increased.";
			this.canStack = true;
			this.maxStacks = 100;
		}
		else if(this.id == 73)
		{
			this.name = "Dimensional Protection";
			this.description = "Large shield.";
			this.canStack = true;
			this.maxStacks = -1;
			this.shield = true;
		}
		else if(this.id == 74)
		{
			this.name = "Dimensional Protection Cooldown";
			this.description = "Large shields needs large nerfs.";
		}
		else if(this.id == 75)
		{
			this.name = "Shadow Shuriken Mark";
			this.description = "Oh no!";
		}
		else if(this.id == 76)
		{
			this.name = "Sparkles";
			this.description = "Friendship is magic!";
		}
		else if(this.id == 77)
		{
			this.name = "Sparkles Cooldown";
			this.description = "The little friends are cooling down!";
		}
		
		if(origin.getClass() == Player.class)
		{
			Player player = (Player)origin;
			this.originUid = player.whoAmI;
			this.originInfo = "player";
		}
		else if(origin.getClass() == Monster.class)
		{
			Monster m = (Monster)origin;
			this.originUid = m.uid;
			this.originInfo = "monster";
		}
		else if(origin.getClass() == Projectile.class)
		{
			Projectile p = (Projectile)origin;
			this.originUid = p.whoAmI;
			this.originInfo = "projectile";
		}
		return this;
	}
	
	public void draw(Entity owner, SpriteBatch batch)
	{
		Player player = null;
		if(owner instanceof Player)
			player = (Player)owner;
		// Modifiers sprites
		if(this.id == 28)
		{
			Sprite sprite = new Sprite(Content.atkr);
			sprite.setPosition(owner.position.x + owner.width + 8, owner.position.y + owner.height - 24);
			Main.prettySpriteDraw(sprite, batch);
		}
		
		if(this.id == 29 || (this.id == 12 && this.stacks >= 3))
		{
			Sprite sprite = new Sprite(Content.defr);
			sprite.setPosition(owner.position.x + owner.width + 8, owner.position.y + owner.height - 44);
			Main.prettySpriteDraw(sprite, batch);
		}

		// General effects
		if(this.id == 3)
		{
			Sprite sprite = new Sprite(Content.darkrai[this.stacks-1]);
			sprite.setPosition(owner.Center().x - sprite.getWidth()/2, owner.position.y + owner.height + 18);
			sprite.draw(batch);
		}
		else if((this.id == 71 && player != null && player.haveBuff(10) == -1) || this.id == 10)
		{
			/*float angle = Constant.gameTick()*5;
			float curveAngle = 5*Constant.gameTick()%360;
			float sin = (float)Math.sin(angle*Math.PI/180f);
			float cos = (float)Math.cos(angle*Math.PI/180f);
			Vector2 pos = new Vector2();
			Vector2 max = new Vector2(1f, 0f).setAngle(curveAngle);
			max.x = Math.abs(max.x);
			max.y = Math.abs(max.y);
			pos.x = cos * 20 + cos*max.x * 70f;
			pos.y = sin * 20 + sin*max.y * 70f;
			Particle p = Particle.Create(pos.cpy().add(owner.Center()), Vector2.Zero, 10, new Color(0.92f, 0.96f, 0.6f, 1f), 0f, 0.5f, 1f);
			float minAngle = 180-curveAngle;
			float maxAngle = 360-curveAngle;
			p.drawBack = !(angle%360 > minAngle && angle%360 < maxAngle);
			p.setLight(32, p.color);
			Lighting.Create(p.Center(), 256, Main.getLightingColor(p.color, 0.5f), 1f, 0.6f).setParent(p);*/
			if(this.ticks > 30)
			{
				float angle = Constant.gameTick()*5;
				float curveAngle = (Constant.gameTick()/6f)%360;
				float sin = (float)Math.sin(angle*Math.PI/180f);
				float cos = (float)Math.cos(angle*Math.PI/180f);
				Vector2 pos = new Vector2();
				Vector2 max = new Vector2(1f, 0f).setAngle(curveAngle);
				max.x = Math.abs(max.x);
				max.y = Math.abs(max.y);
				pos.x = cos * 15 + cos*max.x * 80f;
				pos.y = sin * 15 + sin*max.y * 80f;
				Particle p = Particle.Create(pos.cpy().add(owner.Center()), Vector2.Zero, 10, new Color(0.92f, 0.96f, 0.6f, 1f), 0f, 0.5f, 1f);
				float minAngle = 180-(curveAngle>180?curveAngle-180:curveAngle);
				float maxAngle = 360-(curveAngle>180?curveAngle-180:curveAngle);
				p.drawBack = !(angle%360 > minAngle && angle%360 < maxAngle);
				p.setLight(32, p.color);
				p.rotation = Constant.gameTick();
				Lighting.Create(p.Center(), 256, Main.getLightingColor(p.color, 0.5f), 1f, 0.6f).setParent(p);
			}
			else
			{
				for(int j = this.ticks*10;j < this.ticks*10 + 10;j++)
				{
					float angle = j*4;
					float cos = (float)Math.cos(angle*Math.PI/180f);
					Vector2 pos = player.Center().add(cos * 80, j*0.8f - 40);
					Particle p = Particle.Create(pos, Vector2.Zero, 10, new Color(0.92f, 0.96f, 0.6f, 1f), 0f, 0.5f, 1f);
					p.drawBack = angle < 180;
					p.setLight(32, p.color);
					p.rotation = Constant.gameTick();
					Lighting.Create(p.Center(), 256, Main.getLightingColor(p.color, 0.5f), 1f, 0.6f).setParent(p);
				}
			}
		}
		else if(this.id == 12)
		{
			Sprite sprite = new Sprite(Content.dartThrowMark);
			if(this.lastTimeLeft - this.timeLeft < 1f && this.stacks >= 3)
			{
				float sin = (float)Math.sin((this.lastTimeLeft - this.timeLeft) * Math.PI);
				sprite.setScale(1 + sin);
			}
			
			sprite.setPosition(owner.Center().x - (sprite.getWidth()*(sprite.getScaleX()/2))/2, owner.position.y + owner.height + 32 + sprite.getHeight() * sprite.getScaleY()/4);
			sprite.setRegion(0, (int)(sprite.getHeight()/3)*(this.stacks-1), (int)sprite.getWidth(), (int)sprite.getHeight()/3);
			sprite.setSize(sprite.getWidth(), sprite.getHeight()/3);
			sprite.draw(batch);
		}
		else if(this.id == 17)
		{
			for(int i = 0;i < 36;i++)
			{
				float sin = (float)Math.sin(i * 5 * Math.PI/180f);
				float cos = (float)Math.cos(i * 5 * Math.PI/180f);
				Particle p = Particle.Create(new Vector2(owner.Center().x + cos * 60f, owner.Center().y + sin * 60f), new Vector2(MathUtils.random(-15, 15), MathUtils.random(-15, 15)), 2, new Color(0f, 1f, 1f, 1f), 2f, 0.5f, 1f);
				p.lights = true;
				p.lightSize = 16;
				p.lightColor = new Color(0f, 1f, 1f, 1f);
			}
			for(int i = 36;i < 72;i++)
			{
				float sin = (float)Math.sin(i * 5 * Math.PI/180f);
				float cos = (float)Math.cos(i * 5 * Math.PI/180f);
				Particle p = Particle.Create(new Vector2(owner.Center().x + cos * 60f, owner.Center().y + sin * 60f), new Vector2(MathUtils.random(-15, 15), MathUtils.random(-15, 15)), 2, new Color(0f, 1f, 1f, 1f), 2f, 0.5f, 1f);
				p.lights = true;
				p.lightSize = 16;
				p.lightColor = new Color(0f, 1f, 1f, 1f);
			}
		}
		else if(this.id == 24)
		{
			Sprite sprite = new Sprite(Content.stunmark);
			sprite.setPosition(owner.Center().x - 18, owner.position.y + (owner.height * owner.scale) + 32);
			sprite.setRotation(-Main.gameTick * 3);
			sprite.draw(batch);
		}
		else if(this.id == 25)
		{
			for(int i = 0;i < 10;i++)
			{
				Particle p = Particle.Create(owner.randomHitBoxPosition(), Vector2.Zero, 2, new Color(0f, 0f, 0f, 0f), 0f, 0.5f, 1f);
				p.setLight(64, Color.PURPLE);
			}
		}
		else if(this.id == 33)
		{
			int direction = owner.directionByVelocity();
			if(owner instanceof Player)
				direction = ((Player)owner).direction;
			
			Particle p = Particle.Create(owner.randomHitBoxPosition(), new Vector2(-800 * direction, 0), 2, Color.WHITE, 0f, 0.25f, 1f);
			p.drawBack = true;
		}
		else if(this.id == 34)
		{
			for(int i = 0;i < 4;i++)
			{
				Color color = Constant.EXPLOSION_DEFAULT_SCHEME[MathUtils.random(0, 3)];
				Particle p = Particle.Create(owner.randomHitBoxPosition(), new Vector2(0f, 50f), 24, color, 0f, 1f, 1f);
				p.drawBack = i >= 1 ? true : false;
				p.lights = true;
				p.lightSize = 16;
				p.lightColor = p.color;
			}
		}
		else if(this.id == 36)
		{
			if(owner instanceof Player)
			{
				((Player)owner).playAnim2(Content.handPointing, 1, 2, true, false, false, new Vector2(-48, 0));
				if(this.ticks % 5 == 0)
				{
					Projectile p = Projectile.Summon(107, owner.Center(), owner.velocity.cpy().setLength(50f), 0, 1f, owner);
					p.mirrored = owner.direction == -1;
				}
			}
		}
		else if(this.id == 37)
		{
			if(this.ticks % 60 == 0)
			{
				Particle p = Particle.Create(owner.Center(), Vector2.Zero, 22, new Color(0.1f, 1f, 1f, 1f), 0f, 2f, Math.max(owner.width+30, owner.height+30)/200f);
				p.parent = owner;
			}
		}
		else if(this.id == 38)
		{
			if(!(owner instanceof Player))
				return;
			
			/*Vector2 offset = player.getBodyPosition();

			Sprite sprite = new Sprite(Content.psa[7]);
			sprite.setPosition(offset.x-4, offset.y);
			sprite.flip(player.direction == -1, false);
			sprite.rotate(player.rotation);
			if(player.invincible > 0f)
			{
				float invalpha = Math.abs(player.invincible*100%100/100);
				invalpha = 1-invalpha*2;
				if(invalpha < 0.75f)
					invalpha = 0.75f;
				sprite.setAlpha(invalpha);
			}
			if(player.haveBuff(11) != -1)
			{
				sprite.setAlpha(0.5f);
			}
			sprite.draw(batch);*/
			
			Sprite sprite = new Sprite(Content.barrier);
			sprite.setPosition(player.Center().x - 64, player.Center().y - 64);
			sprite.setColor(new Color(0.48f, 0.15f, 0f, 0.6f));
			sprite.setAlpha(0.6f);
			sprite.setScale(2f);
			sprite.draw(batch);
		}
		else if(this.id == 40)
		{
			if(owner instanceof Player)
			{
				if(player.inventory[Constant.ITEMSLOT_RIGHT] != null && player.inventory[Constant.ITEMSLOT_RIGHT].itemclass == Constant.ITEMCLASS_SHIELD)
				{
					player.playAnim(player.inventory[Constant.ITEMSLOT_RIGHT].getAttackTexture(), player.inventory[Constant.ITEMSLOT_RIGHT].attackFrames, 2, false, true, false, new Vector2(40, 0));
					player.customAnimFrame = player.customAnimFrames-1;
				}
			}
		}
		else if(this.id == 44)
		{
			for(int i = 0;i < 2;i++)
			{
				Particle p = Particle.Create(owner.randomHitBoxPosition(), new Vector2(0f, MathUtils.random(50, 100)), 10, new Color(1f, 1f, 0.5f, 1f), 0f, 1f, 1f);
				p.drawBack = MathUtils.randomBoolean();
			}
		}
		else if(this.id == 46)
		{
			for(int i = 0;i < 2;i++)
			{
				Particle p = Particle.Create(owner.randomHitBoxPosition(), Vector2.Zero, 10, new Color(1f, 1f, 1f, 1f), 0f, 1f, 1f);
				p.drawBack = MathUtils.randomBoolean();
			}
		}
		else if(this.id == 49)
		{
			if(owner instanceof Player)
			{
				player.playAnim(Content.psa[21], 6, 2, false, true, true);
				player.playAnim2(Content.psa[20], 6, 2, false, true, true);

				for(int i = 0;i < 15;i++)
				{
					Vector2 pos = player.Center().add(player.direction * MathUtils.random(90f, 100f), -8f);
					Vector2 vel = new Vector2(player.direction * MathUtils.random(-120, -80), MathUtils.random(-300, 300));
					Particle p = Particle.Create(pos, vel, 2, Color.WHITE, 0f, 0.3f, 1f);
					p.drawFront = true;
					p.setLight(32, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ENERGY]);
					Lighting.Create(p.position, 256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ENERGY], p.duration, 0f);
				}
			}
		}
		else if(this.id == 51)
		{
			if(owner instanceof Player)
			{
				player.playAnim(Content.psa[21], 6, 1, false, true, true);
				player.playAnim2(Content.psa[20], 6, 1, false, true, true);

				if(Math.abs(owner.velocity.x) > 50)
				{
					for(int i = 0;i < 15;i++)
					{
						Vector2 pos = player.Center().add(player.direction * MathUtils.random(60f, 100f), -8f);
						Vector2 vel = new Vector2(player.direction * MathUtils.random(-120, -80), MathUtils.random(-300, 300));
						Particle p = Particle.Create(pos, vel, 2, Color.WHITE, 0f, 0.3f, 1f);
						p.drawFront = true;
						p.setLight(32, new Color(0f, 0.69f, 1f, 1f));
						Lighting.Create(p.position, 256, new Color(0f, 0.69f, 1f, 1f), p.duration, 0.5f);
					}
				}
			}
		}
		else if(this.id == 53)
		{
			if(owner instanceof Player)
			{
				player.playAnim(Content.psa[25], 6, 1, false, true, true);
				player.playAnim2(Content.psa[26], 6, 1, false, true, true);

				for(int i = 0;i < 15;i++)
				{
					Vector2 pos = player.Center().add(player.direction * MathUtils.random(60f, 100f), -8f);
					Vector2 vel = new Vector2(player.direction * MathUtils.random(-200, -160), MathUtils.random(-300, 300));
					Particle p = Particle.Create(pos, vel, 2, Color.WHITE, 0f, 0.3f, 1f);
					p.drawFront = true;
					p.lights = true;
					p.lightSize = 64;
					p.lightColor = new Color(0f, 0.69f, 1f, 1f);
				}
			}
		}
		else if(this.id == 57)
		{
			for(int i = 0;i < 3;i++)
			{
				Particle p = Particle.Create(owner.randomHitBoxPosition(), new Vector2(owner.direction * MathUtils.random(-200, -100), MathUtils.random(-50, 50)), 10, new Color(MathUtils.random()/2 + 0.5f, 0f, 0f, 1f), 0f, 1f, 1f);
				p.drawBack = true;
				p.lights = true;
				p.lightSize = 64;
				p.lightColor = new Color(1f, 0f, 0f, 1f);
			}
			Lighting.Create(owner.Center().add(MathUtils.random(-16, 16), MathUtils.random(-16, 16)), 512, new Color(1f, 0.2f, 0.2f, 1f), 1f);
		}
		else if(this.id == 59)
		{
			if(Main.gameTick % 10 == 0)
			{
				Lighting l = Lighting.Create(owner.Center(), 512, new Color(1f, 0.6f, 0.1f, 1f), 1f, 1f, true);
				l.vibrancy = 16;
			}
			Particle.Create(owner.randomHitBoxPosition(), Vector2.Zero, 10, new Color(1f, 0.6f, 0.1f, 1f), 0f, 0.5f, 1f);
		}
		else if(this.id == 61)
		{
			for(int i = 0;i < 4;i++)
			{
				float sin = (float)Math.sin((i * 90 + this.ticks * 2) * Math.PI/180);
				float cos = (float)Math.cos((i * 90 + this.ticks * 2) * Math.PI/180);
				float sin2 = (float)Math.pow(Math.sin(Math.max(((this.timeLeft-1) * 90/(this.lastTimeLeft-1)), 0) * Math.PI/180f), 2);
				float size = Math.max(owner.width, owner.height) + sin2 * 200;
				Vector2 pos = new Vector2(owner.Center().x + cos * size, owner.Center().y + sin * size);
				Vector2 vel = new Vector2(cos * 10, sin * 10);
				Particle p = Particle.Create(pos, vel, 2, new Color(1f, 0.1f, 0.1f, 1f), 0f, 1f, 1f);
				p.setLight(32, p.color);
				p.parent = owner;
			}
		}
		else if(this.id == 63)
		{
			float sin = (float)Math.sin(Main.gameTick * 5 * Math.PI/180f);
			float cos = (float)Math.cos(Main.gameTick * 5 * Math.PI/180f);
			Vector2 pos = owner.Center().add(cos * 80, sin * 20f + 4);
			Color color = new Color(1f, 1f, 1f, 1f).fromHsv(Main.gameTick*3, 1f, 1f);
			Color lc = color.cpy();
			if(owner instanceof Player)
			{
				if(((Player)owner).haveBuff(65) != -1)
				{
					color = Color.WHITE;
					lc = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ENERGY];
				}
			}
			Particle p = Particle.Create(pos, Vector2.Zero, 2, color, 0f, 1f, 1f);
			p.parent = owner;
			p.rotation = Main.gameTick * 10;
			p.setLight(16, lc);
			if(sin > 0)
				p.drawBack = true;
		}
		else if(this.id == 65)
		{
			for(int i = 0;i < 2;i++)
			{
				float angle = MathUtils.random(360f);
				float sin = (float)Math.sin(angle * Math.PI/180f);
				float cos = (float)Math.cos(angle * Math.PI/180f);
				float str = MathUtils.random(0f, 100f);
				Particle p = Particle.Create(owner.Center().add(cos * str, sin * str), new Vector2(0f, 50f), 2, Color.WHITE, 0f, 1f, 1f);
				p.setLight(16, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ENERGY]);
				p.drawBack = true;
			}
			if(this.ticks % 5 == 0)
			{
				Lighting l = Lighting.Create(owner.Center(), 2048, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ENERGY], 1f, 1f, true);
				l.vibrancy = 40f;
			}
		}
		else if(this.id == 73)
		{
			if(this.ticks % 5 == 0)
			{
				Lighting l = Lighting.Create(owner.Center(), 512, new Color(0.9f, 0.95f, 0.8f, 1f), 1f, 1f, true);
				l.vibrancy = 20f;
			}
		}
		else if(this.id == 75)
		{
			if(this.ticks % 15 == 0)
			{
				for(int i = 0;i < 72;i++)
				{
					Vector2 pos = new Vector2(1f, 0f).setAngle(i*5).setLength(50 + (240 - this.ticks * 4)).add(owner.Center());
					Particle.Create(pos, Vector2.Zero, 2, new Color(0.5f, 0f, 0.5f, 1f), 0f, 0.5f, 1f).setLight(32, Color.PURPLE);
				}
			}
		}
		else if(this.id == 76)
		{
			for(int i = 0;i < 4;i++)
			{
				Vector2 pos = owner.Center().add(new Vector2(100f, 0f).setAngle(i * 90 + Constant.gameTick()));
				Particle.Create(pos, Vector2.Zero, 16, new Color(0.92f, 0.96f, 0.8f, 1f), 0f, 0.5f, 1f).setLight(16, new Color(0.92f, 0.96f, 0.8f, 1f)).rotation = Constant.gameTick() + i * 55;
			}
		}

		/*else if(this.id == 30)
		{
			if(owner instanceof Player)
			{
				Player player = (Player)owner;
				for(int i = 0;i < 2;i++)
				{
					Vector2 pos = player.getHandPosition();
					Vector2 offset = player.getItemHandOffset();
					pos.x += offset.x;
					Particle p = Particle.Create(pos.add(MathUtils.random(12), MathUtils.random(12)), Vector2.Zero, 2, Color.RED, -2f, 1f, 1f);
					if(player.direction == -1)
						p.position.x -= 12;
					
					p.collisions = true;
				}
			}
		}*/
	}
	
	public void backDraw(Entity owner, SpriteBatch batch)
	{
		if(this.id == 56)
		{
			Sprite sprite = new Sprite(Content.extras[18]);
			float size = Math.max(owner.width, owner.height)+60;
			sprite.setSize(size, size);
			sprite.setRegion(0, 100*((Constant.gameTick()/2)%10), 100, 100);
			sprite.setPosition(owner.Center().x-size/2f, owner.Center().y-size/2f);
			sprite.setAlpha(Math.min(1f, this.timeLeft));
			sprite.draw(batch);
		}
	}
	
	public void onEnd(Entity owner)
	{
		if(owner != null && owner instanceof Player)
		{
			Player player = (Player)owner;
			for(int i = 0;i < player.skillSlotAvailable;i++)
			{
				Skill s = player.skills[i];
				if(s != null && s.waitForBuff == this.id)
				{
					s.setCooldown(s.getCooldown(player));
				}
			}

			if(this.id == 66)
			{
				player.healEx(this.stacks);
				Particle p = Particle.Create(player.Center(), Vector2.Zero, 30, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_GOOD].cpy().mul(1f, 1f, 1f, 0.35f), 0f, 1f, 0.716f);
				p.parent=player;
				p.drawBack=true;
			}
		}
		
		Entity origin = this.getOrigin();

		if(origin != null && origin instanceof Player)
		{
			Player player = (Player)origin;
			
			Color[] quadcolors = new Color[4];
			quadcolors[0] = new Color(1f, 0.02f, 1f, 1f);
			quadcolors[1] = new Color(0.8f, 0.02f, 0.8f, 1f);
			quadcolors[2] = new Color(0.5f, 0.015f, 0.5f, 1f);
			quadcolors[3] = new Color(0.3f, 0.01f, 0.3f, 1f);
			
			if(this.id == 75)
			{
				Skill s = player.getSkill(127);
				if(s != null)
				{
					Main.createCustomExplosion(owner.Center(), 256, 1f, false, quadcolors, quadcolors[0]);
					Main.doEarthquake(15);
					for(Monster m : Constant.getMonstersInRange(owner, 256))
					{
						m.hurtDmgVar(s.getInfoValueI(player, 1), Main.directionFromTo(owner, m), 1f, player.canCritical(m), Constant.DAMAGETYPE_DARKNESS, player);
						if(m == owner)
							m.Stun(2f, player);
					}
				}
			}
		}
	}

	private void ResetStats()
	{
		this.name = "";
		this.description = "";
		this.stacks = 1;
		this.timeLeft = 0f;
		this.canStack = false;
		this.maxStacks = 1;
		this.debuff = false;
		this.originUid = -1;
		this.originInfo = "";
		this.shield = false;
	}

	public Texture getTexture()
	{
		return Content.buffs[this.id - 1];
	}
	
	public Entity getOrigin()
	{
		if(this.originInfo.equalsIgnoreCase("monster"))
		{
			for(Monster m : Constant.getMonsterList(false))
			{
				if(m.uid == this.originUid)
				{
					return m;
				}
			}
		}
		else if(this.originInfo.equalsIgnoreCase("player"))
		{
			for(Player p : Constant.getPlayerList())
			{
				if(p.whoAmI == this.originUid)
				{
					return p;
				}
			}
		}
		else if(this.originInfo.equalsIgnoreCase("projectile"))
		{
			for(Projectile p : Constant.getProjectileList())
			{
				if(p.whoAmI == this.originUid)
				{
					return p;
				}
			}
		}
		else if(this.originInfo.equalsIgnoreCase("npc"))
		{
			for(NPC n : Constant.getNPCList())
			{
				if(n.id == this.originUid)
				{
					return n;
				}
			}
			NPC ret = new NPC();
			ret.SetInfos(this.originUid, true);
			return ret;
		}
		return null;
	}
}