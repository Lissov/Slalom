package com.pl.slalom.data.achievment;

public class SlopeResult extends Result
{
	public static final int TYPE = 1;
	
	public int turns;
	public float time;

	public SlopeResult(int turns, float time)
	{
		this.turns = turns;
		this.time = time;
	}

	@Override
	public int getType()
	{
		return TYPE;
	}
}
