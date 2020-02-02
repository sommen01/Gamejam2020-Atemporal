package com.roguelike.constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.entities.Lighting;
import com.roguelike.entities.Monster;
import com.roguelike.entities.Particle;
import com.roguelike.entities.Player;
import com.roguelike.entities.Prop;
import com.roguelike.entities.Treasure;
import com.roguelike.entities.Projectile;
import com.roguelike.entities.NPC;
import com.roguelike.game.Content;
import com.roguelike.game.Event;
import com.roguelike.game.Main;
import com.roguelike.game.MathEx;
import com.roguelike.game.SavedWorld;
import com.roguelike.world.GameMap;

public class Skill
{
	// attributes
	public transient String name;
	public transient String[] baseDescription = new String[5];
	public transient String description;
	public transient float[] cooldown = new float[5];
	public transient float cdf;
	public transient boolean passive;
	public transient boolean classPassive;
	public int id;
	public transient int frame = 0;
	public transient int castTime = 0;
	public transient int[] manaCost = new int[5];
	public transient boolean attackRestricted;
	public transient int multiCasts = 1;
	public transient int cooldownTicksAfterCast = 0;
	public transient CompactAnim castAnim = null;
	public int level;
	public transient int element;
	public transient boolean offensive;
	public transient boolean debugging;
	public transient boolean channel;
	public transient int multiCastDelay;
	public transient int maxChannelTime;
	public int totalCasts=0;
	public transient int maxChannelHoldTime;
	public int[] extraInfos = new int[5];
	public transient MathEx[] infos = new MathEx[10];
	public transient float[] storedInfos = new float[10];
	public transient boolean freeUse = false;
	public transient boolean mageUltimate = false;
	public transient int waitForBuff = -1;
	public transient boolean manualRecast = false;
	public transient int levelRequired = 1;
	
	// vars
	public transient int ticksFromCast = 9999;
	public transient int ticksFromCast2 = 0;
	public transient boolean alreadyCast = false;
	public transient int casts = 0;
	public transient boolean channeling = false;
	public transient int channelTicks = 0;
	public transient Vector2 mouseOnCast;
	public transient Vector2 posOnCast;
	public transient float lastCdf = 1;

	public void initialize()
	{
		this.SetInfos(this.id, false);
	}
	
	public Skill()
	{}

	public Skill(int id)
	{
		this.SetInfos(id, true);
	}

