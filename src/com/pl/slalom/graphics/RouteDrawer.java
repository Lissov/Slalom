package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;

public class RouteDrawer extends Drawer
{
	private Paint paintLine = new Paint();

	@Override
	protected void initialize()
	{
		paintLine.setColor(getColor(R.color.routeLine));
	}
	
	public void draw(Canvas canvas, int x1, int y1, int x2, int y2){
		drawLine(x1, y1, x2, y2, canvas, paintLine);
	}
}
