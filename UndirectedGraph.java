import java.util.*;

public class UndirectedGraph<E,A> implements Graph<E,A>{

	private UnVertex<E> firstVertex;
	private UnEdge<A> firstEdge;
	private int size;
	private Map<Integer,Vertex<E>> vertexIdMap;
	
	public UndirectedGraph(){
		firstVertex = null;
		firstEdge = null;
		size = 0;
		vertexIdMap = new HashMap<Integer,Vertex<E>>();
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.size;
	}

	@Override
	public int degree(Vertex<E> v) {
		// TODO Auto-generated method stub
		Iterator<Edge> iterator = connectingEdges(v);
		int i = 0;
		while(iterator.hasNext()){
			i++;
			iterator.next();
		}
		return i;
	}

	@Override
	public boolean containsEdge(Vertex<E> v0, Vertex<E> v1) {
		// TODO Auto-generated method stub
		Iterator<Edge> iterator = edges();
		while(iterator.hasNext()){
			UnEdge<A> edge = (UnEdge<A>)iterator.next();
			if((edge.source.equals(v0) && edge.destination.equals(v1)) ||
					(edge.source.equals(v1) && edge.destination.equals(v0))){
				return true;
			}
		}
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		size = 0;
		firstVertex = null;
		firstEdge = null;
	}

	@Override
	public Vertex<E> addVertex(E elem) {
		// TODO Auto-generated method stub
		UnVertex<E> vertex = new UnVertex<E>(elem, null, null);
		UnVertex<E> curr = firstVertex;
		if(curr==null)
			firstVertex = vertex;
		else{
			vertex.succ = firstVertex;
			firstVertex.pred = vertex;
			firstVertex = vertex;
		}
		size++;
		return vertex;
	}

