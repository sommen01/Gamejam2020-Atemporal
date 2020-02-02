package com.roguelike.constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.entities.Dummy;
import com.roguelike.entities.Entity;
import com.roguelike.entities.Lighting;
import com.roguelike.entities.Monster;
import com.roguelike.entities.NPC;
import com.roguelike.entities.Particle;
import com.roguelike.entities.Player;
import com.roguelike.entities.Projectile;
import com.roguelike.game.Content;
import com.roguelike.game.DJ;
import com.roguelike.game.Event;
import com.roguelike.game.Main;
import com.roguelike.game.MathEx;
import com.roguelike.game.Roguelike;
import com.roguelike.game.SaveInfos;
import com.roguelike.online.interpretations.SomeRequest;
import com.roguelike.world.GameMap;
import com.roguelike.world.Tile;

public class Item extends Entity
{
	// Item attributes
	public int id;
	public transient String trivia = "";
	public transient Color triviaColor;
	public transient String description = "";
	public transient String baseDescription;
	public transient Color descriptionColor;
	public transient int attackFrames;
	public transient int standFrames;
	public transient int hurtFrame;
	public transient int walkFrames;
	public transient int damage;
	public transient String damageFormula = "";
	public transient int damageType;
	public transient int quality;
	public transient boolean handCrafted;
	public transient int type;
	public transient int itemclass;
	public transient boolean rotateOnDualEquip;
	public transient int overrideStand;
	public transient int overrideAttack;
	public transient boolean dontAttack;
	public transient boolean canFlipAttack;
	public transient float offsetX;
	public transient float leftOffsetX;
	public transient float offsetY;
	public transient float roffsetX;
	public transient float roffsetY;
	public transient float woffsetX;
	public transient float wleftOffsetX;
	public transient float woffsetY;
	public transient Vector2 offsetRotateAttack = new Vector2();
	public transient boolean dualHanded;
	public transient boolean dualHandedAttack;
	public Skill specialSkill;
	public transient int cooldownFrames;
	public transient boolean standDrawHand;
	public transient boolean canStack;
	public int level;
	public transient int shootProj;
	public transient float shootSpeed;
	public transient float shootAccuracy;
	public transient boolean spawnProjGunMode;
	public transient boolean rotateAfterAttack;
	public transient boolean sneakyCritical;
	public transient boolean usable;
	public transient ArrayList<Integer> extraHits = new ArrayList<Integer>();
	public transient HashMap<Integer, Integer> extraAnimations = new HashMap<Integer, Integer>();
	public transient Set partOfSet;
	public transient MathEx[] infos = new MathEx[10];
	public Color dye = new Color(1f, 0.8f, 0.15f, 1f);
	public boolean dyed;
	public transient boolean dyeArmorColor = true;
	public transient Color armorColor = null;
	public transient Vector2 spawnProjectileOffset = new Vector2();
	public transient boolean diagonalAttackBase = false;
	public transient boolean canGoBack = true;
	public transient HashMap<Integer, DJ> attackSounds = new HashMap<Integer, DJ>();
	public transient float attackSoundPitch = 1f;

	// Coding & AI attributes
	public transient int animFrame;
	public transient int animFrameCounter;
	public transient float rotation;
	public boolean inWorld = false;
	public transient float timeInWorld;
	public transient int cdf;
	public transient int lastcdf;
	public transient Texture overrideStandTexture;
	public transient int overrideStandTextureFrames;
	public transient Texture overrideAttackTexture;
	public transient int overrideAttackTextureFrames;
	private transient boolean debugging;
	public transient int spriteWidth;
	public transient int spriteHeight;
	public int stacks = 1;
	private transient boolean rotateLeftFix;
	private transient int spriteHoldWidth;
	private transient int spriteHoldHeight;
	public transient int currentAnimation;
	public transient boolean currentDiagonalAttack;
	public transient boolean runningDiagAttack = false;
	public transient Player holder = null;
	public transient int equippedHand = 0;
	public int[] data = new int[10];
	
	public void initialize()
	{
		int level = this.level;
		int stacks = this.stacks;
		this.SetInfos(this.id, false);
		this.stacks = stacks;
		this.reforge(level);
	}
	
	public Item SetInfos(int index)
	{
		return this.SetInfos(index, true);
	}

