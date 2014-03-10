package com.pl.slalom.Controls.Adapters;
import android.widget.*;
import com.pl.slalom.data.race.*;
import android.app.*;
import android.view.*;

public class CareerCompetitionAdapter extends BaseAdapter implements OnClickListener
{
	private Activity activity;
	private CompetitionDef[] data;
	private LayoutInflater inflater = null;
	
	public CareerCompetitionAdapter(Activity activity, CompetitionDef[] data){
		this.activity = activity;
		this.data = data;
		
		this.inflater = activity.getLayoutInflater();
	}

	@Override
	public int getCount()
	{
		return data.length;
	}

	@Override
	public Object getItem(int position)
	{
		return data[position];
	}

	@Override
	public long getItemId(int position)
	{
		return data[position].id;
	}



	
	
}
