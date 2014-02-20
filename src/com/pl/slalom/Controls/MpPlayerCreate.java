package com.pl.slalom.Controls;
import android.view.*;
import android.content.*;
import android.app.*;
import com.pl.slalom.*;
import android.widget.*;
import java.util.*;
import com.pl.slalom.player.ski.*;
import com.pl.slalom.data.statics.*;

public class MpPlayerCreate extends LinearLayout
{
	private Spinner spSkis;
	
	public MpPlayerCreate(Activity context, List<ISki> skis, List<Country> countries){
		super(context);
		View view = context
						.getLayoutInflater()
					.inflate(R.layout.multiplayer_player, null);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT);
		this.addView(view, params);
		
		spSkis = (Spinner)findViewById(R.id.mpPlayerSki);
		String[] skinames = new String[skis.size()];
		for (int i = 0; i<skis.size(); i++)
			skinames[i] = skis.get(i).getName();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			context, android.R.layout.simple_spinner_item,	skinames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSkis.setAdapter(adapter);
		
		Spinner spCountry = (Spinner)findViewById(R.id.mpPlayerCountry);
		String[] c = new String[countries.size()];
		for (int i = 0; i<countries.size(); i++)
			c[i] = countries.get(i).name;
		ArrayAdapter<String> a = new ArrayAdapter<String>(
			context, android.R.layout.simple_spinner_item,	c);
		a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spCountry.setAdapter(a);
	}
}
