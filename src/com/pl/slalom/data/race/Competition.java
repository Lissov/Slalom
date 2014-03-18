package com.pl.slalom.data.race;
import java.util.*;

public class Competition
{
	public long id;
	public List<Competitor> competitors;
	public List<Race> races;
	
	public int currentRace;
	
	public boolean isFinished(){
		return currentRace == races.size()-1 
				&& races.get(currentRace).isFinished();
	}
	
	public boolean isStarted(){
		return currentRace > 0 
			|| races.get(currentRace).isStarted();
	}
}
