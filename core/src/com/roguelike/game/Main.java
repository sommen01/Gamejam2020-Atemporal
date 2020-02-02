package com.roguelike.game;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.roguelike.constants.ArkaClass;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Cutscene;
import com.roguelike.constants.Dialogs;
import com.roguelike.constants.Item;
import com.roguelike.constants.Objective;
import com.roguelike.constants.Quest;
import com.roguelike.constants.Recipe;
import com.roguelike.constants.Set;
import com.roguelike.constants.Skill;
import com.roguelike.constants.Story;
import com.roguelike.entities.*;
import com.roguelike.online.AHS;
import com.roguelike.online.GameClient;
import com.roguelike.online.interpretations.CompactMonster;
import com.roguelike.online.interpretations.CompactProjectile;
import com.roguelike.online.interpretations.CompressedBiosphere;
import com.roguelike.online.interpretations.DamageRequest;
import com.roguelike.online.interpretations.EntitiesUpdater;
import com.roguelike.online.interpretations.EntityPacket;
import com.roguelike.online.interpretations.MapInfos;
import com.roguelike.online.interpretations.MapRequest;
import com.roguelike.online.interpretations.MapSaveRequest;
import com.roguelike.online.interpretations.SomeRequest;
import com.roguelike.online.interpretations.SomeResponse;
import com.roguelike.world.*;
import com.roguelike.world.loader.GameMapLoader;
import com.roguelike.world.loader.GenerationLoader;
import com.roguelike.world.loader.PlayerLoader;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;

public class Main implements Screen {
	public static SpriteBatch batch;
	public static SpriteBatch hudBatch;
	public static SpriteBatch mapBatch;
	public static SpriteBatch fb;
	public static SpriteBatch fb2;
	public static SpriteBatch fb3;
	public static BitmapFont font;
	public static BitmapFont italicfont;
	public static final int FONT_SIZE = 16;
	public static OrthographicCamera camera;
	public static Vector2 customCameraPosition;
	public static float customCameraZoom;
	public static int customCameraTime;
	public static Vector2 oldCameraPos;
	public static Game game;
	public static GameMap worldMap;
	public static ShapeRenderer shapeRenderer;
	public static ShapeRenderer hudShapeRenderer;

	public static boolean displayInventory;
	public static boolean displayCharacter;
	public static boolean displayDialog;
	public static boolean displaySkill;
	public static int displaySkillOffset = 0;
	public static Skill displaySkillInspecting = null;
	public static int displaySkillInspectingLevel = 1;
	public static boolean equippingSkill = false;
	public static Skill equippingSkillRef;
	public static int currentSkillBar = 0;
	public static Dialogs dialog = null;
	public static int dialogTicks = 0;
	public static Story currentStory;
	public static float currentStoryBGAlpha = 0f;
	public static float currentStoryImageAlpha = 0f;
	public static Story currentLoadedStory;
	public static Entity dialogEntity = null;
	public static boolean dialogFullDrawn = false;
	public static boolean displayWarps = false;
	public static boolean waitingForMap = false;
	public static boolean instaCamera = false;
	
	public static boolean drawHud = true;

	public static int myBlock = 1;

	public static Player[] player = new Player[4];
	public static int me = -1;
	public static ArrayList<Monster> monster = new ArrayList<Monster>();
	public static ArrayList<Monster> monsterex = new ArrayList<Monster>();
	public static ArrayList<Monster> natural = new ArrayList<Monster>();
	public static ArrayList<Monster> naturalex = new ArrayList<Monster>();
	public static ArrayList<Particle> particle = new ArrayList<Particle>();
	public static ArrayList<Particle> bgparticle = new ArrayList<Particle>();
	public static ArrayList<Particle> particleex = new ArrayList<Particle>();
	public static ArrayList<Lighting> lighting = new ArrayList<Lighting>();
	public static ArrayList<Lighting> lightingex = new ArrayList<Lighting>();
	public static ArrayList<Treasure> treasure = new ArrayList<Treasure>();
	public static ArrayList<Item> items = new ArrayList<Item>();
	public static ArrayList<NPC> npc = new ArrayList<NPC>();
	public static LivingText[] livingtext = new LivingText[100];
	public static ArrayList<Projectile> projectile = new ArrayList<Projectile>();
	public static ArrayList<Projectile> projectileex = new ArrayList<Projectile>();
	public static ArrayList<Prop> prop = new ArrayList<Prop>();
	public static ArrayList<Respawn> respawns = new ArrayList<Respawn>();
	
	public static HashMap<Integer, ArrayList<HPBG>> HPBGs = new HashMap<Integer, ArrayList<HPBG>>();
	
	public static boolean hosting = false;

	public static float gravity = -48f;

	public static int gameTick = 0;

	public static boolean debug = false;
	public static int[] var = new int[10];
	public static int currentVar = 0;
	public static int inventoryX = -300;
	public static int characterX = -300;
	public static int craftingY = -500;
	public static int selectedSlot = -1;
	public static boolean mouseJustPressed = false;
	public static boolean mouse2JustPressed = false;
	public static boolean mouse4JustPressed = false;
	public static boolean holdingMouse = false;
	public static boolean holdingMouse4 = false;
	public static Item holdingItem;
	public static int originalSlot = -1;
	public static boolean gotThisFrame = false;

	public static float blackScreenTime = 0f;
	public static Color blackScreenColor = Color.BLACK;
	public static Vector2 blackScreenPostPosition = Vector2.Zero.cpy();

	public static int nextMap = 1;
	public static float timeForNextmap = 0f;
	public static boolean readyForNextmap = false;
	public static boolean nminvertx = false;
	public static boolean nminverty = false;
	public static String nmname = "";
	public static int[] nmspecialgen = null;

	public static float skillAlpha = 0.5f;

	public static boolean newGen = false;

	public static GameMap cameMap;
	public boolean holdingMouse2;
	public static boolean displayCrafting = false;
	public static Recipe displayCraftingRecipe = null;
	public static int displayCraftStation = 0;
	public static Entity displayCraftOrigin = null;

	public static String saveName;

	public static GlyphLayout layout;
	public static GlyphLayout cl;
	public static ShaderProgram shader;
	public static String usingShader = "";
	
	public static FrameBuffer fbo;
	public static TextureRegion fboRegion;
	
	public static boolean colorMode = false;
	public static boolean pause = false;
	
	public static ShaderProgram basicShader;
	
	public static boolean mapTransitioning = false;
	public static int mapTransitionFromX; // Global map from X
	public static int mapTransitionFromY; // Global map from Y
	public static int mapTransitionToX; // Global map to X
	public static int mapTransitionToY; // Global map to Y
	public static int mapTransitionToFinalX; // Local final map to X
	public static int mapTransitionToFinalY; // Local final map to Y
	public static int mapTransitionTicks = 0;

	public static Texture worldMapImage;
	public static int mapDrawX = 0;
	public static int mapDrawY = 0;
	
	public static boolean displayMap = false;
	
	public static Texture[][] worldMapImages = new Texture[10][10];
	
	public static Cutscene cutscene;
	public static int cutsceneBordersTicks;
	
	public static int lastQuestTicks = 999999;
	public static Quest lastQuest = null;
	public static String lastQuestText = "";
	
	public static int earthquake = 0;
	
	public static GameClient client = null;
	
	public static boolean isOnline = false;
	
	public static boolean serverSide = false;
	
	public static ArrayList<Object> serverQuery = new ArrayList<Object>();
	public static ArrayList<Object> serverQueryEx = new ArrayList<Object>();
	public static ArrayList<Event> scheduledTasks = new ArrayList<Event>();
	public static int lastDialogTicks = 9999;
	public static float customShowOff = 0f;
	public static int customShowOffTicks = 0;
	private static int customShowOffLastTicks;
	public static String enterAs;
	private static int lightcount;
	public static int displayCraftingOffset = 0;
	public static Json json = new Json();
	
	public static float desiredRotation = 0;
	public static float rotateby = 0;
	
	public static boolean loadingMap = true;
	public static boolean loadingBasics = true;
	public static String loadingProgress;
	
	public static int superAnimationTicks = 99999;

	public static Boolean[] originalKeysState = new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
	public static Boolean[] trackedKeysState = new Boolean[]{Boolean.FALSE, Boolean.FALSE, Boolean.FALSE};
	public static boolean gameUpdatedLastFrame = true;
	
	public static Toolkit toolkit;
	
	public static boolean playing = false;
	private static int resetTime = 0;
	
	static {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/PixelFJVerdana12pt.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.characters = FreeTypeFontGenerator.DEFAULT_CHARS+"•";
		params.magFilter = TextureFilter.Nearest;
		params.minFilter = TextureFilter.Linear;        
		params.genMipMaps = true;
		font = generator.generateFont(params);
		font.getData().markupEnabled = true;
		generator.dispose();

		json.setIgnoreUnknownFields(true);
		json.setOutputType(OutputType.minimal);
		layout = new GlyphLayout();
		cl = new GlyphLayout();
	}
	
	public Main(Game roguelike, String string) {
		this();
		game = roguelike;
		enterAs = string;
	}

	public Main()
	{
		worldMap = null;
	}

