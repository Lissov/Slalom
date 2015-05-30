package com.pl.slalom.player;
import com.pl.slalom.*;
import com.pl.slalom.player.ai.*;
import android.graphics.*;
import java.util.*;

public class PlayerHandlerAI extends PlayerHandler
{
	ArtIntelBase player;
	public PlayerHandlerAI(Game game, ArtIntelBase player)
	{
		super(game);
		this.player = player;
		
		int m = Constants.MaxPossibleMove;
		tempPossMoves = new boolean[2*m+1][2*m+1];
		tempPossMoves[m][m] = true;
	}

	@Override
	public boolean canMove()
	{
		return true;
	}

	private boolean[][] tempPossMoves;
	@Override
	public boolean[][] getPossibleMoves(){
		return tempPossMoves;
	}
	

	@Override
	public void makeMove(int dx, int dy){
		Point move = player.getNextMove();
		game.makeMove(move.x, move.y);
	}

	@Override
	public void moveComplete()
	{
		player.startThinking();
	}

	@Override
	public HashMap<String, String> GetDebugInfo()
	{
		return player.debugMap;
	}
}
