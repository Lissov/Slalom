package com.pl.slalom.data.race;
import java.util.*;

import android.content.Context;
import android.content.res.Resources;

import com.pl.slalom.Constants;
import com.pl.slalom.R;
import com.pl.slalom.data.Data;
import com.pl.slalom.data.DataManager;

public class CompetitionManager
{
	private Resources r;
	
	private CompetitionDef getComp(int id, int[] slopeIds, int[] runCounts, int nameRes, int descRes){
		return new CompetitionDef(id, slopeIds, runCounts, r.getString(nameRes), r.getString(descRes));
	}
	
	public List<CompetitionDef> getAllCompetitions(Context context){
		r = context.getResources();
				
		List<CompetitionDef> result = new LinkedList<CompetitionDef>();
		
		result.add(getComp(
				Constants.Events.Austria.evt_quali_1, 
				new int[] {Constants.Slopes.Austria.hohewandwiese},
				new int[] {1},
				R.string.evt_au_quali1_name, R.string.evt_au_quali1_desc
				));
		
		result.add(getComp(
				Constants.Events.Austria.evt_locchamp_1, 
				new int[] {Constants.Slopes.Austria.hohewandwiese},
				new int[] {2},
				R.string.evt_au_champ1_name, R.string.evt_au_champ1_desc
				));
		
		
		return result;
	}
	
	public Competition createFullCompetition(CompetitionDef def){
		Data d = DataManager.getInstance().getData();
		Competition result = new Competition();
		
		result.currentRace = 0;
		result.competitors = new LinkedList<Competitor>();
		result.competitors.add(new Competitor(d.getFullName(), d.selectedSkiId, d.countryId, Constants.AIID_Human, 0));
		int plCount = result.competitors.size();
		
		result.races = new LinkedList<Race>();
		int i = 0;
		for (int slopeId : def.tracks){
			int rc = def.runCounts[i];
			result.races.add(new Race(slopeId, i, rc, new RaceRun[plCount][rc]));
			i++;
		}
		
		result.currentRace = 0;
		
		return result;
	}
}
