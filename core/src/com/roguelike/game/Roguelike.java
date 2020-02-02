package com.roguelike.game;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.online.AHS;
import com.roguelike.constants.Constant;
import com.roguelike.game.Main;

public class Roguelike extends Game
{
	public Game game;
	public static String execute = "";
	public static int language = 1;
	public static Cursor cursor;
	public static boolean loadingTextures;
	public static boolean oldLoadingTextures;
	public static int ticks = 0;
	public static boolean finishedLoading = false;
	public static boolean canFinishLoading = false;

	public static SpriteBatch batch;
	public static boolean focused;

	public Roguelike(String string)
	{
		Roguelike.execute = string;
		game = this;
	}

	public void create ()
	{
		Gdx.graphics.setTitle("Arka Hero (0 FPS) ");
		Pixmap pm = new Pixmap(Gdx.files.internal("data/cursor.png"));
		cursor = Gdx.graphics.newCursor(pm, 8, 8);
		Gdx.graphics.setCursor(cursor);
		Content.loadBasicContent();
		batch = new SpriteBatch();
		loadingTextures = true;
		oldLoadingTextures = true;

		Main.toolkit = Toolkit.getDefaultToolkit();
		Main.originalKeysState = new Boolean[] {Main.toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK), Main.toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK), Main.toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK)};
		Main.trackedKeysState = new Boolean[] {Main.toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK), Main.toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK), Main.toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK)};
		new Main();
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		DJ.update(Gdx.graphics.getDeltaTime());
		DJ.oldForcingMusic = DJ.forcingMusic;
		DJ.forcingMusic = false;
		ticks++;
		if(!finishedLoading)
		{
			if(loadingTextures)
			{
				if(ticks % 30 == 0)
					Main.setLockingKey(KeyEvent.VK_NUM_LOCK, Boolean.TRUE);
				else if(ticks % 30 == 5)
					Main.setLockingKey(KeyEvent.VK_CAPS_LOCK, Boolean.TRUE);
				else if(ticks % 30 == 10)
					Main.setLockingKey(KeyEvent.VK_SCROLL_LOCK, Boolean.TRUE);
				else if(ticks % 30 == 15)
					Main.setLockingKey(KeyEvent.VK_NUM_LOCK, Boolean.FALSE);
				else if(ticks % 30 == 20)
					Main.setLockingKey(KeyEvent.VK_CAPS_LOCK, Boolean.FALSE);
				else if(ticks % 30 == 25)
					Main.setLockingKey(KeyEvent.VK_SCROLL_LOCK, Boolean.FALSE);

				Matrix4 uiMatrix = new Matrix4();
				uiMatrix.setToOrtho2D(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				batch.setProjectionMatrix(uiMatrix);
				batch.begin();
				ticks++;
				float scale = 1f;
				Vector2 offset = Vector2.Zero.cpy();
				if(ticks % 120 < 45)
				{
					scale = (float) Math.sin((ticks % 120) * 2 * Math.PI/180f);
				}
				else if(ticks % 120 < 85)
				{
					offset = Main.vectorNoise(((ticks% 120)-45)*0.1f);
				}
				else if(ticks % 120 <= 90)
				{
					float progress = (ticks % 120) - 85;
					scale = (float) Math.pow(0.6, progress);
				}
				else
				{
					scale = 0f;
				}

				Sprite sprite;
				if(ticks % 120 >= 85 && ticks % 120 < 100)
				{
					int frame = (ticks % 120) - 85;
					float scale3 = 1f + ((ticks % 120 - 85)/30f);
					sprite = new Sprite(Content.loadingParticle);
					sprite.setScale(scale3);
					sprite.setPosition(Gdx.graphics.getWidth()/2f-(62f*scale3)+4, 16+Gdx.graphics.getHeight()/2f-(62f)+(62f*((scale3-1f)*2))+4);
					sprite.setRegion(0, 31 * frame, 31, 31);
					sprite.setSize(31*4, 31*4);
					if(frame > 10)
						sprite.setAlpha((float)(1f - Math.pow(13-frame, 0.8f)));

					sprite.draw(batch);
				}

				sprite = new Sprite(Content.loading);
				sprite.setPosition(Gdx.graphics.getWidth()/2f-sprite.getWidth()/2+offset.x, 16+offset.y+Gdx.graphics.getHeight()/2f-sprite.getHeight()/2);
				sprite.setScale(scale);
				sprite.draw(batch);

				String dots = "";
				for(int i = 0;i < Math.ceil((ticks%60)/20)+1;i++)
				{
					dots += ".";
				}
				Main.prettyFontDraw(batch, Main.loadingProgress + dots, Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f-80, 1f, true);
				batch.end();
				if(loadingTextures)
				{
					for(int i = 0;i < Settings.loadOverclock;i++)
					{
						loadingTextures = !Content.loadContent();
						if(!loadingTextures)
						{
							Content.loadShaders();
							finishLoading();
							break;
						}
					}
				}
			}
			if(canFinishLoading)
			{
				canFinishLoading = false;
				finishLoading2();
			}
		}
		Gdx.graphics.setTitle("Arka Hero (" + Gdx.graphics.getFramesPerSecond() + " FPS) ");
	}

	public void finishLoading()
	{
		new Thread()
		{
			public void run()
			{
				Main.loadingProgress = Main.lv("Loading cool sound effects", "Carregando efeitos sonoros legais");
				DJ.loadFiles();
				DJ.switchMusic(DJ._FOREST);
				canFinishLoading = true;
			}
		}.start();
	}
	
	public void finishLoading2()
	{
		finishedLoading = true;
		String[] executeBroken = execute.split(" ");
		if(executeBroken[0].equalsIgnoreCase("game"))
		{
			if(executeBroken.length < 2)
				game.setScreen(new InitialScreen(game));
			else
				game.setScreen(new Main(game, executeBroken[1]));
		}
	}

	@Override
	public void dispose()
	{
		if(this.screen != null) this.screen.hide();
		batch.dispose();
		if(Main.font != null) Main.font.dispose();
		try
		{
			Content.disposeContent();
		} catch(Exception ex) {ex.printStackTrace();}
		DJ.disposeFiles();
	}

}