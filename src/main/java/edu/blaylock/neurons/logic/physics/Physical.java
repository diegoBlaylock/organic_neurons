package edu.blaylock.neurons.logic.physics;

import java.util.ArrayList;
import java.util.List;

public class Physical {
	
	public static List<Physical> cache = new ArrayList<Physical>();

	
	PhysicsState state = PhysicsState.buildState(1, new Vecf(0,0), new Vecf(0,0));
	
	public double getMass() {
		return state.mass;
	}
	
	public void setPosition(Vecf pos) {
		state.position = pos;
		
	}
	
	public Vecf getShape() {
		return state.shape;
	}
	
	public void setShape(Vecf shape) {
		state.shape = shape;
	}
	
	public Vecf getPosition() {
		return state.position;
	}
	
	public Vecf getVelocity() {
		return state.velocity;
	}
	
	public void addForce(Vecf v) {
		state.forces.add(v);
	}
	
	public ForceHandle getHandle() {
		return state.forces;
	}
	
	public PhysicsState gestPhysics() {
		return state;
	}

	
}
