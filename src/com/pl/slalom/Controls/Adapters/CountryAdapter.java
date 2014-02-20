/*package com.pl.slalom.Controls.Adapters;
import android.widget.*;
import com.pl.slalom.*;
import com.pl.slalom.data.statics.*;
import java.util.*;
import android.view.*;
import android.app.*;
import android.graphics.*;

public class CountryAdapter extends BaseAdapter implements SpinnerAdapter
{
	private Activity context;
	private List<Country> countries;
	
	public CountryAdapter(Activity context, List<Country> countries){
		this.context = context;
		this.countries = countries;
	}

	@Override
	public int getCount()
	{
		return 0;
		//return countries.size();
	}

	@Override
	public Object getItem(int p1)
	{
		return countries.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		return p1;
	}

	@Override
	public View getView(int pos, View v, ViewGroup vg)
	{
		if (v == null){
			v = context.getLayoutInflater().inflate(R.layout.country_dropdown, vg);
		}
		
		ImageView flag = (ImageView)v.findViewById(R.id.cs_ivFlag);
		flag.setBackgroundColor(Color.WHITE);

		TextView name = (TextView)v.findViewById(R.id.cs_tvCountryName);
		TextView code = (TextView)v.findViewById(R.id.cs_tvCountryCode);
		
		if (name != null)
			name.setText(countries.get(pos).name);
		if (code != null)
			code.setText(countries.get(pos).code);
		
		return v;
	}
}*/
