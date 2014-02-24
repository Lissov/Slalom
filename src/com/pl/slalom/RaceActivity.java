package com.pl.slalom;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.*;
import java.util.*;
import com.pl.slalom.Utility.*;

public class RaceActivity extends Activity
{
	Button btnStartNext;
	TextView tvNext;
	LinearLayout llRacers;
	Competition comp;
	private View[] racerViews;
	private Race race;
	private PlayerResult[] results;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.race);
		
		btnStartNext = (Button)findViewById(R.id.race_btnStartNext);
		llRacers = (LinearLayout)findViewById(R.id.race_llPlayers);
		tvNext = (TextView)findViewById(R.id.race_txtNext);
		
		btnStartNext.setEnabled(false);
		
		addHeader();

		try{
			comp = DataManager.getInstance().getCompetition();		
			race = comp.races.get(comp.currentRace);
			results = buildPlayerResults();

			addRacers(comp.competitors.size());
			update();
		}
		catch(Exception ex){
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		update();
	}
	
	private void update(){
		try{
			//reload may be redundant
			comp = DataManager.getInstance().getCompetition();		
			race = comp.races.get(comp.currentRace);
			
			results = buildPlayerResults();
			showData();

			prepareNextTurn();
		}
		catch(Exception ex){
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void doStartNext(View view){
		try{
			int plN = getNextPlayer();
			final int compN = results[plN].compN;
			final int runNum = getCurrentRun();
		
			RunData rd = new RunData();
	 		rd.slopeId = race.trackId;
			rd.skiId = comp.competitors.get(compN).skiId;
			rd.resUpdater = new ResultUpdater(){
				@Override
				public void updateResult(RunResult result){
					race.playerRuns[compN][runNum].runResult = result;
					DataManager.getInstance().storeCompetition(comp, false);
				}
			};
			int rdId = DataManager.getInstance().pushRunData(rd);
			Intent irun = new Intent(this, GameActivity.class);
			irun.putExtra(Constants.Extra_RunData, rdId);
			irun.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(irun);
		} catch (Exception ex){
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void prepareNextTurn(){
		int np = getNextPlayer();
		if (np == -1){
			btnStartNext.setEnabled(false);
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
				.setText(results[i].bestRes == null 
							? results[i].resTxt 
							: "" + results[i].bestRes.turns);
			((TextView)racerViews[i].findViewById(R.id.race_rTime))
				.setText(results[i].bestRes == null 
						 ? "" 
						 : "" + results[i].bestRes.time);
		}
	}
	
	private PlayerResult[] buildPlayerResults(){
		PlayerResult[] res = new PlayerResult[comp.competitors.size()];
		
		for (int i = 0; i<comp.competitors.size(); i++){
			res[i] = new PlayerResult();
			res[i].compN = i;
			res[i].bestRes = getBestResult(i);
			res[i].runCount = getRunCount(i);
			if (res[i].bestRes == null){
				if (res[i].runCount == 0)
					res[i].resTxt = getResources().getString(R.string.race_notStarted);
				else
					res[i].resTxt = getResources().getString(R.string.race_notFinished);
			}
		}
		
		Arrays.sort(res, getComparator());
		for (int i = 0; i<comp.competitors.size(); i++){
			res[i].rank = i + 1;
		}
		return res;
	}
	
	private RunResult getBestResult(int cNum){
		RunResult bestRes = null;
		
		for (int r = 0; r < race.runCount; r++){
			if (race.playerRuns[cNum][r].runResult.status == RunStatus.NotStarted){
				break;
			}
			if (race.playerRuns[cNum][r].runResult.status == RunStatus.Finished){
				if (bestRes == null 
					|| RunResultComparator
						.getComparator()
						.compare(bestRes, 
							race.playerRuns[cNum][r].runResult
						) > 0
					)
				{
					bestRes = race.playerRuns[cNum][r].runResult;
				}
			}
		}
		
		return bestRes;
	}
	
	private int getRunCount(int cNum){
		int runC = 0;
		while (runC < race.runCount &&
				race.playerRuns[cNum][runC].runResult.status != RunStatus.NotStarted){
			runC++;
		}
		return runC;
	}
		
	private static Comparator<PlayerResult> getComparator(){
		return new Comparator<PlayerResult>(){
			public int compare(PlayerResult pr1, PlayerResult pr2){
				if (pr1.bestRes == null && pr2.bestRes == null)
					return Integer.valueOf(pr1.compN).compareTo(pr2.compN);
				if (pr1.bestRes == null) return +1;
				if (pr2.bestRes == null) return -1;
				return RunResultComparator.getComparator().compare(
					pr1.bestRes, pr2.bestRes);
			}
		};
	}
	
	class PlayerResult {
		public int compN;
		public int rank;
		public RunResult bestRes;
		public int runCount;
		public String resTxt;
	}
}
