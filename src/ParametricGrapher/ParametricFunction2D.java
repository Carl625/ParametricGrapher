package ParametricGrapher;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.Double.NaN;

public class ParametricFunction2D {

    private Pair<Function, Function> polarComponents;
    private Pair<Function, Function> rectangularComponents;

    /**
     * @Description
     * @param combined
     * @param isPolar
     */
    public ParametricFunction2D(Function combined, boolean isPolar) {

        if (isPolar) {
            polarComponents = parametrize(combined, isPolar);
            rectangularComponents = rectangularize(polarComponents.get1(), polarComponents.get2());
        } else {
            rectangularComponents = parametrize(combined, isPolar);
            polarComponents = polarize(rectangularComponents.get1(), rectangularComponents.get2());
        }
    }

    /**
     * @Description for cartesian: one is the x component and two is the y component; for polar: one is the function for magnitude and two is the function for theta
     * @param one
     * @param two
     * @param isPolar
     */

    public ParametricFunction2D(Function one, Function two, boolean isPolar) {

        if (!one.getVariable().equals("t")) {
            Function.replaceVariable(one.getRoot(), "t", one.getVariable(), one.getConstantList());
        }
        if (!two.getVariable().equals("t")) {
            Function.replaceVariable(two.getRoot(), "t", two.getVariable(), two.getConstantList());
        }

        if (isPolar) {

            polarComponents = new Pair<Function, Function>(one, two);
            rectangularComponents = rectangularize(one, two);
        } else {

            rectangularComponents = new Pair<Function, Function>(one, two);
            polarComponents = polarize(one, two);
        }
    }

    public ParametricFunction2D(ParametricFunction2D oldFunc) {

        rectangularComponents = oldFunc.getRectangularComponents();
        polarComponents = oldFunc.getPolarComponents();
    }

    //---------- Function Properties ----------//

    public String getVariable() {
        return "t";
    }

    public double genMag(double tVal) {

        return Math.sqrt(Math.pow(rectangularComponents.get1().output(tVal), 2) + Math.pow(rectangularComponents.get2().output(tVal), 2));
    }

    public double genAngle(double tVal) {
        double xCompOutput = rectangularComponents.get1().output(tVal);
        double yCompOutput = rectangularComponents.get2().output(tVal);
        double theta = 0;

        if (xCompOutput == 0 && yCompOutput == 0) {

            theta = 0;
        } else {

            if (xCompOutput == 0) {

                theta = Math.PI / 2;

                if (yCompOutput < 0) {
                    theta *= -1;
                }
            } else {

                theta = Math.atan(yCompOutput / xCompOutput);

                if (xCompOutput < 0) {

                    if (yCompOutput >= 0) {

                        theta += Math.PI;
                    } else if (yCompOutput < 0) {

                        theta -= Math.PI;
                    } else {

                        theta = Math.PI;
                    }
                }
            }
        }

        return theta;
    }

    public Pair<Function, Function> getRectangularComponents() {
        return (new Pair<Function, Function>(Function.makeFunctionCopy(rectangularComponents.get1()), Function.makeFunctionCopy(rectangularComponents.get2())));
    }

    public Pair<Function, Function> getPolarComponents() {
        return (new Pair<Function, Function>(Function.makeFunctionCopy(polarComponents.get1()), Function.makeFunctionCopy(polarComponents.get2())));
    }

    //---------- Function Operations ----------//

    public static Pair<Function, Function> parametrize(Function pathComponent, boolean isPolar) {

        Node dependent = Function.replaceVariable(pathComponent.getRoot(), "t", pathComponent.getVariable(), pathComponent.getConstantList());
        Node independent = new Node(Node.paramType.Variable, "t");

        if (isPolar) {
            return new Pair<Function, Function>(Function.makeFunction(dependent, "t"), Function.makeFunction(independent, "t"));
        } else {
            return new Pair<Function, Function>(Function.makeFunction(independent, "t"), Function.makeFunction(dependent, "t"));
        }
    }

