package com.pl.slalom.player.ski;

public interface ISki
{
	String getName();
	int GetPrice();
	boolean[][] getPossibleMoves(int x, int y, boolean canMove, boolean isStart, boolean flying);
}
