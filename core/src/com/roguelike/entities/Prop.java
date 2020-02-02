package com.roguelike.entities;

import java.util.regex.Pattern;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Dialogs;
import com.roguelike.game.Content;
import com.roguelike.game.Event;
import com.roguelike.game.Main;
import com.roguelike.game.SaveInfos;

public class Prop extends Entity {

	public int id;
	public boolean usable = false;
	public int[] infos = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	public boolean checked = false;
	public String genTag = "";
	
	public Prop()
	{
		super.active = true;
		this.checked = false;
	}
	
	public Prop(Vector2 position, int width, int height, int type, int[] infos, boolean usable)
	{
		super.position = position;
		super.width = width;
		super.height = height;
		this.id = type;
		this.infos = infos;
		this.usable = usable;
		super.active = true;
		this.checked = false;
	}
	
	@Override
	public void update(float delta)
	{
		if(!this.checked)
		{
			if(this.infos.length < 10)
			{
				int[] array = new int[10];
				for(int i = 0;i < 10;i++)
				{
					if(i < this.infos.length)
						array[i] = this.infos[i];
					else
						array[i] = 0;
				}
				this.infos = array;
			}
			this.checked = true;
		}
		for(NPC n : Constant.getNPCList())
		{
			if(n.hitBox().overlaps(this.hitBox()))
			{
				OnEntityCollision(n);
			}
		}
		for(Monster m : Constant.getMonsterList(false))
		{
			if(m.hitBox().overlaps(this.hitBox()))
			{
				OnEntityCollision(m);
			}
		}
		for(Player p : Constant.getPlayerList())
		{
			if(p.hitBox().overlaps(this.hitBox()))
			{
				OnEntityCollision(p);
			}
		}
		
		if(this.usable)
		{
			boolean closest = true;
			for(NPC i : Constant.getNPCList())
			{
				if(i.Center().dst(Constant.getPlayerList()[Main.me].Center()) < this.Center().dst(Constant.getPlayerList()[Main.me].Center()) && i.useDialog != -1 && !i.sleeping)
				{
					closest = false;
					break;
				}
			}
			for(Prop i : Constant.getPropList())
			{
				if(!i.equals(this) && i.Center().dst(Constant.getPlayerList()[Main.me].Center()) < this.Center().dst(Constant.getPlayerList()[Main.me].Center()) && i.usable)
				{
					closest = false;
					break;
				}
			}
			if(Gdx.input.isKeyJustPressed(Keys.Z) && closest && this.Center().dst(Constant.getPlayerList()[Main.me].Center()) < 120 && !Main.displayDialog && !Main.readyForNextmap)
			{
				this.onUse(Main.player[Main.me]);
			}
		}
		
		if(this.id == 6)
		{
			if(this.infos[1] == 1)
			{
				this.infos[2]++;
				float initialY = this.position.y;
				this.position.y -= this.infos[2] * 8 * delta;
				if(Main.worldMap.doesRectCollideWithMap(this.position.x, this.position.y, this.width, this.height))
				{
					this.infos[1] = 0;
					this.infos[3] = 1;
					this.position.y = (float)Math.floor(this.position.y/64f)*64+64;
				}
				for(Prop p : Main.prop)
				{
					if(p.active && p.id == 4 && p.infos[0] == this.infos[0])
					{
						p.position.y += this.position.y-initialY;
					}
				}
				Rectangle hitBox = new Rectangle(this.position.x,this.position.y-4,this.width,this.height+6);
				if(Main.player[Main.me].hitBox().overlaps(hitBox) && Main.player[Main.me].velocity.y <= 0f)
				{
					Main.player[Main.me].position.y += this.position.y-initialY;
					Main.player[Main.me].standingOnPlatform = true;
					Main.player[Main.me].grounded = true;
				}
			}
			else
			{
				Rectangle hitBox = new Rectangle(this.position.x,this.position.y-4,this.width,this.height+6);
				if(this.infos[4] != 1 && this.infos[3] != 1 && Main.player[Main.me].hitBox().overlaps(hitBox) && Main.player[Main.me].position.y >= this.position.y+this.height && Main.player[Main.me].velocity.y <= 0f)
				{
					final Prop prop = this;
					Event e = new Event(15) {
						@Override
						public void function()
						{
							prop.infos[1] = 1;
						}
					};
					Main.scheduledTasks.add(e);
					this.infos[4] = 1;
				}
			}
		}

	}