	@Override
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1) {
		// TODO Auto-generated method stub
		UnEdge<A> edge = new UnEdge<A>(null,v0,v1,null,null);
		UnEdge<A> curr = firstEdge;
		if(curr==null)
			firstEdge = edge;
		else{
			edge.succ = firstEdge;
			firstEdge.pred = edge;
			firstEdge = edge;
		}
		return edge;
	}

	@Override
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1, A attr) {
		// TODO Auto-generated method stub
		UnEdge<A> edge = new UnEdge<A>(attr,v0,v1,null,null);
		UnEdge<A> curr = firstEdge;
		if(curr==null)
			firstEdge = edge;
		else{
			edge.succ = firstEdge;
			firstEdge.pred = edge;
			firstEdge = edge;
		}
		return edge;
	}

	@Override
	public void removeVertex(Vertex<E> v) {
		// TODO Auto-generated method stub
		
		//remove all edges that connect to vertex v	
		Iterator<Edge> it = connectingEdges(v);
		while(it.hasNext()){
			UnEdge<A> e = (UnEdge<A>)it.next();
			removeEdge(e);
		}
		
		//then remove v
		UnVertex<E> remVertex = (UnVertex<E>)v;
		UnVertex<E> before = remVertex.pred;
		UnVertex<E> next = remVertex.succ;
		if(before==null)
			firstVertex = next; //if v is first, remove from first
		else{ //else remove from its position
			before.succ = next;
			if(next!=null)
				next.pred = before;
		}
		size--; //decrement the size of the graph
		mapVertexToId();

	}

	@Override
	public void removeEdge(Edge<A> e) {
		// TODO Auto-generated method stub
		UnEdge<A> reEdge = (UnEdge<A>)e;
		UnEdge<A> pre = reEdge.pred;
		UnEdge<A> post = reEdge.succ;
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
	}

	@Override
	public Iterator<Vertex> vertices() {
		// TODO Auto-generated method stub
		return new VertexIterator();
	}

	@Override
	public Iterator<Edge> edges() {
		// TODO Auto-generated method stub
		return new EdgeIterator();
	}

	@Override
	public Iterator<Vertex> neighbours(Vertex<E> v) {
		// TODO Auto-generated method stub
		return new VertexNeighbourIterator(v);
	}

	@Override
	public Iterator<Edge> connectingEdges(Vertex<E> v) {
		// TODO Auto-generated method stub
		return new VertexEdgesIterator(v);
	}
	
	public List<Vertex<E>> depthFirstTraversal(Vertex<E> startV){
		UnVertex<E> start = (UnVertex<E>)startV;
		Stack<Vertex<E>> vertexStack = new Stack<Vertex<E>>();
		List<Vertex<E>> list = new ArrayList<Vertex<E>>();
		
		vertexStack.push(start);
		start.visited = true;
		
		while(!vertexStack.isEmpty()){
			Vertex<E> v = vertexStack.pop();
			list.add(v);
			//get vertex v's neighbours
			Iterator<Vertex> vNeighbours = this.neighbours(v);
			while(vNeighbours.hasNext()){
				UnVertex<E> w = (UnVertex<E>)vNeighbours.next();
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
		UnVertex<E> start = (UnVertex<E>)startV;
		Queue<Vertex<E>> vertexQueue = new LinkedList<Vertex<E>>();
		List<Vertex<E>> list = new ArrayList<Vertex<E>>();
		
		vertexQueue.add(start);
		start.visited = true;
		
		while(!vertexQueue.isEmpty()){
			Vertex<E> v = vertexQueue.remove();
			list.add(v);
			//get vertex v's neighbours
			Iterator<Vertex> vNeighbours = this.neighbours(v);
			while(vNeighbours.hasNext()){
				UnVertex<E> w = (UnVertex<E>)vNeighbours.next();
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
		UnVertex<E> curr = firstVertex;
		while(curr != null){
			curr.visited = false;
			curr = curr.succ;
		}
	}
	
	public double[][] getAdjacencyMatrix(){
		double[][] A = new double[size][size];
		UnVertex<E> curr = firstVertex;
		int i=0, j=0;
		UnVertex<E> next = firstVertex;
		while(curr!=null){
			while(next != null){
				if(containsEdge(curr,next))
					A[i][j] = 1;
				else
					A[i][j] = 0;
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
				System.out.print(A[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public void mapVertexToId(){
		int i = 0;
		UnVertex<E> curr = firstVertex;
		while(curr!=null){
			vertexIdMap.put(i,curr);
			curr.setId(i);
			i++;
			curr = curr.succ;
		}
	}
	
	public void setVertexId(Vertex<E> v, int i){
		((UnVertex<E>)v).setId(i);
		vertexIdMap.put(i, v);
	}
	
	public Vertex getVertexWithId(int i){
		return vertexIdMap.get(i);
	}
	
	private class VertexNeighbourIterator implements Iterator<Vertex>{
		
		private UnVertex<E> vertex;
		private UnEdge<A> pos;
		
		public VertexNeighbourIterator(Vertex<E> v){
			this.vertex = (UnVertex<E>)v;
			//look for the first edge containing vertex v and move pos there
			UnEdge<A> curr = firstEdge;
			
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
			// TODO Auto-generated method stub
			UnEdge<A> curr = pos; 
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
			// TODO Auto-generated method stub
			Vertex<E> current = null;
			if(pos.source.equals(vertex))
				current = pos.destination;
			else
				current = pos.source;
			pos = pos.succ;
			return current;
		}
		
	}
	
	private class EdgeIterator implements Iterator<Edge>{

		private UnEdge<A> pos;
		
		public EdgeIterator(){
			pos = firstEdge;
		}
		
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return (pos != null);
		}

		@Override
		public Edge<A> next() {
			// TODO Auto-generated method stub
			if(!hasNext())
				throw new NoSuchElementException();
			
			Edge<A> edge = pos;
			pos = pos.succ;
			return edge;
			
		}
		
	}
	
	private class VertexIterator implements Iterator<Vertex>{

		private UnVertex<E> post;
		
		public VertexIterator(){
			post = firstVertex;
		}
		
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return (post != null);
		}

		@Override
		public Vertex<E> next() {
			// TODO Auto-generated method stub
			if(!hasNext())
				throw new NoSuchElementException();
			
			Vertex<E> vertex = post;
			post = post.succ;
			return vertex;
			
		}
		
	}
	
	private class VertexEdgesIterator implements Iterator<Edge>{

		private UnVertex<E> vertex;
		private UnEdge<A> pos;
		
		public VertexEdgesIterator(Vertex<E> v){
			this.vertex = (UnVertex<E>)v;
			//look for the first edge containing vertex v and move pos there
			UnEdge<A> curr = firstEdge;
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
			// TODO Auto-generated method stub
			//the method checks the edges list and looks for the next edge that links vertex
			UnEdge<A> curr = pos; 
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
			// TODO Auto-generated method stub
			Edge<A> current = pos;
			pos = pos.succ;
			return current;
		}
		
	}
	
	public class UnVertex<E> implements Vertex<E>{
		
		private E elem;
		private UnVertex<E> pred;
		private UnVertex<E> succ;
		private boolean visited;
		private int id;
		
		public UnVertex(E elem, UnVertex<E> pred, UnVertex<E> succ){
			this.elem = elem;
			this.pred = pred;
			this.succ = succ;
			visited = false;
		}
		@Override
		public E getElement() {
			// TODO Auto-generated method stub
			return elem;
		}

		@Override
		public void setElement(E elem) {
			// TODO Auto-generated method stub
			this.elem = elem;
		}
		
		public void setId(int id){
			this.id = id;
		}
		
		public int getId(){
			return this.id;
		}
	}
	
	public class UnEdge<A> implements Edge<A>{
		
		private A attribute;
		public UnEdge<A> pred;
		public UnEdge<A> succ;
		private Vertex<E> source;
		private Vertex<E> destination;
		
		public UnEdge(A attr, Vertex<E> source, Vertex<E> destination, UnEdge<A> pred, UnEdge<A> succ){
			this.attribute = attr;
			this.pred = pred;
			this.succ = succ;
			this.source = source;
			this.destination = destination;
		}

		@Override
		public A getAttribute() {
			// TODO Auto-generated method stub
			return attribute;
		}

		@Override
		public void setAttribute(A attr) {
			// TODO Auto-generated method stub
			this.attribute = attr;
		}

		@Override
		public Vertex<E>[] getVertices() {
			// TODO Auto-generated method stub
			Vertex<E> [] v = (UnVertex<E> [])(new Object[2]);
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
