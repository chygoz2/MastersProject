import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

public class UndirectedGraph<E,A> implements Graph<E,A>{

	private UnEdge firstEdge;
	private Map<Integer,Vertex<E>> vertexElementMap;
	
	public UndirectedGraph(){
		firstEdge = null;
		vertexElementMap = new HashMap<Integer,Vertex<E>>();
	}
	
	@Override
	public int size() {
		return vertexElementMap.size();
	}

	@Override
	public int degree(Vertex<E> v) {
		return ((UnVertex)v).neighbours.size();
	}

	@Override
	public boolean containsEdge(Vertex<E> v0, Vertex<E> v1) {
		if(degree(v0) > degree(v1)){
			List<Edge<A>> neighbours = ((UnVertex)v0).neighbours;
			for(Edge<A> edge1: neighbours){
				UnEdge edge = (UnEdge)edge1;
				if(edge.getSource().equals(v1) && edge.getDestination().equals(v0) ||
						edge.getSource().equals(v0) && edge.getDestination().equals(v1)){
					return true;
				}
			}
		}
		else{
			List<Edge<A>> neighbours = ((UnVertex)v1).neighbours;
			for(Edge<A> edge1: neighbours){
				UnEdge edge = (UnEdge)edge1;
				if(edge.getSource().equals(v1) && edge.getDestination().equals(v0) ||
						edge.getSource().equals(v0) && edge.getDestination().equals(v1)){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void clear() {
		vertexElementMap.clear();
		firstEdge = null;
	}

	@Override
	public Vertex<E> addVertex(E elem) {
		UnVertex vertex = new UnVertex(elem);		
		vertexElementMap.put((Integer) vertex.getElement(), vertex);
		return vertex;
	}

	@Override
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1) {
		return addEdge(v0,v1,null);
	}

	@Override
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1, A attr) {
		UnEdge edge = new UnEdge(attr,v0,v1,null,null);
		UnEdge curr = firstEdge;
		if(curr==null)
			firstEdge = edge;
		else{
			edge.succ = firstEdge;
			firstEdge.pred = edge;
			firstEdge = edge;
		}
		if(!v0.getElement().equals(v1.getElement())){
			((UnVertex)v0).neighbours.add(edge);
			((UnVertex)v1).neighbours.add(edge);
		}
		
		return edge;
	}

	@Override
	public void removeVertex(Vertex<E> v) {		
		//remove all edges that connect to vertex v	
		Iterator<Edge<A>> it = connectingEdges(v);
		while(it.hasNext()){
			UnEdge e = (UnEdge)it.next();
			removeEdge(e);
		}
		
		//then remove v
		vertexElementMap.remove((int)v.getElement());

	}

	@Override
	public void removeEdge(Edge<A> e) {
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
		UnVertex s = (UnVertex)e.getSource();
		UnVertex d = (UnVertex)e.getDestination();
		s.neighbours.remove(e);
		d.neighbours.remove(e);
	}

	@Override
	public Iterator<Vertex<E>> vertices() {
		return vertexElementMap.values().iterator();
	}

	@Override
	public Iterator<Edge<A>> edges() {
		return new EdgeIterator();
	}

	@Override
	public Iterator<Vertex<E>> neighbours(Vertex<E> v) {
		return new VertexNeighbourIterator(v);
	}

	@Override
	public Iterator<Edge<A>> connectingEdges(Vertex<E> v) {
		return new VertexEdgesIterator(v);
	}
	
	public List<Vertex<E>> depthFirstTraversal(Vertex<E> startV){
		UnVertex start = (UnVertex)startV;
		Stack<Vertex<E>> vertexStack = new Stack<Vertex<E>>();
		List<Vertex<E>> list = new ArrayList<Vertex<E>>();
		
		vertexStack.push(start);
		start.visited = true;
		
		while(!vertexStack.isEmpty()){
			Vertex<E> v = vertexStack.pop();
			list.add(v);
			//get vertex v's neighbours
			Iterator<Vertex<E>> vNeighbours = this.neighbours(v);
			while(vNeighbours.hasNext()){
				UnVertex w = (UnVertex)vNeighbours.next();
				if(!w.visited){
					vertexStack.push(w);
					w.visited = true;
				}
			}
			
		}
		this.resetVisited();
		return list;
	}
	
	public List<Vertex<E>> breadthFirstTraversal(Vertex<E> startV){
		UnVertex start = (UnVertex)startV;
		Queue<Vertex<E>> vertexQueue = new LinkedList<Vertex<E>>();
		List<Vertex<E>> list = new ArrayList<Vertex<E>>();
		
		vertexQueue.add(start);
		start.visited = true;
		
		while(!vertexQueue.isEmpty()){
			Vertex<E> v = vertexQueue.remove();
			list.add(v);
			//get vertex v's neighbours
			Iterator<Vertex<E>> vNeighbours = this.neighbours(v);
			while(vNeighbours.hasNext()){
				UnVertex w = (UnVertex)vNeighbours.next();
				if(!w.visited){
					vertexQueue.add(w);
					w.visited = true;
				}
			}
			
		}
		this.resetVisited();
		return list;
	}
	
	public void resetVisited(){
		for(Integer i: vertexElementMap.keySet()){
			UnVertex v = (UnVertex)vertexElementMap.get(i);
			v.visited = false;
		}
	}
	
	public double[][] getAdjacencyMatrix(){
		int size = vertexElementMap.size();
		double[][] A = new double[size][size];
		int i=0, j=0;
		for(int a: vertexElementMap.keySet()){
			for(int b: vertexElementMap.keySet()){
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
	
	public double[][] getComplementMatrix(){
		int size = vertexElementMap.size();
		double[][] A = new double[size][size];
		int i=0, j=0;
		for(int a: vertexElementMap.keySet()){
			for(int b: vertexElementMap.keySet()){
				if(i==j){
					j++;
					continue;
				}
				if(containsEdge(vertexElementMap.get(a),vertexElementMap.get(b))){
					A[i][j] = 0;
				}else{
					A[i][j] = 1;
				}
				j++;
			}
			i++; j = 0;
		}
		return A;
	}
	
	public void printAdjacencyMatrix(){
		double[][] A = this.getAdjacencyMatrix();
		for(int i=0; i<A.length; i++){
			for(int j=0; j<A[i].length; j++){
				System.out.print((int)A[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public Vertex<E> getVertexWithElement(int i){
		return vertexElementMap.get(i);
	}
	
	private class EdgeIterator implements Iterator<Edge<A>>, Iterable<Edge<A>>{

		private UnEdge pos;
		
		public EdgeIterator(){
			pos = firstEdge;
		}
		
		@Override
		public boolean hasNext() {
			return (pos != null);
		}

		@Override
		public Edge<A> next() {
			if(!hasNext())
				throw new NoSuchElementException();
			
			Edge<A> edge = pos;
			pos = pos.succ;
			return edge;
			
		}

		@Override
		public Iterator<Edge<A>> iterator() {
			return this;
		}
		
	}
	
	private class VertexEdgesIterator implements Iterator<Edge<A>>, Iterable<Edge<A>>{

		private int pos;
		private List<Edge<A>> neighbours;
		
		public VertexEdgesIterator(Vertex<E> v){
			this.neighbours = ((UnVertex)v).neighbours;
			this.pos = neighbours.size()-1;
		}
		
		@Override
		public boolean hasNext() {
			return !(pos == -1);
		}

		@Override
		public Edge<A> next() {
			if(!hasNext())
				throw new NoSuchElementException();
			UnEdge edge = (UnEdge)neighbours.get(pos);
			pos--;
			return edge;
		}

		@Override
		public Iterator<Edge<A>> iterator() {
			return this;
		}
		
	}
	
	public class UnVertex implements Vertex<E>{
		
		private E elem;
		private boolean visited;
		private List<Edge<A>> neighbours;
		
		public UnVertex(E elem){
			this.elem = elem;
			visited = false;
			neighbours = new ArrayList<Edge<A>>();
		}
		@Override
		public E getElement() {

			return elem;
		}

		@Override
		public void setElement(E elem) {

			this.elem = elem;
		}
		
	}
	
	public class UnEdge implements Edge<A>{
		
		private A attribute;
		public UnEdge pred;
		public UnEdge succ;
		private Vertex<E> source;
		private Vertex<E> destination;
		
		public UnEdge(A attr, Vertex<E> source, Vertex<E> destination, UnEdge pred, UnEdge succ){
			this.attribute = attr;
			this.pred = pred;
			this.succ = succ;
			this.source = source;
			this.destination = destination;
		}

		@Override
		public A getAttribute() {
			return attribute;
		}

		@Override
		public void setAttribute(A attr) {
			this.attribute = attr;
		}

		@Override
		public Vertex<E>[] getVertices() {
			Vertex<E> [] v = (UnVertex [])new Object[2];
			v[0] = source;
			v[1] = destination;
			return v;
		}
		
		public Vertex<E> getSource(){
			return source;
		}
		
		public Vertex<E> getDestination(){
			return destination;
		}
		
	}
	
	private class VertexNeighbourIterator implements Iterator<Vertex<E>>, Iterable<Vertex<E>>{
		
		private int pos;
		private List<Edge<A>> neighbours;
		private Vertex<E> v;
		
		public VertexNeighbourIterator(Vertex<E> v){
			this.neighbours = ((UnVertex)v).neighbours;
			this.v = v;
			pos = 0;
		}
		
		@Override
		public boolean hasNext() {
			return !(pos == neighbours.size());
		}

		@Override
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

		@Override
		public Iterator<Vertex<E>> iterator() {
			return this;
		}
		
	}

}
