package com.pl.slalom.career;

import com.pl.slalom.Constants;
import com.pl.slalom.MultiplayerSetupActivity;
import com.pl.slalom.R;
import com.pl.slalom.RaceActivity;
import com.pl.slalom.data.DataManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.pl.slalom.Controls.Adapters.*;
import com.pl.slalom.data.race.*;

import android.view.View.*;
import android.view.*;

public class CareerEventsActivity extends Activity {
	private CompetitionDef[] events;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.career_events);

		Competition c = DataManager.getInstance().getCompetitionByType(Constants.CompetitionType.CAREER);
		if (c != null)
		{
			if (c.isFinished()){
				processCompetitionFinished(c);
			} else {
				Intent irace = new Intent(this, RaceActivity.class);
				irace.putExtra(Constants.Extra.CompetitionId, c.id);
				startActivity(irace);
				return;
			}
		}
		
		try
		{
			events = 
				DataManager
				.getInstance()
				.getAvailableCompetitions()
				.toArray(new CompetitionDef[0]);
			ListView lvEvents = (ListView)findViewById(R.id.career_events_lv);
			lvEvents.setAdapter(new CareerCompetitionAdapter(this, events));
			
			lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener(){
				  @Override
				  public void onItemClick(AdapterView<?> parent, View view,
				    int position, long id) {
					  	startCompetition((int)id);
				  }
				}); 
		}
		catch (Exception ex)
		{
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}	
	
	private void processCompetitionFinished(Competition comp){
		
		DataManager.getInstance().dropCompetitions(Constants.CompetitionType.CAREER);
	}
	
	private Competition getCompetition(int competitionId){
		for (int i = 0; i < events.length; i++){
			if (events[i].id == competitionId && events[i].isAvailable)
			{
				return new CompetitionManager().createFullCompetition(events[i]);
			}
		}
		
		return null;
	}
	private void startCompetition(int competitionId){
		Competition c = getCompetition(competitionId);
		if (c == null)
			return;

		DataManager.getInstance().dropCompetitions(Constants.CompetitionType.CAREER);
		DataManager.getInstance().insertCompetition(c, Constants.CompetitionType.CAREER);
		
		Intent irace = new Intent(this, RaceActivity.class);
		irace.putExtra(Constants.Extra.CompetitionId, c.id);
		startActivity(irace);
	}
}
