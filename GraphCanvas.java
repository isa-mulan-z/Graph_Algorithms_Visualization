import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 *  Implements a graphical canvas that displays a graph.
 *
 *  @author  Mulangma "Isabella" Zhu
 *  @version CSC 212, 28 April 2017
 */
public class GraphCanvas extends JComponent {
    /** The Graph */
    public Graph<NodeData,EdgeData> graph;

    /** Constructor */
    public GraphCanvas() {
        graph = new Graph<NodeData,EdgeData>();
    }

    /**
     *  Paints a blue circle thirty pixels in diameter at each node
     *  and a blue line for each edge.
     *  @param g The graphics object to draw with
     */
    public void paintComponent(Graphics g){
        // paint edges
        for (Graph<NodeData,EdgeData>.Edge edge : graph.getEdge()) {
            g.setColor(edge.getData().getColor());
            Point h = edge.getHead().getData().getPosition();
            Point t = edge.getTail().getData().getPosition();
            // paint the arrow, paint and calculate the distance between two points
            double dist = paintArrowLine(g,(int)h.getX(),(int)h.getY(),(int)t.getX(),(int)t.getY(),20,10,edge.getData().getDistance());
            if (edge.getData().getDistance()==-1) {
                edge.getData().setDistance(dist);
            }
        }
        // paint nodes
        for (Graph<NodeData,EdgeData>.Node node : graph.getNode()) {
            Point p = node.getData().getPosition();
            g.setColor(node.getData().getColor());
            g.fillOval((int) p.getX()-40, (int) p.getY()-40, 80, 80);
            //paint text
            g.setColor(Color.white);
            g.drawString(node.getData().getText(),(int)p.getX()-5,(int)p.getY()+5);
        }
    }

    /**
     * Draw an arrow line between two points, revised from the code of @phibao37
     * http://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
     * @param x1 x-position of first point
     * @param y1 y-position of first point
     * @param x2 x-position of second point
     * @param y2 y-position of second point
     * @param d  the width of the arrow
     * @param h  the height of the arrow
     * @return the distance between two points
     */
    private double paintArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h,Double dist){
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double sin = dy/D, cos = dx/D;

        //change the end point of the line, r = 40
        x2 = (int)(x2 - 40 * cos);
        y2 = (int)(y2 - 40 * sin);
        D = D - 40; // change the length of the line
        double xm = D - d, xn = xm, ym = h, yn = -h, x;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
        // paint the distance
        if (dist == -1.0) {
            // the actual distance(D+40) between two points next to the tail (D=the length of the arrow)
            dist = Math.round((D + 40) * 100) / 100.00;
        }
        g.drawString(Double.toString(dist),(int) (xm - 30 * cos), (int) (ym - 30 * sin));
        return dist;
    }

    /**
     * Paint the traversal path to red
     * @param path the list of edges in the traversal path
     * @return whether there is a traversal to paint or not
     */
    public Boolean paintTraversal(LinkedList<Graph<NodeData,EdgeData>.Edge> path){
        boolean painting;
        if (path.isEmpty()){
            painting = false;
            return painting;
        } else {
            painting = true;
        }
        // set every thing to gray, and set the start point to orange
        for (Graph<NodeData,EdgeData>.Edge edge:graph.getEdge()){
            edge.getData().setColor(Color.lightGray);
        }
        for (Graph<NodeData,EdgeData>.Node node:graph.getNode()){
            node.getData().setColor(Color.lightGray);
        }
        path.get(0).getHead().getData().setColor(Color.ORANGE);
        repaint();

        for (Graph<NodeData,EdgeData>.Edge edge:path){
            try {
            Thread.sleep(500);
            } catch (InterruptedException ignore) {
            }
            // Color the current edge
            edge.getData().setColor(new Color(8,83,109));
            edge.getTail().getData().setColor(Color.ORANGE);
            repaint();
            try {
                Thread.sleep(800);
            } catch (InterruptedException ignore) {
            }
            edge.getTail().getData().setColor(new Color(8,83,109));
            repaint();
        }
        return painting;
    }

    /**
     * Repaint every thing to the default color
     */
    public void refresh(){
        for (Graph<NodeData,EdgeData>.Edge edge:graph.getEdge()){
            edge.getData().setColor(new Color(8,83,109));
        }
        for (Graph<NodeData,EdgeData>.Node node:graph.getNode()){
            node.getData().setColor(new Color(8,83,109));
        }
        repaint();
    }

    /**
     *  The component will look bad if it is sized smaller than this
     *
     *  @returns The minimum dimension
     */
    public Dimension getMinimumSize() {
        return new Dimension(1500,9000);
    }

    /**
     *  The component will look best at this size
     *
     *  @returns The preferred dimension
     */
    public Dimension getPreferredSize() {
        return new Dimension(1500,900);
    }
}
