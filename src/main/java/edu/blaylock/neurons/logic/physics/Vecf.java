package edu.blaylock.neurons.logic.physics;

public class Vecf {
	double x;
	double y;
	
	public Vecf() {}
	
	public Vecf(double x, double y) {
		this.setX(x);
		this.setY(y);
	}
	
	public Vecf(Vecf force) {
		this(force.getX(), force.getY());
	}

	public static Vecf add(Iterable<Vecf> vectors) {
		Vecf result = new Vecf();
		
		vectors.forEach((Vecf vec) -> {
			result.x += vec.x;
			result.y += vec.y;}
		);
		
		return result;
	}
	
	public static Vecf add(Vecf... vecfs ) {
		Vecf result = new Vecf();
		
		for(Vecf v : vecfs) {
			result.x +=   v.getX();
			result.y +=  v.getY();
		}
	
		return result;
	}

	public void div(double mass) {
		setX(getX() / mass);
		setY(getY() / mass);
	}
	
	public void scale(double mass) {
		x=(x * mass);
		y= (y * mass);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return String.format("(%.2f, %.2f)", x, y);
	}

	public double FastSqrtInvAroundOne(double x)
	{
	  final float a0 = 15.0f / 8.0f;
	  final float a1 = -5.0f / 4.0f;
	  final float a2 = 3.0f / 8.0f;
	   
	  return a0 + (a1  + a2 * x )* x;
	}
	 
	
	public void normalize() {
		double result = 1/ Math.sqrt(x*x + y*y + 0.0000000001);
		
		x*=result;
		y*=result;
	}

	
	
}
