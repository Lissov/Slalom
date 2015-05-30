package com.pl.slalom.Controls;
import android.app.*;
import android.view.*;
import android.widget.*;
import com.pl.slalom.*;
import com.pl.slalom.data.race.*;
import com.pl.slalom.data.statics.*;
import com.pl.slalom.player.ski.*;
import java.util.*;
import com.pl.slalom.Controls.Adapters.*;
import com.pl.slalom.player.ai.*;

public class MpPlayerCreate extends LinearLayout
{
	private TextView tvName;
	private Spinner spSkis;
	private Spinner spTypes;
	private Spinner spCountry;
	private List<ISki> skis;
	private List<String> types;
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
		spCountry.setAdapter(CountryAdapter.getAdapter(context, countries));
		
		spTypes = (Spinner)findViewById(R.id.mpPlayerType);
		types = new LinkedList<String>();
		String[] typenames = new String[PlayerManager.AvailableTypes.length];
		for (int i = 0; i < typenames.length; i++)
			typenames[i] = PlayerManager.AvailableTypes[i].name;
		ArrayAdapter<String> adapterT = new ArrayAdapter<String>(
			context, android.R.layout.simple_spinner_item, typenames);
		adapterT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spTypes.setAdapter(adapterT);
	}
	
	public Competitor getCompetitor(){
		return new Competitor(
			String.valueOf(tvName.getText()),
			skis.get(spSkis.getSelectedItemPosition()).getId(),
			countries.get(spCountry.getSelectedItemPosition()).id,
			PlayerManager.AvailableTypes[spTypes.getSelectedItemPosition()].type,
			0);
	}
}
