package processor.pipeline;

import generic.*;
import processor.Clock;
import processor.Processor;

public class InstructionFetch implements Element {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable() && !IF_EnableLatch.isIFWaiting)
		{
			if(!IF_EnableLatch.isEndEncountered){
				Statistics.incrementInstructions();
			}
			int pc = 0;
			if(EX_IF_Latch.isBranchTaken){
				pc = EX_IF_Latch.newPC;
				EX_IF_Latch.isBranchTaken = false;
			}else{
				pc = containingProcessor.getRegisterFile().getProgramCounter();
			}
//			IF_EnableLatch.setIF_enable(false);
//			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.currentPC = pc;
			MemoryReadEvent memoryReadEvent = new MemoryReadEvent(Clock.getCurrentTime(), this,
					containingProcessor.getMainMemory(), pc);
			Simulator.getEventQueue().addEvent(memoryReadEvent);
			IF_EnableLatch.isIFWaiting = true;
		}
	}

	@Override
	public void handleEvent(Event event) {
		if(event.getEventType() == Event.EventType.MemoryResponse){
			if(!IF_OF_Latch.isOFBusy){
				if(IF_OF_Latch.isCurrentDataValid){
					System.out.println("Error in IF : Failed sync between busy and data validity");
				}

				int newIns = ((MemoryResponseEvent)event).getValue();
				IF_OF_Latch.setInstruction(newIns);
				containingProcessor.getRegisterFile().setProgramCounter(IF_EnableLatch.currentPC + 1);
				IF_OF_Latch.pc = IF_EnableLatch.currentPC;
				IF_OF_Latch.isCurrentDataValid = true;

				IF_EnableLatch.isIFWaiting = false;
			}else{
				event.setEventTime(event.getEventTime() + 1);
				Simulator.getEventQueue().addEvent(event);
				IF_EnableLatch.isIFWaiting = true;
			}
		}else{
			System.out.println("Wrong event type in Ins Fetch");
		}
	}

	/*

	public boolean checkForEnd(int ins){
		boolean ans = false;
		int opcode = OperandFetch.getOpcode(ins);
		if(opcode == 29){
			ans = true;
		}
		return ans;
	}
	*/



}
