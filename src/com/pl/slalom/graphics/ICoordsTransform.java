package com.pl.slalom.graphics;
import android.graphics.*;

public interface ICoordsTransform
{
	void setYProgress(float value);
	float getScreenX(float gamex, float gamey);
	float getScreenY(float gamey);
	float getScaleXAtPoint(float gamex, float gamey);
	float getScaleYAtPoint(float gamex, float gamey);
	float getMinYVisible();
	float getMaxYVisible();
	PointF toFieldPoint(float screenx, float screeny);
}
