package com.pl.slalom.career;

import com.pl.slalom.R;
import com.pl.slalom.data.DataManager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.*;
import com.pl.slalom.Controls.Adapters.*;
import com.pl.slalom.data.race.*;
import android.view.View.*;
import android.view.*;

public class CareerEventsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.career_events);

		try
		{
			CompetitionDef[] events = 
				DataManager
				.getInstance()
				.getAvailableCompetitions()
				.toArray(new CompetitionDef[0]);
			ListView lvEvents = (ListView)findViewById(R.id.career_events_lv);
			lvEvents.setAdapter(new CareerCompetitionAdapter(this, events));
		}
		catch (Exception ex)
		{
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}	
}
