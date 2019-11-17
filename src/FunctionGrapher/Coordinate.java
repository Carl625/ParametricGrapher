package FunctionGrapher;

public class Coordinate {
	
	private double x;
	private double y;
	private boolean drawTo;
	private boolean drawFrom;
	
	Coordinate(double newX, double newY) {
		
		x = newX;
		y = newY;
		drawTo = true;
		drawFrom = true;
	}
	
	Coordinate(double newX, double newY, boolean newDrawTo, boolean newDrawFrom) {
		
		x = newX;
		y = newY;
		drawTo = newDrawTo;
		drawFrom = newDrawFrom;
	}
	
	public double getX() {
		
		return x;
	}
	
	public double getY() {
		
		return y;
	}
	
	public boolean drawTo() {
		
		return drawTo;
	}
	
	public boolean drawFrom() {
		
		return drawFrom;
	}
}
