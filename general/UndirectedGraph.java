package general;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import general.Graph.Edge;
import general.Graph.Vertex;

/**
 * Graph abstract data type class for undirected graphs
 * @author Chigozie Ekwonu
 *
 * @param <E>	the element type of a vertex
 * @param <A>	the attribute type of an edge
 */
public class UndirectedGraph<E,A> implements Graph<E,A>{

	//instance variables
	private UnEdge firstEdge; //pointer to the first edge object in the doubly linked list
	private int edgeCount;	//the number of edges in graph
	private Map<E,Vertex<E>> vertexElementMap; //map containing collection of vertices
	
	/**
	 * constructor to initialize instance variables
	 */
	public UndirectedGraph(){
		firstEdge = null;
		edgeCount = 0;
		vertexElementMap = new HashMap<E,Vertex<E>>();
	}
	
	/**
	 * returns the size of the graph.
	 */
	public int size() {
		return vertexElementMap.size();
	}

	/**
	 * returns the degree of a vertex
	 */
	public int degree(Vertex<E> v) {
		if(v==null)
			throw new NullPointerException();
		return ((UnVertex)v).neighbours.size();
	}

	/**
	 * checks if two vertices are adjacent
	 */
	public boolean containsEdge(Vertex<E> v0, Vertex<E> v1) {
		//checks the adjacency list of the vertex with smaller degree in order to save time
		List<Edge<A>> neighbours;
		if(degree(v0) > degree(v1)){
			neighbours = ((UnVertex)v0).neighbours;
		}
		else{
			neighbours = ((UnVertex)v1).neighbours;
		}
		for(Edge<A> edge: neighbours){
			if(edge.getSource().equals(v1) && edge.getDestination().equals(v0) ||
					edge.getSource().equals(v0) && edge.getDestination().equals(v1)){
				return true;
			}
		}
		
		return false;
	}

	/**
	 * clears the graph
	 */
	public void clear() {
		vertexElementMap.clear();
		firstEdge = null;
	}

	/**
	 * adds vertex with specified element to graph and returns the newly added vertex
	 */
	public Vertex<E> addVertex(E elem) {
		UnVertex vertex = new UnVertex(elem);		
		vertexElementMap.put( vertex.getElement(), vertex);
		return vertex;
	}

