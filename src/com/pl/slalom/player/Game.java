package com.pl.slalom.player;

import com.pl.slalom.*;
import com.pl.slalom.track.*;
import android.graphics.*;
import com.pl.slalom.player.ski.*;
import com.pl.slalom.Utility.*;
import com.pl.slalom.data.*;

public class Game
{
	public Slope slope;
	public Route route;
	private IMoveCallback callback;
	
	public boolean finished;
	public boolean failed;
	private int tramplinFly;
	private long startTime;
	private long finishTime;
	private ISki ski;
	private PlayerSkills playerSkills;
	private MoveCalculator moveCalculator;
	
	public Game(int slopeN, int skiN, int playerId, IMoveCallback callback) throws Exception {
		this.callback = callback;
		this.playerSkills = DataManager.getInstance().getPlayerSkills(playerId);
		slope = new SlopeManager().getSlope(slopeN);
		route = new Route(slope.startPos, 0);
		ski = new SkiManager().getSki(skiN);
		moveCalculator = new MoveCalculator(playerSkills, ski.getSkiParameters());
		
		finished = false;
		failed = false;
		tramplinFly = 0;
		
		startTime = 0;
		finishTime = 0;
		calcPossibleMoves();
	}
	
	private long moveStart;
	public boolean makingMove;
	public int makingX;
	public int makingY;
	private boolean[][] possibleMoves;
	
	public boolean[][] getPossibleMoves(){
		return possibleMoves;
	}
	
	private void calcPossibleMoves(){
		Point last = route.getLastMove();
		possibleMoves = moveCalculator.getPossibleMoves(
									last.x, last.y,
									!finished && !failed && !makingMove, 
									route.currentPosition == 0,
									tramplinFly > 0);
	}
	
	public void makeMove(int dx, int dy){
		if (!canMove(dx, dy)) return;
		
		makingX = dx;
		makingY = dy;
		moveStart = System.currentTimeMillis();
		makingMove = true;
		calcPossibleMoves();
	}
	
	public boolean canMove(int dx, int dy){
		int m = Constants.MaxPossibleMove;
		Point last = route.getLastMove();
		return possibleMoves[dx - last.x + m][dy - last.y + m];
	}
	
	public float getMovePercentage(){
		if (!makingMove) return 1.0f;
		
		long ct = System.currentTimeMillis();
		long len = (ct - moveStart);
		
		if (len > Constants.moveCounted){
			movePerformed(makingX, makingY);
			return 0f;
		}
		
		if (len > Constants.moveLength){
			return 1f;
		}
		
		return ((float)len) / Constants.moveLength;
	}
	
	private void movePerformed(int dx, int dy){
		makingMove = false;
		if (route.currentPosition == 0)
			startTime = System.currentTimeMillis();
		
		int prX = route.positionsX[route.currentPosition];
		int prY = route.positionsY[route.currentPosition];
		
		route.makeMove(dx, dy);
		
		int x = route.positionsX[route.currentPosition];
		int y = route.positionsY[route.currentPosition];
		
		if (x <= 0 || x >= slope.width){
			failed = true;
		}
		
		Gate nextg = slope.gates[route.passedCount];
		while (TrackCalculator.getCross(nextg.position, nextg.leftPos, nextg.rightPos,
				prX, prY, x, y) == PassType.Pass){
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
			PassType crossS = TrackCalculator.getCross(nextg.position, nextg.leftPos, nextg.rightPos,
									   prX, prY, x, y);
			if (crossS == PassType.Hit)
				failed = true;
				
			PassType crossO = TrackCalculator.getCross(nextg.position, nextg.leftPos, nextg.rightPos,
								 x, y, prX, prY);
			if (crossO == PassType.Pass)
				failed = true;
		}
		
		Gate finish = slope.gates[slope.gates.length - 1];
		PassType finalPass = TrackCalculator.getCross(finish.position, finish.leftPos, finish.rightPos, prX, prY, x, y);
		if (finalPass == PassType.Miss || finalPass == PassType.Hit){
			failed = true;
		}
		
		if (tramplinFly == 0){
			int i = 0;
			while (i < slope.tramplins.length){
				Tramplin t = slope.tramplins[i];
				PassType tpt = TrackCalculator.getCross(t.position, t.left, t.right, prX, prY, x, y);
				if (tpt == PassType.Pass || tpt == PassType.Hit){
					tramplinFly = t.power;
					i = slope.tramplins.length; // stop in, only one tramplin per move
				}
				i++;
			}
			
			for (int j = 0; j < slope.tramplins.length; j++){
				Tramplin t = slope.tramplins[j];
				PassType tpt = TrackCalculator.getCross(t.position, t.left, t.right, x, y, prX, prY);
				if (tpt == PassType.Pass || tpt == PassType.Hit){
					failed = true; // crash in tramplin
				}
			}
		} else {
			tramplinFly--;
		}
		
		calcPossibleMoves();
		
		callback.moveComplete();
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
