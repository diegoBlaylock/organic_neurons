package world;

import logic.Attributes.Attr;
import logic.physics.Physical;

public abstract class Entity extends Physical{
	
	World world;
	boolean rem_flag = false;
		
	public Entity() {super();}
	public Entity(World world) {
		this();
		world.addEntity(this);
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
	
	static boolean hasAttr(Entity e, Attr cls) {
		for(Attr a: e.getAttributes()){
			if(a == cls) return true;
		}
		return false;
	}
	
	public boolean removed() {
		return rem_flag;
	}
}
