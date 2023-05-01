package ai.neurons;
import logic.ITick;

public class Terminal implements ITick {
	static final byte INCREASE = 2;
	static final byte DECREASE = 1;
	
	Neuron connection;
	short strength;
			
	public Terminal(Neuron conectee, short initialStrength) {
		connection = conectee;
		strength = initialStrength;
	}
	
	public void send(short resp) {
		if(connection.tickle((short) (resp * ( strength-Short.MIN_VALUE)/Short.MAX_VALUE))) strength=(short) (Math.min(strength+INCREASE, Short.MAX_VALUE));
	}
	
	@Override
	public void tick() {
		strength = (byte) Math.max(strength - DECREASE, Short.MAX_VALUE+1);
		connection.tick();
	}
	
}
