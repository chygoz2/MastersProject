import java.util.Iterator;

public class UndirectedGraph<E,A> implements Graph<E,A>{

	private UnVertex<E> firstVertex;
	private UnEdge<A> firstEdge;
	private int size;
	
	public UndirectedGraph(){
		firstVertex = null;
		firstEdge = null;
		size = 0;
	}
	
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return this.size;
	}

	@Override
	public int degree(Vertex v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean containsEdge(Vertex v0, Vertex v1) {
		// TODO Auto-generated method stub
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
	public Edge<A> addEdge(Vertex v0, Vertex v1) {
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
	public Edge<A> addEdge(Vertex v0, Vertex v1, A attr) {
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
	public void removeVertex(Vertex v) {
		// TODO Auto-generated method stub
		
		UnVertex<E> curr = firstVertex;
		if(curr == null)
			return; //vertex DLL was empty
		while(curr!=null){
			if(curr.equals(v)){ //found
				UnVertex<E> before = curr.pred;
				UnVertex<E> next = curr.succ;
				if(before==null)
					firstVertex = next; //if v is first, remove from first
				else{ //else remove from its position
					before.succ = next;
					next.pred = before;
				}
				size--; //decrement the size of the graph
				
				//remove all edges that connect to vertex v
			}
			curr = curr.succ;
		}
		
	}

	@Override
	public void removeEdge(Edge e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Vertex> vertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Edge> edges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Vertex> neighbours(Vertex v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Edge> connectingEdges(Vertex v) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class UnVertex<E> implements Vertex<E>{
		
		private E elem;
		public UnVertex<E> pred;
		public UnVertex<E> succ;
		
		public UnVertex(E elem, UnVertex<E> pred, UnVertex<E> succ){
			this.elem = elem;
			this.pred = pred;
			this.succ = succ;
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
		
	}
	
	public class UnEdge<A> implements Edge<A>{
		
		private A attribute;
		public Edge<A> pred;
		public Edge<A> succ;
		private Vertex<E> source;
		private Vertex<E> destination;
		
		public UnEdge(A attr, Vertex<E> source, Vertex<E> destination, Edge<A> pred, Edge<A> succ){
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
		public Vertex[] getVertices() {
			// TODO Auto-generated method stub
			Vertex<E> [] v = (UnVertex<E> [])(new Object[2]);
			v[0] = source;
			v[1] = destination;
			return v;
		}
	}

}
