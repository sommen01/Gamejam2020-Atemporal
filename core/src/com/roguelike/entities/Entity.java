package com.roguelike.entities;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.Constant;
import com.roguelike.game.Content;
import com.roguelike.game.Main;
import com.roguelike.game.MathEx;
import com.roguelike.world.GameMap;
import com.roguelike.world.Tile;

public abstract class Entity
{
	public Vector2 position;
	public Vector2 velocity;
	public boolean active;
	public int width;
	public transient int direction = 1;
	public int height;
	public float rotation = 0f;
	public int mapDisplayIndex = -1;
	public Vector2 savedPosition;
	public Vector2 savedVelocity;
	
	public transient Vector2 lastPosition;
	
	public String name;
	public int tagSymbol;
	public int myMapX;
	public int myMapY;
	public float scale = 1f;
	
	public transient ShaderProgram customShader;
	public transient HashMap<String, String[]> customShaderUValues;
	public transient HashMap<Integer, Texture> customShaderUTextures;

	public Entity()
	{
		this.position = Vector2.Zero.cpy();
		this.lastPosition = Vector2.Zero.cpy();
		this.velocity = Vector2.Zero.cpy();
		this.active = false;
		this.rotation = 0f;
		this.tagSymbol = 0;
		this.direction = 1;
		this.customShaderUValues = new HashMap<String, String[]>();
		this.customShaderUTextures = new HashMap<Integer, Texture>();
	}

	public void preUpdate(float delta)
	{
		this.lastPosition = this.position.cpy();
		
		this.update(delta);
	}

	public abstract void update(float delta);

	public Vector2 Center()
	{
		return new Vector2(this.position.x + (this.width * this.scale)/2, this.position.y + (this.height * this.scale)/2);
	}

	/*public Rectangle hitBox()
	{
		return new Rectangle(this.position.x, this.position.y, this.width * this.scale, this.height * this.scale);
	}*/
	
	public Rectangle hitBox()
	{
		return new Rectangle(this.Center().x-(this.width * this.scale)/2f, this.Center().y-(this.height * this.scale)/2f, this.width * this.scale, this.height * this.scale);
	}
	
	public Polygon perfectHitBox()
	{
		Polygon r = new Polygon(new float[] {
				0, 0, 
				this.width * this.scale, 0, 
				this.width * this.scale, this.height * this.scale, 
				0, this.height * this.scale});

		r.setOrigin(this.width * this.scale/2f, this.height * this.scale/2f);
		r.rotate(this.rotation);
		r.setPosition(this.Center().x - (this.width * this.scale / 2f), this.Center().y - (this.height * this.scale / 2f));
		return r;
	}

	public Vector2 randomHitBoxPosition()
	{
		Vector2 pos = new Vector2(MathUtils.random(-this.width*this.scale/2f, this.width*this.scale/2f), MathUtils.random(-this.height*this.scale/2f, this.height*this.scale/2f));
		pos.rotate(this.rotation);
		pos.add(this.Center());
		return pos;
	}
	
	public boolean sameMapAs(Entity entity2)
	{
		return (this.myMapX == entity2.myMapX && this.myMapY == entity2.myMapY);
	}
	
	public int directionByVelocity()
	{
		return (this.velocity.x > 0 ? 1 : -1);
	}
	
	public boolean unstuck()
	{
		GameMap map = Constant.tryGetMapForEntity(this);
		if(map == null)
			return false;
		
		if(map.doesRectCollideWithMap(this.position.x, this.position.y, this.width, this.height))
		{
			this.position = map.getNextestFreeSpace(this.position.x, this.position.y, this.width, this.height, 
					(this.velocity.x > 0 ? 1 : -1), (this.velocity.y > 0 ? 1 : -1)).scl(Tile.TILE_SIZE);
			return true;
		}
		return false;
	}
	
	public void redirectVelocity(Vector2 destination)
	{
		Vector2 vel = new Vector2(this.velocity.len(), 0f).rotate(Main.angleBetween(this.Center(), destination));
		this.velocity = vel;
	}
	
