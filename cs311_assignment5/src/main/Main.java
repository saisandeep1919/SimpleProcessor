package main;
import configuration.Configuration;
import generic.Misc;
import generic.Simulator;
import generic.Statistics;
import processor.Processor;
import processor.memorysystem.MainMemory;
import processor.pipeline.RegisterFile;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		if(args.length != 3)
		{
			Misc.printErrorAndExit("usage: java -jar <path-to-jar-file> <path-to-config-file> <path-to-stat-file> <path-to-object-file>\n");
		}
		
		Configuration.parseConfiguratioFile(args[0]);
		
		Processor processor = new Processor();
		
		Simulator.setupSimulation(args[2], processor);
		Statistics.openStatisticsFile(args[1]);
		Simulator.simulate();
		
		processor.printState(0, 50); // ((0, 0) refers to the range of main memory addresses we wish to print. this is an empty set.

		Statistics.closeStatisticsFile();
		
		System.out.println("Hash of the Processor State = "+getHashCode(processor.getRegisterFile(), processor.getMainMemory()));
	}
	
	static int getHashCode(RegisterFile registerState, MainMemory memoryState) {
		ArrayList<Integer> hash = new ArrayList<Integer>();
		
		hash.add(registerState.getProgramCounter());
		
		for(int i=0;i<32;i++) {
			hash.add(registerState.getValue(i));
		}
		
		for(int i=0;i<65536;i++) {
			hash.add(memoryState.getWord(i));
		}
		
		return hash.hashCode();
	}

}
