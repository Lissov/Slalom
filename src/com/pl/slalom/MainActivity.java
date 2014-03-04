package com.pl.slalom;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import com.pl.slalom.data.*;
import com.pl.slalom.data.race.*;
import com.pl.slalom.training.*;
import com.pl.slalom.career.*;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		try{
			DataManager.getInstance().Init(this);
		} catch (Exception ex){
			Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();			
		}
    }
	
	public void trainingClick(View view){
		//startActivity(new Intent(this, TrainingActivity.class));
		startActivity(new Intent(this, DatabaseActivity.class));
	}
	
	public void careerClick(View view){
		String name = DataManager.getInstance().getData().name;
		if (null == name || name.trim().equals("")){
			Intent intent = new Intent(this, SetupCareerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
		} else {
			startActivity(new Intent(this, CareerActivity.class));
		}
	}
	
	public void multiplayerClick(View view){
		try{
			Competition comp = DataManager.getInstance().getCompetition();
		
			boolean exists = comp != null && 
					!(comp.currentRace == comp.races.size()-1 
						&& comp.races.get(comp.currentRace).isFinished());
		
			if (exists)
			{
				AlertDialog alert = new AlertDialog.Builder(this).create();
				alert.setMessage(getResources().getString(R.string.mp_gameExists));
				alert.setCancelable(true);
				alert.setButton(AlertDialog.BUTTON_POSITIVE,
					getResources().getString(R.string.mp_continue),
					new DialogInterface.OnClickListener(){ public void onClick(DialogInterface dialog, int which)
						{ continueMP(); }});
				alert.setButton(AlertDialog.BUTTON_NEGATIVE,
					getResources().getString(R.string.mp_new),
					new DialogInterface.OnClickListener(){ public void onClick(DialogInterface dialog, int which)
						{ setupNewMP(); }});
				alert.setButton(AlertDialog.BUTTON_NEUTRAL,
					getResources().getString(R.string.mp_cancel),
					new DialogInterface.OnClickListener(){ public void onClick(DialogInterface dialog, int which)
						{ }});
				alert.show();
			}
			else
			{
				setupNewMP();
			}
		} catch(Exception ex){
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void setupNewMP(){
		DataManager.getInstance().dropAllCompetitions();
		
		Intent isetup = new Intent(this, MultiplayerSetupActivity.class);
		isetup.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(isetup);		
	}
	private void continueMP(){
		Intent irace = new Intent(this, RaceActivity.class);
		startActivity(irace);				
	}
}
