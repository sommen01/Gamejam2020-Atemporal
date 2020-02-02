package com.roguelike.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.Buff;
import com.roguelike.entities.Player;
import com.roguelike.world.loader.PlayerLoader;

public class CharScreen implements Screen
{
	public Game game;
	public static SpriteBatch batch;
	public static ArrayList<Player> playerList = new ArrayList<Player>();
	public static boolean mouseJustPressed = false;
	public static boolean holdingMouse = false;
	public static OrthographicCamera camera;
	
	public static int ticks = 0;
	
	public CharScreen(Game game)
	{
		this.game = game;
	}

	@Override
	public void show()
	{
		batch = new SpriteBatch();
		updatePlayerList();
		ticks = 0;
		holdingMouse = true;
		mouseJustPressed = false;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		
		if(InitialScreen.birdPos == null)
			InitialScreen.birdPos = new Vector2[5];
		
		for(int i = 0;i < InitialScreen.birdPos.length;i++)
		{
			if(InitialScreen.birdPos[i] == null)
				InitialScreen.birdPos[i] = new Vector2(Gdx.graphics.getWidth()+300+MathUtils.random(-80,200),Gdx.graphics.getHeight()-35-i*30+MathUtils.random(-10, 10));
		}
	}

	@Override
	public void render(float delta) {
		ticks++;
		mouseJustPressed = false;
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (!holdingMouse) {
				mouseJustPressed = true;
				holdingMouse = true;
			}
		} else {
			holdingMouse = false;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
		{
			game.setScreen(new InitialScreen(game));
		}
		for(int i = 0;i < InitialScreen.birdPos.length;i++)
		{
			InitialScreen.birdPos[i].x -= 200*delta;
			if(InitialScreen.birdPos[i].x < -100)
			{
				InitialScreen.birdPos[i] = new Vector2(Gdx.graphics.getWidth()+300+MathUtils.random(-80,200),Gdx.graphics.getHeight()-35-i*30+MathUtils.random(-10, 10));
			}
		}
		boolean shouldUpdateList = false;
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		int i = 1;
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.setProjectionMatrix(uiMatrix);
		batch.begin();
		Sprite bg = new Sprite(Content.menuBg[Roguelike.ticks/15%3]);
		float scaleX = Gdx.graphics.getWidth()/bg.getWidth();
		float scaleY = Gdx.graphics.getHeight()/bg.getHeight();
		bg.setScale(Math.max(scaleX, scaleY));
		bg.setPosition(Gdx.graphics.getWidth()/2f-bg.getWidth()/2f, Gdx.graphics.getHeight()/2f-bg.getHeight()/2f);
		bg.draw(batch);
		
		for(int j = 0;j < InitialScreen.birdPos.length;j++)
		{
			Sprite sprite = new Sprite(Content.birdBg[((Roguelike.ticks/5)+j)%5]);
			sprite.setPosition(InitialScreen.birdPos[j].x, InitialScreen.birdPos[j].y);
			sprite.setScale(3f);
			sprite.draw(batch);
		}
		
		float offset = (float) (Math.pow(0.9f, ticks));
		
		float yOffset = (playerList.size()+1)*110/2f;
		Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		float listConstant = offset*300;
		for(Iterator<Player> it = playerList.iterator();it.hasNext();)
		{
			Player p = it.next();
			Rectangle hitBox = new Rectangle(Gdx.graphics.getWidth()/2f-200, Gdx.graphics.getHeight()/2f+yOffset-i*110-listConstant, 400, 100);
			if(!hitBox.contains(mouse))
			{
				Main.drawGenericBox(batch, Gdx.graphics.getWidth()/2f-200, Gdx.graphics.getHeight()/2f+yOffset-i*110-listConstant, 400, 100);
			}
			else
			{
				Main.drawBoxedSprite(batch, Content.genericBox2, Gdx.graphics.getWidth()/2f-200, Gdx.graphics.getHeight()/2f+yOffset-i*110-listConstant, 400, 100);
				if(mouseJustPressed)
				{
					game.setScreen(new Main(game, p.name));
					Main.playing = true;
				}
			}
			p.position = new Vector2(Gdx.graphics.getWidth()/2f-184, Gdx.graphics.getHeight()/2f+yOffset-i*110+16-listConstant);
			p.timeSinceLastAttack = 9999;
			float[] array = p.getPCShaderArray();
			Content.pcS.setUniform3fv("color", array, 0, 3);
			Content.empowerBladeS.setUniform3fv("color", array, 0, 3);
			Content.stellarAttackS.setUniform3fv("color", array, 0, 3);
			Main.DrawPlayer(batch, p, false);
			Main.prettyFontDraw(batch, p.name, Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f+yOffset-i*110+66-listConstant, 1f, true);
			hitBox = new Rectangle(Gdx.graphics.getWidth()/2f+196, Gdx.graphics.getHeight()/2f+yOffset-i*110+25-listConstant, 50, 50);
			boolean shouldDelete = false;
			if(!hitBox.contains(mouse))
			{
				Main.drawGenericBox(batch, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
				Main.prettyFontDraw(batch, "X", hitBox.x+16, hitBox.y + 40, 1f, false);
			}
			else
			{
				Main.drawBoxedSprite(batch, Content.genericBox2, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
				Main.prettyFontDraw(batch, "X", hitBox.x+16, hitBox.y + 40, 1f,
						Color.RED, Color.BLACK, 1f, false, -1);
				if(mouseJustPressed)
				{
					File directory = Gdx.files.local("saves/"+p.name+"/").file();

					File[] files = directory.listFiles();
					for(File f : files)
					{
						f.delete();
					}
					directory.delete();

					directory = Gdx.files.local("maps/"+p.name+"/").file();
					if(directory.exists())
					{
						files = directory.listFiles();
						if(files != null && files.length > 0)
						{
							for(File f : files)
							{
								f.delete();
							}
						}
						directory.delete();
					}

					shouldDelete = true;
					shouldUpdateList = true;
				}
			}
			
			if(shouldDelete) it.remove();
			i++;
		}
		
		Rectangle hitBox = new Rectangle(Gdx.graphics.getWidth()/2f-200, Gdx.graphics.getHeight()/2f+yOffset-i*110-listConstant, 400, 100);
		if(!hitBox.contains(mouse))
		{
			Main.drawGenericBox(batch, Gdx.graphics.getWidth()/2f-200, Gdx.graphics.getHeight()/2f+yOffset-i*110-listConstant, 400, 100);
		}
		else
		{
			Main.drawBoxedSprite(batch, Content.genericBox2, Gdx.graphics.getWidth()/2f-200, Gdx.graphics.getHeight()/2f+yOffset-i*110-listConstant, 400, 100);
			if(mouseJustPressed)
			{
				game.setScreen(new CreateCharScreen(game));
			}
		}
		Main.prettyFontDraw(batch, Main.lv("Create new character...", "Criar novo personagem..."), Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f+yOffset-i*110+66-listConstant, 1f, true);
		
		Sprite sprite = new Sprite(Content.cursor);
		sprite.setPosition((Gdx.input.getX()) - 8, (Gdx.graphics.getHeight() - Gdx.input.getY()) - 8);
		sprite.setRotation((Roguelike.ticks * 10) % 360);
		sprite.draw(batch);
		batch.end();
		
		if(shouldUpdateList)
			updatePlayerList();
	}
	
	public void updatePlayerList()
	{
		File directory = Gdx.files.local("saves/").file();
		
		playerList.clear();
		File[] files = directory.listFiles();
		for(File f : files)
		{
			if(f.isDirectory())
			{
				Player p = PlayerLoader.loadPlayer(f.getName());
				p.buffs = new Buff[30];
				playerList.add(p);
			}
		}
	}

	@Override
	public void resize(int width, int height)
	{
		camera.setToOrtho(false, width, height);
		camera.update();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
