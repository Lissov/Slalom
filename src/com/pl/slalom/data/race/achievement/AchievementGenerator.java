package com.pl.slalom.data.race.achievement;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.achievment.*;
import java.util.*;

public abstract class AchievementGenerator
{
	public abstract List<Achievement> getBetter(
		Competition competition, List<Achievement> existing);
		
	public Achievement getAchievement(List<Achievement> existing, int identifier){
		for (Achievement a : existing){
			if (a.identifier == identifier)
				return a;
		}
		
		return null;
	}
}