	public void draw(SpriteBatch batch)
	{
		if(Main.debug)
		{
			String str = "[";
			for(int i = 0;i < this.infos.length;i++)
			{
				str += this.infos[i] + (i != 9 ?", " : "]");
			}
			Main.prettyFontDraw(batch, str, this.Center().x, this.position.y + this.height + 16, 0.5f, 1f, true);
		}
		if(this.id == 3)
		{
			int sid = 41;
			this.usable = true;
			for(Prop p : Main.prop)
			{
				if(p.active && p.id == 4)
				{
					sid = 40;
					this.usable = false;
					break;
				}
			}
			Sprite s = new Sprite(Content.extras[sid]);
			s.setPosition(this.Center().x-100, this.position.y);
			s.draw(batch);
		}
		else if(this.id == 4)
		{
			int sid = 42;
			Sprite s = new Sprite(Content.extras[sid]);
			float sin = (float)Math.sin(Constant.gameTick()*Math.PI/180f);
			s.setPosition(this.Center().x-10, this.position.y+sin*16);
			s.draw(batch);
		}
		else if(this.id == 5)
		{
			int sid = 43;
			Sprite s = new Sprite(Content.extras[sid]);
			s.setPosition(this.Center().x-s.getWidth()/2, this.Center().y-s.getHeight()/2);
			s.setRotation(Constant.gameTick());
			s.draw(batch);
		}
		else if(this.id == 6)
		{
			int sid = 44;
			if(this.width > 130)
				sid = 45;
			
			Sprite s = new Sprite(Content.extras[sid]);
			s.setPosition(this.position.x, this.position.y);
			s.draw(batch);
		}
		if(this.usable)
		{
			boolean closest = true;
			for(NPC i : Main.npc)
			{
				if(i.Center().dst(Main.player[Main.me].Center()) < this.Center().dst(Main.player[Main.me].Center()) && i.useDialog != -1)
				{
					closest = false;
					break;
				}
			}
			for(Prop i : Main.prop)
			{
				if(!i.equals(this) && i.Center().dst(Main.player[Main.me].Center()) < this.Center().dst(Main.player[Main.me].Center()) && i.usable)
				{
					closest = false;
					break;
				}
			}
			if(closest && this.Center().dst(Main.player[Main.me].Center()) < 120 && !Main.displayDialog && !Main.readyForNextmap)
			{
				Sprite pressx = new Sprite(Content.pressz);
				pressx.setPosition(this.position.x + this.width/2 - pressx.getWidth()/2, this.position.y + this.height + 8);
				Main.prettySpriteDraw(pressx, batch);
			}
		}
	}
	
	public void OnEntityCollision(Entity ent)
	{
		if(this.id == 4)
		{
			if(ent.getClass() == Player.class)
			{
				this.active = false;
			}
		}
	}
	
	public void onUse(Player player)
	{
		if(this.id == Constant.PROPID_DIALOG)
		{
			Dialogs dialog = new Dialogs().setInfos(this.infos[Constant.PROPDIALOG_DIALOGID]);
			if(!dialog.actionOnly && !Main.displayDialog && Main.cutscene == null)
			{
				Main.cutsceneBordersTicks = 0;
			}
			Main.displayDialog = true;
			dialog.text = dialog.text.replaceAll(Pattern.quote ("{PLAYER}"), Main.saveName);
			Main.dialog = dialog;
			Main.dialogTicks = 0;
			Main.dialogFullDrawn = false;
			if(Main.dialog.actionOnly)
				Main.displayDialog = false;
			
			Main.dialogEntity = this;
			
			if(Main.dialog.npcPreference > 0 && SaveInfos.findNPCWithID(Main.dialog.npcPreference) != null)
				Main.dialogEntity = SaveInfos.findNPCWithID(Main.dialog.npcPreference);
			else if(Main.dialog.npcPreference == -1)
				Main.dialogEntity = null;
			
			Main.dialog.onOpen(player);
		}
		else if((this.id == Constant.PROPID_TELEPORT || this.id == 3) && !Main.gotThisFrame)
		{
			if(player.myMapX != this.infos[Constant.PROPTELEPORT_GLOBALMAPX] || player.myMapY != this.infos[Constant.PROPTELEPORT_GLOBALMAPY])
			{
				player.updateCameMap();
				Main.SaveMap();
				player.myMapX = this.infos[Constant.PROPTELEPORT_GLOBALMAPX];
				player.myMapY = this.infos[Constant.PROPTELEPORT_GLOBALMAPY];
				Main.SwitchMap(this.infos[Constant.PROPTELEPORT_SPECIALGEN+5], new Vector2(this.infos[Constant.PROPTELEPORT_LOCALMAPX], this.infos[Constant.PROPTELEPORT_LOCALMAPY]), 
						new int[] {this.infos[Constant.PROPTELEPORT_SPECIALGEN], this.infos[Constant.PROPTELEPORT_SPECIALGEN+1], 
								this.infos[Constant.PROPTELEPORT_SPECIALGEN+2], this.infos[Constant.PROPTELEPORT_SPECIALGEN+3], 
								this.infos[Constant.PROPTELEPORT_SPECIALGEN+4]});
			}
			else
			{
				player.position = new Vector2(this.infos[Constant.PROPTELEPORT_LOCALMAPX], this.infos[Constant.PROPTELEPORT_LOCALMAPY]);
				Main.instaCamera = false;
				Main.gotThisFrame = true;
				System.out.println("Teleported to " + player.position.toString());
			}
			/*Vector2 freeSpace = Main.worldMap.getNextestFreeSpace(this.infos[Constant.PROPTELEPORT_LOCALMAPX], this.infos[Constant.PROPTELEPORT_LOCALMAPY], player.width, player.height, 1, 1).scl(64);
			player.position.x = freeSpace.x;
			player.position.y = freeSpace.y;*/
		}
		else if(this.id == 5)
		{
			for(Prop p : Main.prop)
			{
				if(p != this && p.active && p.id == 5 && this.infos[0] == p.infos[0] && player.cooldown <= 0f)
				{
					player.position.set(p.Center().sub(player.width/2f, player.height/2f));
					player.cooldown = 0.1f;
					/*if(Main.worldMap.doesRectCollideWithMap2(player.position.x, player.position.y, player.width, player.height))
					{
						player.position = Main.worldMap.getNextestFreeSpaceSafe(player.position.x, player.position.y, player.width, player.height, 1, 1).scl(64f);
					}*/
				}
			}
		}
	}

}
