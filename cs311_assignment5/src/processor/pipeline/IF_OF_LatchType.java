package processor.pipeline;

public class IF_OF_LatchType {

	boolean isCurrentDataValid = false;
	boolean isOFBusy = false;
	boolean OF_enable;
	int instruction;
	boolean pastEncounteredBubble;
	public int pc;
	public boolean dropThisIns;
	
	public IF_OF_LatchType()
	{
		OF_enable = true;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

	public boolean getPastEncounteredBubble(){
		return pastEncounteredBubble;
	}

}
