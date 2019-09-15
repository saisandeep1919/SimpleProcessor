package processor.pipeline;

import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if(EX_MA_Latch.isMA_enable()){
			int aluResult = EX_MA_Latch.aluResult;
			boolean isLoad = EX_MA_Latch.isLoad;
			boolean isStore = EX_MA_Latch.isStore;
			int r1 = EX_MA_Latch.r1;
			int rI2 = EX_MA_Latch.rI2;
			int rd = EX_MA_Latch.rd;
			boolean isWb = EX_MA_Latch.isWb;

			//checking store
			if(isStore){
				containingProcessor.getMainMemory().setWord(aluResult, r1);
			}

			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
			MA_RW_Latch.aluResult = aluResult;
			MA_RW_Latch.rd = rd;
			MA_RW_Latch.isWb = isWb;


		}
	}

}
