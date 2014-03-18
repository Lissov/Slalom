package com.pl.slalom.Controls.Adapters;
import android.widget.*;
import com.pl.slalom.*;
import com.pl.slalom.data.race.*;
import android.app.*;
import android.view.*;
import android.view.View.*;
import android.content.*;
import android.opengl.*;

public class CareerCompetitionAdapter extends BaseAdapter
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

	@Override
	public View getView(int position, View convertView, ViewGroup group)
	{
		if (convertView == null){
			convertView = inflater.inflate(R.layout.event_item, group, false);
		}
		
		CompetitionDef item = data[position];
		((ImageView)convertView.findViewById(R.id.career_event_image))
			.setImageResource(getTypeResource(item.type, item.isAvailable));
		((TextView)convertView.findViewById(R.id.career_event_name))
			.setText(item.name);
		((TextView)convertView.findViewById(R.id.career_event_desc))
			.setText(item.description);
		/*((TextView)convertView.findViewById(R.id.career_event_locked))
			.setVisibility(item.isAvailable ? View.GONE : View.VISIBLE);*/
			
		return convertView;
	}
	
	private int getTypeResource(CompetitionType type, boolean isAvailable){
		
		switch (type){
			case Qualification:
				return isAvailable 
						? R.drawable.eventtype_time
						: R.drawable.eventtype_time_locked;
			case LocalChamp:
				return isAvailable 
					? R.drawable.eventtype_race
					: R.drawable.eventtype_race_locked;
			default:
				return isAvailable 
					? R.drawable.eventtype_unknown
					: R.drawable.eventtype_unknown_locked;
		}
	}
}
