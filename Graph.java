import java.util.Iterator;

public interface Graph<E,A> {
	public int size();
	public int degree(Vertex v);
	public boolean containsEdge(Vertex v0, Vertex v1);
	public void clear();
	public Vertex<E> addVertex(E elem);
	public Edge<A> addEdge(Vertex v0, Vertex v1);
	public Edge<A> addEdge(Vertex v0, Vertex v1, A attr);
	public void removeVertex(Vertex v);
	public void removeEdge(Edge e);
	public Iterator<Vertex> vertices();
	public Iterator<Edge> edges();
	public Iterator<Vertex> neighbours(Vertex v);
	public Iterator<Edge> connectingEdges(Vertex v);
}
