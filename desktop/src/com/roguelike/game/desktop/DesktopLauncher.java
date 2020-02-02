package com.roguelike.game.desktop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.opengl.Display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;
import com.roguelike.constants.Constant;
import com.roguelike.entities.Monster;
import com.roguelike.entities.NPC;
import com.roguelike.entities.Prop;
import com.roguelike.game.AHME;
import com.roguelike.game.DJ;
import com.roguelike.game.Roguelike;
import com.roguelike.world.Background;
import com.roguelike.world.GameMap;
import com.roguelike.world.Respawn;
import com.roguelike.world.Tile;

public class DesktopLauncher {
	public static ArrayList<String> postMessages = new ArrayList<String>();
	public static ArrayList<Boolean> goodMessages = new ArrayList<Boolean>();
	public static AHME game;

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        if(arg.length < 1)
        {
        	config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
        	config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        	//config.fullscreen = true;
        	//config.resizable = false;
        	//System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        	new LwjglApplication(new Roguelike("Game"), config);
        	Timer timer = new Timer();
        	TimerTask tt = new TimerTask() {
        		public void run()
        		{
        			if(Display.isCreated())
        				Roguelike.focused = Display.isActive();
        		}
        	};
        	timer.schedule(tt, 0L, 1000L/60L);
        }
        else if(arg[0].equalsIgnoreCase("AHME"))
        {
        	config.foregroundFPS = 0;
        	config.vSyncEnabled = false;
        	game = new AHME();
        	new LwjglApplication(game, config);
        	startMenu();
        }
        else if(arg[0].equalsIgnoreCase("Calculator"))
        {
        	calculatorMenu();
        }
        else if(arg[0].equalsIgnoreCase("Server"))
        {
        	config.foregroundFPS = 60;
        	config.backgroundFPS = 60;
        	config.vSyncEnabled = false;
        	new LwjglApplication(new Roguelike("Server"), config);
        }
        else if(arg[0].equalsIgnoreCase("-char"))
        {
        	config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
        	config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        	//config.fullscreen = true;
        	//config.resizable = false;
        	//System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        	new LwjglApplication(new Roguelike("Game " + arg[1]), config);
        	Timer timer = new Timer();
        	TimerTask tt = new TimerTask() {
        		public void run()
        		{
        			if(Display.isCreated())
        				Roguelike.focused = Display.isActive();
        		}
        	};
        	timer.schedule(tt, 0L, 1000L/60L);
        }
	}
	
	public static void calculatorMenu()
	{
		Scanner sc = new Scanner(System.in);
		do
		{
			/*10 + (level-1) * (200/99); - Maximum value of a stat increase for items.*/
			ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(FColor.WHITE).background(BColor.BLACK).build();
			cp.print("Insert a monster level and I will calculate it's medium stats.\n", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);
			cp.setForegroundColor(FColor.YELLOW);
			cp.print(">> ");
			cp.clear();
			int level = sc.nextInt();
			cp.print("Insert the monster IA so I can display some infos.\n", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);
			boolean[] flyer = new boolean[] {false, true, true, false, true, false, false, false};
			boolean[] ranged = new boolean[] {false, true, false, false, false, false, true, true};
			cp.clear();
			cp.print("1 = Charge and Run\n");
			cp.print("2 = Fly Away and Shoot\n");
			cp.print("3 = Flying Knight\n");
			cp.print("4 = Charge and Dash\n");
			cp.print("5 = Flying Dummy\n");
			cp.print("6 = Walking Dummy\n");
			cp.print("7 = Shooting Sentry\n");
			cp.print("8 = Marksman\n");
			cp.setForegroundColor(FColor.YELLOW);
			cp.print(">> ");
			cp.clear();
			int ia = sc.nextInt();
			ia = Math.max(1, Math.min(ia, 8));
			float healthReducer = 1f;
			if(flyer[ia-1])
			{
				healthReducer *= 0.75f;
			}
			if(ranged[ia-1])
			{
				healthReducer *= 0.8f;
			}
			if(!flyer[ia-1] && !ranged[ia-1])
			{
				healthReducer *= 1.1f;
			}
			clearConsole();
			cp.print("=============================", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);cp.print("Monster Base", Attribute.BOLD, FColor.YELLOW, BColor.BLACK);cp.clear();cp.print("==============================", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);
			int maxHealth = (int)((20 + Math.pow(level * 8f, 1+level/110f)) * healthReducer * 1.5);
			int impactDamage = (int)((5 + Math.pow(level * 3f, 1+level/500f)) * 0.66f);
			cp.print("\nMonster Level (Tier): ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print(level + " (" + (int)(Math.max(level/5, 1)) + ")", Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			cp.print("\nHealth: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print(maxHealth, Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			cp.print("\nImpact Damage: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print(impactDamage, Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			cp.print("\nBase Move/Fly Speed: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((600 + level * 5), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			cp.print("\nBase Experience: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((int)DesktopLauncher.xpdrop(maxHealth, impactDamage, flyer[ia-1], ranged[ia-1]), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			cp.print("\n==============================", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);cp.print("AI Specific", Attribute.BOLD, FColor.YELLOW, BColor.BLACK);cp.print("==============================", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);
			if(ia == 1)
			{
				cp.print("\nStop Time: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((100 - level/4), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nMax Speed: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((int)((600 + level * 5)*1.25f), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nStop Distance: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((500 - level * 3), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nScare/Threat Distance: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((150 + level * 3), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			}
			else if(ia == 2)
			{
				cp.print("\nSpeed Divisor: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((int)((101-level)/10)+1, Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nDistance: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((500+level*5), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nProjectile Damage: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((int)(10 + level * 2.75f), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nProjectile Speed: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print("The lower the speed the higher the damage", Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nProjectile Cooldown: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print("The lower the cooldown the lower the damage", Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			}
			else if(ia == 3)
			{
				cp.print("\nDash Speed: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((750 + level * 6), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nKeep Height: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((500 - level), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			}
			else if(ia == 4)
			{
				cp.print("\nCharge Time: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((30 - ((level*15)/100)), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nDash Speed: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((1700 + level * 8), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nDash Disaccel: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print("95~98, increases duration and range.", Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nDistance To Charge: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((int)((400 - level * 2)/2), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			}
			else if(ia == 5)
			{
				cp.print("\nAcceleration: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((25 + level / 5), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			}
			else if(ia == 6)
			{
				cp.print("\nAcceleration: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((20 + level / 5), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nJump Height: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((900 + level * 2), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			}
			else if(ia == 7)
			{
				cp.print("\nProjectile Damage: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((int)(10 + level * 3), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nProjectile Speed: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print("The lower the speed the higher the damage", Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nProjectile Cooldown: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print("The lower the cooldown the lower the damage", Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nShoot Range: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((1000 + level * 8), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			}
			else if(ia == 8)
			{
				cp.print("\nProjectile Damage: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((int)(10 + level * 3.25f), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nProjectile Speed: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print("The lower the speed the higher the damage", Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nProjectile Cooldown: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print("The lower the cooldown the lower the damage", Attribute.BOLD, FColor.WHITE, BColor.BLACK);
				cp.print("\nKeep Distance: ", Attribute.CLEAR, FColor.CYAN, BColor.BLACK);cp.print((450 + level * 2), Attribute.BOLD, FColor.WHITE, BColor.BLACK);
			}
			cp.print("\n=======================================================================", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);
			cp.print("\nOnly Impact Damage and Health changes the Base EXP.", Attribute.CLEAR, FColor.RED, BColor.BLACK);
			cp.print("\nPress ENTER to start a new calculation.", Attribute.CLEAR, FColor.WHITE, BColor.BLACK);
			cp.clear();
			try {
				System.in.read();
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			clearConsole();
		} while(true);
	}
	
	public static void startMenu()
	{
		Scanner sc = new Scanner(System.in);
		String cmd = "";
		ColoredPrinter cp = new ColoredPrinter.Builder(1, false).foreground(FColor.WHITE).background(BColor.BLACK).build();
		clearConsole();
		cp.print("\"La tristesse durera toujours\"", Attribute.BOLD, FColor.MAGENTA, BColor.MAGENTA);cp.print("\n - Vincent Van Gogh\n\n", Attribute.LIGHT, FColor.MAGENTA, BColor.BLACK);
		do
		{
			cp.print("################################################\n");
			cp.print("#                                              #\n");
			cp.print("#");cp.print("        Arka Hero's Map Editor Console        ", Attribute.CLEAR, FColor.YELLOW, BColor.BLACK);cp.print("#\n");cp.clear();
			cp.print("#                                              #\n");
			cp.print("################################################\n");
			cp.print("#                                              #\n");
			cp.print("#");cp.print("        Insert commands in this window        ", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);cp.print("#\n");
			cp.print("#");cp.print("         Use ", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);cp.clear();
			cp.print("HELP", Attribute.CLEAR, FColor.YELLOW, BColor.BLACK); cp.clear();
			cp.print(" to see the commands         ", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);cp.clear();
			cp.print("#\n#                                              #\n");
			cp.print("################################################\n\n");
			if(postMessages.isEmpty())
			{
				cp.println("No messages to send", Attribute.CLEAR, FColor.GREEN, BColor.BLACK);
			}
			else
			{
				for(String s : postMessages)
				{
					cp.println(s, Attribute.CLEAR, (goodMessages.get(postMessages.indexOf(s)) ? FColor.GREEN : FColor.RED), BColor.BLACK);
				}
			}
			postMessages.clear();
			goodMessages.clear();
			cp.setForegroundColor(FColor.YELLOW);cp.print("");
			System.out.print(">> ");
			cp.setForegroundColor(FColor.CYAN);cp.print("");
			cmd = sc.nextLine();
			interpretateCommand(cmd);
			clearConsole();
		} while(!cmd.equalsIgnoreCase("QUIT"));
		sc.close();
	}
	
	private static void interpretateCommand(String cmd)
	{
		String[] params = cmd.split(" ");
		String[] sargs = cmd.split(" ", 2);
		String args = "";
		if(sargs.length >= 2)
			args = sargs[1];
		
		int paramCount = params.length-1;
		
		if(params[0].equalsIgnoreCase("setname"))
		{
			if(paramCount < 1)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			
			AHME.mapName = args;
			addMessage("Switched map name to: " + args, true);
		}
		if(params[0].equalsIgnoreCase("setbgcount"))
		{
			if(paramCount < 1)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			
			try
			{
				int id = Integer.parseInt(params[1]);
				game.bg = new Background[game.map.length][game.map[0].length][id];
				addMessage("Switched background layer count to: " + id, true);
			}
			catch(Exception e)
			{
				addMessage("Input ERROR!", false);
			}
		}
		else if(params[0].equalsIgnoreCase("setid"))
		{
			if(paramCount < 1)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			
			try
			{
				int id = Integer.parseInt(params[1]);
				AHME.mapId = id;
				addMessage("Switched map id to: " + id, true);
			}
			catch(Exception e)
			{
				addMessage("Input ERROR!", false);
			}
		}
		else if(params[0].equalsIgnoreCase("setzoom"))
		{
			if(paramCount < 1)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			
			try
			{
				float[] valid = new float[]{0.5f, 1f, 2f, 4f};
				float val = Float.parseFloat(params[1]);
				if(!Utils.isInArray(valid, val))
				{
					addMessage("Please, use only 0.5, 1, 2 and 4 values", false);
					return;
				}
				game.zoom = val;
				addMessage("Switched map zoom to: " + val, true);
			}
			catch(Exception e)
			{
				addMessage("Input ERROR!", false);
			}
		}
		else if(params[0].equalsIgnoreCase("loadmap"))
		{
			if(paramCount < 1)
			{
				addMessage("Invalid parameters", false);
				return;
			}

			if(!args.contains(".map"))
			{
				args = args + ".map";
			}
			
			FileHandle file = Gdx.files.local("maps/" + args);
			if(file.exists() && !file.isDirectory())
			{
				try {
					AHME.mapFileName = args;
					game.LoadMap();
					addMessage("Loaded map!", true);
				}
				catch(Exception e)
				{
					addMessage("There was an error while loading the map.", false);
					e.printStackTrace();
				}
			}
			else
			{
				args = "base/" + args;
				file = Gdx.files.local("maps/" + args);
				if(file.exists() && !file.isDirectory())
				{
					try {
						AHME.mapFileName = args;
						game.LoadMap();
						addMessage("Loaded map as " + args + "!", true);
					}
					catch(Exception e)
					{
						addMessage("There was an error while loading the base map.", false);
						e.printStackTrace();
					}
				}
				else
				{
					addMessage("File not found.", false);
				}
			}
		}
		else if(params[0].equalsIgnoreCase("savemap"))
		{
			try
			{
				game.SaveMap();
				addMessage("Map saved!", true);
			}
			catch (Exception e)
			{
				addMessage("Error during map save.", false);
			}
		}
		else if(params[0].equalsIgnoreCase("newmap"))
		{
			if(paramCount < 2)
			{
				addMessage("Invalid parameters", false);
				return;
			}

			try
			{
				int width = Integer.parseInt(params[2]);
				int height = Integer.parseInt(params[1]);
				game.startingNewMap = true;
				game.newMapWidth = width;
				game.newMapHeight = height;
				AHME.mapId = 666999;
				AHME.mapName = "Unnamed";
				AHME.mapFileName = "base.map";
				game.zoom = 1.0f;
				addMessage("Starting new " + height + "x" + width + " map.", true);

			}
			catch(Exception e)
			{
				addMessage("Input ERROR!", false);
			}
		}
		else if(params[0].equalsIgnoreCase("generate"))
		{
			if(paramCount < 1)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			
			try
			{
				int customWidth = -1;
				if(paramCount >= 2)
				{
					customWidth = Integer.parseInt(params[2]);
				}
				GameMap g = GameMap.GenerateBaseMap(Integer.parseInt(params[1]), null, 0, 0, 0, 0, 0, 0, true, customWidth);
				AHME.mapwidth = g.height;
				AHME.mapheight = g.width;
				game.map = g.map;
				AHME.mapName = g.name;
				AHME.mapId = g.id;
				game.zoom = g.zoom;
				AHME.baseLight = g.minimumLight;
				AHME.maxLight = g.maximumLight;
				AHME.prop = g.props;
				AHME.npc = g.npcs;
				AHME.monster = g.monsters;
				AHME.respawns = g.respawns;
				AHME.bgColor = g.bgColor;
				game.bg = g.bg;
				if(g.fg != null)
					game.fg = g.fg;
				else
					game.fg = new Tile[g.map.length][g.map[0].length];
				
				if(paramCount >= 3)
				{
					g.postPostGen();
					g.postLoad();
				}
			}
			catch(Exception e)
			{
				addMessage("An error ocurred.", false);
				e.printStackTrace();
			}
		}
		else if(params[0].equalsIgnoreCase("addnpc"))
		{
			if(paramCount < 3)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			
			int id = Integer.parseInt(params[3]);
			float x = Float.parseFloat(params[1]);
			float y = Float.parseFloat(params[2]);
			
			NPC n = new NPC();
			n.SetInfos(id, true);
			n.position.x = x;
			n.position.y = y;
			n.active = true;
			addMessage("Added " + n.name, true);
			if(paramCount >= 4)
			{
				try
				{
					n.useDialog = Integer.parseInt(params[4]);
					addMessage("Custom dialog: " + n.useDialog, true);
				}
				catch(Exception ex)
				{
					addMessage("Bad dialog", false);
				}
			}
			AHME.npc.add(n);
		}
		else if(params[0].equalsIgnoreCase("addmonster"))
		{
			if(paramCount < 3)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			
			int id = Integer.parseInt(params[3]);
			float x = Float.parseFloat(params[1]);
			float y = Float.parseFloat(params[2]);
			int cooldown = -1;
			
			if(paramCount > 3)
				cooldown = Integer.parseInt(params[4]);
			
			if(cooldown <= 0)
			{
				Monster m = new Monster();
				m.id = id;
				m.position.x = x;
				m.active = true;
				m.position.y = y;
				m.Reset(true);
				AHME.monster.add(m);
			}
			else
			{
				Respawn r = new Respawn(id, cooldown, new Vector2(x, y));
				AHME.respawns.add(r);
			}
		}
		else if(params[0].equalsIgnoreCase("addtpprop"))
		{
			if(paramCount < 14 && paramCount != 8)
			{
				addMessage("Invalid parameters: addtpprop X Y WIDTH HEIGHT GLOBALX GLOBALY LOCALX LOCALY [GEN1 GEN2 GEN3 GEN4 GEN5 DESIREDMAP]", false);
				return;
			}

			try
			{
				float x = Float.parseFloat(params[1]);
				float y = Float.parseFloat(params[2]);
				int width = Integer.parseInt(params[3]);
				int height = Integer.parseInt(params[4]);
				int globalX = Integer.parseInt(params[5]);
				int globalY = Integer.parseInt(params[6]);
				int localX = Integer.parseInt(params[7]);
				int localY = Integer.parseInt(params[8]);
				int special1 = 1337;
				int special2 = 1337;
				int special3 = 1337;
				int special4 = 1337;
				int special5 = 1337;
				int special6 = 1337;
				try
				{
					special1 = Integer.parseInt(params[9]);
				}
				catch(Exception ex)
				{ }

				try
				{
					special2 = Integer.parseInt(params[10]);
				}
				catch(Exception ex)
				{ }

				try
				{
					special3 = Integer.parseInt(params[11]);
				}
				catch(Exception ex)
				{ }

				try
				{
					special4 = Integer.parseInt(params[12]);
				}
				catch(Exception ex)
				{ }

				try
				{
					special5 = Integer.parseInt(params[13]);
				}
				catch(Exception ex)
				{ }

				try
				{
					special6 = Integer.parseInt(params[14]);
				}
				catch(Exception ex)
				{ }

				Prop p = new Prop();
				p.id = Constant.PROPID_TELEPORT;
				p.infos[Constant.PROPTELEPORT_GLOBALMAPX] = globalX;
				p.infos[Constant.PROPTELEPORT_GLOBALMAPY] = globalY;
				p.infos[Constant.PROPTELEPORT_LOCALMAPX] = localX;
				p.infos[Constant.PROPTELEPORT_LOCALMAPY] = localY;
				p.infos[Constant.PROPTELEPORT_SPECIALGEN] = special1;
				p.infos[Constant.PROPTELEPORT_SPECIALGEN+1] = special2;
				p.infos[Constant.PROPTELEPORT_SPECIALGEN+2] = special3;
				p.infos[Constant.PROPTELEPORT_SPECIALGEN+3] = special4;
				p.infos[Constant.PROPTELEPORT_SPECIALGEN+4] = special5;
				p.infos[Constant.PROPTELEPORT_SPECIALGEN+5] = special6;
				p.position = new Vector2(x, y);
				p.width = width;
				p.height = height;
				p.usable = true;
				AHME.prop.add(p);
				addMessage("Teleport prop added.", true);
			}
			catch(Exception ex)
			{
				addMessage("Error: " + ex.getMessage(), false);
			}
		}
		else if(params[0].equalsIgnoreCase("adddialogprop"))
		{
			if(paramCount < 6)
			{
				addMessage("Invalid parameters", false);
				return;
			}

			try
			{
				float x = Float.parseFloat(params[1]);
				float y = Float.parseFloat(params[2]);
				int width = Integer.parseInt(params[3]);
				int height = Integer.parseInt(params[4]);
				int dialog = Integer.parseInt(params[5]);
				boolean usable = Boolean.parseBoolean(params[6]);

				Prop p = new Prop();
				p.id = Constant.PROPID_DIALOG;
				p.infos[Constant.PROPDIALOG_DIALOGID] = dialog;
				p.position = new Vector2(x, y);
				p.width = width;
				p.height = height;
				p.usable = usable;
				AHME.prop.add(p);
				addMessage("Dialog prop added.", true);
			}
			catch(Exception ex)
			{
				addMessage("Error: " + ex.getMessage(), false);
			}
		}else if(params[0].equalsIgnoreCase("addprop"))
		{
			if(paramCount < 4)
			{
				addMessage("Invalid parameters", false);
				return;
			}

			try
			{
				float x = Float.parseFloat(params[1]);
				float y = Float.parseFloat(params[2]);
				int width = Integer.parseInt(params[3]);
				int height = Integer.parseInt(params[4]);

				Prop p = new Prop();
				p.id = 0;
				p.position = new Vector2(x, y);
				p.width = width;
				p.height = height;
				p.usable = true;
				AHME.prop.add(p);
				addMessage("Prop added.", true);
			}
			catch(Exception ex)
			{
				addMessage("Error: " + ex.getMessage(), false);
			}
		}
		else if(params[0].equalsIgnoreCase("setfilename"))
		{
			if(paramCount < 1)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			
			AHME.mapFileName = args;
			addMessage("Set save directory to maps/" + args, true);
		}
		else if(params[0].equalsIgnoreCase("setbgcolor"))
		{
			if(paramCount == 3)
			{
				try
				{
					float r = Float.parseFloat(params[1]);
					float g = Float.parseFloat(params[2]);
					float b = Float.parseFloat(params[3]);
					AHME.bgColor = new Color(r, g, b, 1f);
					addMessage("Background color set to ["+r+" "+g+" "+b+"]",true);
				}
				catch(Exception ex)
				{
					addMessage("There was an error while setting the background color:\n"+ex.getMessage(), false);
				}
			}
			else if(paramCount == 0)
			{
				AHME.bgColor = null;
				addMessage("Background color removed", true);
			}
			else
			{
				addMessage("Invalid parameters", false);
			}
		}
		else if(params[0].equalsIgnoreCase("setmonster"))
		{
			if(paramCount < 2)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			
			int id = Integer.parseInt(params[1]);
			int cooldown = Integer.parseInt(params[2]);
			AHME.currentMonster = id;
			AHME.currentRespCd = cooldown;
			Monster m = new Monster();
			m.id = id;
			m.Reset(true);
			if(cooldown <= 0)
			{
				addMessage("[M] now creates a fix " + m.name + ".", true);
			}
			else
			{
				addMessage("[M] now creates a " + m.name + " with " + cooldown + "s respawn time.", true);
			}
		}
		else if(params[0].equalsIgnoreCase("infos"))
		{
			addMessage("==============================================================", false);
			addMessage("Map name: " + AHME.mapName, true);
			addMessage("Map ID: " + AHME.mapId, true);
			addMessage("Map zoom: " + game.zoom, true);
			addMessage("Map file: " + AHME.mapFileName, true);
			addMessage("Map light: " + AHME.baseLight + " - " + AHME.maxLight, true);
			addMessage("Map iposition: " + AHME.initialPos.x + " - " + AHME.initialPos.y, true);
			addMessage("Map bg color: " + (AHME.bgColor != null ? AHME.bgColor.toString() : "none"), true);
			addMessage("Map bg music: " + (AHME.bgm != null ? AHME.bgm.name() : "none"), true);
			addMessage("==============================================================", false);
		}
		else if(params[0].equalsIgnoreCase("setlight"))
		{
			if(paramCount < 2)
			{
				addMessage("Invalid parameters", false);
				return;
			}

			float light = Float.parseFloat(params[1]);
			float light2 = Float.parseFloat(params[2]);
			AHME.baseLight = light;
			AHME.maxLight = light2;
			addMessage("Defined map light to: " + light + " - " + light2, true);
		}
		else if(params[0].equalsIgnoreCase("setipos"))
		{
			if(paramCount < 2)
			{
				addMessage("Invalid parameters", false);
				return;
			}

			float light = Float.parseFloat(params[1]);
			float light2 = Float.parseFloat(params[2]);
			AHME.initialPos = new Vector2(light, light2);
			addMessage("Defined initial positions to: " + light + " - " + light2, true);
		}
		else if(params[0].equalsIgnoreCase("setbgm"))
		{
			if(paramCount < 1)
			{
				addMessage("Invalid parameters", false);
				return;
			}
			String value = params[1];
			DJ ans = null;
			for(DJ d : DJ.values())
			{
				if(d.name().equalsIgnoreCase(value))
				{
					ans = d;
				}
			}
			
			if(ans != null)
			{
				AHME.bgm = ans;
				addMessage("Defined map BGM to: " + value, true);
			}
			else
			{
				addMessage("BGM not found.", false);
			}
		}
		else if(params[0].equalsIgnoreCase("help"))
		{
			addMessage("==============================================================", false);
			addMessage("setname <nome>                                          - Muda o nome do mapa", true);
			addMessage("setid <id>                                                - Muda o ID do mapa", true);
			addMessage("setfilename <nome>                                 - Muda o diretorio do mapa", true);
			addMessage("setzoom <valor>                                         - Muda o zoom do mapa", true);
			addMessage("setbgm <nome>                               - Muda a música de fundo do mapa", true);
			addMessage("loadmap <diretorio do mapa em /maps>                        - Carrega um mapa", true);
			addMessage("savemap                                                  - Salva o mapa atual", true);
			addMessage("infos                      		           - Mostra informacoes do mapa atual", true);
			addMessage("help                                                       - Mostra essa tela", true);
			addMessage("newmap <largura> <altura>              	                  - Cria um novo mapa", true);
			addMessage("generate <bioma> [custom width] [postgen+postload]        - Gera um novo mapa", true);
			addMessage("addnpc <pos x> <pos y> <id>                         - Adiciona um npc no mapa", true);
			addMessage("addmonster <pos x> <pos y> <id> [respawn time]  - Adiciona um monstro no mapa", true);
			addMessage("addtpprop <x> <y> <w> <h> <gx> <gy> <lx> <ly>  - Adiciona um teleprop no mapa", true);
			addMessage("adddialogprop <x> <y> <w> <h> <dialog> <use>- Adiciona um dialog prop no mapa", true);
			addMessage("setmonster <id> <respawn time>   - Define o monstro adicionado ao apertar [M]", true);
			addMessage("setlight <minimum> <maximum>             - Define a iluminacao minima do mapa", true);
			addMessage("setbgcolor [<r> <g> <b>]                         - Define a cor do background", true);
			addMessage("==============================================================", false);
		}
	}
	
	public static void addMessage(String s, boolean good)
	{
		postMessages.add(s);
		goodMessages.add(good);
	}

	public static void clearConsole()
	{
		for(int i = 0;i < 50;i++)
		{
			System.out.println("\n");
		}
	}
	
	public static float xpdrop(float maxHealth, int impactDamage, boolean flyer, boolean ranged)
	{
		float mediaLV = (float) (Math.pow(maxHealth/32f, 1.05f+(impactDamage/6666f)) + impactDamage);
		float aux = 1;

		if (flyer)
		{
			aux += 0.5f;
		}
		if (ranged)
		{
			aux += 0.5f;
		}
		return (mediaLV * aux);
	}
}
