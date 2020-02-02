package com.roguelike.world;

import java.util.HashMap;

public enum Background
{
	BRICKS(1, "Bricks", false),
	BRICKS2(2, "Bricks", false),
	OAKPILLAR(3, "Oak Wood", true),
	DOORLB(4, "Door", true),
	DOORLT(5, "Door", true),
	DOORRT(6, "Door", true),
	DOORRB(7, "Door", true),
	ROOFC(8, "Roof", true),
	ROOFL(9, "Roof", true),
	ROOFR(10, "Roof", true),
	HOUSEWALL(11, "Wall", true),
	ROOFT(12, "Roof", true),
	ROOFB(13, "Roof", true),
	CAVE(14, "Cave", true),
	THRASHDARKBUILDINGCENTER(15, "Building", true),
	BRIDGESIDE(16, "Bridge", true),
	SUNFLOWER(17, "Sunflower", true),
	SUNFLOWER2(18, "Sunflower", true),
	BUSH(19, "Bush", true),
	BUSH2(20, "Bush", true),
	HIGHGRASS(21, "High Grass", true),
	BOOKSHELF(22, "Bookshelf", false),
	STAND(23, "Stand", true),
	DANDELIONPOT(24, "Dandelion in a pot", true),
	ROSEPOT(25, "Rose in a pot", true),
	DREAMFRAME(26, "Frame", false),
	STAIRFENCE(27, "Fence", true),
	STAIRFENCE2(28, "Fence", true),
	WOODENBOX(29, "Wooden Box", true),
	STAIRFENCE3(30, "Fence", true),
	STAIRFENCE4(31, "Fence", true),
	CAVESTONE(32, "Cave", false),
	STONEHOUSEWALL(33, "Stone House Wall", false),
	SNOWCAVEBG(34, "Snow Cave Background", false),
	ICEBRICKBG(35, "Ice Brick", false),
	BRICKCHIMNEY(36, "Chimney", true),
	BRICKCHIMNEYTOP(37, "Chimney", true),
	DARKWOOD(38, "Dark Wood", true),
	DARKWOODLEFT(39, "Dark Wood", true),
	DARKWOODRIGHT(40, "Dark Wood", true),
	WHITEWINDOWTOP(41, "Window", true),
	WHITEWINDOWBOT(42, "Window", true),
	WHITESTOP(43, "White Stop", true),
	SNOWROOFLEFT(44, "Snow Roof", true),
	SNOWROOFRIGHT(45, "Snow Roof", true),
	SNOWROOFRIGHTFIX(46, "Snow Roof", true),
	SNOWROOFLEFTFIX(47, "Snow Roof", true),
	PURESNOW(48, "Snow", true),
	PURESNOWRIGHT(49, "Snow", true),
	PURESNOWLEFT(50, "Snow", true),
	DARKWOODDARKER(51, "Dark Wood", true),
	SNOWHIGHGRASS(52, "High Grass", true),
	SNOWHIGHGRASS2(53, "High Grass", true),
	SMALLSTONES(54, "Small Stone", true),
	SMALLSTONES2(55, "Small Stone", true),
	STONEHOUSEWALLDARK(56, "Stone House Wall", true),
	ROOFDARK(57, "Roof", true),
	ROOFDARKSLIDE(58, "Roof", true),
	DEADVEGETATION(59, "Dead Vegetation", true),
	DEADVEGETATION2(60, "Dead Vegetation", true),
	PRETTYCACTUS(61, "Cactus", true),
	FIREPLANT(62, "Fire Plant", true),
	FIREPLANT2(63, "Fire Plant", true),
	CARBONIZEDBONEHEAD(64, "Carbonized Head", true),
	CORN(65, "Corn", true),
	CORN2(66, "Corn", true),
	CORN3(67, "Corn", true),
	CATTLEFENCE(68, "Cattle Fence", true),
	EARTH(69, "Earth", false),
	DARKWOODWALL(70, "Wood Wall", true),
	EMPTYBOOKSHELF(71, "Empty Bookshelf", false),
	BIGWINDOWFRAME(72, "Window", true),
	SMALLWINDOW(73, "Window", true),
	TOMBSTONE(74, "Tombstone", true),
	WOODBARRICADE(75, "Wood Barricade", true),
	DUNGEONDOORGRAY(76, "Dungeon Door", true),
	DUNGEONDOORYELLOW(77, "Dungeon Door", true),
	DUNGEONDOORGREEN(78, "Dungeon Door", true),
	DUNGEONDOORCYAN(79, "Dungeon Door", true),
	DUNGEONDOORBLUE(80, "Dungeon Door", true),
	DUNGEONDOORFUCHSIA(81, "Dungeon Door", true),
	DUNGEONDOORRED(82, "Dungeon Door", true),
	SIGN(83, "Sign", true),
	REWARDBOARD(84, "Reward Board", true),
	ARROWSIGNRIGHT(85, "Arrow Sign", true),
	ARROWSIGNLEFT(86, "Arrow Sign", true),
	DEADHIGHGRASS(87, "Dead High Grass", true),
	DEADSUNFLOWER(88, "Dead Sunflower", true),
	DEADSUNFLOWER2(89, "Dead Sunflower", true),
	DEADBUSH(90, "Dead Bush", true),
	DEADBUSH2(91, "Dead Bush", true),
	DEADCORN(92, "Dead Corn", true),
	DEADCORN2(93, "Dead Corn", true),
	DEADCORN3(94, "Dead Corn", true),
	GHOSTSHIP(95, "Ghost Ship", true),
	VINES(96, "Vines", true),
	JUNGLEWOOD(97, "Jungle Wood", true),
	JUNGLEWOODDARK(98, "Jungle Wood", true),
	LEAFROOFL(99, "Leaves", true),
	LEAFROOFR(100, "Leaves", true),
	LEAFROOFC(101, "Leaves", true),
	LEAFROOFDARKR(102, "Leaves", true),
	LEAFROOFDARKL(103, "Leaves", true),
	LEAFROOFDARKC(104, "Leaves", true),
	ASHLEYFORGERY(105, "Ashley Forgery", true),
	WELL(106, "Well", true),
	BUCKHOUSE(107, "Buck's House", true),
	STGVADVENTURERHOUSE(108, "Stor Gave Adventurer House", true),
	STGVMASTERHOUSE(109, "Stor Gave Master House", true),
	STGVDEFAULTHOUSE(110, "Stor Gave Default House", true),
	STGVWALL(111, "Stor Gave Wall", true),
	ROCKS1(112, "Rocks", true),
	ROCKS2(113, "Rocks", true),
	ROCKS3(114, "Rocks", true),
	STALAGMITES1(115, "Stalagmites", true),
	STALAGMITES2(116, "Stalagmites", true),
	STALAGMITES3(117, "Stalagmites", true),
	STALAGMITES4(118, "Stalagmites", true);
	
	protected int id;
	protected String name;
	protected boolean lightMaker;
	public static HashMap<Integer, Background> tileMap;
	public boolean highPriority = false;
	
	public static final float TILE_SIZE = 64;

	private Background(int id, String name, boolean lightMaker)
	{
		this.id = id;
		this.name = name;
		this.lightMaker = lightMaker;
		this.highPriority = false;
	}
	
	static
	{
		Load();
	}
	
	public static void Load()
	{
		tileMap = new HashMap<Integer, Background>();
		for(Background tile : Background.values())
		{
			tileMap.put(tile.id, tile);
		}
	}
	
	public static Background getTileTypeById (int id) {
		return tileMap.get(id);
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isLightMaker() {
		return lightMaker;
	}
}