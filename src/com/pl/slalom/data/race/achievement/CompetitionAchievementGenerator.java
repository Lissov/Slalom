package com.pl.slalom.data.race.achievement;
import java.util.*;
import com.pl.slalom.*;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.achievment.*;

public class CompetitionAchievementGenerator extends AchievementGenerator
{
	@Override
	public List<Achievement> getBetter(
		Competition competition, List<Achievement> existing)
	{
		List<Achievement> result = new LinkedList<Achievement>();
		
		int plN = competition.getPlayerNum();
		int achieveId = competition.definition.id + Constants.Achievement.CompetitionBase;
		
		int position = competition.getPlayerPosition(plN);
		Achievement a = AchievementManager.getAchievement(existing, achieveId);
		if (a == null){
			a = new Achievement();
			a.identifier = achieveId;
			a.result = new CompetitionResult(position);
			result.add(a);
		} else {
			CompetitionResult cr = (CompetitionResult)a.result;
			if (cr.place > position)
				cr.place = position;
			result.add(a);
		}
		
		return result;
	}
}
