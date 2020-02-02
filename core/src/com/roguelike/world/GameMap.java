package com.roguelike.world;

import java.io.File;
import java.io.FilenameFilter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.roguelike.constants.Biome;
import com.roguelike.constants.Constant;
import com.roguelike.constants.Dialogs;
import com.roguelike.constants.Item;
import com.roguelike.constants.Quest;
import com.roguelike.entities.Monster;
import com.roguelike.entities.NPC;
import com.roguelike.entities.Prop;
import com.roguelike.entities.Treasure;
import com.roguelike.game.Content;
import com.roguelike.game.DJ;
import com.roguelike.game.Event;
import com.roguelike.game.Main;
import com.roguelike.online.AHS;
import com.roguelike.entities.Player;

public class GameMap
{
	public Tile[][] map;
	public Tile[][] fg;
	public Background[][][] bg;
	public int width;
	public int height;
	public String name;
	public String subtitle;
	public int id;
	public Color bgColor = null;
	public int mapTick = 0;
	public transient float[][] lightMap;
	public transient boolean[][] madeLight;
	public boolean usesLight = true;
	public float minimumLight = 0.15f;
	public float maximumLight = 1f;
	public int[] expected = new int[4];
	public transient static int oldWidth = 172;
	public transient static int oldHeight = 172;
	public int[] infos = new int[10];
	public float zoom = 1f;
	public String useShader = "";
	public int globalX;
	public int globalY;
	public long lastUpdateTime = 0;
	public DJ backgroundMusic;
	public Vector2 initialPosition;
	
	public ArrayList<Monster> monsters = new ArrayList<Monster>();
	public transient ArrayList<Monster> naturals = new ArrayList<Monster>();
	public transient ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Treasure> treasures = new ArrayList<Treasure>();
	public ArrayList<NPC> npcs = new ArrayList<NPC>();
	public ArrayList<Prop> props = new ArrayList<Prop>();
	public ArrayList<Respawn> respawns = new ArrayList<Respawn>();
	public transient ArrayList<Integer> randomrespawns = new ArrayList<Integer>();
	
	public void initialize()
	{
		for(Item i : items)
		{
			i.initialize();
		}
		for(NPC n : npcs)
		{
			n.initialize();
			n.myMapX = this.globalX;
			n.myMapY = this.globalY;
		}
		ArrayList<Monster> monsterlist = new ArrayList<Monster>();
		monsterlist.addAll(monsters);
		monsterlist.addAll(naturals);
		for(Monster m : monsterlist)
		{
			m.initialize();
		}
		if(fg == null)
		{
			fg = new Tile[map.length][map[0].length];
		}
	}
	
	public static GameMap GenerateBaseMap(int biomeType, int[] specialFlag, int myX, int myY, int comeGX, int comeGY, int comeLX, int comeLY, boolean ignoreWidth)
	{
		return GenerateBaseMap(biomeType, specialFlag, myX, myY, comeGX, comeGY, comeLX, comeLY, ignoreWidth, -1);
	}
	
