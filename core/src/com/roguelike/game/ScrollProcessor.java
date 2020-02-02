package com.roguelike.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;
import com.roguelike.constants.Recipe;

public class ScrollProcessor implements InputProcessor {

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if(Main.displaySkill)
		{
			Main.displaySkillOffset += amount;
			if(Main.displaySkillOffset + 8 > Main.player[Main.me].learnedSkills.size())
				Main.displaySkillOffset--;
			
			Main.displaySkillOffset = Math.max(0, Main.displaySkillOffset);
		}
		if(Main.displayCrafting)
		{
			ArrayList<Recipe> fullList = new ArrayList<Recipe>();
			for(Recipe r : Recipe.values())
			{
				if(r.craftStation == Main.displayCraftStation)
				{
					fullList.add(r);
				}
			}
			Main.displayCraftingOffset += amount;
			if(Main.displayCraftingOffset + 8 > fullList.size())
				Main.displayCraftingOffset--;
			
			Main.displayCraftingOffset = Math.max(0, Main.displayCraftingOffset);
		}
		return false;
	}

}
