package com.pl.slalom.data;
import android.content.*;
import com.pl.slalom.data.race.*;

public interface IDataLoader
{
	Data loadData();
	
	void storeData(Data data);
	
	void deleteCompetition(Competition competition);
	void insertCompetition(long playerId, Competition competition);
	void updateCompetition(Competition competition);
	
	Competition getCompetition(long playerId);
}
