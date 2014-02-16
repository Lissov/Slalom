package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;

public class TramplinDrawer extends Drawer
{
	private Paint paintTramplin = new Paint();

	@Override
	protected void initialize()
	{
		paintTramplin.setColor(getColor(R.color.tramplin));
		paintTramplin.setStyle(Paint.Style.FILL_AND_STROKE);
	}

	public void draw(Canvas canvas, float position, float left, float right, int size){
		float yt = position + 0.04f;
		float yb = position - 0.02f;
		drawRect(left, yt, right, yb, canvas, paintTramplin);
		drawLine(left, yb, left - 0.5f, yb - 0.5f, canvas, paintTramplin);
		drawLine(right, yb, right + 0.5f, yb - 0.5f, canvas, paintTramplin);
		
		float smw = 0.2f;
		for (int i = 0; i < size; i++){
			float d = 0.2f + i * 0.1f;
			drawLine(left - d, yb - d, left - d + smw, yb - d, canvas, paintTramplin);
			drawLine(right + d - smw, yb - d, right + d, yb - d, canvas, paintTramplin);
		}
	}
}
