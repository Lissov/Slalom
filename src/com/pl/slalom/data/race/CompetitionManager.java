package com.pl.slalom.data.race;
import java.util.*;

import android.content.Context;
import android.content.res.Resources;

import com.pl.slalom.Constants;
import com.pl.slalom.R;
import com.pl.slalom.data.Data;
import com.pl.slalom.data.DataManager;
import com.pl.slalom.data.achievment.AchievementManager;
import com.pl.slalom.data.achievment.SlopeResult;
import com.pl.slalom.data.race.achievement.*;

public class CompetitionManager
{
	private Resources r;
	
	private CompetitionDef getComp(int id, CompetitionType type,
								   int[] slopeIds, int[] runCounts,
								   ResultMeasureType[] resTypes,
								   int nameRes, int descRes,
								   int lockedExplainationRes,
								   CompetitionAvailCalc availCalc,
								   AchievementGenerator[] achievGens)
	{
		return new CompetitionDef(id, type, slopeIds, runCounts, resTypes, 
								  r.getString(nameRes), r.getString(descRes),
								  r.getString(lockedExplainationRes),
								  availCalc, achievGens);
	}
	
	public List<CompetitionDef> getAllCompetitions(Context context){
		List<CompetitionDef> result = new LinkedList<CompetitionDef>();
		
		result.add(getCompetitionDef(context, Constants.Events.Austria.evt_quali_1));
		result.add(getCompetitionDef(context, Constants.Events.Austria.evt_locchamp_1));
		
		return result;
	}

	public CompetitionDef getCompetitionDef(Context context, int competitionId)
	{
		r = context.getResources();

		switch (competitionId)
		{
			case Constants.Events.Austria.evt_quali_1:
				return getComp(
					Constants.Events.Austria.evt_quali_1,
					CompetitionType.Qualification,
					new int[] {Constants.Slopes.Austria.hohewandwiese},
					new int[] {1},
					new ResultMeasureType[] {ResultMeasureType.Turns},
					R.string.evt_au_quali1_name, R.string.evt_au_quali1_desc,
					R.string.evt_au_quali1_locked,
					new CompetitionAvailCalc(){
						public boolean isAvailable()
						{ return true; }
					},
					new AchievementGenerator[] { 
						new SlopeAchievementGenerator() 
					}
				);
			case Constants.Events.Austria.evt_locchamp_1: 
				return getComp(
					Constants.Events.Austria.evt_locchamp_1,
					CompetitionType.LocalChamp,
					new int[] {Constants.Slopes.Austria.hohewandwiese},
					new int[] {2},
					new ResultMeasureType[] {ResultMeasureType.TurnsAndTime},
					R.string.evt_au_champ1_name, R.string.evt_au_champ1_desc,
					R.string.evt_au_champ1_locked,
					new CompetitionAvailCalc(){
						public boolean isAvailable()
						{ 
							SlopeResult sr = AchievementManager.getSlopeAchievement(Constants.Slopes.Austria.hohewandwiese);
							return sr != null && sr.turns <= 18;
						}
					},
					new AchievementGenerator[] { 
						new SlopeAchievementGenerator(), 
						new CompetitionAchievementGenerator() 
					}
				);
			default:
				return null;
		}
	}
	
	public Competition createFullCompetition(CompetitionDef def){
		Data d = DataManager.getInstance().getData();
		Competition result = new Competition(def);
		
		result.currentRace = 0;
		result.competitors = new LinkedList<Competitor>();
		result.competitors.add(new Competitor(d.getFullName(), d.selectedSkiId, d.countryId, Constants.AIID_Human, 0));
		int plCount = result.competitors.size();
		
		result.races = new LinkedList<Race>();
		int i = 0;
		for (int slopeId : def.tracks){
			int rc = def.runCounts[i];
			ResultMeasureType rt = def.resultTypes[i];
			Race r = new Race(slopeId, i, rc, rt, new RaceRun[plCount][rc]);
			for (int p = 0; p < plCount; p++){
				for (int rn = 0; rn < rc; rn++)
					r.playerRuns[p][rn] = new RaceRun(RunStatus.NotStarted);
			}
			result.races.add(r);
			i++;
		}
		
		result.currentRace = 0;
		
		return result;
	}
}
