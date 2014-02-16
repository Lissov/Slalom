package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;
import android.content.*;

public class TreeDrawer extends Drawer
{
    //private float scale;
	private Paint paintGround = new Paint();
	private Paint paintTrunk = new Paint();
	private Paint paintLeaves = new Paint();

	@Override
	protected void initialize()
	{
		paintGround.setColor(getColor(R.color.firGround));
		paintTrunk.setColor(getColor(R.color.firTrunk));
		paintLeaves.setColor(getColor(R.color.firLeaves));
		paintLeaves.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	@Override
	public void draw(Canvas canvas, float x, float y)
	{
		float ox = 0.15f;
		float oy = 0.1f;
		drawOval(x-ox, y+oy, x+ox, y-oy, canvas, paintGround);
		
		ox = 0.05f;
		oy = 0.2f;
		drawRect(x - ox, y + oy, x+ox, y, canvas, paintTrunk);
		
		drawFirTriangle(canvas, paintLeaves, x, 0.3f, y + 0.5f, y + 0.2f);
		drawFirTriangle(canvas, paintLeaves, x, 0.2f, y + 0.7f, y + 0.4f);
		drawFirTriangle(canvas, paintLeaves, x, 0.1f, y + 0.9f, y + 0.6f);
	}
	
	private void drawFirTriangle(Canvas canvas, Paint paint, 
							  float xc, float dx, float yt, float yb){
		drawTriangle(canvas, paint, xc, yt, xc + dx, yb, xc - dx, yb);
	}
}
