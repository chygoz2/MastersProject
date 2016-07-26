package general;
import java.util.*;

public class UndirectedGraph2<E,A> implements Graph<E,A>{

	private UnVertex firstVertex;
	private UnVertex lastVertex;
	private UnEdge firstEdge;
	private int size;
	//private int size,order;
	private Map<Integer,Vertex<E>> vertexElementMap;
	
	public UndirectedGraph2(){
		firstVertex = null;
		lastVertex = null;
		firstEdge = null;
		size = 0;
		vertexElementMap = new HashMap<Integer,Vertex<E>>();
	}
	
	@Override
	public int size() {
		return this.size;
	}
	
//	@Override
//	public int order() {
//		return this.order;
//	}

	@Override
	public int degree(Vertex<E> v) {
//		Iterator<Edge<A>> iterator = connectingEdges(v);
//		int i = 0;
//		while(iterator.hasNext()){
//			i++;
//			iterator.next();
//		}
//		return i;
		return ((UnVertex)v).neighbours.size();
	}

	@Override
	public boolean containsEdge(Vertex<E> v0, Vertex<E> v1) {
		
//		Iterator<Edge<A>> iterator = edges();
//		while(iterator.hasNext()){
//			UnEdge edge = (UnEdge)iterator.next();
//			if((edge.source.equals(v0) && edge.destination.equals(v1)) ||
//					(edge.source.equals(v1) && edge.destination.equals(v0))){
//				return true;
//			}
//		}
		//System.out.println(v1==null);
		return ((UnVertex)v0).neighbours.contains(v1);
//		return false;
	}

	@Override
	public void clear() {
		size = 0;
//		order = 0;
		firstVertex = null;
		firstEdge = null;
	}

	@Override
	public Vertex<E> addVertex(E elem) {
		UnVertex vertex = new UnVertex(elem, null, null);
		UnVertex curr = lastVertex;
		if(curr==null){
			firstVertex = vertex;
			lastVertex = vertex;
		}
		else{
			vertex.pred = lastVertex;
			lastVertex.succ = vertex;
			lastVertex = vertex;
		}
		
		vertexElementMap.put((Integer) vertex.getElement(), vertex);
		size++;
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
			((UnVertex)v0).neighbours.add(v1);
			((UnVertex)v1).neighbours.add(v0);
		}
		
