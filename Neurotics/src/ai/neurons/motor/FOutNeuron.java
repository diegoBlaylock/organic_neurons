package ai.neurons.motor;

import ai.neurons.Neuron;
import ai.neurons.Relay;

public class FOutNeuron extends Neuron{

	public FOutNeuron(Relay shared, short potential, short pressure) {
		super(Neuron.null_terminals, potential, pressure);

		signal = shared;
		signal.set(pressure);
	}

	private Relay signal;
	
	@Override
	public void fire(short signal) {
		this.signal.set(signal);
	}
	
}
