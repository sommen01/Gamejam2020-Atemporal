package com.roguelike.game;

import java.io.File;
import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.ArkaClass;
import com.roguelike.entities.Player;
import com.roguelike.world.loader.PlayerLoader;

public class CreateCharScreen implements Screen, InputProcessor
{
	public Game game;
	public static SpriteBatch batch;
	public static boolean mouseJustPressed = false;
	public static boolean holdingMouse = false;
	
	public static Player player;
	public static int currentClass = 1;
	public static boolean writingName = false;
	
	public static ArrayList<String> blockedNames = new ArrayList<String>();
	
	public CreateCharScreen(Game game)
	{
		this.game = game;
	}

	@Override
	public void show()
	{
		batch = new SpriteBatch();
		player = new Player();
		player.classType = ArkaClass.KNIGHT;
		player.name = "Hero";
		currentClass = 1;
		
		Gdx.input.setInputProcessor(this);
		
		blockedNames.clear();

		File directory = Gdx.files.local("saves/").file();
		File[] files = directory.listFiles();
		for(File f : files)
		{
			if(f.isDirectory())
			{
				blockedNames.add(f.getName());
			}
		}
		holdingMouse = true;
		mouseJustPressed = false;
	}

	@Override
	public void render(float delta) {
		
		mouseJustPressed = false;
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (!holdingMouse) {
				mouseJustPressed = true;
				holdingMouse = true;
			}
		} else {
			holdingMouse = false;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.BACKSPACE))
		{
			if(writingName)
			{
				try
				{
					player.name = player.name.substring(0, player.name.length()-1);
				}catch(Exception ex) {}
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
		{
			game.setScreen(new CharScreen(game));
		}
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		Sprite bg = new Sprite(Content.menuBg[Roguelike.ticks/15%3]);
		float scaleX = Gdx.graphics.getWidth()/bg.getWidth();
		float scaleY = Gdx.graphics.getHeight()/bg.getHeight();
		bg.setScale(Math.max(scaleX, scaleY));
		bg.setPosition(Gdx.graphics.getWidth()/2f-bg.getWidth()/2f, Gdx.graphics.getHeight()/2f-bg.getHeight()/2f);
		bg.setColor(Color.WHITE.cpy().mul(0.5f, 0.5f, 0.5f, 1f));
		bg.draw(batch);

		for(int i = 0;i < InitialScreen.birdPos.length;i++)
		{
			InitialScreen.birdPos[i].x -= 200*delta;
			if(InitialScreen.birdPos[i].x < -100)
			{
				InitialScreen.birdPos[i] = new Vector2(Gdx.graphics.getWidth()+300+MathUtils.random(-80,200),Gdx.graphics.getHeight()-35-i*30+MathUtils.random(-10, 10));
			}
		}

		for(int j = 0;j < InitialScreen.birdPos.length;j++)
		{
			Sprite sprite = new Sprite(Content.birdBg[((Roguelike.ticks/5)+j)%5]);
			sprite.setPosition(InitialScreen.birdPos[j].x, InitialScreen.birdPos[j].y);
			sprite.setScale(3f);
			sprite.draw(batch);
		}
		Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		player.position = new Vector2(Gdx.graphics.getWidth()/2f-player.width/2f, Gdx.graphics.getHeight()/2f-player.height/2f);
		player.scale = 2f;
		float[] array = player.getPCShaderArray();
		Content.pcS.setUniform3fv("color", array, 0, 3);
		Content.empowerBladeS.setUniform3fv("color", array, 0, 3);
		Content.stellarAttackS.setUniform3fv("color", array, 0, 3);
		Main.DrawPlayer(batch, player, false);
		
		Main.drawGenericBox(batch, Gdx.graphics.getWidth()/2f-250, -16f, 500, 216f);

		Main.prettyFontDraw(batch, Main.lv("This is the tale of...", "Esta é a história de..."), Gdx.graphics.getWidth()/2f, 270, 0.5f, 1f);
		Rectangle r = new Rectangle(Gdx.graphics.getWidth()/2f-250, 225, 500, 20);
		if(mouseJustPressed)
			writingName = false;
		
		if(r.contains(mouse))
		{
			Main.prettyFontDraw(batch, player.name + (writingName ? (Roguelike.ticks % 120 > 60 ? "|" : " ") : ""), Gdx.graphics.getWidth()/2f, 245, 1f, 
					Color.GREEN, Color.BLACK, 1f, true, -1);
			if(mouseJustPressed)
				writingName = true;
		}
		else
		{
			Main.prettyFontDraw(batch, player.name + (writingName ? (Roguelike.ticks % 120 > 60 ? "|" : " ") : ""), Gdx.graphics.getWidth()/2f, 245, 1f);
		}
		
		Main.prettyFontDraw(batch, Main.lv("Class: ", "Classe: ") + player.classType.name, Gdx.graphics.getWidth()/2f-230, 184, 1f, false);
		r = new Rectangle(Gdx.graphics.getWidth()/2f+174, 152, 30, 30);
		if(!r.contains(mouse))
		{
			Main.drawGenericBox(batch, r.x, r.y, r.width, r.height);
			Main.prettyFontDraw(batch, "<", Gdx.graphics.getWidth()/2f+190, 182, 1f);
		}
		else
		{
			Main.drawBoxedSprite(batch, Content.genericBox2, r.x, r.y, r.width, r.height);
			Main.prettyFontDraw(batch, "<", Gdx.graphics.getWidth()/2f+190, 182, 1f, 
					Color.GREEN, Color.BLACK, 1f, true, -1);
			if(mouseJustPressed)
			{
				int min = 1;
				currentClass--;
				if(currentClass < min)
					currentClass = ArkaClass.values().length-1;
				
				player.classType = ArkaClass.values()[currentClass];
			}
		}
		
		r = new Rectangle(Gdx.graphics.getWidth()/2f+209, 152, 30, 30);
		if(!r.contains(mouse))
		{
			Main.drawGenericBox(batch, r.x, r.y, r.width, r.height);
			Main.prettyFontDraw(batch, ">", Gdx.graphics.getWidth()/2f+227, 182, 1f);
		}
		else
		{
			Main.drawBoxedSprite(batch, Content.genericBox2, r.x, r.y, r.width, r.height);
			Main.prettyFontDraw(batch, ">", Gdx.graphics.getWidth()/2f+227, 182, 1f, 
					Color.GREEN, Color.BLACK, 1f, true, -1);
			if(mouseJustPressed)
			{
				int max = ArkaClass.values().length-1;
				currentClass++;
				if(currentClass > max)
					currentClass = 1;
				
				player.changeClass(ArkaClass.values()[currentClass]);
			}
		}

		if(canCreate() == 0)
		{
			Main.prettyFontDraw(batch, Main.lv("This name is already in use", "Este nome já está em uso"), Gdx.graphics.getWidth()/2f, 72, 0.5f, 
					Color.RED, Color.BLACK, 1f, true, -1);
		}
		else if(canCreate() == 1)
		{
			Main.prettyFontDraw(batch, Main.lv("This name is a keyword", "Este nome é uma palavra-chave"), Gdx.graphics.getWidth()/2f, 72, 0.5f, 
					Color.RED, Color.BLACK, 1f, true, -1);
		}
		r = new Rectangle(Gdx.graphics.getWidth()/2f-50-8, 10-8, 100+16, 32+16);
		if(canCreate() != -1)
		{
			Main.drawBoxedSprite(batch, Content.genericBox3, r.x, r.y, r.width, r.height);
			Main.prettyFontDraw(batch, Main.lv("Create", "Criar"), Gdx.graphics.getWidth()/2f, 38, 1f);
		}
		else if(!r.contains(mouse))
		{
			Main.drawGenericBox(batch, r.x, r.y, r.width, r.height);
			Main.prettyFontDraw(batch, Main.lv("Create", "Criar"), Gdx.graphics.getWidth()/2f, 38, 1f);
		}
		else
		{
			Main.drawBoxedSprite(batch, Content.genericBox2, r.x, r.y, r.width, r.height);
			Main.prettyFontDraw(batch, Main.lv("Create", "Criar"), Gdx.graphics.getWidth()/2f, 38, 1f,
					Color.GREEN, Color.BLACK, 1f, true, -1);
			
			if(mouseJustPressed)
			{
				player.scale = 1f;
				PlayerLoader.generateNewPlayerInfos(player);
				PlayerLoader.savePlayer(player);
				game.setScreen(new CharScreen(game));
			}
		}
		Sprite sprite = new Sprite(Content.cursor);
		sprite.setPosition((Gdx.input.getX()) - 8, (Gdx.graphics.getHeight() - Gdx.input.getY()) - 8);
		sprite.setRotation((Roguelike.ticks * 10) % 360);
		sprite.draw(batch);
		batch.end();
	}
	
	public static int canCreate()
	{
		if(blockedNames.contains(player.name))
			return 0;
		
		if(player.name.equalsIgnoreCase("base"))
			return 1;
		
		return -1;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		if(writingName && player.name.length() < 20 && (Character.isAlphabetic(character) || Character.isDigit(character)))
		{
			player.name += character;
		}
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
		// TODO Auto-generated method stub
		return false;
	}

}
