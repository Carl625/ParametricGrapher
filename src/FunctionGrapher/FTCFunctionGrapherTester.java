package FunctionGrapher;

import java.util.HashMap;

public class FTCFunctionGrapherTester extends FTCFunctionGrapher {
	
	private Function process;
	private Coordinate graphOffset;
	private double deltaX;
	private double xRange;
	private double yRange;
	
	FTCFunctionGrapherTester(Function newFunc, Coordinate initGraph, double newDeltaX, double newXRange, double newYRange) {
		
		process = newFunc;
		graphOffset = initGraph;
		deltaX = newDeltaX;
		xRange = newXRange;
		yRange = newYRange;
	}
	
	public double xIncrement() {
		return deltaX;
	}
	
	public double yValue(double xValue) {
		
		return process.output(xValue);
	}
	
	public Coordinate xyStart() {
		return graphOffset;
	}
	
	public double xRange() {
		return xRange;
	}
	
	public double yRange() {
		return yRange;
	}
	
	public static void main(String[] args) {
		
		HashMap<String, Double> TFVariables = new HashMap<String, Double>();
		TFVariables.put("JETH", 69.0);
		TFVariables.put("JETHANIEL", 881.233);
		TFVariables.put("JETHANY", 12.0);
		TFVariables.put("ELIZAJETH", 2.0);
		Function testFunction = new Function("(((JETHANIEL + LIFE_AND_JETH) + ((JETHANY ^ ELIZAJETH) ^ LIFE_AND_JETH)) + JETH)", "LIFE_AND_JETH", TFVariables);
		
		Function testFunc2 = new Function("(x ^ 2)", "x", new HashMap<String, Double>());
		
//		FTCFunctionGrapherTester f = new FTCFunctionGrapherTester(testFunction, new Coordinate(-10, 949), 0.01, 20.0, 20.0);
//		f.drawGraph(0, 0, 2000, 2000);
		
		// Trancendentals
		Function sinusoid = new Function("(x + ((sin(3 * x)) ^ x))", "x", new HashMap<String, Double>());
		Function naturalLog = new Function("(tanh(3 * x))", "x", new HashMap<String, Double>());
		Function cardiod = new Function("(1 + (sin(x)))", "x", new HashMap<String, Double>());
		
		FTCFunctionGrapherTester g = new FTCFunctionGrapherTester(testFunction, new Coordinate(-5, 945), 0.01, 10.0, 10.0);
		g.drawGraph(0, 0, 800, 800);
	}
}
