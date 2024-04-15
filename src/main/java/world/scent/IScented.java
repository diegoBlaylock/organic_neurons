package world.scent;

import edu.blaylock.neurons.logic.physics.Vecf;

public interface IScented {
	
	public Vecf getPosition();
	
	public byte smell();
	
	public Scent getScent();
}
