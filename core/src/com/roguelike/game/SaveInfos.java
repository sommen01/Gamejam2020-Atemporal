package com.roguelike.game;

import com.roguelike.constants.Constant;
import com.roguelike.entities.Entity;
import com.roguelike.entities.NPC;

public class SaveInfos {
	public int bragusInPlace = 0;
	public int lewinFeeling = Constant.LEWINFEELING_EMPTYNOTHINGTOFEELLIFEISNOTSPECIAL;
	public boolean toldNameToLewin = false;
	public boolean chapter2helpedFarmer = false;
	public boolean chapter2helpedCultist = false;
	public boolean toldNameToFarmer = true;
	public boolean metBuckTheFarmer = false;
	public boolean metSGWestGuard = false;
	public boolean canConvertMagicEssence = false;
	public long myWorldTime = 0L;
	
	public static NPC findNPCWithID(int id)
	{
		NPC n = null;
		for(NPC npc : Constant.getNPCList())
		{
			if(npc.id == id)
			{
				n = npc;
				break;
			}
		}
		return n;
	}
	
	public static NPC findNPCWithID(int id, Entity reference)
	{
		NPC n = null;
		for(NPC npc : Constant.getNPCList())
		{
			if(npc.id == id && npc.sameMapAs(reference))
			{
				n = npc;
				break;
			}
		}
		return n;
	}
}