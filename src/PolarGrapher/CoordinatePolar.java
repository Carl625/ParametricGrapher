package PolarGrapher;

public class CoordinatePolar {
	
	private double r;
	private double theta;
	private boolean drawTo;
	private boolean drawFrom;
	
	CoordinatePolar(double newR, double newTheta) {
		
		r = newR;
		theta = newTheta;
		drawTo = true;
		drawFrom = true;
	}
	
	CoordinatePolar(double newR, double newTheta, boolean newDrawTo, boolean newDrawFrom) {
		
		r = newR;
		theta = newTheta;
		drawTo = newDrawTo;
		drawFrom = newDrawFrom;
	}
	
	public double getR() {
		
		return r;
	}
	
	public double getTheta() {
		
		return theta;
	}
	
	public boolean drawTo() {
		
		return drawTo;
	}
	
	public boolean drawFrom() {
		
		return drawFrom;
	}
}
