package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import ai.Creature;
import world.Entity;
import world.World;

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
