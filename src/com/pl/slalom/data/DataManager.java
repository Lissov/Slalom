package com.pl.slalom.data;
import com.pl.slalom.*;
import com.pl.slalom.player.ski.*;
import android.content.*;
import com.pl.slalom.data.database.*;
import java.util.*;

import com.pl.slalom.track.*;
import android.widget.*;
import com.pl.slalom.data.race.*;
import com.pl.slalom.player.*;
import com.pl.slalom.data.achievment.*;

public class DataManager
{
	private static DataManager instance = null;
	private Data data = null;
	private IDataLoader dataLoader;
	private Context context;
	
	public static DataManager getInstance(){
		if (instance == null){
			instance = new DataManager();
		}
			
		return instance;
	}
	
	public void Init(Context context){
		if (data != null)
			return;
		
		dataLoader = new SqlLiteDataLoader(context);
		data = dataLoader.loadData();
		this.context = context;
	}
	
	public Data getData(){
		return data;	
	}
	
	public void storeData(){
		dataLoader.storeData(data);
	}
	
		
	public List<ISki> getAvailableSkis(){
		List<ISki> result = new LinkedList<ISki>();
		SkiManager sm = new SkiManager();
		if (Constants.allUnlocked){
			for (int i : sm.AllSkiIds){
				result.add(sm.getSki(i));
			}
		} else {
			for (int i = 0; i< data.availableSkiIds.size(); i++){
				result.add(sm.getSki(data.availableSkiIds.get(i)));
			}
		}
		return result;
	}

	public List<Slope> getAvailableSlopes(){
		List<Slope> result = new LinkedList<Slope>();
		SlopeManager sm = new SlopeManager();
		for (int i = 0; i< data.availableTrackIds.size(); i++){
			Slope s = sm.getSlope(data.availableTrackIds.get(i));
			result.add(s);
		}
		return result;
	}
	
	public List<MpMode> getMpModes(){
		List<MpMode> result = new LinkedList<MpMode>();
		
		result.add(new MpMode(Constants.MP_ModeTurns, context.getResources().getString(R.string.mp_modeTurns)));
		result.add(new MpMode(Constants.MP_ModeTime, context.getResources().getString(R.string.mp_modeTime)));
		
		return result;
	}
	
	public Competition getCompetitionById(long id){
		Competition c = dataLoader.getCompetitionById(id);
		return c;		
	}
	
	public Competition getCompetitionByType(int competitionType){
		try{
			Competition c = dataLoader.getCompetitionByType(data.id, competitionType);
			return c;
		} catch(Exception ex){
			Toast.makeText(context, "Error DM1: " + ex.getMessage(), Toast.LENGTH_LONG).show();
			return null;
		}
	}

	public void insertCompetition(Competition value, int competitionType){
		try{
			dataLoader.insertCompetition(data.id, value, competitionType);
		
			dataLoader.updateCompetition(value);
		} catch(Exception ex){
			Toast.makeText(context, "Error DM2: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	public void updateCompetition(Competition value){
		dataLoader.updateCompetition(value);		
	}
	
	public void dropCompetitions(int competitionType){
		Competition c = dataLoader.getCompetitionByType(data.id, competitionType);
		while (c != null){
			dataLoader.deleteCompetition(c);
			c = dataLoader.getCompetitionByType(data.id, competitionType);
		}
	}
	
	private int rdC = 0;
	private Hashtable<Integer, RunData> runData = new Hashtable<Integer, RunData>();
	public int pushRunData(RunData data){
		rdC++;
		runData.put(rdC, data);
		return rdC;
	}
	public RunData getRunData(int key){
		return runData.get(key);
	}
	
	public PlayerSkills getPlayerSkills(long playerId) throws Exception {
		if (playerId == Constants.MP_player_id){
			PlayerSkills s =  new PlayerSkills();
			s.strAcc_k = 1f;
			s.strSkiMaxV_k = 1f;
			s.uphillSkiMaxV_k= 1f; 
			s.startSpeedK = 1f;
			s.speedHandling = 20;
			s.skiControl = 0;
			return s;
		}
		
		if (playerId != data.id)
			throw new Exception("Can't get player id for player that is not a current player");
			
		PlayerSkills s =  new PlayerSkills();
		s.strAcc_k = 0.5f;
		s.strSkiMaxV_k = 0.1f;	// means it can only make 1 step
		s.uphillSkiMaxV_k= 0.5f; 
		s.startSpeedK = 0.5f;
		s.speedHandling = 0;
		s.skiControl = 0;		// not better than ski defaults
		return s;
	}
	
	public int getNextExpLevel(int experience){
		int i = 0;
		while (Constants.experienceLevels[i] < experience) i++;
		
		return i + 1;
	}
	
	public int getNextExpRequired(int experience){
		return (Constants.experienceLevels[getNextExpLevel(experience)-1]);
	}
	
	public List<CompetitionDef> getAvailableCompetitions(){
		Hashtable<Integer, CompetitionDef> competitions = new Hashtable<Integer, CompetitionDef>();		
		List<CompetitionDef> comps = new CompetitionManager().getAllCompetitions(context);
		
		for (CompetitionDef cd : comps){
			cd.isAvailable = cd.availabilityCalc.isAvailable();
			competitions.put(cd.id, cd); 
		}
		
		//competitions.get(Constants.Events.Austria.evt_quali_1).isAvailable = true;
		
		return new LinkedList<CompetitionDef>(competitions.values());
	}
	
	private ProgramStatus status;
	public ProgramStatus getStatus(){
		if (status == null)
			status = new ProgramStatus();
		
		return status;
	}
	
	public List<Achievement> getAchievements(){
		return dataLoader.getAllAchievements(data.id);
	}
	
	public void storeAchievement(Achievement a){
		dataLoader.storeAchievement(data.id, a);
	}
}

