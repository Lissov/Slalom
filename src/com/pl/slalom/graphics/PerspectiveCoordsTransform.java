package com.pl.slalom.graphics;

public class PerspectiveCoordsTransform implements ICoordsTransform
{
	private float scale;
	private float borderAbs;
	private float yProgress;
	private float canvasHeight;
	private float trackW;
	
	private float perspectiveFactor = 0.5f;
	private float perspM;
	
	public PerspectiveCoordsTransform(float scale, float borderAbs, float canvasHeight, float canvasWidth){
		this.scale = scale;
		this.borderAbs = borderAbs;
		this.canvasHeight = canvasHeight;
		trackW = canvasWidth - 2*borderAbs;
		
		perspM = perspectiveFactor * scale / canvasHeight;
	}

	@Override
	public void setYProgress(float value)
	{
		this.yProgress = value;
	}
	
	private float perspFactor(float gamey){
		return (1f - gamey * perspM);
	}
	
	@Override
	public float getScaleXAtPoint(float gamex, float gamey)
	{
		return scale * perspFactor(gamey);
	}

	@Override
	public float getScaleYAtPoint(float gamex, float gamey)
	{
		return scale;
	}

	@Override
	public float getScreenX(float gamex, float gamey)
	{
		return borderAbs + (gamex * scale) * perspFactor(gamey)  + (1f - perspFactor(gamey)) * 0.5f * trackW;
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
