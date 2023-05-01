package ai.neurons;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ai.Brain;
import ai.neurons.motor.FOutNeuron;
import ai.neurons.motor.MovementNeuron;
import ai.neurons.sensory.ASensoryNeuron;
import ai.neurons.sensory.FInNeuron;
import ai.neurons.sensory.ScentNeuron;
import ent.Creature;
import logic.physics.Vecf;
import world.scent.Scent;

public class DNA {
	
	final static Random RNG = new Random();
	final static int NUM_LAYERS = 5;
	final static int LAYER_SIZE = 8;
	final static int FEEDBACK_SIZE = 2;
	
	T[][] inputTerminals;
	N[][] layers = new N[NUM_LAYERS][] ;
	N[] outputNeurons;
	
	DNA(){}
	
	
	static class N {
		public short pot;
		public short init;
		public T[] termin;
		
		public void setPotential(short newPotential) {
			if(newPotential <= 0) {
				if(pot < 100) pot = 1;
				else pot = Short.MAX_VALUE;
			}
			
			pot = newPotential;
		}

		public void setInit(short s) {
			if(s <= 0) {
				if(init < 100) init = 1;
				else init = Short.MAX_VALUE;
			}
		}
	}
	
	static class T {
		public short str;
		
		public void setStrength(byte newStrength) {
			if(newStrength <= 0) {
				if(str < 0) str = Short.MIN_VALUE+1;
				else str = Short.MAX_VALUE;
			}
			
			str=newStrength;
		}
	}
	
	public final static DNA genRandom() {
		
		DNA dna = new DNA();
		
		dna.outputNeurons = new N[4+FEEDBACK_SIZE];
		
		for(int i = 0 ; i < 4+FEEDBACK_SIZE; i++) {
			dna.outputNeurons[i] = genN(0);
		}
		
		for(int i = dna.layers.length - 1; i >= 0; i--) {
			dna.layers[i] = new N[LAYER_SIZE];
			
			for(int j = 0; j < dna.layers[i].length; j++) {
				dna.layers[i][j] = genN((i == dna.layers.length -1)? dna.outputNeurons.length : dna.layers[i+1].length);
			}
		}
		
		dna.inputTerminals = new T[4+FEEDBACK_SIZE][];
		
		for(int i = 0 ; i < 4+FEEDBACK_SIZE; i++) {
			dna.inputTerminals[i] = new T[dna.layers[0].length];
			for(int j = 0 ; j < dna.layers[0].length; j++) {
				dna.inputTerminals[i][j] = genT();	
			}
		}
		
		return dna;
	}
	
	static N genN(int terminals) {
		N n = new N();
		n.init = (short) RNG.nextInt(1024);
		n.pot = (short) RNG.nextInt(1024);
		if(terminals != 0) {
			n.termin =  new T[terminals];
			
			for(int i = 0 ; i < n.termin.length; i++) {
				n.termin[i] = genT();
			}
		}
		return n;
	}
	
	static T genT() {
		T t = new T();
		t.str = (short) (RNG.nextInt(2*Short.MAX_VALUE+1)-Short.MIN_VALUE);
		return t;
	}
	
	public Brain build(Creature c) {
		Prebrain brain = new Prebrain();
		buildMotors(brain, c);
		buildPool(brain);
		buildSenses(brain, c);
		
		return brain.build();
		
	}
	
	void buildPool(Prebrain brain) {
		Neuron[][] layers = new Neuron[NUM_LAYERS][];
		for(int i = layers.length-1 ; i >= 0; i--) {
			
			layers[i] = new Neuron[this.layers[i].length];
			
			for(int j = 0; j < this.layers[i].length; j++) {
				Terminal[] terminals = new Terminal[this.layers[i][j].termin.length];
				
				for(int k = 0; k < terminals.length; k++) {
					
					Neuron connection = (i == this.layers.length-1)? brain.outputs[k] : layers[i+1][k];
					
					terminals[k] = new Terminal(connection, this.layers[i][j].termin[k].str);	
				}
				
				layers[i][j] = new Neuron(terminals, this.layers[i][j].pot);
				layers[i][j].pressure =  this.layers[i][j].init;
			}
		}
		
		brain.layers = layers;
	}
	
