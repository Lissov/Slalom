package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;

public class PlayerDrawer extends Drawer
{
	private Paint paintActive = new Paint();
	private Paint paintInactive = new Paint();
	
	@Override
	protected void initialize()
	{
		paintActive.setColor(getColor(R.color.playerActive));
		paintInactive.setColor(getColor(R.color.playerInactive));
	}

	public void draw(Canvas canvas, float x, float y, boolean isActive){
		float dx = 0.1f;
		float dy = 0.08f;
		
		drawOval(x-dx, y-dy, x+dx, y+dy, canvas, (isActive ? paintActive : paintInactive));
	}
}
