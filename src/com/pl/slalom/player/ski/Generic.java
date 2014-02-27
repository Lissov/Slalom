package com.pl.slalom.player.ski;

public class Generic implements ISki
{
	private int id;
	private String name;
	private int price;
	private SkiParameters skiParameters; 
	
	
	public Generic(int id, String name, int price, SkiParameters parameters)
	{
		this.id = id;
		this.name = name;
		this.skiParameters = parameters;
		this.price = price;
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public int getPrice()
	{
		return price;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public SkiParameters getSkiParameters() {
		return skiParameters;
	}
}
