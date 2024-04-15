package edu.blaylock.neurons.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import edu.blaylock.neurons.ai.Creature;
import edu.blaylock.neurons.world.Entity;
import edu.blaylock.neurons.world.World;

public class ViewPort extends JComponent {
	
	World world;
	
	public ViewPort(World world) {
		super();
		this.world = world;
	}
	
	void paintIndividual(IPaint e, Graphics2D g) {
		e.Paint(g);
	}
	
	void render(Graphics2D g) {
		synchronized(world.getLock()) {
			world.getEntities().stream()
			.filter(IPaint.class::isInstance)
			.map(Entity::<IPaint>convert)
			.forEach((IPaint e) -> paintIndividual(e, g));
		}	
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		render((Graphics2D)g);
	}
	
	
}
