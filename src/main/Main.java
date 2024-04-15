package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingUtilities;

import ai.Creature;
import ai.group.EvolutionController;
import gui.ViewPort;
import gui.Window;
import logic.Timer;
import world.Fruit;
import world.World;
import world.events.ISpawnListener;

public class Main {
	
	static World world;
	static Window window;
	static Timer timer;
	
	public static void main(String[] args) {
		setupWorld();
		populate(16);
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
		window.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				

				if(e.getKeyChar() == 's') {
					timer.setSpeed(Math.max(0,timer.getSpeed()-1));
				}
				
				if(e.getKeyChar() == 'w') {
					timer.setSpeed(Math.min(20,timer.getSpeed()+1));

				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
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
