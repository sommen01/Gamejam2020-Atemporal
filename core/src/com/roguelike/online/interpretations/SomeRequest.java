package com.roguelike.online.interpretations;

import java.util.ArrayList;

public class SomeRequest {
	public ArrayList<String> text = new ArrayList<String>();

	public SomeRequest(String text)
	{
		this.text.add(text);
	}
	
	public SomeRequest() {}
}
