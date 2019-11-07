package processor.pipeline;

public class EX_MA_LatchType {

	boolean isMAWaiting = false;
	boolean isCurrentDataValid = false;
	boolean isMABusy = false;
	boolean MA_enable;
	int aluResult;
	boolean isLoad;
	boolean isStore;
	int r1;
	int rI2;
	int rd;
	boolean isWb;
	boolean isSpecialWb;
	int specialAluResult;
	boolean inBubble;
	boolean isEnd = false;
	int endPC;
	int pc;
	
	public EX_MA_LatchType()
	{
		MA_enable = true;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

}
