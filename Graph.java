import java.util.Iterator;

public interface Graph<E,A> {
	public int size();
	public int order();
	public int degree(Vertex<E> v);
	public boolean containsEdge(Vertex<E> v0, Vertex<E> v1);
	public void clear();
	public Vertex<E> addVertex(E elem);
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1);
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1, A attr);
	public void removeVertex(Vertex<E> v);
	public void removeEdge(Edge<A> e);
	public Iterator<Vertex> vertices();
	public Iterator<Edge> edges();
	public Iterator<Vertex> neighbours(Vertex<E> v);
	public Iterator<Edge> connectingEdges(Vertex<E> v);
}
