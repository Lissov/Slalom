package com.pl.slalom.player.ski;
import com.pl.slalom.player.*;

public interface ISki
{
	int getId();
	String getName();
	int getPrice();
	boolean[][] getPossibleMoves(PlayerSkills skills, int x, int y, boolean canMove, boolean isStart, boolean flying);
}
