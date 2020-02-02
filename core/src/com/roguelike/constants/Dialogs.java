package com.roguelike.constants;

import java.util.regex.Pattern;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.entities.Entity;
import com.roguelike.entities.Monster;
import com.roguelike.entities.NPC;
import com.roguelike.entities.Particle;
import com.roguelike.entities.Player;
import com.roguelike.entities.Prop;
import com.roguelike.game.Event;
import com.roguelike.game.Main;
import com.roguelike.game.Roguelike;
import com.roguelike.game.SaveInfos;
import com.roguelike.world.GameMap;

public class Dialogs
{
	public String text;
	public String[] options = new String[4];
	public int[] optionsDialog = new int[4];
	public int id;
	public boolean playerDialog;
	public boolean canExit;
	public boolean actionOnly;
	public int npcPreference;

	public Dialogs setInfos(int id)
	{
		this.id = id;
		resetStats();
		this.setOption(0, ". . .", this.id+1);
		Player player = Constant.getPlayerList()[Main.me];
		if(this.id == 1)
		{
			this.text = "Hello, what a little boy like you is doing in a cold and dark night like that?";
			this.setOption(0, "Say you don't have anywhere to go.", 5);
			this.setOption(1, "Try to make him go away.", 6);
			this.setOption(2, "Ignore", 8);
			this.setOption(3, "Clean dialog", 10);
		}
		else if(this.id == 2)
		{
			this.text = "I know, almost nobody in this city have, but do you know what I have?";
			this.setOption(0, "What?", 3);
		}
		else if(this.id == 3)
		{
			this.text = "You.";
			this.setOption(0, ". . .", 4);
		}
		else if(this.id == 4)
		{
			this.text = "*Throws smoke bomb*";
			this.setOption(0, ". . .", -1);
		}
		else if(this.id == 5)
		{
			this.text = "I don't have anywhere to go.";
			this.setOption(0,  ". . .", 2);
			this.playerDialog = true;
		}
		else if(this.id == 6)
		{
			this.text = "Trying to avoid strangers like you, could you go away?";
			this.setOption(0, ". . .", 7);
			this.playerDialog = true;
		}
		else if(this.id == 7)
		{
			this.text = "No.";
			this.setOption(0, ". . .", 4);
		}
		else if(this.id == 8)
		{
			this.text = ". . .";
			this.setOption(0, ". . .", 9);
			this.playerDialog = true;
		}
		else if(this.id == 9)
		{
			this.text = "I don't like bad kids.";
			this.setOption(0, ". . .", 4);
		}
		else if(this.id == 10)
		{
			this.text = "Some text";
			this.setOption(0, "Some options", -1);
		}
		else if(this.id == 11)
		{
			this.text = "Hello.";
			this.setOption(0, ". . .", 12);
			this.playerDialog = true;
		}
		else if(this.id == 12)
		{
			this.text = "Hey, you\nWhere are you going to?";
			this.setOption(0, "Nowhere", 13);
		}
		else if(this.id == 13)
		{
			this.text = "I'm going nowhere, I just left my town";
			this.setOption(0, ". . .", 14);
			this.playerDialog = true;
		}
		else if(this.id == 14)
		{
			this.text = "It's strange to hear someone I never met\nBut tell me, what happened?";
			this.setOption(0, "Tell him", 15);
		}
		else if(this.id == 15)
		{
			this.text = "So... I lived all my life in Thrash City, I never saw the world out there, but now I don't have anywhere to go.";
			this.setOption(0, ". . .", 16);
			this.playerDialog = true;
		}
		else if(this.id == 16)
		{
			this.text = "And I can't return to Thrash, the archers in the towers would kill me if they see me out of the city.";
			this.setOption(0, ". . .", 17);
			this.playerDialog = true;
		}
		else if(this.id == 17)
		{
			if(Main.isAtDay())
				this.text = "Oh, I don't like to see people this way\nIt's so bad to feel bad at day\nWorld is dangerous like a dark cave\nDon't you want to live in Stor Gave?";
			else
				this.text = "Man, I'm feeling so tight\nDon't you want to stay here this night?\nIf you need me, I can stay together\nAnd you can live here forever.";

			this.setOption(0, "Yes", 18);
			this.setOption(1, "You know, you can't really refuse, just accept it", 18);
		}
		else if(this.id == 18)
		{
			this.text = "Really? Yes I do.";
			this.setOption(0, ". . .", 19);
			this.playerDialog = true;
		}
		else if(this.id == 19)
		{
			this.text = "Nice! But you need to know how to defend yourself\nYou can choose: a bow, a sword, a dagger or a staff\nLet's visit the class masters\nThen you can choose you class";
			this.setOption(0, "Okay, lets que lets", 20);
		}
		else if(this.id == 20)
		{
			this.actionOnly = true;
		}
		else if(this.id == 21)
		{
			this.text = "Hi Victor, I am here with a new apprentice, but he don't know what to be yet. Can you show him a bit about the ranger class?";
			this.setOption(0, ". . .", 22);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 22)
		{
			this.text = "No.";
			this.setOption(0, ". . .", 63);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 23)
		{
			this.text = "Please Victor, he don't have anywhere to go.";
			this.setOption(0, ". . .", 24);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 24)
		{
			this.text = "Also, he is just a kid, he will not read a 5000 pages book.";
			this.setOption(0, ". . .", 25);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 25)
		{
			this.text = "Okay, but you owe me a favor.";
			this.setOption(0, ". . .", 26);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 26)
		{
			this.text = "So, rangers are a ranged class, with high-skilled agility, capables of dodging most attacks and skills while counter-attacking the enemy.";
			this.setOption(0, ". . .", 27);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 27)
		{
			this.text = "Rangers can attack enemies in high distances, causing high damage while being safe.";
			this.setOption(0, ". . .", 28);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 28)
		{
			this.text = "Take this bow and try to shoot an arrow in the dummy.";
			this.setOption(0, "Okay.", -1);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 29)
		{
			this.text = "*face palm*";
			this.setOption(0, ". . .", 30);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 30)
		{
			this.text = "*face palm*";
			this.setOption(0, "Hehe.", 31);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 31)
		{
			this.text = "Ooops, hehe.";
			this.playerDialog = true;
			this.setOption(0, ". . .", 32);
		}
		else if(this.id == 32)
		{
			this.text = "Excuse me.";
			this.setOption(0, ". . .", 33);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 33)
		{
			this.text = "Okay, thanks Victor.";
			this.setOption(0, ". . .", 34);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 34)
		{
			this.text = "You and your favor are welcome.";
			this.setOption(0, "Goodbye", 35);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 35)
		{
			this.text = "Goodbye Victor.";
			this.setOption(0, ". . .", 36);
			this.playerDialog = true;
		}
		else if(this.id == 36)
		{
			this.text = "Bye.";
			this.setOption(0, ". . .", 49);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 37)
		{
			this.text = "Hi Leonidas, I am here with a new apprentice, he is choosing his class, can you tell him about the knights?";
			this.setOption(0, ". . .", 38);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 38)
		{
			this.text = "Of course my brave.";
			this.setOption(0, ". . .", 40);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 40)
		{
			this.text = "Hi boy, what's your name?";
			this.setOption(0, Main.saveName, this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 41)
		{
			this.text = "Hi Leonidas, my name is {PLAYER}.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 42)
		{
			this.text = "So {PLAYER}, listen...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 43)
		{
			this.text = "Knights are warriors specialized in melee combat...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 44)
		{
			this.text = "Depending on your combat style you can choose between resisting damage or doing damage...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 45)
		{
			this.text = "You can hilt heavy melee weapons, light melee weapons, shields, dual swords, everything you can imagine...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 46)
		{
			this.text = "Your skills are based in your strenght and life, using empowered physical attacks and summoning magical weapons to cast special slashes and etc.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 47)
		{
			this.text = "Nice explanation Leonidas, can you demonstrate to him?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 48)
		{
			this.text = "Yes, yes. But first, take this sword and attack the dummy.";
			this.setOption(0, ". . .", -1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 49)
		{
			this.actionOnly = true;
		}
		else if(this.id == 50)
		{
			this.text = "Ho ho my brave...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 51)
		{
			this.text = "Not like this...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 52)
		{
			this.text = "Let me demonstrate to you.";
			this.setOption(0, "Okay", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 53)
		{
			this.text = "";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 54)
		{
			this.text = "Nice slash Leonidas.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 55)
		{
			this.text = "Thanks, but that was slower than I usually do.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 56)
		{
			this.text = "Anyway, keep practicing. Bye.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 57)
		{
			this.text = "Bye Bragus, bye {PLAYER}.";
			this.setOption(0, "Bye", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 58)
		{
			this.text = "Bye Leonidas.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 59)
		{
			this.text = "Please, call me Leon.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 60)
		{
			this.text = "Okay Leon.";
			this.setOption(0, ". . .", 62);
			this.playerDialog = true;
		}
		/*else if(this.id == 61)
		{
			this.text = "Bye Leonidas.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}*/
		else if(this.id == 62)
		{
			this.actionOnly = true;
		}
		else if(this.id == 63)
		{
			this.text = "If you want to learn about rangers get a book and read about it.";
			this.setOption(0, ". . .", 23);
			this.npcPreference = Constant.NPCID_VICTOR;
		}
		else if(this.id == 64)
		{
			this.text = "Hi Lushmael, I am here with a new apprentice, he is choosing his class yet.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 65)
		{
			this.text = "If today is not a bad day for you, can you please tell him about the rogue class?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 66)
		{
			this.text = "";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 6;
		}
		else if(this.id == 67)
		{
			this.text = "Hey.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 68)
		{
			this.text = "Why are you guys talking to my clone?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 69)
		{
			this.text = "Hey Lushmael, are you okay?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 70)
		{
			this.text = "Yes Bragus, I'm fine today.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 71)
		{
			this.text = "Nice, so...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 72)
		{
			this.text = "I am here with a new apprentice, he is choosing his class, can you tell him about the rogues?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 73)
		{
			this.text = "Yes.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 74)
		{
			this.text = "Hello boy, what's your name?";
			this.setOption(0, Main.saveName, this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 75)
		{
			this.text = "Hi Lushmael, my name is {PLAYER}";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 76)
		{
			this.text = "Okay {PLAYER}, the rogue class is simple like a magic cube...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 77)
		{
			this.text = "If you know how to use it then killing people becomes funny...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 78)
		{
			this.text = "If you don't know...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 79)
		{
			this.text = "You know...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 80)
		{
			this.text = "The only thing you can be sure as an assassin is that you will die someday...";
			this.setOption(0, ". . .", 82);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		/*else if(this.id == 81)
		{
			this.text = "... is that you will die someday.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}*/
		else if(this.id == 82)
		{
			this.text = "Aside that, the rogues are a melee class specialized in using sneaky abilities and lethal weapons...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 83)
		{
			this.text = "Receiving a sneaky attack from a high level rogue in a battle can make you lose the battle instantly...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 84)
		{
			this.text = "His abilities are in most offensive or tricky abilities, like poisoned darts, invisibility, dashes, etc...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 85)
		{
			this.text = "If you want to try it, take this dagger and try to stab the dummy.";
			this.setOption(0, ". . .", 96);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 86)
		{
			this.text = "Yes, just like this...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 87)
		{
			this.text = "But you can be a bit more fast and lethal...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 88)
		{
			this.text = "Like...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 89)
		{
			this.text = "Thanks for the demonstration Lushmael.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 90)
		{
			this.text = "You are welcome.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 91)
		{
			this.text = "So...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 92)
		{
			this.text = "We are going to Lewin now, goodbye.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 93)
		{
			this.text = "Bye Bragus, bye {PLAYER}.";
			this.setOption(0, "Goodbye", this.id+1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 94)
		{
			this.text = "Bye.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 95)
		{
			this.actionOnly = true;
		}
		else if(this.id == 96)
		{
			this.text = "I got it from a stranger in the forest.";
			this.setOption(0, ". . .", -1);
			this.npcPreference = Constant.NPCID_LUSHMAEL;
		}
		else if(this.id == 97)
		{
			this.text = "Hi Lewin, I am here with a new apprentice, can you tell him about the mages?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 98)
		{
			this.text = "Depends on his name...";
			this.setOption(0, Main.saveName, 99);
			this.setOption(1, "I have no name", 100);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 99)
		{
			this.text = "Hello Lewin, my name is {PLAYER}.";
			this.setOption(0, ". . .", 101);
			this.playerDialog = true;
		}
		else if(this.id == 100)
		{
			this.text = "I never met my mom, so I don't really know my name.";
			this.setOption(0, ". . .", 102);
			this.playerDialog = true;
		}
		else if(this.id == 101)
		{
			if(Main.saveName.compareToIgnoreCase("Trump") >= 0)
				this.text = "Get out of my house.";
			else
				this.text = "Oh, that's a cool name.";
			
			this.setOption(0, ". . .", 103);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 102)
		{
			this.text = "Oh, sorry boy...";
			this.setOption(0, ". . .", 103);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 103)
		{
			this.text = "So...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 104)
		{
			this.text = "Mages are warriors specialized in using magic...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 105)
		{
			this.text = "And with magic I mean any type of magic...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 106)
		{
			this.text = "Magic do tipo comer seu cu sua rapariga! :DDD...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 107)
		{
			this.text = "Our magic comes from our grimoire, staves, wands, etc...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 108)
		{
			this.text = "Mages studies magic all the time, this means mages are very intelligent...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 109)
		{
			this.text = "And you NEED to be intelligent to use magic, or else they will fail...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 110)
		{
			this.text = "Let me see, take this staff and cast its spell in the dummy...";
			this.setOption(0, ". . .", -1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 111)
		{
			this.text = "I think it's not working...";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 112)
		{
			this.text = "It's cause you are stupid...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 113)
		{
			this.text = "It should be like...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 114)
		{
			this.text = "";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 115)
		{
			this.text = "It seems you are still training Lewin.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 116)
		{
			this.text = "Where you were when I said mages are studying all the time?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 117)
		{
			this.text = "Nofa.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 118)
		{
			this.text = "Anyway, so... {PLAYER}, after listening all these masters talk about their classes which class do you prefer to be?";
			this.setOption(0, "Knight", 119);
			this.setOption(1, "Mage", 120);
			this.setOption(2, "Rogue", 121);
			this.setOption(3, "Ranger", 122);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 119)
		{
			this.text = "Are you sure you want to be a Knight? Your choice is permanent and cannot be reversed.";
			this.setOption(0, "Yes", 123);
			this.setOption(1, "No", 118);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 120)
		{
			this.text = "Are you sure you want to be a Mage? Your choice is permanent and cannot be reversed.";
			this.setOption(0, "Yes", 124);
			this.setOption(1, "No", 118);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 121)
		{
			this.text = "Are you sure you want to be a Rogue? Your choice is permanent and cannot be reversed.";
			this.setOption(0, "Yes", 125);
			this.setOption(1, "No", 118);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 122)
		{
			this.text = "Are you sure you want to be a Ranger? Your choice is permanent and cannot be reversed.";
			this.setOption(0, "Yes", 126);
			this.setOption(1, "No", 118);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 123)
		{
			this.text = "Take this scroll, just use it and you will be the newest knight of Stor Gave.";
			this.setOption(0, "Okay", -1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 124)
		{
			this.text = "Take this scroll, just use it and you will be the newest mage of Stor Gave.";
			this.setOption(0, "Okay", -1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 125)
		{
			this.text = "Take this scroll, just use it and you will be the newest rogue of Stor Gave.";
			this.setOption(0, "Okay", -1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 126)
		{
			this.text = "Take this scroll, just use it and you will be the newest ranger of Stor Gave.";
			this.setOption(0, "Okay", -1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 127)
		{
			this.text = "Hey boy.";
			this.setOption(0, "Hello", this.id+1);
			this.npcPreference = 9;
		}
		else if(this.id == 128)
		{
			this.text = "Hello.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 129)
		{
			this.text = "What's your name?";
			this.setOption(0, Main.saveName, 130);
			this.setOption(1, "Why do you want to know?", 131);
			this.npcPreference = 9;
		}
		else if(this.id == 130)
		{
			this.text = "My name is {PLAYER}.";
			this.setOption(0, ". . .", 132);
			this.playerDialog = true;
		}
		else if(this.id == 131)
		{
			this.text = "Why do you want to know my name?";
			this.setOption(0, ". . .", 133);
			this.playerDialog = true;
		}
		else if(this.id == 132)
		{
			this.text = "Hello {PLAYER}...";
			this.setOption(0, ". . .",  134);
			this.npcPreference = 9;
		}
		else if(this.id == 133)
		{
			this.text = "Sorry calling you suddenly, but I really need your help...";
			this.setOption(0, ". . .",  134);
			this.npcPreference = 9;
		}
		else if(this.id == 134)
		{
			this.text = "Could you help me?";
			this.setOption(0, "Later", 136);
			this.setOption(1, "Later too but in another place", 136);
			this.npcPreference = 9;
		}
		else if(this.id == 135)
		{
			this.text = "Yes, what do you need?";
			this.setOption(0, ". . .", 137);
			this.playerDialog = true;
		}
		else if(this.id == 136)
		{
			this.text = "Not now, could it be later?";
			this.setOption(0, ". . .", 145);
			this.playerDialog = true;
		}
		else if(this.id == 137)
		{
			this.text = "The empire taxes are getting bigger, and the king is wasting our money with riches while we, the people,"
					+ " are in misery...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 9;
		}
		else if(this.id == 138)
		{
			this.text = "I know you are the newest warrior of Stor Gave, the news comes here fast...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference= 9;
		}
		else if(this.id == 139)
		{
			this.text = "The misery makes people go crazy, doing things they would never do...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference= 9;
		}
		else if(this.id == 140)
		{
			this.text = "Like... people here are stealing each other frequently...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference= 9;
		}
		else if(this.id == 141)
		{
			this.text = "But my problem is not about other people, but about other creatures...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference= 9;
		}
		else if(this.id == 142)
		{
			this.text = "There is something killing my sheeps...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference= 9;
		}
		else if(this.id == 143)
		{
			this.text = "And sheeps are my main breeding...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference= 9;
		}
		else if(this.id == 144)
		{
			this.text = "Could you find and eradicate the creature for me?";
			this.setOption(0, "Yes", 149);
			this.setOption(1, "Sorry man, main quests, you need to accept it.", 149);
			this.npcPreference= 9;
		}
		else if(this.id == 145)
		{
			this.text = "Hi {PLAYER}, I am the storyteller, do you see the exclamation above his head?\n"
					+ "It means he is important right now, and the farmer gives a main quest so you can't really reject his quest right now.\n"
					+ "So please, consider accepting the quest.";
			this.setOption(0, "Okay", 146);
			this.npcPreference = -1;
		}
		else if(this.id == 146)
		{
			this.text = "Thanks. Also, it is me who writes the answers you can give to the NPCs and all the other texts, so the next time something simillar"
					+ " happens I will use any text in the game that I can to help you instead to avoid invading the story again.";
			this.setOption(0, "Okay, thank you my lord, the best storyteller ever.", 147);
			this.npcPreference = -1;
		}
		else if(this.id == 147)
		{
			this.text = "Owwwn, thanks.";
			this.setOption(0, ":3", 148);
			this.npcPreference = -1;
		}
		else if(this.id == 148)
		{
			this.text = "Actually, yes, what do you need?";
			this.setOption(0, ". . .", 137);
			this.playerDialog = true;
		}
		else if(this.id == 149)
		{
			this.text = "Yes, I will report as soon as I finish it.";
			this.setOption(0, ". . .", -1);
			this.playerDialog = true;
		}
		else if(this.id == 150)
		{
			this.text = "Congratulations {PLAYER}, you can now live with us in Stor Gave!";
			
			if(!Main.player[Main.me].saveInfo.toldNameToLewin)
				this.setOption(0, "Thanks", 151);
			else
				this.setOption(0, "Thanks", 156);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 151)
		{
			this.text = "Wait, didn't you say you had no name?";
			this.setOption(0, "Improvise something", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 152)
		{
			this.text = "Yes...";
			this.setOption(0, ". . .",  this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 153)
		{
			this.text = "But...";
			this.setOption(0, ". . .",  this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 154)
		{
			this.text = "As I have no name this is the way people like to call me... ?";
			this.setOption(0, ". . .",  this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 155)
		{
			this.text = "Oh, so obvious, sorry, I'm stupid some times.";
			this.setOption(0, "Thank Bragus", this.id+1);
			this.npcPreference = Constant.NPCID_LEWIN;
		}
		else if(this.id == 156)
		{
			this.text = "Thanks Bragus.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 157)
		{
			this.text = "You're welcome.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 158)
		{
			this.text = "So...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 159)
		{
			this.text = "I am going to my brother's house, but I will leave you on the way to Stor Gave.";
			this.setOption(0, "Okay", this.id+1);
			this.npcPreference = Constant.NPCID_BRAGUS;
		}
		else if(this.id == 160)
		{
			this.text = "Okay.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 161)
		{
			this.actionOnly = true;
		}
		else if(this.id == 162)
		{
			this.text = "Hello Farmer.";
			
			Objective o = Main.player[Main.me].haveKillObjective(2);
			if(o != null)
				this.addOption("Report", (o.concluded ? 164 : 167));
			
			o = Main.player[Main.me].haveObjective(8);
			if(o != null)
				this.addOption("Ask about the goblin", 180);
			
			this.addOption("Go away", -1);
			this.playerDialog = true;
		}
		else if(this.id == 164)
		{
			this.text = "I eradicated the creature recently.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 165)
		{
			this.text = "Nice! Thanks for the help.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 9;
		}
		else if(this.id == 166)
		{
			this.text = "You're welcome.";
			this.setOption(0, ". . .", -1);
			this.playerDialog = true;
		}
		else if(this.id == 167)
		{
			this.text = "I'm still trying to kill the creature, or at least find it.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 168)
		{
			this.text = "Okay, I think it is living in the forests to the west.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 9;
		}
		else if(this.id == 169)
		{
			this.text = "Okay.";
			this.setOption(0, ". . .", -1);
			this.playerDialog = true;
		}
		else if(this.id == 170)
		{
			this.text = "UGA UGA!";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 11;
		}
		else if(this.id == 171)
		{
			this.text = "What?";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 172)
		{
			this.text = "Oh sorry...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 11;
		}
		else if(this.id == 173)
		{
			this.text = "Hello boy, can you help me?";
			this.setOption(0, "Yes", this.id+1);
			this.setOption(1, "Guess what?", this.id+1);
			this.npcPreference = 11;
		}
		else if(this.id == 174)
		{
			this.text = "Yes! What you want?";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 175)
		{
			this.text = "We from the top of the Volcano live here to pray to Apollo, the Sun God...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 11;
		}
		else if(this.id == 176)
		{
			this.text = "But recently, a sneaky goblin stole our apollo statue, and without it we can't pray to him...";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 11;
		}
		else if(this.id == 177)
		{
			this.text = "Like, how is he going to know that we are praising him?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 11;
		}
		else if(this.id == 178)
		{
			this.text = "Please, find the statue, but don't kill the goblin, Apollo don't like to see living beings killing themselves.";
			this.setOption(0, "Okay", this.id+1);
			this.npcPreference = 11;
		}
		else if(this.id == 179)
		{
			this.text = "Okay, I'm on the way for it.";
			this.setOption(0, ". . .", -1);
			this.playerDialog = true;
		}
		else if(this.id == 180)
		{
			this.text = "Do you know something about a goblin?";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 181)
		{
			this.text = "Yes, there is a goblin living in my basement, why?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 9;
		}
		else if(this.id == 182)
		{
			this.text = "I talked to the cultists at the top of the volcano and they said me a goblin stole their statue.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 183)
		{
			this.text = "Can I go in?";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 184)
		{
			this.text = "Yes, the basement is at downstairs.";
			this.setOption(0, ". . .", -1);
			this.npcPreference = 9;
		}
		else if(this.id == 185)
		{
			this.text = "Hello little goblin.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 186)
		{
			this.text = "You want my statue, don't you?";
			this.setOption(0, "Yes", this.id+1);
			this.npcPreference = 12;
		}
		else if(this.id == 187)
		{
			this.text = "Actually yes.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 188)
		{
			this.text = "Guess what?";
			this.setOption(0, "You will give me", this.id+1);
			this.npcPreference = 12;
		}
		else if(this.id == 189)
		{
			this.text = "You will give it to me!";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 190)
		{
			this.text = "Yes, oh my Apollo, are you a seer or something?";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 12;
		}
		else if(this.id == 191)
		{
			this.text = "Really?";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 192)
		{
			this.text = "Obviously not, I stole it, it's my credit as a thief.";
			this.setOption(0, "Convince him to go with you", 193);
			this.setOption(1, "Take from him", 202);
			this.npcPreference = 12;
		}
		else if(this.id == 193)
		{
			this.text = "Look, you are living in this house's basement ...";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 194)
		{
			this.text = "I am going to Stor Gave, I can find a new master to you, you will have a so much better life.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 195)
		{
			this.text = "Do you accept it?";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 196)
		{
			this.text = "Hmmm, anything that is not trash food is good to me.";
			this.setOption(0, "What about the statue?", this.id+1);
			this.npcPreference = 12;
		}
		else if(this.id == 197)
		{
			this.text = "Nice! But what about the statue?";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 198)
		{
			this.text = "No problem, as a goblin I can carry things so much heavier than my own weight.";
			this.setOption(0, "Go!", this.id+1);
			this.npcPreference = 12;
		}
		else if(this.id == 199)
		{
			this.text = "Nice, this is so useful, you will find a master very easy.";
			this.setOption(0, ". . .", this.id+1);
			this.playerDialog = true;
		}
		else if(this.id == 200)
		{
			this.text = "Okay, but I will go in your neck.";
			this.setOption(0, ". . .", this.id+1);
			this.npcPreference = 12;
		}
		else if(this.id == 201)
		{
			this.text = "Wow, you are very fast.";
			this.setOption(0, ". . .", -1);
			this.playerDialog = true;
		}
		else if(this.id == 202)
		{
			this.text = "He will be very sad if you do this.";
			this.setOption(0, "Convince him", 193);
			this.npcPreference = -1;
		}
		else if(this.id == 203)
		{
			this.text = "Hello volcano master.";
			Objective o;
			if((o = Main.player[Main.me].haveObjective(9)) != null && o.concluded)
			{
				this.addOption("Return the statue", 204);
			}
			this.addOption("Go away", -1);
			this.playerDialog = true;
		}
		else if(this.id == 204)
		{
			this.text = "Here is the statue.";
			this.playerDialog = true;
		}
		else if(this.id == 205)
		{
			this.text = "Thanks boy, you saved our lives, look how he is happy.";
			this.npcPreference = 11;
		}
		else if(this.id == 206)
		{
			this.text = "I'm happy!";
			this.npcPreference = 10;
		}
		else if(this.id == 207)
		{
			this.text = "Thanks for your help, I will give you this volcanic stone-forjed armor as thanks.";
			this.npcPreference = 11;
		}
		else if(this.id == 208)
		{
			this.text = "It was forjed by Bygorn, the ancient blacksmith master when he was an apprentice, it means it is not"
					+ " a very good armor, but it is valuable.";
			this.npcPreference = 11;
			this.setOption(0, ". . .", -1);
		}
		else if(this.id == 209)
		{
			Monster m = null;
			for(Monster n : Main.monster)
			{
				if(n.id == 2)
				{
					m = n;
					break;
				}
			}
			if(m == null)
			{
				this.text = "Hello little dwarf.";
				this.playerDialog = true;
				this.setOption(0, ". . .", this.id+1);
			}
			else
			{
				this.text = "No time to talk.";
				this.npcPreference = 14;
				this.setOption(0, ". . .", -1);
			}
		}
		else if(this.id == 210)
		{
			this.text = "Hello boy, thanks for the help, if you wasn't here I would be dead.";
			this.npcPreference = 14;
			this.setOption(0, ". . .", this.id+1);
		}
		else if(this.id == 211)
		{
			this.text = "You're welcome!";
			this.playerDialog = true;
			this.setOption(0, ". . .", this.id+1);
		}
		else if(this.id == 212)
		{
			this.text = "Please, let me present you to my dad, he is a blacksmith, a blacksmith master to be sincere, he will give you "
					+ "something as thanks for helping me.";
			this.npcPreference = 14;
			this.setOption(0, "Accept", 215);
			this.setOption(1, "Reject", 213);
		}
		else if(this.id == 213)
		{
			this.text = "No no, it was nothing.";
			this.playerDialog = true;
			this.setOption(0, ". . .", this.id+1);
		}
		else if(this.id == 214)
		{
			this.text = "I need to, it's the least I can do for you.";
			this.npcPreference = 14;
			this.setOption(0, "Accept", this.id+1);
		}
		else if(this.id == 215)
		{
			this.text = "Okay, let's go then.";
			this.playerDialog = true;
			this.setOption(0, ". . .", this.id+1);
		}
		else if(this.id == 216)
		{
			this.actionOnly = true;
		}
		else if(this.id == 217)
		{
			this.text = "This wall, I will leave it open for you, so the next time you come here you can enter with no problem.";
			this.npcPreference = 14;
			this.setOption(0, ". . .", this.id+1);
		}
		else if(this.id == 218)
		{
			this.text = "KODAK ZKI NOMA IICHATIAMAK KUNOMAT";
			this.npcPreference = 14;
			this.setOption(0,  ". . .", this.id+1);
		}
		else if(this.id == 219)
		{
			this.text = "*whistling*";
			this.npcPreference = 14;
			this.setOption(0, ". . .", this.id+1);
		}
		else if(this.id == 220)
		{
			this.text = "Okay, let's enter.";
			this.npcPreference = 14;
			this.setOption(0, ". . .", -1);
		}
		else if(this.id == 221)
		{
			this.text = "Spider...";
			this.playerDialog = true;
			this.setOption(0, ". . .", this.id+1);
		}
		else if(this.id == 222)
		{
			this.text = "You mom is ugly.";
			this.playerDialog = true;
			this.setOption(0, ". . .", this.id+1);
		}
		else if(this.id == 223)
		{
			this.text = ":'C";
			this.npcPreference = -1;
			this.setOption(0, ". . .", -1);
		}
		else if(this.id == 224)
		{
			this.text = Main.lv("Hi boyie, can I help you?", "Olá maninho, como posso ajudar?");
			this.npcPreference = 17;
			boolean haveTask = player.hasQuest(5);
			this.setOption(0, !haveTask ? Main.lv("Ask for tasks", "Pedir tarefa") : Main.lv("Report actual task", "Reportar tarefa atual"), !haveTask ? 225 : 227);
			this.setOption(1, Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 225)
		{
			this.text = Main.lv("I want a new task.", "Eu quero uma nova tarefa.");
			this.playerDialog = true;
			this.setOption(0,  ". . .", this.id + 1);
		}
		else if(this.id == 226)
		{
			this.text = "--{ Auto generated dialog text.";
			this.npcPreference = 17;
			this.setOption(0, "Okay", -1);
		}
		else if(this.id == 227)
		{
			boolean conc = Main.player[Main.me].readyToAccomplish(5);
			if(conc)
			{
				this.text = Main.lv("Hi Helena, I concluded the last task you asked me.",
						"Olá Helena, eu concluí a última tarefa que você pediu.");
				this.playerDialog = true;
				this.setOption(0, ". . .", 228);
			}
			else
			{
				this.text = Main.lv("Hi Helena, I'm still trying to finish the task you gave me.",
						"Olá Helena, ainda estou tentando concluir a tarefa que você me deu.");
				this.playerDialog = true;
				this.setOption(0,  ". . .", 229);
			}
		}
		else if(this.id == 228)
		{
			this.text = Main.lv("Nice, boyie! Take your reward.", "Boa, maninho! Toma aqui sua recompensa.");
			this.setOption(0, "Thanks", -1);
		}
		else if(this.id == 229)
		{
			this.text = Main.lv("Nice, keep the effort.", "Tudo bem, mantenha o esforço.");
			this.setOption(0, "Ok", -1);
		}
		else if(this.id == 230)
		{
			this.text = "HELLO USER!";
			this.addOption("Choose warp", 231);
			this.addOption("Warp to Aecia", 232);
			this.addOption("Warp to Old Gave", 233);
			this.addOption("Exit", -1);
		}
		else if(this.id == 231)
		{
			this.actionOnly = true;
		}
		else if(this.id == 232)
		{
			this.actionOnly = true;
		}
		else if(this.id == 233)
		{
			this.actionOnly = true;
		}
		else if(this.id == 234)
		{
			this.text = Main.lv("Adventuring is not a bad hobby...", "Se aventurar não é um hobby ruim...");
			this.addOption(". . .", this.id+1);
		}
		else if(this.id == 235)
		{
			this.text = Main.lv("Indeed, I was an adventurer just like you before entering the guard...",
					"De fato, eu era um aventureiro assim como você antes de entrar na guarda.");
			this.addOption(". . .", this.id+1);
		}
		else if(this.id == 236)
		{
			this.text = Main.lv("But one day I got an arrow in the knee...", "Mas um dia eu levei uma flechada no joelho.");
			this.addOption(". . .", this.id+1);
		}
		else if(this.id == 237)
		{
			this.text = Main.lv("And this is not a medieval slang for marriage, it was an actual arrow.",
					"E isso não é uma gíria medieval pra casamento, foi uma flecha mesmo.");
			this.addOption(". . .", this.id+1);
		}
		else if(this.id == 238)
		{
			this.text = Main.lv("It still hurts.", "Ainda dói.");
			this.addOption("Get well", this.id+1);
		}
		else if(this.id == 239)
		{
			this.text = Main.lv("Too bad, get well soon, Stor Gave needs good defenders.",
					"Que pena, melhore logo, Stor Gave precisa de bons defensores.");
			this.playerDialog = true;
			this.addOption(Main.lv("Go away", "Ir embora"), -1);
		}
		else if(this.id == 240)
		{
			this.text = "OLD WADE TEXT";
			this.addOption("Hmmm...", -1);
		}
		else if(this.id == 241) //Anvil
		{
			this.actionOnly = true;
		}
		else if(this.id == 242)
		{
			this.text = Main.lv("Hi Leonidas...", "Olá Leonidas...");
			this.playerDialog = true;
			this.addOption(". . .", 406);
			this.addOption(Main.lv("Ask for a tip", "Pedir uma dica"), 243);
			this.addOption(Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 243)
		{
			this.text = "LEONIDAS TIP";
			this.addOption(Main.lv("Cool.", "Legal."), 245);
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 244)
		{
			this.text = "Sorry my brave, no quests yet.";
			this.npcPreference = Constant.NPCID_LEONIDAS;
		}
		else if(this.id == 245)
		{
			this.text = Main.lv("Cool.", "Legal.");
			this.playerDialog = true;
			this.addOption(Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 246)
		{
			this.text = Main.lv("Hello guard...", "Olá guarda...");
			this.playerDialog = true;
			this.addOption(Main.lv("Tell me about you", "Conte um pouco sobre você"), 234);
			this.addOption(Main.lv("Tip", "Conselho"), 311);
			if(!player.hasQuest(6) && !player.haveCompletedQuest(6))
			{
				this.addOption(Main.lv("Quest", "Missão"), 247);
			}
			else if(player.hasQuest(6))
			{
				this.addOption(Main.lv("Quest", "Missão"), 251);
			}
			this.addOption(Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 247)
		{
			this.text = Main.lv("I was thinking about upgrading my weapon, but I am the only one who protects Stor Gave west side and this "
					+ "means I need to stand here all the time...",
					"Eu estava pensando em melhorar minha arma, mas eu sou o único que protege o lado oeste de Stor Gave e isso "
					+ "significa que eu preciso ficar parado aqui o tempo todo...");
			this.npcPreference = 20;
		}
		else if(this.id == 248)
		{
			this.text = Main.lv("I have an extra spear here in my inventory. If you bring me 10 iron ores for me to "
					+ "finish my sword I will give the spear to you.",
					"Eu tenho uma lança extra no meu inventário. Se você me trouxer 10 minérios de ferro para que "
					+ "eu consiga terminar minha espada eu darei a lança a você.");
			this.npcPreference = 20;
			this.addOption("Okay", this.id+1);
		}
		else if(this.id == 249)
		{
			this.text = "Okay.";
			this.playerDialog = true;
		}
		else if(this.id == 250)
		{
			this.text = Main.lv("Thank you.", "Obrigado.");
			this.npcPreference = 20;
			this.quit();
		}
		else if(this.id == 251)
		{
			this.text = Main.lv("Have you finished your mission?", "Você finalizou sua missão?");
			this.npcPreference = 20;
			if(player.readyToAccomplish(6))
			{
				this.addOption(Main.lv("Yes", "Sim"), 252);
			}
			else
			{
				this.addOption(Main.lv("No", "Não"), 254);
			}
		}
		else if(this.id == 252)
		{
			this.text = Main.lv("Yes, I do.", "Sim, eu finalizei.");
			this.playerDialog = true;
		}
		else if(this.id == 253)
		{
			this.text = Main.lv("Nice, here is your spear.", "Ótimo, aqui está sua lança.");
			this.npcPreference = 20;
			this.addOption("Thanks", 257);
		}
		else if(this.id == 254)
		{
			this.text = Main.lv("Not yet.", "Ainda não.");
			this.playerDialog = true;
		}
		else if(this.id == 255)
		{
			this.text = Main.lv("Okay, bring me the ores as soon as you get them.", 
					"Okay, me traga os minérios assim que você consegui-los.");
			this.npcPreference = 20;
		}
		else if(this.id == 256)
		{
			this.text = "Okay";
			this.playerDialog = true;
			this.addOption(Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 257)
		{
			this.text = Main.lv("Thank you.", "Obrigado.");
			this.playerDialog = true;
			this.addOption(Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 258)
		{
			this.text = Main.lv("It seems like someone was buried right here. They forgot to take the shovel.", 
					"Me parece que alguem foi enterrado bem aqui. Eles esqueceram a de levar a pá.");
			this.playerDialog = true;
			this.addOption(Main.lv("Pull the shovel", "Puxar a pá"), 259);
			this.addOption(Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 259)
		{
			this.actionOnly = true;
		}
		else if(this.id == 260)
		{
			this.text = Main.lv("Hi...", "Olá...");
			this.playerDialog = true;
			this.addOption(Main.lv("What is your name?", "Qual é o seu nome?"), this.id+1);
			this.addOption(Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 261)
		{
			this.text = Main.lv("My name is Buck, I am a new Stor Gave farmer, and you?", "Eu me chamo Buck, sou um fazendeiro novo de Stor Gave, e você?");
			this.addOption(player.name, this.id + 1);
			this.npcPreference = 24;
		}
		else if(this.id == 262)
		{
			this.text = Main.lv("Hello Buck" + (player.saveInfo.metBuckTheFarmer ? "." : ", my name is {PLAYER}."),
					"Olá Buck" + (player.saveInfo.metBuckTheFarmer ? "." : ", meu nome é {PLAYER}."));
			this.addOption(Main.lv("East guard", "Guarda leste"), 263);
			this.playerDialog = true;
			this.addOption(Main.lv("Tell me about you", "Conte um pouco sobre você"), 273);
		}
		else if(this.id == 263)
		{
			this.text = Main.lv("Thales, the east guard died some weeks ago...", "Thales, o soldado leste morreu algumas semanas atrás...");
			this.npcPreference = 24;
		}
		else if(this.id == 264)
		{
			this.text = Main.lv("It was a sad bury. Thales's last wish was to be buried in the cave depths "
					+ "along with his shovel.",
					"Foi um enterro triste. O último desejo de Thales foi ser enterrado nas profundezas das cavernas "
					+ "junto de sua pá.");
			this.addOption(Main.lv("My condolences", "Meus pêsames"), 265);
			this.addOption(Main.lv("Shovel?", "Pá?"), 269);
			this.npcPreference = 24;
		}
		else if(this.id == 265)
		{
			this.text = Main.lv("My condolences.", "Meus pêsames.");
			this.playerDialog = true;
		}
		else if(this.id == 266)
		{
			this.text = Main.lv("It's okay I think... Leonidas is trying to find a new guard "
					+ "to protect the east side now.",
					"Tá tudo bem eu acho... Leonidas está tentando encontrar um novo guarda "
					+ "para proteger o lado leste agora.");
			this.npcPreference = 24;
		}
		else if(this.id == 267)
		{
			this.text = Main.lv("I hope he finds a new soon.", "Espero que ele encontre um logo.");
			this.playerDialog = true;
		}
		else if(this.id == 268)
		{
			this.text = Main.lv("Me too.", "Eu também.");
			this.npcPreference = 24;
			this.addOption(Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 269)
		{
			this.text = Main.lv("His shovel?", "Como assim sua pá?");
			this.playerDialog = true;
		}
		else if(this.id == 270)
		{
			this.text = Main.lv("Yes, he refused to use a lance as a weapon...", 
					"Sim, ele se recusou a usar uma lança como arma...");
			this.npcPreference = 24;
		}
		else if(this.id == 271)
		{
			this.text = Main.lv("One day his father called Cryoki, the Stor Gave Gravedigger died and he gave his shovel to Thales. He liked that shovel so much "
					+ "he sharpened it and used it as a weapon...",
					"Um dia seu pai chamado Cryoki, o Coveiro morreu e ele deu a pá pro Thales. Ele gostou tanto "
					+ "daquela pá que ele resolveu afiar e usar como arma...");
			this.npcPreference = 24;
			this.addOption(". . .", 274);
		}
		else if(this.id == 272)
		{
			this.text = Main.lv("Okay.", "Okay.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 273)
		{
			this.text = Main.lv("I am a simple Stor Gave farmer, just making my work.",
					"Eu sou um simples fazendeiro de Stor Gave, apenas fazendo meu trabalho.");
			this.npcPreference = 24;
			this.addOption("Exit", 262);
		}
		else if(this.id == 274)
		{
			this.text = Main.lv("If you wanna see it, I think he was buried deep in the caves in the west.",
					"Se você quiser ver, eu acho que ele foi enterrado no fundo das cavernas no oeste.");
			this.addOption(". . .", 272);
		}
		else if(this.id == 275)
		{
			this.text = Main.lv("It is blocked, you can not pass.", "Está bloqueado, você não pode passar.");
			this.npcPreference = -1;
			this.addOption(Main.lv("Jump over", "Pular por cima"), this.id + 1);
			this.addOption("Okay", -1);
		}
		else if(this.id == 276)
		{
			this.text = Main.lv("THINGS ARE NOT EASY LIKE THAT, {UPLAYER}!", "AS COISAS NÃO SÃO FACEIS ASSIM, {UPLAYER}!");
			this.npcPreference = -1;
			this.addOption("Okay", -1);
		}
		else if(this.id == 277)
		{
			this.text = Main.lv("Boy, do not enter there, it's the kitchen.", "Menino, não entre aí, é a cozinha.");
			this.npcPreference = 25;
			this.addOption("Okay", -1);
		}
		else if(this.id == 278)
		{
			String pcl = player.classType.name;
			this.text = Main.lv("Hi {PLAYER}, have you finished your "+pcl+" training?", "Oi {PLAYER}, você terminou seu treinamento de "+pcl+"?");
			this.npcPreference = 26;
		}
		else if(this.id == 279)
		{
			String master = player.classType.master;
			this.text = Main.lv("Yes, " + master + " has finally trained me.", "Sim, " + master + " finalmente me treinou.");
			this.playerDialog = true;
		}
		else if(this.id == 280)
		{
			this.text = Main.lv("Nice.", "Ótimo.");
			this.npcPreference = 26;
			this.quit();
		}
		else if(this.id == 281)
		{
			this.text = Main.lv("Hello {PLAYER}, I am a human if you did not notice.", "Olá {PLAYER}, eu sou um humano se você não percebeu.");
			this.npcPreference = 27;
		}
		else if(this.id == 282)
		{
			this.text = Main.lv("I believe in you.", "Eu acredito em você.");
			this.playerDialog = true;
		}
		else if(this.id == 283)
		{
			this.text = Main.lv("Thank you, my human partner.", "Obrigado, meu parceiro humano.");
			this.npcPreference = 27;
			this.quit();
		}
		else if(this.id == 284)
		{
			this.text = Main.lv("Stor Gave Internal Laws:\n"
					+ "1 - The entry of non-human beings is not allowed.\n"
					+ "2 - The bakery only works at friday.\n"
					+ "3 - It is prohibited to put signs inside the city.\n"
					+ "4 - It is prohibited to break rules, but if you want you can do it.", 
					"Leis Internas de Stor Gave:\n"
					+ "1 - É proibido a entrada de seres não-humanos.\n"
					+ "2 - A padaria só funciona na sexta-feira.\n"
					+ "3 - É proibido colocar placas no interior da cidade.\n"
					+ "4 - É proibido quebrar as regras mas se quiser pode.");
			this.npcPreference = -1;
			this.quit();
		}
		else if(this.id == 285)
		{
			this.text = Main.lv("Looking for an east guard to protect Stor Gave.\n"
					+ "If you are interested, search for Leonidas in the city.", 
					"Procura-se um guarda leste para proteger Stor Gave.\n"
					+ "Interessados favor procurar Leonidas pela cidade.");
			this.npcPreference = -1;
			this.quit();
		}
		else if(this.id == 286)
		{
			this.text = Main.lv("Watch out...",
					"Cuidado...");
			this.addOption(Main.lv("Pull sword", "Puxar a espada"), 287);
			this.addOption(Main.lv("Do nothing", "Não fazer nada"), -1);
		}
		else if(this.id == 287)
		{
			this.actionOnly = true;
		}
		else if(this.id == 288)
		{
			this.text = Main.lv("A treasure awaits you...", "Um tesouro te aguarda...");
			this.quit();
		}
		else if(this.id == 289)
		{
			this.text = Main.lv("Hi {PLAYER}, *hee-hee*, how are you doing?", "Oi {PLAYER}, hihi, como vai você?");
		}
		else if(this.id == 290)
		{
			this.text = Main.lv("Fine, thanks...", "Bem, obrigado...");
			this.addOption(Main.lv("Recipes", "Receitas"), 297);
			this.addOption(Main.lv("Use the forge", "Usar a forja"), this.id+1);
			boolean quest = !player.haveCompletedQuest(7);
			if(quest)
			{
				this.addOption(Main.lv("Quest", "Missão"), (player.hasQuest(7) ? 328 : 318));
			}
			
			this.playerDialog = true;
		}
		else if(this.id == 291)
		{
			this.text = Main.lv("... can I use the forge?", "... posso usar a forja?");
			this.playerDialog = true;
		}
		else if(this.id == 292)
		{
			this.text = Main.lv("Of course, *hee-hee*, the forge is public, I am here just to take care of the little disasters.", 
					"Claro, hihi, a forja é pública, só estou aqui para cuidar dos pequenos desastres.");
		}
		else if(this.id == 293)
		{
			this.text = Main.lv("Still don't know why they made a forge in a wood structure.", 
					"Ainda não sei porque fizeram uma forja em uma estrutura de madeira.");
		}
		else if(this.id == 294)
		{
			this.text = Main.lv("Me neither.", "Eu também não.");
			this.quit();
			this.playerDialog = true;
		}
		else if(this.id == 295)
		{
			this.actionOnly = true;
		}
		else if(this.id == 296)
		{
			if(!Main.isAtDay())
				this.text = Main.lv("The flame light makes your soul peaceful at night.", "A luz da chama torna sua alma mais pacífica durante a noite.");
			else
				this.text = Main.lv("The flame heat helps you relaxing.", "O calor da chama te ajuda a relaxar.");
			
			this.npcPreference = -1;
			this.quit();
		}
		else if(this.id == 297)
		{
			this.text = Main.lv("How do I acquire some crafting recipes?", "Como eu consigo receitas para fabricação?");
			this.playerDialog = true;
		}
		else if(this.id == 298)
		{
			this.text = Main.lv("Oh, you can complete some taskies for Helena or complete dungeons in the caves to obtain recipes.", "Ah, você pode fazer algumas tarefinhas com a Helena ou completar calabouços nas cavernas pra conseguir receitas.");
		}
		else if(this.id == 299)
		{
			this.text = Main.lv("Ok, thanks.", "Ok, obrigado.");
			this.quit();
			this.playerDialog = true;
		}
		else if(this.id == 300)
		{
			this.text = Main.lv("What's up {PLAYER}, just chilling out?", "E aí {PLAYER}, só de boa?");
			this.npcPreference = 3;
		}
		else if(this.id == 301)
		{
			this.text = Main.lv("Hello Bragus, just chilling.", "Olá Bragus, só de boa.");
			this.addOption(Main.lv("Verse", "Estrofe"), 304);
			this.playerDialog = true;
		}
		else if(this.id == 302)
		{
			this.text = "-{ Bragus Verses";
			this.npcPreference = 3;
		}
		else if(this.id == 303)
		{
			this.text = Main.lv("Great!", "Ótimo!");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 304)
		{
			this.text = Main.lv("Can you sing me a verse?", "Pode cantar uma estrofe pra mim?");
			this.playerDialog = true;
		}
		else if(this.id == 305)
		{
			this.text = Main.lv("Yes, let me think...", "Sim, deixa eu pensar...");
			this.npcPreference = 3;
			this.addOption(". . .", 302);
		}
		else if(this.id == 306)
		{
			this.text = Main.lv("What's up {PLAYER}, how are you?", "E aí {PLAYER}, tudo bem com você?");
			this.npcPreference = 6;
		}
		else if(this.id == 307)
		{
			this.text = Main.lv("All right, and you?", "Tudo bem, e contigo?");
			this.playerDialog = true;
		}
		else if(this.id == 308)
		{
			this.text = Main.lv("I'm never good.", "Eu nunca estou bem.");
		}
		else if(this.id == 309)
		{
			this.text = Main.lv("My bad...", "Foi mal...");
			this.playerDialog = true;
		}
		else if(this.id == 310)
		{
			this.text = Main.lv("No problem.", "Sem problemas.");
			this.addOption(". . .", 430);
		}
		else if(this.id == 311)
		{
			this.text = Main.lv("Could you say a tip to this young adventurer?", "Poderia dizer um conselho para este jovem aventureiro?");
			this.playerDialog = true;
		}
		else if(this.id == 312)
		{
			this.text = Main.lv("Yes, of course. Be aware of where you go, the further you go, the stronger the creatures get.", "Sim, claro. Cuidado com onde você vai, quanto mais longe você vai, mais forte as criaturas se tornam.");
			this.quit();
		}
		else if(this.id == 313)
		{
			this.text = Main.lv("Okay, now let me see the iron you got!", "Ok, agora me deixa ver o ferro que você pegou!");
			this.npcPreference = 29;
			this.addOption("...", this.id+2);
		}
		else if(this.id == 314)
		{
			this.playerDialog = true;
			this.actionOnly = true;
			this.addOption(". . .", this.id+1);
		}
		else if(this.id == 315)
		{
			this.text = Main.lv("It's enough...", "É o suficiente...");
			this.npcPreference = 29;
		}
		else if(this.id == 316)
		{
			this.text = Main.lv("I guess...", "Eu acho...");
			this.npcPreference = 29;
		}
		else if(this.id == 317)
		{
			this.actionOnly = true;
			this.quit();
		}
		else if(this.id == 318)
		{
			this.text = Main.lv("Oh, how do you know I have a quest for you?", "Oh, como você sabe que eu tenho uma missão pra você?");
			this.npcPreference = 29;
		}
		else if(this.id == 319)
		{
			this.text = Main.lv("I don't know, something in my head says to me that you have...", "Eu não sei, alguma coisa na minha cabeça me diz que você tem...");
			this.playerDialog = true;
			this.addOption(". . .",  321);
		}
		else if(this.id == 320)
		{
			this.text = Main.lv("Or in yours...", "Ou na sua...");
			this.playerDialog = true;
		}
		else if(this.id == 321)
		{
			this.text = Main.lv("Must be you eyes, *hee-hee*.", "Deve ser seus olhos, hihi.");
			this.npcPreference = 29;
		}
		else if(this.id == 322)
		{
			this.text = Main.lv("So, you can learn now how to craft iron items!", "Então, você pode aprender a fazer itens de ferro!");
			this.npcPreference = 29;
		}
		else if(this.id == 323)
		{
			this.text = Main.lv("Wow, so will I learn to create laser-shooting suits with jet thrusters?", "Uau, então eu vou aprender a fazer trajes que atiram laser com propulsores a jato?");
			this.playerDialog = true;
		}
		else if(this.id == 324)
		{
			this.text = Main.lv("Not yet, just common iron stuff.", "Ainda não, só coisinhas normais de ferro.");
			this.npcPreference = 29;
		}
		else if(this.id == 325)
		{
			this.text = Main.lv("Okay.", "Ok.");
			this.playerDialog = true;
		}
		else if(this.id == 326)
		{
			this.text = Main.lv("Bring me 35 iron ores from the caves so we can try some craftings.", "Me traz 35 minérios de ferro das cavernas pra gente poder começar algumas criações.");
			this.npcPreference = 29;
		}
		else if(this.id == 327)
		{
			this.text = Main.lv("Consider it done!", "É pra já!");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 328)
		{
			this.text = Main.lv("You got the ores?", "Conseguiu os minérios?");
			this.addOption(". . .", player.getQuest(7).isConcluded() ? 331 : 328);
			this.npcPreference = 29;
		}
		else if(this.id == 329)
		{
			this.text = Main.lv("Not yet.", "Ainda não.");
			this.playerDialog = true;
		}
		else if(this.id == 330)
		{
			this.text = Main.lv("I'll be waiting for you here.", "Estarei esperando por você aqui.");
			this.npcPreference = 29;
			this.quit();
		}
		else if(this.id == 331)
		{
			this.text = Main.lv("Yes, I got them.", "Sim, eu peguei eles.");
			this.playerDialog = true;
		}
		else if(this.id == 332)
		{
			this.text = Main.lv("Nice, let's go to the forge.", "Ótimo, vamos à forja.");
			this.npcPreference = 29;
		}
		else if(this.id == 333)
		{
			this.actionOnly = true;
		}
		else if(this.id == 334)
		{
			this.text = Main.lv("Sorry, you can't go any further.", "Perdão, você não pode ir mais longe.");
			this.npcPreference = 31;
			this.addOption(Main.lv("All right", "Tudo bem"), -1);
			this.addOption(Main.lv("What if?", "E se?"), 335);
		}
		else if(this.id == 335)
		{
			this.text = Main.lv("Okay but what if I wan-", "Ok mas e se eu qui-");
			this.playerDialog = true;
		}
		else if(this.id == 336)
		{
			this.actionOnly = true;
			this.quit();
		}
		else if(this.id == 337)
		{
			this.text = Main.lv("Hello {PLAYER}, how are you?", "Olá {PLAYER}, como você está?");
			this.npcPreference = 32;
		}
		else if(this.id == 338)
		{
			this.text = Main.lv("I'm fine, thanks...", "Estou bem, obrigado...");
			this.playerDialog = true;
			boolean haveTask = player.hasQuest(8);
			this.setOption(0, !haveTask ? Main.lv("Ask for tasks", "Pedir tarefa") : Main.lv("Report actual task", "Reportar tarefa atual"), !haveTask ? 339 : 341);
			this.setOption(1, "Skat?", 381);
			this.setOption(2, Main.lv("Exit", "Sair"), -1);
		}
		else if(this.id == 339)
		{
			this.text = Main.lv("I want a new task.", "Eu quero uma nova tarefa.");
			this.playerDialog = true;
			this.setOption(0,  ". . .", this.id + 1);
		}
		else if(this.id == 340)
		{
			this.text = "--{ Auto generated dialog text.";
			this.npcPreference = 17;
			this.setOption(0, "Okay", -1);
		}
		else if(this.id == 341)
		{
			boolean conc = Main.player[Main.me].readyToAccomplish(8);
			if(conc)
			{
				this.text = Main.lv("Hi Dill, I concluded the last task you asked me.",
						"Olá Dill, eu concluí a última tarefa que você pediu.");
				this.playerDialog = true;
				this.setOption(0, ". . .", 342);
			}
			else
			{
				this.text = Main.lv("Hi Dill, I'm still trying to finish the task you gave me.",
						"Olá Dill, ainda estou tentando concluir a tarefa que você me deu.");
				this.playerDialog = true;
				this.setOption(0,  ". . .", 343);
			}
		}
		else if(this.id == 342)
		{
			this.text = Main.lv("Nice job, little man! Take your reward.", "Bom trabalho, pequeno rapaz! Toma aqui sua recompensa.");
			this.setOption(0, "Thanks", -1);
		}
		else if(this.id == 343)
		{
			this.text = Main.lv("Nice, keep the effort.", "Tudo bem, mantenha o esforço.");
			this.setOption(0, "Ok", -1);
		}
		else if(this.id == 344)
		{
			this.text = Main.lv("OH DAMN, IT'S MY MAN {UPLAYER}!", "OLHA SÓ SE NÃO É O MEU GAROTO {UPLAYER}!");
			this.npcPreference = -1;
		}
		else if(this.id == 345)
		{
			this.text = Main.lv("It's my name!", "É o meu nome!");
			this.addOption(". . .", 385);
			if(!player.haveCompletedQuest(9))
			{
				this.addOption("Quest", player.hasQuest(9) ? 350 : 346);
			}
			this.playerDialog = true;
		}
		else if(this.id == 346)
		{
			this.text = Main.lv("I think you have a quest for me!", "Eu acho que você tem uma missão pra mim!");
			this.playerDialog = true;
		}
		else if(this.id == 347)
		{
			this.text = Main.lv("Smart boy! Yes, I do.", "Garoto esperto! Sim, eu tenho.");
			this.npcPreference = 15;
		}
		else if(this.id == 348)
		{
			this.text = Main.lv("I need you to collect some gold and silver ores, so you can learn more about orecrafting.", 
					"Eu preciso que você colete alguns minérios de ouro e de prata, para que você possa aprender mais sobre fabricação com minérios.");
			this.npcPreference = 15;
		}
		else if(this.id == 349)
		{
			this.text = Main.lv("Okay, I will search some ores.", "Ok, vou procurar alguns minérios.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 350)
		{
			if(player.getQuest(9).isConcluded())
			{
				this.text = Main.lv("I got the ores.", "Eu consegui os minérios.");
				this.addOption(". . .", 351);
			}
			else
			{
				this.text = Main.lv("I'm still getting the ores.", "Ainda estou conseguindo os minérios.");
				this.addOption(". . .", 355);
			}
			this.playerDialog = true;
		}
		else if(this.id == 351)
		{
			this.text = Main.lv("THAT'S MUSIC TO MY EARS!", "ISSO É MÚSICA PARA OS MEUS OUVIDOS!");
			this.npcPreference = 15;
		}
		else if(this.id == 352)
		{
			this.text = Main.lv("There are the recipes.", "Aqui estão as receitas.");
			this.npcPreference = 15;
		}
		else if(this.id == 353)
		{
			this.text = Main.lv("Thanks!", "Obrigado!");
			this.playerDialog = true;
		}
		else if(this.id == 354)
		{
			this.text = Main.lv("You're welcome.", "De nada.");
			this.quit();
			this.npcPreference = 15;
		}
		else if(this.id == 355)
		{
			this.text = Main.lv("Gotta catch 'em all.", "Tens que pegar todos.");
			this.npcPreference = 15;
			this.quit();
		}
		else if(this.id == 356)
		{
			this.text = Main.lv("[GHOST]HELLO WANDERER.[]", "[GHOST]OLÁ VIAJANTE.[]");
			this.npcPreference = 33;
		}
		else if(this.id == 357)
		{
			this.text = Main.lv("Hello mister ghost...", "Olá senhor fantasma...");
			this.playerDialog = true;
			this.addOption(". . .", !player.hasQuest(10) && !player.haveCompletedQuest(10) ? 358 : 378);
		}
		else if(this.id == 358)
		{
			this.text = Main.lv("Why is there a ship if there is no sea nearby?", "Por que tem um barco aqui se não tem nenhum mar por perto?");
			this.playerDialog = true;
		}
		else if(this.id == 359)
		{
			this.text = Main.lv("[GHOST]WHY AM I WALKING IN THE GROUND IF I CAN FLY?[]", "[GHOST]POR QUE ESTOU ANDANDO NO CHÃO SE EU POSSO VOAR?[]");
			this.npcPreference = 33;
		}
		else if(this.id == 360)
		{
			this.text = Main.lv("Hmmm...", "Hmmm...");
			this.playerDialog = true;
		}
		else if(this.id == 361)
		{
			this.text = Main.lv("[GHOST]GHOST SHIPS FLOATS IN THE AIR...[]", "[GHOST]BARCOS FANTASMAS FLUTUAM NO AR...[]");
			this.npcPreference = 33;
		}
		else if(this.id == 362)
		{
			this.text = Main.lv("[GHOST]WE USE THIS SHIP TO TRANSPORT PEOPLE TO THE ETERNITY ISLAND.[]", "[GHOST]NÓS USAMOS ESSE BARCO PARA TRANSPORTAR AS PESSOAS PARA A ILHA DA ETERNIDADE.[]");
			this.npcPreference = 33;
		}
		else if(this.id == 363)
		{
			this.text = Main.lv("But how do people not fall off from the ship? Like, it is a ghost ship, right?", "Mas como as pessoas não caem do barco? Tipo, é um barco fantasma, certo?");
			this.playerDialog = true;
		}
		else if(this.id == 364)
		{
			this.text = Main.lv("[GHOST]HMMM...[]", "[GHOST]HMMM...[]");
			this.npcPreference = 33;
		}
		else if(this.id == 365)
		{
			this.text = Main.lv("And how is it a \"ghost ship\"? Ships can not die.", "E como assim é um \"barco fantasma\"? Barcos não morrem.");
			this.playerDialog = true;
		}
		else if(this.id == 366)
		{
			this.text = Main.lv("[GHOST]HMMMMMM...[]", "[GHOST]HMMMMMM...[]");
			this.npcPreference = 33;
		}
		else if(this.id == 367)
		{
			this.text = Main.lv("[GHOST]I THINK I WAS FOOLED AGAIN.[]", "[GHOST]ACHO QUE FUI ENGANADO DE NOVO.[]");
			this.npcPreference = 33;
		}
		else if(this.id == 368)
		{
			this.text = Main.lv("Who has the courage to fool a ghost pirate?", "Quem tem a coragem de enganar um pirata fantasma?");
			this.playerDialog = true;
		}
		else if(this.id == 369)
		{
			this.text = Main.lv("[GHOST]SOME DUDE CALLED DEVOLE...[]", "[GHOST]UM CARA AÍ CHAMADO DEVOLE...[]");
			this.npcPreference = 33;
		}
		else if(this.id == 370)
		{
			this.text = Main.lv("[GHOST]HE SOLD ME THIS BOAT IN EXCHANGE FOR MY SERVICE OF TRANSPORTING PEOPLE...[]", "[GHOST]ELE ME VENDEU ESSE BARCO EM TROCA DO MEU SERVIÇO DE TRANSPORTAR PESSOAS...[]");
			this.npcPreference = 33;
		}
		else if(this.id == 371)
		{
			this.text = Main.lv("[GHOST]AND AS A GHOST PIRATE I WANTED A GHOST SHIP...[]", "[GHOST]E COMO UM PIRATA FANTASMA EU QUERIA UM BARCO FANTASMA...[]");
			this.npcPreference = 33;
		}
		else if(this.id == 372)
		{
			this.text = Main.lv("[GHOST]I WILL PUNCH HIS FACE WITH MY HOOK WHEN I MEET HIM AGAIN...[]", "[GHOST]EU VOU SOCAR A CARA DELE COM O MEU GANCHO QUANDO ENCONTRAR ELE DE NOVO...[]");
			this.npcPreference = 33;
		}
		else if(this.id == 373)
		{
			this.text = Main.lv("[GHOST]ANYWAY, WHAT DO YOU WANT?[]", "[GHOST]ENFIM, O QUE VOCÊ QUER?[]");
			this.npcPreference = 33;
		}
		else if(this.id == 374)
		{
			this.text = Main.lv("How do I travel to the other island?", "Como eu faço pra viajar pra outra ilha?");
			this.playerDialog = true;
		}
		else if(this.id == 375)
		{
			this.text = Main.lv("[GHOST]DEVOLE NEEDS SOME ECTOPLASM FOR HIS FLOATING MONEY HOUSE...[]", "[GHOST]DEVOLE PRECISA DE ECTOPLASMA PARA A SUA CASA DE DINHEIRO FLUTUANTE...[]");
			this.npcPreference = 33;
		}
		else if(this.id == 376)
		{
			this.text = Main.lv("[GHOST]BRING ME SOME ECTOPLASM AND I WILL TAKE YOU TO THE [EPIC]ETERNITY ISLAND[].[]", "[GHOST]ME TRAGA UM POUCO DE ECTOPLASMA E EU VOU TE LEVAR PARA A [EPIC]ILHA DA ETERNIDADE[].[]");
			this.npcPreference = 33;
		}
		else if(this.id == 377)
		{
			this.text = Main.lv("Okay.", "Ok.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 378)
		{
			if(!player.haveCompletedQuest(10))
			{
				if(player.readyToAccomplish(10))
				{
					this.text = Main.lv("I finally collected the ectoplasms.", "Eu terminei de coletar os ectoplasmas.");
					this.addOption(". . .", 379);
				}
				else
				{
					this.text = Main.lv("I'm still collecting the ectoplasms.", "Eu ainda estou coletando os ectoplasmas.");
					this.addOption(". . .", 380);
				}
			}
			else
			{
				this.text = Main.lv("Hey Captain, I need a ride to the Eternity Island.", "Ei Capitão, eu preciso de uma carona pra Ilha da Eternidade.");
				this.addOption(". . .", 379);
			}
			this.playerDialog = true;
		}
		else if(this.id == 379)
		{
			if(!player.haveCompletedQuest(10))
				this.text = Main.lv("[GHOST]NICE HOOK WANDERER, THIS SHIP IS GONNA FLY RIGHT NOW.[]", "[GHOST]BOA FISGADA VIAJANTE, ESSE BARCO VAI VOAR AGORA MESMO.[]");
			else
				this.text = Main.lv("[GHOST]GOT IT, GET ON THE SHIP.[]", "[GHOST]ENTENDI, SOBE NO BARCO.[]");
			
			this.npcPreference = 33;
			this.quit();
		}
		else if(this.id == 380)
		{
			this.text = Main.lv("[GHOST]PUNCH THESE GHOSTS DOWN, WANDERER.[]", "[GHOST]DESCE ESSES FANTASMAS NA PORRADA, VIAJANTE.[]");
			this.npcPreference = 33;
			this.quit();
		}
		else if(this.id == 381)
		{
			this.text = Main.lv("Are you related to Helena Skat?", "Você é parente da Helena Skat?");
			this.playerDialog = true;
		}
		else if(this.id == 382)
		{
			this.text = Main.lv("Yes, we have relatives all over Arka! Everybody with the same objective: train adventurers by giving them tasks.", "Sim, nós temos parentes em toda Arka! Todos com o mesmo objetivo: treinar aventureiros através de tarefas.");
			this.npcPreference = 32;
		}
		else if(this.id == 383)
		{
			this.text = Main.lv("Woah! I can't wait to meet your relatives.", "Uou! Mal posso esperar para conhecer seus parentes.");
			this.playerDialog = true;
		}
		else if(this.id == 384)
		{
			this.text = Main.lv("When you do, say hello for me.", "Quando conseguir, diga oi por mim.");
			this.npcPreference = 32;
			this.quit();
		}
		else if(this.id == 385)
		{
			this.text = Main.lv("Life is becoming harder down here.", "A vida está ficando mais difícil aqui embaixo.");
			this.npcPreference = 15;
		}
		else if(this.id == 386)
		{
			this.text = Main.lv("But I fight for my people! Dwarville will never surrender!", "Mas eu luto pelo meu povo! Dwarville nunca vai se render!");
			this.npcPreference = 15;
		}
		else if(this.id == 387)
		{
			this.text = Main.lv("Surrender?", "Se render?");
			this.playerDialog = true;
		}
		else if(this.id == 388)
		{
			this.text = Main.lv("Yes, some ghosts are invading the Simplicity continent and the underground ghosts are tougher than the overworld ones.", "Sim, alguns fantasmas estão invadindo o continente da Simplicidade e os fantasmas que ficam por aqui são mais fortes do que os que ficam lá em cima.");
			this.npcPreference = 15;
		}
		else if(this.id == 389)
		{
			this.text = Main.lv("I will help you in this war, I'm gonna defeat every ghost in my way, they will not expect a punch in the face!", "Eu vou te ajudar com essa guerra, vou derrotar todo fantasma que tiver no meu caminho, eles não vão esperar por um soco na cara!");
			this.playerDialog = true;
		}
		else if(this.id == 390)
		{
			this.text = Main.lv("I guess it is wiser to know why there are ghosts near here and do what is better to everyone. This way everyone can stay happy and alive!", "Eu acho que é mais sábio saber por que há fantasmas por aqui e fazer o que é melhor para todos. Dessa maneira todos ficamos felizes e vivos!");
			this.npcPreference = 15;
		}
		else if(this.id == 391)
		{
			this.text = Main.lv("I mean, we stay alive, they are dead creatures anyway...", "Digo, nós ficamos vivos, eles são criaturas mortas de qualquer forma...");
			this.npcPreference = 15;
		}
		else if(this.id == 392)
		{
			this.text = Main.lv("Got it.", "Entendi.");
			this.playerDialog = true;
		}
		else if(this.id == 393)
		{
			this.text = Main.lv("You can talk with Siren in Amazona underground forest. If I'm not mistaken you can find it in the east of Stor Gave in the underground.", "Você pode conversar com Siren na floresta subterrânea de Amazona. Se eu não estiver enganado você pode encontrá-lo no leste de Stor Gave no subsolo.");
			this.npcPreference = 15;
		}
		else if(this.id == 394)
		{
			this.text = Main.lv("Underground... forest?", "Floresta no... subsolo?");
			this.playerDialog = true;
		}
		else if(this.id == 395)
		{
			this.text = Main.lv("Yes, like a forest but under the ground!", "Sim, como uma floresta mas embaixo do chão!");
			this.npcPreference = 15;
		}
		else if(this.id == 396)
		{
			this.text = Main.lv("But this is something you gotta ask him, not me.", "Mas isso é algo que você deve perguntar pra ele, não pra mim.");
			this.npcPreference = 15;
		}
		else if(this.id == 397)
		{
			this.text = Main.lv("Okay, I'm on my way.", "Ok, vou lá ver.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 398)
		{
			this.text = Main.lv("Hello {PLAYER}.", "Olá {PLAYER}.");
			this.npcPreference = 8;
			this.addOption(Main.lv("Talk", "Conversar"), 399);
			boolean canConvertJewels = player.saveInfo.canConvertMagicEssence;
			if(canConvertJewels)
				this.addOption(Main.lv("Magic Essence", "Essência Mágica"), 522);
		}
		else if(this.id == 399)
		{
			this.text = Main.lv("Hello Lewin, how you doing?", "Olá Lewin, como vai?");
			this.playerDialog = true;
		}
		else if(this.id == 400)
		{
			this.text = Main.lv("Studying as always.", "Estudando como sempre.");
			this.npcPreference = 8;
		}
		else if(this.id == 401)
		{
			this.text = Main.lv("I read about an entire biome made out of candy next to Simplicity, can you believe it?!", "Eu li sobre um bioma feito inteiramente de doces próximo à Simplicidade, consegue acreditar?!");
			this.npcPreference = 8;
		}
		else if(this.id == 402)
		{
			this.text = Main.lv("Candies?", "Doces?");
			this.playerDialog = true;
		}
		else if(this.id == 403)
		{
			this.text = Main.lv("Yes, but it seems to be a bit dangerous. A yummy princess seems to defend and empower their people.", "Sim, mas parece ser um pouco perigoso. Uma princesa deliciosa parece defender e aumentar o poder do povo de lá.");
			this.npcPreference = 8;
			this.addOption(". . .",  444);
		}
		else if(this.id == 404)
		{
			this.text = Main.lv("I gotta search about it.", "Vou pesquisar sobre.");
			this.playerDialog = true;
		}
		else if(this.id == 405)
		{
			this.text = Main.lv("Okay, I found the book about it in the library.", "Ok, eu achei o livro sobre isso lá ná biblioteca.");
			this.npcPreference = 8;
			this.quit();
		}
		else if(this.id == 406)
		{
			this.text = Main.lv("Hello my brave.", "Olá meu bravo.");
			this.npcPreference = 5;
		}
		else if(this.id == 407)
		{
			this.text = Main.lv("Hello Leonidas! How you doing?", "Olá Leonidas! Como vai?");
			this.playerDialog = true;
		}
		else if(this.id == 408)
		{
			this.text = Main.lv("I'm nice!", "Estou ótimo!");
			this.npcPreference = 5;
		}
		else if(this.id == 409)
		{
			this.text = Main.lv("I'm just worried about something.", "Só estou preocupado com uma coisa.");
			this.npcPreference = 5;
		}
		else if(this.id == 410)
		{
			this.text = Main.lv("A man called Jason.", "Um homem chamado Jasão.");
			this.npcPreference = 5;
		}
		else if(this.id == 411)
		{
			this.text = Main.lv("He wanna get the Scrolls of Ancient Knowledge.", "Ele quer obter os Pergaminhos do Conhecimento Ancião.");
			this.addOption(Main.lv("Scroll of Ancient Knowledge", "Pergaminho do Conhecimento Ancião"), 412);
			this.addOption(Main.lv("Jason objective", "Objetivo do Jasão"), 419);
			this.npcPreference = 5;
		}
		else if(this.id == 412)
		{
			this.text = Main.lv("What is an Scroll of Ancient Knowledge?", "O que é um Pergaminho do Conhecimento Ancião?");
			this.playerDialog = true;
		}
		else if(this.id == 413)
		{
			this.text = Main.lv("So! When a new adventurer is born usually he learns one of the four base classes: Knight, Mage, Rogue or Ranger.", "Bem! Quando um novo aventureiro nasce normalmente ele aprende uma das quatro classes base: Guerreiro, Mago, Ladino ou Atirador.");
			this.npcPreference = 5;
		}
		else if(this.id == 414)
		{
			this.text = Main.lv("For that he needs to make use of a Scroll of Basic Knowledge, with which he learns the basics of a class, just like a 'level 1'.", "Para isso ele deve fazer uso de um Pergaminho do Conhecimento Básico, com o qual ele aprende os básicos de classe, como se fosse um 'nível 1'.");
			this.npcPreference = 5;
		}
		else if(this.id == 415)
		{
			this.text = Main.lv("We do not give advanced scrolls to new adventurers cause there is a famous saying in Arka that says: 'Goodwill grows over time, bad thoughts arise from low effort'.", "Nós não concedemos pergaminhos avançados à novos aventureiros pois há um ditado famoso em Arka que diz: 'A boa vontade cresce com o tempo, os pensamentos maus surgem da falta de esforço'.");
			this.npcPreference = 5;
		}
		else if(this.id == 416)
		{
			this.text = Main.lv("Imagine giving a profane an Knight Ancient Scroll.", "Imagine dar a um profano um Pegaminho Ancião do Guerreiro.");
			this.npcPreference = 5;
		}
		else if(this.id == 417)
		{
			this.text = Main.lv("It's like asking for a enemy to born! He do not know how hard was to gather this knowledge and will waste his power doing bad things!", "É como pedir para um inimigo nascer! Ele não sabe quão difícil foi juntar esse conhecimento e irá desperdiçar seu poder fazendo coisas ruins!");
			this.npcPreference = 5;
		}
		else if(this.id == 418)
		{
			this.text = Main.lv("I got it.", "Entendi.");
			this.playerDialog = true;
		}
		else if(this.id == 419)
		{
			this.text = Main.lv("So, what is Jason's plan?", "Então, qual é o plano de Jasão?");
			this.playerDialog = true;
		}
		else if(this.id == 420)
		{
			this.text = Main.lv("I don't know how exactly he is gonna do it, but he want to collect all the four Scrolls of Ancient Knowledge and dominate the Simplicity continent.", "Eu não sei exatamente como ele irá fazer, mas ele quer coletar os quatro Pergaminhos do Conhecimento Ancião e dominar o continente da Simplicidade.");
			this.npcPreference = 5;
		}
		else if(this.id == 421)
		{
			this.text = Main.lv("But luckly, Lushmael, Lewin, Viktor and I know where the four scrolls are. We can stop him!", "Mas por sorte, Lushmael, Lewin, Viktor e eu sabemos onde os quatro pergaminhos estão. Nós podemos pará-lo!");
			this.npcPreference = 5;
		}
		else if(this.id == 422)
		{
			this.text = Main.lv("Nice! You can count on me on that!", "Ótimo! Você pode contar comigo nessa!");
			this.playerDialog = true;
		}
		else if(this.id == 423)
		{
			this.text = Main.lv("We will certainly do!", "Nós certamente contaremos.");
			this.npcPreference = 5;
		}
		else if(this.id == 424)
		{
			this.text = Main.lv("But we have to do it fast, Jason seems to be allying with some bad intentioned people.", "Mas nós temos que fazer isso rápido, Jasão parece estar se aliando à algumas pessoas mal intencionadas.");
			this.npcPreference = 5;
		}
		else if(this.id == 425)
		{
			this.text = Main.lv("Lewin told me that the first one seems to be a colleague from the time he was in magic school.", "Lewin me contou que o primeiro parece ser um colega da época em que ele estava no colégio de magia.");
			this.npcPreference = 5;
		}
		else if(this.id == 426)
		{
			this.text = Main.lv("A man called Yokai, who ended up becoming a necromancer.", "Um rapaz chamado Yokai, que acabou se tornando um necromante.");
			this.npcPreference = 5;
		}
		else if(this.id == 427)
		{
			this.text = Main.lv("I don't know much about him, but if you see some zombies and ghosts in Simplicity just know that we are in danger.", "Nós não sabemos muito sobre ele, mas se você ver alguns fantasmas ou zumbis aqui em Simplicidade saiba que estamos em perigo.");
			this.npcPreference = 5;
		}
		else if(this.id == 428)
		{
			this.text = Main.lv("I will check it.", "Vou checar.");
			this.playerDialog = true;
		}
		else if(this.id == 429)
		{
			this.text = Main.lv("Good adventures my brave.", "Boas aventuras meu bravo.");
			this.npcPreference = 5;
			this.quit();
		}
		else if(this.id == 430)
		{
			this.text = Main.lv("Any news?", "Alguma novidade?");
			this.playerDialog = true;
		}
		else if(this.id == 431)
		{
			this.text = Main.lv("Yes, the other Stor Gave masters and I are studying a new enemy. Some dude called Jason.", "Sim, eu e os outros mestres de Stor Gave estamos estudando um novo inimigo. Um cara aí chamado Jasão.");
			this.npcPreference = 6;
		}
		else if(this.id == 432)
		{
			this.text = Main.lv("I finally can kill someone without being guilty!", "Eu finalmente posso matar alguém ser ser culpado!");
			this.npcPreference = 6;
		}
		else if(this.id == 433)
		{
			this.text = Main.lv("Death is better when there is no mercy.", "A morte é melhor quando não há piedade.");
			this.npcPreference = 6;
		}
		else if(this.id == 434)
		{
			this.text = Main.lv("It is nice to see you motivated to do something!", "É ótimo te ver motivado a fazer alguma coisa!");
			this.playerDialog = true;
		}
		else if(this.id == 435)
		{
			this.text = Main.lv("Death always motivates me, that's why I am a rogue!", "A morte sempre me motiva, é por isso que sou um ladino!");
			this.npcPreference = 6;
		}
		else if(this.id == 436)
		{
			this.text = Main.lv("And a GOOD one.", "E um dos BONS!");
			this.npcPreference = 6;
		}
		else if(this.id == 437)
		{
			this.text = Main.lv("We all know that!", "Todos sabemos disso!");
			this.playerDialog = true;
		}
		else if(this.id == 438)
		{
			this.text = Main.lv("Do you already know what you gonna do about Jason?", "Você já sabe o que vai fazer quanto ao Jasão?");
			this.playerDialog = true;
		}
		else if(this.id == 439)
		{
			this.text = Main.lv("Yes, I can kill it a billion ways! I'm just thinking which one I'm gonna use!", "Sim, eu posso matar ele de um bilhão de maneiras! Só estou pensando em qual delas vou usar!");
			this.npcPreference = 6;
		}
		else if(this.id == 440)
		{
			this.text = Main.lv("Autoconfidence is a thing.", "Autoconfiança é uma coisa.");
			this.playerDialog = true;
		}
		else if(this.id == 441)
		{
			this.text = Main.lv("If you gonna kill him, do it stylish.", "Se você for matar ele, faz com estilo.");
			this.playerDialog = true;
		}
		else if(this.id == 442)
		{
			this.text = Main.lv("Yeah, yeah. Gonna prepare some show off for my DAMN SKILLZ.", "Sim, sim. Vou preparar umas apresentações bonitas para as minhas demonstrar as minhas SKILLZ!");
			this.npcPreference = 6;
		}
		else if(this.id == 443)
		{
			this.text = Main.lv("Nice!", "Ótimo!");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 444)
		{
			this.text = Main.lv("But I am sure I could beat her with the POWER OF HUNGRINESS!", "Mas eu tenho certeza que eu ganharia dela com o PODER DA FOME!");
			this.npcPreference = 8;
			this.addOption(". . .", 404);
		}
		else if(this.id == 445)
		{
			this.text = Main.lv("The picture in this frame is actually the void deep in the purgatory.", "A imagem nesse quadro é na verdade o vazio nas profundezas do purgatório.");
			this.npcPreference = 8;
		}
		else if(this.id == 446)
		{
			this.text = Main.lv("It is a place with massively powered creatures. No one ever got out from there alive.", "Lá é um lugar com criaturas incrivelmente poderosas. Ninguém nunca saiu de lá vivo.");
			this.npcPreference = 8;
		}
		else if(this.id == 447)
		{
			this.text = Main.lv("Damn!", "Caramba!");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 448)
		{
			this.text = Main.lv("Hello little one.", "Olá pequenino.");
			this.npcPreference = 36;
		}
		else if(this.id == 449)
		{
			this.text = Main.lv("Hello!", "Olá!");
			this.playerDialog = true;
		}
		else if(this.id == 450)
		{
			this.text = Main.lv("Do you have any cool story about when you were younger?", "Você tem alguma história legal de quando você era mais jovem?");
			this.playerDialog = true;
		}
		else if(this.id == 451)
		{
			this.text = Main.lv("Yes, yes...", "Sim, sim...");
			this.npcPreference = 36;
			if(player.level < 10)
				this.addOption(". . .", 452);
			else
				this.addOption(". . .", 457);
		}
		else if(this.id == 452)
		{
			this.text = Main.lv("But it would not make sense to tell you anyway. You are too new to this adventure thing to understand.", "Mas de qualquer forma não faria sentido te contar. Você é muito jovem com essa coisa de aventura para entender.");
			this.npcPreference = 36;
		}
		else if(this.id == 453)
		{
			this.text = Main.lv("So... if I get better on that will you tell me?", "Então... se eu melhorar nisso você vai me contar?");
			this.playerDialog = true;
		}
		else if(this.id == 454)
		{
			this.text = Main.lv("Yes, yes...", "Sim, sim...");
			this.npcPreference = 36;
		}
		else if(this.id == 455)
		{
			this.text = Main.lv("Talk with me when you get a little better and I will tell you a legend.", "Converse comigo quando você ficar melhor e te contarei uma lenda.");
			this.npcPreference = 36;
		}
		else if(this.id == 456)
		{
			this.text = Main.lv("Okay.", "Ok.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 457)
		{
			this.text = Main.lv("You know... some legends and myths are dying in the course of the time...", "Sabe... algumas lendas e mitos estão morrendo com o passar do tempo...");
			this.npcPreference = 36;
		}
		else if(this.id == 458)
		{
			this.text = Main.lv("Or I would say...", "Ou melhor...");
			this.npcPreference = 36;
		}
		else if(this.id == 459)
		{
			this.text = Main.lv("In the curse of the time...", "Com a maldição do tempo...");
			this.npcPreference = 36;
		}
		else if(this.id == 460)
		{
			this.text = Main.lv("This is the legend of four demons called 'the Geas'.", "Essa é a lenda de quatro demônios chamados 'os Geas'.");
			this.npcPreference = 36;
		}
		else if(this.id == 461)
		{
			this.text = Main.lv("Angels and demons exists since the beginning of the first society.", "Os anjos e demônios existem desde o início da primeira sociedade.");
			this.npcPreference = 36;
		}
		else if(this.id == 462)
		{
			this.text = Main.lv("The angels protects the good ones and the demons punishes the bad ones.", "Os anjos protegem os bons e os demônios punem os maus.");
			this.npcPreference = 36;
			this.addOption(". . .", 463);
		}
		else if(this.id == 463)
		{
			this.text = Main.lv("I lived a life of adventures, always being a good person to be protected by the angels and not harmed by the demons.", "Eu vivi uma vida de aventuras, sempre sendo uma boa pessoa para ser protegido pelos anjos e não ser machucado pelos demônios.");
			this.npcPreference = 36;
			this.addOption(". . .", 465);
		}
		else if(this.id == 464)
		{
			this.text = Main.lv("Humans have never been perfect... In every society there have always been sins, crimes and any bad behaviour...", "Os humanos nunca foram perfeitos... Em todas as sociedades sempre houveram pecados, crimes e todo tipo de comportamento mau...");
			this.npcPreference = 36;
			this.addOption(". . .",  466);
		}
		else if(this.id == 465)
		{
			this.text = Main.lv("Anyway...", "De qualquer forma...");
			this.npcPreference = 36;
			this.addOption(". . .",  464);
		}
		else if(this.id == 466)
		{
			this.text = Main.lv("They punished four social sins:", "Eles puniam quatro pecados sociais:");
			this.npcPreference = 36;
		}
		else if(this.id == 467)
		{
			this.text = Main.lv("The intolerance, the ignorance, the hypocrisy and the insensibility.",
					"A intolerância, a ignorância, a hipocrisia e a insensibilidade.");
			this.npcPreference = 36;
		}
		else if(this.id == 468)
		{
			this.text = Main.lv("These acts caught the attention of the Geas, who would appear nearby.", "Esses atos chamavam a atenção dos Geas, que apareceriam por perto.");
			this.npcPreference = 36;
		}
		else if(this.id == 469)
		{
			this.text = Main.lv("The society was made as an union, people should help each other. But when this didn't happen...", 
					"A sociedade foi feita como uma união, as pessoas deveriam ajudar umas as outras. Mas quando isso não acontecia...");
			this.npcPreference = 36;
		}
		else if(this.id == 470)
		{
			this.text = Main.lv("At night, in the sinner's first moment of solitude the beast would appear.", "Durante a noite, no primeiro momento de solidão do pecador a besta iria aparecer.");
			this.npcPreference = 36;
		}
		else if(this.id == 471)
		{
			this.text = Main.lv("And that night...", "E essa noite...");
			this.npcPreference = 36;
		}
		else if(this.id == 472)
		{
			this.text = Main.lv("That night is the last night of his life.", "Essa noite é a última noite de sua vida.");
			this.npcPreference = 36;
		}
		else if(this.id == 473)
		{
			this.text = Main.lv("But demons don't live forever, they are not immortals.", "Mas demônios não vivem pra sempre, eles não são imortais.");
			this.npcPreference = 36;
		}
		else if(this.id == 474)
		{
			this.text = Main.lv("Indeed, I don't know how long the demons live.", "De fato, eu não sei quanto tempo os demônios vivem.");
			this.npcPreference = 36;
		}
		else if(this.id == 475)
		{
			this.text = Main.lv("But their time has come.", "Mas seus tempos chegaram.");
			this.npcPreference = 36;
		}
		else if(this.id == 476)
		{
			this.text = Main.lv("And the Demon King would never let the Geas die.", "E o Rei dos Demônios nunca deixaria os Geas morrerem.");
			this.npcPreference = 36;
		}
		else if(this.id == 477)
		{
			this.text = Main.lv("The cruelty of time could not erase them from existence.", "A crueldade do tempo não poderia apagá-los da existência.");
			this.npcPreference = 36;
		}
		else if(this.id == 478)
		{
			this.text = Main.lv("Their legacies should punish society forever for their sins.", "Seus legados deveriam punir a sociedade para sempre por seus pecados.");
			this.npcPreference = 36;
		}
		else if(this.id == 479)
		{
			this.text = Main.lv("So he took their souls and forged four floating blades.", "Então ele tomou suas almas e forjou quatro lâminas flutuantes.");
			this.npcPreference = 36;
		}
		else if(this.id == 480)
		{
			this.text = Main.lv("He stored the Geas in his throne, having knowledge that his owner should be trustable.", "Ele guardou os Geas no seu trono, tendo conhecimento de que seu portador deve ser confiável.");
			this.npcPreference = 36;
		}
		else if(this.id == 481)
		{
			this.text = Main.lv("Cause whoever holds the Geas will have the control of the societies.", "Pois aquele que portar os Geas terá o controle das sociedades.");
			this.npcPreference = 36;
		}
		else if(this.id == 482)
		{
			this.text = Main.lv("\"You can forget your sins, but they won't forget you.\"", "\"Você pode esquecer seus pecados, mas eles não se esquecerão de você.\"");
			this.npcPreference = 36;
		}
		else if(this.id == 483)
		{
			this.text = Main.lv("This was Demon King's last sentence before losing his post.", "Foi a última frase do Rei dos Demônios antes de perder seu posto.");
			this.npcPreference = 36;
		}
		else if(this.id == 484)
		{
			this.text = Main.lv("It's a cool legend, but is it true?", "É uma lenda legal, mas é verdade?");
			this.playerDialog = true;
		}
		else if(this.id == 485)
		{
			this.text = Main.lv("Like... it's a legend. It's impossible to know.", "É que... é uma lenda. Não da pra saber.");
			this.npcPreference = 36;
		}
		else if(this.id == 486)
		{
			this.text = Main.lv("But these legends are fading over time, these are things you can only dream with. They are never coming back.", "Mas essas lendas estão desaparecendo com o tempo, são coisas com as quais você só pode sonhar. Elas nunca voltarão.");
			this.npcPreference = 36;
		}
		else if(this.id == 487)
		{
			this.text = Main.lv("People today are liars, nobody does good if they don't expect something in return.", "As pessoas hoje em dia são mentirosas, ninguém faz o bem se não espera algo em troca.");
			this.npcPreference = 36;
		}
		else if(this.id == 488)
		{
			this.text = Main.lv("All because the Geas disappeared.", "Tudo isso porque os Geas desapareceram.");
			this.npcPreference = 36;
		}
		else if(this.id == 489)
		{
			this.text = Main.lv("Maybe if they still existed the world would be a good place.", "Talvez se eles ainda existissem o mundo seria um bom lugar.");
			this.npcPreference = 36;
		}
		else if(this.id == 490)
		{
			this.text = Main.lv("With no punishment the bad ones have nothing to fear.", "Sem punição os maus não têm o que temer.");
			this.npcPreference = 36;
		}
		else if(this.id == 491)
		{
			this.text = Main.lv("I think so...", "Eu também acho...");
			this.playerDialog = true;
		}
		else if(this.id == 492)
		{
			this.text = Main.lv("Also, I didn't know demons didn't harm good people.", "Aliás, eu não sabia que demônios não machucavam pessoas boas.");
			this.playerDialog = true;
		}
		else if(this.id == 493)
		{
			this.text = Main.lv("Yes, this is a common sense. If you are not a bad person or is not invading their home, you do not have to fear them.", 
					"Sim, isso é um senso comum. Se você não é uma pessoa ruim ou não está invadindo o lar deles, você não precisa ter medo.");
			this.npcPreference = 36;
		}
		else if(this.id == 494)
		{
			this.text = Main.lv("It is good to know.", "É bom saber.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 495)
		{
			this.text = Main.lv("Beware the hole!", "Cuidado com o buraco!");
			this.npcPreference = -1;
			this.quit();
		}
		else if(this.id == 496)
		{
			this.text = Main.lv("Hmm... {PLAYER} I see...", "Hmm... {PLAYER} eu vejo...");
			this.npcPreference = 37;
		}
		else if(this.id == 497)
		{
			this.text = Main.lv("What are you searching for here?", "O que procura por aqui?");
			this.npcPreference = 37;
		}
		else if(this.id == 498)
		{
			this.text = Main.lv("It's all right here?", "Está tudo bem por aqui?");
			this.playerDialog = true;
		}
		else if(this.id == 499)
		{
			this.text = Main.lv("For now.", "Por enquanto.");
			this.npcPreference = 37;
		}
		else if(this.id == 500)
		{
			this.text = Main.lv("While I'm here Amazona will be protected.", "Enquanto eu estiver por aqui Amazona estará protegida.");
			this.npcPreference = 37;
		}
		else if(this.id == 501)
		{
			this.text = Main.lv("Have there been any incidents recently?", "Houve algum incidente recentemente?");
			this.playerDialog = true;
		}
		else if(this.id == 502)
		{
			this.text = Main.lv("There is always an incident.", "Sempre há um incidente.");
			this.npcPreference = 37;
		}
		else if(this.id == 503)
		{
			this.text = Main.lv("Or do you think you are here by chance?", "Ou você pensa que está aqui por acaso?");
			this.npcPreference = 37;
		}
		else if(this.id == 504)
		{
			this.text = Main.lv("I am everywhere by chance.", "Eu estou em todo lugar por acaso.");
			this.playerDialog = true;
		}
		else if(this.id == 505)
		{
			this.text = Main.lv("But you didn't answer my question!", "Mas você não respondeu minha pergunta!");
			this.playerDialog = true;
		}
		else if(this.id == 506)
		{
			this.text = Main.lv("Yes, there have been an incident.", "Sim, houve um incidente.");
			this.npcPreference = 37;
		}
		else if(this.id == 507)
		{
			this.text = Main.lv("It has been some time since I see ghosts down here.", "Já faz algum tempo que estou vendo fantasmas por aqui.");
			this.npcPreference = 37;
		}
		else if(this.id == 508)
		{
			this.text = Main.lv("But I feel in them, they are inocent ghosts. They were killed and brought to this plane by someone.", "Mas eu sinto neles, eles são fantasmas inocentes. Eles foram mortos e conduzidos para este plano por alguém.");
			this.npcPreference = 37;
		}
		else if(this.id == 509)
		{
			this.text = Main.lv("Perhaps it's a necromancer.", "Talvez seja um necromante.");
			this.npcPreference = 37;
		}
		else if(this.id == 510)
		{
			this.text = Main.lv("I also saw a ghost pirate in the overworld forest to the east, but he is not a recent ghost. No...", "Eu também vi um fantasma na floresta para o leste lá em cima, mas ele não é um fantasma recente. Não...");
			this.npcPreference = 37;
		}
		else if(this.id == 511)
		{
			this.text = Main.lv("He was also brought to this plane.", "Ele também foi conduzido a este plano.");
			this.npcPreference = 37;
		}
		else if(this.id == 512)
		{
			this.text = Main.lv("I feel something bad is going to happen...", "Sinto que algo ruim está para acontecer...");
			this.npcPreference = 37;
		}
		else if(this.id == 513)
		{
			this.text = Main.lv("I will do my best for it not to happen.", "Darei o meu melhor para que não aconteça.");
			this.playerDialog = true;
		}
		else if(this.id == 514)
		{
			this.text = Main.lv("Good luck, adventurer.", "Boa sorte, aventureiro...");
			this.npcPreference = 37;
			this.quit();
		}
		else if(this.id == 515)
		{
			int questState = player.getQuestState(11);
			if(questState == Constant.QUESTSTATE_NEVER)
			{
				this.text = Main.lv("Please, can you help me?", "Por favor, você pode me ajudar?");
				this.addOption(". . .", 516);
			}
			else if(questState == Constant.QUESTSTATE_COMPLETED)
			{
				this.text = Main.lv("Thank you for helping me.", "Obrigado por me ajudar.");
				this.quit();
			}
			else
			{
				if(!player.readyToAccomplish(11))
				{
					this.text = Main.lv("Bring me the essences as often as possible, please...", "Me traga as essências o mais rápido possível, por favor...");
					this.quit();
				}
				else
				{
					this.text = Main.lv("Thank you very much, you can keep this [EPIC]special recipe[].", "Muito obrigado, você pode ficar com essa [EPIC]receita especial[].");
					this.quit();
				}
			}
			this.npcPreference = 38;
		}
		else if(this.id == 516)
		{
			this.text = Main.lv("What is going on?", "O que está acontecendo?");
			this.playerDialog = true;
		}
		else if(this.id == 517)
		{
			this.text = Main.lv("Someone brought me to this plane, I need to go back.", "Alguém me trouxe para esse plano, eu preciso voltar.");
			this.npcPreference = 38;
		}
		else if(this.id == 518)
		{
			this.text = Main.lv("How do I help you?", "Como eu te ajudo?");
			this.playerDialog = true;
		}
		else if(this.id == 519)
		{
			this.text = Main.lv("Ask any mage you know for some magic essence, I can get back using its energy.", "Peça essência mágica para algum mago que você conheça, eu consigo voltar usando a energia dela.");
			this.npcPreference = 38;
		}
		else if(this.id == 520)
		{
			this.text = Main.lv("Alright.", "Certo.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 521)
		{
			this.text = Main.lv("It's not a real door, it's just a painting on the wall", "Não é uma porta de verdade, é só uma pintura na parede.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 522)
		{
			this.text = Main.lv("I need to get some magic essence.", "Eu preciso pegar um pouco de essência mágica.");
			this.playerDialog = true;
		}
		else if(this.id == 523)
		{
			this.text = Main.lv("Alright, just bring me some jewels and I can do it for you!", "Certo, só me traz umas jóias que eu faço pra você!");
			this.npcPreference = 8;
		}
		else if(this.id == 524)
		{
			this.actionOnly = true;
			this.quit();
		}
		else if(this.id == 525)
		{
			this.text = Main.lv("You're welcome...", "De nada...");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 526)
		{
			int qs = player.getQuestState(12);
			if(qs == Constant.QUESTSTATE_NEVER)
			{
				this.text = Main.lv("Hello good adventurer...", "Olá bom aventureiro...");
			}
			else if(qs == Constant.QUESTSTATE_PROGRESS)
			{
				this.text = Main.lv("Bring me the essences as soon as you can.", "Me traga as essências assim que possível.");
				this.quit();
			}
			else
			{
				this.text = Main.lv("Thank you very much, adventurer. I shall return now.", "Muito obrigada, aventureiro. Eu devo retornar agora.");
				this.addOption(". . .", 548);
			}
			this.npcPreference = 39;
		}
		else if(this.id == 527)
		{
			this.text = Main.lv("Hello totally trustable ghost!", "Olá fantasma definitivamente confiável!");
			this.playerDialog = true;
		}
		else if(this.id == 528)
		{
			this.text = Main.lv("I was the Stor Gave Knight Master while I was alive, you can trust me.", "Eu era a Mestra dos Guerreiros de Stor Gave enquanto estava viva, você pode confiar em mim.");
			this.npcPreference = 39;
		}
		else if(this.id == 529)
		{
			this.text = Main.lv("Okay, so who is the actual master?", "Ok, então em quem é o mestre atual?");
			this.playerDialog = true;
		}
		else if(this.id == 530)
		{
			this.text = Main.lv("I don't know, I died like...", "Eu não sei, eu morri há mais ou menos...");
			this.npcPreference = 39;
		}
		else if(this.id == 531)
		{
			this.text = Main.lv("150 years?", "150 anos?");
			this.npcPreference = 39;
		}
		else if(this.id == 532)
		{
			this.text = Main.lv("Time is different in different planes.", "O tempo é diferente em planos diferentes.");
			this.npcPreference = 39;
		}
		else if(this.id == 533)
		{
			this.text = Main.lv("Do you know who Leonidas is?", "Você sabe quem é Leonidas?");
			this.playerDialog = true;
		}
		else if(this.id == 534)
		{
			this.text = Main.lv("Leonidas...", "Leonidas...");
			this.npcPreference = 39;
		}
		else if(this.id == 535)
		{
			this.text = Main.lv("I know this name...", "Eu conheço esse nome...");
			this.npcPreference = 39;
		}
		else if(this.id == 536)
		{
			this.text = Main.lv("I guess my apprentice trained this man when he took my post as the Stor Gave Knight Master...", "Eu acho que meu aprendiz treinou este homem quando ele tomou meu posto de Mestre dos Guerreiros de Stor Gave...");
			this.npcPreference = 39;
		}
		else if(this.id == 537)
		{
			this.text = Main.lv("Leonidas, the Brave!", "Leonidas, o Bravo!");
			this.npcPreference = 39;
		}
		else if(this.id == 538)
		{
			this.text = Main.lv("Got it!", "Lembrei!");
			this.npcPreference = 39;
		}
		else if(this.id == 539)
		{
			this.text = Main.lv("Congratulations you just achieved [EPIC]nothing[]!", "Parabéns você acabou de conseguir [EPIC]nada[]!");
			this.playerDialog = true;
		}
		else if(this.id == 540)
		{
			this.text = Main.lv("Respect the older ones, [RED]please[].", "Respeite os mais velhos, [RED]por favor[].");
			this.npcPreference = 39;
		}
		else if(this.id == 541)
		{
			this.text = Main.lv(":(", ":(");
			this.playerDialog = true;
		}
		else if(this.id == 542)
		{
			this.text = Main.lv("Okay, okay. So, what do you want from me?", "Ok, ok. Então, o que você quer de mim?");
			this.playerDialog = true;
		}
		else if(this.id == 543)
		{
			this.text = Main.lv("I just want to go back to the ghosts plane.", "Eu só quero voltar para os planos dos fantasmas.");
			this.npcPreference = 39;
		}
		else if(this.id == 544)
		{
			this.text = Main.lv("Just bring me some magic essence so I can return.", "Apenas me traga um pouco de essência mágica para que eu possa retornar.");
			this.npcPreference = 39;
			this.canExit = false;
		}
		else if(this.id == 545)
		{
			this.text = Main.lv("If you don't know how to get it try talking to any mage you know.", "Se você não sabe como conseguir tente conversar com algum mago que você conheça.");
			this.npcPreference = 39;
		}
		else if(this.id == 546)
		{
			this.text = Main.lv("Alright.", "Certo.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 547)
		{
			this.text = Main.lv("Thank you, I shall return now.", "Obrigada, eu devo retornar agora.");
			this.npcPreference = 39;
		}
		else if(this.id == 548)
		{
			this.text = Main.lv("You're welcome.", "De nada.");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 549)
		{
			this.text = Main.lv("It is very well done!", "Será que eu poço pegar água aqui?");
			this.playerDialog = true;
		}
		else if(this.id == 550)
		{
			this.text = Main.lv("Got it?", "Entendeu?");
			this.playerDialog = true;
			this.quit();
		}
		else if(this.id == 1000)
		{
			this.text = Main.lv("It is locked.", "Está trancado.");
			this.playerDialog = true;
			this.quit();
		}
		/* 
		 this.setOption(0, ". . .", this.id+1);
		 */
		return this;
	}
	
	public void quit()
	{
		this.addOption(". . .", -1);
	}

	public void doAction(int opt)
	{
		if(this.optionsDialog[opt] == -1)
		{
			Main.displayDialog = false;
			Main.lastDialogTicks = 0;
		}
		else
		{
			Dialogs dialog = new Dialogs().setInfos(optionsDialog[opt]);
			dialog.onOpen(Main.player[Main.me]);
			if(!dialog.actionOnly && !Main.displayDialog && Main.cutscene == null)
			{
				Main.cutsceneBordersTicks = 0;
			}
			Main.displayDialog = true;
			dialog.text = dialog.text.replaceAll(Pattern.quote ("{PLAYER}"), Main.saveName);
			String upperName = Main.saveName.toUpperCase();
			dialog.text = dialog.text.replaceAll(Pattern.quote ("{UPLAYER}"), upperName);
			Main.dialog = dialog;
			Main.dialogTicks = 0;
			Main.dialogFullDrawn = false;
			if(Main.dialog.actionOnly)
				Main.displayDialog = false;
			
			if(Main.dialog.npcPreference > 0 && SaveInfos.findNPCWithID(Main.dialog.npcPreference) != null)
				Main.dialogEntity = SaveInfos.findNPCWithID(Main.dialog.npcPreference);
			else if(Main.dialog.npcPreference == -1)
				Main.dialogEntity = null;
			
		}
	}

	public void onOpen(Player player)
	{
		Entity ent = Main.dialogEntity;
		int direction;
		if(ent != null)
			direction = (player.Center().x < ent.Center().x ? -1 : 1);
		else
			direction = 0;
		
		if(this.id == 4)
		{
			Item item = new Item().SetInfos(15);
			item.position = ent.Center();
			item.position.x -= 16;
			item.velocity.y = 100;
			item.velocity.x = 100 * direction;
			item.inWorld = true;
			item.timeInWorld = 0;
			Main.items.add(item);
		}
		else if(this.id == 10)
		{
			Main.dialogEntity = null;
		}
		else if(this.id == 20)
		{
			transition(0, 300, 300, 300);
			Main.displayDialog = false;
			player.saveInfo.bragusInPlace = 1;
			
			for(Quest q : player.quests)
			{
				if(q.id == 3)
				{
					q.objectives.clear();
					q.objectives.add(new Objective(5));
					player.updateObjectives(q);
				}
			}
		}
		else if(this.id == 21)
		{
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			NPC victor = SaveInfos.findNPCWithID(Constant.NPCID_VICTOR);
			bragus.lookingTo = victor;
			victor.lookingTo = bragus;
		}
		else if(this.id == 28)
		{
			dropItem(34);
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			bragus.useDialog = -1;
			NPC victor = SaveInfos.findNPCWithID(Constant.NPCID_VICTOR);
			bragus.lookingTo = player;
			victor.lookingTo = player;
		}
		else if(this.id == 32)
		{
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			NPC victor = SaveInfos.findNPCWithID(Constant.NPCID_VICTOR);
			bragus.lookingTo = victor;
			victor.lookingTo = null;
		}
		else if(this.id == 37)
		{
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			NPC leonidas = SaveInfos.findNPCWithID(Constant.NPCID_LEONIDAS);
			bragus.lookingTo = leonidas;
		}
		else if(this.id == 40)
		{
			NPC leonidas = SaveInfos.findNPCWithID(Constant.NPCID_LEONIDAS);
			leonidas.lookingTo = player;
		}
		else if(this.id == 48)
		{
			dropItem(35);
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			bragus.useDialog = -1;
			NPC leonidas = SaveInfos.findNPCWithID(Constant.NPCID_LEONIDAS);
			bragus.lookingTo = player;
			leonidas.lookingTo = player;
		}
		else if(this.id == 49)
		{
			transition(1, 300, 300, 300);
			Main.displayDialog = false;
			player.saveInfo.bragusInPlace = 2;
		}
		else if(this.id == 52)
		{
			player.inventory[Constant.ITEMSLOT_LEFT] = null;
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			NPC leonidas = SaveInfos.findNPCWithID(Constant.NPCID_LEONIDAS);
			bragus.lookingTo = leonidas;
			leonidas.lookingTo = null;
		}
		else if(this.id == 62)
		{
			transition(2, 300, 300, 300);
			Main.displayDialog = false;
			player.saveInfo.bragusInPlace = 3;
		}
		else if(this.id == 64)
		{
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			NPC lushmael = SaveInfos.findNPCWithID(6);
			bragus.lookingTo = lushmael;
		}
		else if(this.id == 66)
		{
			NPC n = NPC.CreateBase(new Vector2(1376, 64), Constant.NPCID_LUSHMAEL);
			n.lookingTo = player;
			n.myMapX = player.myMapX;
			n.myMapY = player.myMapY;
			Main.npc.add(n);
			NPC clone = SaveInfos.findNPCWithID(6);
			if(clone != null)
			{
				for(int i = 0;i < 15;i++)
				{
					float r = 0.75f + 0.25f * MathUtils.random();
					Color c = new Color(r, r, r, 1f);
					Particle p = Particle.Create(clone.randomHitBoxPosition(), Vector2.Zero, 3, c, 4, MathUtils.random(1.25f, 1.75f), MathUtils.random(0.5f, 1.5f));
					p.drawFront = true;
				}
				Main.npc.remove(clone);
			}
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			bragus.lookingTo = n;
			//Main.npc.remove(DecisionSaver.findNPCWithID(6));
		}
		else if(this.id == 68)
		{
			Cutscene.Start(1, player);
		}
		else if(this.id == 74)
		{
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			NPC lushmael = SaveInfos.findNPCWithID(Constant.NPCID_LUSHMAEL);
			bragus.lookingTo = player;
			lushmael.lookingTo = player;
		}
		else if(this.id == 85)
		{
			dropItem(15);
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			bragus.useDialog = -1;
		}
		else if(this.id == 87)
		{
			player.inventory[Constant.ITEMSLOT_LEFT] = null;
		}
		else if(this.id == 95)
		{
			player.saveInfo.bragusInPlace = 4;
			transition(3, 300, 300, 300);
		}
		else if(this.id == 97)
		{
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			NPC lewin = SaveInfos.findNPCWithID(Constant.NPCID_LEWIN);
			bragus.lookingTo = lewin;
			lewin.lookingTo = bragus;
		}
		else if(this.id == 99)
		{
			player.saveInfo.toldNameToLewin = true;
		}
		else if(this.id == 102)
		{
			player.saveInfo.lewinFeeling = Constant.LEWINFEELING_SADNESS;
		}
		else if(this.id == 110)
		{
			dropItem(38);
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			NPC lewin = SaveInfos.findNPCWithID(Constant.NPCID_LEWIN);
			bragus.lookingTo = player;
			lewin.lookingTo = player;
			bragus.useDialog = -1;
		}
		else if(this.id == 111)
		{
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			NPC lewin = SaveInfos.findNPCWithID(Constant.NPCID_LEWIN);
			bragus.lookingTo = lewin;
		}
		else if(this.id == 112)
		{
			player.inventory[Constant.ITEMSLOT_LEFT] = null;
		}
		else if(this.id == 118)
		{
			NPC bragus = SaveInfos.findNPCWithID(Constant.NPCID_BRAGUS);
			bragus.direction = Main.directionFromTo(bragus.Center(), player.Center());
		}
		else if(this.id == 123)
		{
			dropItem(39);
		}
		else if(this.id == 124)
		{
			dropItem(40);
		}
		else if(this.id == 125)
		{
			dropItem(41);
		}
		else if(this.id == 126)
		{
			dropItem(42);
		}
		else if(this.id == 133)
		{
			player.saveInfo.toldNameToFarmer = false;
		}
		else if(this.id == 149)
		{
			player.saveInfo.chapter2helpedFarmer = true;
			
			Quest q;
			if((q = player.getQuest(2)) != null)
			{
				q.objectives.clear();
				q.objectives.add(Quest.generateKillObjective(2, 1));
				player.updateObjectives(q);
				GameMap g = Main.PreLoadMap(47, 150);
				Monster.Create(g, new Vector2(1000, 1000), 2);
				Main.SaveMap(g, 47, 150);
				q.npcQuesterId = 9;
			}
			NPC farmer = SaveInfos.findNPCWithID(9);
			farmer.useDialog = 162;
		}
		else if(this.id == 161)
		{
			transition(48, 150, 100, 100);
			if(player.hasQuest(3))
			{
				player.accomplishQuest(3);
				player.addQuest(2, true);
			}
		}
		else if(this.id == 165)
		{
			Main.worldMap.expected[Constant.AIDIRECTION_RIGHT-1] = 1;
			Quest q;
			if((q = player.getQuest(2)) != null)
			{
				q.objectives.clear();
				q.objectives.add(new Objective(7));
				player.updateObjectives(q);
				q.npcQuesterId = 11;
			}
		}
		else if(this.id == 179)
		{
			Quest q;
			if((q = player.getQuest(2)) != null)
			{
				q.objectives.clear();
				q.objectives.add(new Objective(8));
				player.updateObjectives(q);
			}
			NPC n = SaveInfos.findNPCWithID(11);
			if(n != null)
			{
				n.useDialog = 203;
			}
		}
		else if(this.id == 184)
		{
			Prop prop = new Prop(new Vector2(2112, 256), 128, 128, Constant.PROPID_TELEPORT, new int[]{4, 300, 1024, 64}, true);
			Main.prop.add(prop);
			Quest q;
			if((q = player.getQuest(2)) != null)
			{
				q.objectives.clear();
				q.objectives.add(new Objective(9));
				player.updateObjectives(q);
			}
		}
		else if(this.id == 201)
		{
			NPC statue = SaveInfos.findNPCWithID(13);
			NPC goblin = SaveInfos.findNPCWithID(12);
			if(statue != null)
				Main.npc.remove(statue);
				
			if(goblin != null)
				Main.npc.remove(goblin);
			
			player.carryingGoblin = true;
			
			Objective o;
			if((o = player.haveObjective(9)) != null)
			{
				o.concluded = true;
			}
		}
		else if(this.id == 207)
		{
			this.dropItem(43);
			if((player.getQuest(2)) != null)
			{
				player.accomplishQuest(2);
				player.addQuest(4, true);
			}
			Main.worldMap.expected[Constant.AIDIRECTION_RIGHT-1] = 2;
			player.saveInfo.chapter2helpedCultist = true;
			player.carryingGoblin = false;
			NPC n = NPC.CreateBase(player.position, 13);
			Main.npc.add(n);
		}
		else if(this.id == 216)
		{
			NPC lildwa = SaveInfos.findNPCWithID(14);
			Main.npc.remove(lildwa);
			Quest q;
			if((q = player.getQuest(4)) != null)
			{
				q.objectives.clear();
				q.objectives.add(new Objective(11));
			}
			transition(52, 148, 3648, 2560);
		}
		else if(this.id == 219)
		{
			Main.earthquake = 10;
		}
		else if(this.id == 220)
		{
			Main.earthquake = 0;
			for(int y = 40;y < 44;y++)
			{
				for(int x = 60;x < 62;x++)
				{
					Main.worldMap.map[y][x] = null;
				}
			}
			for(int i = 0;i < 130;i++)
			{
				Vector2 position = new Vector2(60 * 64 + MathUtils.random(2*64), 40*64+MathUtils.random(4*64));
				Vector2 velocity = new Vector2(MathUtils.random(50, 100), MathUtils.random(-30, 30));
				float tone = MathUtils.random(0.05f, 0.2f);
				Color c = new Color(tone, tone, tone, 1f);
				Particle p = Particle.Create(position, velocity, 2, c, -3f, MathUtils.random(2f, 3f), MathUtils.random(0.75f, 1.5f));
				p.collisions = true;
			}
			for(int i = 0;i < 100;i++)
			{
				Vector2 position = new Vector2(60 * 64 + MathUtils.random(2*64), 40*64+MathUtils.random(4*64));
				Vector2 velocity = new Vector2(MathUtils.random(50, 100), MathUtils.random(-30, 30));
				Color c = new Color(1f, MathUtils.random(0.5f, 1f), 0f, 1f);
				Particle p = Particle.Create(position, velocity, 2, c, -3f, MathUtils.random(2f, 3f), MathUtils.random(0.75f, 1.5f));
				p.collisions = true;
			}
			Cutscene.Start(2, player);
		}
		else if(this.id == 223)
		{
			Monster spider = null;
			for(Monster m : Main.monster)
			{
				if(m.id == 2)
				{
					spider = m;
					break;
				}
			}
			if(spider != null)
				Main.dialogEntity = spider;
		}
		else if(this.id == 226)
		{
			int[] validMonsters;
			int[] validMaterials;

			if(player.level <= 5)
			{
				validMonsters = new int[] {
						23, 3,
						36, 10,
						25, 10,
						10, 10
				};
				validMaterials = new int[] {
						83, 20,
						114, 6,
						113, 6,
						125, 3,
						126, 6
				};
			}
			else
			{
				validMonsters = new int[] {
						29, 5,
						18, 10,
						20, 10,
						27, 10,
						26, 10
				};
				validMaterials = new int[] {
						83, 30,
						130, 6,
						135, 3,
						185, 6,
						186, 6,
						187, 1
				};
			}

			Quest q = player.addQuest(5, true);
			q.rewardXP = Math.min((int) (player.level * 10 + player.nextLevelExp() * 0.2f), 350);
			q.rewardGold = Math.min((int) (Math.pow(player.level, 2f) * 2f), 200);
			if(MathUtils.randomBoolean())
			{
				Monster m = new Monster();
				int type = MathUtils.random(0, (validMonsters.length-2)/2)*2;
				m.id = validMonsters[type];
				m.Reset(true);
				int quant = validMonsters[type+1];
				this.text = Main.lv("Okay, kill me "+quant+" " + Main.pluralOf(m.name) + " for me.",
						"Tudo bem, mate "+quant+" " + Main.pluralOf(m.name) + " pra mim.");
				q.objectives.add(Quest.generateKillObjective(m.id, quant));
			}
			else
			{
				int type = MathUtils.random(0, (validMaterials.length-2)/2)*2;
				Item i = new Item().SetInfos(validMaterials[type]);
				int quant = validMaterials[type+1];
				this.text = Main.lv("Okay, collect "+quant+" " + Main.pluralOf(i.name) + " for me.",
						"Tudo bem, colete "+quant+" " + Main.pluralOf(i.name) + " pra mim.");
				q.objectives.add(Quest.generateCollectObjective(i.id, quant));
			}
		}
		else if(this.id == 228)
		{
			if(player.getQuest(5) != null)
			{
				Objective o = null;
				if((o = player.getQuest(5).getObjective(2))!=null)
				{
					player.removeItem(o.infos[0], o.infos[1]);
				}
				this.dropItem(115);
				player.accomplishQuest(5);
			}
		}
		else if(this.id == 231)
		{
			Main.displayWarps = true;
		}
		else if(this.id == 232)
		{
			Warp warp = Warp.AECIA;
			this.transition(warp.mapX, warp.mapY, warp.localMapX, warp.localMapY);
		}
		else if(this.id == 233)
		{
			Warp warp = Warp.OLDGAVE;
			this.transition(warp.mapX, warp.mapY, warp.localMapX, warp.localMapY);
		}
		else if(this.id == 240)
		{
			String[] wadetexts = new String[] {
				Main.lv("I still believe that the universe is a matrix and we all live in a simulation.",
						"Eu ainda acredito que o universo é uma matrix e nós todos vivemos em uma simulação."),
				Main.lv("Is there life outside Arka?",
						"Existe vida fora de Arka?")
			};
			this.text = wadetexts[MathUtils.random(wadetexts.length-1)];
		}
		else if(this.id == 243)
		{
			String[] leontexts = new String[] {
				Main.lv("Axes causes a very increased damage against trees the same way pickaxes causes very increased "
						+ "damage against stones and ores.", 
						"Machados causam danos aumentados contra árvores da mesma maneira que picaretas causam danos aumentados "
						+ "contra pedras e minérios."),
				Main.lv("As a Knight you do not have to fear facing your enemies as you naturally takes reduced damage,"
				+ " especially if you are focusing have much vitality.",
						"Como sendo um Knight, você não precisa ter medo de encarar seus inimigos já que você naturalmente "
						+ "recebe dano reduzido, principalmente se você estiver focando ter muita vitalidade."),
				Main.lv("Knights can be very hard to kill if they have much vitality. His life becomes increased and "
						+ "his increased life regeneration helps him get his life back between the fights.",
						"Knights podem ser bem difíceis de matar quando eles possuem muita vitalidade. Sua vida é aumentada e "
						+ "sua regeneração de vida aumentada ajuda-o a restaurar sua vida mais rápido entre as lutas."),
				Main.lv("Knights can cause very much damage with his skills and weapons when they have much strenght "
						+ "or lethality. But still, they cause very much damage with big cooldowns between them.",
						"Knights podem causar muito dano com suas habilidades e armas quando eles possuem muita força "
						+ "ou letalidade. Mas ainda sim, eles causam muito dano com grandes intervalos entre eles."),
				Main.lv("Knights can have a big constant damage when they have much agility. Agility can be even more useful "
						+ "when holding dual weapons.",
						"Knights podem ter um dano grande e constante quando eles possuem muita agilidade. Agilidade pode "
						+ "ser ainda mais útil quando duas armas estão equipadas ao mesmo tempo.")
			};
			this.text = leontexts[MathUtils.random(leontexts.length-1)];
		}
		else if(this.id == 246)
		{
			player.saveInfo.metSGWestGuard = true;
		}
		else if(this.id == 249)
		{
			player.addQuest(6, true);
		}
		else if(this.id == 253)
		{
			player.removeItem(78, 10);
			this.dropItem(19);
			player.accomplishQuest(6);
		}
		else if(this.id == 259)
		{
			Item.Create(new Vector2(20352, 576), new Vector2(0f, 400f), 79, player.myMapX, player.myMapY, true);
			
			for(Prop p : Constant.getPropList())
			{
				if(p.sameMapAs(player) && p.infos[0] == 258)
				{
					p.usable = false;
				}
			}
			for(NPC n : Constant.getNPCList())
			{
				if(n.sameMapAs(player) && n.id == 23)
				{
					n.active = false;
				}
			}
		}
		else if(this.id == 262)
		{
			player.saveInfo.metBuckTheFarmer = true;
			NPC n = SaveInfos.findNPCWithID(24);
			n.SetInfos(24);
		}
		else if(this.id == 287)
		{
			GameMap m = Constant.getPlayerMap(player.whoAmI);
			if(m != null && Main.dialogEntity != null)
			{
				int myRoom = 0;
				float myX = Main.dialogEntity.Center().x;
				for(NPC n : Constant.getNPCList())
				{
					if(n.active && n.sameMapAs(Main.dialogEntity))
					{
						if(n.Center().x < myX)
						{
							myRoom++;
						}
					}
				}
				int waveSize = MathUtils.random(4,6) + myRoom * 3;
				m.summonWave(Main.dialogEntity.Center(), waveSize, -1);
				NPC sword = (NPC)Main.dialogEntity;
				sword.currentFrame = 1;
				sword.useDialog = -1;
				sword.infos[0] = 0;
				sword.infos[1] = waveSize;
			}
		}
		else if(this.id == 295)
		{
			ToggleCraftStation(1);
		}
		else if(this.id == 302)
		{
			if(Roguelike.language == Constant.LANGUAGE_PORTUGUESE)
			{
				String[] verses = new String[] {
						"[HOLY]Sua beleza é inexplicável,\n" + 
							"seu olhar é o que me alucina\n" + 
							"Nem mesmo o brilho da lua\n" + 
							"chega no sorriso dessa menina",
						"[ICE]Você está muito longe de mim\n" + 
							"pra eu pensar que você tá tão perto assim\n" + 
							"Acho que estou vendo coisas...\n" + 
							"Esse tipo de evento pra mim é incomum\n" + 
							"Te vejo em todo lugar\n" + 
							"mas a gente não forma par\n" + 
							"em lugar nenhum",
						"[ICE]Não te amo aos montes,\n" + 
							"te amo às montanhas\n" + 
							"Te peço: não jogue a gente fora\n" + 
							"No fim dessas brigas\n" + 
							"quem é que ganha?\n" + 
							"Eu sei que eu não sou,\n" + 
							"sou quem mais apanha",
						"[FIRE]Eu só quero voltar\n" + 
							"Naqueles momentos\n" + 
							"Que eu gosto de lembrar\n" + 
							"Mas tempos ruins\n" + 
							"Hoje me fazem feliz\n" + 
							"E os tempos bons\n" + 
							"Me fazem chorar",
						"[FIRE]Rabiscando minhas paredes desde o chão até o teto\n" + 
							"Um, dois, cem, perdi a conta de pensar quanto te quero\n" + 
							"Jogo com minha própria sombra tentando entender também\n" + 
							"A razão de você ter me trocado por outro alguém",
						"[HOLY]Se no mundo não houvesse cores,\n" + 
							"não houvesse dores, amores,\n" + 
							"não haveria sentimento\n" + 
							"sentiriamos a falta de algo\n" + 
							"que se perderia com o tempo.",
						"[FIRE]Eu me vejo só\n" + 
							"e vindo de lá eu fujo\n" + 
							"Pois o que eu sinto é dó\n" + 
							"de viver em um mundo tão sujo",
						"[ICE]Eu vejo no tempo que eu não me sustento\n" + 
							"e também não tenho pra onde correr\n" + 
							"Já pulei muralhas, já ganhei batalhas\n" + 
							"e não é esse o momento que eu quero perder"
				};

				this.text = verses[MathUtils.random(0, verses.length-1)]+"...[]";
			}
			else
			{
				this.text = "I will think in one.";
			}
		}
		else if(this.id == 314)
		{
			this.doAction(0);
		}
		else if(this.id == 317)
		{
			if(player.getQuest(7) != null)
			{
				player.accomplishQuest(7);
				player.removeItem(78, 35);
			}
		}
		else if(this.id == 326)
		{
			player.addQuest(7, true);
		}
		else if(this.id == 333)
		{
			Cutscene.Start(3, player);
		}
		else if(this.id == 336)
		{
			player.myMapX = 0;
			player.myMapY = 0;
			Main.SwitchMap(0, new Vector2(0, 0), null);
			player.onChangeMap();
			Main.blackScreenTime = -2f;
		}
		else if(this.id == 340)
		{
			int[] validMonsters;
			int[] validMaterials;
			if(player.level <= 20)
			{
				validMonsters = new int[] {
						21, 25,
						22, 25,
						35, 25,
						1, 25,
						30, 25
				};
				validMaterials = new int[] {
						140, 20,
						78, 20,
						120, 5
				};
			}
			else
			{
				validMonsters = new int[] {
						32, 25,
						37, 25,
						42, 25,
						44, 25
				};
				validMaterials = new int[] {
						120, 20,
						82, 10
				};
			}
			
			Quest q = player.addQuest(8, true);
			q.rewardXP = Math.min((int) (player.level * 15 + player.nextLevelExp() * 0.15f), 4500);
			q.rewardGold = Math.min((int) (Math.pow(player.level, 2f) * 2f), 2500);
			if(MathUtils.randomBoolean())
			{
				Monster m = new Monster();
				int type = MathUtils.random(0, (validMonsters.length-2)/2)*2;
				m.id = validMonsters[type];
				m.Reset(true);
				int quant = validMonsters[type+1];
				this.text = Main.lv("Okay, kill me "+quant+" " + Main.pluralOf(m.name) + " for me.",
						"Tudo bem, mate "+quant+" " + Main.pluralOf(m.name) + " pra mim.");
				q.objectives.add(Quest.generateKillObjective(m.id, quant));
			}
			else
			{
				int type = MathUtils.random(0, (validMaterials.length-2)/2)*2;
				Item i = new Item().SetInfos(validMaterials[type]);
				int quant = validMaterials[type+1];
				this.text = Main.lv("Okay, collect "+quant+" " + Main.pluralOf(i.name) + " for me.",
						"Tudo bem, colete "+quant+" " + Main.pluralOf(i.name) + " pra mim.");
				q.objectives.add(Quest.generateCollectObjective(i.id, quant));
			}
		}
		else if(this.id == 342)
		{
			if(player.getQuest(8) != null)
			{
				Objective o = null;
				if((o = player.getQuest(8).getObjective(2))!=null)
				{
					player.removeItem(o.infos[0], o.infos[1]);
				}
				this.dropItem(116);
				player.accomplishQuest(8);
			}
		}
		else if(this.id == 348)
		{
			player.addQuest(9, true);
		}
		else if(this.id == 352)
		{
			if(player.getQuest(9).isConcluded())
			{
				for(Objective o : player.getQuest(9).objectives)
				{
					if(o.id == 2)
					{
						player.removeItem(o.infos[0], o.infos[1]);
					}
				}
				player.accomplishQuest(9);
			}
		}
		else if(this.id == 360)
		{
			Main.setCustomCamera(player.Center(), 0.2f, 180);
		}
		else if(this.id == 361)
		{
			Main.customCameraTime = 0;
		}
		else if(this.id == 364)
		{
			Main.setCustomCamera(Main.dialogEntity.Center(), 0.2f, 180);
		}
		else if(this.id == 365)
		{
			Main.customCameraTime = 0;
		}
		else if(this.id == 366)
		{
			Main.setCustomCamera(Main.dialogEntity.Center(), -0.2f, 180);
		}
		else if(this.id == 367)
		{
			Main.customCameraTime = 0;
		}
		else if(this.id == 376)
		{
			player.addQuest(10, true);
		}
		else if(this.id == 379)
		{
			if(player.readyToAccomplish(10))
			{
				player.accomplishQuest(10);
				player.removeItem(168, 300);
			}
			//levar pra ilha da eternidade
		}
		else if(this.id == 460)
		{
			Main.loadStory(Story.NOTHING);
		}
		else if(this.id == 461)
		{
			Main.loadStory(Story.LAGEAS8);
		}
		else if(this.id == 463)
		{
			Main.loadStory(null);
		}
		else if(this.id == 464)
		{
			Main.loadStory(Story.NOTHING);
		}
		else if(this.id == 465)
		{
			//Main.loadStory(Story.LAGEAS2);
		}
		else if(this.id == 468)
		{
			Main.loadStory(Story.LAGEAS3);
		}
		else if(this.id == 470)
		{
			Main.loadStory(Story.LAGEAS4);
		}
		else if(this.id == 472)
		{
			Main.loadStory(Story.LAGEAS5);
			Main.currentStoryImageAlpha = 0f;
		}
		else if(this.id == 473)
		{
			Main.loadStory(Story.LAGEAS6);
			Main.currentStoryImageAlpha = 0f;
		}
		else if(this.id == 474)
		{
			Main.loadStory(Story.NOTHING);
		}
		else if(this.id == 476)
		{
			Main.loadStory(Story.LAGEAS7);
		}
		else if(this.id == 479)
		{
			Main.loadStory(Story.LAGEAS1);
		}
		else if(this.id == 482)
		{
			Main.loadStory(Story.NOTHING);
		}
		else if(this.id == 483)
		{
			Main.loadStory(null);
		}
		else if(this.id == 515)
		{
			if(player.readyToAccomplish(11))
			{
				player.accomplishQuest(11);
				Item item = new Item();
				item.data[0] = 175;
				item.SetInfos(115);
				this.dropItem(item);
				for(int i = 0;i < 20;i++)
				{
					this.dropItem(168);
				}
				final NPC n = SaveInfos.findNPCWithID(38, player);
				if(n != null)
				{
					n.useDialog = -1;
					n.temporary = true;
					for(int i = 0;i < 240;i++)
					{
						final int loop = i;
						Event e = new Event(i) {
							@Override
							public void function()
							{
								for(int j = 0;j < Math.min(loop*2,360);j++)
								{
									float sin = (float)Math.sin(j*Math.PI/180f);
									float cos = (float)Math.cos(j*Math.PI/180f);
									Vector2 pos = new Vector2(cos * 100, sin * 100).add(n.Center());
									Color c = new Color(0.92f, 0.97f, 0.83f, 1f);
									Particle p = Particle.Create(pos, Vector2.Zero, 2, new Color(0f,0f,0f,0f), 0f, 0.05f, 1f);
									p.setLight(32, c);
								}
								if(loop == 239)
								{
									Color c = new Color(0.92f, 0.97f, 0.83f, 1f);
									Particle.Create(n.Center(), Vector2.Zero, 11, c, 0f, 1f, 4f);
									n.active = false;
								}
							}
						};
						Main.scheduledTasks.add(e);
					}
				}
			}
		}
		else if(this.id == 519)
		{
			player.addQuest(11, true);
			player.saveInfo.canConvertMagicEssence = true;
		}
		else if(this.id == 524)
		{
			this.ToggleCraftStation(2);
		}
		else if(this.id == 526)
		{
			if(player.readyToAccomplish(12))
			{
				player.accomplishQuest(12);
				Item item = new Item();
				item.data[0] = 177;
				item.SetInfos(115);
				this.dropItem(item);
				for(int i = 0;i < 20;i++)
				{
					this.dropItem(168);
				}
				final NPC n = SaveInfos.findNPCWithID(39, player);
				if(n != null)
				{
					n.useDialog = -1;
					n.temporary = true;
					for(int i = 0;i < 240;i++)
					{
						final int loop = i;
						Event e = new Event(i) {
							@Override
							public void function()
							{
								for(int j = 0;j < Math.min(loop*2,360);j++)
								{
									float sin = (float)Math.sin(j*Math.PI/180f);
									float cos = (float)Math.cos(j*Math.PI/180f);
									Vector2 pos = new Vector2(cos * 100, sin * 100).add(n.Center());
									Color c = new Color(0.92f, 0.97f, 0.83f, 1f);
									Particle p = Particle.Create(pos, Vector2.Zero, 2, new Color(0f,0f,0f,0f), 0f, 0.05f, 1f);
									p.setLight(32, c);
								}
								if(loop == 239)
								{
									Color c = new Color(0.92f, 0.97f, 0.83f, 1f);
									Particle.Create(n.Center(), Vector2.Zero, 11, c, 0f, 1f, 4f);
									n.active = false;
								}
							}
						};
						Main.scheduledTasks.add(e);
					}
				}
			}
		}
		else if(this.id == 546)
		{
			player.addQuest(12, true);
			player.saveInfo.canConvertMagicEssence = true;
		}
	}

	private void ToggleCraftStation(int i)
	{
		Main.displayCrafting = !Main.displayCrafting;
		Main.displayCraftStation = i;
		Main.craftingY = -500;
		Main.displayCraftingRecipe = null;
		Main.displayCraftingOffset = 0;
		Main.displayDialog = false;
		Main.displayCraftOrigin = Main.dialogEntity;
		Main.cutsceneBordersTicks = 0;
	}

	public void travel(int destX, int destY)
	{
		Player player = Main.player[Main.me];
		player.updateCameMap();
		Main.SaveMap();
		player.myMapX = destX;
		player.myMapY = destY;
		Main.SwitchMap(10, false, false, null);
		player.onChangeMap();
	}
	
	public void transition(int destX, int destY, int destLocalX, int destLocalY)
	{
		Player player = Main.player[Main.me];
		player.updateCameMap();
		Main.mapTransitioning = true;
		Main.mapTransitionFromX = player.myMapX;
		Main.mapTransitionFromY = player.myMapY;
		Main.mapTransitionToX = destX;
		Main.mapTransitionToY = destY;
		Main.mapTransitionToFinalX = destLocalX;
		Main.mapTransitionToFinalY = destLocalY;
		Main.mapTransitionTicks = 0;
		player.onChangeMap();
	}
	
	public Item dropItem(int id)
	{
		int direction = (Main.player[Main.me].Center().x < Main.dialogEntity.Center().x ? -1 : 1);
		Item item = new Item().SetInfos(id);
		item.position = Main.dialogEntity.Center();
		item.position.x -= 16;
		item.velocity.y = 100;
		item.velocity.x = 100 * direction;
		item.inWorld = true;
		if(Main.dialogEntity != null)
		{
			item.myMapX = Main.dialogEntity.myMapX;
			item.myMapY = Main.dialogEntity.myMapY;
			item.timeInWorld = 0;
			Constant.getItemsList().add(item);
		}
		return item;
	}

	public Item dropItem(Item item)
	{
		int direction = (Main.player[Main.me].Center().x < Main.dialogEntity.Center().x ? -1 : 1);
		item.position = Main.dialogEntity.Center();
		item.position.x -= 16;
		item.velocity.y = 100;
		item.velocity.x = 100 * direction;
		item.inWorld = true;
		if(Main.dialogEntity != null)
		{
			item.myMapX = Main.dialogEntity.myMapX;
			item.myMapY = Main.dialogEntity.myMapY;
			item.timeInWorld = 0;
			Constant.getItemsList().add(item);
		}
		return item;
	}

	private void resetStats()
	{
		this.text = "";
		for(int i = 0;i < 4;i++)
		{
			this.options[i] = "";
			this.optionsDialog[i] = -1;
		}
		this.playerDialog = false;
		this.canExit = true;
		this.actionOnly = false;
		this.npcPreference = 0;
	}

	private void setOption(int optionId, String text, int dialogId)
	{
		this.options[optionId] = text;
		this.optionsDialog[optionId] = dialogId;
	}
	
	private void addOption(String text, int dialogId)
	{
		int slot = 3;
		for(int i = 0;i < 3;i++)
		{
			if(this.options[i].length() <= 1 || this.options[i].equalsIgnoreCase(". . ."))
			{
				slot = i;
				break;
			}
		}
		this.setOption(slot, text, dialogId);
	}
}