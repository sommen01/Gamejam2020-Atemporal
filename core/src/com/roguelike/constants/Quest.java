package com.roguelike.constants;

import java.util.ArrayList;
import java.util.List;

import com.roguelike.entities.Player;
import com.roguelike.game.Main;

public class Quest
{
	public int id;
	public String name;
	public String description;
	public List<Objective> objectives = new ArrayList<Objective>();
	public int rewardXP;
	public int rewardGold;
	public int npcQuesterId;
	public int step;

	public Quest setInfos(int id)
	{
		this.reset();
		this.id = id;
		if(this.id == 1)
		{
			this.name = Main.lv("Arachnofobia", "Aracnofobia");
			this.description = "Gill called you to kill some spiders in his house, he probably have arachnofobia.";
			this.rewardXP = 300;
			this.rewardGold = 50;
			this.objectives.add(Quest.generateKillObjective(1, 4));
		}
		else if(this.id == 2)
		{
			this.name = Main.lv("A New World", "Um Novo Mundo");
			this.description = "A whole new world opens for you.";
			this.rewardXP = 100;
			this.rewardGold = 10;
			this.objectives.add(new Objective(6));
		}
		else if(this.id == 3)
		{
			this.name = Main.lv("Freedom", "Liberdade");
			this.description = "I think you are free now.";
			this.rewardXP = 50;
			this.rewardGold = 5;
			this.objectives.add(new Objective(4));
		}
		else if(this.id == 4)
		{
			this.name = Main.lv("Hammer & Anvil", "Martelo & Bigorna");
			this.description = "The secret dwarves society.";
			this.rewardXP = 300;
			this.rewardGold = 50;
			this.objectives.add(new Objective(10));
		}
		else if(this.id == 5)
		{
			this.name = Main.lv("Helena's Task", "Tarefa da Helena");
			this.description = "Helena asked for you help.";
			this.npcQuesterId = 17;
		}
		else if(this.id == 6)
		{
			this.name = Main.lv("Guard's Request", "O Pedido do Guarda");
			this.description = "The Stor Gave east guard can not go out during his office hour, help him getting his iron for his"
					+ "weapon improvement.";
			this.npcQuesterId = 20;
			this.objectives.add(Quest.generateCollectObjective(78, 10));
			this.rewardXP = 200;
			this.rewardGold = 300;
		}
		else if(this.id == 7)
		{
			this.name = Main.lv("The Iron Man", "O Homem de Ferro");
			this.rewardXP = 500;
			this.rewardGold = 500;
			this.objectives.add(Quest.generateCollectObjective(78, 35));
			this.npcQuesterId = 29;
		}
		else if(this.id == 8)
		{
			this.name = Main.lv("Dill's Task", "Tarefa do Dill");
			this.description = "Dill asked for you help.";
			this.npcQuesterId = 32;
		}
		else if(this.id == 9)
		{
			this.name = Main.lv("The Shiny White", "O Branco Brilhante");
			this.rewardXP = 5000;
			this.rewardGold = 5000;
			this.objectives.add(Quest.generateCollectObjective(120, 50));
			this.objectives.add(Quest.generateCollectObjective(82, 35));
			this.npcQuesterId = 15;
		}
		else if(this.id == 10)
		{
			this.name = Main.lv("Ghost Devolution", "Devolução Fantasma");
			this.rewardXP = 10000;
			this.rewardGold = 10000;
			this.objectives.add(Quest.generateCollectObjective(168, 300));
			this.npcQuesterId = 33;
		}
		else if(this.id == 11)
		{
			this.name = Main.lv("Ghost Help: Adventurer", "Ajuda Fantasma: Aventureiro");
			this.description = Main.lv("Help the Lost Adventurer go back to his plane.", "Ajude o Aventureiro Perdido a voltar para o seu plano.");
			this.rewardGold = 500;
			this.rewardXP = 500;
			this.objectives.add(Quest.generateCollectObjective(174, 2));
			this.npcQuesterId = 38;
		}
		else if(this.id == 12)
		{
			this.name = Main.lv("Ghost Help: Knight", "Ajuda Fantasma: Guerreira");
			this.description = Main.lv("Help the Lost Knight go back to his plane.", "Ajude a Guerreira Perdida a voltar para o seu plano.");
			this.rewardGold = 500;
			this.rewardXP = 500;
			this.objectives.add(Quest.generateCollectObjective(174, 2));
			this.npcQuesterId = 39;
		}
		return this;
	}

	public void reset()
	{
		this.name = "";
		this.description = "";
		this.objectives.clear();
		this.rewardGold = 0;
		this.rewardXP = 0;
		this.step = 1;
	}

	public static Objective generateKillObjective(int monsterid, int howmany)
	{
		int[] infos = new int[]{monsterid, howmany, 0};
		return new Objective(1, infos);
	}

	public static Objective generateCollectObjective(int itemid, int howmany)
	{
		int[] infos = new int[]{itemid, howmany, 0};
		return new Objective(2, infos);
	}

	public Objective getObjective(int oid) {
		Objective have = null;
		for (Objective o : this.objectives) {
			if (o.id == oid) {
				have = o;
				break;
			}
		}

		return have;
	}
	
	public void onComplete(Player player)
	{
		Main.lastQuestText = Main.lv("You completed the \""+this.name+"\" quest.", "Você completou a missão \""+this.name+"\".");
		Main.lastQuestTicks = 0;
		if(this.id == 7)
		{
			player.learnedRecipes.add(Recipe._IRONBOW);
			player.learnedRecipes.add(Recipe._IRONBREASTPLATE);
			player.learnedRecipes.add(Recipe._IRONBROADSWORD);
			player.learnedRecipes.add(Recipe._IRONDAGGER);
			player.learnedRecipes.add(Recipe._IRONHELMET);
			player.learnedRecipes.add(Recipe._IRONSHIELD);
			player.learnedRecipes.add(Recipe._IRONSHORTSWORD);
			player.updateRecipes();
		}
		else if(this.id == 9)
		{
			player.learnedRecipes.add(Recipe._SILVERBROADSWORD);
			player.learnedRecipes.add(Recipe._SILVERSHORTSWORD);
			player.learnedRecipes.add(Recipe._SILVERHELMET);
			player.learnedRecipes.add(Recipe._SILVERPLATE);
			player.learnedRecipes.add(Recipe._GOLDENBREASTPLATE);
			player.learnedRecipes.add(Recipe._GOLDENHELMET);
			player.learnedRecipes.add(Recipe._GOLDENBROADSWORD);
			player.learnedRecipes.add(Recipe._GOLDENSHIELD);
			player.learnedRecipes.add(Recipe._GOLDENSHORTSWORD);
			player.updateRecipes();
		}
		else if(this.id == 10)
		{
			Main.lastQuestText = Main.lv("You unlocked the [EPIC]Eternity Island[].", "Você desbloqueou a [EPIC]Ilha da Eternidade[].");
			Main.lastQuestTicks = 0;
			// desbloquear warp
		}
	}
	
	public boolean isConcluded()
	{
		for(Objective o : this.objectives)
		{
			if(!o.concluded)
			{
				return false;
			}
		}
		return true;
	}
}