    public static Pair<Function, Function> polarize(Function x, Function y) {

        Node square1 = Function.operate(x.getRoot(), new Node(Node.paramType.Const, "2"), "^"); // x^2
        Node square2 = Function.operate(y.getRoot(), new Node(Node.paramType.Const, "2"), "^"); // y^2
        Node sum = Function.operate(square1, square2, "+"); // (x^2) + (y^2)
        Node r = Function.operate(sum, new Node(Node.paramType.Const, "0.5"), "^"); // sqrt((x^2) + (y^2))
        r = Function.operate(Function.composeTFUNC(x.getRoot(), "t", Node.T_FUNC_TYPES.sgn), r, "*");

        Node diff = Function.operate(y.getRoot(), x.getRoot(), "/"); // y/x
        Node theta = Function.composeTFUNC(diff, "t", Node.T_FUNC_TYPES.atan); // arctan(y/x)

        return (new Pair<Function, Function>(Function.makeFunction(r, "t"), Function.makeFunction(theta, "t")));
    }

    public static Pair<Function, Function> rectangularize(Function r, Function theta) {

        Node sin = Function.composeTFUNC(theta.getRoot(), "t", Node.T_FUNC_TYPES.sin); // sin(theta)
        Node cos = Function.composeTFUNC(theta.getRoot(), "t", Node.T_FUNC_TYPES.cos); // cos(theta)

        Node x = Function.operate(r.getRoot(), cos, "*");
        Node y = Function.operate(r.getRoot(), sin, "*");

        return (new Pair<Function, Function>(Function.makeFunction(x, "t"), Function.makeFunction(y, "t")));
    }

    //---------- Parametric Operations ----------//

    public void add(ParametricFunction2D term_two) {

        Pair<Function, Function> two_comp = term_two.getRectangularComponents();
        rectangularComponents.setT1(Function.operate(rectangularComponents.get1(), two_comp.get1(), Function.operation.addition));
        rectangularComponents.setT2(Function.operate(rectangularComponents.get2(), two_comp.get2(), Function.operation.addition));

        polarComponents = polarize(rectangularComponents.get1(), rectangularComponents.get2());
    }

    public void sub(ParametricFunction2D term_two) {

        add(invert(term_two));
    }

    public ParametricFunction2D invert(ParametricFunction2D term_two) {

        ParametricFunction2D iTwo = new ParametricFunction2D(term_two);
        iTwo.scale(-1);

        return iTwo;
    }

    public void scale(double scalar) {

        Node constantRoot = new Node(Node.paramType.Const, String.valueOf(scalar));
        rectangularComponents.setT1(Function.operate(rectangularComponents.get1(), Function.makeFunction(constantRoot, rectangularComponents.get1().getVariable()), Function.operation.multiplication));
        rectangularComponents.setT2(Function.operate(rectangularComponents.get2(), Function.makeFunction(constantRoot, rectangularComponents.get2().getVariable()), Function.operation.multiplication));

        polarComponents = polarize(rectangularComponents.get1(), rectangularComponents.get2());
    }

    public double dot(ParametricFunction2D term_two, double tVal) {

        Pair<Function, Function> two_comp = term_two.getRectangularComponents();

        return ((rectangularComponents.get1().output(tVal) * two_comp.get1().output(tVal)) + (rectangularComponents.get2().output(tVal) * two_comp.get2().output(tVal)));
    }

    public double angleBetween(ParametricFunction2D f2, double tVal) {

        double dotProduct = this.dot(f2, tVal);
        double magnitudeProducts = polarComponents.get1().output(tVal) * f2.getPolarComponents().get1().output(tVal);

        return Math.acos(dotProduct / magnitudeProducts);
    }

