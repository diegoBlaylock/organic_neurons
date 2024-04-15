package world.scent;

import logic.physics.Vecf;

public interface IScented {
	
	public Vecf getPosition();
	
	public byte smell();
	
	public Scent getScent();
}