	public void setRotationToVelocity()
	{
		this.rotation = this.velocity.angle();
	}

	public float getScaledWidth() {
		return width;
	}

	public float getScaledHeight() {
		return height;
	}
	
	public int getFakeY()
	{
		return Main.getFakeY(this.myMapY);
	}

	public int blocksAhead()
	{
		return blocksAhead(3, true);
	}
	
	public int blocksAheadDown()
	{
		return blocksAheadDown(3, true);
	}
	
	public int blocksAhead(int max, boolean byDirection)
	{
		int count = 0;
		GameMap entmap = Constant.tryGetMapForEntity(this);
		if(entmap == null)
			return 0;
		
		boolean condition = this.direction < 0;
		if(!byDirection)
			condition = new Vector2(1f, 0f).setAngle(this.rotation).x < 0f;
		
		if(condition)
		{
			int mx = (int)(this.position.x/64);
			int my = (int)(this.position.y/64);
			if(mx > 0 && my < entmap.height-2)
			{
				for(int i = Main.clamp(0, my, entmap.height-1);i < Math.min(my+max, entmap.height);i++)
				{
					if(entmap.map[i][mx-1] != null && entmap.map[i][mx-1].isCollidable())
					{
						count++;
					}
				}
			}
		}
		else
		{
			int mx = (int)Math.ceil((this.position.x+this.width*this.scale)/64);
			int my = (int)(this.position.y/64);
			if(mx < entmap.width && my < entmap.height-2)
			{
				for(int i = Main.clamp(0, my, entmap.height-1);i < Math.min(my+max, entmap.height);i++)
				{
					if(entmap.map[i][mx] != null && entmap.map[i][mx].isCollidable())
					{
						count++;
					}
				}
			}
		}
		return count;
	}
	
	public int blocksAheadDown(int max, boolean byDirection)
	{
		int count = 0;
		GameMap entmap = Constant.tryGetMapForEntity(this);
		if(entmap == null)
			return 0;

		boolean condition = this.direction < 0;
		if(!byDirection)
			condition = new Vector2(1f, 0f).setAngle(this.rotation).x < 0f;
		
		if(condition)
		{
			int mx = (int)(this.position.x/64);
			int my = (int)(this.position.y/64);
			if(mx > 0 && my < entmap.height-2)
			{
				for(int i = Math.max(0, Math.min(my+max, entmap.height)-1);i >= Math.max(0, Math.min(my+max, entmap.height));i--)
				{
					if(entmap.map[i][mx-1] != null && entmap.map[i][mx-1].isCollidable())
					{
						count++;
					}
				}
			}
		}
		else
		{
			int mx = (int)Math.ceil((this.position.x+this.width*this.scale)/64);
			int my = (int)(this.position.y/64);
			if(mx < entmap.width && my < entmap.height-2)
			{
				for(int i = Math.max(0, Math.min(my+max, entmap.height)-1);i >= Math.max(0, Math.min(my+max, entmap.height));i--)
				{
					if(entmap.map[i][mx] != null && entmap.map[i][mx].isCollidable())
					{
						count++;
					}
				}
			}
		}
		return count;
	}

