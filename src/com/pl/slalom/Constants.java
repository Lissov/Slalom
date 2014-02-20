package com.pl.slalom;

//import android.graphics.*;

public class Constants
{
	public static final boolean isFullVersion = true;
	
	public static final String Extra_LevelNumber = "levelnumber";
	public static final String Extra_SkiNumber = "skinumber";
	
	public static final int MaxRecordedSteps = 1000;
	public static final int MaxPossibleMove = 2;
	
	public static final boolean controlsOnSlope = true;
	
	public static final float moveClickR2 = 0.5f;
	
	public static final long moveLength = 1000;
	public static final long moveCounted = 1100;
	
	public static final int MaxMPPlayrs = isFullVersion ? 10 : 2;

	public static final int MP_ModeTurns = 1;
	public static final int MP_ModeTime = 2;
}
