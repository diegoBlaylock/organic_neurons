package world;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import ai.neurons.IThink;
import logic.Attributes.Attr;
import logic.ITick;
import logic.physics.Physical;
import logic.physics.PhysicsEngine;
import logic.physics.Vecf;
import world.events.ISpawnListener;
import world.scent.IScented;
import world.scent.Scent;

public class World {
	
	final String INSERTLOCK  = "worldinsert";
	final String REMOVELOCK  = "worldinsert";
	final String UNITLOCK = "worldlock";
	
	Collection<Entity> creatures = new ArrayList<Entity>();
	Queue<Entity> enteringCreatures = new ArrayDeque<Entity>();
	Queue<Entity> exitingCreatures = new ArrayDeque<Entity>();
	
	Collection<IScented>[] scentSources = (Collection<IScented>[]) Array.newInstance(Collection.class, Scent.values().length);
		
	public World() {
		for(int i = 0; i < Scent.values().length; i++) {
			scentSources[i] = new ArrayList<IScented>();
		}
	}
	
	public short scentIntensity(Scent type, Vecf loc) {
		short total = 0;
		
		for(IScented scented : scentSources[type.ordinal()]) {
			short intensity = (short) (scented.smell() + 128);
			double x = (scented.getPosition().getX() - loc.getX());
			double y = (scented.getPosition().getY() - loc.getY());
			
			double distance_sq = 1/15 * (x*x + y*y);
			
			total+= intensity / (distance_sq + 1);
		}
		
		
		return total;
		
	}
	
	public void addEntity(Entity e) {
		synchronized (INSERTLOCK) {
			enteringCreatures.add(e);
		}
	}
	
	public void remEntity(Entity e) {
		synchronized (REMOVELOCK) {
			if(!e.rem_flag) {
				exitingCreatures.add(e);
				e.rem_flag=true;
			}
		}
	}
	
	public Collection<Entity> getEntities(){
		return creatures;
	}
	
	public void tick() {
		
		synchronized (UNITLOCK) {
			synchronized (INSERTLOCK) {
				while(!enteringCreatures.isEmpty()) {
					enteringCreatures.peek().world = this;
					if(Entity.hasAttr(enteringCreatures.peek(), Attr.SCENTED)) {
						IScented candle = Entity.<IScented>convert(enteringCreatures.peek());
						scentSources[candle.getScent().ordinal()].add(candle);
					}
					
					synchronized(Physical.cache) {
						Physical.cache.add(enteringCreatures.peek());
					}

					creatures.add(enteringCreatures.peek());
					enteringCreatures.peek().onSpawn(this);
					ISpawnListener.postSpawnEvent(this, enteringCreatures.poll());
				}
			}
		}
		
		synchronized (UNITLOCK) {	
			synchronized (REMOVELOCK) {
				while(!exitingCreatures.isEmpty()) {
					exitingCreatures.peek().world = null;
					
					if(Entity.hasAttr(exitingCreatures.peek(), Attr.SCENTED)) {
						IScented candle = Entity.<IScented>convert(exitingCreatures.peek());
						scentSources[candle.getScent().ordinal()].remove(candle);
					}
					
					synchronized(Physical.cache) {
						Physical.cache.remove(exitingCreatures.peek());
					}

					creatures.remove(exitingCreatures.peek());
					exitingCreatures.peek().onDispawn(this);
					ISpawnListener.postDespawnEvent(this, exitingCreatures.poll());

				}				
			}
		}
		
		synchronized(UNITLOCK) {
			creatures.stream().filter((Entity e) -> Entity.hasAttr(e, Attr.TICKABLE))
			.map(Entity::<ITick>convert)
			.forEach(ITick::tick);
		}
		
		synchronized(UNITLOCK) {
//			for(Entity e : creatures) {
//				if(Entity.hasAttr(e, Attr.THINKABLE)) {
//					Entity.<IThink>convert(e).think();
//				}
//			}
			creatures.stream().filter((Entity e) -> Entity.hasAttr(e, Attr.THINKABLE))
			.map(Entity::<IThink>convert)
			.forEach((IThink e) -> {
				e.think();
			});
		}
		
		synchronized(UNITLOCK) {
			PhysicsEngine.runSim();
		}
	}

	public String getLock() {
		return UNITLOCK;
	}
	
}
