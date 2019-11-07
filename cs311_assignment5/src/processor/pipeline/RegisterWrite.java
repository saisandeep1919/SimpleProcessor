package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable() && MA_RW_Latch.isCurrentDataValid && !MA_RW_Latch.inBubble) {

			int aluResult = MA_RW_Latch.aluResult;
			int rd = MA_RW_Latch.rd;
			boolean isWb = MA_RW_Latch.isWb;
			boolean isSpecailWb = MA_RW_Latch.isSpecailWb;
			int specailAluResult = MA_RW_Latch.specailAluResult;
			boolean isEnd = MA_RW_Latch.isEnd;
			int endPC = MA_RW_Latch.endPC;

			if(isWb){
				containingProcessor.getRegisterFile().setValue(rd, aluResult);
				containingProcessor.getInUse().remove(String.valueOf(rd));
			}
			if(isSpecailWb){
				containingProcessor.getRegisterFile().setValue(31, specailAluResult);
				containingProcessor.getInUse().remove(String.valueOf(31));
				MA_RW_Latch.isSpecailWb = false;
			}

			if(isEnd){
				Simulator.setSimulationComplete(true);
				Simulator.setEndPC(endPC);

			}

			MA_RW_Latch.isCurrentDataValid = false;
			MA_RW_Latch.isRWBusy = false;
			MA_RW_Latch.inBubble = false;

//			MA_RW_Latch.setRW_enable(false);
//			IF_EnableLatch.setIF_enable(true);
		}else if(MA_RW_Latch.inBubble && MA_RW_Latch.isCurrentDataValid){
			MA_RW_Latch.isRWBusy = false;
			MA_RW_Latch.isCurrentDataValid = false;
			MA_RW_Latch.inBubble = false;
		}
	}

}
