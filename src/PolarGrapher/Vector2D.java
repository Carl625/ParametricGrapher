package PolarGrapher;

import java.util.Arrays;

public class Vector2D {

    private String name = "Vector";
    private double R;
    private double theta;
    private double[] components;

    Vector2D(double comp1, double comp2) {

        components = new double[] {comp1, comp2};
        genAngles();
    }

    Vector2D(float newTheta, float newMag) {

        R = newMag;
        theta = newTheta;
        genComp();
    }

    Vector2D(Vector2D prevV) {

        components = prevV.getComponents();
        R = prevV.getMag();
        theta = prevV.getTheta();
    }

    //---------- Vector Refresh ----------//

    public void genComp() {
        components = new double[2];

        components[0] = R * Math.cos(theta);
        components[1] = R * Math.sin(theta);
    }

    public void genMag() {

        R = Math.sqrt(Math.pow(components[0], 2) + Math.pow(components[1], 2));
    }

    public void genAngles() {
        genMag();

        theta = Math.atan(components[1] / components[0]);

        if (components[0] < 0) {

            if (components[1] > 0) {

                theta += Math.PI;
            } else if (components[1] < 0) {

                theta -= Math.PI;
            }
        }
    }

    public void setName(String newName) {

        name = newName;
    }

    //---------- Vector Properties ----------//

    public double getMag() {

        return R;
    }

    public double getTheta() {

        return theta;
    }

    public double[] getComponents() {

        return Arrays.copyOf(components, components.length);
    }

    public String getName() {

        return name;
    }

    //---------- Vector Operations ----------//

    public void add(Vector2D term_two) {

        double[] two_comp = term_two.getComponents();

        components[0] = two_comp[0];
        components[1] = two_comp[1];
    }

    public void sub(Vector2D term_two) {

        add(invert(term_two));
    }

    public Vector2D invert(Vector2D term_two) {

        Vector2D iTwo = new Vector2D(term_two);
        iTwo.scale(-1);

        return iTwo;
    }

    public void zero() {

        this.scale(0);
    }

    public void scale(double scalar) {

        components[0] *= scalar;
        components[1] *= scalar;
    }

    public double dot(Vector2D term_two) {

        double[] two_comp = term_two.getComponents();

        return ((components[0] * two_comp[0]) + (components[1] * two_comp[1]));
    }

    public double angleBetween(Vector2D v2) {

        double dotProduct = this.dot(v2);
        double magnitudeProducts = R * v2.getMag();

        return Math.acos(dotProduct / magnitudeProducts);
    }

    public static double standardPosAngle(Vector2D v) {

        Vector2D i = new Vector2D(1d, 0d);
        Vector2D j = new Vector2D(0d, 1d);

        double iAngle = v.angleBetween(i);

        if (v.angleBetween(j) > (Math.PI / 2.0)) {

            iAngle = (Math.PI * 2.0) - iAngle;
        }

        return iAngle;
    }

    /**
     * reverses specified component of the vector
     * @param dimension
     */
    public void flipDimension(int dimension) {

        components[dimension] *= -1;
        genAngles();
    }

    /**
     * This function rotates the vector x radians counterclockwise
     * @param radians
     */
    public void rotate(double radians) {

        theta += radians % (2 * Math.PI);
        theta = theta % (2 * Math.PI);
        genComp();
    }

    public String toString() {

        String vector = name + ": <";
        vector += components[0] + ", ";
        vector += components[1] + ">";

        return vector;
    }

    public static void main(String[] args) {

        for (int angle = 0; angle < 360; angle++) {

            float theta = (float) Math.toRadians(angle);
            Vector2D v = new Vector2D(theta, 1);
            System.out.println("Angle: " + angle + " genAngle: " + Math.toDegrees(v.getTheta()) + " standardPosAngle: " + Math.toDegrees(standardPosAngle(v)));
        }
    }
}
