package ai;

import java.awt.Color;
import java.awt.Graphics2D;

import ai.neurons.DNA;
import ai.neurons.IThink;
import gui.IPaint;
import logic.Attributes.Attr;
import logic.ITick;
import logic.physics.ICollide;
import logic.physics.Physical;
import logic.physics.Vecf;
import world.Entity;
import world.Fruit;

public class Creature extends Entity implements IPaint, ITick, IThink, ICollide {

	final static Attr[] attributes = {Attr.THINKABLE, Attr.TICKABLE, Attr.COLLIDABLE};
	static short ID = Short.MIN_VALUE;
	final static Vecf shape = new Vecf(20,20);
	
	short id = ID++;
	final Brain brain;
	final DNA dna ;
	
	short hunger = 400;
	public long life = 0;
	public double score = 1000;
	
	public Creature() {
		this(DNA.genRandom());
	}
	
	public Creature(DNA mutate) {
		super();
		this.setShape(shape);
		this.dna = mutate;
		brain = dna.build(this);
		this.setPosition( new Vecf(75,75));//(Math.random() > 0.5 ? 75: 225), (Math.random() > 0.5 ? 75: 225)));
	}

	

	public void tick() {
		brain.tick();
		hunger--;
		life++;
	}
	
	public void think() {
		brain.think();
		
		
		
		if(hunger == Byte.MIN_VALUE) {
			this.getWorld().remEntity(this); 
			this.getWorld().getEntities().forEach((Entity e) -> {
				if(e instanceof Fruit) {
					double x = e.getPosition().getX() - this.getPosition().getX();
					double y = e.getPosition().getY() - this.getPosition().getY();
					score = x*x+y*y;
				}
			});
		}
	}
	
	@Override
	public Attr[] getAttributes() {
		return attributes;
	}

	@Override
	public void Paint(Graphics2D g) {
		
		g.setColor(Color.yellow);
		g.fillRect((int) (getPosition().getX()-shape.getX()/2),(int) (getPosition().getY()-shape.getY()/2), (int) shape.getX(), (int) shape.getY());
		g.setColor(Color.black);
		g.drawString(Integer.toHexString(Short.toUnsignedInt(id)), (int)getPosition().getX() +5 , (int)getPosition().getY()+5);
	}

	@Override
	public void onCollide(Physical ph) {
		if(ph instanceof Fruit ) {
			Fruit fruit = (Fruit) ph;
			if(fruit.removed()) return;
			hunger = (short) Math.min(hunger + 200, 500);
			fruit.getWorld().remEntity(fruit);
			
		}
	}

	public final DNA getDNA() {
		return dna;
	}
}
