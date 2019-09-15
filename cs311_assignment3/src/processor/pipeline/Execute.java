package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		if(OF_EX_Latch.isEX_enable()){
			//TODO
			int aluSignal = OF_EX_Latch.getAluSignal();
			boolean isImmediate = OF_EX_Latch.isImmediate();
			int r1 = OF_EX_Latch.getR1();
			int rI2 = OF_EX_Latch.getRi2();
			int rd = OF_EX_Latch.getRd();

			boolean isBranch = false;
			boolean isLoad = false;
			boolean isStore = false;
			int aluResult = 0;
			boolean isWb = false;

			//R2I and jmp
			if(isImmediate){
				//Checking Is Branch
				if(aluSignal == 25 || aluSignal == 26 || aluSignal == 27 || aluSignal == 28 ){
					if(aluSignal == 25){
						int temp = containingProcessor.getRegisterFile().getValue(rd);
						if(r1 == temp){
							isBranch = true;
						}else{
							isBranch = false;
						}
					}
					else if(aluSignal == 26){
						int temp = containingProcessor.getRegisterFile().getValue(rd);
						if(r1 != temp){
							isBranch = true;
						}else{
							isBranch = false;
						}
					}
					else if(aluSignal == 27){
						int temp = containingProcessor.getRegisterFile().getValue(rd);
						if(r1 > temp){
							isBranch = true;
						}else{
							isBranch = false;
						}
					}
					else if(aluSignal == 28){
						int temp = containingProcessor.getRegisterFile().getValue(rd);
						if(r1 < temp){
							isBranch = true;
						}else{
							isBranch = false;
						}
					}
				}

				//checking Is Load
				//loads the value into rd = r1 + r2I
				else if(aluSignal == 22){
					isLoad = true;
					aluResult = r1 + rI2;
					isWb = true;
				}

				//checking Is store
				//stores the value at rd + rI2 to r1
				else if(aluSignal == 23){
					isStore = true;
					int temp = containingProcessor.getRegisterFile().getValue(rd);
					aluResult = temp + rI2;
				}

				//checking jmp
				else if(aluSignal == 24){
					isBranch = true;
					aluResult = rI2;
				}

				//arithmetic operations
				//addi
				else if(aluSignal == 1){
					aluResult = r1 + rI2;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//subi
				else if(aluSignal == 3){
					aluResult = r1 - rI2;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//multi
				else if(aluSignal == 5){
					aluResult = r1 * rI2;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//divi
				else if(aluSignal == 7){
					aluResult = r1 / rI2;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
			}

			//R3 and R1
			else{
				//checking jmp
				if(aluSignal == 24){
					isBranch = true;
					aluResult = rd;
				}
				else if(aluSignal == 29){
					Simulator.setSimulationComplete(true);
				}
				//add
				else if(aluSignal == 0){
					aluResult = r1 + rI2;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//sub
				else if(aluSignal == 2){
					aluResult = r1 - rI2;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//mul
				else if(aluSignal == 4){
					aluResult = r1 * rI2;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//div
				else if(aluSignal == 6){
					aluResult = r1 / rI2;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
			}


			//preparing Memory access latch

			EX_MA_Latch.setMA_enable(true);
			EX_MA_Latch.aluResult = aluResult;
			EX_MA_Latch.isWb = isWb;
			if(isLoad && isStore){
				System.out.println("Error in Execute - Both Load and Store");
			}else{
				EX_MA_Latch.r1 = r1;
				EX_MA_Latch.rI2 = rI2;
				EX_MA_Latch.rd = rd;
				if(isLoad){
					EX_MA_Latch.isLoad = true;
					EX_MA_Latch.isStore = false;
				}else if(isStore){
					EX_MA_Latch.isLoad = false;
					EX_MA_Latch.isStore = true;
				}
			}

			if(isBranch){
				EX_IF_Latch.isBranchTaken = true;
				EX_IF_Latch.newPC = containingProcessor.getRegisterFile().getProgramCounter() - 1 + rI2;
			}else{
				EX_IF_Latch.isBranchTaken = false;
			}

			OF_EX_Latch.setEX_enable(false);

		}
	}

}
