import java.util.*;

public class DetectDiamond {
	public static void main(String [] args){
		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		
		Set[] verticesPartition = partitionVertices(graph);
		
		Set<Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		Set<Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		System.out.println("Printing low degree vertices degrees");
		for(Vertex v: lowDegreeVertices){
			System.out.println("Vertex: "+ v.getElement() + ", degree: " + graph.degree(v));
		}
		
		System.out.println("Printing high degree vertices degrees");
		for(Vertex v: highDegreeVertices){
			System.out.println("Vertex: "+ v.getElement() + ", degree: " + graph.degree(v));
		}
		
	}
	
	//method to partition the vertices into low degree vertices and high degree vertices
	public static Set[] partitionVertices(UndirectedGraph graph){
		Set[] vertices = new Set[2];
		vertices[0] = new HashSet<Vertex>();
		vertices[1] = new HashSet<Vertex>();
		
		//get vertices
		Iterator<Vertex> vertexIterator = graph.vertices();
		
		//get edges
		Iterator<Edge> edgeIterator = graph.edges();
		
		//get number of edges
		int noOfEdges = 0;
		while(edgeIterator.hasNext()){
			edgeIterator.next();
			noOfEdges++;
		}
		

		//calculate D for vertex partitioning
		double alpha = 2.376;
		double pow = (alpha-1)/(alpha+1);
		double D = Math.pow(noOfEdges, pow);
		
		while(vertexIterator.hasNext()){
			Vertex<Integer> v = vertexIterator.next();
			if(graph.degree(v)>D)
				vertices[1].add(v);
			else
				vertices[0].add(v);
		}
		
		return vertices;
	}
}
