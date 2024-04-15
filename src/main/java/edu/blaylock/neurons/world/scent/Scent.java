package edu.blaylock.neurons.world.scent;

public enum Scent {
	FRUITY(0),
	CREATURE(1),
	DEATH(2);
	
	final byte analog;
	Scent(int i){
		analog = (byte) i;
	}
}
