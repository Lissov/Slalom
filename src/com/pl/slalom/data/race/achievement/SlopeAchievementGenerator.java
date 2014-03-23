package com.pl.slalom.data.race.achievement;
import java.util.*;
import com.pl.slalom.*;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.achievment.*;
import com.pl.slalom.Utility.*;

public class SlopeAchievementGenerator extends AchievementGenerator
{
	@Override
	public List<Achievement> getBetter(
		Competition competition, List<Achievement> existing)
	{
		List<Achievement> result = new LinkedList<Achievement>();

		int plN = competition.getPlayerNum();
		for (Race r : competition.races)
		{
			int achieveId = r.trackId + Constants.Achievement.SlopeBase;

			RunResult bestr = r.getBestResult(plN);
			if (bestr.status != RunStatus.Finished)
				continue;

			SlopeResult sr = new SlopeResult(bestr.turns, bestr.time);

			Achievement ex = AchievementManager.getAchievement(existing, achieveId);
	
			if (isBetter(sr, ex, r)){
				if (ex == null){
					ex = new Achievement();
					ex.identifier = achieveId;
				}
				ex.result = sr;
				result.add(ex);
			}
		}

		return result;
	}
	
	private boolean isBetter(SlopeResult sr, Achievement a, Race r){
		if (a == null) 
			return true;
		SlopeResult srA = (SlopeResult)a.result;
			
		RunResult rr1 = new RunResult(sr.turns, sr.turns, RunStatus.Finished);
		RunResult rr2 = new RunResult(srA.turns, srA.turns, RunStatus.Finished);
		return 
			RunResultComparator
			.getComparator(r.resultType)
			.compare(rr1, rr2) < 0;
	}
}
