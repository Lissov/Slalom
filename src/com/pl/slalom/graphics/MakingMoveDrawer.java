package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;

public class MakingMoveDrawer extends Drawer
{
	private Paint paintLine = new Paint();
	private Paint paintGraphite = new Paint();
	private Paint paintWood = new Paint();
	private Paint paintHandle1 = new Paint();
	private Paint paintHandle2 = new Paint();
	private Paint paintHandle3 = new Paint();
	
	@Override
	protected void initialize()
	{
		paintLine.setColor(getColor(R.color.drawingLine));
		paintGraphite.setColor(getColor(R.color.pencilGraphite));
		paintWood.setColor(getColor(R.color.pencilWood));
		paintHandle1.setColor(getColor(R.color.pencilHandle1));
		paintHandle2.setColor(getColor(R.color.pencilHandle2));
		paintHandle3.setColor(getColor(R.color.pencilHandle3));
	}

	public void draw(Canvas canvas, float x, float y, float dx, float dy, float perc){
		float px = x + dx * perc;
		float py = y + dy * perc;
		
		drawLine(x, y, px, py, canvas, paintLine);
		double angle = Math.PI / 4;
		double a1 = Math.PI / 6;
		double a2 = Math.PI / 2 - a1;
		float gp1x = px + 0.15f * (float)Math.cos(a1);
		float gp1y = py + 0.15f * (float)Math.sin(a1);
		float gp2x = px + 0.15f * (float)Math.cos(a2);
		float gp2y = py + 0.15f * (float)Math.sin(a2);

		float wp1x = px + 0.4f * (float)Math.cos(a1);
		float wp1y = py + 0.4f * (float)Math.sin(a1);
		float wp2x = px + 0.4f * (float)Math.cos(a2);
		float wp2y = py + 0.4f * (float)Math.sin(a2);
		
		float hp1x = wp1x + 1.5f * (float)Math.cos(angle);
		float hp1y = wp1y + 1.5f * (float)Math.sin(angle);
		float hp2x = wp2x + 1.5f * (float)Math.cos(angle);
		float hp2y = wp2y + 1.5f * (float)Math.sin(angle);
		
		double a3 = a1 + (a2-a1) / 3;
		double a4 = a1 + 2*(a2-a1) / 3;
		
		float wp3x = px + 0.4f * (float)Math.cos(a3);
		float wp3y = py + 0.4f * (float)Math.sin(a3);
		float wp4x = px + 0.4f * (float)Math.cos(a4);
		float wp4y = py + 0.4f * (float)Math.sin(a4);

		float hp3x = wp3x + 1.5f * (float)Math.cos(angle);
		float hp3y = wp3y + 1.5f * (float)Math.sin(angle);
		float hp4x = wp4x + 1.5f * (float)Math.cos(angle);
		float hp4y = wp4y + 1.5f * (float)Math.sin(angle);
		
		drawTriangle(canvas, paintWood, px, py, wp1x, wp1y, wp2x, wp2y);
		drawTriangle(canvas, paintGraphite, px, py, gp1x, gp1y, gp2x, gp2y);
		
		drawTriangle(canvas, paintHandle3, wp1x, wp1y, hp1x, hp1y, wp3x, wp3y);
		drawTriangle(canvas, paintHandle3, hp3x, hp3y, hp1x, hp1y, wp3x, wp3y);

		drawTriangle(canvas, paintHandle2, wp3x, wp3y, hp3x, hp3y, wp4x, wp4y);
		drawTriangle(canvas, paintHandle2, hp4x, hp4y, hp3x, hp3y, wp4x, wp4y);

		drawTriangle(canvas, paintHandle1, wp4x, wp4y, hp4x, hp4y, wp2x, wp2y);
		drawTriangle(canvas, paintHandle1, hp2x, hp2y, hp4x, hp4y, wp2x, wp2y);
	}
}
