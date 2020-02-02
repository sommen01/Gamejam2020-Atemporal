package com.roguelike.world;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

public enum Tile
{
	METALFLOOR(1, true, "Metal Tile", false, false, new Color(0.8f, 0.8f, 0.8f, 1f)),
	GRASS(2, true, "Grass", false, true, new Color(0f, 1f, 0f, 1f)),
	ROCKS(3, true, "Rocks", false, false, new Color(0.2f, 0.2f, 0.2f, 1f)),
	SUNFLOWER(4, false, "Sunflower", false, false, new Color(1f, 1f, 0f, 1f)),
	SUNFLOWER2(5, false, "Sunflower", false, false, new Color(1f, 1f, 0f, 1f)),
	BUSH(6, false, "Bush", false, false, new Color(0f, 0.8f, 0f, 1f)),
	BUSH2(7, false, "Bush", false, false, new Color(0f, 0.8f, 0f, 1f)),
	HIGHGRASS(8, false, "High Grass", false, false, new Color(0f, 1f, 0f, 1f)),
	STONE(9, true, "Stone", false, true, new Color(0.15f, 0.15f, 0.15f, 1f)),
	STONE2(10, true, "Stone", false, false, new Color(0.15f, 0.15f, 0.15f, 1f)),
	COBBLESTONE(11, true, "Cobblestone", false, false, new Color(0.2f, 0.2f, 0.2f, 1f)),
	INVISIBLELIGHT(12, false, "", true, false, new Color(0.2f, 0.2f, 0.2f, 0f)),
	ASPHALT(13, true, "Asphalt", false, false, new Color(0.12f, 0.12f, 0.12f, 1f)),
	TREEBODY(14, false, "Tree", false, true, new Color(0.2f, 0.11f, 0.04f, 1f)),
	TREELEAVES(15, false, "Tree", false, true, new Color(0f, 0.6f, 0f, 1f)),
	PROCESSEDWOOD(16, true, "Wood", false, false, new Color(0.48f, 0.22f, 0.14f, 1f)),
	VOLCANIC(17, true, "Volcano", false, true, new Color(1f, 0.8f, 0f, 1f)),
	VOLCANIC2(18, true, "Volcano", false, true, new Color(0.15f, 0.15f, 0.15f, 1f)),
	SNOWGRASS(19, true, "Snow", false, true, new Color(0.85f, 0.9f, 1f, 1f)),
	SNOWBRICK(20, true, "Snow Brick", false, false, new Color(0.85f, 0.9f, 1f, 1f)),
	SNOWSTONE(21, true, "Frozen Stone", false, true, new Color(0.48f, 0.8f, 1f, 1f)),
	STREET(22, true, "Street", false, true, new Color(0.15f, 0.15f, 0.15f, 1f)),
	SNOWSTREET(23, true, "Snow Street", false, true, new Color(0.85f, 0.9f, 1f, 1f)),
	DESERT(24, true, "Sand", false, false, new Color(1f, 0.83f, 0.5f, 1f)),
	PLOWEDLAND(25, true, "Plowed Land", false, true, new Color(0.37f, 0.11f, 0f, 1f)),
	SHOPBALCONY(26, false, "Shop Balcony", true, true, new Color(0.25f, 0.17f, 0.1f, 1f)),
	DEADGRASS(27, true, "Dead Grass", false, true, new Color(0.47f, 0.47f, 0.47f, 1f)),
	VINES(28, false, "Vines", true, false, new Color(0f, 0.45f, 0f, 1f));
	protected int id;
	protected boolean collidable;
	protected String name;
	protected boolean lightMaker;
	public static HashMap<Integer, Tile> tileMap;
	protected boolean tileMapped;
	public Color color;
	
	public static final float TILE_SIZE = 64;

	private Tile(int id, boolean collidable, String name, boolean lightMaker, boolean tileMapped, Color color)
	{
		this.id = id;
		this.collidable = collidable;
		this.name = name;
		this.lightMaker = lightMaker;
		this.tileMapped = tileMapped;
		this.color = color;
	}
	
	static
	{
		Load();
	}
	
	public static void Load()
	{
		tileMap = new HashMap<Integer, Tile>();
		for(Tile tile : Tile.values())
		{
			tileMap.put(tile.id, tile);
		}
	}
	
	public static Tile getTileTypeById (int id) {
		return tileMap.get(id);
	}

	public int getId() {
		return id;
	}

	public boolean isCollidable() {
		return collidable;
	}

	public String getName() {
		return name;
	}

	public boolean isTileMapped() {
		return tileMapped;
	}

	public boolean isLightMaker() {
		return lightMaker;
	}
}