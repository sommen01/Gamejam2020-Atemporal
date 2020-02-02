package com.roguelike.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.game.Content;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;

public class Particle extends Entity
{
	public int id;
	public int frames;
	public int frameCounterTicks;
	public int currentFrame;
	public int currentFrameTicks;
	public int deathType;
	public float duration;
	public float maxDuration;
	public Color color = Color.WHITE;
	public float gravity;
	public float alpha;
	public boolean drawBack = false;
	public boolean drawFront = false;
	public Entity parent;
	public boolean fixAlpha = false;
	public boolean lights = false;
	public int lightSize = 256;
	public Color lightColor = Color.WHITE;
	public int lightIntensity = 1;
	public float minAlpha = 1f;
	public boolean loseSpeed = false;
	public float loseSpeedMult = 0.985f;
	public boolean killOnCollision = false;
	public int ticks = 0;
	public float specialAi = 0;
	public boolean collisions = false;
	public float lightingSize = 0f;
	public Color lightingColor = Color.WHITE;
	public transient Texture customTexture;
	public boolean forceDraw = false;
	
	public Particle()
	{
		this.color = new Color();
	}
	
	public void SetInfos(int value)
	{
		this.ResetValues();
		this.id = value;
		if(this.id == 1)
		{
			this.width = 64;
			this.height = 64;
			this.frames = 9;
			this.frameCounterTicks = 0;
			this.deathType = 0;
			this.fixAlpha = true;
		}
		else if(this.id == 2)
		{
			this.width = 4;
			this.height = 4;
			this.deathType = 1;
			this.loseSpeed = true;
			this.frameCounterTicks = 1;
		}
		else if(this.id == 3)
		{
			this.width = 16;
			this.height = 16;
			this.deathType = 1;
		}
		else if(this.id == 4)
		{
			this.width = 30;
			this.height = 30;
			this.deathType = 1;
		}
		else if(this.id == 5)
		{
			this.width = 28;
			this.height = 40;
			this.deathType = 2;
			this.frames = 9;
			this.fixAlpha = true;
		}
		else if(this.id == 6)
		{
			this.width = 32;
			this.height = 32;
			this.deathType = 0;
			this.frames = 9;
			this.frameCounterTicks = 3;
		}
		else if(this.id == 7)
		{
			this.width = 37;
			this.height = 37;
			this.deathType = 0;
			this.frames = 13;
			this.frameCounterTicks = 5;
		}
		else if(this.id == 8)
		{
			this.width = 32;
			this.height = 32;
			this.deathType = 2;
			this.frames = 16;
			this.frameCounterTicks = 0;
		}
		else if(this.id == 9)
		{
			this.width = 32;
			this.height = 32;
			this.deathType = 1;
			this.frames = 13;
			this.frameCounterTicks = 2;
		}
		else if(this.id == 10)
		{
			this.width = 16;
			this.height = 16;
			this.deathType = 1;
		}
		else if(this.id == 11)
		{
			this.deathType = 0;
			this.width = 31;
			this.height = 31;
			this.frames = 15;
			this.frameCounterTicks = 0;
		}
		else if(this.id == 12)
		{
			this.deathType = 1;
			this.width = 32;
			this.height = 32;
		}
		else if(this.id == 13)
		{
			this.deathType = 1;
			this.width = 11;
			this.height = 10;
		}
		else if(this.id == 14)
		{
			this.deathType = 1;
			this.width = 48;
			this.height = 48;
		}
		else if(this.id == 15)
		{
			this.deathType = 0;
			this.width = 32;
			this.height = 32;
			this.frames = 4;
			this.frameCounterTicks = 2;
		}
		else if(this.id == 16)
		{
			this.deathType = 1;
			this.width = 10;
			this.height = 10;
			this.frames = 4;
			this.frameCounterTicks = 2;
		}
		else if(this.id == 17)
		{
			this.deathType = 0;
			this.width = 16;
			this.height = 16;
			this.frames = 8;
			this.frameCounterTicks = 2;
		}
		else if(this.id == 18)
		{
			this.deathType = 0;
			this.width = 96;
			this.height = 96;
			this.frames = 4;
			this.frameCounterTicks = 2;
		}
		else if(this.id == 19)
		{
			this.deathType = 0;
			this.width = 15;
			this.height = 3;
			this.frames = 8;
			this.frameCounterTicks = 2;
		}
		else if(this.id == 20)
		{
			this.deathType = 1;
			this.width = 16;
			this.height = 16;
			this.loseSpeed = true;
		}
		else if(this.id == 21)
		{
			this.deathType = 0;
			this.width = 31;
			this.height = 31;
			this.frames = 12;
			this.frameCounterTicks = 0;
		}
		else if(this.id == 22)
		{
			this.width = 215;
			this.height = 215;
			this.deathType = 2;
		}
		else if(this.id == 23)
		{
			this.width = 195;
			this.height = 195;
			this.deathType = 2;
		}
		else if(this.id == 24)
		{
			this.width = 16;
			this.height = 16;
			this.deathType = 2;
		}
		else if(this.id == 25)
		{
			this.width = 6;
			this.height = 6;
			this.deathType = 1;
		}
		else if(this.id == 26)
		{
			this.width = 224;
			this.height = 52;
			this.frames = 5;
			this.frameCounterTicks = 3;
			this.deathType = 0;
			this.fixAlpha = true;
		}
		else if(this.id == 27)
		{
			this.width = 28;
			this.height = 26;
			this.deathType = 1;
		}
		else if(this.id == 28)
		{
			this.width = 32;
			this.height = 32;
			this.deathType = 2;
			this.frames = 16;
			this.frameCounterTicks = 0;
		}
		else if(this.id == 29)
		{
			this.deathType = 1;
			this.width = 300;
			this.height = 300;
		}
		else if(this.id == 30)
		{
			this.deathType = 1;
			this.width = 300;
			this.height = 300;
		}
		else if(this.id == 31)
		{
			this.width = 32;
			this.height = 32;
			this.deathType = 2;
			this.frames = 16;
			this.frameCounterTicks = 0;
		}
		else if(this.id == 32)
		{
			this.width = 16;
			this.height = 16;
			this.deathType = 1;
		}
	}
	private void ResetValues()
	{
		this.id = 0;
		this.frames = 1;
		this.frameCounterTicks = 0;
		this.currentFrame = 0;
		this.currentFrameTicks = 0;
		this.gravity = 0;
		this.color = Color.WHITE;
		this.alpha = 1f;
		this.drawBack = false;
		this.parent = null;
		this.fixAlpha = false;
		this.loseSpeedMult = 0.985f;
		this.drawFront = false;
		this.lights = false;
		this.lightSize = 128;
		this.lightColor = Color.WHITE;
		this.minAlpha = 1f;
		this.loseSpeed = false;
		this.killOnCollision = false;
		this.specialAi = 0;
		this.lightingSize = 0;
		this.collisions = false;
	}
	@Override
	public void update(float delta)
	{
		this.duration -= delta;
		this.ticks++;
		if(this.killOnCollision && this.ticks >= 4)
		{
			if(Main.worldMap.doesRectCollideWithMap(this.position.x, this.position.y, (int)(this.width*this.scale), (int)(this.height*this.scale)))
			{
				this.active = false;
			}
		}

		this.velocity.y += this.gravity;

		if(this.loseSpeed)
		{
			this.velocity.scl(this.loseSpeedMult);
		}
		if(this.collisions)
		{
			float addAmountX = this.velocity.x * delta;
			float newX = this.position.x + addAmountX;
			float addAmountY = this.velocity.y * delta;
			float newY = this.position.y + addAmountY;

			if(!Main.worldMap.doesRectCollideWithMap(this.position.x, newY, (int)(this.width * this.scale), (int)(this.height * this.scale)))
			{
				this.position.y = newY;
			}
			else
			{
				if(this.velocity.y < 0f)
				{
					//this.position.y = (float) (Math.floor(this.position.y) - Math.floor(this.position.y) % Tile.TILE_SIZE);
					//this.position.y -= Math.floor(this.position.y) % Tile.TILE_SIZE;
				}
				else if(this.velocity.y > 0f)
				{
					this.position.y = this.position.y;
				}
				this.velocity.y *= -0.5f;
			}

			if(!Main.worldMap.doesRectCollideWithMap(newX, this.position.y, (int)(this.width * this.scale), (int)(this.height * this.scale)))
			{
				this.position.x = newX;
			}
			else
			{
				if(this.velocity.x < 0f)
				{
					//this.position.x = (float) (Math.floor(this.position.x) - Math.floor(this.position.x) % Tile.TILE_SIZE);
					//this.position.x -= Math.floor(this.position.x) % Tile.TILE_SIZE;
				}
				else if(this.velocity.x > 0f)
				{
					//this.position.y = (float) (Math.floor(this.position.y) + (Tile.TILE_SIZE - Math.floor(this.position.y) % Tile.TILE_SIZE) - (this.height - Tile.TILE_SIZE));
					//this.position.x = this.position.x;
				}

				this.velocity.x *= -0.5f;
			}
		}
		if(this.deathType < 2)
		{
			this.currentFrameTicks++;
			if(this.currentFrameTicks >= this.frameCounterTicks)
			{
				this.currentFrameTicks = 0;
				this.currentFrame++;
			}
			if(this.currentFrame >= this.frames)
			{
				if(this.deathType == 0)
					this.active = false;
				else
					this.currentFrame = 0;
			}
			if(!this.fixAlpha)
			{
				if(this.duration < this.maxDuration/2f)
				{
					this.alpha = (this.duration)/(maxDuration/2f);
					if(this.alpha > minAlpha)
						this.alpha = minAlpha;
				}
				else
				{
					this.alpha = minAlpha;
				}
			}
			if(this.duration <= 0f)
			{
				this.active = false;
			}
		}
		if(this.id == 5)
		{
			if(this.currentFrame < 2)
			{

				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 5)
				{
					this.currentFrameTicks = 0;
					this.currentFrame = (this.currentFrame + 1) % this.frames;
				}
			}
			if(this.duration <= this.maxDuration/2f && this.currentFrame < 6)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 5)
				{
					this.currentFrameTicks = 0;
					this.currentFrame = (this.currentFrame + 1) % this.frames;
				}
			}
			if(this.duration <= 0f)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= 5)
				{
					this.currentFrameTicks = 0;
					this.currentFrame = this.currentFrame + 1;
					if(this.currentFrame == 9)
						this.active = false;
				}
			}
		}
		else if(this.id == 8 || this.id == 28 || this.id == 31)
		{
			if(this.duration < 0f)
			{
				this.currentFrameTicks++;
				if(this.currentFrameTicks >= this.frameCounterTicks)
				{
					this.currentFrameTicks = 0;
					int sum = 1;
					if(this.frameCounterTicks < 0)
						sum += Math.abs(this.frameCounterTicks);
					
					this.currentFrame = this.currentFrame + sum;
					if(this.currentFrame == this.frames)
						this.active = false;
				}
			}
			if(this.specialAi >= 1)
			{
				this.width += specialAi;
				this.position.y += (specialAi)/2;
				if(specialAi % 2 == 1)
				{
					this.position.y += 0.5f;
				}
				Particle p = Particle.Create(new Vector2(this.position.x + MathUtils.random(-this.height/2 - 16, this.height/2 + 16), this.position.y - this.width/2 + MathUtils.random(-16, 16)), Vector2.Zero, 6, this.color, 0f, 1f, 1f);
				p.drawFront = true;
				p.parent = this.parent;

				for(int i = 0;i < 2;i++)
				{
					p = Particle.Create(new Vector2(this.position.x + MathUtils.random(-this.height/2, this.height/2), this.position.y + MathUtils.random(-this.width/2, this.width/2)), new Vector2(0, MathUtils.random(8, 32)), 7, new Color(this.color.r * 0.8f, this.color.g * 0.8f, this.color.b * 0.8f, 1f), 1f, 1f, 1f);
					p.drawFront = true;
					p.parent = this.parent;
				}
			}
		}
		else if(this.id == 7)
		{
			this.rotation += 15f;
		}
		else if(this.id == 9)
		{
			this.rotation += 10f;
		}
		else if(this.id == 10)
		{
			float prop = 0.05f/(0.05f+(this.specialAi/100f));
			float quant = 30*prop;
			float incr = 0.05f + this.specialAi/100f;
			if(this.ticks % quant < quant/2f)
			{
				this.scale -= incr;
			}
			else
			{
				this.scale += incr;
			}
		}
		else if(this.id == 12 || this.id == 29 || this.id == 30 || this.id == 32)
		{
			this.scale *= 0.9f;
		}
		else if(this.id == 13)
		{
			this.rotation += 5f;
		}
		else if(this.id == 20)
		{
			this.scale = this.duration/this.maxDuration;
		}
		else if(this.id == 22)
		{
			if(this.duration < 0)
			{
				this.active = false;
			}
			if(this.duration < 1f)
			{
				this.alpha = this.duration;
			}
			else if(this.maxDuration - this.duration < 1)
			{
				this.alpha = this.maxDuration - this.duration;
			}
			else
			{
				this.alpha = 1;
			}
			this.scale = this.specialAi*(float) (1f-Math.pow(0.9f, 1+(this.maxDuration-this.duration)*100));
		}
		else if(this.id == 23)
		{
			this.scale = this.specialAi*(float) (1f-Math.pow(0.9f, 1+(this.maxDuration-this.duration)*100));
			this.alpha = this.duration/this.maxDuration;
			if(this.duration < 0)
			{
				this.active = false;
			}
		}
		else if(this.id == 24)
		{
			this.duration = 1f;
			if(this.ticks > 1)
			{
				this.scale = this.specialAi * (float) (Math.pow(0.9f, 1+(Main.ticksToSeconds(this.ticks))*20));
				if(this.scale < 0.5f)
					this.duration = this.scale;
				
				if(this.scale < 0.1f)
				{
					this.active = false;
				}
				else if(this.scale < 0.2f)
				{
					this.alpha = (this.scale-0.1f)*10f;
				}
			}
		}
		else if(this.id == 25)
		{
			if(this.ticks%4 == 0)
			{
				Particle p = Particle.Create(this.randomHitBoxPosition(), Vector2.Zero, 15, this.color, 0f, 1f, this.scale);
				if(this.lights)
				{
					p.setLight(this.lightSize, this.lightColor);
				}
			}
		}
		else if(this.id == 27)
		{
			/*this.scale -= 0.05f;
			this.duration = this.scale;
			if(this.scale < 0f)
			{
				this.active = false;
			}*/
			this.deathType = 1;
		}
		
		this.position.x += this.velocity.x * delta;
		this.position.y += this.velocity.y * delta;
		if(this.parent != null)
		{
			if(this.parent.getClass() != Player.class || ((Player)this.parent).untargetable < 0f)
			{
				/*this.position.x += parent.velocity.x * delta;
				this.position.y += parent.velocity.y * delta;*/
				Vector2 dif = new Vector2(parent.position.x - parent.lastPosition.x, parent.position.y - parent.lastPosition.y);
				this.position.x += dif.x;
				this.position.y += dif.y;
			}
			else
			{
				this.parent = null;
			}
		}
	}
	
	public Particle setCustomTexture(Texture texture, int frames)
	{
		this.customTexture = texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight()/frames;
		this.frames = frames;
		return this;
	}

	public static Particle Create(Vector2 pos, Vector2 vel, int type, Color color, float gravity, float duration, float scale)
	{
		if(AHS.isUp || Main.pause || Main.me < 0 || Main.loadingBasics || Main.loadingMap)
			return new Particle();
		
		Particle p = new Particle();
		p.myMapX = Main.player[Main.me].myMapX;
		p.myMapY = Main.player[Main.me].myMapY;
		p.SetInfos(type);
		p.active = true;
		p.position = pos.cpy();
		p.velocity = vel.cpy();
		p.gravity = gravity;
		p.color = color;
		p.duration = duration;
		p.maxDuration = duration;
		p.scale = scale;
		p.rotation = 0;
		p.ticks = 0;
		
		if(type == 22 || type == 23)
		{
			p.specialAi = scale;
		}
		else if(type == 24)
		{
			p.maxDuration = 1.2f;
			p.duration = 1.2f;
			p.specialAi = scale;
		}
		Main.particleex.add(p);
		
		return p;
	}

	public void draw(SpriteBatch batch)
	{
		if(this.id == 22 || this.id == 23 || Main.getScreen().overlaps(this.hitBox()) || this.forceDraw)
		{
			if(this.color == null)
			{
				this.color = Color.WHITE;
			}
			if(this.id == 18)
			{
				Sprite sprite = new Sprite(Content.p17l);
				sprite.setRegion(0, this.currentFrame*this.height, this.width, this.height);
				sprite.setSize(this.width*this.scale, this.height*this.scale);
				sprite.setPosition(this.position.x - (this.width*this.scale/2), this.position.y - (this.height*this.scale/2));

				sprite.setColor(this.color.r, this.color.g, this.color.b, 1f);
				sprite.setAlpha(this.alpha * this.color.a);
				sprite.setOriginCenter();
				sprite.rotate(this.rotation);
				sprite.draw(batch);
			}
			
			Sprite sprite = new Sprite(this.customTexture == null ? Content.particles[this.id-1] : this.customTexture);
			sprite.setRegion(0, this.currentFrame*this.height, this.width, this.height);
			if(this.id != 8 && this.id != 28 && this.id != 31)
			{
				sprite.setSize(this.width*this.scale, this.height*this.scale);
				sprite.setPosition(this.position.x - (this.width*this.scale/2), this.position.y - (this.height*this.scale/2));
			}
			else
			{
				sprite.setSize(this.width, this.height*this.scale);
				sprite.setPosition(this.position.x-this.width/2, this.position.y-(this.height*this.scale/2));
			}
			if(this.id != 18)
				sprite.setColor(this.color.r, this.color.g, this.color.b, 1f);
			

			sprite.setAlpha(this.alpha * this.color.a);
			sprite.setOriginCenter();
			sprite.rotate(this.rotation);
			sprite.draw(batch);
		}
	}
	public void drawLight(SpriteBatch batch, boolean trueLight)
	{
		if(this.id == 22 || this.id == 23 || Main.getScreen().overlaps(this.hitBox()) || this.forceDraw)
		{
			Sprite sprite = new Sprite(trueLight?Content.light:Content.light2);
			float size = this.lightSize*2;
			if(this.duration < 0.5f)
			{
				size = (this.lightSize*this.duration*2)*2;
			}
			sprite.setSize(size, size);
			sprite.setColor(this.lightColor);
			Vector2 center = new Vector2(this.position.x - (this.width*this.scale/2), this.position.y - (this.height*this.scale/2));
			sprite.setPosition(center.x-size/2+(this.width*this.scale/2), center.y-size/2+(this.height*this.scale/2));
			for(int i = 0;i < this.lightIntensity;i++)
				sprite.draw(batch);
		}
	}
	public Particle setLight(int i, Color color2)
	{
		this.lights = true;
		this.lightSize = i;
		this.lightColor = color2;
		return this;
	}
	
	public Particle setParent(Entity parent)
	{
		this.parent = parent;
		return this;
	}
	
	@Override
	public Vector2 Center()
	{
		return this.position.cpy();
	}
}
