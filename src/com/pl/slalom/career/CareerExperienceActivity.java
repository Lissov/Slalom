package com.pl.slalom.career;

import com.pl.slalom.*;
import com.pl.slalom.data.DataManager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CareerExperienceActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.career_experience);
		
		showData();
	}
	
	private void showData(){
		DataManager d = DataManager.getInstance();
		((TextView)findViewById(R.id.career_exp_toNextLevel)).setText(
				String.format(
						getResources().getString(R.string.career_exp_nextLevel),
						d.getNextExpLevel(d.getData().experience)
				)
			);
	}
}
