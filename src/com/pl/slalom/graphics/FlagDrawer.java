package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;

public class FlagDrawer extends Drawer
{
	private static Paint paintGround = new Paint();
	private static Paint paintPole = new Paint();
	private static Paint[] paintFlags;
	
	@Override
	protected void initialize()
	{
		paintGround.setColor(getColor(R.color.poleGround));
		paintPole.setColor(getColor(R.color.SFPole));
		
		paintFlags = new Paint[3];
		for (int i = 0; i < paintFlags.length; i++) 
			paintFlags[i] = new Paint();
		paintFlags[0].setColor(getColor(R.color.flag1));
		paintFlags[1].setColor(getColor(R.color.flag2));
		paintFlags[2].setColor(getColor(R.color.flag3));
	}

	public void draw(Canvas canvas, float x, float y, boolean isLeft, int cIndex){
		
		float ox = 0.3f;
		float oy = 0.15f;
		float px = 0.05f;
		float py = 0.9f;
		
		float fb = 0.4f;
		float fs = isLeft ? -0.3f : 0.3f;
		
		drawTriangle(canvas, paintFlags[cIndex], 
			x, y + py, x, y + fb, x + fs, y + (py + fb) / 2);
		
		drawOval(x-ox, y+oy, x+ox, y-oy, canvas, paintGround);
		drawRect(x - px, y, x + px, y + py, canvas, paintPole);		
	}
}
