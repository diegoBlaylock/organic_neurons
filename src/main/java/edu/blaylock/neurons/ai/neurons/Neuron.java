package edu.blaylock.neurons.ai.neurons;
import edu.blaylock.neurons.logic.ITick;

public class Neuron implements IFireable, ITick {
	int num = 0;

	Terminal[] terminals;
	private final short actionPotential;
	short pressure;
	private boolean visited;
	
	public Neuron(Terminal[] terminals, short potential) {
		this(terminals, potential, (short) 0);
	}
	
	public Neuron(Terminal[] terminals, short potential, short pressure) {
		this.terminals = terminals;
		this.actionPotential = potential;
		this.pressure = pressure;
		
		/*if(pressure > actionPotential) {
			fire(pressure);
		}*/
	}
	
	public boolean tickle(short amount) {
		if(pressure < actionPotential) {
			pressure += amount;
			if(pressure >= actionPotential) {
				fire(pressure);
				return true;
			}
		}
		
		return false;
	}
	
	public void tick() {
		if(visited) return;	
		if(pressure >= actionPotential) pressure = 0;
		visited = true;
		for(Terminal t: terminals) {
			t.tick();
		}
		visited = false;
	}

	@Override
	public void fire(short signal) {
		num++;
		System.out.println("NUM " + num);
		
		for(Terminal t: terminals) {
			t.send(signal);
		}
	}
	
}
