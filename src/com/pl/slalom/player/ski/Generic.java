package com.pl.slalom.player.ski;
import com.pl.slalom.*;
import com.pl.slalom.player.*;
import java.util.*;

public class Generic implements ISki
{
	private int id;
	private String name;
	private int price;
	private int[] probs;
	private float uphillDec;
	private float straightDec;
	private float uphillMaxSpeed;
	private float straightMaxSpeed;
	
	private Random rnd = new Random();

	public Generic(int id, String name, int price, int[] probs, float uphillDec, float straightDec, float uphillMaxSpeed, float straightMaxSpeed)
	{
		this.id = id;
		this.name = name;
		this.probs = probs;
		this.uphillDec = uphillDec;
		this.straightDec = straightDec;
		this.uphillMaxSpeed = uphillMaxSpeed;
		this.straightMaxSpeed = straightMaxSpeed;
		this.price = price;
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public int getPrice()
	{
		return price;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean[][] getPossibleMoves(PlayerSkills skills, int x, int y, boolean canMove, boolean isStart, boolean flying)
	{
		int mm = Constants.MaxPossibleMove;
		int mcnt = 2*mm + 1;
		boolean[][] result = new boolean[mcnt][mcnt];
		for (int i = 0; i < mcnt; i++)
			for (int j = 0; j < mcnt; j++)
				result[i][j] = false;
		
		if (!canMove) 
			return result;
			
		if (isStart){
			result[mm][mm+1] = true;
			return result;
		}
		
		if (flying){
			result[mm][mm] = true;
			return result;
		}
		
		int s = getMaxS();
		float cspd = (float)Math.sqrt(x*x + y*y);
		for (int i = -mm; i <= mm; i++)
			for (int j = -mm; j <= mm; j++)
			{
				if (Math.abs(i) + Math.abs(j) > s) continue;

				int sx = x + i;
				int sy = y + j;
				
				if (sy == 0){
					if (Math.abs(sx) > straightMaxSpeed)
					{
						if (Math.abs(sx) > straightDec * cspd) continue;
					}
				}
				
				if (sy < 0){
					float spd = (float)Math.sqrt(sx*sx + sy*sy);
					if (spd > uphillMaxSpeed) {
						if (spd > uphillDec * cspd) continue;
					}
				}
				
				result[i + mm][j + mm] = true;
			}
		
		return result;
	}
	
	private int getMaxS(){
		int r = rnd.nextInt(100);
		float p = 0;
		for (int i = 0; i < probs.length; i++)
		{
			if (r < p + probs[i])
				return i;
			p += probs[i];
		}
		
		return 0;
	}
}
