package com.pl.slalom.career;
import com.pl.slalom.*;
import android.app.*;
import android.os.*;
import com.pl.slalom.data.*;
import android.content.*;
import android.widget.*;

public class CareerActivity extends Activity
{
	Data data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
				setContentView(R.layout.career);

		data = DataManager.getInstance().getData();
		showGeneral();
		
		if (data.experience < 0){
			startCareer();
		}
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