    public static double standardPosAngle(ParametricFunction2D v, double tVal) {

        Vector2D i = new Vector2D(1d, 0d);
        Vector2D j = new Vector2D(0d, 1d);

        Function const1 = Function.makeFunction(new Node(Node.paramType.Const, "1"), v.getRectangularComponents().get1().getVariable());
        Function const0 = Function.makeFunction(new Node(Node.paramType.Const, "0"), v.getRectangularComponents().get2().getVariable());
        ParametricFunction2D iVector = new ParametricFunction2D(const1, const0, false);
        ParametricFunction2D jVector = new ParametricFunction2D(const0, const1, false);

        double iAngle = v.angleBetween(iVector, tVal);

        if (v.angleBetween(jVector, tVal) > (Math.PI / 2.0)) {

            iAngle = (Math.PI * 2.0) - iAngle;
        }

        return iAngle;
    }

    /**
     * reverses the first rectangular component of the parametric function, or reflects over the Y axis
     */
    public void flipOverY() {
        rectangularComponents.setT1(Function.operate(rectangularComponents.get1(), Function.makeFunction(new Node(Node.paramType.Const, "-1"), rectangularComponents.get1().getVariable()), Function.operation.multiplication));
    }

    /**
     * reverses the second rectangular component of the parametric function, or reflects over the X axis
     */
    public void flipOverX() {
        rectangularComponents.setT2(Function.operate(rectangularComponents.get2(), Function.makeFunction(new Node(Node.paramType.Const, "-1"), rectangularComponents.get2().getVariable()), Function.operation.multiplication));
    }

    /**
     * This function rotates the function x radians counterclockwise
     * @param radians
     */
    public static ParametricFunction2D rotate(ParametricFunction2D originalFunc, double radians) {

        Function newScale = Function.makeFunctionCopy(originalFunc.getPolarComponents().get1());
        Function newTheta = Function.makeFunction(Function.operate(Function.makeTreeCopy(originalFunc.getPolarComponents().get2().getRoot()), new Node(Node.paramType.Const, Double.toString(radians)), "+"), "t");

        return (new ParametricFunction2D(newScale, newTheta, true));
    }

    public static ParametricFunction2D derivativeParametric(ParametricFunction2D originalFunc) {

        Function derivativeyt = Function.constSimplify(Function.derivative(originalFunc.getRectangularComponents().get2()));
        Function derivativext = Function.constSimplify(Function.derivative(originalFunc.getRectangularComponents().get1()));

        return (new ParametricFunction2D(derivativeyt, derivativext, false));
    }

    public static Function derivative(ParametricFunction2D originalFunc) {

        ParametricFunction2D derivative = derivativeParametric(originalFunc);

        return (Function.operate(derivative.getRectangularComponents().get2(), derivative.getRectangularComponents().get1(), Function.operation.division));
    }



    public static Function getSignedCurvature(ParametricFunction2D originalFunc) {

        Function x = originalFunc.getRectangularComponents().get1();
        Function y = originalFunc.getRectangularComponents().get2();

        // first and second derivatives
        Function dy_1 = Function.constSimplify(Function.derivative(y));
        Function dy_2 = Function.constSimplify(Function.derivative(dy_1));
        Function dx_1 = Function.constSimplify(Function.derivative(x));
        Function dx_2 = Function.constSimplify(Function.derivative(dx_1));

        // top term
        Function topProduct1 = Function.operate(dx_1, dy_2, Function.operation.multiplication);
        Function topProduct2 = Function.operate(dx_2, dy_1, Function.operation.multiplication);
        Function topTerm = Function.operate(topProduct1, topProduct2, Function.operation.subtraction);

        // bottom term
        Node bottomSquare1 =  Function.operate(dx_1.getRoot(), new Node(Node.paramType.Const, "2"), "^");
        Node bottomSquare2 = Function.operate(dy_1.getRoot(), new Node(Node.paramType.Const, "2"), "^");
        Node bottomSum = Function.operate(bottomSquare1, bottomSquare2, "+");
        Node bottomTerm = Function.operate(bottomSum, new Node(Node.paramType.Const, "1.5"), "^");

        Function curvature = Function.operate(topTerm, Function.makeFunction(bottomTerm, dy_2.getVariable()), Function.operation.division);

        return curvature;
    }

