package logic;

public class Timer {
	long speed = 1;
	volatile boolean isRunning = false;

	final Runnable tick;
	Thread thread;
	
	public Timer(Runnable r) {
		thread = new Thread(this::loop);
		tick = r;
	}
	
	
	public void start() {
		isRunning = true;
		thread.start();
	}
	
	
	private void loop() {
		long start;
		while(isRunning) {
			
			start = System.currentTimeMillis();
			tick.run();
			try {
				Thread.sleep(Math.max(0, - System.currentTimeMillis()+ speed + start));
			} catch (InterruptedException e) {
				
				break;
			}
		}
	}


	public long getSpeed() {
		return speed;
	}


	public void setSpeed(long max) {
			speed = max;
	}
	
	
}
