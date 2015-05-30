package com.pl.slalom.track;
import android.graphics.*;

public class TrackCalculator
{
	public static PassType getCross(
		float y, float left, float right,
		int x1, int y1, int x2, int y2)
	{
		if (y < y1 || y > y2) 
			return PassType.None;

		float crossX = x1 + (x2 - x1) * (y - y1) / (y2 - y1);

		if (crossX > left && crossX < right)
			return PassType.Pass;
		if (crossX == left || crossX == right)
			return PassType.Hit;
		return PassType.Miss;
	}

	public static PassType getGateCross(Gate g, Point position, int x, int y){
		return getCross(g.position, g.leftPos, g.rightPos,
			position.x, position.y, position.x + x, position.y + y);
	}
}
