package com.pl.slalom;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import com.pl.slalom.data.*;
import java.util.*;
import com.pl.slalom.player.ski.*;

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
	
	public void tutorialClick(View view){
/*		Intent igame = new Intent(this, GameActivity.class);
		igame.putExtra(Constants.Extra_LevelNumber, levelnum);
		igame.putExtra(Constants.Extra_SkiNumber, 10);
		startActivity(igame);*/
	}
	
	public void careerClick(View view){
		
	}
	
	public void multiplayerClick(View view){
		Intent igame = new Intent(this, MultiplayerSetupActivity.class);
		startActivity(igame);
	}
}
