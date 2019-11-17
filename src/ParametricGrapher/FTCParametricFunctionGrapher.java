package ParametricGrapher;

public abstract class FTCParametricFunctionGrapher extends XYTGrapher{
	
	abstract public double xValue(double tValue);
	abstract public double yValue(double tValue);
	
	public double tValue(int pointNum) {
		
		return (tInitial()) + (tIncrement() * pointNum);
	}
	
	public Coordinate getPoint(int pointNum) {
		
		Coordinate newPoint;
		
		double tVal = tValue(pointNum);
		
		if (tVal >= (tInitial() + tRange())) {
			
			newPoint = null;
		} else if (tVal < tInitial()) {
			
			newPoint = null;
		} else {
			
			newPoint = new Coordinate(xValue(tVal), yValue(tVal));
		}
		
		return newPoint;
	}
	
}
