package com.roguelike.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;

public class LivingText extends Entity
{
    public String text = "";
    public Color color = Color.BLACK;
    public float timeLeft = 0f;
    public float scale = 1f;
    public boolean critical = false;
    public Color originalColor = Color.BLACK;
    public boolean damageText = true;
	public int whoAmI;
	public Entity attached;
	public int ticks = 0;
    
    @Override
    public void update(float delta)
    {
        timeLeft -= delta;
        if(timeLeft <= 0)
        {
            this.active = false;
        }
        this.velocity.scl(0.95f);
        this.position.x += this.velocity.x * delta;
        this.position.y += this.velocity.y * delta;
        if(this.critical && Main.gameTick % 3 == 0)
        {
            this.scale += 0.01f;
            this.position.y += 1f;
            if(this.color == this.originalColor)
            {
                this.color = Color.WHITE;
            }
            else
            {
                this.color = this.originalColor;
            }
        }
        
        if(this.damageText && this.attached != null)
        {
	        for(LivingText t : Main.livingtext)
	        {
	        	if(this.active && t.active && t != this && this.color == t.color && t.damageText && t.attached == this.attached && this.ticks < 2 && t.ticks < 2)
	        	{
	        		this.text = String.valueOf(Integer.parseInt(this.text) + Integer.parseInt(t.text));
	        		this.timeLeft = Math.max(Math.max(this.timeLeft, t.timeLeft), 1f);
	        		this.critical = t.critical || this.critical;
	        		float scale = 0.5f + (Integer.parseInt(this.text) * 1f)/1000f;
	        		scale = (scale > 1.5f ? 1.5f : scale);
	        		this.scale = scale;
	        		t.active = false;
	        		if(t.Center().dst(attached.Center()) < this.Center().dst(attached.Center()))
	        		{
	        			this.position = t.position;
	        			this.velocity = t.velocity;
	        		}
	        	}
	        }
        }
        this.ticks++;
    }
    
    public static LivingText Create(Vector2 position, Vector2 velocity, String text, Color color, float scale, float timeLeft, 
    		boolean damageText, boolean crit)
    {
    	return Create(position, velocity, text, color, scale, timeLeft, damageText, crit, null);
    }
    
    public static LivingText Create(Vector2 position, Vector2 velocity, String text, Color color, float scale, float timeLeft, 
    		boolean damageText, boolean crit, Entity attached)
    {
    	if(AHS.isUp)
    	{ 
    		LivingText t = new LivingText();
			t.text = text;
			t.color = color;
			t.active = true;
			t.timeLeft = timeLeft;
			t.position = position.cpy();
			t.velocity = velocity.cpy();
			t.scale = scale;
			t.originalColor = color;
			t.damageText = damageText;
			t.critical = crit;
			t.attached = attached;
			t.ticks = 0;
		}
    	else if(!Main.isOnline)
    	{
    		int index = Main.livingtext.length - 1;
    		for(int i = 0;i < Main.livingtext.length;i++)
    		{
    			if(!Main.livingtext[i].active)
    			{
    				index = i;
    				break;
    			}
    		}
    		Main.livingtext[index].text = text;
    		Main.livingtext[index].color = color;
    		Main.livingtext[index].active = true;
    		Main.livingtext[index].timeLeft = timeLeft;
    		Main.livingtext[index].position = position.cpy();
    		Main.livingtext[index].velocity = velocity.cpy();
    		Main.livingtext[index].scale = scale;
    		Main.livingtext[index].originalColor = color;
    		Main.livingtext[index].damageText = damageText;
    		Main.livingtext[index].critical = crit;
			Main.livingtext[index].attached = attached;
			Main.livingtext[index].ticks = 0;
    		return Main.livingtext[index];
    	}
    	return new LivingText();
    }
}
