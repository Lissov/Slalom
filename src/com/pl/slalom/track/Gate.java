package com.pl.slalom.track;

public class Gate
{
	public float position;
	public int leftPos;
	public int rightPos;
	public GateType gateType;
	
	public boolean crossed;

	public Gate(float position, int leftPos, int rightPos, GateType gateType)
	{
		this.position = position;
		this.leftPos = leftPos;
		this.rightPos = rightPos;
		this.gateType = gateType;
	}
}
