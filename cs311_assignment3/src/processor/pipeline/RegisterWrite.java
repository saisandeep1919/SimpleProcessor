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
		if(MA_RW_Latch.isRW_enable())
		{
			//TODO

			int aluResult = MA_RW_Latch.aluResult;
			int rd = MA_RW_Latch.rd;
			boolean isWb = MA_RW_Latch.isWb;
			boolean isSpecailWb = MA_RW_Latch.isSpecailWb;
			int specailAluResult = MA_RW_Latch.specailAluResult;

			if(isWb){
				containingProcessor.getRegisterFile().setValue(rd, aluResult);
			}

			if(isSpecailWb){
				containingProcessor.getRegisterFile().setValue(31, specailAluResult);
				MA_RW_Latch.isSpecailWb = false;
			}


			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
