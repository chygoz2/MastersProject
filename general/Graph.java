package general;
import java.util.Iterator;

/**
 * interface which represents contract of a graph
 * @author Chigozie Ekwonu
 *
 * @param <E>	the type of element of a vertex
 * @param <A>	the type of attribute of an edge
 */
public interface Graph<E,A> {
	public int size();
	public int getEdgeCount();
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
	
	/**
	 * inner interface which represents contract of a vertex object
	 * @author Chigozie Ekwonu
	 *
	 * @param <E>	represents the vertex element type
	 */
	public interface Vertex<E>{
		public E getElement();
		public void setElement(E elem);
	}
	
	/**
	 * inner interface which represents the contract for an edge object
	 * @author Chigozie Ekwonu
	 *
	 * @param <A>	the edge attribute type
	 */
	public interface Edge<A>{
		public A getAttribute();
		public void setAttribute(A attr);
		public Vertex getSource();
		public Vertex getDestination();
	}
}
