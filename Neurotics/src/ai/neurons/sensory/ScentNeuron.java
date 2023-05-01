package ai.neurons.sensory;

import ai.neurons.Neuron;
import ai.neurons.Terminal;
import ent.Creature;
import logic.physics.Vecf;
import world.scent.Scent;

public class ScentNeuron extends ASensoryNeuron {
	private Scent scent;
	private Vecf offset;


	public ScentNeuron(Creature creature, Terminal[] neuron, Scent fruity, Vecf vec) {
		super(creature, neuron);
		this.scent= fruity;
		this.offset = vec;
	}

	@Override
	public short sense(Creature creature) {
		return creature.getWorld().scentIntensity(scent, Vecf.add(offset, creature.getPosition()));
	}

	
	
}
