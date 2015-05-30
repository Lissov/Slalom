package com.pl.slalom.data.race;
import java.util.*;
import com.pl.slalom.*;
import com.pl.slalom.data.race.achievement.*;
import com.pl.slalom.player.ai.*;

public class Competition
{
	public long id;
	public CompetitionDef definition;
	public List<Competitor> competitors;
	public List<Race> races;
	
	public Competition(CompetitionDef def){
		definition = def;
	}
	
	public int currentRace;
	
	public boolean isFinished(){
		return currentRace == races.size()-1 
				&& races.get(currentRace).isFinished();
	}
	
	public boolean isStarted(){
		return currentRace > 0
			|| races.get(currentRace).isStarted();
	}
	
	public int getPlayerNum(){
		for (int i = 0; i < competitors.size(); i++){
			if (competitors.get(i).ai_id == PlayerManager.AIID_Human)
				return i;
		}
		return -1;
	}
	
	public int getPlayerPosition(int playerNum){
		int better = 0;
		int points = competitors.get(playerNum).points;
		for (int i = 0; i < competitors.size(); i++){
			if (competitors.get(i).points > points)
				better++;
		}
		
		return better + 1;
	}
}
