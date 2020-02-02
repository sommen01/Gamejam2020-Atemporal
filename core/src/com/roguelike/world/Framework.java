package com.roguelike.world;

import com.badlogic.gdx.math.MathUtils;
import com.roguelike.game.Main;

public class Framework
{
	public static void createThrashDarkBuilding(GameMap w, int hx, int hy, int width, int height)
	{
		int maxX = hx + width;
		int maxY = hy + height;
		for(int y = hy;y <= maxY;y++)
		{
			for(int x = hx;x <= maxX;x++)
			{
				if(x >= w.width || x < 0 || y >= w.height || y < 0)
					continue;
				
				w.bg[y][x][0] = Background.THRASHDARKBUILDINGCENTER;
			}
		}
	}
	
	public static void createForestTree(GameMap w, int hx, int hy)
	{
		int height = MathUtils.random(15, 20);
		int hymax = hy+height;
		for(int y = hy; y < hymax;y++)
		{
			for(int x = hx-1;x <= hx+1;x++)
			{
				w.map[y][x] = Tile.TREEBODY;
			}
		}
		for(int y = hymax-1; y < hymax+3;y++)
		{
			for(int x = hx-3;x <= hx+3;x++)
			{
				w.map[y][x] = Tile.TREELEAVES;
			}
		}
		
		for(int y = hy;y < hymax-3;y++)
		{
			if(MathUtils.random(1) == 0)
			{
				int direction = -1;
				if(MathUtils.randomBoolean())
					direction = 1;
				
				if(direction == -1)
				{
					for(int x = hx - 3;x <= hx;x++)
					{
						w.map[y][x] = Tile.TREEBODY;
						w.map[y+1][x] = Tile.TREEBODY;
					}
				}
				else
				{
					for(int x = hx;x <= hx + 3;x++)
					{
						w.map[y][x] = Tile.TREEBODY;
						w.map[y+1][x] = Tile.TREEBODY;
					}
				}
				y += 2;
			}
		}
	}
	
	public static int createMountain(GameMap w, int mx, int my, Tile blockType, int size, int minStepY, int maxStepY, int maxHeight)
	{
		int finalSize = 0;
		int median = (int) MathUtils.random(size * 0.3f, size * 0.7f);
		int actualY = 0;
		for(int x = Main.clamp(0, mx, w.width);((size > 0 && x <= mx + size) || actualY >= 0) && x < w.width;x++)
		{
			for(int y = Main.clamp(0, my, w.height-1);y <= Main.clamp(0, my + Math.min(actualY, maxHeight), w.height-1);y++)
			{
				w.map[y][x] = blockType;
			}
			if(x < mx + median * 0.3f)
			{
				actualY += MathUtils.random(minStepY, maxStepY);
			}
			else if(x > mx + median * 0.7f)
			{
				actualY -= MathUtils.random(minStepY, maxStepY);
			}
			finalSize++;
		}
		return finalSize;
	}
	
	public static int createLine(GameMap w, int mx, int my, int layer, Background bgUse, int size)
	{
		for(int x = Math.max(mx, 0);x < Math.min(mx + size, w.width);x++)
		{
			w.bg[my][x][layer] = bgUse;
		}
		return size;
	}
	
	public static int createLine(GameMap w, int mx, int my, Tile tUse, int size)
	{
		for(int x = mx;x < mx + size;x++)
		{
			w.map[my][x] = tUse;
		}
		return size;
	}
	
	public static void createHouse(GameMap w, int hx, int hy, int width, int height, int roofheight)
	{
		int maxX = hx + width;
		if(maxX >= w.width)
		{
			maxX = w.width-1;
		}
		for(int y = hy; y <= hy + height;y++)
		{
			for(int x = hx; x <= maxX;x++)
			{
				if(x == hx || x == maxX)
				{
					w.bg[y][x][0] = Background.OAKPILLAR;
				}
				else
				{
					w.bg[y][x][0] = Background.HOUSEWALL;
				}
			}
		}
		
		int x1 = (int) Math.floor(hx + width/2);
		w.bg[hy][x1][0] = Background.DOORLB;
		w.bg[hy][x1+1][0] = Background.DOORRB;
		w.bg[hy+1][x1][0] = Background.DOORLT;
		w.bg[hy+1][x1+1][0] = Background.DOORRT;
		for(int y = hy+height;y < hy+height+roofheight;y++)
		{
			for(int x = hx-1; x <= maxX+1;x++)
			{
				w.bg[y][x][1] = Background.ROOFC;
			}
		}
		for(int y = 0;y < roofheight;y++)
		{
			for(int x = 0; x <= width+2;x++)
			{
				if(x-y < 0 || (width+2)-x-y < 0)
					w.bg[hy+height+y][hx-1+x][1] = null;
			}
		}
		for(int y = hy+height;y < hy+height+roofheight;y++)
		{
			for(int x = hx-1; x <= maxX+1;x++)
			{
				if(w.bg[y][x][1] == Background.ROOFC)
				{
					if(w.bg[y+1][x][1] == null || w.bg[y+1][x][1].name.compareToIgnoreCase("Roof") == -1)
					{
						w.bg[y][x][1] = Background.ROOFT;
					}
					if(w.bg[y-1][x][1] == null || w.bg[y-1][x][1].name.compareToIgnoreCase("Roof") == -1)
					{
						w.bg[y][x][1] = Background.ROOFB;
					}
					if(w.bg[y][x-1][1] == null || w.bg[y][x-1][1].name.compareToIgnoreCase("Roof") == -1)
					{
						w.bg[y][x][1] = Background.ROOFL;
					}
					if(w.bg[y][x+1][1] == null || w.bg[y][x+1][1].name.compareToIgnoreCase("Roof") == -1)
					{
						w.bg[y][x][1] = Background.ROOFR;
					}
				}
			}
		}
	}
}