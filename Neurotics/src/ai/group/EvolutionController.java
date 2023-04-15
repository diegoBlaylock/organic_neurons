package ai.group;

import world.events.ISpawnListener;
import ai.Creature;
import ai.neurons.DNA;
import world.Fruit;
import world.World;

public class EvolutionController implements ISpawnListener<Creature>{
	long max_life = 0;
	long score = 1000;
	DNA superior = null;

	int sum = 0;
	
	public void onSpawn(World world, Creature c) {
		sum++;
	}
	
	public void onDespawn(World world, Creature c) {

		sum--;
		
		if(c.life > max_life || (c.life == max_life && c.score < score)) {
			max_life = c.life;
			score = (long) c.score;
			superior = c.getDNA();
			System.out.println("\r" + max_life + " | " + score);
		}
		
		if(sum == 0) {
			repopulate(world);
			max_life = 0;

			score = 0;

		}
		
		
		
	}
	
	public void repopulate(World world) {
		for(int i = 0; i < 100 ; i++) {
		if(Math.random() < 0.01) {
			world.addEntity(new Creature());

		} else {
			if(superior != null) {
				world.addEntity(new Creature(superior.mutate()));
			} else {
				world.addEntity(new Creature());

			}
		}
		}
	}
}


