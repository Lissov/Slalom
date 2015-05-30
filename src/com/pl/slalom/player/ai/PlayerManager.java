package com.pl.slalom.player.ai;
import com.pl.slalom.player.*;

public class PlayerManager
{
	public static final int AIID_Human = -1;
	public static final int AIID_NormalStat = 10;
	
	public static PlayerType[] AvailableTypes = new PlayerType[] {
		new PlayerType(AIID_Human, "Player"),
		new PlayerType(AIID_NormalStat, "AI Normal")
	};
	
	public static ArtIntelBase getPlayer(int id, Game game){
		switch (id){
			case AIID_NormalStat:
				return new StatisticalPlayer(game);
			default:
				return null;
		}
	}
}
