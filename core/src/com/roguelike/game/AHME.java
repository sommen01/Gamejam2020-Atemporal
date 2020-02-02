package com.roguelike.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.roguelike.constants.Item;
import com.roguelike.entities.Entity;
import com.roguelike.entities.Monster;
import com.roguelike.entities.NPC;
import com.roguelike.entities.Prop;
import com.roguelike.entities.Treasure;
import com.roguelike.world.Background;
import com.roguelike.world.GameMap;
import com.roguelike.world.Respawn;
import com.roguelike.world.Tile;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;

public class AHME extends ApplicationAdapter implements InputProcessor
{
	public static int mapheight = 100; // its width tho
	public static int mapwidth = 80; // its height tho

	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	SpriteBatch hudBatch;
	ShapeRenderer hudShapeRenderer;
	int TILECOUNT = 0;
	int BGCOUNT = 0;
	int MONSTERCOUNT = 0;
	int NPCCOUNT = 0;
	Texture[] tiles = new Texture[1000];
	boolean[] tileMapped = new boolean[1000];
	Texture[] backgrounds = new Texture[1000];
	Texture[] monsters = new Texture[100];
	Texture[] npcs = new Texture[100];
	public static OrthographicCamera camera;

	public static final float TILESIZE = 64;
	public float cameraSpeed = 5000;
	public float zoom = 1f;

	public Tile[][] map = new Tile[mapwidth][mapheight];
	public Tile[][] fg = new Tile[mapwidth][mapheight];
	public Background[][][] bg = new Background[mapwidth][mapheight][3];

	public int myTile = 0;
	public int myBg = 0;

	public boolean displayTileMenu = false;
	public boolean displayBgMenu = false;
	public boolean deleteMode = false;
	public boolean deleteModeDel = false;
	public boolean deleteModeShift = false;
	public boolean entityMode = false;

	public int selectedX = 0;
	public int selectedY = 0;
	public boolean holdingMouse = false;
	public boolean oldMouse = false;
	public boolean holdingMouse2 = false;
	public boolean oldMouse2 = false;
	private static Json json = new Json();

	public int myLayer = 0;

	public static ArrayList<Monster> monster = new ArrayList<Monster>();
	public static ArrayList<Treasure> treasure = new ArrayList<Treasure>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	public static ArrayList<NPC> npc = new ArrayList<NPC>();
	public static ArrayList<Prop> prop = new ArrayList<Prop>();
	public static ArrayList<Respawn> respawns = new ArrayList<Respawn>();

	public static String mapName = "Unnamed";
	public static int mapId = 666999;
	public static String mapFileName = "base.map";
	public static Color bgColor = null;
	public static DJ bgm;

	public boolean debugTiles = false;

	public boolean startingNewMap = false;
	public int newMapWidth = 0;
	public int newMapHeight = 0;
	public static int currentMonster = 1;
	public static int currentRespCd = 0;

	BitmapFont font; 
	private boolean foregrounding;
	public static float baseLight = 0.15f;
	public static int offset = 0;
	
	public static Vector2 lastClickPos = Vector2.Zero.cpy();
	public static Long lastClickTime = 0L;
	public static Entity lastClickEntity;
	public static boolean entityMove = false;
	public static Vector2 lastFrameMouseMove = Vector2.Zero.cpy();
	public static Vector2 lastMousePos = Vector2.Zero.cpy();
	
	public static Vector2 initialPos = new Vector2(0f, 0f);
	
	public static boolean mouseJustPressed = false;
	public static boolean holdingMouse1 = false;
	public static float maxLight = 1f;

	@Override
	public void create () {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PropFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PropFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PropFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PropFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
		json.setIgnoreUnknownFields(true);
		json.setOutputType(OutputType.minimal);
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		hudBatch = new SpriteBatch();
		hudShapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.setToOrtho(false);
		camera.update();
		loadFiles();

		for(int y = 0;y < map.length;y++)
		{
			for(int x = 0;x < map[0].length;x++)
			{
				map[y][x] = null;
				if(x == 0 || y == 0 || x == map[0].length-1 || y == map.length-1)
					map[y][x] = Tile.METALFLOOR;
				for(int z = 0;z < bg[0][0].length;z++)
					bg[y][x][z] = null;
			}
		}
		Gdx.input.setInputProcessor(this);
	}

