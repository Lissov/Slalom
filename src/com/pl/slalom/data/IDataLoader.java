package com.pl.slalom.data;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.achievment.*;
import java.util.*;

public interface IDataLoader
{
	Data loadData();
	
	void storeData(Data data);
	
	void deleteCompetition(Competition competition);
	void insertCompetition(long playerId, Competition competition, int competitionType);
	void updateCompetition(Competition competition);
	
	Competition getCompetitionByType(long playerId, int competitionType);
	Competition getCompetitionById(long competitionId);

	List<Achievement> getAllAchievements(long playerId);
	void storeAchievement(long playerId, Achievement a);
}
