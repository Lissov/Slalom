package com.pl.slalom.data.race;
import java.util.*;

import android.content.Context;
import android.content.res.Resources;

import com.pl.slalom.Constants;
import com.pl.slalom.R;

public class CompetitionManager
{
	private Resources r;
	
	private CompetitionDef getComp(int id, int[] slopeIds, int nameRes, int descRes){
		return new CompetitionDef(id, slopeIds, r.getString(nameRes), r.getString(descRes));
	}
	
	public List<CompetitionDef> getAllCompetitions(Context context){
		r = context.getResources();
				
		List<CompetitionDef> result = new LinkedList<CompetitionDef>();
		
		result.add(getComp(
				Constants.Events.Austria.evt_quali_1, 
				new int[] {Constants.Slopes.Austria.hohewandwiese},
				R.string.evt_au_quali1_name, R.string.evt_au_quali1_desc
				));
		
		return result;
	}
}
