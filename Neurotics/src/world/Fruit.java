package world;

import java.awt.Color;
import java.awt.Graphics2D;

import gui.IPaint;
import logic.Attributes.Attr;
import logic.ITick;
import logic.physics.Vecf;
import world.scent.IScented;
import world.scent.Scent;

public class Fruit extends Entity implements ITick, IPaint, IScented{
	
	final static Attr[] ATTRIBUTES = {Attr.TICKABLE, Attr.SCENTED};

	public Fruit() {
		super();
		this.setShape(new Vecf(14,14));
		this.setPosition(new Vecf(100 + (Math.random() * 100), 100 +  (Math.random() *100)));
	}

	@Override
	public void tick() {
		
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
