package com.pl.slalom;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class MainActivity extends Activity
{
	private Spinner spLevels;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		spLevels = (Spinner)findViewById(R.id.spLevels);
    }
	
	public void startClick(View view){
		int levelnum = spLevels.getSelectedItemPosition() + 1;
		
		Intent igame = new Intent(this, GameActivity.class);
		igame.putExtra(Constants.Extra_LevelNumber, levelnum);
		startActivity(igame);
	}
}
