package FunctionGrapher;

public abstract class FTCFunctionGrapher extends XYGrapher{
	
	abstract public double xIncrement();
	
	abstract public double yValue(double xValue);
	
	public double xValue(int pointNum) {
		
		return (xyStart().getX()) + (xIncrement() * pointNum);
	}
	
	public Coordinate getPoint(int pointNum) {
		
		Coordinate newPoint;
		
		double xVal = xValue(pointNum);
		
		if (xVal >= (xyStart().getX() + xRange())) {
			
			newPoint = null;
		} else if (xVal < (xyStart().getX())) {
			
			newPoint = null;
		} else {
			
			newPoint = new Coordinate(xVal, yValue(xVal));
		}
		
		return newPoint;
	}
	
}
