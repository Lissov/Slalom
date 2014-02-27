package com.pl.slalom.player.ski;

public class SkiParameters {
	public int[] probs;
	public float uphillDec;
	public float straightDec;
	public float straightAcc;
	public float uphillMaxSpeed;
	public float straightMaxSpeed;
	
	public float startSpeed;
	
	public SkiParameters(int[] probs, float uphillDec, float straightDec, float straightAcc, float uphillMaxSpeed, float straightMaxSpeed, float startSpeed){
		this.probs = probs;
		this.uphillDec = uphillDec;
		this.straightDec = straightDec;
		this.straightAcc = straightAcc;
		this.uphillMaxSpeed = uphillMaxSpeed;
		this.straightMaxSpeed = straightMaxSpeed;
		
		this.startSpeed = startSpeed;
	}
}
