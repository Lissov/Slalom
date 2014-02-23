package com.pl.slalom.Controls;
import android.app.*;
import android.view.*;
import android.widget.*;
import com.pl.slalom.*;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.statics.*;
import com.pl.slalom.player.ski.*;
import java.util.*;

public class MpPlayerCreate extends LinearLayout
{
	private TextView tvName;
	private Spinner spSkis;
	private Spinner spCountry;
	private List<ISki> skis;
	private List<Country> countries;
	
	public MpPlayerCreate(Activity context, List<ISki> skis, List<Country> countries, String defaultName){
		super(context);
		this.skis = skis;
		this.countries = countries;
		
		View view = context
						.getLayoutInflater()
					.inflate(R.layout.multiplayer_player, null);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT);
		this.addView(view, params);
		
		tvName = (TextView)findViewById(R.id.mpPlayerName);
		tvName.setText(defaultName);
		
		spSkis = (Spinner)findViewById(R.id.mpPlayerSki);
		String[] skinames = new String[skis.size()];
		for (int i = 0; i<skis.size(); i++)
			skinames[i] = skis.get(i).getName();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			context, android.R.layout.simple_spinner_item,	skinames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSkis.setAdapter(adapter);
		
		spCountry = (Spinner)findViewById(R.id.mpPlayerCountry);
		String[] c = new String[countries.size()];
		for (int i = 0; i<countries.size(); i++)
			c[i] = countries.get(i).name;
		ArrayAdapter<String> a = new ArrayAdapter<String>(
			context, android.R.layout.simple_spinner_item,	c);
		a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCountry.setAdapter(a);
	}
	
	public Competitor getCompetitor(){
		return new Competitor(
			String.valueOf(tvName.getText()),
			skis.get(spSkis.getSelectedItemPosition()).getId(),
			countries.get(spCountry.getSelectedItemPosition()).id,
			Constants.AIID_Human,
			0);
	}
}
