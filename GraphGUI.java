import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import javax.swing.*;

/**
 *  Implements a GUI for inputting Graph
 *
 *  @author Mulangma "Isabella" Zhu
 *  @version CSC 212, 28 April 2017
 */
public class GraphGUI {
    /** The graph to be displayed */
    private GraphCanvas canvas;

    /** Label for the instructions */
    private JLabel instr;

    /** Button for add node */
    private JButton addNodeButton;

    /** Button for remove node */
    private JButton rmvNodeButton;

    /** Button for add edge */
    private JButton addEdgeButton;

    /** Button for remove node */
    private JButton rmvEdgeButton;

    /** Button for change text */
    private JButton chgTextButton;

    /** Button for change distance */
    private JButton chgDistButton;

    /** Button for Breath-First Traversal */
    private JButton BFSButton;

    /** Button for Depth-First Traversal */
    private JButton DFSButton;

    /** Button for Shortest-Path */
    private JButton spButton;

    /** Button for Topological Sorting */
    private JButton TPSButton;

    /** Button for Refresh */
    private JButton rfButton;


    /** The input mode */
    private InputMode mode = InputMode.ADD_NODES;

    /** Remembers node where last mousedown event occurred */
    private Graph<NodeData,EdgeData>.Node nodeUnderMouse;

