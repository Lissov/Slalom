package com.pl.slalom.track;

public class Slope
{
	public int id;	
	public String name;
	
	public int width;
	public Gate[] gates;
	public Tramplin[] tramplins;
	
	public int startPos;

	public Slope(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
}

