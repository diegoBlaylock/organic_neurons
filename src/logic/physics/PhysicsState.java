package logic.physics;

public class PhysicsState {
	
	public static final PhysicsState NULL_STATE = null;
	
	
	PhysicsState(double mass) {
		this.mass = mass;
	}
	
	public ForceHandle forces = new ForceHandle();
	
	public Vecf position;
	
	public Vecf shape = new Vecf(0,0);
	
	public Vecf velocity;
	
	public final double mass;
	
	
	public static PhysicsState buildState(double mass, Vecf position, Vecf velocity) { 
		PhysicsState state = new PhysicsState(mass);
		state.position = position;
		state.velocity = velocity;
		return state;
	}
	
}
