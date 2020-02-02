package com.roguelike.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Item;
import com.roguelike.game.Content;
import com.roguelike.game.Main;
import com.roguelike.world.Tile;

import java.util.ArrayList;

public class Treasure extends Entity
{
	public ArrayList<Item> content = new ArrayList<Item>();
	public int pressTick = 0;
	public boolean givingItems = false;
	
	public Treasure()
	{
		this.width = 100;
		this.height = 80;
		this.active = true;
	}
	
	public Treasure(Vector2 position)
	{
		this();
		this.position = position;
	}
	
	@Override
	public void update(float delta)
	{
		if(Constant.getPlayerList()[Main.me].Center().dst(this.Center()) < 100 && Gdx.input.isKeyPressed(Keys.X))
		{
			givingItems = true;
		}
		if(this.givingItems)
		{
			this.pressTick++;
			if(this.pressTick % 5 == 0)
			{
				int index = -1;
				for(Item i : content)
				{
					if(i != null)
					{
						index = content.indexOf(i);
						break;
					}
				}
				if(index != -1)
				{
					Item.Create(this.Center(), new Vector2(MathUtils.random(-300, 300), MathUtils.random(400, 700)), content.get(index).id, this.myMapX, this.myMapY, true);
					content.remove(index);
				}
				else
				{
					this.active = false;
					for(int i = 0;i < this.position.dst(new Vector2(this.position.x + this.width, this.position.y + this.height));i++)
					{
						Particle.Create(new Vector2(this.position.x + MathUtils.random(this.width), this.position.y + MathUtils.random(this.height)), new Vector2(MathUtils.random(-50, 50), MathUtils.random(-50, 50)), 3, Color.WHITE, 0f, 1.5f, MathUtils.random(0.5f, 1f));
					}
				}
			}
		}
		this.velocity.y += Main.gravity/1.5f;
		this.velocity.x *= 0.975f;
		float addAmountX = this.velocity.x * delta;
		float newX = this.position.x + addAmountX;
		float addAmountY = this.velocity.y * delta;
		float newY = this.position.y + addAmountY;

		if(!Constant.tryGetMapForEntity(this).doesRectCollideWithMap(this.position.x, newY, this.width, this.height))
		{
			this.position.y = newY;
		}
		else
		{
			if(this.velocity.y < 0f)
			{
				this.position.y -= Math.floor(this.position.y) % Tile.TILE_SIZE;
			}
			else if(this.velocity.y > 0f)
			{
				this.position.y = this.position.y;
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
				this.position.x -= Math.floor(this.position.x) % Tile.TILE_SIZE;
			}
			else if(this.velocity.x > 0f)
			{
				this.position.x = this.position.x;
			}

			this.velocity.x = 0;
		}
	}

	public void draw(SpriteBatch batch)
	{
		Sprite sprite = new Sprite(Content.woodentreasure);
		sprite.setPosition(this.position.x, this.position.y);
		sprite.draw(batch);
	} 
}
