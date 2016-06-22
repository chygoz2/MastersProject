public interface Edge<A>{
	public A getAttribute();
	public void setAttribute(A attr);
	public Vertex[] getVertices();
}