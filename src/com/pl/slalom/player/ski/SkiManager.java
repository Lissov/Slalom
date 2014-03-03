package com.pl.slalom.player.ski;

public class SkiManager
{
	public ISki getSki(int id){
		switch(id){
			case -1:
				return new Generic(-1, "Training one", 0,
									new SkiParameters(new int[] {100, 0, 0}, 5, 5, 1f, 5, 5, 1));
			case -2:
				return new Generic(-2, "Training two", 0,
								   new SkiParameters(new int[] {0, 100, 0}, 5, 5, 1f, 5, 5, 2));
			case 10:
				return new Generic(10, "Basic downhill", 50, 
									new SkiParameters(new int[] {100, 0, 0}, 0.65f, 0.99f, 1f, 0f, 1f, 1));
			case 20:
				return new Generic(20, "Normal max2", 150, 
								   new SkiParameters(new int[] {30, 70, 0}, 0.65f, 0.99f, 1f, 0f, 1f, 1));
			default:
				return null;
		}
	}
}
