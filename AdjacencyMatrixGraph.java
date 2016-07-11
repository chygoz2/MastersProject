import java.util.Iterator;

public class AdjacencyMatrixGraph<E,A> implements Graph<E,A> {
	
	private Object[][] matrix;
	private Vertex<E>[] vertexArray;
	
	public AdjacencyMatrixGraph(int m){
		matrix = new Object[m][m];
		vertexArray = (Vertex<E> [])new Object[m];
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

//	@Override
//	public int order() {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public int degree(Vertex<E> v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean containsEdge(Vertex<E> v0, Vertex<E> v1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vertex<E> addVertex(E elem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge<A> addEdge(Vertex<E> v0, Vertex<E> v1, A attr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeVertex(Vertex<E> v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEdge(Edge<A> e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Vertex<E>> vertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Edge<A>> edges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Vertex<E>> neighbours(Vertex<E> v) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Edge<A>> connectingEdges(Vertex<E> v) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class AMEdge<A> implements Edge<A>{

		private A attribute;
		int source;
		int destination;
		
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
			Vertex<E> [] v = (Vertex<E> [])new Object[2];
			v[0] = vertexArray[source];
			v[1] = vertexArray[destination];
			return v;
		}

		@Override
		public Vertex getSource() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Vertex getDestination() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
