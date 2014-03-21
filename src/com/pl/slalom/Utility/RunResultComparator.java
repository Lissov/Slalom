package com.pl.slalom.Utility;
import java.util.*;
import com.pl.slalom.data.race.*;

public class RunResultComparator
{
	public static Comparator<RunResult> getComparator(ResultMeasureType resType){
		switch (resType){
			case Turns:
				return new Comparator<RunResult>(){
					public int compare(RunResult rr1, RunResult rr2){
						return Integer.compare(rr1.turns, rr2.turns);
					}
				};
			case Time:
				return new Comparator<RunResult>(){
					public int compare(RunResult rr1, RunResult rr2){
						return Float.compare(rr1.time, rr2.time);
					}
				};
			case TurnsAndTime:
				return new Comparator<RunResult>(){
					public int compare(RunResult rr1, RunResult rr2){
						int r = Integer.valueOf(rr1.turns).compareTo(rr2.turns);
						if (r != 0) 
							return r;
						return Float.compare(rr1.time, rr2.time);
					}
				};
			default:
				return null;
		}
	}
}
