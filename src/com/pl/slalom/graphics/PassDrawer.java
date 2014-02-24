package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;

public class PassDrawer extends Drawer
{
	private static Paint paintGatePassed = new Paint();
	private static Paint paintGateFailed = new Paint();
	
	@Override
	protected void initialize()
	{
		paintGatePassed.setColor(getColor(R.color.gatePassed));
		paintGateFailed.setColor(getColor(R.color.gateFailed));
	}

	public void drawPass(Canvas canvas, float y, float x1, float x2){
		float dy = 0.05f;
		drawRect(x1, y - dy, x2, y + dy, canvas, paintGatePassed);
	}	
}