	private void loadFiles()
	{
		for(int i = 0;i < 999;i++)
		{
			if(Content.loadContent())
				break;
		}
		Content.loadShaders();
		while(true)
		{
			File f = new File("data/Tile_" + TILECOUNT + ".png");
			if(f.exists() && !f.isDirectory())
			{
				tiles[TILECOUNT] = new Texture("data/Tile_" + TILECOUNT + ".png");
				if(tiles[TILECOUNT].getWidth() > TILESIZE)
				{
					tileMapped[TILECOUNT] = true;
				}
			}
			else
			{
				break;
			}
			TILECOUNT++;
		}
		while(true)
		{
			File f = new File("data/Background_" + BGCOUNT + ".png");
			if(f.exists() && !f.isDirectory())
			{
				backgrounds[BGCOUNT] = new Texture("data/Background_" + BGCOUNT + ".png");
			}
			else
			{
				break;
			}
			BGCOUNT++;
		}
		while(true)
		{
			File f = new File("data/Monster_" + MONSTERCOUNT + ".png");
			if(f.exists() && !f.isDirectory())
			{
				monsters[MONSTERCOUNT] = new Texture("data/Monster_" + MONSTERCOUNT + ".png");
			}
			else
			{
				break;
			}
			MONSTERCOUNT++;
		}
		while(true)
		{
			File f = new File("data/NPC_" + NPCCOUNT + ".png");
			if(f.exists() && !f.isDirectory())
			{
				npcs[NPCCOUNT] = new Texture("data/NPC_" + NPCCOUNT + ".png");
			}
			else
			{
				break;
			}
			NPCCOUNT++;
		}

	}

