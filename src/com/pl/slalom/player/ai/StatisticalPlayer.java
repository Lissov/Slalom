package com.pl.slalom.player.ai;
import com.pl.slalom.player.*;
import com.pl.slalom.*;
import android.graphics.*;
import java.util.concurrent.locks.*;
import com.pl.slalom.track.*;

public class StatisticalPlayer extends ArtIntelBase
{
	private class Probabilities{
		public float[][] probs;
		public int cnt;
	}
	private Probabilities[][] stats;
	private int minx = -5;
	private int maxx = 5;
	private int miny = -2;
	private int maxy = 10;
	private int xcnt = maxx - minx + 1;
	private int ycnt = maxy - miny + 1;
	private float[][] normalProbs;
	private int minYMoveAllowed = 0;
	
	public StatisticalPlayer(Game game){
		super(game);
		
		initStats();
	}
	
	@Override
	public Point getNextMove()
	{
		debugMap.put("status", "requested");
		updateStats();
		
		if (bestMove == null)
			startThinking(false);

		Point result = new Point(bestMove.x, bestMove.y);
		bestMove = null;
		debugMap.put("status", "returned: " + result.x + ":" + result.y);
		return result;
	}

	Thread moveThinker = null;
	
	@Override
	public void startThinking()
	{
		super.startThinking();
		startThinking(true);
	}
	
	public void startThinking(boolean startNewThread)
	{
		debugMap.put("status", "thinking");
				
		requestNum++;
		if (moveThinker != null){
			moveThinker.interrupt();
			moveThinker = null;
		}
		
		final Point lm = game.route.getLastMove();
		int m = Constants.MaxPossibleMove;
		final boolean[][] posMoves = game.getPossibleMoves();
		// get any possible move
		bestMove = null;
		if (posMoves[m][m])
			bestMove = new Point(lm.x,lm.y);
		else {
			for (int i = 0; i<2*m+1; i++){
				for (int j = 0; j<2*m+1; j++){
					if (posMoves[i][j]){
						bestMove = new Point(lm.x + i-m, lm.y + j-m);
						break;
					}
				}
				if (bestMove != null) break;
			}
		}
		
		if (startNewThread){
			moveThinker = new Thread(new Runnable(){
				public void run(){
					ThinkTheMove(posMoves, lm, requestNum);
				}
			});
			moveThinker.start();
		}
		debugMap.put("status", "thinking activated");
	}
	
	protected void ThinkTheMove(boolean[][] posMoves, Point lm, int reqNum){
		try {
			debugMap.put("status", "thinking started");
			debugMap.put("foundBest", "");
			
			int m = Constants.MaxPossibleMove;
		
			float[][] probs = new float[2*m+1][2*m+1];
			for (int x = 0; x < 2*m+1; x++){
				for (int y = 0; y < 2*m+1; y++){
					probs[x][y] = posMoves[x][y] ? 1 : 0;
				}
			}
			
			ThinkingContext context = new ThinkingContext();
			context.allowedProb = 1f;
			context.maxReachedProb = 0f;
			context.maxDeep = 8;
			context.reqNum = reqNum;
			context.slope = game.slope;
			context.reachedFinishAtDeep = Integer.MAX_VALUE;
			context.bestReachedGate = 0;
			context.bestReachedPos = 0;
			ThinkMoveFromHere(game.route.getPosition(), lm, game.route.passedCount - 1,
				probs, 1f, 0, null, context);
			
			debugMap.put("status", "thinking ended");
		} catch (Exception ex){
			// something broken
			debugMap.put("error", ex.getMessage());
		}
	}

	/*
	for additional calculations
	private class GoodMoveVariant {
		
	}*/
	
	private class ThinkingContext{
		public float allowedProb;
		public float maxReachedProb;
		public int maxDeep;
		public int reqNum;
		public Slope slope;
		public int reachedFinishAtDeep;
		
		public int bestReachedGate;
		public int bestReachedPos;
		/*, best10*/
	}
	