	public Item SetInfos(int index, boolean fullReset)
	{
		this.id = index;
		if(fullReset)
		{
			this.specialSkill = null;
		}
		this.ResetStats();
		if(this.id == 1)
		{
			this.name = Main.lv("Tetano Blade", "Lâmina de Tétano");
			this.baseDescription = Main.lv("-=Tetanum:=- Increases the target's poison damage indefinitely ({G+1} damage per hit)."
					, "-=Tetanum=-: Aumenta o veneno do inimigo indefinidamente ({G+1} de dano por acerto).");
			this.trivia = Main.lv("+200% damage against diabetes.", "+200% de dano contra diabetes.");
			this.attackFrames = 6;
			this.hurtFrame = 3;
			this.damageFormula = "15 + (lvl * 5)/30";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_LEFT;
			this.cooldownFrames = 6;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.level = 42;
		}
		else if(this.id == 2)
		{
			this.name = Main.lv("Patience's Purpose", "Propósito da Paciência");
			this.baseDescription = Main.lv("-=Soul Judgement:=- Hits the target enemy even if out of range.",
					"-=Soul Judgement=-: Acerta o inimigo-alvo mesmo se fora de alcance.");
			this.descriptionColor = Color.WHITE;
			this.trivia = Main.lv("You can't live forever if you don't know the patience's purpose.",
					"Você não pode viver pra sempre se não souber o propósito da paciência");
			this.standFrames = 5;
			this.attackFrames = 7;
			this.hurtFrame = 4;
			this.damageFormula = "lvl*4.5";
			this.damageType = Constant.DAMAGETYPE_HOLY;
			this.quality = Constant.QUALITY_IMAGINARY;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.type = Constant.ITEMTYPE_LEFT;
			this.specialSkill = new Skill(1);
			this.dontAttack = true;
			this.cooldownFrames = 10;
			this.offsetX = -24;
			this.offsetY = 16;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.itemclass = Constant.ITEMCLASS_LONGSWORD;
			this.level = 100;
		}
		else if(this.id == 3)
		{
			this.name = Main.lv("Wooden Bow", "Arco de Madeira");
			this.trivia = Main.lv("Avoid barbs.", "Evite as farpas.");
			this.attackFrames = 7;
			this.hurtFrame = 4;
			this.damageFormula = "3 + (lvl)";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 2;
			this.offsetY = -8;
			this.dualHandedAttack = true;
			this.cooldownFrames = 30;
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.level = 1;
			this.shootProj = 1;
			this.shootSpeed = 1100f;
			this.rotateAfterAttack = true;
		}
		else if(this.id == 4)
		{
			this.name = Main.lv("Laser Saber", "Sabre Laser");
			this.baseDescription = Main.lv("-=Phase:=- Increases the next hit damage by {G#0} every time you hit an enemy. (up to {G#1} stacks)", 
					"-=Phase=-: Aumenta o dano do próximo ataque em {G#0} toda vez que acertar um inimigo. (máximo de {G#1} acúmulos)");
			this.descriptionColor = Color.WHITE;
			this.trivia = Main.lv("May the force be with you.", "Que a força esteja com você");
			this.attackFrames = 6;
			this.standFrames = 4;
			this.hurtFrame = 3;
			this.damageFormula = "lvl*1.5";
			this.damageType = Constant.DAMAGETYPE_FIRE;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_LEFT;
			this.cooldownFrames = 10;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.level = 46;
			this.infos[0].expression = "lvl / 3";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "lvl / 3";
			this.infos[1].useAsInteger = true;
		}
		else if(this.id == 5)
		{
			this.name = Main.lv("Darkrai's Fang", "Presa de Darkrai");
			this.baseDescription = Main.lv("-=Darkrai Eye:=- Applies a {Gmark} to the target every time you hit. A third {Gmark} in the same target will apply a guaranteed critical {R#0} additional damage.", 
					"-=Darkrai Eye=-: Aplica uma {Gmarca} no inimigo toda vez que você acertá-lo. Uma terceira {Gmarca} no mesmo alvo vai aplicar um crítico garantido com {R#0} de dano adicional.");
			this.trivia = Main.lv("This monster fits in your pocket.", "Esse monstro cabe no seu bolso.");
			this.damageFormula = "7 + (lvl*1.6)";
			this.attackFrames = 10;
			this.hurtFrame = 8;
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.offsetX = 24;
			this.offsetY = 24;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.cooldownFrames = 10;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.level = 21;
			this.infos[0].expression = "lvl * 12";
			this.infos[0].useAsInteger = true;
		}
		else if(this.id == 6)
		{
			this.name = Main.lv("Ice Queen Staff", "Cajado da Rainha do Gelo");
			this.baseDescription = Main.lv("Fires an ice ray.", "Dispara um raio de gelo.");
			this.trivia = Main.lv("She had a frozen heart.", "Ela tinha um coração congelado.");
			this.damageFormula = "lvl*2";
			this.attackFrames = 12;
			this.hurtFrame = 8;
			this.damageType = Constant.DAMAGETYPE_ICE;
			this.quality = Constant.QUALITY_EPIC;
			this.type = Constant.ITEMTYPE_LEFT;
			this.dontAttack = true;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.offsetX = 24;
			this.offsetY = 24;
			this.offsetRotateAttack = new Vector2(8, 0);
			this.cooldownFrames = 12;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.level = 63;
			this.shootProj = 2;
			this.shootSpeed = 700f;
		}
		else if(this.id == 7)
		{
			this.name = "La Geas";
			this.baseDescription = Main.lv("Blades {Gdamage} heavily increases based on {Gtime} and {Gvelocity}.",
					"O {Gdano} das lâminas aumenta fortemente baseado no {Gtempo} e {Gvelocidade}.");
			this.trivia = Main.lv("You are the hero who honor Tec, Polaris, Lamari and Niiko.",
					"Você é o heroi que honra Tec, Polaris, Lamari e Niiko.");
			this.damageFormula = "lvl/7.5";
			this.attackFrames = 17;
			this.hurtFrame = 0;
			this.standFrames = 14;
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_IMAGINARY;
			this.type = Constant.ITEMTYPE_LEFT;
			this.dontAttack = true;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.overrideStand = Constant.OVERRIDEATTACK_OVERRIDE;
			this.offsetRotateAttack = new Vector2(8, 0);
			this.cooldownFrames = 17;
			this.specialSkill = new Skill(2);
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.level = 100;
		}
		else if(this.id == 8)
		{
			this.name = Main.lv("Branch", "Galho");
			this.trivia = Main.lv("May be useful.", "Pode ser útil");
			this.damageFormula = "4+(lvl*0.75)";
			this.attackFrames = 6;
			this.hurtFrame = 3;
			this.standFrames = 1;
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.overrideAttack = Constant.OVERRIDEATTACK_SWING;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.cooldownFrames = 9;
			this.itemclass = Constant.ITEMCLASS_CLUB;
			this.level = 1;
		}
		else if(this.id == 9)
		{
			this.name = Main.lv("Death Frenesis", "Frênesi de Morte");
			this.baseDescription = Main.lv("-=Death Frenesis=-: Increases your critical hit chance after being hit by {G#0%} for 5 seconds", 
					"-=Death Frenesis=-: Aumenta a chance de acerto crítico em {G#0%} por 5 segundos após sofrer dano.");
			this.trivia = Main.lv("Shi, the Death.", "Shi, a Morte.");
			this.type = Constant.ITEMTYPE_SPECIAL;
			this.quality = Constant.QUALITY_LEGENDARY;
			this.overrideAttack = Constant.OVERRIDEATTACK_OVERRIDE;
			this.overrideStand = Constant.OVERRIDESTAND_OVERRIDE;
			this.itemclass = Constant.ITEMCLASS_PASSIVE;
			this.level = 83;
			this.infos[0].expression = "lvl";
		}
		else if(this.id == 10)
		{
			this.name = Main.lv("Nightfall", "Cair da Noite");
			this.baseDescription = Main.lv("Explosive projectile, high damage potential.", "Projétil explosivo, grande potencial de dano.");
			this.trivia = Main.lv("At least something to light this darkness and endless night.",
					"Ao menos algo para iluminar esta noite escura e inacabável.");
			this.type = Constant.ITEMTYPE_LEFT;
			this.quality = Constant.QUALITY_IMAGINARY;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.overrideStand = Constant.OVERRIDESTAND_OVERRIDE;
			this.offsetX = 48;
			this.offsetY = -16;
			this.dontAttack = true;
			this.attackFrames = 8;
			this.hurtFrame = 5;
			this.damageFormula = "lvl*6";
			this.damageType = Constant.DAMAGETYPE_HOLY;
			this.cooldownFrames = 8;
			this.offsetRotateAttack = new Vector2(-16, 0);
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.level = 100;
			this.shootProj = 5;
			this.shootSpeed = 2500f;
			this.dualHandedAttack = true;
		}
		else if(this.id == 11)
		{
			this.name = Main.lv("Cool Hat", "Chapéu Legal");
			this.baseDescription = Main.lv("{G+200} agility\n"
					+ "{G+300} lethality\n"
					+ "{G+10%} life steal",
					"{G+200} de agilidade\n"
					+ "{G+300} de lethality\n"
					+ "{G+10%} de roubo de vida");
			this.trivia = Main.lv("Keep cool with green!", "Fique legal com verde!");
			this.type = Constant.ITEMTYPE_HEAD;
			this.quality = Constant.QUALITY_LEGENDARY;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -4;
			this.woffsetX = -4;
			this.offsetY = 24;
			this.woffsetY = 24;
			this.leftOffsetX = -4;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.level = 85;
		}
		else if(this.id == 12)
		{
			this.name = Main.lv("Espectrophobia", "Espectrofobia");
			this.trivia = Main.lv("Try to not be consumed also.", "Tente não ser consumido junto.");
			this.baseDescription = Main.lv("-=Soul Blade:=- The blade receives {G1 soul} every time it hits an enemy and {G5 souls} every time it "
					+ "kills an enemy. Every {Gsoul} grants {G+30%} after-hit damage.",
					"-=Soul Blade=-: A lâmina recebe {G1 alma} toda vez que ela acerta um inimigo e {G5 almas} toda vez que ela "
					+ "mata um inimigo. Cada {Galma} garante {G+30%} de dano após os ataques.");
			this.attackFrames = 7;
			this.standFrames = 1;
			this.hurtFrame = 4;
			this.damageFormula = "lvl*4.5";
			this.damageType = Constant.DAMAGETYPE_DARKNESS;
			this.type = Constant.ITEMTYPE_LEFT;
			this.quality = Constant.QUALITY_IMAGINARY;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.cooldownFrames = 7;
			this.specialSkill = new Skill(3);
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.level = 100;
		}
		else if(this.id == 13)
		{
			this.name = Main.lv("Blood Ritual", "Ritual de Sangue");
			this.baseDescription = Main.lv("Better look author notes.", "Melhor olhar as notas do autor.");
			this.trivia = Main.lv("I asked for it everyday, I told her:\n\"Ah, send it.\".\nBut I never got what I wanted.",
					"Eu pedi por isso todo dia, eu disse a ela:\n\"Ah, manda.\".\nMas eu nunca consegui o que queria.");
			this.attackFrames = 3;
			this.standFrames = 1;
			this.hurtFrame = 0;
			this.damageFormula = "(lvl-7)*3";
			this.damageType = Constant.DAMAGETYPE_BLOOD;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_LEFT;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.cooldownFrames = 3;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.level = 47;
			this.shootProj = 8;
			this.shootSpeed = 800f;
			this.rotateAfterAttack = true;
		}
		else if(this.id == 14)
		{
			this.name = Main.lv("Axsis", "Machassis");
			this.trivia = Main.lv("Shock of culture.", "Choque de cultura");
			this.baseDescription = Main.lv("Causes {G100%} of your max health and {G5%} of enemy actual health ({G2%} against "
					+ "bosses) as extra damage.",
					"Causa {G100%} da sua vida máxima e {G5%} da vida atual do inimigo ({G2%} contra chefões) como dano adicional.");
			this.attackFrames = 11;
			this.hurtFrame = 9;
			this.standFrames = 1;
			this.damageFormula = "lvl*7.5";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_IMAGINARY;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_HEAVYAXE;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 15;
			this.offsetX -= 4;
			this.offsetY += 20;
			this.canFlipAttack = false;
			this.overrideStand = Constant.OVERRIDESTAND_OVERRIDE;
			this.level = 100;
		}
		else if(this.id == 15)
		{
			this.name = Main.lv("Dagger", "Adaga");
			this.trivia = Main.lv("A new world opens for you.", "Um novo mundo se abre para você");
			this.attackFrames = 6;
			this.hurtFrame = 1;
			this.standFrames = 1;
			this.damageFormula = "7 + lvl*4";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.cooldownFrames = 10;
			this.offsetRotateAttack = new Vector2(8, -16);
			this.itemclass = Constant.ITEMCLASS_DAGGER;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.offsetX = 0;
			this.offsetY = 24;
			this.canFlipAttack = false;
			this.level = 1;
			this.standDrawHand = false;
			this.sneakyCritical = true;
		}
		else if(this.id == 16)
		{
			this.name = Main.lv("Hood", "Capuz");
			this.trivia = Main.lv("Hides your eyes, keeps your smiles.", "Esconda seus olhos, mantenha seus sorrisos.");
			this.baseDescription = Main.lv("{G+#0} agility", "{G+#0} de agilidade");
			this.type = Constant.ITEMTYPE_HEAD;
			this.quality = Constant.QUALITY_COMMON;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -12;
			this.offsetY = 0;
			this.woffsetX = -12;
			this.woffsetY = 0;
			this.walkFrames = 5;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.infos[0].expression = "(lvl*1.5) + 6";
			this.infos[0].useAsInteger = true;
			this.level = 1;
		}
		else if(this.id == 17)
		{
			this.name = Main.lv("Starshooter", "Estrela Cadente");
			this.trivia = Main.lv("I keep looking to the sky, trying to find a way to see you.", 
					"Eu continuo olhando para o céu, tentando encontrar uma maneira de te encontrar.");
			this.baseDescription = Main.lv("-=Shooting Star:=- Increases the next attack damage by {G+#0%} for {3} seconds on flip.",
					"-=Shooting Star=-: Aumenta o dano do próximo ataque em {G+#0%} por {3} segundos ao usar o salto mortal.");
			this.type = Constant.ITEMTYPE_LEFT;
			this.attackFrames = 6;
			this.hurtFrame = 4;
			this.standFrames = 1;
			this.damageFormula = "lvl*2.4";
			this.damageType = Constant.DAMAGETYPE_HOLY;
			this.quality = Constant.QUALITY_RARE;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 30;
			this.offsetX = 2;
			this.offsetY = -8;
			this.offsetRotateAttack = new Vector2(8, 0);
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.level = 50;
			this.shootProj = 9;
			this.shootSpeed = 2500f;
			this.infos[0].expression = "lvl * 4";
			this.rotateAfterAttack = true;
		}
		else if(this.id == 18)
		{
			this.name = Main.lv("Steel Plate", "Placa de Aço");
			this.trivia = Main.lv("Makes you feel like a motorcyclist.", "Faz você se sentir um motoqueiro.");
			this.baseDescription = Main.lv("{G+#0} strength\n"
					+ "{G+#1%} maximum health",
					"{G+#0} de força\n"
					+ "{G+#1%} de vida máxima");
			this.type = Constant.ITEMTYPE_HEAD;
			this.quality = Constant.QUALITY_COMMON;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -4;
			this.woffsetX = -4;
			this.leftOffsetX = -4;
			this.offsetY = 12;
			this.woffsetY = 12;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.level = 1;
			this.infos[0].expression = "(lvl*1.5) + 3";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "((lvl*1.5) + 3)*2";
			this.infos[1].useAsInteger = true;
		}
		else if(this.id == 19)
		{
			this.name = Main.lv("Soldier Spear", "Lança de Soldado");
			this.trivia = Main.lv("Shouldn't it be illegal to have one?", "Não deveria ser ilegal ter uma?");
			this.type = Constant.ITEMTYPE_LEFT;
			this.quality = Constant.QUALITY_COMMON;
			this.attackFrames = 13;
			this.hurtFrame = 2;
			this.standFrames = 11;
			this.damageFormula = "12 + lvl * 3.3";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.itemclass = Constant.ITEMCLASS_LANCE;
			this.dualHandedAttack = true;
			this.offsetX = -20;
			this.offsetY = -20;
			this.cooldownFrames = 30;
			this.offsetRotateAttack.x = -48;
			this.level = 4;
			this.specialSkill = new Skill(61);
		}
		else if(this.id == 20)
		{
			this.name = Main.lv("Leather Robe", "Túnica de Couro");
			this.trivia = "Shhh.";
			this.baseDescription = Main.lv("{G+#0} lethality\n"
					+ "{G+#0%} maximum health\n"
					+ "{G+#1} movement speed",
					"{G+#0} de letalidade\n"
					+ "{G+#0%} de vida máxima\n"
					+ "{G+#1} de velocidade de movimento");
			this.type = Constant.ITEMTYPE_BODY;
			this.quality = Constant.QUALITY_COMMON;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = 0;
			this.offsetY = 0;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.level = 1;
			this.infos[0].expression = "(lvl*1.5) + 3";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "lvl + 30";
			this.infos[1].useAsInteger = true;
			this.dyeArmorColor = true;
			this.armorColor = new Color(0.22f, 0.16f, 0.11f, 1f);
		}
		else if(this.id == 21)
		{
			this.name = Main.lv("Dark Star", "Estrela Negra");
			this.trivia = "Baby I'm a dark star, dark star (8).";
			this.baseDescription = Main.lv("Shoots a fast dark projectiles chain.\n"
					+ "Arrows can stay {C0.25} seconds after being shot without colliding with terrain.", 
					"Atira projéteis rapidamente em sequência.\n"
					+ "As flechas permanecem {C0.25} segundos incapazes de colidirem com o terreno após serem atiradas.");
			this.attackFrames = 5;
			this.hurtFrame = 3;
			this.cooldownFrames = 0;
			this.damageFormula = "lvl*1.5";
			this.damageType = Constant.DAMAGETYPE_DARKNESS;
			this.type = Constant.ITEMTYPE_LEFT;
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.dualHandedAttack = true;
			this.offsetX = 6;
			this.offsetY = 0;
			this.level = 65;
			this.rotateAfterAttack = true;
		}
		else if(this.id == 22)
		{
			this.name = Main.lv("Unexpected Sword", "Espada Inesperada");
			this.trivia = Main.lv("You can't know the blade size if you can't see it.",
					"Você não sabe o tamanho da espada se não puder vê-la.");
			this.baseDescription = Main.lv("I n v i s i b l e", "I n v i s í v e l");
			this.descriptionColor = Color.GRAY;
			this.attackFrames = 1;
			this.hurtFrame = 0;
			this.standFrames = 1;
			this.damageFormula = "lvl*2";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.cooldownFrames = 5;
			this.level = 20;
		}
		else if(this.id == 23)
		{
			this.name = "Mie Hat";
			this.trivia = Main.lv("\"PUT A BUNNY HAT IN THE GAME!\"", "\"COLOCA UM CHAPÉU DE COELHINHO NO JOGO!\"");
			this.standFrames = 1;
			this.walkFrames = 1;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = 0;
			this.offsetY = -4;
			this.woffsetX = 0;
			this.woffsetY = -4;
			this.level = 45;
		}
		else if(this.id == 24)
		{
			this.name = "Mie Clothes";
			this.baseDescription = "{G+300%} attack speed";
			this.trivia = Main.lv("\"AN ENTIRE SET OF IT\"", "\"UM CONJUNTO INTEIRO!\"");
			this.type = Constant.ITEMTYPE_BODY;
			this.quality = Constant.QUALITY_RARE;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = -8;
			this.offsetY = 0;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.level = 1;
			this.leftOffsetX = 0;
			this.woffsetX = -8;
		}
		else if(this.id == 25)
		{
			this.name = Main.lv("Heavy Carrot", "Cenoura Pesada");
			this.trivia = "Used to frighten other creatures.";
			this.attackFrames = 1;
			this.hurtFrame = 0;
			this.damageFormula = "lvl*1.5";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_HEAVYCLUB;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.debugging = true;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 10;
			this.standDrawHand = false;
			this.level = 42;
		}
		else if(this.id == 26)
		{
			this.name = Main.lv("Pretty Plasma Pocket Pistol", "Pistola Perfeita e Portátil de Plasma");
			this.trivia = Main.lv("Pretty plasma, pretty pistol, put in the pocket.",
					"Plasma perfeito, pistola perfeita, permaneça-a parada e disparando.");
			this.baseDescription = Main.lv("-=Perfection=-: Increases attack speed and damage by {G2%} for {G3} seconds every "
					+ "time you hit. (Max {G#0} stacks)",
					"-=Perfection=-: Aumenta a velocidade de ataque e o dano em {G2%} por {G3} segundos toda "
					+ "vez que você acertar um ataque. (Max {G#0} stacks)");
			this.attackFrames = 6;
			this.standFrames = 1;
			this.hurtFrame = 0;
			this.damageFormula = "lvl";
			this.damageType = Constant.DAMAGETYPE_ENERGY;
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.offsetX = -12;
			this.offsetY = 8;
			this.cooldownFrames = 20;
			this.level = 63;
			this.shootProj = 13;
			this.shootSpeed = 500;
			this.spawnProjGunMode = true;
			this.rotateAfterAttack = true;
			this.infos[0].expression = "lvl";
			this.infos[0].useAsInteger = true;
		}
		else if(this.id == 27)
		{
			this.name = Main.lv("Time Glitch", "Falha Temporal");
			this.trivia = Main.lv("Kurikaesu, the Repeater.", "Kurikaesu, a Repetição.");
			this.baseDescription = Main.lv("-=Time Glitch:=- Receives {G1} {OTime Glitch} stack "
					+ "for {G#0} seconds every time you hit. \n"
					+ "After {G5} stacks any on-hit effect is applied twice.",
					"-=Time Glitch=-: Recebe {G1} acúmulo de {OTime Glitch} "
					+ "por {G#0} segundos toda vez que você acertar um ataque. \n"
					+ "Após {G5} acúmulos, qualquer efeito adicional de acerto é aplicado duas vezes.");
			this.itemclass = Constant.ITEMCLASS_PASSIVE;
			this.type = Constant.ITEMTYPE_SPECIAL;
			this.level = 50;
			this.infos[0].expression = "lvl/10";
			this.quality = Constant.QUALITY_EPIC;
		}
		else if(this.id == 28)
		{
			this.name = Main.lv("Vampiric Lord Helmet", "Capacete do Lorde Vampírico");
			this.trivia = Main.lv("Blood sounds tasty", "Sangue até que parece legal");
			this.baseDescription = Main.lv("{G+#0%} life steal", "{G+#0%} de roubo de vida");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.level = 22;
			this.offsetY = -4;
			this.woffsetY = -4;
			this.offsetX = 0;
			this.woffsetX = 0;
			this.leftOffsetX = 0;
			this.infos[0].expression = "(lvl/20) + 1";
			this.partOfSet = Set.VAMPIRICLORD;
		}
		else if(this.id == 29)
		{
			this.name = Main.lv("Vampiric Lord Breastplate", "Peitoral do Lorde Vampírico");
			this.trivia = Main.lv("Blood sounds even more tasty.", "Sangue parece ainda mais legal.");
			this.baseDescription = Main.lv("{G+#0%} life steal", "{G+#0%} de roubo de vida");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.level = 24;
			this.offsetX = 0;
			this.woffsetX = 0;
			this.offsetY = 0;
			this.woffsetY = 0;
			this.armorColor = new Color(0.9f, 0.3f, 0.3f, 1f);
			this.partOfSet = Set.VAMPIRICLORD;
			this.infos[0].expression = "(lvl/15) + 1";
		}
		else if(this.id == 30)
		{
			this.name = Main.lv("Vampiric Broadsword", "Espada Larga Vampírica");
			this.trivia = Main.lv("Did I ever say blood sounds tasty?", "Eu já disse que sangue parece legal?");
			this.baseDescription = Main.lv("-=Vampirism:=- {G#1%} ({R#0}) of this weapon's damage is returned as "
					+ "health when it hits an enemy.",
					"-=Vampirism=-: {G#1%} ({R#0}) do dano desta arma é retornado como "
					+ "vida quando ela acerta um inimigo.");
			this.attackFrames = 11;
			this.standFrames = 1;
			this.hurtFrame = 3;
			this.damageFormula = "30 + (lvl*1.8)";
			this.damageType = Constant.DAMAGETYPE_BLOOD;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_LONGSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 15;
			this.offsetX = -24;
			this.level = 25;
			this.infos[0].expression = "dmg * (lvl/100 * 0.2)";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "lvl * 0.2";
			this.partOfSet = Set.VAMPIRICLORD;
		}
		else if(this.id == 31)
		{
			this.name = Main.lv("Rounded Shield", "Escudo Redondo");
			this.trivia = Main.lv("Smash 'em", "Esmague-os");
			this.baseDescription = Main.lv("{G+#0%} max health", "{G+#0%} de vida máxima");
					//+ "This is your first shield, shields can deflect contact attacks, increases your resistances and use some special spells.";
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_RIGHT;
			this.itemclass = Constant.ITEMCLASS_SHIELD;
			this.level = 13;
			this.offsetX = 18;
			this.offsetY = 16;
			this.attackFrames = 1;
			this.hurtFrame = 4;
			this.infos[0].expression = "lvl*1.2";
			this.canGoBack = false;
		}
		else if(this.id == 32)
		{
			this.name = Main.lv("Perfect Excalibur", "Excalibur Perfeita");
			this.trivia = Main.lv("The original in your hands, do you feel stone-d?", "A original em suas mãos.");
			this.baseDescription = Main.lv("Curses the target in the holy smite, causing {G4%} of this weapon damage per stack "
					+ "per tick for {G5} seconds. (Max 25 stacks)", 
					"Almadiçoa o alvo em julgamento santo, causando {G4%} do dano desta arma por acúmulo "
					+ "por tick por {G5} segundos. (Máx 25 acúmulos)");
			this.attackFrames = 11;
			this.standFrames = 1;
			this.hurtFrame = 7;
			this.damageFormula = "lvl*6";
			this.damageType = Constant.DAMAGETYPE_HOLY;
			this.quality = Constant.QUALITY_IMAGINARY;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_BROADSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.offsetX = 128;
			this.offsetY = 12;
			this.dualHanded = false;
			this.dualHandedAttack = true;
			this.standDrawHand = false;
			this.cooldownFrames = 11;
			this.level = 100;
		}
		else if(this.id == 33)
		{
			this.name = Main.lv("Forge God's Coin", "Moeda do Deus da Forja");
			this.trivia = Main.lv("Unknown and unbreakable material.", "Material desconhecido e inquebrável.");
			this.baseDescription = Main.lv("Use at any {OForge God's Altar} to activate it temporarily.",
					"Use em qualquer {OAltar do Deus da Forja} para ativá-lo temporariamente.");
			this.level = 50;
			this.canStack = true;
			this.quality = Constant.QUALITY_RARE;
		}
		else if(this.id == 34)
		{
			this.name = Main.lv("Wooden Bow", "Arco de Madeira");
			this.trivia = Main.lv("Did you think it would be easy?", "Você achou que seria fácil?");
			this.attackFrames = 7;
			this.hurtFrame = 4;
			this.damageFormula = "6";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 2;
			this.offsetY = -8;
			this.dualHandedAttack = true;
			this.cooldownFrames = 30;
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.level = 1;
			this.shootProj = 16;
			this.shootSpeed = 1100f;
			this.rotateAfterAttack = true;
		}
		else if(this.id == 35)
		{
			this.name = Main.lv("Silver Shortsword", "Espada Curta de Prata");
			this.trivia = Main.lv("Did you think it would be easy?", "Você achou que seria fácil?");
			this.damageFormula = "15";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.attackFrames = 10;
			this.hurtFrame = 7;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 15;
			this.level = 1;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.standDrawHand = false;
			this.offsetX = -16;
			this.offsetY = 8;
		}
		else if(this.id == 36)
		{
			this.name = Main.lv("Top Hat", "Cartola");
			this.trivia = Main.lv("Their voices echoes in your head.", "As vozes deles ecoam em sua cabeça.");
			this.quality = Constant.QUALITY_LEGENDARY;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.level = 85;
			this.offsetX = -8;
			this.offsetY = 24;
			this.leftOffsetX = -8;
			this.woffsetX = -8;
			this.woffsetY = 24;
		}
		else if(this.id == 37)
		{
			this.name = Main.lv("Dead Formality", "Formalidade Morta");
			this.trivia = Main.lv("You heart is now finally filled.", "Seu coração finalmente está completo.");
			this.quality = Constant.QUALITY_LEGENDARY;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.level = 90;
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
			this.leftOffsetX = 0;
			this.armorColor = new Color(0.04f, 0.04f, 0.04f, 1f);
		}
		else if(this.id == 38)
		{
			this.name = Main.lv("Fire Wood Staff", "Cajado de Madeira de Fogo");
			this.trivia = "Mans not hot!";
			this.attackFrames = 10;
			this.standFrames = 1;
			this.hurtFrame = 0;
			this.damageFormula = "15";
			this.damageType = Constant.DAMAGETYPE_FIRE;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 0;
			this.offsetY = -28;
			this.cooldownFrames = 30;
			this.level = 1;
		}
		else if(this.id == 39)
		{
			this.name = Main.lv("Scroll of Basic Learning: Knight", "Pergaminho de Conhecimento Básico: Knight");
			this.trivia = Main.lv("Be brave like Leonidas.", "Seja corajoso como Leonidas.");
			this.baseDescription = Main.lv("Use this scroll to become a {GKnight}.", 
					"Use este pergaminho para se tornar um {GKnight}.");
			this.quality = Constant.QUALITY_RARE;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 40)
		{
			this.name = Main.lv("Scroll of Basic Learning: Mage", "Pergaminho de Conhecimento Básico: Mago");
			this.trivia = Main.lv("Be crazy like Lewin.", "Seja louco como Lewin.");
			this.baseDescription = Main.lv("Use this scroll to become a {GMage}.",
					"Use este pergaminho para se tornar um {GMago}.");
			this.quality = Constant.QUALITY_RARE;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 41)
		{
			this.name = Main.lv("Scroll of Basic Learning: Rogue", "Pergaminho de Conhecimento Básico: Ladino");
			this.trivia = Main.lv("Be neutral like Lushmael.", "Seja neutro como Lushmael.");
			this.baseDescription = Main.lv("Use this scroll to become a {GRogue}.", 
					"Use este pergaminho para se tornar um {GLadino}");
			this.quality = Constant.QUALITY_RARE;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 42)
		{
			this.name = Main.lv("Scroll of Basic Learning: Ranger", "Pergaminho de Conhecimento Básico: Atirador");
			this.trivia = Main.lv("Be ignorant like Victor.", 
					"Seja ignorante como Victor.");
			this.baseDescription = Main.lv("Use this scroll to become a {GRanger}.",
					"Use este pergaminho para se tornar um {GAtirador}");
			this.quality = Constant.QUALITY_RARE;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 43)
		{
			this.name = Main.lv("Volcanic Stone-forjed Armor", "Armadura Forjada em Pedra Vulcânica");
			this.trivia = Main.lv("In memory of Bygorn.", "Em memória de Bygorn.");
			this.baseDescription = Main.lv("{G+#0%} max health", "{G+#0%} de vida máxima");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = 0;
			this.offsetY = 0;
			this.roffsetX = 0;
			this.roffsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
			this.armorColor = new Color(0.16f, 0.16f, 0.16f, 1f);
			this.level = 10;
			this.infos[0].expression = "lvl*1.6";
		}
		else if(this.id == 44)
		{
			this.name = Main.lv("Yeti's Paradox", "Paradoxo do Yeti");
			this.trivia = Main.lv("One of the seven forbidden links.", "Um dos sete elos proibidos.");
			this.triviaColor = new Color(0f, 1f, 1f, 1f);
			this.baseDescription = Main.lv("This weapon constitutes of a three attacks combo.\n"
					+ "-=First attack=-: Causes {G50%} extra damage to enemies affected by {OCold Tiredness}.\n"
					+ "-=Second attack=-: Freezes enemies for {G1.5} seconds and applies {OCold Tiredness} for {G6} seconds.\n"
					+ "-=Third attack=-: Instant kills frozen enemies with less than {G20%} health.",
					"Esta arma constitui em um combo de três ataques.\n"
					+ "-=Primeiro ataque=-: Causa {G50%} de dano extra para inimigos afetados por {OCansaço Gélido}.\n"
					+ "-=Segundo ataque=-: Congela inimigos por {G1.5} segundos e aplica {OCansaço Gélido} for {G6} segundos.\n"
					+ "-=Terceiro ataque=-: Finaliza instantaneamente inimigos congelados com menos de {G20%} de vida.");
			this.attackFrames = 26;
			this.extraHits.add(5);
			this.extraHits.add(15);
			this.hurtFrame = 20;
			this.damageFormula = "lvl*3";
			this.damageType = Constant.DAMAGETYPE_ICE;
			this.quality = Constant.QUALITY_LEGENDARY;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_BROADSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.standDrawHand = false;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = false;
			this.offsetX = 90;
			this.offsetY = 90;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 15;
			this.level = 85;
		}
		else if(this.id == 45)
		{
			this.name = "Golden Heart";
			this.trivia = "Someday, someone humble, somewhere in the world, doing something good for someone else "
					+ "one day will equip this armour and get his eternal reward.";
			this.baseDescription = "Op pra uma desgraca.";
			this.standFrames = 1;
			this.quality = Constant.QUALITY_IMAGINARY;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = -4;
			this.offsetY = -8;
			this.woffsetX = -4;
			this.woffsetY = -12;
			this.leftOffsetX = -4;
			this.level = 100;
		}
		else if(this.id == 46)
		{
			this.name = "Gilded Crown";
			this.trivia = "Someday.";
			this.baseDescription = "Mais op ;d";
			this.standFrames = 1;
			this.walkFrames = 1;
			this.quality = Constant.QUALITY_IMAGINARY;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -4;
			this.offsetY = 24;
			this.woffsetX = -4;
			this.woffsetY = 24;
			this.leftOffsetX = -4;
			this.level = 100;
		}
		else if(this.id == 47)
		{
			this.name = Main.lv("Phoenix Tear", "Lágrima de Fênix");
			this.trivia = Main.lv("One of the seven forbidden links.", "Um dos sete elos proibidos.");
			this.triviaColor = new Color(1f, 0.85f, 0f, 1f);
			this.baseDescription = Main.lv("Attack rate increases with attack speed.", 
					"A taxa de ataque aumenta com a velocidade de ataque.");
			this.attackFrames = 17;
			this.standFrames = 1;
			this.hurtFrame = 0;
			this.damageFormula = "lvl*3";
			this.damageType = Constant.DAMAGETYPE_FIRE;
			this.quality = Constant.QUALITY_LEGENDARY;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_HEAVYAXE;
			this.overrideStand = Constant.OVERRIDESTAND_OVERRIDE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.offsetX = -24;
			this.offsetY = -12;
			this.dualHandedAttack = true;
			this.shootProj = 17;
			this.shootSpeed = 900f;
			this.level = 85;
			this.cooldownFrames = 30;
			this.specialSkill = new Skill(17);
		}
		else if(this.id == 48)
		{
			this.name = "Silence";
			this.trivia = "";
			this.baseDescription = "{G+100%} life steal";
			this.standFrames = 1;
			this.walkFrames = 1;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = 4;
			this.offsetY = -4;
			this.woffsetX = 4;
			this.woffsetY = -4;
			this.leftOffsetX = -12;
			this.level = 42;
		}
		else if(this.id == 49)
		{
			this.name = Main.lv("Sanity's Epilogue", "Epílogo da Sanidade");
			this.trivia = "Are you insane like me?";
			this.baseDescription = "-=Bullet Mode:=- Increases attack speed by {G10%} for {G2} seconds for every successful hit. (Max {G3} stacks)\n"
					+ "-=Dragon Mode:=- Shots a fireball which explodes upon contact.\n"
					+ "-=Overall:=- Killing enemies increases attack speed by {G30%} for {G5} seconds. (Unlimited stacks)";
			this.attackFrames = 5;
			this.standFrames = 1;
			this.hurtFrame = 0;
			this.damageFormula = "lvl*3";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_IMAGINARY;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.offsetX = -70;
			this.offsetY = 8;
			this.offsetRotateAttack = new Vector2(-18, -16);
			this.dualHanded = true;
			this.cooldownFrames = 30;
			this.standDrawHand = false;
			this.level = 100;
			this.shootProj = 11;
			this.shootSpeed = 300f;
			this.rotateAfterAttack = true;
			this.specialSkill = new Skill(18);
			this.rotateLeftFix = true;
		}
		else if(this.id == 50)
		{
			this.name = Main.lv("Punishment Mask", "Mascara da Punição");
			this.trivia = Main.lv("Pray for your life.", "Reze por sua vida.");
			this.baseDescription = Main.lv("{G+#0%} critical hit damage\n"
					+ "{G-#1%} critical hit chance\n"
					+ "{G-#1} vitality\n"
					+ "{G+#1} agility",
					"{G+#0%} de dano crítico\n"
					+ "{G-#1%} de chance de acerto crítico\n"
					+ "{G-#1} de vitalidade\n"
					+ "{G+#1} de agilidade");
			this.standFrames = 1;
			this.walkFrames = 1;
			this.quality = Constant.QUALITY_EPIC;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.level = 71;
			this.offsetY = -16;
			this.offsetX = -24;
			this.woffsetX = -24;
			this.woffsetY = -16;
			this.leftOffsetX = -16;
			this.infos[0].expression = "(lvl*5)/7";
			this.infos[1].expression = "lvl/7";
			this.infos[1].useAsInteger = true;
			this.partOfSet = Set.PUNISHMENT;
		}
		else if(this.id == 51)
		{
			this.name = Main.lv("Punishment Robe", "Manto da Punição");
			this.trivia = Main.lv("This does not mean you will be spared.", "Não significa que você será perdoado.");
			this.baseDescription = Main.lv("{G+#0%} critical hit chance\n"
					+ "{G+#0} vitality\n"
					+ "{G+#1} agility", 
					"{G+#0%} de chance de acerto crítico\n"
					+ "{G+#0} de vitalidade\n"
					+ "{G+#1} de agilidade");
			this.quality = Constant.QUALITY_EPIC;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.level = 71;
			this.offsetX = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
			this.offsetY = 0;
			this.armorColor = new Color(0.08f, 0.08f, 0.08f, 1f);
			this.partOfSet = Set.PUNISHMENT;
			this.infos[0].expression = "lvl/7";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "(lvl)";
			this.infos[1].useAsInteger = true;
		}
		else if(this.id == 52)
		{
			this.name = Main.lv("Intuitive Expertise", "Perícia Intuitiva");
			this.baseDescription = Main.lv("-=Intuitive Expertise=-: Increases attack speed by {G#0%} for {G#1} seconds "
					+ "every time you hit. (Max {G10} stacks)",
					"-=Intuitive Expertise=-: Aumenta a velocidade ataque em {G#0%} por {G#1} segundos "
					+ "toda vez que você acertar um ataque. (Máx {G10} acúmulos)");
			this.trivia = Main.lv("Senmon, the Specialty.", "Senmon, a Especialidade");
			this.type = Constant.ITEMTYPE_SPECIAL;
			this.quality = Constant.QUALITY_RARE;
			this.overrideAttack = Constant.OVERRIDEATTACK_OVERRIDE;
			this.overrideStand = Constant.OVERRIDESTAND_OVERRIDE;
			this.itemclass = Constant.ITEMCLASS_PASSIVE;
			this.level = 43;
			this.infos[0].expression = "lvl/8";
			this.infos[1].expression = "1+lvl/100";
		}
		else if(this.id == 53)
		{
			this.name = Main.lv("Sins Pain", "Dor dos Pecados");
			this.trivia = Main.lv("Batsu, the Punishment", "Batsu, a Punição");
			this.type = Constant.ITEMTYPE_SPECIAL;
			this.quality = Constant.QUALITY_EPIC;
			this.overrideAttack = Constant.OVERRIDEATTACK_OVERRIDE;
			this.overrideStand = Constant.OVERRIDESTAND_OVERRIDE;
			this.itemclass = Constant.ITEMCLASS_PASSIVE;
			this.level = 70;
			this.partOfSet = Set.PUNISHMENT;
		}
		else if(this.id == 54)
		{
			this.name = "Peacemaker's Cover";
			this.trivia = "Scare and kill.";
			this.baseDescription = "TODO";
			this.walkFrames = 2;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -12;
			this.offsetY = 0;
			this.woffsetX = -12;
			this.woffsetY = 0;
			this.leftOffsetX = -12;
			this.level = 8;
		}
		else if(this.id == 55)
		{
			this.name = "Great Lord Sword";
			this.trivia = "He was not great cause he was good.";
			this.attackFrames = 5;
			this.hurtFrame = 0;
			this.damageFormula = "75 + lvl*1.5";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.offsetX = 15;
			this.offsetY = 13;
			this.rotateOnDualEquip = true;
			this.cooldownFrames = 10;
			this.offsetRotateAttack = new Vector2(-92, 0);
			this.level = 49;
		}
		else if(this.id == 56)
		{
			this.name = "Great Lord Helmet";
			this.trivia = "He covered his face so he could walk on the streets without being judged by his actitudes.";
			this.standFrames = 1;
			this.walkFrames = 1;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -4;
			this.offsetY = -4;
			this.woffsetX = -4;
			this.woffsetY = -4;
			this.level = 51;
		}
		else if(this.id == 57)
		{
			this.name = "Great Lord Breastplate";
			this.trivia = "He covered his face so he could walk on the streets without being judged by his actitudes.";
			this.standFrames = 1;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = -4;
			this.offsetY = 0;
			this.woffsetX = -4;
			this.woffsetY = 0;
			this.armorColor = new Color(1f, 0.42f, 0f, 1f);
			this.level = 50;
		}
		else if(this.id == 58)
		{
			this.name = "Crescent Moonlight Lance";
		}
		else if(this.id == 59)
		{
			this.name = Main.lv("Arthur's Prologue", "Prólogo de Arthur");
			this.trivia = Main.lv("The beggining is always the most striking moment.", 
					"O começo é sempre o momento mais memorável.");
			this.attackFrames = 6;
			this.standFrames = 15;
			this.hurtFrame = 2;
			this.damageFormula = "110 + (lvl)";
			this.baseDescription = Main.lv("-=Prologue=-: The {Gfirst hit} on an enemy causes {R#0%} of his actual health "
					+ "as additional damage.",
					"-=Prologue=-: O {Gprimeiro ataque} em um inimigo causa {R#0%} da vida atual dele como dano adicional.");
			this.infos[0].expression = "5 + lvl/4";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_RARE;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_LONGSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.offsetX = -16;
			this.offsetY = 8;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 15;
			this.standDrawHand = false;
			this.level = 52;
		}
		else if(this.id == 60)
		{
			this.name = Main.lv("Also Known As 2147", "Algo Komo 2147");
			this.trivia = Main.lv("Classic as 200 years ago.", "Clássica como há 200 anos atrás.");
			this.attackFrames = 4;
			this.standFrames = 1;
			this.hurtFrame = 0;
			this.damageFormula = "20 + (lvl * 1.4)";
			this.damageType = Constant.DAMAGETYPE_ENERGY;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.rotateAfterAttack = true;
			//this.rotateLeftFix = true;
			this.dontAttack = true;
			this.offsetRotateAttack = new Vector2(-44, 0);
			this.offsetX = 8;
			this.offsetY = 16;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 15;
			this.standDrawHand = false;
			this.level = 22;
			this.spawnProjGunMode = true;
			this.shootProj = 37;
			this.shootSpeed = 200;
			this.shootAccuracy = 5f;
		}
		else if(this.id == 61)
		{
			this.name = Main.lv("Leaf Mask", "Máscara de Folhagem");
			this.trivia = Main.lv("Oh my God, where did he go?", "Ó meu Deus, pra onde ele foi?");
			this.baseDescription = Main.lv("{G+#0%} max health\n"
					+ "{G+#1} agility",
					"{G+#0%} de vida máxima\n"
					+ "{G+#1} de agilidade");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -8;
			this.offsetY = -8;
			this.leftOffsetX = -4;
			this.woffsetX = -8;
			this.woffsetY = -8;
			this.infos[0].expression = "4 + (lvl/4)";
			this.infos[1].expression = "3 + (lvl/1.5)";
			this.infos[1].useAsInteger = true;
			this.level = 4;
			this.standFrames = 1;
			this.walkFrames = 1;
			this.partOfSet = Set.LEAF;
		}
		else if(this.id == 62)
		{
			this.name = Main.lv("Leaf Cover", "Cobertura de Folhagem");
			this.trivia = Main.lv("Maybe he is behind this bush that strangely can walk.",
					"Talvez ele esteja atrás desse arbusto que estranhamente consegue andar.");
			this.baseDescription = Main.lv("{G+#0%} max health\n"
					+ "{G+#1} agility",
					"{G+#0%} de vida máxima\n"
					+ "{G+#1} de agilidade");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = -4;
			this.offsetY = -8;
			this.woffsetX = -4;
			this.woffsetY = -8;
			this.leftOffsetX = -4;
			this.level = 4;
			this.infos[0].expression = "5 + (lvl/3.9)";
			this.infos[1].expression = "3 + (lvl/2)";
			this.infos[1].useAsInteger = true;
			this.partOfSet = Set.LEAF;
		}
		else if(this.id == 63)
		{
			this.name = Main.lv("Sharp Stinger", "Ferrão Afiado");
			this.trivia = Main.lv("* Don't tell anyone but the stinger is only at the tip of the blade *",
					"* Não conte pra ninguem mas o ferrão só está na ponta da lâmina *");
			this.baseDescription = Main.lv("-=Stinger=-: Every attack has a 10% chance to cause {R#0} extra damage.",
					"-=Stinger=-: Cada ataque possui 10% de chance de causar {R#0} de dano adicional.");
			this.infos[0].expression = "13 + lvl * 4";
			this.infos[0].useAsInteger = true;
			this.attackFrames = 6;
			this.standFrames = 1;
			this.hurtFrame = 2;
			this.damageFormula = "9+ lvl * 1.8";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.rotateOnDualEquip = true;
			this.canFlipAttack = true;
			this.offsetX = 12;
			this.offsetY = 12;
			this.cooldownFrames = 30;
			this.level = 4;
		}
		else if(this.id == 64)
		{
			this.name = Main.lv("Royal Stinger", "Ferrão Real");
			this.trivia = Main.lv("Sometimes the royal bee life isn't so fortunate.",
					"As vezes a vida das abelhas rainhas não é tão feliz.");
			this.baseDescription = Main.lv("-=Stinger=-: Every attack has a 10% chance to cause {R#0} extra damage.",
					"-=Stinger=-: Cada ataque possui 10% de chance de causar {R#0} de dano adicional.");
			this.infos[0].expression = "50 + lvl * 8";
			this.infos[0].useAsInteger = true;
			this.attackFrames = 6;
			this.standFrames = 1;
			this.damageFormula = "8 + lvl*2.2";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_DAGGER;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.offsetRotateAttack = new Vector2(8, -16);
			this.canFlipAttack = false;
			this.standDrawHand = false;
			this.offsetX = 4;
			this.offsetY = 32;
			this.cooldownFrames = 20;
			this.level = 4;
		}
		else if(this.id == 65)
		{
			this.name = Main.lv("Beehive Scepter", "Cetro de Colméia");
			this.trivia = Main.lv("There is a big mistery BEEHIVE the bee's life.",
					"Ainda há um grande mistério COLM essas abelhas.");
			this.baseDescription = Main.lv("Summons {G10} hornets that fires stingers in the mouse direction, each stinger causes 50% of this weapon damage.\n"
					+ "This weapon has no knockback in any of its attacks.",
					"Invoca {G10} zangões que disparam ferrões na direção do mouse, cada ferrão causa 50% do dano desta arma.\n"
					+ "Esta arma não possui empurrão em seus ataques.");
			this.attackFrames = 6;
			this.standFrames = 1;
			this.hurtFrame = 1;
			this.damageFormula = "lvl * 4.8 + 10";
			this.damageType = Constant.DAMAGETYPE_NATURE;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.offsetX = 18;
			this.offsetY = 33;
			this.cooldownFrames = 30;
			this.offsetRotateAttack = new Vector2(-20, 0);
			this.level = 4;
			this.specialSkill = new Skill(57);
		}
		else if(this.id == 66)
		{
			this.name = Main.lv("Vikristmas Cap", "Toca de Vikristmas");
			this.trivia = Main.lv("Release the kristmas!", "Liberem o kristmas!");
			this.baseDescription = Main.lv("{G+#0} luck\n"
					+ "{G+#1%} max health",
					"{G+#0} de sorte\n"
					+ "{G+#1%} de vida máxima");
			this.standFrames = 1;
			this.walkFrames = 4;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -12;
			this.woffsetX = -20;
			this.offsetY = 0;
			this.woffsetY = 0;
			this.level = 6;
			this.infos[0].expression = "4 + lvl/1.8";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "4 + lvl/3.2";
		}
		else if(this.id == 67)
		{
			this.name = Main.lv("Slime Bubble", "Bolha de Slime");
			this.trivia = "Boing boing boing...";
			this.baseDescription = Main.lv("{G+#0} vitality\n"
					+ "{G+#1%} max health",
					"{G+#0} de vitalidade\n"
					+ "{G+#1%} de vida máxima");
			this.standFrames = 1;
			this.walkFrames = 5;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.woffsetX = -12;
			this.wleftOffsetX = -12;
			this.woffsetY = -8;
			this.offsetX = -8;
			this.offsetY = -8;
			this.leftOffsetX = -8;
			this.level = 2;
			this.infos[0].expression = "4 + lvl/1.8";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "4 + lvl/3.2";
		}
		else if(this.id == 68)
		{
			this.name = Main.lv("Slime Plate", "Placa de Slime");
			this.trivia = Main.lv("It goes like boing boing...", "É tipo boing boing...");
			this.baseDescription = Main.lv("{G+#0} vitality\n"
					+ "{G+#1%} max health",
					"{G+#0} de vitalidade\n"
					+ "{G+#1%} de vida máxima");
			this.standFrames = 1;
			this.walkFrames = 1;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.woffsetX = -4;
			this.woffsetY = -4;
			this.offsetX = -4;
			this.offsetY = -4;
			this.leftOffsetX = -4;
			this.level = 2;
			this.infos[0].expression = "4 + lvl/1.8";
			this.infos[0].useAsInteger = true;
			this.infos[1].expression = "4 + lvl/3.2";
		}
		else if(this.id == 69)
		{
			this.name = Main.lv("Iron Helmet", "Capacete de Ferro");
			this.trivia = Main.lv("Handcrafting metal items is a helpful knowledge for warriors.",
					"Construção artesanal de itens de metal é um conhecimento útil para guerreiros.");
			this.baseDescription = Main.lv("{G+#0%} max health", "{G+#0%} de vida máxima");
			this.standFrames = 1;
			this.walkFrames = 1;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = 0;
			this.offsetY = -4;
			this.woffsetX = 0;
			this.woffsetY = -4;
			this.leftOffsetX = 0;
			this.level = 10;
			this.infos[0].expression = "8 + lvl/1.8";
		}
		else if(this.id == 70)
		{
			this.name = Main.lv("Iron Breastplate", "Armadura de Ferro");
			this.trivia = Main.lv("They told me this was the brest plate.", "Eles me disseram que era a armadura mais ferrada.");
			this.baseDescription = Main.lv("{G+#0%} max health", "{G+#0%} de vida máxima");
			this.standFrames = 1;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
			this.leftOffsetX = 0;
			this.armorColor = new Color(0.24f, 0.24f, 0.24f, 1f);
			this.level = 10;
			this.infos[0].expression = "8.5 + lvl/1.7";
		}
		else if(this.id == 71)
		{
			this.name = Main.lv("Iron Broadsword", "Espada Larga de Ferro");
			this.trivia = Main.lv("I ron't know if it's really sharp.", "Se é afiado? Aí ferrou em.");
			this.standFrames = 1;
			this.attackFrames = 10;
			this.hurtFrame = 6;
			this.damageFormula = "13 + lvl * 3.8";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_BROADSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = true;
			this.offsetX = -16;
			this.offsetY = 8;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 35;
			this.standDrawHand = false;
			this.level = 10;
		}
		else if(this.id == 72)
		{
			this.name = Main.lv("Iron Bow", "Arco de Ferro");
			this.trivia = Main.lv("The arrow is pretty fragile. Just kidding, I'm being iron-ic.", 
					"A flecha é bem frágil. Brincadeira, só estou sendo iron-ico.");
			this.attackFrames = 5;
			this.standFrames = 1;
			this.hurtFrame = 3;
			this.damageFormula = "7 + (lvl * 1.7)";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 4;
			this.offsetY = -4;
			this.dualHandedAttack = true;
			this.cooldownFrames = 28;
			this.level = 10;
			this.shootProj = 51;
			this.shootSpeed = 1200;
		}
		else if(this.id == 73)
		{
			this.name = Main.lv("Iron Shield", "Escudo de Ferro");
			this.trivia = Main.lv("At least it's not flammable.", "Ao menos não é inflamável");
			this.baseDescription = Main.lv("{G+#0%} max health", "{G+#0%} de vida máxima");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_RIGHT;
			this.itemclass = Constant.ITEMCLASS_SHIELD;
			this.level = 10;
			this.offsetX = 18;
			this.offsetY = 15;
			this.attackFrames = 1;
			this.hurtFrame = 4;
			this.infos[0].expression = "5 + lvl/1.8";
			this.canGoBack = false;
		}
		else if(this.id == 74)
		{
			this.name = Main.lv("Iron Shortsword", "Espada Curta de Ferro");
			this.trivia = Main.lv("Because bigger swords are overrated.", "Porque espadas grandes são superestimadas.");
			this.damageFormula = "9 + lvl * 2.2";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.attackFrames = 9;
			this.standFrames = 1;
			this.hurtFrame = 3;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = true;
			this.offsetX = 15;
			this.offsetY = 13;
			this.cooldownFrames = 25;
			this.level = 10;
			this.rotateOnDualEquip = true;
		}
		else if(this.id == 75)
		{
			this.name = Main.lv("Iron Dagger", "Adaga de Ferro");
			this.trivia = Main.lv("Blood is also made of iron.", "Sangue também é composto por ferro.");
			this.attackFrames = 6;
			this.hurtFrame = 1;
			this.standFrames = 1;
			this.damageFormula = "12 + lvl*4.8";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.cooldownFrames = 10;
			this.offsetRotateAttack = new Vector2(8, -16);
			this.itemclass = Constant.ITEMCLASS_DAGGER;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.offsetX = 8;
			this.offsetY = 32;
			this.canFlipAttack = false;
			this.level = 10;
			this.standDrawHand = false;
			this.sneakyCritical = true;
		}
		else if(this.id == 76)
		{
			this.name = Main.lv("Wooden Shortsword", "Espada Curta de Madeira");
			this.trivia = Main.lv("The sword itself causes no damage, only the barbs hurts.",
					"A espada em si não causa dano, apenas as farpas machucam.");
			this.attackFrames = 6;
			this.standFrames = 1;
			this.hurtFrame = 3;
			this.damageFormula = "4 + lvl * 1.5";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.cooldownFrames = 28;
			this.offsetX = 12;
			this.offsetY = 12;
			this.level = 1;
			this.rotateOnDualEquip = true;
		}
		else if(this.id == 77)
		{
			this.name = Main.lv("Wooden Broadsword", "Espada Larga de Madeira");
			this.trivia = Main.lv("It's wider and out of barbs but still, it only hurts because of its weight.",
					"É mais larga e sem farpas mas ainda assim, só machuca por causa do peso.");
			this.attackFrames = 8;
			this.standFrames = 1;
			this.hurtFrame = 5;
			this.damageFormula = "7 + lvl * 1.8";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_BROADSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.standDrawHand = false;
			this.cooldownFrames = 35;
			this.offsetX = -16;
			this.offsetY = 8;
			this.level = 1;
		}
		else if(this.id == 78)
		{
			this.name = Main.lv("Iron Ore", "Minério de Ferro");
			this.trivia = Main.lv("You need almost 100,000 beans to get 500g of iron.", 
					"É necessário quase 100.000 feijões para conseguir 500g de ferro.");
			this.baseDescription = Main.lv("Can be used to craft iron-themed and other items.",
					"Pode ser usado para construir itens de ferro e afins.");
			this.level = 10;
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 79)
		{
			this.name = Main.lv("Knight's Shovel", "Pá do Guerreiro");
			this.trivia = Main.lv("Totally not stolen.", "Certamente não foi roubada.");
			this.baseDescription = Main.lv("This weapon {Rcan not} critical hit randomly.\n"
					+ "Enemies under {G#0%} health receives guaranteed critical hits.",
					"Esta arma {Rnão pode} causar acertos críticos aleatóriamente.\n"
					+ "Inimigos com menos de {G#0%} de vida recebem acertos críticos garantidos.");
			this.attackFrames = 10;
			this.standFrames = 1;
			this.hurtFrame = 6;
			this.damageFormula = "9 + lvl * 2.8";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_HEAVYAXE;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = false;
			this.offsetX = 48;
			this.offsetY = 12;
			this.dualHanded = false;
			this.dualHandedAttack = true;
			this.cooldownFrames = 30;
			this.level = 8;
			this.infos[0].expression = "20 + lvl * 0.8";
		}
		else if(this.id == 80)
		{
			this.name = Main.lv("Butcher's Cleaver", "Cutelo do Açougueiro");
			this.trivia = Main.lv("", "");
			this.baseDescription = Main.lv("-=Butchery=-: Flesh and blood enemies recieves #0% increased damage.", 
					"-=Cutelaria=-: Inimigos de carne e osso recebem #0% de dano aumentado.");
			this.attackFrames = 8;
			this.standFrames = 1;
			this.hurtFrame = 4;
			this.damageFormula = "8 + lvl * 1.5";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.offsetX = 12;
			this.offsetY = 8;
			this.cooldownFrames = 35;
			this.level = 15;
			this.infos[0].expression = "lvl * 4";
			this.infos[0].useAsInteger = true;
		}
		else if(this.id == 81)
		{
			this.name = Main.lv("Machete", "Machete");
			this.trivia = Main.lv("\"Made to cut bush\"", "\"Feita para cortar mato\"");
			this.attackFrames = 6;
			this.standFrames = 1;
			this.hurtFrame = 2;
			this.damageFormula = "10 + lvl * 2";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.cooldownFrames = 30;
			this.offsetX = 12;
			this.offsetY = 12;
			this.level = 2;
		}
		else if(this.id == 82)
		{
			this.name = Main.lv("Gold Ore", "Minério de Ouro");
			this.trivia = Main.lv("I think you should keep it, it's going to be very valuable in the future.", 
					"Acho que você deveria manter com você, vai ser muito valioso no futuro.");
			this.baseDescription = Main.lv("Can be used to craft gold-themed and other items.",
					"Pode ser usado para construir itens de ouro e afins.");
			this.level = 30;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 83)
		{
			this.name = Main.lv("Stone", "Pedra");
			this.trivia = Main.lv("Better a stone in the hand than two in the reins.", 
					"Antes uma pedra na mão do que duas nos rins.");
			this.baseDescription = Main.lv("Can be used to craft stone-themed and other items.",
					"Pode ser usado para construir itens de pedra e afins.");
			this.level = 5;
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 84)
		{
			this.name = Main.lv("Polished Stone Armor", "Armadura de Pedra Polida");
			this.trivia = Main.lv("You can't imagine how heavy this actually is.",
					"Você não consegue imaginar o quão pesado isso é.");
			this.baseDescription = Main.lv("{G+#0%} max health\n"
					+ "{G+#1} strength",
					"{G+#0%} de vida máxima\n"
					+ "{G+#1} de força");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetY = 0;
			this.woffsetX = 0;
			this.armorColor = new Color(0.45f, 0.45f, 0.45f, 1f);
			this.level = 6;
			this.infos[0].expression = "5 + lvl/2.15";
			this.infos[1].expression = "5 + lvl/4";
			this.infos[1].useAsInteger = true;
		}
		else if(this.id == 85)
		{
			this.name = Main.lv("Polished Stone Helmet", "Capacete de Pedra Polida");
			this.trivia = Main.lv("It has never been so difficult to headbang.",
					"Nunca foi tão difícil fazer headbang.");
			this.baseDescription = Main.lv("{G+#0%} max health\n"
					+ "{G+#1} strength",
					"{G+#0%} de vida máxima\n"
					+ "{G+#1} de força");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = 0;
			this.offsetY = -4;
			this.woffsetY = -4;
			this.level = 5;
			this.infos[0].expression = "4 + lvl/2.2";
			this.infos[1].expression = "4 + lvl/3.5";
			this.infos[1].useAsInteger = true;
		}
		else if(this.id == 86)
		{
			this.name = Main.lv("Stone Warhammer", "Martelo de Guerra de Pedra");
			this.trivia = Main.lv("With sanded sides for a clean SMASH.",
					"Com os lados lixados para um belo ESMAGAMENTO.");
			this.baseDescription = Main.lv("-=Smash=-: {G#0%} chance to stun enemies for 1 second on hit.",
					"-=Smash=-: {G#0%} de chance de atordoar inimigos por 1 segundo ao acertá-los.");
			this.attackFrames = 1;
			this.hurtFrame = 0;
			this.damageFormula = "9 + lvl * 3.2";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_HEAVYHAMMER;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = false;
			this.offsetX = -32;
			this.offsetY = 20;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 45;
			this.standDrawHand = false;
			this.level = 8;
			this.infos[0].expression = "10 + lvl * 0.2";
		}
		else if(this.id == 87)
		{
			this.name = Main.lv("Magic Wood Sword", "Espada de Madeira Mágica");
			this.trivia = Main.lv("Be aware when passing by under a tree, risk of falling swords.", 
					"Tome cuidado quando for passar por baixo de uma árvore, risco de espadas cadentes.");
			this.attackFrames = 6;
			this.hurtFrame = 3;
			this.damageFormula = "11 + lvl * 2.6";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.offsetX = 9;
			this.offsetY = 12;
			this.roffsetX = -3;
			this.roffsetY = 1;
			this.cooldownFrames = 26;
			this.level = 6;
			this.rotateOnDualEquip = true;
			this.specialSkill = new Skill(63);
		}
		else if(this.id == 88)
		{
			this.name = Main.lv("Scroll of Basic Learning: Hemomancer", "Pergaminho de Conhecimento Básico: Hemomante");
			this.trivia = Main.lv("Be thirsty like Trot.", "Tenha sede como Trot.");
			this.baseDescription = Main.lv("Use this scroll to become a {GHemomancer}.",
					"Use este pergaminho para se tornar um {GHemomante}.");
			this.quality = Constant.QUALITY_LEGENDARY;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 89)
		{
			this.name = Main.lv("Small Health Potion", "Poção de Vida Pequena");
			this.trivia = Main.lv("Yummy.", "Delicioso.");
			this.baseDescription = Main.lv("Use this potion to heal yourself by 20 + {R5%} of your max health.", 
					"Use esta poção para curar-se em 20 + {R5%} de sua vida máxima.");
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_USABLE;
			this.level = 1;
			this.usable = true;
			this.canStack = true;
		}
		else if(this.id == 90)
		{
			this.name = Main.lv("Medium Health Potion", "Poção de Vida Média");
			this.trivia = Main.lv("Yummier.", "Deliciosão.");
			this.baseDescription = Main.lv("Use this potion to heal yourself by 200 + {R15%} of your max health.", 
					"Use esta poção para curar-se em 200 + {R15%} de sua vida máxima.");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_USABLE;
			this.level = 1;
			this.usable = true;
			this.canStack = true;
		}
		else if(this.id == 91)
		{
			this.name = Main.lv("Large Health Potion", "Poção de Vida Grande");
			this.trivia = Main.lv("Yummierest.", "Mais deliciosão.");
			this.baseDescription = Main.lv("Use this potion to heal yourself by 500 + {R40%} of your max health.", 
					"Use esta poção para curar-se em 500 + {R40%} de sua vida máxima.");
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_USABLE;
			this.level = 1;
			this.usable = true;
			this.canStack = true;
		}
		else if(this.id == 92)
		{
			this.name = Main.lv("Scroll of Basic Learning: Samurai", "Pergaminho de Conhecimento Básico: Samurai");
			this.trivia = Main.lv("Be honoured like Musashi.", "Seja honrado como Musashi.");
			this.baseDescription = Main.lv("Use this scroll to become a {GSamurai}.",
					"Use este pergaminho para se tornar um {GSamurai}.");
			this.quality = Constant.QUALITY_LEGENDARY;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 93)
		{
			this.name = Main.lv("Scroll of Basic Learning: Lancer", "Pergaminho de Conhecimento Básico: Lanceiro");
			this.trivia = Main.lv("Be invictus like Louis.", "Seja invicto como Louis.");
			this.baseDescription = Main.lv("Use this scroll to become a {GLancer}.",
					"Use este pergaminho para se tornar um {GLanceiro}.");
			this.quality = Constant.QUALITY_LEGENDARY;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 94)
		{
			this.name = Main.lv("Scroll of Basic Learning: Cassino Gambler", "Pergaminho de Conhecimento Básico: Trapaceiro de Cassino");
			this.trivia = Main.lv("Be lucky like Devole.", "Seja sortudo como Devole.");
			this.baseDescription = Main.lv("Use this scroll to become a {GCassino Gambler}.",
					"Use este pergaminho para se tornar um {GTrapaceiro de Cassino}.");
			this.quality = Constant.QUALITY_LEGENDARY;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 95)
		{
			this.name = Main.lv("Scroll of Basic Learning: Arcane Trickster", "Pergaminho de Conhecimento Básico: Indolente Arcano");
			this.trivia = Main.lv("Be tricky like Nelirem.", "Seja malandro como Nelirem.");
			this.baseDescription = Main.lv("Use this scroll to become an {GArcane Trickster}.",
					"Use este pergaminho para se tornar um {Indolente Arcano}.");
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 96)
		{
			this.name = Main.lv("Golden Helmet", "Capacete Dourado");
			this.trivia = Main.lv("People eyes shines when looking at you.", "Os olhos das pessoas brilham ao olhar para você.");
			this.infos[0].expression = "10+lvl/1.6";
			this.baseDescription = Main.lv("{G+#0%} max health\n{GShiny}", "{G+#0%} de vida máxima\n{GBrilhante}");
			this.walkFrames = 1;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.level = 30;
			this.offsetY = -4;
			this.woffsetY = -4;
			this.leftOffsetX = -4;
			this.offsetX = 0;
			this.woffsetX = 0;
			
			this.customShader = Content.shinyS;
			this.customShaderUValues.put("u_ticks", new String[] {"ticks"});
		}
		else if(this.id == 97)
		{
			this.name = Main.lv("Golden Breastplate", "Peitoral Dourado");
			this.trivia = Main.lv("A respectable person.", "Uma pessoa de respeito.");
			this.infos[0].expression = "10+lvl/1.5";
			this.baseDescription = Main.lv("{G+#0%} max health\n{GShiny}", "{G+#0%} de vida máxima\n{GBrilhante}");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.level = 30;
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
			this.armorColor = new Color(1f, 0.9f, 0.14f, 1f);
			
			this.customShader = Content.shinyS;
			this.customShaderUValues.put("u_ticks", new String[] {"ticks"});
		}
		else if(this.id == 98)
		{
			this.name = Main.lv("Golden Broadsword", "Espada Larga Dourada");
			this.trivia = Main.lv("Deadly beauty.", "Beleza mortal.");
			this.standFrames = 1;
			this.attackFrames = 11;
			this.hurtFrame = 5;
			this.damageFormula = "16 + lvl * 4.5";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_BROADSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = true;
			this.offsetX = -16;
			this.offsetY = 8;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 35;
			this.standDrawHand = false;
			this.level = 30;
			
			this.customShader = Content.shinyS;
			this.customShaderUValues.put("u_ticks", new String[] {"ticks"});
			this.customShaderUValues.put("color", new String[] {"scr", "scg", "scb"});
		}
		else if(this.id == 99)
		{
			this.name = Main.lv("Golden Shortsword", "Espada Curta Dourada");
			this.trivia = Main.lv("Compact and fancy.", "Compacto e bonito.");
			this.standFrames = 1;
			this.attackFrames = 9;
			this.hurtFrame = 3;
			this.damageFormula = "9 + lvl * 2.5";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = true;
			this.offsetX = 9;
			this.offsetY = 12;
			this.roffsetX = -2;
			this.roffsetY = 4;
			this.rotateOnDualEquip = true;
			this.cooldownFrames = 25;
			this.level = 30;
			
			this.customShader = Content.shinyS;
			this.customShaderUValues.put("u_ticks", new String[] {"ticks"});
		}
		else if(this.id == 100)
		{
			this.name = Main.lv("Golden Shield", "Escudo Dourado");
			this.trivia = Main.lv("Smash 'em (stylish)", "Esmague-os (com estilo)");
			this.baseDescription = Main.lv("{G+#0%} max health", "{G+#0%} de vida máxima");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_RIGHT;
			this.itemclass = Constant.ITEMCLASS_SHIELD;
			this.level = 30;
			this.offsetX = 19;
			this.offsetY = 15;
			this.attackFrames = 1;
			this.hurtFrame = 4;
			this.infos[0].expression = "lvl*2";
			this.canGoBack = false;
			
			this.customShader = Content.shinyS;
			this.customShaderUValues.put("u_ticks", new String[] {"ticks"});
		}
		else if(this.id == 101)
		{
			this.name = Main.lv("Silver Breastplate", "Peitoral de Prata");
			this.trivia = Main.lv("Not the best elo.", "Não é o melhor elo.");
			this.infos[0].expression = "10+lvl/1.8";
			this.baseDescription = Main.lv("{G+#0%} max health", "{G+#0%} de vida máxima");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.level = 20;
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
			this.armorColor = new Color(0.9f, 0.9f, 0.95f, 1f);
		}
		else if(this.id == 102)
		{
			this.name = Main.lv("Silver Helmet", "Capacete de Prata");
			this.trivia = Main.lv("Run fast enough and you get quicksilver.", "Corra rápido o suficiente e consiga quicksilver.");
			this.infos[0].expression = "10+lvl/1.85";
			this.baseDescription = Main.lv("{G+#0%} max health", "{G+#0%} de vida máxima");
			this.walkFrames = 1;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.level = 20;
			this.offsetY = -4;
			this.woffsetY = -4;
			this.offsetX = 0;
			this.woffsetX = 0;
		}
		else if(this.id == 103)
		{
			this.name = Main.lv("Silver Broadsword", "Espada Larga de Prata");
			this.trivia = Main.lv("It is cheaper to melt down silver coins and make it than buying it.", "É mais barato derreter moedas de prata e fazê-la do que comprar uma.");
			this.standFrames = 1;
			this.attackFrames = 10;
			this.hurtFrame = 6;
			this.damageFormula = "14 + lvl * 4.2";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_BROADSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = true;
			this.offsetX = -16;
			this.offsetY = 8;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 35;
			this.standDrawHand = false;
			this.level = 20;
		}
		else if(this.id == 104)
		{
			this.name = Main.lv("Silver Shortsword", "Espada Curta de Prata");
			this.trivia = Main.lv("Also known as SSS.", "Também conhecida como SSS.");
			this.standFrames = 1;
			this.attackFrames = 9;
			this.hurtFrame = 3;
			this.damageFormula = "8 + lvl * 2.35";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = true;
			this.offsetX = 9;
			this.offsetY = 12;
			this.roffsetX = -2;
			this.roffsetY = 4;
			this.rotateOnDualEquip = true;
			this.cooldownFrames = 25;
			this.level = 20;
		}
		else if(this.id == 105)
		{
			this.name = Main.lv("Amethyst", "Ametista");
			this.trivia = Main.lv("No whips included.", "Sem chicotes inclusos.");
			this.baseDescription = Main.lv("Darkness Jewel", "Jóia da Escuridão");
			this.descriptionColor = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_DARKNESS];
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 106)
		{
			this.name = Main.lv("Sapphire", "Safira");
			this.trivia = Main.lv("Blue is warmest color, call it safire.", "Azul é a cor mais quente, chame-a de safire.");
			this.baseDescription = Main.lv("Energy Jewel", "Jóia da Energia");
			this.descriptionColor = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ENERGY];
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 107)
		{
			this.name = Main.lv("Emerald", "Esmeralda");
			this.trivia = Main.lv("Purple is not the venom color and you can't change my mind.", "Roxo não é a cor do veneno e você não pode mudar minha opinião.");
			this.baseDescription = Main.lv("Nature Jewel", "Jóia da Natureza");
			this.descriptionColor = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_NATURE];
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 108)
		{
			this.name = Main.lv("Vanadinite", "Vanadinita");
			this.trivia = Main.lv("Now you can literally call it safire.", "Agora você pode literalmente chamá-la de safire.");
			this.baseDescription = Main.lv("Fire Jewel", "Jóia do Fogo");
			this.descriptionColor = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE];
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 109)
		{
			this.name = Main.lv("Diamond", "Diamante");
			this.trivia = Main.lv("Hail old Minecraft times.", "Saudemos os velhos tempos de Minecraft.");
			this.baseDescription = Main.lv("Ice Jewel", "Jóia do Gelo");
			this.descriptionColor = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_ICE];
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 110)
		{
			this.name = Main.lv("Ruby", "Rubi");
			this.trivia = Main.lv("Gemstones are complex, just like Rubyk Cubes.", "Pedras preciosas são complexas como os Cubos de Rubik.");
			this.baseDescription = Main.lv("Blood Jewel", "Jóia do Sangue");
			this.descriptionColor = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_BLOOD];
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 111)
		{
			this.name = Main.lv("Onyx", "Ônix");
			this.trivia = Main.lv("Emanates a totally reliable death aura.", "Emana uma aura de morte totalmente confiável.");
			this.baseDescription = Main.lv("Death Jewel", "Jóia da Morte");
			this.descriptionColor = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_DEATH];
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 112)
		{
			this.name = Main.lv("Ambar", "Âmbar");
			this.trivia = Main.lv("If ambar is the holy jewel then were old creatures saintful?", "Se âmbar é a jóia sagrada, então as criaturas antigas eram santas?");
			this.baseDescription = Main.lv("Holiness Jewel", "Jóia da Santidade");
			this.descriptionColor = Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_HOLY];
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 113)
		{
			this.name = Main.lv("Slime Ball", "Bola de Slime");
			this.trivia = Main.lv("Views not included.", "Visualizações não inclusas.");
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 114)
		{
			this.name = Main.lv("Bush Leaves", "Folhas de Arbusto");
			this.trivia = Main.lv("Sneaky sneaky.", "Sorrateiro.");
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 115)
		{
			this.name = Main.lv("Scroll of Recipes", "Pergaminho das Receitas");
			this.trivia = Main.lv("Aka blacksmith in a nutshell.", "Ou então ferreiro enlatado.");
			this.baseDescription = Main.lv("Use this scroll to learn a new -=Common Recipe=-.",
					"Use este pergaminho para aprender uma -=Receita Comum=-.");
			if(this.data[0] > 0)
			{
				String itemname = Main.lv("[RED]FAIL[]", "[RED]FALHA[]");
				Recipe r = Recipe.getRecipeForItem(this.data[0]);
				if(r != null)
				{
					Item i = new Item().SetInfos(r.itemid);
					itemname = "["+Constant.QUALITYCOLORNAME[i.quality]+"]"+i.name+"[]";
				}
				
				this.baseDescription += "\n[LIME]Receita Especial:[] " + itemname;
			}
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 116)
		{
			this.name = Main.lv("Scroll of Recipes", "Pergaminho das Receitas");
			this.trivia = Main.lv("Aka blacksmith in a nutshell.", "Ou então ferreiro enlatado.");
			this.baseDescription = Main.lv("Use this scroll to learn a new -=Uncommon Recipe=-.",
					"Use este pergaminho para aprender uma -=Receita Incomum=-.");
			if(this.data[0] > 0)
			{
				String itemname = Main.lv("[RED]FAIL[]", "[RED]FALHA[]");
				Recipe r = Recipe.getRecipeForItem(this.data[0]);
				if(r != null)
				{
					Item i = new Item().SetInfos(r.itemid);
					itemname = "["+Constant.QUALITYCOLORNAME[i.quality]+"]"+i.name+"[]";
				}
				
				this.baseDescription += "\n[LIME]Receita Especial:[] " + itemname;
			}
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 20;
			this.usable = true;
		}
		else if(this.id == 117)
		{
			this.name = Main.lv("Scroll of Recipes", "Pergaminho das Receitas");
			this.trivia = Main.lv("Aka blacksmith in a nutshell.", "Ou também ferreiro enlatado.");
			this.baseDescription = Main.lv("Use this scroll to learn a new -=Rare Recipe=-.",
					"Use este pergaminho para aprender uma -=Receita Rara=-.");
			if(this.data[0] > 0)
			{
				String itemname = Main.lv("[RED]FAIL[]", "[RED]FALHA[]");
				Recipe r = Recipe.getRecipeForItem(this.data[0]);
				if(r != null)
				{
					Item i = new Item().SetInfos(r.itemid);
					itemname = "["+Constant.QUALITYCOLORNAME[i.quality]+"]"+i.name+"[]";
				}
				
				this.baseDescription += "\n[LIME]Receita Especial:[] " + itemname;
			}
			this.quality = Constant.QUALITY_RARE;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 40;
			this.usable = true;
		}
		else if(this.id == 118)
		{
			this.name = Main.lv("Scroll of Recipes", "Pergaminho das Receitas");
			this.trivia = Main.lv("Aka blacksmith in a nutshell.", "Ou então ferreiro enlatado.");
			this.baseDescription = Main.lv("Use this scroll to learn a new -=Epic Recipe=-.",
					"Use este pergaminho para aprender uma -=Receita Épica=-.");
			if(this.data[0] > 0)
			{
				String itemname = Main.lv("[RED]FAIL[]", "[RED]FALHA[]");
				Recipe r = Recipe.getRecipeForItem(this.data[0]);
				if(r != null)
				{
					Item i = new Item().SetInfos(r.itemid);
					itemname = "["+Constant.QUALITYCOLORNAME[i.quality]+"]"+i.name+"[]";
				}
				
				this.baseDescription += "\n[LIME]Receita Especial:[] " + itemname;
			}
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 60;
			this.usable = true;
		}
		else if(this.id == 119)
		{
			this.name = Main.lv("Scroll of Recipes", "Pergaminho das Receitas");
			this.trivia = Main.lv("Aka blacksmith in a nutshell.", "Ou então ferreiro enlatado.");
			this.baseDescription = Main.lv("Use this scroll to learn a new -=Legendary Recipe=-.",
					"Use este pergaminho para aprender uma -=Receita Lendária=-.");
			if(this.data[0] > 0)
			{
				String itemname = Main.lv("[RED]FAIL[]", "[RED]FALHA[]");
				Recipe r = Recipe.getRecipeForItem(this.data[0]);
				if(r != null)
				{
					Item i = new Item().SetInfos(r.itemid);
					itemname = "["+Constant.QUALITYCOLORNAME[i.quality]+"]"+i.name+"[]";
				}
				
				this.baseDescription += "\n[LIME]Receita Especial:[] " + itemname;
			}
			this.quality = Constant.QUALITY_LEGENDARY;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 80;
			this.usable = true;
		}
		else if(this.id == 120)
		{
			this.name = Main.lv("Silver Ore", "Minério de Prata");
			this.trivia = Main.lv("In second place even when against itself.", 
					"Em segundo lugar até mesmo quando contra si mesmo.");
			this.baseDescription = Main.lv("Can be used to craft silver-themed and other items.",
					"Pode ser usado para construir itens de prata e afins.");
			this.level = 20;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 121)
		{
			this.name = Main.lv("Magic Shell", "Casco Mágico");
			this.trivia = Main.lv("Unknown composition.", 
					"Composição desconhecida.");
			this.level = 1;
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 122)
		{
			this.name = Main.lv("Antlers", "Galhada");
			this.trivia = Main.lv("What a cruelty...", "Mas que crueldade...");
			this.infos[0].expression = "10+lvl/1.9";
			this.infos[1].expression = "5+lvl";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/2";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} agility\n{G+#2} vitality", "{G+#0%} de vida máxima\n{G+#1} de agilidade\n{G+#2} de vitalidade");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -4;
			this.offsetY = 24;
			this.woffsetX = -4;
			this.woffsetY = 24;
			this.leftOffsetX = -20;
			this.level = 30;
		}
		else if(this.id == 123)
		{
			this.name = Main.lv("Broken Reindeer Antler", "Chifre de Rena Quebrado");
			this.trivia = Main.lv("Hide that from Santa Claus.", 
					"Esconda isso do Papai Noel.");
			this.level = 30;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 124)
		{
			this.name = Main.lv("Tiny Crown", "Coroazinha");
			this.trivia = Main.lv("In the land of the blind, the one-eyed man is king.", "Em terra de cego, quem tem olho é rei.");
			this.level = 12;
			this.infos[0].expression = "10+lvl/3";
			this.infos[1].expression = "4+lvl/2";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/4";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} strength\n{G+#2} vitality", "{G+#0%} de vida máxima\n{G+#1} de força\n{G+#2} de vitalidade");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -8;
			this.offsetY = 22;
			this.leftOffsetX = 25;
			this.woffsetX = -8;
			this.woffsetY = 22;
			
			this.customShader = Content.shinyS;
			this.customShaderUValues.put("u_ticks", new String[] {"ticks"});
		}
		else if(this.id == 125)
		{
			this.name = Main.lv("Stinger", "Ferrão");
			this.trivia = Main.lv("Fun fact: whenever a hornet uses his stinger, he dies.", 
					"Fato engraçado: quando um zangão usa seu ferrão, ele morre.");
			this.level = 4;
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 126)
		{
			this.name = Main.lv("Bee Cloth", "Tecido de Abelha");
			this.trivia = Main.lv("Fun fact: neither the stinger nor the honeycomb trivia are funny.", 
					"Fato engraçado: nem a trivialidade do ferrão, nem a da colméia são engraçadas.");
			this.level = 4;
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 127)
		{
			this.name = Main.lv("Honeycomb", "Colméia");
			this.trivia = Main.lv("Fun fact: if this is in your hands, then tons of bees are homeless.", 
					"Fato engraçado: se isto está em suas mãos, então várias abelhas perderam suas casas.");
			this.level = 4;
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 128)
		{
			this.name = Main.lv("Bear Hood", "Capuz de Urso");
			this.trivia = Main.lv("Meet the bearserker.", "Conheça o bearserker.");
			this.level = 7;
			this.infos[0].expression = "10+lvl/2.8";
			this.infos[1].expression = "4+lvl/1.5";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/3.6";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} strength\n{G+#2} vitality", "{G+#0%} de vida máxima\n{G+#1} de força\n{G+#2} de vitalidade");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -4;
			this.offsetY = -4;
			this.leftOffsetX = -12;
			this.woffsetX = -4;
			this.woffsetY = -4;
		}
		else if(this.id == 129)
		{
			this.name = Main.lv("Bear Robe", "Manto de Urso");
			this.trivia = Main.lv("Bear hugs rate increased to 100%.", "Taxa de abraços de urso aumentados para 100%.");
			this.level = 7;
			this.infos[0].expression = "10+lvl/2.7";
			this.infos[1].expression = "4+lvl/1.4";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/3.5";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} strength\n{G+#2} vitality", "{G+#0%} de vida máxima\n{G+#1} de força\n{G+#2} de vitalidade");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.armorColor = new Color(0.6f, 0.4f, 0.1f, 1f);
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
		}
		else if(this.id == 130)
		{
			this.name = Main.lv("Bear Skin", "Pele de Urso");
			this.trivia = Main.lv("Bear carpets are scary.", 
					"Tapetes de urso são assustadores.");
			this.level = 7;
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 131)
		{
			this.name = Main.lv("Berserking Axe", "Machado Furioso");
			this.baseDescription = Main.lv("-=Berserking=-: Melee damage and physical power increased up to {G+#0%} based on your lost health.", "-=Berserking=-: Dano corpo-a-corpo e poder físico aumentado em até {G+#0%} baseado na su vida perdida.");
			this.trivia = Main.lv("The crystal is not shining in your hands.", "O cristal não está brilhando em suas mãos.");
			this.triviaColor = Color.SALMON;
			this.attackFrames = 15;
			this.infos[0].expression = "50+lvl*3.5";
			this.infos[0].useAsInteger = true;
			this.hurtFrame = 9;
			this.damageFormula = "15 + lvl * 4";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_HEAVYAXE;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.cooldownFrames = 35;
			this.standDrawHand = false;
			this.level = 12;
			this.specialSkill = new Skill(99);
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.offsetX = -32;
			this.offsetY = 24;
		}
		else if(this.id == 132)
		{
			this.name = Main.lv("Black Panther Hood", "Capuz de Pantera Negra");
			this.trivia = Main.lv("Almost a superhero.", "Quase um super-herói.");
			this.level = 7;
			this.infos[0].expression = "10+lvl/2.8";
			this.infos[1].expression = "4+lvl/1.5";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/3.6";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} lethality\n{G+#2} agility", "{G+#0%} de vida máxima\n{G+#1} de letalidade\n{G+#2} de agilidade");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -4;
			this.offsetY = -4;
			this.leftOffsetX = -4;
			this.woffsetX = -4;
			this.woffsetY = -4;
		}
		else if(this.id == 133)
		{
			this.name = Main.lv("Black Panther Suit", "Traje de Pantera Negra");
			this.trivia = Main.lv("You are still not a king.", "Você continua não sendo um rei.");
			this.level = 7;
			this.infos[0].expression = "10+lvl/2.7";
			this.infos[1].expression = "4+lvl/1.4";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/3.5";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} agility\n{G+#2} lethality", "{G+#0%} de vida máxima\n{G+#1} de agilidade\n{G+#2} de letalidade");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.armorColor = new Color(0.03f, 0.03f, 0.05f, 1f);
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
		}
		else if(this.id == 134)
		{
			this.name = Main.lv("Ogre's Club", "Porrete de Orc");
			this.trivia = "Somebody once told me.";
			this.damageFormula = "10 + lvl * 4";
			this.attackFrames = 10;
			this.hurtFrame = 6;
			this.level = 8;
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_HEAVYCLUB;
			this.overrideStand = Constant.OVERRIDESTAND_FRONT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.offsetX = -20;
			this.offsetY = 20;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 40;
			this.standDrawHand = false;
		}
		else if(this.id == 135)
		{
			this.name = Main.lv("Black Panther Skin", "Pele de Pantera Negra");
			this.trivia = Main.lv("It feels so marvelous.", 
					"O toque é tão maravilhoso.");
			this.level = 7;
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 136)
		{
			this.name = Main.lv("Scarecrow Hat", "Chapéu de Espantalho");
			this.trivia = Main.lv("+100% crow scare rate chance.", "+100% de chance de assustar corvos.");
			this.level = 1;
			this.infos[0].expression = "7+lvl/5";
			this.baseDescription = Main.lv("{G+#0%} max health", "{G+#0%} de vida máxima");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -4;
			this.offsetY = 24;
			this.leftOffsetX = -8;
			this.woffsetX = -4;
			this.woffsetY = 24;
		}
		else if(this.id == 137)
		{
			this.name = Main.lv("Apple", "Maçã");
			this.trivia = Main.lv("iApple for the intimates.", "iApple para os íntimos.");
			this.baseDescription = Main.lv("Use this item to heal yourself by 10 + {R3%} of your max health.", 
					"Use este item para curar-se em 10 + {R3%} de sua vida máxima.");
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_USABLE;
			this.level = 1;
			this.usable = true;
			this.canStack = true;
		}
		else if(this.id == 138)
		{
			this.name = Main.lv("Spider Mask", "Máscara de Aranha");
			this.trivia = Main.lv("Not recommended if you have arachnofobia.", "Não recomendado se você tem aracnofobia.");
			this.level = 12;
			this.infos[0].expression = "10+lvl/2.6";
			this.infos[1].expression = "4+lvl/1.1";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/4";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} intelligence\n{G+#2} lethality", "{G+#0%} de vida máxima\n{G+#1} de inteligência\n{G+#2} de letalidade");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = 0;
			this.offsetY = -4;
			this.leftOffsetX = 0;
			this.woffsetX = 0;
			this.woffsetY = -4;
		}
		else if(this.id == 139)
		{
			this.name = Main.lv("Spider Suit", "Traje de Aranha");
			this.trivia = Main.lv("No webs included.", "Teias não inclusas.");
			this.level = 12;
			this.infos[0].expression = "10+lvl/2.5";
			this.infos[1].expression = "4+lvl/1.2";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/4";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} intelligence\n{G+#2} lethality", "{G+#0%} de vida máxima\n{G+#1} de inteligência\n{G+#2} de letalidade");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = -20;
			this.offsetY = 0;
			this.leftOffsetX = -16;
			this.armorColor = new Color(0.09f, 0.09f, 0.09f, 1f);
			this.woffsetX = -28;
			this.woffsetY = 0;
			this.wleftOffsetX = -24;
			this.walkFrames = 5;
		}
		else if(this.id == 140)
		{
			this.name = Main.lv("Spider Skin", "Pele de Aranha");
			this.trivia = Main.lv("This rude cloth produces smooth webs.", 
					"Este rude tecido produz teias suaves.");
			this.level = 12;
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 141)
		{
			this.name = Main.lv("Reindeer Costume", "Fantasia de Rena");
			this.trivia = Main.lv("The little tail: bloop bloop bloop.", "O rabinho: bloop bloop bloop.");
			this.level = 32;
			this.infos[0].expression = "10+lvl/1.7";
			this.infos[1].expression = "5+lvl*0.85";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/2.2";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} agility\n{G+#2} vitality", "{G+#0%} de vida máxima\n{G+#1} de agilidade\n{G+#2} de vitalidade");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = -8;
			this.offsetY = 0;
			this.leftOffsetX = 0;
			this.armorColor = new Color(0.5f, 0.5f, 0.41f, 1f);
			this.woffsetX = -8;
			this.woffsetY = 0;
		}
		else if(this.id == 142)
		{
			this.name = Main.lv("Reindeer Skin", "Pele de Rena");
			this.trivia = Main.lv("If you get close enough you can hear the bells.", 
					"Se você se aproximar o suficiente você consegue ouvir os sinos.");
			this.level = 30;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 143)
		{
			this.name = Main.lv("Snowman's Hat", "Cartola do Boneco de Neve");
			this.trivia = Main.lv("R.I.P. carrot.", "R.I.P. cenoura.");
			this.level = 32;
			this.infos[0].expression = "10+lvl/2.3";
			this.infos[1].expression = "5+lvl*1.4";
			this.infos[1].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} intelligence", "{G+#0%} de vida máxima\n{G+#1} de inteligência");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -8;
			this.offsetY = 20;
			this.leftOffsetX = -8;
			this.woffsetX = -8;
			this.woffsetY = 20;
		}
		else if(this.id == 144)
		{
			this.name = Main.lv("Christmas Clothing Cloth", "Tecido de Roupa Natalina");
			this.trivia = Main.lv("I didn't know it was such specific.", 
					"Não sabia que era tão específico.");
			this.level = 30;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 145)
		{
			this.name = Main.lv("Christmas Elf Suit", "Traje de Elfo de Natal");
			this.trivia = Main.lv("The world hardest and most vacant job.", "O trabalho mais difícil e mais vago do mundo.");
			this.level = 32;
			this.infos[0].expression = "10+lvl/1.6";
			this.infos[1].expression = "3+lvl*0.9";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/1.3";
			this.infos[2].useAsInteger = true;
			this.infos[3].expression = "lvl/4";
			this.infos[3].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} agility\n{G+#2} luck\n{G+#3} intelligence", "{G+#0%} de vida máxima\n{G+#1} de agilidade\n{G+#2} de sorte\n{G+#3} de inteligência");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = 0;
			this.offsetY = 0;
			this.leftOffsetX = 0;
			this.armorColor = new Color(0.12f, 0.75f, 0.1f, 1f);
			this.woffsetX = 0;
			this.woffsetY = 0;
		}
		else if(this.id == 146)
		{
			this.name = Main.lv("Christmas Hat", "Touca de Natal");
			this.trivia = Main.lv("Jingle bells.", "Jingle bells.");
			this.level = 32;
			this.infos[0].expression = "10+lvl/1.7";
			this.infos[1].expression = "3+lvl*0.95";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/1.25";
			this.infos[2].useAsInteger = true;
			this.infos[3].expression = "lvl/4";
			this.infos[3].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} agility\n{G+#2} luck\n{G+#3} intelligence", "{G+#0%} de vida máxima\n{G+#1} de agilidade\n{G+#2} de sorte\n{G+#3} de inteligência");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -16;
			this.offsetY = 16;
			this.leftOffsetX = 0;
			this.woffsetX = -20;
			this.woffsetY = 16;
			this.walkFrames = 4;
		}
		else if(this.id == 147)
		{
			this.name = Main.lv("Snow", "Neve");
			this.trivia = Main.lv("It would be much easier if you could get it from the ground but the game is not sandbox.", 
					"Seria muito mais fácil se você pudesse obter diretamente do chão mas o jogo não é sandbox.");
			this.level = 30;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 148)
		{
			this.name = Main.lv("Fox Costume", "Fantasia de Raposa");
			this.trivia = Main.lv("Onegaaai >//<.", "Onegaaai >//<.");
			this.level = 32;
			this.infos[0].expression = "10+lvl/1.9";
			this.infos[1].expression = "3+lvl*1.1";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl*0.9";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} intelligence\n{G+#2} lethality", "{G+#0%} de vida máxima\n{G+#1} de inteligência\n{G+#2} de letalidade");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = -8;
			this.offsetY = -4;
			this.leftOffsetX = 0;
			this.woffsetX = -12;
			this.woffsetY = -4;
			this.armorColor = new Color(1f, 0.4f, 0f, 1f);
			this.walkFrames = 5;
		}
		else if(this.id == 149)
		{
			this.name = Main.lv("Fox Ears", "Orelhas de Raposa");
			this.trivia = Main.lv("Senpai UwU.", "Senpai UwU.");
			this.level = 32;
			this.infos[0].expression = "10+lvl/1.9";
			this.infos[1].expression = "3+lvl*1.1";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl*0.9";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} intelligence\n{G+#2} lethality", "{G+#0%} de vida máxima\n{G+#1} de inteligência\n{G+#2} de letalidade");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = 4;
			this.offsetY = 36;
			this.leftOffsetX = 4;
			this.woffsetX = 4;
			this.woffsetY = 36;
		}
		else if(this.id == 150)
		{
			this.name = Main.lv("Fox Hair", "Pelagem de Raposa");
			this.trivia = Main.lv("Japanese references incoming.", 
					"Referências nipônicas se aproximando.");
			this.level = 30;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.canStack = true;
		}
		else if(this.id == 151)
		{
			this.name = Main.lv("Lamp", "Lampião");
			this.trivia = Main.lv("Candle in a shell.", "Vela num casco.");
			this.baseDescription = Main.lv("Use to light a big area around you.", "Use para iluminar uma grande área em sua volta.");
			this.quality = Constant.QUALITY_COMMON;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.itemclass = Constant.ITEMCLASS_ACCESSORY;
			this.leftOffsetX = 3;
			this.offsetX = -3;
			this.offsetY = -2;
			this.woffsetX = -3;
			this.woffsetY = -2;
			this.standFrames = 2;
			this.walkFrames = 2;
			this.level = 1;
			this.usable = true;
			this.infos[0].expression = "0";
		}
		else if(this.id == 152)
		{
			this.name = Main.lv("Scroll of Basic Learning: Pyromancer", "Pergaminho de Conhecimento Básico: Piromante");
			this.trivia = Main.lv("Be maniac like Kil.", "Seja maníaco como Kil.");
			this.baseDescription = Main.lv("Use this scroll to become a {GPyromancer}.",
					"Use este pergaminho para se tornar um {GPiromante}.");
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 153)
		{
			this.name = Main.lv("Sapphire Staff", "Cajado de Safira");
			this.trivia = Main.lv("One of the six rainbow colors...", "Uma das seis cores do arco iris...");
			this.triviaColor = Color.SALMON;
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico");
			this.attackFrames = 10;
			this.hurtFrame = 7;
			this.damageFormula = "10 + lvl * 2";
			this.damageType = Constant.DAMAGETYPE_ENERGY;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 12;
			this.offsetY = 40;
			this.roffsetX = -33;
			this.roffsetY = 29;
			this.cooldownFrames = 30;
			this.level = 10;
			this.shootProj = 100;
			this.shootSpeed = 400;
			this.rotateOnDualEquip = true;
			this.infos[0].expression = "15+lvl";
		}
		else if(this.id == 154)
		{
			this.name = Main.lv("Amethyst Staff", "Cajado de Ametista");
			this.trivia = Main.lv("One of the six rainbow colors...", "Uma das seis cores do arco iris...");
			this.triviaColor = Color.SALMON;
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico");
			this.attackFrames = 10;
			this.hurtFrame = 7;
			this.damageFormula = "10 + lvl * 2";
			this.damageType = Constant.DAMAGETYPE_DARKNESS;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 12;
			this.offsetY = 40;
			this.roffsetX = -33;
			this.roffsetY = 29;
			this.cooldownFrames = 30;
			this.level = 10;
			this.shootProj = 101;
			this.shootSpeed = 400;
			this.rotateOnDualEquip = true;
			this.infos[0].expression = "15+lvl";
		}
		else if(this.id == 155)
		{
			this.name = Main.lv("Emerald Staff", "Cajado de Esmeralda");
			this.trivia = Main.lv("One of the six rainbow colors...", "Uma das seis cores do arco iris...");
			this.triviaColor = Color.SALMON;
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico");
			this.attackFrames = 10;
			this.hurtFrame = 7;
			this.damageFormula = "10 + lvl * 2";
			this.damageType = Constant.DAMAGETYPE_NATURE;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 12;
			this.offsetY = 40;
			this.roffsetX = -33;
			this.roffsetY = 29;
			this.cooldownFrames = 30;
			this.level = 10;
			this.shootProj = 102;
			this.shootSpeed = 400;
			this.rotateOnDualEquip = true;
			this.infos[0].expression = "15+lvl";
		}
		else if(this.id == 156)
		{
			this.name = Main.lv("Ruby Staff", "Cajado de Rubi");
			this.trivia = Main.lv("One of the six rainbow colors...", "Uma das seis cores do arco iris...");
			this.triviaColor = Color.SALMON;
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico");
			this.attackFrames = 10;
			this.hurtFrame = 7;
			this.damageFormula = "10 + lvl * 2";
			this.damageType = Constant.DAMAGETYPE_BLOOD;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 12;
			this.offsetY = 40;
			this.roffsetX = -33;
			this.roffsetY = 29;
			this.cooldownFrames = 30;
			this.level = 10;
			this.shootProj = 103;
			this.shootSpeed = 400;
			this.rotateOnDualEquip = true;
			this.infos[0].expression = "15+lvl";
		}
		else if(this.id == 157)
		{
			this.name = Main.lv("Vanadinite Staff", "Cajado de Vanadinita");
			this.trivia = Main.lv("One of the six rainbow colors...", "Uma das seis cores do arco iris...");
			this.triviaColor = Color.SALMON;
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico");
			this.attackFrames = 10;
			this.hurtFrame = 7;
			this.damageFormula = "10 + lvl * 2";
			this.damageType = Constant.DAMAGETYPE_FIRE;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 12;
			this.offsetY = 40;
			this.roffsetX = -33;
			this.roffsetY = 29;
			this.cooldownFrames = 30;
			this.level = 10;
			this.shootProj = 104;
			this.shootSpeed = 400;
			this.rotateOnDualEquip = true;
			this.infos[0].expression = "15+lvl";
		}
		else if(this.id == 158)
		{
			this.name = Main.lv("Diamond Staff", "Cajado de Diamante");
			this.trivia = Main.lv("One of the six rainbow colors...", "Uma das seis cores do arco iris...");
			this.triviaColor = Color.SALMON;
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico");
			this.attackFrames = 10;
			this.hurtFrame = 7;
			this.damageFormula = "10 + lvl * 2";
			this.damageType = Constant.DAMAGETYPE_ICE;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 12;
			this.offsetY = 40;
			this.roffsetX = -33;
			this.roffsetY = 29;
			this.cooldownFrames = 30;
			this.level = 10;
			this.shootProj = 105;
			this.shootSpeed = 400;
			this.rotateOnDualEquip = true;
			this.infos[0].expression = "15+lvl";
		}
		else if(this.id == 159)
		{
			this.name = Main.lv("Rainbow Staff", "Cajado Arco-íris");
			this.trivia = Main.lv("Do you believe in magic?", "Você acredita em magia?");
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico");
			this.attackFrames = 10;
			this.hurtFrame = 7;
			this.damageFormula = "12 + lvl * 3.2";
			this.damageType = Constant.DAMAGETYPE_ICE;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 18;
			this.offsetY = 51;
			this.roffsetX = -28;
			this.roffsetY = 33;
			this.cooldownFrames = 30;
			this.level = 20;
			this.shootProj = 105;
			this.shootSpeed = 400;
			this.rotateOnDualEquip = true;
			this.infos[0].expression = "25+lvl*2";
		}
		else if(this.id == 160)
		{
			this.name = Main.lv("Fire Wood Staff", "Cajado de Madeira de Fogo");
			this.trivia = "Mans not hot!";
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico");
			this.attackFrames = 10;
			this.standFrames = 1;
			this.hurtFrame = 8;
			this.damageFormula = "3 + lvl * 1";
			this.damageType = Constant.DAMAGETYPE_FIRE;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = 12;
			this.offsetY = 23;
			this.roffsetX = -7;
			this.roffsetY = 18;
			this.cooldownFrames = 30;
			this.level = 1;
			this.shootProj = 106;
			this.shootSpeed = 1000;
			this.shootAccuracy = 5f;
			this.rotateOnDualEquip = true;
			this.spawnProjectileOffset = new Vector2(100f, 0f);
			this.infos[0].expression = "5+lvl*0.7";
		}
		else if(this.id == 161)
		{
			this.name = Main.lv("Apprentice Robe", "Túnica do Aprendiz");
			this.trivia = Main.lv("Literally a clothing who says you are a newbie.", "Literalmente uma roupa que diz que você é um novato.");
			this.infos[0].expression = "5+lvl/2";
			this.infos[1].expression = "3+lvl/3";
			this.infos[1].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} intelligence", "{G+#0%} de vida máxima\n{G+#1} de inteligência");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
			this.level = 1;
			this.armorColor = new Color(0.85f, 0.85f, 0.85f, 1f);
			this.dyeArmorColor = false;
		}
		else if(this.id == 162)
		{
			this.name = Main.lv("Blue Orb Staff", "Cajado do Orbe Azul");
			this.trivia = Main.lv("You didn't expect this name.", "Você não esperava por esse nome.");
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico");
			this.attackFrames = 10;
			this.hurtFrame = 4;
			this.damageFormula = "10 + lvl * 2";
			this.damageType = Constant.DAMAGETYPE_ENERGY;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGROTATE;
			this.dontAttack = true;
			this.canFlipAttack = true;
			this.offsetX = 9;
			this.offsetY = 42;
			this.roffsetX = -33;
			this.roffsetY = 34;
			this.cooldownFrames = 35;
			this.level = 1;
			this.shootProj = 100;
			this.shootSpeed = 400;
			this.rotateOnDualEquip = true;
			this.infos[0].expression = "15+lvl";
			this.spawnProjectileOffset = new Vector2(80, 0f);
		}
		else if(this.id == 163)
		{
			this.name = Main.lv("Tin Pocketsword", "Espada de Bolso de Estanho");
			this.trivia = Main.lv("Use it to cut trees, stones and other natural things.", "Use para cortar árvores, pedras, dentre outras coisas naturais.");
			this.triviaColor = Color.LIME;
			this.attackFrames = 9;
			this.hurtFrame = 5;
			this.damageFormula = "5 + lvl";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_SHORTSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.offsetX = 10;
			this.offsetY = 9;
			this.cooldownFrames = 25;
			this.level = 1;
		}
		else if(this.id == 164)
		{
			this.name = Main.lv("Blood Stealer", "Ladrão de Sangue");
			this.trivia = Main.lv("Kyuushuu, the Absorption.", "Kyuushuu, a Absorção.");
			this.baseDescription = Main.lv("-=Blood Stealer:=- Regenerates {G#0} health whenever you kill an enemy.", 
					"-=Blood Stealer:=- Regenera {O#0} de vida sempre que você matar um inimigo.");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_SPECIAL;
			this.itemclass = Constant.ITEMCLASS_PASSIVE;
			this.level = 1;
			this.infos[0].expression = "5+lvl*1.5";
			this.infos[0].useAsInteger = true;
		}
		else if(this.id == 165)
		{
			this.name = Main.lv("Electric Current", "Corrente Elétrica");
			this.trivia = Main.lv("Tsuuden, the Energized", "Tsuuden, o Energizado");
			this.baseDescription = Main.lv("-=Electric Current:=- Increases mana regeneration by {G+#0%} for 10 seconds whenever you kill an enemy. (Max. 5 stacks)", 
					"-=Electric Current:=- Aumenta a regeneração de mana em {G+#0%} por 10 segundos sempre que você matar um inimigo. (Máx. 5 acúmulos)");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_SPECIAL;
			this.itemclass = Constant.ITEMCLASS_PASSIVE;
			this.level = 1;
			this.infos[0].expression = "20+lvl*0.8";
		}
		else if(this.id == 166)
		{
			this.name = Main.lv("Power Converter", "Conversor de Poder");
			this.trivia = Main.lv("Koukan, the Exchange", "Koukan, o Câmbio");
			this.baseDescription = Main.lv("-=Power Converter:=- Inverts your base physical power and base magic power.", 
					"-=Power Converter:=- Inverte seu poder físico base e seu poder mágico base.");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_SPECIAL;
			this.itemclass = Constant.ITEMCLASS_PASSIVE;
			this.level = 1;
		}
		else if(this.id == 167)
		{
			this.name = Main.lv("Scroll of Basic Learning: Reaper", "Pergaminho de Conhecimento Básico: Ceifador");
			this.trivia = Main.lv("Be deadly as Lushmael.", "Seja mortal como Lushmael.");
			this.baseDescription = Main.lv("Use this scroll to become a {GReaper}.",
					"Use este pergaminho para se tornar um {GCeifador}.");
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_SCROLL;
			this.level = 1;
			this.usable = true;
		}
		else if(this.id == 168)
		{
			this.name = Main.lv("Ectoplasm", "Ectoplasma");
			this.trivia = Main.lv("Woooooo!!!", "Uuuuuuu!!!");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 25;
			this.canStack = true;
		}
		else if(this.id == 169)
		{
			this.name = Main.lv("Soul Blade", "Lâmina Espiritual");
			this.trivia = Main.lv("Sliced up, b e a u t i f u l l y!", "Dilacerado, l i n d a m e n t e.");
			this.quality = Constant.QUALITY_EPIC;
			this.itemclass = Constant.ITEMCLASS_LONGSWORD;
			this.level = 64;
			this.damageFormula = "20 + lvl * 6";
			this.hurtFrame = 1;
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.type = Constant.ITEMTYPE_LEFT;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = false;
			this.offsetX = 72;
			this.offsetY = 13;
			this.woffsetX = 72;
			this.woffsetY = 13;
			this.dualHanded = true;
			this.dualHandedAttack = true;
			this.standDrawHand = false;
			this.cooldownFrames = 25;
			this.attackFrames = 5;
			this.extraAnimations.put(35, 34);
			this.specialSkill = new Skill(123);
			this.canGoBack = false;
			this.attackSounds.put(1, DJ.SLASH);
			this.attackSoundPitch = 1f;
		}
		else if(this.id == 170)
		{
			
		}
		else if(this.id == 171)
		{
			this.name = Main.lv("Makeshift Campfire", "Fogueira Improvisada");
			this.trivia = Main.lv("Scouting for survival.", "Escotismo para a sobrevivência.");
			this.baseDescription = Main.lv("Creates a 30 seconds long campfire in the user position.", "Cria uma fogueira que dura 30 segundos na posição do usuário.");
			this.quality = Constant.QUALITY_COMMON;
			this.itemclass = Constant.ITEMCLASS_USABLE;
			this.level = 10;
			this.usable = true;
		}
		else if(this.id == 172)
		{
			this.name = Main.lv("Demon Mark", "Marca Demoníaca");
			this.trivia = Main.lv("'You feel your sins crawling on your head'", "'Você sente seus pecados rastejando sobre seu rosto'");
			this.quality = Constant.QUALITY_IMAGINARY;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.level = 100;
			this.offsetX = 0;
			this.offsetY = -4;
			this.woffsetX = 0;
			this.woffsetY = -4;
			
			this.customShader = Content.maskIncS;
			int width = Content.nebula.getWidth();
			int height = Content.nebula.getHeight();
			this.customShaderUValues.put("u_offset", new String[] {"(((ed*ex*0.011 % sw)/(sw/2)) + (ticks*0.0005)) - floor((((ed*ex*0.011 % sw)/(sw/2)) + (ticks*0.0005)))", "(((ed*ey*0.011 % sh)/(sh/2)) + (ticks*0.00025)) - floor((((ed*ey*0.011 % sh)/(sh/2)) + (ticks*0.00025)))"});
			this.customShaderUValues.put("u_maskScale", new String[] {width+"/sw", height+"/sh"});
			this.customShaderUValues.put("u_frames", new String[] {"1"});
			this.customShaderUTextures.put(1, Content.extras[38]);
		}
		else if(this.id == 173)
		{
			this.name = Main.lv("Demon Mark", "Marca Demoníaca");
			this.trivia = Main.lv("'You feel your sins crawling on your skin'", "'Você sente seus pecados rastejando sobre sua pele'");
			this.quality = Constant.QUALITY_IMAGINARY;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.level = 100;
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
			
			this.customShader = Content.maskIncS;
			int width = Content.nebula.getWidth();
			int height = Content.nebula.getHeight();
			this.customShaderUValues.put("u_offset", new String[] {"(((ed*ex*0.011 % sw)/(sw/2)) + (ticks*0.0005)) - floor((((ed*ex*0.011 % sw)/(sw/2)) + (ticks*0.0005)))", "(((ed*ey*0.011 % sh)/(sh/2)) + (ticks*0.00025)) - floor((((ed*ey*0.011 % sh)/(sh/2)) + (ticks*0.00025)))"});
			this.customShaderUValues.put("u_maskScale", new String[] {width+"/sw", height+"/sh"});
			this.customShaderUValues.put("u_frames", new String[] {"1"});
			this.customShaderUTextures.put(1, Content.extras[38]);
		}
		else if(this.id == 174)
		{
			this.name = Main.lv("Magic Essence", "Essência Mágica");
			this.trivia = Main.lv("A pretty shimmering dust.", "Uma bela poeira cintilante.");
			this.quality = Constant.QUALITY_UNCOMMON;
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 1;
			this.canStack = true;
		}
		else if(this.id == 175)
		{
			this.name = Main.lv("Ghost Aura", "Aura Fantasma");
			this.trivia = Main.lv("Reiki, the Spirituality", "Reiki, a Espiritualidade");
			this.baseDescription = Main.lv("Creates a lighting aura around the user whenever he is in a dark spot.", 
					"Cria uma aura luminosa em volta do usuário sempre que estiver em um lugar escuro.");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_SPECIAL;
			this.itemclass = Constant.ITEMCLASS_PASSIVE;
			this.level = 15;
		}
		else if(this.id == 176)
		{
			this.name = Main.lv("Overreality", "Sobrerrealidade");
			this.trivia = Main.lv("He still follows you.", "Ele ainda te acompanha.");
			this.baseDescription = Main.lv("-=Unreal:=- A ghost arrow is shot 0.5s after the first shoot.\n"
					+ "No arrow collision.", "-=Surreal:=- Uma flecha fantasma é atirada 0.5s após o primeiro disparo.\n"
							+ "Flechas não possuem colisão.");
			this.attackFrames = 7;
			this.hurtFrame = 5;
			this.damageFormula = "7 + lvl * 2";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.rotateAfterAttack = true;
			this.dontAttack = true;
			this.dualHandedAttack = true;
			this.canFlipAttack = false;
			this.offsetX = -8;
			this.offsetY = -2;
			this.cooldownFrames = 30;
			this.level = 15;
			this.shootProj = 116;
			this.shootSpeed = 2000;
			this.shootAccuracy = 5f;
			this.canGoBack = false;
			this.specialSkill = new Skill(124);
		}
		else if(this.id == 177)
		{
			this.name = Main.lv("Ethereal Refraction", "Refração Etérea");
			this.trivia = Main.lv("She still follows you.", "Ela ainda te acompanha.");
			this.damageFormula = "13 + lvl * 4";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.attackFrames = 10;
			this.hurtFrame = 5;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_BROADSWORD;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGSWING;
			this.canFlipAttack = true;
			this.offsetX = 16;
			this.offsetY = 20;
			this.dualHandedAttack = true;
			this.specialSkill = new Skill(125);
			this.cooldownFrames = 30;
			this.level = 15;
		}
		else if(this.id == 178)
		{
			this.name = Main.lv("Slime Wand", "Varinha de Slime");
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power\n\n-=When equipped on secondary hand:=-\n{G+#1%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico\n\n-=Quando equipado na mão secundária:=-\n{G+#1%} de poder mágico");
			this.trivia = Main.lv("Be aware: this is not marshmallow.", "Esteja avisado: isso não é marshmallow");
			this.infos[0].expression = "7 + lvl * 0.8";
			this.infos[1].expression = "5 + lvl * 0.4";
			this.attackFrames = 6;
			this.hurtFrame = 3;
			this.damageFormula = "4 + lvl * 1.1";
			this.type = Constant.ITEMTYPE_LEFT;
			this.damageType = Constant.DAMAGETYPE_POISON;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.overrideAttack = Constant.OVERRIDEATTACK_BIGROTATE;
			this.dontAttack = true;
			this.canFlipAttack = true;
			this.offsetX = 16;
			this.offsetY = 8;
			this.cooldownFrames = 25;
			this.level = 5;
			this.shootProj = 119;
			this.shootSpeed = 1200;
			this.shootAccuracy = 5f;
			this.spawnProjGunMode = true;
		}
		else if(this.id == 179)
		{
			this.name = Main.lv("Fairy Orb", "Orbe de Fada");
			this.baseDescription = Main.lv("{G+#0%} magic power\n{G+#1%} cooldown reduction", "{G+#0%} de poder mágico\n{G+#1%} de redução de tempo de recarga");
			this.trivia = Main.lv("There is a fairy inside this orb.", "Existe uma fada dentro desse orbe.");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_RIGHT;
			this.itemclass = Constant.ITEMCLASS_ORB;
			this.overrideStand = Constant.OVERRIDESTAND_ORB;
			this.level = 8;
			this.canGoBack = false;
			this.offsetX = -28;
			this.offsetY = -20;
			this.infos[0].expression = "10 + lvl * 1.6";
			this.infos[1].expression = "15+lvl*0.1";
		}
		else if(this.id == 180)
		{
			this.name = Main.lv("Macaw Mask", "Máscara de Arara");
			this.trivia = Main.lv("The not-so-black plague.", "A peste não-tão-negra.");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_HEAD;
			this.itemclass = Constant.ITEMCLASS_HAT;
			this.overrideStand = Constant.OVERRIDESTAND_HEAD;
			this.offsetX = -8;
			this.offsetY = -8;
			this.leftOffsetX = -16;
			this.woffsetX = -8;
			this.woffsetY = -8;
			this.infos[0].expression = "10+lvl/2.8";
			this.infos[1].expression = "4+lvl/1.5";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/3.6";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} agility\n{G+#2} lethality", "{G+#0%} de vida máxima\n{G+#1} de agilidade\n{G+#2} de letalidade");
			this.level = 6;
		}
		else if(this.id == 181)
		{
			this.name = Main.lv("Macaw Costume", "Fantasia de Arara");
			this.trivia = Main.lv("CO-COW!", "CO-CÓ!");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_BODY;
			this.itemclass = Constant.ITEMCLASS_BODYGUARD;
			this.overrideStand = Constant.OVERRIDESTAND_BODY;
			this.offsetX = 0;
			this.offsetY = 0;
			this.woffsetX = 0;
			this.woffsetY = 0;
			this.leftOffsetX = 0;
			this.level = 6;
			this.infos[0].expression = "10+lvl/2.5";
			this.infos[1].expression = "4+lvl/1.4";
			this.infos[1].useAsInteger = true;
			this.infos[2].expression = "lvl/3.2";
			this.infos[2].useAsInteger = true;
			this.baseDescription = Main.lv("{G+#0%} max health\n{G+#1} agility\n{G+#2} lethality", "{G+#0%} de vida máxima\n{G+#1} de agilidade\n{G+#2} de letalidade");
			this.armorColor = new Color(0f, 0f, 1f, 1f);
			this.dyeArmorColor = false;
		}
		else if(this.id == 182)
		{
			this.name = Main.lv("Makbow", "Ararco");
			this.trivia = Main.lv("Look, I am your feather!", "Ora, é uma pena!");
			this.baseDescription = Main.lv("-=Air Strike=-: Critical hit increased by {G+#0%} for arrows shot in the air.",
											"-=Ataque Aéreo=-: Chance de crítico aumentada em {G+#0%} para flechas atiradas no ar.");
			this.attackFrames = 6;
			this.hurtFrame = 4;
			this.damageFormula = "6 + (lvl * 1.5)";
			this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_LEFT;
			this.itemclass = Constant.ITEMCLASS_RANGED;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = false;
			this.offsetX = -4;
			this.offsetY = -4;
			this.debugging = true;
			this.dualHandedAttack = true;
			this.cooldownFrames = 28;
			this.level = 8;
			this.shootProj = 120;
			this.shootSpeed = 1500;
			this.rotateAfterAttack = true;
			this.infos[0].expression = "20 + lvl";
		}
		else if(this.id == 183)
		{
			this.name = Main.lv("Horned Shield", "Escudo Com Chifres");
			this.trivia = Main.lv("Moooo!", "Muuuuu!");
			this.baseDescription = Main.lv("{G+#0%} max health\n-=Horned:=- Reflects {R#1} damage when struck.",
					"{G+#0%} de vida máxima\n-=Chifrudo:=- Reflete {R#1} de dano ao sofrer contato.");
			this.infos[0].expression = "lvl*2";
			this.infos[1].expression = "lvl*3";
			this.quality = Constant.QUALITY_UNCOMMON;
			this.type = Constant.ITEMTYPE_RIGHT;
			this.itemclass = Constant.ITEMCLASS_SHIELD;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.offsetX = -3;
			this.offsetY = -6;
			this.level = 22;
			this.canGoBack = false;
		}
		else if(this.id == 184)
		{
			this.name = Main.lv("Shaman Wand", "Varinha do Xamã");
			this.baseDescription = Main.lv("-=When equipped on main hand:=-\n{G+#0%} magic power\n\n-=When equipped on secondary hand:=-\n{G+#1%} magic power", "-=Quando equipado na mão principal:=-\n{G+#0%} de poder mágico\n\n-=Quando equipado na mão secundária:=-\n{G+#1%} de poder mágico");
			this.trivia = Main.lv("Somewhat obvious and unimaginable.", "Um tanto quanto óbvio e inimaginável.");
			this.attackFrames = 4;
			this.hurtFrame = 0;
			this.damageFormula = "10 + lvl * 2.6";
			this.type = Constant.ITEMTYPE_LEFT;
			this.damageType = Constant.DAMAGETYPE_DEATH;
			this.itemclass = Constant.ITEMCLASS_MAGIC;
			this.overrideStand = Constant.OVERRIDESTAND_HILT;
			this.quality = Constant.QUALITY_UNCOMMON;
			this.overrideAttack = Constant.OVERRIDEATTACK_ROTATE;
			this.dontAttack = true;
			this.canFlipAttack = true;
			this.offsetX = 20;
			this.offsetY = 12;
			this.cooldownFrames = 25;
			this.level = 22;
			this.shootProj = 122;
			this.shootSpeed = 1500;
			this.shootAccuracy = 5f;
			this.spawnProjGunMode = true;
			this.infos[0].expression = "18 + lvl * 1.7";
			this.infos[1].expression = "12 + lvl * 1.1";
			this.offsetRotateAttack = new Vector2(-12, 0f);
		}
		else if(this.id == 185)
		{
			this.name = Main.lv("Fairy Dust", "Pó de Fada");
			this.trivia = Main.lv("You hear little bells.", "Você escuta sininhos.");
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 8;
			this.canStack = true;
		}
		else if(this.id == 186)
		{
			this.name = Main.lv("Macaw Feathers", "Penas de Arara");
			this.trivia = Main.lv("I can't believe you are contributing to the macaw extinction.",
					"Não acredito que você está contribuindo para a extinção das araras.");
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 8;
			this.canStack = true;
		}
		else if(this.id == 187)
		{
			this.name = Main.lv("Macaw Beak", "Bico de Arara");
			this.trivia = Main.lv("Co-cow.",
					"Co-có.");
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 8;
			this.canStack = true;
		}
		else if(this.id == 188)
		{
			this.name = Main.lv("Minotaur Horn", "Chifre de Minotauro");
			this.trivia = Main.lv("Horned humanoids?",
					"Humanóides chifrudos?");
			this.itemclass = Constant.ITEMCLASS_MATERIAL;
			this.level = 13;
			this.canStack = true;
		}
		else if(this.id == 189)
		{
			this.name = Main.lv("Dimensional Shell", "Casca Dimensional");
			this.trivia = Main.lv("She still follows you.", "Ela ainda te acompanha.");
			this.baseDescription = Main.lv("-=Dimensional Protection:=- Receives a shield of {G#0} for 5 seconds after being hurt below 25% health. (Cooldown: 150s)",
					"-=Proteção Dimensional:=- Recebe um escudo de {G#0} por 5 segundos depois de ser atacado com vida inferior a 25%. (Tempo de recarga: 150s)");
			this.quality = Constant.QUALITY_COMMON;
			this.type = Constant.ITEMTYPE_RIGHT;
			this.itemclass = Constant.ITEMCLASS_SHIELD;
			this.overrideStand = Constant.OVERRIDESTAND_MIDDLE;
			this.offsetX = -1;
			this.offsetY = -7;
			this.canGoBack = false;
			this.level = 15;
			this.infos[0].expression = "100 + lvl * 12";
		}


		// End Item
		/* 	
		 *	public int id;
			public String name;
			public String trivia;
			public Color triviaColor;
			public String baseDescription;
			public Color descriptionColor;
			public int attackFrames;
			public int standFrames;
			public int hurtFrame;
			public int walkFrames; // hats/bodies only
			public String damageFormula;
			public int damageType;
			public int quality;
			public int type;
			public int itemclass;
			public int overrideStand;
			public int overrideAttack;
			public boolean dontAttack;
			public boolean canFlipAttack;
			public float offsetX;
			public float offsetY;
			public float roffsetX;
			public float roffsetY;
			public float woffsetX;
			public float woffsetY;
			public Vector2 offsetRotateAttack;
			public boolean dualHanded;
			public boolean dualHandedAttack;
			public Skill specialSkill;
			public int cooldownFrames;
			public boolean standDrawHand;
			public boolean canStack;
			public int level;
			public boolean rotateOnDualEquip;
			public boolean rotateAfterAttack;
			public int shootProj;
			public float shootSpeed;
			public float shootAccuracy; // maximum degrees variation
			public boolean spawnProjGunMode;
			public boolean usable;
			public ArrayList<Integer> extraHits;
			public Set partOfSet;
			public MathEx infos[10];
		 */

		this.spriteWidth = this.getAttackTexture().getWidth();
		this.spriteHeight = this.getAttackTexture().getHeight()/this.attackFrames;
		this.spriteHoldWidth = this.getTexture().getWidth();
		this.spriteHoldHeight = this.getTexture().getHeight();
		if(this.wleftOffsetX == -1)
			this.wleftOffsetX = this.leftOffsetX;
		this.updateDamage();
		this.updateDescription();
		return this;
	}


	public void updateEquip(Player player, int equipSlot)
	{
		if(this.id == 9 && Main.ticksToSeconds(player.lastHurtTicks()) <= 5f)
		{
			player.criticalChance += this.getInfoValueI(0);
		}
		else if(this.id == 11)
		{
			player.eagility += 200;
			player.elethality += 300;
			player.lifeSteal += 0.1f;
		}
		else if(this.id == 16)
		{
			player.eagility += this.getInfoValueI(0);
		}
		else if(this.id == 18)
		{
			player.estrength += this.getInfoValueI(0);
			player.maxHealthPerc += this.getInfoValueI(1) / 100f;
		}
		else if(this.id == 20)
		{
			int info0 = this.getInfoValueI(0);
			player.maxHealthPerc += info0/100f;
			player.elethality += info0;
			player.maxSpeed += this.getInfoValueI(1);
		}
		else if(this.id == 24)
		{
			player.attackSpeed += 1.975f;
		}
		else if(this.id == 28)
		{
			player.lifeSteal += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 29)
		{
			player.lifeSteal += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 31)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 43)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 48)
		{
			player.lifeSteal += 1f;
		}
		else if(this.id == 50)
		{
			int info1 = this.getInfoValueI(1);
			player.critMult += this.getInfoValueF(0)/100f;
			player.criticalChance -= info1;
			player.evitality -= info1;
			player.eagility += info1;
		}
		else if(this.id == 51)
		{
			int info0 = this.getInfoValueI(0);
			player.criticalChance += info0;
			player.evitality += info0;
			player.eagility += this.getInfoValueI(1);
		}
		else if(this.id == 61)
		{
			float info0 = this.getInfoValueF(0);
			int info1 = this.getInfoValueI(1);
			player.eagility += info1;
			player.maxHealthPerc += (info0)/100f;
		}
		else if(this.id == 62)
		{
			float info0 = this.getInfoValueF(0);
			int info1 = this.getInfoValueI(1);
			player.eagility += info1;
			player.maxHealthPerc += (info0)/100f;
		}
		else if(this.id == 66)
		{
			player.eluck += this.getInfoValueI(0);
			player.maxHealthPerc += this.getInfoValueF(1)/100f;
		}
		else if(this.id == 67)
		{
			player.evitality += this.getInfoValueI(0);
			player.maxHealthPerc += this.getInfoValueF(1)/100f;
		}
		else if(this.id == 68)
		{
			player.evitality += this.getInfoValueI(0);
			player.maxHealthPerc += this.getInfoValueF(1)/100f;
		}
		else if(this.id == 69)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 70)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 84)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.estrength += this.getInfoValueI(1);
		}
		else if(this.id == 85)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.estrength += this.getInfoValueI(1);
		}
		else if(this.id == 96)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 97)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 100)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 122)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eagility += this.getInfoValueI(1);
			player.evitality += this.getInfoValueI(2);
		}
		else if(this.id == 124)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.estrength += this.getInfoValueI(1);
			player.evitality += this.getInfoValueI(2);
		}
		else if(this.id == 128)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.estrength += this.getInfoValueI(1);
			player.evitality += this.getInfoValueI(2);
		}
		else if(this.id == 129)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.estrength += this.getInfoValueI(1);
			player.evitality += this.getInfoValueI(2);
		}
		else if(this.id == 131)
		{
			player.extraPPperc += (this.getInfoValueF(0)/100f)*player.getLostHealth(true);
		}
		else if(this.id == 132)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.elethality += this.getInfoValueI(1);
			player.eagility += this.getInfoValueI(2);
		}
		else if(this.id == 133)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eagility += this.getInfoValueI(2);
			player.elethality += this.getInfoValueI(1);
		}
		else if(this.id == 136)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 138)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eintelligence += this.getInfoValueI(1);
			player.elethality += this.getInfoValueI(2);
		}
		else if(this.id == 139)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eintelligence += this.getInfoValueI(1);
			player.elethality += this.getInfoValueI(2);
		}
		else if(this.id == 141)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eagility += this.getInfoValueI(1);
			player.evitality += this.getInfoValueI(2);
		}
		else if(this.id == 143)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eintelligence += this.getInfoValueI(1);
		}
		else if(this.id == 145)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eagility += this.getInfoValueI(1);
			player.eluck += this.getInfoValueI(2);
			player.eintelligence += this.getInfoValueI(3);
		}
		else if(this.id == 146)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eagility += this.getInfoValueI(1);
			player.eluck += this.getInfoValueI(2);
			player.eintelligence += this.getInfoValueI(3);
		}
		else if(this.id == 148)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eintelligence += this.getInfoValueI(1);
			player.elethality += this.getInfoValueI(2);
		}
		else if(this.id == 149)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.eintelligence += this.getInfoValueI(1);
			player.elethality += this.getInfoValueI(2);
		}
		else if(this.id == 151)
		{
			if(this.infos[0].expression.equalsIgnoreCase("1"))
			{
				Lighting l = Lighting.Create(player.Center().add(MathUtils.random(-16, 16), MathUtils.random(-16, 16)), 1024, new Color(1f, 0.8f, 0.2f, 1f), 0.5f, true);
				l.vibrancy = 64;
				l.power = 3;
			}
		}
		else if(this.id == 153 && equipSlot == Constant.ITEMSLOT_LEFT)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 154 && equipSlot == Constant.ITEMSLOT_LEFT)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 155 && equipSlot == Constant.ITEMSLOT_LEFT)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 156 && equipSlot == Constant.ITEMSLOT_LEFT)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 157 && equipSlot == Constant.ITEMSLOT_LEFT)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 158 && equipSlot == Constant.ITEMSLOT_LEFT)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 159 && equipSlot == Constant.ITEMSLOT_LEFT)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 160 && equipSlot == Constant.ITEMSLOT_LEFT)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 161)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100;
			player.eintelligence += this.getInfoValueI(1);
		}
		else if(this.id == 162 && equipSlot == Constant.ITEMSLOT_LEFT)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 172 || this.id == 173)
		{
			player.extraMPperc += 0.5f;
			player.extraPPperc += 0.5f;
			player.manaRegen += 10f;
			player.healthRegen += 100f;
			player.estrength += 450;
			player.eintelligence += 450;
			player.elethality += 50;
			player.eagility += 50;
			player.evitality += 50;
			player.eluck += 50;
			player.cooldownReduction *= 0.8f;
		}
		else if(this.id == 175)
		{
			GameMap map = Constant.tryGetMapForEntity(player);
			if(map != null)
			{
				int x = (int)Main.clamp(0, player.Center().x/64, map.width-1);
				int y = (int)Main.clamp(0, player.Center().y/64, map.height-1);
				if(map.lightMap[y][x] < 1f || !Main.isAtDay())
				{
					Lighting.Create(player.Center(), 512, new Color(0.92f, 0.97f, 0.82f, 1f), 1f, 0.8f);
				}
			}
		}
		else if(this.id == 178)
		{
			player.extraMPperc += this.getInfoValueF(equipSlot == Constant.ITEMSLOT_LEFT ? 0 : 1)/100f;
		}
		else if(this.id == 179)
		{
			player.extraMPperc += this.getInfoValueF(0)/100f;
			player.cooldownReduction *= 1f-(this.getInfoValueF(1)/100f);
		}
		else if(this.id == 180)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.elethality += this.getInfoValueI(2);
			player.eagility += this.getInfoValueI(1);
		}
		else if(this.id == 181)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
			player.elethality += this.getInfoValueI(2);
			player.eagility += this.getInfoValueI(1);
		}
		else if(this.id == 183)
		{
			player.maxHealthPerc += this.getInfoValueF(0)/100f;
		}
		else if(this.id == 184)
		{
			player.extraMPperc += this.getInfoValueF(equipSlot == Constant.ITEMSLOT_LEFT ? 0 : 1)/100f;
		}
	}

	public void updateVanity(Player player)
	{
		if(this.id == 11)
		{
			for(int i = 0;i < 36;i++)
			{
				float sin = (float) Math.sin(Constant.gameTick()/4+i*10*Math.PI/180);
				float cos = (float) Math.cos(Constant.gameTick()/4+i*10*Math.PI/180);
				Particle p = Particle.Create(new Vector2(player.Center().x, player.position.y+player.height), new Vector2(cos * 100f, sin * 100f), 2, new Color(0f, 0.5f + MathUtils.random()/2, 0f, 1f), 0f, 0.4f, 0.5f+MathUtils.random());
				p.parent = player;
				p.rotation = Constant.gameTick() * 4 % 360;
				p.drawBack = true;
			}
		}
	}

	public void draw(SpriteBatch batch, Player player, int hand)
	{
		this.holder = player;
		if(player.isUntargetable())
		{
			return;
		}
		if(this.debugging)
		{
			this.offsetX = Main.var[6];
			this.offsetY = Main.var[7];
			this.roffsetX = Main.var[8];
			this.roffsetY = Main.var[9];
			this.woffsetX = Main.var[6];
			this.woffsetY = Main.var[7];
			this.leftOffsetX = Main.var[8];
			this.wleftOffsetX = Main.var[8];
		}
		boolean drawLeft = true;
		if((!player.isWearingShield() && this.type == Constant.ITEMTYPE_LEFT) || (player.isWearingShield() && this.type == Constant.ITEMTYPE_RIGHT))
		{
			if(player.customAnim != null)
			{
				drawLeft = false;
			}
		}
		Color darkColor = new Color(0.8f, 0.8f, 0.8f, 1f);

		Vector2 handPos = player.getHandPosition();
		if(hand == 1)
			handPos = player.getBackHandPosition();
		
		Sprite sprite = null;
		if(this.canGoBack && player.timeSinceLastAttack >= Constant.BACKWEAPON_TIME && this.itemclass != Constant.ITEMCLASS_MATERIAL && this.itemclass != Constant.ITEMCLASS_PASSIVE)
		{
			sprite = new Sprite(this.getWorldTexture());
			int width = (int) sprite.getWidth();
			int height = (int) (sprite.getHeight()/this.standFrames);
			sprite.setRegion(0, ((Constant.gameTick()/3 % this.standFrames) * height), width, height);
			sprite.setSize(width, height);
			sprite.setPosition(player.Center().x-width/2f, player.Center().y-height/2f+player.getYAnimationOffset());
			sprite.flip(false, true);
			sprite.flip(player.direction == -1, false);
			sprite.flip(hand == 1, false);
			if(player.getAnim() == Constant.ANIMATION_ROLL)
			{
				int tick = player.lastResetTicks();
				float rotation = tick * -16;
				if (player.direction == Constant.DIRECTION_LEFT)
					rotation *= -1f;
				
				sprite.setOriginCenter();
				sprite.setRotation(rotation);
			}
			this.drawEnt(batch, sprite);
			this.afterDraw(sprite, player, batch, hand);
		}
		else
		{
		if(hand == 0 && player.customAnim != null)
			return;
		
		if(hand == 1 && player.customAnim2 != null)
			return;
		
		if((this.type == Constant.ITEMTYPE_LEFT || this.type == Constant.ITEMTYPE_RIGHT) && !player.dead && player.getAnim() != Constant.ANIMATION_DANCE && player.getAnim() != Constant.ANIMATION_INVENTORY && player.getAnim() != Constant.ANIMATION_ROLL && drawLeft)
		{
			boolean attacking = !player.isWearingShield() ? player.attacking : player.attacking2;
			if(hand == 1)
				attacking = !player.isWearingShield() ? player.attacking2 : player.attacking;
			

			if(player.getAnim() != Constant.ANIMATION_ATTACKING || !attacking)
			{
				boolean dualSame = (player.getLeft() != null && player.getRight() != null && player.getLeft().id == player.getRight().id);
				if(player.timeSinceLastAttack < 2 && this.rotateAfterAttack)
				{
					
					sprite = new Sprite(this.getAttackTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getAttackFrames());

					float x = player.Center().x;
					float y = player.Center().y - height/2;
					int animFrame = this.attackFrames-1;

					sprite.setRegion(0, (animFrame * height), width, height);
					sprite.setSize(width, height);
					sprite.setOrigin(0, height/2);
					
					Vector2 dif = player.mousePos.cpy().sub(player.Center());
					int cDir = (dif.x < 0 ? -1 : 1);
					
					Vector2 cpos = new Vector2(dif.x, dif.y);
					sprite.setRotation((float)Math.toDegrees((Math.atan2(cpos.y, cpos.x))));
					Vector2 pos = new Vector2(x, y);
					Vector2 atkToP = Vector2.Zero.cpy();
					atkToP = this.offsetRotateAttack.cpy();
					if(player.direction == Constant.DIRECTION_LEFT && this.rotateLeftFix)
						atkToP.y += this.spriteHeight/2f;
					
					if(hand == 1 && dualSame)
					{
						atkToP.x += 8;
						atkToP.y += 8 * cDir;
					}
					atkToP.rotate(sprite.getRotation());
					pos.add(atkToP);
					sprite.setPosition(pos.x, pos.y);
					sprite.setFlip(false, cDir == -1);
					if(hand == 1)
					{
						sprite.setColor(darkColor);
					}
					this.drawEnt(batch, sprite);
					this.rotation = sprite.getRotation();
					if(player.haveBuff(30) != -1)
					{
						Vector2 pp = new Vector2(sprite.getX(), sprite.getY());
						Particle p = Particle.Create(pp.add(MathUtils.random(12), MathUtils.random(12)), Vector2.Zero, 2, Color.RED, -2f, 1f, 1f);
						if(player.direction == -1)
							p.position.x -= 12;
						
						p.collisions = true;
						p.setLight(32, p.color);
					}
					
					/*sprite = new Sprite(this.getTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getStandFrames());
					sprite.setRegion(0, ((player.animFrame % this.getStandFrames()) * height), width, height);
					sprite.setSize(width, height);
					sprite.setOrigin(0, height/2);
					Vector2 cpos = new Vector2(player.atkMouseDif.x, player.atkMouseDif.y);
					sprite.setRotation((float)Math.toDegrees((Math.atan2(cpos.y, cpos.x))));
					float x = player.Center().x;
					float y = player.Center().y - height/2;
					Vector2 pos2 = new Vector2(x, y);
					Vector2 atkToP = Vector2.Zero.cpy();
					atkToP = this.offsetRotateAttack.cpy();
					if(hand == 1)
					{
						atkToP.x += 8;
						atkToP.y += 8 * player.attackDir;
					}
					atkToP.rotate(sprite.getRotation());
					pos2.add(atkToP);
					sprite.setPosition(pos2.x, pos2.y);
					this.rotation = sprite.getRotation();
					sprite.setFlip(false, player.attackDir == -1);
					
					if(player.invincible > 0f)
					{
						float invalpha = Math.abs(player.invincible*100%100/100);
						sprite.setAlpha(1-invalpha*2);
					}
					if(hand == 1)
					{
						sprite.setY(sprite.getY() + 4);
						sprite.setColor(darkColor);
					}
					sprite.draw(batch);
					afterDraw(sprite, player, batch);
					this.rotation = sprite.getRotation();*/
				}
				else if(this.overrideStand == 1)
				{
					sprite = new Sprite(this.getTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getStandFrames());
					sprite.setPosition(handPos.x + player.offSetByDirection(this.offsetX-width, -this.offsetX), handPos.y-this.offsetY);
					sprite.setRegion(0, ((player.animFrame % this.getStandFrames()) * height), width, height);
					sprite.setSize(width, height);
					sprite.flip((player.direction != 1), false);
					if(hand == 1 && dualSame)
					{
						sprite.setY(sprite.getY() + 8);
						sprite.setColor(darkColor);
					}
					if(player.invincible > 0f)
					{
						float invalpha = Math.abs(player.invincible*100%100/100);
						//sprite.setAlpha(1-invalpha*2);
					}
					if(player.haveBuff(11) != -1)
					{
						sprite.setAlpha(0.5f);
					}
					if(this.rotateOnDualEquip && player.dualEquipping())
					{
						sprite.setOrigin(0, 0);
						sprite.setRotation(sprite.getRotation() - 90);
						sprite.setY(sprite.getY() + sprite.getWidth() - 1 + this.roffsetY);
						if(player.direction == -1)
						{
							sprite.flip(true, true);
							sprite.setX(sprite.getX() - sprite.getHeight() + sprite.getWidth());
						}
						sprite.setX(sprite.getX() + this.roffsetX * player.direction);
					}
					this.drawEnt(batch, sprite);
					afterDraw(sprite, player, batch, hand);
					if(player.haveBuff(30) != -1)
					{
						Vector2 pp = new Vector2(sprite.getX(), sprite.getY());
						if(player.direction == -1)
							pp.x += sprite.getWidth();
						Particle p = Particle.Create(pp.add(MathUtils.random(12), MathUtils.random(12)), Vector2.Zero, 2, Color.RED, -2f, 1f, 1f);
						if(player.direction == -1)
							p.position.x -= 12;
						
						p.collisions = true;
						p.setLight(32, p.color);
					}
				}
				else if(this.overrideStand == 2)
				{
					sprite = new Sprite(this.getTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getStandFrames());
					sprite.setPosition(handPos.x - this.offsetX * player.direction - width/2, handPos.y-height/2 - this.offsetY);
					sprite.setRegion(0, ((player.animFrame % this.getStandFrames()) * height), width, height);
					sprite.setSize(width, height);
					sprite.flip((player.direction != 1), false);
					if(hand == 1 && dualSame)
					{
						sprite.setY(sprite.getY() + 4);
						sprite.setColor(darkColor);
					}
					if(player.invincible > 0f)
					{
						float invalpha = Math.abs(player.invincible*100%100/100);
						//sprite.setAlpha(1-invalpha*2);
					}
					if(player.haveBuff(11) != -1)
					{
						sprite.setAlpha(0.5f);
					}
					if(this.rotateOnDualEquip && player.dualEquipping())
					{
						sprite.setOrigin(0, 0);
						sprite.setRotation(sprite.getRotation() - 90);
						sprite.setX(sprite.getX() + this.roffsetX * player.direction);
						sprite.setY(sprite.getY() + sprite.getWidth() - 1 + this.roffsetY);
					}
					this.drawEnt(batch, sprite);
					afterDraw(sprite, player, batch, hand);
					if(player.haveBuff(30) != -1)
					{
						Vector2 pp = new Vector2(sprite.getX(), sprite.getY());
						if(player.direction == -1)
							pp.x += sprite.getWidth();
						Particle p = Particle.Create(pp.add(MathUtils.random(12), MathUtils.random(12)), Vector2.Zero, 2, Color.RED, -2f, 1f, 1f);
						if(player.direction == -1)
							p.position.x -= 12;
						
						p.collisions = true;
						p.setLight(32, p.color);
					}
				}
				else if(this.overrideStand == 5)
				{
					sprite = new Sprite(this.getTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getStandFrames());
					sprite.setY(handPos.y-this.offsetY + (hand == 1 && dualSame ? 8 : 0));
					float pos = player.position.cpy().x;
					if(player.direction == Constant.DIRECTION_RIGHT)
						pos += player.width;
					else
						pos -= sprite.getWidth();
					
					if(hand == 1)
						pos += 8 * player.direction;
					
					sprite.setX(pos + player.offSetByDirection(-this.offsetX, this.offsetX));
					sprite.setRegion(0, ((player.animFrame % this.getStandFrames()) * height), width, height);
					sprite.setSize(width, height);
					sprite.flip((player.direction != 1), false);
					if(hand == 1)
					{
						sprite.setY(sprite.getY() + 4);
						sprite.setColor(darkColor);
					}
					if(player.invincible > 0f)
					{
						float invalpha = Math.abs(player.invincible*100%100/100);
						//sprite.setAlpha(1-invalpha*2);
					}
					if(player.haveBuff(11) != -1)
					{
						sprite.setAlpha(0.5f);
					}
					if(this.rotateOnDualEquip && player.dualEquipping())
					{
						sprite.setOrigin(0, 0);
						sprite.setRotation(sprite.getRotation() - 90);
						sprite.setY(sprite.getY() + sprite.getWidth() - 1);
					}
					this.drawEnt(batch, sprite);
					afterDraw(sprite, player, batch, hand);
					if(player.haveBuff(30) != -1)
					{
						Vector2 pp = new Vector2(sprite.getX(), sprite.getY());
						if(player.direction == -1)
							pp.x += sprite.getWidth();
						
						Particle p = Particle.Create(pp.add(MathUtils.random(12), MathUtils.random(12)), Vector2.Zero, 2, Color.RED, -2f, 1f, 1f);
						if(player.direction == -1)
							p.position.x -= 12;
						
						p.collisions = true;
						p.setLight(32, p.color);
					}
				}
				else if(this.overrideStand == Constant.OVERRIDESTAND_ORB)
				{
					sprite = new Sprite(this.getTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getStandFrames());
					float sin = (float)Math.sin((Constant.gameTick()*2 + player.whoAmI * 15)*Math.PI/180f)*8-4;
					sprite.setY(handPos.y-this.offsetY + sin);
					float pos = player.position.cpy().x;
					if(player.direction == Constant.DIRECTION_RIGHT)
						pos += player.width;
					else
						pos -= sprite.getWidth();
					
					if(hand == 1)
						pos += 8 * player.direction;
					
					sprite.setX(pos + player.offSetByDirection(-this.offsetX, this.offsetX));
					sprite.setRegion(0, ((player.animFrame % this.getStandFrames()) * height), width, height);
					sprite.setSize(width, height);
					sprite.setRotation(Constant.gameTick()%360);
					sprite.flip((player.direction != 1), false);
					if(hand == 1)
					{
						sprite.setY(sprite.getY() + 4);
						//sprite.setColor(darkColor);
					}
					if(player.invincible > 0f)
					{
						float invalpha = Math.abs(player.invincible*100%100/100);
						//sprite.setAlpha(1-invalpha*2);
					}
					if(player.haveBuff(11) != -1)
					{
						sprite.setAlpha(0.5f);
					}
					this.drawEnt(batch, sprite);
					afterDraw(sprite, player, batch, hand);
					if(player.haveBuff(30) != -1)
					{
						Vector2 pp = new Vector2(sprite.getX(), sprite.getY());
						if(player.direction == -1)
							pp.x += sprite.getWidth();
						
						Particle p = Particle.Create(pp.add(MathUtils.random(12), MathUtils.random(12)), Vector2.Zero, 2, Color.RED, -2f, 1f, 1f);
						if(player.direction == -1)
							p.position.x -= 12;
						
						p.collisions = true;
						p.setLight(32, p.color);
					}
				}
			}
			else if(player.getAnim() == Constant.ANIMATION_ATTACKING && attacking)
			{
				if(this.overrideAttack == 1)
				{
					sprite = new Sprite(this.getAttackTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getAttackFrames());

					float x = handPos.x + (player.direction == 1 ? -16 : 16);
					x -= (player.direction == -1 ? 120 : 0);
					x += (player.direction == -1 ? this.offsetX : -this.offsetX);
					float y = player.position.y - 32;
					sprite.setPosition(x, y);

					int animFrame = !player.isWearingShield() ? player.animFrame : player.animFrame2;
					if(hand == 1)
						animFrame = !player.isWearingShield() ? player.animFrame2 : player.animFrame;

					sprite.setRegion(0, (animFrame * height), width, height);
					sprite.setSize(width, height);
					sprite.flip(player.direction == -1, (hand == 1 && !player.isWearingShield() ? player.invertedSwing2 : player.invertedSwing));
					if(hand == 1)
					{
						sprite.setY(sprite.getY() + 4);
						sprite.setColor(darkColor);
					}
					this.drawEnt(batch, sprite);
					this.afterDraw(sprite, player, batch, hand);
				}
				else if(this.overrideAttack == 2)
				{
					sprite = new Sprite(this.getAttackTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getAttackFrames());

					float x = player.Center().x;
					float y = player.Center().y - height/2;
					int animFrame = !player.isWearingShield() ? player.animFrame : player.animFrame2;
					if(hand == 1)
						animFrame = !player.isWearingShield() ? player.animFrame2 : player.animFrame;

					sprite.setRegion(0, (animFrame * height), width, height);
					sprite.setSize(width, height);
					sprite.setOrigin(0, height/2);
					Vector2 cpos = new Vector2(player.atkMouseDif.x, player.atkMouseDif.y);
					sprite.setRotation((float)Math.toDegrees((Math.atan2(cpos.y, cpos.x))));
					Vector2 pos = new Vector2(x, y);
					Vector2 atkToP = Vector2.Zero.cpy();
					atkToP = this.offsetRotateAttack.cpy();
					if(player.direction == -1)
					{
						atkToP.y *= -1f;
					}
					if(hand == 1)
					{
						atkToP.x += 8;
						atkToP.y += 8 * player.direction;
					}
					atkToP.rotate(sprite.getRotation());
					pos.add(atkToP);
					sprite.setPosition(pos.x, pos.y);
					sprite.setFlip(false, player.direction == -1);
					if(hand == 1)
					{
						sprite.setColor(darkColor);
					}
					this.drawEnt(batch, sprite);
					this.rotation = sprite.getRotation();
					this.afterDraw(sprite, player, batch, hand);
				}
				else if(this.overrideAttack == 3)
				{
					sprite = new Sprite(this.getAttackTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getAttackFrames());

					float x = player.Center().x - width/2;
					float y = player.Center().y - height/2;
					int animFrame = !player.isWearingShield() ? player.animFrame : player.animFrame2;
					if(hand == 1)
						animFrame = !player.isWearingShield() ? player.animFrame2 : player.animFrame;

					sprite.setPosition(x, y);
					sprite.setRegion(0, (animFrame * height), width, height);
					sprite.setSize(width, height);
					sprite.flip(player.direction == -1, (hand == 1 && !player.isWearingShield() ? player.invertedSwing2 : player.invertedSwing));
					if(hand == 1)
					{
						sprite.setColor(darkColor);
					}
					this.drawEnt(batch, sprite);
					afterDraw(sprite, player, batch, hand);
				}
				else if(this.overrideAttack == 4)
				{
					sprite = new Sprite(this.getAttackTexture());
					int width = (int)(sprite.getWidth());
					int height = (int)(sprite.getHeight()/this.getAttackFrames());

					float x = player.Center().x;
					float y = player.Center().y - height/2;
					int animFrame = !player.isWearingShield() ? player.animFrame : player.animFrame2;
					if(hand == 1)
						animFrame = !player.isWearingShield() ? player.animFrame2 : player.animFrame;

					sprite.setRegion(0, (animFrame * height), width, height);
					sprite.setSize(width, height);
					sprite.setOriginCenter();
					sprite.setFlip(false, player.direction == -1);
					if(hand == 1)
					{
						sprite.setColor(darkColor);
					}
					Vector2 pos = player.Center().cpy().sub(width/2f, height/2f);
					pos.add(this.offsetRotateAttack.cpy().setAngle(player.atkMouseDif.angle()));
					sprite.setPosition(pos.x, pos.y);
					sprite.flip(false, hand == 1 && !player.isWearingShield() ? player.invertedSwing2 : player.invertedSwing);
					sprite.setRotation(player.atkMouseDif.angle());
					this.drawEnt(batch, sprite);
					this.rotation = sprite.getRotation();
					this.afterDraw(sprite, player, batch, hand);
				}
			}
		}
		}
		if(this.id == 47 && !Projectile.Exists(17) && !Projectile.Exists(18) && (player.getAnim() == Constant.ANIMATION_STAND || player.getAnim() == Constant.ANIMATION_WALKING))
		{
			int tid = (player.haveBuff(14) != -1 ? 17 : 16);
			sprite = new Sprite(Content.projectiles[tid]);
			float width = sprite.getWidth();
			float height = sprite.getHeight();
			sprite.setPosition(player.getHandPosition().x - width/2, player.getHandPosition().y - height/2);
			sprite.setRotation(-Constant.gameTick() * 12);
			this.drawEnt(batch, sprite);
			
		}
		if(this.id == 10 && (player.getAnim() == Constant.ANIMATION_STAND || player.getAnim() == Constant.ANIMATION_WALKING) && drawLeft)
		{
			sprite = new Sprite(this.getTexture());
			int width = (int)(sprite.getWidth());
			int height = (int)(sprite.getHeight()/this.standFrames);
			sprite.setPosition(handPos.x + player.offSetByDirection(this.offsetX-width, -this.offsetX), handPos.y-height/2 - offsetY);

			int animFrame = player.animFrame;
			if(hand == 1)
				animFrame = player.animFrame2;

			sprite.setRegion(0, ((animFrame % this.standFrames) * height), width, height);
			sprite.setSize(width, height);
			sprite.flip((player.direction != 1), false);
			if(player.invincible > 0f)
			{
				float invalpha = Math.abs(player.invincible*100%100/100);
				//sprite.setAlpha(1-invalpha*2);
			}
			Vector2 origin = new Vector2(sprite.getX(), sprite.getY());
			if(hand == 1)
			{
				sprite.setColor(darkColor);
			}
			this.drawEnt(batch, sprite);
			sprite = new Sprite(Content.projectiles[4]);
			sprite.setPosition(origin.x, origin.y);
			Dummy ent = new Dummy();
			ent.width = (int) sprite.getWidth();
			ent.height = (int) sprite.getHeight();
			ent.position = origin;
			if(player.direction == Constant.DIRECTION_RIGHT)
				ent.position.x -= 16;
			else
				ent.position.x -= 40;

			ent.position.y += 52;

			ent.rotation = 45 * player.direction;
			for(int i = 0;i < 1;i++)
			{
				Vector2 pos = ent.randomHitBoxPosition();
				Particle p = Particle.Create(pos, Vector2.Zero, 2, new Color(0f,1f,1f,1f), -2f, 1f, 1f);
				p.setLight(16, p.color);
			}
		}
		else if(this.id == 2 && (player.getAnim() == Constant.ANIMATION_STAND || player.getAnim() == Constant.ANIMATION_WALKING) && drawLeft)
		{
			Dummy ent = new Dummy();
			ent.width = (int) 110;
			ent.height = (int) 40;
			ent.rotation = sprite.getRotation()+45;
			if(sprite.isFlipX())
			{
				ent.rotation = 180-ent.rotation;
			}
			if(sprite.isFlipY())
			{
				ent.rotation = 180-ent.rotation;
			}
			ent.position = new Vector2(sprite.getX(), sprite.getY()+24*(sprite.isFlipY() ? 0 : 1)).add(new Vector2(16, 0).rotate(ent.rotation));
			for(int i = 0;i < 1;i++)
			{
				Vector2 pos = ent.randomHitBoxPosition();
				Particle p = Particle.Create(pos, Vector2.Zero, 2, new Color(0.8f,1f,1f,1f), 2f, 1f, 0.5f + MathUtils.random());
				p.setLight(32, new Color(0.1f, 1f, 1f, 1f));
				Lighting l = Lighting.Create(player.Center(), 512, Main.getLightingColor(new Color(0.1f, 1f, 1f, 1f), 0.5f), 1f, 0.9f);
				p.parent = player;
				l.vibrancy = 50f;
				p.drawBack = player.timeSinceLastAttack >= Constant.BACKWEAPON_TIME && this.canGoBack;
			}
		}
		else if(this.id == 14 && (player.getAnim() == Constant.ANIMATION_STAND || player.getAnim() == Constant.ANIMATION_WALKING) && drawLeft)
		{
			sprite = new Sprite(this.getTexture());
			int width = (int)(sprite.getWidth());
			int height = (int)(sprite.getHeight()/this.standFrames);
			sprite.setPosition(handPos.x + player.offSetByDirection(this.offsetX-width-8, -this.offsetX+8), handPos.y - this.offsetY);

			int animFrame = player.animFrame;
			if(hand == 1)
				animFrame = player.animFrame2;

			sprite.setRegion(0, ((animFrame % this.standFrames) * height), width, height);
			sprite.setSize(width, height);
			sprite.flip((player.direction != 1), false);
			if(player.invincible > 0f)
			{
				float invalpha = Math.abs(player.invincible*100%100/100);
				//sprite.setAlpha(1-invalpha*2);
			}
			if(hand == 1)
			{
				sprite.setColor(darkColor);
			}
			this.drawEnt(batch, sprite);
		}
		else if(this.id == 32 && (player.getAnim() == Constant.ANIMATION_STAND || player.getAnim() == Constant.ANIMATION_WALKING) && drawLeft)
		{
			Vector2 origin = new Vector2(sprite.getX(), sprite.getY());
			Dummy ent = new Dummy();
			ent.width = (int) 120;
			ent.height = (int) 40;
			ent.position = origin;
			if(player.direction == Constant.DIRECTION_RIGHT)
				ent.position.x += 8;
			else
				ent.position.x += 48;

			//ent.position.y = 0;

			ent.rotation = 0 * player.direction;
			for(int i = 0;i < 1;i++)
			{
				Vector2 pos = ent.randomHitBoxPosition();
				Particle p = Particle.Create(pos, Vector2.Zero, 2, new Color(1f,1f,1f,1f), 2f, 1f, 0.5f + MathUtils.random());
				p.parent = player;
				p.setLight(32, new Color(1f,1f,0.4f,1f));
				Lighting.Create(p.position, 256, new Color(0.1f, 1f, 1f, 1f), p.duration);
			}
		}

		if((player.getAnim() == Constant.ANIMATION_STAND || player.getAnim() == Constant.ANIMATION_WALKING))
		{
			if(this.itemclass == Constant.ITEMCLASS_RANGED)
			{
				if(player.haveBuff(6) != -1 && sprite != null)
				{
					Vector2 pos = new Vector2(sprite.getX() + MathUtils.random(sprite.getWidth()), sprite.getY() + MathUtils.random(sprite.getHeight()));
					Vector2 center = new Vector2(sprite.getX() + sprite.getWidth()/2, sprite.getY() + sprite.getHeight()/2);
					pos.sub(center);
					pos.rotate(sprite.getRotation());
					pos.add(center);
					Particle p = Particle.Create(pos, Vector2.Zero, 10, new Color(1f, 1f, MathUtils.random(), 1f), 2f, 1f, 1f).setLight(16, new Color(0.92f, 0.96f, 0.6f, 1f));
					p.drawFront = true;
					p.rotation = MathUtils.random(360);
					
					Lighting.Create(player.Center(), 512, Main.getLightingColor(new Color(0.96f, 0.92f, 0.6f, 1f), 0.3f), 1f, 0.7f);
				}
				if(player.haveBuff(31) != -1 && sprite != null)
				{
					float dist = Math.max(sprite.getWidth(), sprite.getHeight())/2f+ 10;
					int quant = 0;
					Skill s = player.getSkill(39);
					if(s !=  null)
					{
						quant = s.getInfoValueI(player, 0);
					}
					for(int i = 0;i < quant;i++)
					{
						float angle = 360/quant * i + Constant.gameTick()*2;
						Vector2 pos = new Vector2(dist, 0f).setAngle(angle);
						Vector2 center = new Vector2(sprite.getX() + sprite.getWidth()/2, sprite.getY() + sprite.getHeight()/2);
						pos.rotate(sprite.getRotation());
						pos.add(center);
						Particle p = Particle.Create(pos, Vector2.Zero, 16, new Color(0.96f, 0.92f, 0.6f, 1f), 0f, 0.5f, 1f).setLight(16, new Color(0.92f, 0.96f, 0.6f, 1f)).setParent(player);
						p.drawFront = true;
						p.rotation = angle;
					}
					
					Lighting.Create(player.Center(), 512, Main.getLightingColor(new Color(0.96f, 0.92f, 0.6f, 1f), 0.3f), 1f, 0.7f);
				}
				if(player.haveBuff(32) != -1 && sprite != null)
				{
					Vector2 pos = new Vector2(sprite.getX() + MathUtils.random(sprite.getWidth()), sprite.getY() + MathUtils.random(sprite.getHeight()));
					Vector2 center = new Vector2(sprite.getX() + sprite.getWidth()/2, sprite.getY() + sprite.getHeight()/2);
					pos.sub(center);
					pos.rotate(sprite.getRotation());
					pos.add(center);
					Particle p = Particle.Create(pos, Vector2.Zero, 17, new Color(1f, 1f, MathUtils.random()/2f, 1f), 2f, 1f, 1f);
					p.drawFront = true;
					p.rotation = MathUtils.random(360);
				}
			}
			if(player.haveBuff(39) != -1 && sprite != null)
			{
				Vector2 pos = new Vector2(sprite.getX() + MathUtils.random(sprite.getWidth()), sprite.getY() + MathUtils.random(sprite.getHeight()));
				Vector2 center = new Vector2(sprite.getX() + sprite.getWidth()/2, sprite.getY() + (sprite.getHeight()/this.getStandFrames())/2);
				pos.sub(center);
				pos.rotate(sprite.getRotation() + (sprite.isFlipY() ? 45f * player.direction : 0f));
				pos.add(center);
				if(this.rotateOnDualEquip && player.dualEquipping())
				{
					pos = new Vector2(sprite.getX() + MathUtils.random(sprite.getHeight()), sprite.getY() + MathUtils.random(sprite.getWidth()) - sprite.getWidth() - 1);
					center = new Vector2(sprite.getX() + (sprite.getHeight()/this.getStandFrames())/2, sprite.getY() + sprite.getWidth()/2);
				}
				if(this.overrideStand == Constant.OVERRIDESTAND_FRONT)
				{
					Dummy ent = new Dummy();
					ent.position.x = sprite.getX() + this.offsetX;
					ent.position.y = sprite.getY() + this.offsetY + 16;
					ent.width = (int) (Math.sqrt(Math.pow(this.spriteHoldWidth, 2) + Math.pow((this.spriteHoldHeight/this.getStandFrames()), 2)));
					ent.height = 32;
					ent.rotation = (Main.xor(player.direction == 1, sprite.isFlipY()) ? 45 : 135);
					pos = ent.randomHitBoxPosition();
				}
				Particle p = Particle.Create(pos, Vector2.Zero, 16, new Color(0.5f + MathUtils.random()/2f, 0f, 0f, 0f), 2f, 1f, 1f);
				if(hand == 0)
					p.drawFront = true;
				else
					p.drawBack = true;
				
				p.setLight(32, Constant.EXPLOSION_DEFAULT_SCHEME[3]);
				Lighting.Create(p.position, 256, Constant.EXPLOSION_DEFAULT_SCHEME[3], p.duration, 0.8f, true);
				p.rotation = MathUtils.random(360);
			}
		}
	}

	public void drawHat(SpriteBatch batch, Player player, Sprite sprite2)
	{
		this.holder = player;
		if(this.debugging)
		{
			this.offsetX = Main.var[6];
			this.offsetY = Main.var[7];
			this.roffsetX = Main.var[6];
			this.roffsetY = Main.var[7];
			this.woffsetX = Main.var[6];
			this.woffsetY = Main.var[7];
			this.leftOffsetX = Main.var[8];
			this.wleftOffsetX = Main.var[8];
		}
		if(this.overrideStand == Constant.OVERRIDESTAND_HEAD || this.overrideStand == Constant.OVERRIDESTAND_BODY)
		{
			if(player.rolling)
			{
				Vector2 offset = player.getHeadPosition();
				if(this.overrideStand == Constant.OVERRIDESTAND_BODY)
					offset = player.getBodyPosition();
				Sprite sprite = new Sprite(this.getHeadRTexture());
				int width = (int)(sprite.getWidth());
				int height = (int)(sprite.getHeight());
				sprite.setPosition(player.Center().x-sprite.getWidth()/2f, player.Center().y - sprite.getHeight()/2f);
				sprite.flip(sprite2.isFlipX(), false);
				sprite.setOriginCenter();
				sprite.setOrigin(sprite.getOriginX()+this.roffsetX, sprite.getOriginY()+this.roffsetY);
				sprite.setRotation(sprite2.getRotation());
				if(player.invincible > 0f)
				{
					float invalpha = Math.abs(player.invincible*100%100/100);
					invalpha = 1-invalpha*2;
					if(invalpha < 0.75f)
						invalpha = 0.75f;
					//sprite.setAlpha(invalpha);
				}
				if(player.haveBuff(11) != -1)
				{
					sprite.setAlpha(0.5f);
				}
				this.drawEnt(batch, sprite);
				if(this.dyed)
				{
					sprite.setTexture(this.getDHeadRTexture());
					sprite.setColor(this.dye);
					this.drawEnt(batch, sprite);
				}
			}
			else if(!player.walking)
			{
				Vector2 offset = player.getHeadPosition();
				if(this.overrideStand == Constant.OVERRIDESTAND_BODY)
					offset = player.getBodyPosition();

				Sprite sprite = new Sprite(this.getHeadSTexture());
				int width = (int)(sprite.getWidth());
				int height = (int)(sprite.getHeight()/this.standFrames);
				sprite.setRegion(0, player.animFrame % this.standFrames * height, width, height);
				sprite.setSize(width, height);
				sprite.setPosition(offset.x+player.offSetByDirection(this.leftOffsetX, this.offsetX), offset.y+this.offsetY);
				sprite.flip(player.direction == -1, false);
				sprite.rotate(player.rotation);
				if(player.invincible > 0f)
				{
					float invalpha = Math.abs(player.invincible*100%100/100);
					invalpha = 1-invalpha*2;
					if(invalpha < 0.75f)
						invalpha = 0.75f;
					//sprite.setAlpha(invalpha);
				}
				if(player.haveBuff(11) != -1)
				{
					sprite.setAlpha(0.5f);
				}
				this.drawEnt(batch, sprite);
				if(this.dyed)
				{
					sprite.setTexture(this.getDHeadSTexture());
					sprite.setColor(this.dye);
					this.drawEnt(batch, sprite);
				}
			}
			else if(player.walking)
			{
				Vector2 offset = player.getHeadPosition();
				if(this.overrideStand == Constant.OVERRIDESTAND_BODY)
					offset = player.getBodyPosition();

				Sprite sprite = new Sprite(this.getHeadWTexture());
				int width = (int)(sprite.getWidth());
				int height = (int)(sprite.getHeight()/this.walkFrames);
				sprite.setRegion(0, player.animFrame % this.walkFrames * height, width, height);
				sprite.setSize(width, height);
				sprite.setPosition(offset.x+player.offSetByDirection(this.wleftOffsetX, this.woffsetX), offset.y+this.woffsetY);
				sprite.flip(player.direction == -1, false);
				sprite.rotate(player.rotation);
				if(player.invincible > 0f)
				{
					float invalpha = Math.abs(player.invincible*100%100/100);
					invalpha = 1-invalpha*2;
					if(invalpha < 0.75f)
						invalpha = 0.75f;
					//sprite.setAlpha(invalpha);
				}
				if(player.haveBuff(11) != -1)
				{
					sprite.setAlpha(0.5f);
				}
				this.drawEnt(batch, sprite);

				if(this.dyed)
				{
					sprite.setTexture(this.getDHeadWTexture());
					sprite.setColor(this.dye);
					this.drawEnt(batch, sprite);
				}
			}
		}
	}

	private void afterDraw(Sprite sprite2, Player player, SpriteBatch batch, int hand)
	{
		this.holder = player;
		if(this.id == 12)
		{
			Vector2 pos = new Vector2(sprite2.getX() + MathUtils.random(sprite2.getWidth() - 16) + 16, sprite2.getY() + MathUtils.random(sprite2.getHeight() - 20));
			if(player.direction == Constant.DIRECTION_LEFT)
			{
				pos.x -= 16;
			}
			if(player.attacking)
			{
				/*pos = new Vector2(sprite2.getX() + MathUtils.random(sprite2.getWidth()/2 - 16) + 16, sprite2.getY() + MathUtils.random(sprite2.getHeight() - 20));
				if(player.direction == Constant.DIRECTION_RIGHT)
					pos.x += sprite2.getWidth()/2;*/

				float random = MathUtils.random(-90, 90);
				if(player.direction == Constant.DIRECTION_LEFT)
					random = MathUtils.random(90, 270);
				float sin = (float) Math.sin(random * Math.PI/180);
				float cos = (float) Math.cos(random * Math.PI/180);
				pos = new Vector2(player.Center().x + cos * MathUtils.random(30, 100), player.Center().y + sin * MathUtils.random(30, 100));
			}
			if(player.getBuffStacks(4) > 1)
			{
				int q = player.getBuffStacks(4)-2;
				if(q > 2)
					q = 2;
				for(int i = 0;i < q;i++)
				{
					float value = MathUtils.random()*0.5f;
					Color color = new Color(value, 0f, value, 1f);
					pos.y += 4;
					Particle p = Particle.Create(pos, Vector2.Zero, 6, color, 2f, 1f, (MathUtils.random() + 0.5f) * 0.75f);
					p.setLight(32, p.color);
					Lighting.FromParticle(p, true);
					p.frameCounterTicks = 7;
				}
			}
			if(player.attacking && player.animFrame >= 1)
			{
				Sprite sprite = new Sprite(this.getAttackTexture());
				int width = (int)(sprite.getWidth());
				int height = (int)(sprite.getHeight()/this.attackFrames);

				float x = player.Center().x - width/2;
				float y = player.Center().y - height/2;
				sprite.setPosition(x, y);
				sprite.setRegion(0, ((player.animFrame-1) * height), width, height);
				sprite.setSize(width, height);
				sprite.flip(player.direction == -1, player.invertedSwing);
				sprite.setAlpha(0.4f);
				this.drawEnt(batch, sprite);
			}
			if(player.attacking && player.animFrame >= 2)
			{
				Sprite sprite = new Sprite(this.getAttackTexture());
				int width = (int)(sprite.getWidth());
				int height = (int)(sprite.getHeight()/this.attackFrames);

				float x = player.Center().x - width/2;
				float y = player.Center().y - height/2;
				sprite.setPosition(x, y);
				sprite.setRegion(0, ((player.animFrame-2) * height), width, height);
				sprite.setSize(width, height);
				sprite.flip(player.direction == -1, player.invertedSwing);
				sprite.setAlpha(0.2f);
				this.drawEnt(batch, sprite);
			}
		}
		else if(this.id == 2)
		{
			/*if(player.attacking && player.animFrame >= 1)
			{
				Sprite sprite = new Sprite(this.getAttackTexture());
				int width = (int)(sprite.getWidth());
				int height = (int)(sprite.getHeight()/this.attackFrames);

				float x = player.Center().x - width/2;
				float y = player.Center().y - height/2;
				sprite.setPosition(x, y);
				sprite.setRegion(0, ((player.animFrame-1) * height), width, height);
				sprite.setSize(width, height);
				sprite.flip(player.direction == -1, player.invertedSwing);
				sprite.setAlpha(0.4f);
				sprite.draw(batch);
			}
			if(player.attacking && player.animFrame >= 2)
			{
				Sprite sprite = new Sprite(this.getAttackTexture());
				int width = (int)(sprite.getWidth());
				int height = (int)(sprite.getHeight()/this.attackFrames);

				float x = player.Center().x - width/2;
				float y = player.Center().y - height/2;
				sprite.setPosition(x, y);
				sprite.setRegion(0, ((player.animFrame-2) * height), width, height);
				sprite.setSize(width, height);
				sprite.flip(player.direction == -1, player.invertedSwing);
				sprite.setAlpha(0.2f);
				sprite.draw(batch);
			}*/
		}
		else if(this.id == 159)
		{
			Vector2 pos = new Vector2(sprite2.getX(), sprite2.getY());
			Color color = new Color(1f, 1f, 1f, 1f).fromHsv(Main.gameTick, 1f, 1f);
			if(player.timeSinceLastAttack >= Constant.BACKWEAPON_TIME && this.canGoBack)
			{
				sprite2.setTexture(Content.extras[31]);
				sprite2.setColor(color);
				sprite2.draw(batch);
			}
			else if((hand == 0 && player.attacking) || (hand == 1 && player.attacking2))
			{
				sprite2.setTexture(Content.extras[30]);
				sprite2.setColor(color);
				sprite2.draw(batch);
			}
			else
			{
				/*Sprite sprite = new Sprite(Content.extras[29]);
				sprite.setColor(color);
				if(player.dualEquipping())
				{
					sprite.rotate(player.direction * -90);
					pos.x += 39;
					pos.y -= 80;
				}
				sprite.setPosition(pos.x + (player.direction == -1 ? 1 : 0), pos.y);
				sprite.draw(batch);*/
				sprite2.setTexture(Content.extras[29]);
				sprite2.setColor(color);
				this.drawEnt(batch, sprite2);
			}
			//Lighting.Create(pos, 256, color, 1f, 1f, true);
		}
		else if(this.id == 179)
		{
			Dummy dummy = new Dummy();
			dummy.position.x = sprite2.getX();
			dummy.position.y = sprite2.getY();
			dummy.width = (int)sprite2.getWidth();
			dummy.height = (int)sprite2.getHeight();
			dummy.rotation = sprite2.getRotation();
			Particle.Create(dummy.randomHitBoxPosition(), Vector2.Zero, 2, new Color(0.95f, 0.95f, 0.95f, 1f), 1f, 1f, 1f).setLight(16, Color.WHITE).drawBack = true;
		}
	}


	public void backDraw(SpriteBatch batch, Player player)
	{
		this.holder = player;
		if(this.id == 12)
		{
			int slot = player.haveBuff(4);
			this.overrideStandTexture = null;
			this.overrideStandTextureFrames = 1;
			this.overrideAttackTexture = null;
			this.overrideAttackTextureFrames = 1;
			if(slot != -1)
			{
				if(player.buffs[slot].stacks >= 5)
				{
					this.overrideStandTexture = Content.sfH;
					this.overrideStandTextureFrames = 3;
					this.overrideAttackTexture = Content.sfA;
					this.overrideAttackTextureFrames = 7;
				}
			}
		}
		else if(this.id == 47)
		{
			int slot = player.haveBuff(14);
			this.overrideStandTexture = null;
			this.overrideStandTextureFrames = 1;
			this.overrideAttackTexture = null;
			this.overrideAttackTextureFrames = 1;
			this.shootProj = 17;
			if(slot != -1)
			{
				this.overrideStandTexture = Content.ptH;
				this.shootProj = 18;
				
				if(!Projectile.Exists(18))
				{
					float ang = MathUtils.random(360);
					float sin = (float)Math.sin(ang * Math.PI/180f);
					float cos = (float)Math.cos(ang * Math.PI/180f);
					float ran = MathUtils.random(70, 85);
					Particle p = Particle.Create(new Vector2(player.getHandPosition().x + sin * ran, player.getHandPosition().y + cos * ran), Vector2.Zero.cpy(), 2, new Color(1f, 0.5f + MathUtils.random()/2f, 0f, 1f), 2f, 2.5f, 1f);
					
					p.setLight(16, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE]);
					Lighting l = Lighting.Create(player.Center(), 256, Constant.ELEMENTCOLOR[Constant.DAMAGETYPE_FIRE], Main.ticksToSeconds(2));
					l.vibrancy = 30;
					p.drawFront = true;
				}
			}
		}
		else if(this.id == 49)
		{
			if(player.haveBuff(18) != -1)
			{
				this.overrideStandTexture = Content.seH;
				this.overrideAttackTexture = Content.seA;
				this.overrideAttackTextureFrames = 5;
				this.shootProj = 46;
				this.shootSpeed = 1500;
			}
			else
			{
				this.overrideStandTexture = null;
				this.overrideAttackTexture = null;
				this.shootProj = 55;
				this.shootSpeed = 300;
			}
		}
		else if(this.id == 131)
		{
			if(player.haveBuff(57)!=-1)
			{
				this.overrideAttackTexture = Content.extras[19];
				this.overrideAttackTextureFrames = 15;
				this.damageType = Constant.DAMAGETYPE_BLOOD;
			}
			else
			{
				this.overrideAttackTexture = null;
				this.damageType = Constant.DAMAGETYPE_PHYSICAL;
			}
		}
		else if(this.id == 189)
		{
			if(player.haveBuff(73) != -1)
			{
				this.overrideStandTexture = Content.extras[39];
			}
			else
			{
				this.overrideStandTexture = null;
			}
		}
		
		for(int i = 0;i < laGeasCount();i++)
		{
			if(this.id == 7)
			{
				int sin = (int) (((Constant.gameTick() * player.getAttackSpeed()) + 90 * i) % 360);
				double sinV = Math.sin(sin * Math.PI/180);
				if(sin >= 90 && sin <= 270)
				{
					Sprite sprite = new Sprite(Content.projectiles[2]);
					float width = sprite.getWidth();
					float height = sprite.getHeight();
					int cSin = Math.abs(180 - sin);
					float offsetY = ((180 - cSin) * 16)/180;
					sprite.setPosition((float) (player.position.x + sinV * width), player.Center().y - width/2 + offsetY);
					Vector2 spCenter = new Vector2(sprite.getX() + width/2, sprite.getY() + height/2);
					float angle = (float)Math.toDegrees(Math.atan2(spCenter.x - player.Center().x, spCenter.y - player.Center().y - 100));
					sprite.rotate((float) (angle + 45));
					this.drawEnt(batch, sprite);
				}
			}
		}
	}

	public void postDraw(SpriteBatch batch, Player player)
	{
		this.holder = player;
		for(int i = 0;i < laGeasCount();i++)
		{
			if(this.id == 7)
			{
				int sin = (int) (((Constant.gameTick() * player.getAttackSpeed()) + 90 * i) % 360);
				double sinV = Math.sin(sin * Math.PI/180);
				if((sin < 90 || sin > 270))
				{
					Sprite sprite = new Sprite(Content.projectiles[2]);
					float width = sprite.getWidth();
					float height = sprite.getHeight();
					int cSin = Math.abs(180 - sin);
					float offsetY = ((180 - cSin) * 16)/180;
					sprite.setPosition((float) (player.position.x + sinV * width), player.Center().y - width/2 + offsetY);
					Vector2 spCenter = new Vector2(sprite.getX() + width/2, sprite.getY() + height/2);
					float angle = (float)Math.toDegrees(Math.atan2(spCenter.x - player.Center().x, spCenter.y - player.Center().y - 100));
					sprite.rotate((float) (angle + 45));
					this.drawEnt(batch, sprite);
				}
			}
		}
	}

	public void onFrame(int hand, Player player)
	{
		this.holder = player;
		Vector2 mouseNor = player.mouseOnAttack.cpy().sub(player.Center());
		mouseNor.nor();
		Vector2 handPos = player.getHandPosition();
		if(hand == 1)
			handPos = player.getBackHandPosition();
		if(this.id == 7 && this.laGeasCount() > 0 && player.attacking && player.animFrame % 4 == 0 && player.animFrameCounter == 0)
		{
			mouseNor.scl(1200f * (player.getAttackSpeed()/2));
			Projectile.Summon(3, player.Center(), mouseNor, this.damage, 10f, player);
		}
		else if(this.id == 9 && player.lastHurtTicks() < 300)
		{
			for(int j = 1;j <= 36;j++)
			{
				Color color = new Color(1f, 0f, 0f, 1f);

				float value = (float) (MathUtils.random()*Math.PI*2);
				float sin = (float) Math.sin(value);
				float cos = (float) Math.cos(value);
				float val = 0;
				val = MathUtils.random() * (player.lastHurtTicks());
				Vector2 vel = new Vector2(cos * val, sin * val);
				Particle p = Particle.Create(player.Center(), vel, 2, color, 0f, 0.3f, 2f);
				p.position.add(vel);
				p.velocity.scl(-5f);
				p.drawBack = true;
				p.parent = player;
				p.fixAlpha = true;
			}
		}
		else if(this.id == 21 && player.attacking)
		{
			int speed = (int)Math.ceil(8/player.getAttackSpeed());
			if(player.getAttackSpeed() >= 4.2)
				speed = 1;

			if(Constant.gameTick() % speed == 0)
			{
				Vector2 noise = new Vector2(MathUtils.random(-50, 50), MathUtils.random(-50, 50));
				Vector2 pos = new Vector2(player.Center().x + mouseNor.x * 70 + noise.x, player.Center().y + mouseNor.y * 70 + noise.y);
				float r = MathUtils.random()/2;
				Particle p = Particle.Create(new Vector2(player.Center().x + mouseNor.x * 55 + noise.x, player.Center().y + mouseNor.y * 55 + noise.y), Vector2.Zero, 7, new Color(r, 0f, r, 1), 0f, 1f, 1f);
				p.drawFront = true;
				Vector2 cPos = player.mouseOnAttack.cpy();
				if(cPos.dst(player.Center()) < 2000f)
				{
					cPos.sub(player.Center());
					cPos.nor().scl(2000f);
					cPos.add(player.Center());
				}
				
				Vector2 vel = cPos.sub(pos).nor().scl(1500f);
				Projectile.Summon(11, pos, vel, this.damage, 6f, player);
			}
		}
		else if(this.id == 22)
		{
			Vector2 pos = new Vector2();
			pos.x = MathUtils.random(72);
			pos.y = MathUtils.random(12);
			pos.rotate((player.direction == Constant.DIRECTION_RIGHT ? 45f : 135f));
			pos.add(handPos);
			pos.x += 8f;
			Particle p = Particle.Create(pos, Vector2.Zero.cpy(), 2, new Color(0f, 0.5f + MathUtils.random()/2, 1f, 1f), 0f, 0.4f, 0.5f+MathUtils.random()/2f);
			p.parent = player;
			p.drawFront = true;

			pos = new Vector2();
			pos.x = MathUtils.random(12) + 8;
			pos.y = MathUtils.random(-16, 16);
			pos.rotate((player.direction == Constant.DIRECTION_RIGHT ? 45f : 135f));
			pos.add(handPos);
			p = Particle.Create(pos, Vector2.Zero.cpy(), 2, new Color(0f, 0.5f + MathUtils.random()/2, 1f, 1f), 0f, 0.4f, 0.5f+MathUtils.random()/2f);
			p.parent = player;
			p.drawFront = true;

			pos = new Vector2();
			pos.x = MathUtils.random(16) - 16;
			pos.y = MathUtils.random(8);
			pos.rotate((player.direction == Constant.DIRECTION_RIGHT ? 45f : 135f));
			pos.add(handPos);
			pos.x += 8f;
			p = Particle.Create(pos, Vector2.Zero.cpy(), 2, new Color(0f, 0.5f + MathUtils.random()/2, 1f, 1f), 0f, 0.4f, 0.5f+MathUtils.random()/2f);
			p.parent = player;
			p.drawFront = true;
		}
		else if(this.id == 14 && (player.getAnim() == Constant.ANIMATION_STAND || player.getAnim() == Constant.ANIMATION_WALKING))
		{
			Vector2 pos = new Vector2();
			pos.x = MathUtils.random(51) + 83;
			pos.y = MathUtils.random(65) - 65;
			pos.rotate((player.direction == Constant.DIRECTION_RIGHT ? 45f : 135f));
			pos.add(handPos);
			pos.x += -12 * player.direction;
			if(player.direction == Constant.DIRECTION_LEFT)
			{
				pos.x -= 36;
				pos.y -= 48;
			}
			Particle p = Particle.Create(pos, Vector2.Zero.cpy(), 13, new Color(0f, 0.5f + MathUtils.random()/2, 1f, 1f), -4f, 0.4f, 0.5f+MathUtils.random()/2f);
			p.drawFront = true;
		}
		else if(this.id == 65 && (player.getAnim() == Constant.ANIMATION_STAND || player.getAnim() == Constant.ANIMATION_WALKING))
		{
			Dummy ent = new Dummy();
			ent.width = 56;
			ent.height = 64;
			ent.position.x = player.getHandPosition().x - this.offsetX - 8;
			ent.position.y = player.getHandPosition().y + 10;
			if(Constant.gameTick() % 5 == 0)
			{
				Color color = new Color(1f, 0.6f + MathUtils.random(0.3f), 0f, 1f);
				Vector2 vel = new Vector2(20 * MathUtils.random(-1f, 1f), 0f);
				Particle p = Particle.Create(ent.randomHitBoxPosition(), vel, 2, color, 0f, 1f, 1f);
				p.drawBack = MathUtils.randomBoolean();
			}
		}
	}

	public int laGeasCount()
	{
		int count = 4;
		for(Projectile p : Constant.getProjectileList())
		{
			if(p.active && p.type == 3)
			{
				count--;
			}
		}
		return count;
	}

	public void onSwing(Player player)
	{
		this.currentAnimation = (this.currentAnimation + 1) % (1 + this.extraAnimations.size());
	}

	public void onShoot(final Projectile proj, final Player player)
	{
		if(this.id == 26)
		{
			proj.damage *= (1 + player.getBuffStacks(8) * 0.02f);
			Particle p = Particle.Create(proj.Center(), Vector2.Zero, 11, new Color(0f, 1f, 1f, 1f), 0f, 1f, 1.5f);
			p.frameCounterTicks--;
			p.rotation = MathUtils.random() * 360f;
		}
		else if(this.id == 159)
		{
			this.shootProj++;
			if(this.shootProj > 105)
				this.shootProj = 100;
		}
		else if(this.id == 176)
		{
			Event e = new Event(player.getAttackSpeed() >= 2f ? 5 : 10) {
				@Override
				public void function()
				{
					Vector2 pos = player.Center().add(player.mouseOnAttack.cpy().sub(player.Center()).nor().scl(64f)).add(MathUtils.random(-64f, 64f), MathUtils.random(-64f, 64f));
					Projectile p = Projectile.Summon(116, pos, proj.velocity.cpy(), proj.damage, proj.timeLeft, player);
					for(int k = 0;k < 15;k++)
					{
						Particle p2 = Particle.Create(p.randomHitBoxPosition(), new Vector2(MathUtils.random(-128f, 128f), MathUtils.random(-128f, 128f)), 2, new Color(), 0f, 0.5f, 1f);
						p2.setLight(32, p.lightColor);
					}
				}
			};
			Main.scheduledTasks.add(e);
		}
		else if(this.id == 182)
		{
			if(!player.grounded)
			{
				proj.addBuff(72, this.getInfoValueI(0), 8f, player);
			}
		}
	}

	public void onHurtFrame(Sprite sprite, Player player)
	{
		Vector2 handPos = new Vector2(sprite.getX(), sprite.getY());
		Vector2 mouseNor = player.mouseOnAttack.cpy().sub(handPos);
		mouseNor.nor();
		if(this.id == 2)
		{
			int iterat = 1;
			if(player.doubleOnHit)
				iterat++;
			
			for(int x = 0;x < iterat;x++)
			{
				Monster target = Main.getMouseTarget(900f, true);
					
				if(target != null)
				{
					for(int i = 0;i < 36;i++)
					{
						Vector2 vel = new Vector2(MathUtils.random(1000, 2500), MathUtils.random(-300, 300));
						vel.rotate(Main.angleBetween(player.Center(), target.Center()));
						Color color = new Color(MathUtils.random(), 1f, 1f, 1f);
						Particle p = Particle.Create(target.randomHitBoxPosition(), vel, 2, color, -5f, 1f, 0.5f + MathUtils.random());
						p.setLight(32, Color.CYAN);
					}
					for(Monster m : Constant.getMonsterList(false))
					{
						if(m.Center().dst(target.Center()) < 100)
						{
							m.hurtDmgVar((int)(this.damage), 0, 0f, player.canCritical(m), this.damageType, this.itemclass, player);
							if(!m.boss && !m.knockbackImmune)
							{
								m.velocity = Vector2.Zero.cpy();
								m.uncapped = 0f;
							}
							if(x == 0)
							{
								for(int i = Constant.ITEMSLOT_OFFSET;i <= Constant.ITEMSLOT_MAX;i++)
								{
									if(player.inventory[i] != null)
									{
										player.inventory[i].onHit(m, player);
										if(player.doubleOnHit)
											player.inventory[i].onHit(m, player);
									}
								}
							}
						}
					}
				}
			}
		}
		else if(this.id == 13)
		{
			for(int i = 0;i < 36;i++)
			{
				float sin = (float)(Math.sin(i * 10 * Math.PI/180));
				float cos = (float)(Math.cos(i * 10 * Math.PI/180));
				Particle p = Particle.Create(new Vector2(cos * 60 + MathUtils.random(-2,2), sin * 60 + MathUtils.random(-2,2)), Vector2.Zero, 2, Color.RED, 0f, 0.1f, 1f);
				p.position.add(player.Center());
				p.setLight(32, p.color);
				p.parent = player;
			}
			mouseNor.scl(800f);
			Projectile.Summon(8, player.Center(), mouseNor, this.damage, 4, player);
		}
		else if(this.id == 65)
		{
			Dummy ent = new Dummy();
			ent.width = 56;
			ent.height = 64;
			ent.position.x = player.getHandPosition().x - this.offsetX - 8;
			ent.position.y = player.getHandPosition().y + 10;
			float angle = Main.angleBetween(player.posOnAttack, player.mouseOnAttack);
			for(int i = 0;i < 10;i++)
			{
				Vector2 pos = ent.randomHitBoxPosition();
				Projectile p = Projectile.Summon(47, pos, new Vector2(MathUtils.random(-32, 32), MathUtils.random(-32, 32)),
						damage, 2.5f, player);
				
				p.rotation = angle;
			}
		}
	}
	
	public void onUse(Player player)
	{
		if(player.level < this.level)
		{
			Main.lastQuestTicks=0;
			Main.lastQuestText = Main.lv("You need level "+this.level+" to use this item.", "Você precisa de nível "+this.level+" para usar este item.");
			return;
		}
		if(this.id == 39)
		{
			for(int i = 0;i <= Constant.ITEMSLOT_MAX;i++)
			{
				player.inventory[i] = null;
			}
			player.inventory[0] = new Item().SetInfos(76);
			player.inventory[1] = new Item().SetInfos(76);
			player.inventory[2] = new Item().SetInfos(77);
			player.inventory[3] = new Item().SetInfos(31);
			player.inventory[4] = new Item().SetInfos(163);
			player.inventory[5] = new Item().SetInfos(151);
			player.inventory[6] = new Item().SetInfos(164);
			player.inventory[7] = new Item().SetInfos(165);
			player.inventory[8] = new Item().SetInfos(166);
			player.changeClass(ArkaClass.KNIGHT);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
			/*NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			if(bragus != null)
				bragus.showDialog(150);*/
		}
		else if(this.id == 40)
		{
			for(int i = 0;i <= Constant.ITEMSLOT_MAX;i++)
			{
				player.inventory[i] = null;
			}
			player.inventory[Constant.ITEMSLOT_LEFT] = new Item().SetInfos(160);
			player.inventory[Constant.ITEMSLOT_BODY] = new Item().SetInfos(161);
			player.inventory[0] = new Item().SetInfos(163);
			player.inventory[1] = new Item().SetInfos(151);
			player.inventory[2] = new Item().SetInfos(164);
			player.inventory[3] = new Item().SetInfos(165);
			player.inventory[4] = new Item().SetInfos(166);
			player.changeClass(ArkaClass.MAGE);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
			/*NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			if(bragus != null)
				bragus.showDialog(150);*/
		}
		else if(this.id == 41)
		{
			player.changeClass(ArkaClass.ROGUE);
			for(int i = 0;i <= Constant.ITEMSLOT_MAX;i++)
			{
				player.inventory[i] = null;
			}
			player.inventory[0] = new Item().SetInfos(15);
			player.inventory[1] = new Item().SetInfos(163);
			player.inventory[2] = new Item().SetInfos(151);
			player.inventory[3] = new Item().SetInfos(164);
			player.inventory[4] = new Item().SetInfos(165);
			player.inventory[5] = new Item().SetInfos(166);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
			/*NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			if(bragus != null)
				bragus.showDialog(150);*/
		}
		else if(this.id == 42)
		{
			for(int i = 0;i <= Constant.ITEMSLOT_MAX;i++)
			{
				player.inventory[i] = null;
			}
			player.inventory[0] = new Item().SetInfos(3);
			player.inventory[9] = new Item().SetInfos(163);
			player.inventory[10] = new Item().SetInfos(151);
			player.inventory[11] = new Item().SetInfos(164);
			player.inventory[12] = new Item().SetInfos(165);
			player.inventory[13] = new Item().SetInfos(166);
			player.changeClass(ArkaClass.RANGER);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
			/*NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			if(bragus != null)
				bragus.showDialog(150);*/
		}
		else if(this.id == 88)
		{
			for(int i = 0;i <= Constant.ITEMSLOT_MAX;i++)
			{
				player.inventory[i] = null;
			}
			Item item = new Item().SetInfos(161);
			item.dyed = true;
			item.dye = new Color(1f, 0.2f, 0.2f, 1f);
			player.inventory[Constant.ITEMSLOT_LEFT] = new Item().SetInfos(160);
			player.inventory[Constant.ITEMSLOT_BODY] = item;
			player.inventory[0] = new Item().SetInfos(163);
			player.inventory[1] = new Item().SetInfos(151);
			player.inventory[2] = new Item().SetInfos(164);
			player.inventory[3] = new Item().SetInfos(165);
			player.inventory[4] = new Item().SetInfos(166);
			player.changeClass(ArkaClass.HEMOMANCER);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
		}
		else if(this.id == 89)
		{
			if(player.getActualHealth(true) >= 1f)
			{
				return;
			}
			player.heal(20 + player.maxHealth * 0.05f);
			for(int i = 0;i < 15;i++)
			{
				Particle.Create(player.randomHitBoxPosition(), Vector2.Zero, 16, new Color(0.4f, 1f, 0.4f, 1f), 2f, 1.5f, 1f);
			}
			this.stacks--;
		}
		else if(this.id == 90)
		{
			if(player.getActualHealth(true) >= 1f)
			{
				return;
			}
			player.heal(200 + player.maxHealth * 0.15f);
			for(int i = 0;i < 50;i++)
			{
				Particle p = Particle.Create(player.randomHitBoxPosition(), Vector2.Zero, 16, new Color(0.4f, 1f, 0.4f, 1f), 2f, 1.75f, 1f);
				p.setLight(16, p.color);
			}
			this.stacks--;
		}
		else if(this.id == 91)
		{
			if(player.getActualHealth(true) >= 1f)
			{
				return;
			}
			player.heal(500 + player.maxHealth * 0.4f);
			for(int i = 0;i < 80;i++)
			{
				Particle p = Particle.Create(player.randomHitBoxPosition(), Vector2.Zero, 16, new Color(0.4f, 1f, 0.4f, 1f), 2f, 2f, 1f);
				p.setLight(16, p.color);
			}
			for(int i = 0;i < 20;i++)
			{
				Particle p = Particle.Create(player.randomHitBoxPosition(), new Vector2(MathUtils.random(-60, 60), 50f), 16, new Color(0.4f, 1f, 0.4f, 1f), -2f, 2f, 1f);
				p.setLight(16, p.color);
			}
			this.stacks--;
		}
		else if(this.id == 92)
		{
			player.changeClass(ArkaClass.SAMURAI);
			player.inventory[0] = new Item().SetInfos(76);
			player.inventory[1] = new Item().SetInfos(76);
			player.inventory[2] = new Item().SetInfos(77);
			player.inventory[3] = new Item().SetInfos(31);
			player.inventory[4] = new Item().SetInfos(163);
			player.inventory[5] = new Item().SetInfos(151);
			player.inventory[6] = new Item().SetInfos(164);
			player.inventory[7] = new Item().SetInfos(165);
			player.inventory[8] = new Item().SetInfos(166);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
		}
		else if(this.id == 93)
		{
			player.changeClass(ArkaClass.LANCER);
			player.inventory[0] = new Item().SetInfos(76);
			player.inventory[1] = new Item().SetInfos(76);
			player.inventory[2] = new Item().SetInfos(77);
			player.inventory[3] = new Item().SetInfos(31);
			player.inventory[4] = new Item().SetInfos(163);
			player.inventory[5] = new Item().SetInfos(151);
			player.inventory[6] = new Item().SetInfos(164);
			player.inventory[7] = new Item().SetInfos(165);
			player.inventory[8] = new Item().SetInfos(166);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
		}
		else if(this.id == 94)
		{
			player.changeClass(ArkaClass.CASSINOGAMBLER);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
		}
		else if(this.id == 95)
		{
			player.changeClass(ArkaClass.ARCANETRICKSTER);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
		}
		else if(this.id >= 115 && this.id <= 119)
		{
			Recipe r = Recipe.getUnlockableItem(player, this.quality);
			if(this.data[0] > 0)
			{
				r = Recipe.getRecipeForItem(this.data[0]);
				if(r == null)
				{
					Main.lastQuestText = Main.lv("Error getting item recipe.", "Erro ao obter receita do item.");
					Main.lastQuestTicks=0;
					return;
				}
			}
			if(r != null && !player.learnedRecipes.contains(r))
			{
				for(int j = 0;j < 2;j++)
				{
					Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
					for(int i = -2;i <= 2;i++)
					{
						Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
						p.drawFront = true;
						p.rotation = 90f;
						p.height = 128;
						p.specialAi = 90;
						p.parent = this;
					}
				}
				player.learnedRecipes.add(r);
				if(r.bundleid != null)
				{
					for(Recipe r2 : Recipe.values())
					{
						if(r2.bundleid == r.bundleid && !player.learnedRecipes.contains(r2))
						{
							player.learnedRecipes.add(r2);
						}
					}
				}
				player.updateRecipes();
				Main.lastQuestTicks=0;
				String name = (new Item().SetInfos(r.itemid)).name;
				if(r.bundleid != Bundle.GENERIC && r.bundleid != null)
				{
					Main.lastQuestText = Main.lv("You learned the " + r.bundleid.name + " recipes.", "Você aprendeu as receitas de " + r.bundleid.name +".");
				}
				else
				{
					Main.lastQuestText = Main.lv("You learned the " + name + " recipe.", "Você aprendeu a receita de " + name+".");
				}
				this.stacks--;
			}
			else
			{
				Main.lastQuestTicks=0;
				if(this.data[0] > 0)
					Main.lastQuestText = Main.lv("You already learned this recipe.", "Você já aprendeu esta receita.");
				else
					Main.lastQuestText = Main.lv("No new recipes.", "Não há receitas novas.");
			}
		}
		else if(this.id == 137)
		{
			if(player.getActualHealth(true) >= 1f)
			{
				return;
			}
			player.heal(10 + player.maxHealth * 0.03f);
			for(int i = 0;i < 20;i++)
			{
				Particle p = Particle.Create(player.Center().add(player.direction * 12, 12).add(MathUtils.random(-6, 6), MathUtils.random(-6, 6)), new Vector2(MathUtils.random(-100, 100), MathUtils.random(50, 80)), 2, new Color(1f, 0.2f, 0.2f, 1f), -2f, 1.5f, 1f);
				p.rotation = MathUtils.random(360);
			}
			this.stacks--;
		}
		else if(this.id == 151)
		{
			if(this.infos[0].expression.equalsIgnoreCase("1"))
			{
				this.infos[0].expression = "0";
			}
			else
			{
				this.infos[0].expression = "1";
			}
		}
		else if(this.id == 152)
		{
			player.changeClass(ArkaClass.PYROMANCER);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
		}
		else if(this.id == 167)
		{
			player.changeClass(ArkaClass.REAPER);
			for(int j = 0;j < 2;j++)
			{
				Color c = new Color(j * 0.5f, j * 0.5f, j * 0.5f, 1f);
				for(int i = -2;i <= 2;i++)
				{
					Particle p = Particle.Create(new Vector2(player.Center().x + i * 8, player.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
					p.drawFront = true;
					p.rotation = 90f;
					p.height = 128;
					p.specialAi = 90;
					p.parent = this;
				}
			}
			this.stacks--;
		}
		else if(this.id == 171)
		{
			if(!player.grounded)
			{
				Main.lastQuestText = Main.lv("You can't use in the air.", "Você não pode usar no ar.");
				Main.lastQuestTicks = 0;
				return;
			}
			NPC n = NPC.Create(player.Center(), player.myMapX, player.myMapY, 34);
			n.temporary = true;
			n.infos[0] = 1800;
			this.stacks--;
		}
	}

	public void onAttack(Player player)
	{
		if(player.haveBuff(11) != -1)
		{
			player.removeBuff(11);
			player.invincible = 1.5f;
		}
		player.removeBuff(27);

		if(this.id == 34)
		{
			player.inventory[Constant.ITEMSLOT_LEFT] = null;
		}
		else if(this.id == 38)
		{
			NPC n = SaveInfos.findNPCWithID(Constant.NPCID_LEWIN);
			if(n != null)
				n.showDialog(111);
		}
	}

	public void onHit(Monster m, Player player)
	{
		if(m.natural)
			return;
		
		if(this.id == 1)
		{
			m.addBuff(2, 3f, player);
		}
		else if(this.id == 2)
		{
			Skill s = player.skills[10];
			if(s != null)
			{
				s.cdf -= 0.25f;
			}
		}
		else if(this.id == 4)
		{
			int slot;
			player.addBuff(1, 1, this.getInfoValueI(1), 5f, this);
			if((slot = player.haveBuff(1)) != -1)
			{
				int extraDam = this.getInfoValueI(0);
				m.hurt(extraDam * player.buffs[slot].stacks, 0, 1f, false, Constant.DAMAGETYPE_FIRE, player);
			}
		}
		else if(this.id == 5)
		{
			int slot = m.haveBuff(3);
			if(slot != -1)
			{
				if(m.buffs[slot].stacks == 3)
				{
					m.hurt(this.getInfoValueI(0), 0, 0f, true, Constant.DAMAGETYPE_PHYSICAL, player);
					m.removeBuff(3);
				}
			}
			m.addBuff(3, 4f, player);
		}
		else if(this.id == 12)
		{
			int slot = player.addBuff(4, 30f, this);
			m.hurt((int) (this.damage * (0.3f * player.buffs[slot].stacks)), 0, 0f, player.canCritical(m), this.damageType, player);
		}
		else if(this.id == 14)
		{
			float mult = (m.boss ? 0.02f : 0.05f);
			float extraDamage = (player.maxHealth + m.health * mult);
			m.hurt((int)(extraDamage), 0, 0f, player.canCritical(m), Constant.DAMAGETYPE_HOLY, player);
		}
		else if(this.id == 15)
		{
			NPC n = SaveInfos.findNPCWithID(Constant.NPCID_LUSHMAEL);
			if(n != null && m.id == 3)
				n.showDialog(86);
		}
		else if(this.id == 26)
		{
			player.addBuff(8, 1, this.getInfoValueI(0), 3f, this);
		}
		else if(this.id == 27)
		{
			player.addBuff(9, this.getInfoValueF(0), this);
		}
		else if(this.id == 30)
		{
			//player.heal(this.damage * (this.getInfoValueF(0)/100f));
			player.heal(this.getInfoValueF(0));
		}
		else if(this.id == 32)
		{
			m.addBuff(13, 5f, player);
		}
		else if(this.id == 35)
		{
			player.hurt(10, -player.direction, 1, player);
			NPC n = SaveInfos.findNPCWithID(Constant.NPCID_LEONIDAS);
			if(n != null)
				n.showDialog(50);
		}
		else if(this.id == 44)
		{
			if(player.animFrame == 5 && m.haveBuff(16) != -1)
			{
				m.hurtDmgVar((int) (this.damage * 0.5f), 0, 0, player.canCritical(this, m), this.damageType, this.itemclass, player);
			}
			if(player.animFrame == 15 && m.haveBuff(16) == -1)
			{
				m.addBuff(15, 1.5f, player);
				m.addBuff(16, 6f, player);
				for(int i = 0;i < 200;i++)
				{
					Particle.Create(m.randomHitBoxPosition(), new Vector2(MathUtils.random(-100, 100), MathUtils.random(-100, 100)), 2, new Color(0f, 1f, 1f, 1f), -2f, 2f, 1f);
				}
			}
			if(player.animFrame == 20 && m.haveBuff(15) != -1)
			{
				if(m.health <= m.maxHealth * 0.2f)
				{
					m.hurt(m.health+1, 0, 0f, true, Constant.DAMAGETYPE_HOLY, player);
					for(int j = 0;j < 2;j++)
					{
						Color c = new Color(0f, 1f, 1f, 1f);
						for(int i = -2;i <= 2;i++)
						{
							Particle p = Particle.Create(new Vector2(m.Center().x + i * 8, m.position.y + 16), Vector2.Zero, 8, c, 0f, 0.5f, 1f);
							p.drawFront = true;
							p.rotation = 90f;
							p.height = 128;
							p.specialAi = 90;
							p.parent = m;
						}
					}
				}
			}
		}
		else if(this.id == 52)
		{
			player.addBuff(19, this.getInfoValueF(1), this);
		}
		else if(this.id == 59)
		{
			if(m.haveBuff(22) == -1)
			{
				float info0 = this.getInfoValueF(0)/100f;
				m.hurtDmgVar((int)(m.health * info0), 0, 0f, false, Constant.DAMAGETYPE_PHYSICAL, Constant.ITEMCLASS_PASSIVE, player);
				m.addBuff(22, 120, player);
			}
		}
		else if(this.id == 63 || this.id == 64)
		{
			if(MathUtils.randomBoolean(0.1f))
			{
				m.hurtDmgVar(this.getInfoValueI(0), 0, 0f, false, Constant.DAMAGETYPE_PHYSICAL, player);
			}
		}
		else if(this.id == 80)
		{
			if(m.fleshandblood)
			{
				int extraDamage = (int) (this.damage * (this.getInfoValueF(0)/100f));
				m.hurtDmgVar(extraDamage, 0, 0f, player.canCritical(m), damageType, player);
			}
		}
		else if(this.id == 86)
		{
			if(MathUtils.random(0, 100) <= this.getInfoValueF(0))
			{
				m.Stun(1f, player);
			}
		}
		
		int count;
		if((count = Set.playerItemSetCount(player, Set.PUNISHMENT)) >= 2 && player.haveBuff(20) == -1)
		{
			float mult = 0.04f;
			if(this.isMelee())
				mult *= 2f;
			
			m.hurt((int) (m.health * mult), 0, 0, false, Constant.DAMAGETYPE_HOLY, player);
			if(count < 4)
			{
				player.addBuff(20, 0.5f, this);
			}
			else
			{
				player.addBuff(20, 0.05f, this);
			}
		}
	}

	public void onKill(Monster monster, Player player)
	{
		if(this.id == 12)
		{
			int slot = player.addBuff(4, 30f, this);
			player.buffs[slot].stacks = 5;
		}
	}

	public Texture getTexture()
	{
		Texture text = Content.hitems[this.id-1];
		if(this.overrideStandTexture !=  null)
			text = this.overrideStandTexture;
		else if(Content.hitems[this.id-1] == null)
			text = getInvTexture();

		return text;
	}

	public Texture getInvTexture()
	{
		return Content.items[this.id-1];
	}

	public Texture getAttackTexture()
	{
		Texture text = Content.aitems[this.id-1];
		if(this.overrideAttackTexture !=  null)
			text = this.overrideAttackTexture;
		else if(Content.aitems[this.id-1] == null)
			text = getInvTexture();
		else if(this.currentAnimation > 0 && this.extraAnimations.size() > 0)
		{
			Iterator<Entry<Integer, Integer>> it = this.extraAnimations.entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Entry<Integer, Integer> pair = (Entry<Integer, Integer>)it.next();
				i++;
				if(i >= this.currentAnimation)
				{
					text = !this.runningDiagAttack ? Content.extras[pair.getKey()] : Content.extras[pair.getValue()];
					break;
				}
			}
		}

		return text;
	}
	
	public boolean isDoubleAnimation()
	{
		if(this.currentAnimation > 0 && this.extraAnimations.size() > 0)
		{
			Iterator<Entry<Integer, Integer>> it = this.extraAnimations.entrySet().iterator();
			int i = 0;
			while (it.hasNext()) {
				Entry<Integer, Integer> pair = (Entry<Integer, Integer>)it.next();
				i++;
				if(i >= this.currentAnimation)
				{
					return pair.getValue() != -1;
				}
			}
		}
		return this.diagonalAttackBase;
	}
	
	public Texture getWorldTexture()
	{
		Texture text = Content.witems[this.id-1];
		if(Content.witems[this.id-1] == null)
			text = getTexture();

		return text;
	}

	private Texture getHeadSTexture()
	{
		Texture text = Content.heads[this.id-1];
		if(Content.heads[this.id-1] == null)
			text = getInvTexture();

		return text;
	}

	private Texture getHeadWTexture()
	{
		Texture text = Content.headw[this.id-1];
		if(Content.headw[this.id-1] == null)
		{
			text = getHeadSTexture();
		}

		return text;
	}

	private Texture getHeadRTexture()
	{
		Texture text = Content.headr[this.id-1];
		if(Content.headr[this.id-1] == null)
		{
			text = getInvTexture();
		}

		return text;
	}
	
	private Texture getDHeadSTexture()
	{
		Texture text = Content.dheads[this.id-1];
		if(Content.dheads[this.id-1] == null)
			text = getHeadSTexture();

		return text;
	}

	private Texture getDHeadWTexture()
	{
		Texture text = Content.dheadw[this.id-1];
		if(Content.dheadw[this.id-1] == null)
		{
			text = getDHeadSTexture();
		}

		return text;
	}

	private Texture getDHeadRTexture()
	{
		Texture text = Content.dheadr[this.id-1];
		if(Content.dheadr[this.id-1] == null)
		{
			text = getHeadRTexture();
		}

		return text;
	}

	private void ResetStats()
	{
		this.name = "Unnamed";
		this.description = "";
		this.baseDescription = "";
		for(int i = 0;i < 10;i++)
		{
			this.infos[i] = new MathEx();
		}
		this.descriptionColor = Color.WHITE;
		this.trivia = "";
		this.triviaColor = Color.YELLOW;
		this.attackFrames = 1;
		this.hurtFrame = 0;
		this.damage = 0;
		this.damageFormula = "0";
		this.quality = Constant.QUALITY_COMMON;
		this.handCrafted = false;
		this.type = Constant.ITEMTYPE_LEFT;
		this.damageType = Constant.DAMAGETYPE_PHYSICAL;
		this.standFrames = 1;
		this.walkFrames = 1;
		this.overrideAttack = Constant.OVERRIDEATTACK_SWING;
		this.overrideStand = Constant.OVERRIDESTAND_HILT;
		this.dontAttack = false;
		this.wleftOffsetX = -1;
		this.canFlipAttack = true;
		this.offsetX = 8;
		this.offsetY = 8;
		this.leftOffsetX = 0;
		this.roffsetX = 0;
		this.canStack = false;
		this.roffsetY = 0;
		this.woffsetX = 0;
		this.woffsetY = 0;
		this.offsetRotateAttack = Vector2.Zero.cpy();
		this.rotateOnDualEquip = false;
		this.dualHanded = false;
		this.dualHandedAttack = false;
		this.shootAccuracy = 0f;
		this.overrideStandTexture = null;
		this.overrideStandTextureFrames = 1;
		this.overrideAttackTexture = null;
		this.overrideAttackTextureFrames = 1;
		this.itemclass = Constant.ITEMCLASS_MATERIAL;
		this.debugging = false;
		this.standDrawHand = true;
		this.shootProj = 0;
		this.shootSpeed = 0f;
		this.spawnProjGunMode = false;
		this.rotateAfterAttack = false;
		this.sneakyCritical = false;
		this.usable = false;
		this.extraHits.clear();
		this.rotateLeftFix = false;
		this.partOfSet = null;
		this.spawnProjectileOffset = new Vector2();
	}

	public void onEquip(Player player, int itemslot, boolean shouldEcho)
	{
		this.holder = player;
		if(itemslot == Constant.ITEMSLOT_RIGHT)
			this.equippedHand = 1;
		else
			this.equippedHand = 0;
		
		player.skills[10] = this.specialSkill;
		if(this.id == 7)
		{
			for(int i = 0;i < 4;i++)
			{
				Projectile p = Projectile.Summon(3, player.Center().cpy().add(5, 800 + i * 150), Vector2.Zero.cpy(), this.damage, 10f, player);
				p.ai[5] = 1;
				p.ai[6] = 1;
			}
		}
		
		if(shouldEcho && Main.shouldNet() && player.whoAmI == Main.me)
		{
			SomeRequest request = new SomeRequest("EquipItem " + player.whoAmI + " " + this.id + " " + itemslot);
			Main.client.client.sendTCP(request);
		}
	}

	public void onDeEquip(Player player, int itemslot, boolean shouldEcho)
	{
		this.holder = player;
		if(this.type == Constant.ITEMSLOT_LEFT)
		{
			player.skills[10] = null;
		}
		
		if(shouldEcho && Main.shouldNet() && player.whoAmI == Main.me)
		{
			SomeRequest request = new SomeRequest("DeEquipItem " + player.whoAmI + " " + itemslot);
			Main.client.client.sendTCP(request);
		}

	}

	public static Item Create(Vector2 pos, Vector2 vel, int type, int gx, int gy, boolean serverPermission)
	{
		Item p = new Item();
		p.SetInfos(type);
		p.inWorld=true;
		p.timeInWorld = 0f;
		p.position = pos.cpy();
		p.velocity = vel.cpy();
		Sprite sprite = new Sprite(p.getInvTexture());
		p.myMapX = gx;
		p.myMapY = gy;
		p.width = (int) sprite.getWidth();
		p.height = (int) sprite.getHeight();
		Main.items.add(p);
		return p;
	}

	public static Item Create2(Vector2 pos, Vector2 vel, int type, int gx, int gy)
	{
		Item p = new Item();
		p.SetInfos(type);
		p.inWorld=true;
		p.timeInWorld = 0f;
		p.position = pos.cpy();
		p.velocity = vel.cpy();
		p.myMapX = gx;
		p.myMapY = gy;
		Sprite sprite = new Sprite(p.getInvTexture());
		p.width = (int) sprite.getWidth();
		p.height = (int) sprite.getHeight();
		Main.worldMap.items.add(p);
		return p;
	}


	@Override
	public void update(float delta)
	{
		if(this.inWorld)
		{
			this.timeInWorld += delta;
			this.velocity.y += Main.gravity/1.5f;
			this.velocity.x *= 0.975f;
			float addAmountX = this.velocity.x * delta;
			float newX = this.position.x + addAmountX;
			float addAmountY = this.velocity.y * delta;
			float newY = this.position.y + addAmountY;

			if(!Main.worldMap.doesRectCollideWithMap(this.position.x, newY, this.width, this.height))
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

			if(!Main.worldMap.doesRectCollideWithMap(newX, this.position.y, this.width, this.height))
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
			boolean closest = true;
			for(Item i : Main.items)
			{
				if(!i.equals(this) && i.inWorld && i.Center().dst(Main.player[Main.me].Center()) < this.Center().dst(Main.player[Main.me].Center()))
				{
					closest = false;
					break;
				}
			}
			if(this.timeInWorld >= 0.5f && Gdx.input.isKeyPressed(Keys.X) && closest && !Main.gotThisFrame && this.Center().dst(Main.player[Main.me].Center()) < 80)
			{
				int slot = -1;
				boolean shouldStack = false;
				Player p = Main.player[Main.me];
				for(int i = 0;i < Constant.ITEMSLOT_OFFSET;i++)
				{
					if(p.inventory[i] != null && (p.inventory[i].canStack && p.inventory[i].id == this.id && p.inventory[i].stacks < 100))
					{
						slot = i;
						shouldStack = true;
						break;
					}
				}
				if(slot == -1)
				{
					for(int i = 0;i < Constant.ITEMSLOT_OFFSET;i++)
					{
						if(p.inventory[i] == null)
						{
							slot = i;
							break;
						}
					}
				}
				if(slot > -1)
				{
					if(!shouldStack)
					{
						Main.player[Main.me].inventory[slot] = this;
						this.inWorld = false;
						Main.gotThisFrame = true;
					}
					else
					{
						p.inventory[slot].stacks++;
						Main.gotThisFrame = true;
						this.inWorld = false;
					}
				}
			}
		}
	}
	
	public void destroy()
	{
		this.stacks = 0;
		if(this.inWorld)
		{
			this.inWorld = false;
			Main.items.remove(this);
		}
	}

	public void drawInWorld(SpriteBatch batch)
	{
		this.holder = null;
		Sprite sprite = new Sprite(this.getInvTexture());
		sprite.setPosition(this.position.x, this.position.y);
		this.drawEnt(batch, sprite);
		boolean closest = true;
		for(Item i : Main.items)
		{
			if(!i.equals(this) && i.inWorld && i.Center().dst(Main.player[Main.me].Center()) < this.Center().dst(Main.player[Main.me].Center()))
			{
				closest = false;
				break;
			}
		}
		if(closest && this.Center().dst(Main.player[Main.me].Center()) < 80)
		{
			Sprite pressx = new Sprite(Content.pressx);
			pressx.setPosition(sprite.getX()+sprite.getWidth()/2-pressx.getWidth()/2, sprite.getY()+sprite.getHeight()+8);
			Main.prettySpriteDraw(pressx, batch);
		}
	}
	
	public void drawInWorldPost(SpriteBatch batch)
	{
		this.holder = null;
		boolean closest = true;
		for(Item i : Main.items)
		{
			if(!i.equals(this) && i.inWorld && i.Center().dst(Main.player[Main.me].Center()) < this.Center().dst(Main.player[Main.me].Center()))
			{
				closest = false;
				break;
			}
		}
		if(closest && this.Center().dst(Main.player[Main.me].Center()) < 80 && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
		{
			Main.displayItemStats(this, new Vector2(this.position.x - Main.cameraRealPosition2().x - 8, (this.position.y + this.height) - Main.cameraRealPosition2().y));
		}
	}

	public int getStandFrames()
	{
		int frames = this.standFrames;
		if(this.overrideStandTexture != null)
			frames = this.overrideStandTextureFrames;

		return frames;
	}

	public int getAttackFrames()
	{
		int frames = this.attackFrames;
		if(this.overrideAttackTexture != null)
			frames = this.overrideAttackTextureFrames;

		return frames;
	}
	
	public boolean isMelee()
	{
		return Main.isMelee(this.itemclass);
	}
	
	public boolean isRanged()
	{
		if(this.itemclass == Constant.ITEMCLASS_RANGED)
			return true;
		
		return false;
	}
	
	public boolean isMagic()
	{
		if(this.itemclass == Constant.ITEMCLASS_MAGIC)
			return true;
		
		return false;
	}
	
	public void updateDescription()
	{
		String desc = this.baseDescription;
		double info;
		info = MathEx.evaluate(this.infos[0].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[0].useAsInteger)
			desc = Pattern.compile("#0").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#0").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[1].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[1].useAsInteger)
			desc = Pattern.compile("#1").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#1").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[2].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[2].useAsInteger)
			desc = Pattern.compile("#2").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#2").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[3].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[3].useAsInteger)
			desc = Pattern.compile("#3").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#3").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[4].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[4].useAsInteger)
			desc = Pattern.compile("#4").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#4").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[5].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[5].useAsInteger)
			desc = Pattern.compile("#5").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#5").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[6].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[6].useAsInteger)
			desc = Pattern.compile("#6").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#6").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[7].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[7].useAsInteger)
			desc = Pattern.compile("#7").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#7").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[8].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[8].useAsInteger)
			desc = Pattern.compile("#8").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#8").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		info = MathEx.evaluate(this.infos[9].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
		if(this.infos[9].useAsInteger)
			desc = Pattern.compile("#9").matcher(desc).replaceAll(String.valueOf((int)info));
		else
			desc = Pattern.compile("#9").matcher(desc).replaceAll(String.valueOf(new DecimalFormat("#.##").format(info)));

		desc = Pattern.compile("\\}").matcher(desc).replaceAll("[]");
		desc = Pattern.compile("\\{G").matcher(desc).replaceAll("["+Constant.QUALITYCOLORNAME[Constant.QUALITY_EPIC]+"]");
		desc = Pattern.compile("\\{R").matcher(desc).replaceAll("[SCARLET]");
		desc = Pattern.compile("\\{C").matcher(desc).replaceAll("[CYAN]");
		desc = Pattern.compile("\\{O").matcher(desc).replaceAll("[GOLDENROD]");
		desc = Pattern.compile("\\{").matcher(desc).replaceAll("["+Constant.QUALITYCOLORNAME[this.quality]+"]");
		desc = Pattern.compile("-=").matcher(desc).replaceAll("[ORANGE]");
		desc = Pattern.compile("=-").matcher(desc).replaceAll("[]");
		
		this.description = desc;
	}
	
	public float getInfoValueF(int infoNum)
	{
		return (float)MathEx.evaluate(this.infos[infoNum].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
	}
	
	public int getInfoValueI(int infoNum)
	{
		return (int)MathEx.evaluate(this.infos[infoNum].expression.replaceAll("lvl", String.valueOf(this.level)).replaceAll("dmg", String.valueOf(this.damage)));
	}
	
	public void updateQuality()
	{
		if(this.level < 20)
			this.quality = Constant.QUALITY_COMMON;
		else if(this.level < 40)
			this.quality = Constant.QUALITY_UNCOMMON;
		else if(this.level < 60)
			this.quality = Constant.QUALITY_RARE;
		else if(this.level < 80)
			this.quality = Constant.QUALITY_EPIC;
		else
			this.quality = Constant.QUALITY_LEGENDARY;
	}
	
	public void updateDamage()
	{
		this.damage = (int)MathEx.evaluate(this.damageFormula.replaceAll("lvl", String.valueOf(this.level)));
	}
	
	public void reforge(int level)
	{
		this.level = level;
		this.updateDamage();
		//this.updateQuality();
		this.updateDescription();
	}
	
	public void updateItemLanguage()
	{
		Item item = new Item().SetInfos(this.id);
		this.name = item.name;
		this.baseDescription = item.baseDescription;
		this.trivia = item.trivia;
		this.updateDescription();
	}


	public boolean isWeapon()
	{
		return (this.isMelee() || this.isRanged() || this.isMagic());
	}
	
	@Override
	protected String makeShaderReplacements(String str, Sprite sprite)
	{
		return str
				.replaceAll("ticks", String.valueOf(Main.gameTick))
				.replaceAll("ex", ""+(this.holder != null ? this.holder.Center().x : this.Center().x))
				.replaceAll("ey", ""+(this.holder != null ? this.holder.Center().y : this.Center().y))
				.replaceAll("cx", ""+(Main.camera == null ? Roguelike.ticks : Main.camera.position.x))
				.replaceAll("cy", ""+(Main.camera == null ? Roguelike.ticks : Main.camera.position.y))
				.replaceAll("ed", ""+(this.holder != null ? this.holder.direction : 1))
				.replaceAll("#0", ""+Main.var[0])
				.replaceAll("sw", ""+sprite.getWidth())
				.replaceAll("sh", ""+sprite.getHeight())
				.replaceAll("scr", ""+(this.holder != null?this.holder.getPCShaderArray()[0]:0f))
				.replaceAll("scg", ""+(this.holder != null?this.holder.getPCShaderArray()[1]:0f))
				.replaceAll("scb", ""+(this.holder != null?this.holder.getPCShaderArray()[2]:0f));
	}
}