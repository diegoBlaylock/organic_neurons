package ai.group;

import world.events.ISpawnListener;
import ai.neurons.DNA;
import ent.Creature;
import ent.Fruit;
import world.World;

public class EvolutionController implements ISpawnListener<Creature>{
	long max_life = 0;
	double score = Double.MAX_VALUE;
	DNA superior = null;

	int sum = 0;
	
	public void onSpawn(World world, Creature c) {
		sum++;
	}
	
	public void onDespawn(World world, Creature c) {

		sum--;
		
		if(c.life > max_life) {
			max_life = c.life;
			superior = c.getDNA();
			score = Double.MAX_VALUE;
			System.out.println("\r" + max_life + " | " + score);
			
		}/* else if ((c.life == max_life && c.score < score)) {
			score = (long) c.score;
			superior = c.getDNA();
		}*/
		
		if(sum == 0) {
			

			repopulate(world);
		}
		
		
		
	}
	
	public void repopulate(World world) {
		for(int i = 0; i < 4 ; i++) {
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


