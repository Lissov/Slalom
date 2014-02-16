package com.pl.slalom.graphics;
import android.content.*;
import android.graphics.*;

public abstract class Drawer
{
	protected Context context;
	protected ICoordsTransform transform;
	
	public void init(Context context, ICoordsTransform transform){
		this.context = context;
		this.transform = transform;
		
		initialize();
	}
	
	protected abstract void initialize();
	
	protected int getColor(int resourceId){
		return context.getResources().getColor(resourceId);
	}
	
	protected RectF getRectFMed(float x1, float y1, float x2, float y2){
		float ym = (y1 + y2) / 2;
		return new RectF(
			transform.getScreenX(x1, ym),
			transform.getScreenY(y1),
			transform.getScreenX(x2, ym),
			transform.getScreenY(y2));
	}

	protected void drawTriangle(Canvas canvas, Paint paint, 
							  float x1, float y1,
							  float x2, float y2,
							  float x3, float y3){
		/*float[] vertices = new float[8];
		 vertices[0] = xc;
		 vertices[1] = yt;
		 vertices[2] = xc + dx;
		 vertices[3] = yb;
		 vertices[4] = xc - dx;
		 vertices[5] = yb;
		 vertices[6] = vertices[0];
		 vertices[7] = vertices[1];*/

		Path path = new Path();
		path.moveTo(transform.getScreenX(x1, y1), transform.getScreenY(y1));
		path.lineTo(transform.getScreenX(x2, y2), transform.getScreenY(y2));
		path.lineTo(transform.getScreenX(x3, y3), transform.getScreenY(y3));
		canvas.drawPath(path, paint);
	}
	
	protected void drawLine(float x1, float y1, float x2, float y2,
						Canvas canvas, Paint paint){
		canvas.drawLine(
			transform.getScreenX(x1, y1),
			transform.getScreenY(y1),
			transform.getScreenX(x2, y2),
			transform.getScreenY(y2),
			paint);
	}
	
	protected void drawOval(float x1, float y1, float x2, float y2,
							Canvas canvas, Paint paint){
		canvas.drawOval(getRectFMed(x1, y1, x2, y2), paint);
	}

	protected void drawRect(float x1, float y1, float x2, float y2,
							Canvas canvas, Paint paint){
		/*canvas.drawRect(
			transform.getScreenX(x1, y1),
			transform.getScreenY(y1),
			transform.getScreenX(x2, y2),
			transform.getScreenY(y2),
			paint);*/
		drawTriangle(canvas, paint, x1, y1, x2, y1, x1, y2);
		drawTriangle(canvas, paint, x2, y1, x2, y2, x1, y2);
	}
	
	protected void drawText(String text, float x, float y,
							Canvas canvas, Paint paint){
		canvas.drawText(
			text,
			transform.getScreenX(x, y),
			transform.getScreenY(y),
			paint);
	}
	
}
