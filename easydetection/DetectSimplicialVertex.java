package easydetection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectSimplicialVertex {
	
	public static void main(String [] args){
//		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
//		
//		Graph.Vertex<Integer> v1 = graph.addVertex(1);
//		Graph.Vertex<Integer> v2 = graph.addVertex(2);
//		Graph.Vertex<Integer> v3 = graph.addVertex(3);
//		Graph.Vertex<Integer> v4 = graph.addVertex(4);
//		Graph.Vertex<Integer> v5 = graph.addVertex(5);
//		graph.addEdge(v1, v2);
//		graph.addEdge(v3, v2);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v1, v3);
////		graph.addEdge(v1, v5);
////		graph.addEdge(v3, v5);
//		graph.addEdge(v2, v5);
//		graph.addEdge(v2, v4);
//		graph.addEdge(v1, v4);
//		graph.addEdge(v4, v5);
//		
//		Utility.saveGraphToFile(graph, 0.7, 10);
//		String fileName = "generated_graphs\\size_5\\graph_5_0.7_10.txt";
//		String fileName = "generated_graphs\\size_6\\graph_6_0.6_2.txt";
		String fileName = "generated_graphs\\size_150\\graph_150_1.0_1.txt";

		for(int i=0; i<1; i++){
			UndirectedGraph<Integer, Integer> graph = Utility.makeGraphFromFile(fileName);
			long starttime = System.currentTimeMillis();
			List<Graph.Vertex<Integer>> simpVertex = detect(graph);
			long stoptime = System.currentTimeMillis();
			
			long timetaken = stoptime-starttime;
			
			if(!simpVertex.isEmpty()){
				for(Graph.Vertex<Integer> s: simpVertex)
					System.out.print(s.getElement()+", ");
			}else{
				System.out.println("Simplicial vertex not found");
			}
			System.out.println("\nTime taken in milliseconds: "+timetaken);
		}
	}
	

	public static List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		List<Graph.Vertex<Integer>> simplicialVertices = new ArrayList<Graph.Vertex<Integer>>();
		
		//get the vertices
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		while(vertices.hasNext()){
			Graph.Vertex<Integer> vertex = vertices.next();
			
			//get the neighbourhood graph of vertex
			UndirectedGraph<Integer,Integer> vertexNeighbourhood = Utility.getNeighbourGraph(graph, vertex);
			
			//check if each pair of vertices in the neighbourhood are adjacent
			Iterator<Graph.Vertex<Integer>> vIt1 = vertexNeighbourhood.vertices();
			
			boolean contains = true;
			
			here:
			while(vIt1.hasNext()){
				Graph.Vertex<Integer> v1 = vIt1.next();
				
				Iterator<Graph.Vertex<Integer>> vIt2 = vertexNeighbourhood.vertices();
				while(vIt2.hasNext()){
					Graph.Vertex<Integer> v2 = vIt2.next();
					
					if(v1!=v2 && !vertexNeighbourhood.containsEdge(v1, v2)){
						contains = false;
						break here;
					}
				}
			}
			
			if(contains){
				simplicialVertices.add(vertex);
			}
		}
		return simplicialVertices;
	}
}
