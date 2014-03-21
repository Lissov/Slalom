package com.pl.slalom.data.race;
import com.pl.slalom.Utility.*;
import java.util.*;

public class Race
{
	public long id;
	public int order;
	public int trackId;
	public int runCount;
	public ResultMeasureType resultType;
	public RaceRun[][] playerRuns;

	public Race(int trackId, int order, int runCount, 
				ResultMeasureType resultType, RaceRun[][] playerRuns)
	{
		this.trackId = trackId;
		this.order = order;
		this.runCount = runCount;
		this.resultType = resultType;
		this.playerRuns = playerRuns;
	}
	
	public boolean isFinished(){
		for (int p = 0; p < playerRuns.length; p++){
			if (playerRuns[p][runCount - 1].runResult.status == RunStatus.NotStarted)
				return false;
		}
		
		return true;
	}
	
	public boolean isStarted(){
		return playerRuns[0][0].runResult.status != RunStatus.NotStarted;
	}
	
	public RunResult getBestResult(int cNum){
		RunResult bestRes = null;
		Comparator<RunResult> comparator = RunResultComparator.getComparator(resultType);

		for (int r = 0; r < runCount; r++){
			if (playerRuns[cNum][r].runResult.status == RunStatus.NotStarted){
				break;
			}
			if (playerRuns[cNum][r].runResult.status == RunStatus.Finished){
				if (bestRes == null || 
					comparator.compare(bestRes, playerRuns[cNum][r].runResult) > 0
					)
				{
					bestRes = playerRuns[cNum][r].runResult;
				}
			}
		}

		return bestRes;
	}
	
	public PlayerRaceResult[] getResults(){
		int playerCnt = playerRuns.length;
		PlayerRaceResult[] res = new PlayerRaceResult[playerCnt];

		for (int i = 0; i<playerCnt; i++){
			res[i] = new PlayerRaceResult();
			res[i].compN = i;
			res[i].bestRes = getBestResult(i);
			res[i].runCount = getRunCount(i);
			if (res[i].bestRes == null){
				if (res[i].runCount == 0)
					res[i].overallStatus = RaceResultStatus.NotStarted;
				else
					res[i].overallStatus = RaceResultStatus.NotFinished;
			} else
				res[i].overallStatus = RaceResultStatus.HasResult;
		}

		Arrays.sort(res, getComparator(resultType));
		for (int i = 0; i<playerCnt; i++){
			res[i].rank = i + 1;
		}
		return res;
	}
	
	private int getRunCount(int cNum){
		int runC = 0;
		while (runC < runCount &&
			   playerRuns[cNum][runC].runResult.status != RunStatus.NotStarted)
		{
			runC++;
		}
		return runC;
	}
	
	private static Comparator<PlayerRaceResult> getComparator(ResultMeasureType resType)
	{
		final Comparator<RunResult> comparator = RunResultComparator.getComparator(resType);
		return new Comparator<PlayerRaceResult>(){
			public int compare(PlayerRaceResult pr1, PlayerRaceResult pr2)
			{
				if (pr1.bestRes == null && pr2.bestRes == null)
					return Integer.valueOf(pr1.compN).compareTo(pr2.compN);
				if (pr1.bestRes == null) return +1;
				if (pr2.bestRes == null) return -1;
				return comparator.compare(pr1.bestRes, pr2.bestRes);
			}
		};
	}

	public int getPlayerPlace(int compN){
		PlayerRaceResult[] results = getResults();
		for (int i = 0; i<results.length; i++){
			if (results[i].compN == compN)
				return i;
		}
		return 500;
	}
}
