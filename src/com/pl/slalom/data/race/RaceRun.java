package com.pl.slalom.data.race;

public class RaceRun
{
	public long id;
	public RunResult runResult;

	public RaceRun(RunStatus status)
	{
		this.runResult = new RunResult(-1,-1,status);
	}
}