	@Override
	public void render () {
		
		if(startingNewMap)
		{
			startNewMap(newMapWidth, newMapHeight);
			startingNewMap = false;
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Vars
		camera.viewportHeight = Gdx.graphics.getHeight();
		camera.viewportWidth = Gdx.graphics.getWidth();
		camera.update();

		Vector2 mousePos = new Vector2((Gdx.input.getX()) * camera.zoom, (camera.viewportHeight - Gdx.input.getY()) * camera.zoom);
		mousePos.add(crp());
		
		lastFrameMouseMove = mousePos.cpy().sub(lastMousePos);
		
		// Handle
		float delta = Gdx.graphics.getDeltaTime();
		

		mouseJustPressed = false;
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (!holdingMouse1) {
				mouseJustPressed = true;
				checkClick(mouseInWorld(), System.currentTimeMillis());
				lastClickPos = mouseInWorld();
				lastClickTime = System.currentTimeMillis();
				holdingMouse1 = true;
			}
		} else {
			holdingMouse1 = false;
		}
		
		if(Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))
		{
			camera.position.y += cameraSpeed * delta;
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))
		{
			camera.position.y -= cameraSpeed * delta;
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A))
		{
			camera.position.x -= cameraSpeed * delta;
		}
		if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))
		{
			camera.position.x += cameraSpeed * delta;
		}
		if(Gdx.input.isKeyJustPressed(Keys.PLUS))
		{
			camera.zoom += 0.5f;
		}
		if(Gdx.input.isKeyJustPressed(Keys.MINUS))
		{
			camera.zoom -= 0.5f;
		}
		if(Gdx.input.isKeyJustPressed(Keys.PAGE_UP))
		{
			myLayer++;
			if(myLayer > bg[0][0].length-1)
				myLayer = bg[0][0].length-1;
		}
		if(Gdx.input.isKeyJustPressed(Keys.PAGE_DOWN))
		{
			myLayer--;
			if(myLayer < 0)
				myLayer = 0;
		}
		if(Gdx.input.isKeyJustPressed(Keys.F))
		{
			foregrounding = !foregrounding;
		}
		if(Gdx.input.isKeyJustPressed(Keys.Z))
		{
			displayTileMenu = !displayTileMenu;
		}
		if(Gdx.input.isKeyJustPressed(Keys.X))
		{
			displayBgMenu = !displayBgMenu;
		}
		if(Gdx.input.isKeyPressed(Keys.R))
		{
			for(Iterator<Monster> i = monster.iterator();i.hasNext();)
			{
				Monster m = i.next();
				if(m.hitBox().contains(mouseInWorld()))
				{
					i.remove();
				}
			}
			for(Iterator<NPC> i = npc.iterator();i.hasNext();)
			{
				NPC n = i.next();
				if(n.hitBox().contains(mouseInWorld()))
				{
					i.remove();
				}
			}
			for(Iterator<Respawn> i = respawns.iterator();i.hasNext();)
			{
				Respawn r = i.next();
				Monster m = new Monster();
				m.id = r.type;
				m.Reset(true);
				Rectangle rect = new Rectangle(r.position.x, r.position.y, m.width, m.height);
				if(rect.contains(mouseInWorld()))
				{
					i.remove();
				}
			}
			for(Iterator<Prop> i = prop.iterator();i.hasNext();)
			{
				Prop p = i.next();
				if(p.hitBox().contains(mouseInWorld()))
				{
					i.remove();
				}
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.M))
		{
			if(currentRespCd <= 0)
			{
				Monster m = new Monster();
				m.id = currentMonster;
				m.Reset(true);
				m.active = true;
				m.position.x = (float) (Math.floor(mouseInWorld().x/64)*64);
				m.position.y = (float) (Math.floor(mouseInWorld().y/64)*64);
				AHME.monster.add(m);
			}
			else
			{
				Respawn r = new Respawn();
				r.type = currentMonster;
				r.cooldown = currentRespCd;
				r.position.x = (float) (Math.floor(mouseInWorld().x/64)*64);
				r.position.y = (float) (Math.floor(mouseInWorld().y/64)*64);
				AHME.respawns.add(r);
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.FORWARD_DEL))
		{
			deleteModeDel = !deleteModeDel;
		}
		if(Gdx.input.isKeyJustPressed(Keys.END))
		{
			debugTiles = !debugTiles;
		}
		deleteModeShift = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);

		deleteMode = deleteModeDel || deleteModeShift;
		
		entityMode = Gdx.input.isKeyPressed(Keys.ALT_LEFT);

		if(!displayTileMenu && !displayBgMenu && !Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
		{
			if(!entityMode)
			{
				if(Gdx.input.isButtonPressed(Buttons.LEFT))
				{
					int x = (toMap(mousePos.x));
					int y = (toMap(mousePos.y));
					if(x >= 0 && x < map[0].length && y >= 0 && y < map.length)
					{
						if(!deleteMode)
							if(!foregrounding)
								map[y][x] = Tile.getTileTypeById(myTile+1);
							else
								fg[y][x] = Tile.getTileTypeById(myTile+1);
						else
							if(!foregrounding)
								map[y][x] = null;
							else
								fg[y][x] = null;
					}
				}
				if(Gdx.input.isButtonPressed(Buttons.RIGHT))
				{
					int x = (toMap(mousePos.x));
					int y = (toMap(mousePos.y));
					if(x >= 0 && x < map[0].length && y >= 0 && y < map.length)
					{
						if(!deleteMode)
						{
							bg[y][x][myLayer] = Background.getTileTypeById(myBg+1);
						}
						else
							bg[y][x][myLayer] = null;
					}
				}
			}
		}

		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.F))
		{
			Tile[][] oldmap = new Tile[mapwidth][mapheight];
			for(int y = 0;y < mapheight;y++)
			{
				for(int x = 0;x < mapwidth;x++)
				{
					oldmap[y][x] = map[y][x];
				}
			}

			for(int y = 0;y < mapheight;y++)
			{
				for(int x = 0;x < mapwidth;x++)
				{
					map[y][x] = oldmap[(mapheight-1)-y][x];
				}
			}
		}
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.M))
		{
			Tile[][] oldmap = new Tile[mapwidth][mapheight];
			for(int y = 0;y < mapheight;y++)
			{
				for(int x = 0;x < mapwidth;x++)
				{
					oldmap[y][x] = map[y][x];
				}
			}

			for(int y = 0;y < mapheight;y++)
			{
				for(int x = 0;x < mapwidth;x++)
				{
					map[y][x] = oldmap[y][(mapwidth-1)-x];
				}
			}
		}

		if(!entityMode)
		{
			boolean mouse = Gdx.input.isButtonPressed(Buttons.LEFT);
			if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
			{
				if(!oldMouse && mouse)
				{
					selectedX = toMap(mousePos.x);
					selectedY = toMap(mousePos.y);
				}
				if(oldMouse && !mouse)
				{
					applyTiles();
				}
				if(oldMouse && mouse)
				{
					holdingMouse = true;
				}
				if(!oldMouse && !mouse)
				{
					holdingMouse = false;
				}
			}
			else
			{
				holdingMouse = false;
			}


			boolean mouse2 = Gdx.input.isButtonPressed(Buttons.RIGHT);
			if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
			{
				if(!oldMouse2 && mouse2)
				{
					selectedX = toMap(mousePos.x);
					selectedY = toMap(mousePos.y);
				}
				if(oldMouse2 && !mouse2)
				{
					applyBg();
				}
				if(oldMouse2 && mouse2)
				{
					holdingMouse2 = true;
				}
				if(!oldMouse2 && !mouse2)
				{
					holdingMouse2 = false;
				}
			}
			else
			{
				holdingMouse2 = false;
			}
		}
		else
		{
			entityMove = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && Gdx.input.isButtonPressed(Buttons.LEFT);
			if(lastClickEntity != null)
			{
				if(entityMove)
				{
					lastClickEntity.position.add(lastFrameMouseMove);
				}
				boolean correctPos = Gdx.input.isButtonPressed(Buttons.RIGHT);
				if(correctPos)
				{
					if(!Gdx.input.isKeyPressed(Keys.Y))
						lastClickEntity.position.x = (float)Math.floor(lastClickEntity.position.x/64)*64;
					
					if(!Gdx.input.isKeyPressed(Keys.T))
					lastClickEntity.position.y = (float)Math.floor(lastClickEntity.position.y/64)*64;
				}
			}
		}

		if(Gdx.input.isKeyJustPressed(Keys.HOME))
		{
			TILECOUNT = 0;
			BGCOUNT = 0;
			loadFiles();
			Background.Load();
			Tile.Load();
		}

		// Draw
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudBatch.setProjectionMatrix(uiMatrix);
		hudShapeRenderer.setProjectionMatrix(uiMatrix);
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		if(bgColor == null)
			shapeRenderer.setColor(0.25f, 0.67f, 1f, 1f);
		else
			shapeRenderer.setColor(bgColor.r, bgColor.g, bgColor.b, 1f);
		shapeRenderer.rect(0, 0, mapheight * AHME.TILESIZE, mapwidth * AHME.TILESIZE);
		shapeRenderer.end();
		batch.begin();
		renderMap(batch);
		for(Monster m : monster)
		{
			batch.draw(monsters[m.id-1], m.position.x, m.position.y, 0, 0, m.width, m.height, m.scale, m.scale, 0f, 0, 0, m.width, m.height, false, false);
		}
		for(Respawn r : respawns)
		{
			Monster m = new Monster();
			m.id = r.type;
			m.Reset(true);
			batch.draw(monsters[r.type-1], r.position.x, r.position.y, 0, 0, m.width, m.height);
		}
		for(NPC n : npc)
		{
			Sprite spr = new Sprite(npcs[n.id-1]);
			spr.setPosition(n.position.x, n.position.y);
			spr.draw(batch);
		}
		batch.end();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		if(debugTiles)
		{
			shapeRenderer.begin(ShapeType.Filled);
			for(Monster m : monster)
			{
				shapeRenderer.setColor(1, 0, 0, 0.3f);
				if(lastClickEntity == m && entityMode && (entityMove || System.currentTimeMillis()-lastClickTime < 50L))
					shapeRenderer.setColor(1f, 0f, 1f, 1f);
				shapeRenderer.rect(m.position.x, m.position.y, m.width, m.height);
			}
			for(Respawn r : respawns)
			{
				shapeRenderer.setColor(1, 0, 1, 0.3f);
				Monster m = new Monster();
				m.id = r.type;
				m.Reset(true);
				shapeRenderer.rect(r.position.x, r.position.y, m.width, m.height);
			}
			for(NPC n : npc)
			{
				shapeRenderer.setColor(0, 0, 1, 0.3f);
				if(lastClickEntity == n && entityMode && (entityMove || System.currentTimeMillis()-lastClickTime < 50L))
					shapeRenderer.setColor(1f, 0f, 1f, 1f);
				shapeRenderer.rect(n.position.x, n.position.y, n.width, n.height);
			}
			shapeRenderer.setAutoShapeType(true);
			shapeRenderer.set(ShapeType.Line);
			for(Prop n : prop)
			{
				shapeRenderer.setColor(0, 1, 0, 1);
				if(lastClickEntity == n && entityMode && (entityMove || System.currentTimeMillis()-lastClickTime < 50L))
					shapeRenderer.setColor(1f, 0f, 1f, 1f);
				shapeRenderer.rect(n.position.x, n.position.y, n.width, n.height);
			}
			shapeRenderer.end();
		}
		Gdx.gl.glDisable(GL20.GL_BLEND);
		hudBatch.begin();
		font.draw(hudBatch, mouseInWorld().x + " (" + ((int)Math.floor(mouseInWorld().x/64f)*64) + ")\n" + mouseInWorld().y + " (" + ((int)Math.floor(mouseInWorld().y/64f)*64) + ")", 32, Gdx.graphics.getHeight() - 32);
		hudBatch.end();
		boolean cd = true;
		if(displayTileMenu)
		{
			displayTileMenu();
			cd = false;
		}
		if(displayBgMenu)
		{
			displayBgMenu();
			cd = false;
		}

		if(cd)
		{
			if(deleteMode)
				Gdx.gl.glLineWidth(2f);

			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Line);
			if(entityMode)
				shapeRenderer.setColor(new Color(1f, 0f, 1f, 1f));
			else if(deleteMode)
				shapeRenderer.setColor(Color.RED);
			else
				shapeRenderer.setColor(Color.WHITE);

			if(!entityMode)
			{
				if(!holdingMouse && !holdingMouse2)
					shapeRenderer.rect(toMap(mousePos.x) * TILESIZE, toMap(mousePos.y) * TILESIZE, TILESIZE, TILESIZE);
				else
				{
					float width = toMap(mousePos.x) - selectedX + 1;
					float height = toMap(mousePos.y) - selectedY + 1;

					shapeRenderer.rect(selectedX * TILESIZE, selectedY * TILESIZE, TILESIZE * width, height * TILESIZE);
				}
			}

			shapeRenderer.end();
		}

		hudBatch.begin();
		if(foregrounding)
		{
			font.draw(hudBatch, "* Foregrounding", 32, Gdx.graphics.getHeight()-256);
		}
		hudBatch.end();

		Gdx.graphics.setTitle("Arka Hero Map Editor (" + Gdx.graphics.getFramesPerSecond() + ") " + mapheight + "x" + mapwidth + " " + camera.zoom);
		oldMouse = Gdx.input.isButtonPressed(Buttons.LEFT);
		oldMouse2 = Gdx.input.isButtonPressed(Buttons.RIGHT);
		lastMousePos = mousePos.cpy();
	}

	private void checkClick(Vector2 pos, long time)
	{
		lastClickEntity = null;
		entityMove = false;
		for(Prop p : prop)
		{
			if(positionTouchesEntity(pos, p))
			{
				lastClickEntity = p;
				if(entityMode)
				{
					if(positionTouchesEntity(lastClickPos, p) && time-lastClickTime < 250L)
					{
						openPropFrame(prop.indexOf(p));
					}
				}
			}
		}
	}

	public void openPropFrame(int propId)
	{
		new PropFrame(propId).setVisible(true);
	}
	
	public boolean positionTouchesEntity(Vector2 pos, Entity e)
	{
		return e.hitBox().contains(pos);
	}

	public void LoadMap()
	{
		System.out.println("Entered LoadMap");
		Gdx.files.local("maps/" + mapFileName).file().mkdirs();
		System.out.println("Made directory if not found");
		FileHandle file = Gdx.files.local("maps/" + mapFileName);
		System.out.println("Handled file maps/"+mapFileName);
		GameMap data = json.fromJson(GameMap.class, file.readString());
		System.out.println("json Loaded");
		map = data.map;
		System.out.println("Loaded map");
		fg = data.fg;
		System.out.println("Loaded fg");
		bg = data.bg;
		System.out.println("Loaded bg");
		mapwidth = data.height;
		System.out.println("Loaded height");
		mapheight = data.width;
		System.out.println("Loaded width");
		monster = data.monsters;
		System.out.println("Loaded monster");
		npc = data.npcs;
		System.out.println("Loaded npc");
		prop = data.props;
		System.out.println("Loaded prop");
		mapId = data.id;
		System.out.println("Loaded mapid");
		mapName = data.name;
		System.out.println("Loaded mapname");
		zoom = data.zoom;
		System.out.println("Loaded zoom");
		respawns = data.respawns;
		System.out.println("Loaded respawns");
		bgColor = data.bgColor;
		System.out.println("Loaded bgColor");
		bgm = data.backgroundMusic;
		AHME.initialPos = data.initialPosition;
		System.out.println("Loaded bgm");
		if(fg == null)
		{
			fg = new Tile[map.length][map[0].length];
			System.out.println("No fg found, generating a blank one");
		}
	}

	public void SaveMap()
	{
		GameMap data = new GameMap();
		data.map = map;
		data.width = mapheight;
		data.height = mapwidth;
		data.name = mapName;
		data.fg = fg;
		data.id = mapId;
		data.bgColor = Color.WHITE;
		data.usesLight = true;
		data.expected = new int[]{0, 0, 0, 0};
		data.zoom = zoom;
		data.minimumLight = baseLight;
		data.maximumLight = maxLight;
		data.bg = bg;
		data.props = prop;
		data.npcs = npc;
		data.monsters = monster;
		data.useShader = "";
		data.respawns = respawns;
		data.bgColor = bgColor;
		data.initialPosition = AHME.initialPos;
		data.backgroundMusic = bgm;
		Gdx.files.local("maps/").file().mkdirs();
		FileHandle file = Gdx.files.local("maps/" + mapFileName);
		file.writeString(json.prettyPrint(data), false);

	}

	public void applyTiles()
	{ 
		Vector2 mousePos = new Vector2((Gdx.input.getX()) * camera.zoom, (camera.viewportHeight - Gdx.input.getY()) * camera.zoom);
		mousePos.add(crp());

		float width = toMap(mousePos.x) - selectedX + 1;
		float height = toMap(mousePos.y) - selectedY + 1;

		if(height < 0)
		{
			height *= -1f;
			selectedY -= height;
		}

		if(width < 0)
		{
			width *= -1f;
			selectedX -= width;
		}

		if(selectedX < 0)
			selectedX = 0;

		if(selectedY < 0)
			selectedY = 0;

		for(int y = selectedY;y < Math.min(selectedY + height, mapwidth);y++)
		{
			for(int x = selectedX;x < Math.min(selectedX + width, mapheight);x++)
			{
				if(x < 0 || x > map[0].length || y < 0 || y > map.length)
					continue;

				if(!deleteMode)
					map[y][x] = Tile.getTileTypeById(myTile+1);
				else
					map[y][x] = null;
			}
		}
	}

	public void applyBg()
	{ 
		Vector2 mousePos = new Vector2((Gdx.input.getX()) * camera.zoom, (camera.viewportHeight - Gdx.input.getY()) * camera.zoom);
		mousePos.add(crp());

		float width = toMap(mousePos.x) - selectedX + 1;
		float height = toMap(mousePos.y) - selectedY + 1;

		if(height < 0)
			selectedY -= height;

		if(width < 0)
			selectedX -= width;

		if(selectedX < 0)
			selectedX = 0;

		if(selectedY < 0)
			selectedY = 0;

		for(int y = selectedY;y < Math.min(selectedY + height, mapwidth);y++)
		{
			for(int x = selectedX;x < Math.min(selectedX + width, mapheight);x++)
			{
				if(x < 0 || x > map[0].length || y < 0 || y > map.length)
					continue;

				if(!deleteMode)
					bg[y][x][myLayer] = Background.getTileTypeById(myBg+1);
				else
					bg[y][x][myLayer] = null;
			}
		}
	}

	public void renderMap(SpriteBatch batch)
	{
		Rectangle s = AHME.getScreen();
		int startX = (int) Math.floor(s.x/Tile.TILE_SIZE);
		int startY = (int) Math.floor(s.y/Tile.TILE_SIZE);
		int endX = (int)Math.ceil((s.x+s.width)/Tile.TILE_SIZE);
		int endY = (int)Math.ceil((s.y+s.height)/Tile.TILE_SIZE);

		startX = Main.clamp(0, Math.max(0, startX), AHME.mapheight);
		startY = Main.clamp(0, Math.max(0, startY), AHME.mapwidth);

		endX = Main.clamp(0, Math.min(AHME.mapheight, endX), AHME.mapheight);
		endY = Main.clamp(0, Math.min(AHME.mapwidth, endY), AHME.mapwidth);

		for(int z = 0;z < bg[0][0].length;z++)
		{
			for(int y = startY;y < endY;y++)
			{
				for(int x = startX;x < endX;x++)
				{
					if(bg[y][x][z] == null || bg[y][x][z].getId() > BGCOUNT)
						continue;

					batch.draw(backgrounds[bg[y][x][z].getId()-1], x * TILESIZE, y * TILESIZE);
				}
			}
		}
		for(int y = startY;y < endY;y++)
		{
			for(int x = startX;x < endX;x++)
			{
				if(map[y][x] == null)
					continue;

				if(tileMapped[map[y][x].getId()-1])
				{
					int hx = 64;
					int hy = 64;
					TextureRegion treg = new TextureRegion(tiles[map[y][x].getId()-1]);
					boolean haveleft = true;
					boolean haveright = true;
					boolean haveup = true;
					boolean havedown = true;

					if(x-1 < 0)
					{
						haveleft = true;
					}
					else if((map[y][x-1] == null))
					{
						haveleft = false;
					}

					if(x+1 >= mapheight)
					{
						haveright = true;
					}
					else if((map[y][x+1] == null))
					{
						haveright = false;
					}

					if(y+1 >= mapwidth)
					{
						haveup = true;
					}
					else if((map[y+1][x] == null))
					{
						haveup = false;
					}

					if(y-1 < 0)
					{
						havedown = true;
					}
					else if((map[y-1][x] == null))
					{
						havedown = false;
					}


					if(haveleft && !haveright)
					{
						hx = 128;
					}
					else if(!haveleft && haveright)
					{
						hx = 0;
					}

					if(havedown && !haveup)
					{
						hy = 0;
					}
					else if(!havedown && haveup)
					{
						hy = 128;
					}

					treg.setRegion(hx, hy, 64, 64);
					batch.draw(treg, x * TILESIZE, y * TILESIZE);
				}
				else
				{
					batch.draw(tiles[map[y][x].getId()-1], x * TILESIZE, y * TILESIZE);
				}
			}
		}
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.setColor(1, 1, 1, 0.5f);
		if(fg != null)
		{
			for(int y = startY;y < endY;y++)
			{
				for(int x = startX;x < endX;x++)
				{
					try
					{
						if(fg[y][x] == null)
							continue;
					}
					catch(Exception ex)
					{
						System.out.println("Exception thrown: X = " + x + " Y = " + y);
						System.out.println("Exception thrown: Limits X = " + this.fg[0].length + " Y = " + this.fg.length);
						System.out.println("Exception thrown: MAP X = " + AHME.mapheight + " Y = " + AHME.mapwidth);
						System.out.println("Exception thrown: Ends X = " + endX + " Y = " + endY);
					}

					if(tileMapped[fg[y][x].getId()-1])
					{
						int hx = 64;
						int hy = 64;
						TextureRegion treg = new TextureRegion(tiles[fg[y][x].getId()-1]);
						boolean haveleft = true;
						boolean haveright = true;
						boolean haveup = true;
						boolean havedown = true;

						if(x-1 < 0)
						{
							haveleft = true;
						}
						else if((fg[y][x-1] == null))
						{
							haveleft = false;
						}

						if(x+1 >= mapheight)
						{
							haveright = true;
						}
						else if((fg[y][x+1] == null))
						{
							haveright = false;
						}

						if(y+1 >= mapwidth)
						{
							haveup = true;
						}
						else if((fg[y+1][x] == null))
						{
							haveup = false;
						}

						if(y-1 < 0)
						{
							havedown = true;
						}
						else if((fg[y-1][x] == null))
						{
							havedown = false;
						}


						if(haveleft && !haveright)
						{
							hx = 128;
						}
						else if(!haveleft && haveright)
						{
							hx = 0;
						}

						if(havedown && !haveup)
						{
							hy = 0;
						}
						else if(!havedown && haveup)
						{
							hy = 128;
						}

						treg.setRegion(hx, hy, 64, 64);
						batch.draw(treg, x * TILESIZE, y * TILESIZE);
					}
					else
					{
						batch.draw(tiles[fg[y][x].getId()-1], x * TILESIZE, y * TILESIZE);
					}
				}
			}
		}
		batch.setColor(1, 1, 1, 1f);
	}

	public void displayTileMenu()
	{
		hudShapeRenderer.begin(ShapeType.Filled);
		hudShapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
		hudShapeRenderer.rect(0, 0, 512, Gdx.graphics.getHeight());
		hudShapeRenderer.end();

		hudBatch.begin();
		int d = 0;
		for(int i = 0 + offset;i < TILECOUNT;i++)
		{
			float y = d / 8 * TILESIZE;
			float x = d % 8 * TILESIZE;
			TextureRegion treg = new TextureRegion(tiles[i]);
			if(tileMapped[i])
			{
				treg.setRegion(64, 0, 64, 64);
			}
			hudBatch.draw(treg, x, y);
			Rectangle hitBox = new Rectangle(x, y, TILESIZE, TILESIZE);
			if(hitBox.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				if(Gdx.input.isButtonPressed(Buttons.LEFT))
				{
					myTile = i;
				}
			}
			d++;
		}
		hudBatch.end();

		Gdx.gl.glLineWidth(2);
		hudShapeRenderer.begin(ShapeType.Line); 
		float y = (myTile-offset) / 8 * TILESIZE;
		float x = (myTile-offset) % 8 * TILESIZE;
		hudShapeRenderer.setColor(1f, 1f, 0f, 1f);
		hudShapeRenderer.rect(x, y, TILESIZE, TILESIZE);
		hudShapeRenderer.end();
	}

	public void displayBgMenu()
	{
		hudShapeRenderer.begin(ShapeType.Filled);
		hudShapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
		hudShapeRenderer.rect(Gdx.graphics.getWidth() - 512, 0, 512, Gdx.graphics.getHeight());
		hudShapeRenderer.end();

		hudBatch.begin();
		int d = 0;
		for(int i = 0 + offset;i < BGCOUNT;i++)
		{
			float y = d / 8 * TILESIZE;
			float x = d % 8 * TILESIZE + Gdx.graphics.getWidth() - 512;
			hudBatch.draw(backgrounds[i], x, y, TILESIZE, TILESIZE);
			Rectangle hitBox = new Rectangle(x, y, TILESIZE, TILESIZE);
			if(hitBox.contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY()))
			{
				if(Gdx.input.isButtonPressed(Buttons.LEFT))
				{
					myBg = i;
				}
			}
			d++;
		}
		hudBatch.end();

		Gdx.gl.glLineWidth(2);
		hudShapeRenderer.begin(ShapeType.Line); 
		float y = (myBg-offset) / 8 * TILESIZE;
		float x = (myBg-offset) % 8 * TILESIZE + Gdx.graphics.getWidth() - 512;
		hudShapeRenderer.setColor(1f, 1f, 0f, 1f);
		hudShapeRenderer.rect(x, y, TILESIZE, TILESIZE);
		hudShapeRenderer.end();

		hudBatch.begin();
		font.draw(hudBatch, "BG Layer: " + myLayer, Gdx.graphics.getWidth()/2-30, Gdx.graphics.getHeight()-30);
		hudBatch.end();
	}

	public void startNewMap(int width, int height)
	{
		map = new Tile[width][height];
		bg = new Background[width][height][3];
		fg = new Tile[width][height];
		AHME.mapwidth = width;
		AHME.mapheight = height;
		for(int y = 0;y < map.length;y++)
		{
			for(int x = 0;x < map[0].length;x++)
			{
				map[y][x] = null;
				if(x == 0 || y == 0 || x == map[0].length-1 || y == map.length-1)
					map[y][x] = Tile.METALFLOOR;
				for(int z = 0;z < 3;z++)
					bg[y][x][z] = null;
			}
		}
		monster.clear();
		treasure.clear();
		items.clear();
		npc.clear();
		prop.clear();
	}

	@Override
	public void dispose () {
		batch.dispose();
		for (Texture tile : tiles) {
			if (tile != null) {
				tile.dispose();
			}
		}
	}

	public Vector2 crp()
	{
		return new Vector2(camera.position.x - (camera.viewportWidth * camera.zoom)/2, camera.position.y - (camera.viewportHeight * camera.zoom)/2);
	}

	public int toMap(float x)
	{
		return (int)(Math.floor(x/TILESIZE));
	}

	public static Vector2 cameraRealPosition() {
		Vector2 result = new Vector2(camera.position.x - (Gdx.graphics.getWidth() * camera.zoom) / 2,
				camera.position.y - (Gdx.graphics.getHeight() * camera.zoom) / 2);
		return result;
	}

	public static Vector2 mouseInWorld() {
		return new Vector2(cameraRealPosition().x + Gdx.input.getX() * camera.zoom,
				cameraRealPosition().y + Gdx.graphics.getHeight() * camera.zoom - Gdx.input.getY() * camera.zoom);
	}

	public static Rectangle getScreen() {
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		Rectangle screen = new Rectangle(camera.position.x - width / 2, camera.position.y - height / 2, width, height);
		return screen;
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
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		offset = Math.max(0, offset - amount*8);
		return false;
	}

}
