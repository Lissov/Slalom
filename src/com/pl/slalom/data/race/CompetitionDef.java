package com.pl.slalom.data.race;

public class CompetitionDef
{
	public int id;

	public int[] tracks;
	public int[] runCounts;
	public String name;
	public String description;
	
	public boolean isAvailable;
	public String trackNames;

	public CompetitionDef(int id, int[] tracks, int[] runCounts, String name, String description)
	{
		this.id = id;
		this.tracks = tracks;
		this.runCounts = runCounts;
		this.name = name;
		this.description = description;
	}
}
