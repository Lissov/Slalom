package com.pl.slalom.player;

import com.pl.slalom.*;
import com.pl.slalom.track.*;
import android.graphics.*;
import android.widget.*;
import android.content.*;

public class Game
{
	public Slope slope;
	public Route route;
	
	public boolean finished;
	public boolean failed;
	private int tramplinFly;
	private long startTime;
	private long finishTime;
	
	public Game(int slopeN){
		slope = new SlopeManager().getSlope(slopeN);
		route = new Route(slope.startPos, 0);
		
		finished = false;
		failed = false;
		tramplinFly = 0;
		
		startTime = 0;
		finishTime = 0;
	}
	
	public boolean[][] getPossibleMoves(){
		int mc = Constants.MaxPossibleMove;
		int mcnt = 2 * Constants.MaxPossibleMove + 1;
		boolean[][] result = new boolean[mcnt][mcnt];
		for (int x = 0; x < mcnt; x++)
			for (int y = 0; y < mcnt; y++)
				result[x][y] = false;
				
		if (finished || failed)
			return result;
		
		if (route.currentPosition == 0){
			result[mc][mc + 1] = true;
			return result;
		}
		
		if(tramplinFly > 0)	{
			result[mc][mc] = true;
			return result;
		}
		
		result[mc][mc] = true;
		result[mc+1][mc] = true;
		result[mc-1][mc] = true;
		result[mc][mc+1] = true;
		result[mc][mc-1] = true;
		
		return result;
	}
	
	public void makeMove(int dx, int dy){
		if (route.currentPosition == 0)
			startTime = System.currentTimeMillis();
		
		int prX = route.positionsX[route.currentPosition];
		int prY = route.positionsY[route.currentPosition];
		
		Point last = route.getLastMove();
		route.makeMove(last.x + dx, last.y + dy);
		
		int x = route.positionsX[route.currentPosition];
		int y = route.positionsY[route.currentPosition];
		
		Gate nextg = slope.gates[route.passedCount];
		while (isCross(nextg.position, nextg.leftPos, nextg.rightPos,
				prX, prY, x, y)){
			route.passedCount++;
		
	 		if (route.passedCount == slope.gates.length)
			{
		 		finished = true;
				finishTime = System.currentTimeMillis();
				break;
			}

	 		nextg = slope.gates[route.passedCount];
		}
					
		for (int i = 0; i < slope.gates.length; i++){
			if (isCross(nextg.position, nextg.leftPos, nextg.rightPos,
					x, y, prX, prY))
				failed = true;
		}
		
		if (tramplinFly == 0){
			int i = 0;
			while (i < slope.tramplins.length){
				Tramplin t = slope.tramplins[i];
				if (isCross(t.position, t.left, t.right, prX, prY, x, y)){
					tramplinFly = t.power;
					i = slope.tramplins.length; // stop in, only one tramplin per move
				}
				i++;
			}
			
			for (int j = 0; j < slope.tramplins.length; j++){
				Tramplin t = slope.tramplins[j];
				if (isCross(t.position, t.left, t.right, x, y, prX, prY)){
					failed = true; // crash in tramplin
				}
			}
		} else {
			tramplinFly--;
		}
	}
	
	private boolean isCross(float y, float left, float right,
		int x1, int y1, int x2, int y2)
	{
		if (y < y1 || y > y2) 
			return false;
			
		float crossX = x1 + (x2 - x1) * (y - y1) / (y2 - y1);
		
		return (crossX > left && crossX < right);
	}
	
	public float getTime(){
		if (route.currentPosition == 0) 
			return 0;
		
		long et;
		if (finished)
			et = finishTime;
		else
			et = System.currentTimeMillis();
			
		return (float)((et - startTime) / 100) / 10f;
	}
}
