package general;
import java.util.Iterator;

public interface Graph<E,A> {
	public int size();
	//public int order();
	public int degree(Vertex<E> v);
	public boolean containsEdge(Vertex<E> v0, Vertex<E> v1);
	public void clear();
	public Vertex<E> addVertex(E elem);
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1);
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1, A attr);
	public void removeVertex(Vertex<E> v);
	public void removeEdge(Edge<A> e);
	public Iterator<Vertex<E>> vertices();
	public Iterator<Edge<A>> edges();
	public Iterator<Vertex<E>> neighbours(Vertex<E> v);
	public Iterator<Edge<A>> connectingEdges(Vertex<E> v);
	
	public interface Vertex<E>{
		public E getElement();
		public void setElement(E elem);
		//public int getId();
	}
	
	public interface Edge<A>{
		public A getAttribute();
		public void setAttribute(A attr);
		public Vertex[] getVertices();
		public Vertex getSource();
		public Vertex getDestination();
	}
}
