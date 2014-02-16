package com.pl.slalom.graphics.controls;
import android.graphics.*;
import android.content.*;
import com.pl.slalom.*;
import com.pl.slalom.graphics.*;

public class LastMoveDrawer
{
	private Paint paintLastMove = new Paint();
	private float arrowSize;
	
	public void initialize(Context context, float controlSize)
	{
		paintLastMove.setColor(context.getResources().getColor(R.color.controlLastMove));

		paintLastMove.setStrokeWidth(controlSize* 0.05f);
		arrowSize = controlSize * 0.3f;
	}
	
	public void draw(Canvas canvas, float x1, float y1, float x2, float y2)
	{
		double angle = Math.atan2(y2-y1, x2-x1);
		float lx = x2 - (arrowSize * 2 / 3) * (float)Math.cos(angle);
		float ly = y2 - (arrowSize * 2 / 3) * (float)Math.sin(angle);
		
		canvas.drawLine(x1, y1, lx, ly, paintLastMove);
		
		float x3 = x2 - arrowSize * (float)Math.cos(angle + 0.2);
		float y3 = y2 - arrowSize * (float)Math.sin(angle + 0.2);
		float x4 = x2 - arrowSize * (float)Math.cos(angle - 0.2);
		float y4 = y2 - arrowSize * (float)Math.sin(angle - 0.2);
		Path path = new Path();
		path.moveTo(x2, y2);
		path.lineTo(x3, y3);
		path.lineTo(x4, y4);
		canvas.drawPath(path, paintLastMove);
	}
}
