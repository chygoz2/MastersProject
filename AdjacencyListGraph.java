import java.util.*;

public class AdjacencyListGraph {
	private int size, order;
	private Set<Integer> [] adjList;
	
	public AdjacencyListGraph(int max){
		adjList = (Set<Integer>[])new Object[max];
		order = 0;
		size = 0;
	}
	
	public int size(){
		return size;
	}

	public int order(){
		return order;
	}

	public int degree(int v){
		validateVertex(v);
		if(adjList[v] == null)
			throw new RuntimeException("Vertex "+v+ " does not exist.");
		return adjList[v].size();
	}
	
	public boolean containsEdge(int v1, int v2){
		validateVertex(v1); validateVertex(v2);
		if(adjList[v1] == null || adjList[v2] == null)
			throw new RuntimeException("One or more of the vertices does not exist.");
		return (adjList[v1].contains(v2) || adjList[v2].contains(v1));
	}

	public void clear(){
		for(int i=0;i<order;i++){
			adjList[i]=null;
		}	
		size=order=0;
	}

	public void addVertex(int v){
		if(validateVertex(v) && adjList[v] == null){
			adjList[v] = new HashSet<Integer>();
			order++;
		}
		else
			throw new RuntimeException("Vertex " + v + " already exists.");
	}
	
	public void addEdge(int v1, int v2){
		if(validateVertex(v1) && validateVertex(v2) && adjList[v1] != null && adjList[v2] != null){
			adjList[v1].add(v2);
			adjList[v2].add(v1);
			size++;
		}else
			throw new RuntimeException("One or more of the vertices was does not exist");
	}
	
	public void removeVertex(int v){
		validateVertex(v);
		adjList[v] = null;
		order--;
	}
	
	public void removeEdge(int v1, int v2){
		if(containsEdge(v1,v2)){
			adjList[v1].remove(v2);
			adjList[v2].remove(v1);
			size--;
		}
	}
	
	public Iterator vertices(){
		return new VertexIterator();
	}
	
	public Iterator neighbours(int v){
		validateVertex(v);
		if(adjList[v] == null)
			throw new RuntimeException("Vertex "+v+ " does not exist.");
		return adjList[v].iterator();
	}
	
	private boolean validateVertex(int v){
		if(v<0 || v>=adjList.length){
			throw new IndexOutOfBoundsException(String.format("Vertex %d is not valid", v));
		}
		return true;
	}
	
	private class VertexIterator implements Iterator, Iterable{
		private int pos=0;
		
		@Override
		public Iterator iterator() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			if(pos>=adjList.length)
				return false;
			while(true){
				if(adjList[pos]==null)
					pos++;
				else if(pos<adjList.length)
					return true;
				else 
					return false;
			}
		}

		@Override
		public Integer next() {
			// TODO Auto-generated method stub
			return pos++;
		}
		
	}
	
}
