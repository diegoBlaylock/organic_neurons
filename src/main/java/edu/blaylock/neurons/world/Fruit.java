package edu.blaylock.neurons.world;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.blaylock.neurons.gui.IPaint;
import edu.blaylock.neurons.logic.ITick;
import edu.blaylock.neurons.logic.Attributes.Attr;
import edu.blaylock.neurons.logic.physics.Vecf;
import edu.blaylock.neurons.world.scent.IScented;
import edu.blaylock.neurons.world.scent.Scent;

public class Fruit extends Entity implements IPaint, IScented{
	
	final static Attr[] ATTRIBUTES = {Attr.SCENTED, Attr.EDIBLE, Attr.PAINTABLE};

	public Fruit() {
		super();
		this.setShape(new Vecf(14,14));
		this.setPosition(new Vecf(100 + (Math.random() * 100), 100 +  (Math.random() *100)));
	}

	@Override
	public Attr[] getAttributes() {
		return ATTRIBUTES;
	}

	@Override
	public void Paint(Graphics2D g) {
		g.setColor(Color.orange);
		g.fillOval((int) this.getPosition().getX() -7, (int) this.getPosition().getY()-7, 14, 14);
	}

	@Override
	public byte smell() {
		return 127;
	}

	@Override
	public Scent getScent() {
		return Scent.FRUITY;
	}
	
	@Override
	public void onDispawn(World world) {
		world.addEntity(new Fruit());
	}
	
}
