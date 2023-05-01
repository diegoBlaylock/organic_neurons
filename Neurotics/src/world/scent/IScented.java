package world.scent;

import logic.physics.Vecf;

public interface IScented {
	
	public Vecf getPosition();
	
	public short smell();
	
	public Scent getScent();
}
