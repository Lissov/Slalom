package com.pl.slalom.data.race;

public class RunResult
{
	public int turns;
	public float time;
	public RunStatus status;

	public RunResult(int turns, float time, RunStatus runStatus)
	{
		this.turns = turns;
		this.time = time;
		this.status = runStatus;
	}	
}
