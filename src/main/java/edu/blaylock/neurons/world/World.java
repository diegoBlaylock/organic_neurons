package edu.blaylock.neurons.world;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import edu.blaylock.neurons.ai.neurons.IThink;
import edu.blaylock.neurons.logic.ITick;
import edu.blaylock.neurons.logic.Attributes.Attr;
import edu.blaylock.neurons.logic.physics.Physical;
import edu.blaylock.neurons.logic.physics.PhysicsEngine;
import edu.blaylock.neurons.logic.physics.Vecf;
import edu.blaylock.neurons.world.events.ISpawnListener;
import edu.blaylock.neurons.world.scent.IScented;
import edu.blaylock.neurons.world.scent.Scent;

public class World {
	
	final String INSERTLOCK  = "worldinsert";
	final String REMOVELOCK  = "worldinsert";
	final String UNITLOCK = "worldlock";
	
	Collection<Entity> creatures = new ArrayList<Entity>();
	Queue<Entity> enteringCreatures = new ArrayDeque<Entity>();
	Queue<Entity> exitingCreatures = new ArrayDeque<Entity>();
	
	private final Map<Attr, Set<Integer>> attributeMap = new HashMap<Attr, Set<Integer>>();
	
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
					Entity e = enteringCreatures.poll();
					e.world = this;
					
					for(Attr attr : e.getAttributes()) {
						attributeMap.computeIfAbsent(attr, (nil)->new HashSet<Integer>()).add(e.getId());
					
						if(attr ==  Attr.SCENTED) {
							IScented candle = Entity.<IScented>convert(e);
							scentSources[candle.getScent().ordinal()].add(candle);
						}
					}
					
					synchronized(Physical.cache) {
						Physical.cache.add(e);
					}

					creatures.add(e);
					e.onSpawn(this);
					ISpawnListener.postSpawnEvent(this, e);
				}
			}
		}
		
		synchronized (UNITLOCK) {	
			synchronized (REMOVELOCK) {
				while(!exitingCreatures.isEmpty()) {
					Entity e = exitingCreatures.poll();
					e.world = null;
					
					for(Attr attr : e.getAttributes()) {
						attributeMap.computeIfAbsent(attr, (nil)->new HashSet<Integer>()).remove(e.getId());
					
						if(attr ==  Attr.SCENTED) {
							IScented candle = Entity.<IScented>convert(e);
							scentSources[candle.getScent().ordinal()].remove(candle);
						}
					}
					
					synchronized(Physical.cache) {
						Physical.cache.remove(e);
					}

					creatures.remove(e);
					e.onDispawn(this);
					ISpawnListener.postDespawnEvent(this, e);

				}				
			}
		}
		
		synchronized(UNITLOCK) {
			getEntitiesByAttribute(Attr.TICKABLE).stream()
			.map(Entity::<ITick>convert)
			.forEach(ITick::tick);
		}
		
		synchronized(UNITLOCK) {
			getEntitiesByAttribute(Attr.THINKABLE).stream()
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
	
	public Collection<Entity> getEntitiesByAttribute(Attr attr) {
		Set<Integer> set = attributeMap.get(attr);
		if(set.size() == 0) return Collections.emptyList();
		
		return set.stream().map(Entity::getEntity).collect(Collectors.toUnmodifiableList());
		
	}
	
}
