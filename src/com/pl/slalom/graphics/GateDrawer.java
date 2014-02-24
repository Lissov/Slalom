package com.pl.slalom.graphics;
import android.graphics.*;
import com.pl.slalom.*;
import com.pl.slalom.track.*;

public class GateDrawer extends Drawer
{
	private static Paint paintGround = new Paint();
	private static Paint paintPole = new Paint();
	private static Paint paintFlagS = new Paint();
	private static Paint paintFlagF = new Paint();
	private static Paint paintFlagT = new Paint();
	private static Paint paintTextS = new Paint();
	private static Paint paintTextF = new Paint();
	private static Paint paintTextT = new Paint();
	private static Paint paintRope = new Paint();
	
	private static Paint[] paintFlags;
	
	@Override
	protected void initialize()
	{
		paintGround.setColor(getColor(R.color.poleGround));
		paintPole.setColor(getColor(R.color.SFPole));
		paintFlagS.setColor(getColor(R.color.bannerStart));
		paintFlagF.setColor(getColor(R.color.bannerFinish));
		paintFlagT.setColor(getColor(R.color.bannerText));
		paintRope.setColor(getColor(R.color.rope));
		paintTextS.setColor(getColor(R.color.bannerTxtStart));
		paintTextF.setColor(getColor(R.color.bannerTxtFinish));
		paintTextT.setColor(getColor(R.color.bannerTxtText));
		paintTextS.setTextAlign(Paint.Align.CENTER);
		paintTextF.setTextAlign(Paint.Align.CENTER);
		paintTextT.setTextAlign(Paint.Align.CENTER);
		
		paintFlags = new Paint[3];
		for (int i = 0; i < paintFlags.length; i++) 
			paintFlags[i] = new Paint();
		paintFlags[0].setColor(getColor(R.color.flag1));
		paintFlags[1].setColor(getColor(R.color.flag2));
		paintFlags[2].setColor(getColor(R.color.flag3));
	}

	public void draw(Canvas canvas, float x1, float x2, float y, 
			GateType mode, String text,
			int colorIndex){
		float fh = (x2 - x1 >= 2) ? 2f : 0.8f;
		
		float ox = 0.3f;
		float oy = 0.15f;
		float px = 0.05f;
		float py = 0.95f * fh;
		
		drawOval(x1-ox, y+oy, x1+ox, y-oy, canvas, paintGround);
		drawRect(x1 - px, y, x1 + px, y + py, canvas, paintPole);		
		drawOval(x2-ox, y+oy, x2+ox, y-oy, canvas, paintGround);
		drawRect(x2 - px, y, x2 + px, y + py, canvas,  paintPole);
		
		float ryt = y + 0.925f * fh;
		float ryb = y + 0.525f * fh;
		float fyt = y + 0.9f * fh;
		float fyb = y + 0.55f * fh;

		float rx = 0.1f * fh;
		
		drawLine(x1, ryt, x1 + rx, fyt, canvas, paintRope);
		drawLine(x1, ryb, x1 + rx, fyb, canvas, paintRope);
		drawLine(x2, ryt, x2 - rx, fyt, canvas, paintRope);
		drawLine(x2, ryb, x2 - rx, fyb, canvas, paintRope);
		
		Paint pFlag = getBannerPaint(mode, colorIndex);
		Paint pText = getTextPaint(mode);
		float xc = (x1 + x2) / 2;
		float yc = (fyt + fyb) / 2;
		float sy = transform.getScaleYAtPoint(xc, yc);
		float sx = transform.getScaleXAtPoint(xc, yc);
		float th = 0.6f;
		pText.setTextSize(sy * th);

		float w = pText.measureText(text);
		float bannerMaxTextW = (x2 - x1 - 2 * rx - 0.1f);
		if (w >= bannerMaxTextW * sx)
		{
			th = th * sx * bannerMaxTextW / w;
			pText.setTextSize(sy * th);
		}
		
		drawRect(x1 + rx, fyt, x2 - rx, fyb, canvas, pFlag);
		drawText(text, xc, yc - 0.4f * th, canvas, pText);
	}
	
	private Paint getBannerPaint(GateType mode, int cIndex){
		switch (mode){
			case Start:
				return paintFlagS;
			case Finish:
				return paintFlagF;
			case SmallBanners:
				return paintFlags[cIndex];
			default:
				return paintFlagT;
		}
	}
	
	private Paint getTextPaint(GateType mode){
		switch (mode){
			case Start:
				return paintTextS;
			case Finish:
				return paintTextF;
			default:
				return paintTextT;
		}
	}
}
