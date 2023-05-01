package ai.neurons;

public class Relay {
	
	private short signal;
	
	public Relay(short init) {
		signal = init;
	}
	
	public void set(short newSignal) {
		signal = newSignal;
	}
	
	public short get() {
		return signal;
	}
	
}
