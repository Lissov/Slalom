package com.pl.slalom.data.race;

public class Race
{
	public long id;
	public int order;
	public int trackId;
	public int runCount;
	public RaceRun[][] playerRuns;

	public Race(int trackId, int order, int runCount, RaceRun[][] playerRuns)
	{
		this.trackId = trackId;
		this.order = order;
		this.runCount = runCount;
		this.playerRuns = playerRuns;
	}
	
	public boolean isFinished(){
		for (int p = 0; p < playerRuns.length; p++){
			if (playerRuns[p][runCount - 1].runResult.status == RunStatus.NotStarted)
				return false;
		}
		
		return true;
	}
}
