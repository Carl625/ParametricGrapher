package PolarGrapher;

public class PolarGrapherTester extends PolarFunctionGrapher {

	public double thetaIncrement() {
		
		return (Math.PI / 20);
	}
	
	public double rValue(double thetaValue) {
		
		return thetaValue;
	}
	
	public Pair<Coordinate, Coordinate> xyStart() {
		
		return new Pair<Coordinate, Coordinate>(new Coordinate(-10, -10), new Coordinate(10, 10));
	}
	
	public double thetaInitial() {
		
		return 0;
	}
	
	public double thetaRange() {
		
		return (Math.PI * 8);
	}
	
	public static void main(String[] args) {
		
		PolarGrapherTester f = new PolarGrapherTester();
		f.drawGraph(0, 0, 700, 700);
	}

}