	// Custom senses- right now 1 scent for bi pole
	void buildSenses(Prebrain brain, Creature creature) {
		Terminal[][] terminals = new Terminal[4+FEEDBACK_SIZE][inputTerminals[0].length];
		
		for(int i = 0 ; i < inputTerminals.length; i++) {
			for(int j = 0 ; j < inputTerminals[i].length; j++) {
				terminals[i][j] = new Terminal(brain.layers[0][j], inputTerminals[i][j].str);
			}
		}
		
		ASensoryNeuron[] senses = {
				new ScentNeuron(creature, terminals[0], Scent.FRUITY, new Vecf(-10,0)), 
				new ScentNeuron(creature, terminals[1], Scent.FRUITY, new Vecf(10,0)),
				new ScentNeuron(creature, terminals[2], Scent.FRUITY, new Vecf(0,-10)),
				new ScentNeuron(creature, terminals[3], Scent.FRUITY, new Vecf(0,10))};
		
		FInNeuron[] inputs = new FInNeuron[FEEDBACK_SIZE];
		
		for(int i = 0; i < inputs.length; i++) {
			inputs[i] = new FInNeuron(brain.relays[i], terminals[i+senses.length]);
		}
				
		brain.senses = Arrays.copyOf(senses, senses.length + inputs.length);
		System.arraycopy(inputs, 0, brain.senses, senses.length, inputs.length);
	}
	
	
	
	void buildMotors(Prebrain brain, Creature creature) {
		final float strength = 0.01f;
		Vecf[] directions = {new Vecf(-strength, 0), new Vecf(strength, 0), new Vecf(0, -strength), new Vecf(0, strength)};
	
		Neuron[] motors = new MovementNeuron[4];
		
		for(int i = 0 ; i < motors.length; i++) {
			motors[i] = new MovementNeuron(creature, directions[i], outputNeurons[i].pot, outputNeurons[i].init);
		}
		
		brain.relays = new Relay[FEEDBACK_SIZE];
		Neuron[] feedbacks = new FOutNeuron[FEEDBACK_SIZE];
		
		for(int i = 0 ; i < feedbacks.length; i++) {
			brain.relays[i] = new Relay((short) 0);
			feedbacks[i] = new FOutNeuron(brain.relays[i], outputNeurons[i+motors.length].pot, outputNeurons[i+motors.length].init);
		}
		brain.outputs = new Neuron[motors.length + feedbacks.length];
		System.arraycopy( motors, 0, brain.outputs, 0, motors.length);
		System.arraycopy( feedbacks, 0, brain.outputs, motors.length, feedbacks.length);
	}
	
	
	private class Prebrain{
		Neuron[] outputs;
		Neuron[][] layers;
		ASensoryNeuron[] senses;
		Relay[] relays;
		
		public Brain build() {
			return new Brain(senses);
		}
	}
	
	public DNA mutate() {
		final short pot_change = prob(0.05f);
		final short str_change = prob(0.05f);
		final short change_factor = 10;
		
		
		DNA dna = new DNA();
		dna.inputTerminals = this.inputTerminals;
		dna.outputNeurons = this.outputNeurons;
		dna.layers = this.layers;
		
		
		// POOL
		for(N[] layer : dna.layers) {
			for(N n : layer) {
				if(roll(pot_change)) {
					n.setPotential((short) (n.pot + change_factor * (Math.random()*2 -1)));
				}
				
				if(roll(pot_change)) {
					n.setInit((short) (n.pot + change_factor * (Math.random()*2 -1)));
				}
				
				for(T t : n.termin) {
					if(roll(str_change)) {
						t.setStrength((byte) (t.str+ change_factor * (Math.random()*2 -1)));
					}
				}
			}
		}
		
		//MOTORS
		for(N n : dna.outputNeurons) {
			if(roll(pot_change)) {
				n.setPotential((short) (n.pot + change_factor * (Math.random()*2 -1)));
			}
		}
		
		return dna;
	}
	
	private short prob(float p) {
		return (short) ((Short.MAX_VALUE - Short.MIN_VALUE * p)+Short.MIN_VALUE);
	}
	
	private boolean roll(short amount) {
		return (short)RNG.nextInt() <= amount;
	}
	
	public DNA combine(DNA other) {
		return null;
	}
}
