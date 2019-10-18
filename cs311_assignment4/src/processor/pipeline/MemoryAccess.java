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
			boolean isSpecialWb = EX_MA_Latch.isSpecialWb;
			int specialAluResult = EX_MA_Latch.specialAluResult;
			boolean isEnd = EX_MA_Latch.isEnd;
			int endPC = EX_MA_Latch.endPC;
			int pc = EX_MA_Latch.pc;

			//checking store
			if(isStore){
				containingProcessor.getMainMemory().setWord(aluResult, r1);
				EX_MA_Latch.isStore = false;
			}
			if(isLoad){
				aluResult = containingProcessor.getMainMemory().getWord(aluResult);
				EX_MA_Latch.isLoad = false;
			}

			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
			MA_RW_Latch.aluResult = aluResult;
			MA_RW_Latch.rd = rd;
			MA_RW_Latch.isWb = isWb;
			MA_RW_Latch.isSpecailWb = isSpecialWb;
			MA_RW_Latch.specailAluResult = specialAluResult;
			MA_RW_Latch.isEnd = isEnd;
			MA_RW_Latch.endPC = endPC;
			MA_RW_Latch.pc = pc;
		}

		if(EX_MA_Latch.inBubble){
			MA_RW_Latch.setRW_enable(false);
			MA_RW_Latch.inBubble = true;
		}else{
			MA_RW_Latch.inBubble = false;
		}
	}

}
