package ParametricGrapher;

import java.util.HashMap;

public class FTCParamFuncGrapherTester extends FTCParametricFunctionGrapher {
	
	private Pair<Function, Function> process;
	private Pair<Coordinate, Coordinate> graphOffset;
	private double tInit;
	private double deltaT;
	private double tRange;
	private boolean isPolar;
	
	FTCParamFuncGrapherTester(ParametricFunction2D newFunc, Pair<Coordinate, Coordinate> initGraph, double newTInit, double newDeltaT, double newTRange, boolean polarity) {
		
		if (polarity) {
			process = newFunc.getPolarComponents();
		} else {
			process = newFunc.getRectangularComponents();
		}
		
		System.out.println("Process 1: " + process.get1().getOperationsTree());
		System.out.println("Process 2: " + process.get2().getOperationsTree());
		
		graphOffset = initGraph;
		tInit = newTInit;
		deltaT = newDeltaT;
		tRange = newTRange;
		isPolar = polarity;
	}
	
	public double xValue(double tValue) {
		
		return process.get1().output(tValue);
	}
	
	public double yValue(double tValue) {
		
		return process.get2().output(tValue);
	}
	
	public Pair<Coordinate, Coordinate> xyStart() {
		
		return graphOffset;
	}
	
	public double tInitial() {
		return tInit;
	}
	
	public double tIncrement() {
		return deltaT;
	}
	
	public double tRange() {
		return tRange;
	}
	
	public boolean graphPolar() {
		return isPolar;
	}
	
	public static void main(String[] args) {
		
//		Function sine = new Function("(8 * (sin(x)))", "x", new HashMap<String, Double>());
//		Function cosine = new Function("(4 * (cos(x)))", "x", new HashMap<String, Double>());
//		ParametricFunction2D circle = new ParametricFunction2D(sine, cosine, false);
		
		Function parabola = new Function("(x ^ 2)", "x", new HashMap<String, Double>());
		ParametricFunction2D parabolaParametric = new ParametricFunction2D(parabola, false);
		
		Function derivative1 = Function.simplify(Function.derivative(parabola));
		System.out.println(derivative1);
		ParametricFunction2D derivParametric = new ParametricFunction2D(derivative1, false);
		
		//ParametricFunction2D rotatedPP = ParametricFunction2D.rotate(parabolaParametric, Math.toRadians(168));
		//ParametricFunction2D regeneratedParabolaParametric = new ParametricFunction2D(rotatedPP.getPolarComponents().get1(), rotatedPP.getPolarComponents().get2(), true);
		
		FTCParamFuncGrapherTester g = new FTCParamFuncGrapherTester(parabolaParametric, new Pair<Coordinate, Coordinate>(new Coordinate(-2, -2), new Coordinate(2, 2)), -(Math.PI * 6), Math.PI / 500, Math.PI * 12, true);
		g.drawGraph(0, 0, 2000, 2000);
		//System.out.println(parabola);
		
		FTCParamFuncGrapherTester h = new FTCParamFuncGrapherTester(derivParametric, new Pair<Coordinate, Coordinate>(new Coordinate(-20, -20), new Coordinate(20, 20)), -10, 0.001, 20.0, false);
		h.drawGraph(0,  0, 2000, 2000);
	}
}
