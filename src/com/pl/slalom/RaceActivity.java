package com.pl.slalom;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.*;
import com.pl.slalom.player.*;
import com.pl.slalom.player.ai.*;

public class RaceActivity extends Activity
{
	Button btnStartNext;
	TextView tvNext;
	LinearLayout llRacers;
	Competition comp;
	private View[] racerViews;
	private Race race;
	private PlayerRaceResult[] results;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.race);
		
		long id = getIntent().getExtras().getLong(Constants.Extra.CompetitionId, -1);
		if (id == -1){
			this.finish();
			return;
		}
		
		btnStartNext = (Button)findViewById(R.id.race_btnStartNext);
		llRacers = (LinearLayout)findViewById(R.id.race_llPlayers);
		tvNext = (TextView)findViewById(R.id.race_txtNext);
		
		btnStartNext.setEnabled(false);
		
		addHeader();

		try{
			comp = DataManager.getInstance().getCompetitionById(id);
			race = comp.races.get(comp.currentRace);
			results = buildPlayerResults();

			addRacers(comp.competitors.size());
			update();
		}
		catch(Exception ex){
			Toast.makeText(this, "Error RA1: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		update();
		DataManager.getInstance().getStatus().RacePaused = false;
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		if (comp.isStarted() && !comp.isFinished())
			DataManager.getInstance().getStatus().RacePaused = true;
	}
	
	private void update(){
		try{
			//comp = DataManager.getInstance().getCompetition();
			race = comp.races.get(comp.currentRace);
			
			results = buildPlayerResults();
			showData();

			prepareNextTurn();
		}
		catch(Exception ex){
			Toast.makeText(this, "Error RA2: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void doStartNext(View view){
		try{
			int plN = getNextPlayer();
			
			if (plN == -1){
				finish();
				return;
			}
			
			final int compN = results[plN].compN;
			final int runNum = getCurrentRun();
		
			RunData rd = new RunData();
	 		rd.slopeId = race.trackId;
			rd.skiId = comp.competitors.get(compN).skiId;
			// todo: why?
			rd.playerId = Constants.MP_player_id; //DataManager.getInstance().getData().id;
			rd.aiId = comp.competitors.get(compN).ai_id;
			rd.resUpdater = new ResultUpdater(){
				@Override
				public void updateResult(RunResult result){
					race.playerRuns[compN][runNum].runResult = result;
					DataManager.getInstance().updateCompetition(comp);
				}
			};
			int rdId = DataManager.getInstance().pushRunData(rd);
			Intent irun = new Intent(this, GameActivity.class);
			irun.putExtra(Constants.Extra.RunData, rdId);
			irun.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(irun);
		} catch (Exception ex){
			Toast.makeText(this, "Error RA3: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void prepareNextTurn(){
		int np = getNextPlayer();
		if (np == -1){
			btnStartNext.setEnabled(true);
			btnStartNext.setText(R.string.race_eventFinished);
			tvNext.setText(String.format(
				this.getResources().getString(R.string.race_Winner),
				comp.competitors.get(results[0].compN).name)
			);
		} else{
			btnStartNext.setEnabled(true);
			tvNext.setText(String.format(
				this.getResources().getString(R.string.race_Next),
				comp.competitors.get(results[np].compN).name,
				(results[np].runCount + 1)
				)
			);
		}
	}
	
	private int getNextPlayer(){
		int nextR = getCurrentRun();
		
		if (nextR == race.runCount)
			return -1;
			
		for (int i = 0; i< results.length; i++){
			if (results[i].runCount <= nextR)
				return i;
		}
		
		return -1;
	}
	
	private int getCurrentRun(){
		int minRuns = results[0].runCount;
		int maxRuns = results[0].runCount;
		for (int i = 0; i < comp.competitors.size(); i++){
			if (minRuns > results[i].runCount)
				minRuns = results[i].runCount;
			if (maxRuns > results[i].runCount)
				maxRuns = results[i].runCount;
		}
		if (maxRuns == minRuns) 
			return minRuns; // all completed this run, proceed to next run
		
		return minRuns - 1; // we need "run index" instead of "run number" (0 instead of 1)
	}
	
	private void addHeader(){
		View header = getLayoutInflater().inflate(R.layout.race_header, null);
		header.findViewById(R.id.race_hPoints).setVisibility(View.GONE);
		llRacers.addView(header);
	}
	
	private void addRacers(int count){
		racerViews = new View[count];
		
		for (int i = 0; i< count; i++){
			View racer = getLayoutInflater().inflate(R.layout.race_racer, null);
			racer.findViewById(R.id.race_rPoints).setVisibility(View.GONE);
			
			racerViews[i] = racer;
			llRacers.addView(racer);
		}
	}
	
	private void showData(){
		for (int i= 0; i<results.length; i++){
			Competitor c = comp.competitors.get(results[i].compN);
			
			((TextView)racerViews[i].findViewById(R.id.race_rRank))
				.setText("" + results[i].rank);
			((TextView)racerViews[i].findViewById(R.id.race_rName))
				.setText(c.name);
			String rc = results[i].runCount > 0 ? "" + results[i].runCount : "-";
			((TextView)racerViews[i].findViewById(R.id.race_rRuns))
				.setText(rc);
			
			((TextView)racerViews[i].findViewById(R.id.race_rTurns))
				.setText(getResultTxt(results[i]));
			((TextView)racerViews[i].findViewById(R.id.race_rTime))
				.setText(results[i].bestRes == null 
						 ? "" 
						 : "" + results[i].bestRes.time);
		}
	}
	
	private String getResultTxt(PlayerRaceResult result){
		switch (result.overallStatus){
			case NotStarted: 
				return	getResources().getString(R.string.race_notStarted);
			case NotFinished:
				return getResources().getString(R.string.race_notFinished);
			case HasResult:
				return "" + result.bestRes.turns;
			default:
				return "error";
		}
	}
	
	private PlayerRaceResult[] buildPlayerResults(){
		return race.getResults();
	}
}
