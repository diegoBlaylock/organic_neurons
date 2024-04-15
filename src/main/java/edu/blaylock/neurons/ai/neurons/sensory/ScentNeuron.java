package edu.blaylock.neurons.ai.neurons.sensory;

import edu.blaylock.neurons.ai.Creature;
import edu.blaylock.neurons.ai.neurons.Neuron;
import edu.blaylock.neurons.logic.physics.Vecf;
import edu.blaylock.neurons.world.scent.Scent;

public class ScentNeuron extends ASensoryNeuron {
	private Scent scent;
	private Vecf offset;


	public ScentNeuron(Creature creature, Neuron neuron, Scent fruity, Vecf vec) {
		super(creature, neuron);
		this.scent= fruity;
		this.offset = vec;
	}

	@Override
	public short sense(Creature creature) {
		return creature.getWorld().scentIntensity(scent, Vecf.add(offset, creature.getPosition()));
	}

	
	
}
