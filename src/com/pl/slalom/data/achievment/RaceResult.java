package com.pl.slalom.data.achievment;

public class RaceResult extends Result
{
	public static final int TYPE = 1;
	
	public int turns;
	public float time;
	public int place;

	@Override
	public int getType()
	{
		return TYPE;
	}
}
