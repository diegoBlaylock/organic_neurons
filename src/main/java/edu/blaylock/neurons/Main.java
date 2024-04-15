package edu.blaylock.neurons;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingUtilities;

import edu.blaylock.neurons.ai.Creature;
import edu.blaylock.neurons.ai.group.EvolutionController;
import edu.blaylock.neurons.gui.ViewPort;
import edu.blaylock.neurons.gui.Window;
import edu.blaylock.neurons.logic.Timer;
import edu.blaylock.neurons.world.Fruit;
import edu.blaylock.neurons.world.World;
import world.events.ISpawnListener;

public class Main {
	
	static World world;
	static Window window;
	static Timer timer;
	
	public static void main(String[] args) {
		setupWorld();
		populate(64);
		setupTimer();
		
		setupGUI();
		
		start();
	}
	
	static void setupWorld() {
		world = new World();
		ISpawnListener.addListener(new EvolutionController(), Creature.class);
	}
	
	static void populate(int num) {
		
		
		for(int i = 0; i < num; i++) {
			world.addEntity(new Creature());
		}
		world.addEntity(new Fruit());

	}
	
	static void setupTimer() {
		timer = new Timer(Main::tick);
	}
	
	static void setupGUI() {
		window = new Window(500,500);
		window.add(new ViewPort(world));
		window.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == 's') {
					timer.setSpeed(Math.max(0,timer.getSpeed()-1));
				}
				
				if(e.getKeyChar() == 'w') {
					timer.setSpeed(Math.min(20,timer.getSpeed()+1));

				}
			}
		});
	}
	
	static void start() {
		timer.start();
		window.setVisible(true);
	}
	
	static void tick() {
		world.tick();
		SwingUtilities.invokeLater(window::repaint);
	}
}
