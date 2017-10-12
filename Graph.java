import sun.awt.image.ImageWatched;

import java.util.*;

/**
 *  A directed Graph Class
 *
 *  @author Mulangma "Isabella" Zhu
 *  @version CSC 212, 22 April 2017
 */
public class Graph<V,E> extends Object {

    /** a list of edges in a graph */
    private LinkedList<Edge> edges;

    /** a list of nodes in a graph */
    private LinkedList<Node> nodes;

    /** the constructor */
    public Graph() {
        edges = new LinkedList<Edge>();
        nodes = new LinkedList<Node>();
    }

    /**
     * Breath-first traversal of graph
     * @param start the start node
     * @return a LinkedList of edges
     */
    public LinkedList<Edge> BFS(Node start) {
        LinkedList<Edge> path = new LinkedList<Edge>();
        Queue<Node> queue = new LinkedList<Node>();
        HashSet<Node> visited = new HashSet<Node>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            Node current = queue.remove();
            for (Edge edge:current.getEdgeOut()) {
                Node next = edge.getTail();
                if (!visited.contains(next)) {
                    visited.add(next);
                    path.add(edge);
                    queue.add(next);
                }
            }
        }
        return path;
    }

    /**
     * Checks the consistency of the graph
     */
    public boolean check(){
        boolean checkEdge = true;
        boolean checkNode = true;

        for (Edge edge:edges){
            Node head = edge.getHead();
            Node tail = edge.getTail();
            if (!head.getEdgeOut().contains(edge)
                || !tail.getEdgeIn().contains(edge)
                || !nodes.contains(head)
                || !nodes.contains(tail)){
                checkEdge = false;
                System.out.println("Wrong Edge connection1: "+head.getData()+" --> "+tail.getData());
            }
        }

        for (Node node:nodes){
            for (Edge edge:node.getEdgeOut()){
                Node head = edge.getHead();
                Node tail = edge.getTail();
                if ((head!=node && tail!=node)
                    ||(head==node && tail==node)
                    ||!edges.contains(edge)){
                    checkNode = false;
                    System.out.println("Wrong Node connection2: "+head.getData()+" --> "+tail.getData());
                }
            }
            for (Edge edge:node.getEdgeIn()){
                Node head = edge.getHead();
                Node tail = edge.getTail();
                if ((head!=node && tail!=node)
                        ||(head==node && tail==node)
                        ||!edges.contains(edge)){
                    checkNode = false;
                    System.out.println("Wrong Node connection2: "+head.getData()+" --> "+tail.getData());
                }
            }
        }
        return checkEdge && checkNode;
    }

    /**
     * Depth-first traversal of graph -- public interface
     * @param start the start node
     * @return an ArrayList of passing edges
     */
    public LinkedList<Edge> DFS(Node start){
        LinkedList<Edge> path = new LinkedList<Edge>();
        HashSet<Node> visited = new HashSet<Node>();
        return DFS(start,path,visited);
    }

    /**
     * The worker method of Depth-first traversal
     * @param current the current node
     * @param path an ArrayList of passing edges
     * @return
     */
    private LinkedList<Edge> DFS(Node current, LinkedList<Edge> path, HashSet<Node> visited){
        visited.add(current);
        try {
            for (Edge edge : current.getEdgeOut()) {
                Node next = edge.getTail();
                if (!visited.contains(next)) {
                    path.add(edge);
                    DFS(next, path, visited);
                }
            }
        }
        catch (NullPointerException e){}
        return path;
    }

    /**
     * Find the closest node in unvisited nodes
     * @param unvisited a set of unvisited nodes
     * @param dist an array of distances from the start to other nodes
     * @return the closest node
     */
    public Node closest(HashSet<Node> unvisited, HashMap<Node,Double> dist){
        double min = Double.MAX_VALUE;
        Node closestNode = null;
        for (Node node:unvisited){
            if (dist.get(node)<=min){
                min = dist.get(node);
                closestNode = node;
            }
        }
        return closestNode;
    }

    /**
     * Dijkstra's shortest-path algorithm
     * to compute distances to nodes
     * @param start the start node
     * @return a HashMap of distances
     */
    public HashMap<Node,Double> distances(Node start) {
        // Initial set up
        HashMap<Node,Double> dist = new HashMap<Node,Double>(numNodes());
        for (Node node:nodes){
            dist.put(node,Double.MAX_VALUE);
        }
        dist.put(start,0.0);

        HashSet<Node> unvisited = new HashSet<Node>();
        unvisited.addAll(getNode());

        while(!unvisited.isEmpty()){
            Node node = closest(unvisited,dist);
            try {
                for (Edge edge : node.getEdgeOut()) {
                    Node neighbor = edge.getTail();
                    double temp = dist.get(node) + Double.valueOf(edge.getData().toString());
                    if (temp < dist.get(neighbor)) {
                        dist.put(neighbor, temp);
                    }

                }
            }
            catch (NullPointerException e){}
            unvisited.remove(node);
        }
        return dist;
    }

    /**
     * Dijkstra's shortest-path algorithm for GUI purpose
     * to compute distance between two nodes
     * @param start the start node
     * @param end the end node
     * @return an ArrayList of passing edges
     */
    public LinkedList<Edge> distances(Node start, Node end) {
        // Initial set up
        HashMap<Node,Double> dist = new HashMap<Node,Double>(numNodes());
        HashMap<Node,Edge> predecessors = new HashMap<Node, Edge>(numNodes());
        for (Node node:nodes){
            dist.put(node,Double.MAX_VALUE);
        }
        dist.put(start,0.0);
        predecessors.put(start,null);

        // Calculate the distances
        HashSet<Node> unvisited = new HashSet<Node>();
        unvisited.addAll(getNode());

        while(!unvisited.isEmpty()){
            Node node = closest(unvisited,dist);
            try {
                for (Edge edge : node.getEdgeOut()) {
                    Node neighbor = edge.getTail();
                    double temp = dist.get(node) + Double.valueOf(edge.getData().toString());
                    if (temp < dist.get(neighbor)) {
                        dist.put(neighbor, temp);
                        predecessors.put(neighbor, edge);
                    }
                }
            }
            catch (NullPointerException e){}
            unvisited.remove(node);
        }

        // Get the path between two nodes, return null if no path exists
        LinkedList<Edge> path = new LinkedList<Edge>();
        Node current = end;
        Edge edge = predecessors.get(current);
        while (!current.equals(start) && edge!=null) {
            path.addFirst(edge);
            current = edge.getHead();
            edge = predecessors.get(current);
        }
        return path;
    }

    /**
     * Topological Sorting Algorithm,
     * which helps to determine which class should take first based on the dependency graph
     */
    public LinkedList<V> topologicalSort() {
        Stack<Node> stack = new Stack<Node>();
        HashSet<Node> visited = new HashSet<>();
        // call the recursive helper
        for (Node node:getNode()){
            if (!visited.contains(node)){
                topoSorting(node, visited, stack);
            }
        }
        // output the topological sorting order
        LinkedList<V> order = new LinkedList<V>();
        while (!stack.empty()) {
            order.addFirst(stack.pop().getData());
        }
        return order;
    }

    public void topoSorting(Node current, HashSet<Node> visited, Stack<Node> stack) {
        visited.add(current);
        try {
            for (Node next: current.getNeighborOut()) {
                if (!visited.contains(next)) {
                    topoSorting(next, visited, stack);
                }
            }
            stack.push(current);
        }
        catch (NullPointerException e){}
    }

    /**
     * Returns nodes that are heads of a list of edges
     * @param edges a list of edges
     * @return a HashSet of heads
     */
    public HashSet<Node> heads(HashSet<Edge> edges){
        HashSet<Node> headNodes = new HashSet<Node>();
        for (Edge edge:edges) {
            headNodes.add(edge.getHead());
        }
        return headNodes;
    }

    /**
     * Returns nodes that are tails of a list of edges
     * @param edges a list of edges
     * @return a HashSet of tails
     */
    public HashSet<Node> tails(HashSet<Edge> edges){
        HashSet<Node> tailNodes = new HashSet<Node>();
        for (Edge edge:edges) {
            tailNodes.add(edge.getTail());
        }
        return tailNodes;
    }

    /**
     * Accessor for nodes
     * @param i an index
     * @return the node at the index
     */
    public Node getNode(int i){
        return nodes.get(i);
    }

    /**
     * Accessor for all nodes
     * @return all nodes in the graph
     */
    public LinkedList<Node> getNode(){
        return nodes;
    }

    /**
     * Accessor for edges
     * @param i an index
     * @return the node at the index
     */
    public Edge getEdge(int i){
        return edges.get(i);
    }

    /**
     * Accessor for all edges
     * @return the node at the index
     */
    public LinkedList<Edge> getEdge(){
        return edges;
    }

    /**
     * Accessor for specific edge
     * @param head the head of that edge
     * @param tail the tail of that edge
     * @return the edge that connects the head and tail
     */
    public Edge getEdgeRef(Node head, Node tail){
        return head.edgeTo(tail);
    }

    /**
     * Accessor for number of nodes
     * @return the number of nodes in the graph
     */
    public int numNodes(){
        return nodes.size();
    }

    /**
     * Accessor for number of edges
     * @return the number of edges in the graph
     */
    public int numEdges(){
        return edges.size();
    }

    /**
     * Adds an node
     * @param data the data on that node
     * @return the new Node
     */
    public Node addNode(V data){
        Node newNode = new Node(data);
        nodes.add(newNode);
        return newNode;
    }

    /**
     * Adds an edge
     * @param data the data on that edge
     * @param head the head of that edge
     * @param tail the tail of that edge
     * @return
     */
    public Edge addEdge(E data, Node head, Node tail){
        if (head.edgeTo(tail)==null) {
            Edge newEdge = new Edge(data, head, tail);
            head.addEdgeOut(newEdge);
            tail.addEdgeIn(newEdge);
            edges.add(newEdge);
            return newEdge;
        } else {
            return null;
        }
    }

    /**
     * Returns nodes not on a given list
     * @param group a given HashSet of nodes
     * @return a HashSets of nodes that are no in the given set
     */
    public HashSet<Node> otherNodes(HashSet<Node> group){
        HashSet<Node> others = new HashSet<Node>(nodes);
        others.removeAll(group);
        return others;
    }

    /**
     * Prints a representation of the graph
     */
    public void print(){
        // print every node and its connecting nodes
        for(Node node:nodes){
            System.out.println(node.getData()+":");
            for (Edge edge:node.getEdgeOut()){
                System.out.println(edge.getHead().getData()+"--"+edge.getData()+"-->"+edge.getTail().getData());
            }
            for (Edge edge:node.getEdgeIn()){
                System.out.println(edge.getHead().getData()+"--"+edge.getData()+"-->"+edge.getTail().getData());
            }
            System.out.println();
        }

        // print every edge
        System.out.println("AllEdges:");
        for(Edge edge:edges){
            System.out.println(edge.getHead().getData()+"--"+edge.getData()+"-->"+edge.getTail().getData());
        }
    }

    /**
     * Removes an edge
     * @param edge the edge that needs to be removed
     */
    public void removeEdge(Edge edge){
        edge.getHead().removeEdgeRef(edge);
        edge.getTail().removeEdgeRef(edge);
        edges.remove(edge);
    }

    /**
     * Removes an edge
     * @param head the head of that edge
     * @param tail the tail of that edge
     */
    public void removeEdge(Node head, Node tail){
        Edge removedEdge = head.edgeTo(tail);
        removeEdge(removedEdge);
    }

    /**
     * Removes a node
     * @param node the node that needs to be removed
     */
    public void removeNode(Node node){
        while(!(node.getEdgeOut().isEmpty())) {
            Edge edge = node.getEdgeOut().get(0);
            removeEdge(edge);
        }
        while(!(node.getEdgeIn().isEmpty())) {
            Edge edge = node.getEdgeIn().get(0);
            removeEdge(edge);
        }
        nodes.remove(node);
    }


    // Nested classes:
    /**
     *  Nested class for Edge
     */
    public class Edge {
        /** The data of a edge */
        private E data;

        /** The head of a edge */
        private Node head;

        /** The tail of a edge */
        private Node tail;

        /**
         * The constructor of a edge
         * @param data the data of the edge
         * @param head the head of the edge
         * @param tail the tail of the edge
         */
        public Edge(E data, Node head, Node tail){
            this.data = data;
            this.head = head;
            this.tail = tail;
        }

        /**
         * Test if two edges connect the same endpoints
         * regardless of the data they carry
         * @param o an edge
         * @return if two edges are equal or not
         */
        public boolean equals(Object o){
            boolean result = false;
            if (o.getClass() == getClass()){
                @SuppressWarnings("unchecked")
                Edge edge = (Edge)o;
                if (edge.getHead()==head && edge.getTail()==tail){
                    result = true;
                }
            }
            return result;
        }

        /**
         * Accessor for data
         * @return the data of a edge
         */
        public E getData(){
            return data;
        }

        /**
         * Accessor for endpoint #1
         * @return the head of a edge
         */
        public Node getHead(){
            return head;
        }

        /**
         * Accessor for endpoint #2
         * @return the tail of a edge
         */
        public Node getTail(){
            return tail;
        }

        /**
         * Redefined hashcode to match redefined equals
         * Revised from the code of @Jon Skeet
         * http://stackoverflow.com/questions/1646807/quick-and-simple-hash-code-combinations
         * @return an int which is the hashCode of the edge
         */
        public int hashCode(){
            int hash = 17;
            hash = hash * 31 + head.hashCode();
            hash = hash * 31 + tail.hashCode();
            return hash;
        }

        /**
         * Accessor for opposite node
         * @return The opposite node
         */
        public Node oppositeTo(Node node){
            // not sure
            if (node==head){
                return tail;
            } else if (node==tail){
                return head;
            } else {
                return null;
            }
        }

        /**
         * Set the data of the edge
         * @param data the data to be set
         */
        public void setData(E data){
            this.data = data;
        }


    }// the end of nested class Edge


    /**
     * Nested class for Node
     */
    public class Node {
        /** The data of a node */
        private V data;

        /** The list of edge pointing out from the node */
        private LinkedList<Edge> edgeRefOut;

        /** The list of edge pointing into the node */
        private LinkedList<Edge> edgeRefIn;

        /** Boolean used in Topological Sorting*/
        public boolean temp_visited;

        /**
         * The constructor of a node
         * @param data the data of the node
         */
        public Node(V data){
            this.data = data;
            edgeRefOut = new LinkedList<Edge>();
            edgeRefIn = new LinkedList<Edge>();
            temp_visited = false;
        }

        /**
         * Adds an edge to the edge list
         */
        private void addEdgeOut(Edge edge){
            edgeRefOut.add(edge);
        }

        /**
         * Adds an edge to the edge list
         */
        private void addEdgeIn(Edge edge){
            edgeRefIn.add(edge);
        }

        /**
         * Returns the edge to a specified node, or null if there is none
         * @param neighbor the possible neighbor node that connects with the node
         * @return the edge to the neighbor node
         */
        public Edge edgeTo(Node neighbor){
            Edge result = null;
            Edge possibleEdge = new Edge(null,this,neighbor);
            for (Edge edge:edgeRefOut){
                if (edge.equals(possibleEdge)){
                    result = edge;
                    break;
                }
            }
            if (result==null) {
                for (Edge edge : edgeRefIn) {
                    if (edge.equals(possibleEdge)) {
                        result = edge;
                        break;
                    }
                }
            }
            return result;
        }

        /**
         * Accessor for data
         * @return the data of the node
         */
        public V getData(){
            return data;
        }


        /**
         * Accessor for edgeRefOut
         * @return all the edges pointing out the node
         */
        public LinkedList<Edge> getEdgeOut() {
            return edgeRefOut;
        }

        /**
         * Accessor for edgeRefIn
         * @return all the edges pointing to the node
         */
        public LinkedList<Edge> getEdgeIn() {
            return edgeRefIn;
        }

        /**
         * Returns a set of neighbors out
         * @return the list of neighbors out
         */
        public HashSet<Node> getNeighborOut(){
            HashSet<Node> neighborOut = new HashSet<Node>();
            for (Edge edge:edgeRefOut){
                neighborOut.add(edge.getTail());
            }
            return neighborOut;
        }

        /**
         * Returns a set of neighbors in
         * @return the list of neighbors in
         */
        public HashSet<Node> getNeighborIn(){
            HashSet<Node> neighborIn = new HashSet<Node>();
            for (Edge edge:edgeRefIn){
                neighborIn.add(edge.getHead());
            }
            return neighborIn;
        }



        /**
         * Returns true if there is an edge to the node in question
         * @return if the node in question is a neighbor
         */
        public boolean isNeighbor(Node node){
            return node.getNeighborOut().contains(node)||node.getNeighborIn().contains(node);
        }

        /**
         * Remove an edge from the edgeRef
         * @param edge the edge to be removed
         */
        private void removeEdgeRef(Edge edge){
            if (!edgeRefOut.remove(edge)){
                edgeRefIn.remove(edge);
            }
        }

        /**
         * Set the data of the node
         * @param data the data to be set
         */
        public void setData(V data){ this.data = data; }

    } // the end of nested class node
}// the end of the class Graph
