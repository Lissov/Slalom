package com.pl.slalom.data.achievment;

public class CompetitionResult extends Result
{
	public static final int TYPE = 2;

	public int place;

	public CompetitionResult(int place)
	{
		this.place = place;
	}

	@Override
	public int getType()
	{
		return TYPE;
	}
}
