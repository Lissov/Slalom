package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;

public class NextMoveDrawer extends Drawer
{
	Paint paintMove = new Paint();
	Paint paintLine = new Paint();
	
	@Override
	protected void initialize()
	{
		paintMove.setColor(getColor(R.color.slopeMove));
		paintLine.setColor(getColor(R.color.slopeMoveArrow));
	}

	public void draw(Canvas canvas, int cx, int cy, int newx, int newy, boolean[][] posMoves){
		int mm = Constants.MaxPossibleMove;
		for (int x = -mm; x <= mm; x++)
			for (int y = -mm; y <= mm; y++)
			{
				if (posMoves[x + mm][y + mm])
					drawLine(cx, cy, newx + x, newy + y, canvas, paintLine);
			}
		float xo = 0.3f;
		float yo = 0.15f;
		for (int x = -mm; x <= mm; x++)
			for (int y = -mm; y <= mm; y++)
			{
				if (posMoves[x + mm][y + mm]){
					float xx = newx + x;
					float yy = newy + y;
					drawOval(xx-xo, yy-yo, xx+xo, yy+yo, canvas, paintMove);
				}
			}
	}
}
