package processor.pipeline;

import generic.Statistics;

public class RegisterFile {
	int[] registerFile;
	int programCounter;
	
	public RegisterFile()
	{
		registerFile = new int[32];
		registerFile[0]=0;			//%xo is always 0 [RISC V]
	}
	
	public int getValue(int registerNumber)
	{
		return registerFile[registerNumber];
	}
	
	public void setValue(int registerNumber, int value)
	{
		registerFile[registerNumber] = value;
	}

	public int getProgramCounter()
	{
		return programCounter;
	}

	public void setProgramCounter(int programCounter)
	{
		this.programCounter = programCounter;
	}
	
	public void incrementProgramCounter()
	{
		this.programCounter++;
	}
	
	public String getContentsAsString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("\nRegister File Contents:\n\n");
		sb.append("PC" + "\t: " + programCounter + "\n\n");

		sb.append("x" + 0 + "\t: " + registerFile[0]+ "\n");		//%xo is always 0 [RISC V]
		for(int i = 1; i < 32; i++)
		{
			sb.append("x" + i + "\t: " + registerFile[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	public String getContentsAsMyString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("x" + 0 + " - " + registerFile[0]+ Statistics.getZeroes(String.valueOf(registerFile[0]).length(), 7) + " | ");		//%xo is always 0 [RISC V]
		for(int i = 1; i < 32; i++)
		{
			sb.append("x" + i + " - " + registerFile[i] + Statistics.getZeroes(String.valueOf(registerFile[i]).length(), 7) + " | ");
		}
		return sb.toString();
	}


}
