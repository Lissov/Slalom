package com.pl.slalom.player.ski;

public interface ISki
{
	int getId();
	String getName();
	int getPrice();
	boolean[][] getPossibleMoves(int x, int y, boolean canMove, boolean isStart, boolean flying);
}
