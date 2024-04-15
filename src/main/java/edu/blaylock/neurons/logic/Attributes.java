package edu.blaylock.neurons.logic;

import edu.blaylock.neurons.ai.neurons.IThink;
import edu.blaylock.neurons.gui.IPaint;
import edu.blaylock.neurons.logic.physics.ICollide;
import edu.blaylock.neurons.world.scent.IScented;

public class Attributes {
	public static enum Attr {
		TICKABLE(ITick.class),
		THINKABLE(IThink.class),
		SCENTED(IScented.class), 
		COLLIDABLE(ICollide.class),
		PAINTABLE(IPaint.class),
		EDIBLE(null);
		
		public Class<?> m_class;
		Attr(Class<?> cls){
			this.m_class = cls;
		}
		
	}
}
