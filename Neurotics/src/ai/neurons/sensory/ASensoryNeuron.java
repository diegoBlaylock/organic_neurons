package ai.neurons.sensory;

import ai.neurons.IThink;
import ai.neurons.Neuron;
import ai.neurons.Terminal;
import ent.Creature;
import logic.ITick;

public abstract class ASensoryNeuron implements ITick, IThink{
	Terminal[] connections;
	Creature creature;
	
	public ASensoryNeuron(Creature creature, Terminal[] connection) {
		this.creature = creature;
		connections = connection;
	}
	
	public void tick() {
		for(Terminal wrap: connections) wrap.tick();
	}
	
	public void think() {
		for(Terminal wrap: connections) wrap.send(sense(creature));

	}
	
	public abstract short sense(Creature creature);
}