	public static GameMap GenerateBaseMap(int biomeType, int[] specialFlag, int myX, int myY, int comeGX, int comeGY, int comeLX, int comeLY, boolean ignoreMapSymmetry, int customWidth)
	{
		GameMap g = new GameMap();
		g.globalX = myX;
		g.globalY = myY;
		GameMap cm = Main.cameMap;
		if(specialFlag == null)
		{
			specialFlag = new int[] {MathUtils.random(1000000, Integer.MAX_VALUE/2),
					MathUtils.random(1000000, Integer.MAX_VALUE/2),
					MathUtils.random(1000000, Integer.MAX_VALUE/2),
					MathUtils.random(1000000, Integer.MAX_VALUE/2),
					MathUtils.random(1000000, Integer.MAX_VALUE/2)};
		}
		
		for(int i = 0;i < 5;i++)
		{
			if(specialFlag[i] == 1337)
			{
				specialFlag[i] = MathUtils.random(1000000, Integer.MAX_VALUE/2);
			}
		}
		System.out.println("Generating new map with flags: [" + specialFlag[0] + " " + specialFlag[1] + " " + specialFlag[2] + " "
				 + specialFlag[3] + " " + specialFlag[4] + "]");
		
		for(int i = 0;i < 10;i++)
		{
			g.infos[i] = 0;
		}
		g.mapTick = Main.gameTick;
		int saveId = biomeType;
		for(int i = 0;i < 4;i++)
		{
			g.expected[i] = biomeType;
		}
		g.width = -1;
		if(!ignoreMapSymmetry && Main.getLayerY(myY) != 3)
			g.width = GameMap.getXMapWidth(myX, myY);
		
		if(g.width == -1)
			g.width = MathUtils.random(200, 350);
		
		if(customWidth > 0)
		{
			g.width = customWidth;
		}
		
		if(biomeType <= 1)
		{
			saveId = 1;
			//g.width = 172;
			g.height = 172;
			g.name = "Test Area";
			g.id = 1;
			g.zoom = Constant.MAPZOOM_BIG;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			g.usesLight = false;
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.METALFLOOR;

					boolean isHorizontal = (int)((y+4)/20)%2 == 0;
					boolean isVertical = (int)((x+4)/20)%2 == 0;

					if((y+4)/20%2 == 0 && (x+4)/20%2 == 0)
					{
						g.map[y][x] = null;
					}

					if(isHorizontal && (y+4)%20 >= 8 && (y+4)%20 <= 11)
					{
						g.map[y][x] = null;
					}
					if(isVertical && (x+4)%20 >= 8 && (x+4)%20 <= 11)
					{
						g.map[y][x] = null;
					}

					if(x < 4 || x >= g.width - 4 || y < 4 || y >= g.height - 4)
					{
						g.map[y][x] = Tile.METALFLOOR;
					}
				}
			}
			Monster.Create(g, new Vector2(2624, 2304), 1);
		}
		else if(biomeType == 2)
		{
			//g.width =  MathUtils.random(400, 800);
			g.height = 100;
			g.name = "Peaceful Fields";
			g.id = 2;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = null;
					if(y < 16)
					{
						g.map[y][x] = Tile.GRASS;
					}
				}
			}
			Respawn r;
			for(int y = 16;y < g.height-1;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					int chance = MathUtils.random(6);
					if(g.map[y-1][x] == Tile.GRASS && chance == 0)
					{
						g.bg[y][x][0] = Background.getTileTypeById(MathUtils.random(17, 21));
					}
					else if(g.map[y-1][x] == Tile.GRASS && chance < 4)
					{
						g.bg[y][x][0] = Background.HIGHGRASS;
					}
					else if(g.map[y-1][x] != null && chance == 5 && MathUtils.random(4) == 0)
					{
						r = new Respawn(46, 300, new Vector2(x, y).scl(64f));
						g.respawns.add(r);
					}
				}
			}
			System.out.println("Starting Plains spawn generation.");
			for(int x = 50;x < g.width - 50;x++)
			{
				System.out.println(x + "/" + g.width + " - Trying to generate a surrounded spawn");
				if(MathUtils.random(4) == 0)
				{
					System.out.println("Surrounded spawn succeeded, randomizing the surround type.");
					if(MathUtils.random(3) != 0)
					{
						System.out.println("Creating a cornfield.");
						int size = MathUtils.random(15, 25);
						Framework.createLine(g, x, 16, 2, Background.CORN, size);
						Framework.createLine(g, x, 16, 1, Background.CORN2, size);
						Framework.createLine(g, x, 16, 0, Background.CORN3, size);
						Framework.createLine(g, x, 15, Tile.PLOWEDLAND, size);
						r = new Respawn(23, 300, new Vector2(x + size/2, 16).scl(64f));
						g.respawns.add(r);
						x += size + MathUtils.random(20, 40);
					}
					else
					{
						System.out.println("Creating a bull breeding.");
						int size = MathUtils.random(25, 35);
						Framework.createLine(g, x, 16, 0, Background.CATTLEFENCE, size);
						float sizeVar = size * 32f;
						for(int i = 0;i < 1;i++)
						{
							r = new Respawn(11, 300, new Vector2(x + size/2, 16).scl(64f).add(MathUtils.random(-sizeVar, sizeVar), 0));
							g.respawns.add(r);
						}
						if(MathUtils.random(4) == 0)
						{
							for(int i = 0;i < MathUtils.random(1, 2);i++)
							{
								r = new Respawn(11, 300, new Vector2(x + size/2, 16).scl(64f).add(MathUtils.random(-sizeVar, sizeVar), 0));
								g.respawns.add(r);
							}
						}
						x += size + MathUtils.random(20, 40);
					}
				}

				x += MathUtils.random(10, 15);
			}
			
			g.expected[Constant.AIDIRECTION_UP-1] = -1;
		}
		else if(biomeType == 3)
		{
			//g.width =  100;
			g.height = 100;
			g.name = "Caverns";
			g.id = 3;
			g.zoom = Constant.MAPZOOM_NORMAL;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][1];
			g.bgColor = new Color(0.1f, 0.1f, 0.1f, 1f);

			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.STONE;

					boolean isHorizontal = (int)((y+4)/15)%2 == 0;
					boolean isVertical = (int)((x+4)/15)%2 == 0;

					if((y+4)/15%2 == 0 && (x+4)/15%2 == 0)
					{
						g.map[y][x] = null;
						g.bg[y][x][0] = Background.CAVE;
					}

					if(isHorizontal && (y+4)%15 >= 6 && (y+4)%15 <= 8)
					{
						g.map[y][x] = null;
						g.bg[y][x][0] = Background.CAVE;
					}
					if(isVertical && (x+4)%15 >= 6 && (x+4)%15 <= 8)
					{
						g.map[y][x] = null;
						g.bg[y][x][0] = Background.CAVE;
					}

					if(x < 4 || x >= g.width - 4 || y < 3 || y >= g.height - 4)
					{
						g.map[y][x] = Tile.STONE;
					}
				}
			}
			int h = MathUtils.random(0, 3);
			g.infos[0] = h;
			for(int y = 3 + h*30;y < 5 + h*30;y++)
			{
				for(int x = g.width-4;x < g.width;x++)
				{
					g.map[y][x] = null;
				}
			}
			
			int hcm = 0;
			if(cm.id == g.id)
			{
				hcm = cm.infos[0];
			}
			
			for(int y = 3 + hcm*30;y < 5 + hcm*30;y++)
			{
				for(int x = 0;x < 4;x++)
				{
					g.map[y][x] = null;
				}
			}
			

			for(int i = 0;i < 15;i++)
			{
				//Monster m = Monster.Create(new Vector2(MathUtils.random(g.width*Tile.TILE_SIZE), MathUtils.random(g.height*Tile.TILE_SIZE)), 1, true);
				//g.monsters.add(m);
			}
		}
		else if(biomeType == 5)
		{
			//g.width =  1000;
			g.height = 100;
			g.name = "The Truth";
			g.id = 5;
			g.zoom = Constant.MAPZOOM_NORMAL;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			g.bgColor = new Color(1f, 1f, 1f, 1f);
			g.usesLight = false;
		}
		else if(biomeType == 4)
		{
			//g.width =  80;
			g.height = 100;
			g.name = "Cavern Room";
			g.id = 4;
			g.zoom = Constant.MAPZOOM_BIG;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			g.bgColor = new Color(0.1f, 0.1f, 0.1f, 1f);

			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = null;
					g.bg[y][x][0] = Background.CAVE;

					if(x < 4 || x >= g.width - 4 || y < 3 || y >= g.height - 4)
					{
						g.map[y][x] = Tile.STONE;
					}
				}
			}
			for(int y = 8;y < g.height;y++)
			{
				for(int x = 0;x < 20;x++)
				{
					g.map[y][x] = Tile.STONE;
				}
			}
			int hcm = 0;
			if(cm.id == 3)
			{
				hcm = cm.infos[0];
			}
			
			for(int y = 3 + hcm*30;y < 5 + hcm*30;y++)
			{
				for(int x = 0;x < 20;x++)
				{
					g.map[y][x] = null;
				}
			}
			for(int i = 0;i < 4;i++)
			{
				//Monster m = Monster.Create(new Vector2(MathUtils.random(g.width*Tile.TILE_SIZE), MathUtils.random(g.height*Tile.TILE_SIZE)), 1, true);
				//g.monsters.add(m);
			}
			//Monster m = Monster.Create(new Vector2(75*Tile.TILE_SIZE, 5*Tile.TILE_SIZE), 2, true);
			//g.monsters.add(m);
		}
		else if(biomeType == 6)
		{
			//g.width =  70;
			g.height = 100;
			g.name = "Hidden Room";
			g.id = 6;
			g.zoom = Constant.MAPZOOM_BIG;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			g.bgColor = new Color(0.1f, 0.1f, 0.1f, 1f);
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.STONE;
					g.bg[y][x][0] = Background.CAVE;
					if((x > 0 && x < g.width-1) && (y > 0 && y < g.height-1))
					{
						g.map[y][x] = null;
					}
				}
			}
			//Monster m = Monster.Create(new Vector2(MathUtils.random(g.width*Tile.TILE_SIZE), MathUtils.random(g.height*Tile.TILE_SIZE)), 4, true);
			//g.monsters.add(m);
		}
		else if(biomeType == 7)
		{
			//g.width =  80;
			g.height = 100;
			g.name = "Sewer";
			g.id = 7;
			g.zoom = Constant.MAPZOOM_CLOSE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			g.bgColor = Color.BLACK;
			g.useShader = "scared";
			g.minimumLight = 0.15f;
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.STONE;
					if(y > 4 && y < 8)
					{
						g.bg[y][x][0] = Background.BRICKS;
						if(MathUtils.random() <= 0.93f)
						{
							g.bg[y][x][0] = Background.BRICKS2;
						}
						
						g.map[y][x] = null;
						
						if(x == g.width-1)
							g.map[y][x] = Tile.INVISIBLELIGHT;
					}
				}
			}
			g.expected[Constant.AIDIRECTION_RIGHT-1] = 2;
		}
		else if(biomeType == 8)
		{
			//g.width =  200;
			g.height = 100;
			g.name = "yTic dEtpuRroCc";
			g.id = 8;
			g.zoom = Constant.MAPZOOM_HUGE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = null;
					if(y == 5)
					{
						g.map[y][x] = Tile.COBBLESTONE;
					}
					else if(y < 5)
					{
						g.map[y][x] = Tile.ASPHALT;
					}
				}
			}
			for(int x = 10;x < g.width-20;x++)
			{
				if(MathUtils.random(3) == 0)
				{
					int width = 5 + MathUtils.random(0, 5)*2;
					int height = MathUtils.random(4, 6);
					int roofheight = MathUtils.random(2,3);
					Framework.createHouse(g, x, 4, width, height, roofheight);
					x += width+2;
				}
			}
			
			for(int i = 0;i < 7;i++)
			{
				//Monster m = Monster.Create(new Vector2(MathUtils.random(g.width*Tile.TILE_SIZE), MathUtils.random(g.height*Tile.TILE_SIZE)), 5, true);
				//m.position = g.getNextestFreeSpace(m.position.x, m.position.y, m.width, m.height, 1, 1).scl(Tile.TILE_SIZE);
				//g.monsters.add(m);
			}
		}
		else if(biomeType == 9)
		{
			//g.width =  200;
			g.height = 100;
			g.name = "Thrash City";
			g.id = 9;
			g.zoom = Constant.MAPZOOM_BIG;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = null;
					if(y == 5)
					{
						g.map[y][x] = Tile.COBBLESTONE;
					}
					else if(y < 5)
					{
						g.map[y][x] = Tile.ASPHALT;
					}
				}
			}
			for(int x = 0;x < g.width;x++)
			{
				if(MathUtils.random(3) == 0)
				{
					int y = 6;
					int w = MathUtils.random(5, 9);
					int h = MathUtils.random(10, 20);
					Framework.createThrashDarkBuilding(g, x, y, w, h);
					x += (w + 2);
				}
			}
		}
		else if(biomeType == 10)
		{
			//g.width =  10;
			g.height = 7;
			g.name = "Lushmael Room";
			g.id = 10;
			g.zoom = Constant.MAPZOOM_NORMAL;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = null;
					if(x == 0 || x == g.width-1 || y == 0 || y == g.height-1)
					{
						g.map[y][x] = Tile.GRASS;
					}
				}
			}
		}
		else if(biomeType == 11)
		{
			//g.width =  200;
			g.height = 100;
			g.name = "Bridge";
			g.id = 11;
			g.zoom = Constant.MAPZOOM_BIG;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = null;
					if(y < 4)
					{
						g.map[y][x] = Tile.GRASS;
					}
				}
			}
			for(int y = 0;y < 4;y++)
			{
				for(int x = 150;x < 160;x++)
				{
					if(y < 3)
						g.map[y][x] = null;
					else
					{
						g.map[y][x] = Tile.PROCESSEDWOOD;
						g.bg[y+1][x][0] = Background.BRIDGESIDE;
					}
				}
			}
			
			for(int y = 4;y < g.height-1;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					int chance = MathUtils.random(6);
					if(g.map[y-1][x] == Tile.GRASS && chance == 0)
					{
						g.bg[y][x][0] = Background.getTileTypeById(MathUtils.random(17, 21));
					}
					else if(g.map[y-1][x] == Tile.GRASS && chance < 4)
					{
						g.bg[y][x][0] = Background.HIGHGRASS;
					}
				}
			}
			//Monster m = Monster.Create(new Vector2((165)*Tile.TILE_SIZE, 5*Tile.TILE_SIZE), 2, true);
			//m.position = g.getNextestFreeSpace(m.position.x, m.position.y, m.width, m.height, 1, 1).scl(Tile.TILE_SIZE);
			//g.monsters.add(m);
			for(int i = 0;i < 5;i++)
			{
				//m = Monster.Create(new Vector2((g.width/2+MathUtils.random(140, 160))*Tile.TILE_SIZE, 5*Tile.TILE_SIZE), 1, true);
				//g.monsters.add(m);
			}

			for(int i = 0;i < 8;i++)
			{
				//m = Monster.Create(new Vector2((g.width/2+MathUtils.random(0, 100))*Tile.TILE_SIZE, 5*Tile.TILE_SIZE), 1, true);
				//g.monsters.add(m);
			}

			g.expected[Constant.AIDIRECTION_UP-1] = -1;
			
			if(Main.player[Main.me].myMapX == 52)
			{
				NPC n = new NPC();
				n.SetInfos(1);
				n.position = new Vector2(35 * 64, 6 * 64);
				n.active = true;
				g.npcs.add(n);
			}
		}
		else if(biomeType == 101)
		{
			//g.width =  MathUtils.random(500, 1000);
			g.height = 100;
			g.name = "Snowy Plains";
			g.id = 101;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					if(y < 16)
					{
						g.map[y][x] = Tile.SNOWGRASS;
					}
				}
			}
			for(int i = 0;i < 2;i++)
			{
				for(int x = 0;x < g.width;x++)
				{
					if(MathUtils.randomBoolean(0.7f))
					{
						int r = MathUtils.random(3);
						if(r == 0)
						{
							g.bg[16][x][i] = Background.SNOWHIGHGRASS;
						}
						else if(r == 1)
						{
							g.bg[16][x][i] = Background.SNOWHIGHGRASS2;
						}
						else if(r == 2)
						{
							g.bg[16][x][i] = Background.SMALLSTONES;
						}
						else if(r == 3)
						{
							g.bg[16][x][i] = Background.SMALLSTONES2;
						}
					}
				}
			}
			for(int x = 100;x < g.width * Tile.TILE_SIZE - 500;)
			{
				g.respawns.add(new Respawn(15, 300, new Vector2(x, 1024)));
				x += MathUtils.random(440, 860);
			}
			for(int i = 0;i < MathUtils.random(2, 7);i++)
			{
				g.respawns.add(new Respawn(9, 300, new Vector2(MathUtils.random(600, g.width * Tile.TILE_SIZE - 1000), 1024)));
			}
			for(int i = 0;i < MathUtils.random(2, 3);i++)
			{
				g.respawns.add(new Respawn(12, 300, new Vector2(MathUtils.random(600, g.width * Tile.TILE_SIZE - 1000), 1024)));
			}
			for(int i = 0;i < MathUtils.random(2, 5);i++)
			{
				g.respawns.add(new Respawn(13, 300, new Vector2(MathUtils.random(600, g.width * Tile.TILE_SIZE - 1000), 1024)));
			}
			for(int i = 0;i < MathUtils.random(2, 6);i++)
			{
				g.respawns.add(new Respawn(16, 300, new Vector2(MathUtils.random(600, g.width * Tile.TILE_SIZE - 1000), MathUtils.random(1320, 1700))));
			}
		}
		else if(biomeType == 102)
		{
			//g.width =  MathUtils.random(500, 1000);
			g.height = 100;
			g.name = "Snowy Uplands";
			g.id = 102;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			for(int y = 0;y < 16;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.SNOWGRASS;
				}
			}
			
			int currentX = MathUtils.random(30, 60);
			while(currentX < g.width - 50)
			{
				currentX += Framework.createMountain(g, currentX, 16, Tile.SNOWGRASS, MathUtils.random(10, 20), 0, 1, 8) + MathUtils.random(5, 12);
			}
			
			for(int y = 0;y < g.height-1;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					if(g.map[y][x] != null && g.map[y+1][x] == null)
					{
						if(MathUtils.randomBoolean(0.15f))
						{
							g.bg[y+1][x][0] = Background.SNOWHIGHGRASS;
							if(MathUtils.randomBoolean())
								g.bg[y+1][x][0] = Background.SNOWHIGHGRASS2;
						}
					}
				}
			}
			
			for(int y = 1;y < 30;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					if(g.map[y-1][x] != null && g.map[y][x] == null)
					{
						if(MathUtils.randomBoolean(0.2f))
						{
							int mid = 0;
							switch(MathUtils.random(1, 6))
							{
							case 1:
							{
								mid = 40;
								break;
							}
							case 2:
							{
								mid = 12;
								break;
							}
							case 3:
							{
								mid = 13;
								break;
							}
							case 4:
							{
								mid = 34;
								break;
							}
							case 5:
							{
								mid = 14;
								break;
							}
							case 6:
							{
								mid = 9;
								break;
							}
							}
							
							g.respawns.add(new Respawn(mid, 300, new Vector2(x, y).scl(64f)));
							x += MathUtils.random(30, 45);
						}
						x += MathUtils.random(10, 15);
					}
				}
			}

			for(int x = 0;x < g.width;x++)
			{
				if(MathUtils.randomBoolean(0.2f))
				{
					int y = 28 + MathUtils.random(-4, 4);
					int mid = 0;
					switch(MathUtils.random(1, 2))
					{
					case 1:
					{
						mid = 33;
						break;
					}
					case 2:
					{
						mid = 16;
						break;
					}
					}

					g.respawns.add(new Respawn(mid, 300, new Vector2(x, y).scl(64f)));
					x += MathUtils.random(30, 45);
				}
				x += MathUtils.random(10, 15);
			}

			for(int x = 10;x < g.width-10;x++)
			{
				int y = g.getFreeY(x);
				if(MathUtils.randomBoolean(0.4f) && g.map[y][x-2] == null && g.map[y][x-1] == null && g.map[y][x] == null && g.map[y][x+1] == null && g.map[y][x+2] == null)
				{
					Respawn r = new Respawn(15, 300, new Vector2(x * 64 - 217, 64 * y));
					g.respawns.add(r);
					x += 4;
				}
			}
		}
		else if(biomeType == 110)
		{
			//g.width =  MathUtils.random(500, 1000);
			g.height = 100;
			g.name = "Sand Desert";
			g.id = 110;
			g.zoom = Constant.MAPZOOM_BIG;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			
			for(int y = 0;y < 10;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.DESERT;
				}
			}

			int currentX = 5;
			while(currentX < g.width - 5)
			{
				currentX += Framework.createMountain(g, currentX, 10, Tile.DESERT, MathUtils.random(10, 20), 0, 1, -1) + MathUtils.random(2, 5);
			}
			
			for(int y = 1;y < g.height-1;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					if(g.map[y][x] != null && g.map[y+1][x] == null)
					{
						if(MathUtils.randomBoolean(0.1f))
						{
							int r = MathUtils.random((int)1, (int)3);
							if(r == 1)
							{
								g.bg[y+1][x][0] = Background.DEADVEGETATION;
							}
							else if(r == 2)
							{
								g.bg[y+1][x][0] = Background.DEADVEGETATION2;
							}
							else if(r == 3)
							{
								g.bg[y+1][x][0] = Background.PRETTYCACTUS;
							}
						}
					}
				}
			}
		}
		else if(biomeType == 120)
		{
			//g.width =  MathUtils.random(500, 800);
			g.height = 100;
			g.name = "Volcano";
			g.id = 120;
			g.zoom = Constant.MAPZOOM_BIG;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			for(int y = 0;y < 20;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = MathUtils.randomBoolean() ? Tile.VOLCANIC : Tile.VOLCANIC2;
				}
			}
			
			int actualY = 0;
			for(int x = (int)(g.width * 0.1f);x <= g.width - 5;x++)
			{
				for(int y = 10;y <= 10 + actualY;y++)
				{
					g.map[y][x] = MathUtils.randomBoolean() ? Tile.VOLCANIC : Tile.VOLCANIC2;
				}
				if(x < g.width * 0.2f && actualY < 80)
				{
					actualY += MathUtils.random(1, 3);
				}
				else if(x > g.width * 0.8f)
				{
					actualY -= MathUtils.random(1, 3);
					if(actualY < 0)
						break;
				}
			}
			
			for(int y = 0;y < g.height-1;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					if(g.map[y][x] != null && g.map[y+1][x] == null)
					{
						if(MathUtils.randomBoolean(0.15f))
						{
							int r = MathUtils.random(0, 10);
							if(r < 5)
								g.bg[y+1][x][0] = Background.FIREPLANT;
							else if(r < 10)
								g.bg[y+1][x][0] = Background.FIREPLANT2;
							else
								g.bg[y][x][0] = Background.CARBONIZEDBONEHEAD;
						}
					}
				}
			}
		}
		else if(biomeType == 130)
		{
			//g.width =  MathUtils.random(400, 800);
			g.height = 100;
			g.name = "Groves";
			g.id = 130;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][1];
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = null;
					if(y < 16)
					{
						g.map[y][x] = Tile.GRASS;
					}
				}
			}

			int currentX = MathUtils.random(30, 60);
			while(currentX < g.width - 50)
			{
				currentX += Framework.createLine(g, currentX, 15, null, MathUtils.random(6, 12)) + MathUtils.random(8, 12);
			}
			
			currentX = MathUtils.random(30, 60);
			while(currentX < g.width - 50)
			{
				currentX += Framework.createMountain(g, currentX, 15, Tile.GRASS, MathUtils.random(10, 20), 0, 1, 9) + MathUtils.random(5, 12);
			}
			
			for(int y = 0;y < g.height-1;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					if(g.map[y][x] != null && g.map[y+1][x] == null)
					{
						if(MathUtils.randomBoolean(0.25f))
						{
							g.bg[y+1][x][0] = Background.HIGHGRASS;
							if(MathUtils.randomBoolean())
								g.bg[y+1][x][0] = MathUtils.randomBoolean() ? Background.BUSH2 : Background.BUSH;
						}
					}
				}
			}
			g.expected[Constant.AIDIRECTION_UP-1] = -1;
		}
		else if(biomeType == 140)
		{
			//g.width =  MathUtils.random(400, 800);
			g.name = "Rock Caves";
			g.height = 100;
			g.id = 140;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][2];
			g.bgColor = new Color(0.05f, 0.05f, 0.05f, 1f);
			g.useShader = "scared";
			for(int y = 0;y < 10;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.STONE;
				}
			}
			for(int y = g.height-1;y >= g.height-4;y--)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.STONE;
				}
			}
			int layers = 8;
			int clayers = layers;
			int originalLayers = layers;
			boolean lastDouble = false;
			int gapX = MathUtils.random(15, g.width-15);
			int gapWidth = MathUtils.random(4, 7);
			int gapIncl = MathUtils.random(-1, 1);
			for(int y = g.height-1;y >= 10;y--)
			{
				if(layers > 0)
				{
					layers--;
					clayers -= MathUtils.random(0, 1);
					if(!lastDouble && MathUtils.randomBoolean())
					{
						clayers--;
						lastDouble = true;
					}
					else
					{
						lastDouble = false;
					}
					if(layers <= 0)
					{
						layers = 8;
						originalLayers = layers;
						gapX = MathUtils.random(15, g.width-15);
						gapWidth = MathUtils.random(4, 7);
						gapIncl = MathUtils.randomBoolean() ? 1 : -1;
						y -= 4;
						y--;
					}
				}
				if(y >= 10 && y <= g.height - 4)
				{
					for(int x = 0;x < g.width;x++)
					{
						g.map[y][x] = Tile.STONE;
					}
					if(gapIncl == -1)
					{
						int actualStep = originalLayers - clayers;
						for(int x = Math.max(0, gapX-actualStep);x > Main.clamp(0, (gapX - actualStep) - gapWidth, g.width);x--)
						{
							g.map[y][x] = null;
						}
					}
					else if(gapIncl == 1)
					{
						int actualStep = originalLayers - clayers;
						for(int x = Math.max(0, gapX+actualStep);x < Main.clamp(0, (gapX + actualStep) + gapWidth, g.width);x++)
						{
							g.map[y][x] = null;
						}
					}
				}

				for(int x = 0;x < g.width;x++)
				{
					int size = MathUtils.random(5, 10);
					int median = (int) MathUtils.random(size * 0.3f, size * 0.7f);
					System.out.println("X: " + x + " | Size: " + size + " | GapX: " + gapX + " | Gap width: " + gapWidth);
					if(gapIncl == -1)
					{
						int actualStep = originalLayers - clayers;
						if(x >= Math.max(0, gapX-actualStep) && x <= Main.clamp(0, (gapX - actualStep) - gapWidth, g.width))
						{
							System.out.println("Mountain will cover the gap, jumping it");
							continue;
						}
					}
					else
					{
						int actualStep = originalLayers - clayers;
						if(x >= Math.max(0, gapX+actualStep) && x <= Main.clamp(0, (gapX + actualStep) + gapWidth, g.width))
						{
							System.out.println("Mountain will cover the gap, jumping it");
							continue;
						}
					}
					int actualY = 0;
					if(layers == originalLayers)
					{
						for(int xs = Main.clamp(0, x, g.width);(xs <= x + size || actualY > 0) && xs < g.width;xs++)
						{
							for(int ys = Main.clamp(0, y, g.height-1);ys <= Main.clamp(0, y + Math.min(actualY, 1), g.height-1);ys++)
							{
								if(g.map[ys][xs] != null)
									g.map[ys+1][xs] = Tile.METALFLOOR;
							}
							if(xs < x + median * 0.3f)
							{
								actualY += MathUtils.random(0, 1);
							}
							else if(xs > x + median * 0.7f)
							{
								actualY -= MathUtils.random(0, 1);
							}
							if(actualY < 0)
							{
								actualY = 0;
							}
						}
						x += size + MathUtils.random(0, 6);
					}
					else if(layers == 2)
					{
						for(int xs = Main.clamp(0, x, g.width);(xs <= x + size || actualY > 0) && xs < g.width;xs++)
						{
							for(int ys = Main.clamp(0, y-1, g.height-1);ys <= Main.clamp(0, y + Math.min(actualY, 1), g.height-1);ys++)
							{
								g.map[ys][xs] = Tile.METALFLOOR;
							}
							if(xs < x + median * 0.3f)
							{
								actualY += MathUtils.random(0, 1);
							}
							else if(xs > x + median * 0.7f)
							{
								actualY -= MathUtils.random(0, 1);
							}
							if(actualY < 0)
							{
								actualY = 0;
							}
						}
						x += size + MathUtils.random(0, 6);
					}
				}
			}
			
			for(int y = 4;y < g.height - 4;y++)
			{
				for(int x = 0; x < g.width;x++)
				{
					if(g.map[y+1][x] == Tile.METALFLOOR)
					{
						g.map[y][x] = Tile.METALFLOOR;
					}
				}
			}
			for(int y = 1;y < g.height;y++)
			{
				for(int x = 0; x < g.width;x++)
				{
					if(g.map[y][x] == Tile.METALFLOOR)
					{
						g.map[y][x] = null;
					}
					//g.bg[y][x][0] = Background.CAVESTONE;
				}
			}
			
			for(int y = g.height-2;y > 1;y--)
			{
				for(int x = 2;x < g.width-2;x++)
				{
					if(g.map[y+1][x] == Tile.STONE && g.map[y][x-1] != Tile.STONE && g.map[y][x+1] != Tile.STONE && g.map[y+1][x-1] != Tile.STONE && g.map[y+1][x+1] != Tile.STONE)
					{
						g.map[y][x] = null;
						g.bg[y][x][0] = MathUtils.randomBoolean() ? Background.STALAGMITES1 : Background.STALAGMITES2;
					}
					if(g.map[y][x] == Tile.STONE && g.map[y][x-1] != Tile.STONE && g.map[y][x+1] != Tile.STONE && g.map[y-1][x-1] == Tile.STONE && g.map[y-1][x+1] == Tile.STONE && g.map[y-1][x] == Tile.STONE)
					{
						g.map[y][x] = null;
						g.bg[y][x][0] = MathUtils.randomBoolean() ? Background.STALAGMITES3 : Background.STALAGMITES4;
					}
				}
			}
			
			for(int y = g.height-2;y > 1;y--)
			{
				for(int x = 2;x < g.width-2;x++)
				{
					boolean doit = MathUtils.randomBoolean(1/3f);
					if(doit && g.map[y][x] == null && g.map[y-1][x] == Tile.STONE)
					{
						g.bg[y][x][1] = Background.getTileTypeById(MathUtils.random(112, 114));
					}
				}
			}
		}
		else if(biomeType == 141)
		{
			g.name = "Dungeon";
			g.height = 36;
			g.width = 440;
			g.id = 141;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][2];
			g.bgColor = new Color(0.05f, 0.05f, 0.05f, 1f);
			g.useShader = "scared";
			
			int door2flag = (int) ((specialFlag[1] * 1.1523f) % (60 * g.height));
			int door3flag = (int) ((specialFlag[1] * 1.6235f) % (60 * g.height));
			specialFlag[0] = specialFlag[0] % (60 * g.height);
			specialFlag[1] = specialFlag[1] % (60 * g.height);
			System.out.println("First door flag: " + specialFlag[0]);
			
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.STONE;
				}
			}
			for(int y = 10;y < g.height-10;y++)
			{
				for(int x = 10;x < 70;x++)
				{
					g.map[y][x] = null;
				}
				for(int x = 130;x < 190;x++)
				{
					g.map[y][x] = null;
				}
				for(int x = 250;x < 310;x++)
				{
					g.map[y][x] = null;
				}
				for(int x = 370;x < 430;x++)
				{
					g.map[y][x] = null;
				}
			}
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.bg[y][x][0] = Background.CAVE;
				}
			}
			
			// -=========================FIRST DOOR===========================-
			int dx = 11;
			int dy = 11;
			Background door = Background.DUNGEONDOORGRAY;
			g.bg[dy][dx][1] = door;
			
			g.map[dy-1][dx-1] = Tile.STONE;
			g.map[dy-1][dx] = Tile.STONE;
			g.map[dy-1][dx+1] = Tile.STONE;
			g.map[dy-1][dx+2] = Tile.STONE;
			
			Prop p = new Prop(new Vector2(dx, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					comeGX, comeGY, comeLX, comeLY, specialFlag[1], 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			NPC n = NPC.CreateBase(new Vector2(40 * 64 - 40, 64 * 11), 28);
			n.useDialog = 286;
			g.npcs.add(n);
			int oldDx = dx;
			int oldDy = dy;
			// -=========================SECOND DOOR===========================-
			dx = Main.clamp(1, specialFlag[1] % 60, 26)+10;
			dy = Main.clamp(11, (int)Math.floor(specialFlag[1] / 60), g.height-14);
			if(dx == oldDx && dy == oldDy)
				dy += dy > 12 ? -1 : 1;
			System.out.println("Second door flag: " + specialFlag[1]);
			door = Background.getTileTypeById(77 + specialFlag[1] % 6);
			g.bg[dy][dx][1] = door;
			
			g.map[dy-1][dx-1] = Tile.STONE;
			g.map[dy-1][dx] = Tile.STONE;
			g.map[dy-1][dx+1] = Tile.STONE;
			g.map[dy-1][dx+2] = Tile.STONE;
			
			p = new Prop(new Vector2(dx, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx+120)*64, dy*64, specialFlag[1], 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			g.bg[dy][dx+120][1] = door;
			
			g.map[dy-1][dx-1+120] = Tile.STONE;
			g.map[dy-1][dx+120] = Tile.STONE;
			g.map[dy-1][dx+1+120] = Tile.STONE;
			g.map[dy-1][dx+2+120] = Tile.STONE;
			
			p = new Prop(new Vector2(dx+120, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx)*64, dy*64, specialFlag[1], 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			n = NPC.CreateBase(new Vector2(160 * 64 - 40, 64*11), 28);
			n.useDialog = 286;
			g.npcs.add(n);
			oldDx = dx;
			oldDy = dy;
			// -=========================THIRD DOOR===========================-
			dx = Main.clamp(1, door2flag % 60, 26) + 130;
			dy = Main.clamp(11, (int)Math.floor(door2flag / 60), g.height-14);
			if(dx == oldDx && dy == oldDy)
				dy += dy > 12 ? -1 : 1;
			System.out.println("Third door flag: " + door2flag);
			door = Background.getTileTypeById(77 + door2flag % 6);
			g.bg[dy][dx][1] = door;
			
			g.map[dy-1][dx-1] = Tile.STONE;
			g.map[dy-1][dx] = Tile.STONE;
			g.map[dy-1][dx+1] = Tile.STONE;
			g.map[dy-1][dx+2] = Tile.STONE;
			
			p = new Prop(new Vector2(dx, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx+120)*64, dy*64, door2flag, 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			g.bg[dy][dx+120][1] = door;
			
			g.map[dy-1][dx-1+120] = Tile.STONE;
			g.map[dy-1][dx+120] = Tile.STONE;
			g.map[dy-1][dx+1+120] = Tile.STONE;
			g.map[dy-1][dx+2+120] = Tile.STONE;
			
			p = new Prop(new Vector2(dx+120, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx)*64, dy*64, door2flag, 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			n = NPC.CreateBase(new Vector2(280 * 64 - 40, 64*11), 28);
			n.useDialog = 286;
			g.npcs.add(n);
			oldDx = dx;
			oldDy = dy;
			// -=========================FOURTH DOOR===========================-
			dx = Main.clamp(1, door3flag % 60, 26) + 250;
			dy = Main.clamp(11, (int)Math.floor(door3flag / 60), g.height-14);
			if(dx == oldDx && dy == oldDy)
				dy += dy > 12 ? -1 : 1;
			System.out.println("Fourth door flag: " + door3flag);
			door = Background.getTileTypeById(77 + door3flag % 6);
			g.bg[dy][dx][1] = door;
			
			g.map[dy-1][dx-1] = Tile.STONE;
			g.map[dy-1][dx] = Tile.STONE;
			g.map[dy-1][dx+1] = Tile.STONE;
			g.map[dy-1][dx+2] = Tile.STONE;
			
			p = new Prop(new Vector2(dx, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx+120)*64, dy*64, door3flag, 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			g.bg[dy][dx+120][1] = door;
			
			g.map[dy-1][dx-1+120] = Tile.STONE;
			g.map[dy-1][dx+120] = Tile.STONE;
			g.map[dy-1][dx+1+120] = Tile.STONE;
			g.map[dy-1][dx+2+120] = Tile.STONE;
			
			p = new Prop(new Vector2(dx+120, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx)*64, dy*64, door3flag, 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			n = NPC.CreateBase(new Vector2(400 * 64 - 40, 64*11), 28);
			n.useDialog = 286;
			g.npcs.add(n);

		}
		else if(biomeType == 150)
		{
			//g.width =  MathUtils.random(400, 800);
			g.name = "Underground Passages";
			g.height = 100;
			g.id = 150;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][1];
			g.bgColor = new Color(0.05f, 0.05f, 0.05f, 1f);
			//g.useShader = "scared";
			for(int y = 0;y < 10;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.GRASS;
				}
			}
			for(int y = g.height-1;y >= g.height-4;y--)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.GRASS;
				}
			}
			int layers = 8;
			int clayers = layers;
			int originalLayers = layers;
			boolean lastDouble = false;
			int gapX = MathUtils.random(15, g.width-15);
			int gapWidth = MathUtils.random(4, 7);
			int gapIncl = MathUtils.random(-1, 1);
			int actualLayer = 0;
			for(int y = g.height-1;y >= 10;y--)
			{
				if(layers > 0)
				{
					layers--;
					clayers -= MathUtils.random(0, 1);
					if(!lastDouble && MathUtils.randomBoolean())
					{
						clayers--;
						lastDouble = true;
					}
					else
					{
						lastDouble = false;
					}
					if(layers <= 0)
					{
						layers = 8;
						originalLayers = layers;
						gapX = MathUtils.random(15, g.width-15);
						gapWidth = MathUtils.random(4, 7);
						gapIncl = MathUtils.randomBoolean() ? 1 : -1;
						actualLayer++;
						y -= 4;
						y--;
					}
				}
				if(y >= 10 && y <= g.height - 4)
				{
					for(int x = 0;x < g.width;x++)
					{
						g.map[y][x] = Tile.GRASS;
					}
					if(gapIncl == -1)
					{
						int actualStep = originalLayers - clayers;
						for(int x = Math.max(0, gapX-actualStep);x > Main.clamp(0, (gapX - actualStep) - gapWidth, g.width);x--)
						{
							g.map[y][x] = null;
						}
					}
					else if(gapIncl == 1)
					{
						int actualStep = originalLayers - clayers;
						for(int x = Math.max(0, gapX+actualStep);x < Main.clamp(0, (gapX + actualStep) + gapWidth, g.width);x++)
						{
							g.map[y][x] = null;
						}
					}
				}

				for(int x = 0;x < g.width;x++)
				{
					int size = MathUtils.random(5, 10);
					int median = (int) MathUtils.random(size * 0.3f, size * 0.7f);
					System.out.println("X: " + x + " | Size: " + size + " | GapX: " + gapX + " | Gap width: " + gapWidth);
					if(gapIncl == -1)
					{
						int actualStep = originalLayers - clayers;
						if(x >= Math.max(0, gapX-actualStep) && x <= Main.clamp(0, (gapX - actualStep) - gapWidth, g.width))
						{
							System.out.println("Mountain will cover the gap, jumping it");
							continue;
						}
					}
					else
					{
						int actualStep = originalLayers - clayers;
						if(x >= Math.max(0, gapX+actualStep) && x <= Main.clamp(0, (gapX + actualStep) + gapWidth, g.width))
						{
							System.out.println("Mountain will cover the gap, jumping it");
							continue;
						}
					}
					int actualY = 0;
					if(layers == originalLayers)
					{
						for(int xs = Main.clamp(0, x, g.width);(xs <= x + size || actualY > 0) && xs < g.width;xs++)
						{
							for(int ys = Main.clamp(0, y, g.height-1);ys <= Main.clamp(0, y + Math.min(actualY, 1), g.height-1);ys++)
							{
								if(g.map[ys][xs] != null)
									g.map[ys+1][xs] = Tile.METALFLOOR;
							}
							if(xs < x + median * 0.3f)
							{
								actualY += MathUtils.random(0, 1);
							}
							else if(xs > x + median * 0.7f)
							{
								actualY -= MathUtils.random(0, 1);
							}
							if(actualY < 0)
							{
								actualY = 0;
							}
						}
						x += size + MathUtils.random(0, 6);
					}
					else if(layers == 2)
					{
						for(int xs = Main.clamp(0, x, g.width);(xs <= x + size || actualY > 0) && xs < g.width;xs++)
						{
							for(int ys = Main.clamp(0, y-1, g.height-1);ys <= Main.clamp(0, y + Math.min(actualY, 1), g.height-1);ys++)
							{
								g.map[ys][xs] = Tile.METALFLOOR;
							}
							if(xs < x + median * 0.3f)
							{
								actualY += MathUtils.random(0, 1);
							}
							else if(xs > x + median * 0.7f)
							{
								actualY -= MathUtils.random(0, 1);
							}
							if(actualY < 0)
							{
								actualY = 0;
							}
						}
						x += size + MathUtils.random(0, 6);
					}
				}
			}
			
			for(int y = 4;y < g.height - 4;y++)
			{
				for(int x = 0; x < g.width;x++)
				{
					if(g.map[y+1][x] == Tile.METALFLOOR)
					{
						g.map[y][x] = Tile.METALFLOOR;
					}
				}
			}
			for(int y = 1;y < g.height;y++)
			{
				for(int x = 0; x < g.width;x++)
				{
					if(g.map[y][x] == Tile.METALFLOOR)
					{
						g.map[y][x] = null;
					}
					//g.bg[y][x][0] = Background.EARTH;
				}
			}
			

			for(int y = 1;y < g.height-1;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					int chance = MathUtils.random(5);
					if(g.map[y-1][x] == Tile.GRASS && chance == 0)
					{
						g.bg[y][x][0] = Background.getTileTypeById(MathUtils.random(17, 21));
					}
					else if(g.map[y-1][x] == Tile.GRASS && chance != 0)
					{
						g.bg[y][x][0] = Background.HIGHGRASS;
					}
				}
			}
		}
		else if(biomeType == 160)
		{
			//g.width =  MathUtils.random(400, 800);
			g.height = 80;
			g.name = "Dark Fields";
			g.id = 160;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][3];
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = null;
					if(y < 16)
					{
						g.map[y][x] = Tile.DEADGRASS;
					}
				}
			}
			Respawn r;
			for(int y = 16;y < g.height-1;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					int chance = MathUtils.random(6);
					if(g.map[y-1][x] == Tile.DEADGRASS && chance == 0)
					{
						g.bg[y][x][0] = Background.getTileTypeById(MathUtils.random(87, 91));
					}
					else if(g.map[y-1][x] == Tile.DEADGRASS && chance < 4)
					{
						g.bg[y][x][0] = Background.DEADHIGHGRASS;
					}
					/*else if(g.map[y-1][x] != null && chance == 5 && MathUtils.random(4) == 0)
					{
						r = new Respawn(46, 300, new Vector2(x, y).scl(64f));
						g.respawns.add(r);
					}*/
				}
			}
			System.out.println("Starting Plains spawn generation.");
			for(int x = 50;x < g.width - 50;x++)
			{
				System.out.println(x + "/" + g.width + " - Trying to generate a surrounded spawn");
				if(MathUtils.random(3) == 0)
				{
					System.out.println("Surrounded spawn succeeded, randomizing the surround type.");
					if(MathUtils.random(3) != 0)
					{
						System.out.println("Creating a cornfield.");
						int size = MathUtils.random(15, 25);
						Framework.createLine(g, x, 16, 2, Background.DEADCORN, size);
						Framework.createLine(g, x, 16, 1, Background.DEADCORN2, size);
						Framework.createLine(g, x, 16, 0, Background.DEADCORN3, size);
						r = new Respawn(50, 300, new Vector2(x + size/2, 16).scl(64f));
						g.respawns.add(r);
						x += size + MathUtils.random(20, 40);
					}
					else
					{
						System.out.println("Creating a bull breeding.");
						int size = MathUtils.random(25, 35);
						Framework.createLine(g, x, 16, 0, Background.CATTLEFENCE, size);
						float sizeVar = size * 32f;
						for(int i = 0;i < 1;i++)
						{
							r = new Respawn(51, 300, new Vector2(x + size/2, 16).scl(64f).add(MathUtils.random(-sizeVar, sizeVar), 0));
							g.respawns.add(r);
						}
						for(int i = 0;i < MathUtils.random(1, 2);i++)
						{
							if(MathUtils.random(4) == 0)
							{
								r = new Respawn(51, 300, new Vector2(x + size/2, 16).scl(64f).add(MathUtils.random(-sizeVar, sizeVar), 0));
								g.respawns.add(r);
							}
						}
						x += size + MathUtils.random(20, 40);
					}
				}

				x += MathUtils.random(10, 15);
			}
			
			g.expected[Constant.AIDIRECTION_UP-1] = -1;
		}
		else if(biomeType == 170)
		{
			//g.width =  MathUtils.random(400, 800);
			g.height = 100;
			g.name = "Ghost Groves";
			g.id = 170;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][1];
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = null;
					if(y < 16)
					{
						g.map[y][x] = Tile.DEADGRASS;
					}
				}
			}
			
			int currentX = MathUtils.random(30, 60);
			while(currentX < g.width - 50)
			{
				currentX += Framework.createLine(g, currentX, 15, null, MathUtils.random(6, 12)) + MathUtils.random(8, 12);
			}
			
			currentX = MathUtils.random(30, 60);
			while(currentX < g.width - 50)
			{
				currentX += Framework.createMountain(g, currentX, 15, Tile.DEADGRASS, MathUtils.random(10, 20), 0, 1, 9) + MathUtils.random(5, 12);
			}
			
			Respawn r;
			for(int y = 16;y < g.height-1;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					int chance = MathUtils.random(6);
					if(g.map[y-1][x] == Tile.DEADGRASS && chance == 0)
					{
						g.bg[y][x][0] = Background.getTileTypeById(MathUtils.random(87, 91));
					}
					else if(g.map[y-1][x] == Tile.DEADGRASS && chance < 4)
					{
						g.bg[y][x][0] = Background.DEADHIGHGRASS;
					}
				}
			}
			for(int x = 4;x < g.width - 4;x += 6)
			{
				int y = 16;
				r = new Respawn(61, 300, new Vector2(x, y).scl(64f));
				g.respawns.add(r);
			}
			g.expected[Constant.AIDIRECTION_UP-1] = -1;
		}
		else if(biomeType == 180)
		{
			//g.width =  MathUtils.random(400, 800);
			g.name = "Spirit Lair";
			g.height = 100;
			g.id = 180;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.maximumLight = 0.6f;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][2];
			g.bgColor = new Color(0.05f, 0.05f, 0.05f, 1f);
			g.useShader = "scared";
			for(int y = 0;y < 10;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.STONE;
				}
			}
			for(int y = g.height-1;y >= g.height-4;y--)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.STONE;
				}
			}
			int layers = 8;
			int clayers = layers;
			int originalLayers = layers;
			boolean lastDouble = false;
			int gapX = MathUtils.random(15, g.width-15);
			int gapWidth = MathUtils.random(4, 7);
			int gapIncl = MathUtils.random(-1, 1);
			for(int y = g.height-1;y >= 10;y--)
			{
				if(layers > 0)
				{
					layers--;
					clayers -= MathUtils.random(0, 1);
					if(!lastDouble && MathUtils.randomBoolean())
					{
						clayers--;
						lastDouble = true;
					}
					else
					{
						lastDouble = false;
					}
					if(layers <= 0)
					{
						layers = 8;
						originalLayers = layers;
						gapX = MathUtils.random(15, g.width-15);
						gapWidth = MathUtils.random(4, 7);
						gapIncl = MathUtils.randomBoolean() ? 1 : -1;
						y -= 4;
						y--;
					}
				}
				if(y >= 10 && y <= g.height - 4)
				{
					for(int x = 0;x < g.width;x++)
					{
						g.map[y][x] = Tile.STONE;
					}
					if(gapIncl == -1)
					{
						int actualStep = originalLayers - clayers;
						for(int x = Math.max(0, gapX-actualStep);x > Main.clamp(0, (gapX - actualStep) - gapWidth, g.width);x--)
						{
							g.map[y][x] = null;
						}
					}
					else if(gapIncl == 1)
					{
						int actualStep = originalLayers - clayers;
						for(int x = Math.max(0, gapX+actualStep);x < Main.clamp(0, (gapX + actualStep) + gapWidth, g.width);x++)
						{
							g.map[y][x] = null;
						}
					}
				}

				for(int x = 0;x < g.width;x++)
				{
					int size = MathUtils.random(5, 10);
					int median = (int) MathUtils.random(size * 0.3f, size * 0.7f);
					System.out.println("X: " + x + " | Size: " + size + " | GapX: " + gapX + " | Gap width: " + gapWidth);
					if(gapIncl == -1)
					{
						int actualStep = originalLayers - clayers;
						if(x >= Math.max(0, gapX-actualStep) && x <= Main.clamp(0, (gapX - actualStep) - gapWidth, g.width))
						{
							System.out.println("Mountain will cover the gap, jumping it");
							continue;
						}
					}
					else
					{
						int actualStep = originalLayers - clayers;
						if(x >= Math.max(0, gapX+actualStep) && x <= Main.clamp(0, (gapX + actualStep) + gapWidth, g.width))
						{
							System.out.println("Mountain will cover the gap, jumping it");
							continue;
						}
					}
					int actualY = 0;
					if(layers == originalLayers)
					{
						for(int xs = Main.clamp(0, x, g.width);(xs <= x + size || actualY > 0) && xs < g.width;xs++)
						{
							for(int ys = Main.clamp(0, y, g.height-1);ys <= Main.clamp(0, y + Math.min(actualY, 1), g.height-1);ys++)
							{
								if(g.map[ys][xs] != null)
									g.map[ys+1][xs] = Tile.METALFLOOR;
							}
							if(xs < x + median * 0.3f)
							{
								actualY += MathUtils.random(0, 1);
							}
							else if(xs > x + median * 0.7f)
							{
								actualY -= MathUtils.random(0, 1);
							}
							if(actualY < 0)
							{
								actualY = 0;
							}
						}
						x += size + MathUtils.random(0, 6);
					}
					else if(layers == 2)
					{
						for(int xs = Main.clamp(0, x, g.width);(xs <= x + size || actualY > 0) && xs < g.width;xs++)
						{
							for(int ys = Main.clamp(0, y-1, g.height-1);ys <= Main.clamp(0, y + Math.min(actualY, 1), g.height-1);ys++)
							{
								g.map[ys][xs] = Tile.METALFLOOR;
							}
							if(xs < x + median * 0.3f)
							{
								actualY += MathUtils.random(0, 1);
							}
							else if(xs > x + median * 0.7f)
							{
								actualY -= MathUtils.random(0, 1);
							}
							if(actualY < 0)
							{
								actualY = 0;
							}
						}
						x += size + MathUtils.random(0, 6);
					}
				}
			}
			
			for(int y = 4;y < g.height - 4;y++)
			{
				for(int x = 0; x < g.width;x++)
				{
					if(g.map[y+1][x] == Tile.METALFLOOR)
					{
						g.map[y][x] = Tile.METALFLOOR;
					}
				}
			}
			for(int y = 1;y < g.height;y++)
			{
				for(int x = 0; x < g.width;x++)
				{
					if(g.map[y][x] == Tile.METALFLOOR)
					{
						g.map[y][x] = null;
					}
					g.bg[y][x][0] = Background.CAVESTONE;
				}
			}
		}
		else if(biomeType == 181)
		{
			g.name = "Spirit Dungeon";
			g.height = 36;
			g.width = 440;
			g.id = 181;
			g.zoom = Constant.MAPZOOM_OUTSIDE;
			g.map = new Tile[g.height][g.width];
			g.bg = new Background[g.height][g.width][2];
			g.bgColor = new Color(0.05f, 0.05f, 0.05f, 1f);
			g.useShader = "scared";
			
			int door2flag = (int) ((specialFlag[1] * 1.1523f) % (60 * g.height));
			int door3flag = (int) ((specialFlag[1] * 1.6235f) % (60 * g.height));
			specialFlag[0] = specialFlag[0] % (60 * g.height);
			specialFlag[1] = specialFlag[1] % (60 * g.height);
			System.out.println("First door flag: " + specialFlag[0]);
			
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.map[y][x] = Tile.STONE;
				}
			}
			for(int y = 10;y < g.height-10;y++)
			{
				for(int x = 10;x < 70;x++)
				{
					g.map[y][x] = null;
				}
				for(int x = 130;x < 190;x++)
				{
					g.map[y][x] = null;
				}
				for(int x = 250;x < 310;x++)
				{
					g.map[y][x] = null;
				}
				for(int x = 370;x < 430;x++)
				{
					g.map[y][x] = null;
				}
			}
			for(int y = 0;y < g.height;y++)
			{
				for(int x = 0;x < g.width;x++)
				{
					g.bg[y][x][0] = Background.CAVE;
				}
			}
			
			// -=========================FIRST DOOR===========================-
			int dx = 11;
			int dy = 11;
			Background door = Background.DUNGEONDOORGRAY;
			g.bg[dy][dx][1] = door;
			
			g.map[dy-1][dx-1] = Tile.STONE;
			g.map[dy-1][dx] = Tile.STONE;
			g.map[dy-1][dx+1] = Tile.STONE;
			g.map[dy-1][dx+2] = Tile.STONE;
			
			Prop p = new Prop(new Vector2(dx, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					comeGX, comeGY, comeLX, comeLY, specialFlag[1], 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			NPC n = NPC.CreateBase(new Vector2(40 * 64 - 40, 64 * 11), 28);
			n.useDialog = 286;
			g.npcs.add(n);
			// -=========================SECOND DOOR===========================-
			dx = Main.clamp(1, specialFlag[1] % 60, 26)+10;
			dy = Main.clamp(11, (int)Math.floor(specialFlag[1] / 60), g.height-14);
			System.out.println("Second door flag: " + specialFlag[1]);
			door = Background.getTileTypeById(77 + specialFlag[1] % 6);
			g.bg[dy][dx][1] = door;
			
			g.map[dy-1][dx-1] = Tile.STONE;
			g.map[dy-1][dx] = Tile.STONE;
			g.map[dy-1][dx+1] = Tile.STONE;
			g.map[dy-1][dx+2] = Tile.STONE;
			
			p = new Prop(new Vector2(dx, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx+120)*64, dy*64, specialFlag[1], 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			g.bg[dy][dx+120][1] = door;
			
			g.map[dy-1][dx-1+120] = Tile.STONE;
			g.map[dy-1][dx+120] = Tile.STONE;
			g.map[dy-1][dx+1+120] = Tile.STONE;
			g.map[dy-1][dx+2+120] = Tile.STONE;
			
			p = new Prop(new Vector2(dx+120, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx)*64, dy*64, specialFlag[1], 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			n = NPC.CreateBase(new Vector2(160 * 64 - 40, 64*11), 28);
			n.useDialog = 286;
			g.npcs.add(n);
			// -=========================THIRD DOOR===========================-
			dx = Main.clamp(1, door2flag % 60, 26) + 130;
			dy = Main.clamp(11, (int)Math.floor(door2flag / 60), g.height-14);
			System.out.println("Third door flag: " + door2flag);
			door = Background.getTileTypeById(77 + door2flag % 6);
			g.bg[dy][dx][1] = door;
			
			g.map[dy-1][dx-1] = Tile.STONE;
			g.map[dy-1][dx] = Tile.STONE;
			g.map[dy-1][dx+1] = Tile.STONE;
			g.map[dy-1][dx+2] = Tile.STONE;
			
			p = new Prop(new Vector2(dx, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx+120)*64, dy*64, door2flag, 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			g.bg[dy][dx+120][1] = door;
			
			g.map[dy-1][dx-1+120] = Tile.STONE;
			g.map[dy-1][dx+120] = Tile.STONE;
			g.map[dy-1][dx+1+120] = Tile.STONE;
			g.map[dy-1][dx+2+120] = Tile.STONE;
			
			p = new Prop(new Vector2(dx+120, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx)*64, dy*64, door2flag, 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			n = NPC.CreateBase(new Vector2(280 * 64 - 40, 64*11), 28);
			n.useDialog = 286;
			g.npcs.add(n);

			// -=========================FOURTH DOOR===========================-
			dx = Main.clamp(1, door3flag % 60, 26) + 250;
			dy = Main.clamp(11, (int)Math.floor(door3flag / 60), g.height-14);
			System.out.println("Fourth door flag: " + door3flag);
			door = Background.getTileTypeById(77 + door3flag % 6);
			g.bg[dy][dx][1] = door;
			
			g.map[dy-1][dx-1] = Tile.STONE;
			g.map[dy-1][dx] = Tile.STONE;
			g.map[dy-1][dx+1] = Tile.STONE;
			g.map[dy-1][dx+2] = Tile.STONE;
			
			p = new Prop(new Vector2(dx, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx+120)*64, dy*64, door3flag, 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			g.bg[dy][dx+120][1] = door;
			
			g.map[dy-1][dx-1+120] = Tile.STONE;
			g.map[dy-1][dx+120] = Tile.STONE;
			g.map[dy-1][dx+1+120] = Tile.STONE;
			g.map[dy-1][dx+2+120] = Tile.STONE;
			
			p = new Prop(new Vector2(dx+120, dy).scl(64f), 156, 196, Constant.PROPID_TELEPORT, new int[] {
					g.globalX, g.globalY, (dx)*64, dy*64, door3flag, 0, g.globalX, g.globalY, 0}, true);
			g.props.add(p);
			
			n = NPC.CreateBase(new Vector2(400 * 64 - 40, 64*11), 28);
			n.useDialog = 286;
			g.npcs.add(n);

		}
		
		if(myX == -20)
		{
			g.expected[Constant.AIDIRECTION_LEFT-1] = -1;
			NPC n = NPC.CreateBase(new Vector2(300f, g.height * 64 + 100), 31);
			g.npcs.add(n);
		}
		else if(myX == 20)
		{
			g.expected[Constant.AIDIRECTION_RIGHT-1] = -1;
			NPC n = NPC.CreateBase(new Vector2(g.width * 64 - 300f, g.height * 64 + 100), 31);
			g.npcs.add(n);
		}
		g.postGen();
		oldWidth = g.width;
		oldHeight = g.height;
		//Generation.lastId[saveId]++;
		Main.cameMap = g;
		return g;
	}
	
	public void update(float delta)
	{
		Player player = Main.player[Main.me];
		if(this.id == 1000)
		{
			if(player.Center().x > this.width*32 + 200)
			{
				this.map[14][20] = Tile.METALFLOOR;
				this.map[13][20] = Tile.METALFLOOR;
				this.map[12][20] = Tile.METALFLOOR;
			}
		}
		
		if(this.globalX < 4 || this.globalX == 8)
		{
			try
			{
			Main.player[Main.me].skills[1].cdf = 1f;
			}
			catch(Exception ex) {}
		}
	}
	
	public void setTeleports(int iml, int imr, int imu, int imd)
	{
		if(imd != 0)
			this.expected[Constant.AIDIRECTION_DOWN-1] = imd;
		
		if(imu != 0)
			this.expected[Constant.AIDIRECTION_UP-1] = imu;
		
		if(iml != 0)
			this.expected[Constant.AIDIRECTION_LEFT-1] = iml;
		
		if(imr != 0)
			this.expected[Constant.AIDIRECTION_RIGHT-1] = imr;
	}

	public boolean doesRectCollideWithMap(float x, float y, int width, int height)
	{
		if (x < 0 || y < 0 || x + width > this.width * Tile.TILE_SIZE || y + height > this.height * Tile.TILE_SIZE)
			return true;

		for(int lin = (int)(y / Tile.TILE_SIZE);lin < Math.ceil((y + height)/Tile.TILE_SIZE);lin++)
		{
			for(int col = (int)(x / Tile.TILE_SIZE);col < Math.ceil((x + width)/Tile.TILE_SIZE);col++)
			{
				Tile type = getTileTypeByCoordinate(col, lin);
				if(type != null && type.isCollidable())
					return true;
			}
		}

		return false;
	}
	
	public boolean doesPolygonCollideWithMap(Polygon poly)
	{
		float x = 999999;
		float y = 999999;
		float maxX = -999999;
		float maxY = -999999;
		float[] vert = poly.getTransformedVertices();
		for(int i = 0;i < vert.length;i += 2)
		{
			if(vert[i] < x)
				x = vert[i];
			
			if(vert[i] > maxX)
				maxX = vert[i];
		}
		for(int i = 1;i < vert.length;i += 2)
		{
			if(vert[i] < y)
				y = vert[i];
			
			if(vert[i] > maxY)
				maxY = vert[i];
		}
		if (x < 0 || y < 0 || maxX > this.width * Tile.TILE_SIZE || maxY > this.height * Tile.TILE_SIZE)
			return true;

		for(int lin = (int)(y / Tile.TILE_SIZE);lin < Math.ceil((maxY)/Tile.TILE_SIZE);lin++)
		{
			for(int col = (int)(x / Tile.TILE_SIZE);col < Math.ceil((maxX)/Tile.TILE_SIZE);col++)
			{
				Tile type = getTileTypeByCoordinate(col, lin);
				
				if(type != null && type.isCollidable())
				{
					Polygon t = Main.rectToPoly(new Rectangle(col * Tile.TILE_SIZE, lin * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE));
					if(Intersector.overlapConvexPolygons(poly, t))
					{
						return true;
					}
				}
			}
		}

		return false;
	}
		
	public boolean doesRectCollideWithMap2(float x, float y, int width, int height)
	{
		if(this.globalY >= 250)
			return this.doesRectCollideWithMap(x, y, width, height);
		
		int minX = 0;
		if(this.expected[Constant.AIDIRECTION_LEFT-1] != -1)
			minX = -width*2;
		int minY = 0;
		if(this.expected[Constant.AIDIRECTION_DOWN-1] != -1)
			minY = -height*2;
		
		int maxX = 0;
		if(this.expected[Constant.AIDIRECTION_RIGHT-1] != -1)
			maxX = width*2;
		
		int maxY = 0;
		if(this.expected[Constant.AIDIRECTION_UP-1] != -1)
			maxY = height*2;
		
		if (x < minX || y < minY || x + width > this.width * Tile.TILE_SIZE + maxX || y + height > this.height * Tile.TILE_SIZE + maxY)
			return false;

		for(int lin = (int)(y / Tile.TILE_SIZE);lin < Math.ceil((y + height)/Tile.TILE_SIZE);lin++)
		{
			for(int col = (int)(x / Tile.TILE_SIZE);col < Math.ceil((x + width)/Tile.TILE_SIZE);col++)
			{
				Tile type = getTileTypeByCoordinate(col, lin);
				if(type != null && type.isCollidable())
					return true;
			}
		}
		
		for(Prop p : Main.prop) {
			if(p.active && p.id == 6 && new Rectangle(x, y, width, height).overlaps(p.hitBox()))
			{
				return true;
			}
		}

		return false;
	}
	
	public boolean doesRectCollideWithForeground(float x, float y, int width, int height)
	{
		int minX = 0;
		if(this.expected[Constant.AIDIRECTION_LEFT-1] != -1)
			minX = -width*2;
		int minY = 0;
		if(this.expected[Constant.AIDIRECTION_DOWN-1] != -1)
			minY = -height*2;
		
		int maxX = 0;
		if(this.expected[Constant.AIDIRECTION_RIGHT-1] != -1)
			maxX = width*2;
		
		int maxY = 0;
		if(this.expected[Constant.AIDIRECTION_UP-1] != -1)
			maxY = height*2;
		
		if (x < minX || y < minY || x + width > this.width * Tile.TILE_SIZE + maxX || y + height > this.height * Tile.TILE_SIZE + maxY)
			return true;

		for(int lin = (int)(y / Tile.TILE_SIZE);lin < Math.ceil((y + height)/Tile.TILE_SIZE);lin++)
		{
			for(int col = (int)(x / Tile.TILE_SIZE);col < Math.ceil((x + width)/Tile.TILE_SIZE);col++)
			{
				Tile type = getTileTypeByCoordinateFg(col, lin);
				if(type != null)
					return true;
			}
		}

		return false;
	}

	public Vector2 getNextestFreeSpace(float xP, float yP, int width, int height, int directionX, int directionY)
	{
		boolean finished = false;
		int cDist = 0;

		float realXP = xP;
		float realYP = yP;

		int xToPos, yToPos;
		if(directionX == -1)
			xToPos = (int)Math.floor((double)realXP/64f);
		else
			xToPos = (int)Math.ceil((double)realXP/64f);

		yToPos = (int)Math.floor((double)realYP/64f);

		Vector2 result = new Vector2(xToPos, yToPos);
		while(!finished)
		{
			if(finished)
				break;

			if(directionX == 1)
			{
				for(int x = -cDist;x <= cDist;x++)
				{
					if(finished)
						break;

					if(directionY == 1)
					{
						for(int y = -cDist;y <= cDist;y++)
						{
							if(finished)
								break;

							if(xToPos+x >= 0 && xToPos+x < this.width && yToPos+y >= 0 && yToPos+y < this.height && 
									(!this.doesRectCollideWithMap2((xToPos+x)*64, (yToPos+y)*64, width, height)))
							{
								/*if(yToPos+y-1 < 0 || this.map[yToPos+y-1][xToPos+x] == null || !this.map[yToPos+y-1][xToPos+x].isCollidable())
									continue;*/
								
								result = new Vector2(xToPos+x, yToPos+y);
								finished = true;
								break;
							}
						}
					}
					else
					{
						for(int y = cDist;y >= -cDist;y--)
						{
							if(finished)
								break;

							if(xToPos+x >= 0 && xToPos+x < this.width && yToPos+y >= 0 && yToPos+y < this.height && 
									(!this.doesRectCollideWithMap2((xToPos+x)*64, (yToPos+y)*64, width, height)))
							{
								/*if(yToPos+y-1 < 0 || this.map[yToPos+y-1][xToPos+x] == null || !this.map[yToPos+y-1][xToPos+x].isCollidable())
									continue;*/
								
								result = new Vector2(xToPos+x, yToPos+y);
								finished = true;
								break;
							}
						}
					}
				}
			}
			else
			{
				for(int x = cDist;x >= -cDist;x--)
				{
					if(finished)
						break;

					if(directionY == 1)
					{
						for(int y = -cDist;y <= cDist;y++)
						{
							if(finished)
								break;

							if(xToPos+x >= 0 && xToPos+x < this.width && yToPos+y >= 0 && yToPos+y < this.height && 
									(!this.doesRectCollideWithMap2((xToPos+x)*64, (yToPos+y)*64, width, height)))
							{
								/*if(yToPos+y-1 < 0 || this.map[yToPos+y-1][xToPos+x] == null || !this.map[yToPos+y-1][xToPos+x].isCollidable())
									continue;*/
								
								result = new Vector2(xToPos+x, yToPos+y);
								finished = true;
								break;
							}
						}
					}
					else
					{
						for(int y = cDist;y >= -cDist;y--)
						{
							if(finished)
								break;

							if(xToPos+x >= 0 && xToPos+x < this.width && yToPos+y >= 0 && yToPos+y < this.height && 
									(!this.doesRectCollideWithMap2((xToPos+x)*64, (yToPos+y)*64, width, height)))
							{
								/*if(yToPos+y-1 < 0 || this.map[yToPos+y-1][xToPos+x] == null || !this.map[yToPos+y-1][xToPos+x].isCollidable())
									continue;*/
								
								result = new Vector2(xToPos+x, yToPos+y);
								finished = true;
								break;
							}
						}
					}

				}
			}
			cDist++;
		}
		return result;
	}
	
	public Vector2 getNextestFreeSpaceSafe(float xP, float yP, int width, int height, int directionX, int directionY)
	{
		boolean finished = false;
		int cDist = 0;

		float realXP = xP;
		float realYP = yP;

		int xToPos, yToPos;
		if(directionX == -1)
			xToPos = (int)Math.floor((double)realXP/64f);
		else
			xToPos = (int)Math.ceil((double)realXP/64f);

		yToPos = (int)Math.floor((double)realYP/64f);

		Vector2 result = new Vector2(xToPos, yToPos);
		while(!finished)
		{
			if(finished)
				break;

			if(directionX == 1)
			{
				for(int x = -cDist;x <= cDist;x++)
				{
					if(finished)
						break;

					if(directionY == 1)
					{
						for(int y = -cDist;y <= cDist;y++)
						{
							if(finished)
								break;

							if(xToPos+x >= 0 && xToPos+x < this.width && yToPos+y >= 0 && yToPos+y < this.height && 
									(!this.doesRectCollideWithMap((xToPos+x)*64, (yToPos+y)*64, width, height)))
							{
								if(yToPos+y-1 < 0 || this.map[yToPos+y-1][xToPos+x] == null || !this.map[yToPos+y-1][xToPos+x].isCollidable())
									continue;
								
								result = new Vector2(xToPos+x, yToPos+y);
								finished = true;
								break;
							}
						}
					}
					else
					{
						for(int y = cDist;y >= -cDist;y--)
						{
							if(finished)
								break;

							if(xToPos+x >= 0 && xToPos+x < this.width && yToPos+y >= 0 && yToPos+y < this.height && 
									(!this.doesRectCollideWithMap((xToPos+x)*64, (yToPos+y)*64, width, height)))
							{
								if(yToPos+y-1 < 0 || this.map[yToPos+y-1][xToPos+x] == null || !this.map[yToPos+y-1][xToPos+x].isCollidable())
									continue;
								
								result = new Vector2(xToPos+x, yToPos+y);
								finished = true;
								break;
							}
						}
					}
				}
			}
			else
			{
				for(int x = cDist;x >= -cDist;x--)
				{
					if(finished)
						break;

					if(directionY == 1)
					{
						for(int y = -cDist;y <= cDist;y++)
						{
							if(finished)
								break;

							if(xToPos+x >= 0 && xToPos+x < this.width && yToPos+y >= 0 && yToPos+y < this.height && 
									(!this.doesRectCollideWithMap((xToPos+x)*64, (yToPos+y)*64, width, height)))
							{
								if(yToPos+y-1 < 0 || this.map[yToPos+y-1][xToPos+x] == null || !this.map[yToPos+y-1][xToPos+x].isCollidable())
									continue;
								
								result = new Vector2(xToPos+x, yToPos+y);
								finished = true;
								break;
							}
						}
					}
					else
					{
						for(int y = cDist;y >= -cDist;y--)
						{
							if(finished)
								break;

							if(xToPos+x >= 0 && xToPos+x < this.width && yToPos+y >= 0 && yToPos+y < this.height && 
									(!this.doesRectCollideWithMap((xToPos+x)*64, (yToPos+y)*64, width, height)))
							{
								if(yToPos+y-1 < 0 || this.map[yToPos+y-1][xToPos+x] == null || !this.map[yToPos+y-1][xToPos+x].isCollidable())
									continue;
								
								result = new Vector2(xToPos+x, yToPos+y);
								finished = true;
								break;
							}
						}
					}

				}
			}
			cDist++;
		}
		return result;
	}

	public Tile[][] getMap() {
		return map;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public Tile getTileTypeByCoordinate(int col, int row)
	{
		if (col < 0 || col >= this.width || row < 0 || row >= this.height)
			return null;

		return map[row][col];
	}

	public Tile getTileTypeByCoordinateFg(int col, int row)
	{
		if (col < 0 || col >= this.width || row < 0 || row >= this.height)
			return null;

		return fg[row][col];
	}

	public void drawTiles(SpriteBatch batch)
	{
		Rectangle s = Main.getScreen(64);
		int startX = (int) Math.floor(s.x/Tile.TILE_SIZE);
		int startY = (int) Math.floor(s.y/Tile.TILE_SIZE);
		int endX = (int)Math.ceil((s.x+s.width)/Tile.TILE_SIZE);
		int endY = (int)Math.ceil((s.y+s.height)/Tile.TILE_SIZE);

		startX = Math.max(0, startX);
		startY = Math.max(0, startY);

		endX = Math.min(this.width, endX);
		endY = Math.min(this.height, endY);
		int count = 0;
		for(int y = startY;y < endY;y++)
		{
			for(int x = startX;x < endX;x++)
			{
				if(map[y][x] != null)
				{
					if(!map[y][x].isTileMapped())
					{
						Color color = new Color(Main.mixFloats(1f, 0.5f, this.globalX/8f), Main.mixFloats(1f, 0.01f, this.globalX/8f), Main.mixFloats(1f, 0.5f, this.globalX/8f), 1f);
						batch.setColor(color);
						batch.draw(Content.tiles[map[y][x].id-1], x * Tile.TILE_SIZE, y * Tile.TILE_SIZE);
						batch.setColor(Color.WHITE);
					}
					else
					{
						int hx = 64;
						int hy = 64;
						TextureRegion txtreg = new TextureRegion(Content.tiles[map[y][x].id-1]);
						
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
						
						if(x+1 >= this.width)
						{
							haveright = true;
						}
						else if((map[y][x+1] == null))
						{
							haveright = false;
						}
						
						if(y+1 >= this.height)
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
						
						txtreg.setRegion(hx, hy, 64, 64);
						batch.draw(txtreg, x * Tile.TILE_SIZE, y * Tile.TILE_SIZE);
						count++;
					}
				}
			}
		}
		//Main.var[3] = count;
	}
	
	public void drawForeground(SpriteBatch batch)
	{
		if(fg == null || fg[0] == null)
			return;
		
		Rectangle s = Main.getScreen(64);
		int startX = (int) Math.floor(s.x/Tile.TILE_SIZE);
		int startY = (int) Math.floor(s.y/Tile.TILE_SIZE);
		int endX = (int)Math.ceil((s.x+s.width)/Tile.TILE_SIZE);
		int endY = (int)Math.ceil((s.y+s.height)/Tile.TILE_SIZE);

		startX = Math.max(0, startX);
		startY = Math.max(0, startY);

		endX = Math.min(this.width, endX);
		endY = Math.min(this.height, endY);
		int count = 0;
		boolean foregrounding = this.doesRectCollideWithForeground(Main.player[Main.me].position.x, Main.player[Main.me].position.y, Main.player[Main.me].width, Main.player[Main.me].height);
		if(foregrounding)
		{
			batch.setColor(batch.getColor().r,batch.getColor().g,batch.getColor().b,0.1f);
		}
		else
		{
			batch.setColor(batch.getColor().r,batch.getColor().g,batch.getColor().b,1f);
		}
		if(Main.usingSkill(Main.player[Main.me]))
		{
			float minus = 1f-Main.usingSkillMinus(Main.player[Main.me]);
			batch.setColor(batch.getColor().mul(minus, minus, minus, 1f));
		}
		for(int y = startY;y < endY;y++)
		{
			for(int x = startX;x < endX;x++)
			{
				if(fg[y][x] != null)
				{
					if(!fg[y][x].isTileMapped())
						batch.draw(Content.tiles[fg[y][x].id-1], x * Tile.TILE_SIZE, y * Tile.TILE_SIZE);
					else
					{
						int hx = 64;
						int hy = 64;
						TextureRegion txtreg = new TextureRegion(Content.tiles[fg[y][x].id-1]);
						
						boolean haveleft = true;
						boolean haveright = true;
						boolean haveup = true;
						boolean havedown = true;
						
						if(x-1 < 0)
						{
							haveleft = true;
						}
						else if((fg[y][x-1] == null) && (map[y][x-1] == null))
						{
							haveleft = false;
						}
						
						if(x+1 >= this.width)
						{
							haveright = true;
						}
						else if((fg[y][x+1] == null) && (map[y][x+1] == null))
						{
							haveright = false;
						}
						
						if(y+1 >= this.height)
						{
							haveup = true;
						}
						else if((fg[y+1][x] == null) && (map[y+1][x] == null))
						{
							haveup = false;
						}
						
						if(y-1 < 0)
						{
							havedown = true;
						}
						else if((fg[y-1][x] == null) && (map[y-1][x] == null))
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
						
						txtreg.setRegion(hx, hy, 64, 64);
						batch.draw(txtreg, x * Tile.TILE_SIZE, y * Tile.TILE_SIZE);
						count++;
					}
				}
			}
		}
		batch.setColor(1f,1f,1f,1f);
		//Main.var[3] = count;
	}
	
	public void drawBg(SpriteBatch batch)
	{
		float width = Main.camera.viewportWidth * Math.abs(Main.camera.zoom);
		float height = Main.camera.viewportHeight * Math.abs(Main.camera.zoom);
		Rectangle s = new Rectangle(Main.camera.position.x - (width / 2) - 300,
				Main.camera.position.y - (height / 2) - 300, width + (200 * 2), height + (200 * 2));
		int startX = (int) Math.floor(s.x/Tile.TILE_SIZE);
		int startY = (int) Math.floor(s.y/Tile.TILE_SIZE);
		int endX = (int)Math.ceil((s.x+s.width)/Tile.TILE_SIZE);
		int endY = (int)Math.ceil((s.y+s.height)/Tile.TILE_SIZE);

		startX = Math.max(0, startX);
		startY = Math.max(0, startY);

		endX = Math.min(this.width, endX);
		endY = Math.min(this.height, endY);
		int count = 0;
		for(int z = 0;z < this.bg[0][0].length;z++)
		{
			for(int y = startY;y < endY;y++)
			{
				for(int x = startX;x < endX;x++)
				{
					if(bg[y][x][z] != null && !bg[y][x][z].highPriority)
					{
						if(Main.player[Main.me].myMapY < 0 && this.map[y][x] != null)
						{
							continue;
						}
						batch.draw(Content.bg[bg[y][x][z].id-1], x * Tile.TILE_SIZE, y * Tile.TILE_SIZE);
						count++;
					}
				}
			}
			if(!Main.loadingMap && Main.HPBGs != null && Main.HPBGs.get(z) != null && Main.HPBGs.get(z).size() > 0)
			{
				for(Iterator<HPBG> it = Main.HPBGs.get(z).iterator();it.hasNext();)
				{
					if(Main.loadingMap)
						break;
					HPBG hpbg = it.next();
					Texture txt = Content.bg[hpbg.background.id-1];
					if(Main.getScreen(50).overlaps(new Rectangle(hpbg.x * Tile.TILE_SIZE, hpbg.y * Tile.TILE_SIZE, txt.getWidth(), txt.getHeight())))
					{
						batch.draw(txt, hpbg.x * Tile.TILE_SIZE, hpbg.y * Tile.TILE_SIZE);
						count++;
					}
				}
			}
		}
		//Main.var[1] = count;
	}
	
	public void generateLightMap()
	{
		madeLight = new boolean[height][width];
		this.lightMap = new float[height][width];
		if(!this.usesLight)
			return;
		
		for(int y = 0;y < height;y++)
		{
			for(int x = 0;x < width;x++)
			{
				madeLight[y][x] = false;
				lightMap[y][x] = 1f;
				if(lightPasses(y, x))
				{
					madeLight[y][x] = true;
				}
			}
		}
		for(int y = 0;y < height;y++)
		{
			for(int x = 0;x < width;x++)
			{
				if(!lightPasses(y, x))
				{
					lightMap[y][x] = this.maximumLight * (float)Math.pow(0.9f, (calculateLightDistance(y,x))*2);
					
					if(this.id == 23)
					{
						lightMap[y][x] *= 0.7f;
					}
					if(this.lightMap[y][x] < this.minimumLight)
						this.lightMap[y][x] = this.minimumLight;
				}
			}
		}
	}
	
	public void renderShadow(SpriteBatch batch)
	{
		if(lightMap == null)
			return;
		Vector2 cameraPos = Main.cameraRealPosition();
		float cameraWidth = Main.camera.viewportWidth*Main.camera.zoom;
		float cameraHeight = Main.camera.viewportHeight*Main.camera.zoom;
		int startY = (int)Math.floor((cameraPos.y)/Tile.TILE_SIZE)-1;
		int endY = (int)Math.ceil((cameraPos.y+cameraHeight)/Tile.TILE_SIZE)+1;
		int startX = (int)Math.floor((cameraPos.x)/Tile.TILE_SIZE)-1;
		int endX = (int)Math.ceil((cameraPos.x+cameraWidth)/Tile.TILE_SIZE)+1;
		
		if(startY < 0)
			startY = 0;
		
		if(endY > height)
			endY = height;
		
		if(startX < 0)
			startX = 0;
		
		if(endX > width)
			endX = width;

		float mult = 1f;
		if(this.isOutside())
		{
			if(Main.dayEHour() >= 19f || Main.dayEHour() <= 4)
			{
				mult = 0.65f;
			}
			else if(Main.dayEHour() > 18f && Main.dayEHour() < 19f)
			{
				mult = Main.mixFloats(0.65f, 1f, 19f-Main.dayEHour());
			}
			else if(Main.dayEHour() > 4 && Main.dayEHour() < 5)
			{
				mult = Main.mixFloats(1f, 0.65f, 5f-Main.dayEHour());
			}
		}
		for(int y = startY;y < endY;y++)
		{
			for(int x = startX;x < endX;x++)
			{
				/*if(madeLight[y][x])
				{
					batch.draw(Content.light, x*Tile.TILE_SIZE-256, y*Tile.TILE_SIZE-256, 512, 512);
				}*/
				Sprite black = new Sprite(Content.black);
				black.setPosition(x*Tile.TILE_SIZE, y*Tile.TILE_SIZE);
				black.setSize(Tile.TILE_SIZE, Tile.TILE_SIZE);
				float extra = 0f;
				for(Player p : Constant.getPlayerList())
				{
					if(p != null && p.sameMapAs(Constant.getPlayerList()[Main.me]) && Main.usingSkill(p) && Main.usingSkillMinus(p) > extra)
					{
						extra = Main.usingSkillMinus(p);
					}
				}
				black.setAlpha(Main.clamp(0f, (1f-lightMap[y][x]-extra/2)*mult, 1f));
				black.draw(batch);
			}
		}
	}
	
	public int calculateLightDistance(int y, int x)
	{
		int nextestDistance = 999;
		for(int j = 0;j < height;j++)
		{
			for(int i = 0;i < width;i++)
			{
				if(madeLight[j][i])
				{
					int dist = calculateTileDistance(x, y, i, j)-1;
					if(dist < nextestDistance)
					{
						nextestDistance = dist;
					}
				}
			}
		}
		return nextestDistance;
	}

	public int calculateTileDistance(int x1, int y1, int x2, int y2)
	{
		int xDist = (int) (Math.abs(x1 - x2));
		int yDist = (int) (Math.abs(y1 - y2));		
		return xDist + yDist;
	}
	
	public boolean lightPasses(int y, int x)
	{
		boolean pass = false;
		/*if((map[y][x] == null) || (map[y][x] != null && !map[y][x].isCollidable()))
			pass = true;*/
		
		if((map[y][x] == null || !map[y][x].isCollidable()) && (fg[y][x] == null || !fg[y][x].isCollidable()))
		{
			boolean canPass = true;
			/*for(int z = 0;z < this.bg[0][0].length;z++)
			{
				if((bg[y][x][z] != null && !bg[y][x][z].isLightMaker()))
					canPass = false;
			}*/
			pass = canPass;
		}
		if(map[y][x] != null)
		{
			if(map[y][x].isLightMaker())
				pass = true;
		}
		if(fg[y][x] != null)
		{
			if(fg[y][x].isLightMaker())
				pass = true;
		}
		
		return pass;
	}


	public Texture drawMinimap()
	{
		Pixmap pixmap = new Pixmap(this.width, this.height, Pixmap.Format.RGBA8888);
		for(int y = 0;y < this.height;y++)
		{
			for(int x = 0;x < this.width;x++)
			{
				if(this.map[(height-1)-y][(width-1)-x] != null)
				{
					Color fc = new Color(0.5f, 0.5f, 0.5f, 1f);
					fc.a = 0.8f;
					pixmap.setColor(fc);
					pixmap.drawPixel((width-1)-x, y);
				}
			}
		}
		Texture t = new Texture(pixmap);
		pixmap.dispose();
		return t;
	}
	
	public void postLoad()
	{
		if(this.id == 2)
		{
			this.name = Main.lv("Peaceful Fields", "Campos Pacíficos");
			this.subtitle = Main.lv("Recommended level: 1", "Nível recomendado: 1");
			this.randomrespawns.add(36);
			this.randomrespawns.add(25);
			this.randomrespawns.add(8);
			this.randomrespawns.add(10);
			this.backgroundMusic = DJ._PFIELDS;
		}
		else if(this.id == 130)
		{
			this.name = Main.lv("Groves", "Arvoredos");
			this.subtitle = Main.lv("Recommended level: 5", "Nível recomendado: 5");
			this.randomrespawns.add(26);
			this.randomrespawns.add(28);
			this.randomrespawns.add(29);
			this.randomrespawns.add(20);
			this.randomrespawns.add(18);
			this.randomrespawns.add(27);
			this.backgroundMusic = DJ._FOREST;
		}
		else if(this.id == 140)
		{
			this.name = Main.lv("Rock Caves", "Cavernas Rochosas");
			this.maximumLight = 0.6f;
			this.subtitle = Main.lv("Recommended level: 10", "Nível recomendado: 10");
			this.randomrespawns.add(21);
			this.randomrespawns.add(22);
			this.randomrespawns.add(35);
			this.randomrespawns.add(1);
			this.randomrespawns.add(30);
			this.randomrespawns.add(49);
			this.backgroundMusic = DJ._CAVE;
			float refreshTime = 60*30;
			if(this.lastUpdateTime < Instant.now().getEpochSecond()-refreshTime)
			{
				for (Iterator<Prop> iterator = this.props.iterator(); iterator.hasNext();) {
					Prop p = iterator.next();
					if(p.genTag.equalsIgnoreCase("dungeon"))
					{
						iterator.remove();
					}
					
				}
				for(int y = 0;y < this.bg.length;y++)
				{
					for(int x = 0;x < this.bg[0].length;x++)
					{
						if(this.bg[y][x][0] == Background.DUNGEONDOORGRAY)
						{
							this.bg[y][x][0] = null;
						}
					}
				}
				for(int i = 0;i < MathUtils.random(5, 8);i++)
				{
					Vector2 pos = new Vector2();
					pos = new Vector2(MathUtils.random(this.width*64f), MathUtils.random(this.height*64f));
					pos = this.getNextestFreeSpace(pos.x, pos.y, 156, 196, 1, 1);
					pos.y = this.getNotFreeY((int)pos.x, (int)pos.y);
					
					this.bg[(int) pos.y][(int) pos.x][0] = Background.DUNGEONDOORGRAY;
					
					pos.scl(64f);
					float localx = 1.5f;
					int localy = 1;
					Prop p = new Prop(pos, 156, 196, Constant.PROPID_TELEPORT, new int[] {
							MathUtils.random(305, Integer.MAX_VALUE/2), MathUtils.random(305, Integer.MAX_VALUE/2),
							(int)(localx*64)+640, localy*64+640, 30*localy + (int)localx, Constant.MAPFLAG_RANDOM, Constant.MAPFLAG_RANDOM, Constant.MAPFLAG_RANDOM, Constant.MAPFLAG_RANDOM, 141
							}, true);
					p.mapDisplayIndex = 32;
					p.genTag = "dungeon";
					this.props.add(p);
				}
				this.updateLastUpdate();
				System.out.println("[@@] Updated ["+this.name+"] dungeon doors at (" + this.globalX + ", " + this.globalY +").");
			}
		}
		else if(this.id == 150)
		{
			this.name = Main.lv("Underground Passages", "Passagens Subterrâneas");
			this.subtitle = Main.lv("Recommended level: 20", "Nível recomendado: 20");
			this.randomrespawns.add(32);
			this.randomrespawns.add(42);
			this.randomrespawns.add(44);
			this.randomrespawns.add(37);
			this.backgroundMusic = DJ._FOREST;
		}
		else if(this.id == 160)
		{
			this.name = Main.lv("Dark Fields", "Campos Sombrios");
			this.subtitle = Main.lv("Recommended level: 20", "Nível recomendado: 20");
			this.randomrespawns.add(52);
			this.randomrespawns.add(53);
			this.randomrespawns.add(54);
			this.backgroundMusic = DJ._CAVE;
		}
		else if(this.id == 170)
		{
			this.name = Main.lv("Ghost Groves", "Floresta Fantasma");
			this.subtitle = Main.lv("Recommended level: 25", "Nível recomendado: 25");
			this.randomrespawns.add(55);
			this.randomrespawns.add(56);
			this.randomrespawns.add(57);
			this.randomrespawns.add(58);
			this.randomrespawns.add(59);
			this.randomrespawns.add(60);
			this.backgroundMusic = DJ._CAVE;
		}
		else if(this.id == 180)
		{
			this.name = Main.lv("Spirit Lair", "Covil dos Espíritos");
			this.subtitle = Main.lv("Recommended level: 30", "Nível recomendado: 30");
			this.randomrespawns.add(62);
			this.randomrespawns.add(63);
			this.randomrespawns.add(64);
			this.randomrespawns.add(65);
			this.randomrespawns.add(66);
			this.randomrespawns.add(67);
			this.backgroundMusic = DJ._CAVE;
			this.maximumLight = 0.6f;
			float refreshTime = 60*30;
			if(this.lastUpdateTime < Instant.now().getEpochSecond()-refreshTime)
			{
				for (Iterator<Prop> iterator = this.props.iterator(); iterator.hasNext();) {
					Prop p = iterator.next();
					if(p.genTag.equalsIgnoreCase("dungeon"))
					{
						iterator.remove();
					}
					
				}
				for(int i = 0;i < MathUtils.random(5, 8);i++)
				{
					Vector2 pos = new Vector2();
					pos = new Vector2(MathUtils.random(this.width*64f), MathUtils.random(this.height*64f));
					pos = this.getNextestFreeSpace(pos.x, pos.y, 156, 196, 1, 1);
					pos.y = this.getNotFreeY((int)pos.x, (int)pos.y);
					
					this.bg[(int) pos.y][(int) pos.x][1] = Background.DUNGEONDOORGRAY;

					pos.scl(64f);
					float localx = 1.5f;
					int localy = 1;
					Prop p = new Prop(pos, 156, 196, Constant.PROPID_TELEPORT, new int[] {
							MathUtils.random(305, Integer.MAX_VALUE/2), MathUtils.random(305, Integer.MAX_VALUE/2),
							(int)(localx*64)+640, localy*64+640, 30*localy + (int)localx, Constant.MAPFLAG_RANDOM, Constant.MAPFLAG_RANDOM, Constant.MAPFLAG_RANDOM, Constant.MAPFLAG_RANDOM, 181
					}, true);
					p.mapDisplayIndex = 32;
					p.genTag = "dungeon";
					this.props.add(p);
				}
				this.updateLastUpdate();
				System.out.println("[@@] Updated ["+this.name+"] dungeon doors at (" + this.globalX + ", " + this.globalY +").");
			}
		}
	}
	
	private void postGen()
	{
		if(this.globalY == -1)
		{
			HashMap<Integer, Integer> ghosts = new HashMap<Integer, Integer>();
			ghosts.put(4, 38);
			ghosts.put(-3, 39);
			if(ghosts.containsKey(this.globalX))
			{
				Vector2 pos = this.getRandomTile().add(0, 1).scl(64f);
				NPC g = NPC.CreateBase(pos, ghosts.get(this.globalX));
				this.npcs.add(g);
			}
		}
	}

	public void postPostGen()
	{
		System.out.println("Started postGen method for map id " + this.id);
		if(this.id == 130)
		{
			for(int y = 16;y < this.height-1;y++)
			{
				for(int x = 0;x < this.width;x++)
				{
					int chance = MathUtils.random(6);
					if(this.map[y-1][x] == Tile.GRASS && chance == 0)
					{
						this.bg[y][x][0] = Background.getTileTypeById(MathUtils.random(17, 21));
					}
					else if(this.map[y-1][x] == Tile.GRASS && chance < 4)
					{
						this.bg[y][x][0] = Background.HIGHGRASS;
					}
					else if(this.map[y-1][x] != null && chance == 5 && MathUtils.random(4) == 0)
					{
						Respawn r = new Respawn(46, 300, new Vector2(x, y).scl(64f));
						this.respawns.add(r);
					}
				}
			}
			for(int x = 4;x < this.width - 4;x += 6)
			{
				int y = this.getNotFreeY(x, this.height-1);
				Respawn r = new Respawn(17, 300, new Vector2(x, y).scl(64f));
				this.respawns.add(r);
			}
		}
		else if(this.id == 140)
		{
			for(int y = 1;y < this.height;y++)
			{
				for(int x = 0; x < this.width;x++)
				{
					if(this.map[y-1][x] != null && this.map[y][x] == null)
					{
						int r = MathUtils.random(1, 25);
						if(r >= 2 && r <= 4)
						{
							Respawn re = new Respawn(47, 600, new Vector2(x, y).scl(64f));
							this.respawns.add(re);
						}
						else if(r == 1)
						{
							Respawn re = new Respawn(68, 600, new Vector2(x, y).scl(64f));
							this.respawns.add(re);
						}
						r = MathUtils.random(15);
						if(r == 1)
						{
							this.respawns.add(new Respawn(19, 300, new Vector2(x, y+2).scl(64f)));
							x += MathUtils.random(0, 30);
						}
						x += MathUtils.random(5, 10);
					}
				}
			}
		}
		else if(this.id == 150)
		{
			for(int y = 1;y < this.height;y++)
			{
				for(int x = 0; x < this.width;x++)
				{
					if(this.map[y-1][x] != null && this.map[y][x] == null)
					{
						int r = MathUtils.random(1, 25);
						if(r >= 2 && r <= 4)
						{
							Respawn re = new Respawn(68, 600, new Vector2(x, y).scl(64f));
							this.respawns.add(re);
						}
						else if(r == 1)
						{
							Respawn re = new Respawn(48, 600, new Vector2(x, y).scl(64f));
							this.respawns.add(re);
						}
						x += MathUtils.random(5, 10);
					}
				}
			}
		}
		else if(this.id == 180)
		{
			for(int y = 1;y < this.height;y++)
			{
				for(int x = 0; x < this.width;x++)
				{
					if(this.map[y-1][x] != null && this.map[y][x] == null)
					{
						int r = MathUtils.random(1, 15);
						if(r == 1)
						{
							Respawn re = new Respawn(47, 600, new Vector2(x, y).scl(64f));
							this.respawns.add(re);
						}
						r = MathUtils.random(20);
						if(r == 1)
						{
							this.respawns.add(new Respawn(19, 300, new Vector2(x, y+2).scl(64f)));
							x += MathUtils.random(0, 30);
						}
						x += MathUtils.random(5, 10);
					}
				}
			}
		}
	}
	
	public Vector2 getRandomTile()
	{
		ArrayList<Vector2> possibleTiles = new ArrayList<Vector2>();
		for(int y = 0;y < this.height-1;y++)
		{
			for(int x = 0;x < this.width;x++)
			{
				if(this.map[y][x] != null && this.map[y][x].isCollidable() && (this.map[y+1][x] == null || !this.map[y+1][x].isCollidable()))
				{
					possibleTiles.add(new Vector2(x, y));
				}
			}
		}
		Collections.shuffle(possibleTiles);
		return possibleTiles.get(0);
	}
	
	public void onEnter()
	{
		if(this.id == 12)
		{
			if(Main.player[Main.me].saveInfo.bragusInPlace == 1)
			{
				NPC n = NPC.CreateBase(Main.player[Main.me].position, Constant.NPCID_BRAGUS);
				n.useDialog = 21;
				n.temporary = true;
				n.myMapX = Main.player[Main.me].myMapX;
				n.myMapY = Main.player[Main.me].myMapY;
				Main.npc.add(n);
			}
		}
		else if(this.id == 13)
		{
			if(Main.player[Main.me].saveInfo.bragusInPlace == 2)
			{
				NPC n = NPC.CreateBase(Main.player[Main.me].position, Constant.NPCID_BRAGUS);
				n.useDialog = 37;
				n.temporary = true;
				n.myMapX = Main.player[Main.me].myMapX;
				n.myMapY = Main.player[Main.me].myMapY;
				Main.npc.add(n);
			}
		}
		else if(this.id == 14)
		{
			if(Main.player[Main.me].saveInfo.bragusInPlace == 3)
			{
				NPC n = NPC.CreateBase(Main.player[Main.me].position, Constant.NPCID_BRAGUS);
				n.useDialog = 64;
				n.temporary = true;
				n.myMapX = Main.player[Main.me].myMapX;
				n.myMapY = Main.player[Main.me].myMapY;
				Main.npc.add(n);
			}
		}
		else if(this.id == 15)
		{
			if(Main.player[Main.me].saveInfo.bragusInPlace == 4)
			{
				NPC n = NPC.CreateBase(Main.player[Main.me].position, Constant.NPCID_BRAGUS);
				n.useDialog = 97;
				n.temporary = true;
				n.myMapX = Main.player[Main.me].myMapX;
				n.myMapY = Main.player[Main.me].myMapY;
				Main.npc.add(n);
			}
		}
		else if(Main.player[Main.me].myMapX == 50 && Main.player[Main.me].myMapY == 149 && Main.player[Main.me].haveObjective(10) != null)
		{
			Quest q;
			if((q = Main.player[Main.me].getQuest(4)) != null)
			{
				q.objectives.clear();
				q.objectives.add(Quest.generateKillObjective(2, 1));
				q.npcQuesterId = 14;
			}
		}
		else if(Main.player[Main.me].myMapX == 52 && Main.player[Main.me].myMapY == 148 && Main.player[Main.me].haveObjective(11) != null)
		{
			NPC n = NPC.CreateBase(Main.player[Main.me].position.cpy().add(new Vector2(64, 0)), 14);
			n.useDialog = 217;
			n.collisionsEnabled = false;
			n.myMapX = Main.player[Main.me].myMapX;
			n.myMapY = Main.player[Main.me].myMapY;
			n.temporary = true;
			Main.npc.add(n);
		}
	}
	
	public int getFreeY(int x)
	{
		int my = 0;
		for(int y = 0;y < this.height;y++)
		{
			if(this.map[y][x] == null || !this.map[y][x].isCollidable())
			{
				my = y;
				break;
			}
		}
		return my+1;
	}
	
	public int getFreeY(int x, int startY)
	{
		int my = 0;
		for(int y = Main.clamp(0, startY, this.height-1);y >= 0;y--)
		{
			if(this.map[y][x] == null || !this.map[y][x].isCollidable())
			{
				my = y;
				break;
			}
		}
		return my+1;
	}
	
	public int getNotFreeY(int x, int startY)
	{
		int my = 0;
		for(int y = Main.clamp(0, startY, this.height-1);y >= 0;y--)
		{
			if(this.map[y][x] != null && this.map[y][x].isCollidable())
			{
				my = y;
				break;
			}
		}
		return my+1;
	}
	
	public static int getXMapWidth(final int mx, final int referenceY)
	{
		System.out.println("Finding maps in global X " + mx);
		String useName = Main.saveName;
		
		int width = -1;
		File f = new File("maps/" + useName + "/");
		System.out.println("Searching in maps/" + useName + "/");
		File[] matchingFiles = f.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
				return name.startsWith(mx + "_");
		    }
		});
		if(matchingFiles.length < 1)
		{
			f = new File("maps/base/");
			System.out.println("Searching in maps/base/");
			matchingFiles = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.startsWith(mx + "_");
			    }
			});
		}
		for(File file : matchingFiles)
		{
			String name = file.getName().substring(0, file.getName().length() - 4);
			String[] broken = name.split("_");
			int globalX = Integer.parseInt(broken[0]);
			int globalY = Integer.parseInt(broken[1]);
			System.out.println("Found file: " + name + "(" + globalX + ", " + globalY + ")");
			if(globalY != 300 && Math.abs(globalY - referenceY) < 10)
			{
				System.out.println("Valid Global Y, checking width.");
				GameMap temp = Main.PreLoadMap(globalX, globalY);
				width = temp.width;
				System.out.println(width + " width");
			}
		}
		return width;
	}

	public boolean isOutside()
	{
		if(this.globalY < 100 && Main.getFakeY(this.globalY)>=0)
			return true;
		
		return false;
	}

	public void summonWave(final Vector2 center, final int waveSize, final int force)
	{
		final ArrayList<ArrayList<Integer>> validMonsters = this.getAreaMonsterIds();
		System.out.println("0 size: " + validMonsters.get(0).size());
		System.out.println("1 size: " + validMonsters.get(1).size());
		if(validMonsters.get(0).size() <= 0 && validMonsters.get(1).size() <= 0)
			return;
		
		final GameMap gm = this;
		final int random = MathUtils.random(1, waveSize);
		for(int x = 1;x <= waveSize;x++)
		{
			final int i = x;
			Event e = new Event(i*15) {
				@Override
				public void function()
				{
					int dir = 1;
					if(i >= waveSize/2)
						dir = -1;

					boolean shouldFlyer = ((i <= waveSize/5 && validMonsters.get(1).size() > 0) || validMonsters.get(0).size() <= 0);
					ArrayList<Integer> list = validMonsters.get(shouldFlyer ? 1 : 0);
					int type = list.get(MathUtils.random(list.size()-1));

					Monster m = Monster.Create(center.cpy().add(MathUtils.random(700, 1700) * dir, (MathUtils.random(200f, 300f))), type, gm.globalX, gm.globalY);
					Player target = m.getNextestPlayer();
					if(target != null)
						m.target = target.whoAmI;
					
					m.swordTombSummon = true;

					if(i == random)
					{
						m.scale *= 2;
						m.health *= 5;
						m.maxHealth *= 5;
						m.impactDamage *= 2;
						m.knockbackImmune = true;
						m.boss = true;
						m.name = "Champion " + m.name;
						m.recommendedLevel += 6;
						m.addBuff(59, 999999f, m);
					}
				}
			};
			Main.scheduledTasks.add(e);
		}
		if(force > 0)
		{
			Event e = new Event((waveSize + 2) * 15) {
				@Override
				public void function()
				{
					Monster m = Monster.Create(center.add(MathUtils.random(700, 1700) * (MathUtils.randomBoolean() ? 1 : -1), MathUtils.random(200f, 300f)), force, gm.globalX, gm.globalY);
					m.target = m.getNextestPlayer().whoAmI;
					m.swordTombSummon = true;
				}
			};
			Main.scheduledTasks.add(e);
		}
	}

	public ArrayList<ArrayList<Integer>> getAreaMonsterIds()
	{
		ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>(2);
		ret.add(new ArrayList<Integer>());
		ret.add(new ArrayList<Integer>());
		// 0 = ground
		// 1 = flyer
		if(this.id == 141)
		{
			ret.get(0).add(21);
			ret.get(0).add(22);
			ret.get(0).add(35);
			ret.get(0).add(1);
			ret.get(0).add(30);
			ret.get(1).add(19);
		}
		else if(this.id == 181 || this.id > 0)
		{
			ret.get(0).add(62);
			ret.get(0).add(63);
			ret.get(0).add(64);
			ret.get(0).add(65);
			ret.get(0).add(67);
		}
		return ret;
	}
	
	public void updateLastUpdate()
	{
		this.lastUpdateTime = Instant.now().getEpochSecond();
		System.out.println("[@] Updated map [" + this.name + "] at (" + this.globalX + ", " + this.globalY + ")");
	}
}