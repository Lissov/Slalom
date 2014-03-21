package com.pl.slalom.track;

import com.pl.slalom.*;
import android.content.*;
import android.content.res.*;

public class SlopeManager
{
	public Slope getSlope(int number){
		switch (number){
			case Constants.Slopes.Training.first:
				return getSlope_train_first();
			case Constants.Slopes.Training.tramplin:
				return getSlope_train_tramplin();
			case Constants.Slopes.Training.slalom:
				return getSlope_train_slalom();
			case Constants.Slopes.Austria.hohewandwiese: 
				return getSlope_au_hww();
			default:
				return null;
		}
	}
	
	private Slope getSlope_train_first(){
		Slope slope = new Slope(Constants.Slopes.Training.first);
		slope.width = 12;
		slope.gates = new Gate[5];
		slope.tramplins = new Tramplin[0];
		
		slope.startPos = 6;
		slope.gates[0] = new Gate(0.1f, 5, 7, GateType.Start);
		slope.gates[1] = new Gate(3.05f, 5, 7, GateType.Flags);
		slope.gates[2] = new Gate(7.05f, 7, 9, GateType.Flags);
		slope.gates[3] = new Gate(11.05f, 3, 5, GateType.Flags);
		slope.gates[4] = new Gate(13.1f, 5, 7, GateType.Finish);
		

		return slope;
	}
	
	private Slope getSlope_train_tramplin(){
		Slope slope = new Slope(Constants.Slopes.Training.tramplin);
		slope.width = 20;
		slope.gates = new Gate[5];
		slope.tramplins = new Tramplin[1];
		
		slope.startPos = 10;
		slope.gates[0] = new Gate(0.1f, 9, 11, GateType.Start);
		slope.gates[1] = new Gate(5.05f, 8, 10, GateType.Flags);
		slope.tramplins[0] = new Tramplin(7.5f, 8, 12, 1);
		slope.gates[2] = new Gate(14.05f, 14, 18, GateType.Flags);
		slope.gates[3] = new Gate(17.05f, 5, 10, GateType.Flags);
		slope.gates[4] = new Gate(19.9f, 8, 12, GateType.Finish);

		return slope;
	}

	private Slope getSlope_train_slalom(){
		Slope slope = new Slope(Constants.Slopes.Training.slalom);
		slope.width = 18;
		slope.gates = new Gate[13];
		slope.tramplins = new Tramplin[1];

		slope.startPos = 9;
		slope.gates[0] = new Gate(0.1f, 8, 10, GateType.Start);
		slope.gates[1] = new Gate(3.05f, 8, 11, GateType.Flags);
		slope.gates[2] = new Gate(8.05f, 14, 17, GateType.Flags);
		slope.gates[3] = new Gate(13.05f, 5, 7, GateType.Flags);
		slope.gates[4] = new Gate(15.05f, 8, 10, GateType.Flags);
		slope.gates[5] = new Gate(19.05f, 12, 16, GateType.Banner);
		slope.tramplins[0] = new Tramplin(22.5f, 11, 17, 2);
		slope.gates[6] = new Gate(28.05f, 12, 16, GateType.Flags);
		slope.gates[7] = new Gate(31.05f, 8, 10, GateType.Flags);
		slope.gates[8] = new Gate(35.05f, 9, 12, GateType.Flags);
		slope.gates[9] = new Gate(39.05f, 7, 10, GateType.Flags);
		slope.gates[10] = new Gate(43.05f, 9, 12, GateType.Flags);
		slope.gates[11] = new Gate(48.05f, 8, 10, GateType.Flags);
		slope.gates[12] = new Gate(49.9f, 5, 13, GateType.Finish);
		
		return slope;
	}

	private Slope getSlope_au_hww(){
		Slope slope = new Slope(Constants.Slopes.Austria.hohewandwiese);
		slope.width = 13;
		slope.gates = new Gate[6];
		slope.tramplins = new Tramplin[0];

		slope.startPos = 7;
		slope.gates[0] = new Gate(0.1f, 6, 8, GateType.Start);
		slope.gates[1] = new Gate(3.05f, 6, 8, GateType.Flags);
		slope.gates[2] = new Gate(8.05f, 9, 12, GateType.Flags);
		slope.gates[3] = new Gate(13.05f, 2, 5, GateType.Flags);
		slope.gates[4] = new Gate(15.05f, 7, 10, GateType.Flags);
		slope.gates[5] = new Gate(18.95f, 6, 8, GateType.Finish);

		return slope;
	}
	
	public static String getSlopeName(Context context, int slopeId){
		Resources r = context.getResources();
		
		switch (slopeId)
		{
			case Constants.Slopes.Training.first:
				return r.getString(R.string.slope_train_first);
			case Constants.Slopes.Training.tramplin:
				return r.getString(R.string.slope_train_tramplin);
			case Constants.Slopes.Training.slalom:
				return r.getString(R.string.slope_train_slalom);
			case Constants.Slopes.Austria.hohewandwiese:
				return r.getString(R.string.slope_at_hww);
			default:
				return "Undefined";
		}
	}
}
