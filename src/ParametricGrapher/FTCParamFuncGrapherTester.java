package ParametricGrapher;

import java.util.Arrays;
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
		
		Function parabola = new Function("(((sgn(t)) * (((t ^ 2.0) + ((t + 2.0) ^ 2.0)) ^ 0.5)) * (cos(atan((t + 2.0) / t))))", "t", new HashMap<String, Double>());
		ParametricFunction2D parabolaParametric = new ParametricFunction2D(parabola, false);
		
		Function derivative1 = Function.constSimplify(Function.derivative(parabola));
		//System.out.println(derivative1);
		//ParametricFunction2D derivParametric = new ParametricFunction2D(derivative1, false);
		
		ParametricFunction2D rotatedPP = ParametricFunction2D.rotate(parabolaParametric, Math.toRadians(90));
		//ParametricFunction2D regeneratedParabolaParametric = new ParametricFunction2D(rotatedPP.getPolarComponents().get1(), rotatedPP.getPolarComponents().get2(), true);
		
		FTCParamFuncGrapherTester g = new FTCParamFuncGrapherTester(parabolaParametric, new Pair<Coordinate, Coordinate>(new Coordinate(-10, -10), new Coordinate(10, 10)), -20, Math.PI / 100, 32, false);
		g.drawGraph(0, 0, 800, 800);
		//System.out.println(parabola);
		System.out.println(Arrays.toString(parabolaParametric.approximateBounds(new double[] {-20, 12}, 0.01)));
		System.out.println(Arrays.toString(parabolaParametric.findBounds(new double[] {-20, 12})));
//		FTCParamFuncGrapherTester h = new FTCParamFuncGrapherTester(derivParametric, new Pair<Coordinate, Coordinate>(new Coordinate(-20, -20), new Coordinate(20, 20)), -10, 0.001, 20.0, false);
//		h.drawGraph(0,  0, 2000, 2000);
	}
}
