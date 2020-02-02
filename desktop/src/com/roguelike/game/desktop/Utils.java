package com.roguelike.game.desktop;

public class Utils {
	public static boolean isInArray(String[] array, String item)
{
	for(String z : array)
	{
		if(item.equalsIgnoreCase(z))
		{
			return true;
		}
	}
	return false;
}

public static boolean isInArray(int[] array, int item)
{
	for(int z : array)
	{
		if(item == z)
		{
			return true;
		}
	}
	return false;
}

public static boolean isInArray(float[] array, float item)
{
	for(float z : array)
	{
		if(item == z)
		{
			return true;
		}
	}
	return false;
}

public static boolean isInArray(double[] array, double item)
{
	for(double z : array)
	{
		if(item == z)
		{
			return true;
		}
	}
	return false;
}

public static boolean isInArray(long[] array, long item)
{
	for(long z : array)
	{
		if(item == z)
		{
			return true;
		}
	}
	return false;
}

}
