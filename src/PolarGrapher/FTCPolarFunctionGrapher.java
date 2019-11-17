package PolarGrapher;

import java.util.HashMap;

public class FTCPolarFunctionGrapher extends PolarFunctionGrapher {
	
	private Function process;
	private Pair<Coordinate, Coordinate> graphOffset;
	private double thetaInit;
	private double deltaTheta;
	private double thetaRange;
	
	FTCPolarFunctionGrapher(Function newFunc, Pair<Coordinate, Coordinate> initGraph, double newThetaInit, double newDeltaTheta, double newThetaRange) {
		
		process = newFunc;
		graphOffset = initGraph;
		thetaInit = newThetaInit;
		deltaTheta = newDeltaTheta;
		thetaRange = newThetaRange;
	}
	
	public double thetaIncrement() {
		
		return deltaTheta;
	}
	
	public double rValue(double thetaValue) {
		
		return process.output(thetaValue);
	}
	
	public Pair<Coordinate, Coordinate> xyStart() {
		
		return graphOffset;
	}
	
	public double thetaInitial() {
		
		return thetaInit;
	}
	
	public double thetaRange() {
		
		return thetaRange;
	}
	
	public static void main(String[] args) {
		
		Function polarFunc1 = new Function("((theta ^ 3) + (2 * (theta ^ 2)) - (7 * theta))", "theta", new HashMap<String, Double>());
		Function polarFunc2 = new Function("(((((x ^ 3) - (7 * x)) - (2 * (x ^ 2))) * ((x ^ 2) + (2 * x))) + 3)", "x", new HashMap<String, Double>());
		//FTCPolarFunctionGrapher f = new FTCPolarFunctionGrapher(polarFunc2, new Pair<Coordinate, Coordinate>(new Coordinate(-10, -10), new Coordinate(10, 10)), -(Math.PI * 12), Math.PI / 500, Math.PI * 32);
		//f.drawGraph(0, 0, 1500, 1500);
		
		Function polarTFUNC1 = new Function("(atan(x ^ x))", "x", new HashMap<String, Double>());
		//Function cardoid = new Function("(1 + (sin(x)))", "x", new HashMap<String, Double>());
        FTCPolarFunctionGrapher g = new FTCPolarFunctionGrapher(polarTFUNC1, new Pair<Coordinate, Coordinate>(new Coordinate(-3, -3), new Coordinate(3, 3)), -(Math.PI * 12), Math.PI / 100, Math.PI * 24);
		g.drawGraph(0, 0, 1500, 1500);
	}
}
