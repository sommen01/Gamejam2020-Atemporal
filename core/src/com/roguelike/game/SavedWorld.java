package com.roguelike.game;

import java.util.ArrayList;

import com.roguelike.constants.Item;
import com.roguelike.entities.Lighting;
import com.roguelike.entities.LivingText;
import com.roguelike.entities.Monster;
import com.roguelike.entities.NPC;
import com.roguelike.entities.Particle;
import com.roguelike.entities.Projectile;
import com.roguelike.entities.Prop;
import com.roguelike.entities.Treasure;
import com.roguelike.world.Respawn;

public class SavedWorld {
	public static boolean saved = false;
	public static int pDir = 1;
	public static ArrayList<Monster> monster = new ArrayList<Monster>();
	public static ArrayList<Monster> monsterex = new ArrayList<Monster>();
	public static ArrayList<Monster> natural = new ArrayList<Monster>();
	public static ArrayList<Monster> naturalex = new ArrayList<Monster>();
	public static ArrayList<Particle> particle = new ArrayList<Particle>();
	public static ArrayList<Particle> bgparticle = new ArrayList<Particle>();
	public static ArrayList<Particle> particleex = new ArrayList<Particle>();
	public static ArrayList<Lighting> lighting = new ArrayList<Lighting>();
	public static ArrayList<Lighting> lightingex = new ArrayList<Lighting>();
	public static ArrayList<Treasure> treasure = new ArrayList<Treasure>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	public static ArrayList<NPC> npc = new ArrayList<NPC>();
	public static ArrayList<Projectile> projectile = new ArrayList<Projectile>();
	public static ArrayList<Projectile> projectileex = new ArrayList<Projectile>();
	public static ArrayList<Prop> prop = new ArrayList<Prop>();
	
	public static void Save()
	{
		for(Monster m : monster)
		{
			m.savedPosition = m.position.cpy();
			m.savedVelocity = m.velocity.cpy();
		}
		Main.player[Main.me].savedPosition = Main.player[Main.me].position.cpy();
		Main.player[Main.me].savedVelocity = Main.player[Main.me].velocity.cpy();
		pDir = Main.player[Main.me].direction;
		saved = true;
	}
	
	public static void Load()
	{
		for(Monster m : Main.monster)
		{
			m.position = m.savedPosition;
			m.velocity = m.savedVelocity;
		}
		saved = false;
	}
}