	@Override
	public void show() {
		//Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		//System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
	    /*Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width+4,
	    	    Gdx.graphics.getDisplayMode().height+1);*/
		worldMap = null;
		particleex.clear();
		monsterex.clear();
		projectileex.clear();
		loadingBasics = true;
		batch = new SpriteBatch(1000, shader);
		hudBatch = new SpriteBatch();
		mapBatch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		oldCameraPos = new Vector2(camera.position.x, camera.position.y);
		shapeRenderer = new ShapeRenderer();
		hudShapeRenderer = new ShapeRenderer();
		batch.setProjectionMatrix(camera.combined);

		ScrollProcessor inputProcessor = new ScrollProcessor();
		Gdx.input.setInputProcessor(inputProcessor);

		toolkit = Toolkit.getDefaultToolkit();
		originalKeysState = new Boolean[] {toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK), toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK), toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK)};
		trackedKeysState = new Boolean[] {toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK), toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK), toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK)};
		loadAfterTextures();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		serverQuery.addAll(serverQueryEx);
		serverQueryEx.clear();
		for(Object object : Main.serverQuery)
		{
			if (object instanceof SomeResponse) 
			{
				SomeResponse response = (SomeResponse)object;
				for(String responseT : response.text)
				{
					String[] msg = responseT.split(" ");
					if(msg[0].equalsIgnoreCase("ActivePlayers") && !Main.hosting)
					{
						for(int i = 1;i <= 4;i++)
						{
							Main.player[i-1].active = Boolean.parseBoolean(msg[i]);
						}
					}
					else if(msg[0].equalsIgnoreCase("KeyPress"))
					{
						int id = Integer.parseInt(msg[1]);
						Main.player[id].pressingButtons[0] = Boolean.parseBoolean(msg[2]);
						Main.player[id].pressingButtons[1] = Boolean.parseBoolean(msg[3]);
						Main.player[id].pressingButtons[2] = Boolean.parseBoolean(msg[4]);
						Main.player[id].pressingButtons[3] = Boolean.parseBoolean(msg[5]);
						Main.player[id].onlineWorking = true;
					}
					else if(msg[0].equalsIgnoreCase("Inventory"))
					{
						int id = Integer.parseInt(msg[1]);
						Main.player[id].checkingInventory = !Main.player[id].checkingInventory;
						Main.player[id].resetAnim();
						System.out.println("Client " + id + " toggled inventory");
					}
					if(msg[0].equalsIgnoreCase("Jump"))
					{
						int id = Integer.parseInt(msg[1]);
						Main.player[id].executeJump();
						System.out.println("Client " + id + " jumped");
					}
					else if(msg[0].equalsIgnoreCase("Roll"))
					{
						int rolldir = Integer.parseInt(msg[1]);
						int id = Integer.parseInt(msg[2]);
						Main.player[id].executeRoll(rolldir);
						System.out.println("Client " + id + " rolled");
					}
					else if(msg[0].equalsIgnoreCase("mousePos"))
					{
						float x = Float.parseFloat(msg[2]);
						float y = Float.parseFloat(msg[3]);
						int id = Integer.parseInt(msg[1]);
						Main.player[id].mousePos.x = x;
						Main.player[id].mousePos.y = y;
					}
					else if(msg[0].equalsIgnoreCase("EquipItem"))
					{
						int id = Integer.parseInt(msg[1]);
						int itemid = Integer.parseInt(msg[2]);
						int itemslot = Integer.parseInt(msg[3]);
						Item item = new Item().SetInfos(itemid);
						Main.player[id].inventory[itemslot] = item;
						Main.player[id].inventory[itemslot].onEquip(Main.player[id], itemslot, false);
						System.out.println("Client " + id + " equipped " + item.name);
					}
					else if(msg[0].equalsIgnoreCase("DeEquipItem"))
					{
						int id = Integer.parseInt(msg[1]);
						int itemslot = Integer.parseInt(msg[2]);
						Main.player[id].inventory[itemslot].onDeEquip(Main.player[id], itemslot, false);
						Main.player[id].inventory[itemslot] = null;
						System.out.println("Client " + id + " deequipped an item");
					}
					else if(msg[0].equalsIgnoreCase("ExecAttack"))
					{
						int hand = Integer.parseInt(msg[1]);
						int id = Integer.parseInt(msg[2]);
						float atx = Float.parseFloat(msg[3]);
						float aty = Float.parseFloat(msg[4]);
						Main.player[id].executeAttack(hand, new Vector2(atx, aty));
						System.out.println("Client " + id + " executed attack.");
					}
					else if(msg[0].equalsIgnoreCase("RequestMonster"))
					{
						int type = Integer.parseInt(msg[3]);
						float atx = Float.parseFloat(msg[1]);
						float aty = Float.parseFloat(msg[2]);
						int mapx = Integer.parseInt(msg[4]);
						int mapy = Integer.parseInt(msg[5]);
						int uid = Integer.parseInt(msg[6]);
						Monster.Create(new Vector2(atx, aty), type, mapx, mapy, uid);
					}
					else if(msg[0].equalsIgnoreCase("FinishMapLoad"))
					{
						Main.player[Main.me].loadingMapOnline = false;
						Main.blackScreenTime = -3f;
						System.out.println("Maps are equal, running local map.");
					}
					else if(msg[0].equalsIgnoreCase("changingMap"))
					{
						int id = Integer.parseInt(msg[1]);
						boolean val = Boolean.parseBoolean(msg[2]);
						Main.player[id].loadingMapOnline = val;
					}
				}
			}
			else if (object instanceof EntityPacket)
			{
				EntityPacket ep = (EntityPacket)object;
				if(ep.message.equalsIgnoreCase("My Player") || ep.message.equalsIgnoreCase("Connected"))
				{
					Player player = (Player)ep.entity;
					int slot = player.whoAmI;
					Main.player[slot] = player;
					System.out.println("Received player, name|slot|active: " + player.name + "|"+player.whoAmI+"|"+player.active);
					if(ep.message.equalsIgnoreCase("My Player"))
					{
						me = slot;
						Main.player[slot].loadingMapOnline = true;
						System.out.println("Now this is my player: " + Main.player[slot]);
						Main.SwitchMap(1, Main.player[me].position, null);
					}
				}
			}
			else if(object.getClass() == Player[].class)
			{
				Player[] player = (Player[])object;
				Main.player = player;
			}
			else if (object instanceof EntitiesUpdater) 
			{
				EntitiesUpdater response = (EntitiesUpdater)object;
				String[] broken = response.infos.split(" ");
				if(broken[0].equalsIgnoreCase("player"))
				{
					int id = response.id;
					Main.player[id].position = response.position;
					Main.player[id].velocity = response.velocity;
				}
			}
			else if(object instanceof Monster)
			{
				Monster m = (Monster)object;
				System.out.println("Received a " + m.name + " in map " + m.myMapX + " " + m.myMapY);
				if(!m.natural)
					Main.monsterex.add(m);
				else
					Main.naturalex.add(m);
			}
			else if(object instanceof NPC)
			{
				NPC m = (NPC)object;
				System.out.println("Received a " + m.name + " in map " + m.myMapX + " " + m.myMapY);
				Main.npc.add(m);
			}
			else if(object instanceof Item)
			{
				Main.items.add((Item)object);
			}
			else if(object instanceof CompactProjectile)
			{
				Main.projectile.add(((CompactProjectile)object).toProjectile());
			}
			else if(object instanceof Treasure)
			{
				Main.treasure.add((Treasure)object);
			}
			else if(object instanceof Prop)
			{
				Main.prop.add((Prop)object);
			}
			else if(object instanceof LivingText)
			{
				System.out.println("Received living text.");
				for(int i = 0;i < Main.livingtext.length;i++)
				{
					if(!livingtext[i].active)
					{
						livingtext[i] = (LivingText)object;
						livingtext[i].active = true;
						break;
					}
				}
			}
			else if(object instanceof DamageRequest)
			{
				DamageRequest dr = (DamageRequest)object;
				Monster m = null;
				for(Monster e : Main.monster)
				{
					if(e.uid == dr.monsteruid)
					{
						m = e;
						break;
					}
				}
				System.out.println("Received Damage Request");
				if(m != null)
				{
					System.out.println("Applying " + dr.damage + " to " + m.name);
					m.applyDamage(dr.damage, dr.direction, dr.scalar, dr.critical, dr.attackerid >= 0 ? Main.player[dr.attackerid] : null, dr.damageType, dr.damageClass, true);
				}
				else
				{
					System.out.println("Can't find monster UID " + dr.monsteruid);
				}
			}
			else if(object instanceof MapInfos)
			{
				if(player[me].loadingMapOnline)
				{
					MapInfos infos = (MapInfos)object;
					System.out.println("Map received");
					LoadMap(infos.map);
					System.out.println("Map request finished, loading map.");
					Main.player[Main.me].loadingMapOnline = false;
					Main.blackScreenTime = -3f;
				}
			}
			else if(object instanceof CompressedBiosphere)
			{
				CompressedBiosphere biosphere = (CompressedBiosphere)object;
				monster.clear();
				monsterex.clear();
				for(CompactMonster cm : biosphere.compactedMonsters)
				{
					Monster m = cm.toMonster();
					if(!m.natural)
						monster.add(m);
					else
						natural.add(m);
				}
				for(int i = 0;i < biosphere.compactedPlayers.length;i++)
				{
					biosphere.compactedPlayers[i].toPlayer(player[i]);
				}
			}
		}
		serverQuery.clear();
		
		if(gameTick > Integer.MAX_VALUE/1000)
			gameTick = 0;


		if (mouseJustPressed) {
			mouseJustPressed = false;
		}

		if (mouse2JustPressed) {
			mouse2JustPressed = false;
		}
		
		if (mouse4JustPressed) {
			mouse4JustPressed = false;
		}

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			if (!holdingMouse) {
				mouseJustPressed = true;
				holdingMouse = true;
			}
		} else {
			holdingMouse = false;
		}

		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			if (!holdingMouse2) {
				mouse2JustPressed = true;
				holdingMouse2 = true;
			}
		} else {
			holdingMouse2 = false;
		}
		
		if (Gdx.input.isButtonPressed(Buttons.FORWARD) || Gdx.input.isButtonPressed(Buttons.BACK)) {
			if (!holdingMouse4) {
				mouse4JustPressed = true;
				holdingMouse4 = true;
			}
		} else {
			holdingMouse4 = false;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) && !Main.displayCharacter && !Main.displayCrafting && !Main.displayDialog
				&& !Main.displayInventory && !Main.displayMap && !Main.displaySkill && !Main.displayWarps && !Main.equippingSkill)
		{
			pause = !pause;
		}

		if(!loadingAnything())
		{
			if(!pause)
			{
				gameTick++;
				player[me].saveInfo.myWorldTime++;
				HandleInput();
				GameUpdate(1f/60f);

				if(Main.mapTransitioning && (Main.mapTransitionToX != player[me].myMapX || Main.mapTransitionToY != player[me].myMapY))
				{
					Main.SaveMap();
					player[me].myMapX = mapTransitionToX;
					player[me].myMapY = mapTransitionToY;
					Main.SwitchMap(Constant.tryGetMapForEntity(Constant.getPlayerList()[me]).id, false, false, null);
					System.out.println("Switching maps cause map transitioning.");
					Vector2 freeSpace = worldMap.getNextestFreeSpace(Main.mapTransitionToFinalX, Main.mapTransitionToFinalY, player[me].width, player[me].height, 1, 1).scl(64);
					player[me].position.x = freeSpace.x;
					player[me].position.y = freeSpace.y;
				}
				Main.mapTransitionTicks++;
				if(mapTransitionTicks > 180)
				{
					mapTransitioning = false;
				}

				if(!Main.displayDialog)
				{
					if(instaCamera)
					{
						camera.position.x = player[me].Center().x;
						camera.position.y = player[me].Center().y;
					}
					else
					{
						boolean offground = false;
						int distanceY = (int)(player[me].position.y/64) - Main.worldMap.getNotFreeY(Main.clamp(0, (int)player[me].Center().x/64, worldMap.width-1), Main.clamp(0, (int)player[me].position.y/64, worldMap.height-1));
						if(distanceY > 5)
							offground = true;
						camera.position.x += (player[me].Center().x - camera.position.x)/8f;
						camera.position.y += (player[me].Center().y - camera.position.y+(!offground ? 128 : 0))/8f;
					}
					/*if(Main.worldMap.isOutside())
				{
					//camera.position.y += 128;
					camera.position.x -= (Gdx.graphics.getWidth()/2f-Gdx.input.getX())/2f;
					camera.position.y -= (Gdx.graphics.getHeight()/2f - (Gdx.graphics.getHeight()-Gdx.input.getY()))/2f;
				}*/
				}
				/*
				 * boolean haveBoss = false; for(int i = 0;i < 200;i++) { if(monster[i].active
				 * && monster[i].boss && player.Center().dst(monster[i].Center()) < 1500) {
				 * haveBoss = true; break; } } for(Monster m : monster) { if(m.active && m.boss)
				 * { haveBoss = true; break; } }
				 */

				/*
				 * if(haveBoss || player.superVision) { camera.zoom += (2 + (var[2]/10f) -
				 * camera.zoom)/8; } else { camera.zoom += (1 + (var[2]/10f) - camera.zoom)/8; }
				 * if(camera.zoom <= 1.00005 + var[2]/10f) camera.zoom = 1 + var[2]/10f;
				 * 
				 * if(camera.zoom >= 1.999995 + var[2]/10f) camera.zoom = 2 + var[2]/10f;
				 */
				if(Main.customCameraTime > 0)
				{
					Vector2 dest = Main.customCameraPosition;
					Vector2 cameraPos = new Vector2(camera.position.x, camera.position.y);
					if(cameraPos.dst(dest) > 1)
					{
						camera.position.x += (dest.x - camera.position.x) / 8;
						camera.position.y += (dest.y - camera.position.y) / 8;
					}
					else
					{
						camera.position.x = dest.x;
						camera.position.y = dest.y;
					}
					if(customCameraZoom != -1)
					{
						camera.zoom += (Main.customCameraZoom - camera.zoom) / 8;
					}
				}
				else if(superAnimationTicks <= 90)
				{
					float ticks = (superAnimationTicks*180f)/90f;
					float sin = (float)Math.sin(ticks * Math.PI/180f);
					float zoom = Main.worldMap.zoom * (1f-sin/2f);
					camera.zoom = zoom;
					if(ticks > 90)
					{
						sin = 1f + (1f-sin);
					}
					Main.setCameraRotation(360*sin);
				}
				else if(Main.usingSkill(player[me]))
				{
					camera.zoom += (Main.worldMap.zoom * 1f - camera.zoom) / 2;
					//camera.zoom += (Main.worldMap.zoom * 0.8f - camera.zoom) / 2;
					//camera.zoom += (Main.worldMap.zoom * 0.75f - camera.zoom)/10f;
				}
				else if((Main.dialog != null && Main.displayDialog) || Main.displayCrafting || Main.cutscene != null)
				{
					Vector2 cameraPos = new Vector2(camera.position.x, camera.position.y);

					Vector2 dest = new Vector2(player[me].Center().x, player[me].Center().y - player[me].height - 16);
					if(!Main.displayCrafting && !dialog.playerDialog && dialogEntity != null)
						dest = new Vector2(dialogEntity.Center().x, dialogEntity.Center().y - dialogEntity.height - 16);

					if(cameraPos.dst(dest) > 1)
					{
						camera.position.x += (dest.x - camera.position.x) / 8;
						camera.position.y += (dest.y - camera.position.y) / 8;
					}
					else
					{
						camera.position.x = dest.x;
						camera.position.y = dest.y;
					}
					camera.zoom += (Main.worldMap.zoom * 0.8f - camera.zoom) / 8;
				}
				else if(!Main.usingSkill(player[me]) && Math.abs(camera.zoom-(worldMap.zoom + var[2] / 100f)) > 0.01f)
				{
					camera.zoom += ((worldMap.zoom + var[2] / 100f) - camera.zoom) / 4;
				}
				else
				{
					camera.zoom += ((worldMap.zoom + var[2] / 100f) - camera.zoom)/ 8f;
					if(Math.abs(camera.zoom - (worldMap.zoom + var[2] / 100f)) < 0.005f)
					{
						camera.zoom = (worldMap.zoom + var[2] / 100f);
					}
				}

				camera.position.x += MathUtils.random(earthquake * -1, earthquake);
				camera.position.y += MathUtils.random(earthquake * -1, earthquake);

				if(!Main.displayCrafting)
				{
					FixCameraX();
					FixCameraY();
				}
				camera.direction.rotate(camera.direction, rotateby);
				camera.up.rotate(camera.direction, rotateby);
				rotateby = 0f;
				camera.update();
				batch.setProjectionMatrix(camera.combined);
				shapeRenderer.setProjectionMatrix(camera.combined);

				Matrix4 uiMatrix = camera.combined.cpy();
				uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				hudBatch.setProjectionMatrix(uiMatrix);
			}
			else
			{
				gameUpdatedLastFrame = false;
			}
			if(shouldDrawGame())
			{
				GameDraw(1f/60f);

				if(canDrawHud())
					DrawHud(1f/60f);

				DrawStory();
				if (displayCrafting) {
					DrawCraftScreen(1f/60f);
				}
				if (displayInventory) {
					DrawInventory(1f/60f);
				}
				if (displaySkill) {
					DrawSkillList(1f/60f);
				}
				if (displayCharacter) {
					DrawCharacter(1f/60f);
				}
				if (displayDialog) {
					DrawDialog(1f/60f);
				}
				if(displayWarps)
				{
					DrawWarpScreen(1f/60f);
				}
				Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				int type = 0;
				Rectangle skillsHitBox = new Rectangle(16, 16, player[me].skillSlotAvailable * 68 + 4, 64);
				Rectangle wpskillHitBox = new Rectangle(20 + player[me].skillSlotAvailable * 68 + 20, 20, 64, 64);
				if(skillsHitBox.contains(mouse) && !displayCrafting)
				{
					type = 3;
				}
				if(wpskillHitBox.contains(mouse) && !displayCrafting)
				{
					type = 4;
				}
				if (type == 3 || type == 4) {
					/*int x, slot;
        		x = (int) Math.floor((mouse.x - skillsHitBox.x) / 68f);
        		slot = x;
        		if (mouse.x >= 20 + player[me].skillSlotAvailable * 68 + 16) {
        			slot = 10;
        		}
        		if (slot >= 0 && slot <= 10 && player[me].skills[slot] != null) {
        			Skill skill = player[me].skills[slot];
        			int offsetY = 0;
        			float highestX = 300;
        			float sumY = 0;

        			font.getData().setScale(1f);
        			layout.setText(font, skill.name);
        			sumY += layout.height + 8;
        			if (layout.width > highestX)
        				highestX = layout.width;

        			String manaCost = "No cost";
        			if(skill.getMana() > 0)
        			{
        				manaCost = skill.getMana() + " mana";
        			}
        			String cdf = String.valueOf(skill.getCooldown(player[me])) + "s";
        			if(skill.getCooldown(player[me]) <= 0f)
        			{
        				cdf = "No";
        			}
        			else if(Math.floor(skill.getCooldown(player[me])) == skill.getCooldown(player[me]))
        			{
        				cdf = String.valueOf((int)Math.floor(skill.getCooldown(player[me]))) + "s";
        			}
        			String cd = cdf + " cooldown";
        			String mccd = manaCost + ", " + cd;
        			font.getData().setScale(0.5f);
        			layout.setText(font, mccd);
        			sumY += layout.height + 8;
        			if (layout.width > highestX)
        				highestX = layout.width;

        			if (skill.getDescription(player[me]).length() > 0) {
        				font.getData().setScale(0.55f);
        				layout.setText(font, skill.getDescription(player[me]), Color.BLACK, highestX, -1, true);
        				sumY += layout.height + 16;
        			}

        			hudBatch.begin();
        			Main.drawGenericBox(hudBatch, mouse.x - 8 + 20, mouse.y + 32, highestX + 16, sumY + 8);
        			hudShapeRenderer.begin(ShapeType.Filled);
        			hudShapeRenderer.setColor(Color.GRAY);
        			hudShapeRenderer.rect(mouse.x - 8 + 20, mouse.y + 32, highestX + 16, sumY + 8);
        			hudShapeRenderer.end();

        			// ----------------------------------------------
        			//hudBatch.begin();
        			font.getData().setScale(1f);
        			Main.prettyFontDraw(hudBatch, skill.name, mouse.x + 20, mouse.y + 30 - offsetY + sumY, 1f, Color.YELLOW, Color.BLACK, 1f, false, (int)highestX);
        			offsetY += layout.height + 8;

        			Main.prettyFontDraw(hudBatch, mccd, mouse.x + 20, mouse.y + 30 - offsetY + sumY, 0.5f, Color.CYAN, Color.BLACK, 1f, false, (int)highestX);
        			offsetY += layout.height + 8;

        			if (skill.getDescription(player[me]).length() > 0) {
        				Main.prettyFontDraw(hudBatch, skill.getDescription(player[me]), mouse.x + 20, mouse.y + 30 - offsetY + sumY, 0.55f, Color.WHITE, Color.BLACK, 1f, false, (int)highestX);
        				offsetY += layout.height + 16;
        			}
        			hudBatch.end();
        		}*/
				}
				if(equippingSkill)
				{
					Sprite black = new Sprite(Content.black);
					black.setPosition(-100, -100);
					black.setSize(Gdx.graphics.getWidth()+ 200, Gdx.graphics.getHeight()+200);
					black.setAlpha(0.8f);
					hudBatch.begin();
					black.draw(hudBatch);
					Main.prettyFontDraw(hudBatch, "Press a number to equip the skill", Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 1f, Color.WHITE, Color.BLACK, 1f, true, 0);
					hudBatch.end();
				}
				if(pause)
				{
					Sprite black = new Sprite(Content.black);
					black.setPosition(-100, -100);
					black.setSize(Gdx.graphics.getWidth()+ 200, Gdx.graphics.getHeight()+200);
					black.setAlpha(0.8f);
					hudBatch.begin();
					black.draw(hudBatch);
					Main.prettyFontDraw(hudBatch, "The game is paused", Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 1f, Color.WHITE, Color.BLACK, 1f, true, 0);
					Main.prettyFontDraw(hudBatch, "\nUse Ctrl+T to close the game", Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f, 1f, Color.WHITE, Color.BLACK, 1f, true, 0);
					hudBatch.end();
				}
			}

			if(shouldDrawWorldMap() || mapTransitioning)
			{
				DrawWorldMap(1f/60f);
			}
			drawForegroundTexts(1f/60f);

			hudBatch.begin();
			Sprite sprite = new Sprite(Content.cursor);
			sprite.setPosition((Gdx.input.getX()) - 8, (Gdx.graphics.getHeight() - Gdx.input.getY()) - 8);
			sprite.setRotation((Main.gameTick * 10) % 360);
			sprite.draw(hudBatch);
			if (holdingItem != null) {
				sprite = new Sprite(holdingItem.getInvTexture());
				sprite.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()-64);
				sprite.draw(hudBatch, 0.8f);
				if (holdingItem.canStack)
				{
					font.getData().setScale(1f);
					Main.prettyFontDraw(hudBatch, String.valueOf(holdingItem.stacks),
							Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY()-64+font.getCapHeight(),
							1f, Color.WHITE, Color.BLACK, 0.8f, false, -1);
				}
			}
			hudBatch.end();
		}
		else
		{
			gameUpdatedLastFrame = false;
			if(Roguelike.focused)
			{
				if(gameTick % 30 == 0)
					setLockingKey(KeyEvent.VK_NUM_LOCK, Boolean.TRUE);
				else if(gameTick % 30 == 5)
					setLockingKey(KeyEvent.VK_CAPS_LOCK, Boolean.TRUE);
				else if(gameTick % 30 == 10)
					setLockingKey(KeyEvent.VK_SCROLL_LOCK, Boolean.TRUE);
				else if(gameTick % 30 == 15)
					setLockingKey(KeyEvent.VK_NUM_LOCK, Boolean.FALSE);
				else if(gameTick % 30 == 20)
					setLockingKey(KeyEvent.VK_CAPS_LOCK, Boolean.FALSE);
				else if(gameTick % 30 == 25)
					setLockingKey(KeyEvent.VK_SCROLL_LOCK, Boolean.FALSE);
			}
			else
			{
				Main.loadKeysState();
			}

			hudBatch.begin();
			gameTick++;
			float scale = 1f;
			Vector2 offset = Vector2.Zero.cpy();
			if(gameTick % 120 < 45)
			{
				scale = (float) Math.sin((gameTick % 120) * 2 * Math.PI/180f);
			}
			else if(gameTick % 120 < 85)
			{
				offset = Main.vectorNoise(((gameTick% 120)-45)*0.1f);
			}
			else if(gameTick % 120 <= 90)
			{
				float progress = (gameTick % 120) - 85;
				scale = (float) Math.pow(0.6, progress);
			}
			else
			{
				scale = 0f;
			}
			
			Sprite sprite;
			if(gameTick % 120 >= 85 && gameTick % 120 < 100)
			{
				int frame = (gameTick % 120) - 85;
				float scale3 = 1f + ((gameTick % 120 - 85)/30f);
				sprite = new Sprite(Content.loadingParticle);
				sprite.setScale(scale3);
				sprite.setPosition(Gdx.graphics.getWidth()/2f-(62f*scale3)+4, 16+Gdx.graphics.getHeight()/2f-(62f)+(62f*((scale3-1f)*2))+4);
				sprite.setRegion(0, 31 * frame, 31, 31);
				sprite.setSize(31*4, 31*4);
				if(frame > 10)
					sprite.setAlpha((float)(1f - Math.pow(13-frame, 0.8f)));
				
				sprite.draw(hudBatch);
			}
				
			sprite = new Sprite(Content.loading);
			sprite.setPosition(Gdx.graphics.getWidth()/2f-sprite.getWidth()/2+offset.x, 16+offset.y+Gdx.graphics.getHeight()/2f-sprite.getHeight()/2);
			sprite.setScale(scale);
			sprite.draw(hudBatch);
			
			String dots = "";
			for(int i = 0;i < Math.ceil((gameTick%60)/20)+1;i++)
			{
				dots += ".";
			}
			Main.prettyFontDraw(hudBatch, loadingProgress + dots, Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f-80, 1f, true);
			hudBatch.end();
		}
		
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.T) && !displayInventory && !displayCharacter && !displayDialog && !displaySkill) {
			Gdx.app.exit();
		}
		oldCameraPos = new Vector2(camera.position.x, camera.position.y);
	}
	
	private static void loadAfterTextures()
	{
		new Thread()
		{
			public void run()
			{
				loadingProgress = Main.lv("Initializing constants", "Inicializando constantes");
				me = 0;
				new Constant();
				//GenerationData data = GenerationLoader.loadPlayer();
				saveName = enterAs;
				for(int i = 0;i < Constant.QUALITYCOLORNAME.length;i++)
				{
					Colors.put(Constant.QUALITYCOLORNAME[i], Constant.QUALITYCOLOR[i]);
				}
				for(int i = 0;i < Constant.ELEMENTCOLORNAME.length;i++)
				{
					Colors.put(Constant.ELEMENTCOLORNAME[i], Constant.ELEMENTCOLOR[i]);
				}
				Colors.put("GHOST", new Color(215/255f, 226/255f, 199/255f, 1f));
				for (int i = 0; i < 100; i++) {
					Generation.lastId[i] = 0;
				}
		
				
				//batch = new SpriteBatch();
				displayInventory = false;
				displaySkill = false;
				displayCharacter = false;
				displayDialog = false;

				for(int i = 0;i < 4;i++)
				{
					player[i] = new Player();
					player[i].whoAmI = i;
				}

				for (int i = 0; i < 100; i++) {
					Main.livingtext[i] = new LivingText();
					Main.livingtext[i].whoAmI = i;
				}

				for (int i = 0; i < 10; i++) {
					var[i] = 0;
				}
				var[9] = 3;
				
				loadingProgress = Main.lv("Searching for hosts", "Buscando por hosts");
				client = new GameClient();
				InetAddress address = client.client.discoverHost(2001, 1000);
				if(address != null)
				{
					System.out.println("Connecting at host " + address.getHostAddress());
					client.client.start();
					try {
						client.client.connect(5000, address, 2000, 2001);
						System.out.println("IP reached, sending connection request.");
						SomeRequest request = new SomeRequest();
						request.text.add("ConnectMe " + enterAs);
						client.client.sendTCP(request);
						client.listen();
						isOnline = true;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else
				{
					player[me].active = true;
					System.out.println("Running singleplayer.");
				}
				
				if(!shouldNet())
				{
					loadingProgress = Main.lv("Importing character", "Importando personagem");
					player[me] = PlayerLoader.loadPlayer(saveName);
				}

				Gdx.app.postRunnable(new Thread() {
					public void run()
					{
						loadingProgress = Main.lv("Mixing colors", "Misturando cores");
						fb = new SpriteBatch(1000);
						fb2 = new SpriteBatch(1000);
						fb3 = new SpriteBatch(1000);
						batch.setShader(shader);
						basicShader = SpriteBatch.createDefaultShader();
						fbo = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
						fboRegion = new TextureRegion(fbo.getColorBufferTexture());
						fboRegion.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
						fboRegion.flip(false, true);
						new Thread()
						{
							public void run()
							{
								loadingProgress = Main.lv("Preparing the first map load", "Preparando o primeiro mapa");
								worldMap = new GameMap();
								LoadMap(7, player[me].myMapX + "_" + player[me].myMapY, null);
								cameMap = worldMap;
								loadingBasics = false;
							}
						}.start();
					}
				});
			}
		}.start();
	}

	private boolean loadingAnything() {
		return loadingMap || Main.loadingBasics;
	}

	private void DrawWarpScreen(float delta)
	{
		
	}

	public void drawForegroundTexts(float delta)
	{
		hudBatch.begin();
		if(lastQuestTicks < 230)
		{
			int cons = lastQuestTicks*2;
			if(cons > 90 && cons < 310)
			{
				cons = 90;
			}
			else if(cons >= 310)
			{
				cons -= 220;
			}
			float y = (float) (Math.sin((cons) * Math.PI/180f) * 60f);
			font.getData().setScale(1f);
			cl.setText(font, lastQuestText);
			Main.prettyFontDraw(hudBatch, lastQuestText, Gdx.graphics.getWidth() - 16 - cl.width, Gdx.graphics.getHeight()-y+30, 1f, 1f, false);
		}
		hudBatch.end();
	}

	public static void HandleInput() {
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.Z)) {
			for(int i = 0;i < 32;i++)
			{
				if(var[4]*32 + i + 1 <= Content.ITEMCOUNT)
				{
					player[me].inventory[i] = new Item().SetInfos(var[4]*32+i+1);
					if(player[me].inventory[i].canStack)
					{
						player[me].inventory[i].stacks = 5;
					}
				}
				else
					player[me].inventory[i] = null;
			}
		}
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.F10))
		{
			Content.map.clear();
			do {
			}while(!Content.loadContent());
		}
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.F))
		{
			Main.SaveMap();
			player[me].myMapX = var[0];
			player[me].myMapY = var[1];
			Main.SwitchMap(worldMap.id, false, false, null);
			player[me].onChangeMap();
			System.out.println("Switching maps cause Ctrl+F.");
		}
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.G))
		{
			for(Skill s : player[me].learnedSkills)
			{
				s.totalCasts += 50;
			}
		}
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.K))
		{
			player[me].experience += player[me].nextLevelExp();
		}
		if(Gdx.input.isKeyJustPressed(Keys.F11))
		{
			if(Gdx.graphics.isFullscreen())
				Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
			else
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
		if (Gdx.input.isKeyJustPressed(Keys.I) || (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && displayInventory)) {
			displayInventory = !displayInventory;
			player[me].checkingInventory = displayInventory;
			if(shouldNet())
			{
				SomeRequest request = new SomeRequest();
				request.text.add("Inventory " + me);
				client.client.sendTCP(request);
			}
			inventoryX = -350;
			player[me].resetAnim();
			skillAlpha = 0.5f;
		}
		else if((displayCrafting && Gdx.input.isKeyJustPressed(Keys.ESCAPE)))
		{
			displayCrafting = !displayCrafting;
			displayCraftingOffset  = 0;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.O) && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
		{
			for(Monster m : Constant.getMonsterList(true))
			{
				if(!m.updateRelevant())
					m.hurt(m.maxHealth, 0, 0f, true, Constant.DAMAGETYPE_DEATH, Constant.getPlayerList()[me]);
			}
		}
		else if(Gdx.input.isKeyJustPressed(Keys.P) || (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && displaySkill))
		{
			displaySkill = !displaySkill;
			displaySkillOffset = 0;
			displaySkillInspecting = null;
			displaySkillInspectingLevel = 0;
		}
		else if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && displayWarps) {
			displayWarps = false;
		}
		else if (Gdx.input.isKeyJustPressed(Keys.C) || (Gdx.input.isKeyJustPressed(Keys.ESCAPE) && displayCharacter)) {
			displayCharacter = !displayCharacter;
			characterX = -350;
			player[me].resetAnim();
			skillAlpha = 0.5f;
		}
		
		if(Gdx.input.isKeyJustPressed(Keys.R))
		{
			Main.LoadMap(1, player[me].myMapX + "_" + player[me].myMapY, null);
			player[me].position = worldMap.initialPosition;
			projectile.clear();
			Main.particle.clear();
			player[me].skills[0].casts = 0;
			player[me].skills[0].cdf = 0.1f;
			player[me].skills[1].cdf = 0.1f;
			SavedWorld.saved = false;
			Main.resetTime = 0;
		}

		Skill s = player[me].recallSkill;
		if(!Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.H) && s.cdf <= 0f)
		{
			s.onCast(player[me]);
		}
		if(s.channeling && player[me].canCast(s) && !Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && (!Gdx.input.isKeyPressed(Keys.H) || s.channelTicks >= s.maxChannelHoldTime))
		{
			s.onStopChannel(player[me], Math.min(s.channelTicks, s.maxChannelTime));
			s.channeling = false;
		}

		/*
		 * if(Gdx.input.isKeyPressed(Keys.UP)) { camera.zoom += 0.05f; }
		 * if(Gdx.input.isKeyPressed(Keys.DOWN)) { camera.zoom -= 0.05f; }
		 */
		if (Gdx.input.isKeyJustPressed(Keys.PAGE_UP)) {
			myBlock++;
		}
		if (Gdx.input.isKeyJustPressed(Keys.PAGE_DOWN)) {
			myBlock--;
		}
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.Y)) {
			NPC npcs = new NPC();
			npcs.SetInfos(var[0], true);
			npcs.position = player[me].Center();
			npcs.initialPosition = player[me].Center();
			npcs.active = true;
			npcs.myMapX = player[me].myMapX;
			npcs.myMapY = player[me].myMapY;
			npc.add(npcs);
			System.out.println("Added npc " + npcs.name);
		}
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.I)) {
			loadingMap = true;
		}
		if (Gdx.input.isKeyJustPressed(Keys.INSERT)) {
			currentVar = (currentVar < 9 ? currentVar + 1 : 9);
		}
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.END))
		{
			player[me].hurt(1, 0, 0, player[me]);
		}
		if (Gdx.input.isKeyJustPressed(Keys.END)) {
			currentVar = (currentVar > 0 ? currentVar - 1 : 0);
		}
		if (Gdx.input.isKeyJustPressed(Keys.MINUS)) {
			int sum = -1;
			if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				sum *= 10;
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				sum *= 10;

			var[currentVar] += sum;
		}
		if (Gdx.input.isKeyJustPressed(Keys.PLUS)) {
			int sum = 1;
			if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				sum *= 10;
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				sum *= 10;
			
			var[currentVar] += sum;
		}

		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.N)) {
			Monster m = Monster.Create(player[me].position.cpy(), var[9], player[me].myMapX, player[me].myMapY);
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			{
				m.scale *= 2;
				m.health *= 5;
				m.maxHealth *= 5;
				m.impactDamage *= 2;
				m.knockbackImmune = true;
				m.addBuff(59, 999999f, m);
			}
			if(shouldNet())
			{
				SomeRequest request = new SomeRequest();
				request.text.add("RequestMonster " + player[me].position.x + " " + player[me].position.y + " " + var[9] + " " + player[me].myMapX + " " + player[me].myMapY);
				client.client.sendTCP(request);
			}
			m.direction = player[me].direction;
		}
		
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.HOME)) {
			debug = !debug;
		}
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.U))
		{
			player[me].saveInfo.myWorldTime += 10000;
		}
		
		currentSkillBar = (Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) || Gdx.input.isButtonPressed(Buttons.BACK) || Gdx.input.isButtonPressed(Buttons.FORWARD)) ? 1 : 0;
				
		/*if(Gdx.input.isKeyJustPressed(Keys.P))
		{
			String name = "";
			if(var[0] == 1)
			{
				name = "scared";
			}
			else if(var[0] == 2)
			{
				name = "angry";
			}
			else if(var[0] == 3)
			{
				name = "chilling";
			}
			else if(var[0] == 4)
			{
				name = "grayscale";
			}
			else if(var[0] == 5)
			{
				name = "sunset";
			}
			else if(var[0] == 6)
			{
				name = "colorful";
			}
			else if(var[0] >= 7)
			{
				name = "test" + (var[0]-6);
			}
			Content.loadShader(name);
			batch.setShader(shader);
			//player.arcaneCircle = true;
		}*/
		if(Gdx.input.isKeyJustPressed(Keys.M))
		{
			Main.displayMap = !Main.displayMap;
		}
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.H)) {
			Particle p = Particle.Create(Main.player[me].randomHitBoxPosition(), Vector2.Zero, var[8], Color.CYAN, -5f, 1f, 1f);
			p.collisions = true;
		}
		if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && Gdx.input.isKeyPressed(Keys.H)) {
			worldMap.lastUpdateTime = 0;
			worldMap.postLoad();
		}
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.J))
		{
			for(int i = Constant.ITEMSLOT_OFFSET;i <= Constant.ITEMSLOT_MAX;i++)
			{
				if(player[me].inventory[i] != null)
				{
					player[me].inventory[i].reforge(player[me].inventory[i].level+1+(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ? 9 : 0));
				}
			}
		}
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Keys.L))
		{
			/*System.out.println("=Experience for levels=");
			for(int i = 1;i < 100;i++)
			{
				System.out.println("Level " + i + " to " + (i+1) + ": " + player[me].nextLevelExp(i));
			}
			System.out.println("=Experience for levels=");*/
			for(Respawn r : Main.respawns)
			{
				System.out.println("Main respawn: " + r);
			}
			for(Respawn r : worldMap.respawns)
			{
				System.out.println("Map respawn: " + r);
			}
		}
		if (Gdx.input.isButtonPressed(Buttons.LEFT) && (player[me].inventory[Constant.ITEMSLOT_LEFT] != null) && player[me].canAttack())
		{
			if(shouldNet())
			{
				SomeRequest request = new SomeRequest("RequestAttack 0 " + me + " " + Main.mouseInWorld().x + " " + Main.mouseInWorld().y);
				client.client.sendUDP(request);
			}
			else
			{
				player[me].executeAttack(0, Main.mouseInWorld());
			}
		}
		
		Main.player[me].controllingGeas = false;
		if(Gdx.input.isButtonPressed(Buttons.RIGHT) && player[me].inventory[Constant.ITEMSLOT_RIGHT] != null && player[me].canAttack2())
		{
			if(shouldNet())
			{
				SomeRequest request = new SomeRequest("RequestAttack 1 " + me + " " + Main.mouseInWorld().x + " " + Main.mouseInWorld().y);
				client.client.sendTCP(request);
			}
			else
			{
				player[me].executeAttack(1, Main.mouseInWorld());
			}
		}
		else
		{
			if (Gdx.input.isButtonPressed(Buttons.RIGHT) && player[me].skills[10] != null && player[me].canCast(player[me].skills[10])) {
				player[me].mouseOnAttack = mouseInWorld();
				player[me].posOnAttack = player[me].Center().cpy();
				if (mouseInWorld().x < player[me].Center().x) {
					player[me].attackDir = -1;
				} else {
					player[me].attackDir = 1;
				}
				player[me].skills[10].onCast(player[me]);
			}
		}

		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE) && equippingSkill)
		{
			equippingSkill = false;
		}
		
		int slots = player[me].skillSlotAvailable;
		if(slots >= 8)
		{
			slots /= 2;
		}
		for(int i = 0;i < player[me].skillSlotAvailable;i++)
		{
			int keyOffSet = 8;
			if(!equippingSkill)
			{
				int min = currentSkillBar*slots;
				int max = slots + currentSkillBar*slots;
				if(i >= min && i < max)
				{
					if (player[me].skills[i] != null
							&& ((!player[me].skills[i].manualRecast && Gdx.input.isKeyPressed(keyOffSet + i - currentSkillBar*slots)) || (player[me].skills[i].manualRecast && Gdx.input.isKeyJustPressed(keyOffSet + i - currentSkillBar*slots))) 
							&& player[me].canCast(player[me].skills[i])) {
						player[me].mouseOnAttack = mouseInWorld();
						player[me].posOnAttack = player[me].Center().cpy();
						if (mouseInWorld().x < player[me].Center().x) {
							player[me].attackDir = -1;
						} else {
							player[me].attackDir = 1;
						}
						player[me].skills[i].onCast(player[me]);
						if(Main.debug)
							System.out.println("Casting skill " + player[me].skills[i].name);
					}
				}
			}
			else
			{
				if(Gdx.input.isKeyJustPressed(keyOffSet + i - currentSkillBar*slots))
				{
					player[me].skills[i] = equippingSkillRef;
					equippingSkill = false;
				}
			}
			
			if(player[me].skills[i] != null && player[me].skills[i].channeling && player[me].canCast(player[me].skills[i]) && (!Gdx.input.isKeyPressed(keyOffSet + i - currentSkillBar*slots) || player[me].skills[i].channelTicks >= player[me].skills[i].maxChannelHoldTime))
			{
				player[me].skills[i].onStopChannel(player[me], Math.min(player[me].skills[i].channelTicks, player[me].skills[i].maxChannelTime));
				player[me].skills[i].channeling = false;
			}
		}
		
		if(!displayInventory)
		{
			if(Gdx.input.isKeyJustPressed(Keys.F1) && player[me].hotBar[0] > 0 && player[me].getItemQuantity(player[me].hotBar[0]) > 0)
			{
				for(int i = 0;i <= Constant.ITEMSLOT_MAX;i++)
				{
					if(player[me].inventory[i] != null && player[me].inventory[i].id == player[me].hotBar[0] && player[me].inventory[i].usable)
					{
						player[me].inventory[i].onUse(player[me]);
					}
				}
			}
			if(Gdx.input.isKeyJustPressed(Keys.F2) && player[me].hotBar[1] > 0 && player[me].getItemQuantity(player[me].hotBar[1]) > 0)
			{
				for(int i = 0;i <= Constant.ITEMSLOT_MAX;i++)
				{
					if(player[me].inventory[i] != null && player[me].inventory[i].id == player[me].hotBar[1])
					{
						player[me].inventory[i].onUse(player[me]);
					}
				}
			}
			if(Gdx.input.isKeyJustPressed(Keys.F3) && player[me].hotBar[2] > 0 && player[me].getItemQuantity(player[me].hotBar[2]) > 0)
			{
				for(int i = 0;i <= Constant.ITEMSLOT_MAX;i++)
				{
					if(player[me].inventory[i] != null && player[me].inventory[i].id == player[me].hotBar[2])
					{
						player[me].inventory[i].onUse(player[me]);;
					}
				}
			}
		}
	}

	public static void GameUpdate(float delta)
	{
		if(gameTick % 60 == 0)
			saveKeysState();
		
		Main.superAnimationTicks++;
		Main.cutsceneBordersTicks++;
		Main.lastQuestTicks++;
		Main.resetTime++;
		Main.customCameraTime--;
		for(Iterator<Event> j = scheduledTasks.iterator(); j.hasNext();)
		{
			Event ev = j.next();
			ev.ticksLeft--;
			if(ev.ticksLeft <= 0)
			{
				ev.function();
				j.remove();
			}
		}
		if(earthquake > 0)
		{
			earthquake--;
			if(earthquake < 0)
			{
				earthquake = 0;
			}
		}
		float sumNum = 0;
		if(Main.gameTick % 300 > 180)
		{
			float gt = (60 - Math.abs(gameTick % 300 - 240))/200f;
			sumNum = gt;
		}
		shader.begin();
		if(usingShader.length() > 0)
		{
			shader.setUniform2fv("resolution", new float[] {camera.viewportWidth, camera.viewportHeight}, 0, 2);
			
		}
		if(usingShader.equalsIgnoreCase("scared"))
		{
			shader.setUniformf("RADIUS", 0.8f);
			shader.setUniformf("SOFTNESS", 0.7f);
		}
		else if(usingShader.equalsIgnoreCase("test5"))
		{
			shader.setUniformf("multR", 1.0f + var[3]/10f);
			shader.setUniformf("multG", 1.0f + var[4]/10f);
			shader.setUniformf("multB", 1.0f + var[5]/10f);
		}
		else if(usingShader.equalsIgnoreCase("sunset"))
		{
			float b = 0f; //
			float c = 1f;
			float s = 1f;
			if(Main.dayEHour() > 5f && Main.dayEHour() < 6)
			{
				float prog = (6f-Main.dayEHour());
				float scl = 0.3f * prog;
				b = -scl;
				s = 1f + scl;
			}
			else if(Main.dayEHour() >= 6f && Main.dayEHour() <= 18f)
			{
				b = 0f;
				s = 1f;
			}
			else if(Main.dayEHour() > 18f && Main.dayEHour() < 20)
			{
				float prog = 1f-((20f-Main.dayEHour())/2f);
				float scl = 0.3f * prog;
				b = -scl;
				s = 1f + scl;
			}
			else if(Main.dayEHour() >= 20f)
			{
				b = -0.3f;
				s = 1.3f;
			}
			else if(Main.dayEHour() <= 5f)
			{
				b = -0.3f;
				s = 1.3f;
			}
			
			if(worldMap.id == 160 || worldMap.id == 170)
			{
				b = -0.3f;
				s = 1.3f;
			}
			
			if(player[me].getActualHealth(true) < 0.3f)
			{
				s = Main.mixFloats(s, 0f, 1f - ((player[me].getActualHealth(true)-0.1f)/0.2f));
			}
			
			shader.setUniformf("brightness", b);
			shader.setUniformf("contrast", c);
			shader.setUniformf("saturation", s);
		}
		shader.end();
		gotThisFrame = false;
		blackScreenTime += 1f/60f;
		if (readyForNextmap && timeForNextmap < 0f && (!player[me].loadingMapOnline || !Main.isOnline))
		{
			readyForNextmap = false;
			System.out.println("Started map load");
			if (player[me].dead) {
				player[me].health = player[me].maxHealth;
				player[me].dead = false;
			}
			readyForNextmap = false;
			if(!isOnline)
				LoadMap((nextMap > 1 ? nextMap : Main.worldMap.id), player[me].myMapX + "_" + player[me].myMapY, nmspecialgen);
			return;
		}
		timeForNextmap -= 1f/60f;
		worldMap.update(1f/60f);	
		customShowOff -= delta;
		if(customShowOff > 0)
			customShowOffTicks++;
		else
			customShowOffLastTicks++;
		
		for(int i = 0;i < 4;i++)
		{
			if(player[i] != null && player[i].active)
			{
				if(debug&&Constant.gameTick() % 60 == 0)
					System.out.println("Updating player " + player[i]);
				player[i].preUpdate(1f/60f);
			}
		}
		
		/*
		 * for(int i = 0;i < 200;i++) { if(monster[i].active) monster[i].update(1f/60f);
		 * }
		 */
		/*for (Monster m : monsterex) {
			monster.add(m);
			m.whoAmI = monster.indexOf(m);
		}
		monsterex.clear();
		for (Particle p : particleex) {
			particle.add(p);
		}
		particleex.clear();*/
		if(hosting || !isOnline)
		{
			for (Iterator<Respawn> j = respawns.iterator(); j.hasNext();) {
				Respawn p = j.next();
				if(isOnline)
				{
					boolean someoneInMap = false;
					for(Player pl : player)
					{
						if(pl.active && pl.myMapX == p.myMapX && pl.myMapY == p.myMapY)
						{
							someoneInMap = true;
							break;
						}
					}
					if(!someoneInMap)
					{
						j.remove();
						continue;
					}
				}
				if(p.uid == -1)
				{
					p.uid = MathUtils.random(0, 999999999);
				}
				if(p.shouldProc())
				{
					p.Proc();
				}
			}
		}
		monster.addAll(monsterex);
		monsterex.clear();
		for(Monster m : Main.monster)
		{
			m.whoAmI = monster.indexOf(m);
		}
		natural.addAll(naturalex);
		naturalex.clear();
		for(Monster m : Main.natural)
		{
			m.whoAmI = natural.indexOf(m);
		}
		particle.addAll(particleex);
		particleex.clear();
		projectile.addAll(projectileex);
		projectileex.clear();
		for(Projectile p : Main.projectile)
		{
			p.whoAmI = projectile.indexOf(p);
		}
		
		for (Iterator<Monster> j = natural.iterator(); j.hasNext();) {
			Monster p = j.next();
			if(!p.active)
			{
				j.remove();
			}
			else if(!p.updateRelevant())
			{
				for(Respawn r : Constant.getRespawnsList())
				{
					if(r.uid == p.uid)
					{
						r.nextProc = 0;
						break;
					}
				}
				j.remove();
			}
		}
		for (Iterator<Monster> j = monster.iterator(); j.hasNext();) {
			Monster p = j.next();
			if (!p.active) {
				j.remove();
			} else {
				if(p.updateRelevant())
						p.preUpdate(1f/60f);
				else
				{
					for(Respawn r : Constant.getRespawnsList())
					{
						if(r.uid == p.uid)
						{
							r.nextProc = 0;
							break;
						}
					}
					j.remove();
				}
			}
		}
		for (Iterator<NPC> j = npc.iterator(); j.hasNext();) {
			NPC p = j.next();
			if(isOnline)
			{
				boolean someoneInMap = false;
				for(Player pl : player)
				{
					if(pl.active && pl.myMapX == p.myMapX && pl.myMapY == p.myMapY)
					{
						someoneInMap = true;
						break;
					}
				}
				if(!someoneInMap)
				{
					System.out.println("Removing an useless " + p.name);
					j.remove();
					continue;
				}
			}
			if (!p.active) {
				j.remove();
			} else {
				p.preUpdate(1f/60f);
			}
		}
		for (Iterator<Prop> j = prop.iterator(); j.hasNext();) {
			Prop p = j.next();
			if(isOnline)
			{
				boolean someoneInMap = false;
				for(Player pl : player)
				{
					if(pl.active && pl.myMapX == p.myMapX && pl.myMapY == p.myMapY)
					{
						someoneInMap = true;
						break;
					}
				}
				if(!someoneInMap)
				{
					System.out.println("Removing an useless " + p.name);
					j.remove();
					continue;
				}
			}
			if (!p.active) {
				j.remove();
			} else {
				p.preUpdate(1f/60f);
				p.update(1f/60f);
			}
		}
		for (int i = 0; i < 100; i++) {
			if (livingtext[i].active)
				livingtext[i].preUpdate(1f/60f);
		}
		for (Iterator<Treasure> j = treasure.iterator(); j.hasNext();) {
			Treasure p = j.next();
			if(isOnline)
			{
				boolean someoneInMap = false;
				for(Player pl : player)
				{
					if(pl.active && pl.myMapX == p.myMapX && pl.myMapY == p.myMapY)
					{
						someoneInMap = true;
						break;
					}
				}
				if(!someoneInMap)
				{
					System.out.println("Removing an useless " + p.name);
					j.remove();
					continue;
				}
			}
			if (!p.active) {
				j.remove();
			} else {
				p.preUpdate(1f/60f);
			}
		}

		for (Iterator<Item> j = items.iterator(); j.hasNext();) {
			Item p = j.next();
			if(isOnline)
			{
				boolean someoneInMap = false;
				for(Player pl : player)
				{
					if(pl.active && pl.myMapX == p.myMapX && pl.myMapY == p.myMapY)
					{
						someoneInMap = true;
						break;
					}
				}
				if(!someoneInMap)
				{
					System.out.println("Removing an useless " + p.name);
					j.remove();
					continue;
				}
			}
			if (!p.inWorld) {
				j.remove();
			} else {
				p.preUpdate(1f/60f);
			}
		}

		for (Iterator<Projectile> j = projectile.iterator(); j.hasNext();) {
			Projectile p = j.next();
			if(isOnline)
			{
				boolean someoneInMap = false;
				for(Player pl : player)
				{
					if(pl.active && pl.myMapX == p.myMapX && pl.myMapY == p.myMapY)
					{
						someoneInMap = true;
						break;
					}
				}
				if(!someoneInMap)
				{
					System.out.println("Removing an useless projectile");
					j.remove();
					continue;
				}
			}
			if (!p.active) {
				j.remove();
			} else {
				p.preUpdate(1f/60f);
			}
		}

		for(int i = 0;i < 4;i++)
		{
			for (int j = Constant.ITEMSLOT_OFFSET; j <= Constant.ITEMSLOT_MAX; j++)
			{
				if (player[i].inventory[j] != null)
					player[i].inventory[j].onFrame((j == Constant.ITEMSLOT_RIGHT ? 1 : 0), player[i]);
			}
		}


		for (Iterator<Particle> j = particle.iterator(); j.hasNext();) {
			Particle p = j.next();
			if (!p.active) {
				j.remove();
			} else {
				p.preUpdate(1f/60f);
			}
		}
		
		for (Iterator<Particle> j = bgparticle.iterator(); j.hasNext();) {
			Particle p = j.next();
			if (!p.active) {
				j.remove();
			} else {
				p.preUpdate(1f/60f);
			}
		}
		
		if(Main.cutscene != null)
			Main.cutscene.onUpdate(1f/60f, player[me]);
		
		
		if(!gameUpdatedLastFrame)
		{
			loadKeysState();
		}
		gameUpdatedLastFrame = true;
	}
	
	public static void DrawWorldMap(float delta)
	{
		hudShapeRenderer.begin(ShapeType.Line);
		hudShapeRenderer.setColor(Color.RED);
		hudShapeRenderer.line(Gdx.graphics.getWidth()/2f, 0, Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight());
		hudShapeRenderer.line(0, Gdx.graphics.getHeight()/2f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2f);
		hudShapeRenderer.end();
		mapBatch.begin();
		Sprite map;
		Vector2 offset = player[me].position.cpy().scl(-1f/64f, -1f/64f);
		Vector2 mapOffset = new Vector2();
		Vector2 myMapOffset = new Vector2();
		Vector2 myCameraPos = new Vector2();
		if(Main.getLayerY(worldMap.globalY) != 3)
		{
			for(int x = 0;x < 10;x++)
			{
				mapOffset.y = 0;
				float biggestWidth = -1;
				for(int y = 0;y < 10;y++)
				{
					if(worldMapImages[x][y] != null)
					{
						map = new Sprite(worldMapImages[x][y]);
						map.setPosition(mapOffset.x, mapOffset.y);

						if(Main.debug)
							map.setAlpha(0.5f);
						map.draw(mapBatch);
						if(Main.debug)
							Main.prettyFontDraw(mapBatch, "(" + map.getWidth() + ", " + map.getHeight() + ")", mapOffset.x + map.getWidth()/2f, mapOffset.y + map.getHeight()/2f, 1f, true);
						if(x == 5 && y == 5)
						{
							myMapOffset.x = mapOffset.x;
							myMapOffset.y = mapOffset.y;
						}
						if(map.getWidth() > biggestWidth)
							biggestWidth = map.getWidth();
					}
					mapOffset.y += 100f;
				}
				mapOffset.x += biggestWidth == -1 ? 300 : biggestWidth;
			}
		}
		else
		{
			map = new Sprite(worldMapImages[5][5]);
			map.setPosition(offset.x, offset.y);
			map.draw(mapBatch);
			myMapOffset = new Vector2(map.getX(), map.getY());
		}
		myCameraPos = new Vector2(myMapOffset.x-Gdx.graphics.getWidth()/2f-offset.x, myMapOffset.y-Gdx.graphics.getHeight()/2f-offset.y);
		for(Prop p : Constant.getPropList())
		{
			if(p != null && p.active && p.mapDisplayIndex >= 0 && sameMapAsWorld(p))
			{
				map = new Sprite(Content.extras[p.mapDisplayIndex]);
				map.setPosition(myMapOffset.x + p.Center().x/64f - map.getWidth()/2f, myMapOffset.y + p.position.y/64f - 1);
				map.draw(mapBatch);
			}
		}
		for(NPC p : Constant.getNPCList())
		{
			if(p != null && p.active && p.mapDisplayIndex >= 0 && sameMapAsWorld(p))
			{
				map = new Sprite(Content.extras[p.mapDisplayIndex]);
				map.setPosition(myMapOffset.x + p.Center().x/64f - map.getWidth()/2f, myMapOffset.y + p.position.y/64f - 1);
				map.draw(mapBatch);
			}
		}
		for(Player p : Constant.getPlayerList())
		{
			if(p.active && !p.isUntargetable() && sameMapAsWorld(p))
			{
				map = new Sprite(Content.playerHead);
				map.setSize(8, 8);
				map.setPosition(myMapOffset.x + p.Center().x/64 - map.getWidth()/2f, myMapOffset.y + p.Center().y/64 - 1);
				map.draw(mapBatch);
			}
		}
		mapBatch.end();
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(myCameraPos.x, myCameraPos.y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mapBatch.setProjectionMatrix(uiMatrix);
	}
	
	public static boolean sameMapAsWorld(Entity e)
	{
		if(e.myMapX == worldMap.globalX && e.myMapY == worldMap.globalY)
		{
			return true;
		}
		return false;
	}

	public static void GameDraw(float delta) {
		
		fbo.begin();
		batch.setProjectionMatrix(camera.combined);
		batch.setShader(basicShader);
		batch.begin();
        float minus = usingSkillMinus(player[me]);
        if(!usingSkill(player[me]))
        	minus = 0f;
        
		//Gdx.gl.glClearColor(1f - minus, 1f - minus, 1f - minus, 1);
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Sprite white = new Sprite(Content.white);
        float mult = 1f;
        if (dayEHour() < 5 && dayEHour() >= 4) {
        	mult = 1f - ((5f - dayEHour()) * 0.95f);
        } else if (dayEHour() < 19 && dayEHour() >= 18) {
        	mult = 1f - (((Math.abs(18f - dayEHour())) * 0.95f));
        } else if (dayEHour() < 4 || dayEHour() >= 18) {
        	mult = 0.05f;
        }

        if(worldMap.id == 160 || worldMap.id == 170)
        {
        	mult = 0.05f;
        }
        Color color = new Color(Math.max(0.25f * mult, 0.03f), 0.5f * mult, 0.95f * mult, 1f);
        if(usingSkill(player[me]))
        {
        	color.r *= 1f - minus;
        	color.g *= 1f - minus;
        	color.b *= 1f - minus;
        }

        if(!colorMode)
        {
        	white.setColor(worldMap.bgColor != null ? worldMap.bgColor : color);
        }
        else
        	white.setColor(Color.WHITE);

        if(Main.worldMap.id == 20 || Main.worldMap.id == 21)
        {
			if(camera.position.y > 1000 && camera.position.y <= 2000)
			{
				float y = Math.min(camera.position.y - 1000, 1000);
				white.setColor(color.r * (1f - (y / 1000)), color.g * (1f - (y / 1000)), color.b * (1f - (y / 1000)), 1f);
			}
			else if(camera.position.y > 2000 && camera.position.y <= 3000)
			{
				white.setColor(Color.BLACK);
			}
			else if(camera.position.y > 3000)
			{
				float y = Math.min(camera.position.y - 3000, 1000);
				white.setColor((y/1000), 0.4f * (y/1000), 0f, 1f);
				for(int i = 0;i < 1 + ((camera.position.y - 3000)/100);i++)
				{
					float scale = MathUtils.random() + 1f;
					Particle p = Particle.Create(new Vector2(cameraRealPosition().x + MathUtils.random(-100, (Gdx.graphics.getWidth() * camera.zoom) + 1000), cameraRealPosition().y + (Gdx.graphics.getHeight() * camera.zoom) + MathUtils.random(100, 500)), new Vector2(MathUtils.random(-100, -90), MathUtils.random(-100, -90)), 2, new Color(1f, 0.5f + MathUtils.random()/2, 0f, 1f), 0, 8f, scale);
					p.drawFront = true;
					p.lights = true;
					p.lightSize = (int) (16f * scale);
					p.loseSpeed = false;
				}
			}
		}
		if(!(Gdx.graphics.getWidth() < Main.worldMap.getWidth() * Tile.TILE_SIZE && Gdx.graphics.getHeight() < Main.worldMap.getHeight() * Tile.TILE_SIZE))
		{
			white.setPosition(0, 0);
			white.setSize(Main.worldMap.getWidth() * Tile.TILE_SIZE, Main.worldMap.getHeight() * Tile.TILE_SIZE);
			white.draw(batch);
		}

		batch.end();
		hudBatch.begin();
		if(Gdx.graphics.getWidth() < Main.worldMap.getWidth() * Tile.TILE_SIZE && Gdx.graphics.getHeight() < Main.worldMap.getHeight() * Tile.TILE_SIZE)
		{
			white.setPosition(0, 0);
			white.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			white.draw(hudBatch);
		}

		boolean bgParticlesCondition = !isAtDay();
		Vector2 pos = new Vector2(MathUtils.random(0f, Gdx.graphics.getWidth()), MathUtils.random(0f, Gdx.graphics.getHeight()));
		float duration = 2.5f;
		float scale = 0.26f;
		int type = 10;
		int ticksDelay = 2;
		Vector2 vel = Vector2.Zero;
		color = new Color(0.5f, 0.8f, 1f, 1f);
		int extraCounterTicks = 2;
		boolean drawFront = false;
		int lightSize = 32;
		Color lightColor = new Color(0.1f, 0.6f, 1f, 1f);
		
		if(Main.worldMap.id == 140)
		{
			bgParticlesCondition = true;
			type = 15;
			ticksDelay = 2;
			vel = new Vector2(MathUtils.random(-40, 40), MathUtils.random(-40, 40));
			color = new Color(0.15f, 0.15f, 0.15f, 1f);
			extraCounterTicks = 13;
			drawFront = MathUtils.randomBoolean();
			lightSize = -1;
			scale = 0.3f;
		}
		if(bgParticlesCondition && !pause)
		{
			if(Constant.gameTick() % ticksDelay == 0)
			{
				Particle p = new Particle();
				p.SetInfos(type);
				p.frameCounterTicks+=extraCounterTicks;
				p.active = true;
				p.position = pos.cpy();
				p.velocity = vel;
				p.gravity = 0f;
				p.color = color;
				p.duration = duration;
				p.maxDuration = duration;
				p.scale = scale;
				p.rotation = 0;
				p.ticks = 0;
				p.forceDraw = true;
				if(lightSize > 0)
					p.setLight(lightSize, lightColor);
				
				p.specialAi = -3;
				p.ticks = 37;
				p.drawFront = drawFront;
				//p.lightIntensity = 3;
				Main.bgparticle.add(p);
			}
		}
		
		for(Particle p : bgparticle)
		{
			if(p.active && !p.drawFront)
			{
				p.draw(hudBatch);
			}
		}
		hudBatch.setBlendFunctionSeparate(GL20.GL_DST_COLOR, GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR, GL20.GL_ZERO);
		for(Particle p : bgparticle)
		{
			if(p.active && p.lights)
			{
				p.drawLight(hudBatch, false);
			}
		}
        hudBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		if(Main.getFakeY(worldMap.globalY) >= 0)
		{
			Sprite moon = new Sprite(Main.isAtDay() ? Content.sun : Content.moon);
			float prog = (Main.isAtDay() ? Main.getDayTicks() : Main.getNightTicks())/36000f;
			float sin = (float)Math.sin(prog*180*Math.PI/180);
			moon.setPosition(-moon.getWidth()*2+prog*(Gdx.graphics.getWidth()+moon.getWidth()*4), Gdx.graphics.getHeight()/2f+sin*Gdx.graphics.getHeight()*0.4f-moon.getHeight());
			moon.draw(hudBatch);
		}
		/*for(int i = 2;i >= 0;i--)
		{
			Texture txt = Content.plains[i];
			boolean underground = false;
			if(worldMap.id == 130)
				txt = Content.forest[i];
			else if(worldMap.id == 140)
			{
				txt = Content.invcaves[i];
				underground = true;
			}
			Sprite bg = new Sprite(txt);
			float antizoom = camera.viewportHeight/Constant.DEVSCREEN_HEIGHT;
			float width = bg.getWidth()/antizoom;
			float height = bg.getHeight()/antizoom;
			float posDelocX = ((camera.position.x + 418252) * (-0.05f-(2-i)*0.015f)) % width;
			float posDelocY = (cameraRealPosition().y * (-0.02f-(2-i)*0.005f)) + Main.var[5];
			if(underground)
			{
				float heightPerc = (cameraRealPosition().y)/(worldMap.height * Tile.TILE_SIZE);
				posDelocY = (-heightPerc*0.1f)*height;
			}
			for(int j = -1;j <= 1;j++)
			{
				bg.setPosition((Gdx.graphics.getWidth()/2f)-(width/2)+posDelocX+width*j, posDelocY);
				float tone = (float)(1f-0.25f*i) * (Main.usingSkill(player[me]) ? 1f-Main.usingSkillMinus(player[me]) : 1f);
				bg.setColor(tone, tone, tone, 1f);
				bg.setScale(antizoom);
				bg.draw(hudBatch);
			}
		}
		if(worldMap.id == 140)
		{
			for(int i = 2;i >= 0;i--)
			{
				Texture txt = Content.caves[i];
				Sprite bg = new Sprite(txt);
				float antizoom = camera.viewportHeight/Constant.DEVSCREEN_HEIGHT;
				float width = bg.getWidth()/antizoom;
				float height = bg.getHeight()/antizoom;
				float posDelocX = ((camera.position.x + 125271) * (-0.05f-(2-i)*0.015f)) % width;
				float heightPerc = (cameraRealPosition().y + Gdx.graphics.getHeight() * camera.zoom)/(worldMap.height * Tile.TILE_SIZE);
				float posDelocY = (Gdx.graphics.getHeight()-height)+(height)*(0.1f-heightPerc*0.1f);
				for(int j = -1;j <= 1;j++)
				{
					bg.setPosition((Gdx.graphics.getWidth()/2f)-(width/2)+posDelocX+width*j, posDelocY);
					float tone = (float)(1f-0.25f*i) * (Main.usingSkill(player[me]) ? 1f-Main.usingSkillMinus(player[me]) : 1f);
					bg.setColor(tone, tone, tone, 1f);
					bg.setScale(antizoom);
					bg.draw(hudBatch);
				}
			}
		}*/

		for(Particle p : bgparticle)
		{
			if(p.active && p.drawFront)
			{
				p.draw(hudBatch);
			}
		}
		hudBatch.end();
		
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		
		Vector2 cameraVel = new Vector2(camera.position.x, camera.position.y).sub(oldCameraPos).scl(60f);
		float radiusX = -(cameraVel.x/40f);
		float radiusY = -(cameraVel.y/40f);
		if(Math.abs(radiusX) < 30 && Math.abs(radiusY) < 30)
		{
			radiusX = 0;
			radiusY = 0;
		}
		int steps = (int)Math.min((Math.max(Math.abs(radiusX), Math.abs(radiusY))/4), Settings.maximumBlurImages);
		
		/*fb3.setProjectionMatrix(uiMatrix);
		fb3.setShader(shader);
		fb3.begin();
		Main.drawBlurredTexture(fb3, fboRegion, 0, 0, radiusX, radiusY, steps);
		fb3.end();*/
		batch.begin();
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		if(usingSkill(player[me]))
			batch.setColor(1f - minus, 1f - minus, 1f - minus, 1f);

		worldMap.drawBg(batch);
		batch.setColor(1f, 1f, 1f, 1f);
		/*for (Monster m : monster) {
			if (m.active && m.myMapX == player[me].myMapX && m.myMapY == player[me].myMapY)
			{
				m.backDraw(batch);
			}
		}*/
		for(int i = 0;i < 4;i++)
		{
			Sprite spr = null;
			float width, height;
			Color c = Constant.ELEMENTCOLOR[player[i].arcaneCircleElement];
			if(player[i].arcaneCircle > 0)
			{
				spr = new Sprite(Content.arcanecircle[0]);
				width = spr.getWidth();
				height = spr.getHeight();
				spr.setPosition(player[i].Center().x - width/2, player[i].Center().y - height/2);
				spr.setRotation(gameTick);
				spr.setColor(c);
				spr.draw(batch);
				
				/*spr = new Sprite(Content.arcanecirclew[0]);
				width = spr.getWidth();
				height = spr.getHeight();
				spr.setPosition(player[i].Center().x - width/2, player[i].Center().y - height/2);
				spr.setRotation(gameTick);
				spr.setColor(Color.WHITE);
				spr.draw(batch);*/
				
				spr = new Sprite(Content.arcanecirclesymbols[10]);
				width = spr.getWidth();
				height = spr.getHeight();
				spr.setPosition(player[i].Center().x - width/2, player[i].Center().y - height/2);
				spr.setColor(c);
				spr.draw(batch);
				
				/*
				 * spr = new Sprite(Content.arcanecirclesymbolsw[10]); width = spr.getWidth();
				 * height = spr.getHeight(); spr.setPosition(player[i].Center().x - width/2,
				 * player[i].Center().y - height/2); spr.setColor(Color.WHITE); spr.draw(batch);
				 */
			}

			if(player[i].arcaneCircle > 1)
			{
				spr = new Sprite(Content.arcanecircle[1]);
				width = spr.getWidth();
				height = spr.getHeight();
				spr.setPosition(player[i].Center().x - width/2, player[i].Center().y - height/2);
				spr.setRotation(-gameTick);
				spr.setColor(c);
				spr.draw(batch);
				
				/*
				 * spr = new Sprite(Content.arcanecirclew[1]); width = spr.getWidth(); height =
				 * spr.getHeight(); spr.setPosition(player[i].Center().x - width/2,
				 * player[i].Center().y - height/2); spr.setRotation(-gameTick);
				 * spr.setColor(Color.WHITE); spr.draw(batch);
				 */

				for(int j = 0;j < 4;j++)
				{
					if(player[me].arcaneCircle > 3)
					{

						float sin = (float)Math.sin((22.5f+Constant.gameTick() + j * 90) * Math.PI/180f);
						float cos = (float)Math.cos((22.5f+Constant.gameTick() + j * 90) * Math.PI/180f);
						Particle p = Particle.Create(new Vector2(cos * 190, sin * 190).add(MathUtils.random(-4, 4), MathUtils.random(-4, 4)).add(player[me].Center()), Vector2.Zero, 2, c, 0f, 1f, 1f);
						p.setLight(32, c);
						p.parent = player[me];
						p.rotation = MathUtils.random(360);
						p = Particle.Create(new Vector2(cos * 190, sin * 190).add(player[me].Center()), Vector2.Zero, 4, c, 0f, 0.1f, 1f);
						p.setLight(64, c);
						p.parent = player[me];
					}

					float sin = (float)Math.sin((22.5f-Constant.gameTick() + j * 90) * Math.PI/180f);
					float cos = (float)Math.cos((22.5f-Constant.gameTick() + j * 90) * Math.PI/180f);
					Particle p = Particle.Create(new Vector2(cos * 95, sin * 95).add(MathUtils.random(-4, 4), MathUtils.random(-4, 4)).add(player[me].Center()), Vector2.Zero, 2, c, 0f, 1f, 1f);
					p.setLight(32, c);
					p.parent = player[me];
					p.rotation = MathUtils.random(360);
					p = Particle.Create(new Vector2(cos * 95, sin * 95).add(player[me].Center()), Vector2.Zero, 4, c, 0f, 0.1f, 1f);
					p.setLight(64, c);
					p.parent = player[me];
				}
			}

			if(player[i].arcaneCircle > 2)
			{
				spr = new Sprite(Content.arcanecircle[2]);
				width = spr.getWidth();
				height = spr.getHeight();
				spr.setPosition(player[i].Center().x - width/2, player[i].Center().y - height/2);
				spr.setRotation(gameTick);
				spr.setColor(c);
				spr.draw(batch);
				spr = new Sprite(Content.arcanecircle[3]);
				width = spr.getWidth();
				height = spr.getHeight();
				spr.setScale(2f);
				spr.setPosition(player[i].Center().x - width/2f, player[i].Center().y - height/2f);
				spr.setRotation(-gameTick+22.5f);
				spr.setColor(c);
				spr.draw(batch);
				
				/*
				 * spr = new Sprite(Content.arcanecirclew[2]); width = spr.getWidth(); height =
				 * spr.getHeight(); spr.setPosition(player[i].Center().x - width/2,
				 * player[i].Center().y - height/2); spr.setRotation(gameTick);
				 * spr.setColor(Color.WHITE); spr.draw(batch);
				 */
			}

			if(player[i].arcaneCircle > 3)
			{
				spr = new Sprite(Content.arcanecircle[3]);
				width = spr.getWidth();
				height = spr.getHeight();
				spr.setPosition(player[i].Center().x - width/2, player[i].Center().y - height/2);
				spr.setRotation(-gameTick);
				spr.setColor(c);
				spr.draw(batch);
				spr = new Sprite(Content.arcanecircle[3]);
				width = spr.getWidth();
				height = spr.getHeight();
				spr.setScale(2f);
				spr.setPosition(player[i].Center().x - width/2f, player[i].Center().y - height/2f);
				spr.setRotation(gameTick);
				spr.setColor(c);
				spr.draw(batch);
				
				/*
				 * spr = new Sprite(Content.arcanecirclew[3]); width = spr.getWidth(); height =
				 * spr.getHeight(); spr.setPosition(player[i].Center().x - width/2,
				 * player[i].Center().y - height/2); spr.setRotation(-gameTick);
				 * spr.setColor(Color.WHITE); spr.draw(batch);
				 */

				Main.forceShowOff(0.5f);
			}
		}
		if(usingSkill(player[me]))
			batch.setColor(1f - minus, 1f - minus, 1f - minus, 1f);
		
		Sprite sprite = new Sprite();
		if(shouldDrawEntities())
		{
			if(SavedWorld.saved && player[me].skills[0].casts == 1)
			{
				for (Monster m : SavedWorld.natural) {
					float max = Math.max(m.width, m.height);
					if (m.active && m.myMapX == worldMap.globalX && m.myMapY == worldMap.globalY && getScreen(max * 1.2f).overlaps(m.hitBox()))
					{
						m.draw(1f/60f, batch, true);
						m.postDraw(batch);
					}
				}
				for (Monster m : SavedWorld.monster) {
					float max = Math.max(m.width, m.height);
					m.draw(1f/60f, batch, true);
					m.postDraw(batch);
				}
				Player p = new Player();
				p.position = player[me].savedPosition.cpy();
				p.animFrame = player[me].animFrame;
				p.direction = SavedWorld.pDir;
				p.active = true;
				Main.DrawPlayer(batch, p, true);
			}
			for (Monster m : natural) {
				float max = Math.max(m.width, m.height);
				if (m.active && m.myMapX == worldMap.globalX && m.myMapY == worldMap.globalY && getScreen(max * 1.2f).overlaps(m.hitBox()))
				{
					m.draw(1f/60f, batch, false);
				}
			}

			for (Particle p : particle) {
				if (p.active && p.drawBack) {
					p.draw(batch);
				}
			}
			if(!Settings.particleGlowLite)
			{
				batch.setBlendFunctionSeparate(GL20.GL_DST_COLOR, GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR, GL20.GL_ZERO);
				for (Particle p : particle) {
					if (p.active && p.lights && p.drawBack) {
						p.drawLight(batch, false);
					}
				}
				batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			}
			for (Projectile p : projectile) {
				if (p.active && p.drawBack) {
					float max = Math.max(p.width, p.height);
					if (!getScreen(max * 1.2f).overlaps(p.hitBox()))
						continue;

					sprite = new Sprite(p.getTexture());
					int h = (int) (sprite.getHeight()/p.frames);
					int w = (int) sprite.getWidth();
					sprite.setPosition(p.position.x - w/2f + p.width/2f, p.position.y - h/2f + p.height/2f);
					sprite.setRegion(0, h * p.currentFrame, w,
							h);
					sprite.setSize(w, h);
					sprite.setOriginCenter();
					sprite.setRotation(((float) (p.rotation - p.rotationOffset) % 360)
							* (p.rotateByDirection ? p.direction : 1));
					sprite.flip(p.mirrored, p.flipped);
					sprite.setScale(p.scale);
					sprite.setAlpha(p.alpha);
					sprite.draw(batch);
				}
			}
			for(Player player : Constant.getPlayerList())
			{
				if(player != null && player.active && player.myMapX == worldMap.globalX && player.myMapY == worldMap.globalY
						&& !player.loadingMapOnline && player.untargetable < 0f && player.timeSinceLastAttack > Constant.BACKWEAPON_TIME)
				{
					batch.setShader(Main.basicShader);
					if(player.haveBuff(39) != -1)
						batch.setShader(Content.empowerBladeS);
					if(player.haveBuff(6) != -1)
						batch.setShader(Content.stellarAttackS);
					if(player.haveBuff(65) != -1)
						batch.setShader(Content.whiteS);

					if(player.inventory[Constant.ITEMSLOT_LEFT] != null && player.inventory[Constant.ITEMSLOT_LEFT].canGoBack && player.inventory[Constant.ITEMSLOT_LEFT].itemclass != Constant.ITEMCLASS_MATERIAL)
					{
						player.inventory[Constant.ITEMSLOT_LEFT].draw(batch, player, 0);
					}
					if(player.inventory[Constant.ITEMSLOT_RIGHT] != null && player.inventory[Constant.ITEMSLOT_RIGHT].canGoBack && player.inventory[Constant.ITEMSLOT_RIGHT].itemclass != Constant.ITEMCLASS_MATERIAL)
					{
						player.inventory[Constant.ITEMSLOT_RIGHT].draw(batch, player, 1);
					}
				}
			}
		}
		batch.setShader(Main.basicShader);
		worldMap.drawTiles(batch);
		/*batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_SRC_COLOR);
		batch.draw(Content.brokenGround[0], player[me].Center().x-100, player[me].position.y-100);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);*/

		batch.setColor(1f, 1f, 1f, 1f);
		for (Treasure p : treasure) {
			if (p.active && p.myMapX == worldMap.globalX && p.myMapY == worldMap.globalY)
			{
				p.draw(batch);
			}
		}
		batch.end();

		if(shouldDrawEntities())
		{
			for(Player player : Constant.getPlayerList())
			{
				if(player != null && player.active)
				{
					player.UpdateBand();
					Item hat = null;
					if(player.inventory[Constant.ITEMSLOT_HEAD] != null && player.inventory[Constant.ITEMSLOT_HEAD].id == 23)
					{
						hat = player.inventory[Constant.ITEMSLOT_HEAD];
					}
					if(player.vanity[2] != null && player.vanity[2].id == 23)
					{
						hat = player.vanity[2];
					}
					if(hat != null && player.myMapX == worldMap.globalX && player.myMapY == worldMap.globalY 
							&& !player.loadingMapOnline && player.untargetable < 0f)
					{
						Main.shapeRenderer.begin(ShapeType.Filled);
						for(int j = 1;j >= 0;j--)
						{
							for(int x = 0; x < 9;x++)
							{
								Color darkcolor = new Color(1f, 1f, 1f, 1f);
								if(hat.dyed)
								{
									darkcolor = hat.dye.cpy();
								}
								if(j == 1)
								{
									darkcolor.r *= 0.7f;
									darkcolor.g *= 0.7f;
									darkcolor.b *= 0.7f;
								}
								float width = 4 + (x+1);
								if(x > 6)
									width -= (x-6) * 2;
								Main.shapeRenderer.setColor(darkcolor);
								Main.shapeRenderer.rectLine(player.myBand[j][x], player.myBand[j][x+1], width);

							}
						}
						Main.shapeRenderer.end();
					}
				}
			}
		}

		if(debug)
		{
			Main.shapeRenderer.begin(ShapeType.Line);
			Main.shapeRenderer.circle(player[me].Center().x, player[me].Center().y, var[1]);
			Main.shapeRenderer.end();
		}

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.begin();

		sprite = new Sprite();
		if(shouldDrawEntities())
		{
			for (Prop m : prop) {
				m.draw(batch);
			}
			for (Monster m : monster) {
				float max = Math.max(m.width, m.height);
				if (m.active && m.myMapX == worldMap.globalX && m.myMapY == worldMap.globalY && getScreen(max * 1.2f).overlaps(m.hitBox()))
				{
					m.draw(1f/60f, batch, false);
				}
			}
			for (NPC m : npc) {
				float max = Math.max(m.width, m.height);
				if (m.active && m.myMapX == worldMap.globalX && m.myMapY == worldMap.globalY && getScreen(max * 1.2f).overlaps(m.hitBox()))
					m.draw(1f/60f, batch);
			}

			batch.setShader(Content.pcS);
			for(int i = 0;i < Constant.getPlayerList().length;i++)
			{
				if(player[i].active && sameMapAsWorld(player[i]) && !player[i].isUntargetable())
				{
					if(player[i].haveBuff(65) != -1)
					{
						batch.setShader(Content.whiteS);
					}
					float[] array = player[i].getPCShaderArray();
					Content.pcS.setUniform3fv("color", array, 0, 3);
					Content.empowerBladeS.setUniform3fv("color", array, 0, 3);
					Content.stellarAttackS.setUniform3fv("color", array, 0, 3);
					player[i].preDraw(batch);
					DrawPlayer(batch, player[i], false);
					player[i].postDraw(batch);
				}
			}
			batch.setShader(Main.basicShader);

			for(int j = 0;j < 4;j++)
			{
				for (int i = 0; i < 30; i++) {
					if (player[j].buffs[i] != null) {
						player[j].buffs[i].draw(player[j], batch);
					}
				}	
			}
			for (Monster m : Constant.getMonsterList(true)) {
				if (m.active && m.myMapX == worldMap.globalX && m.myMapY == worldMap.globalY)
					m.postDraw(batch);
			}
			for (NPC m : npc) {
				if (m.active && m.myMapX == worldMap.globalX && m.myMapY == worldMap.globalY)
					m.postDraw(batch);
			}
			for (Particle p : particle) {
				if (p.active && !p.drawBack && !p.drawFront) {
					p.draw(batch);
				}
			}
			if(!Settings.particleGlowLite)
			{
				batch.setBlendFunctionSeparate(GL20.GL_DST_COLOR, GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR, GL20.GL_ZERO);
				for (Particle p : particle) {
					if (p.active && p.lights && !p.drawBack && !p.drawFront) {
						p.drawLight(batch, false);
					}
				}
				batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			}

			for (Item p : items) {
				if (p.inWorld && p.myMapX == worldMap.globalX && p.myMapY == worldMap.globalY) {
					p.drawInWorld(batch);
				}
			}
			for (Projectile p : projectile) {
				if (p.active && !p.drawBack) {
					float max = Math.max(p.width, p.height);
					if (!getScreen(max * 1.2f).overlaps(p.hitBox()))
						continue;

					sprite = new Sprite(p.getTexture());
					int h = (int) (sprite.getHeight()/p.frames);
					int w = (int) sprite.getWidth();
					sprite.setPosition(p.position.x - w/2f + p.width/2f, p.position.y - h/2f + p.height/2f);
					sprite.setRegion(0, h * p.currentFrame, w,
							h);
					sprite.setSize(w, h);
					sprite.setOriginCenter();
					sprite.setRotation(((float) (p.rotation - p.rotationOffset) % 360)
							* (p.rotateByDirection ? p.direction : 1));
					sprite.flip(p.mirrored, p.flipped);
					sprite.setScale(p.scale);
					sprite.setAlpha(p.alpha);
					ShaderProgram bkp = batch.getShader();
					if(p.haveBuff(6) != -1)
						batch.setShader(Content.stellarAttackS);
					sprite.draw(batch);
					
					if(batch.getShader() != bkp)
						batch.setShader(bkp);
				}
			}
		}

		worldMap.drawForeground(batch);
		if(shouldDrawEntities())
		{
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			for (Particle p : particle) {
				if (p.active && p.drawFront) {
					p.draw(batch);
				}
			}
			batch.setBlendFunctionSeparate(GL20.GL_DST_COLOR, GL20.GL_SRC_ALPHA, GL20.GL_DST_COLOR, GL20.GL_ZERO);
			for (Particle p : particle) {
				if (p.active && p.lights && (p.drawFront || Settings.particleGlowLite)) {
					p.drawLight(batch, false);
				}
			}
			for (Projectile p : projectile) {
				if (p.active && p.lights) {
					float max = Math.max(p.width, p.height);
					if (!getScreen(max * 1.2f).overlaps(p.hitBox()))
						continue;

					sprite = new Sprite(Content.light2);
					float size = p.lightSize/2f;
					if(p.timeLeft < 0.5f)
						size = (float) (p.lightSize/2f * Math.pow(0.9f, (0.5f-p.timeLeft)*100));
					
					sprite.setSize(size, size);
					sprite.setPosition(p.Center().x - sprite.getWidth() / 2,
							p.Center().y - sprite.getHeight() / 2);
					for (int j = 0; j < p.lightStrength; j++)
						sprite.draw(batch);
				}
			}
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			for (Item p : items) {
				if (p.inWorld) {
					p.drawInWorldPost(batch);
				}
			}

		}

		if(usingSkill(player[me]))
			batch.setColor(1f - minus, 1f - minus, 1f - minus, 1f);

		batch.setColor(1f,1f,1f,1f);

		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		if(worldMap.id == 160 || worldMap.id == 170 || worldMap.id == 180)
		{
			float width = 250;
			float zoom = Math.max(1f, Math.abs(camera.zoom));
			int count = (int) (Gdx.graphics.getWidth()*zoom/width)*2;
			for(float y = cameraRealPosition().y-250;y < (cameraRealPosition().y + Gdx.graphics.getHeight()*zoom);y+=50)
			{
				int i = (int)(y/50);
				for(int cl = 1;cl <= 5;cl++)
				{
					sprite = new Sprite(Content.ghostcloud[(int) (((Math.pow(i+cl, 4)) + cl) % Content.ghostcloud.length)]);
					sprite.setPosition((float) (((Math.pow(i + cl * 2, 4) + i * 1.75f) % 1000000 - gameTick * (cl % 2 == 0 ? (-0.01f * cl) : (0.01f * cl))) % (worldMap.width + 20)*64) - 640, i*50 - 25 + (cl/10f)*50f);
					if(Main.usingSkill(player[me]))
					{
						float tone = 1f-Main.usingSkillMinus(player[me]);
						sprite.setColor(tone, tone, tone, 1f);
					}
					sprite.draw(batch);
				}
			}
			for(int cl = -count;cl <= count;cl++)
			{
				float limit = count*width*2;
				float posDelocX = ((Main.gameTick * 1f - camera.position.x * 1f + 100000) + cl * 250) % limit;

				sprite = new Sprite(Content.ghostcloud[(int) (((Math.pow(cl, 4)) + cl) % Content.ghostcloud.length)]);
				if(Main.usingSkill(player[me]))
				{
					float tone = 1f-Main.usingSkillMinus(player[me]);
					sprite.setColor(tone, tone, tone, 1f);
				}
				sprite.setPosition(camera.position.x+posDelocX-limit/2, Main.cameraRealPosition().y-100);
				sprite.draw(batch);
				sprite = new Sprite(Content.ghostcloud[(int) (((Math.pow(cl, 4)) + cl) % Content.ghostcloud.length)]);
				if(Main.usingSkill(player[me]))
				{
					float tone = 1f-Main.usingSkillMinus(player[me]);
					sprite.setColor(tone, tone, tone, 1f);
				}
				sprite.setPosition(camera.position.x+posDelocX-limit/2, Main.cameraRealPosition().y+Gdx.graphics.getHeight()*zoom-100);
				sprite.draw(batch);
				
			}
		}
		for (int i = 0; i < 100; i++) {
			if (livingtext[i].active) {
				float alpha = 1f;
				if (livingtext[i].timeLeft < 0.5f) {
					alpha = livingtext[i].timeLeft * 2f;
				}

				String text = livingtext[i].text;
				try
				{
					if(livingtext[i].damageText && Integer.parseInt(text) > 5000000)
					{
						text = "a very big damage";
					}
				}
				catch(Exception ex)	{}
				
				
				font.setColor(Color.BLACK);
				font.getData().setScale(livingtext[i].scale * (camera.zoom+0.00001f));
				layout.setText(font, text);
				font.draw(batch, text, livingtext[i].position.x - layout.width / 2 - 1,
						livingtext[i].position.y - layout.height / 2 - 1);
				font.draw(batch, text, livingtext[i].position.x - layout.width / 2 + 1,
						livingtext[i].position.y - layout.height / 2 - 1);
				font.draw(batch, text, livingtext[i].position.x - layout.width / 2 - 1,
						livingtext[i].position.y - layout.height / 2 + 1);
				font.draw(batch, text, livingtext[i].position.x - layout.width / 2 + 1,
						livingtext[i].position.y - layout.height / 2 + 1);

				font.setColor(livingtext[i].color.r, livingtext[i].color.g, livingtext[i].color.b, alpha);
				font.getData().setScale(livingtext[i].scale * (camera.zoom));
				layout.setText(font, text);
				font.draw(batch, text, livingtext[i].position.x - layout.width / 2,
						livingtext[i].position.y - layout.height / 2);
				if (livingtext[i].critical)
					font.draw(batch, text, livingtext[i].position.x - layout.width / 2 + 1,
							livingtext[i].position.y - layout.height / 2 + 1);
			}
		}
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		batch.end();
		batch.flush();
		fbo.end();

		fb.setProjectionMatrix(uiMatrix);
		fb.setShader(shader);
		fb.begin();
		if(Settings.highSpeedBlur)
		{
			Main.drawBlurredTexture(fb, fboRegion, 0, 0, radiusX, radiusY, steps);
		}
		else
		{
			fb.draw(fboRegion, 0, 0);
		}
		fb.end();
		
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		fbo.begin();
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		worldMap.renderShadow(batch);
		lighting.addAll(lightingex);
		lightingex.clear();
		for (Iterator<Lighting> j = lighting.iterator(); j.hasNext();) {
			Lighting p = j.next();
			if (p.timeLeft <= 0 || p.remove) {
				j.remove();
			} else {
				p.timeLeft -= delta;
				if(p.parent != null && p.parent.active)
				{
					p.center = p.parent.Center();
				}
				else if(p.hadParent)
				{
					p.timeLeft = Math.min(0.75f, p.timeLeft);
					p.hadParent = false;
				}
			}
		}
		lightcount = 0;
		for(Lighting p : lighting)
		{
			Circle c = new Circle(p.center, p.radius);
			if (!Intersector.overlaps(c, getScreen(p.radius*2)) || p.timeLeft <= 0 || p.remove)
				continue;

			sprite = new Sprite(Content.light);
			float size = p.radius;

			float angle = MathUtils.random(360f);
			float vibrancy = MathUtils.random(p.vibrancy);
			float sin = (float)(Math.sin(angle*Math.PI/180f));
			float cos = (float)(Math.cos(angle*Math.PI/180f));
			Vector2 offset = new Vector2(cos * vibrancy, sin * vibrancy);
			
			sprite.setSize(size*2, size*2);
			sprite.setColor(p.color);
			sprite.setAlpha(Math.min(1f, (float)Math.pow(p.timeLeft, 2)));
			sprite.setPosition(p.center.x - sprite.getWidth() / 2 + offset.x,
					p.center.y - sprite.getHeight() / 2 + offset.y);
			for(int i = 0;i < p.power;i++)
				sprite.draw(batch);
			lightcount++;
		}
		batch.end();
		fbo.end();
		
		fb2.setProjectionMatrix(uiMatrix);
		fb2.begin();
		fb2.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_SRC_COLOR);
		fb2.draw(fboRegion, 0, 0);
		fb2.end();
		
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0f, 1f, 0f, 1f);
		if(debug)
		{
			for(Prop p : Main.prop)
			{
				shapeRenderer.rect(p.position.x, p.position.y, p.width, p.height);
			}
			for(Monster m : Main.monster)
			{
				shapeRenderer.rect(m.hitBox().x, m.hitBox().y, m.hitBox().width, m.hitBox().height);
				shapeRenderer.circle(m.Center().x, m.Center().y, 4);
			}
		}
		shapeRenderer.end();
	}

	public static boolean shouldDrawEntities()
	{
		return (!player[me].loadingMapOnline && Main.blackScreenTime > 0f);
	}

	public static void DrawPlayer(SpriteBatch batch, Player player, boolean transp)
	{
		Sprite head = new Sprite(Content.playerHeadStand);
		Sprite sprite = new Sprite(Content.playerStand);
		Sprite hand = new Sprite(Content.playerHandStand);
		Sprite bhand = new Sprite(Content.playerBHandStand);
		if (player.walking) {
			sprite.setTexture(Content.playerWalking);
			head.setTexture(Content.playerHeadWalking);
			hand.setTexture(Content.playerHandWalking);
			bhand.setTexture(Content.playerBHandWalking);
			sprite.setRegion(0, (player.animFrame % 5) * (player.height + 8), player.width + 8, player.height + 8);
			head.setRegion(0, (player.animFrame % 5) * (player.height + 8), player.width + 8, player.height + 8);
			hand.setRegion(0, (player.animFrame % 5) * (player.height + 8), player.width + 8, player.height + 8);
			bhand.setRegion(0, (player.animFrame % 5) * (player.height + 8), player.width + 8, player.height + 8);
		}
		if (player.checkingInventory) {
			sprite.setTexture(Content.playerInventory);
			head.setTexture(Content.playerHeadInventory);
			hand.setTexture(Content.playerHandInventory);
			bhand.setTexture(Content.playerBHandInventory);
			
			int animFrame = player.animFrame;
			if (animFrame > 6)
				animFrame = 6;

			sprite.setRegion(0, (animFrame % 7) * (player.height + 8), player.width + 8, player.height + 8);
			head.setRegion(0, (animFrame % 7) * (player.height + 8), player.width + 8, player.height + 8);
			hand.setRegion(0, (animFrame % 7) * (player.height + 8), player.width + 8, player.height + 8);
			bhand.setRegion(0, (animFrame % 7) * (player.height + 8), player.width + 8, player.height + 8);
		}
		if (player.rolling) {
			sprite.setTexture(Content.playerRoll);
			head.setTexture(Content.playerHeadRoll);
			hand.setTexture(Content.playerHandRoll);
			bhand.setTexture(Content.playerBHandRoll);
			/*int animFrame = player.animFrame;
			if (animFrame > 1)
				animFrame = 1;

			sprite.setRegion(0, (animFrame % 2) * 120, 90, 120);
			head.setRegion(0, (animFrame % 2) * 120, 90, 120);
			hand.setRegion(0, (animFrame % 2) * 120, 90, 120);
			bhand.setRegion(0, (animFrame % 2) * 120, 90, 120);*/
			sprite.setSize(90, 120);
			head.setSize(90, 120);
			hand.setSize(90, 120);
			bhand.setSize(90, 120);

			int tick = player.lastResetTicks();
			float rotation = tick * -16;
			if (player.direction == Constant.DIRECTION_LEFT)
				rotation *= -1f;
			
			sprite.setRotation(rotation);
			head.setRotation(rotation);
			hand.setRotation(rotation);
			bhand.setRotation(rotation);
			/*sprite.setPosition(player.position.x - 17, player.position.y - 4);
			head.setPosition(player.position.x - 17, player.position.y - 4);
			hand.setPosition(player.position.x - 17, player.position.y - 4);
			bhand.setPosition(player.position.x - 17, player.position.y - 4);*/
			sprite.setOriginCenter();
			head.setOriginCenter();
			hand.setOriginCenter();
			bhand.setOriginCenter();
			sprite.setPosition(player.Center().x-sprite.getWidth()/2f, player.Center().y - sprite.getHeight()/2f);
			head.setPosition(player.Center().x-head.getWidth()/2f, player.Center().y - head.getHeight()/2f);
			hand.setPosition(player.Center().x-hand.getWidth()/2f, player.Center().y - hand.getHeight()/2f);
			bhand.setPosition(player.Center().x-bhand.getWidth()/2f, player.Center().y - bhand.getHeight()/2f);
		}
		else
		{
			sprite.setPosition(player.position.x - 4, player.position.y - 4);
			head.setPosition(player.position.x - 4, player.position.y - 4);
			hand.setPosition(player.position.x - 4, player.position.y - 4);
			bhand.setPosition(player.position.x - 4, player.position.y - 4);
		}
		if (player.dancing) {
			sprite.setTexture(Content.playerDance);
			int animFrame = player.animFrame;

			sprite.setRegion(0, (animFrame % 8) * 96, 64, 96);
			sprite.setSize(64, 96);
			sprite.setY(sprite.getY() - 4);
		}
		if (player.customAnim != null && !(player.rolling || player.dead)) {
			hand = new Sprite(player.customAnim);
			int width = (int) (hand.getWidth());
			int height = (int) (hand.getHeight() / player.customAnimFrames);
			if(player.customAnimShouldRotate)
			{
				float x = player.Center().x;
				float y = player.Center().y - height / 2;

				hand.setRegion(0, (player.customAnimFrame * height), width, height);
				hand.setSize(width, height);
				hand.setOrigin(0f, height / 2);
				hand.setRotation(player.customAnimRotation);
				Vector2 pos = new Vector2(x, y);
				Vector2 atkToP = Vector2.Zero.cpy();
				atkToP = player.customAnimOffset.cpy();
				if(player.direction == -1)
				{
					atkToP.y *= -1f;
				}
				atkToP.rotate(hand.getRotation());
				pos.add(atkToP);
				hand.setPosition(pos.x, pos.y);
				hand.setFlip(false, player.direction == -1);
			}
			else
			{
				hand = new Sprite(player.customAnim);
				float x = player.Center().x - width/2;
				float y = player.Center().y - height/2;
				hand.setPosition(x + (player.customAnimOffset.x * player.direction), y + player.customAnimOffset.y);
				hand.setRegion(0, (player.customAnimFrame * height), width, height);
				hand.setSize(width, height);
				hand.flip(player.direction == -1, player.invertedSwing);
				hand.setScale(player.scale);
				if(transp)
					hand.setAlpha(0.5f);
				hand.draw(batch);
			}
		}
		if (player.dead) {
			int tick = player.lastResetTicks();
			float rotation = tick * 18;
			if (rotation > 90)
				rotation = 90;

			if (player.direction == Constant.DIRECTION_LEFT)
				rotation *= -1f;

			sprite.setRotation(rotation);
			head.setRotation(rotation);
			hand.setRotation(rotation);
			bhand.setRotation(rotation);

			sprite.setY(sprite.getY() - 8);
			head.setY(sprite.getY() - 8);
			hand.setY(hand.getY() - 8);
			bhand.setY(bhand.getY() - 8);
		}
		sprite.flip(player.direction == -1, false);
		head.flip(player.direction == -1, false);
		if (player.customAnim == null)
			hand.flip(player.direction == -1, false);

		if(player.customAnim2 == null)
			bhand.flip(player.direction == -1, false);
		
		if(player.haveBuff(11) != -1)
		{
			head.setAlpha(0.5f);
			sprite.setAlpha(0.5f);
			hand.setAlpha(0.5f);
			bhand.setAlpha(0.5f);
		}
		for (int i = Constant.ITEMSLOT_OFFSET; i <= Constant.ITEMSLOT_MAX; i++) {
			if (player.inventory[i] != null)
			{
				player.inventory[i].backDraw(batch, player);
			}
		}
		if (player.customAnim2 != null && !(player.rolling || player.dead)) {
			bhand = new Sprite(player.customAnim2);
			int width = (int) (bhand.getWidth());
			int height = (int) (bhand.getHeight() / player.customAnim2Frames);
			if(player.customAnim2ShouldRotate)
			{
				float x = player.Center().x;
				float y = player.Center().y - height / 2;

				bhand.setRegion(0, (player.customAnim2Frame * height), width, height);
				bhand.setSize(width, height);
				bhand.setOrigin(0f, height / 2);
				bhand.setRotation(player.customAnim2Rotation);
				Vector2 pos = new Vector2(x, y);
				Vector2 atkToP = Vector2.Zero.cpy();
				atkToP = player.customAnim2Offset.cpy();
				if(player.direction == -1)
				{
					atkToP.y *= -1f;
				}
				atkToP.rotate(bhand.getRotation());
				pos.add(atkToP);
				bhand.setPosition(pos.x, pos.y);
				bhand.setFlip(false, player.direction == -1);
				bhand.setScale(player.scale);
				if(transp)
					bhand.setAlpha(0.5f);
				bhand.draw(batch);
			}
			else
			{
				bhand = new Sprite(player.customAnim2);
				float x = player.Center().x - width/2;
				float y = player.Center().y - height/2;
				bhand.setPosition(x + (player.customAnim2Offset.x * player.direction), y + player.customAnim2Offset.y);
				bhand.setRegion(0, (player.customAnim2Frame * height), width, height);
				bhand.setSize(width, height);
				bhand.flip(player.direction == -1, player.invertedSwing2);
				bhand.setScale(player.scale);
				if(transp)
					bhand.setAlpha(0.5f);
				bhand.draw(batch);
			}
		}

		if(player.haveBuff(39) != -1)
			batch.setShader(Content.empowerBladeS);
		
		if(player.haveBuff(6) != -1)
			batch.setShader(Content.stellarAttackS);
		
		if(player.haveBuff(65) != -1)
			batch.setShader(Content.whiteS);

		if (player.drawBHBack() && player.drawBackHand()) {
			if(!player.isWearingShield() || !player.attacking)
			{
				bhand.setScale(player.scale);
				if(transp)
					bhand.setAlpha(0.5f);
				bhand.draw(batch);
			}

			if(!player.isWearingShield())
			{
				if (player.inventory[Constant.ITEMSLOT_RIGHT] != null && player.inventory[Constant.ITEMSLOT_RIGHT].itemclass != Constant.ITEMCLASS_MATERIAL && (!player.inventory[Constant.ITEMSLOT_RIGHT].canGoBack || player.timeSinceLastAttack <= Constant.BACKWEAPON_TIME))
				{
					player.inventory[Constant.ITEMSLOT_RIGHT].draw(batch, player, 1);
				}
			}
			else
			{
				if (player.inventory[Constant.ITEMSLOT_LEFT] != null && player.inventory[Constant.ITEMSLOT_LEFT].itemclass != Constant.ITEMCLASS_MATERIAL && (!player.inventory[Constant.ITEMSLOT_LEFT].canGoBack || player.timeSinceLastAttack <= Constant.BACKWEAPON_TIME))
					player.inventory[Constant.ITEMSLOT_LEFT].draw(batch, player, 1);

			}
		}
		if(player.haveBuff(65) == -1)
			batch.setShader(Content.pcS);

		if(player.shouldDashPosture())
		{
			sprite = new Sprite(Content.playerDash);
			sprite.setPosition(player.position.x-4, player.position.y-4);
			sprite.setSize(60, 80);
			sprite.flip(player.direction == -1, false);
			head = new Sprite(Content.playerHeadDash);
			head.setPosition(player.position.x-4, player.position.y-4);
			head.setSize(60, 80);
			head.flip(player.direction == -1, false);
		}
		if(player.checkingInventory && player.animFrame < 4)
		{
			Sprite bag = new Sprite(Content.playerBagInventory);
			int animFrame = player.animFrame;
			if (animFrame > 6)
				animFrame = 6;

			bag.setPosition(player.position.x - 4, player.position.y - 4);
			bag.setRegion(0, (animFrame % 7) * (player.height + 8), player.width + 8, player.height + 8);
			bag.setSize(60, 80);
			bag.setFlip(player.direction == -1, false);
			bag.setScale(player.scale);
			if(transp)
				bag.setAlpha(0.5f);
			bag.draw(batch);
		}
		sprite.setScale(player.scale);
		if(transp)
			sprite.setAlpha(0.5f);
		sprite.draw(batch);
				
		if(player.vanity[3] != null)
			player.vanity[3].drawHat(batch, player, sprite);
		else if (player.inventory[Constant.ITEMSLOT_BODY] != null)
			player.inventory[Constant.ITEMSLOT_BODY].drawHat(batch, player, sprite);
		
		if(!player.dancing)
		{
			head.setScale(player.scale);
			if(transp)
				head.setAlpha(0.5f);
			head.draw(batch);
		}
		
		if(player.vanity[2] != null)
			player.vanity[2].drawHat(batch, player, sprite);
		
		else if (player.inventory[Constant.ITEMSLOT_HEAD] != null)
			player.inventory[Constant.ITEMSLOT_HEAD].drawHat(batch, player, sprite);


		if (player.inventory[Constant.ITEMSLOT_SPECIAL1] != null)
			player.inventory[Constant.ITEMSLOT_SPECIAL1].draw(batch, player, 0);
		if (player.inventory[Constant.ITEMSLOT_SPECIAL2] != null)
			player.inventory[Constant.ITEMSLOT_SPECIAL2].draw(batch, player, 0);
		if (player.inventory[Constant.ITEMSLOT_SPECIAL3] != null)
			player.inventory[Constant.ITEMSLOT_SPECIAL3].draw(batch, player, 0);
		if (player.inventory[Constant.ITEMSLOT_SPECIAL4] != null)
			player.inventory[Constant.ITEMSLOT_SPECIAL4].draw(batch, player, 0);

		for(int i : player.hotBar)
		{
			if(i > 0)
			{
				Item item = player.getItem(i);
				if(item.itemclass == Constant.ITEMCLASS_ACCESSORY)
				{
					item.drawHat(batch, player, sprite);
				}
			}
		}

		if(player.haveBuff(39) != -1)
			batch.setShader(Content.empowerBladeS);
		
		if(player.haveBuff(6) != -1)
			batch.setShader(Content.stellarAttackS);
		
		if(!player.isWearingShield())
		{
			if (player.inventory[Constant.ITEMSLOT_LEFT] != null && player.inventory[Constant.ITEMSLOT_LEFT].itemclass != Constant.ITEMCLASS_MATERIAL && (!player.inventory[Constant.ITEMSLOT_LEFT].canGoBack || player.timeSinceLastAttack <= Constant.BACKWEAPON_TIME))
				player.inventory[Constant.ITEMSLOT_LEFT].draw(batch, player, 0);
		}
		else
		{
			if((!player.inventory[Constant.ITEMSLOT_RIGHT].canGoBack || player.timeSinceLastAttack <= Constant.BACKWEAPON_TIME))
				player.inventory[Constant.ITEMSLOT_RIGHT].draw(batch, player, 0);
		}
		if(player.haveBuff(65) == -1)
			batch.setShader(Content.pcS);
		
		if(player.checkingInventory && player.animFrame >= 4)
		{
			Sprite bag = new Sprite(Content.playerBagInventory);
			int animFrame = player.animFrame;
			if (animFrame > 6)
				animFrame = 6;

			bag.setPosition(player.position.x - 4, player.position.y - 4);
			bag.setRegion(0, (animFrame % 7) * (player.height + 8), player.width + 8, player.height + 8);
			bag.setSize(60, 80);
			bag.setFlip(player.direction == -1, false);
			bag.setScale(player.scale);
			if(transp)
				bag.setAlpha(0.5f);
			bag.draw(batch);
		}
		if (!player.drawBHBack() && player.drawBackHand()) {
			if(!player.attacking2 && player.customAnim2 == null)
			{
				bhand.setScale(player.scale);
				if(transp)
					bhand.setAlpha(0.5f);
				bhand.draw(batch);
			}
			if(player.timeSinceLastAttack <= Constant.BACKWEAPON_TIME)
			{
				if (player.inventory[Constant.ITEMSLOT_RIGHT] != null && player.inventory[Constant.ITEMSLOT_RIGHT].itemclass != Constant.ITEMCLASS_MATERIAL)
					player.inventory[Constant.ITEMSLOT_RIGHT].draw(batch, player, 1);
			}
		}
		if (player.drawHand() && !player.attacking) {
			hand.setScale(player.scale);
			if(transp)
				hand.setAlpha(0.5f);
			hand.draw(batch);
		}
		for (int i = Constant.ITEMSLOT_OFFSET; i <= Constant.ITEMSLOT_MAX; i++) {
			if (player.inventory[i] != null)
				player.inventory[i].postDraw(batch, player);
		}
	}

	public static void SwitchMap(int map, boolean invertX, boolean invertY, int[] specialGen) {
		if (!readyForNextmap) {
			blackScreenTime = -3f;
			timeForNextmap = 1f;
			readyForNextmap = true;
			nextMap = map;
			nminvertx = invertX;
			nminverty = invertY;
			nmspecialgen = specialGen;
			blackScreenPostPosition = Vector2.Zero.cpy();
			if(isOnline)
			{
				loadingProgress = Main.lv("Esperando por mapa...", "Waiting for map...");
				player[me].loadingMapOnline = true;
				System.out.println("Sending map request to server...");
				MapRequest mr = new MapRequest(player[me].myMapX, player[me].myMapY, player[me].cameFromX, player[me].cameFromY, player[me].cameFromLX, player[me].cameFromLY, map);
				Main.client.client.sendTCP(mr);
				System.out.println("Map request sent.");
			}
		}
	}
	
	public static void SwitchMap(int map, Vector2 pos, int[] specialGen) {
		if (!readyForNextmap) {
			blackScreenTime = -3f;
			timeForNextmap = 1f;
			readyForNextmap = true;
			nextMap = map;
			nminvertx = false;
			nminverty = false;
			nmspecialgen = specialGen;
			blackScreenPostPosition = pos.cpy();
			if(isOnline)
			{
				player[me].loadingMapOnline = true;
				System.out.println("Sending map request to server...");
				String mapname = player[me].myMapX + "_" + player[me].myMapY;
				long size = -1;
				FileHandle file = Gdx.files.local("maps/" + player[me].name + "/" + mapname + ".map");
				if(file.exists())
				{
					size = file.length();
				}
				MapRequest mr = new MapRequest(player[me].myMapX, player[me].myMapY, player[me].cameFromX, player[me].cameFromY, player[me].cameFromLX, player[me].cameFromLY, map);
				Main.client.client.sendTCP(mr);
				System.out.println("Map request sent.");
			}
		}
	}

	public static void SaveMap()
	{
		SaveMap(Main.worldMap, Main.worldMap.globalX, Main.worldMap.globalY);
	}
	
	public static void SaveMap(GameMap g, int mx, int my) {
		SaveMap(g, mx, my, true);
	}
	
	public static void SaveMap(GameMap g, int mx, int my, boolean onlineResend) {
		if(!isOnline || !onlineResend)
		{
		}
	}
	
	public static void LoadMap(final GameMap map) {
		loadingMap = true;
		new Thread() {
			public void run()
			{
				for (Projectile p : projectile) {
					if (p.active)
						p.active = false;
				}
				int oldW = Main.worldMap.width;
				int oldH = Main.worldMap.height;
				cameMap = Main.worldMap;
				loadingProgress = Main.lv("Importing map file", "Importando arquivo de mapa");
				Main.worldMap = map;

				if (newGen)
				{
					loadingProgress = Main.lv("Initializing post generation", "Inicializando pós-geração");
					Main.worldMap.postPostGen();
				}

				loadingProgress = Main.lv("Updating map infos", "Atualizando informações do mapa");
				Main.worldMap.postLoad();
				
				HPBGs.clear();
				for(int z = 0;z < worldMap.bg[0][0].length;z++)
				{
					HPBGs.put(z, new ArrayList<HPBG>());
				}
				for(int y = 0;y < worldMap.bg.length;y++)
				{
					for(int x = 0;x < worldMap.bg[0].length;x++)
					{
						for(int z = 0;z < worldMap.bg[0][0].length;z++)
						{
							if(worldMap.bg[y][x][z] != null && worldMap.bg[y][x][z].highPriority)
							{
								HPBGs.get(z).add(new HPBG(worldMap.bg[y][x][z], x, y));
							}
						}
					}
				}

				Main.worldMap.globalX = player[me].myMapX;
				Main.worldMap.globalY = player[me].myMapY;
				GameMap.oldWidth = oldW;
				GameMap.oldHeight = oldH;
				loadingProgress = Main.lv("Loading map entities", "Carregando entidades do mapa");
				if(!isOnline)
				{
					ArrayList<Entity> entities = new ArrayList<Entity>();
					entities.addAll(Main.worldMap.monsters);
					entities.addAll(Main.worldMap.naturals);
					entities.addAll(Main.worldMap.items);
					entities.addAll(Main.worldMap.treasures);
					entities.addAll(Main.worldMap.npcs);
					entities.addAll(Main.worldMap.props);
					for(Entity e : entities)
					{
						e.myMapX = player[me].myMapX;
						e.myMapY = player[me].myMapY;
					}
					monster = Main.worldMap.monsters;
					for(Monster m : monster)
					{
						m.whoAmI = monster.indexOf(m);
						m.uid = MathUtils.random(1, 9999999);
					}
					natural = Main.worldMap.naturals;
					items = Main.worldMap.items;
					treasure = Main.worldMap.treasures;
					npc = Main.worldMap.npcs;
					prop = Main.worldMap.props;
					for(Monster m : monster)
					{
						m.Reset(false);
					}

					if(Main.worldMap.respawns != null)
					{
						for(Respawn r : worldMap.respawns)
						{
							r.myMapX = player[me].myMapX;
							r.myMapY = player[me].myMapY;
						}
						respawns = Main.worldMap.respawns;
					}
				}
				else
				{
				}
				Main.worldMap.mapTick = gameTick + 180;
				Main.player[me].invincible = 5.5f;

				System.out.println("Map load process finished.");

				loadingProgress = Main.lv("Updating player position", "Atualizando posição do jogador");
				if(Main.blackScreenPostPosition.x == 0 && Main.blackScreenPostPosition.y == 0)
				{
					boolean right = Main.nminvertx;
					boolean up = Main.nminverty;
					float newX = Main.player[me].position.x;
					if (right) {
						newX = (Main.worldMap.width * Tile.TILE_SIZE)
								- (Main.worldMap.width * Main.player[me].position.x) / GameMap.oldWidth;
					}
					float newY = Main.player[me].position.y;
					if (up) {
						newY = (Main.worldMap.height * Tile.TILE_SIZE)
								- (Main.worldMap.height * Main.player[me].position.y) / GameMap.oldHeight;
					}
					Main.player[me].position = Main.worldMap
							.getNextestFreeSpaceSafe(newX, newY, Main.player[me].width, Main.player[me].height, (right ? -1 : 1), (up ? -1 : 1))
							.scl(Tile.TILE_SIZE);
				}
				else
				{
					//Vector2 freeSpace = Main.worldMap.getNextestFreeSpaceSafe(Main.blackScreenPostPosition.x, Main.blackScreenPostPosition.y, player[me].width, player[me].height, 1, 1).scl(64);
					Vector2 freeSpace = new Vector2(Main.blackScreenPostPosition.x, Main.blackScreenPostPosition.y);
					player[me].position.x = freeSpace.x;
					player[me].position.y = freeSpace.y;
				}

				loadingProgress = Main.lv("Initializing map data", "Inicializando dados do mapa");
				System.out.println("Updated world map.");
				worldMap.onEnter();
				System.out.println("Executed onEnter function.");
				Main.blackScreenPostPosition = Vector2.Zero.cpy();
				worldMap.initialize();
				loadingProgress = Main.lv("Generating lighting map", "Gerando mapa de iluminação");
				worldMap.generateLightMap();
				System.out.println("Generated lightMap.");
				loadingProgress = Main.lv("Memorizing music sheets", "Decorando partituras");
				if(worldMap.backgroundMusic != null)
					DJ.switchMusic(worldMap.backgroundMusic);

				Gdx.app.postRunnable(new Thread() {
					public void run()
					{
						loadingProgress = Main.lv("Loading map shader", "Carregando tonalizador do mapa");
						Content.loadShader(Main.worldMap.useShader);
						loadingProgress = Main.lv("Updating world map", "Atualizando mapa global");
						updateWorldMap();
						if(!isOnline)
						{
							loadingProgress = Main.lv("Saving player data", "Salvando dados do jogador");
							PlayerLoader.savePlayer(player[me]);
							player[me].position = worldMap.initialPosition.cpy();
						}
						loadingMap = false;
					}
				});
			}
		}.start();
	}

	public static void LoadMap(final int expectedId, final String mname, final int[] specialGen) {
		LoadMap(GameMapLoader.loadMap(expectedId, mname, specialGen, player[me].myMapX, player[me].myMapY,
				player[me].cameFromX, player[me].cameFromY, player[me].cameFromLX, player[me].cameFromLY));
	}
	
	public static GameMap PreLoadMap(int mx, int my)
	{
		return PreLoadMap(mx, my, 0, 0, 0, 0);
	}
	
	public static GameMap PreLoadMap(int mx, int my, int cgx, int cgy, int clx, int cly) {
		GameMap g;
		g = GameMapLoader.loadMap(1, mx + "_" + my, null, mx, my, cgx, cgy, clx, cly);
		g.globalX = mx;
		g.globalY = my;
		return g;
	}
 
	public static void updateWorldMap()
	{
		Pixmap px = new Pixmap(worldMap.width, worldMap.height, Pixmap.Format.RGB888);
		if(player[me].myMapY >= 0)
			px.setColor(0.08f, 0.67f, 1f, 1f);
		else
			px.setColor(worldMap.bgColor != null ? worldMap.bgColor : new Color(0.08f, 0.67f, 1f, 1f));
		
		px.fill();
		for(int y = 0;y < worldMap.height;y++)
		{
			for(int x = 0;x < worldMap.width;x++)
			{
				if(worldMap.map[y][x] != null)
				{
					px.setColor(worldMap.map[y][x].color);
					px.drawPixel(x, (worldMap.height-y));
				}
				else
				{
					for(int z = 0;z < worldMap.bg[0][0].length;z++)
					{
						if(worldMap.bg[y][x][z] != null)
						{
							px.setColor(0.3f, 0.3f, 0.3f, 1f);
							px.drawPixel(x, (worldMap.height-y));
						}
					}
				}
			}
		}
		Gdx.files.local("maps/" + Main.saveName + "/").file().mkdirs();
		FileHandle file = new FileHandle("maps/" + Main.saveName + "/" + player[me].myMapX + "_" + player[me].myMapY + ".png");
		PixmapIO.writePNG(file, px);
		px.dispose();
		
		File[] files = new File("maps/" + Main.saveName + "/").listFiles();
		if(files.length < 1)
			return;
		
		for(int y = 0;y < 10;y++)
		{
			for(int x = 0;x < 10;x++)
			{
				if(worldMapImages[y][x] != null)
				{
					worldMapImages[y][x].dispose();
					worldMapImages[y][x] = null;
				}
			}
		}

		for(File f : files)
		{
			if(f.isFile())
			{
				String name = f.getName();
				String[] broken = name.split("\\.");
				if(broken[1].equalsIgnoreCase("png"))
				{
					name = broken[0];
					int mapx = Integer.parseInt(name.split("_")[0]);
					int mapy = Integer.parseInt(name.split("_")[1]);
					if(Math.abs(player[me].myMapX - mapx) < 5 && Math.abs(player[me].myMapY - mapy) < 5)
					{
						System.out.println("Attaching file " + name + " to worldMapImages[" + (10 - (player[me].myMapX-mapx + 5)) + "][" + (10 - (player[me].myMapY-mapy + 5)) + "]");
						worldMapImages[10 - (player[me].myMapX-mapx + 5)][10 - (player[me].myMapY-mapy + 5)] = new Texture(new FileHandle(f));
					}
				}
			}
		}
	}

	public static void DrawHud(float delta) {
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudBatch.setProjectionMatrix(uiMatrix);
		hudShapeRenderer.setProjectionMatrix(uiMatrix);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		hudBatch.begin();
		
		Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY());
		int displaySkillStats = -1;
		if(cutscene != null || displayDialog || displayCrafting)
		{
			Sprite black = new Sprite(Content.black);
			int ticks = Math.min(100, Main.cutsceneBordersTicks * 4);
			black.setPosition(0, Gdx.graphics.getHeight() - ticks);
			black.setSize(Gdx.graphics.getWidth(), 100);
			black.draw(hudBatch);

			black.setPosition(0, 0);
			black.setSize(Gdx.graphics.getWidth(), ticks);
			black.draw(hudBatch);
		}
		else
		{
			Sprite sprite = null;

			float canvas = 64/camera.zoom;

			if(canvas > 128)
				canvas = 128;

			Vector2 drawPos = new Vector2(player[me].Center().x, player[me].Center().y);

			if(camera.zoom == Constant.MAPZOOM_CLOSE)
			{
				float val = Gdx.graphics.getWidth()*camera.zoom/2;
				if(player[me].Center().x < val)
				{
					float val2 = val - player[me].Center().x;
					drawPos.x -= val2;
				}
				val = worldMap.width * Tile.TILE_SIZE - (Gdx.graphics.getWidth()*camera.zoom/2);
				if(player[me].Center().x > val)
				{
					float val2 = player[me].Center().x - val;
					drawPos.x += val2;
				}

				val = Gdx.graphics.getHeight()*camera.zoom/2;
				if(player[me].Center().y < val)
				{
					float val2 = val - player[me].Center().y;
					drawPos.y -= val2;
				}
				val = worldMap.height * Tile.TILE_SIZE - (Gdx.graphics.getHeight()*camera.zoom/2);
				if(player[me].Center().y > val)
				{
					float val2 = player[me].Center().y - val;
					drawPos.y += val2;
				}
			}
			else if(camera.zoom == Constant.MAPZOOM_BIG)
			{
				float val = Gdx.graphics.getWidth()*camera.zoom/2;
				if(player[me].Center().x < val)
				{
					float val2 = val - player[me].Center().x;
					drawPos.x -= -val2/camera.zoom;
				}
				val = worldMap.width * Tile.TILE_SIZE - (Gdx.graphics.getWidth()*camera.zoom/2);
				if(player[me].Center().x > val)
				{
					float val2 = player[me].Center().x - val;
					drawPos.x += -val2/camera.zoom;
				}

				val = Gdx.graphics.getHeight()*camera.zoom/2;
				if(player[me].Center().y < val)
				{
					float val2 = val - player[me].Center().y;
					drawPos.y -= -val2/camera.zoom;
				}
				val = worldMap.height * Tile.TILE_SIZE - (Gdx.graphics.getHeight()*camera.zoom/2);
				if(player[me].Center().y > val)
				{
					float val2 = player[me].Center().y - val;
					drawPos.y += -val2/camera.zoom;
				}
			}
			else if(camera.zoom == Constant.MAPZOOM_HUGE)
			{
				float val = Gdx.graphics.getWidth()*camera.zoom/2;
				if(player[me].Center().x < val)
				{
					float val2 = val - player[me].Center().x;
					drawPos.x += val2/(1.334);
				}
				val = worldMap.width * Tile.TILE_SIZE - (Gdx.graphics.getWidth()*camera.zoom/2);
				if(player[me].Center().x > val)
				{
					float val2 = player[me].Center().x - val;
					drawPos.x -= val2/(1.334);
				}

				val = Gdx.graphics.getHeight()*camera.zoom/2;
				if(player[me].Center().y < val)
				{
					float val2 = val - player[me].Center().y;
					drawPos.y += val2/(1.334);
				}
				val = worldMap.height * Tile.TILE_SIZE - (Gdx.graphics.getHeight()*camera.zoom/2);
				if(player[me].Center().y > val)
				{
					float val2 = player[me].Center().y - val;
					drawPos.y -= val2/(1.334);
				}
			}

			Vector2 screenDraw = new Vector2(drawPos.x - cameraRealPosition2().x - canvas, drawPos.y - cameraRealPosition2().y - canvas);

			if(screenDraw.x < 0)
			{
				screenDraw.x = 0;
			}
			if(screenDraw.x + canvas * 2 > Gdx.graphics.getWidth())
			{
				screenDraw.x = Gdx.graphics.getWidth() - canvas * 2;
			}
			if(screenDraw.y < 0)
			{
				screenDraw.y = 0;
			}
			if(screenDraw.y + canvas * 2> Gdx.graphics.getHeight())
			{
				screenDraw.y = Gdx.graphics.getHeight() - canvas * 2;
			}

			int quant = 0;
			
			if(player[me].inventory[Constant.ITEMSLOT_LEFT] != null && player[me].inventory[Constant.ITEMSLOT_LEFT].damage > 0)
			{
				sprite = new Sprite(Content.slot);
				sprite.setPosition(16, 96 + (player[me].skillSlotAvailable >= 8 ? 68 : 0));
				sprite.draw(hudBatch);
				sprite = new Sprite(player[me].inventory[Constant.ITEMSLOT_LEFT].getInvTexture());
				sprite.setPosition(20, 100 + (player[me].skillSlotAvailable >= 8 ? 68 : 0));
				player[me].inventory[Constant.ITEMSLOT_LEFT].drawEnt(hudBatch, sprite);
			}
			if(player[me].inventory[Constant.ITEMSLOT_RIGHT] != null && player[me].inventory[Constant.ITEMSLOT_RIGHT].damage > 0)
			{
				sprite = new Sprite(Content.slot);
				sprite.setPosition(84, 96 + (player[me].skillSlotAvailable >= 8 ? 68 : 0));
				sprite.draw(hudBatch);
				sprite = new Sprite(player[me].inventory[Constant.ITEMSLOT_RIGHT].getInvTexture());
				sprite.setPosition(88, 100 + (player[me].skillSlotAvailable >= 8 ? 68 : 0));
				player[me].inventory[Constant.ITEMSLOT_RIGHT].drawEnt(hudBatch, sprite);
			}
			hudBatch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			hudBatch.begin();
			Monster boss = null;
			for (Monster m : monster) {
				if (m.active && m.boss && m.Center().dst(player[me].Center()) < 2000) {
					boss = m;
					break;
				}
			}

			if (boss != null) {
				sprite = new Sprite(Content.black);
				sprite.setPosition(Gdx.graphics.getWidth() - 32, 16);
				sprite.setSize(16, Gdx.graphics.getHeight() - 32);
				sprite.draw(hudBatch);
				sprite = new Sprite(Content.healthBar);
				sprite.rotate90(false);
				sprite.setPosition(Gdx.graphics.getWidth() - 32, 16);
				sprite.setSize(16, boss.getActualHealth(true) * (Gdx.graphics.getHeight()-32));
				sprite.draw(hudBatch);
			}
			
			if(player[me].displayQuest < player[me].quests.size() && player[me].displayQuest >= 0)
			{
				Quest dQ = player[me].quests.get(player[me].displayQuest);
				if(dQ != null)
				{
					int totalHeight = 0;

					font.getData().setScale(1f);
					cl.setText(font, dQ.name);;
					totalHeight += cl.height + 8;
					
					boolean fullConcluded = true;

					for(Objective o : dQ.objectives)
					{
						if(!o.concluded)
						{
							font.getData().setScale(0.5f);
							cl.setText(font, "- " + o.text, Color.BLACK, 300, -1, true);
							totalHeight += cl.height + 6;
							fullConcluded = false;
						}
						else
						{
							font.getData().setScale(0.5f);
							cl.setText(font, "- " + o.text + " (Concluded)", Color.BLACK, 300, -1, true);
							totalHeight += cl.height + 6;
						}
					}
					
					if(fullConcluded)
					{
						totalHeight += 12;
						NPC ex = new NPC();
						ex.SetInfos(dQ.npcQuesterId);
						font.getData().setScale(0.5f);
						cl.setText(font, "- Return to " + ex.name + " to continue the quest.", Color.BLACK, 300, -1, true);
						totalHeight += cl.height + 6;
					}
					/*sprite = new Sprite(Content.white);
					sprite.setPosition(Gdx.graphics.getWidth() - 348, Gdx.graphics.getHeight() * 0.75f - totalHeight);
					sprite.setSize(348, totalHeight + 24);
					sprite.setColor(0.8f, 0.8f, 0.25f, 1f);
					sprite.setAlpha(0.25f);
					sprite.draw(hudBatch);*/
					Main.drawGenericBox(hudBatch, Gdx.graphics.getWidth() - 348, Gdx.graphics.getHeight() * 0.75f - totalHeight, 360, totalHeight + 24);
					font.getData().setScale(1f);
					cl.setText(font, dQ.name);
					int extraY = 0;
					float scl = 300/Math.max(300, cl.width);
					Main.prettyFontDraw2(hudBatch, dQ.name, Gdx.graphics.getWidth() - 32 - 300,
							Gdx.graphics.getHeight()*0.75f + 8, scl, new Color(1f, 1f, 0.3f, 1f), Color.BLACK, 1f, false, 0, cl);

					extraY += cl.height + 8;

					for(Objective o : dQ.objectives)
					{
						if(!o.concluded)
						{
							Main.prettyFontDraw2(hudBatch, "- " + o.text, Gdx.graphics.getWidth() - 32 - 300,
									Gdx.graphics.getHeight()*0.75f + 8 - extraY, 0.5f, new Color(1f, 1f, 0.3f, 1f), Color.BLACK, 1f, false, 300, cl);
						}
						else
						{
							Main.prettyFontDraw2(hudBatch, "- " + o.text + " (Concluded)", Gdx.graphics.getWidth() - 32 - 300,
									Gdx.graphics.getHeight()*0.75f + 8 - extraY, 0.5f, new Color(0f, 1f, 0f, 1f), Color.BLACK, 1f, false, 300, cl);
						}

						extraY += cl.height + 6;
					}
					
					if(fullConcluded)
					{
						extraY += 12;

						NPC ex = new NPC();
						ex.SetInfos(dQ.npcQuesterId);
						Main.prettyFontDraw2(hudBatch, "- Return to " + ex.name + " to continue the quest.", Gdx.graphics.getWidth() - 32 - 300,
								Gdx.graphics.getHeight()*0.75f + 8 - extraY, 0.5f, new Color(1f, 1f, 0.3f, 1f), Color.BLACK, 1f, false, 300, cl);
						extraY += cl.height + 6;
					}
				}
			}

			if (debug)
			{
				Main.prettyFontDraw(hudBatch, "var{" + currentVar + "} = " + var[currentVar], 50, 50, 0.5f, new Color(1f, 1f, 0.3f, 1f), Color.BLACK, 1f, false, -1);
			}
		}

		if (blackScreenTime < 2f) {
			float a = 1f;
			if (blackScreenTime > 1f) {
				a = 2f - blackScreenTime;
			}
			if (blackScreenTime < -2f) {
				a = 3f - Math.abs(blackScreenTime);
			}
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Sprite screen = new Sprite(Content.black);
			screen.setAlpha(a);
			screen.setPosition(-25, -25);
			screen.setSize(Gdx.graphics.getWidth()+50,Gdx.graphics.getHeight()+50);
			screen.draw(hudBatch);
		}
		
		if(displaySkillStats != -1)
		{
			Skill skill = player[me].skills[displaySkillStats];
			skill.updateStoredInfos(player[me], skill.level);
			int offsetY = 0;
			float highestX = 300;
			float sumY = 0;

			font.getData().setScale(1f);
			layout.setText(font, skill.name);
			sumY += layout.height + 8;
			if (layout.width > highestX)
				highestX = layout.width;

			String manaCost = "No cost";
			if(skill.getMana(player[me]) > 0)
			{
				manaCost = skill.getMana(player[me]) + " mana";
			}
			String cdf = String.valueOf(skill.getCooldown(player[me])) + "s";
			if(skill.getCooldown(player[me]) <= 0f)
			{
				cdf = "No";
			}
			else if(Math.floor(skill.getCooldown(player[me])) == skill.getCooldown(player[me]))
			{
				cdf = String.valueOf((int)Math.floor(skill.getCooldown(player[me]))) + "s";
			}
			String cd = cdf + " cooldown";
			String mccd = manaCost + ", " + cd;
			font.getData().setScale(0.5f);
			layout.setText(font, mccd);
			sumY += layout.height + 8;
			if (layout.width > highestX)
				highestX = layout.width;

			if (skill.getDescription(player[me]).length() > 0) {
				font.getData().setScale(0.55f);
				layout.setText(font, skill.getDescription(player[me]), Color.BLACK, highestX, -1, true);
				sumY += layout.height + 16;
			}

			Main.drawGenericBox(hudBatch, mouse.x - 8 + 20, mouse.y + 32, highestX + 16, sumY + 8);
			/*hudShapeRenderer.begin(ShapeType.Filled);
			hudShapeRenderer.setColor(Color.GRAY);
			hudShapeRenderer.rect(mouse.x - 8 + 20, mouse.y + 32, highestX + 16, sumY + 8);
			hudShapeRenderer.end();*/

			// ----------------------------------------------
			//hudBatch.begin();
			font.getData().setScale(1f);
			Main.prettyFontDraw(hudBatch, skill.name, mouse.x + 20, mouse.y + 30 - offsetY + sumY, 1f, Color.YELLOW, Color.BLACK, 1f, false, (int)highestX);
			offsetY += layout.height + 8;

			Main.prettyFontDraw(hudBatch, mccd, mouse.x + 20, mouse.y + 30 - offsetY + sumY, 0.5f, Color.CYAN, Color.BLACK, 1f, false, (int)highestX);
			offsetY += layout.height + 8;

			if (skill.getDescription(player[me]).length() > 0) {
				Main.prettyFontDraw(hudBatch, skill.getDescription(player[me]), mouse.x + 20, mouse.y + 30 - offsetY + sumY, 0.55f, Color.WHITE, Color.BLACK, 1f, false, (int)highestX);
				offsetY += layout.height + 16;
			}
		}

		String hour;
		if (dayHour() < 10)
			hour = "0" + dayHour();
		else
			hour = "" + dayHour();

		String min;
		if (dayMinute() < 10)
			min = "0" + dayMinute();
		else
			min = "" + dayMinute();

		if (debug) {
			font.getData().setScale(0.5f);
			font.setColor(Color.WHITE);
			font.draw(hudBatch, hour + ":" + min + " (" + Gdx.graphics.getFramesPerSecond() + ")" + " " + lightcount + "/" + lighting.size(),
					Gdx.graphics.getWidth() - 200, 100);
                        
                        font.draw(hudBatch, "(" + player[me].myMapX + "," + player[me].myMapY + ")\n" + 
                        		"(" + (int)Main.player[me].position.x + "," + (int)Main.player[me].position.y + ")\n"
                        				+ "(" + (int)Main.mouseInWorld().x + "," + (int)Main.mouseInWorld().y + ") "
                        				+ "(" + (int)(Math.floor(Main.mouseInWorld().x/64)*64) + "," + (int)(Math.floor(Main.mouseInWorld().y/64)*64) + ") ",
                                Gdx.graphics.getWidth()/2f,
                                100);
		}
		if(player[me].skills[0] != null && player[me].skills[0].casts == 0 && player[me].skills[0].ticksFromCast2 < 15)
		{
			float alpha = (15-player[me].skills[0].ticksFromCast2)/15f;
			Sprite sprite = new Sprite(Content.white);
			sprite.setPosition(-100, -100);
			sprite.setColor(new Color(0.5f, 0f, 0.5f, 1f));
			sprite.setSize(Gdx.graphics.getWidth()+200, Gdx.graphics.getHeight()+200);
			sprite.setAlpha(alpha);
			sprite.draw(hudBatch);
		}
		if(Main.resetTime < 15)
		{
			float alpha = (15-Main.resetTime)/15f;
			Sprite sprite = new Sprite(Content.white);
			sprite.setPosition(-100, -100);
			sprite.setSize(Gdx.graphics.getWidth()+200, Gdx.graphics.getHeight()+200);
			sprite.setAlpha(alpha);
			sprite.draw(hudBatch);
		}
		hudBatch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		fbo = new FrameBuffer(Format.RGBA8888, width, height, false);
		fboRegion = new TextureRegion(fbo.getColorBufferTexture());
		fboRegion.getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		fboRegion.flip(false, true);
		camera.setToOrtho(false, width, height);
		if(usingShader.length() > 0)
		{
			shader.begin();
			shader.setUniformf("resolution", width, height);
			shader.end();
		}
		camera.update();
		if(worldMap != null)
		{
			FixCameraX();
			FixCameraY();
		}
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		this.dispose();
	}

	@Override
	public void dispose() {
		playing = false;
		System.out.println("Calling out dispose...");
		batch.dispose();
		fb.dispose();
		basicShader.dispose();
		hudBatch.dispose();
		mapBatch.dispose();
		shapeRenderer.dispose();
		hudShapeRenderer.dispose();
		
		GenerationLoader.saveGen();
		for(int y = 0;y < 10;y++)
		{
			for(int x = 0;x < 10;x++)
			{
				if(Main.worldMapImages[y][x] != null)
					Main.worldMapImages[y][x].dispose();
			}
		}
		SaveMap();
		if(!isOnline)
			PlayerLoader.savePlayer(player[me]);
		Main.loadKeysState();
	}

	public static Vector2 cameraRealPosition() {
		Vector2 result = new Vector2(camera.position.x - (Gdx.graphics.getWidth() * camera.zoom) / 2,
				camera.position.y - (Gdx.graphics.getHeight() * camera.zoom) / 2);
		return result;
	}

	public static Vector2 cameraRealPosition2() {
		Vector2 result = new Vector2(camera.position.x - (Gdx.graphics.getWidth()) / 2,
				camera.position.y - (Gdx.graphics.getHeight()) / 2);
		return result;
	}

	private float FixCameraX() {
		float adjustX = camera.position.x - (Gdx.graphics.getWidth() * camera.zoom) / 2;
		if(Gdx.graphics.getWidth() * camera.zoom >= Main.worldMap.getWidth() * Tile.TILE_SIZE)
		{
			if(Main.displayDialog || Main.cutscene != null)
			{
        		/*Vector2 dest = new Vector2(player[me].Center().x, player[me].Center().y - 30 * (1/camera.zoom));
	        	if(!dialog.playerDialog && dialogEntity != null)
	        		dest = new Vector2(dialogEntity.Center().x, dialogEntity.Center().y - 30 * (1/camera.zoom));
	        	
				camera.position.x += (dest.x - camera.position.x) / 8f;*/
			}
			else
			{
				camera.position.x = worldMap.getWidth() * Tile.TILE_SIZE/2;
			}
		}
		else if(worldMap.globalY < 300 || worldMap.globalY >= 400)
		{
			if (adjustX < 0f) {
				camera.position.x = (Gdx.graphics.getWidth() * camera.zoom) / 2f;
			}
			if (adjustX > (worldMap.getWidth() * Tile.TILE_SIZE) - (Gdx.graphics.getWidth() * camera.zoom)) {
				camera.position.x = (worldMap.getWidth() * Tile.TILE_SIZE) - (Gdx.graphics.getWidth() * camera.zoom) / 2;
			}
		}
		camera.update();
		return camera.position.x - player[me].position.x;
	}

	private float FixCameraY() {
		float adjustY = camera.position.y - (Gdx.graphics.getHeight() * camera.zoom) / 2;
		if(Gdx.graphics.getHeight() * camera.zoom >= Main.worldMap.getHeight() * Tile.TILE_SIZE)
		{
			if(Main.displayDialog || Main.cutscene != null)
			{
        		/*Vector2 dest = new Vector2(player[me].Center().x, player[me].Center().y - 30 * (1/camera.zoom));
	        	if(!dialog.playerDialog && dialogEntity != null)
	        		dest = new Vector2(dialogEntity.Center().x, dialogEntity.Center().y - 30 * (1/camera.zoom));
	        	
				camera.position.y += (dest.y - 100 - camera.position.y) / 8f;*/
			}
			else
			{
				camera.position.y = worldMap.getHeight() * Tile.TILE_SIZE/2;
			}
		}
		else if(player[me].myMapY < 300 || player[me].myMapY >= 400)
		{
			if (adjustY < 0f) {
				camera.position.y = (Gdx.graphics.getHeight() * camera.zoom) / 2f;
			}
			if (adjustY > (worldMap.getHeight() * Tile.TILE_SIZE) - (Gdx.graphics.getHeight() * camera.zoom)) {
				camera.position.y = (worldMap.getHeight() * Tile.TILE_SIZE) - (Gdx.graphics.getHeight() * camera.zoom) / 2;
			}
		}
		camera.update();
		return camera.position.y - player[me].position.y;
	}

	private void DrawCharacter(float delta) {
		characterX += (50f - characterX) / 8;
		skillAlpha += 0.05f;
		if (skillAlpha > 1) {
			skillAlpha = 1f;
		}
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		/*hudShapeRenderer.setProjectionMatrix(uiMatrix);
		hudShapeRenderer.begin(ShapeType.Filled);
		hudShapeRenderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1f));
		hudShapeRenderer.rect(characterX, Gdx.graphics.getBackBufferHeight() / 2f - 310f, 620f, 620f);
		hudShapeRenderer.setColor(new Color(0.6f, 0.6f, 0.6f, 1f));
		hudShapeRenderer.rect((characterX + 10), Gdx.graphics.getBackBufferHeight() / 2f - 300f, 600f, 600f);
		hudShapeRenderer.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
		hudShapeRenderer.rect((characterX + 30), Gdx.graphics.getBackBufferHeight() / 2f + 220f, 560f, 60f);
		hudShapeRenderer.rect((characterX + 30), Gdx.graphics.getBackBufferHeight() / 2f + 70f, 560f, 140f);
		hudShapeRenderer.rect((characterX + 30), Gdx.graphics.getBackBufferHeight() / 2f - 170f, 560f, 230f);
		hudShapeRenderer.rect((characterX + 30), Gdx.graphics.getBackBufferHeight() / 2f - 280f, 560f, 100f);
		hudShapeRenderer.end();*/
		hudBatch.setProjectionMatrix(uiMatrix);
		hudBatch.begin();
		Sprite box = new Sprite(Content.charBox);
		box.setPosition(characterX, Gdx.graphics.getBackBufferHeight()/2f-310f);
		box.draw(hudBatch);
		box.setTexture(Content.charBoxF);
		box.draw(hudBatch);

		Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		float currentY = Gdx.graphics.getBackBufferHeight() / 2f + 270f;
		String text = Main.saveName;
		cl.setText(font, text);
		Main.prettyFontDraw2(hudBatch, text, (characterX + 30) + 270, currentY, 1f, true, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, 560, cl.height)).contains(mouse)) {
			text = Main.lv("This is your name.\n"
					+ "You rock.", "Este é o seu nome.\nVocê é demais.");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		currentY -= cl.height * 1.4f;

		text = player[me].getClassName();
		Main.prettyFontDraw2(hudBatch, text, (characterX + 30) + 270, currentY, 0.5f, 1f, true, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, 560, cl.height)).contains(mouse)) {
			text = Main.lv("This is how the NPCs know you by.\n"
					+ "Your actual class is " + player[me].classType.name + ".",
					"Isto é como os NPCs te conhecem por.\n"
					+ "Sua classe atual é " + player[me].classType.name + ".");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}

		currentY = Gdx.graphics.getBackBufferHeight() / 2f + 200f;

		int plusOffset = 285;

		text = Main.lv("Strength: ", "Força: ") + player[me].strength + " (" + player[me].estrength + ")";
		Main.prettyFontDraw2(hudBatch, text, (characterX + 40), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Increases your melee and ranged weapons damage\n by 2% for every 5 strength points\n"
					+ "Increases your vitality by 1 for every 4 strength points",
					"Aumenta seu dano com armas corpo-a-corpo e de alcance em 2%\n para cada 5 pontos de força.\n"
					+ "Aumenta sua vitalidade em 1 para cada 4 pontos de força");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		if (player[me].characterPoints > 0) {
			Main.prettyFontDraw2(hudBatch, "(+)", (characterX + plusOffset), currentY, 1f, Color.GREEN, Color.BLACK, 1f,
					false, 0, cl);
			if ((new Rectangle(characterX + plusOffset, currentY - cl.height, cl.width, cl.height))
					.contains(mouse) && mouseJustPressed) {
				int times = 1;
				if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				{
					times = 10;
				}
				for(int i = 0;i < times;i++)
				{
					if(player[me].characterPoints <= 0)
						break;

					player[me].strength++;
					player[me].characterPoints--;
				}
			}
		}
		currentY -= cl.height * 1.4f;

		text = Main.lv("Intelligence: ", "Inteligência: ") + player[me].intelligence + " (" + player[me].eintelligence + ")";
		Main.prettyFontDraw2(hudBatch, text, (characterX + 40), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Increases your elemental damage and magical weapons damage\n by 2% for every 5 intelligence points\n"
					+ "Increases mana regeneration by 0.1m/s per point",
					"Aumenta seu dano elemental e com armas mágicas em 2%\n para cada 5 pontos de inteligência\n"
					+ "Aumenta sua regeneração de mana em 0.1m/s por ponto");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		if (player[me].characterPoints > 0) {
			Main.prettyFontDraw2(hudBatch, "(+)", (characterX + plusOffset), currentY, 1f, Color.GREEN, Color.BLACK, 1f,
					false, 0, cl);
			if ((new Rectangle(characterX + plusOffset, currentY - cl.height, cl.width, cl.height))
					.contains(mouse) && mouseJustPressed) {
				int times = 1;
				if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				{
					times = 10;
				}
				for(int i = 0;i < times;i++)
				{
					if(player[me].characterPoints <= 0)
						break;

					player[me].intelligence++;
					if(player[me].classType == ArkaClass.ARCANETRICKSTER)
					{
						player[me].intelligence++;
					}
					player[me].characterPoints--;
				}
			}
		}
		currentY -= cl.height * 1.4f;

		text = Main.lv("Lethality: ", "Letalidade: ") + player[me].lethality + " (" + player[me].elethality + ")";
		Main.prettyFontDraw2(hudBatch, text, (characterX + 40), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Lethality increases your Physical Power and Magic Power by 0.5 per point\n"
					+ "Increases your Physical Power and Magic Power by 0.75% per point\n"
					+ "Increases critical hit chance by 0.15% per point\n"
					+ "Increases critical hit damage by 0.25% per point\n",
					"Letalidade aumenta seu Poder Físico e Poder Mágico em 0.5 por ponto\n"
					+ "Aumenta seu Poder Físico e Poder Mágico em 0.75% por ponto\n"
					+ "Aumenta sua chance de acerto crítico em 0.15% por ponto\n"
					+ "Aumenta seu dano de acerto crítico em 0.25% por ponto\n");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		if (player[me].characterPoints > 0) {
			Main.prettyFontDraw2(hudBatch, "(+)", (characterX + plusOffset), currentY, 1f, Color.GREEN, Color.BLACK, 1f,
					false, 0, cl);
			if ((new Rectangle(characterX + plusOffset, currentY - cl.height, cl.width, cl.height))
					.contains(mouse) && mouseJustPressed) {
				int times = 1;
				if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				{
					times = 10;
				}
				for(int i = 0;i < times;i++)
				{
					if(player[me].characterPoints <= 0)
						break;

					player[me].lethality++;
					player[me].characterPoints--;
				}
			}
		}
		currentY -= cl.height * 1.4f;

		currentY = Gdx.graphics.getBackBufferHeight() / 2f + 200f;

		text = Main.lv("Agility: ", "Agilidade: ") + player[me].agility + " (" + player[me].eagility + ")";
		Main.prettyFontDraw2(hudBatch, text, (characterX + 350), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 350, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Agility increases your attack speed by 0.75% per point\n"
					+ "Increases your movement speed by 1 per point\n"
					+ "Increases your max stamina by 1 every 3 points\n",
					"Agilidade aumenta sua velocidade de ataque em 0.75% por ponto\n"
					+ "Aumenta sua velocidade de movimento em 1 por ponto\n"
					+ "Aumenta sua energia máxima em 1 para cada 3 pontos\n");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		if (player[me].characterPoints > 0) {
			Main.prettyFontDraw2(hudBatch, "(+)", (characterX + plusOffset * 2 - 40), currentY, 1f, Color.GREEN,
					Color.BLACK, 1f, false, 0, cl);
			if ((new Rectangle(characterX + plusOffset * 2 - 40, currentY - cl.height, cl.width, cl.height))
					.contains(mouse) && mouseJustPressed) {
				int times = 1;
				if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				{
					times = 10;
				}
				for(int i = 0;i < times;i++)
				{
					if(player[me].characterPoints <= 0)
						break;

					player[me].agility++;
					player[me].characterPoints--;
				}
			}
		}
		currentY -= cl.height * 1.4f;

		text = Main.lv("Luck: ", "Sorte: ") + player[me].luck + " (" + player[me].eluck + ")";
		Main.prettyFontDraw2(hudBatch, text, (characterX + 350), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 350, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Luck increases your critical hit chance by 0.4% per point\n"
					+ "Increases your overall chances by +0.5% per point (Only base luck)\n"
					+ "Increases the chance of dropping rarer items by 2% per point",
					"Sorte aumenta sua chance de acerto crítico em 0.4% por ponto\n"
					+ "Aumenta todas as suas chances em +0.5% por ponto (Apenas sorte base)\n"
					+ "Aumenta as chances de dropar itens raros em 2% por ponto\n");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		if (player[me].characterPoints > 0) {
			Main.prettyFontDraw2(hudBatch, "(+)", (characterX + plusOffset * 2 - 40), currentY, 1f, Color.GREEN,
					Color.BLACK, 1f, false, 0, cl);
			if ((new Rectangle(characterX + plusOffset * 2 - 40, currentY - cl.height, cl.width, cl.height))
					.contains(mouse) && mouseJustPressed) {
				int times = 1;
				if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				{
					times = 10;
				}
				for(int i = 0;i < times;i++)
				{
					if(player[me].characterPoints <= 0)
						break;

					player[me].luck++;
					player[me].characterPoints--;
				}
			}
		}
		currentY -= cl.height * 1.4f;

		text = Main.lv("Vitality: ", "Vitalidade: ") + player[me].vitality + " (" + player[me].evitality + ")";
		Main.prettyFontDraw2(hudBatch, text, (characterX + 350), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 350, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Vitality increases your max health by 3 per point\n"
					+ "Increases max health by 0.2% per point\n"
					+ "Increases your health regeneration by 0.05h/s per point\n",
					"Vitalidade aumenta sua vida máxima em 3 por ponto\n"
					+ "Aumenta sua vida máxima em 0.2% por ponto\n"
					+ "Aumenta sua regeneração de vida em 0.05v/s por ponto");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		if (player[me].characterPoints > 0) {
			Main.prettyFontDraw2(hudBatch, "(+)", (characterX + plusOffset * 2 - 40), currentY, 1f, Color.GREEN,
					Color.BLACK, 1f, false, 0, cl);
			if ((new Rectangle(characterX + plusOffset * 2 - 40, currentY - cl.height, cl.width, cl.height))
					.contains(mouse) && mouseJustPressed) {
				int times = 1;
				if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				{
					times = 10;
				}
				for(int i = 0;i < times;i++)
				{
					if(player[me].characterPoints <= 0)
						break;

					player[me].vitality++;
					player[me].characterPoints--;
				}
			}
		}
		currentY -= cl.height * 1.4f;

		text = Main.lv("Points: ", "Pontos: ") + player[me].characterPoints;
		Main.prettyFontDraw2(hudBatch, text, (characterX + 280), currentY, 1f, true, cl);
		if ((new Rectangle(characterX + 235, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("These are the points you have to distribute\n"
					+ "You get 5 of them every level up\n"
					+ "Hold CTRL to add 10 points at once",
					"Estes são os pontos que você tem para distribuir\n"
					+ "Você consegue 5 deles a cada nível\n"
					+ "Segure CTRL para adicionar 10 de uma vez");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}

		currentY = Gdx.graphics.getHeight() / 2 + 50;

		text = Main.lv("Level: ", "Nível: ") + player[me].level;
		Main.prettyFontDraw2(hudBatch, text, (characterX + 40), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("This is your level\n"
					+ "You experience is: ",
					"Este é seu nível\n"
					+ "Sua experiência é: ") + player[me].experience + "/" + player[me].nextLevelExp();
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		currentY -= cl.height * 1.4f;

		String attspeed = String.format("%.1f", player[me].getAttackSpeed() * 100f);
		text = Main.lv("Attack Speed: ", "Velocidade de Ataque: ") + attspeed + "%";
		Main.prettyFontDraw2(hudBatch, text, (characterX + 40), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Attack speed defines the cooldown of your weapons\n"
					+ "At 200% attack speed your weapon animation is faster\n"
					+ "The attack speed limit is 300%\n"
					+ "Can be increased with agility and items overall",
					"Velocidade de ataque define o tempo de recarga de suas armas\n"
					+ "Em 200% de velocidade de ataque sua animação de ataque é acelerada\n"
					+ "O limite de velocidade de ataque é de 300%\n"
					+ "Pode ser aumentado com agilidade e itens em geral");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		currentY -= cl.height * 1.4f;

		attspeed = String.format("%.1f", Math.min(player[me].criticalChance, 100));
		text = Main.lv("Critical Hit Chance: ", "Chance de Acerto Crítico: ") + attspeed + "%";
		Main.prettyFontDraw2(hudBatch, text, (characterX + 40), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Critical hit chance is the chance for you to get a random critical hit\n"
					+ "Your critical hit damage multiplier is: " + (int)(player[me].critMult * 100) + "%\n"
					+ "Can be increased with lethality, luck and items overall",
					"Chance de acerto crítico é a chance que você tem de obter\n acertos críticos aleatórios\n"
					+ "Seu multiplicador de dano crítico é: " + (int)(player[me].critMult * 100) + "%\n"
							+ "Pode ser aumentado com letalidade, sorte e itens em geral");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		currentY -= cl.height * 1.4f;

		attspeed = String.format("%.1f", player[me].lifeSteal * 100);
		text = Main.lv("Life Steal: ", "Roubo de Vida: ") + attspeed + "%";
		Main.prettyFontDraw2(hudBatch, text, (characterX + 40), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Life steal defines the % of your damage that is converted as health for you\n"
					+ "It can be any source of damage\n" + "Can be increased with items overall",
					"Roubo de vida define quantos % do seu dano é convertido em vida para você\n"
					+ "Pode ser qualquer fonte de dano\n"
					+ "Pode ser aumentado com itens em geral");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		currentY -= cl.height * 1.4f;

		attspeed = String.format("%d", (player[me].getPP()));
		text = Main.lv("Physical Power: ", "Poder Físico: ") + attspeed;
		Main.prettyFontDraw2(hudBatch, text, (characterX + 40), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Your physical power is used to calculate the values of some skills\n"
					+ "Your PP is calculated as the sum of half of the damage given by\n"
					+ "equipped melee or ranged weapons summed up with half of your strength and lethality\n"
					+ "Each point in lethality increases your PP by 0.75%\n"
				+ "Also, your physical damage multiplier is: " + (player[me].getPhysicalDamageMult() * 100) + "%",
				"Seu poder físico é usado para calcular os valores de algumas habilidades\n"
				+ "Seu PF é calculado com a soma da metade do seu dano obtido por armas\n "
				+ "corpo-a-corpo e de alcance somado com metade da sua força e letalidade\n"
				+ "Cada ponto em letalidade aumenta seu PF em 0.75%\n"
				+ "Aliás, seu multiplicador de dano físico é: " + (player[me].getPhysicalDamageMult() * 100) + "%");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		currentY -= cl.height * 1.4f;

		attspeed = String.format("%d", (player[me].getMP()));
		text = Main.lv("Magic Power: ", "Poder Mágico: ") + attspeed;
		Main.prettyFontDraw2(hudBatch, text, (characterX + 40), currentY, 1f, false, cl);
		if ((new Rectangle(characterX + 40, currentY - cl.height, cl.width, cl.height)).contains(mouse)) {
			text = Main.lv("Your magic power is used to calculate the values of some skills\n"
					+ "Your MP is calculated as the sum of half of the damage given by\n"
					+ "equipped magic weapons summed up with half of your intelligence and lethality\n"
					+ "Each point in lethality increases your MP by 0.75%\n"
					+ "Also, your elemental damage multiplier is: " + (player[me].getElementalDamageMult() * 100) + "%",
						"Seu poder mágico é usado para calcular os valores de algumas habilidades\n"
					+ "Seu PM é calculado com a soma da metade do seu dano obtido por armas\n "
					+ "mágicas somado com metade da sua inteligência e letalidade\n"
					+ "Cada ponto em letalidade aumenta seu PM em 0.75%\n"
					+ "Aliás, seu multiplicador de dano elemental é: " + (player[me].getElementalDamageMult() * 100) + "%");
			Main.prettyFontDraw(hudBatch, text, (characterX + 40), Gdx.graphics.getBackBufferHeight() / 2f - 190f, 0.5f,
					Color.WHITE, Color.BLACK, 1f, false, 0);
		}
		currentY -= cl.height * 1.4f;

		hudBatch.end();
	}

	private void DrawDialog(float delta)
	{
		Vector2 screenPos = new Vector2(100, 32);

		boolean openedCol = false;
		if(dialogTicks < Main.dialog.text.length() && Main.dialog.text.charAt(dialogTicks) == '[')
			openedCol = true;
		
		dialogTicks++;

		if(openedCol)
		{
			while(dialogTicks < Main.dialog.text.length())
			{
				dialogTicks++;
				if(Main.dialog.text.toCharArray()[dialogTicks-1] == ']')
					break;
			}
		}
		
		boolean quickOption = false;
		if(dialogTicks >= Main.dialog.text.length() && dialogTicks >= Main.dialog.text.length() + 15 && Gdx.input.isKeyJustPressed(Keys.Z))
		{
			quickOption = true;
		}
		else if(dialogTicks > 1 && dialogTicks < Main.dialog.text.length() && Gdx.input.isKeyJustPressed(Keys.Z))
		{
			dialogTicks = Main.dialog.text.length();
		}
		Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		skillAlpha += 0.05f;
		if (skillAlpha > 1) {
			skillAlpha = 1f;
		}
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudBatch.setProjectionMatrix(uiMatrix);
		/*hudShapeRenderer.setProjectionMatrix(uiMatrix);
		hudShapeRenderer.begin(ShapeType.Filled);
		hudShapeRenderer.setColor(new Color(0.4f, 0.4f, 0.4f, 1f));
		hudShapeRenderer.rect(screenPos.x, screenPos.y, Gdx.graphics.getWidth() - 200, 350);
		hudShapeRenderer.end();*/
		hudBatch.begin();
		Sprite reg = new Sprite(Content.dialogBox);
		reg.setPosition(screenPos.x, screenPos.y);
		reg.setRegion(0, 0, 16, 350);
		reg.setSize(16, 350);
		reg.draw(hudBatch);
		reg.setPosition(screenPos.x+16, screenPos.y);
		reg.setRegion(16, 0, 284, 350);
		reg.setSize(Gdx.graphics.getWidth()-232, 350);
		reg.draw(hudBatch);
		reg.setRegion(300, 0, 16, 350);
		reg.setSize(16, 350);
		reg.setPosition(screenPos.x+Gdx.graphics.getWidth()-216, screenPos.y);
		reg.draw(hudBatch);
		int currentChar = Math.min(Main.dialogTicks, Main.dialog.text.length());
		if(currentChar == Main.dialog.text.length())
		{
			dialogFullDrawn = true;
		}
		String text = Main.dialog.text.substring(0, currentChar);
		float currentY = screenPos.y + 290;
		float drawSize = 330;
		Sprite port = null;
		if(Main.dialog.playerDialog)
		{
			Player player = new Player();
			player.inventory = Main.player[me].inventory;
			player.position = new Vector2(screenPos.x + 24, screenPos.y + 242);
			player.vanity = Main.player[me].vanity;
			player.preDraw(hudBatch);
			Main.DrawPlayer(hudBatch, player, false);
			player.postDraw(hudBatch);
			hudBatch.setShader(Main.basicShader);
		}
		else if(dialogEntity != null && dialogEntity.getClass() == NPC.class)
		{
			port = new Sprite(Content.npc[((NPC)dialogEntity).id-1]);
		}
		hudBatch.end();
		
		if(port != null || dialog.playerDialog)
		{
			hudBatch.begin();
			String nameString = (Main.dialog.playerDialog ? player[me].name : dialogEntity.name);
			if(Gdx.graphics.getWidth() > 900 && 
					Main.dialog != null && 
					!Main.dialog.playerDialog && 
					dialogEntity != null && 
					dialogEntity.getClass() == NPC.class &&
					((NPC)dialogEntity).title != null &&
					((NPC)dialogEntity).title.length() > 0)
			{
				nameString += ", " + ((NPC)dialogEntity).title;
			}
			Main.prettyFontDraw(hudBatch, nameString, screenPos.x + 100, screenPos.y + 300, 1.5f, Color.WHITE, Color.BLACK, 1f, false, Gdx.graphics.getWidth() - 230);

			currentY -= layout.height + 15;
			drawSize -= layout.height + 15 + 35;
			if(!dialog.playerDialog)
			{
				if(dialogEntity.getClass() == NPC.class)
				{
					NPC n = (NPC)dialogEntity;
					NPC clone = new NPC();
					clone.SetInfos(n.id);
					clone.position = new Vector2(screenPos.x + 20, screenPos.y + 310 -64+9);
					clone.currentFrame = n.currentFrame;
					clone.infos = n.infos;
					clone.draw(delta, hudBatch);
				}
			}
			hudBatch.end();
		}

		hudBatch.begin();
		reg = new Sprite(port != null || dialog.playerDialog ? Content.dialogBoxF : Content.dialogBoxF2);
		reg.setPosition(screenPos.x, screenPos.y);
		reg.setRegion(0, 0, 16, 350);
		reg.setSize(16, 350);
		reg.draw(hudBatch);
		reg.setPosition(screenPos.x+16, screenPos.y);
		reg.setRegion(16, 0, 284, 350);
		reg.setSize(Gdx.graphics.getWidth()-232, 350);
		reg.draw(hudBatch);
		reg.setRegion(300, 0, 16, 350);
		reg.setSize(16, 350);
		reg.setPosition(screenPos.x+Gdx.graphics.getWidth()-216, screenPos.y);
		reg.draw(hudBatch);
		//currentY
		Main.prettyFontDraw(hudBatch, text, screenPos.x + 20, screenPos.y + drawSize-4, 9f/12f, Color.WHITE, Color.BLACK, 1f, false, Gdx.graphics.getWidth() - 230);
		currentY -= layout.height + 10;
		hudBatch.end();
		
		if(dialog.canExit && Gdx.input.isKeyJustPressed(Keys.ESCAPE)/* && Main.cutscene == null*/)
		{
			Main.displayDialog = false;
		}

		if(Main.dialogFullDrawn)
		{
			int count = 0;
			for(int i = 0;i < 4;i++)
			{
				if(dialog.options[i] != null && dialog.options[i].length() > 0)
				{
					count++;
				}
			}
			boolean drawOptions = count > 1;
			hudBatch.begin();
			if(drawOptions)
			{
				/*hudShapeRenderer.begin(ShapeType.Filled);
				hudShapeRenderer.setColor(new Color(1f, 1f, 0.5f, 1f));
				hudShapeRenderer.rectLine(screenPos.x+43, currentY, Gdx.graphics.getWidth()-143, currentY, 1f);
				hudShapeRenderer.end();*/
				float lw = Gdx.graphics.getWidth()-286f;
				int ticks = Main.dialogTicks - Main.dialog.text.length();
				float sin = 1f-(float)Math.pow(0.93f, ticks);
				Sprite line = new Sprite(Content.white);
				line.setPosition(Gdx.graphics.getWidth()/2f-(lw*sin)/2f, currentY);
				line.setSize(lw*sin, 1f);
				line.setColor(new Color(1f, 1f, 0.5f, 1f));
				line.draw(hudBatch);
				currentY -= 10;
			}

			float currentX = screenPos.x + 20;
			if(quickOption && Main.cutscene == null)
			{
				dialog.doAction(0);
			}
			else if(drawOptions)
			{
				for(int i = 0;i < 4;i++)
				{
					if(dialog.options[i] != null && dialog.options[i].length() <= 0)
						continue;
					
					if(i == 2)
					{
						currentY += (layout.height + 10) * 2;
						currentX += (Gdx.graphics.getWidth()-200)/2f;
					}
	
					Main.prettyFontDraw(hudBatch, dialog.options[i], currentX, currentY, 1f, Color.WHITE, Color.BLACK, 1f, false, Gdx.graphics.getWidth() - 180);
					if ((new Rectangle(currentX, currentY-layout.height, layout.width, layout.height)).contains(mouse) && Main.cutscene == null) {
						Main.prettyFontDraw(hudBatch, dialog.options[i], currentX, currentY, 1f, Color.GREEN, Color.BLACK, 1f, false, Gdx.graphics.getWidth() - 180);
						if(mouseJustPressed && Main.cutscene == null)
						{
							dialog.doAction(i);
							break;
						}
					}
					currentY -= layout.height + 10;
				}
			}
			hudBatch.end();
		}
	}
	
	private void DrawCraftScreen(float delta)
	{
		craftingY += (0f - craftingY) / 8;
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		hudBatch.begin();
		/*hudShapeRenderer.begin(ShapeType.Filled);
		hudShapeRenderer.setColor(0.4f, 0.4f, 0.4f, 1f);
		hudShapeRenderer.rect(width/2f - 600, height/2f-330+craftingY, 500, 660);
		hudShapeRenderer.rect(width/2f + 100, height/2f-330-craftingY, 500, 660);
		hudShapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
		hudShapeRenderer.rect(width/2f - 590, height/2f-320+craftingY, 480, 640);
		hudShapeRenderer.rect(width/2f + 110, height/2f-320-craftingY, 480, 640);*/
		Main.drawGenericBox(hudBatch, width/2f - 600, height/2f-330+craftingY, 500, 660);
		Main.drawGenericBox(hudBatch, width/2f + 100, height/2f-330-craftingY, 500, 660);
		Main.drawGenericBox(hudBatch, width/2f - 590, height/2f-320+craftingY, 480, 640);
		Main.drawGenericBox(hudBatch, width/2f + 110, height/2f-320-craftingY, 480, 640);
		ArrayList<Recipe> playerRecipeList = new ArrayList<Recipe>();
		ArrayList<Recipe> fullList = new ArrayList<Recipe>();
		for(Recipe r : Recipe.values())
		{
			if(r.craftStation == Main.displayCraftStation)
			{
				fullList.add(r);
				if(player[me].learnedRecipes.contains(r))
					playerRecipeList.add(r);
			}
		}
		ArrayList<Recipe> recipeSequence = new ArrayList<Recipe>();
		ArrayList<Recipe> list2 = new ArrayList<Recipe>();
		list2.addAll(fullList);
		list2.removeAll(playerRecipeList);
		recipeSequence.addAll(playerRecipeList);
		recipeSequence.addAll(list2);
		Item displayInfo = null;
		int x = 1;
		for(int i = displayCraftingOffset;i < 8+displayCraftingOffset;i++)
		{
			try
			{
				Rectangle hitBox = new Rectangle(width/2f - 590, height/2f+320-80*(x)+craftingY, 480, 80);
				Texture txt = Content.genericBox;
				if(hitBox.contains(new Vector2(Gdx.input.getX(), height-Gdx.input.getY())) && playerRecipeList.contains(recipeSequence.get(i)))
				{
					txt = Content.genericBox2;
					if(mouseJustPressed)
					{
						Main.displayCraftingRecipe = recipeSequence.get(i);
					}
				}
				if(!playerRecipeList.contains(recipeSequence.get(i)))
				{
					txt=  Content.genericBox3;
				}
				Main.drawBoxedSprite(hudBatch, txt, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
				x++;
			}catch(Exception ex) {}
		}
		//hudShapeRenderer.end();
		//hudBatch.begin();
		x=1;
		for(int i = displayCraftingOffset;i < 8+displayCraftingOffset;i++)
		{
			try
			{
				Recipe r = recipeSequence.get(i);
				Item item = new Item().SetInfos(r.itemid);
				Rectangle hitbox = new Rectangle(width/2f - 590+4, height/2f+320-80*x+4+craftingY, 72, 72);
				Sprite s = new Sprite(Content.slot);
				s.setPosition(width/2f - 590+4, height/2f+320-80*x+4+craftingY);
				s.draw(hudBatch);
				s = new Sprite(item.getInvTexture());
				s.setPosition(width/2f - 590+8, height/2f+320-80*x+8+craftingY);
				if(!playerRecipeList.contains(r))
					s.setColor(Color.BLACK);

				item.drawEnt(hudBatch, s);
				Main.prettyFontDraw(hudBatch, item.name.substring(0, Math.min(item.name.length(), 25)), width/2f - 508, height/2f+320-80*x+72+craftingY, 1f, Constant.QUALITYCOLOR[item.quality], Color.BLACK, 1f, false, -1);
				String title = Main.getItemTitle(item);
				Main.prettyFontDraw(hudBatch, title, width/2f - 508, height/2f+320-80*x+40+craftingY, 0.5f, Constant.QUALITYCOLOR[item.quality], Color.BLACK, 1f, false, -1);
				if(hitbox.contains(new Vector2(Gdx.input.getX(), height-Gdx.input.getY())))
				{
					displayInfo = item;
				}
				x++;
			}
			catch(Exception ex) {}
		}
		
		if(displayCraftingRecipe != null)
		{
			//width/2f + 100, height/2f-350, 500, 700
			Item item = new Item().SetInfos(displayCraftingRecipe.itemid);
			Sprite s = new Sprite(Content.slot);
			s.setPosition(width/2f+114, height/2f+244-craftingY);
			s.draw(hudBatch);
			s = new Sprite(item.getInvTexture());
			s.setPosition(width/2f+118, height/2f+248-craftingY);
			item.drawEnt(hudBatch, s);
			Main.prettyFontDraw(hudBatch, item.name.substring(0, Math.min(item.name.length(), 25)), width/2f+196, height/2f+312-craftingY, 1f, Constant.QUALITYCOLOR[item.quality], Color.BLACK, 1f, false, -1);
			String title = Main.getItemTitle(item);
			Main.prettyFontDraw(hudBatch, title, width/2f+196, height/2f+282-craftingY, 0.5f, Constant.QUALITYCOLOR[item.quality], Color.BLACK, 1f, false, -1);
			x = 0;
			boolean haveAll=true;
			for(int i : displayCraftingRecipe.getMaterials())
			{
				Rectangle hitbox = new Rectangle(width/2f+114+80*x, height/2f+154-craftingY, 72, 72);
				Item mat = new Item().SetInfos(i);
				mat.stacks = displayCraftingRecipe.getMaterialCount(i);
				s = new Sprite(Content.slot);
				s.setPosition(width/2f+114+80*x, height/2f+154-craftingY);
				s.draw(hudBatch);
				s = new Sprite(mat.getInvTexture());
				s.setPosition(width/2f+118+80*x, height/2f+158-craftingY);
				mat.drawEnt(hudBatch, s);
				Color color = Color.GREEN;
				if(player[me].getItemQuantity(i) < mat.stacks)
				{
					color = Color.RED;
					haveAll = false;
				}
				Main.prettyFontDraw(hudBatch, player[me].getItemQuantity(i)+"/"+mat.stacks, width/2f+118+80*x, height/2f+171-craftingY, 0.5f, color, Color.BLACK, 1f, false, -1);
				if(hitbox.contains(new Vector2(Gdx.input.getX(), height-Gdx.input.getY())))
				{
					displayInfo = mat;
				}
				x++;
			}
			
			font.getData().setScale(1f);
			cl.setText(font, "Craft");
			Rectangle r = new Rectangle(width/2f+120, height/2f-280-cl.height-craftingY, cl.width, cl.height);
			Color color = Color.WHITE;
			if(r.contains(new Vector2(Gdx.input.getX(), height-Gdx.input.getY())))
			{
				color = Color.GREEN;
				if(mouseJustPressed)
				{
					if(!haveAll)
					{
						Main.lastQuestTicks=0;
						Main.lastQuestText = Main.lv("Insufficient material", "Material insuficiente");
					}
					else
					{
						int slot = -1;
						for(int i = 0;i < Constant.ITEMSLOT_OFFSET;i++)
						{
							if(player[me].inventory[i] == null)
							{
								slot = i;
								break;
							}
						}
						if(slot != -1)
						{
							player[me].inventory[slot] = item;
							for(int i : displayCraftingRecipe.getMaterials())
							{
								player[me].removeItem(i, displayCraftingRecipe.getMaterialCount(i));
							}
							Main.lastQuestTicks=0;
							Main.lastQuestText = item.name+Main.lv(" crafted", " construido");
						}
						else
						{
							Main.lastQuestTicks=0;
							Main.lastQuestText = Main.lv("No inventory slot available", "Não há espaço no inventário");
						}
					}
				}
			}
			Main.prettyFontDraw(hudBatch, "Craft", r.x, r.y+cl.height-craftingY, 1f, color, Color.BLACK, 1f, false, -1);
		}
		else
		{
			Main.prettyFontDraw(hudBatch, Main.lv("No item selected.", "Nenhum item foi selecionado."), width/2f + 350, height/2f+6-craftingY, 1f, Color.WHITE, Color.BLACK, 1f, true, -1);
		}
		hudBatch.end();
		if(displayInfo != null)
			Main.displayItemStats(displayInfo, new Vector2(Gdx.input.getX()+400,Gdx.graphics.getHeight()-Gdx.input.getY()));
	}
	
	public void DrawSkillList(float delta)
	{
		hudBatch.begin();
		/*hudShapeRenderer.begin(ShapeType.Filled);
		hudShapeRenderer.setColor(0.4f, 0.4f, 0.4f, 1f);
		hudShapeRenderer.rect(Gdx.graphics.getWidth()/2f - 410, Gdx.graphics.getHeight()/2f-330, 400, 660);
		hudShapeRenderer.rect(Gdx.graphics.getWidth()/2f + 10, Gdx.graphics.getHeight()/2f-330, 400, 660);*/
		Main.drawGenericBox(hudBatch, Gdx.graphics.getWidth()/2f - 410, Gdx.graphics.getHeight()/2f-330, 820, 660);
		Object[] sklist = player[me].learnedSkills.toArray();
		Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		for(int i = displaySkillOffset;i < Math.min(displaySkillOffset + 8, sklist.length);i++)
		{
			if(sklist[i] != null)
			{
				Skill skill = (Skill)sklist[i];
				//float tone = (i % 2 == 0 ? 0.5f : 0.6f);
				Texture txt = Content.genericBox;
				int drawPos = i - displaySkillOffset;
				Rectangle hitBox = new Rectangle(Gdx.graphics.getWidth()/2f - 400, Gdx.graphics.getHeight()/2f+240 - drawPos * 79-4, 390, 80);
				if(hitBox.contains(mouse))
				{
					txt = Content.genericBox2;
					if(mouseJustPressed)
					{
						displaySkillInspecting = player[me].learnedSkills.get(player[me].learnedSkills.indexOf(skill));
						displaySkillInspectingLevel = displaySkillInspecting.level;
						displaySkillInspecting.updateStoredInfos(player[me], displaySkillInspecting.level);
					}
				}
				//hudShapeRenderer.setColor(tone, tone, tone, 1f);
				//hudShapeRenderer.rect(Gdx.graphics.getWidth()/2f - 400, Gdx.graphics.getHeight()/2f+240 - drawPos * 80, 390, 80);
				Main.drawBoxedSprite(hudBatch, txt, Gdx.graphics.getWidth()/2f - 400, Gdx.graphics.getHeight()/2f+240 - drawPos * 79-4, 390, 80);
			}
		}
		
		if(displaySkillInspecting != null)
		{
			/*hudShapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1f);
			hudShapeRenderer.rect(Gdx.graphics.getWidth()/2f + 20, Gdx.graphics.getHeight()/2f+230, 380, 90);
			hudShapeRenderer.rect(Gdx.graphics.getWidth()/2f + 20, Gdx.graphics.getHeight()/2f-230, 380, 450);
			hudShapeRenderer.rect(Gdx.graphics.getWidth()/2f + 20, Gdx.graphics.getHeight()/2f-320, 380, 80);*/
			Main.drawGenericBox(hudBatch, Gdx.graphics.getWidth()/2f + 20, Gdx.graphics.getHeight()/2f+230, 380, 90);
			Main.drawGenericBox(hudBatch, Gdx.graphics.getWidth()/2f + 20, Gdx.graphics.getHeight()/2f-230, 380, 450);
			Main.drawGenericBox(hudBatch, Gdx.graphics.getWidth()/2f + 20, Gdx.graphics.getHeight()/2f-320, 380, 80);
			for(int i = 0;i < 5;i++)
			{
				if(new Rectangle(Gdx.graphics.getWidth()/2f + 410, Gdx.graphics.getHeight()/2f+240-24 - i * 80, 55, 70).contains(mouse))
				{
					/*hudShapeRenderer.setColor(0.3f, 0.3f, 0.3f, 1f);
					hudShapeRenderer.rect(Gdx.graphics.getWidth()/2f + 410, 580 - i * 80, 55, 70);*/
					Main.drawGenericBox(hudBatch, Gdx.graphics.getWidth()/2f + 409, Gdx.graphics.getHeight()/2f+240-24- i * 80, 55, 70);
					if(mouseJustPressed)
					{
						displaySkillInspectingLevel = i + 1;
						displaySkillInspecting.updateStoredInfos(player[me], i+1);
					}
				}
				else
				{
					/*hudShapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
					hudShapeRenderer.rect(Gdx.graphics.getWidth()/2f + 410, 580 - i * 80, 40, 70);*/
					Main.drawGenericBox(hudBatch, Gdx.graphics.getWidth()/2f + 409, Gdx.graphics.getHeight()/2f+240-24 - i * 80, 40, 70);
				}
			}
		}
		//hudShapeRenderer.end();
		//hudBatch.begin();
		Sprite s;
		for(int i = displaySkillOffset;i < Math.min(displaySkillOffset + 8, sklist.length);i++)
		{
			if(sklist[i] != null)
			{
				int drawPos = i - displaySkillOffset;
				Skill skill = (Skill)sklist[i];
				/*hudShapeRenderer.setColor(tone, tone, tone, 1f);
				hudShapeRenderer.rect(Gdx.graphics.getWidth()/2f - 400, 40 + drawPos * 80, 390, 80);*/
				s = new Sprite(Content.slot);
				s.setPosition(Gdx.graphics.getWidth()/2f - 396, Gdx.graphics.getHeight()/2f+240 - drawPos*79);
				s.draw(hudBatch);
				s = new Sprite(Content.seb);
				s.setColor(Constant.ELEMENTBGCOLOR[skill.element]);
				if(player[me].level < skill.levelRequired)
					s.setColor(new Color(0.25f, 0.25f, 0.25f, 1f));
				s.setPosition(Gdx.graphics.getWidth()/2f - 392, Gdx.graphics.getHeight()/2f+240+4 - drawPos*79);
				s.draw(hudBatch);
				s = new Sprite(Content.skills[skill.id-1]);
				s.setPosition(Gdx.graphics.getWidth()/2f - 392, Gdx.graphics.getHeight()/2f+240+4 - drawPos*79);
				if(player[me].level < skill.levelRequired)
					s.setColor(Color.BLACK);
				s.draw(hudBatch);
				float size = 1f;
				if(skill.name.length() > 16)
					size = 0.8f;
				if(skill.name.length() > 24)
					size = 0.6f;
				
				Main.prettyFontDraw(hudBatch, skill.name, Gdx.graphics.getWidth()/2f - 304, Gdx.graphics.getHeight()/2f+240+68 - drawPos*79, size, Color.WHITE, Color.BLACK, 1f, false, -1);
				for(int j = 0;j < 5;j++)
				{
					if(j < skill.level && player[me].level >= skill.levelRequired)
					{
						s = new Sprite(Content.star1);
					}
					else
					{
						s = new Sprite(Content.star0);
					}
					s.setPosition(Gdx.graphics.getWidth()/2f - 304 + j * 24, Gdx.graphics.getHeight()/2f+240+12 - drawPos * 79);
					Main.prettySpriteDraw(s, hudBatch);
				}
			}
		}
		if(displaySkillInspecting != null)
		{
			Skill skill = displaySkillInspecting;
			s = new Sprite(Content.slot);
			s.setPosition(Gdx.graphics.getWidth()/2f + 30, Gdx.graphics.getHeight()/2f+240-12+11);
			s.draw(hudBatch);
			s = new Sprite(Content.seb);
			s.setColor(Constant.ELEMENTBGCOLOR[displaySkillInspecting.element]);
			if(player[me].level < skill.levelRequired)
				s.setColor(new Color(0.25f, 0.25f, 0.25f, 1f));
			s.setPosition(Gdx.graphics.getWidth()/2f + 34, Gdx.graphics.getHeight()/2f+240-8+11);
			s.draw(hudBatch);
			s = new Sprite(Content.skills[skill.id-1]);
			s.setPosition(Gdx.graphics.getWidth()/2f + 34, Gdx.graphics.getHeight()/2f+240-8+11);
			if(player[me].level < skill.levelRequired)
				s.setColor(Color.BLACK);
			s.draw(hudBatch);
			float size = 1f;
			if(displaySkillInspecting.name.length() > 16)
				size = 0.8f;
			if(displaySkillInspecting.name.length() > 24)
				size = 0.6f;
			
			Main.prettyFontDraw(hudBatch, skill.name, Gdx.graphics.getWidth()/2f + 122, Gdx.graphics.getHeight()/2f+240+56+11, size, Color.WHITE, Color.BLACK, 1f, false, -1);
			String cooldown = (skill.getCooldown(displaySkillInspectingLevel, player[me]) <= 0f ? "No" : skill.getCooldown(displaySkillInspectingLevel, player[me]) + "s");
			String manaCost = (skill.getMana(displaySkillInspectingLevel, player[me]) <= 0f ? "No" : "" + skill.getMana(displaySkillInspectingLevel, player[me]));
			String useInfos = cooldown + " cooldown, " + (skill.passive ? "passive skill" : manaCost + " mana");
			if(skill.classPassive)
				useInfos = "Class passive ability";
			Main.prettyFontDraw(hudBatch, useInfos, Gdx.graphics.getWidth()/2f + 122, Gdx.graphics.getHeight()/2f+240+22+11, 0.5f, new Color(0.25f, 0.25f, 1f, 1f), Color.BLACK, 1f, false, -1);
			for(int j = 0;j < 5;j++)
			{
				if(j < displaySkillInspectingLevel)
				{
					s = new Sprite(Content.star1);
				}
				else
				{
					s = new Sprite(Content.star0);
				}
				s.setPosition(Gdx.graphics.getWidth()/2f + 122 + j * 24, Gdx.graphics.getHeight()/2f+240-12+11);
				Main.prettySpriteDraw(s, hudBatch);
			}
			font.getData().setScale(0.6f);
			cl.setText(font, skill.getDescription(displaySkillInspectingLevel, player[me]), Color.BLACK, 370, -1, true);
			float scale = (cl.height > 400 ? 0.5f : 0.6f);
			Main.prettyFontDraw(hudBatch, skill.getDescription(displaySkillInspectingLevel, player[me]) + "\n\n"
					, Gdx.graphics.getWidth()/2f + 30, Gdx.graphics.getHeight()/2f+240-32, scale, Color.WHITE, Color.BLACK, 1f, false, 370);

			if(player[me].level >= skill.levelRequired)
			{
				if(!skill.classPassive)
				{
					int needCasts = 1;
					if(skill.level == 1)
					{
						needCasts = 40;
					}
					else if(skill.level == 2)
					{
						needCasts = 100;
					}
					else if(skill.level == 3)
					{
						needCasts = 250;
					}
					else if(skill.level == 4)
					{
						needCasts = 500;
					}

					if(skill.level < 5)
						Main.prettyFontDraw2(hudBatch, "Casts: " + skill.totalCasts + "/" + needCasts, Gdx.graphics.getWidth()/2f + 40, Gdx.graphics.getHeight()/2f-270, 1f, Color.WHITE, Color.BLACK, 1f, false, 0, cl);
					else
						Main.prettyFontDraw2(hudBatch, "Maxed!", Gdx.graphics.getWidth()/2f + 40, Gdx.graphics.getHeight()/2f-270, 1f, Color.WHITE, Color.BLACK, 1f, false, 0, cl);

					boolean isEquipped = false;
					for(int i = 0;i < player[me].skills.length;i++)
					{
						if(player[me].skills[i] != null && player[me].skills[i].id == skill.id)
						{
							isEquipped = true;
							break;
						}
					}
					if(!isEquipped)
					{
						cl.setText(font, "Equipp");
						if(new Rectangle(Gdx.graphics.getWidth()/2f + 250, Gdx.graphics.getHeight()/2f-270 - cl.height - 10, cl.width, cl.height + 10).contains(mouse))
						{
							Main.prettyFontDraw2(hudBatch, "Equip", Gdx.graphics.getWidth()/2f + 260, Gdx.graphics.getHeight()/2f-270, 1f, Color.GREEN, Color.BLACK, 1f, false, 0, cl);
							if(mouseJustPressed)
							{
								Main.equippingSkill = true;
								Main.equippingSkillRef = skill;
							}
						}
						else
						{
							Main.prettyFontDraw2(hudBatch, "Equip", Gdx.graphics.getWidth()/2f + 260, Gdx.graphics.getHeight()/2f-270, 1f, Color.WHITE, Color.BLACK, 1f, false, 0, cl);
						}
					}
					else
					{
						cl.setText(font, "Unequipp");
						if(new Rectangle(Gdx.graphics.getWidth()/2f + 250, 100 - cl.height - 10, cl.width, cl.height + 10).contains(mouse))
						{
							Main.prettyFontDraw2(hudBatch, "Unequip", Gdx.graphics.getWidth()/2f + 260, Gdx.graphics.getHeight()/2f-270, 1f, Color.GREEN, Color.BLACK, 1f, false, 0, cl);
							if(mouseJustPressed)
							{
								for(int i = 0;i < player[me].skills.length;i++)
								{
									if(player[me].skills[i] != null && player[me].skills[i].id == skill.id)
									{
										player[me].skills[i] = null;
										break;
									}
								}
							}
						}
						else
						{
							Main.prettyFontDraw2(hudBatch, "Unequip", Gdx.graphics.getWidth()/2f + 260, Gdx.graphics.getHeight()/2f-270, 1f, Color.WHITE, Color.BLACK, 1f, false, 0, cl);
						}
					}
				}
				else
				{
					Main.prettyFontDraw(hudBatch, "Class passives auto-upgrades based on your level.", Gdx.graphics.getWidth()/2f + 30, Gdx.graphics.getHeight()/2f-255, 0.5f, Color.WHITE, Color.BLACK, 1f, false, 150);
					Main.prettyFontDraw(hudBatch, "Class passives don't need to be equipped.", Gdx.graphics.getWidth()/2f + 240, Gdx.graphics.getHeight()/2f-255, 0.5f, Color.WHITE, Color.BLACK, 1f, false, 150);
				}
			}
			else
			{
				Main.prettyFontDraw(hudBatch, Main.lv("[RED]This skill requires level "+skill.levelRequired+".[]", "[RED]Essa skill requer nível "+skill.levelRequired+".[]"), Gdx.graphics.getWidth()/2f + 215, Gdx.graphics.getHeight()/2f-270, 0.5f, Color.WHITE, Color.BLACK, 1f, true, -1);
			}
		}
		hudBatch.end();
	}

	private void DrawInventory(float delta) {
		Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		inventoryX += (655f - inventoryX) / 8;
		skillAlpha += 0.05f;
		if (skillAlpha > 1) {
			skillAlpha = 1f;
		}
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hudShapeRenderer.setProjectionMatrix(uiMatrix);
		hudBatch.setProjectionMatrix(uiMatrix);
		hudBatch.begin();
		Sprite box = new Sprite(Content.inventoryBox);
		box.setPosition(Gdx.graphics.getBackBufferWidth() - inventoryX,
				Gdx.graphics.getBackBufferHeight() / 2f - 355f);
		box.draw(hudBatch);
		Main.prettyFontDraw(hudBatch, player[me].gold + " Gold",
				Gdx.graphics.getBackBufferWidth() - (inventoryX - 30), 
				Gdx.graphics.getBackBufferHeight() / 2f - 20, 
				0.5f, Color.YELLOW, Color.BLACK, 0f, false, -1);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 8; x++) {
				Sprite sprite = new Sprite(Content.inventoryslot);
				sprite.setColor(Color.WHITE);
				sprite.setPosition(Gdx.graphics.getBackBufferWidth() - (inventoryX - 30) + 68 * x + 6,
						Gdx.graphics.getBackBufferHeight() / 2f - 325f + 68 * (3 - y) + 7);
				if (player[me].inventory[y * 8 + x] != null) {
					sprite.setColor(Constant.QUALITYCOLOR[player[me].inventory[y * 8 + x].quality]);
				}
				sprite.draw(hudBatch);
			}
		}
		for (int x = 0; x < 4; x++) {
			Sprite sprite = new Sprite(Content.inventoryslot);
			sprite.setColor(Color.WHITE);
			sprite.setPosition(Gdx.graphics.getBackBufferWidth() - (inventoryX - 45),
					Gdx.graphics.getBackBufferHeight() / 2f + 238 - 76 * x);
			if (player[me].inventory[32 + x] != null) {
				sprite.setColor(Constant.QUALITYCOLOR[player[me].inventory[32 + x].quality]);
			}
			sprite.draw(hudBatch);
		}

		for (int x = 0; x < 4; x++) {
			Sprite sprite = new Sprite(Content.inventoryslot);
			sprite.setColor(Color.WHITE);
			sprite.setPosition(Gdx.graphics.getBackBufferWidth() - (inventoryX - 503),
					Gdx.graphics.getBackBufferHeight() / 2f + 238 - 76 * x);
			if (player[me].vanity[x] != null) {
				sprite.setColor(Constant.QUALITYCOLOR[player[me].vanity[x].quality]);
			}
			sprite.draw(hudBatch);
		}
		
		for (int x = 0; x < 4; x++) {
			Sprite sprite = new Sprite(Content.inventoryslot);
			sprite.setColor(Color.WHITE);
			int slot2 = x;
			if (player[me].vanity[slot2] != null) {
				hudBatch.draw(player[me].vanity[slot2].getInvTexture(),
						Gdx.graphics.getBackBufferWidth() - (inventoryX - 507),
						Gdx.graphics.getBackBufferHeight() / 2f + 242 - 76 * x);
			}
		}

		for (int x = 0; x < 4; x++) {
			Sprite sprite = new Sprite(Content.inventoryslot);
			sprite.setColor(Color.WHITE);
			
			int xC = x-2;
			int xT = 0;
			if(xC == -2)
				xT = -1;
			else if(xC == 1)
				xT = 1;
			
			int yT = 0;
			if(xC == -1)
				yT = -1;
			if(xC == 0)
				yT = 1;

			if (player[me].inventory[36 + x] != null) {
				sprite.setColor(Constant.QUALITYCOLOR[player[me].inventory[36 + x].quality]);
			}
			sprite.setPosition(Gdx.graphics.getBackBufferWidth() - (inventoryX - 310 + sprite.getWidth()/2 + xT * 78),
					Gdx.graphics.getBackBufferHeight() / 2f + 124 + yT * 78);
			sprite.draw(hudBatch);
		}
		
		for (int x = 0; x < 4; x++) {
			Sprite sprite = new Sprite(Content.inventoryslot);
			sprite.setColor(Color.WHITE);
			
			int xC = x-2;
			int xT = 0;
			if(xC == -2)
				xT = -1;
			else if(xC == 1)
				xT = 1;
			
			int yT = 0;
			if(xC == -1)
				yT = -1;
			if(xC == 0)
				yT = 1;

			int slot2 = 36 + x;
			if (player[me].inventory[slot2] != null) {
				hudBatch.draw(player[me].inventory[slot2].getInvTexture(),
						Gdx.graphics.getBackBufferWidth() - (inventoryX - 314 + sprite.getWidth()/2 + xT * 78),
						Gdx.graphics.getBackBufferHeight() / 2f + 128 + yT * 78);
			}
		}

		font.getData().setScale(1f);
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 8; x++) {
				if (player[me].inventory[y * 8 + x] != null) {
					hudBatch.draw(player[me].inventory[y * 8 + x].getInvTexture(),
							Gdx.graphics.getBackBufferWidth() - (inventoryX - 34) + 68 * x + 6,
							Gdx.graphics.getBackBufferHeight() / 2f - 321f + 68 * (3 - y) + 7);

					if (player[me].inventory[y * 8 + x].canStack) {
						Main.prettyFontDraw(hudBatch, ""+player[me].inventory[y * 8 + x].stacks, 
								Gdx.graphics.getBackBufferWidth() - (inventoryX - 34) + 68 * x + 6,
								Gdx.graphics.getBackBufferHeight() / 2f - 321f + 68 * (3 - y) + font.getCapHeight() + 7,
								1f, Color.WHITE, Color.BLACK, 1f, false, -1);
					}
				}
			}
		}
		for (int x = 0; x < 4; x++) {
			if (player[me].inventory[32 + x] != null) {
				hudBatch.draw(player[me].inventory[32 + x].getInvTexture(),
						Gdx.graphics.getBackBufferWidth() - (inventoryX - 49),
						Gdx.graphics.getBackBufferHeight() / 2f + 242 - 76 * x);
			}
		}
		hudBatch.end();
		if (Math.abs(inventoryX + 50f) > 400) {
			Rectangle inventoryHitBox = new Rectangle(Gdx.graphics.getBackBufferWidth() - (inventoryX - 34) + 6,
					Gdx.graphics.getBackBufferHeight() / 2f - 321f + 7, 68 * 8, 68 * 4);
			
			/*
			 * Gdx.graphics.getBackBufferWidth() / 2f - (inventoryX - 45),
					Gdx.graphics.getBackBufferHeight() / 2f + 238 - 76 * x
					*/
			
			Rectangle equippedHitBox = new Rectangle(Gdx.graphics.getBackBufferWidth() - (inventoryX - 45),
					Gdx.graphics.getBackBufferHeight() / 2f + 238 - 76 * 3, 68, 76 * 4);
			/*Gdx.graphics.getBackBufferWidth() / 2f - (inventoryX - 503),
					Gdx.graphics.getBackBufferHeight() / 2f + 238 - 76 * x*/
			Rectangle vanityHitBox = new Rectangle(Gdx.graphics.getBackBufferWidth() - (inventoryX - 503),
					Gdx.graphics.getBackBufferHeight() / 2f + 238 - 76 * 3, 68, 76 * 4);
			Rectangle skillsHitBox = new Rectangle(16, 16, 456, 136);
			Rectangle wpskillHitBox = new Rectangle(376, 54, 64, 64);
			Rectangle passive1HitBox = new Rectangle(Gdx.graphics.getBackBufferWidth() - (inventoryX - 282 + (-1 * 78)),
					Gdx.graphics.getBackBufferHeight() / 2f + 128 + (0 * 78), 64, 64);
			Rectangle passive2HitBox = new Rectangle(Gdx.graphics.getBackBufferWidth() - (inventoryX - 282 + (0 * 78)),
					Gdx.graphics.getBackBufferHeight() / 2f + 128 + (-1 * 78), 64, 64);
			Rectangle passive3HitBox = new Rectangle(Gdx.graphics.getBackBufferWidth() - (inventoryX - 282 + (1 * 78)),
					Gdx.graphics.getBackBufferHeight() / 2f + 128 + (0 * 78), 64, 64);
			Rectangle passive4HitBox = new Rectangle(Gdx.graphics.getBackBufferWidth() - (inventoryX - 282 + (0 * 78)),
					Gdx.graphics.getBackBufferHeight() / 2f + 128 + (1 * 78), 64, 64);
			
			int type = 0;
			int subtype = 0;
			if (inventoryHitBox.contains(mouse))
				type = 1;
			if (equippedHitBox.contains(mouse))
				type = 2;
			if (skillsHitBox.contains(mouse))
				type = 3;
			if (wpskillHitBox.contains(mouse))
				type = 4;
			if(vanityHitBox.contains(mouse))
				type = 5;
			if(passive1HitBox.contains(mouse))
			{
				type = 6;
				subtype = 0;
			}
			if(passive2HitBox.contains(mouse))
			{
				type = 6;
				subtype = 1;
			}
			if(passive3HitBox.contains(mouse))
			{
				type = 6;
				subtype = 3;
			}
			if(passive4HitBox.contains(mouse))
			{
				type = 6;
				subtype = 2;
			}

			if(type == 6)
			{
				int slot;
				slot = Constant.ITEMSLOT_SPECIAL1 + subtype;
				if (mouseJustPressed) {
					boolean canSwitch = false;
					if (slot >= 0) {
						if(holdingItem != null)
						{
							if (holdingItem.type == Constant.ITEMTYPE_SPECIAL) {
								canSwitch = true;
							}
						}
						else
						{
							canSwitch = true;
						}
					}
					if (canSwitch && slot >= 0) {
						Item og = player[me].inventory[slot];
						player[me].inventory[slot] = holdingItem;
						holdingItem = og;
					}
				}
				if(slot >= 0 && player[me].inventory[slot] != null)
					Main.displayItemStats(player[me].inventory[slot]);
			}
			else if(type == 5)
			{
				int x;
				int slot;
				x = (int) Math.floor((mouse.y - equippedHitBox.y) / 76f);
				slot = (3 - x);
				if (mouseJustPressed) {
					boolean canSwitch = false;
					if (slot >= 0) {
						if(holdingItem != null)
						{
							if (holdingItem.type == slot || (holdingItem.type == Constant.ITEMTYPE_LEFT && slot == 1)) {
								canSwitch = true;
							}
							if((holdingItem.dualHanded || holdingItem.dualHandedAttack) && (slot == 1 || player[me].vanity[1] != null))
							{
								canSwitch = false;
							}
							if(player[me].vanity[0] != null && (player[me].vanity[0].dualHanded || player[me].vanity[0].dualHandedAttack) && slot == 1)
							{
								canSwitch = false;
							}
						}
						else
						{
							canSwitch = true;
						}
					}
					if (canSwitch && slot >= 0) {
						Item og = player[me].vanity[slot];
						player[me].vanity[slot] = holdingItem;
						holdingItem = og;
					}
				}
				if(slot >= 0 && slot <= 3 && player[me].vanity[slot] != null)
					Main.displayItemStats(player[me].vanity[slot]);
			}
			else if (type == 1 || type == 2) {
				int x, y;
				int slot;
				if (type == 1) {
					x = (int) Math.floor((mouse.x - inventoryHitBox.x) / 68f);
					y = 3 - (int) Math.floor((mouse.y - inventoryHitBox.y) / 68f);
					slot = y * 8 + x;
				} else {
					x = (int) Math.floor((mouse.y - equippedHitBox.y) / 76f);
					slot = 32 + (3 - x);
				}
				if (mouseJustPressed) {
					if (holdingItem == null) {
						if(!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
						{
							originalSlot = slot;
							holdingItem = player[me].inventory[slot];
							player[me].inventory[slot] = null;
						}
						else
						{
							if(slot >= 0)
							{
								holdingItem = new Item().SetInfos(player[me].inventory[slot].id);
								originalSlot = slot;
							}
						}
					} else {
						if(player[me].inventory[slot] == null || !holdingItem.canStack || holdingItem.id != player[me].inventory[slot].id)
						{
							boolean canSwitch = false;
							if (slot >= Constant.ITEMSLOT_OFFSET) {
								if (holdingItem.type == slot - Constant.ITEMSLOT_OFFSET || (holdingItem.type == Constant.ITEMTYPE_LEFT && slot == Constant.ITEMSLOT_RIGHT)) {
									canSwitch = true;
								}
								if((holdingItem.dualHanded || holdingItem.dualHandedAttack) && (slot == Constant.ITEMSLOT_RIGHT || player[me].inventory[Constant.ITEMSLOT_RIGHT] != null))
								{
									canSwitch = false;
								}
								if(player[me].inventory[Constant.ITEMSLOT_LEFT] != null && (player[me].inventory[Constant.ITEMSLOT_LEFT].dualHanded || player[me].inventory[Constant.ITEMSLOT_LEFT].dualHandedAttack) && slot == Constant.ITEMSLOT_RIGHT)
								{
									canSwitch = false;
								}
							}
							else
							{
								canSwitch = true;
							}
							if (canSwitch && slot >= 0 && slot <= Constant.ITEMSLOT_MAX && originalSlot >= 0
									&& originalSlot <= Constant.ITEMSLOT_MAX) {

								if (originalSlot >= Constant.ITEMSLOT_OFFSET) {
									holdingItem.onDeEquip(player[me], originalSlot, true);
								}
								if (slot >= Constant.ITEMSLOT_OFFSET) {
									if (holdingItem != null)
										holdingItem.onEquip(player[me], slot, true);
								}
								Item og = player[me].inventory[slot];
								//player[me].inventory[originalSlot] = player[me].inventory[slot];
								player[me].inventory[slot] = holdingItem;
								holdingItem = og;
							}
						}
						else
						{
							player[me].inventory[slot].stacks += holdingItem.stacks;
							holdingItem = null;
						}
					}
				}
				if (mouse2JustPressed) {
					if (slot >= 0 && slot <= Constant.ITEMSLOT_OFFSET && player[me].inventory[slot] != null) {
						if(player[me].inventory[slot].canStack)
						{
							if(holdingItem == null)
							{
								player[me].inventory[slot].stacks--;
								holdingItem = new Item().SetInfos(player[me].inventory[slot].id);
								holdingItem.stacks = 1;
							}
							else if(holdingItem != null && holdingItem.id == player[me].inventory[slot].id)
							{
								player[me].inventory[slot].stacks--;
								holdingItem.stacks++;
							}
						}
						else if(!player[me].inventory[slot].usable)
						{
							int correctSlot = -1;
							Item item = player[me].inventory[slot];
							if (item.type == Constant.ITEMTYPE_LEFT) {
								if(player[me].inventory[Constant.ITEMSLOT_LEFT] == null || (player[me].inventory[Constant.ITEMSLOT_LEFT].dualHanded || player[me].inventory[Constant.ITEMSLOT_LEFT].dualHandedAttack))
									correctSlot = Constant.ITEMSLOT_LEFT;
								else if(!item.dualHanded && !item.dualHandedAttack && player[me].inventory[Constant.ITEMSLOT_LEFT] != null && !player[me].inventory[Constant.ITEMSLOT_LEFT].dualHanded && !player[me].inventory[Constant.ITEMSLOT_LEFT].dualHandedAttack)
									correctSlot = Constant.ITEMSLOT_RIGHT;
								else if((item.dualHanded || item.dualHandedAttack) && player[me].inventory[Constant.ITEMSLOT_RIGHT] == null)
									correctSlot = Constant.ITEMSLOT_LEFT;
							}
							if (item.type == Constant.ITEMTYPE_RIGHT) {
								correctSlot = Constant.ITEMSLOT_RIGHT;
							}
							if (item.type == Constant.ITEMTYPE_HEAD) {
								correctSlot = Constant.ITEMSLOT_HEAD;
							}
							if (item.type == Constant.ITEMTYPE_BODY) {
								correctSlot = Constant.ITEMSLOT_BODY;
							}
							if (item.type == Constant.ITEMTYPE_SPECIAL) {
								correctSlot = Constant.ITEMSLOT_SPECIAL1;
								for(int i = Constant.ITEMSLOT_SPECIAL1;i <= Constant.ITEMSLOT_SPECIAL4;i++)
								{
									if(player[me].inventory[i] == null)
									{
										correctSlot = i;
										break;
									}
								}
							}
							
							if(item.type == Constant.ITEMTYPE_LEFT && (item.dualHanded || item.dualHandedAttack) && player[me].inventory[Constant.ITEMSLOT_RIGHT] != null)
							{
								correctSlot = -1;
							}
							
							if(item.type == Constant.ITEMTYPE_RIGHT && player[me].inventory[Constant.ITEMSLOT_LEFT] != null && (player[me].inventory[Constant.ITEMSLOT_LEFT].dualHanded || player[me].inventory[Constant.ITEMSLOT_LEFT].dualHandedAttack))
							{
								correctSlot = -1;
							}
							
							if (correctSlot != -1) {
								player[me].inventory[slot] = player[me].inventory[correctSlot];
								player[me].inventory[correctSlot] = item;
								if (slot >= Constant.ITEMSLOT_OFFSET && player[me].inventory[slot] != null) {
									player[me].inventory[slot].onDeEquip(player[me], slot, true);
								}
								if (correctSlot >= Constant.ITEMSLOT_OFFSET && player[me].inventory[correctSlot] != null) {
									player[me].inventory[correctSlot].onEquip(player[me], correctSlot, true);
								}
							}
						}
						else
						{
							player[me].inventory[slot].onUse(player[me]);
						}
					}
				}
				if (slot >= 0 && slot <= Constant.ITEMSLOT_MAX && player[me].inventory[slot] != null) {
					Item item = player[me].inventory[slot];
					Main.displayItemStats(item);
					if(item.usable || item.itemclass == Constant.ITEMCLASS_ACCESSORY)
					{
						if(Gdx.input.isKeyJustPressed(Keys.F1))
						{
							if(player[me].hotBar[0] != item.id)
							{
								if(player[me].hotBar[1] == item.id)
									player[me].hotBar[1] = -1;
								if(player[me].hotBar[2] == item.id)
									player[me].hotBar[2] = -1;
								player[me].hotBar[0] = item.id;
								System.out.println("Attached item id " + item.id + " to hotbar slot 1.");
							}
							else
								player[me].hotBar[0] = -1;
						}
						if(Gdx.input.isKeyJustPressed(Keys.F2))
						{
							if(player[me].hotBar[1] != item.id)
							{
								if(player[me].hotBar[0] == item.id)
									player[me].hotBar[0] = -1;
								if(player[me].hotBar[2] == item.id)
									player[me].hotBar[2] = -1;
								
								player[me].hotBar[1] = item.id;
								System.out.println("Attached item id " + item.id + " to hotbar slot 2.");
							}
							else
								player[me].hotBar[1] = -1;
						}
						if(Gdx.input.isKeyJustPressed(Keys.F3))
						{

							if(player[me].hotBar[2] != item.id)
							{
								player[me].hotBar[2] = item.id;
								if(player[me].hotBar[1] == item.id)
									player[me].hotBar[1] = -1;
								if(player[me].hotBar[0] == item.id)
									player[me].hotBar[0] = -1;
								System.out.println("Attached item id " + item.id + " to hotbar slot 3.");
							}
							else
								player[me].hotBar[2] = -1;
						}
					}
				}
			}else if (!new Rectangle(Gdx.graphics.getBackBufferWidth() - inventoryX,
					Gdx.graphics.getBackBufferHeight() / 2f - 310f, 620, 620).contains(mouse.x, mouse.y)) {
				if (mouseJustPressed && holdingItem != null && originalSlot >= 0) {
					for(int i = 0;i < holdingItem.stacks;i++)
					{
						Item.Create(player[me].Center(), new Vector2(MathUtils.random(200, 700) * player[me].direction, MathUtils.random(300, 500)), holdingItem.id, player[me].myMapX, player[me].myMapY, true);
					}
					player[me].inventory[originalSlot] = null;
					holdingItem = null;
				}
			}
		}
	}

	public static Vector2 mouseInWorld() {
		return new Vector2(cameraRealPosition().x + Gdx.input.getX() * camera.zoom,
				cameraRealPosition().y + Gdx.graphics.getHeight() * camera.zoom - Gdx.input.getY() * camera.zoom);
	}

	public static float ticksToSeconds(int ticks) {
		return ticks / 60f;
	}

	public static int secondsToTicks(float seconds) {
		return (int) (seconds * 60f);
	}

	public static int dayTime() {
		//return (Main.gameTick + 12600) % 86400;
		return (Constant.getWorldTime()) % 86400;
	}

	public static int dayHour() {
		return dayTime() / 3600;
	}

	public static float dayEHour() {
		return dayTime() / 3600f;
	}

	public static int dayMinute() {
		return (dayTime() % 3600) / 60;
	}

	public static Rectangle getScreen() {
		float width = camera.viewportWidth * Math.abs(camera.zoom);
		float height = camera.viewportHeight * Math.abs(camera.zoom);
		Rectangle screen = new Rectangle(camera.position.x - width / 2, camera.position.y - height / 2, width, height);
		return screen;
	}

	public static Rectangle getScreen(float offset) {
		float width = camera.viewportWidth * Math.abs(camera.zoom);
		float height = camera.viewportHeight * Math.abs(camera.zoom);
		Rectangle screen = new Rectangle(camera.position.x - (width / 2) - offset,
				camera.position.y - (height / 2) - offset, width + (offset * 2), height + (offset * 2));
		return screen;
	}

	public static void prettyFontDraw(SpriteBatch spritebatch, String str2, float x, float y, float scale,
			Color color, Color backColor, float alpha, boolean centered, int maxWidth) {
		boolean wrap = (maxWidth > 0);
		String str = str2;
		/*for(int i = 0;i < Constant.QUALITYCOLORNAME.length;i++)
		{
			str = Pattern.compile("\\["+Constant.QUALITYCOLORNAME[i]+"\\]").matcher(str).replaceAll("[BLACK]");
		}*/
		str = Pattern.compile("\\[(.*?)\\]").matcher(str).replaceAll("[BLACK]");
		font.getData().setScale(scale);
		Color bc = new Color(backColor.r, backColor.g, backColor.b, alpha);
		font.setColor(bc);
		layout.setText(font, str, bc, maxWidth, -1, wrap);
		if (centered) {
			font.draw(spritebatch, layout, x - 1 - layout.width / 2, y - 1);
			font.draw(spritebatch, layout, x + 1 - layout.width / 2, y - 1);
			font.draw(spritebatch, layout, x - 1 - layout.width / 2, y + 1);
			font.draw(spritebatch, layout, x + 1 - layout.width / 2, y + 1);
			font.draw(spritebatch, layout, x - 1 - layout.width / 2, y);
			font.draw(spritebatch, layout, x + 1 - layout.width / 2, y);
			font.draw(spritebatch, layout, x - layout.width / 2, y + 1);
			font.draw(spritebatch, layout, x - layout.width / 2, y - 1);
		} else {
			font.draw(spritebatch, layout, x - 1, y - 1);
			font.draw(spritebatch, layout, x + 1, y - 1);
			font.draw(spritebatch, layout, x - 1, y + 1);
			font.draw(spritebatch, layout, x + 1, y + 1);
			font.draw(spritebatch, layout, x - 1, y);
			font.draw(spritebatch, layout, x + 1, y);
			font.draw(spritebatch, layout, x, y + 1);
			font.draw(spritebatch, layout, x, y - 1);
		}
		Color c = new Color(color.r, color.g, color.b, alpha);
		font.setColor(c);
		layout.setText(font, str2, c, maxWidth, -1, wrap);

		font.draw(spritebatch, layout, x - (centered ? layout.width / 2 : 0), y);
	}
	
	public static void prettyFontDraw2(SpriteBatch spritebatch, String str, float x, float y, float scale,
			Color color, Color backColor, float alpha, boolean centered, int maxWidth, GlyphLayout cl) {
		boolean wrap = (maxWidth > 0);
		font.getData().setScale(scale);
		font.setColor(backColor.r, backColor.g, backColor.b, alpha);
		cl.setText(font, str, backColor, maxWidth, -1, wrap);
		if (centered) {
			font.draw(spritebatch, cl, x - 1 - cl.width / 2, y - 1);
			font.draw(spritebatch, cl, x + 1 - cl.width / 2, y - 1);
			font.draw(spritebatch, cl, x - 1 - cl.width / 2, y + 1);
			font.draw(spritebatch, cl, x + 1 - cl.width / 2, y + 1);
			
			font.draw(spritebatch, cl, x - 1 - cl.width / 2, y);
			font.draw(spritebatch, cl, x + 1 - cl.width / 2, y);
			font.draw(spritebatch, cl, x - cl.width / 2, y + 1);
			font.draw(spritebatch, cl, x - cl.width / 2, y - 1);
		} else {
			font.draw(spritebatch, cl, x - 1, y - 1);
			font.draw(spritebatch, cl, x + 1, y - 1);
			font.draw(spritebatch, cl, x - 1, y + 1);
			font.draw(spritebatch, cl, x + 1, y + 1);

			font.draw(spritebatch, cl, x - 1, y);
			font.draw(spritebatch, cl, x + 1, y);
			font.draw(spritebatch, cl, x, y + 1);
			font.draw(spritebatch, cl, x, y - 1);
		}
		font.setColor(color.r, color.g, color.b, alpha);
		cl.setText(font, str, color, maxWidth, -1, wrap);

		if (centered)
			font.draw(spritebatch, cl, x - cl.width / 2, y);
		else
			font.draw(spritebatch, cl, x, y);
	}
	
	public static void prettyFontDraw(SpriteBatch sb, String str, float x, float y, float scale, float alpha,
			boolean centered) {
		prettyFontDraw(sb, str, x, y, scale, Color.WHITE, Color.BLACK, alpha, centered, 0);
	}

	public static void prettyFontDraw(SpriteBatch sb, String str, float x, float y, float scale, float alpha) {
		prettyFontDraw(sb, str, x, y, scale, Color.WHITE, Color.BLACK, alpha, true, 0);
	}

	public static void prettyFontDraw(SpriteBatch sb, String str, float x, float y, float alpha, boolean centered) {
		prettyFontDraw(sb, str, x, y, 1f, Color.WHITE, Color.BLACK, alpha, centered, 0);
	}

	public static void prettyFontDraw(SpriteBatch sb, String str, float x, float y, float alpha) {
		prettyFontDraw(sb, str, x, y, 1f, Color.WHITE, Color.BLACK, alpha, true, 0);
	}
	
	//
	
	public static void prettyFontDraw2(SpriteBatch sb, String str, float x, float y, float scale, float alpha,
			boolean centered, GlyphLayout cl) {
		prettyFontDraw2(sb, str, x, y, scale, Color.WHITE, Color.BLACK, alpha, centered, 0, cl);
	}

	public static void prettyFontDraw2(SpriteBatch sb, String str, float x, float y, float scale, float alpha, GlyphLayout cl) {
		prettyFontDraw2(sb, str, x, y, scale, Color.WHITE, Color.BLACK, alpha, true, 0, cl);
	}

	public static void prettyFontDraw2(SpriteBatch sb, String str, float x, float y, float alpha, boolean centered, GlyphLayout cl) {
		prettyFontDraw2(sb, str, x, y, 1f, Color.WHITE, Color.BLACK, alpha, centered, 0, cl);
	}

	public static void prettyFontDraw2(SpriteBatch sb, String str, float x, float y, float alpha, GlyphLayout cl) {
		prettyFontDraw2(sb, str, x, y, 1f, Color.WHITE, Color.BLACK, alpha, true, 0, cl);
	}

	public static Sprite prettySpriteDraw(Sprite pressx, SpriteBatch sb, Color border)
	{
		Vector2 originalPos = new Vector2(pressx.getX(), pressx.getY());
		pressx.setColor(border);
		pressx.setPosition(originalPos.x-1, originalPos.y-1);
		pressx.draw(sb);
		pressx.setPosition(originalPos.x+1, originalPos.y+1);
		pressx.draw(sb);
		pressx.setPosition(originalPos.x+1, originalPos.y-1);
		pressx.draw(sb);
		pressx.setPosition(originalPos.x-1, originalPos.y+1);
		pressx.draw(sb);
		
		pressx.setPosition(originalPos.x-1, originalPos.y);
		pressx.draw(sb);
		pressx.setPosition(originalPos.x+1, originalPos.y);
		pressx.draw(sb);
		pressx.setPosition(originalPos.x, originalPos.y-1);
		pressx.draw(sb);
		pressx.setPosition(originalPos.x, originalPos.y+1);
		pressx.draw(sb);
		
		pressx.setColor(Color.WHITE);
		pressx.setPosition(originalPos.x, originalPos.y);
		pressx.draw(sb);
		return pressx;
	}

	public static Sprite prettySpriteDraw(Sprite pressx, SpriteBatch sb)
	{
		return prettySpriteDraw(pressx, sb, Color.BLACK);
	}
	
	public static void displayItemStats(Item item)
	{
		Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		displayItemStats(item, mouse);
	}
	
	public static String getItemTitle(Item item)
	{
		String lvl = Main.lv("Level " + item.level + " ", "de Nível " + item.level + " ");

		String qlt = "";
		if (item.quality == Constant.QUALITY_COMMON)
			qlt += Main.lv("Common ", "Comum ");
		else if (item.quality == Constant.QUALITY_UNCOMMON)
			qlt += Main.lv("Uncommon ", "Incomum ");
		else if (item.quality == Constant.QUALITY_RARE)
			qlt += Main.lv("Rare ", "Raro ");
		else if (item.quality == Constant.QUALITY_EPIC)
			qlt += Main.lv("Epic ", "Épico ");
		else if (item.quality == Constant.QUALITY_LEGENDARY)
			qlt += Main.lv("Legendary ", "Lendário ");
		else if (item.quality == Constant.QUALITY_IMAGINARY)
			qlt += Main.lv("Imaginary ", "Imaginário ");

		String tp = "";
		if(item.itemclass == Constant.ITEMCLASS_BODYGUARD)
			tp += Main.lv("Bodyguard ", "Peitoral ");
		else if(item.itemclass == Constant.ITEMCLASS_HAT)
			tp += Main.lv("Hat ", "Chapéu ");
		else if(item.itemclass == Constant.ITEMCLASS_MAGIC)
			tp += Main.lv("Magical Weapon ", "Arma Mágica ");
		else if(item.itemclass == Constant.ITEMCLASS_MATERIAL)
			tp += Main.lv("Material ", "Material ");
		else if(item.itemclass == Constant.ITEMCLASS_SHORTSWORD)
			tp += Main.lv("Shortsword ", "Espada Curta ");
		else if(item.itemclass == Constant.ITEMCLASS_LONGSWORD)
			tp += Main.lv("Longsword ", "Espada Longa ");
		else if(item.itemclass == Constant.ITEMCLASS_CLUB)
			tp += Main.lv("Club ", "Porrete ");
		else if(item.itemclass == Constant.ITEMCLASS_HAMMER)
			tp += Main.lv("Hammer ", "Martelo ");
		else if(item.itemclass == Constant.ITEMCLASS_HEAVYHAMMER)
			tp += Main.lv("Heavy Hammer ", "Martelo Pesado ");
		else if(item.itemclass == Constant.ITEMCLASS_HEAVYCLUB)
			tp += Main.lv("Heavy Club ", "Porrete Pesado ");
		else if(item.itemclass == Constant.ITEMCLASS_AXE)
			tp += Main.lv("Axe ", "Machado ");
		else if(item.itemclass == Constant.ITEMCLASS_HEAVYAXE)
			tp += Main.lv("Heavy Axe ", "Machado Pesado ");
		else if(item.itemclass == Constant.ITEMCLASS_DAGGER)
			tp += Main.lv("Dagger ", "Adaga ");
		else if(item.itemclass == Constant.ITEMCLASS_LANCE)
			tp += Main.lv("Lance ", "Lança ");
		else if(item.itemclass == Constant.ITEMCLASS_BROADSWORD)
			tp += Main.lv("Broadsword ", "Espada Larga ");
		else if(item.itemclass == Constant.ITEMCLASS_USABLE)
			tp += Main.lv("Utility ", "Utilitário ");
		else if(item.itemclass == Constant.ITEMCLASS_ACCESSORY)
			tp += Main.lv("Acessório ", "Accessory ");
		else if(item.itemclass == Constant.ITEMCLASS_PASSIVE)
			tp += Main.lv("Rune ", "Runa ");
		else if(item.itemclass == Constant.ITEMCLASS_RANGED)
			tp += Main.lv("Ranged Weapon ", "Arma de Longo Alcance ");
		else if(item.itemclass == Constant.ITEMCLASS_SHIELD)
			tp += Main.lv("Shield ", "Escudo ");
		else if(item.itemclass == Constant.ITEMCLASS_SCROLL)
			tp += Main.lv("Scroll ", "Pergaminho ");
		else if(item.itemclass == Constant.ITEMCLASS_ORB)
			tp += Main.lv("Orb ", "Orbe ");
		
		return Main.lv(lvl + qlt + tp, tp + qlt + lvl);
	}
	
	public static void displayItemStats(Item item, Vector2 pos)
	{
		Vector2 mouse = pos.cpy();
		int offsetY = 0;
		float highestX = 400;
		float sumY = 0;
		String title = "";
		if (item.handCrafted)
			title += "Handcrafted ";

		title += getItemTitle(item);

		String damageText = "";

		if (item.damageType == Constant.DAMAGETYPE_PHYSICAL)
			damageText = Main.lv(" physical", " físico");
		if (item.damageType == Constant.DAMAGETYPE_AIR)
			damageText = Main.lv(" wind", " de vento");
		if (item.damageType == Constant.DAMAGETYPE_ENERGY)
			damageText = Main.lv(" energy", " de energia");
		if (item.damageType == Constant.DAMAGETYPE_FIRE)
			damageText = Main.lv(" fire", " de fogo");
		if (item.damageType == Constant.DAMAGETYPE_HOLY)
			damageText = Main.lv(" holy", " de luz");
		if (item.damageType == Constant.DAMAGETYPE_ICE)
			damageText = Main.lv(" ice", " de gelo");
		if (item.damageType == Constant.DAMAGETYPE_NATURE)
			damageText = Main.lv(" nature", " de natureza");
		if (item.damageType == Constant.DAMAGETYPE_DARKNESS)
			damageText = Main.lv(" darkness", " de sombra");
		if (item.damageType == Constant.DAMAGETYPE_POISON)
			damageText = Main.lv(" poison", " de veneno");
		if (item.damageType == Constant.DAMAGETYPE_BLOOD)
			damageText = Main.lv(" blood", " de sangue");
		if (item.damageType == Constant.DAMAGETYPE_GOOD)
			damageText = Main.lv(" good", " de bondade");
		if (item.damageType == Constant.DAMAGETYPE_DEATH)
			damageText = Main.lv(" death", " de morte");
		

		font.getData().setScale(1f);
		layout.setText(font, item.name);
		sumY += layout.height + 8;
		if (layout.width > highestX)
			highestX = layout.width;

		font.getData().setScale(0.5f);
		layout.setText(font, title);
		sumY += layout.height + 8;
		if (layout.width > highestX)
			highestX = layout.width;
		
		if(item.dualHanded || item.dualHandedAttack)
		{
			font.getData().setScale(0.5f);
			layout.setText(font, Main.lv("Dual-handed", "Duas mãos"));
			sumY += layout.height + 8;
			if (layout.width > highestX)
				highestX = layout.width;
		}
		
		if(item.dyed)
		{
			font.getData().setScale(0.5f);
			layout.setText(font, Main.lv("Dyed with RGB: ", "Pintado com RGB: ") + (int)(item.dye.r*255) + ", " + (int)(item.dye.g*255) + ", " + (int)(item.dye.b*255));
			sumY += layout.height + 8;
			if (layout.width > highestX)
				highestX = layout.width;			
		}

		if (item.quality != Constant.QUALITY_IMAGINARY && item.damage != 0) {
			font.getData().setScale(3/4f);
			layout.setText(font, Main.lv(item.damage + damageText + " damage", item.damage + " de dano " + damageText));
			sumY += layout.height + 10;
			if (layout.width > highestX)
				highestX = layout.width;
		}

		if (item.description.length() > 0) {
			font.getData().setScale(525f / 900f);
			layout.setText(font, item.description, item.descriptionColor, highestX, -1, true);
			sumY += layout.height + 16;
		}
		
		if(item.partOfSet != null)
		{
			font.getData().setScale(0.6f);
			layout.setText(font, item.partOfSet.name, Color.CYAN, highestX, -1, true);
			sumY += layout.height + 16;
			font.getData().setScale(525f / 900f);
			for(Bonus b : item.partOfSet.bonus)
			{
				layout.setText(font, b.itemcount + Main.lv(" items: ", " itens: ") + b.bonusString, Color.GREEN, highestX, -1, true);
				sumY += layout.height + 16;
			}
		}
		
		if(item.specialSkill != null)
		{
			Skill s = item.specialSkill;
			String text = Main.lv("[LIME]Special skill:[] [", "[LIME]Habilidade especial:[] [") + Constant.ELEMENTCOLORNAME[s.element] + "]" + s.name + "[]";
			font.getData().setScale(525f / 900f);
			layout.setText(font, text, Color.CYAN, highestX, -1, true);
			sumY += layout.height + 16;
		}

		if (item.trivia.length() > 0) {
			font.getData().setScale(0.5f);
			layout.setText(font, item.trivia, item.triviaColor, highestX, -1, true);
			sumY += layout.height + 8;
		}
		
		if(item.usable || item.itemclass == Constant.ITEMCLASS_ACCESSORY)
		{
			font.getData().setScale(0.5f);
			layout.setText(font, item.itemclass == Constant.ITEMCLASS_ACCESSORY ? 
					Main.lv("As an accessory, you need to attach it to any hotbar slot\nPress F1-F3 to attach this item to the hotbar",
							"Sendo um acessório, você precisar anexar este item na hotbar\nPressione F1-F3 para anexar este item à hotbar") 
					: 
					Main.lv("Press F1-F3 to attach this item to the hotbar",
							"Pressione F1-F3 para anexar este item à hotbar"), item.triviaColor, highestX, -1, true);
			sumY += layout.height + 16;
		}

		float drawY = mouse.y - sumY - 16;
		if((drawY + sumY) - (sumY + 24) < 0)
		{
			drawY = 0;
		}
		/*hudShapeRenderer.begin(ShapeType.Filled);
		hudShapeRenderer.setColor(Color.GRAY);
		hudShapeRenderer.rect(mouse.x - 8 - highestX - 20, drawY, highestX + 16, sumY + 24);
		hudShapeRenderer.end();*/
		hudBatch.begin();
		Main.drawGenericBox(hudBatch, mouse.x-8 - highestX - 20, drawY + 16, highestX + 16,  sumY + 8);
		
		drawY += sumY + 16;

		// ----------------------------------------------
		//hudBatch.begin();
		Main.prettyFontDraw(hudBatch, item.name, mouse.x-highestX-21, drawY - offsetY - 2, 
				1f, Constant.QUALITYCOLOR[item.quality], Color.BLACK, 1f, false, (int)highestX);
		offsetY += layout.height + 8;

		Main.prettyFontDraw(hudBatch, title, mouse.x-highestX-20, drawY - offsetY, 
				0.5f, Constant.QUALITYCOLOR[item.quality], Color.BLACK, 1f, false, (int)highestX);
		offsetY += layout.height + 8;
		
		if(item.dualHanded || item.dualHandedAttack)
		{
			Main.prettyFontDraw(hudBatch, Main.lv("Dual-handed", "Duas mãos"), mouse.x-highestX-20, drawY - offsetY, 
					0.5f, Color.RED, Color.BLACK, 1f, false, (int)highestX);
			offsetY += layout.height + 8;
		}
		
		if(item.dyed)
		{
			Main.prettyFontDraw(hudBatch, Main.lv("Dyed with RGB: ", "Pintado com RGB: ") + (int)(item.dye.r*255) + ", " + (int)(item.dye.g*255) + ", " + (int)(item.dye.b*255), mouse.x-highestX-20, drawY - offsetY, 
					0.5f, item.dye, Color.BLACK, 1f, false, (int)highestX);
			offsetY += layout.height + 8;
		}

		if (item.quality != Constant.QUALITY_IMAGINARY && item.damage != 0) {
			Main.prettyFontDraw(hudBatch, Main.lv(item.damage+damageText+" damage",item.damage+" de dano" +damageText), mouse.x-highestX-20, drawY - offsetY, 
					2f/3f, Constant.ELEMENTCOLOR[item.damageType], Color.BLACK, 1f, false, (int)highestX);
			offsetY += layout.height + 10;
		}

		if (item.description.length() > 0) {
			Main.prettyFontDraw(hudBatch, item.description, mouse.x-highestX-20, drawY - offsetY, 
					525f/900f, item.descriptionColor, Color.BLACK, 1f, false, (int)highestX);
			offsetY += layout.height + 16;
		}
		
		if(item.partOfSet != null)
		{
			Main.prettyFontDraw(hudBatch, item.partOfSet.name, mouse.x-highestX-20, drawY - offsetY, 
					0.6f, Color.CYAN, Color.BLACK, 1f, false, (int)highestX);
			offsetY += layout.height + 16;
			
			for(Bonus b : item.partOfSet.bonus)
			{
				Main.prettyFontDraw(hudBatch, b.itemcount + Main.lv(" items: ", " itens: ") + b.bonusString, mouse.x-highestX-20, drawY - offsetY, 
						525f/900f, (Set.playerItemSetCount(player[me], item.partOfSet) >= b.itemcount ? Color.CYAN : new Color(0f, 1f, 1f, 0f)),
						Color.BLACK, 1f, false, (int)highestX);
				offsetY += layout.height + 16;
			}
		}
		
		if(item.specialSkill != null)
		{
			Skill s = item.specialSkill;
			String text = Main.lv("[LIME]Special skill:[] [", "[LIME]Habilidade especial:[] [") + Constant.ELEMENTCOLORNAME[s.element] + "]" + s.name + "[]";
			Main.prettyFontDraw(hudBatch, text, mouse.x-highestX-20, drawY - offsetY, 
					525f/900f, Color.LIME, Color.BLACK, 1f, false, (int)highestX);
			offsetY += layout.height + 16;
		}

		if (item.trivia.length() > 0) {
			Main.prettyFontDraw(hudBatch, item.trivia, mouse.x-highestX-20, drawY - offsetY, 
					0.5f, item.triviaColor, Color.BLACK, 1f, false, (int)highestX);
			offsetY += layout.height + 12;
		}
		
		if(item.usable || item.itemclass == Constant.ITEMCLASS_ACCESSORY)
		{
			Main.prettyFontDraw(hudBatch, item.itemclass == Constant.ITEMCLASS_ACCESSORY ? Main.lv("As an accessory, you need to attach it to any hotbar slot\nPress F1-F3 to attach this item to the hotbar",
					"Sendo um acessório, você precisar anexar este item na hotbar\nPressione F1-F3 para anexar este item à hotbar") 
			: 
			Main.lv("Press F1-F3 to attach this item to the hotbar",
					"Pressione F1-F3 para anexar este item à hotbar"), mouse.x-highestX-20, drawY - offsetY, 
					0.5f, new Color(0f, 1f, 0f, 1f), Color.BLACK, 1f, false, (int)highestX);
			sumY += layout.height + (item.itemclass == Constant.ITEMCLASS_ACCESSORY ? 8 : 8);
		}
		hudBatch.end();
	}
	
	public static Polygon rectToPoly(Rectangle r)
	{
		Polygon poly = new Polygon(new float[]{0, 0, 
				r.width, 0, 
				r.width, r.height, 
				0, r.height});
		poly.setPosition(r.getX(), r.getY());
		return poly;
	}
	
	public static boolean canDrawHud()
	{
		return drawHud;
	}

	public static boolean usingSkill(Player player) {
		if(customShowOff > 0)
			return true;
		
		if (player.customAnim != null && (player.customAnimBlockAttack || player.channelingSkillBlocksAttack())
				&& player.customAnimBlockMove)
			return true;

		if (player.customAnim2 != null && (player.customAnim2BlockAttack || player.channelingSkillBlocksAttack())
				&& player.customAnim2BlockMove)
			return true;
		
		if(player.ticksFromLastAnimation1 < 20 && (player.customAnimBlockAttack || player.channelingSkillBlocksAttack())
				&& player.customAnimBlockMove)
			return true;
		
		if (player.ticksFromLastAnimation2 < 20 && (player.customAnim2BlockAttack || player.channelingSkillBlocksAttack())
				&& player.customAnim2BlockMove)
			return true;
		
		if(customShowOff <= 0f && customShowOffLastTicks < 20)
			return true;

		return false;
	}
	
	public static float usingSkillMinus(Player player)
	{
		if(customShowOff > 0f)
		{
			return Math.min(0.75f, customShowOffTicks * 0.05f);
		}
		
		if (player.customAnim != null && (player.customAnimBlockAttack || player.channelingSkillBlocksAttack())
				&& player.customAnimBlockMove)
			return Math.min(0.75f, player.ticksFromLastSkill * 0.05f);

		if (player.customAnim2 != null && (player.customAnim2BlockAttack || player.channelingSkillBlocksAttack())
				&& player.customAnim2BlockMove)
			return Math.min(0.75f, player.ticksFromLastSkill * 0.05f);
		
		if(player.ticksFromLastAnimation1 < 20 && (player.customAnimBlockAttack || player.channelingSkillBlocksAttack())
				&& player.customAnimBlockMove)
			return (1f - (player.ticksFromLastAnimation1)/20f)*0.75f;
		
		if (player.ticksFromLastAnimation2 < 20 && (player.customAnim2BlockAttack || player.channelingSkillBlocksAttack())
				&& player.customAnim2BlockMove)
			return (1f - (player.ticksFromLastAnimation2)/20f)*0.75f;
		
		if(customShowOff <= 0f && customShowOffLastTicks < 20)
			return (1f - (customShowOffLastTicks/20f))*0.75f;

		return 0f;
	}
	
	public static boolean shouldDrawGame()
	{
		if(Main.mapTransitioning)
			return false;
		
		if(isOnline && player[me].loadingMapOnline)
			return false;
		
		return true;
	}

	public static boolean shouldFreeze()
	{
		if(usingSkill(player[me]))
			return true;

		return false;
	}

	public static String pluralOf(String text)
	{
		if(text != null)
		{
			String plural = text.substring(0);
			int lastChar = plural.charAt(plural.length()-1);
			if(lastChar == 'h' || lastChar == 'H')
				plural += "es";
			else if(lastChar == 'y' || lastChar == 'Y')
			{
				plural = plural.substring(0, plural.length()-1);
				plural += "ies";
			}
			else if(lastChar == 'f' || lastChar == 'F')
			{
				plural = plural.substring(0, plural.length()-1);
				plural += "ves";
			}
			else if(lastChar != 's' && lastChar != 'S')
			{
				plural += "s";
			}
			return plural;
		}
		return "";
	}

	public static boolean isAtDay()
	{
		return (Main.dayHour() >= 4 && Main.dayHour() <= 18);
	}
	
	public static int getNightTicks()
	{
		if(isAtDay())
		{
			return -1;
		}
		else
		{
			if(Main.dayEHour() > 18)
			{
				return Main.secondsToTicks((Main.dayEHour()-18)*60);
			}
			else
			{
				return Main.secondsToTicks((6+Main.dayEHour())*60);
			}
		}
	}
	
	public static int getDayTicks()
	{
		if(!isAtDay())
		{
			return -1;
		}
		else
		{
			return Main.secondsToTicks((Main.dayEHour()-6)*60);
		}
	}

    public static boolean shouldDrawWorldMap()
    {
    	if(Main.mapTransitioning || Main.displayMap)
    		return true;
    	
    	return false;
    }
    
    public static int getLayerY(int y)
    {
    	return (int)Math.floor(y/100f);
    }
    
    public static int directionFromTo(Entity from, Entity to)
    {
    	if(from.Center().x > to.Center().x)
    		return Constant.DIRECTION_LEFT;
    	else
    		return Constant.DIRECTION_RIGHT;
    }

    public static int directionFromTo(Vector2 from, Vector2 to)
    {
    	if(from.x > to.x)
    		return Constant.DIRECTION_LEFT;
    	else
    		return Constant.DIRECTION_RIGHT;
    }
    
    public static int directionFromTo(float from, float to)
    {
    	if(from > to)
    		return Constant.DIRECTION_LEFT;
    	else
    		return Constant.DIRECTION_RIGHT;
    }
    
    public static boolean shouldNet()
    {
    	return (Main.isOnline || AHS.isUp);
    }
    
    public static float angleBetween(Vector2 v1, Vector2 v2)
    {
		return (float)Math.toDegrees(Math.atan2(v2.y - v1.y, v2.x - v1.x));
    }
    
    public static double angleBetweenD(Vector2 v1, Vector2 v2)
    {
		return Math.toDegrees(Math.atan2(v2.y - v1.y, v2.x - v1.x));
    }
    
    public static boolean xnor(boolean a, boolean b)
    {
    	return ((a && b) || (!a && !b));
    }
    
    public static boolean xor(boolean a, boolean b)
    {
    	return ((a && !b) || (!a && b));
    }
    
    public static Monster getMouseTarget(float maxDistanceFromPlayer, boolean checkDirection)
    {
    	Monster ret = null;
    	float nextestDistance = 999999f;
		for(Monster m : Constant.getMonsterList(false))
		{
			if(m.active && m.sameMapAs(Main.player[Main.me]))
			{
				float size = 50 + Math.max(m.width, m.height);
				if(Main.mouseInWorld().dst(m.Center()) < size && m.Center().dst(Main.mouseInWorld()) < nextestDistance && m.Center().dst(player[me].Center()) < maxDistanceFromPlayer && (!checkDirection || player[me].direction == Main.directionFromTo(player[me], m)))
				{
					nextestDistance = m.Center().dst(Main.mouseInWorld());
					ret = m;
				}
			}
		}
		return ret;
    }
    
    public static Monster getMouseTarget(float maxDistanceFromPlayer)
    {
    	return Main.getMouseTarget(maxDistanceFromPlayer, false);
    }
    
    public static Monster getMouseTarget(boolean checkDirection)
    {
    	return Main.getMouseTarget(9999999f, checkDirection);
    }
    
    public static Monster getMouseTarget()
    {
    	return Main.getMouseTarget(9999999f, false);
    }
    public static float clamp(float min, float val, float max) {
        return Math.max(min, Math.min(max, val));
    }
    public static int clamp(int min, int val, int max) {
        return (int) Math.max(min, Math.min(max, val));
    }
    public static void doEarthquake(int force)
    {
    	Main.earthquake = Math.max(Main.earthquake, force);
    }
    
    public static String lv(String english, String portuguese)
    {
    	if(Roguelike.language == Constant.LANGUAGE_ENGLISH)
    		return english;
    	else if(Roguelike.language == Constant.LANGUAGE_PORTUGUESE)
    		return portuguese;
    	else
    		return english;
    }
    
	public static boolean isMelee(int itemclass)
	{
		if (itemclass == Constant.ITEMCLASS_SHORTSWORD || itemclass == Constant.ITEMCLASS_LONGSWORD
				|| itemclass == Constant.ITEMCLASS_CLUB || itemclass == Constant.ITEMCLASS_HAMMER
				|| itemclass == Constant.ITEMCLASS_HEAVYHAMMER || itemclass == Constant.ITEMCLASS_HEAVYCLUB
				|| itemclass == Constant.ITEMCLASS_AXE || itemclass == Constant.ITEMCLASS_HEAVYAXE
				|| itemclass == Constant.ITEMCLASS_DAGGER || itemclass == Constant.ITEMCLASS_LANCE
				|| itemclass == Constant.ITEMCLASS_BROADSWORD)
			return true;

		return false;
	}
	
	public static int getFakeY(int y)
	{
		int fakeY = y % 100;
		if(y % 100 > 50)
		{
			fakeY = (y % 100) - 100;
		}
		return fakeY;
	}
	
	public static void forceShowOff(float time)
	{
		if(Main.customShowOff < 0)
		{
			Main.customShowOffTicks = 0;
			Main.customShowOffLastTicks = 0;
		}
		
		if(Main.customShowOff < time)
			Main.customShowOff  = time;
	}
	
	public static void createExplosion(Vector2 center, float radius, float deep, boolean instant)
	{
		Lighting.Create(center, radius*2, Constant.EXPLOSION_DEFAULT_SCHEME[1], 1f);
		int particles = (int) ((Math.pow(radius, 1.15f)) * deep);
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8);
			float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
			float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
			float distance = MathUtils.random(radius);
			Color color = new Color();
			if(distance < radius/4)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[0];
			else if(distance < radius/3f)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[1];
			else if(distance < radius/1.5f)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[2];
			else
				color = Constant.EXPLOSION_DEFAULT_SCHEME[3];

			Vector2 pos = new Vector2(cos * distance, sin * distance).add(center);
			Vector2 vel = new Vector2(cos * 40 + MathUtils.random(-10, 10), sin * 40 + MathUtils.random(-10, 10));
			if(!instant)
			{
				pos = new Vector2(cos * distance / 10, sin * distance/10).add(center);
				vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			}
			Particle p = Particle.Create(pos, vel, 24, color, 0f, 1f, 2f);
			p.setLight((int) (16*p.scale), color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = instant ? 0.95f : 0.85f;
		}
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8);
			float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
			float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
			float distance = MathUtils.random(radius);
			Vector2 pos = new Vector2(cos * distance, sin * distance).add(center);
			Vector2 vel = new Vector2(cos * 70 + MathUtils.random(-25, 25), sin * 70 + MathUtils.random(-25, 25));
			if(!instant)
			{
				pos = new Vector2(cos * distance / 10, sin * distance/10).add(center);
				vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			}
			Particle p = Particle.Create(pos, vel, 24, Constant.EXPLOSION_DEFAULT_SMOKE, 0f, 1f, 1f);
			p.setLight((int) (16*p.scale), Constant.EXPLOSION_DEFAULT_SMOKE);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = instant ? 0.95f : 0.85f;
		}
	}
	
	public static void createCustomExplosion(Vector2 center, float radius, float deep, boolean instant, Color[] quadcolors, Color smokeColor)
	{
		Lighting.Create(center, radius*2, quadcolors[1], 1f);
		int particles = (int) ((Math.pow(radius, 1.15f)) * deep);
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8);
			float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
			float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
			float distance = MathUtils.random(radius);
			Color color = new Color();
			if(distance < radius/4)
				color = quadcolors[0];
			else if(distance < radius/3f)
				color = quadcolors[1];
			else if(distance < radius/1.5f)
				color = quadcolors[2];
			else
				color = quadcolors[3];

			Vector2 pos = new Vector2(cos * distance, sin * distance).add(center);
			Vector2 vel = new Vector2(cos * 100 + MathUtils.random(-10, 10), sin * 100 + MathUtils.random(-10, 10));
			if(!instant)
			{
				pos = new Vector2(cos * distance / 10, sin * distance/10).add(center);
				vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			}
			Particle p = Particle.Create(pos, vel, 24, color, 0f, 1f, 1f);
			p.setLight((int) (16*p.scale), color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = instant ? 0.95f : 0.85f;
		}
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8);
			float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
			float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
			float distance = MathUtils.random(radius);
			Vector2 pos = new Vector2(cos * distance, sin * distance).add(center);
			Vector2 vel = new Vector2(cos * 150 + MathUtils.random(-25, 25), sin * 150 + MathUtils.random(-25, 25));
			if(!instant)
			{
				pos = new Vector2(cos * distance / 10, sin * distance/10).add(center);
				vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			}
			Particle p = Particle.Create(pos, vel, 24, smokeColor, 0f, 1f, 1f);
			p.setLight((int) (16*p.scale), smokeColor);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = instant ? 0.95f : 0.85f;
		}
	}
	
	public static void createBackCustomExplosion(Vector2 center, float radius, float deep, boolean instant, Color[] quadcolors, Color smokeColor)
	{
		Lighting.Create(center, radius*2, quadcolors[1], 1f);
		int particles = (int) ((Math.pow(radius, 1.15f)) * deep);
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8);
			float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
			float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
			float distance = MathUtils.random(radius);
			Color color = new Color();
			if(distance < radius/4)
				color = quadcolors[0];
			else if(distance < radius/3f)
				color = quadcolors[1];
			else if(distance < radius/1.5f)
				color = quadcolors[2];
			else
				color = quadcolors[3];

			Vector2 pos = new Vector2(cos * distance, sin * distance).add(center);
			Vector2 vel = new Vector2(cos * 40 + MathUtils.random(-10, 10), sin * 40 + MathUtils.random(-10, 10));
			if(!instant)
			{
				pos = new Vector2(cos * distance / 10, sin * distance/10).add(center);
				vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			}
			Particle p = Particle.Create(pos, vel, 24, color, -4f, 1f, 1f);
			p.setLight((int) (16*p.scale), color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = instant ? 0.95f : 0.85f;
		}
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8);
			float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
			float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
			float distance = MathUtils.random(radius);
			Vector2 pos = new Vector2(cos * distance, sin * distance).add(center);
			Vector2 vel = new Vector2(cos * 70 + MathUtils.random(-25, 25), sin * 70 + MathUtils.random(-25, 25));
			if(!instant)
			{
				pos = new Vector2(cos * distance / 10, sin * distance/10).add(center);
				vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			}
			Particle p = Particle.Create(pos, vel, 24, smokeColor, -4f, 1f, 1f);
			p.setLight((int) (16*p.scale), smokeColor);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = instant ? 0.95f : 0.85f;
		}
	}
	
	public static void setCameraRotation(float angle)
	{
		float oldDesired = desiredRotation;
		desiredRotation = angle;
		rotateby = desiredRotation-oldDesired;
	}
	
	public static void createEntExplosion(Entity ent, float radius, float deep, boolean instant)
	{
		Lighting.Create(ent.Center(), radius*2, Constant.EXPLOSION_DEFAULT_SCHEME[1], 1f);
		int particles = (int) ((Math.pow(radius, 1.15f)) * deep);
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8);
			float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
			float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
			float distance = MathUtils.random(radius);
			Color color = new Color();
			if(distance < radius/4)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[0];
			else if(distance < radius/3f)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[1];
			else if(distance < radius/1.5f)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[2];
			else
				color = Constant.EXPLOSION_DEFAULT_SCHEME[3];

			Vector2 pos = new Vector2(cos * distance, sin * distance).add(ent.randomHitBoxPosition());
			Vector2 vel = new Vector2(cos * 40 + MathUtils.random(-10, 10), sin * 40 + MathUtils.random(-10, 10));
			if(!instant)
			{
				pos = new Vector2(cos * distance / 10, sin * distance/10).add(ent.randomHitBoxPosition());
				vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			}
			Particle p = Particle.Create(pos, vel, 24, color, 0f, 1f, 2f);
			p.setLight((int) (16*p.scale), color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = instant ? 0.95f : 0.85f;
		}
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8);
			float cos = (float)Math.cos(extra+i*(360f/particles)*Math.PI/180);
			float sin = (float)Math.sin(extra+i*(360f/particles)*Math.PI/180);
			float distance = MathUtils.random(radius);
			Vector2 pos = new Vector2(cos * distance, sin * distance).add(ent.randomHitBoxPosition());
			Vector2 vel = new Vector2(cos * 70 + MathUtils.random(-25, 25), sin * 70 + MathUtils.random(-25, 25));
			if(!instant)
			{
				pos = new Vector2(cos * distance / 10, sin * distance/10).add(ent.randomHitBoxPosition());
				vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			}
			Particle p = Particle.Create(pos, vel, 24, Constant.EXPLOSION_DEFAULT_SMOKE, 0f, 1f, 1f);
			p.setLight((int) (16*p.scale), p.color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = instant ? 0.95f : 0.85f;
		}
	}
	
	public static void createCustomEntConeExplosion(Entity ent, float radius, float deep, float angle, float wideangle, Color[] quadcolors, Color smokeColor)
	{
		Lighting.Create(ent.Center(), radius*2, quadcolors[1], 1f);
		int particles = (int) ((Math.pow(radius, 1.15f) / Math.min(2, (360/wideangle))) * deep);
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8) + angle + MathUtils.random(-wideangle/2f, wideangle/2f);
			float cos = (float)Math.cos(extra*Math.PI/180);
			float sin = (float)Math.sin(extra*Math.PI/180);
			float distance = MathUtils.random(radius);
			Color color = new Color();
			if(distance < radius/4)
				color = quadcolors[0];
			else if(distance < radius/3f)
				color = quadcolors[1];
			else if(distance < radius/1.5f)
				color = quadcolors[2];
			else
				color = quadcolors[3];

			Vector2 vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			Particle p = Particle.Create(ent.randomHitBoxPosition(), vel, 24, color, 0f, 1f, 2f);
			p.setLight((int) (16*p.scale), color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = 0.85f;
		}
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8) + angle + MathUtils.random(-wideangle/2f, wideangle/2f);
			float cos = (float)Math.cos(extra*Math.PI/180);
			float sin = (float)Math.sin(extra*Math.PI/180);
			float distance = MathUtils.random(radius);
			Vector2 vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			Particle p = Particle.Create(ent.randomHitBoxPosition(), vel, 24, smokeColor, 0f, 1f, 1f);
			p.setLight((int) (16*p.scale), p.color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = 0.85f;
		}
	}
	
	public static void createEntConeExplosion(Entity ent, float radius, float deep, float angle, float wideangle)
	{
		Lighting.Create(ent.Center(), radius*2, Constant.EXPLOSION_DEFAULT_SCHEME[1], 1f);
		int particles = (int) ((Math.pow(radius, 1.15f) / Math.min(2, (360/wideangle))) * deep);
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8) + angle + MathUtils.random(-wideangle/2f, wideangle/2f);
			float cos = (float)Math.cos(extra*Math.PI/180);
			float sin = (float)Math.sin(extra*Math.PI/180);
			float distance = MathUtils.random(radius);
			Color color = new Color();
			if(distance < radius/4)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[0];
			else if(distance < radius/3f)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[1];
			else if(distance < radius/1.5f)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[2];
			else
				color = Constant.EXPLOSION_DEFAULT_SCHEME[3];

			Vector2 vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			Particle p = Particle.Create(ent.randomHitBoxPosition(), vel, 24, color, 0f, 1f, 2f);
			p.setLight((int) (16*p.scale), color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = 0.85f;
		}
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8) + angle + MathUtils.random(-wideangle/2f, wideangle/2f);
			float cos = (float)Math.cos(extra*Math.PI/180);
			float sin = (float)Math.sin(extra*Math.PI/180);
			float distance = MathUtils.random(radius);
			Vector2 vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			Particle p = Particle.Create(ent.randomHitBoxPosition(), vel, 24, Constant.EXPLOSION_DEFAULT_SMOKE, 0f, 1f, 1f);
			p.setLight((int) (16*p.scale), p.color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = 0.85f;
		}
	}
	
	public static void createConeExplosion(Vector2 center, float radius, float deep, float angle, float wideangle)
	{
		Lighting.Create(center, radius*2, Constant.EXPLOSION_DEFAULT_SCHEME[1], 1f);
		int particles = (int) ((Math.pow(radius, 1.15f) / Math.min(2, (360/wideangle))) * deep);
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8) + angle + MathUtils.random(-wideangle/2f, wideangle/2f);
			float cos = (float)Math.cos(extra*Math.PI/180);
			float sin = (float)Math.sin(extra*Math.PI/180);
			float distance = MathUtils.random(radius);
			Color color = new Color();
			if(distance < radius/4)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[0];
			else if(distance < radius/3f)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[1];
			else if(distance < radius/1.5f)
				color = Constant.EXPLOSION_DEFAULT_SCHEME[2];
			else
				color = Constant.EXPLOSION_DEFAULT_SCHEME[3];

			//Vector2 pos = new Vector2(cos * distance, sin * distance).add(center);
			Vector2 vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			Particle p = Particle.Create(center, vel, 24, color, 0f, 1f, 2f);
			p.setLight((int) (16*p.scale), color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = 0.85f;
		}
		for(int i = 1;i < particles;i++)
		{
			float extra = MathUtils.random(-8, 8) + angle + MathUtils.random(-wideangle/2f, wideangle/2f);
			float cos = (float)Math.cos(extra*Math.PI/180);
			float sin = (float)Math.sin(extra*Math.PI/180);
			float distance = MathUtils.random(radius);
			Vector2 vel = new Vector2((float) (cos * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)), (float) (sin * Math.pow(distance + 40, 1.4f) + MathUtils.random(-10, 10)));
			Particle p = Particle.Create(center, vel, 24, Constant.EXPLOSION_DEFAULT_SMOKE, 0f, 1f, 1f);
			p.setLight((int) (16*p.scale), p.color);
			p.loseSpeed = true;
			p.drawFront = true;
			p.loseSpeedMult = 0.85f;
		}
	}
	
	public static boolean circleContainsRect(Circle circle, Rectangle rect)
	{
		float dx = Math.max(Math.abs(circle.x - (rect.getX())), Math.abs((rect.getX()+rect.getWidth()) - circle.x));
		float dy = Math.max(Math.abs(circle.y - (rect.getY() + rect.getHeight())), Math.abs(rect.getY() - circle.y));
		return (circle.radius * circle.radius) >= (dx * dx) + (dy * dy);
	}
	
	public static boolean circleContainsPolygon(Circle circle, Polygon poly)
	{
		float[] vert = poly.getTransformedVertices();
		Vector2[] points = new Vector2[vert.length/2];
		for(int i = 0;i <vert.length;i+=2)
		{
			points[i/2] = new Vector2(vert[i], vert[i+1]);
			if(points[i/2].dst(circle.x, circle.y) > circle.radius)
			{
				return false;
			}
		}
		return true;
	}
	
	public static void drawBoxedSprite(SpriteBatch batch, Texture boxTexture, float x, float y, float width, float height)
	{
		width += 4;
		height += 4;
		Sprite box = new Sprite(boxTexture);
		box.setPosition(x-2, y-2);
		box.setRegion(0, 46, 10, 10);
		box.setSize(10, 10);
		box.draw(batch);
		
		box = new Sprite(boxTexture);
		box.setPosition(x-2, y+height-8);
		box.setRegion(0, 0, 10, 10);
		box.setSize(10, 10);
		box.draw(batch);
		
		box = new Sprite(boxTexture);
		box.setPosition(x+width-8, y+height-8);
		box.setRegion(46, 0, 10, 10);
		box.setSize(10, 10);
		box.draw(batch);
		
		box = new Sprite(boxTexture);
		box.setPosition(x+width-8, y-2);
		box.setRegion(46, 46, 10, 10);
		box.setSize(10, 10);
		box.draw(batch);
		
		
		
		box = new Sprite(boxTexture);
		box.setPosition(x-2, y+8);
		box.setRegion(0, 10, 10, 36);
		box.setSize(10, height-16);
		box.draw(batch);
		
		box = new Sprite(boxTexture);
		box.setPosition(x+8, y+height-8);
		box.setRegion(10, 0, 36, 10);
		box.setSize(width-16, 10);
		box.draw(batch);
		
		box = new Sprite(boxTexture);
		box.setPosition(x+width-8, y+8);
		box.setRegion(46, 10, 10, 36);
		box.setSize(10, height-16);
		box.draw(batch);
		
		box = new Sprite(boxTexture);
		box.setPosition(x+8, y+2);
		box.setRegion(10, 42, 36, 10);
		box.setSize(width-16, 6);
		box.draw(batch);
		
		
		box = new Sprite(boxTexture);
		box.setPosition(x+8, y+8);
		box.setRegion(10, 10, 36, 36);
		box.setSize(width-16, height-16);
		box.draw(batch);
	}
	
	public static void drawGenericBox(SpriteBatch batch, float x, float y, float width, float height)
	{
		Main.drawBoxedSprite(batch, Content.genericBox, x, y, width, height);
	}
	
	public static void setCustomCamera(Vector2 position, float zoom, int ticks)
	{
		Main.customCameraPosition = position;
		Main.customCameraZoom = zoom;
		Main.customCameraTime = ticks;
	}
	
	public static Vector2 vectorNoise(float maxX, float maxY)
	{
		float maxX2 = Math.abs(maxX);
		float maxY2 = Math.abs(maxY);
		return new Vector2(MathUtils.random(-maxX2, maxX2), MathUtils.random(-maxY2, maxY2));
	}
	
	public static Vector2 vectorNoise(float max)
	{
		return Main.vectorNoise(max, max);
	}

	public static void drawBlurredTexture(SpriteBatch sb, TextureRegion txt, float x, float y, float radiusX, float radiusY, int steps)
	{
		steps = Math.max(1, steps);
		Sprite sprite = new Sprite(txt);
		for(int i = 0;i <= steps;i++)
		{
			float progress = (float)i/steps;
			Vector2 offset = new Vector2(radiusX, radiusY).scl(progress);
			sprite.setPosition((int)(x + offset.x), (int)(y + offset.y));
			sprite.setAlpha(i == 0 ? 1 : (float)(0.734124 / Math.pow(2, i)));
			sprite.draw(sb);
		}
	}
	
	public static float blackScreenProgress()
	{
		return Main.clamp(0f, (blackScreenTime+3f)/4f, 1f);
	}
	
	public static void saveKeysState()
	{
		//toolkit.sync();
		//toolkit = Toolkit.getDefaultToolkit();
		//lockingKeysState = new Boolean[] {toolkit.getLockingKeyState(KeyEvent.VK_NUM_LOCK), toolkit.getLockingKeyState(KeyEvent.VK_CAPS_LOCK), toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK)};
		//System.out.println("Saved key state: [" + lockingKeysState[0] + ", " + lockingKeysState[1] + ", " + lockingKeysState[2] + "]");
	}
	
	public static void loadKeysState()
	{
		setLockingKey(KeyEvent.VK_NUM_LOCK, originalKeysState[0]);
		setLockingKey(KeyEvent.VK_CAPS_LOCK, originalKeysState[1]);
		setLockingKey(KeyEvent.VK_SCROLL_LOCK, originalKeysState[2]);
	}
	
	public static void setLockingKey(int keyCode, boolean value)
	{
		int val = -1;
		switch(keyCode)
		{
		case KeyEvent.VK_NUM_LOCK:
			val = 0;
			break;
		case KeyEvent.VK_CAPS_LOCK:
			val = 1;
			break;
		case KeyEvent.VK_SCROLL_LOCK:
			val = 2;
			break;
		}
		if(val != -1)
		{
			if(trackedKeysState[val] != value)
			{
				toolkit.setLockingKeyState(keyCode, !originalKeysState[val]);
				trackedKeysState[val] = !trackedKeysState[val];
			}
		}
	}
	
	public static void DrawStory()
	{
		if(Main.currentStory != null && Main.displayDialog)
		{
			if(Main.currentStoryBGAlpha < 1)
			{
				Main.currentStoryBGAlpha = Math.min(Main.currentStoryBGAlpha+0.05f, 1f);
			}
			else
			{
				if(Main.currentLoadedStory != Main.currentStory)
				{
					if(Main.currentStoryImageAlpha > 0)
					{
						Main.currentStoryImageAlpha = Math.max(Main.currentStoryImageAlpha-0.05f, 0f);
					}
					if(Main.currentStoryImageAlpha <= 0f)
					{
						Main.currentLoadedStory = Main.currentStory;
					}
				}
				else
				{
					if(Main.currentStoryImageAlpha < 1)
					{
						Main.currentStoryImageAlpha = Math.min(Main.currentStoryImageAlpha+0.05f, 1f);
					}
				}
			}
		}
		else
		{
			if(Main.currentStoryBGAlpha > 0f)
			{
				Main.currentStoryBGAlpha = Math.max(Main.currentStoryBGAlpha-0.05f, 0f);
			}
			if(Main.currentStoryImageAlpha > 0f)
			{
				Main.currentStoryImageAlpha = Math.max(Main.currentStoryImageAlpha-0.05f, 0f);
			}
			Main.currentStory = null;
		}
		
		if(Main.currentStoryBGAlpha > 0f)
		{
			hudBatch.begin();
			Sprite white = new Sprite(Content.white);
			white.setPosition(-100, -100);
			white.setSize(Gdx.graphics.getWidth()+200, Gdx.graphics.getHeight()+200);
			white.setColor(new Color(66/255f, 59/255f, 46/255f, 1f));
			white.setAlpha(Main.currentStoryBGAlpha);
			white.draw(hudBatch);
			
			if(Main.currentLoadedStory != null)
			{
				white = new Sprite(Main.currentLoadedStory.texture);
				float width = white.getWidth();
				float x = Gdx.graphics.getWidth()/2f-width/2f;
				float y = 400;
				white.setPosition(x, y);
				white.setAlpha(Main.currentStoryImageAlpha);
				white.draw(hudBatch);
			}
			hudBatch.end();
		}
	}
	
	public static void loadStory(Story s)
	{
		if(Main.currentStory == null)
		{
			Main.currentStoryBGAlpha = 0f;
			Main.currentStoryImageAlpha = 0f;
			Main.currentLoadedStory = s;
		}
		Main.currentStory = s;
	}
	
	public static Color getLightingColor(Color c, float simplify)
	{
		return c.cpy().mul(1f-simplify, 1f-simplify, 1f-simplify, 1f).add(new Color(simplify, simplify, simplify, 0f));
	}
	
	public static float mixFloats(float f1, float f2, float mix)
	{
		return f1+(f2-f1)*Main.clamp(0f, mix, 1f);
	}
}