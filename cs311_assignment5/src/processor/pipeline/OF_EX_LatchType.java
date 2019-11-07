package processor.pipeline;

public class OF_EX_LatchType {


	boolean isCurrentDataValid = false;
	boolean isEXBusy = false;
	boolean EX_enable;
	public int aluSignal; //For opcode transfer
	public int r1;
	public int ri2;
	public int rd;
	public boolean isImmediate;
	public boolean inBubble;
	public int pc;


	
	public OF_EX_LatchType()
	{
		EX_enable = true;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}


	public int getAluSignal() {
		return aluSignal;
	}

	public void setAluSignal(int aluSignal) {
		this.aluSignal = aluSignal;
	}

	public int getR1() {
		return r1;
	}

	public void setR1(int r1) {
		this.r1 = r1;
	}

	public int getRi2() {
		return ri2;
	}

	public void setRi2(int ri2) {
		this.ri2 = ri2;
	}

	public int getRd() {
		return rd;
	}

	public void setRd(int rd) {
		this.rd = rd;
	}

	public boolean isImmediate() {
		return isImmediate;
	}

	public void setImmediate(boolean immediate) {
		isImmediate = immediate;
	}
}