	/**
	 * adds an edge between two vertices and returns the newly added edge.
	 * The edge added has no attribute
	 */
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1) {
		return addEdge(v0,v1,null);
	}

	/**
	 * adds an edge with the specified attribute between two vertices and returns the newly added edge.
	 */
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1, A attr) {
		//if any of the vertices in null, throw an exception
		if(v0==null || v1==null)
			throw new NullPointerException();
		UnEdge edge = new UnEdge(attr,v0,v1,null,null); //create an edge object
		//insert edge at appropriate position in doubly linked list
		UnEdge curr = firstEdge;
		if(curr==null)
			firstEdge = edge;
		else{
			edge.succ = firstEdge;
			firstEdge.pred = edge;
			firstEdge = edge;
		}
		//add reference to edge object to the adjacency lists of the respective vertices
		if(!v0.getElement().equals(v1.getElement())){
			((UnVertex)v0).neighbours.add(edge);
			((UnVertex)v1).neighbours.add(edge);
		}
		edgeCount++; //increment edge count
		return edge;
	}

	/**
	 * remove a vertex from graph
	 */
	public void removeVertex(Vertex<E> v) {		
		//if vertex is null, throw an exception
		if(v==null)
			throw new NullPointerException();
		//if vertex is not present in graph, throw an exception
		Set<E> keys = vertexElementMap.keySet();
		if(!keys.contains(v.getElement()))
			throw new NoSuchElementException();
		//remove all edges that connect to vertex v	
		Iterator<Edge<A>> it = connectingEdges(v);
		while(it.hasNext()){
			UnEdge e = (UnEdge)it.next();
			removeEdge(e);
		}
		
		//then remove v
		vertexElementMap.remove(v.getElement());
	}

	/**
	 * remove an edge from a graph
	 */
	public void removeEdge(Edge<A> e) {
		//if edge is null, throw an exception
		if(e==null)
			throw new NullPointerException();
		
		//remove edge from linked list
		UnEdge reEdge = (UnEdge)e;
		UnEdge pre = reEdge.pred;
		UnEdge post = reEdge.succ;
		if(pre==null){
			firstEdge = post;
			if(post != null)
				post.pred = pre;
		}
		else{
			pre.succ = post;
			if(post != null)
				post.pred = pre;
		}
		
		//remove reference of edge from the adjacency list of vertices linked by the edge
		UnVertex s = (UnVertex)e.getSource();
		UnVertex d = (UnVertex)e.getDestination();
		s.neighbours.remove(e);
		d.neighbours.remove(e);
		edgeCount--; //decrement edge count
	}

	/**
	 * return iterator that iterates over graph's vertices
	 */
	public Iterator<Vertex<E>> vertices() {
		return vertexElementMap.values().iterator();
	}

	/**
	 * return iterator that iterates over graph's edges
	 */
	public Iterator<Edge<A>> edges() {
		return new EdgeIterator();
	}

	/**
	 * returns iterator that iterates over the vertices adjacent to a given vertex
	 */
	public Iterator<Vertex<E>> neighbours(Vertex<E> v) {
		if(v==null)
			throw new NullPointerException();
		
		return new VertexNeighbourIterator(v);
	}

	/**
	 * returns iterator for iterating over the edges incident on a given vertex
	 */
	public Iterator<Edge<A>> connectingEdges(Vertex<E> v) {
		if(v==null)
			throw new NullPointerException();
		
		return new VertexEdgesIterator(v);
	}
	
	/**
	 * returns list of vertices that are reachable from a given vertex in depth first order
	 * @param startV	the vertex to start from
	 * @return			the list of vertices
	 */
	public List<Vertex<E>> depthFirstTraversal(Vertex<E> startV){
		//throw an exception if provided vertex is null
		if(startV==null)
			throw new NullPointerException();
		
		UnVertex start = (UnVertex)startV;
		Stack<Vertex<E>> vertexStack = new Stack<Vertex<E>>(); //create vertex stack 
		List<Vertex<E>> list = new ArrayList<Vertex<E>>(); //create list of reachable vertices
		
		vertexStack.push(start); //push start vertex to stack
		start.visited = true; //mark the start vertex as visited
		
		while(!vertexStack.isEmpty()){
			Vertex<E> v = vertexStack.pop(); //get vertex from top of stack
			list.add(v); //visit the vertex popped from stack
			//get vertex v's neighbours and push them to stack if they haven't been visited before
			Iterator<Vertex<E>> vNeighbours = this.neighbours(v); 
			while(vNeighbours.hasNext()){
				UnVertex w = (UnVertex)vNeighbours.next();
				if(!w.visited){
					vertexStack.push(w);
					w.visited = true;
				}
			}
			
		}
		this.resetVisited(list); //remove visited mark from vertices
		return list;
	}
	
	/**
	 * returns list of vertices that are reachable from a given vertex in breadth first order
	 * @param startV	the vertex to start from
	 * @return			the list of vertices
	 */
	public List<Vertex<E>> breadthFirstTraversal(Vertex<E> startV){
		//throw an exception if provided vertex is null
		if(startV==null)
			throw new NullPointerException();
		
		UnVertex start = (UnVertex)startV;
		Queue<Vertex<E>> vertexQueue = new LinkedList<Vertex<E>>(); //create vertex queue
		List<Vertex<E>> list = new ArrayList<Vertex<E>>(); //create list of reachable vertices
		
		vertexQueue.add(start); //add start vertex to back of queue
		start.visited = true; //mark start vertex as visited
		
		while(!vertexQueue.isEmpty()){
			Vertex<E> v = vertexQueue.remove(); //remove a vertex from front of queue
			list.add(v); //visit that vertex removed from queue
			//get vertex v's neighbours and add them to the back of the queue if they haven't been visited before
			Iterator<Vertex<E>> vNeighbours = this.neighbours(v);
			while(vNeighbours.hasNext()){
				UnVertex w = (UnVertex)vNeighbours.next();
				if(!w.visited){
					vertexQueue.add(w);
					w.visited = true;
				}
			}
			
		}
		this.resetVisited(list); //remove visited mark from vertices
		return list;
	}
	
	/**
	 * get the number of edges in graph
	 */
	public int getEdgeCount(){
		return edgeCount;
	}
	
	/**
	 * remove visited mark from vertices
	 */
	public void resetVisited(List<Vertex<E>> list){
		for(Vertex<E> vv: list){
			UnVertex v = (UnVertex)vv;
			v.visited = false;
		}
	}
	
	/**
	 * method that returns the adjacency matrix of the graph
	 * @return	the adjacency matrix
	 */
	public int[][] getAdjacencyMatrix(){
		int size = vertexElementMap.size();
		int[][] A = new int[size][size]; //create a two-dimensional array of size equal to size of graph
		int i=0, j=0;
		/*
		 * for every pair of vertices in the graph, if an edge exists between them, set the corresponding
		 * position of the adjacency matrix to 1.
		 */
		
		for(E a: vertexElementMap.keySet()){
			for(E b: vertexElementMap.keySet()){
				if(containsEdge(vertexElementMap.get(a),vertexElementMap.get(b))){
					A[i][j] = 1;
				}else{
					A[i][j] = 0;
				}
				j++;
			}
			i++; j = 0;
		}
		return A;
	}
	
	/**
	 * method that returns the complement matrix of the graph
	 * @return	the complement matrix
	 */
	public int[][] getComplementMatrix(){
		int size = vertexElementMap.size();
		int[][] C = new int[size][size];
		int i=0, j=0;
		/*
		 * for every pair of vertices in the graph, if an edge does not exist between them, set the corresponding
		 * position of the adjacency matrix to 1.
		 */
		for(E a: vertexElementMap.keySet()){
			for(E b: vertexElementMap.keySet()){
				if(i==j){
					j++;
					continue;
				}
				if(containsEdge(vertexElementMap.get(a),vertexElementMap.get(b))){
					C[i][j] = 0;
				}else{
					C[i][j] = 1;
				}
				j++;
			}
			i++; j = 0;
		}
		return C;
	}
	
	/**
	 * method that gets the vertex with a given element
	 * @param e		the element whose vertex is required
	 * @return		the vertex if present
	 */
	public Vertex<E> getVertexWithElement(E e){
		if(!vertexElementMap.keySet().contains(e))
			throw new NoSuchElementException();
		return vertexElementMap.get(e);
	}
	
	/**
	 * method that returns a clone of the graph
	 */
	public UndirectedGraph<E,A> clone(){
		UndirectedGraph<E,A> clone = new UndirectedGraph<E,A>();
		//add vertices
		Iterator<Graph.Vertex<E>> vit = vertices();
		while(vit.hasNext()){
			clone.addVertex(vit.next().getElement());
		}
		//add edges
		Iterator<Graph.Edge<A>> eit = edges();
		while(eit.hasNext()){
			Graph.Edge<A> e = eit.next();
			clone.addEdge(clone.getVertexWithElement((E) e.getSource().getElement()), 
					clone.getVertexWithElement((E) e.getDestination().getElement()));
		}
		return clone;
	}
	
	/**
	 * inner private iterator class that iterates over the edges of the graph in no
	 * particular order
	 * @author Chigozie Ekwonu
	 *
	 */
	private class EdgeIterator implements Iterator<Edge<A>>, Iterable<Edge<A>>{

		private UnEdge pos; //pointer that keeps track of position in the iterator
		
		/**
		 * constructor to initialize instance variable
		 */
		private EdgeIterator(){
			pos = firstEdge;
		}
		
		/**
		 * method that checks if there is still an unvisited edge object 
		 */
		public boolean hasNext() {
			return (pos != null);
		}

		/**
		 * method to return the next unvisited edge object
		 */
		public Edge<A> next() {
			if(!hasNext())
				throw new NoSuchElementException();
			
			Edge<A> edge = pos;
			pos = pos.succ; //advance the position pointer
			return edge;
			
		}

		/**
		 * returns this iterator object
		 */
		public Iterator<Edge<A>> iterator() {
			return this;
		}

		/**
		 * unimplemented method
		 */
		public void remove(){}
		
	}
	
	/**
	 * inner private iterator class that iterates over the edges incident on a vertex in
	 * no particular order
	 * @author Chigozie Ekwonu
	 *
	 */
	private class VertexEdgesIterator implements Iterator<Edge<A>>, Iterable<Edge<A>>{

		private int pos; //pointer to keep track of position in the iterator
		private List<Edge<A>> neighbours; //adjacency list of the vertex
		
		/**
		 * constructor to initialize instance variables
		 * @param v	the vertex whose connecting edges are required
		 */
		private VertexEdgesIterator(Vertex<E> v){
			this.neighbours = ((UnVertex)v).neighbours;
			this.pos = neighbours.size()-1;
		}
		
		/**
		 * method that checks if there is still an unvisited edge object 
		 */
		public boolean hasNext() {
			return !(pos == -1);
		}

		/**
		 * method to return the next unvisited edge object
		 */
		public Edge<A> next() {
			if(!hasNext())
				throw new NoSuchElementException();
			UnEdge edge = (UnEdge)neighbours.get(pos);
			pos--;
			return edge;
		}

		/**
		 * returns this iterator object
		 */
		public Iterator<Edge<A>> iterator() {
			return this;
		}

		/**
		 * unimplemented method
		 */
		public void remove(){}
		
	}
	
	/**
	 * private inner class to represent a vertex object
	 * @author Chigozie Ekwonu
	 *
	 */
	private class UnVertex implements Vertex<E>{
		
		private E elem; //element of the vertex
		private boolean visited; //used in breadth-first and depth-first traversals to record if a vertex has been visited
		private List<Edge<A>> neighbours; //adjacency list of vertex
		
		/**
		 * constructor to initialize instance variables
		 * @param elem the element that the vertex should contain
		 */
		private UnVertex(E elem){
			this.elem = elem;
			visited = false;
			neighbours = new ArrayList<Edge<A>>();
		}
		
		/**
		 * accessor method that returns the element of a vertex
		 */
		public E getElement() {
			return elem;
		}

		/**
		 * mutator method that modifies the element of a vertex  
		 */
		public void setElement(E elem) {
			this.elem = elem;
		}
	}
	
	/**
	 * inner private class that represents an undirected edge object 
	 * @author Chigozie Ekwonu
	 *
	 */
	private class UnEdge implements Edge<A>{
		
		private A attribute; //attribute of the edge
		private UnEdge pred; //predecessor of the edge in the doubly linked list
		private UnEdge succ; //successor of the edge in the doubly linked list
		private Vertex<E> source; //vertex at one end of the edge
		private Vertex<E> destination; //vertex at the other end of the edge
		
		/**
		 * constructor to initialize instance variables
		 * @param attr			attribute to be assigned the edge
		 * @param source		vertex at one end of the edge
		 * @param destination	vertex at the other end of the edge
		 * @param pred			previous edge object in the doubly linked list
		 * @param succ			next edge object in the doubly linked list
		 */
		private UnEdge(A attr, Vertex<E> source, Vertex<E> destination, UnEdge pred, UnEdge succ){
			this.attribute = attr;
			this.pred = pred;
			this.succ = succ;
			this.source = source;
			this.destination = destination;
		}

		/**
		 * accessor method to get the attribute of an edge
		 */
		public A getAttribute() {
			return attribute;
		}

		/**
		 * mutator method to modify the attribute of an edge
		 */
		public void setAttribute(A attr) {
			this.attribute = attr;
		}
		
		/**
		 * accessor method that gets the source vertex of an edge
		 */
		public Vertex<E> getSource(){
			return source;
		}
		
		/**
		 * accessor method that gets the destination vertex of an edge
		 */
		public Vertex<E> getDestination(){
			return destination;
		}
	}
	
	/**
	 * inner private iterator class that iterates through the neighbour vertices of a vertex 
	 * @author Chigozie Ekwonu
	 *
	 */
	private class VertexNeighbourIterator implements Iterator<Vertex<E>>, Iterable<Vertex<E>>{
		
		private int pos; //position in the iterator
		private List<Edge<A>> neighbours; //adjacency list of the vertex
		private Vertex<E> v; //the vertex whose neighbours are required
		
		/**
		 * constructor to initialize instance variables
		 * @param v		the vertex whose neighbours are required
		 */
		private VertexNeighbourIterator(Vertex<E> v){
			this.neighbours = ((UnVertex)v).neighbours;
			this.v = v;
			pos = 0;
		}
		
		/**
		 * returns whether there is still an unvisited neighbour
		 */
		public boolean hasNext() {
			return !(pos == neighbours.size());
		}

		/**
		 * returns the next unvisited neighbour
		 */
		public Vertex<E> next() {
			if(!hasNext())
				throw new NoSuchElementException();
			UnEdge edge = (UnEdge)neighbours.get(pos);
			pos++;
			if(edge.source.equals(v)){
				return edge.destination;
			}else
				return edge.source;
		}

		/**
		 * returns this iterator
		 */
		public Iterator<Vertex<E>> iterator() {
			return this;
		}

		/**
		 * unimplemented method
		 */
		public void remove(){}
		
	}

}
