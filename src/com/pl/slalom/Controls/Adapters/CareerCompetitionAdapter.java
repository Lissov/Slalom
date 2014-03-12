package com.pl.slalom.Controls.Adapters;
import android.widget.*;
import com.pl.slalom.*;
import com.pl.slalom.data.race.*;
import android.app.*;
import android.view.*;
import android.view.View.*;
import android.content.*;
import android.opengl.*;
import android.transition.*;

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

	@Override
	public View getView(int position, View convertView, ViewGroup group)
	{
		if (convertView == null){
			convertView = inflater.inflate(R.layout.event_item, group, false);
		}
		
		CompetitionDef item = data[position];
		((TextView)convertView.findViewById(R.id.career_event_name))
			.setText(item.name);
		((TextView)convertView.findViewById(R.id.career_event_desc))
			.setText(item.description);
		((TextView)convertView.findViewById(R.id.career_event_locked))
			.setVisibility(item.isAvailable ? View.GONE : View.VISIBLE);
		
		convertView.setTag(item.id);
			
		return convertView;
	}

	@Override
	public void onClick(View view)
	{
		Toast.makeText(activity, "" + view.getTag().toString(), Toast.LENGTH_LONG).show();
	}
}
