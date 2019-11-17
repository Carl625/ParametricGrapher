package PolarGrapher;

public abstract class PolarFunctionGrapher extends RθGrapher {
	
	abstract public double thetaIncrement();
	
	abstract public double rValue(double thetaValue);
	
	public double thetaValue(int pointNum) {
		
		return (thetaInitial()) + (thetaIncrement() * pointNum);
	}
	
	public CoordinatePolar getPoint(int pointNum) {
		
		CoordinatePolar newPoint;
		
		double thetaVal = thetaValue(pointNum);
		
		if (thetaVal >= (thetaInitial() + thetaRange())) {
			
			newPoint = null;
		} else if (thetaVal < (thetaInitial())) {
			
			newPoint = null;
		} else {
			
			newPoint = new CoordinatePolar(rValue(thetaVal), thetaVal);
		}
		
		return newPoint;
	}
	
	public static void main(String[] args) {
		System.out.println(true);
	}
}
