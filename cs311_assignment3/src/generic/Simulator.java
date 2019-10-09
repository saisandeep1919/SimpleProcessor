package generic;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import processor.Clock;
import processor.Processor;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}


	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */

		ArrayList<Integer> fileValues = new ArrayList<Integer>();
		try {
			FileInputStream fis = new FileInputStream(assemblyProgramFile);
			DataInputStream dis = new DataInputStream(fis);
			while(dis.available() > 0){
				int temp = dis.readInt();
				fileValues.add(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		int pc = fileValues.get(0);
		processor.getRegisterFile().setProgramCounter(pc);
		processor.getRegisterFile().setValue(0, 0);
		processor.getRegisterFile().setValue(1, 65535);
		processor.getRegisterFile().setValue(2, 65535);

		int pointer = 0;
		for(int i=1;i<pc;i++){
			processor.getMainMemory().setWord(i-1, fileValues.get(i));
		}
		int indexDataEnd = pc - 1;
		int indexInsStart = pc;
		int indexInsEnd = fileValues.size();
		for(int i=pc;i<fileValues.size();i++){
			processor.getMainMemory().setWord(i-1, fileValues.get(i));
		}


		System.out.println();

	}
	
	public static void simulate()
	{
		Statistics.setNumberOfCycles(0);
		Statistics.printStatisticsInfo();
		int pc = 0;
		int insInt = 0;
		String insString = "";
		int cycle = 0;
		while(simulationComplete == false)
		{
			Statistics.incrementCycles();
			//System.out.println(processor.getRegisterFile().getProgramCounter() + "REG x5 : " + processor.getRegisterFile().getValue(5));
			processor.getIFUnit().performIF();
			pc = processor.getRegisterFile().getProgramCounter() - 1;
			insInt = processor.getMainMemory().getWord(pc);
			insString = Integer.toBinaryString(insInt);
			cycle = Statistics.numberOfCycles;
			Statistics.newStatHolder(pc, insInt, insString, cycle);
			Clock.incrementClock();
			Statistics.newAddOperation("IF", (int)Clock.getCurrentTime());
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			Statistics.newAddOperation("OF", (int)Clock.getCurrentTime());
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			Statistics.newAddOperation("EX", (int)Clock.getCurrentTime());
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			Statistics.newAddOperation("MA", (int)Clock.getCurrentTime());
			processor.getRWUnit().performRW();
			Clock.incrementClock();
			Statistics.newAddOperation("RW", (int)Clock.getCurrentTime());
			Statistics.addRegString(processor.getRegisterFile().getContentsAsMyString());
			Statistics.newUploadStatHolder();
			Statistics.printStatistics(); //Automatically updates the number of instructions
		}
		


	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
