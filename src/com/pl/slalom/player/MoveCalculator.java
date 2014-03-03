package com.pl.slalom.player;

import java.util.Random;

import com.pl.slalom.Constants;
import com.pl.slalom.player.ski.SkiParameters;

public class MoveCalculator {

	private Random rnd = new Random();

	PlayerSkills plr; 
	SkiParameters ski;
	
	public MoveCalculator(PlayerSkills skills, SkiParameters parameters){
		this.plr = skills;
		this.ski = parameters;
	}
	
	public boolean[][] getPossibleMoves(int x, int y, boolean canMove, boolean isStart, boolean flying)
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
			float startSpeed = ski.startSpeed * plr.startSpeedK;
			for (int i = 2; i < startSpeed; i++){
				result[mm][mm+i] = true;	
			}
			return result;
		}

		if (flying){
			result[mm][mm] = true;
			return result;
		}
				
		float cspd = (float)Math.sqrt(x*x + y*y);
		int s = getMaxS(cspd);
		if (s > Constants.MaxPossibleMove) 
			s = Constants.MaxPossibleMove;
		for (int i = -mm; i <= mm; i++)
			for (int j = -mm; j <= mm; j++)
			{
				if (Math.abs(i) + Math.abs(j) > s) continue;

				int sx = x + i;
				int sy = y + j;
				
				if (sy == 0){
					float runStraightSpeed = ski.straightMaxSpeed * plr.strSkiMaxV_k;
					if (Math.abs(sx) > runStraightSpeed)
					{
						if (Math.abs(sx) > 1 && Math.abs(sx) > ski.straightDec * cspd) continue;
					} else {
						float maxSpd = cspd + ski.straightAcc * plr.strAcc_k;
						if (Math.abs(sx) > maxSpd) continue;
					}
				}
				
				if (sy < 0){
					float spd = (float)Math.sqrt(sx*sx + sy*sy);
					if (spd > ski.uphillMaxSpeed * plr.uphillSkiMaxV_k) {
						if (spd > ski.uphillDec * cspd) continue;
					}
				}
				
				result[i + mm][j + mm] = true;
			}
		
		return result;
	}
	
	private int getMaxS(float speed){
		float plM = 1f - plr.skiControl;
		float handling = plr.speedHandling > speed ? 1 : (plr.speedHandling / speed);
				
		int r = rnd.nextInt(100);
		float p = 0;
		int i = 1;
		while (i < ski.probs.length && r > p + ski.probs[i-1]*plM)
		{
			p += ski.probs[i-1]*plM;
			i++;
		}
		
		float maxS = i * handling;
		return maxS <= 1 ? 1 : Math.round(maxS);
	}
	
}
