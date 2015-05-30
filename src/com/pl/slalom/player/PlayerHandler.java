package com.pl.slalom.player;
import com.pl.slalom.player.ai.*;
import java.util.*;

public abstract class PlayerHandler
{
	protected Game game;

	public PlayerHandler(Game game)
	{
		this.game = game;
	}
	
	public abstract boolean canMove();
	public abstract boolean[][] getPossibleMoves();
	public abstract void makeMove(int dx, int dy);
	public abstract void moveComplete();
	
	public static PlayerHandler getHandler(Game game, int ai_id){
		if (ai_id == PlayerManager.AIID_Human)
			return new PlayerHandlerHuman(game);
		
		return new PlayerHandlerAI(game, PlayerManager.getPlayer(ai_id, game));
	}
	
	//TODO: remove
	public abstract HashMap<String, String> GetDebugInfo();
}
