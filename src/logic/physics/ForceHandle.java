package logic.physics;

import java.util.ArrayList;
import java.util.List;

public class ForceHandle {
	
	List<Vecf> forces;
	
	public ForceHandle() {
		forces = new ArrayList<Vecf>();
	}
	
	public void add(Vecf vec) {
		forces.add(vec);
	}

	public void clear() {
		forces.clear();
	}
	
	public Iterable<Vecf> getForces(){
		return forces;
	}
}
