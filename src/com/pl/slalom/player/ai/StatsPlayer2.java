package com.pl.slalom.player.ai;
import com.pl.slalom.player.*;
import com.pl.slalom.*;
import android.graphics.*;
import java.util.*;
import com.pl.slalom.track.*;

public class StatsPlayer2 extends StatisticalPlayer
{
	public StatsPlayer2(Game game){
		super(game);
	}

	@Override
	protected void ThinkTheMove(boolean[][] posMoves, Point lm, int reqNum)
	{
		//
	}
	
	private MoveResult getBestMove(
		Point position, Point lastMove,
		float[][] probs,
		int deep,
		float requiredProb,
		SPContext context)
	{
		if (deep == context.maxDeep){
			return getTerminal();
		}
		
		List<MoveResult> results = new LinkedList<MoveResult>();
		int m = Constants.MaxPossibleMove;
		for (int x = -m; x <= m; x++){
			for (int y = -m; y<=m; y++){
				if (probs[x+m][y+m] == 0f) continue;
				//if (!canMove(lastMove.x + x, lastMove.y + y)) continue;
			}
		}
		
		return null;
	}
	
/*	private boolean canMove(int x, int y, Point lastMove, Point position, 
		int lastPassedGate, SPContext context)
	{
		
	}*/
	
	private class SPContext
	{
		public int maxDeep;
		public Slope slope;
	}
	
	private class MoveResult
	{
		public float prob;
	}
	
	private MoveResult getTerminal(){
		MoveResult res = new MoveResult();
		res.prob = 1f;
		return res;
	}
}
