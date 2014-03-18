package com.pl.slalom.data.race;

public class CompetitionDef
{
	public int id;

	public CompetitionType type;
	public int[] tracks;
	public int[] runCounts;
	public String name;
	public String description;
	public String lockedExplaination;
	
	public boolean isAvailable;
	public String trackNames;
	public CompetitionAvailCalc availabilityCalc;

	public CompetitionDef(int id, CompetitionType type, 
					      int[] tracks, int[] runCounts, 
						  String name, String description,
						  String lockedExplaination,
						  CompetitionAvailCalc availabilityCalc)
	{
		this.id = id;
		this.type = type;
		this.tracks = tracks;
		this.runCounts = runCounts;
		this.name = name;
		this.description = description;
		this.lockedExplaination = lockedExplaination;
		this.availabilityCalc = availabilityCalc;
	}
}
