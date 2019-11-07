package processor.memorysystem;

import configuration.Configuration;
import generic.*;
import processor.Clock;

public class MainMemory implements Element {
	int[] memory;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent(Event event){
		if(event.getEventType() == Event.EventType.MemoryRead){
			Simulator.getEventQueue().addEvent(new MemoryResponseEvent(
					Clock.getCurrentTime() + Configuration.mainMemoryLatency,
					this,
					event.getRequestingElement(),
					getWord(((MemoryReadEvent)event).getAddressToReadFrom())
			));
		}

		else if(event.getEventType() == Event.EventType.MemoryWrite){
			setWord(((MemoryWriteEvent)event).getAddressToWriteTo(), ((MemoryWriteEvent)event).getValue() );
		}

		else{
			System.out.println("Wrong event type in MainMemory");
		}


	}
}
