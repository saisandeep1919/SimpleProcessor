package processor.pipeline;

import generic.*;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element {
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
		if(EX_MA_Latch.isMA_enable() && EX_MA_Latch.isCurrentDataValid && !EX_MA_Latch.inBubble && !EX_MA_Latch.isMAWaiting){
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
				MemoryWriteEvent memoryWriteEvent = new MemoryWriteEvent(Clock.getCurrentTime(),
						this, containingProcessor.getMainMemory(), aluResult, r1);
				Simulator.getEventQueue().addEvent(memoryWriteEvent);
				//containingProcessor.getMainMemory().setWord(aluResult, r1);
				EX_MA_Latch.isStore = false;


			}
			else if(isLoad){
				MemoryReadEvent memoryReadEvent = new MemoryReadEvent(Clock.getCurrentTime(), this, containingProcessor.getMainMemory(),aluResult);
				Simulator.getEventQueue().addEvent(memoryReadEvent);
				//aluResult = containingProcessor.getMainMemory().getWord(aluResult);
				EX_MA_Latch.isLoad = false;

				EX_MA_Latch.isCurrentDataValid = true;
				EX_MA_Latch.isMABusy = true;
				EX_MA_Latch.isMAWaiting = true;
			}

			if(!isLoad){
				if(MA_RW_Latch.isRWBusy){
					System.out.println("Error in Memory Access : RW busy during store function");
					System.exit(0);
				}else{
					if(MA_RW_Latch.isCurrentDataValid){
						System.out.println("Error in MA : Failed sync between busy and data validity");
					}
				}

				EX_MA_Latch.isCurrentDataValid = false;
				EX_MA_Latch.isMABusy = false;
				MA_RW_Latch.inBubble = false;

				MA_RW_Latch.aluResult = aluResult;
				MA_RW_Latch.rd = rd;
				MA_RW_Latch.isWb = isWb;
				MA_RW_Latch.isSpecailWb = isSpecialWb;
				MA_RW_Latch.specailAluResult = specialAluResult;
				MA_RW_Latch.isEnd = isEnd;
				MA_RW_Latch.endPC = endPC;
				MA_RW_Latch.pc = pc;

				MA_RW_Latch.isRWBusy = true;
				MA_RW_Latch.isCurrentDataValid = true;
			}
/*			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
			MA_RW_Latch.aluResult = aluResult;
			MA_RW_Latch.rd = rd;
			MA_RW_Latch.isWb = isWb;
			MA_RW_Latch.isSpecailWb = isSpecialWb;
			MA_RW_Latch.specailAluResult = specialAluResult;
			MA_RW_Latch.isEnd = isEnd;
			MA_RW_Latch.endPC = endPC;
			MA_RW_Latch.pc = pc;*/
		}
		else if(EX_MA_Latch.inBubble && EX_MA_Latch.isCurrentDataValid){
			if(!MA_RW_Latch.isRWBusy){
				EX_MA_Latch.isCurrentDataValid = false;
				EX_MA_Latch.isMABusy = false;
				EX_MA_Latch.inBubble = false;

				MA_RW_Latch.isCurrentDataValid = true;
				MA_RW_Latch.inBubble = true;
			}else{
				EX_MA_Latch.isCurrentDataValid = true;
				EX_MA_Latch.isMABusy = true;
			}
		}
	}

	@Override
	public void handleEvent(Event event) {

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



		if(event.getEventType() == Event.EventType.MemoryResponse){
			aluResult = ((MemoryResponseEvent)event).getValue();

			if(!MA_RW_Latch.isRWBusy){

				if(MA_RW_Latch.isCurrentDataValid){
					System.out.println("Error in MA : Failed sync between busy and data validity");
				}

				MA_RW_Latch.aluResult = aluResult;
				MA_RW_Latch.rd = rd;
				MA_RW_Latch.isWb = isWb;
				MA_RW_Latch.isSpecailWb = isSpecialWb;
				MA_RW_Latch.specailAluResult = specialAluResult;
				MA_RW_Latch.isEnd = isEnd;
				MA_RW_Latch.endPC = endPC;
				MA_RW_Latch.pc = pc;
				MA_RW_Latch.inBubble = false;

				MA_RW_Latch.isCurrentDataValid = true;
				MA_RW_Latch.isRWBusy = true;

				EX_MA_Latch.isMABusy = false;
				EX_MA_Latch.isCurrentDataValid = false;
				EX_MA_Latch.isMAWaiting = false;
			}else{
				event.setEventTime(event.getEventTime() + 1);
				Simulator.getEventQueue().addEvent(event);
				EX_MA_Latch.isMAWaiting = true;
			}
		}else{
			System.out.println("Wrong event type in Memory Access");
		}
	}
}
