package com.pl.slalom.graphics;

public class StraightCoordsTransform implements ICoordsTransform
{
	private float scale;
	private float borderAbs;
	private float yProgress;
	private float canvasHeight;
	
	public StraightCoordsTransform(float scale, float borderAbs, float canvasHeight){
		this.scale = scale;
		this.borderAbs = borderAbs;
		this.canvasHeight = canvasHeight;
	}

	@Override
	public void setYProgress(float value)
	{
		this.yProgress = value;
	}
	
	@Override
	public float getScaleXAtPoint(float gamex, float gamey)
	{
		return scale;
	}

	@Override
	public float getScaleYAtPoint(float gamex, float gamey)
	{
		return scale;
	}

	@Override
	public float getScreenX(float gamex, float gamey)
	{
		return borderAbs + (gamex * scale);
	}

	@Override
	public float getScreenY(float gamey)
	{
		return canvasHeight - ( (gamey - yProgress) * scale);
	}
	
	@Override
	public float getMinYVisible(){
		return yProgress;
	}

	@Override
	public float getMaxYVisible(){
		return (canvasHeight / scale) + yProgress;
	}
}
