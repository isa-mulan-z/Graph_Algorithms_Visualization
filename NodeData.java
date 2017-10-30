 import java.awt.*;

/**
 *  NodeData represents the status of the node
 *
 *  @author  Mulangma "Isabella" Zhu
 *  @version CSC 212, April 30 2017
 */
public class NodeData {
    /** the position of the node */
    private Point position;

    /** the data of the node */
    private String text;

    /** the default color of the node */
    private Color color = new Color(8,83,109);

    /**
     * The constructor of NodeData
     * @param position the position of the node
     * @param text the text on the node
     * @param color the color of the node
     */
    public NodeData(Point position, String text) {
        this.position = position;
        this.text = text;
    }


    /**
     * The Accessor for position
     * @return the position of the node
     */
    public Point getPosition() {
        return position;
    }

    /**
     * The Accessor for data
     * @return the data of the node
     */
    public String getText() {
        return text;
    }

    /**
     * The Accessor for color
     * @return the color of the node
     */
    public Color getColor() {
        return color;
    }

    /**
     * The Manipulator for position
     * @param position the new posiiton
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * The Manipulator for text
     * @param text the new text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * The Manipulator for color
     * @param color the new color
     */
    public void setColor(Color color) {
        this.color = color;
    }
 
    /**
     * Override toString
     * @return the text on this node
     */
    @Override
    public String toString() {
        return getText();
    }

} // the end of class NodeData
