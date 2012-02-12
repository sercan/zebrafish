/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagemanipulation;

/**
 *
 * @author Bahri
 */
public class RegionProp {
    private int area;
    private int perimeter;
    private double roundness;
    private double roundness1;
    private double radius;

    public double getRadius() {
        return radius;
    }



    public void setRadius(double radius) {
        this.radius = radius;
    }

    public RegionProp() {
    }

    public int getArea() {
        return area;
    }

    public double getRoundness() {
        return roundness;
    }

    public int getPerimeter() {
        return perimeter;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setPerimeter(int perimeter) {
        this.perimeter = perimeter;
    }

    public void setRoundness() {
        this.roundness = (4*Math.PI*area)/(perimeter*perimeter);
        roundness1 =(Math.PI*radius*radius)/(double)area;
    }




}
