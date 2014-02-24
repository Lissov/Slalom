package com.pl.slalom.graphics.controls;
import com.pl.slalom.*;
import android.graphics.*;
import android.content.*;

public class ButtonMoveDrawer
{
	private Paint paintCirleA = new Paint();
	private Paint paintCirleI = new Paint();
	private Paint paintGridA = new Paint();
	private Paint paintGridI = new Paint();
	private Paint paintMoveA = new Paint();
	private Paint paintMoveI = new Paint();
	private float controlR;
	
	public void initialize(Context context, float controlSize)
	{
		paintCirleA.setColor(context.getResources().getColor(R.color.controlCircleActive));
		paintCirleI.setColor(context.getResources().getColor(R.color.controlCircleInactive));

		controlR = 0.8f * controlSize / 2;
		paintCirleA.setStrokeWidth(controlR * 0.2f);
		paintCirleI.setStrokeWidth(controlR * 0.1f);
		
		paintCirleA.setStyle(Paint.Style.STROKE);
		paintCirleI.setStyle(Paint.Style.STROKE);
		
		paintGridA.setColor(context.getResources().getColor(R.color.controlGridA));
		paintGridI.setColor(context.getResources().getColor(R.color.controlGridI));
		paintMoveA.setColor(context.getResources().getColor(R.color.controlMoveA));
		paintMoveI.setColor(context.getResources().getColor(R.color.controlMoveI));
	}

	public void draw(Canvas canvas, 
		float x, float y, boolean isActive,
		int dx, int dy, int maxSteps){
		
		//grid
		float gridR = controlR * 0.8f;
		float gridS = gridR / maxSteps;
		Paint pGrid = isActive ? paintGridA : paintGridI;
		for (int sx = -maxSteps; sx<= maxSteps; sx++){
			float xd = sx * gridS;
			float yd = (float)Math.sqrt(gridR * gridR - xd * xd);
			canvas.drawLine(x + xd, y - yd, x + xd, y + yd, pGrid);
		}
		for (int sy = -maxSteps; sy<= maxSteps; sy++){
			float yd = sy * gridS;
			float xd = (float)Math.sqrt(gridR * gridR - yd * yd);
			canvas.drawLine(x - xd, y + yd, x + xd, y + yd, pGrid);
		}
		
		//move
		Paint pMove = isActive ? paintMoveA : paintMoveI;
		float mx = x + dx * gridS;
		float my = y - dy * gridS;
		canvas.drawLine(x, y, mx, my, pMove);
		float ro = gridS * 0.25f;
		canvas.drawOval(new RectF(mx - ro, my - ro, mx + ro, my + ro), pMove);
		
		// ontrol oval
		canvas.drawOval(
			new RectF(x - controlR, y - controlR, 
		 	x + controlR, y + controlR),
			isActive ? paintCirleA : paintCirleI);
		
	}
}