	public int blocksBehind()
	{
		int count = 0;
		if(this.direction == 1)
		{
			int mx = (int)(this.position.x/64);
			int my = (int)(this.position.y/64);
			if(mx > 0)
			{
				if(Constant.tryGetMapForEntity(this).map[my][mx-1] != null && Constant.tryGetMapForEntity(this).map[my][mx-1].isCollidable())
				{
					count++;
				}
				if(Constant.tryGetMapForEntity(this).map[my+1][mx-1] != null && Constant.tryGetMapForEntity(this).map[my+1][mx-1].isCollidable())
				{
					count++;
				}
				if(Constant.tryGetMapForEntity(this).map[my+2][mx-1] != null && Constant.tryGetMapForEntity(this).map[my+2][mx-1].isCollidable())
				{
					count++;
				}
			}
		}
		else
		{
			int mx = (int)Math.ceil((this.position.x+this.width)/64);
			int my = (int)(this.position.y/64);
			if(mx < Constant.tryGetMapForEntity(this).width)
			{
				if(Constant.tryGetMapForEntity(this).map[my][mx] != null && Constant.tryGetMapForEntity(this).map[my][mx].isCollidable())
				{
					count++;
				}
				if(Constant.tryGetMapForEntity(this).map[my+1][mx] != null && Constant.tryGetMapForEntity(this).map[my+1][mx].isCollidable())
				{
					count++;
				}
				if(Constant.tryGetMapForEntity(this).map[my+2][mx] != null && Constant.tryGetMapForEntity(this).map[my+2][mx].isCollidable())
				{
					count++;
				}
			}
		}
		return count;
	}
	
	public void setMap(Entity e)
	{
		setMap(e.myMapX, e.myMapY);
	}
	
	public void setMap(int x, int y)
	{
		this.myMapX = x;
		this.myMapY = y;
	}
	
	public void drawEnt(SpriteBatch batch, Sprite sprite)
	{
		if(this.customShader != null)
		{
			ShaderProgram bkp = batch.getShader();
			batch.setShader(this.customShader);
			if(this.customShaderUValues.size() > 0)
			{
				for(String key : this.customShaderUValues.keySet())
				{
					String[] strValues = this.customShaderUValues.get(key);
					MathEx[] value = new MathEx[strValues.length];
					for(int i = 0;i < value.length;i++)
					{
						value[i] = new MathEx();
						if(strValues[i].charAt(0) == '!')
						{
							value[i].useAsInteger = true;
							value[i].expression = strValues[i].substring(1, strValues[i].length());
						}
						else
						{
							value[i].useAsInteger = false;
							value[i].expression = strValues[i];
						}
					}
					if(value.length == 1)
					{
						if(value[0].useAsInteger)
						{
							int ret = (int)(MathEx.evaluate(makeShaderReplacements(value[0].expression, sprite)));
							this.customShader.setUniformi(key, ret);
						}
						else
						{
							float ret = (float)(MathEx.evaluate(makeShaderReplacements(value[0].expression, sprite)));
							this.customShader.setUniformf(key, ret);
						}
					}
					else
					{
						float[] array = new float[value.length];
						for(int i = 0;i < value.length;i++)
						{
							array[i] = (float)(MathEx.evaluate(makeShaderReplacements(value[i].expression, sprite)));
						}
						if(array.length == 2)
						{
							this.customShader.setUniform2fv(key, array, 0, 2);
						}
						else if(array.length == 3)
						{
							this.customShader.setUniform3fv(key, array, 0, 3);
						}
						else if(array.length == 4)
						{
							this.customShader.setUniform4fv(key, array, 0, 4);
						}
					}
				}
			}
			if(this.customShaderUTextures.size() > 0)
			{
				for(int key : this.customShaderUTextures.keySet())
				{
					Content.setShaderTexture(this.customShader, this.customShaderUTextures.get(key), key);
				}
			}
			sprite.draw(batch);
			batch.setShader(bkp);
		}
		else
		{
			sprite.draw(batch);
		}
	}
	
	protected String makeShaderReplacements(String str, Sprite sprite)
	{
		return str
				.replaceAll("ticks", String.valueOf(Main.gameTick))
				.replaceAll("ex", ""+this.Center().x)
				.replaceAll("ey", ""+this.Center().y)
				.replaceAll("cx", ""+Main.camera.position.x)
				.replaceAll("cy", ""+Main.camera.position.y)
				.replaceAll("#0", ""+Main.var[0])
				.replaceAll("ed", ""+this.direction)
				.replaceAll("sw", ""+sprite.getWidth())
				.replaceAll("sh", ""+sprite.getHeight());
	}
}