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
import android.app.*;
import android.content.*;

public class CareerEventsActivity extends Activity {
	private CompetitionDef[] events;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.career_events);

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
					  	processCompetition((int)id);
				  }
				}); 
		}
		catch (Exception ex)
		{
			Toast.makeText(this, "Error CEA1: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}	
		
	private CompetitionDef getCompetitionDef(long competitionId){
		for (int i = 0; i < events.length; i++){
			if (events[i].id == competitionId)
			{
				return events[i];
			}
		}
		return null;		
	}
	
	private Competition getCompetition(long competitionId){
		for (int i = 0; i < events.length; i++){
			if (events[i].id == competitionId)
			{
				return new CompetitionManager().createFullCompetition(events[i]);
			}
		}
		
		return null;
	}
	
	private void processCompetition(long competitionId){
		CompetitionDef cdef = getCompetitionDef(competitionId);
		if (cdef == null)
			return;
			
		if (cdef.isAvailable){
			startCompetition(competitionId);
		} else {
			showCompetitionUnavailable(cdef);
		}
	}
	
	private void showCompetitionUnavailable(CompetitionDef cdef){
		AlertDialog d = new AlertDialog.Builder(this).create();
		d.setTitle(R.string.career_evt_locked_expl);
		d.setMessage(cdef.lockedExplaination);
		d.setButton(
			AlertDialog.BUTTON_POSITIVE,
			getResources().getString(R.string.message_ok),
			new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface d, int p){}
			});
		d.setCancelable(true);
		d.show();
	}
	
	private void startCompetition(long competitionId)
	{
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
