package edu.blaylock.neurons.ai.neurons;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.blaylock.neurons.ai.Brain;
import edu.blaylock.neurons.ai.Creature;
import edu.blaylock.neurons.ai.neurons.motor.MovementNeuron;
import edu.blaylock.neurons.ai.neurons.sensory.ASensoryNeuron;
import edu.blaylock.neurons.ai.neurons.sensory.ScentNeuron;
import edu.blaylock.neurons.logic.physics.Vecf;
import world.scent.Scent;

public class DNA {
	
	final static int POOL_SIZE = 256;
	final static int INP_SIZE = 64; // REMOVE AFTER MVP FOR VARIABLE LENGTH
	final static Random RNG = new Random();

	
	short[] connections;
	N[] pool;
	N[] motorConnections;
	
	DNA(){}
	
	/* terminals 
	  		- short strength
			- connection
		Neurons
			- action potential
			- inital pressure
			
		Sensory nerves: simple feed foward. Preplanned or custom?
			- 16 scent
			- 4 force
			- Reinsert 
		Interneural net:
			- List N neurons
				Neuron[i]:
					- short Potential
					- short initial
					- byte length terminal (1-8)
						Teminal:
							byte strength (0-127)
							int connection (Neuron[connection]
		motor neurons: 
			- planned 4 at end for force
			- planned [Var] for reinsert
			- pick multiple from Interneural net
		
		Attributes:
			- max force
	*/
	
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
		public byte str;
		public short conn;
		
		public void setStrength(byte newStrength) {
			if(newStrength <= 0) {
				if(str < 127) str = 1;
				else str = Byte.MAX_VALUE;
			}
			
			str=newStrength;
		}
	}
	
	public final static DNA genRandom() {
		
		DNA dna = new DNA();
		
		dna.pool = new N[POOL_SIZE];
		
		for(int i = 0; i < POOL_SIZE; i++) {
			
			dna.pool[i] = genN();
		}
		
		dna.motorConnections = new N[4];
		
		for(int i = 0 ; i < 4; i++) {
			dna.motorConnections[i] = genN();
			dna.motorConnections[i].termin = new T[INP_SIZE];
			
			for(int j = 0 ; j < dna.motorConnections[i].termin.length; j++) {
				dna.motorConnections[i].termin[j] = genT();
			}
		}
		
		dna.connections = new short[4];
		
		for(int i = 0 ; i < 4; i++) {
			dna.connections[i] = (short) RNG.nextInt(POOL_SIZE);
			
		}
		
		return dna;
	}
	
	static N genN() {
		N n = new N();
		n.init = (short) RNG.nextInt(1024);
		n.pot = (short) RNG.nextInt(1024);
		n.termin = new T[RNG.nextInt(8)+1];
		
		for(int i = 0 ; i < n.termin.length; i++) {
			n.termin[i] = genT();
		}
		
		return n;
	}
	
	static T genT() {
		T t = new T();
		t.str = (byte) (RNG.nextInt(127));
		t.conn = (short) RNG.nextInt(POOL_SIZE);
		return t;
	}
	
	public Brain build(Creature c) {
		Prebrain brain = new Prebrain();
		buildPool(brain);
		buildSenses(brain, c);
		buildMotors(brain, c);
		return brain.build();
		
	}
	
	void buildPool(Prebrain brain) {
		List<Terminal>[] needsLoading = (List<Terminal>[]) Array.newInstance(List.class, POOL_SIZE);
		Neuron[] pool = new Neuron[POOL_SIZE];
		for(int i = 0 ; i < pool.length; i++) {
			Terminal[] terminals = new Terminal[this.pool[i].termin.length];
			
			
			for(int j = 0; j < terminals.length; j++) {
				short loc = this.pool[i].termin[j].conn;
				Neuron connection = (loc < i)? pool[i] : null;
				
				terminals[j] = new Terminal(connection, this.pool[i].termin[j].str);
				
				if(connection == null) {
					if(needsLoading[loc] == null) {
						needsLoading[loc] = new ArrayList<Terminal>();
					}
					needsLoading[loc].add(terminals[j]);
				}
				
				
			}
			
			pool[i] = new Neuron(terminals, this.pool[i].pot);
			pool[i].pressure =  this.pool[i].init;
		}
		
		for(int i = 0 ; i < needsLoading.length; i++) {
			List<Terminal> terminals = needsLoading[i];
			if(terminals!= null) {
				for(Terminal t : terminals) {
					t.connection = pool[i];
				}
			}
		}
		
		brain.pool = pool;
	}
	
	// Custom senses- right now 1 scent for bi pole
	void buildSenses(Prebrain brain, Creature creature) {
		ASensoryNeuron[] senses = {
				new ScentNeuron(creature, brain.pool[connections[0]], Scent.FRUITY, new Vecf(-10,0)), 
				new ScentNeuron(creature, brain.pool[connections[1]], Scent.FRUITY, new Vecf(10,0)),
				new ScentNeuron(creature, brain.pool[connections[2]], Scent.FRUITY, new Vecf(0,-10)),
				new ScentNeuron(creature, brain.pool[connections[3]], Scent.FRUITY, new Vecf(0,10))};
		brain.senses = senses;
	}
	
	void buildMotors(Prebrain brain, Creature creature) {
		Vecf[] directions = {new Vecf(-0.5, 0), new Vecf(0.5, 0), new Vecf(0, -0.5), new Vecf(0, 0.5)};
		
		MovementNeuron[] motors = new MovementNeuron[4];
		
		for(int i = 0 ; i < motorConnections.length; i++) {
			motors[i] = new MovementNeuron(creature, directions[i], motorConnections[i].pot, motorConnections[i].init);
			
			for(int j = 0 ; j < motorConnections[i].termin.length; j++) {
				Terminal term = new Terminal(motors[i], motorConnections[i].termin[j].str);
				brain.pool[motorConnections[i].termin[j].conn].terminals = Arrays.copyOf(brain.pool[motorConnections[i].termin[j].conn].terminals, brain.pool[motorConnections[i].termin[j].conn].terminals.length+1);
				brain.pool[motorConnections[i].termin[j].conn].terminals[brain.pool[motorConnections[i].termin[j].conn].terminals.length-1] = term;
			
			}
		}
	
	}
	
	
	private class Prebrain{
		Neuron[] pool;
		ASensoryNeuron[] senses;
		
		public Brain build() {
			return new Brain(senses);
		}
	}
	
	public DNA mutate() {
		final float pot_change = 0.1f;
		final float str_change = 0.1f;
		final short change_factor = 20;
		
		
		DNA dna = new DNA();
		dna.connections = this.connections;
		dna.motorConnections = this.motorConnections;
		dna.pool = this.pool;
		//SENSORS
		
		
		// POOL
		for(N n : dna.pool) {
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
				
				if(roll(str_change)) {
					t.conn = (short) ((t.conn+1) % POOL_SIZE);
				}
			}
		}
		
		//MOTORS
		for(N n : dna.motorConnections) {
			if(roll(pot_change)) {
				n.setPotential((short) (n.pot + change_factor * (Math.random()*2 -1)));
			}
			
			for(T t : n.termin) {
				if(roll(str_change)) {
					t.setStrength((byte) (t.str+ change_factor * (Math.random()*2 -1)));
				}
			}
		}
		
		return dna;
	}
	
	private boolean roll(float amount) {
		return (float) RNG.nextFloat() <= amount;
	}
	
	public DNA combine(DNA other) {
		return null;
	}
}
