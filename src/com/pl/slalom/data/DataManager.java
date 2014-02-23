package com.pl.slalom.data;
import com.pl.slalom.*;
import com.pl.slalom.player.ski.*;
import android.content.*;
import com.pl.slalom.data.database.*;
import java.util.*;
import com.pl.slalom.track.*;
import android.widget.*;
import com.pl.slalom.data.race.*;

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
		
	public List<ISki> getAvailableSkis(){
		List<ISki> result = new LinkedList<ISki>();
		SkiManager sm = new SkiManager();
		for (int i = 0; i< data.availableSkiIds.size(); i++){
			result.add(sm.getSki(data.availableSkiIds.get(i)));
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
	
	public Competition getCompetition(){
		try{
			return dataLoader.getCompetition(data.id);
		} catch(Exception ex){
			Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
			return null;
		}
	}

	public void storeCompetition(Competition value, boolean isnew){
		try{
			if (isnew){
				dataLoader.insertCompetition(data.id, value);
			}
		
			dataLoader.updateCompetition(value);
		} catch(Exception ex){
			Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void dropAllCompetitions(){
		Competition c = dataLoader.getCompetition(data.id);
		while (c != null){
			dataLoader.deleteCompetition(c);
			c = dataLoader.getCompetition(data.id);
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
}

