package logic;

import ai.neurons.IThink;
import logic.physics.ICollide;
import world.scent.IScented;

public class Attributes {
	public static enum Attr {
		TICKABLE(ITick.class),
		THINKABLE(IThink.class),
		SCENTED(IScented.class), 
		COLLIDABLE(ICollide.class);
		
		public Class<?> m_class;
		Attr(Class<?> cls){
			this.m_class = cls;
		}
		
	}
}
