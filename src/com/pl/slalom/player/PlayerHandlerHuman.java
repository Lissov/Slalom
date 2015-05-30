package com.pl.slalom.player;
import java.util.*;

public class PlayerHandlerHuman extends PlayerHandler
{
	public PlayerHandlerHuman(Game game)
	{
		super(game);
	}
	
	@Override
	public boolean canMove()
	{
		return true;
	}
	
	@Override
	public boolean[][] getPossibleMoves(){
		return game.getPossibleMoves();
	}
	
	@Override
	public void makeMove(int dx, int dy){
		game.makeMove(dx, dy);
	}

	@Override
	public void moveComplete()
	{
		// nothing to do
	}

	@Override
	public HashMap<String, String> GetDebugInfo()
	{
		return null;
	}
}
