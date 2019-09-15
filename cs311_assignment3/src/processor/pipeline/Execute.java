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
			boolean isSpecialWb = false;
			int specialAluResult = 0;

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
						if(r1 < temp){
							isBranch = true;
						}else{
							isBranch = false;
						}
					}
					else if(aluSignal == 28){
						int temp = containingProcessor.getRegisterFile().getValue(rd);
						if(r1 > temp){
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
					specialAluResult = r1 % rI2;
					isSpecialWb = true;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//andi
				else if(aluSignal == 9){
					aluResult = performAnd(r1, rI2);
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//ORi
				else if(aluSignal == 11){
					aluResult = performOr(r1, rI2);
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//XORi
				else if(aluSignal == 13){
					aluResult = performXor(r1, rI2);
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//SLTI
				else if(aluSignal == 15){
					if(r1 < rI2){
						aluResult = 1;
					}else{
						aluResult = 0;
					}
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//SLLI
				else if(aluSignal == 17){
					isWb = true;
					String temp = logicalLeftShift(r1, rI2);
					int offSet = temp.length() - 32;
					if(offSet > 0){
						isSpecialWb = true;
						aluResult = getInt(getLastSubString(temp, 32));
						specialAluResult = getInt(getFirstSubString(temp, offSet));
					}else{
						isSpecialWb = false;
						aluResult = getInt(temp);
					}

					isBranch = false;;
					isLoad = false;
					isStore = false;
				}
				//SRLI
				else if(aluSignal == 19){
					isWb = true;
					String temp = logicalRightShift(r1, rI2);
					int offset = temp.length() - 32;
					if(offset > 0){
						aluResult = getInt(getFirstSubString(temp, 32));
					}else{
						aluResult = getInt(temp);
					}
					isSpecialWb = false;
					isBranch = false;;
					isLoad = false;
					isStore = false;
				}
				//SRAI
				else if(aluSignal == 21){
					isWb = true;
					aluResult = getInt(ArithmeticRightShift(r1, rI2));
					isSpecialWb = false;
					isBranch = false;;
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
					isSpecialWb = true;
					specialAluResult = r1 % rI2;
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//and
				else if(aluSignal == 8){
					aluResult = performAnd(r1, rI2);
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//OR
				else if(aluSignal == 10){
					aluResult = performOr(r1, rI2);
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//XOR
				else if(aluSignal == 12){
					aluResult = performXor(r1, rI2);
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//SLT
				else if(aluSignal == 14){
					if(r1 < rI2){
						aluResult = 1;
					}else{
						aluResult = 0;
					}
					isWb = true;
					isBranch = false;
					isLoad = false;
					isStore = false;
				}
				//SLL
				else if(aluSignal == 16){
					isWb = true;
					String temp = logicalLeftShift(r1, rI2);
					int offSet = temp.length() - 32;
					if(offSet > 0){
						isSpecialWb = true;
						aluResult = getInt(getLastSubString(temp, 32));
						specialAluResult = getInt(getFirstSubString(temp, offSet));
					}else{
						isSpecialWb = false;
						aluResult = getInt(temp);
					}

					isBranch = false;;
					isLoad = false;
					isStore = false;
				}
				//SRL
				else if(aluSignal == 18){
					isWb = true;
					String temp = logicalRightShift(r1, rI2);
					int offset = temp.length() - 32;
					if(offset > 0){
						aluResult = getInt(getFirstSubString(temp, 32));
					}else{
						aluResult = getInt(temp);
					}
					isSpecialWb = false;
					isBranch = false;;
					isLoad = false;
					isStore = false;
				}
				//SRA
				else if(aluSignal == 20){
					isWb = true;
					aluResult = getInt(ArithmeticRightShift(r1, rI2));
					isSpecialWb = false;
					isBranch = false;;
					isLoad = false;
					isStore = false;
				}
			}


			//preparing Memory access latch

			EX_MA_Latch.setMA_enable(true);
			EX_MA_Latch.aluResult = aluResult;
			EX_MA_Latch.isWb = isWb;
			EX_MA_Latch.isSpecialWb = isSpecialWb;
			EX_MA_Latch.specialAluResult = specialAluResult;

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


	//Helper Funcitons

	public int performAnd(int r1, int r2){
		int ans = 0;
		String r1Str = getBinary(r1);
		String r2Str = getBinary(r2);
		int l1 = r1Str.length();
		int l2 = r2Str.length();
		int l = Math.max(l1, l2);
		r1Str = addBinaryPrefix(l, r1Str);
		r2Str = addBinaryPrefix(l, r2Str);

		//operation
		String ansString = "";
		for(int i=0;i<l;i++){
			if(r1Str.charAt(i) == '1' && r2Str.charAt(i) == '1'){
				ansString = ansString + "1";
			}else{
				ansString = ansString + "0";
			}
		}
		ans = getInt(ansString);

		return ans;
	}

	public int performOr(int r1, int r2){
		int ans = 0;
		String r1Str = getBinary(r1);
		String r2Str = getBinary(r2);
		int l1 = r1Str.length();
		int l2 = r2Str.length();
		int l = Math.max(l1, l2);
		r1Str = addBinaryPrefix(l, r1Str);
		r2Str = addBinaryPrefix(l, r2Str);


		//operation
		String ansString = "";
		for(int i=0;i<l;i++){
			if(r1Str.charAt(i) == '1' || r2Str.charAt(i) == '1'){
				ansString = ansString + "1";
			}else{
				ansString = ansString + "0";
			}
		}
		ans = getInt(ansString);

		return ans;
	}
	public int performXor(int r1, int r2){
		int ans = 0;
		String r1Str = getBinary(r1);
		String r2Str = getBinary(r2);
		int l1 = r1Str.length();
		int l2 = r2Str.length();
		int l = Math.max(l1, l2);
		r1Str = addBinaryPrefix(l, r1Str);
		r2Str = addBinaryPrefix(l, r2Str);


		//operation
		String ansString = "";
		for(int i=0;i<l;i++){
			if(r1Str.charAt(i) != r2Str.charAt(i)){
				ansString = ansString + "1";
			}else{
				ansString = ansString + "0";
			}
		}
		ans = getInt(ansString);

		return ans;
	}


	public String logicalLeftShift(int i1, int i2){
		String ans = "";

		String s1 = getBinary(i1);
		String st = "";
		for(int i=0;i<i2;i++){
			st = st + "0";
		}

		String s = s1 + st;

		ans = s;
		return ans;
	}

	public String logicalRightShift(int i1, int i2){
		String ans = "";
		String s1 = getBinary(i1);
		String st = "";
		for(int i=0;i<i2;i++){
			st = st + "0";
		}
		String s = st + s1;

		ans = s;

		return ans;
	}

	public String ArithmeticRightShift(int i1, int i2){
		String ans = "";
		String s1 = getBinary(i1);
		String tempStr = "";
		if(i1 > 0){
			tempStr = getFirstSubString(logicalRightShift(i1, i2), 32);
		}
		else if (i1 < 0){
			String temp = "";
			for(int i=0;i<i2;i++){
				temp = temp + "1";
			}
			tempStr = getFirstSubString(temp + s1, 32);
		}
		ans = tempStr;

		return  ans;
	}

	public String getLastSubString(String s, int l){
		String ans = "";

		String tempStr = "";
		int offSet = s.length() - l;
		if(offSet > 0){
			for(int i=offSet;i<s.length();i++){
				tempStr = tempStr + s.charAt(i);
			}
		}else{
			tempStr = s;
		}
		ans = tempStr;

		return ans;
	}

	public String getFirstSubString(String s, int l){
		String ans = "";

		String tempStr = "";
		if(s.length() > l){
			for(int i=0;i<l;i++){
				tempStr = tempStr + s.charAt(i);
			}
		}
		else{
			tempStr = s;
		}
		ans = ans + tempStr;

		return ans;
	}


	public String getBinary(int i){
		String ans = "";
		ans = Integer.toBinaryString(i);
		return ans;
	}

	public int getInt(String subStr){
		long ans = 0;
//		String tempStr = "";
//		if(subStr.charAt(0) == '1'){
//			tempStr = "";
//			for(int i=0;i<subStr.length();i++){
//				if(subStr.charAt(i) == '1'){
//					tempStr = tempStr + "0";
//				}else{
//					tempStr = tempStr + "1";
//				}
//			}
//			ans = Long.parseLong(tempStr, 2);
//			return (int)(ans + 1)*-1;
//		}else{
//			tempStr = subStr;
//			ans = Long.parseLong(tempStr, 2);
//		}
//		ans = Long.parseLong(tempStr, 2);
		ans = (int)Long.parseLong(subStr, 2);
		return (int)ans;
	}

	public String addBinaryPrefix(int len, String s){
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
