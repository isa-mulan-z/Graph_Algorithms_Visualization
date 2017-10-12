import java.awt.*;

/**
 *  MyNode represents the status of the edge
 *
 *  @author  Mulangma "Isabella" Zhu
 *  @version CSC 212, April 30 2017
 */
public class EdgeData {
    /** the distance represented by the edge */
    private Double distance;

    /** the color of the edge */
    private Color color = new Color(8,83,109); //default color

    /**
     * The constructor of EdgeData
     * @param distance the distance represented by the edge
     */
    public EdgeData(Double distance) {
        this.distance = distance;
    }


    /**
     * Override the toString method
     * @return the distance represented by the edge
     */
    public String toString(){
        return distance.toString();
    }

    /**
     * The Accessor for distance
     * @return the distance represented by the edge
     */
    public double getDistance(){
        return distance;
    }

    /**
     * The Accessor for color
     * @return the color of the edge
     */
    public Color getColor(){
        return color;
    }

    /**
     * The Manipulator for distance
     * @param distance the new distance
     */
    public void setDistance(double distance){
        this.distance = distance;
    }

    /**
     * The Manipulator for color
     * @param color the new color
     */
    public void setColor(Color color){
        this.color = color;
    }

} // the end of class EdgeData