    public static Function getCurvature(ParametricFunction2D originalFunc) {
        Function curvature = ParametricFunction2D.getSignedCurvature(originalFunc);
        Node absCurvature = Function.composeTFUNC(curvature.getRoot(), originalFunc.getVariable(), Node.T_FUNC_TYPES.abs);

        return Function.makeFunction(absCurvature, originalFunc.getVariable());
    }

    //---------- Function outputs ----------//

    public static Vector2D output(ParametricFunction2D originalFunc, double tValue) {

        double x = originalFunc.getRectangularComponents().get1().output(tValue);
        double y = originalFunc.getRectangularComponents().get2().output(tValue);

        return (new Vector2D(x, y));
    }

    public static Vector2D derivativeParametric(ParametricFunction2D originalFunc, double tValue) {
        ParametricFunction2D parametricDerivative = ParametricFunction2D.derivativeParametric(originalFunc);

        double x = parametricDerivative.getRectangularComponents().get1().output(tValue);
        double y = parametricDerivative.getRectangularComponents().get2().output(tValue);

        return (new Vector2D(x, y));
    }

    public static double derivative(ParametricFunction2D originalFunc, double tValue) {
        Function derivative = ParametricFunction2D.derivative(originalFunc);

        return derivative.output(tValue);
    }

    public static double getSignedCurvature(ParametricFunction2D originalFunc, double tValue) {

        return ParametricFunction2D.getSignedCurvature(originalFunc).output(tValue);
    }

    public static double getCurvature(ParametricFunction2D originalFunc, double tValue) {

        return Math.abs(getSignedCurvature(originalFunc, tValue));
    }

    public double[] findBounds(double[] domain) {

        Function x = rectangularComponents.get1();
        Function y = rectangularComponents.get2();
//        System.out.println("x Func: " + x);
//        System.out.println("y Func: " + y);

        Function dx_dt = Function.constSimplify(Function.derivative(x));
        Function dy_dt = Function.constSimplify(Function.derivative(y));
//        System.out.println("x Derivative: " + dx_dt);
//        System.out.println("y Derivative: " + dy_dt);

        Function d2x_dt2 = Function.constSimplify(Function.derivative(dx_dt));
        Function d2y_dt2 = Function.constSimplify(Function.derivative(dy_dt));
//        System.out.println("x 2nd Derivative: " + d2x_dt2);
//        System.out.println("y 2nd Derivative: " + d2y_dt2);

        double[] horizontalZeroes = dx_dt.getZeroesNewton(domain);
        double[] verticalZeroes = dy_dt.getZeroesNewton(domain);
//        System.out.println("Horizontal Zeroes: " + Arrays.toString(horizontalZeroes));
//        System.out.println("Vertical Zeroes: " + Arrays.toString(verticalZeroes));

        // find max maximums and min minimums and mins

        double horzMax = x.output(domain[1]);
        double horzMin = x.output(domain[0]);
        double vertMax = y.output(domain[1]);
        double vertMin = y.output(domain[0]);

        if (horizontalZeroes.length > 0) {
            //System.out.println(Arrays.toString(horizontalZeroes));
            double horizontalMax = Arrays.stream(horizontalZeroes).filter(d -> (d2x_dt2.output(d) < 0)).max().orElse(NaN);
            double horizontalMin = Arrays.stream(horizontalZeroes).filter(d -> (d2x_dt2.output(d) > 0)).min().orElse(NaN);
            //System.out.println("Horizontal Zero Max: " + horizontalMax + " Horizontal Zero Min: " + horizontalMin);

            if (!Double.isNaN(horizontalMax) && !Double.isNaN(horizontalMin)) {
                if (horzMax < x.output(horizontalMax)) {

                    horzMax = x.output(horizontalMax);
                }

                if (horzMin > x.output(horizontalMin)) {

                    horzMin = x.output(horizontalMin);
                }
            }
        } else {
            //System.out.println("Horizontal Zero Max: " + horzMax + " Horizontal Zero Min: " + horzMin);
        }

        if (verticalZeroes.length > 0) {
            //System.out.println(Arrays.toString(verticalZeroes));
            double verticalMax = Arrays.stream(verticalZeroes).filter(d -> (d2y_dt2.output(d) < 0)).max().orElse(NaN);
            double verticalMin = Arrays.stream(verticalZeroes).filter(d -> (d2y_dt2.output(d) > 0)).min().orElse(NaN);
            //System.out.println("Vertical Zero Max: " + verticalMax + " Vertical Zero Min: " + verticalMin);

            if (!Double.isNaN(verticalMax) && !Double.isNaN(verticalMin)) {
                if (vertMax < y.output(verticalMax)) {

                    vertMax = y.output(verticalMax);
                }

                if (vertMin > y.output(verticalMin)) {

                    vertMin = y.output(verticalMin);
                }
            }
        } else {
            //System.out.println("Vertical Zero Max: " + vertMax + " Vertical Zero Min: " + vertMin);
        }

        return (new double[] {horzMin, vertMin, horzMax, vertMax});
    }

