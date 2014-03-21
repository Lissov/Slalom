package com.pl.slalom;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.pl.slalom.Controls.*;
import com.pl.slalom.data.*;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.statics.*;
import com.pl.slalom.player.ski.*;
import com.pl.slalom.track.*;
import java.util.*;
import android.content.*;

public class MultiplayerSetupActivity extends Activity implements OnItemSelectedListener
{
	private Spinner spPlCount;
	private int plCount = 0;
	private MpPlayerCreate[] playerViews = new MpPlayerCreate[Constants.MaxMPPlayrs];
	private LinearLayout llPlayers;
	private List<ISki> avSkis; 
	private List<Country> avCountries; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer_setup);
		
		avSkis = DataManager.getInstance().getAvailableSkis();
		avCountries = StaticManager.getCountries();
		
		llPlayers = (LinearLayout)findViewById(R.id.mp_llPlayers);
		spPlCount = (Spinner)findViewById(R.id.mp_spinPlCount);
		spPlCount.setOnItemSelectedListener(this);
		setPlayersCountItems();
		
		setPlayerCount(2);
		setTracks();
		setModes();
		} catch(Exception ex){
			Toast.makeText(this, "Error MPSA3: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void btnStartClick(View view){
		Competition c = new Competition(null);
		c.currentRace = 0;
		
		c.competitors = new LinkedList<Competitor>();
		for (int i = 0; i < plCount; i++){
			Competitor cmpt = playerViews[i].getCompetitor();
			c.competitors.add(cmpt);
		}
		
		c.races = new LinkedList<Race>();
		int runCount = 2;
		Race r = new Race(
			availableSlopes.get(sTrack.getSelectedItemPosition()).id,
			0,
			runCount, ResultMeasureType.TurnsAndTime,
			new RaceRun[plCount][runCount]);
		for (int i = 0; i < plCount; i++){
			for (int j = 0; j < runCount; j++)
			{
				r.playerRuns[i][j] = new RaceRun(RunStatus.NotStarted);
			}
		}
		c.races.add(r);
		
		DataManager.getInstance().dropCompetitions(Constants.CompetitionType.CAREER);
		DataManager.getInstance().insertCompetition(c, Constants.CompetitionType.CAREER);
		
		Intent irace = new Intent(this, RaceActivity.class);
		irace.putExtra(Constants.Extra.CompetitionId, c.id);
		startActivity(irace);				
	}
	
	private List<Slope> availableSlopes;
	private Spinner sTrack;
	private void setTracks(){
		try{
			sTrack = (Spinner)findViewById(R.id.mp_spinTrack);
			availableSlopes = DataManager.getInstance().getAvailableSlopes();
			
			String[] trackNames = new String[availableSlopes.size()];
			for (int i = 0; i<availableSlopes.size(); i++)
				trackNames[i] = SlopeManager.getSlopeName(this, availableSlopes.get(i).id);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_spinner_item,
				trackNames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sTrack.setAdapter(adapter);	
		} catch(Exception ex){
			Toast.makeText(this, "Error MPSA2: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private List<MpMode> modes;	
	private void setModes(){
		try{
			Spinner sMode = (Spinner)findViewById(R.id.mp_spinMode);
			modes = DataManager.getInstance().getMpModes();

			String[] modeNames = new String[modes.size()];
			for (int i = 0; i<modes.size(); i++)
				modeNames[i] = modes.get(i).name;
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, 
				android.R.layout.simple_spinner_item,
				modeNames);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sMode.setAdapter(adapter);	
		} catch(Exception ex){
			Toast.makeText(this, "Error MPSA1: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void setPlayersCountItems(){
		String[] players = new String[Constants.MaxMPPlayrs - 1];
		for (int i = 2; i<=Constants.MaxMPPlayrs; i++)
			players[i-2] = "" + i;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this, 
			android.R.layout.simple_spinner_item,
			players);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spPlCount.setAdapter(adapter);		
	}
	
	private void setPlayerCount(int count){
		for (int i = plCount; i > count; i--){
			llPlayers.removeView(playerViews[i]);
		}
		for (int i = plCount; i<count; i++){
			if (playerViews[i] == null){
				playerViews[i] = new MpPlayerCreate(this, avSkis, avCountries, "Player " + i);
			}
			llPlayers.addView(playerViews[i]);
		}
		plCount = count;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
	{
		//if (view == spPlCount){
			setPlayerCount(pos + 2);
		//}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{
		// TODO: Implement this method
	}
}