	private void ThinkMoveFromHere(
		Point position,
		Point lastMove,
		int lastPassedGate,
		float[][] probs,
		float currentProb,
		int deep,
		Point firstMove,
		ThinkingContext context) throws InterruptedException
	{
		if (firstMove != null)
			debugMap.put("Think of ", firstMove.x + ":" + firstMove.y + " -> " + deep);
		
		if (deep == context.maxDeep){
			CheckVariant(firstMove, position, currentProb, lastPassedGate, context);
			return;
		}
		
		int m = Constants.MaxPossibleMove;
		for (int x = 0; x < 2*m+1; x++){
			for (int y = 0; y < 2*m+1; y++){
				
				float p = probs[x][y];
				if (p == 0f) continue;
				float np = p * currentProb;
				if (np < context.maxReachedProb) continue; // too bad
		
				int resGates = lastPassedGate;
				
				Point move = new Point(lastMove.x + x - m, lastMove.y + y - m);
				if (move.y < minYMoveAllowed) continue; // 
				if (move.x == 0 && move.y == 0 && lastMove.x == 0 && lastMove.y == 0) continue; // no infinite stops;
				
				Point fMove = firstMove == null ? move : firstMove;
				
				Point newPos = new Point(position.x + move.x, position.y + move.y);
				if (newPos.x <= 0 || newPos.x >= context.slope.width) continue; // out of track limits
				
				PassType pt = TrackCalculator.getGateCross(
					context.slope.gates[lastPassedGate + 1],
					position, move.x, move.y);
				if (pt == PassType.Hit || pt == PassType.Miss) continue; // missed a gate
				
				if (pt == PassType.Pass){
					resGates++;
					// check finished
					if (resGates == context.slope.gates.length-1){
						if (deep < context.reachedFinishAtDeep){
							setBestMove(fMove.x, fMove.y, context.reqNum);
							context.reachedFinishAtDeep = deep;
							if (context.maxReachedProb <= context.allowedProb){
								if (np >= context.allowedProb)
									context.maxReachedProb = context.allowedProb;
								else
									context.maxReachedProb = np;
							}
						}
						return; // no more calcs - finish
					}
				}
				ThinkMoveFromHere(
					newPos,
					move,
					resGates,
					getProbsForMove(move.x, move.y), // todo - what if out of range?
					np, deep+1,
					fMove,
					context);
				float a = 0;
			}
		}
	}
	
	private void CheckVariant(
		Point firstMove,
		Point position,
		float currentProb,
		int currentGate,
		ThinkingContext context) throws InterruptedException
	{
		if (context.maxReachedProb < context.allowedProb)
		{
			boolean probBetter = (currentProb > context.maxReachedProb);
			boolean probSameResultBetter = 
				currentProb == context.maxReachedProb
				&& (currentGate > context.bestReachedGate ||
					(currentGate == context.bestReachedGate == position.y > context.bestReachedPos));
			
			if (probBetter || probSameResultBetter){
				setBestMove(firstMove.x, firstMove.y, context.reqNum);
				context.maxReachedProb = currentProb;
				context.bestReachedGate = currentGate;
				context.bestReachedPos = position.y;
				return;
			}
		}
		
		// too bad, but not better than last too bad
		if (currentProb < context.allowedProb) return;
		
		if (context.reachedFinishAtDeep < Integer.MAX_VALUE) return; // finish reached - this variant not interesting
		
		if (currentGate >= context.bestReachedGate && position.y > context.bestReachedPos){
			setBestMove(firstMove.x, firstMove.y, context.reqNum);
			context.bestReachedGate = currentGate;
			context.bestReachedPos = position.y;
		}
	}
	
	Lock lock = new ReentrantLock();
	private void setBestMove(int x, int y, int reqNum){
		lock.lock();
		try{
			if (requestNum != reqNum) return;
			if (bestMove == null) return;
			bestMove.x = x;
			bestMove.y = y;
			debugMap.put("foundBest", x + ":" + y);
		} finally{
			lock.unlock();
		}
	}
	
	private Point bestMove = null;
	private int requestNum = 0;
	
	private void initStats(){
		stats = new Probabilities[xcnt][ycnt];
		for (int x = 0; x < xcnt; x++)
			for (int y = 0; y < xcnt; y++){
				stats[x][y] = new Probabilities();
				stats[x][y].probs = buildNormProbs();
				stats[x][y].cnt = 0;
			}
	
		normalProbs = buildNormProbs();
	}
	
	private float[][] buildNormProbs(){
		int m = Constants.MaxPossibleMove;
		float[][] res = new float[2*m + 1][2*m+1];

		res[m][m] = 1f;
		res[m-1][m] = 1f;
		res[m+1][m] = 1f;
		res[m][m-1] = 1f;
		res[m][m+1] = 1f;
		
		return res;
	}
	
	private float[][] getProbsForMove(int x, int y){
		if (x < minx || x > maxx || y < miny || y > maxy) return normalProbs;
		
		return stats[x - minx][y - miny].probs;
	}
	
	private void updateStats(){
		
		if (game.route.currentPosition == 0) // first step is different
			return;
		
		boolean[][] posMoves = game.getPossibleMoves();
		Point lm = game.route.getLastMove();
		
		if (lm.x < minx || lm.x > maxx || lm.y < miny || lm.y > maxy) return;
		
		int m = Constants.MaxPossibleMove;
		Probabilities curr = stats[lm.x - minx][lm.y - miny];
		if (curr.cnt == 0){
			for (int x = 0; x<2*m+1; x++)
				for (int y = 0; y<2*m+1; y++){
					curr.probs[x][y] = posMoves[x][y] ? 1 : 0;
				}
			curr.cnt = 1;
		} else {
			float f1 = (float)curr.cnt / (float)(curr.cnt+1);
			float f2 = 1f / (float)(curr.cnt+1);
			for (int x = 0; x<2*m+1; x++)
				for (int y = 0; y<2*m+1; y++){
					curr.probs[x][y] = curr.probs[x][y] * f1 + f2 * (posMoves[x][y] ? 1f : 0f);
				}
			curr.cnt++;
		}
	}
}