    public double[] approximateBounds(double[] domain, double resolution) {

        double[] xPoints = new double[(int) ((domain[1] - domain[0]) / resolution)];
        double[] yPoints = new double[(int) ((domain[1] - domain[0]) / resolution)];

        for (double t = domain[0]; t < domain[1]; t += resolution) {

            int pointNum = (int) ((t - domain[0]) / resolution);

            xPoints[pointNum] = rectangularComponents.get1().output(t);
            yPoints[pointNum] = rectangularComponents.get2().output(t);
        }

        double xMin = xPoints[0];
        double xMax = xPoints[0];

        for (int hz = 1; hz < xPoints.length; hz++) {

            if (xPoints[hz] > xMax) {

                xMax = xPoints[hz];
            }

            if (xPoints[hz] < xMin) {

                xMin = xPoints[hz];
            }
        }

        double yMin = yPoints[0];
        double yMax = yPoints[0];

        for (int vz = 1; vz < yPoints.length; vz++) {

            if (yPoints[vz] > yMax) {

                yMax = yPoints[vz];
            }

            if (yPoints[vz] < yMin) {

                yMin = yPoints[vz];
            }
        }

        return (new double[] {xMin, yMin, xMax, yMax});
    }

    public String toString() {

        String vector = "<";
        vector += rectangularComponents.get1() + ", ";
        vector += rectangularComponents.get2() + ">";

        return vector;
    }

    public static void main(String[] args) {

//        Function parabola = new Function("(((((x ^ 5) + (3 * (x ^ 4))) + (3 * (x ^ 2))) + (2 * x)) - 10)", "x", new HashMap<String, Double>());
//        //ParametricFunction2D parabolaParametric = new ParametricFunction2D(parabola, false);
//        Function sinusoid = new Function("(sin(x))", "x", new HashMap<String, Double>());
//
//        System.out.println("polynomail Zeroes: " + Arrays.toString(parabola.getZeroesNewton(new double[] {-4, 4})));
//        System.out.println("sin(x) zeroes: " + Arrays.toString(sinusoid.getZeroesNewton(new double[] {-(Math.PI * 2), (Math.PI * 2)})));

        Function parabola = new Function("(x ^ 2)", "x", new HashMap<String, Double>());
        ParametricFunction2D rotatedPP = new ParametricFunction2D(parabola, false);
        rotatedPP = ParametricFunction2D.rotate(rotatedPP, (Math.PI / 2.0));

        System.out.println(Arrays.toString(rotatedPP.findBounds(new double[] {0, 4})));

    }
}