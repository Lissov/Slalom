package com.pl.slalom.track;

public class SlopeManager
{
	public Slope getSlope(int number){
		switch (number){
			case 1: return getSlope1();
			case 2: return getSlope2();
			case 3: return getSlope3();
			default:
				return null;
		}
	}
	
	private Slope getSlope1(){
		Slope slope = new Slope();
		slope.name = "Short training course";
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
	
	private Slope getSlope2(){
		Slope slope = new Slope();
		slope.name = "Tramplin training";
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

	private Slope getSlope3(){
		Slope slope = new Slope();
		slope.name = "Slalom training course";
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
}
