package ai;

import ai.neurons.IThink;
import ai.neurons.Neuron;
import ai.neurons.sensory.ASensoryNeuron;
import logic.ITick;

public class Brain implements IThink, ITick{
	
	ASensoryNeuron[] senses;
	
	
	//Sensory Neurons
	/// 16 scents
	/// Speed
	// INTERNEURONS
	/// 100 Neurons //HOW?
	// MOTOR NEURONS
	/// 4 MovementNeurons
	//		UP
	//		DOWN etc.
	
	public Brain(ASensoryNeuron[] senses) {
		this.senses = senses;
	}

	public void think() {
		for(ASensoryNeuron s: senses) {
			s.think();
		}
	}
	
	public void tick() {
		for(ASensoryNeuron s: senses) {
			s.tick();
		}
	}
	
	
	
}
