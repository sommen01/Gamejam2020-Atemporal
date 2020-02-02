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
import com.roguelike.entities.Player;
import com.roguelike.world.loader.PlayerLoader;

public class InitialScreen implements Screen
{
	public Game game;
	public static SpriteBatch batch;
	public static boolean mouseJustPressed = false;
	public static boolean holdingMouse = false;
	
	public static Vector2[] birdPos = new Vector2[5];
	
	public static int ticks = 0;
	public static OrthographicCamera camera;
	
	public InitialScreen(Game game)
	{
		this.game = game;
	}

	@Override
	public void show()
	{
		batch = new SpriteBatch();
		for(int i = 0;i < birdPos.length;i++)
		{
			if(birdPos[i] == null)
				birdPos[i] = new Vector2(Gdx.graphics.getWidth()+300+MathUtils.random(-80,200),Gdx.graphics.getHeight()-35-i*30+MathUtils.random(-10, 10));
		}
		ticks = 0;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
	}

	@Override
	public void render(float delta) {
		ticks++;
		mouseJustPressed = false;
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
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
			Gdx.app.exit();
		}
		else if(Gdx.input.isKeyJustPressed(Keys.ANY_KEY))
		{
			game.setScreen(new CharScreen(game));
		}
		
		for(int i = 0;i < birdPos.length;i++)
		{
			birdPos[i].x -= 200*delta;
			if(birdPos[i].x < -100)
			{
				birdPos[i] = new Vector2(Gdx.graphics.getWidth()+300+MathUtils.random(-80,200),Gdx.graphics.getHeight()-35-i*30+MathUtils.random(-10, 10));
			}
		}
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
		
		for(int j = 0;j < birdPos.length;j++)
		{
			Sprite sprite = new Sprite(Content.birdBg[((Roguelike.ticks/5)+j)%5]);
			sprite.setPosition(birdPos[j].x, birdPos[j].y);
			sprite.setScale(3f);
			sprite.draw(batch);
		}
		
		bg = new Sprite(Content.arkaHero);
		float offset = (float) (Math.pow(0.9f, ticks));
		bg.setPosition(Gdx.graphics.getWidth()/2f-bg.getWidth()/2f, Gdx.graphics.getHeight() - bg.getHeight()+20 + offset*bg.getHeight()*1.5f);
		bg.draw(batch);
		
		if(ticks > 60)
		{
			Main.prettyFontDraw(batch, Main.lv("Press any key to start...", "Pressione qualquer tecla para começar..."), Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/3f, Math.min((ticks-60f)/60f, 1f));
		}
		
		/*Sprite sprite = new Sprite(Content.cursor);
		sprite.setPosition((Gdx.input.getX()) - 8, (Gdx.graphics.getHeight() - Gdx.input.getY()) - 8);
		sprite.setRotation((Roguelike.ticks * 10) % 360);
		sprite.draw(batch);*/
		batch.end();
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
