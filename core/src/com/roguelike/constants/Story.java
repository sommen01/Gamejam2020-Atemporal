package com.roguelike.constants;

import com.badlogic.gdx.graphics.Texture;
import com.roguelike.game.Content;

public enum Story {
	NOTHING, LAGEAS1, LAGEAS2, LAGEAS3, LAGEAS4, LAGEAS5, LAGEAS6, LAGEAS7, LAGEAS8;
	
	public Texture texture;
	public String file;
	Story()
	{
		String str = this.name().toLowerCase();
		this.file = str+".png";
	}
	
	public void loadFile()
	{
		this.texture = Content.lt("data/others/stories/"+this.file);
	}
	
	public void disposeFile()
	{
		if(this.texture != null)
			this.texture.dispose();
	}
}
