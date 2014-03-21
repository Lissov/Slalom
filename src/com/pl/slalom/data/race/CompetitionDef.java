package com.pl.slalom.data.race;
import com.pl.slalom.data.race.achievement.*;

public class CompetitionDef
{
	public int id;
	
	public CompetitionType type;
	public int[] tracks;
	public int[] runCounts;
	public ResultMeasureType[] resultTypes;
	public String name;
	public String description;
	public String lockedExplaination;
	
	public boolean isAvailable;
	public String trackNames;
	public CompetitionAvailCalc availabilityCalc;
	public AchievementGenerator[] achieveGens;

	public CompetitionDef(int id, CompetitionType type,
					      int[] tracks, int[] runCounts,
						  ResultMeasureType[] resultTypes,
						  String name, String description,
						  String lockedExplaination,
						  CompetitionAvailCalc availabilityCalc,
						  AchievementGenerator[] achieveGens)
	{
		this.id = id;
		this.type = type;
		this.tracks = tracks;
		this.runCounts = runCounts;
		this.resultTypes = resultTypes;
		this.name = name;
		this.description = description;
		this.lockedExplaination = lockedExplaination;
		this.availabilityCalc = availabilityCalc;
		this.achieveGens = achieveGens;
	}
}
