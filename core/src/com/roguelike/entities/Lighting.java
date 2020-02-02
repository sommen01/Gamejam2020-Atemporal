package com.roguelike.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;

public class Lighting
{
	public Vector2 center;
	public float radius;
	public Color color;
	public float timeLeft;
	public Entity parent = null;
	public boolean hadParent = false;
	public float vibrancy = 0f;
	public int power = 1;
	
	public boolean remove = false;
	public boolean important = false;
	
	public static final float maxSimilarity = 0.95f;
	public static final float maxSimilarityFix = 32f;
	public static final int maxSimilarLightings = 1;
	
	public static Lighting Create(Vector2 center, float radius, Color color, float timeLeft)
	{
		return Create(center, radius, color, timeLeft, 1f);
	}
	
	public static Lighting Create(Vector2 center, float radius, Color color, float timeLeft, boolean simplify)
	{
		return Create(center, radius, color, timeLeft, 1f, simplify);
	}
	
	public static Lighting FromParticle(Particle p, boolean autoParent)
	{
		return Create(autoParent?p.position:p.position.cpy(), (p.lightSize+32)*4, p.color.cpy().mul(1.5f).add(0.2f, 0.2f, 0.2f, 1f), p.duration);
	}
	
	public static Lighting Create(Vector2 center, float radius, Color color, float timeLeft, float similarity)
	{
		return Create(center, radius, color, timeLeft, similarity, false);
	}
	
	public static Lighting Create(Vector2 center, float radius, Color color, float timeLeft, float similarity, boolean simplify)
	{
		if(AHS.isUp || Main.pause)
			return new Lighting();

		Lighting p = new Lighting();
		p.center = center;
		p.radius = radius;
		p.color = color;
		if(simplify)
		{
			Color c = p.color.cpy();
			p.color = new Color(c.r*0.75f+0.25f, c.g*0.75f+0.25f, c.b*0.75f+0.25f, c.a);
		}
		p.timeLeft = timeLeft;
		
		if(p.canDraw(similarity))
			Main.lighting.add(p);
		
		return p;
	}
	
	public static Lighting Prepare(Vector2 center, float radius, Color color, float timeLeft)
	{
		if(AHS.isUp || Main.pause)
			return new Lighting();

		Lighting p = new Lighting();
		p.center = center;
		p.radius = radius;
		p.color = color;
		p.timeLeft = timeLeft;
		p.important = true;
		
		return p;
	}
	
	public void setParent(Entity parent)
	{
		this.hadParent = true;
		this.parent = parent;
	}
	
	public static boolean similarColor(Color color1, Color color2)
	{
		if(Math.abs(color1.r-color2.r) < 0.1f && Math.abs(color1.g-color2.g) < 0.1f && Math.abs(color1.b - color2.b) < 0.1f)
			return true;
		
		return false;
	}
	
	public boolean canDraw(float similarity)
	{
		ArrayList<Lighting> lightings = new ArrayList<Lighting>();
		lightings.addAll(Main.lightingex);
		lightings.addAll(Main.lighting);
		lightings.remove(this);
		int similars = 0;
		boolean inQueue = false;
		Lighting before = null;
		for(Lighting l : lightings)
		{
			if(l != this && !l.remove && l.timeLeft > 0 && similarColor(l.color, this.color))
			{
				float distance = l.center.dst(this.center);
				float r1 = l.radius;
				float r2 = this.radius;
				if ((distance+10 <= Math.abs(r1 - r2)))
				{
				    return false;
				}
				else
				{
				   float percentage = 1f-(distance/Math.abs(r1+r2));
				   //System.out.println("Circle2 overlaps Circle1 (" + distance + " " + r1 + " " + r2 + " " + percentage + "%)");
				   if(percentage >= maxSimilarity*similarity || distance < maxSimilarityFix/similarity)
				   {
					   if(l.important)
						   return false;
					   
					   //System.out.println("Overlapping...");
					   if(this.radius >= l.radius*0.95f && this.timeLeft > l.timeLeft)
					   {
						   //System.out.println("Worth radius and timeleft is bigger.: " + l.timeLeft + " " + this.timeLeft);
						   inQueue = true;
						   similars++;
						   before = l;
						   if(inQueue && similars >= maxSimilarLightings)
						   {
							   break;
						   }
					   }
					   else
					   {
						   similars++;
					   }
				   }
				} 
			}
		}
		if(!inQueue)
			return similars < maxSimilarLightings;
		else
		{
			if(similars < maxSimilarLightings)
			{
				return true;
			}
			else
			{
				if(before.important)
					System.out.println("Removing important particle");
				
				before.remove = true;
				return true;
			}
		}
	}
}
