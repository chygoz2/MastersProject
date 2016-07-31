package detectsubgraphs;
import java.util.*;

import general.*;

public class DetectKL2 {
	
	public static void main(String [] args){
		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(2);
		Graph.Vertex<Integer> v3 = graph.addVertex(3);
		Graph.Vertex<Integer> v4 = graph.addVertex(4);
		Graph.Vertex<Integer> v5 = graph.addVertex(5);
		Graph.Vertex<Integer> v6 = graph.addVertex(6);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		graph.addEdge(v3, v5);
		graph.addEdge(v2, v5);
		graph.addEdge(v4, v5);
		graph.addEdge(v2, v4);
		graph.addEdge(v1, v4);
	
		detect(graph, 6);
	}
	
	/**
	 * method to detect the presence of a complete graph of size l
	 * @param graph graph to be checked
	 * @param l	size of the complete subgraph to be found
	 * @return	the complete subgraph if found
	 */
	public static List<UndirectedGraph<Integer,Integer>> detect(UndirectedGraph<Integer,Integer> graph, int k){
		List<UndirectedGraph<Integer,Integer>> klList = new ArrayList<UndirectedGraph<Integer,Integer>>();
		
		//construct auxiliary graph H on l vertices
		UndirectedGraph<Integer,Integer> H = new UndirectedGraph<Integer,Integer>();
		int vCount = 0;
		
		HashMap hVertexMapToGVertices = new HashMap();
		
		k = 6; //assuming k = 6 for coding sake
		int l = k/3;
		
		//look for k2's in G, ie the edges in G
		Iterator<Graph.Edge<Integer>> edgeIt = graph.edges();
		while(edgeIt.hasNext()){
			Graph.Edge<Integer> edge = (Graph.Edge<Integer>)edgeIt.next();
			int[] edgeVerticesArray = new int[2];
			edgeVerticesArray[0] = (int)edge.getSource().getElement();
			edgeVerticesArray[1] = (int)edge.getDestination().getElement();
			hVertexMapToGVertices.put(vCount, edgeVerticesArray);
			vCount++;
		}
		
		//H will contain an edge between 2 vertices if G contains a K4 among the corresponding vertices
		//look for K4's in G
		
		
		
		return klList;
	}
}
