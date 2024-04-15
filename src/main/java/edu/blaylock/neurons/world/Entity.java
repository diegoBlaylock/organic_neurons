package edu.blaylock.neurons.world;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import edu.blaylock.neurons.logic.Attributes.Attr;
import edu.blaylock.neurons.logic.physics.Physical;

public abstract class Entity extends Physical{
	static int ID = Integer.MIN_VALUE;
	static final Map<Id, WeakReference<Entity>> entitiesById = new WeakHashMap<Id, WeakReference<Entity>>();

	protected final Id id = new Id(ID++);
	World world;
	boolean rem_flag = false;
		
	public Entity() {
		super();
		entitiesById.put(id, new WeakReference<Entity>(this));
	}
	public Entity(World world) {
		this();
		if(world!=null) world.addEntity(this);
	}
	
	public World getWorld() {
		return world;
	}
	
	public void onDispawn(World w) {}
	
	public void onSpawn(World w) {}
	
	public abstract Attr[] getAttributes();
	
	public static <T> T convert(Entity e){
		return (T) e;
	}
	
	public static boolean hasAttr(Entity e, Attr cls) {
		for(Attr a: e.getAttributes()){
			if(a == cls) return true;
		}
		return false;
	}
	
	public boolean removed() {
		return rem_flag;
	}
	
	public int getId() {
		return id.value;		
	}
	
	public static Entity getEntity(int id) {
		WeakReference<Entity> entity = entitiesById.get(new Id(id));
		if(entity == null) {
			entitiesById.remove(id);
			return null;
		}
		return entity.get();
	}
	
	static record Id(int value) {
		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			else if(o instanceof Integer i) return this.value == i.intValue();
			else if (o instanceof Id id) return this.value == id.value;
			return false;
		}
	}
}
