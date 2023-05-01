package ai.neurons.sensory;

import ai.neurons.Relay;
import ai.neurons.Terminal;
import ent.Creature;

public class FInNeuron extends ASensoryNeuron{

	private Relay signal;
	
	public FInNeuron(Relay relay, Terminal[] connection) {
		super(null, connection);
		signal = relay;
	}

	@Override
	public short sense(Creature creature) {
		return signal.get();
	}
	
	
	
}
