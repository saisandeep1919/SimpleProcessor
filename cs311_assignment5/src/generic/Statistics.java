package generic;

import processor.pipeline.RegisterFile;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Statistics {
	
	// TODO add your statistics here
	public static int numberOfInstructions;
	public static int numberOfCycles;
	static ArrayList <StatHolder> statDats = new ArrayList<StatHolder>();
	public static StatHolder tempHolder;
	public static String fileName = "";
	public static PrintWriter writer;
	public static int OFhalt;
	public static int wrongBranch;

	public static void openStatisticsFile(String name){
		fileName = name;
		try {
			writer = new PrintWriter(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void printStatisticsInfo()
	{
		try
		{
			//PrintWriter writer = new PrintWriter(fileName);
			
			//writer.println("Number of instructions executed = " + numberOfInstructions);
			//writer.println("Number of cycles taken = " + numberOfCycles);
			writer.println("Format :");
			writer.println("      PC - pc   Instruction - xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx (Integer val)   Cycle - cy   IF (clk)   OF (clk)   EX (clk)   MA (clk)   RW (clk)");
			writer.println(" ");
			writer.println(" ");

			//writer.close();

		}
		catch(Exception e)
		{
			Misc.printErrorAndExit(e.getMessage());
		}
	}

	public static void printStatistics(){

		for(int i=0;i<statDats.size();i++){
			writer.println(
					"PC - " + statDats.get(i).pc + " " +  getZeroes((String.valueOf(statDats.get(i).pc).length()), 6)
							+ "Instruction - "+ statDats.get(i).insStr  + getZeroes(statDats.get(i).insStr.length(), 32) + " (" + statDats.get(i).insInt + ")   "
							+ getZeroes((String.valueOf(statDats.get(i).insInt).length()), 15)
							+ "Cycle - " + statDats.get(i).cycle + " " +  getZeroes((String.valueOf(statDats.get(i).cycle).length()), 15)
							+ "RW (" + statDats.get(i).operations.get(0).clock + ")" + " "  + getZeroes((String.valueOf(statDats.get(i).operations.get(0).clock).length()), 7)
							+ "MA (" + statDats.get(i).operations.get(1).clock + ")" + " "  + getZeroes((String.valueOf(statDats.get(i).operations.get(1).clock).length()), 7)
							+ "EX (" + statDats.get(i).operations.get(2).clock + ")" + " "  + getZeroes((String.valueOf(statDats.get(i).operations.get(2).clock).length()), 7)
							+ "OF (" + statDats.get(i).operations.get(3).clock + ")" + " "  + getZeroes((String.valueOf(statDats.get(i).operations.get(3).clock).length()), 7)
							+ "IF (" + statDats.get(i).operations.get(4).clock + ")" + " "  + getZeroes((String.valueOf(statDats.get(i).operations.get(4).clock).length()), 7)
							+ "[[ REG - " + statDats.get(i).regString + "]]"
			);
		}
		numberOfInstructions = numberOfInstructions + statDats.size();
		statDats = new ArrayList<StatHolder>();
	}

	public static void closeStatisticsFile(){
//		writer.println("");
		writer.println("Number of instructions executed = " + numberOfInstructions);
		writer.println("Number of cycles taken = " + numberOfCycles);
		writer.println("Number of times OF halted " + OFhalt);
		writer.println("Number of times wrong branch taken " + wrongBranch);
		writer.close();
	}

	
	// TODO write functions to update statistics
	public static void setOFhalt(int halt){
		Statistics.OFhalt = halt;
	}

	public static void incrementOFhalt(){
		Statistics.OFhalt++;
	}

	public static void setWrongBranch(int wrong){
		Statistics.wrongBranch = wrong;
	}

	public static void incrementWrongBranch(){
		Statistics.wrongBranch++;
	}

	public static void setNumberOfInstructions(int numberOfInstructions) {
		Statistics.numberOfInstructions = numberOfInstructions;
	}

	public static void setNumberOfCycles(int numberOfCycles) {
		Statistics.numberOfCycles = numberOfCycles;
	}

	public static void incrementCycles(){
		Statistics.numberOfCycles++;
	}

	public static void incrementInstructions(){
		Statistics.numberOfInstructions++;
	}

	public static void decrementInstructinos(){
		Statistics.numberOfInstructions--;
	}

	public static void newStatHolder(int pc, int insInt, String insStr, int cycle){

		tempHolder = new StatHolder();
		tempHolder.pc = pc;
		tempHolder.insInt = insInt;
		tempHolder.insStr = insStr;
		tempHolder.cycle = cycle;
		tempHolder.operations = new ArrayList<StatHolderHelper>();
	}

	public static void addRegString(String reg){
		tempHolder.regString = reg;
	}

	public static void newAddOperation(String type, int clock){
		tempHolder.operations.add(new StatHolderHelper(type, clock));
	}

	public static void newUploadStatHolder(){
		statDats.add(tempHolder);
	}

	public static class StatHolder{
		public int pc;
		public int insInt;
		public String insStr;
		public int cycle;
		public ArrayList<StatHolderHelper> operations;
		public String regString = "";
	}

	public static class StatHolderHelper{
		public String operation;
		public int clock;

		public StatHolderHelper(String op, int clk){
			operation = op;
			clock = clk;
		}

	}

	public static String getZeroes(int l,int l1){
		String ans = "";
		int offset = l1- l;
		for(int i=0;i<offset;i++){
			ans = ans + " ";
		}
		return ans;
	}
}
