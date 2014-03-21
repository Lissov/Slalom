package com.pl.slalom.data.achievment;
import com.pl.slalom.*;
import android.content.*;
import android.content.res.*;
import com.pl.slalom.track.*;
import com.pl.slalom.data.race.*;

public class AchievementManager
{
	public static String getString(Context context, Achievement a){
		Resources r = context.getResources();
		if (a.result.getType() == SlopeResult.TYPE){
			int slopeId = a.identifier - Constants.Achievement.SlopeBase;
			return r.getString(R.string.new_record) + " " + 
				SlopeManager.getSlopeName(context, slopeId);
		}
		
		if (a.result.getType() == CompetitionResult.TYPE){
			int compId = a.identifier - Constants.Achievement.CompetitionBase;
			return String.format(r.getString(R.string.new_record_comp),
				new CompetitionManager().getCompetitionDef(context, compId).name,
				((CompetitionResult)a.result).place);
		}
		
		return "Unknown";
	}
}
