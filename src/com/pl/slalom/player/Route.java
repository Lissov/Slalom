package com.pl.slalom.player;

import com.pl.slalom.*;
import android.graphics.*;

public class Route
{
	public int[] positionsX;
	public int[] positionsY;
	public int currentPosition;
	public int passedCount;
	
	public Route(int startX, int startY){
		positionsX = new int[Constants.MaxRecordedSteps];
		positionsY = new int[Constants.MaxRecordedSteps];
		
		currentPosition = 0;
		positionsX[currentPosition] = startX;
		positionsY[currentPosition] = startY;
		
		passedCount = 0;
	}
	
	public Point getLastMove(){
		if (currentPosition == 0)
			return new Point(0, 0);
		
		int prevPosition = currentPosition - 1;
		return new Point(
			positionsX[currentPosition] - positionsX[prevPosition],
			positionsY[currentPosition] - positionsY[prevPosition]);
	}
	
	public void makeMove(int x, int y){
		
		currentPosition++;
			
		positionsX[currentPosition] = positionsX[currentPosition - 1] + x;
		positionsY[currentPosition] = positionsY[currentPosition - 1] + y;
	}
}
