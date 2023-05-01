package world.scent;

public enum Scent {
	FRUITY(0),
	CREATURE(1),
	DEATH(2);
	
	final short analog;
	Scent(int i){
		analog = (short) i;
	}
}
