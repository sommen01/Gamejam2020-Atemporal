package com.roguelike.world.loader;

public class GenerationData
{
	public int[] lastId = new int[100];
	
	public int[] toArray()
	{
		int[] array = new int[100];
		for(int i = 0;i < 100;i++)
		{
			array[i] = lastId[i];
		}
		return array;
	}
	
}