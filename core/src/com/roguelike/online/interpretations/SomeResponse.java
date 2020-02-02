package com.roguelike.online.interpretations;

import java.util.ArrayList;

public class SomeResponse {
	public ArrayList<String> text = new ArrayList<String>();
	
	public SomeResponse() {}
	
	public SomeResponse(String text)
	{
		this.text.add(text);
	}
	
	public SomeResponse(SomeRequest request)
	{
		this.text.addAll(request.text);
	}
}
