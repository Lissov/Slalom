package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;

public class FieldDrawer extends Drawer
{
	private Paint paintSnow = new Paint();
	private Paint paintGrid = new Paint();

	@Override
	protected void initialize()
	{
		paintSnow.setColor(getColor(R.color.snow));
		paintGrid.setColor(getColor(R.color.grid));
	}
	
	public void drawField(Canvas canvas, int slopeWidth){
		fillBackground(canvas);
		drawGrid(canvas, slopeWidth);
	}
	
	private void fillBackground(Canvas canvas){
			canvas.drawRect(0,0, canvas.getWidth(), canvas.getHeight(), paintSnow);
	}

	private void drawGrid(Canvas canvas, int slopeWidth){
		float yMax = transform.getMaxYVisible();
		float yMin = transform.getMinYVisible();
		float yMaxS = transform.getScreenY(yMax);
		float yMinS = transform.getScreenY(yMin);
		
		for (float y = (int)yMax + 1; y >= yMin; y--){
			float sy = transform.getScreenY(y);
			float xf = transform.getScreenX(-1.5f, y);
			float xt = transform.getScreenX(slopeWidth + 1.5f, y);
			canvas.drawLine(xf, sy, xt, sy, paintGrid); 
		}

		for (int x = 0; x <= slopeWidth; x++){
			float xt = transform.getScreenX(x, yMax);
			float xb = transform.getScreenX(x, yMin);
			canvas.drawLine(xt, yMaxS, xb, yMinS, paintGrid);
		}
	}
}
