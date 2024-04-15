package ai.neurons.motor;

import ai.neurons.Neuron;
import ai.neurons.Terminal;
import logic.physics.ForceHandle;
import logic.physics.Physical;
import logic.physics.Vecf;

public class MovementNeuron extends Neuron {
	static final Terminal[] null_terminals = new Terminal[]{};
	static final float MAX = 100;
	
	
	ForceHandle control;
	
	final Vecf force;
	
	public MovementNeuron(Physical control, Vecf force, short potential, short pressure) {
		super(null_terminals, potential, pressure);
		this.control = control.getHandle();
		this.force = force;
	}
	
	@Override
	public void fire(short signal) {
		//System.out.println("I AM MOVING");
		
		Vecf scaled = new Vecf(force);
		scaled.scale((Math.max(signal, MAX)/ MAX));
		control.add(scaled);
	}

}
