package processor.pipeline;

public class IF_EnableLatchType {

	boolean isCurrentDataValid = false;
	boolean isIFBusy = false;
	boolean IF_enable;
	boolean isEndEncountered = false;
	boolean isIFWaiting = false;
	int currentPC = 0;
	
	public IF_EnableLatchType()
	{
		IF_enable = true;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

	public void setEndEncountered(boolean b){
		isEndEncountered = b;
	}

}
