package ai.neurons;
import logic.ITick;

public class Terminal implements ITick {
	static final byte INCREASE = 4;
	static final byte DECREASE = 1;
	
	Neuron connection;
	short strength;
			
	public Terminal(Neuron conectee, byte initialStrength) {
		connection = conectee;
		strength = initialStrength;
	}
	
	public void send(short resp) {
		if(connection.tickle((short) (resp * (128 + strength)/128))) strength=(short) (Math.min(strength+INCREASE, 360));
	}
	
	@Override
	public void tick() {
		
		strength = (byte) Math.max(strength - DECREASE, 1);
	}
	
}
