package com.pl.slalom.data.race.achievement;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.achievment.*;
import java.util.*;

public abstract class AchievementGenerator
{
	public abstract List<Achievement> getBetter(
		Competition competition, List<Achievement> existing);
}