    /**
     *  Schedules a job for the event dispatching thread
     *  creating and showing this application's GUI.
     */
    public static void main(String[] args) {
        final GraphGUI GUI = new GraphGUI();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.createAndShowGUI();
            }
        });
    }

    /** Sets up the GUI window */
    public void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        JFrame frame = new JFrame("Graph GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add components
        createComponents(frame);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /** Puts content in the GUI window */
    public void createComponents(JFrame frame) {
        // graph display
        Container pane = frame.getContentPane();
        pane.setLayout(new FlowLayout());
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        canvas = new GraphCanvas();
        GraphMouseListener gml = new GraphMouseListener();
        canvas.addMouseListener(gml);
        canvas.addMouseMotionListener(gml);
        panel1.add(canvas);
        instr = new JLabel("Click to add new nodes; drag to move.");
        panel1.add(instr,BorderLayout.NORTH);
        pane.add(panel1);

        // build graph buttons
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(6,1));

        addNodeButton = new JButton("Add/Move Nodes");
        panel2.add(addNodeButton);
        addNodeButton.addActionListener(new AddNodeListener());

        rmvNodeButton = new JButton("Remove Nodes");
        panel2.add(rmvNodeButton);
        rmvNodeButton.addActionListener(new RmvNodeListener());

        addEdgeButton = new JButton("Add Edges");
        panel2.add(addEdgeButton);
        addEdgeButton.addActionListener(new AddEdgeListener());

        rmvEdgeButton = new JButton("Remove Edges");
        panel2.add(rmvEdgeButton);
        rmvEdgeButton.addActionListener(new RmvEdgeListener());

        chgTextButton = new JButton("Change Text");
        panel2.add(chgTextButton);
        chgTextButton.addActionListener(new ChgTextListener());

        chgDistButton = new JButton("Change Distance");
        panel2.add(chgDistButton);
        chgDistButton.addActionListener(new ChgDistListener());

        pane.add(panel2);

        // traversal buttons
        JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayout(4,1));
        BFSButton = new JButton("Breath First Traversal");
        panel3.add(BFSButton);
        BFSButton.addActionListener(new BFSListener());

        DFSButton = new JButton("Depth First Traversal");
        panel3.add(DFSButton);
        DFSButton.addActionListener(new DFSListener());

        spButton = new JButton("Shortest Path");
        panel3.add(spButton);
        spButton.addActionListener(new SPListener());

        TPSButton = new JButton("Topological Sort");
        panel3.add(TPSButton);
        TPSButton.addActionListener(new TPSListener());

        rfButton = new JButton("Refresh");
        panel3.add(rfButton);
        rfButton.addActionListener(new RFListener());
        pane.add(panel3);
    }

    /**
     * Returns a node found within the drawing radius of the given location,
     * or null if none
     *
     *  @param x  the x coordinate of the location
     *  @param y  the y coordinate of the location
     *  @return  a point from the canvas if there is one covering this location,
     *  or a null reference if not
     */
    @SuppressWarnings("unchecked")
    public Graph<NodeData,EdgeData>.Node findNearbyNode(int x, int y) {
        Graph.Node nearbyNode = null;
        for (Graph<NodeData,EdgeData>.Node node:canvas.graph.getNode()){
            Point p = node.getData().getPosition();
            if (p.distance(x,y)<=40){
                nearbyNode = node;
            }
        }
        return nearbyNode;
    }

    /** Constants for recording the input mode */
    enum InputMode {
        ADD_NODES, RMV_NODES, ADD_EDGES, RMV_EDGES, CHG_TEXT, CHG_DIST, BFS, DFS, S_PATH, TPS
    }

    /** Listener for Add Node button */
    private class AddNodeListener implements ActionListener {
        /** Event handler for AddPoint button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.ADD_NODES;
            instr.setText("Click to add new nodes or change their location.");
        }
    }

    /** Listener for Remove Node button */
    private class RmvNodeListener implements ActionListener {
        /** Event handler for RmvPoint button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.RMV_NODES;
            instr.setText("Drag on nodes to remove them.");
        }
    }

    /** Listener for Add Edge button */
    private class AddEdgeListener implements ActionListener {
        /** Event handler for AddEdge button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.ADD_EDGES;
            instr.setText("Drag from one node to another to add an edge.");
        }
    }

    /** Listener for Remove Edge button */
    private class RmvEdgeListener implements ActionListener {
        /** Event handler for RmvEdge button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.RMV_EDGES;
            instr.setText("Drag from one node to another to remove an edge.");
        }
    }

    /** Listener for Change Text button */
    private class ChgTextListener implements ActionListener {
        /** Event handler for Chg button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.CHG_TEXT;
            instr.setText("Click one node to change the text on the node.");
        }
    }

    /** Listener for Change Distance button */
    private class ChgDistListener implements ActionListener {
        /** Event handler for Chg button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.CHG_DIST;
            instr.setText("Drag from one node to another to change the distance on the edge.");
        }
    }

    /** Listener for Breadth First Traversal button */
    private class BFSListener implements ActionListener {
        /** Event handler for Breadth First Traversal button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.BFS;
            instr.setText("Click one node to start Breath First Traversal from it.");
        }
    }

    /** Listener for Depth First Traversal button */
    private class DFSListener implements ActionListener {
        /** Event handler for Depth First Traversal button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.DFS;
            instr.setText("Click one node to start Depth First Traversal from it.");
        }
    }

    /** Listener for Shortest Path button */
    private class SPListener implements ActionListener {
        /** Event handler for Shortest Path button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.S_PATH;
            instr.setText("Drag from one node to another to find their shortest path.");
        }
    }

    /** Listener for Topological Sort button */
    private class TPSListener implements ActionListener {
        /** Event handler for Topological Sort button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.TPS;
            String text = "";
            for (NodeData s: canvas.graph.topologicalSort()){
                text = text + s.getText() + " ";
            }
            instr.setText("You should take classes in the following order: "+text);
        }
    }

    /** Listener for Refresh Path button */
    private class RFListener implements ActionListener {
        /** Event handler for Shortest Path button */
        public void actionPerformed(ActionEvent e) {
            canvas.refresh();
            addNodeButton.setEnabled(true);
            rmvNodeButton.setEnabled(true);
            addEdgeButton.setEnabled(true);
            rmvEdgeButton.setEnabled(true);
            chgTextButton.setEnabled(true);
            chgDistButton.setEnabled(true);
            BFSButton.setEnabled(true);
            DFSButton.setEnabled(true);
            spButton.setEnabled(true);
            TPSButton.setEnabled(true);
            instr.setText("Try functions by clicking buttons.");
        }
    }

    /** Mouse listener for GraphCanvas element */
    private class GraphMouseListener extends MouseAdapter
            implements MouseMotionListener {
        /** Responds to click event depending on mode */
        @SuppressWarnings("unchecked")
        public void mouseClicked(MouseEvent e) {
            Graph<NodeData,EdgeData>.Node nearbyNode = findNearbyNode(e.getX(),e.getY());
            boolean work = false;
            switch (mode) {
                case ADD_NODES:
                    if (nearbyNode==null){
                            char c = (char)(canvas.graph.numNodes()%26+65);
                            canvas.graph.addNode(new NodeData(e.getPoint(),Character.toString(c)));
                            canvas.repaint();
                            work = true;
                        }
                    if (!work){
                        Toolkit.getDefaultToolkit().beep();
                    }
                    break;
                case RMV_NODES:
                    if (nearbyNode!=null){
                        canvas.graph.removeNode(nearbyNode);
                        canvas.repaint();
                        work = true;
                    }
                    if (!work){
                        Toolkit.getDefaultToolkit().beep();
                    }
                    break;
                case BFS:
                    if (nearbyNode!=null){
                        (new TraversalThread(canvas.graph.BFS(nearbyNode))).execute();
                        work = true;
                    }
                    if (!work){
                        Toolkit.getDefaultToolkit().beep();
                    }
                    break;
                case DFS:
                    if (nearbyNode!=null) {
                        (new TraversalThread(canvas.graph.DFS(nearbyNode))).execute();
                        work = true;
                    }
                    if (!work) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    break;
                case CHG_TEXT:
                    if (nearbyNode!=null) {
                        while (!work) {
                            try {
                                JFrame frame = new JFrame("Enter a text");
                                String text = JOptionPane.showInputDialog(frame, "Please enter the text on this node.");
                                if (text != null) {
                                    nearbyNode.getData().setText(text);
                                    canvas.repaint();
                                    work = true;
                                } else {
                                    Toolkit.getDefaultToolkit().beep();
                                }
                            } catch (Exception exception){
                                //do nothing
                            }
                        }
                    }
            }
        }

        /**
         * Records point under mousedown event in anticipation of possible drag
         * Record point under mouse, if any
         */
        public void mousePressed(MouseEvent e) {
            nodeUnderMouse = findNearbyNode(e.getX(),e.getY());
        }

        /**
         * Responds to mouseup event
         * Clear record of point under mouse, if any
         */
        @SuppressWarnings("unchecked")
        public void mouseReleased(MouseEvent e) {
            Graph<NodeData,EdgeData>.Node nearbyNode = findNearbyNode(e.getX(),e.getY());
            boolean work = false;
            switch (mode) {
                case ADD_EDGES:
                    if (nodeUnderMouse != null && nearbyNode != null && nearbyNode != nodeUnderMouse) {
                        // the user don't have to enter the distance now, the program will calculate the pixel distance
                        canvas.graph.addEdge((new EdgeData(-1.0)),nodeUnderMouse,nearbyNode);
                        canvas.repaint();
                        work = true;
                    }
                    if (!work) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    break;
                case RMV_EDGES:
                    if (nodeUnderMouse != null) {
                        Graph.Edge edge = nodeUnderMouse.edgeTo(nearbyNode);
                        if (edge != null) {
                            canvas.graph.removeEdge(edge);
                            canvas.repaint();
                            work = true;
                        }
                    }
                    if (!work) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    break;
                case S_PATH:
                    if (nodeUnderMouse != null && nearbyNode != null && nodeUnderMouse != nearbyNode) {
                        LinkedList<Graph<NodeData, EdgeData>.Edge> path = canvas.graph.distances(nodeUnderMouse, nearbyNode);
                        (new TraversalThread(path)).execute();
                        if (path != null && !path.isEmpty()) {
                            instr.setText(" The shortest distance between "
                                    + nodeUnderMouse.getData().getText() + " and " + nearbyNode.getData().getText()
                                    + " is " + canvas.graph.distances(nodeUnderMouse).get(nearbyNode) + ".");
                        }
                        work = true;
                    }
                    if (!work) {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    break;
                case CHG_DIST:
                    if (nodeUnderMouse != null && nearbyNode !=null){
                        Graph<NodeData,EdgeData>.Edge edge = nodeUnderMouse.edgeTo(nearbyNode);
                        if (edge != null) {
                            while (!work) {
                                try {
                                    JFrame frame = new JFrame("Enter a distance");
                                    String distance = JOptionPane.showInputDialog(frame, "Please enter the distance represented by this edge.");
                                    edge.getData().setDistance(Double.valueOf(distance));
                                    canvas.repaint();
                                    work = true;
                                } catch (Exception exception) {
                                    //do nothing
                                }
                            }
                        }
                    }
                    if (!work) {
                        Toolkit.getDefaultToolkit().beep();
                    }
            }
        }

        /** Responds to mouse drag event */
        @SuppressWarnings("unchecked")
        public void mouseDragged(MouseEvent e) {
            // test if the mouse drags on a node, and make sure the node is in the displaying area(r=40)
            if(mode == InputMode.ADD_NODES && nodeUnderMouse != null
                    && e.getX()>=40 && e.getY()>=40
                    && e.getX()<=1460 && e.getY()<=860) {
                nodeUnderMouse.getData().setPosition(e.getPoint());
                canvas.repaint();
            }
        }

        // Empty but necessary to comply with MouseMotionListener interface.
        public void mouseMoved(MouseEvent e) {
            nodeUnderMouse = null;
        }
    }

    /** Worker class for doing traversals */
    private class TraversalThread extends SwingWorker<Boolean, Object> {
        /** The path that needs to paint */
        private LinkedList<Graph<NodeData,EdgeData>.Edge> path;

        /** The Constructor of TraversalThread */
        public TraversalThread(LinkedList<Graph<NodeData,EdgeData>.Edge> path){
            this.path = path;
        }

        @Override
        public Boolean doInBackground() {
            addNodeButton.setEnabled(false);
            rmvNodeButton.setEnabled(false);
            addEdgeButton.setEnabled(false);
            rmvEdgeButton.setEnabled(false);
            chgTextButton.setEnabled(false);
            chgDistButton.setEnabled(false);
            BFSButton.setEnabled(false);
            DFSButton.setEnabled(false);
            spButton.setEnabled(false);
            TPSButton.setEnabled(false);
            rfButton.setEnabled(false);
            return canvas.paintTraversal(path);
        }

        @Override
        protected void done() {
            try {
                if (path.isEmpty() && path != null) {  // test the result of doInBackground()
                    instr.setText("There is no path. Please refresh.");
                }
                rfButton.setEnabled(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
