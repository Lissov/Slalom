package com.pl.slalom.player.ski;

public class SkiManager
{
	public ISki getSki(int id){
		switch(id){
			case -1:
				return new Generic("Training one", 0,
					new int[] {0, 100, 0},
					5, 5, 5, 5);
			case -2:
				return new Generic("Training two", 0,
								   new int[] {0, 0, 100},
								   5, 5,
								   5, 5);
			case 10:
				return new Generic("Basic downhill", 50, 
								   new int[] {0, 100, 0},
								   0.45f, 0.4f,
								   1, 0);
			default:
				return null;
		}
	}
}
