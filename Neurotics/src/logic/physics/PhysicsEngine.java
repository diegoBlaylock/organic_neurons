package logic.physics;

public class PhysicsEngine {
	final static float MU_COEFF = 16;

	public static void runSim(){
		Physical.cache.forEach(PhysicsEngine::update);
		checkCollisions();
	}
	
	public static void update(Physical state) {

		Vecf netforce = Vecf.add(state.getHandle().getForces());
		netforce.div(state.getMass());


		state.state.position.x =  (state.getPosition().getX() + (state.getVelocity().getX() + 0.5*netforce.getX())); 
		state.state.position.y =  (state.getPosition().getY() + (state.getVelocity().getY() + 0.5*netforce.getY()));
		

		
		state.getVelocity().setX(state.getVelocity().getX() + netforce.getX());
		state.getVelocity().setY(state.getVelocity().getY() + netforce.getY());
		state.getHandle().clear();
		
		
		
		Vecf drag = new Vecf(state.getVelocity());
		drag.normalize();
		drag.scale((state.getVelocity().x*state.getVelocity().x + state.getVelocity().y*state.getVelocity().y) / MU_COEFF);

		drag.setX(-sign(drag.getX(), state.getVelocity().x)); 
		drag.setY(-sign(drag.getY(), state.getVelocity().y));
		state.getHandle().add(drag );
		
	}
	
	public static void checkCollisions() {
		synchronized(Physical.cache) {
			for(int i = 0; i < Physical.cache.size(); i++) {
				Physical a = Physical.cache.get(i);
				for(int j = 0; j < Physical.cache.size(); j++) {
					
					if(i == j) {
						continue;
					}
					
					Physical b = Physical.cache.get(j);

					
					if(b instanceof ICollide && j < i) continue;
					
					if(a instanceof ICollide) {
						if(testCollide(a,b)) {
							((ICollide) a).onCollide(b);
							if(b instanceof ICollide) {
								((ICollide) b).onCollide(a);
							}
						}
					}
				}
			}
		}
	}
	
	private static boolean testCollide(Physical a, Physical b) {
		double la = a.getPosition().x - a.getShape().x/2;
		double ra  = a.getPosition().x + a.getShape().x/2;
		double ua = a.getPosition().y - a.getShape().y/2;
		double da  = a.getPosition().y + a.getShape().y/2;
		
		double lb = b.getPosition().x - b.getShape().x/2;
		double rb  = b.getPosition().x + b.getShape().x/2;
		double ub = b.getPosition().y - b.getShape().y/2;
		double db  = b.getPosition().y + b.getShape().y/2;
		
		return (lb < ra) && (la < rb) &&  (ub < da) && (ua < db) ;
	}

	public static double sign(double a, double b) {		
		if(a>0 == a < b) 	return a;
		else return b;
	}
	
}