		//size++;
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
		UnVertex remVertex = (UnVertex)v;
		UnVertex before = remVertex.pred;
		UnVertex next = remVertex.succ;
		if(before==null){
			firstVertex = next; //if v is first, remove from first
			
			if(next != null)
				next.pred = before;
			else
				lastVertex = null; //if v is also last, re-point last
		}
		else{ //else remove from its position
			before.succ = next;
			if(next!=null)
				next.pred = before;
			else
				lastVertex = before;
		}
		size--; //decrement the size of the graph

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
		s.neighbours.remove(d);
		d.neighbours.remove(s);
	}

	@Override
	public Iterator<Vertex<E>> vertices() {
		return new VertexIterator();
	}

	@Override
	public Iterator<Edge<A>> edges() {
		return new EdgeIterator();
	}

	@Override
	public Iterator<Vertex<E>> neighbours(Vertex<E> v) {
//		return new VertexNeighbourIterator(v);
		return ((UnVertex)v).neighbours.iterator();
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
		UnVertex curr = firstVertex;
		while(curr != null){
			curr.visited = false;
			curr = curr.succ;
		}
	}
	
	public double[][] getAdjacencyMatrix(){
		double[][] A = new double[size][size];
		UnVertex curr = firstVertex;
		int i=0, j=0;
		UnVertex next = firstVertex;
		while(curr!=null){
			while(next != null){
				if(containsEdge(curr,next)){
					//System.out.println("i= "+i+", j= "+j+", next is "+next.getElement());
					A[i][j] = 1;
				}
				else{
					//System.out.println("i= "+i+", j= "+j+", next is "+next.getElement());
					A[i][j] = 0;
				}
				next = next.succ;
				j++;
			}
			curr = curr.succ;
			i++; j=0;
			next = firstVertex;
		}
		
		
		return A;
	}
	
	public double[][] getComplementMatrix(){
		double[][] A = new double[size][size];
		UnVertex curr = firstVertex;
		int i=0, j=0;
		UnVertex next = firstVertex;
		while(curr!=null){
			while(next != null){
				if(containsEdge(curr,next) || curr.equals(next)){
					//System.out.println("i= "+i+", j= "+j+", next is "+next.getElement());
					A[i][j] = 0;
				}
				else{
					//System.out.println("i= "+i+", j= "+j+", next is "+next.getElement());
					A[i][j] = 1;
				}
				next = next.succ;
				j++;
			}
			curr = curr.succ;
			i++; j=0;
			next = firstVertex;
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
	
//	public void mapVertexToId(){
//		int i = 0;
//		UnVertex curr = firstVertex;
//		while(curr!=null){
//			vertexIdMap.put(i,curr);
//			curr.setId(i);
//			i++;
//			curr = curr.succ;
//		}
//	}
	
//	public void setVertexId(Vertex<E> v, int i){
//		((UnVertex)v).setId(i);
//		vertexIdMap.put(i, v);
//	}
//	
	public Vertex<E> getVertexWithElement(int i){
		return vertexElementMap.get(i);
	}
	
	private class VertexNeighbourIterator implements Iterator<Vertex<E>>, Iterable<Vertex<E>>{
		
		private UnVertex vertex;
		private UnEdge pos;
		
		public VertexNeighbourIterator(Vertex<E> v){
			this.vertex = (UnVertex)v;
			//look for the first edge containing vertex v and move pos there
			UnEdge curr = firstEdge;
			
			while(curr != null){
				if(curr.source.equals(vertex) || curr.destination.equals(vertex)){
					pos = curr;
					break;
				}else{
					curr=curr.succ;
				}
			}
		}
		
		@Override
		public boolean hasNext() {
			UnEdge curr = pos; 
			while(curr != null){
				if(curr.source.equals(vertex) || curr.destination.equals(vertex)){
					pos = curr;
					return true;
				}
				else
					curr = curr.succ;
			}
			return false;
		}

		@Override
		public Vertex<E> next() {
			Vertex<E> current = null;
			if(pos.source.equals(vertex))
				current = pos.destination;
			else
				current = pos.source;
			pos = pos.succ;
			return current;
		}

		@Override
		public Iterator<Vertex<E>> iterator() {
			return this;
		}
		
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
	
	private class VertexIterator implements Iterator<Vertex<E>>, Iterable<Vertex<E>>{

		private UnVertex post;
		
		public VertexIterator(){
			post = firstVertex;
		}
		
		@Override
		public boolean hasNext() {
			return (post != null);
		}

		@Override
		public Vertex<E> next() {
			if(!hasNext())
				throw new NoSuchElementException();
			
			Vertex<E> vertex = post;
			post = post.succ;
			return vertex;
			
		}

		@Override
		public Iterator<Vertex<E>> iterator() {
			return this;
		}
		
	}
	
	private class VertexEdgesIterator implements Iterator<Edge<A>>, Iterable<Edge<A>>{

		private UnVertex vertex;
		private UnEdge pos;
		
		public VertexEdgesIterator(Vertex<E> v){
			this.vertex = (UnVertex)v;
			//look for the first edge containing vertex v and move pos there
			UnEdge curr = firstEdge;
			while(curr != null){
				if(curr.source.equals(v) || curr.destination.equals(v)){
					pos = curr;
					break;
				}
				else
					curr = curr.succ;
			}
		}
		
		@Override
		public boolean hasNext() {
			//the method checks the edges list and looks for the next edge that links vertex
			UnEdge curr = pos; 
			while(curr != null){
				if(curr.source.equals(vertex) || curr.destination.equals(vertex)){
					pos = curr;
					return true;
				}
				else
					curr = curr.succ;
			}
			
			return false;
		}

		@Override
		public Edge<A> next() {
			Edge<A> current = pos;
			pos = pos.succ;
			return current;
		}

		@Override
		public Iterator<Edge<A>> iterator() {

			return this;
		}
		
	}
	
	public class UnVertex implements Vertex<E>{
		
		private E elem;
		private UnVertex pred;
		private UnVertex succ;
		private boolean visited;
		//private int id;
		private List<Vertex<E>> neighbours;
		
		public UnVertex(E elem, UnVertex pred, UnVertex succ){
			this.elem = elem;
			this.pred = pred;
			this.succ = succ;
			visited = false;
			neighbours = new ArrayList<Vertex<E>>();
		}
		@Override
		public E getElement() {

			return elem;
		}

		@Override
		public void setElement(E elem) {

			this.elem = elem;
		}
		
//		public void setId(int id){
//			this.id = id;
//		}
//		
//		public int getId(){
//			return this.id;
//		}
		
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
			Vertex<E> [] v = (UnVertex [])(new Object[2]);
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

}
