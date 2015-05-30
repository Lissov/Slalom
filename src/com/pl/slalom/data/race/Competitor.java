package com.pl.slalom.data.race;

public class Competitor
{
	public long id;
	public String name;
	public int skiId;
	public int countryId;
	public int ai_id;
	public int points;
	
	public Competitor(String name, int skiId, int countryId, int ai_id, int points)
	{
		this.name = name;
		this.skiId = skiId;
		this.countryId = countryId;
		this.ai_id = ai_id;
		this.points = points;
	}
}
