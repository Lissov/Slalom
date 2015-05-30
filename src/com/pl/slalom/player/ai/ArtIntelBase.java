package com.pl.slalom.player.ai;
import com.pl.slalom.player.*;
import android.graphics.*;
import java.util.*;

public abstract class ArtIntelBase
{
	protected Game game;
	
	public ArtIntelBase(Game game)
	{
		this.game = game;
	}
	
	public abstract Point getNextMove();
	public void startThinking(){};
	
	//TODO: remove
	public HashMap<String, String> debugMap = new HashMap<String, String>();
}