	public Skill SetInfos(int id, boolean fullReset)
	{
		this.id = id;
		this.ResetStats();
		if(fullReset)
		{
			this.level = 1;
			this.totalCasts = 0;
			for(int i = 0;i < 5;i++)
			{
				this.extraInfos[i] = 0;
			}
		}
		if(this.id == 1)
		{
			this.name = "Soul Shield";
			this.baseDescription[0] = "Blocks the next incoming attack.\n"
					+ "Cooldown reduced by 0.25s for every successful attack.";
			this.cooldown[0] = 10f;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.levelRequired = 1;
		}
		else if(this.id == 2)
		{
			this.name = "Geas Control";
			this.baseDescription[0] = "Controls where your geas are going to.";
			this.cooldown[0] = 0f;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.levelRequired = 1;
		}
		else if(this.id == 3)
		{
			this.name = "Soul Burst";
			this.baseDescription[0] = "Unleashes all your souls in a concentrated energy ball.\n"
					+ "Causes (500% + 10%/player level) of the weapon damage as base damage.\n"
					+ "This skill's cooldown is resetted as you kill an enemy with it.";
			this.cooldown[0] = 10f;
			this.castTime = 10;
			this.offensive = true;
			this.element = Constant.DAMAGETYPE_DARKNESS;
			this.levelRequired = 1;
		}
		else if(this.id == 4)
		{
			this.name = "Fireball";
			this.baseDescription[0] = "Releases a fireball from your bare hands.\n"
					+ "Causes (100 + 40/skill level) fire damage.";
			this.cooldown[0] = 5f;
			this.castTime = 42;
			this.manaCost[0] = 15;
			this.offensive = true;
			this.element = Constant.DAMAGETYPE_FIRE;
			this.levelRequired = 1;
		}
		else if(this.id == 5)
		{
			this.name = "Giant Fireball";
			this.baseDescription[0] = "Releases a bigger fireball from your bare hands.\n"
					+ "Causes (400 + 70/skill level) fire damage.";
			this.cooldown[0] = 5f;
			this.castTime = 42;
			this.manaCost[0] = 15;
			this.offensive = true;
			this.element = Constant.DAMAGETYPE_FIRE;
			this.levelRequired = 1;
		}
		else if(this.id == 6)
		{
			this.name = "Stellar Imbuement";
			this.cooldown[0] = 8f;
			this.cooldown[1] = 7f;
			this.cooldown[2] = 6f;
			this.cooldown[3] = 5f;
			this.cooldown[4] = 4f;
			this.castTime = -1;
			this.manaCost[0] = 0;
			this.baseDescription[0] = "Increases your next ranged attack damage by {#0%}.";
			this.infos[0].expression = "50+lvl*25";
			this.infos[0].useAsInteger = true;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.levelRequired = 1;
		}
		else if(this.id == 7)
		{
			this.name = "Death From Above";
			this.cooldown[0] = 10f;
			this.castTime = -1;
			this.manaCost[0] = 10;
			this.baseDescription[0] = "Stops the gravity effects on you, but you can't jump or flip. Lasts for 4 seconds, jumping will remove the effect.\n"
					+ "Needs to be airborne";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.levelRequired = 1;
		}
		else if(this.id == 8)
		{
			this.name = "Protector Shield";
			this.cooldown[0] = 10f;
			this.cooldown[1] = 8.5f;
			this.cooldown[2] = 7f;
			this.cooldown[3] = 5f;
			this.cooldown[4] = 1f;
			//this.castTime = 1337;
			this.manaCost[0] = 0;
			this.baseDescription[0] = "-=Passive:=- Equipping a shield reduces all damage taken by {#1%}.\n\n"
					+ "-=Active:=- Uses the player shield to create a barrier that destroys any projectile that contacts it. "
					+ "The barrier lasts for {#0s} and increases the passive defense to {#2%}, the knight can't attack while the barrier is active. "
					+ "The skill can be triggered again to manually destroy the barrier.\n"
					+ "Requires a shield.";
			this.infos[0].expression = "2.0+lvl*0.5";
			this.infos[1].expression = "5+lvl*5";
			this.infos[2].expression = "#1*2";
			this.cooldownTicksAfterCast = 180;
			this.multiCasts = 2;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.levelRequired = 1;
		}
		else if(this.id == 9)
		{
			this.name = "No Ability";
			this.cooldown[0] = 0f;
			this.manaCost[0] = 0;
			this.castTime = -1;
			this.baseDescription[0] = "You can't use your weapon special ability because you are using dual weapons.";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.levelRequired = 1;
		}
		else if(this.id == 10)
		{
			this.name = "Guiding Star";
			this.cooldown[0] = 8f;
			this.cooldown[1] = 7f;
			this.cooldown[2] = 6f;
			this.cooldown[3] = 5f;
			this.cooldown[4] = 4f;
			this.waitForBuff = 10;
			this.manaCost[0] = 20;
			this.castTime = -1;
			this.baseDescription[0] = "-=Passive:=- Increases ranger attack speed by {#0%}.\n\n-=Active=-: Increases the ranger attack speed by {#0%} for 4 seconds.";
			this.infos[0].expression = "40 + lvl * 10";
			this.infos[0].useAsInteger = true;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.levelRequired = 1;
		}
		else if(this.id == 11)
		{
			this.name = "Invisibility";
			this.cooldown[0] = 10f;
			this.cooldown[1] = 8.75f;
			this.cooldown[2] = 7.5f;
			this.cooldown[3] = 6.25f;
			this.cooldown[4] = 5f;
			this.manaCost[0] = 5;
			this.manaCost[4] = 0;
			this.castTime = -1;
			this.baseDescription[0] = "The rogues becomes invisible for #0s. During the invisibility the rogue can't receive enemies contact damage.\n\n"
					+ "The first attack while invisible will cancel out the invisibility and will be a Sneaky Attack.\n"
					+ "Enemies can't target you while invisible.";
			this.infos[0].expression = "5";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.levelRequired = 1;
		}
		else if(this.id == 12)
		{
			this.name = "Bash Dash";
			this.cooldown[0] = 6f;
			this.cooldown[1] = 5.5f;
			this.cooldown[2] = 5f;
			this.cooldown[3] = 4.5f;
			this.cooldown[4] = 4f;
			this.manaCost[0] = 0;
			//this.castTime = 12;
			this.baseDescription[0] = "The knight dashes and bashes with his shield if he encounters an enemy, causing {R#2%} of the knight's maximum health {R(#0)} damage in a cone area and stunning them for {#1s}. "
					+ "The bash can be critical."
					+ "\nRequires a shield.";
			this.infos[0].expression = "hp*(0.1+lvl*0.025)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "2+lvl*0.25";
			this.infos[2].expression = "(0.1+lvl*0.025)*100";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.offensive = true;
			this.levelRequired = 1;
		}
		else if(this.id == 13)
		{
			this.name = "Raging Dash";
			this.cooldown[0] = 16f;
			this.cooldown[1] = 14f;
			this.cooldown[2] = 12f;
			this.cooldown[3] = 10f;
			this.cooldown[4] = 8f;
			this.manaCost[0] = 20;
			this.castTime = -1;
			this.baseDescription[0] = "The knight dashes forward causing {#1} {G(+#0)} damage in every enemy in the way."
					+ " The knight can't receive direct damage while in the effect.";
			this.infos[0].expression = "pp*(2+0.25*lvl)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "20+lvl*20";
			this.infos[1].useAsInteger = true;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.offensive = true;
			this.levelRequired = 10;
		}
		else if(this.id == 14)
		{
			this.name = "Dart Throw";
			this.cooldown[0] = 8f;
			this.cooldown[1] = 7f;
			this.cooldown[2] = 6f;
			this.cooldown[3] = 5f;
			this.cooldown[4] = 4f;
			this.manaCost[0] = 5;
			this.castTime = -1;
			this.cooldownTicksAfterCast = 120;
			this.multiCasts = 5;
			this.multiCastDelay = 8;
			this.baseDescription[0] = "The rogue throws up to 5 darts by recasting this ability, if 3 darts hits the same enemy "
					+ "the next damage the enemy receives is increased by {#1%}. Darts also deals {G#0} damage to enemies that have not been hit by 3 darts.";
			
			this.infos[0].expression = "pp*(0.2 + lvl * 0.025)";
			this.infos[1].expression = "50+lvl*12.5";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
		}
		else if(this.id == 15)
		{
			this.name = "Determination";
			this.cooldown[0] = 12f;
			this.manaCost[0] = 15;
			this.castTime = -1;
			this.baseDescription[0] = "Instantly resets your stamina regeneration time, regenerates {#0%} of your maximum stamina and removes all stacks of {OFlipping Dizziness}.";
			this.infos[0].expression = "30 + lvl * 10";
			this.infos[0].useAsInteger = true;
			this.element = Constant.DAMAGETYPE_GOOD;
		}
		else if(this.id == 16)
		{
			this.name = "Wind Slash";
			this.cooldown[0] = 2f;
			this.manaCost[0] = 0;
			this.castTime = -1;
			this.baseDescription[0] = "Summons a wind sword that slashes enemies in your mouse direction, causes 2.5x your weapon damage as air damage";
			this.element = Constant.DAMAGETYPE_AIR;
			this.offensive = true;
		}
		else if(this.id == 17)
		{
			this.name = "Active Mark: Fire";
			this.cooldown[0] = 15f;
			this.manaCost[0] = 0;
			this.castTime = -1;
			this.baseDescription[0] = "Activates the fire mark in the bottom of the scythe, upgrading its basic attacks.";
			this.element = Constant.DAMAGETYPE_FIRE;
		}
		else if(this.id == 18)
		{
			this.name = "Switch Mode";
			this.cooldown[0] = 1f;
			this.manaCost[0] = 0;
			this.castTime = -1;
			this.baseDescription[0] = "Switches between {Dragon} and {Bullet} mode";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
		}
		else if(this.id == 19)
		{
			this.name = "Time Travel";
			this.cooldown[0] = 1f;
			this.manaCost[0] = 0;
			this.baseDescription[0] = "Saves and rewinds time.";
			this.infos[0].expression = "1";
			this.multiCasts = 2;
			this.cooldownTicksAfterCast = 300;
			this.multiCastDelay = 15;
			this.infos[0].useAsInteger = true;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.offensive = true;
		}
		else if(this.id == 20)
		{
			this.name = "Quick Slash";
			this.cooldown[0] = 8f;
			this.manaCost[0] = 0;
			this.castTime = -1;
			this.baseDescription[0] = "Causes damages with an instant slash.";
			this.castAnim = new CompactAnim(0, 6, 2, false, false, true);
			this.attackRestricted = true;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.offensive = true;
		}
		else if(this.id == 21)
		{
			this.name = "Bladerang";
			this.cooldown[0] = 7f;
			this.cooldown[1] = 6.5f;
			this.cooldown[2] = 6f;
			this.cooldown[3] = 5.5f;
			this.cooldown[4] = 5f;
			this.manaCost[0] = 0;
			this.castTime = -1;
			this.baseDescription[0] = "The knight throws a bladerang that causes {G#0} damage in the trip and in the return.\n"
					+ "If no enemy was hit in the trip the bladerang damage is increased to {300%}.";
			this.infos[0].expression = "pp*(0.8+lvl*0.1)";
			this.infos[0].useAsInteger = true;
			this.attackRestricted = true;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.offensive = true;
			this.levelRequired = 5;
		}
		else if(this.id == 22)
		{
			this.name = "Lotus Cut";
			this.cooldown[0] = 8f;
			this.cooldown[1] = 7.5f;
			this.cooldown[2] = 7f;
			this.cooldown[3] = 6.5f;
			this.cooldown[4] = 6f;
			this.manaCost[0] = 0;
			this.castTime = 3;
			this.baseDescription[0] = "The knight executes a double slash at once, causing from {G#0} to {G#1} damage while dashing. "
					+ "The knight is untargetable while dashing.\n\n"
					+ "Hurting enemies decreases {OLotus Cut} cooldown by {0.5s}.\n"
					+ "The untargetable time decreases with attack speed.";
			this.infos[0].expression = "pp*(0.8-lvl*0.1)";
			this.infos[1].expression = "pp*(1.2+lvl*0.2)*2";
			this.infos[0].useAsInteger = true;
			this.infos[1].useAsInteger = true;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.offensive = true;
			this.levelRequired = 15;
		}
		else if(this.id == 23)
		{
			this.name = "Sword Tempest";
			this.cooldown[0] = 25f;
			this.cooldown[1] = 23f;
			this.cooldown[2] = 21f;
			this.cooldown[3] = 19f;
			this.cooldown[4] = 17f;
			this.manaCost[0] = 0;
			this.castTime = -1;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.baseDescription[0] = "The knight summons a cloud in the mouse position, the cloud summons small swords that causes damage "
					+ "in enemies directly under it. The final sword is bigger and causes more damage.";
			
			//this.effects[0] = "-=Small sword damage=-: {G5% PP}\n"
					//+ "-=Final sword damage=-: {G190% PP}\n"
					//+ "-=Sword spawn rate=-: 12/s\n"
					//+ "-=Cloud duration=-: 2s";
			//this.effects[1] = "-=Small sword damage=-: {G7% PP}\n"
					//+ "-=Final sword damage=-: {G205% PP}\n"
					//+ "-=Sword spawn rate=-: 12/s\n"
					//+ "-=Cloud duration=-: 2s";
			//this.effects[2] = "-=Small sword damage=-: {G10% PP}\n"
					//+ "-=Final sword damage=-: {G220% PP}\n"
					//+ "-=Sword spawn rate=-: 12/s\n"
					//+ "-=Cloud duration=-: 2s";
			//this.effects[3] = "-=Small sword damage=-: {G12% PP}\n"
					//+ "-=Final sword damage=-: {G235% PP}\n"
					//+ "-=Sword spawn rate=-: 12/s\n"
					//+ "-=Cloud duration=-: 2s";
			//this.effects[4] = "-=Small sword damage=-: {G15% PP}\n"
					//+ "-=Final sword damage=-: {G250% PP}\n"
					//+ "-=Sword spawn rate=-: 12/s\n"
				//	+ "-=Cloud duration=-: 2s";
			this.offensive = true;
		}
		else if(this.id == 24)
		{
			this.name = "Olympus Hammer";
			this.cooldown[0] = 15f;
			this.cooldown[1] = 13.75f;
			this.cooldown[2] = 12.5f;
			this.cooldown[3] = 11.25f;
			this.cooldown[4] = 10f;
			this.manaCost[0] = 0;
			this.castTime = -1;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.baseDescription[0] = "The knight summons a thunder hammer and throws it. Enemies hit by the hammer are "
					+ "stunned for {#2s} and recieves {C#0} + {G#1} energy damage.\n\nThe hammer then goes around the world and "
					+ "reappears in the knight's back, if the knight picks up the hammer in the air an energy explosion "
					+ "occurs and nearby enemies takes {C#3} + {G#4} damage and are stunned again.";
			
			this.infos[0].expression = "mp*(0.7+lvl*0.2)*2";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "pp*(0.5+lvl*0.125)*2";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "1";
			this.infos[3].expression = "#0*2";
			this.infos[3].useAsInteger = true;
			this.infos[4].expression = "#1*2";
			this.infos[4].useAsInteger = true;
			this.offensive = true;
			this.levelRequired = 25;
		}
		else if(this.id == 25)
		{
			this.name = "Air Cutter";
			this.cooldown[0] = 10f;
			this.cooldown[1] = 9.5f;
			this.cooldown[2] = 9f;
			this.cooldown[3] = 8.5f;
			this.cooldown[4] = 8f;
			this.manaCost[0] = 0;
			this.castTime = 6;
			this.element = Constant.DAMAGETYPE_AIR;
			this.baseDescription[0] = "Cuts out the air creating an air wave that causes {G#0} damage in enemies it hits.\n"
					+ "The air wave damage is increased up to {#2} based on the enemy lost health.";
			this.infos[0].expression = "pp*(1+lvl*0.125)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "150+lvl*37.5";
			this.infos[2].expression = "#0*(#1/100)";
			this.infos[2].useAsInteger = true;
			this.offensive = true;
			this.levelRequired = 20;
		}
		else if(this.id == 26)
		{
			this.name = "Tough Skin";
			this.classPassive = true;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.baseDescription[0] = "Reduces the damage received by contacting enemies by {G#0%}.";
			this.infos[0].expression = "20+lvl*10";
		}
		else if(this.id == 27)
		{
			this.name = "Summon Excalibur";
			this.element = Constant.DAMAGETYPE_HOLY;
			this.cooldown[0] = 100;
			this.baseDescription[0] = "The knights summons a temporary excalibur. The excalibur attacks curses the target in"
					+ " holy smite, causing {G4%} of the excalibur damage per stack as a DoT.";
			//this.effects[0] = "-=Duration=-: 8s\n"
					//+ "-=Excalibur damage=-: 120% player level\n"
					//+ "-=Max stacks=-: 25\n";
			//this.effects[1] = "-=Duration=-: 10s\n"
					//+ "-=Excalibur damage=-: 120% player level\n"
					//+ "-=Max stacks=-: 25\n";
			//this.effects[2] = "-=Duration=-: 12s\n"
					//+ "-=Excalibur damage=-: 120% player level\n"
					//+ "-=Max stacks=-: 25\n";
			//this.effects[3] = "-=Duration=-: 14s\n"
					//+ "-=Excalibur damage=-: 120% player level\n"
					//+ "-=Max stacks=-: 25\n";
			//this.effects[4] = "-=Duration=-: 18s\n"
					//+ "-=Excalibur damage=-: 120% player level\n"
					//+ "-=Max stacks=-: 25\n";
			this.cooldown[0] = 0.25f;
			this.manaCost[0] = 0;
		}
		else if(this.id == 28)
		{
			this.name = "Sneaky Attack: Lethal";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.classPassive = true;
			this.baseDescription[0] = "Sneaky Attacks causes a guaranteed critical hit with {#0%} critical damage bonus.";
			this.infos[0].expression = "20 + lvl * 20";
		}
		else if(this.id == 29)
		{
			this.name = "Blink";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.cooldown[0] = 10f;
			this.cooldown[1] = 8.5f;
			this.cooldown[2] = 7f;
			this.cooldown[3] = 5.5f;
			this.cooldown[4] = 4f;
			this.manaCost[0] = 5;
			this.baseDescription[0] = "The rogue instantly teleports into the mouse position.";
			//this.effects[0] = "-=Distance limit=-: 500px";
		}
		else if(this.id == 30)
		{
			this.name = "Leap";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.cooldown[0] = 15f;
			this.cooldown[1] = 13f;
			this.cooldown[2] = 11f;
			this.cooldown[3] = 9f;
			this.cooldown[4] = 7f;
			this.manaCost[0] = 0;
			this.baseDescription[0] = "The rogue executes a long jump into the selected enemy, causing {G#0} damage upon contact.\n\n"
					+ "The rogue receives 1.5 seconds of damage immunity when he stabs the target.";
			
			this.infos[0].expression = "pp * (1.2 + lvl * 0.2)";
			this.infos[0].useAsInteger = true;
			this.offensive = true;
			this.levelRequired = 5;
		}
		else if(this.id == 31)
		{
			this.name = "Fear";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.cooldown[0] = 10f;
			this.manaCost[0] = 0;
			this.baseDescription[0] = "The rogue weakens an enemy, increasing all the damage he receives by {#0%} for 6 seconds.";
			this.infos[0].expression = "50 + lvl * 25";
			this.levelRequired = 10;
		}
		else if(this.id == 32)
		{
			this.name = "Fragility";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.cooldown[0] = 10f;
			this.baseDescription[0] = "Weakens the enemy, increasing all damages taken by him.";
			//this.effects[0] = "-=Damage increase=-: 30%\n"
				//	+ "-=Duration=-: 4s";
			//this.effects[1] = "-=Damage increase=-: 37%\n"
					//+ "-=Duration=-: 4s";
			//this.effects[2] = "-=Damage increase=-: 45%\n"
					//+ "-=Duration=-: 4s";
			//this.effects[3] = "-=Damage increase=-: 52%\n"
					//+ "-=Duration=-: 4s";
			//this.effects[4] = "-=Damage increase=-: 60%\n"
					//+ "-=Duration=-: 4s";
			this.manaCost[0] = 0;
			this.levelRequired = 15;
		}
		else if(this.id == 33)
		{
			this.name = "Bloodthirsty Dagger";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.cooldown[0] = 15f;
			this.cooldown[1] = 13f;
			this.cooldown[2] = 11f;
			this.cooldown[3] = 9f;
			this.cooldown[4] = 7f;
			this.manaCost[0] = 10;
			this.levelRequired = 20;
			this.baseDescription[0] = "Increases your next attack damage from {+#0%} to {+#1%} based on the target lost health.";
			this.infos[0].expression = "30 + lvl * 10";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "90 + lvl * 25";
			this.infos[1].useAsInteger = true;
			//this.effects[0] = "-=Minimum damage bonus=-: +50%\n"
					//+ "-=Maximum damage bonus=-: +150%";
		}
		else if(this.id == 34)
		{
			this.name = "Sneaky Pleasure";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.passive = true;
			this.baseDescription[0] = "Killing enemies reduces all active cooldowns by {#0s}.\n\nIf equipped, {ODart Throw} completely reset its darts.";
			this.infos[0].expression = "2 + lvl * 0.5";
			//this.effects[0] = "-=Cooldown reduction=-: 2s";
			//this.effects[1] = "-=Cooldown reduction=-: 2.5s";
			//this.effects[2] = "-=Cooldown reduction=-: 3s";
			//this.effects[3] = "-=Cooldown reduction=-: 3.5s";
			//this.effects[4] = "-=Cooldown reduction=-: 4s";
			this.levelRequired = 25;
		}
		else if(this.id == 35)
		{
			this.name = "Boo!";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.manaCost[0] = 0;
			this.cooldown[0] = 15f;
			this.baseDescription[0] = "The rogue cancels his invisibility to scare enemies in a medium range, stunning them for {#0} seconds.\n\n"
					+ "{RCan only be used while invisible and removes invisibility on use.}";
			this.infos[0].expression = "2 + lvl *0.25";
			this.levelRequired = 30;
		}
		else if(this.id == 36)
		{
			this.name = "Blink Knife";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.manaCost[0] = 0;
			this.multiCasts = 3;
			this.cooldownTicksAfterCast = 300;
			this.cooldown[0] = 30f;
			this.baseDescription[0] = "The rogue throws a knife in the mouse direction, damaging every enemy it hits by {G#0}. "
					+ "When the knife touches the terrain the rogue is teleported to the knife position.\n\n"
					+ "Can be cast up to 3 times.";
			this.infos[0].expression = "pp * (2 + lvl * 0.25)";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 35;
		}
		else if(this.id == 37)
		{
			this.name = "Slender Roll";
			this.classPassive = true;
			this.baseDescription[0] = "The ranger is immune to projectile collision while rolling.";
		}
		else if(this.id == 38)
		{
			this.name = "Tornado Arrow";
			this.cooldown[0] = 7f;
			this.baseDescription[0] = "The ranger shoots a tornado arrow that pierce enemies indefinitely until it collides with the terrain.";
			//this.effects[0] = "-=Damage=-: {G100% PP}";
			//this.effects[1] = "-=Damage=-: {G112% PP}";
			//this.effects[2] = "-=Damage=-: {G125% PP}";
			//this.effects[3] = "-=Damage=-: {G137% PP}";
			//this.effects[4] = "-=Damage=-: {G150% PP}";
			this.manaCost[0] = 0;
			this.castTime = 10;
			this.element = Constant.DAMAGETYPE_AIR;
			this.levelRequired = 5;
		}
		else if(this.id == 39)
		{
			this.name = "Heavenly Summoning"; // Convocação Celeste
			this.manaCost[0] = 25;
			this.cooldown[0] = 10f;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.baseDescription[0] = "The ranger receives a buff where the his next projectile collision is going to constant summon up to {#0} of the same projectile headed to the collision area.\n\n"
					+ "If the first projectile hits an enemy all the summoned projectiles [LIME]homes[] the same enemy.";
			this.infos[0].expression = "4 + lvl";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 10;
		}
		else if(this.id == 40)
		{
			this.name = "Judgement Strike";
			this.manaCost[0] = 15;
			this.cooldown[0] = 20f;
			this.cooldown[1] = 17f;
			this.cooldown[2] = 14f;
			this.cooldown[3] = 11f;
			this.cooldown[4] = 8f;
			this.baseDescription[0] = "The ranger receives a buff where the next projectile he uses will summon a lightning in the first enemy it hits, causing {GC#0} damage.";
			this.infos[0].expression = "pp * (1 + lvl * 0.125) + mp * (2 + lvl * 0.5)";
			this.infos[0].useAsInteger = true;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.levelRequired = 15;
		}
		else if(this.id == 41)
		{
			this.name = "Fright Trap";
			this.cooldown[0] = 15f;
			this.manaCost[0] = 0;
			this.element = Constant.DAMAGETYPE_DARKNESS;
			this.baseDescription[0] = "The ranger throws a ground trap that explodes when it collides with an enemy, stunning all nearby enemies for {#0} seconds and causing {G#1} damage.";			
			this.infos[0].expression = "2+lvl*0.25";
			this.infos[1].expression = "pp * (1.5 + lvl * 0.25)";
			this.infos[1].useAsInteger = true;
			this.levelRequired = 20;
		}
		else if(this.id == 42)
		{
			this.name = "Air Trap";
			this.cooldown[0] = 10f;
			this.cooldown[1] = 9f;
			this.cooldown[2] = 8f;
			this.cooldown[3] = 7f;
			this.cooldown[4] = 6f;
			this.manaCost[0] = 0;
			this.element = Constant.DAMAGETYPE_AIR;
			this.baseDescription[0] = "The ranger throws a ground trap that explodes when it collides with an enemy, knocking up all nearby enemies and causing {G#0} damage.";
			this.infos[0].expression = "pp * (1 + lvl * 0.25)";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 25;
		}
		else if(this.id == 43)
		{
			this.name = "Power Arrow";
			this.cooldown[0] = 20f;
			this.cooldown[1] = 19f;
			this.cooldown[2] = 18f;
			this.cooldown[3] = 17f;
			this.cooldown[4] = 16f;
			this.manaCost[0] = 0;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.baseDescription[0] = "The ranger receives a buff where all his projectiles gets accelerated 2x in time. "
					+ "This does not increases or decreases projectile speed, just the time for it to finish his movement.";
			//this.effects[0] = "-=Duration=-: 4s";
			//this.effects[1] = "-=Duration=-: 5s";
			//this.effects[2] = "-=Duration=-: 6s";
			//this.effects[3] = "-=Duration=-: 7s";
			//this.effects[4] = "-=Duration=-: 8s";
			this.levelRequired = 30;
		}
		else if(this.id == 44)
		{
			this.name = "Aether Arrow";
			this.cooldown[0] = 0.1f;
			this.manaCost[0] = 0;
			this.element = Constant.DAMAGETYPE_HOLY;
			this.channel = true;
			this.maxChannelTime = 60;
			this.offensive = true;
			this.maxChannelHoldTime = 120;
			this.baseDescription[0] = "The ranger channels an aether arrow, increasing the arrow damage over the channel time. "
					+ "The arrow damage goes from {G#0} to {G#2} based on the channel.\n\n"
					+ "If the channel is over [LIME]50%[] the arrow [LIME]penetrates[] the enemies.";
			this.infos[0].expression = "pp * (1 + 0.1 * lvl)";
			this.infos[0].useAsInteger = true;
			this.infos[2].expression = "pp * (4 + 0.35 * lvl)";
			this.infos[2].useAsInteger = true;
			this.infos[1].expression = "#2 - #0";
			this.infos[1].useAsInteger = true;
			this.levelRequired = 35;
		}
		else if(this.id == 45)
		{
			this.name = "Elemental Overbreak";
			this.classPassive = true;
			this.cooldown[0] = 10f;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.baseDescription[0] = "-=Elemental Overbreak:=- Every {#0º} skill the mage casts is improved.\n\n"
					+ "-=Ultimate:=- Mages have ultimate abilities. To cast an ultimate ability the "
					+ "mage has to:\n"
					+ "• Cast the ultimate ability to create an arcane circle;\n"
					+ "• Use three skills to complete the arcane circle;\n"
					+ "• Recast the ultimate ability within 3 seconds;\n\n"
					+ "Ultimate abilities does not have Elemental Overbreak improvements.";
			this.infos[0].expression = "6-lvl";
			this.infos[0].useAsInteger = true;
		}
		else if(this.id == 46)
		{
			this.name = "Arcane Prison";
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.baseDescription[0] = "The mage locks the target in an arcane prison, stunning him for {#0} seconds.\n\n"
					+ "-=Elemental Overbreak:=- Increases the stun duration by {2} seconds.";
			this.infos[0].expression = "2+lvl*0.25";
			//this.effects[0] = "-=Duration=-: 1s";
			//this.effects[1] = "-=Duration=-: 1.12s";
			//this.effects[2] = "-=Duration=-: 1.25s";
			//this.effects[3] = "-=Duration=-: 1.37s";
			//this.effects[4] = "-=Duration=-: 1.5s";
		}
		else if(this.id == 47)
		{
			this.name = "Thunderstruck";
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.baseDescription[0] = "The mage summons a cloud above the target, striking a lightning and causing {C#0} damage.\n\n"
					+ "-=Elemental Overbreak:=- Increases the lightning damage by {C#1}.";
			this.infos[0].expression = "mp * (1.2 + lvl * 0.1)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "mp * 0.5";
			this.infos[1].useAsInteger = true;
			//this.effects[0] = "-=Damage=-: {C120% MP}";
			//this.effects[1] = "-=Damage=-: {C130% MP}";
			//this.effects[2] = "-=Damage=-: {C140% MP}";
			//this.effects[3] = "-=Damage=-: {C150% MP}";
			//this.effects[4] = "-=Damage=-: {C160% MP}";
		}
		else if(this.id == 48)
		{
			this.name = "Powerpunch";
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.baseDescription[0] = "The mage punches in a direction, causing an explosion that damages enemies.";
			//this.effects[0] = "-=Damage=-: {C150% MP}";
			//this.effects[1] = "-=Damage=-: {C162% MP}";
			//this.effects[2] = "-=Damage=-: {C175% MP}";
			//this.effects[3] = "-=Damage=-: {C187% MP}";
			//this.effects[4] = "-=Damage=-: {C200% MP}";
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.element = Constant.DAMAGETYPE_FIRE;
		}
		else if(this.id == 49)
		{
			this.name = "Hurricane Blow";
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.element = Constant.DAMAGETYPE_AIR;
			this.channel = true;
			this.maxChannelTime = 60;
			this.offensive = true;
			this.maxChannelHoldTime = 120;
			this.baseDescription[0] = "The mage channels a wind mass in front of his hand.\n\nWhen cast, the mage releases the wind in a cone area, stunning and knocking back all enemies struck.\n\nThe knockback increases with the channel.\n\n"
					+ "-=Elemental Overbreak:=- Increases the arc wide by {30º} and the stun duration by {1} second";
			this.levelRequired = 4;
		}
		else if(this.id == 50)
		{
			this.name = "Ice Storm";
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.manaCost[0] = 0;
			this.element = Constant.DAMAGETYPE_ICE;
			this.baseDescription[0] = "The mage summons some stalactites that deals damage to enemies they collide.";
			//this.effects[0] = "-=Damage=-: {C60% MP}\n"
					//+ "-=Stalactites=-: 5";
			//this.effects[1] = "-=Damage=-: {C65% MP}\n"
					//+ "-=Stalactites=-: 5";
			//this.effects[2] = "-=Damage=-: {C70% MP}\n"
					//+ "-=Stalactites=-: 6";
			//this.effects[3] = "-=Damage=-: {C75% MP}\n"
					//+ "-=Stalactites=-: 6";
			//this.effects[4] = "-=Damage=-: {C80% MP}\n"
					//+ "-=Stalactites=-: 7";
		}
		else if(this.id == 51)
		{
			this.name = "Heathen Air";
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.element = Constant.DAMAGETYPE_FIRE;
			this.baseDescription[0] = "The mage ignites an area, burning all enemies within it for {C#0} damage per tick over 6 seconds.\n\n"
					+ "-=Elemental Overbreak:=- Increases the burn tick damage by {C#1} and the burn duration by {4} seconds.";
			this.infos[0].expression = "mp * (0.4 + lvl * 0.05)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "mp * 0.3";
			this.infos[1].useAsInteger = true;
			this.levelRequired = 8;
			//this.effects[0] = "-=Tick damage=-: {C40% MP}\n"
					//+ "-=Duration=-: 6s";
			//this.effects[1] = "-=Tick damage=-: {C45% MP}\n"
					//+ "-=Duration=-: 6s";
			//this.effects[2] = "-=Tick damage=-: {C50% MP}\n"
					//+ "-=Duration=-: 6s";
			//this.effects[3] = "-=Tick damage=-: {C55% MP}\n"
					//+ "-=Duration=-: 6s";
			//this.effects[4] = "-=Tick damage=-: {C60% MP}\n"
					//+ "-=Duration=-: 6s";
		}
		else if(this.id == 52)
		{
			this.name = "Nuclear Body";
			this.cooldown[0] = 20f;
			this.manaCost[0] = 0;
			this.element = Constant.DAMAGETYPE_FIRE;
			this.baseDescription[0] = "The mage receives a nuclear protection, exploding any enemy who collides with him.";
			//this.effects[0] = "-=Damage=-: {C200% MP}\n"
					//+ "-=Duration=-: 8s";
			//this.effects[1] = "-=Damage=-: {C210% MP}\n"
					//+ "-=Duration=-: 8s";
			//this.effects[2] = "-=Damage=-: {C220% MP}\n"
					//+ "-=Duration=-: 8s";
			//this.effects[3] = "-=Damage=-: {C230% MP}\n"
					//+ "-=Duration=-: 8s";
			//this.effects[4] = "-=Damage=-: {C240% MP}\n"
					//+ "-=Duration=-: 8s";
		}
		else if(this.id == 53)
		{
			this.name = "Space Distortion";
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.element = Constant.DAMAGETYPE_DARKNESS;
			this.baseDescription[0] = "The mage quickly moves into a destination.\n\nWhen the mage reaches his destination "
					+ "a shadowy explosion occurs and all the enemies nearby are damaged for {C#0} damage.\n"
					+ "The mage can recast this skill once to redirect the dash destination.\n\n"
					+ "-=Elemental Overbreak:=- Increases the explosion area and the damage by {C#1}.";
			this.infos[0].expression = "mp * (1 + lvl * 0.25)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "mp";
			this.infos[1].useAsInteger = true;
			this.multiCasts = 5;
			this.multiCastDelay = 1;
			this.cooldownTicksAfterCast = 180;
			this.manualRecast = true;
			this.levelRequired = 12;
			//this.effects[0] = "-=Damage=-: {C100% MP}";
			//this.effects[1] = "-=Damage=-: {C115% MP}";
			//this.effects[2] = "-=Damage=-: {C130% MP}";
			//this.effects[3] = "-=Damage=-: {C145% MP}";
			//this.effects[4] = "-=Damage=-: {C160% MP}";
		}
		else if(this.id == 54)
		{
			this.name = "Fireball";
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.element = Constant.DAMAGETYPE_FIRE;
			this.baseDescription[0] = "The mage casts a fireball that explodes upon contact, enemies within the explosion receives {C#0} damage.\n\n"
					+ "-=Elemental Overbreak:=- Increases the fireball damage by {C#1}, the projectile speed, the explosion radius and applies a burn effect on struck enemies, "
					+ "causing {C#2} damage per tick over {3} seconds.";
			this.infos[0].expression = "mp * (1.5 + 0.25 * lvl)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "mp";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "mp * 0.4";
			this.infos[2].useAsInteger = true;
			this.levelRequired = 16;
			//this.effects[0] = "-=Damage=-: {C150% MP}";
			//this.effects[1] = "-=Damage=-: {C175% MP}";
			//this.effects[2] = "-=Damage=-: {C200% MP}";
			//this.effects[3] = "-=Damage=-: {C225% MP}";
			//this.effects[4] = "-=Damage=-: {C250% MP}";
		}
		else if(this.id == 55)
		{
			this.name = "Soul Chain";
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.infos[0].expression = "mp * (1.5+lvl*0.25)";
			this.infos[0].useAsInteger = true;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldownTicksAfterCast = 240;
			this.multiCasts = 2;
			this.levelRequired = 20;
			this.baseDescription[0] = "-=First cast:=- The mage chains an enemy soul, highlighting him for a short period.\n"
					+ "-=Second cast:=- The mage chains another enemy soul, pulling the two chained enemies to each other, stunning them for {1} second and causing {C#0} damage.";
			//this.effects[0] = "-=Damage=-: {C100% MP}";
			//this.effects[1] = "-=Damage=-: {C105% MP}";
			//this.effects[2] = "-=Damage=-: {C110% MP}";
			//this.effects[3] = "-=Damage=-: {C115% MP}";
			//this.effects[4] = "-=Damage=-: {C120% MP}";
		}
		else if(this.id == 56)
		{
			this.name = "Tera Protection";
			this.cooldown[0] = 20f;
			this.baseDescription[0] = "The mage protects itself, creating an earth armor that blocks the incoming damage.";
			//this.effects[0] = "-=Shield=-: {C50% MP}\n"
			//+ "-=Duration=-: 4s";
			//this.effects[1] = "-=Shield=-: {C75% MP}\n"
			//+ "-=Duration=-: 4s";
			//this.effects[2] = "-=Shield=-: {C100% MP}\n"
			//	+ "-=Duration=-: 4s";
			//this.effects[3] = "-=Shield=-: {C125% MP}\n"
			//	+ "-=Duration=-: 4s";
			//this.effects[4] = "-=Shield=-: {C150% MP}\n"
			//	+ "-=Duration=-: 4s";
			this.manaCost[0] = 30;
			this.manaCost[1] = 25;
			this.manaCost[2] = 20;
			this.manaCost[3] = 15;
			this.manaCost[4] = 10;
			this.element = Constant.DAMAGETYPE_GOOD;
		}
		else if(this.id == 57)
		{
			this.name = "Honey Meteor";
			this.cooldown[0] = 10f;
			this.baseDescription[0] = "Summons a honey-meteor shower in the desired place (very low accurate).";
			this.manaCost[0] = 0;
			this.element = Constant.DAMAGETYPE_NATURE;
		}
		else if(this.id == 58)
		{
			this.name = "Raging Blade";
			this.cooldown[0] = 8f;
			this.cooldown[1] = 7.25f;
			this.cooldown[2] = 6.5f;
			this.cooldown[3] = 5.75f;
			this.cooldown[4] = 5f;
			this.baseDescription[0] = "The knight increases his next melee attack damage from {#0%} to {#1%} based on his lost health.";
			this.infos[0].expression = "100+lvl*25";
			this.infos[1].expression = "#0*2";
			this.manaCost[0] = 0;
			this.element = Constant.DAMAGETYPE_GOOD;
		}
		else if(this.id == 59)
		{
			this.name = "Shockwave";
			this.cooldown[0] = 10f;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.baseDescription[0] = "The knights summons a heavy warhammer and strikes the ground, causing {G#0} damage to all "
					+ "enemies nearby and stunning them for {#1s}.\n\nThe hammer creates a shockwave that damages struck enemies by {G#2} physical damage "
					+ "and knocks up every enemy it strucks. "
					+ "\n\nWhen the shockwave reaches the mouse cast position it explodes, causing {G#3} damage and knocking up again.";
			
			this.infos[0].expression = "pp*(0.75+lvl*0.125)*2";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "2";
			this.infos[2].expression = "pp*(0.3+lvl*0.05)*2";
			this.infos[2].useAsInteger = true;
			this.infos[3].expression = "#0*2";
			this.infos[3].useAsInteger = true;
			this.levelRequired = 30;
		}
		else if(this.id == 60)
		{
			this.name = "Brave Counterstrike";
			this.cooldown[0] = 20f;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.channel = true;
			this.maxChannelTime = 120;
			this.maxChannelHoldTime = 120;
			this.baseDescription[0] = "The knight lifts up his hand and starts a channel, receiving {#0%} reduced damage while channeling. "
					+ "\n\nThe knight accumulates {100%} of the damage received during the channel time and releases it all in one area attack, stunning enemies from {#1s} to {#2s} "
					+ "(based on charge) and damaging them for all the accumulated damage.\n\n"
					+ "The knight receives {#3%} bonus attack speed and {#4%} movement speed after casting that lasts from {#5s} to {#6s} (based on charge).";
			this.infos[0].expression = "40+lvl*10";
			this.infos[1].expression = "0.5";
			this.infos[2].expression = "1.5";
			this.infos[3].expression = "60+lvl*10";
			this.infos[4].expression = "30+lvl*5";
			this.infos[5].expression = "4";
			this.infos[6].expression = "10";
			this.levelRequired = 35;
		}
		else if(this.id == 61)
		{
			this.name = "Spear Throw";
			this.cooldown[0] = 6f;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.baseDescription[0] = "Throws the Soldier Spear, causing ranged damage.";
		}
		else if(this.id == 62)
		{
			this.name = "Home Call";
			this.cooldown[0] = 2f;
			this.channel = true;
			this.maxChannelTime = 300;
			this.maxChannelHoldTime = 300;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.baseDescription[0] = "Go back home.";
			//this.effects[0] = "-=Teleports the user to his home town.=-";
		}
		else if(this.id == 63)
		{
			this.name = "Nature Cage";
			this.cooldown[0] = 1f;
			this.element = Constant.DAMAGETYPE_NATURE;
			this.baseDescription[0] = "Stuns your target for {G2} seconds.";
		}
		else if(this.id == 64)
		{
			this.name = "Red Rivers";
			this.classPassive = true;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.baseDescription[0] = "-=Blood Filled:=- The hemomancer receives up to {R+#2%} bonus magic power based on his health.\n\n"
					+ "-=Blood Thirsty:=- The hemomancer receives up to {+#1h/s} bonus health regeneration based on his lost health.\n\n"
					+ "-=Out of Control:=- Every skill the hemomancer casts drops his health by the double of its mana cost.";
			this.infos[0].expression = "hp * (0.015 + lvl * 0.0025) * 0.01";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "5 + lvl * 15";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "#0*100";
			this.infos[2].useAsInteger = true;
			
		}
		else if(this.id == 65)
		{
			this.name = "Blood Steal";
			this.cooldown[0] = 8f;
			this.cooldown[1] = 7.5f;
			this.cooldown[2] = 7f;
			this.cooldown[3] = 6.5f;
			this.cooldown[4] = 6f;
			this.manaCost[0] = 10;
			this.element = Constant.DAMAGETYPE_BLOOD;
			this.infos[0].expression = "mp * (1 + lvl*0.125)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "mp * 0.5";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "2+lvl*0.5";
			this.infos[2].useAsInteger = true;
			this.infos[3].expression = "#1*2";
			this.infos[3].useAsInteger = true;
			this.baseDescription[0] = "The hemomancer steals the enemy blood, causing {C#0} damage. {#2} blood currents are created, healing the hemomancer "
					+ "by {C#1} as they touch him.\n\n"
					+ "Critical hits also increase the heal of each orb to {C#3}.";
			this.attackRestricted = true;
			this.offensive = true;
			this.manualRecast = true;
		}
		else if(this.id == 66)
		{
			this.name = "Blood Shower";
			this.cooldown[0] = 10f;
			this.manaCost[0] = 20;
			this.element = Constant.DAMAGETYPE_BLOOD;
			this.attackRestricted = true;
			this.baseDescription[0] = "The hemomancer summons {#1} blood orbs directly to an enemy, each one causing {C#0} damage.\n"
					+ "The hemomancer instantly stuns the target for 2 seconds on cast.\n\n"
					+ "{RThis skill consumes #2% of the hemomancer max health.}";
			this.infos[0].expression = "mp * (0.25 + lvl * 0.05)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "15 + lvl * 2.5";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "10";
			this.infos[2].useAsInteger = true;
			this.offensive = true;
			this.castTime = 40;
		}
		else if(this.id == 67)
		{
			this.name = "Lonely Sword";
			this.baseDescription[0] = "Increases your PP by {#0%} if you have only one {sword} equipped with no secondary or surplus items.";
			this.classPassive = true;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.infos[0].expression = "(lvl+1)*10";
		}
		else if(this.id == 68)
		{
			this.name = "Three Slash Combo";
			this.baseDescription[0] = "The samurai attacks up to 3 times with his Muramasa, causing {G#0} melee damage.\n\nHitting 2 enemies at once reduces this skill cooldown by 1 second.";
			this.multiCasts = 3;
			this.cooldownTicksAfterCast = 240;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.offensive = true;
			this.multiCastDelay = 6;
			this.infos[0].expression = "pp*(0.75+lvl*0.125)";
			this.infos[0].useAsInteger = true;
			this.cooldown[0] = 12f;
			this.cooldown[1] = 11.5f;
			this.cooldown[2] = 11f;
			this.cooldown[3] = 10.5f;
			this.cooldown[4] = 10f;
			this.manualRecast = true;
		}
		else if(this.id == 69)
		{
			this.name = "Break Down";
			this.baseDescription[0] = "The samurai dashes into an enemy, slashing him for {G#0} upon contact, knocking him back and stunning for {1} second.\n\n"
					+ "Finishing an enemy with {OBreak Down} completely resets {OBreak Down} cooldown.";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.offensive = true;
			this.infos[0].useAsInteger = true;
			this.infos[0].expression = "pp*(1.0+lvl*0.3)";
			this.cooldown[0] = 10f;
		}
		else if(this.id == 70)
		{
			this.name = "Bird Freedom";
			this.baseDescription[0] = "The samurai points up his Muramasa, causing an explosion in the terrain and knocking up/causing {G#0} damage to enemies in the area.";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.offensive = true;
			this.infos[0].useAsInteger = true;
			this.infos[0].expression = "pp*(1.0+lvl*0.3)";
			this.cooldown[0] = 10f;
			this.levelRequired = 10;
		}
		else if(this.id == 71)
		{
			this.name = "Gathering Suffer";
			this.baseDescription[0] = "Increases the samurai's Physical Power up to {#0%} based on his lost health.";
			this.passive = true;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.infos[0].expression = "100+lvl*25";
			this.levelRequired = 5;
		}
		else if(this.id == 72)
		{
			this.name = "Undying Spirit";
			this.baseDescription[0] = "Increases the samurai's attack speed by {#0%}, movement speed by {#1%}, "
					+ "life steal by {#2%}, cooldowns accelerated by {100%} and he don't lose stamina while the effect is active. "
					+ " effect lasts for 5 seconds.";
			this.cooldown[0] = 15f;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.infos[0].expression = "50+lvl*12.5";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "20+lvl*20";
			this.infos[2].expression = "5+lvl*1.25";
			this.waitForBuff = 46;
			this.levelRequired = 20;
		}
		else if(this.id == 73)
		{
			this.name = "Invigorating Death";
			this.baseDescription[0] = "Regenerates the samurai's health by {R#0%} and his active skills cooldown by {#1%} whenever he kills an enemy.";
			this.passive = true;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.infos[0].expression = "5+lvl*1.25";
			this.infos[1].expression = "10+lvl*2.5";
			this.levelRequired = 25;
		}
		else if(this.id == 74)
		{
			this.name = "Focus";
			this.baseDescription[0] = "The samurai's next attack is going to mark every enemy hit with {Focus} for 5 seconds. "
					+ "If a {Focused} enemy is attacked by the samurai he receives additional damage equal to {R#0%} {G(+#1%)} of his max health.";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.infos[0].expression = "2+lvl*0.5";
			this.infos[1].expression = "pp*0.002";
			this.cooldown[0] = 15f;
			this.levelRequired = 30;
		}
		else if(this.id == 75)
		{
			this.name = "Breaking the Bushido";
			this.baseDescription[0] = "The samurai dashes into a stunned enemy. This skill can be recast up to 16 times to apply {G#0} (max. of {G#1}) damage and restun surrounding stunned targets for 1 second.\n"
					+ "If the enemies are aerial, both the samurai and the enemy receives Air Time.";
			this.infos[0].expression = "pp*(0.5+lvl*0.125)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "pp*(0.5+lvl*0.125)*16";
			this.infos[1].useAsInteger = true;
			this.multiCasts = 16;
			this.cooldownTicksAfterCast = 180;
			this.multiCastDelay = 6;
			this.cooldown[0] = 30f;
			this.levelRequired = 35;
		}
		else if(this.id == 76)
		{
			this.name = "Killing Blow";
			this.baseDescription[0] = "The samurai slashes up to {#1} next enemies one by one, causing {G#0} damage in each slash while being untargetable.";
			this.cooldown[0] = 15f;
			this.infos[0].expression = "pp*(0.675+lvl*0.125)/2";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "6+lvl";
			this.infos[1].useAsInteger = true;
			this.levelRequired = 15;
		}
		else if(this.id == 77)
		{
			this.name = "Engage";
			this.cooldown[0] = 1f;
			this.multiCasts = 2;
			this.multiCastDelay = 10;
			this.cooldownTicksAfterCast = 60;
			this.freeUse = true;
			this.levelRequired = 1;
		}
		else if(this.id == 78)
		{
			this.name = "Lance Spin";
			this.cooldown[0] = 1f;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.levelRequired = 1;
		}
		else if(this.id == 79)
		{
			this.name = "Lance Throw";
			this.cooldown[0] = 1f;
			this.multiCasts = 3;
			this.multiCastDelay = 15;
			this.cooldownTicksAfterCast = 180;
			this.levelRequired = 1;
		}
		else if(this.id == 80)
		{
			this.name = "Sky Call";
			this.cooldown[0] = 1f;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.levelRequired = 1;
		}
		else if(this.id == 81)
		{
			this.name = "Magic Pressure";
			this.cooldown[0] = 1f;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.levelRequired = 1;
		}
		else if(this.id == 82)
		{
			this.name = "Avalon Lance";
			this.cooldown[0] = 1f;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.levelRequired = 1;
		}
		else if(this.id == 83)
		{
			this.name = "Vital Cut";
			this.cooldown[0] = 1f;
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.levelRequired = 1;
		}
		else if(this.id == 84)
		{
			this.name = "Death Arena";
			this.cooldown[0] = 1f;
			this.element = Constant.DAMAGETYPE_DEATH;
			this.levelRequired = 1;
		}
		else if(this.id == 85)
		{
			this.name = "Fury";
			this.cooldown[0] = 1f;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.levelRequired = 1;
		}
		else if(this.id == 86)
		{
			this.name = "Lance Specialty";
			this.classPassive = true;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.levelRequired = 1;
		}
		else if(this.id == 87)
		{
			this.name = "Lightning Cut";
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldown[0] = 1f;
			this.levelRequired = 1;
		}
		else if(this.id == 88)
		{
			this.name = "Poker";
			this.baseDescription[0] = "The Cassino Gambler throws up to {5} cards, each one causing {C#0} damage upon contact.\n\n"
					+ "Each card has 20% chance to transform into a golden card, increasing its damage to {C#1}.";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.cooldown[0] = 1f;
			this.infos[0].expression = "mp*(0.8+0.1*lvl)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "#0*3";
			this.infos[1].useAsInteger = true;
			this.levelRequired = 1;
		}
		else if(this.id == 89)
		{
			this.name = "Royal Flush";
			this.baseDescription[0] = "The Cassino Gambler throws up to {5} cards into the ground, creating a massive wave of giant cards, each one causing {C#0} damage upon contact.\n\n"
					+ "Each card has 20% chance to transform into a golden card, increasing its damage to {C#1} and stunning struck enemies for {2} seconds.";
			this.infos[0].expression = "mp*(1+lvl*0.2)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "#0*2";
			this.infos[1].useAsInteger = true;
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.cooldown[0] = 1f;
			this.levelRequired = 1;
		}
		else if(this.id == 90)
		{
			this.name = "Truco";
			this.baseDescription[0] = "{RThis skill requires 4 free cards}\n\nThe Cassino Gambler throws {4} cards into the air, after a short time the cards fall down bigger, causing {C#0} damage to struck enemies."
					+ " Each sucessive card receives {#1%} bonus damage.\n\n"
					+ "The final card has 20% chance to transform into a golden card, exploding into minor cards.";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.cooldown[0] = 1f;
			this.infos[0].expression = "mp*(1+lvl*0.2)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "30+lvl*5";
			this.infos[1].useAsInteger = true;
			this.levelRequired = 1;
		}
		else if(this.id == 91)
		{
			this.name = "Gambling";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.cooldown[0] = 1f;
			this.baseDescription[0] = "The Cassino Gambler throws a single card that causes {C#0} damage upon contact, after a short duration the card starts to homes next enemies.\n\n"
					+ "The card has 20% chance to transform into a golden card, increasing its damage to {C#1}.";
			this.infos[0].expression = "mp*(1+lvl*0.2)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "#0*3";
			this.infos[1].useAsInteger = true;
			this.levelRequired = 1;
		}
		else if(this.id == 92)
		{
			this.name = "Jackpot";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.baseDescription[0] = "The Cassino Gambler summons a Jackpot machine falling from the sky, enemies struck receives {C#0} damage. The machine breaks and activates when it touches the ground, "
					+ "starting a jackpot with the following effects:\n\n"
					+ "-=Bomb:=- The jackpot explodes, causing {C#1} damage to nearby enemies.\n"
					+ "-=Cherry:=- The jackpot summons a cherry flavoured area, healing nearby allies by {C#2} every second.\n"
					+ "-=Dollar Sign:=- The jackpot summons a green area, accelerating nearby allies cooldowns by {100%} for {#3} seconds.\n"
					+ "-=Sevens:=- The jackpot summons a red area, increasing nearby allies damages by {77%} for {#4} seconds.\n\n"
					+ "You need at least 2 similar slots to receive a buff. Getting 3 similar slots increases the effects substantially.";
			this.cooldown[0] = 1f;
			this.infos[0].expression = "mp*(1+0.25*lvl)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "mp*(2+lvl*0.25)";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "mp*(1+lvl*0.2)";
			this.infos[2].useAsInteger = true;
			this.infos[3].expression = "10+lvl*2.5";
			this.infos[4].expression = "6+lvl";
			this.levelRequired = 1;
		}
		else if(this.id == 93)
		{
			this.name = "Memento Mori";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.mageUltimate = true;
			this.cooldown[0] = 30f;
			this.manaCost[0] = 50;
			this.manaCost[1] = 45;
			this.manaCost[2] = 40;
			this.manaCost[3] = 35;
			this.manaCost[4] = 30;
			this.baseDescription[0] = "-=Ultimate ability=-\n\nThe mage casts up a shadow skull that follows and hits the selected target, causing {C#0} damage upon contact.";
			this.infos[0].expression = "mp * (7 + 0.5 * lvl)";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 24;
		}
		else if(this.id == 94)
		{
			this.name = "Chronobreak";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.levelRequired = 1;
			this.cooldown[0] = 10f;
			this.channel = true;
			this.maxChannelTime = 90;
			this.maxChannelHoldTime = 90;
			this.manaCost[0] = 0;
			this.baseDescription[0] = "-=Ultimate ability=-\n\nThe mage creates an area that freezes all the projectiles and enemies inside it, the mage and his allies however can still move inside it.";
			this.levelRequired = 28;
		}
		else if(this.id == 95)
		{
			this.name = "Skyblades";
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldown[0] = 1f;
			this.levelRequired = 1;
		}
		else if(this.id == 96)
		{
			this.name = "Arcane Dagger";
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldown[0] = 1f;
			this.levelRequired = 1;
		}
		else if(this.id == 97)
		{
			this.name = "Soul Explosion";
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldown[0] = 1f;
			this.levelRequired = 5;
		}
		else if(this.id == 98)
		{
			this.name = "Mind Distortion";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.cooldown[0] = 1f;
			this.levelRequired = 10;
		}
		else if(this.id == 99)
		{
			this.name = "Berserk";
			this.baseDescription[0] = "Increases your attack speed by +80% and change the {Berserking Axe}'s damage type to [RED]Blood[] for 6 seconds.";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.cooldown[0] = 20f;
			this.levelRequired = 1;
		}
		else if(this.id == 100)
		{
			this.name = "Fire Bomb";
			this.element = Constant.DAMAGETYPE_FIRE;
			this.cooldown[0] = 2f;
			this.multiCasts = 5;
			this.cooldownTicksAfterCast = 300;
			this.multiCastDelay = 10;
			this.levelRequired = 1;
		}
		else if(this.id == 101)
		{
			this.name = "Rain";
			this.element = Constant.DAMAGETYPE_FIRE;
			this.cooldown[0] = 2f;
			this.levelRequired = 1;
		}
		else if(this.id == 102)
		{
			this.name = "Thousand Blades";
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldown[0] = 2f;
			this.channel = true;
			this.maxChannelTime = 300;
			this.maxChannelHoldTime = 300;
			this.levelRequired = 35;
		}
		else if(this.id == 103)
		{
			this.name = "Soul Grab";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.cooldown[0] = 2f;
			this.levelRequired = 15;
		}
		else if(this.id == 104)
		{
			this.name = "Arcane Trap";
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldown[0] = 2f;
			this.levelRequired = 20;
		}
		else if(this.id == 105)
		{
			this.name = "Critical Situation";
			this.passive = true;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.infos[0].expression = "2 + lvl";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 25;
		}
		else if(this.id == 106)
		{
			this.name = "Sneaky Attack: Mana";
			this.classPassive = true;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.infos[0].expression = "mp * (0.5 + lvl * 0.125)";
			this.levelRequired = 1;
		}
		else if(this.id == 107)
		{
			this.name = "Alliance";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.cooldown[0] = 2f;
			this.levelRequired = 30;
		}
		else if(this.id == 108)
		{
			this.name = "Hemoxplosion";
			this.element = Constant.DAMAGETYPE_BLOOD;
			this.manaCost[0] = 25;
			this.cooldown[0] = 10f;
			this.levelRequired = 1;
			this.infos[0].expression = "mp * (2 + lvl * 0.25)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "15";
			this.infos[1].useAsInteger = true;
			this.baseDescription[0] = "The hemomancer creates a blood explosion around him, causing {C#0} damage to nearby enemies.\n\n"
					+ "{RThis skill consumes #1% of the hemomancer max health.}";
		}
		else if(this.id == 109)
		{
			this.name = "Vampiric Mercenary";
			this.element = Constant.DAMAGETYPE_BLOOD;
			this.manaCost[0] = 20;
			this.cooldown[0] = 15f;
			this.infos[0].expression = "10+lvl*5";
			this.infos[1].expression = "10 + lvl*10";
			this.baseDescription[0] = "Creates a {OBlood Mark} on the target.\n\n"
					+ "The {OBlood Mark} increases the blood damage received by {+#1%} and returns {#0%} of the damage taken as health.";
			this.levelRequired = 5;
		}
		else if(this.id == 110)
		{
			this.name = "Spell Vampire";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.passive = true;
			this.infos[0].expression = "2 + lvl*2";
			this.baseDescription[0] = "Increases the hemomancer blood damage life steal by {+#0%}.";
			this.levelRequired = 10;
		}
		else if(this.id == 111)
		{
			this.name = "Hemoball";
			this.element = Constant.DAMAGETYPE_BLOOD;
			this.manaCost[0] = 15;
			this.cooldown[0] = 10f;
			this.cooldown[1] = 9f;
			this.cooldown[2] = 8f;
			this.cooldown[3] = 7f;
			this.cooldown[4] = 6f;
			this.channel = true;
			this.maxChannelTime = 30;
			this.maxChannelHoldTime = 60;
			this.baseDescription[0] = "The hemomancer channels a blood ball, increasing the ball speed and damage.\n"
					+ "The hemoball explodes, damaging nearby enemies from {C#0} to {C#1} based on the channel.";
			this.infos[0].expression = "mp * (2 + lvl * 0.25)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "#0*2";
			this.infos[1].useAsInteger = true;
			this.levelRequired = 15;
		}
		else if(this.id == 112)
		{
			this.name = "Vlad Sword";
			this.element = Constant.DAMAGETYPE_BLOOD;
			this.baseDescription[0] = "The hemomancer creates 7 falling magic swords on the targeted area, each causing {C#0} damage and stunning struck enemies for 2 seconds.";
			this.cooldown[0] = 15f;
			this.manaCost[0] = 30;
			this.infos[0].expression = "mp * (2.5 + lvl * 0.2)";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 20;
		}
		else if(this.id == 113)
		{
			this.name = "Ritual";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.cooldown[0] = 30f;
			this.manaCost[0] = 40;
			this.baseDescription[0] = "The hemomancer reveals the blood sign, receiving the {OBlood Sign} buff for 8 seconds.\n\n"
					+ "The {OBlood Sign} increases the hemomancer movement speed by {+#1%} and accelerates all skill cooldowns by {+#0%}.";
			this.infos[0].expression = "100 + lvl * 25";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "30 + lvl * 10";
			this.infos[1].useAsInteger = true;
			this.waitForBuff = 62;
			this.levelRequired = 25;
		}
		else if(this.id == 114)
		{
			this.name = "Black Hole";
			this.element = Constant.DAMAGETYPE_DARKNESS;
			this.cooldown[0] = 30f;
			this.manaCost[0] = 50;
			this.manaCost[1] = 45;
			this.manaCost[2] = 40;
			this.manaCost[3] = 35;
			this.manaCost[4] = 30;
			this.channel = true;
			this.infos[0].expression = "mp * (8 + lvl * 0.5)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "3";
			this.baseDescription[0] = "-=Ultimate ability=-\n\nThe mage channels up a massive black hole over 5 seconds.\n\nIf the channel was completed the mage can release the channel to contract the black hole, exploding it and causing {C#0} damage in a huge area.";
			this.maxChannelTime = 300;
			this.maxChannelHoldTime = 600;
			this.mageUltimate = true;
			this.levelRequired = 32;
		}
		else if(this.id == 115)
		{
			this.name = "Arcane Elevation";
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldown[0] = 30f;
			this.manaCost[0] = 50;
			this.manaCost[1] = 45;
			this.manaCost[2] = 40;
			this.manaCost[3] = 35;
			this.manaCost[4] = 30;
			this.baseDescription[0] = "-=Ultimate ability=-\n\n"
					+ "The mage power ups himself, increasing his mana regeneration by {+#0m/s}, accelerating his cooldowns by {100%} and fills Elemental Overbreak passively over {15} seconds.";
			this.infos[0].expression = "20+lvl*5";
			this.infos[0].useAsInteger = true;
			this.waitForBuff = 65;
			this.mageUltimate = true;
			this.levelRequired = 36;
		}
		else if(this.id == 116)
		{
			this.name = "Life Bubble";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.baseDescription[0] = "The mage shields up himself with a barrier, absorbing up to {C#0} damage.\n\n"
					+ "When the shield expires, all the barrier left is converted to health.\n\n"
					+ "-=Elemental Overbreak:=- The mage receives {C#1} health instantly and the shield is doubled.";
			this.cooldown[0] = 5f;
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.infos[0].expression = "mp * (1.6+lvl*0.2)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "mp * (1.6+lvl*0.2)*2";
			this.infos[1].useAsInteger = true;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.waitForBuff = 66;
			this.levelRequired = 1;
		}
		else if(this.id == 117)
		{
			this.name = "Color Beam";
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.baseDescription[0] = "Mage placeholder skill for the Colormancer class.\n\n{C#0} damage";
			this.infos[0].expression = "mp * (1.25 + 0.3 * lvl)";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 40;
		}
		else if(this.id == 118)
		{
			this.name = "Tesseract";
			this.element = Constant.DAMAGETYPE_FIRE;
			this.cooldown[0] = 5f;
			this.cooldown[1] = 4.5f;
			this.cooldown[2] = 4f;
			this.cooldown[3] = 3.5f;
			this.cooldown[4] = 3f;
			this.manaCost[0] = 20;
			this.manaCost[1] = 17;
			this.manaCost[2] = 15;
			this.manaCost[3] = 12;
			this.manaCost[4] = 10;
			this.channel = true;
			this.baseDescription[0] = "Mage placeholder skill for the Colormancer class.\n\n{C#0} damage";
			this.maxChannelTime = 30;
			this.maxChannelHoldTime = 60;
			this.infos[0].expression = "mp * (1 + 0.25 * lvl)";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 44;
		}
		else if(this.id == 119)
		{
			this.name = "Graceful Attack";
			this.element = Constant.DAMAGETYPE_ENERGY;
			this.cooldown[0] = 1f;
			this.baseDescription[0] = "Mage placeholder skill for the Colormancer class.\n\n{C#0} damage per beam";
			this.infos[0].expression = "mp * (0.4 + 0.1 * lvl)";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 48;
		}
		else if(this.id == 120)
		{
			this.name = "Rest in Scythes";
			this.element = Constant.DAMAGETYPE_DEATH;
			this.baseDescription[0] = "The reaper summons his scythe and executes a slash using it, causing {G#0} damage to struck enemies. The scythe attack applies "
					+ "{OSneaky Attack: Fatality} effects with 100% critical strike chance.\n\n"
					+ "Hitting enemies reduces {ORest in Scythes}'s cooldown by {1} second and killing enemies completely "
					+ "resets {ORest in Scythe}'s cooldown.";
			this.cooldown[0] = 10f;
			this.manaCost[0] = 10;
			this.manaCost[1] = 8;
			this.manaCost[2] = 5;
			this.manaCost[3] = 2;
			this.manaCost[4] = 0;
			this.infos[0].expression = "pp*(1+lvl*0.25)";
			this.infos[0].useAsInteger = true;
			this.offensive = true;
			this.levelRequired = 1;
		}
		else if(this.id == 121)
		{
			this.name = "Sneaky Attack: Fatality";
			this.baseDescription[0] = "Sneaky Attacks instantly executes enemies with less than {#0%} health.";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.classPassive = true;
			this.infos[0].expression = "20+lvl*5";
			this.infos[0].useAsInteger = true;
			this.levelRequired = 1;
		}
		else if(this.id == 122)
		{
			this.name = "Card Master";
			this.baseDescription[0] = "-=Magic Cards:=- The Cassino Gambler is surrounded by {#0} floating cards. Cards are "
					+ "used to cast offensive skills.\n\n"
					+ "-=Lying:=- Each unused card increases Cassino Gambler's magic power by {C#2} {C(#1%)}.";
			this.element = Constant.DAMAGETYPE_GOOD;
			this.classPassive = true;
			this.infos[0].expression = "2 + lvl * 2";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "5+lvl*2";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "mp*(#1/100)";
			this.infos[2].useAsInteger = true;
			this.levelRequired = 1;
		}
		else if(this.id == 123)
		{
			this.name = "Thousand Cuts";
			this.baseDescription[0] = "Shows the true speed of the Golden Dragon Katana, slashing 100 times for {G#0} damage.";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.infos[0].expression = "pp * 0.5";
			this.infos[0].useAsInteger = true;
			this.channel = true;
			this.maxChannelTime = 300;
			this.maxChannelHoldTime = 300;
			this.levelRequired = 1;
		}
		else if(this.id == 124)
		{
			this.name = "Ghost Bow";
			this.baseDescription[0] = "Shoots 50 arrows in a rapid sequence, each causing {G#0} damage.";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.infos[0].expression = "pp * 0.5";
			this.infos[0].useAsInteger = true;
			this.attackRestricted = true;
			this.levelRequired = 1;
		}
		else if(this.id == 125)
		{
			this.name = "Ghost Blade";
			this.element = Constant.DAMAGETYPE_PHYSICAL;
			this.infos[0].expression = "pp * 0.5";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "pp * 2";
			this.infos[1].useAsInteger = true;
			this.attackRestricted = true;
			this.levelRequired = 1;
		}
		else if(this.id == 126)
		{
			this.name = "Divine Duo";
			this.baseDescription[0] = "The ranger draws two revolvers and rapidly shoots three times, the bullets are magical beams that explodes "
					+ "upon contact, causing {G#0} damage to nearby enemies.\n\n"
					+ "The {Othird shot} is stronger and causes {G#1} damage with double explosion radius.";
			this.element = Constant.DAMAGETYPE_HOLY;
			this.manaCost[0] = 30;
			this.cooldown[0] = 10f;
			this.infos[0].expression = "pp * (1.5 + lvl * 0.125)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "#0 * 2.5";
			this.infos[1].useAsInteger = true;
			this.levelRequired = 30;
		}
		else if(this.id == 127)
		{
			this.name = "Shadow Shuriken";
			this.baseDescription[0] = "The rogue throws out a shuriken, causing {G#0} damage and applying a {OShadow Mark} to the enemy struck.\n\n"
					+ "After 1 second the {OShadow Mark} explodes, causing {GC#1} damage to nearby enemies.\n\n"
					+ "The shuriken does not collide with the terrain.";
			this.cooldown[0] = 12f;
			this.cooldown[1] = 11f;
			this.cooldown[2] = 10f;
			this.cooldown[3] = 9f;
			this.cooldown[4] = 8f;
			this.manaCost[0] = 10;
			this.manaCost[4] = 5;
			this.element = Constant.DAMAGETYPE_DARKNESS;
			this.levelRequired = 15;
			this.offensive = true;
			this.infos[0].expression = "pp * (1 + lvl * 0.125)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "pp * (1.5 + lvl * 0.25) + mp * (2 + lvl * 0.125)";
			this.infos[1].useAsInteger = true;
		}
		else if(this.id == 128)
		{
			this.name = "Sparkles";
			this.baseDescription[0] = "The ranger summons 4 stars called {OThe Sparkles} who increases his damage indirectly.\n\n"
					+ "Whenever a ranger's projectile collides with an enemy each {OSparkle} shoots a projectile that homes the enemy and causes {GC#0} damage.\n\n"
					+ "{OThe Sparkles} can not attack in less than 0.1s and lasts for 8 seconds.";
			this.cooldown[0] = 15f;
			this.manaCost[0] = 20;
			this.element = Constant.DAMAGETYPE_GOOD;
			this.levelRequired = 5;
			this.infos[0].expression = "pp * (0.15 + lvl * 0.025) + mp * (0.15 + lvl * 0.025)";
			this.waitForBuff = 76;
		}
		
	/*	public String name;
		public String[] baseDescription = new String[5];
		public float[] cooldown = new float[5];
		public boolean passive;
		public boolean classPassive;
		public int castTime = 0;
		public int manaCost = 0;
		public boolean attackRestricted;
		public int multiCasts = 1;
		public int cooldownTicksAfterCast = 0;
		public CompactAnim castAnim = null;
		public int element;
		public boolean offensive;
		public boolean debugging; // for castTime precision
		public boolean channel;
		public int multiCastDelay;
		public int maxChannelTime;
		public int totalCasts=0;
		public int maxChannelHoldTime;
		public int[] extraInfos = new int[5];
	*/
		return this;
	}
	
