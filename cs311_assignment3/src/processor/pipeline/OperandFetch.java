package processor.pipeline;

import processor.Processor;

public class OperandFetch {

	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			int r1 = 0;
			int rI2 = 0;
			int rd = 0;
			boolean isImmediate = false;
			int instruction = IF_OF_Latch.getInstruction();
			int opcode = getOpcode(IF_OF_Latch.getInstruction());
			int operationType = getOperationType(opcode);  // 1,2,3  values are possible


			if(operationType == 1){
				if(opcode == 29){
					isImmediate = false;
					r1 = 0;
					rI2 = 0;
					rd = 0;
				}else{
					int tempInt = getSubIntValFromInt(instruction, 5, 10);
					long tempLong = 0;
					if(tempInt == 0){
						tempLong = getSubLongValFromInt(instruction, 11, 32);
						if(tempLong == 0){
							System.out.println("Error in OperandFetch type RI (Both rd and imm are zero): Instruction - " + instruction);
						}else{
							isImmediate = true;
							rI2 = (int)tempLong;
							r1 = 0;
							rd = 0;
						}
					}else{
						rd = containingProcessor.getRegisterFile().getValue(tempInt);
						isImmediate = false;
						r1 = 0;
						rI2 = 0;
					}
				}

			}
			else if(operationType == 2){
				isImmediate = true;
				r1 = containingProcessor.getRegisterFile().getValue(getSubIntValFromInt(instruction, 5, 10));
				rd = getSubIntValFromInt(instruction, 10, 15);
				if(opcode == 22 || opcode == 23){
					rI2 = containingProcessor.getMainMemory().getWord((int)getSubLongValFromInt(instruction, 15, 32));
				}
				else{
					rI2 = (int)getSubLongValFromInt(instruction, 15, 32);
				}

			}
			else if(operationType == 3){
				isImmediate = false;
				r1 = containingProcessor.getRegisterFile().getValue(getSubIntValFromInt(instruction, 5, 10));
				rI2 = containingProcessor.getRegisterFile().getValue(getSubIntValFromInt(instruction, 10, 15));
				rd = getSubIntValFromInt(instruction, 15, 20);
			}
			OF_EX_Latch.aluSignal = opcode;
			OF_EX_Latch.isImmediate = isImmediate;
			OF_EX_Latch.r1 = r1;
			OF_EX_Latch.ri2 = rI2;
			OF_EX_Latch.rd = rd;

			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

	private long getSubLongValFromInt(int instruction, int start, int end) {
		long ans = 0;

		String insString = Integer.toBinaryString(instruction);
		insString = addBinaryPrefix(32, insString);
		String subStr = insString.substring(start, end);
		long val = BinToInt(subStr);

		ans = val;

		return ans;
	}

	private long BinToInt(String subStr) {
		long ans = 0;
		String tempStr = "";
		if(subStr.charAt(0) == '1'){
			tempStr = "";
			for(int i=0;i<subStr.length();i++){
				if(subStr.charAt(i) == '1'){
					tempStr = tempStr + "0";
				}else{
					tempStr = tempStr + "1";
				}
			}
			ans = Long.parseLong(tempStr, 2);
			return (ans + 1)*-1;
		}else{
			tempStr = subStr;
			ans = Long.parseLong(tempStr, 2);
		}
		ans = Long.parseLong(tempStr, 2);
		return ans;
	}

	private int getSubIntValFromInt(int instruction, int start, int end) {
		int ans = 0;

		String insString = Integer.toBinaryString(instruction);
		insString = addBinaryPrefix(32, insString);
		String subStr = insString.substring(start, end);
		int val = Integer.valueOf(subStr, 2);
		ans = val;

		return ans;
	}

	private int getOperationType(int opcode) {
		int ans = 0;
		if(opcode % 2 == 0){
			if(opcode <= 20){
				ans = 3;
			}else if(opcode == 24){
				ans = 1;
			}else if(opcode == 22 || opcode == 26 || opcode == 28){
				ans = 2;
			}else{
				System.out.println("Error in Operand Fetch - Invalid opcode found");
				return 0;
			}
		}else if(opcode < 30 && opcode != 29){
			ans = 2;
		}
		else if(opcode == 29){
			ans = 1;
		}
		else{
			System.out.println("Error in Operand Fetch - Invalid opcode found");
			return 0;
		}

		return ans;
	}

	public int getOpcode(int instruction) {
		int ans = 0;
		String insString = Integer.toBinaryString(instruction);
		insString = addBinaryPrefix(32, insString);
		String opcodeStr = insString.substring(0,5);
		int opcode = Integer.valueOf(opcodeStr, 2);
		ans = opcode;
		if(opcode > 29){
			System.out.println("Error in Operand Fetch - Invalid opcode found");
		}
		return ans;
	}

	private static String addBinaryPrefix(int len, String s){
		String ans = "";
		int offset = len - s.length();
		if(offset == 0){
			return s;
		}else if(offset < 0){
			ans = s.substring(-1*offset, s.length());
			if(ans.length() > len){
				System.out.println("Error in adding binary suffix");
			}else{
				return ans;
			}
		}
		String strpref = "";
		for(int i=0;i<offset;i++){
			strpref = strpref + String.valueOf(0);
		}
		ans = strpref + s;
		return ans;
	}

}
