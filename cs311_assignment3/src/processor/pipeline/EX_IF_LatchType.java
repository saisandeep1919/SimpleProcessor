package processor.pipeline;

public class EX_IF_LatchType {

	boolean isBranchTaken;
	int newPC;
	public EX_IF_LatchType(){
		isBranchTaken = false;
	}

	public boolean isBranchTaken() {
		return isBranchTaken;
	}

	public void setBranchTaken(boolean branchTaken) {
		isBranchTaken = branchTaken;
	}

	public int getNewPC() {
		return newPC;
	}

	public void setNewPC(int newPC) {
		this.newPC = newPC;
	}
}
