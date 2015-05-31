package com.pl.slalom;
import com.pl.slalom.data.achievment.*;
import com.pl.slalom.player.ai.*;

//import android.graphics.*;

public class Constants
{
	public static final boolean allUnlocked = true;
	
	public static final boolean isFullVersion = true;
	
	public static class Extra{
		public static final String RunData = "rundataId";
		public static final String CompetitionId = "competitionId";
	}
	
	public static class Achievement{
		public static final int SlopeBase = 20000;
		public static final int CompetitionBase = 10000;
	}
	
	public static final int MaxRecordedSteps = 1000;
	public static final int MaxPossibleMove = 1;
	
	public static final boolean controlsOnSlope = true;
	
	public static final float moveClickR2 = 0.5f;
	
	public static final long moveLength = 1000;
	public static final long moveCounted = 1100;
	
	public static final int MaxMPPlayrs = isFullVersion ? 10 : 2;

	public static final int MP_ModeTurns = 1;
	public static final int MP_ModeTime = 2;
	
	public static final int MP_player_id = PlayerManager.AIID_Human;

	public static final int[] experienceLevels = new int[] 
		{ 50, 200, 500, 1000, 2000, 5000, 10000, 100000 };
		
	public static class Events{
		public static class Austria{	
			public static final int evt_quali_1 = 1;
			public static final int evt_locchamp_1 = 2;
		}
	}
	
	public static class Slopes{
		public static class Training{
			public static final int first = 1;			
			public static final int tramplin = 2;			
			public static final int slalom = 3;			
		}
		public static class Austria{	
			public static final int hohewandwiese = 10;
		}
	}
	
	public static class CompetitionType{
		public static final int CAREER = 1;
		public static final int MULTIPLAYER = 2;
	}
}
