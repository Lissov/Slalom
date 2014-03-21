package com.pl.slalom.career;
import com.pl.slalom.*;
import android.app.*;
import android.os.*;
import android.support.v4.app.FragmentActivity;

import com.pl.slalom.data.*;
import android.content.*;
import android.content.res.Resources;
import android.widget.*;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.race.achievement.*;
import com.pl.slalom.data.achievment.*;
import java.util.*;

public class CareerActivity extends TabActivity implements OnTabChangeListener
{
	Data data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.career);

		data = DataManager.getInstance().getData();
		showGeneral();
		
		TabHost tabHost = getTabHost();
		Resources r = getResources();
		
		TabSpec expSpec = tabHost.newTabSpec("Experience");
		expSpec.setIndicator(r.getString(R.string.career_tabExperience), r.getDrawable(R.drawable.icon_career_exp_tab));
		expSpec.setContent(new Intent(this, CareerExperienceActivity.class));
		
		TabSpec skiSpec = tabHost.newTabSpec("Ski");
		skiSpec.setIndicator(r.getString(R.string.career_tabSki), r.getDrawable(R.drawable.icon_career_ski_tab));
		skiSpec.setContent(new Intent(this, CareerSkiActivity.class));
		
		TabSpec eventsSpec = tabHost.newTabSpec("Experience");
		eventsSpec.setIndicator(r.getString(R.string.career_tabEvents), r.getDrawable(R.drawable.icon_career_cmp_tab));
		eventsSpec.setContent(new Intent(this, CareerEventsActivity.class));
		
		tabHost.addTab(expSpec);
		tabHost.addTab(skiSpec);
		tabHost.addTab(eventsSpec);
		
		if (data.experience < 0){
			startCareer();
		}

	}
	
	private void processCompetitionFinished(Competition comp){		
		CompetitionDef def = comp.definition;
		if (def != null){
			for (AchievementGenerator gen : def.achieveGens)
			{
				List<Achievement> achievements = 
					gen.getBetter(comp, DataManager.getInstance().getAchievements());

				for (Achievement a : achievements)
				{
					DataManager.getInstance().storeAchievement(a);
				}

				for (Achievement a : achievements)
				{
					Toast.makeText(
						this, 
						AchievementManager.getString(this, a), 
						Toast.LENGTH_LONG)
						.show();
				}
			}
		}

		DataManager.getInstance().dropCompetitions(Constants.CompetitionType.CAREER);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		if (DataManager.getInstance().getStatus().RacePaused)
		{
			finish();
			return;
		}

		Competition c = DataManager.getInstance().getCompetitionByType(Constants.CompetitionType.CAREER);
		if (c != null)
		{
			if (c.isStarted()){
				if (c.isFinished()){
					processCompetitionFinished(c);
				} else {
					Intent irace = new Intent(this, RaceActivity.class);
					irace.putExtra(Constants.Extra.CompetitionId, c.id);
					startActivity(irace);
					return;
				}
			}
		}
	}	

	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		
	}

	private void showGeneral(){
		((TextView)findViewById(R.id.career_tvPlrName)).setText(
			data.name + " " + data.lastname.toUpperCase());

		((TextView)findViewById(R.id.career_tvPlrMoney)).setText(
			String.format(
				getResources().getString(R.string.career_Money),
				data.money));		
		((TextView)findViewById(R.id.career_tvPlrExperience)).setText(
			String.format(
				getResources().getString(R.string.career_Experience),
				data.experience));
	}
	
	private void startCareer(){
		data.money = 0;
		data.experience = 0;
		DataManager.getInstance().storeData();
		
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setCancelable(true);
		ad.setTitle(String.format(
						getResources().getString(R.string.career_startTitle), 
						data.name + " " +data.lastname));
		ad.setMessage(R.string.career_startMessage);
		ad.setNeutralButton(R.string.message_ok,
			new DialogInterface.OnClickListener(){ public void onClick(DialogInterface di, int i)
				{ showGeneral(); } });
		ad.show();
	}
}
