package edu.blaylock.neurons.ai.neurons.sensory;

import edu.blaylock.neurons.ai.Creature;
import edu.blaylock.neurons.ai.neurons.IThink;
import edu.blaylock.neurons.ai.neurons.Neuron;
import edu.blaylock.neurons.logic.ITick;

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
