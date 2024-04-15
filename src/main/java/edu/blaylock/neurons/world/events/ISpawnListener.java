package edu.blaylock.neurons.world.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import edu.blaylock.neurons.world.Entity;
import edu.blaylock.neurons.world.World;

public interface ISpawnListener <T extends Entity>{
	public void onSpawn(World world, T entity);
	public void onDespawn(World world, T entity);
	
	static Link<Entity> head = new Link<Entity>(Entity.class);
	
	static class Link <W extends Entity> {
		Collection<ISpawnListener<W>> listeners = new ArrayList<ISpawnListener<W>>();
		Link<? extends W>[] children;
		Class<W> cls;
		
		public Link(Class<?> cls2, ISpawnListener<?> ... listeners) {
			cls = (Class<W>) cls2;
			
			for(ISpawnListener<?> list : listeners) {
				this.listeners.add((ISpawnListener<W>) list);
			}
		}
		
		public void postSpawn(World world, Entity entity) {
			W ent = (W) entity;
			listeners.forEach((ISpawnListener<W> listener)->listener.onSpawn(world, ent));
		}
		
		public void postDespawn(World world, Entity entity) {
			W ent = (W) entity;
			listeners.forEach((ISpawnListener<W> listener)->listener.onDespawn(world, ent));
		}

		public void add(ISpawnListener<?> listener) {
			listeners.add((ISpawnListener<W>) listener);
		}
		
		public  <N extends Entity> void addChild(ISpawnListener<?> listener, Class<N> cls) {
			Link<? extends W> child = (Link<? extends W>) new Link<N>(cls, listener);
			if(children != null)
				children = Arrays.copyOf(children, children.length+1);
			else
				children = new Link[1];
			children[children.length-1] = (Link<? extends W>) child;
		}
	}
	
	static <T extends Entity> void addListener(ISpawnListener<T> listener, Class<T> cls) {
		Link<? extends Entity> track = head;
		
		while(track != null) {
			if(track.cls == cls) {
				track.add(listener);
				return;
			} else {
				if(track.children != null)
				for(Link<? extends Entity> link : track.children) {
					if(link.cls.isAssignableFrom(cls)) {
						track = link;
						continue;
					}
				}
				track.addChild(listener, cls);
				return;
			}
		}
	}
	
	public static <T extends Entity> void postSpawnEvent(World world, T entity) {
		Link<? extends Entity> track = head;
		cont:
		while(track!=null) {
			track.postSpawn(world, entity);
			
			if(track.children!=null)
			for(Link<? extends Entity> link : track.children) {
				if(link.cls.isInstance(entity)) {
					track = link;
					continue cont;
				}
			}
			break;
		}
	}
	
	public static <T extends Entity> void postDespawnEvent(World world, T entity) {
		Link<? extends Entity> track = head;
		
		cont:
		while(track!=null) {
			track.postDespawn(world, entity);
			
			if(track.children!=null)
			for(Link<? extends Entity> link : track.children) {
				if(link.cls.isInstance(entity)) {
					track = link;
					continue cont;
				}
			}
			break;
		}
	}
}