	public void onStopChannel(final Player player, int ticks)
	{
		this.mouseOnCast = player.mousePos.cpy();
		this.posOnCast = player.Center().cpy();
		Vector2 mouseNor = this.mouseOnCast.cpy().sub(player.Center());
		mouseNor.nor();
		float perc = (float)ticks/(float)this.maxChannelTime;
		final Skill skill = this;
		if(this.id == 44)
		{
			float angle = Main.angleBetween(player.Center(), this.mouseOnCast);
			Vector2 pos = new Vector2(64, 0).rotate(angle);
			float damage = this.getInfoValueF(player, 0) + this.getInfoValueF(player, 1) * perc;
			Projectile p = Projectile.Summon(41, player.Center().add(pos), mouseNor.scl(200f), (int) damage, 10f, player);
			if(perc >= 0.5f)
			{
				p.ai[0] = 1;
			}
			player.playAnim(Content.psa[6], 2, 3, true, false, true);
			player.playAnim2(Content.blank, 2, 3, false, false, false);
			this.applyCast(player);
		}
		else if(this.id == 49)
		{
			boolean eob = ArkaClass.hasEoB(player);
			for(int i = 0;i < 400 + ticks * 10;i++)
			{
				float range = MathUtils.random(250f);
				double angle = ((Main.angleBetween(Vector2.Zero, mouseNor) + MathUtils.random(eob ? -30 : -20, eob ? 30 : 20)) * Math.PI/180f);
				double sin = Math.sin(angle);
				double cos = Math.cos(angle);
				Vector2 vel = new Vector2((float)(cos * (500-range) * 5), (float)(sin * (500-range)) * 5);
				Vector2 offset = new Vector2(64, 0).rotate(Main.angleBetween(Vector2.Zero, mouseNor));
				Vector2 offset2 = new Vector2((float)(cos * range), (float)(sin * range));
				float f = MathUtils.random()/4f + 0.75f;
				Particle p = Particle.Create(player.Center().add(offset.add(offset2)).add(MathUtils.random(-8, 8), MathUtils.random(-8, 8)), vel, 2, new Color(f, f, f-0.25f, 1f), 0f, 1f, 0.5f + MathUtils.random()/2f);
				p.loseSpeed = true;
				p.loseSpeedMult = 0.9f;
				p.setLight(16, p.color);
			}
			
			for(Monster m : Constant.getMonstersInCone(player.myMapX, player.myMapY, player.Center(), 500f, Main.angleBetween(Vector2.Zero, mouseNor), eob ? 75 : 45))
			{
				float angle = Main.angleBetween(player.Center(), m.Center());
				Vector2 velocity = new Vector2(500f + ticks * 30, 0f).rotate(angle);
				m.velocity = velocity;
				m.Stun(perc + (eob ? 1f : 0f), player);
			}
			this.applyCast(player);
		}
		else if(this.id == 60)
		{
			player.playAnim(Content.psa[10], 5, 2, false, true, true, new Vector2(0f, 0f));
			Main.doEarthquake(40);
			float damage = 0f;
			if(player.haveBuff(43) != -1)
			{
				Buff b = player.buffs[player.haveBuff(43)];
				damage = b.stacks;
			}
			for(Monster m : Constant.getMonstersInRange(player, this.getChannelTicks()*2+50f))
			{
				m.hurt((int) damage, Main.directionFromTo(player, m), 2f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
				m.Stun(0.5f + perc, player);
			}
			for(int i = -this.getChannelTicks()*2;i <= this.getChannelTicks()*2;i++)
			{
				try
				{
					Vector2 pos = new Vector2(player.Center().x,player.position.y - 8).add(i + MathUtils.random(-4, 4),  MathUtils.random(-16, 16));
					GameMap g = Constant.getPlayerMap(player.whoAmI);
					Color color = g.map[Main.clamp(0, (int)Math.floor((pos.y/64f)-1), g.height-1)][Main.clamp(0, (int)Math.floor((pos.x/64f)), g.width-1)].color;
					Particle p = Particle.Create(pos, new Vector2(MathUtils.random(0, 50), MathUtils.random(100, 400)), 2, color, -3f, 3f, 1f);
					p.drawBack = true;
				}
				catch(Exception ex)
				{continue;}
			}
			player.removeBuff(43);
			player.addBuff(44, 4 + 6 * perc, player);
			Projectile p = Projectile.Summon(57, player.Center(), new Vector2(0f, 20f), 0, 3f, player);
			p.ai[5] = 1;
			this.applyCast(player);
		}
		else if(this.id == 62)
		{
			if(perc >= 1f)
			{
				player.myMapX = 0;
				player.myMapY = 0;
				Main.SwitchMap(0, new Vector2(6400, 384), null);
				this.applyCast(player);
			}
		}
		else if(this.id == 102)
		{
			this.applyCast(player);
		}
		else if(this.id == 111)
		{
			Vector2 pos = mouseNor.cpy().scl(64f).add(player.Center());
			Vector2 vel = mouseNor.cpy().scl(1000f + perc * 1000);
			float min = this.getInfoValueF(player, 0);
			float max = this.getInfoValueF(player, 1);
			float damage = min + ((max-min) * perc);
			Projectile p = Projectile.Summon(98, pos, vel, (int)damage, 5f, player);
			p.ai[5] = 150 + perc * 150;
			this.applyCast(player);
		}
		else if(this.id == 114)
		{
			if(perc >= 0.95f)
			{
				for(int x = 0;x < 60;x++)
				{
					final int loop = x;
					Event e2 = new Event(x) {
						@Override
						public void function()
						{
							for(int i = 0;i < 10;i++)
							{
								Vector2 center = player.Center().add(0f, 350f);
								float angle = MathUtils.random(360f);
								float sin = (float)Math.sin(angle * Math.PI/180f);
								float cos = (float)Math.cos(angle * Math.PI/180f);
								Vector2 vel = new Vector2(cos, sin).scl(MathUtils.random(1200f, 1600f));
								Particle p = Particle.Create(center, vel, 2, new Color(0.5f, 0f, 0.5f, 1f), 0f, 1f, 1f);
								p.rotation = vel.angle();
								p.width = 16;
								p.height = 4;
								p.setLight(32, new Color(0.5f, 0.1f, 0.5f, 1f));
							}
							float scale = (float)(Math.sin(((60-loop)/60f) * 90f * Math.PI/180f) * 600f);
							Particle p2 = Particle.Create(player.Center().add(0f, 350f), Vector2.Zero, 29, new Color(0.5f, 0f, 0.5f, 1f), 0f, 0.05f, scale/300f);
							p2.rotation = loop/2f;
							p2.setLight((int)scale, p2.color);
							Lighting.Create(p2.Center(), scale*2, p2.color, 0.1f);
						}
					};
					Main.scheduledTasks.add(e2);
				}
				Event e = new Event(60) {
					@Override
					public void function()
					{
						Vector2 center = player.Center().add(0f, 350f);
						for(int i = 0;i < 300;i++)
						{
							float angle = MathUtils.random(360f);
							float sin = (float)Math.sin(angle * Math.PI/180f);
							float cos = (float)Math.cos(angle * Math.PI/180f);
							Vector2 vel = new Vector2(cos, sin).scl(MathUtils.random(300, 1500));
							Particle p = Particle.Create(center, vel, 2, new Color(0.5f, 0f, 0.5f, 1f), 0f, 1f, 1f);
							p.rotation = vel.angle();
							p.width = 16;
							p.height = 4;
							p.setLight(32, new Color(0.5f, 0.1f, 0.5f, 1f));
						}
						for(int i = 0;i < 240;i++)
						{
							double angle = (i * 1.5f) * Math.PI/180f;
							float fc = MathUtils.random()/4f + 0.3f;
							Vector2 pos = center.cpy().add(MathUtils.random(-4, 4), MathUtils.random(-4, 4));
							Vector2 vel = new Vector2(100f, 0f).rotate((float) Math.toDegrees(angle));
							Particle p = Particle.Create(pos, vel, 2, new Color(fc, 0f, fc, 1f), 0.5f, 1f, 1f);
							p.drawBack = true;
							p.setLight(16, new Color(0.5f, 0.1f, 0.5f, 1f));
						}
					}
				};
				Main.scheduledTasks.add(e);
				Event e3 = new Event(120) {
					@Override
					public void function()
					{
						Vector2 center = player.Center().add(0f, 350f);
						Main.doEarthquake(30);
						for(int i = 0;i < 240;i++)
						{
							double angle = (i * 1.5f) * Math.PI/180f;
							float sin = (float)Math.sin(angle);
							float cos = (float)Math.cos(angle);
							float fc = MathUtils.random()/4f + 0.3f;
							Vector2 pos = new Vector2(cos * 96, sin * 96).add(center).add(MathUtils.random(-4, 4), MathUtils.random(-4, 4));
							Vector2 vel = new Vector2(600f, 0f).rotate(Main.angleBetween(center, pos));
							Particle p = Particle.Create(pos, vel, 2, new Color(fc, 0f, fc, 1f), 0.5f, 1f, 1f);
							p.drawBack = true;
							p.setLight(16, new Color(0.5f, 0.1f, 0.5f, 1f));
						}
						Color[] quadcolors = new Color[4];
						quadcolors[0] = new Color(0.5f, 0.05f, 0.5f, 1f);
						quadcolors[1] = new Color(0.4f, 0.05f, 0.4f, 1f);
						quadcolors[2] = new Color(0.3f, 0.05f, 0.3f, 1f);
						quadcolors[3] = new Color(0.2f, 0.05f, 0.2f, 1f);
						Color smokeColor = new Color(0.4f, 0f, 0.4f, 1f);
						Main.createCustomExplosion(center, 1000, 3f, false, quadcolors, smokeColor);
						
						for(Monster m : Constant.getMonstersInRange(player.myMapX, player.myMapY, center, 1000f))
						{
							m.hurtDmgVar(skill.getInfoValueI(player, 0), Main.directionFromTo(player, m), 3f, player.canCritical(m), Constant.DAMAGETYPE_DARKNESS, player);
							m.Stun(skill.getInfoValueF(player, 1), player);
						}
					}
				};
				Main.scheduledTasks.add(e3);
				player.playAnim(Content.handLiftedUp, 1, 130, false, true, false);
				Main.forceShowOff(2.5f);
				this.applyCast(player);
			}
		}
		else if(this.id == 118)
		{
			if(perc >= 0.97f)
			{
				float angle = mouseNor.angle();
				float sin = (float)Math.sin(angle * Math.PI/180f);
				float cos = (float)Math.cos(angle * Math.PI/180f);
				float range = 64f;
				Vector2 pos = new Vector2(cos * range, sin * range).add(player.Center());
				Projectile.Summon(110, pos, mouseNor.cpy().scl(300f), this.getInfoValueI(player, 0), 5f, player);
				this.applyCast(player);
			}
		}
		else if(this.id == 94 && perc >= 1f)
		{
			Projectile p = Projectile.Summon(91, player.Center(), Vector2.Zero, 0, 10f, player);
			p.ai[5] = 6;
			p.scale = 4f;
			this.applyCast(player);
		}
		System.out.println("Finished cast: " + ticks + " (" + (perc*100) + "%)");
		this.channeling = false;
		this.channelTicks = 0;
	}

	public void onCast(final Player player)
	{
		if(this.debugging)
			this.castTime = Main.var[6];

		this.mouseOnCast = player.mousePos.cpy();
		this.posOnCast = player.Center().cpy();
		final Vector2 mouseNor = this.mouseOnCast.cpy().sub(player.Center());
		mouseNor.nor();
		
		if((player.mana < this.getMana(player) && this.casts <= 0) || this.cdf > 0f)
			return;

		if(this.mageUltimate)
		{
			if(player.arcaneCircle == 0)
			{
				player.arcaneCircle = 1;
				player.arcaneCircleElement = this.element;
				return;
			}
			else if(player.arcaneCircle < 4)
			{
				return;
			}
			else if(this.element != player.arcaneCircleElement)
			{
				return;
			}
		}
		
		if(this.channel)
		{
			this.channeling = true;
			this.channelTicks++;
		}
		
		if(this.id == 1)
		{
			player.addBuff(17, 10f, player);
			applyCast(player);
		}
		else if(this.id == 2)
		{
			player.controllingGeas = true;
			for(Projectile p : Constant.getProjectileList())
			{
				if(p.active && p.type == 3)
				{
					p.ai[7] = player.mousePos.cpy().x;
					p.ai[8] = player.mousePos.cpy().y;
				}
			}
		}
		else if(this.id == 3)
		{
			int slot = player.haveBuff(4);
			if(slot != -1 && player.buffs[slot].stacks >= 5)
			{
				applyCast(player);
				player.playAnim(Content.sbA, 10, 2, true, true, true);
				player.removeBuff(4);
			}
		}
		else if(this.id == 4)
		{
			applyCast(player);
			player.playAnim(Content.fbA, 9, 4, true, false, false);
		}
		else if(this.id == 6)
		{
			for(int i = 0;i < 40;i++)
			{
				float angle = (float) (MathUtils.random() * Math.PI * 2);
				float sin = (float)(Math.sin(angle));
				float cos = (float)(Math.cos(angle));
				float strenght = MathUtils.random(30, 100);
				Particle p = Particle.Create(player.getHandPosition(), new Vector2(cos * strenght, sin * strenght), 7, new Color(1f, 1f, MathUtils.random(), 1f), 0f, 1f, 1f);
				p.drawFront = true;
				p.rotation = MathUtils.random(360);
			}
			player.addBuff(6, 10f, player);
			player.resetWeaponCdf();
			applyCast(player);
		}
		else if(this.id == 7)
		{
			if(player.grounded)
				return;

			player.addBuff(7, 4f, player);
			applyCast(player);
		}
		else if(this.id == 8)
		{
			Item item = player.inventory[Constant.ITEMSLOT_RIGHT];
			if(item == null || item.itemclass != Constant.ITEMCLASS_SHIELD)
				return;
			
			if(this.casts == 0)
			{
				player.playAnim2(item.getAttackTexture(), item.attackFrames, 1, true, false, true, new Vector2(-32, 0));
				mouseNor.scl(50f);
				Vector2 pos = new Vector2(player.Center().x + mouseNor.x, player.Center().y + mouseNor.y);
				Projectile p = Projectile.Summon(12, pos, Vector2.Zero.cpy(), 0, 5f, player);
				p.rotation = (float)Math.toDegrees((Math.atan2(mouseNor.y, mouseNor.x)));
				this.setCooldown(0.15f);
				applyCast(player);
			}
			else
			{
				player.resetCustomAnim();
				player.skillCasting = false;
				player.skillTicksLeft = 0;
				for(Projectile p : Constant.getProjectileList())
				{
					if(p.active && p.type == 12)
					{
						p.destroy();
					}
				}
				applyCast(player);
			}
		}
		else if(this.id == 10)
		{
			player.addBuff(10, 5f, player);
			player.resetWeaponCdf();
			applyCast(player);
		}
		else if(this.id == 11)
		{
			for(int i = 0;i < 50;i++)
			{
				Particle p = Particle.Create(player.randomHitBoxPosition().add(MathUtils.random(-10, 10), MathUtils.random(-10, 10)), player.velocity.cpy().scl(-0.2f).add(MathUtils.random(-20, 20), MathUtils.random(-20, 20)), 2, new Color(), 0f, 0.5f, 1f);
				p.drawFront = true;
				p.setLight(32, Color.PURPLE);
			}
			player.addBuff(11, 5f, player);
			player.addBuff(27, 5f, player);
			applyCast(player);
		}
		else if(this.id == 12)
		{
			Item item = player.inventory[Constant.ITEMSLOT_RIGHT];
			if(item == null || item.itemclass != Constant.ITEMCLASS_SHIELD)
				return;

			updateDirection(player);
			player.velocity.x = player.maxSpeed * player.direction * 2;
			player.addBuff(40, 0.5f, player);
			player.resetCollisions();
			player.resetWeaponCdf();
			applyCast(player);
		}
		else if(this.id == 13)
		{
			player.resetCollisions();
			player.ragingDash = 60;
			int direction = 1;
			if(player.mousePos.cpy().sub(player.Center()).x < 0)
				direction = -1;
			
			player.velocity.x = player.maxSpeed*2*direction;
			player.direction = direction;
			player.playAnim2(Content.handPointing, 1, 60, false, true, true);
			player.playAnim(Content.playerHandStand, 1, 60, false, false, false);
			applyCast(player);
		}
		else if(this.id == 14)
		{
			Monster target = Main.getMouseTarget(1500f);
			if(target == null)
				return;

			float dmg = player.getPP() * this.valueByLevel("0.2/0.225/0.25/0.275/0.3");
			Projectile p = Projectile.Summon(14, player.Center(), mouseNor.scl(3000f), (int)(dmg), 6f, player);
			p.setTarget(target);
			player.playThrowAnim(false);
			applyCast(player);
		}
		else if(this.id == 15)
		{
			player.timeForStamina = 0f;
			player.stamina += player.maxStamina * (this.getInfoValueF(player, 0)/100f);
			player.removeBuff(21);
			for(int i = 0;i <= 37;i++)
			{
				final int loop = i;
				Main.scheduledTasks.add(new Event(i) {
					@Override
					public void function()
					{
						Color color = new Color(0.92f, 0.96f, 0.65f, 1f);
						Lighting.Create(player.Center(), 512, color, 1f, 0.7f);
						for(int j = 0;j <= Math.min(loop*2, 72);j++)
						{
							Vector2 pos = new Vector2(126f-loop, 0f).setAngle(j*5);
							Vector2 vel = Vector2.Zero.cpy();
							if(loop == 35)
								vel = new Vector2(1000f, 0f).setAngle(j*5);
							Particle p = Particle.Create(pos.add(player.Center()), vel, 16, color, -0.5f, loop == 35 ? 1f : 0.3f, 1f).setLight(32, Main.getLightingColor(color, 0.3f));
							p.loseSpeed = true;
							p.loseSpeedMult = 0.92f;
						}
					}
				});
			}
			applyCast(player);
		}
		else if(this.id == 16)
		{
			if(player.rolling || !player.grounded)
				return;
			
			Vector2 pos = mouseNor.scl(120f);
			Projectile p = Projectile.Summon(15, player.Center().add(pos), Vector2.Zero, (int)player.weaponsDamage()*2, 6f, player);
			p.rotation = pos.angle();
			if(pos.x < 0)
				p.flipped = true;
			
			player.playAnim(Content.handLiftedUp, 1, 30, false, true, true);
			player.velocity.x = 0;
			player.direction = player.mouseOnAttack.cpy().sub(player.Center()).x < 0 ? -1 : 1;
			applyCast(player);
		}
		else if(this.id == 17)
		{
			player.addBuff(14, 10f, player);
			applyCast(player);
		}
		else if(this.id == 18)
		{
			if(player.haveBuff(18) == -1)
				player.addBuff(18, 300f, player);
			else
				player.removeBuff(18);
			
			this.applyCast(player);
		}
		else if(this.id == 19)
		{
			Vector2 destiny = Vector2.Zero.cpy();
			if(this.casts != 1)
			{
				Vector2 add = player.mousePos.sub(player.Center()).limit(400f);
				destiny = player.position.cpy().add(add);
				
				SavedWorld.monster = new ArrayList<Monster>(Main.monster);
				SavedWorld.Save();
				this.extraInfos[2] = player.direction;
				this.extraInfos[3] = (int) player.velocity.x;
				this.extraInfos[4] = (int) player.velocity.y;
			}
			else
			{
				Main.monster = new ArrayList<Monster>(SavedWorld.monster);
				SavedWorld.Load();
				
				player.direction = (this.extraInfos[0] < player.Center().x ? -1 : 1);
				destiny = new Vector2(this.extraInfos[0], this.extraInfos[1]);
				
				Vector2 origin = player.Center().cpy();
				player.velocity = new Vector2(800f, 0f).rotate(Main.angleBetween(player.Center(), destiny));
				player.setCustomDisacceleration(Main.ticksToSeconds(10), 0.985f);
				player.position.set(destiny);
				player.velocity.set(new Vector2(this.extraInfos[3], this.extraInfos[4]));
				player.direction = this.extraInfos[2];
				int directionX = (destiny.x > 0 ? 1 : -1);
				int directionY = (destiny.y > 0 ? 1 : -1);
				if(Constant.getPlayerMap(player.whoAmI).doesRectCollideWithMap2(player.position.x, player.position.y, player.width, player.height))
					player.position = Constant.getPlayerMap(player.whoAmI).getNextestFreeSpace(player.position.x, player.position.y, player.width, player.height, directionX, directionY).scl(64f);
				
				int damage = this.getInfoValueI(player, 0);
				
				Projectile p = Projectile.Summon(23, origin, player.Center().sub(origin).nor().scl(100f), damage, 6f, player);
				p.ai[7] = player.Center().x;
				p.ai[8] = player.Center().y;

				player.setInvincible(0.1f);
			}
			this.extraInfos[0] = (int) player.position.x;
			this.extraInfos[1] = (int) player.position.y;
			this.applyCast(player);
		}
		else if(this.id == 20)
		{
			player.direction = Main.directionFromTo(player.Center().x, mouseOnCast.x);
			float x = player.Center().x;
			if(mouseOnCast.x < player.Center().x)
			{
				x -= 128;
			}
			float y = player.Center().y - 128;
			for(Monster m : Constant.getMonstersInArea(player.myMapX, player.myMapY, x, y, 128, 256))
			{
				float mult = 0.6f * (this.level + 0.2f);
				m.hurtDmgVar((int) (player.getPP() * mult), Main.directionFromTo(player, m), 1f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
			}
			this.applyCast(player);
		}
		else if(this.id == 21)
		{
			float dmg = this.getInfoValueF(player, 0);
			Projectile.Summon(24, player.Center(), mouseNor.scl(250f), (int) (dmg), 10f, player);
			player.playThrowAnim();
			this.applyCast(player);
		}
		else if(this.id == 22)
		{
			player.direction = Main.directionFromTo(player.Center().x, mouseOnCast.x);
			player.setUntargetable(Math.min(0.45f, 0.45f/player.getAttackSpeed()));
			player.setCustomDisacceleration(Math.min(0.45f, 0.45f/player.getAttackSpeed()), 0.92f);
			player.velocity.x = 2500 * player.direction;
			this.applyCast(player);
		}
		else if(this.id == 23)
		{
			updateDirection(player);
			player.playAnim(Content.handLiftedUp, 1, 30, false, false, true);
			Projectile.Summon(26, mouseOnCast, Vector2.Zero, 1000, 5f, player);
			this.applyCast(player);
		}
		else if(this.id == 24)
		{
			updateDirection(player);
			player.playAnim(Content.psa[9], 18, 1, true, true, true, new Vector2(-128f, 128f));
			for(int i = 0;i < 25;i++)
			{
				float r = 0.1f + (MathUtils.random()/3f);
				Vector2 offset = new Vector2(0f, 384f).add(MathUtils.random(-80, 80), MathUtils.random(-50, 10)).rotate(Main.angleBetween(player.Center(), player.mousePos) + (player.direction == -1 ? 180f : 0f));
				Vector2 position = player.Center().add(offset);
				Particle.Create(position, Vector2.Zero.cpy(), 6, new Color(r, r, r, 1f), 0f, 1f, 2f);
			}
			final float damage = this.getInfoValueI(player, 0) + this.getInfoValueI(player, 1);
			Event e = new Event(30) {
				@Override
				public void function()
				{
					Main.doEarthquake(15);
					Projectile p = Projectile.Summon(56, player.Center(), mouseNor.scl(1000f), (int)damage, 8f, player);
					p.setRotationToVelocity();
				}
			};
			Main.scheduledTasks.add(e);
			this.applyCast(player);
		}
		else if(this.id == 25)
		{
			player.direction = Main.directionFromTo(player.Center().x, mouseOnCast.x);
			player.playAnim(Content.psa[2], 8, 1, true, false, true, new Vector2(-128, 0));
			this.applyCast(player);
		}
		else if(this.id == 27)
		{
			
			this.applyCast(player);
		}
		else if(this.id == 29)
		{
			Vector2 destiny = Vector2.Zero.cpy();
			updateDirection(player);
			Vector2 add = player.mousePos.sub(player.Center()).limit(400f);
			destiny = player.position.cpy().add(add);
			Vector2 origin = player.Center().cpy();
			player.velocity = new Vector2(800f, 0f).rotate(Main.angleBetween(player.Center(), destiny));
			player.setCustomDisacceleration(Main.ticksToSeconds(10), 0.985f);
			player.position.set(destiny);
			for(int i = 0;i < 15;i++)
			{
				float angle = Main.angleBetween(origin, player.Center()) + (i-7.5f + MathUtils.random(-1f, 1f))*5;
				float sin = (float)Math.sin(angle * Math.PI/180f);
				float cos = (float)Math.cos(angle * Math.PI/180f);
				float s = MathUtils.random(100, 150);
				Vector2 vel = new Vector2(cos * -s, sin * -s);
				Particle p = Particle.Create(origin.cpy().add(MathUtils.random(-10, 10), MathUtils.random(-10, 10)), vel, 2, new Color(0f, 0f, 0f, 0f), 0f, 1f, MathUtils.random()/2 + 1f);
				p.setLight(32, Color.PURPLE);
			}

			int directionX = (destiny.x > 0 ? 1 : -1);
			int directionY = (destiny.y > 0 ? 1 : -1);
			if(Constant.getPlayerMap(player.whoAmI).doesRectCollideWithMap2(player.position.x, player.position.y, player.width, player.height))
				player.position = Constant.getPlayerMap(player.whoAmI).getNextestFreeSpace(player.position.x, player.position.y, player.width, player.height, directionX, directionY).scl(64f);
			
			Projectile p = Projectile.Summon(112, origin, player.Center().sub(origin).nor().scl(100f), 0, 6f, player);
			p.ai[7] = player.Center().x;
			p.ai[8] = player.Center().y;
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 30)
		{
			Monster target = Main.getMouseTarget(1500f);
			if(target == null)
				return;
			
			Vector2 vel = target.Center().sub(player.Center()).nor().scl(3500f);
			player.velocity = vel;
			player.setCustomDisacceleration(0.45f, 1f);
			player.setInvincible(0.45f);
			player.blockAttack(0.45f);
			player.blockMovement(0.45f);
			player.addBuff(25, 0.45f, player);
			player.addBuff(26, 0.45f, player);
			target.addBuff(45, 0.45f, player);
			player.resetCollisions();
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 31)
		{
			Monster target = Main.getMouseTarget(1000f);
			if(target == null)
				return;
			
			for(int i = 0;i < 20;i++)
			{
				float fc = MathUtils.random(0.1f, 0.2f);
				Color c = new Color(fc, fc, fc, 1f);
				Particle.Create(target.randomHitBoxPosition(), Vector2.Zero, 6, c, 0f, 1f, 1f);
			}
			Projectile.Summon(32, target.Center(), Vector2.Zero.cpy(), 0, 0.5f, player);
			target.addBuff(28, 6, player);
			target.addBuff(29, 6, player);
			this.applyCast(player);
		}
		else if(this.id == 33)
		{
			player.addBuff(30, 10f, player);
			player.resetWeaponCdf();
			this.applyCast(player);
		}
		else if(this.id == 35)
		{
			if(player.haveBuff(11) == -1)
				return;
			
			player.removeBuff(11);
			for(Monster m : Constant.getMonstersInRange(player.myMapX, player.myMapY, player.Center(), 250f))
			{
				m.Stun(this.getInfoValueF(player, 0), player);
			}
			Projectile.Summon(35, player.Center().add(0f, 128f), Vector2.Zero.cpy(), 0, 2.5f, player);
			this.applyCast(player);
		}
		else if(this.id == 36)
		{
			float angle = Main.angleBetween(player.Center(), mouseOnCast);
			Vector2 pos = new Vector2(32, 0);
			pos.rotate(angle);
			Vector2 vel = pos.cpy().nor().scl(1400f);
			float damage = this.getInfoValueF(player, 0);
			Projectile.Summon(36, pos.add(player.Center()), vel, (int) damage, 8f, player);
			player.playThrowAnim();
			this.applyCast(player);
		}
		else if(this.id == 38)
		{
			updateDirection(player);
			player.playAnim(Content.psa[4], 6, 2, true, false, true, new Vector2(-4f, 0f));
			player.playAnim2(Content.blank, 6, 2, false, false, false);
			this.applyCast(player);
		}
		else if(this.id == 39)
		{
			player.addBuff(31, 10f, player);
			player.resetWeaponCdf();
			this.applyCast(player);
		}
		else if(this.id == 40)
		{
			player.addBuff(32, 10f, player);
			player.resetWeaponCdf();
			this.applyCast(player);
		}
		else if(this.id == 41)
		{
			float angle = Main.angleBetween(player.Center(), mouseOnCast);
			Vector2 pos = new Vector2(32, 0);
			pos.rotate(angle);
			Vector2 vel = pos.cpy().nor().scl(1000f);
			Projectile.Summon(39, pos.add(player.Center()), vel, 0, 8f, player);
			player.playThrowAnim();
			this.applyCast(player);
		}
		else if(this.id == 42)
		{
			float angle = Main.angleBetween(player.Center(), mouseOnCast);
			Vector2 pos = new Vector2(32, 0);
			pos.rotate(angle);
			Vector2 vel = pos.cpy().nor().scl(1000f);
			Projectile.Summon(40, pos.add(player.Center()), vel, 0, 8f, player);
			player.playThrowAnim();
			this.applyCast(player);
		}
		else if(this.id == 43)
		{
			float time = this.valueByLevel("4/5/6/7/8");
			player.addBuff(33, time, player);
			player.resetWeaponCdf();
			this.applyCast(player);
		}
		else if(this.id == 44)
		{
			for(int i = 0;i < this.channelTicks/3f;i++)
			{
				float angle = Main.angleBetween(player.Center(), this.mouseOnCast);
				Vector2 pos = new Vector2(64 + MathUtils.random(128), MathUtils.random(-24, 24)).rotate(angle);
				Vector2 vel = new Vector2(-500f, 0f).rotate(angle);
				Color color = new Color(1f, 1f, MathUtils.random()/2f, 1f);
				Particle p = Particle.Create(pos.add(player.Center()), vel, 16, color, 0f, 0.5f, 1f);
				p.drawFront = true;
				p.lights = true;
				p.lightColor = p.color;
				p.lightSize = 32;
			}
			
			player.playAnim(Content.psa[5], 1, 1, true, false, false, new Vector2(0, 0));
			player.playAnim2(Content.blank, 1, 1, false, false, false);
			updateDirection(player);
		}
		else if(this.id == 46)
		{
			Monster target = Main.getMouseTarget(1200);
			if(target == null)
				return;
			
			boolean eob = player.haveBuff(63) != -1;
			float scale = Math.max(target.width * target.scale, target.height * target.scale)/64f;
			float time = this.getInfoValueF(player, 0) + (eob ? 2 : 0);
			Projectile p = Projectile.Summon(eob ? 108 : 42, target.Center().cpy(), Vector2.Zero, 0, time+0.25f, player);
			p.setTarget(target);
			p.scale = scale;
			target.Stun(time, player);
			updateDirection(player);
			player.playMageHandAnimation(15, false);
			this.applyCast(player);
		}
		else if(this.id == 47)
		{
			Monster target = Main.getMouseTarget(1200);
			if(target == null)
				return;
			
			boolean eob = ArkaClass.hasEoB(player);
			
			Projectile p = Projectile.Summon(43, target.Center().add(eob ? -16 : 0, 200), Vector2.Zero, 0, 5f, player);
			if(MathUtils.randomBoolean() && !eob)
				p.mirrored = true;
			
			if(eob)
			{
				Projectile p3 = Projectile.Summon(43, target.Center().add(16, 200), Vector2.Zero, 0, 5f, player);
				p3.mirrored = !p.mirrored;
			}
			
			Particle p2 = Particle.Create(target.grounded ? new Vector2(target.Center().x, target.position.y) : target.Center(), Vector2.Zero, target.grounded ? 21 : 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 5.5f);
			p2.drawFront = true;
			p2.frameCounterTicks = 1;
			p2.fixAlpha = true;
			p2.alpha = 0.5f;
			p2 = Particle.Create(target.grounded ? new Vector2(target.Center().x, target.position.y) : target.Center(), Vector2.Zero, target.grounded ? 21 : 11, Color.WHITE, 0f, 1f, 5f);
			p2.drawFront = true;
			p2.frameCounterTicks = 1;
			
			Main.doEarthquake(eob ? 25 : 15);
			
			float dmg = this.getInfoValueF(player, 0) + (eob ? this.getInfoValueF(player, 1) : 0);
			target.hurtDmgVar((int) dmg, 0, 0f, false, Constant.DAMAGETYPE_ENERGY, player);
			updateDirection(player);
			player.playMageHandAnimation(15, false);
			this.applyCast(player);
		}
		else if(this.id == 48)
		{
			Vector2 posOffset = this.mouseOnCast.cpy().sub(this.posOnCast.cpy());
			float angle = Main.angleBetween(Vector2.Zero.cpy(), posOffset);
			
			Polygon shieldBox = new Polygon(new float[]{0, 0, 
					150, 0, 
					150, player.height + 24, 
					0, player.height + 24});
			shieldBox.setOrigin(0, (player.height + 24)/2);
			shieldBox.setPosition(player.Center().x, player.position.y - 12);
			shieldBox.rotate(angle);
			for(Monster m : Constant.getMonsterList(false))
			{
				if(m.sameMapAs(player))
				{
					if(Intersector.overlapConvexPolygons(shieldBox, Main.rectToPoly(m.hitBox())))
					{
						float dmg = player.getMP() * this.valueByLevel("1.5/1.625/1.75/1.875/2.0");
						m.hurtDmgVar((int) (dmg), Main.directionFromTo(player, m), 1f, player.canCritical(m), Constant.DAMAGETYPE_FIRE, player);
					}
				}
			}
			
			for(int i = 0;i < 70;i++)
			{
				Vector2 pos = new Vector2(32 + MathUtils.random(112), MathUtils.random(-player.height/2f - 12, player.height/2f + 12)).rotate(angle).add(player.Center());
				float r = MathUtils.random()/3f;
				Particle.Create(pos, new Vector2(0f, 50f), 6, new Color(r, r, r, 1f), 2f, 1f, 1f);
				Particle.Create(pos, Vector2.Zero, 11, new Color(1f, MathUtils.random()/3f + 0.5f, 0f, 1f), 0f, 1f, MathUtils.random(0.5f, 1f));
			}
			
			updateDirection(player);
			player.playThrowAnim();
			this.applyCast(player);
		}
		else if(this.id == 49)
		{
			for(int i = 0;i < this.getChannelTicks() + 10;i++)
			{
				float range = this.getChannelTicks()/4f;
				float angle = (float) (MathUtils.random() * Math.PI * 2f);
				float sin = (float) Math.sin(angle);
				float cos = (float) Math.cos(angle);
				Vector2 vel = new Vector2(-cos * range, -sin * range);
				Vector2 offset = new Vector2(64, 0).rotate(Main.angleBetween(Vector2.Zero, mouseNor));
				float f = MathUtils.random()/4f + 0.75f;
				Particle p = Particle.Create(new Vector2(cos * range, sin * range).add(player.Center()).add(offset), vel, 2, new Color(f, f, f-0.25f, 1f), 0f, 1.5f, 1f);
				p.setLight(16, p.color);
			}
			player.playMageHandAnimation(1, false);
			updateDirection(player);
		}
		else if(this.id == 50)
		{
			int count = (int) this.valueByLevel("5/5/6/6/7");
			for(int k = 0;k < 4;k++)
			{
				for(int i = 0;i < count;i++)
				{
					float damage = player.getMP() * this.valueByLevel("0.6/0.65/0.7/0.75/0.8");
					float x = this.mouseOnCast.x;
					if(Math.abs(this.mouseOnCast.x - player.Center().x) > 1000)
					{
						x = player.Center().x + 1000 * (this.mouseOnCast.x - player.Center().x > 0 ? 1 : -1);
					}
					Projectile p = Projectile.Summon(44, new Vector2(x + MathUtils.random(-200, 200), player.position.y + 1000 + k * 600), new Vector2(0f, -1700f), (int) damage, 8f, player);
					p.collisionY = mouseOnCast.y;
					for(int j = 0;j < 20;j++)
					{
						float fc = MathUtils.random()/4f + 0.75f;
						Particle.Create(p.randomHitBoxPosition(), Vector2.Zero, 13, new Color(0f, fc, fc, 1f), -0.5f, 1f, 1f);					
					}
				}
			}
			player.playMageHandAnimation(15, true);
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 51)
		{
			boolean eob = ArkaClass.hasEoB(player);
			Vector2 castpos = this.mouseOnCast.cpy().sub(player.Center());
			if(castpos.len() > 1000)
				castpos.nor().scl(1000f);

			Color[] colors = Arrays.copyOf(Constant.EXPLOSION_DEFAULT_SCHEME, 4);
			colors[3] = new Color(0.6f, 0.2f, 0.2f, 1f);
			Main.createCustomExplosion(this.mouseOnCast, 150 + (eob ? 150 : 0), 2, false, colors, new Color(1f, 0.85f, 0.1f, 1f));
			
			for(Monster m : Constant.getMonstersInRange(player.myMapX, player.myMapY, castpos.cpy().add(player.Center()), (150 + (eob ? 150 : 0))))
			{
				float damage = this.getInfoValueF(player, 0) + (eob ? this.getInfoValueF(player, 1) : 0f);
				m.Burn((int)damage, 6 + (eob ? 4 : 0), player);
			}
			
			player.playMageHandAnimation(15, true);
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 52)
		{
			for(int j = 0;j < 2;j++)
			{
				for(int i = 0;i < 72;i++)
				{
					double angle = i * 5 * Math.PI/180f;
					float sin = (float)(Math.sin(angle));
					float cos = (float)(Math.cos(angle));
					float fc = MathUtils.random()/2f;
					Color color = new Color(1f, fc, 0f, 1f);
					float dist = MathUtils.random(120, 136);
					Particle p = Particle.Create(player.Center().add(cos * dist, sin * dist), new Vector2(0f, MathUtils.random(100f, 200f)), 2, color, 2f, 1f, 1f);
					p.lights = true;
					p.lightSize = 16;
					p.lightColor = new Color(1f, 0.75f, 0f, 1f);
				}
			}
			player.addBuff(35, 8f, player);
			this.applyCast(player);
		}
		else if(this.id == 53)
		{
			boolean eob = ArkaClass.hasEoB(player);
			Vector2 castpos = this.mouseOnCast.cpy().sub(player.Center());
			if(castpos.len() > 1000)
				castpos.nor().scl(1000f);
			
			for(Projectile p : Constant.getProjectileList())
			{
				if(p.type == 45 && p.owner == player)
				{
					p.destroy();
				}
			}
			Projectile p = Projectile.Summon(45, player.Center().add(castpos), Vector2.Zero.cpy(), 0, 5f, player);
			if(eob)
			{
				p.addBuff(63, p.timeLeft, player);
			}
			player.addBuff(36, 3f, player);
			this.applyCast(player);
		}
		else if(this.id == 54)
		{
			boolean eob = ArkaClass.hasEoB(player);
			float angle = Main.angleBetween(this.posOnCast, this.mouseOnCast);
			Vector2 offset = new Vector2(96f, 0f).rotate(angle);
			float damage = this.getInfoValueF(player, 0) + (eob ? this.getInfoValueF(player, 1) : 0);
			Projectile p = Projectile.Summon(46, player.Center().add(offset), mouseNor.cpy().scl(1400f), (int) damage, 6f, player);
			if(eob)
			{
				p.addBuff(63, p.timeLeft, player);
				p.scale = 1.5f;
				p.width *= 1.5f;
				p.height *= 1.5f;
				p.extraUpdates++;
			}
			
			updateDirection(player);
			player.playMageHandAnimation(30, true);
			this.applyCast(player);
		}
		else if(this.id == 55)
		{
			Monster target = Main.getMouseTarget(1600);
			if(target == null || target.uid == this.extraInfos[0])
				return;
			
			Projectile p = Projectile.Summon(89, player.Center().add(new Vector2(40f, 0f).rotate(mouseNor.angle())), mouseNor.scl(700f), 0, 8f, player);
			p.extraUpdates = 2;
			p.setTarget(target);
			p.destroyOnTargetLoss = true;
			player.playMageHandAnimation(30, false);
			if(this.casts == 0)
				this.extraInfos[0] = target.uid;
			else
				this.extraInfos[0] = -1;
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 56)
		{
			int shieldCount = (int)(player.getMP() * this.valueByLevel("0.5/0.75/1.0/1.25/1.5"));
			player.addBuff(38, shieldCount, 4f, player);
			this.applyCast(player);
		}
		else if(this.id == 57)
		{
			float damage = player.getMP() * this.valueByLevel("1.0/1.25/1.5/1.75/2.0");

			Projectile.Summon(50, this.mouseOnCast, Vector2.Zero, (int) damage, 3f, player);
			player.playMageHandAnimation(30, true);
			this.applyCast(player);
		}
		else if(this.id == 58)
		{
			player.addBuff(39, 8f, player);
			player.resetWeaponCdf();
			this.applyCast(player);
		}
		else if(this.id == 59)
		{
			updateDirection(player);
			player.playAnim(Content.psa[8], 24, 2, false, true, true);
			player.playAnim2(Content.blank, 24, 2, false, true, true);
			final int damage = this.getInfoValueI(player, 3);
			final int hammerdmg = (int) (this.getInfoValueF(player, 0));
			final float stun = this.getInfoValueF(player, 1);
			Event ev = new Event(51) {
				@Override
				public void function()
				{
					Main.doEarthquake(30);
					for(Monster m : Constant.getMonstersInRange(player.myMapX, player.myMapY, player.Center(), 1000))
					{
						m.hurtDmgVar(hammerdmg, 0, 1f, false, Constant.DAMAGETYPE_PHYSICAL, player);
						m.Stun(stun, player);
					}
					Projectile p = Projectile.Summon(52, new Vector2(player.Center().x, player.Center().y), Vector2.Zero, 0, 3f, player);
					p.mirrored = player.direction == Constant.DIRECTION_LEFT;
					p = Projectile.Summon(53, new Vector2(player.Center().x + 30 * player.direction, player.position.y + 32), new Vector2(player.direction * 800, 0f), damage, 4f, player);
					p.mirrored = player.direction == Constant.DIRECTION_LEFT;
					p.ai[5] = player.mousePos.x;
				}
			};
			Main.scheduledTasks.add(ev);
			this.applyCast(player);
		}
		else if(this.id == 60)
		{
			player.playAnim(Content.handLiftedUp, 1, 2, false, true, false, new Vector2(-32f, 0f));
			Vector2 spawnPos = player.Center().add(0f, (float) (-110 + 110 * Math.sin(Math.PI/180f * this.getChannelTicks())));
			Projectile.Summon(57, spawnPos, new Vector2(0f, 20f), 0, Main.ticksToSeconds(2), player);
			if(player.haveBuff(43) == -1)
			{
				player.addBuff(43, 10f, player);
			}
			int count = 1 + this.getChannelTicks()/15;
			for(int i = 0;i < count;i++)
			{
				Vector2 pos = new Vector2(player.Center().x + MathUtils.random(-this.getChannelTicks()*2, this.getChannelTicks()*2), player.position.y);
				Particle.Create(pos, new Vector2(0f, MathUtils.random(200, 300)), 10, new Color(1f, 1f, 0.5f, 1f), -2f, 1f, 1f);
			}
		}
		else if(this.id == 61)
		{
			float damage = player.level + player.getPP();
			Projectile.Summon(58, player.Center(), mouseNor.scl(1400f), (int) damage, 8f, player);
			player.playThrowAnim();
			this.applyCast(player);
		}
		else if(this.id == 62)
		{
			player.playAnim(Content.handLiftedUp, 1, 2, false, true, false, new Vector2(-32f, 0f));
			int count = this.getChannelTicks()*4;
			if(this.getChannelTicks() % 2 == 0)
			{
				for(int i = 0;i < Math.min(count/2, 180);i++)
				{
					float sin = (float)Math.sin(i * 2 *Math.PI/180.0);
					float cos = (float)Math.cos(i * 2 * Math.PI/180.0);
					Vector2 pos = new Vector2(cos * 100f, sin * 100f).add(player.Center()).add(MathUtils.random(-4f, 4f), MathUtils.random(-4f, 4f));
					Vector2 vel = new Vector2(cos * -150f, sin * -150f).add(MathUtils.random(-4f, 4f), MathUtils.random(-4f, 4f));
					Particle p = Particle.Create(pos, vel, 2, new Color(0f, 0.7f, 0.7f, 1f), 0f, 1f, MathUtils.random(0.8f, 1.2f));
					p.drawBack = true;
					p.lights = true;
					p.lightSize = 24;
					p.lightColor = new Color(0.1f, 1f, 1f, 1f);
				}
			}
			if(this.getChannelTicks() > 180f)
			{
				Vector2 pos = new Vector2(MathUtils.random(85f, 115f), 0f).rotate(MathUtils.random(360f)).add(player.Center());
				Particle p = Particle.Create(pos, new Vector2(0f, 50f), 7, new Color(0f, 0.85f, 0.85f, 1f), 3f, 1f, 1f);
				p.drawFront = true;
				Lighting.Create(player.Center(), 512, new Color(0.1f, 1f, 1f, 1f), 1f, 1f, true);
			}
			if(this.getChannelTicks() == this.maxChannelTime-1)
			{
				Particle p = Particle.Create(player.Center(), Vector2.Zero, 11, new Color(0f, 1f, 1f, 1f), 0f, 1f, 6.45f);
				p.drawFront = true;
			}
		}
		else if(this.id == 63)
		{
			final Monster m = Main.getMouseTarget(1500f);
			Item i = player.inventory[Constant.ITEMSLOT_LEFT];
			if(m == null || i == null || !i.isWeapon())
				return;
			
			updateDirection(player);
			player.playAnim(Content.psa[11], 10, 2, false, true, true);
			player.playAnim2(Content.blank, 10, 2, false, false, false);
			player.resetAttackTime();
			Event e = new Event(16) {
				@Override
				public void function()
				{
					if(m == null || !m.active)
						return;
					
					m.Stun(2f, player);
					float scale = Math.max(m.width, m.height)/64f;
					Projectile p = Projectile.Summon(59, m.Center().cpy(), Vector2.Zero, 0, 8f, player);
					p.setTarget(m);
					p.scale = scale;
				}
			};
			Main.scheduledTasks.add(e);
			this.applyCast(player);
		}
		else if(this.id == 65)
		{
			final Monster m = Main.getMouseTarget(1200f);
			if(m == null)
				return;
			
			boolean crit = player.canCritical(m);
			float damage = this.getInfoValueF(player, 0);
			m.hurtDmgVar((int) damage, 1, 0.0001f, crit, Constant.DAMAGETYPE_BLOOD, player);
			updateDirection(player);
			player.playMageHandAnimation(15, true);
			int count = this.getInfoValueI(player, 2);
			for(int i = 0;i < count;i++)
			{
				Projectile p = Projectile.Summon(60, m.Center(), new Vector2(1500f, 0f).rotate(MathUtils.random(360f)), this.getInfoValueI(player, 1), 6f, player);
				p.ai[5] = (crit ? 1 : 0);
				p.setLight(crit?64:32, new Color(1f, 0.5f, 0.5f, 1f), 10);
				
			}
			this.applyCast(player);
		}
		else if(this.id == 66)
		{
			final Monster m = Main.getMouseTarget(1200f);
			if(m == null || player.getActualHealth(true) < this.getInfoValueF(player, 2)/100f)
				return;
			
			player.health -= player.maxHealth * this.getInfoValueF(player, 2)/100f;
			final float damage = (float)Math.ceil(this.getInfoValueF(player, 0));
			for(int i = 0;i < this.getInfoValueI(player, 1);i++)
			{
				Event e = new Event(i * 2) {
					@Override
					public void function()
					{
						float scl = Main.clamp(1f, player.Center().dst(m.Center())/200f, 3.5f);
						Projectile p = Projectile.Summon(61, player.Center(), new Vector2(1500f * scl, 0f).rotate(MathUtils.random(360f)), (int) damage, 8f, player);
						p.target = m;
						p.alpha = 0.15f;
					}
				};
				Main.scheduledTasks.add(e);
			}
			m.Stun(2f, player);
			updateDirection(player);
			player.playMageHandAnimation(60, true);
			Main.forceShowOff(1.25f);
			this.applyCast(player);
		}
		else if(this.id == 68)
		{
			updateDirection(player);
			final int damage = this.getInfoValueI(player, 0);
			final Skill s = this;
			float x = player.Center().x - (player.direction == -1 ? 155 : 0);
			float y = player.Center().y - 105;
			if(this.casts == 0)
			{
				this.extraInfos[0] = 0;
				player.playAnim(Content.psa[12], 5, 1, false, false, true);
				player.playAnim2(Content.blank, 5, 1, false, false, true);
				player.resetWeaponCdf();
			}
			else if(this.casts == 1)
			{
				player.playAnim(Content.psa[13], 5, 1, false, false, true);
				player.playAnim2(Content.blank, 5, 1, false, false, true);
				player.resetWeaponCdf();
			}
			else if(this.casts == 2)
			{
				player.playAnim(Content.psa[14], 5, 1, false, false, true);
				player.playAnim2(Content.psa[15], 5, 1, false, false, true);
				player.resetWeaponCdf();
			}
			ArrayList<Monster> mia = Constant.getMonstersInArea(player.myMapX, player.myMapY, x, y, 155, 210);
			for(Monster m : mia)
			{
				m.hurtDmgVar(damage, Main.directionFromTo(player, m), 1f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_SHORTSWORD, player);
			}
			if(mia.size() > 1)
			{
				s.extraInfos[0]++;
			}
			this.applyCast(player);
			final Skill s2 = this;
			if(s2.casts == 3)
			{
				s2.cdf -= s2.extraInfos[0];
			}
		}
		else if(this.id == 69)
		{
			Monster target = Main.getMouseTarget(1500f);
			if(target == null)
				return;
			
			Vector2 vel = target.Center().sub(player.Center()).nor().scl(5000f);
			player.velocity = vel;
			player.setCustomDisacceleration(0.45f, 1f);
			player.setInvincible(0.45f);
			player.blockMovement(0.45f);
			player.addBuff(68, 0.45f, player);
			player.addBuff(26, 0.45f, player);
			target.addBuff(69, 0.45f, player);
			player.resetCollisions();
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 70)
		{
			int damage = this.getInfoValueI(player, 0);
			for(Monster m : Constant.getMonstersInArea(player.myMapX, player.myMapY, player.Center().x - 200, player.Center().y-200, 400, 400))
			{
				m.velocity.y = 1500;
				m.Stun(1.5f, player);
				m.hurt(damage, 0, 0, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
			}
			Projectile pr = Projectile.Summon(62, player.Center(), Vector2.Zero, 0, 3f, player);
			pr.flipped = true;
			player.playAnim(Content.psa[16], 1, 15, true, true, true, new Vector2(16, 0));
			player.mouseOnAttack = player.Center().add(0, 0.1f);
			player.posOnAttack = player.Center();
			for(int i = -200;i <= 200;i++)
			{
				try
				{
					Vector2 pos = new Vector2(player.Center().x,player.position.y - 8).add(i + MathUtils.random(-4, 4),  MathUtils.random(-16, 16));
					GameMap g = Constant.getPlayerMap(player.whoAmI);
					Color color = g.map[Main.clamp(0, (int)Math.floor((pos.y/64f)-1), g.height-1)][Main.clamp(0, (int)Math.floor((pos.x/64f)), g.width-1)].color;
					Particle p = Particle.Create(pos, new Vector2(MathUtils.random(0, 50), MathUtils.random(100, 400)), 2, color, -3f, 3f, 1f);
					p.drawBack = true;
				}
				catch(Exception ex)
				{continue;}
			}
			Main.doEarthquake(30);
			this.applyCast(player);
		}
		else if(this.id == 72)
		{
			for(int loop = 0;loop < 72;loop++)
			{
				for(int j = 0;j < 4;j++)
				{
					final int i = loop;
					final int i2 = j * 90;
					Event e = new Event(i) {
						@Override
						public void function()
						{
							float sin = (float)Math.sin((i * 5 + i2) * Math.PI/180f);
							float cos = (float)Math.cos((i * 5 + i2) * Math.PI/180f);
							Vector2 vel = new Vector2(cos * 200, sin * 200);
							Vector2 pos = player.Center().add(new Vector2(cos * 32, sin * 32));
							Particle p = Particle.Create(pos, vel, 10, Color.WHITE, 0f, 1f, 1f);
							p.loseSpeed = true;
							p.loseSpeedMult = 0.95f;
						}
					};
					Main.scheduledTasks.add(e);
				}
			}
			player.addBuff(46, 5f, player);
			player.playAnim(Content.psa[16], 1, 15, true, false, true, new Vector2(16, 12));
			player.mouseOnAttack = player.Center().add(0, 0.1f);
			player.posOnAttack = player.Center();
			this.applyCast(player);
		}
		else if(this.id == 74)
		{
			Projectile p = Projectile.Summon(64, player.Center(), new Vector2(0f, 20f), 0, 1.5f, player);
			p.rescale(0.5f);
			p.position = player.Center().sub(p.width/2f, p.height/2f);
			player.addBuff(47, 5f, player);
			this.applyCast(player);
		}
		else if(this.id == 75)
		{
			float airtime = Main.ticksToSeconds(this.cooldownTicksAfterCast-this.ticksFromCast);
			if(this.casts <= 0 || this.casts >= this.multiCasts)
			{
				Monster target = null;
				for(Monster m : Constant.getMonstersInRange(player, 1500))
				{
					if(m.isStunned() && !m.grounded)
					{
						target = m;
						break;
					}
				}
				
				if(target == null)
					return;
				
				int quant = 0;
				Vector2 center = Vector2.Zero.cpy();
				for(Monster m : Constant.getMonstersInRange(target, 300))
				{
					if(m.isStunned() && !m.grounded)
					{
						center.add(m.Center());
						quant++;
						player.direction = Main.directionFromTo(player, m);
					}
				}
				center.x = center.x/quant;
				center.y = center.y/quant;
				if(quant == 1)
				{
					Vector2 add = new Vector2(center.x - player.Center().x, center.y - player.Center().y);
					if(add.len() > 300)
					{
						add.setLength(add.len()-150);
					}
					else if(add.len() > 150)
					{
						add.setLength(150f);
					}
					center = player.Center().add(add);
				}
				player.position = center.sub(player.getScaledWidth()/2f, player.getScaledHeight()/2f);
				Projectile.Summon(63, player.Center(), Vector2.Zero, 0, 4f, player);
				Main.forceShowOff(3.75f);
			}
			if(this.casts %3 == 0)
			{
				player.playAnim(Content.psa[12], 5, 1, false, false, false);
				player.playAnim2(Content.blank, 5, 1, false, false, false);
				player.customAnimFrame++;
				player.customAnim2Frame++;
			}
			else if(this.casts%3 == 1)
			{
				player.playAnim(Content.psa[13], 5, 1, false, false, false);
				player.playAnim2(Content.blank, 5, 1, false, false, false);
				player.customAnimFrame++;
				player.customAnim2Frame++;
			}
			else if(this.casts%3 == 2)
			{
				player.playAnim(Content.psa[14], 5, 1, false, false, false);
				player.playAnim2(Content.psa[15], 5, 1, false, false, false);
				player.customAnimFrame++;
				player.customAnim2Frame++;
			}
			boolean cancel = true;
			for(Monster m : Constant.getMonstersInRange(player, 300))
			{
				if(m.isStunned() && !m.grounded)
				{
					m.hurtDmgVar((int)this.getInfoValueF(player, 0), 0, 0f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
					m.Stun(1f, player);
					m.addBuff(7, airtime, player);
					m.velocity = Vector2.Zero.cpy();
					m.position.y+=0.1f;
					float scale = Math.max(m.width, m.height)/200f;
					Projectile p = Projectile.Summon(65, m.Center(), Vector2.Zero, 0, 1f, player);
					p.target = m;
					p.alpha = 0.5f;
					p.rescale(scale);
					cancel = false;
					Projectile p2 = Projectile.Summon(67, m.Center(), Vector2.Zero, 0, 5f, player);
					p2.rotation = MathUtils.random(360);
					p2.scale = 2f;
					Particle p3 = Particle.Create(m.Center(), Vector2.Zero, 26, Color.WHITE, 0f, 1f, 1f);
					p3.rotation = MathUtils.random(360);
					p3.scale = 4f;
				}
			}
			Main.doEarthquake(20);
			player.setInvincible(0.5f);
			player.addBuff(7, airtime, player);
			if(this.casts >= 15)
			{
				cancel = true;
			}
			if(cancel)
			{
				player.removeBuff(7);
				for(int i = this.casts;i < 16;i++)
				{
					this.applyCast(player);
				}
			}
			this.applyCast(player);
		}
		else if(this.id == 76)
		{
			Monster target = Main.getMouseTarget(500f);
			if(target == null)
				return;
			
			final ArrayList<Monster> hitList = new ArrayList<Monster>();
			hitList.add(target);
			player.setUntargetable(Main.ticksToSeconds(7));
			final int damage = this.getInfoValueI(player, 0);
			target.hurtDmgVar(damage, 0, 0, player.canCritical(target), Constant.DAMAGETYPE_PHYSICAL, player);
			Vector2 origin = player.Center().cpy();
			player.position = target.Center().sub(player.getScaledWidth()/2f, player.getScaledHeight()/2f);
			Projectile p = Projectile.Summon(67, target.Center(), Vector2.Zero, 0, 5f, player);
			p.rotation = Main.angleBetween(origin, player.Center());
			p.scale = 2;
			Particle p2 = Particle.Create(target.Center(), Vector2.Zero, 26, Color.WHITE, 0f, 1f, 1f);
			p2.rotation = MathUtils.random(-30, 30);
			p2.scale = 4f;
			player.setInvincible(0.5f);
			Main.doEarthquake(15);
			for(int i = 1;i <= this.getInfoValueI(player, 1)-1;i++)
			{
				final int iter = i;
				Event e = new Event(7*i) {
					@Override
					public void function()
					{
						float nextestDistance = 400f;
						Monster t = null;
						for(Monster m : Constant.getMonsterList(false))
						{
							if(!hitList.contains(m) && m.Center().dst(player.Center())<nextestDistance)
							{
								nextestDistance = m.Center().dst(player.Center());
								t = m;
							}
						}
						if(t == null)
						{
							ArrayList<Monster> nextMonsters = new ArrayList<Monster>();
							for(Monster m : Constant.getMonsterList(false))
							{
								if(m.Center().dst(player.Center())<nextestDistance)
								{
									nextMonsters.add(m);
								}
							}
							if(nextMonsters.size() > 0)
							{
								t = nextMonsters.get(MathUtils.random(nextMonsters.size()-1));
							}
						}
						if(t!=null)
						{
							player.setUntargetable(Main.ticksToSeconds(7));
							player.addBuff(7, Main.ticksToSeconds(8), player);
							t.hurt(damage, 0, 0, player.canCritical(t), Constant.DAMAGETYPE_PHYSICAL, player);
							Vector2 origin = player.Center().cpy();
							player.position = t.Center().sub(player.getScaledWidth()/2f, player.getScaledHeight()/2f);
							Projectile p = Projectile.Summon(67, t.Center(), Vector2.Zero, 0, 5f, player);
							p.rotation = Main.angleBetween(origin, player.Center())+(iter % 2 == 0 ? 25 : -25);
							p.flipped = (iter % 2 == 0);
							p.scale = 2;
							hitList.add(t);
							Particle p2 = Particle.Create(t.Center(), Vector2.Zero, 26, Color.WHITE, 0f, 1f, 1f);
							p2.rotation = 180+(180-p.rotation) + MathUtils.random(-10, 10);
							p2.scale = 4f;
							player.setInvincible(0.5f);
							Main.doEarthquake(15);
						}
					}
				};
				Main.scheduledTasks.add(e);
			}
			this.applyCast(player);
		}
		else if(this.id == 77)
		{
			if(this.casts == 0)
			{
				ArkaClass.doLancerAnimation(player, Content.psa[21], Content.psa[20], 6, 2, false, false, true, Vector2.Zero);
				Event e = new Event(14) {
					@Override
					public void function()
					{
						player.addBuff(49, 1f, player);
						player.velocity.x = player.maxSpeed * 2f * player.direction;
						player.setCustomDisacceleration(1f, 1f);
						player.resetCollisions();
					}
				};
				Main.scheduledTasks.add(e);
				updateDirection(player);
			}
			else
			{
				player.removeBuff(49);
				player.customDisaccelTime = 0f;
			}
			this.applyCast(player);
		}
		else if(this.id == 78)
		{
			ArkaClass.doLancerAnimation(player, Content.dartThrow, null, 6, 3, true, false, true, new Vector2(-64, 0));
			Event e = new Event(14) {
				@Override
				public void function()
				{
					Projectile.Summon(68, player.Center().add(mouseNor.cpy().scl(64f)), mouseNor.cpy().scl(1500f), 1000, 6f, player);
				}
			};
			Main.scheduledTasks.add(e);
			this.applyCast(player);
		}
		else if(this.id == 79)
		{
			if(this.casts != 2 && Main.var[8] != 5)
			{
				ArkaClass.doLancerAnimation(player, Content.psa[22], null, 10, 1, false, false, true, Vector2.Zero);
				Event e = new Event(24) {
					@Override
					public void function()
					{
						Projectile.Summon(70, player.Center().add(mouseNor.cpy().scl(64f)), mouseNor.cpy().scl(2000f), 1000, 6f, player);
					}
				};
				Main.scheduledTasks.add(e);
			}
			else
			{
				ArkaClass.doLancerAnimation(player, Content.psa[23], null, 15, 1, false, false, true, Vector2.Zero);
				Event e2 = new Event(24) {
					@Override
					public void function()
					{
						Main.doEarthquake(20);
						for(int j = 0;j < 2;j++)
						{
							for(int i = 0;i < 8;i++)
							{
								Vector2 pos = player.Center().add(-100 + i * 16, -8 + i * 4);
								if(player.direction == Constant.DIRECTION_LEFT)
								{
									pos = player.Center().add(100 - i * 16, -8 + i * 4);
								}
								if(j == 0)
								{
									Particle p = Particle.Create(pos, Vector2.Zero, 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 2.5f);
									p.drawFront = true;
									p.frameCounterTicks = 0;
									p.fixAlpha = true;
									p.alpha = 0.5f;
								}
								else
								{
									Particle p = Particle.Create(pos, Vector2.Zero, 11, Color.WHITE, 0f, 1f, 2f);
									p.drawFront = true;
									p.frameCounterTicks = 0;
								}
							}
						}
					}
				};
				Main.scheduledTasks.add(e2);

				Event e = new Event(34) {
					@Override
					public void function()
					{
						Projectile.Summon(71, player.Center().add(mouseNor.cpy().scl(64f)), mouseNor.cpy().scl(500f), 1000000, 2f, player);
					}
				};
				Main.scheduledTasks.add(e);
			}
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 80)
		{
			player.velocity.y = 1500;
			player.addBuff(50, 1f, player);
			ArkaClass.doLancerAnimation(player, Content.psa[23], null, 15, 1, false, true, true, Vector2.Zero);
			Event e2 = new Event(24) {
				@Override
				public void function()
				{
					Main.doEarthquake(5);
					for(int j = 0;j < 2;j++)
					{
						for(int i = 0;i < 8;i++)
						{
							Vector2 pos = player.Center().add(-100 + i * 16, -8 + i * 4);
							if(player.direction == Constant.DIRECTION_LEFT)
							{
								pos = player.Center().add(100 - i * 16, -8 + i * 4);
							}
							if(j == 0)
							{
								Particle p = Particle.Create(pos, Vector2.Zero, 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 2.5f);
								p.drawFront = true;
								p.frameCounterTicks = 0;
								p.fixAlpha = true;
								p.alpha = 0.5f;
							}
							else
							{
								Particle p = Particle.Create(pos, Vector2.Zero, 11, Color.WHITE, 0f, 1f, 2f);
								p.drawFront = true;
								p.frameCounterTicks = 0;
							}
						}
					}
				}
			};
			Main.scheduledTasks.add(e2);
			final Vector2 mouseOnCast = this.mouseOnCast.cpy();
			Event e = new Event(34) {
				@Override
				public void function()
				{
					Vector2 mouseNor2 = mouseOnCast.sub(player.Center());
					mouseNor2.nor();
					Projectile.Summon(72, player.Center().add(mouseNor2.cpy().scl(64f)), mouseNor2.cpy().scl(500f), 1000, 6f, player);
					player.velocity = new Vector2(-400 * player.direction, 900);
				}
			};
			Main.scheduledTasks.add(e);
			updateDirection(player);
			Main.forceShowOff(1.5f);
			this.applyCast(player);
		}
		else if(this.id == 81)
		{
			player.playAnim(Content.psa[23], 15, 1, false, false, true, Vector2.Zero);
			Event e2 = new Event(10) {
				@Override
				public void function()
				{
					Main.doEarthquake(5);
					for(int j = 0;j < 2;j++)
					{
						for(int i = 0;i < 8;i++)
						{
							Vector2 pos = player.Center().add(-100 + i * 16, -8 + i * 4);
							if(player.direction == Constant.DIRECTION_LEFT)
							{
								pos = player.Center().add(100 - i * 16, -8 + i * 4);
							}
							if(j == 0)
							{
								Particle p = Particle.Create(pos, Vector2.Zero, 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 2.5f);
								p.drawFront = true;
								p.frameCounterTicks = 0;
								p.fixAlpha = true;
								p.alpha = 0.5f;
							}
							else
							{
								Particle p = Particle.Create(pos, Vector2.Zero, 11, Color.WHITE, 0f, 1f, 2f);
								p.drawFront = true;
								p.frameCounterTicks = 0;
							}
						}
					}
				}
			};
			Main.scheduledTasks.add(e2);

			Event e = new Event(20) {
				@Override
				public void function()
				{
					Projectile p = Projectile.Summon(73, player.Center().add(mouseNor.cpy().scl(64f)), mouseNor.cpy().scl(1500f), 100, 2f, player);
					p.ai[4] = 1;
				}
			};
			Main.scheduledTasks.add(e);
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 82)
		{
			player.velocity.y = 1500;
			player.addBuff(50, 0.5f, player);
			ArkaClass.doLancerAnimation(player, Content.psa[22], null, 10, 1, false, false, true, Vector2.Zero);
			final float y = player.Center().y;
			Event e = new Event(24) {
				@Override
				public void function()
				{
					Projectile p = Projectile.Summon(69, Vector2.Zero, new Vector2(2000 * player.direction, 0f), 1000, 5f, player);
					p.position = new Vector2(player.Center().x, y).add(-4000 * player.direction - p.getScaledWidth()/2f, -p.getScaledHeight()/2f);
					p = Projectile.Summon(70, player.Center().add(64f, 0f), new Vector2(2000 * player.direction, 0f), 1000, 6f, player);
					p.collidable = false;
				}
			};
			Main.scheduledTasks.add(e);
			final Projectile s = Projectile.Summon(57, player.Center(), new Vector2(0f, 20f), 0, 5f, player);
			//s.scale = 2f;
			s.rescale(2f);
			e = new Event(30) {
				@Override
				public void function()
				{
					s.ai[5] = 1;
				}
			};
			Main.scheduledTasks.add(e);
			Main.forceShowOff(3f);
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 83)
		{
			updateDirection(player);
			ArkaClass.doLancerAnimation(player, Content.psa[21], Content.psa[20], 6, 2, false, true, true, Vector2.Zero);
			Projectile p = Projectile.Summon(74, player.Center(), Vector2.Zero, 1000, 5f, player);
			p.mirrored = player.direction == -1;
			Event e = new Event(14) {
				@Override
				public void function()
				{
					float time = 1f - Math.min(0.6f, player.getAttackSpeed()/5f);
					player.addBuff(51, time, player);
					player.velocity.x = player.maxSpeed * 7 * player.direction;
					player.setCustomDisacceleration(0.5f, 0.9f);
					player.resetCollisions();
				}
			};
			Main.scheduledTasks.add(e);
			this.applyCast(player);
		}
		else if(this.id == 84)
		{
			final Vector2 origin = new Vector2(player.Center().x, player.position.y);
			for(int i = -9;i <= 9;i++)
			{
				Vector2 destiny = new Vector2(player.Center().x, Constant.getPlayerMap(player.whoAmI).getNotFreeY((int)player.Center().x/64, (int)player.position.y/64)*64).add(i * 64, -player.height/2f);
				Vector2 pos = destiny.cpy().add(i * 32, 1500 + i * 80);
				Vector2 vel = destiny.cpy().sub(pos).nor().scl(1000f);
				Projectile p = Projectile.Summon(76, pos, vel, 0, 6f, player);
				p.extraUpdates = 5;
				try
				{
					p.ai[5] = Constant.getPlayerMap(player.whoAmI).getNotFreeY((int)destiny.x/64, (int)destiny.y/64)*64+16;
				} catch(Exception ex)
				{
					p.ai[5] = player.position.y-48;
				}
				p.drawBack = true;
			}
			Event e = new Event(15) {
				@Override
				public void function()
				{
					Projectile p = Projectile.Summon(77, new Vector2(origin.x, Constant.getPlayerMap(player.whoAmI).getNotFreeY((int)origin.x/64, (int)origin.y/64)*64), Vector2.Zero, 0, 6f, player);
					p.scale = 1.5f;
					p.position = new Vector2(origin.x, Constant.getPlayerMap(player.whoAmI).getNotFreeY((int)origin.x/64, (int)origin.y/64)*64).sub(p.width/2f, p.height/2f);
					for(Monster m : Constant.getMonstersInRange(p, 560))
					{
						m.addBuff(52, p.timeLeft, player);
					}
				}
			};
			Main.scheduledTasks.add(e);
			player.playAnim(Content.handLiftedUp, 1, 25, false, true, true);
			this.applyCast(player);
		}
		else if(this.id == 85)
		{
			ArkaClass.doLancerAnimation(player, Content.psa[24], null, 18, 1, false, false, true, Vector2.Zero);
			Event e = new Event(28) {
				@Override
				public void function()
				{
					Vector2 pos = player.Center().add(48 * player.direction, 12);
					Particle p = Particle.Create(pos, Vector2.Zero, 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 5.5f);
					p.drawFront = true;
					p.frameCounterTicks = 0;
					p.fixAlpha = true;
					p.alpha = 0.5f;
					p = Particle.Create(pos, Vector2.Zero, 11, Color.WHITE, 0f, 1f, 5f);
					p.drawFront = true;
					p.frameCounterTicks = 0;
					Main.doEarthquake(15);
				}
			};
			Main.scheduledTasks.add(e);
			e = new Event(18) {
				@Override
				public void function()
				{
					float x = player.Center().x - (player.direction == -1 ? 140 : 0);
					float y = player.Center().y-105;
					for(Monster m : Constant.getMonstersInArea(player.myMapX, player.myMapY, x, y, 140, 210))
					{
						m.hurtDmgVar(1000, player.direction, 0.25f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
						m.Stun(0.5f, player);
					}
				}
			};
			Main.scheduledTasks.add(e);
			e = new Event(20) {
				@Override
				public void function()
				{
					float x = player.Center().x - (player.direction == -1 ? 140 : 0);
					float y = player.Center().y-105;
					for(Monster m : Constant.getMonstersInArea(player.myMapX, player.myMapY, x, y, 140, 210))
					{
						m.hurtDmgVar(1000, player.direction, 0.25f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
						m.Stun(0.5f, player);
					}
				}
			};
			Main.scheduledTasks.add(e);
			e = new Event(22) {
				@Override
				public void function()
				{
					float x = player.Center().x - (player.direction == -1 ? 140 : 0);
					float y = player.Center().y-105;
					for(Monster m : Constant.getMonstersInArea(player.myMapX, player.myMapY, x, y, 140, 210))
					{
						m.hurtDmgVar(1000, player.direction, 0.25f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
						m.Stun(0.5f, player);
					}
				}
			};
			Main.scheduledTasks.add(e);
			e = new Event(28) {
				@Override
				public void function()
				{
					float x = player.Center().x - (player.direction == -1 ? 140 : 0);
					float y = player.Center().y-105;
					for(Monster m : Constant.getMonstersInArea(player.myMapX, player.myMapY, x, y, 140, 210))
					{
						m.hurtDmgVar(1000, player.direction, 0.25f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
						m.Stun(0.5f, player);
					}
				}
			};
			Main.scheduledTasks.add(e);
			e = new Event(30) {
				@Override
				public void function()
				{
					float x = player.Center().x - (player.direction == -1 ? 140 : 0);
					float y = player.Center().y-105;
					for(Monster m : Constant.getMonstersInArea(player.myMapX, player.myMapY, x, y, 140, 210))
					{
						m.hurtDmgVar(1000, player.direction, 0.25f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
						m.Stun(0.5f, player);
					}
				}
			};
			Main.scheduledTasks.add(e);
			e = new Event(32) {
				@Override
				public void function()
				{
					float x = player.Center().x - (player.direction == -1 ? 140 : 0);
					float y = player.Center().y-105;
					for(Monster m : Constant.getMonstersInArea(player.myMapX, player.myMapY, x, y, 140, 210))
					{
						m.hurtDmgVar(1000, player.direction, 0.25f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
						m.Stun(0.5f, player);
					}
				}
			};
			Main.scheduledTasks.add(e);
			e = new Event(44) {
				@Override
				public void function()
				{
					float x = player.Center().x - (player.direction == -1 ? 140 : 0);
					float y = player.Center().y-105;
					for(Monster m : Constant.getMonstersInArea(player.myMapX, player.myMapY, x, y, 140, 210))
					{
						m.hurtDmgVar(3000, player.direction, 2f, player.canCritical(m), Constant.DAMAGETYPE_ENERGY, player);
						m.Stun(2f, player);
					}
					Projectile p = Projectile.Summon(43, Vector2.Zero, Vector2.Zero, 0, 2f, player);
					p.scale = 2f;
					p.position = new Vector2(player.Center().x + 120 * player.direction - p.width/2f, player.position.y + p.height/2f);
					p = Projectile.Summon(43, Vector2.Zero, Vector2.Zero, 0, 2f, player);
					p.scale = 2f;
					p.position = new Vector2(player.Center().x + 120 * player.direction - p.width/2f, player.position.y + p.height/2f);
					p.mirrored = true;
					
					Vector2 pos =  new Vector2(player.Center().x + 120 * player.direction, player.position.y);
					Particle p2 = Particle.Create(pos, Vector2.Zero, player.grounded ? 21 : 11, new Color(0f, 0.69f, 1f, 1f), 0f, 1f, 5.5f);
					p2.drawFront = true;
					p2.frameCounterTicks = 0;
					p2.fixAlpha = true;
					p2.alpha = 0.5f;
					p2 = Particle.Create(pos, Vector2.Zero, player.grounded ? 21 : 11, Color.WHITE, 0f, 1f, 5f);
					p2.drawFront = true;
					p2.frameCounterTicks = 0;
					Main.doEarthquake(15);
				}
			};
			Main.scheduledTasks.add(e);
			player.resetWeaponCdf();
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 87)
		{
			Monster target = Main.getMouseTarget(800);
			if(target == null)
				return;
			
			updateDirection(player);
			Vector2 origin = player.Center();
			float distance = target.Center().dst(player.Center());
			Vector2 destiny = player.Center().add(target.Center().sub(player.Center()).nor().scl(distance+100));
			player.position = destiny.cpy().sub(player.width/2f, player.height/2f);
			player.fixY();
			player.playAnim(Content.psa[25], 6, 1, false, false, false);
			player.playAnim2(Content.psa[26], 6, 1, false, false, false);
			Main.doEarthquake(15);
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
			Projectile p = Projectile.Summon(43, Vector2.Zero, Vector2.Zero, 0, 2f, player);
			p.rotation = Main.angleBetween(destiny, origin)-90;
			p.position = new Vector2(p.height/2f, 0f).rotate(Main.angleBetween(destiny, origin)).add(player.Center()).sub(new Vector2(p.width/2f, p.height/2f));
			p.ai[5] = 1;
			target.Stun(1f, player);
			target.hurtDmgVar(1000, Main.directionFromTo(origin, destiny), 1f, player.canCritical(target), Constant.DAMAGETYPE_ENERGY, player);
			this.applyCast(player);
		}
		else if(this.id == 88)
		{
			player.playAnim(Content.handPointing, 1, 30, true, false, true, new Vector2(-48, 0));
			final Vector2 mouse = player.mousePos.cpy();
			final Skill skill = this;
			for(int loop = 0;loop < 5;loop++)
			{
				final int i = loop;
				Event e = new Event(6 + loop * 6) {
					@Override
					public void function()
					{
						int slot = ArkaClass.getNextCGCard(player);
						if(slot != -1)
						{
							Vector2 pos = ArkaClass.getCGCardPosition(player, slot);
							Projectile p = Projectile.Summon(78, pos, mouseNor.cpy().scl((i == 4 ? 1500f : 1000f)), skill.getInfoValueI(player, 0), 2f, player);
							p.flipped = (i % 2) == 0;
							if(i == 4)
							{
								p.ai[6] = 1;
							}
							p.ai[7] = 650f/mouse.dst(pos);
							p.ai[8] = player.randomChance(0.2f) ? 1 : 0;
							p.drawBack = ArkaClass.isCGCardBack(player, slot);
							player.isCardAvailable[slot] = false;
						}
					}
				};
				Main.scheduledTasks.add(e);
			}
			
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 89)
		{
			if(ArkaClass.getCGFreeCards(player) < 1)
				return;
			
			int count = Math.min(ArkaClass.getCGFreeCards(player), 5);
			for(int i = 0;i < count;i++)
			{
				Event e = new Event(i*4) {
					@Override
					public void function()
					{
						if(ArkaClass.getCGFreeCards(player) < 1 || ArkaClass.getNextCGCard(player) == -1)
							return;
						
						Projectile p = Projectile.Summon(86, ArkaClass.getCGCardPosition(player, ArkaClass.getNextCGCard(player)), new Vector2(0f, -3000f), 1000, 2f, player);
						p.drawBack = true;
						player.isCardAvailable[ArkaClass.getNextCGCard(player)] = false;
					}
				};
				Main.scheduledTasks.add(e);
			}
			player.playAnim(Content.handLiftedUp, 1, 25, false, true, true);
			Event e = new Event(20) {
				@Override
				public void function()
				{
					player.playAnim(Content.psa[10], 5, 2, false, true, true, new Vector2(32, 0f));
					player.forceDashPosture = 10;
					Main.doEarthquake(20);
				}
			};
			final Skill skill = this;
			Main.scheduledTasks.add(e);
			for(int loop = 0;loop < count;loop++)
			{
				final int i = loop;
				e = new Event(26 + i * 5) {
					@Override
					public void function()
					{
						boolean golden = player.randomChance(0.2f);
						Projectile p = Projectile.Summon(golden ? 81 : 80, player.Center().add(i * 400f * player.direction, -300f), new Vector2(3000f, 0f).rotate(90f - player.direction * 15), skill.getInfoValueI(player, golden ? 1 : 0), 4f, player);
						p.drawBack = true;
						p.collisionY = player.position.y + 8;
						p.rotation = 90 - MathUtils.random(10f, 30f) * player.direction;
						for(int i = -200;i <= 200;i += 2)
						{
							try
							{
								Vector2 pos = new Vector2(p.Center().x,player.position.y - 8).add(i + MathUtils.random(-4, 4),  MathUtils.random(-16, 16));
								GameMap g = Constant.getPlayerMap(player.whoAmI);
								Color color = g.map[Main.clamp(0, (int)Math.floor((pos.y/64f)-1), g.height-1)][Main.clamp(0, (int)Math.floor((pos.x/64f)), g.width-1)].color;
								Particle p2 = Particle.Create(pos, new Vector2(MathUtils.random(0, 50), MathUtils.random(100, 400)), 2, color, -3f, 3f, 1f);
								p2.drawBack = true;
							}
							catch(Exception ex)
							{continue;}
						}
					}
				};
				Main.scheduledTasks.add(e);
			}
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 90)
		{
			if(ArkaClass.getCGFreeCards(player) < 4)
				return;
			
			for(int i = 0;i < 4;i++)
			{
				Event e = new Event(i*4) {
					@Override
					public void function()
					{
						int nextCard = ArkaClass.getNextCGCard(player);
						if(nextCard >= 0)
						{
							Projectile.Summon(86, ArkaClass.getCGCardPosition(player, ArkaClass.getNextCGCard(player)), new Vector2(0f, 3000f), 10, 2f, player);
							player.isCardAvailable[ArkaClass.getNextCGCard(player)] = false;
						}
					}
				};
				Main.scheduledTasks.add(e);
			}
			
			player.playAnim(Content.handLiftedUp, 1, 25, false, true, true);
			final Skill skill = this;
			for(int loop = 1;loop <= 4;loop++)
			{
				final int i = loop;
				Event e = new Event(i*20) {
					@Override
					public void function()
					{
						boolean golden = player.randomChance(0.2f);
						Projectile p = Projectile.Summon(82, new Vector2(player.mousePos.x, player.Center().y + 1200), new Vector2(0f, -2000f), (int) (skill.getInfoValueI(player, 0) * (1+ (i-1) * (skill.getInfoValueF(player, 1)/100f))), 4f, player);
						p.rotation = 90+MathUtils.random(-30, 30);
						p.scale = 2 + i;
						p.extraUpdates = 4;
						p.collisionY = Constant.tryGetMapForEntity(player).getNotFreeY((int)player.mousePos.x/64, (int)player.mousePos.y/64)*64 + 32;
						if(i == 4)
						{
							p.scale = 16f;
							p.ai[4] = golden ? 1 : 0;
						}
						p.drawBack = true;
					}
				};
				Main.scheduledTasks.add(e);
			}
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 91)
		{
			int slot = ArkaClass.getNextCGCard(player);
			if(slot == -1)
				return;
			
			player.playAnim(Content.handPointing, 1, 30, true, false, true, new Vector2(-48, 0));

			boolean golden = player.randomChance(0.2f);
			int sin = (int) ((Constant.gameTick() + 72 * ((ArkaClass.getMaxCGCards(player)-1)-slot)) % 360);
			double sinV = Math.sin(sin * Math.PI/180);
			int cSin = Math.abs(180 - sin);
			float offsetY = ((180 - cSin) * 32)/180;
			Vector2 pos = player.Center().add((float) (sinV * 64 - 12), -32 + offsetY + 16);
			Projectile p = Projectile.Summon(85, pos, mouseNor.scl(3000f), this.getInfoValueI(player, 0), 5f, player);
			player.isCardAvailable[slot] = false;
			p.ai[8] = golden ? 1 : 0;
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 92)
		{
			player.playAnim(Content.handLiftedUp, 1, 30, false, true, true);
			Projectile p = Projectile.Summon(87, new Vector2(player.mousePos.x, player.Center().y + 1500), new Vector2(0f, -2500f), this.getInfoValueI(player, 0), 15f, player);
			p.collisionY = player.position.y+p.height;
			p.drawBack = true;
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 93)
		{
			Monster target = Main.getMouseTarget(1400);
			if(target == null)
				return;
			
			player.playMageHandAnimation(15, false);
			Vector2 pos = mouseNor.cpy().scl(40).add(player.Center());
			Color[] quadcolors = new Color[4];
			quadcolors[0] = new Color(0.07f, 0.07f, 0.07f, 1f);
			quadcolors[1] = new Color(0.06f, 0.06f, 0.06f, 1f);
			quadcolors[2] = new Color(0.05f, 0.05f, 0.05f, 1f);
			quadcolors[3] = new Color(0.04f, 0.04f, 0.04f, 1f);
			Main.createCustomExplosion(pos, 100, 1, false, quadcolors, quadcolors[0]);
			int damage = this.getInfoValueI(player, 0);
			Projectile p = Projectile.Summon(90, pos, mouseNor.scl(2000f), damage, 5f, player);
			p.setTarget(target);
			Main.doEarthquake(20);
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 94)
		{
			for(int i = 0;i < Math.min(60, this.getChannelTicks());i++)
			{
				float sin = (float)Math.sin(i * 6 * Math.PI/180f);
				float cos = (float)Math.cos(i * 6 * Math.PI/180f);
				
				Particle.Create(new Vector2(cos * 128, sin * 128).add(player.Center()).add(new Vector2(MathUtils.random(-4f, 4f), MathUtils.random(-4f, 4f))), new Vector2(cos * -200, sin * -200), 2, new Color(0.1f, 1f, 1f, 1f), 0f, 1f, 1f).setLight(32, new Color(0.1f, 1f, 1f, 1f));
			}
		}
		else if(this.id == 95)
		{
			updateDirection(player);
			for(int i = 0;i < 5;i++)
			{
				Vector2 pos = this.mouseOnCast.cpy().add(MathUtils.random(-500, 500), 1000f);
				Vector2 vel = new Vector2(1000f, 0f).rotate(Main.angleBetween(pos, this.mouseOnCast.cpy().add(MathUtils.random(-150, 150), 0)));
				Projectile p = Projectile.Summon(92, pos, vel, 1000, 3f, player);
				p.currentFrame = MathUtils.random(0,3);
				p.collisionY = Math.max(player.Center().y, this.mouseOnCast.y);
				
			}
			player.playAnim(Content.handLiftedUp, 1, 30, false, false, true);
			Particle p = Particle.Create(player.Center().add(10 * player.direction, 42), Vector2.Zero, 13, Color.WHITE, 0f, 0.5f, 1f);
			p.setCustomTexture(Content.extras[17], 1);
			p.parent = player;
			this.applyCast(player);
		}
		else if(this.id == 96)
		{
			Projectile.Summon(92, player.Center().add(new Vector2(44f, 0f).rotate(mouseNor.angle())), mouseNor.cpy().scl(1000f), 1000, 3f, player);
			player.playAnim(Content.handPointing, 1, 30, true, false, true, new Vector2(-48, 0));
			Particle p2 = Particle.Create(player.Center().add(new Vector2(48f, 0f).rotate(mouseNor.angle())), Vector2.Zero, 13, Color.WHITE, 0f, 0.5f, 1f);
			p2.setCustomTexture(Content.extras[17], 1);
			p2.parent = player;
			this.applyCast(player);
		}
		else if(this.id == 97)
		{
			for(Monster m : Constant.getMonsterList(false))
			{
				if(m.haveBuff(56) != -1)
				{
					Buff b = m.buffs[m.haveBuff(56)];
					if(b.getOrigin() == player)
					{
						m.hurtDmgVar(1000, 0, 2, player.canCritical(m), Constant.DAMAGETYPE_ENERGY, player);
						m.removeBuff(56);
						Color[] quadcolors = new Color[4];
						quadcolors[0] = new Color(0.95f, 0.975f, 1f, 1f);
						quadcolors[1] = new Color(0.3f, 0.4f, 0.8f, 1f);
						quadcolors[2] = new Color(0.2f, 0.3f, 0.7f, 1f);
						quadcolors[3] = new Color(0.15f, 0.25f, 0.6f, 1f);
						float radius = Math.max(m.width, m.height) + 50;
						Main.createCustomExplosion(m.Center(), radius, 1, false, quadcolors, quadcolors[2]);
						Main.doEarthquake(30);
					}
				}
			}
			player.playAnim(Content.handLiftedUp, 1, 30, false, false, true);
			Particle p = Particle.Create(player.Center().add(10 * player.direction, 42), Vector2.Zero, 13, Color.WHITE, 0f, 0.5f, 1f);
			p.setCustomTexture(Content.extras[17], 1);
			p.parent = player;
			this.applyCast(player);
		}
		else if(this.id == 98)
		{
			for(Monster m : Constant.getMonsterList(false))
			{
				if(m.haveBuff(56) != -1)
				{
					Buff b = m.buffs[m.haveBuff(56)];
					if(b.getOrigin() == player)
					{
						m.Stun(2, player);
						Projectile.Summon(93, m.Center(), Vector2.Zero, 0, 2f, player);
						m.removeBuff(56);
					}
				}
			}
			player.playAnim(Content.handLiftedUp, 1, 30, false, false, true);
			Particle p = Particle.Create(player.Center().add(10 * player.direction, 42), Vector2.Zero, 13, Color.WHITE, 0f, 0.5f, 1f);
			p.setCustomTexture(Content.extras[17], 1);
			p.parent = player;
			this.applyCast(player);
		}
		else if(this.id == 99)
		{
			for(int j = 0;j < 5;j++)
			{
				for(int i = 0;i < 72;i++)
				{
					float r = MathUtils.random(-2,2);
					float sin = (float)Math.sin((i*5+r)*Math.PI/180f);
					float cos = (float)Math.cos((i*5+r)*Math.PI/180f);
					float str = MathUtils.random(100, 120)*j;
					Particle p = Particle.Create(player.Center().add(new Vector2(cos, sin).scl(str)), new Vector2(-cos, -sin).scl(str*2f), 2, Color.RED, 0f, 1.5f, 1f);
					p.loseSpeed = true;
					p.loseSpeedMult = 0.95f;
					p.parent = player;
					p.setLight(32, Color.RED);
					p.rotation = MathUtils.random(360);
				}
			}
			player.addBuff(57, 6f, player);
			this.applyCast(player);
		}
		else if(this.id == 100)
		{
			player.playAnim(Content.handPointing, 1, 30, true, false, true, new Vector2(-40f, 0f));
			Projectile.Summon(94, mouseOnCast, Vector2.Zero, 1000, 30f, player);
			this.applyCast(player);
		}
		else if(this.id == 101)
		{
			for(int i = 0;i < 20;i++)
			{
				Event e = new Event(5 + i * 3) {
					@Override
					public void function()
					{
						Projectile p = Projectile.Summon(95, player.Center().add(16 * player.direction, 32), new Vector2(2000f, 0f).rotate(MathUtils.random(30, 150)), 1000, 5f, player);
						p.ticks = MathUtils.random(-30, 30);
					}
				};
				Main.scheduledTasks.add(e);
			}
			player.playAnim(Content.handLiftedUp, 1, 70, false, true, true);
			this.applyCast(player);
		}
		else if(this.id == 102)
		{
			updateDirection(player);
			if(this.channelTicks % 5 == 0)
			{
				for(int kk = 0;kk < 3;kk++)
				{
					Vector2 pos2 = player.Center().add(MathUtils.random(-128, 128), MathUtils.random(-128, 128));
					Color[] quadcolors = new Color[4];
					quadcolors[0] = new Color(0.95f, 0.975f, 1f, 1f);
					quadcolors[1] = new Color(0.3f, 0.4f, 0.8f, 1f);
					quadcolors[2] = new Color(0.2f, 0.3f, 0.7f, 1f);
					quadcolors[3] = new Color(0.15f, 0.25f, 0.6f, 1f);
					float radius = 64;
					Vector2 center = pos2.cpy();
					Lighting.Create(center, radius*2, quadcolors[1], 1f);
					int particles = (int) ((Math.pow(radius, 1.15f)) * 0.5f);
					for(int i = 1;i < particles;i++)
					{
						float extra = MathUtils.random(-8, 8);
						float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
						float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
						float distance = MathUtils.random(radius);
						Color color = new Color();
						if(distance < radius/4)
							color = quadcolors[0];
						else if(distance < radius/3f)
							color = quadcolors[1];
						else if(distance < radius/1.5f)
							color = quadcolors[2];
						else
							color = quadcolors[3];

						Vector2 pos = new Vector2(cos * distance, sin * distance).add(center);
						Vector2 vel = new Vector2(cos * 40 + MathUtils.random(-10, 10), sin * 40 + MathUtils.random(-10, 10));
						pos = new Vector2(cos * distance / 10, sin * distance/10).add(center);
						vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
						Particle p = Particle.Create(pos, vel, 24, color, 0f, 1f, 1f);
						p.setLight((int) (16*p.scale), color);
						p.loseSpeed = true;
						p.loseSpeedMult = 0.85f;
						p.drawBack = true;
					}
					for(int i = 1;i < particles;i++)
					{
						float extra = MathUtils.random(-8, 8);
						float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
						float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
						float distance = MathUtils.random(radius);
						Vector2 pos = new Vector2(cos * distance, sin * distance).add(center);
						Vector2 vel = new Vector2(cos * 70 + MathUtils.random(-25, 25), sin * 70 + MathUtils.random(-25, 25));
						pos = new Vector2(cos * distance / 10, sin * distance/10).add(center);
						vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
						Particle p = Particle.Create(pos, vel, 24, quadcolors[2], 0f, 1f, 1f);
						p.setLight((int) (16*p.scale), quadcolors[2]);
						p.loseSpeed = true;
						p.loseSpeedMult = 0.85f;
						p.drawBack = true;
					}

					Vector2 vel2 = this.mouseOnCast.cpy().sub(player.Center()).setLength(2000f).add(player.Center()).sub(pos2).nor().scl(2000f);
					Projectile p = Projectile.Summon(92, pos2, vel2, 1000, 5f, player);
					p.collisionWaitFrames = 60;
					p.collidable = true;
					p.drawBack = false;
					p.ai[5] = 1;
				}
			}
			player.playAnim(Content.handPointing, 1, 10, true, true, false, new Vector2(-48, 0f));
			Particle p2 = Particle.Create(player.Center().add(new Vector2(48f, 0f).rotate(mouseNor.angle())), Vector2.Zero, 13, Color.WHITE, 0f, Main.ticksToSeconds(2), 1f);
			p2.setCustomTexture(Content.extras[17], 1);
			p2.parent = player;
			p2.rotation = Main.gameTick * 3;
		}
		else if(this.id == 103)
		{
			for(Monster m : Constant.getMonsterList(false))
			{
				if(m.haveBuff(56) != -1)
				{
					Buff b = m.buffs[m.haveBuff(56)];
					if(b.getOrigin() == player)
					{
						for(int i = 0;i < 36;i++)
						{
							Vector2 vel = new Vector2(MathUtils.random(1000, 2500), MathUtils.random(-300, 300));
							vel.rotate(Main.angleBetween(player.Center(), m.Center()));
							Color color = new Color(MathUtils.random(), 1f, 1f, 1f);
							Particle p = Particle.Create(m.randomHitBoxPosition(), vel, 2, color, -5f, 1f, 0.5f + MathUtils.random());
							p.setLight(32, new Color(0.2f, 0.3f, 0.7f, 1f));
						}
						m.velocity = new Vector2(2000f, 0f).rotate(Main.angleBetween(m.Center(), player.Center()));
						m.Stun(1f, player);
						m.hurtDmgVar(1000, 0, 0f, player.canCritical(m), Constant.DAMAGETYPE_ENERGY, player);
						m.removeBuff(56);
					}
				}
			}
			player.playAnim(Content.handLiftedUp, 1, 30, false, false, true);
			Particle p = Particle.Create(player.Center().add(10 * player.direction, 42), Vector2.Zero, 13, Color.WHITE, 0f, 0.5f, 1f);
			p.setCustomTexture(Content.extras[17], 1);
			p.parent = player;
			this.applyCast(player);
		}
		else if(this.id == 104)
		{
			player.playAnim(Content.handPointing, 1, 30, true, true, false, new Vector2(-48, 0f));
			Particle p2 = Particle.Create(player.Center().add(new Vector2(48f, 0f).rotate(mouseNor.angle())), Vector2.Zero, 13, Color.WHITE, 0f, 0.5f, 1f);
			p2.setCustomTexture(Content.extras[17], 1);
			p2.parent = player;
			
			p2 = Particle.Create(this.mouseOnCast, Vector2.Zero, 13, Color.WHITE, 0f, 1f, 2f);
			p2.setCustomTexture(Content.extras[17], 1);
			
			Lighting.FromParticle(p2, true);
			
			Projectile.Summon(96, this.mouseOnCast, Vector2.Zero, 1000, 60f, player);
			this.applyCast(player);
		}
		else if(this.id == 107)
		{
			Projectile p = Projectile.Summon(97, this.mouseOnCast, Vector2.Zero, 0, 10f, player);
			p.ai[5] = 6;
			p.scale = 4f;
			player.playAnim(Content.handPointing, 1, 30, true, true, false, new Vector2(-48, 0f));
			Particle p2 = Particle.Create(player.Center().add(new Vector2(48f, 0f).rotate(mouseNor.angle())), Vector2.Zero, 13, Color.WHITE, 0f, 0.5f, 1f);
			p2.setCustomTexture(Content.extras[17], 1);
			p2.parent = player;
			p2 = Particle.Create(this.mouseOnCast, Vector2.Zero, 13, Color.WHITE, 0f, 1f, 4f);
			p2.setCustomTexture(Content.extras[17], 1);
			this.applyCast(player);
		}
		else if(this.id == 108)
		{
			if(player.getActualHealth(true) < this.getInfoValueF(player, 1)/100f)
				return;
			
			player.health -= player.maxHealth * this.getInfoValueF(player, 1)/100f;
			final int damage = this.getInfoValueI(player, 0);
			Event e = new Event(5) {
				@Override
				public void function()
				{
					Color[] quadColors = new Color[4];
					quadColors[0] = new Color(1f, 0.4f, 0.4f, 1f);
					quadColors[1] = new Color(1f, 0.1f, 0.1f, 1f);
					quadColors[2] = new Color(0.7f, 0.1f, 0.1f, 1f);
					quadColors[3] = new Color(0.4f, 0.02f, 0.02f, 1f);
					Main.createBackCustomExplosion(player.Center(), 300, 0.5f, false, quadColors, quadColors[1]);
					for(int i = 0;i < 300;i++)
					{
						Particle p = Particle.Create(player.randomHitBoxPosition(), new Vector2(MathUtils.random(-300, 300), MathUtils.random(-300, 300)), 2, quadColors[MathUtils.random(1, 3)], -4f, 3f, 1.5f);
						p.collisions = true;
						p.loseSpeedMult = 0.985f;
						p.drawFront = true;
						p.setLight(32, quadColors[1]);
					}
					for(Monster m : Constant.getMonstersInRange(player, 400))
					{
						m.hurtDmgVar(damage, 0, 0, player.canCritical(m), Constant.DAMAGETYPE_BLOOD, player);
					}
					Main.doEarthquake(20);
				}
			};
			Main.scheduledTasks.add(e);
			player.playAnim(Content.handLiftedUp, 1, 10, false, true, true);
			this.applyCast(player);
		}
		else if(this.id == 109)
		{
			Monster target = Main.getMouseTarget();
			if(target == null)
				return;
			
			target.addBuff(61, 4f, player);
			player.playAnim(Content.handPointing, 1, 10, true, false, true, new Vector2(-48f, 0f));
			this.applyCast(player);
		}
		else if(this.id == 111)
		{
			for(int i = 0;i < this.getChannelTicks()*4 + 10;i++)
			{
				float range = this.getChannelTicks();
				float angle = (float) (MathUtils.random() * Math.PI * 2f);
				float sin = (float) Math.sin(angle);
				float cos = (float) Math.cos(angle);
				Vector2 vel = new Vector2(-cos * range, -sin * range);
				Vector2 offset = new Vector2(64, 0).rotate(Main.angleBetween(Vector2.Zero, mouseNor));
				Particle p = Particle.Create(new Vector2(cos * range, sin * range).add(player.Center()).add(offset), vel, 2, new Color(1f, 0.1f, 0.1f ,1f), 0f, 0.5f, 1f);
				p.parent = player;
				if(i < 20)
					p.setLight(16, p.color);
			}
			for(int i = 0;i < 4;i++)
			{
				float sin = (float)Math.sin((i * 90 + this.channelTicks * 2) * Math.PI/180);
				float cos = (float)Math.cos((i * 90 + this.channelTicks * 2) * Math.PI/180);
				Vector2 pos = player.Center().add(new Vector2(64f, 0f).rotate(mouseNor.angle()));
				Vector2 vel = new Vector2(cos * (50 + this.getChannelTicks()*2), sin * (50 + this.getChannelTicks()*2));
				Particle p = Particle.Create(pos, vel, 2, new Color(1f, 0.1f, 0.1f, 1f), 0f, 1f, 1f);
				p.setLight(32, p.color);
				p.parent = player;
			}
			player.playAnim(Content.handPointing, 1, 5, true, false, false, new Vector2(-48f, 0f));
			updateDirection(player);
		}
		else if(this.id == 112)
		{
			float sy = Math.max(player.position.y+100, this.mouseOnCast.y);
			for(int i = -3;i <= 3;i++)
			{
				Projectile p = Projectile.Summon(28, mouseOnCast.cpy().add(i * 150, 1500 + Math.abs(i) * 250), new Vector2(0f, -1000f), this.getInfoValueI(player, 0), 3.5f, player);
				p.ai[6] = sy;
			}
			player.playAnim(Content.handLiftedUp, 1, 30, false, false, true);
			Main.forceShowOff(2f);
			this.applyCast(player);
		}
		else if(this.id == 113)
		{
			Projectile p = Projectile.Summon(99, player.Center(), Vector2.Zero, 0, 8f, player);
			p.scale = 2f;
			player.addBuff(62, 8f, player);
			this.applyCast(player);
		}
		else if(this.id == 114)
		{
			float perc = ((float)this.getChannelTicks()/this.maxChannelTime);
			float scale = (float)(Math.sin(perc * 90f * Math.PI/180f) * 600f);
			Particle p2 = Particle.Create(player.Center().add(0f, 350f), Vector2.Zero, 29, new Color(0.5f, 0f, 0.5f, 1f), 0f, 0.05f, scale/300f);
			p2.rotation = this.channelTicks/2f;
			p2.setLight((int)scale, p2.color);
			Lighting.Create(p2.Center(), scale*2, p2.color, 0.1f);
			
			for(int i = 0;i < 2 + perc * 5;i++)
			{
				float angle = MathUtils.random(360f);
				float sin = (float)Math.sin(angle * Math.PI/180f);
				float cos = (float)Math.cos(angle * Math.PI/180f);
				float range = MathUtils.random(-500f, -200f) + (perc * -500);
				Vector2 pos = player.Center().add(0f, 350f).add(range * cos, range * sin);
				Vector2 vel = new Vector2(cos, sin).scl(100f + (perc * 200));
				for(int x = 0;x < 2;x++)
				{
					Particle p = Particle.Create(pos, vel, 2, new Color(0.5f, 0.1f, 0.5f, 1f), 0f, 0.5f, 1f);
					p.rotation = Main.angleBetween(p.position, p2.position);
					p.width = 16;
					p.height = 4;
					p.setLight(32, new Color(0.5f, 0.1f, 0.5f, 1f));
					p.loseSpeedMult = 1.1f;
					p.loseSpeed = true;
				}
			}
			
			player.playAnim(Content.handLiftedUp, 1, 10, false, true, false);
			Main.forceShowOff(1f);
		}
		else if(this.id == 115)
		{
			Event e = new Event(45) {
				@Override
				public void function()
				{
					Main.doEarthquake(30);
					player.addBuff(65, 20f, player);
					for(int i = 0;i < 300;i++)
					{
						Particle p = Particle.Create(new Vector2(player.Center().x, player.position.y).add(MathUtils.random(-100, 100), 0f), new Vector2(0f, MathUtils.random(0f, 700f)), 2, Color.WHITE, 0f, 2f, 1f);
						p.setLight(32, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ENERGY]);
					}
					for(int i = 0;i < 300;i++)
					{
						Particle p = Particle.Create(player.randomHitBoxPosition(), new Vector2(MathUtils.random(-300, 300), MathUtils.random(-300, 300)), 2, Color.WHITE, -4f, 2.5f, 1.5f);
						p.collisions = true;
						p.loseSpeedMult = 0.985f;
						p.drawFront = true;
						p.setLight(32, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ENERGY]);
					}
				}
			};
			Main.scheduledTasks.add(e);
			player.playAnim(Content.handLiftedUp, 1, 75, false, true, true, new Vector2(-16, 0));
			this.applyCast(player);
		}
		else if(this.id == 116)
		{
			boolean eob = ArkaClass.hasEoB(player);
			int stacks = this.getInfoValueI(player, 0);
			if(eob)
			{
				stacks *= 2;
				player.heal(stacks);
			}
			player.addBuff(66, stacks, 3f, player);
			Particle p = Particle.Create(player.Center(), Vector2.Zero, 22, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_GOOD], 0f, 3.5f, 1f);
			p.parent = player;
			if(eob)
			{
				p = Particle.Create(player.Center(), Vector2.Zero, 22, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_GOOD], 0f, 3.5f, 2f);
				p.parent = player;
			}
			Lighting l = Lighting.Create(player.Center(), 512*p.scale, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_GOOD], 3.5f, 2f);
			l.parent = player;
			l.important=true;
			this.applyCast(player);
		}
		else if(this.id == 117)
		{
			float angle = mouseNor.angle();
			float sin = (float)Math.sin(angle * Math.PI/180f);
			float cos = (float)Math.cos(angle * Math.PI/180f);
			float range = 48f;
			Vector2 pos = new Vector2(cos * range, sin * range).add(player.Center());
			Projectile.Summon(109, pos, mouseNor.cpy().scl(400f), this.getInfoValueI(player, 0), 5f, player);
			player.playAnim(Content.handPointing, 1, 15, true, false, false, new Vector2(-48f, 0f));
			Color color = Color.WHITE.cpy().fromHsv(Constant.gameTick()%360, 1f, 1f);
			Main.createCustomExplosion(pos, 20, 1, true, new Color[] {Color.WHITE, color, color, color}, color);
			this.applyCast(player);
		}
		else if(this.id == 118)
		{
			float angle = mouseNor.angle();
			float sin = (float)Math.sin(angle * Math.PI/180f);
			float cos = (float)Math.cos(angle * Math.PI/180f);
			float range = 64f;
			Vector2 pos = new Vector2(cos * range, sin * range).add(player.Center());
			Particle p = Particle.Create(pos, Vector2.Zero, 2, Color.WHITE, 0f, Main.ticksToSeconds(2), 1f);
			p.setCustomTexture(Content.projectiles[109], 1);
			Color color = Color.WHITE.cpy().fromHsv(Constant.gameTick()%360, 1f, 1f);
			p.setLight(64, color);
			p.rotation = angle+this.channelTicks;
			if(this.channelTicks%4==0)
			{
				Vector2 mypos = pos.cpy().add(MathUtils.random(-16, 16), MathUtils.random(-16, 16));
				p = Particle.Create(mypos, Vector2.Zero, 32, Color.WHITE, 0f, 1f, 2f);
				p.rotation = MathUtils.random(360f);
				p.setLight(32, color);
			}
			else if(this.channelTicks%4==2)
			{
				Vector2 mypos2 = pos.cpy().add(MathUtils.random(-16, 16), MathUtils.random(-16, 16));
				p = Particle.Create(mypos2, mypos2.cpy().sub(pos).setLength(64f), 32, Color.WHITE, 0f, 0.5f, 2f);
				p.rotation = MathUtils.random(360f);
				//p.setCustomTexture(Content.particles[31], 1);
				p.setLight(32, color);
			}
			player.playAnim(Content.handPointing, 1, 10, true, false, false, new Vector2(-48f, 0f));
		}
		else if(this.id == 119)
		{
			final float angle2 = mouseNor.angle();
			final Skill skill = this;
			int type = MathUtils.random(0, 1);
			int loop = 0;
			for(int i = (type == 0 ? -30 : 30);(type == 0 && i <= 30) || (type == 1 && i >= -30);i+=(type == 0 ? 3 : -3))
			{
				final int x = i;
				Event e = new Event((loop)) {
					@Override
					public void function()
					{
						float angle = angle2+x;
						float sin = (float)Math.sin(angle * Math.PI/180f);
						float cos = (float)Math.cos(angle * Math.PI/180f);
						float range = 48f;
						Vector2 pos = new Vector2(cos * range, sin * range).add(player.Center());
						Projectile.Summon(111, pos, new Vector2(cos, sin).cpy().scl(300f), skill.getInfoValueI(player, 0), 5f, player);
						Color color = Color.WHITE.cpy().fromHsv(Constant.gameTick()%360, 1f, 1f);
						Main.createCustomExplosion(pos, 20, 0.1f, true, new Color[] {Color.WHITE, color, color, color}, color);
					}
				};
				Main.scheduledTasks.add(e);
				loop++;
			}
			player.playAnim(Content.handPointing, 1, 15, true, false, false, new Vector2(-48f, 0f));
			player.addBuff(7, Main.ticksToSeconds(loop+2), player);
			player.forceDashPosture = loop;
			updateDirection(player);
			this.applyCast(player);
		}
		else if(this.id == 120)
		{
			updateDirection(player);
			player.playAnim(Content.psa[30], 10, 1, false, false, true);
			player.playAnim2(Content.blank, 10, 1, false, false, true);
			Projectile p = Projectile.Summon(114, player.Center().add(0f, 64f), Vector2.Zero, this.getInfoValueI(player, 0), 3f, player);
			p.mirrored = player.direction == -1;
			this.applyCast(player);
		}
		else if(this.id == 123)
		{
			player.playAnim(Content.psa[31], 40, 4, false, true, true);
			player.playAnim2(Content.blank, 40, 4, false, true, true);
			player.forceDashPosture = 160;
			player.timeSinceLastAttack = 0f;
			Main.forceShowOff(Main.ticksToSeconds(200));
			
			final Skill skill = this;
			int j = -1;
			for(int i = 0;i < 50;i++)
			{
				j = (j == -1 ? 1 : -1);
				final int jota = j;
				Event e = new Event(5+i*3) {
					@Override
					public void function()
					{
						Vector2 pos = player.Center().add(MathUtils.random(50, 256) * jota, MathUtils.random(0, 128));
						Vector2 vel = new Vector2(MathUtils.random(-500, 500), MathUtils.random(-200, 200));
						Projectile p = Projectile.Summon(115, pos, vel, skill.getInfoValueI(player, 0), 5f, player);
						p.drawBack = MathUtils.randomBoolean();
						p.rotation = vel.angle();
						p.flipped = vel.x < 0;
						//p.extraUpdates = 1;
					}
				};
				Main.scheduledTasks.add(e);
			}
			
			this.applyCast(player);
		}
		else if(this.id == 124)
		{
			player.playAnim(Content.psa[32], 38, 2, true, true, true, new Vector2(-128f, 0f));
			player.playAnim2(Content.blank, 38, 2, true, true, true);
			updateDirection(player);
			final Skill skill = this;
			for(int i = 0;i < 28;i++)
			{
				final int loop = i;
				Event e = new Event(24+i*2) {
					@Override
					public void function()
					{
						if(loop == 0)
						{
							Vector2 pos = player.Center().add(mouseNor.cpy().scl(36f));
							Particle.Create(pos, Vector2.Zero, 11, new Color(0.92f, 0.97f, 0.8f, 1f), 0f, 1f, 4f).frameCounterTicks = 0;
							Main.doEarthquake(15);
						}
						for(int j = 0;j < 2;j++)
						{
							Projectile p = Projectile.Summon(116, player.Center().add(MathUtils.random(-64f, 64f), MathUtils.random(-64f, 64f)), mouseNor.cpy().scl(2500f), skill.getInfoValueI(player, 0), 5f, player);
							p.collisionWaitFrames = 80;
							for(int k = 0;k < 15;k++)
							{
								Particle p2 = Particle.Create(p.randomHitBoxPosition(), new Vector2(MathUtils.random(-128f, 128f), MathUtils.random(-128f, 128f)), 2, new Color(), 0f, 0.5f, 1f);
								p2.setLight(32, p.lightColor);
							}
						}
					}
				};
				Main.scheduledTasks.add(e);
			}
			this.applyCast(player);
		}
		else if(this.id == 125)
		{
			updateDirection(player);
			player.playAnim(Content.psa[33], 30, 1, false, true, true);
			Vector2 ghostPos = player.Center().add(player.direction * 425, 0f);
			final Projectile ghost = Projectile.Summon(118, ghostPos, Vector2.Zero, this.getInfoValueI(player, 1), 10f, player);
			ghost.mirrored = player.direction == 1;
			final Skill skill = this;
			Event e1 = new Event(14) {
				@Override
				public void function()
				{
					Vector2 pos = player.Center().add(mouseNor.cpy().scl(64f));
					Projectile p = Projectile.Summon(117, pos, mouseNor.cpy().scl(2500f), skill.getInfoValueI(player, 0), 8f, player);
					p.target = ghost;
					ghost.target = p;
				}
			};
			Main.scheduledTasks.add(e1);
			player.timeSinceLastAttack = 0f;
			this.applyCast(player);
		}
		else if(this.id == 126)
		{
			updateDirection(player);
			player.playAnim(Content.psa[34], 24, 1, true, false, true, new Vector2(-128f, 0f));
			player.playAnim2(Content.psa[35], 24, 1, true, false, true, new Vector2(-128f, 0f));
			final Skill skill = this;
			Event e1 = new Event(18) {
				@Override
				public void function()
				{
					updateDirection(player);
					float angle = player.mousePos.cpy().sub(player.Center()).angle();
					player.customAnimRotation = angle;
					Vector2 pos = new Vector2(64f, 0f).setAngle(angle).add(player.Center());
					Vector2 pos2 = new Vector2(32f, 0f).setAngle(angle).add(player.Center());
					Projectile.Summon(124, pos, new Vector2(500f, 0f).setAngle(angle), skill.getInfoValueI(player, 0), 1f, player);
					for(int i = 0;i < 30;i++)
					{
						Vector2 vel = new Vector2(MathUtils.random(200, 1000), 0f).setAngle(angle).rotate(MathUtils.random(-35,35));
						Particle p = Particle.Create(pos2, vel, 10, new Color(0.92f,0.96f,0.6f,1f), 0f, 1f, 1f).setLight(32, new Color(0.92f, 0.96f, 0.6f, 1f));
						p.loseSpeed = true;
						p.loseSpeedMult = 0.9f;
					}
				}
			};
			Event e2 = new Event(24) {
				@Override
				public void function()
				{
					updateDirection(player);
					float angle = player.mousePos.cpy().sub(player.Center()).angle();
					player.customAnim2Rotation = angle;
					Vector2 pos = new Vector2(64f, 0f).setAngle(angle).add(player.Center()).add(new Vector2(8f, 8f));
					Vector2 pos2 = new Vector2(32f, 0f).setAngle(angle).add(player.Center()).add(new Vector2(8f, 8f));
					Projectile.Summon(124, pos, new Vector2(500f, 0f).setAngle(angle), skill.getInfoValueI(player, 0), 1f, player);
					for(int i = 0;i < 30;i++)
					{
						Vector2 vel = new Vector2(MathUtils.random(200, 1000), 0f).setAngle(angle).rotate(MathUtils.random(-35,35));
						Particle p = Particle.Create(pos2, vel, 10, new Color(0.92f,0.96f,0.6f,1f), 0f, 1f, 1f).setLight(32, new Color(0.92f, 0.96f, 0.6f, 1f));
						p.loseSpeed = true;
						p.loseSpeedMult = 0.9f;
					}
				}
			};
			Event e3 = new Event(36) {
				@Override
				public void function()
				{
					updateDirection(player);
					float angle = player.mousePos.cpy().sub(player.Center()).angle();
					player.customAnimRotation = angle;
					player.customAnim2Rotation = angle;
					Vector2 pos = new Vector2(64f, 0f).setAngle(angle).add(player.Center());
					Vector2 pos2 = new Vector2(32f, 0f).setAngle(angle).add(player.Center());
					Projectile.Summon(124, pos, new Vector2(750f, 0f).setAngle(angle), skill.getInfoValueI(player, 1), 1f, player).scale = 2f;
					for(int i = 0;i < 30;i++)
					{
						Vector2 vel = new Vector2(MathUtils.random(200, 1500), 0f).setAngle(angle).rotate(MathUtils.random(-45,45));
						Particle p = Particle.Create(pos2, vel, 10, new Color(0.92f,0.96f,0.6f,1f), 0f, 1f, 1f).setLight(32, new Color(0.92f, 0.96f, 0.6f, 1f));
						p.loseSpeed = true;
						p.loseSpeedMult = 0.9f;
					}
				}
			};
			Event ef = new Event(42) {
				@Override
				public void function()
				{
					float angle = mouseNor.angle() + 135 * player.direction;
					Vector2 pos = new Vector2(64f, 0f).setAngle(angle).add(player.Center()).add(0f,-32f);
					Vector2 vel = new Vector2(MathUtils.random(800f, 1300f), 0f).setAngle(angle);
					Vector2 vel2 = new Vector2(MathUtils.random(800f, 1300f), 0f).setAngle(angle);
					Projectile.Summon(123, pos, vel, 0, 1f, player);
					Projectile.Summon(123, pos.cpy().add(16f, 4f), vel2, 0, 1f, player).drawBack = true;
				}
			};
			Main.scheduledTasks.add(ef);
			Main.scheduledTasks.add(e1);
			Main.scheduledTasks.add(e2);
			Main.scheduledTasks.add(e3);
			this.applyCast(player);
		}
		else if(this.id == 127)
		{
			updateDirection(player);
			player.playThrowAnim(false);
			Projectile.Summon(125, player.Center().add(mouseNor.cpy().scl(64f)), mouseNor.cpy().scl(1250f), this.getInfoValueI(player, 0), 5f, player);
			this.applyCast(player);
		}
		else if(this.id == 128)
		{
			player.playAnim(Content.handLiftedUp, 1, 30, false, false, false);
			player.addBuff(76, 8f, player);
			for(int i = 0;i < 36;i++)
			{
				Vector2 pos = player.Center().add(Main.var[6] * player.direction, Main.var[7]);
				Vector2 vel = new Vector2(400f, 0f).setAngle(i * 10);
				Particle.Create(pos, vel, 16, new Color(0.92f, 0.96f, 0.8f, 1f), 0f, 0.5f, 1f).setLight(32, new Color(0.92f, 0.96f, 0.8f, 1f));
			}
			this.applyCast(player);
		}

		if(this.castAnim != null)
		{
			player.playAnim(Content.psa[this.castAnim.customAnim], this.castAnim.customAnimFrames, this.castAnim.customAnimSkipper, 
					this.castAnim.customAnimShouldRotate, this.castAnim.customAnimBlockMove, this.castAnim.customAnimBlockAttack);
		}
		if(this.offensive)
		{
			player.removeBuff(11);
		}
	}
	
	public void onCastTick(Player player)
	{
		Vector2 mouseNor = this.mouseOnCast.cpy().sub(this.posOnCast.cpy());
		mouseNor.nor();
		if(this.id == 3)
		{
			mouseNor.scl(1100f);
			float multiplier = 5f + player.level * 0.1f;
			Projectile.Summon(7, player.Center(), mouseNor, (int)(player.inventory[Constant.ITEMSLOT_LEFT].damage * multiplier), 6f, player);
		}
		else if(this.id == 4)
		{
			mouseNor.scl(1100f);
			Projectile.Summon(10, player.Center(), mouseNor, 100, 6f, player);
		}
		/*else if(this.id == 8)
		{
			mouseNor.scl(50f);
			Vector2 pos = new Vector2(player.Center().x + mouseNor.x, player.Center().y + mouseNor.y);
			Projectile p = Projectile.Summon(12, pos, Vector2.Zero.cpy(), 0, 5f, player);
			p.rotation = (float)Math.toDegrees((Math.atan2(mouseNor.y, mouseNor.x)));
		}
		else if(this.id == 12)
		{
			Vector2 posOffset = this.mouseOnCast.cpy().sub(this.posOnCast.cpy());
			float angle = Main.angleBetween(Vector2.Zero.cpy(), posOffset);
			
			float dmg = this.valueByLevel(10, 20, 40, 70, 150) + player.maxHealth * this.valueByLevel(0.1f, 0.125f, 0.15f, 0.175f, 0.2f);
			boolean hitSomeone = false;
			Polygon shieldBox = new Polygon(new float[]{0, 0, 
					80, 0, 
					80, player.height + 24, 
					0, player.height + 24});
			shieldBox.setOrigin(0, (player.height + 24)/2);
			shieldBox.setPosition(player.Center().x, player.position.y - 12);
			shieldBox.rotate(angle);
			for(Monster m : Constant.getMonsterList())
			{
				if(m.sameMapAs(player))
				{
					if(Intersector.overlapConvexPolygons(shieldBox, Main.rectToPoly(m.hitBox())))
					{
						m.hurtDmgVar((int) (dmg), Main.directionFromTo(player, m), 1f, player.canCritical(m), Constant.DAMAGETYPE_PHYSICAL, player);
						float time = this.valueByLevel(1, 1, 1, 1, 2);
						m.Stun(time, player);
						hitSomeone = true;
					}
				}
			}
			if(hitSomeone)
			{
				for(int i = 0;i < 45;i++)
				{
					Vector2 pos = new Vector2(32, 0).rotate(angle).add(player.Center());
					Vector2 vel = new Vector2(MathUtils.random(100, 300), MathUtils.random(-100, 100));
					vel.rotate(angle);
					Particle.Create(pos, vel, 6, Color.GRAY, 0f, 1f, MathUtils.random(0.5f, 1f));
				}
			}
		}*/
		else if(this.id == 22)
		{
			int damage = MathUtils.random(this.getInfoValueI(player, 0), this.getInfoValueI(player, 1));
			Projectile p = Projectile.Summon(25, player.Center(), Vector2.Zero.cpy(), damage, 1f, player);
			if(player.direction == Constant.DIRECTION_LEFT)
			{
				p.mirrored = true;
			}
		}
		else if(this.id == 25)
		{
			float angle = mouseNor.angle();
			float dmg = this.getInfoValueI(player, 0);
			Projectile.Summon(30, player.Center().add(new Vector2(64f, 0f).rotate(angle)), new Vector2(6000, 0f).rotate(angle), (int) (dmg), 1f, player);
		}
		else if(this.id == 38)
		{
			float angle = Main.angleBetween(Vector2.Zero.cpy(), mouseNor);
			float damage = player.getPP() * this.valueByLevel("1/1.125/1.25/1.375/1.5");
			Vector2 pos = new Vector2(32, 0).rotate(angle).add(player.Center());
			Projectile.Summon(38, pos, mouseNor.cpy().scl(2000f), (int)damage, 5f, player);
		}
	}

	public void applyCast(Player player)
	{
		this.frame = 0;
		player.skillCasting = true;
		if(this.castTime != 1337)
			player.skillTicksLeft = this.castTime+2;
		else
			player.skillTicksLeft = 15;
		
        player.ticksFromLastSkill = 0;
        player.skillOnCast = this.id;
        this.ticksFromCast2 = 0;
        this.channelTicks = 0;
        this.channeling = false;
		if(!this.alreadyCast)
		{
			this.ticksFromCast = -1;
			this.alreadyCast = true;
		}
		this.casts++;
		if(this.casts < this.multiCasts && this.waitForBuff < 0)
		{
			this.setCooldown(Main.ticksToSeconds(this.multiCastDelay));
		}
		if(this.casts == 1)
		{
			player.mana -= this.getMana(player);
			this.totalCasts++;
			if(player.arcaneCircle > 0 && player.arcaneCircle < 4)
			{
				player.arcaneCircle++;
			}
			Skill s = null;
			if((s = player.getSkill(45)) != null)
			{
				if(player.haveBuff(63) == -1)
				{
					Buff b = player.buffs[player.addBuff(64, 1, s.getInfoValueI(player, 0), 5f, player)];
					if(b.stacks >= s.getInfoValueI(player, 0)-1)
					{
						player.removeBuff(64);
						player.addBuff(63, 5f, player);
					}
				}
				else if(!this.mageUltimate)
				{
					player.removeBuff(63);
				}
			}
		}
		if(this.mageUltimate)
		{
			player.arcaneCircle = 0;
		}
		this.level = Skill.levelFromCasts(this.totalCasts);
	}
	
	public static int levelFromCasts(int casts)
	{
		int level = 1;
		if(casts >= 500)
			level = 5;
		else if(casts >= 250)
			level = 4;
		else if(casts >= 100)
			level = 3;
		else if(casts >= 40)
			level = 2;
		
		return level;
	}
	
	public void setCooldown(float time)
	{
		this.cdf = time;
		this.lastCdf = (time != 0 ? time : 1f);
	}
	
	public void update(float delta, Player player)
	{
		this.cdf -= delta*(this.casts == 0 ? player.cooldownAcceleration : 1);

		this.ticksFromCast++;
		this.ticksFromCast2++;
		if(this.passive || this.classPassive)
		{
			this.mouseOnCast = player.mousePos.cpy();
			this.posOnCast = player.Center().cpy();
		}
		if(ticksFromCast == this.cooldownTicksAfterCast || this.casts >= this.multiCasts)
		{
			if(this.waitForBuff < 0)
				this.setCooldown(this.getCooldown(player));
			this.alreadyCast = false;
			this.casts = 0;
			this.ticksFromCast = 9999;
		}
		if(player.skillCasting && player.skillOnCast == this.id)
		{
			if((this.frame == this.castTime && this.castTime != 1337) || this.castTime <= 1 || (this.frame <= this.castTime && this.extraCastDetections(player)))
			{
				this.onCastTick(player);
			}
		}

		if(this.id == 10)
		{
			player.addBuff(71, 0.1f, player);
		}
		else if(this.id == 64)
		{
			player.extraMPperc += player.getActualHealth(true)*this.getInfoValueF(player, 0);
			player.healthRegen += player.getLostHealth(true)*this.getInfoValueF(player, 1);
		}
		else if(this.id == 71 && this.cdf > 0f)
		{
			float extraPP = player.getLostHealth(true) * this.getInfoValueF(player, 0);
			player.extraPPperc += extraPP / 100f;
		}
		this.frame++;
	}
	
	public void updateStoredInfos(Player player, int fakeLevel)
	{
		int originalLevel = this.level;
		this.level = fakeLevel;
		for(int i = 0;i < 10;i++)
		{
			this.storedInfos[i] = this.getInfoValueF(player, i);
		}
		this.level = originalLevel;
	}

	private boolean extraCastDetections(Player player)
	{
		/*if(this.id == 8)
		{
			if(player.customAnim2Frame == player.inventory[Constant.ITEMSLOT_RIGHT].hurtFrame - 1)
			{
				return true;
			}
		}*/
		
		return false;
	}

	public void ResetStats()
	{
		for(int i = 0;i < 10;i++)
		{
			this.infos[i] = new MathEx();
			this.storedInfos[i] = 0f;
		}
		this.name = "Unknown";
		this.passive = false;
		this.castTime = 0;
		this.attackRestricted = false;
		this.channelTicks = 0;
		this.multiCasts = 1;
		this.multiCastDelay = 15;
		this.cooldownTicksAfterCast = 0;
		this.ticksFromCast = 9999;
		this.debugging = false;
		this.offensive = false;
		this.element = Constant.DAMAGETYPE_PHYSICAL;
		for(int i = 0;i < 5;i++)
		{
			this.baseDescription[i] = "";
			this.cooldown[i] = -1f;
			this.manaCost[i] = -1;
		}
		this.channel = false;
		this.maxChannelTime = 0;
		this.waitForBuff = -1;
		this.maxChannelHoldTime = 0;
	}
	
	public String getDescription(Player player)
	{
		return this.getDescription(this.level, player);
	}
	
	public float getCooldown(Player player)
	{
		return this.getCooldown(this.level, player) * player.cooldownReduction;
	}
	
	/*public String getEffects()
	{
		return this.getEffects(this.level);
	}*/
	
	public String getDescription(int level, Player player)
	{
		String currentString = "Unknown use";
		int originalLevel = this.level;
		this.level = level;
		this.updateDescription(player, level);
		this.level = originalLevel;
		if(this.description.length() > 0)
		{
			currentString = this.description;
		}
		return currentString;
	}
	
	public String getBaseDescription(int level)
	{
		String currentString = "";
		for(int i = level-1;i >= 0;i--)
		{
			if(this.baseDescription[i].length() > 0)
			{
				currentString = this.baseDescription[i];
				break;
			}
		}
		return currentString;
	}
	
	/*public String getEffects(int level)
	{
		String currentString = "";
		for(int i = level-1;i >= 0;i--)
		{
			if(this.effects[i].length() > 0)
			{
				currentString = this.effects[i];
				break;
			}
		}
		return currentString;
	}*/
	
	public float getCooldown(int level, Player player)
	{
		float cd = 0f;
		for(int i = level-1;i >= 0;i--)
		{
			if(this.cooldown[i] != -1f)
			{
				cd = this.cooldown[i];
				break;
			}
		}
		
		if(this.id == 11 && Set.playerItemSetCount(player, Set.LEAF) >= 2)
		{
			cd *= 0.7f;
		}
		
		return cd;
	}

	public int getMana(int level, Player player)
	{
		int cd = 0;
		for(int i = level-1;i >= 0;i--)
		{
			if(this.manaCost[i] != -1f)
			{
				cd = this.manaCost[i];
				break;
			}
		}
		
		return (int)(cd * player.manaCostMult);
	}
	
	public int getMana(Player player)
	{
		return this.getMana(this.level, player);
	}
	
	public Texture getTexture()
	{
		return Content.skills[this.id-1];
	}

	public static void updateDirection(Player player)
	{
		if(player.mousePos.cpy().x < player.Center().x)
		{
			player.direction = -1;
		}
		else
		{
			player.direction = 1;
		}
	}

	public float getInfoValueF(Player player, int infoNum)
	{
		float pp = player.getPP();
		float mp = player.getMP();
		float hp = player.maxHealth;
		String string = this.infos[infoNum].expression
				.replaceAll("lvl", String.valueOf(this.level-1))
				.replaceAll("pp", String.valueOf(pp))
				.replaceAll("mp", String.valueOf(mp))
				.replaceAll("hp", String.valueOf(hp));
		
		if(string.contains("#0"))
			string = string.replaceAll("#0", String.valueOf(this.getInfoValueF(player, 0)));
		if(string.contains("#1"))
			string = string.replaceAll("#1", String.valueOf(this.getInfoValueF(player, 1)));
		if(string.contains("#2"))
			string = string.replaceAll("#2", String.valueOf(this.getInfoValueF(player, 2)));
		if(string.contains("#3"))
			string = string.replaceAll("#3", String.valueOf(this.getInfoValueF(player, 3)));
		if(string.contains("#4"))
			string = string.replaceAll("#4", String.valueOf(this.getInfoValueF(player, 4)));
		return (float) MathEx.evaluate(string);
	}
	
	public int getInfoValueI(Player player, int infoNum)
	{
		return (int)this.getInfoValueF(player, infoNum);
	}

	public float valueByLevel(float lv1, float lv2, float lv3, float lv4, float lv5)
	{
		if(this.level == 1)
			return lv1;
		else if(this.level == 2)
			return lv2;
		else if(this.level == 3)
			return lv3;
		else if(this.level == 4)
			return lv4;
		else if(this.level == 5)
			return lv5;
		
		return lv1;
	}
	
	public float valueByLevel(String vl)
	{
		float value = 0f;
		String[] broken = vl.split("/");
		int slot = this.level-1;
		if(slot >= 0 && slot <= 4)
		{
			value = Float.parseFloat(broken[slot]);
		}
		return value;
	}
	
	public int getChannelTicks()
	{
		return Math.min(this.channelTicks, this.maxChannelTime);
	}
	
	public void updateDescription(Player player, int level)
	{
		String desc = this.getBaseDescription(level);
		double info;
		float pp = player.getPP();
		float mp = player.getMP();
		float hp = player.maxHealth;
		info = MathEx.evaluate(this.infos[0].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[0].useAsInteger)
			desc = Pattern.compile("#0").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#0").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[1].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[1].useAsInteger)
			desc = Pattern.compile("#1").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#1").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[2].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[2].useAsInteger)
			desc = Pattern.compile("#2").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#2").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[3].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[3].useAsInteger)
			desc = Pattern.compile("#3").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#3").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[4].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[4].useAsInteger)
			desc = Pattern.compile("#4").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#4").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[5].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[5].useAsInteger)
			desc = Pattern.compile("#5").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#5").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[6].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[6].useAsInteger)
			desc = Pattern.compile("#6").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#6").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[7].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[7].useAsInteger)
			desc = Pattern.compile("#7").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#7").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[8].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[8].useAsInteger)
			desc = Pattern.compile("#8").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#8").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[9].expression.replaceAll("lvl", String.valueOf(level-1)).replaceAll("pp", String.valueOf(pp)).replaceAll("mp", String.valueOf(mp)).replaceAll("hp", String.valueOf(hp)).replaceAll("#0", String.valueOf(this.storedInfos[0])).replaceAll("#1", String.valueOf(this.storedInfos[1])).replaceAll("#2", String.valueOf(this.storedInfos[2])).replaceAll("#3", String.valueOf(this.storedInfos[3])).replaceAll("#4", String.valueOf(this.storedInfos[4])));
		if(this.infos[9].useAsInteger)
			desc = Pattern.compile("#9").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#9").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		desc = Pattern.compile("\\}").matcher(desc).replaceAll("[]");
		desc = Pattern.compile("\\{GC").matcher(desc).replaceAll("["+Constant.QUALITYCOLORNAME[Constant.QUALITY_IMAGINARY]+"]");
		desc = Pattern.compile("\\{G").matcher(desc).replaceAll("["+Constant.QUALITYCOLORNAME[Constant.QUALITY_EPIC]+"]");
		desc = Pattern.compile("\\{R").matcher(desc).replaceAll("[SCARLET]");
		desc = Pattern.compile("\\{C").matcher(desc).replaceAll("[CYAN]");
		desc = Pattern.compile("\\{O").matcher(desc).replaceAll("[GOLDENROD]");
		desc = Pattern.compile("\\{Y").matcher(desc).replaceAll("[YELLOW]");
		desc = Pattern.compile("\\{").matcher(desc).replaceAll("[YELLOW]");
		desc = Pattern.compile("-=").matcher(desc).replaceAll("[ORANGE]");
		desc = Pattern.compile("=-").matcher(desc).replaceAll("[]");
		
		this.description = desc;
	}
}

class CompactAnim
{
	public int customAnim = 0;
	public int customAnimFrames = 1;
	public int customAnimSkipper = 0;
	public boolean customAnimBlockMove = false;
	public boolean customAnimBlockAttack = false;
	public boolean customAnimShouldRotate = false;
	public Vector2 customAnimOffset = Vector2.Zero.cpy();
	
	public CompactAnim() {}

	public CompactAnim(int psaid, int frames, int skipper, boolean shouldRotate, boolean blockMove, boolean blockAttack, Vector2 centered)
	{
		this.customAnim = psaid;
		this.customAnimFrames = frames;
		this.customAnimSkipper = skipper;
		this.customAnimBlockMove = blockMove;
		this.customAnimBlockAttack = blockAttack;
		this.customAnimShouldRotate = shouldRotate;
		this.customAnimOffset = centered;
	}
	
	public CompactAnim(int psaid, int frames, int skipper, boolean shouldRotate, boolean blockMove, boolean blockAttack)
	{
		this.customAnim = psaid;
		this.customAnimFrames = frames;
		this.customAnimSkipper = skipper;
		this.customAnimBlockMove = blockMove;
		this.customAnimBlockAttack = blockAttack;
		this.customAnimShouldRotate = shouldRotate;
		this.customAnimOffset = Vector2.Zero.cpy();
	}
}