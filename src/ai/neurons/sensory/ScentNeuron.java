package ai.neurons.sensory;

import ai.Creature;
import ai.neurons.Neuron;
import logic.physics.Vecf;
import world.scent.Scent;

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
