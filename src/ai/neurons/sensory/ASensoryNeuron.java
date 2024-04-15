package ai.neurons.sensory;

import ai.Creature;
import ai.neurons.IThink;
import ai.neurons.Neuron;
import logic.ITick;

public abstract class ASensoryNeuron implements ITick, IThink{
	Neuron wrapper;
	Creature creature;
	
	public ASensoryNeuron(Creature creature, Neuron connection) {
		this.creature = creature;
		wrapper = connection;
	}
	
	public void tick() {
		wrapper.tick();
	}
	
	public void think() {
		wrapper.tickle(sense(creature));

	}
	
	public abstract short sense(Creature creature);
}
