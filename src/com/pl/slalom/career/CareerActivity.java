package com.pl.slalom.career;
import com.pl.slalom.*;
import android.app.*;
import android.os.*;
import com.pl.slalom.data.*;
import android.content.*;

public class CareerActivity extends Activity
{
	Data data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.database);
		
		data = DataManager.getInstance().getData();
		if (data.experience < 0){
			startCareer();
		}
	}
	
	private void startCareer(){
		data.money = 0;
		data.experience = 0;
		DataManager.getInstance().storeData();
		
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setCancelable(true);
		ad.setTitle(R.string.career_startTitle);
		ad.setNeutralButton(R.string.career_startMessage,
			new DialogInterface.OnClickListener(){ public void onClick(DialogInterface di, int i){} });
		ad.show();
	}
}
