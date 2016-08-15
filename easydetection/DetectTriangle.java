package easydetection;

import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectTriangle {
	private String time = "-";
	private String found = "found";
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph;

//			String fileName = "matrix3.txt";
//			String fileName = "generated_graphs\\size_7\\graph_7_0.2_2.txt";
			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
			graph = Utility.makeGraphFromFile(fileName);
//			int[][] A = {{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1},{1,0,1,0,0},{1,0,1,0,0}};
//			graph = Utility.makeGraphFromAdjacencyMatrix(A);
			
			long starttime = System.currentTimeMillis();
			DetectTriangle d = new DetectTriangle();
			
			Collection<Graph.Vertex<Integer>> triangle = d.detect(graph);
			long stoptime = System.currentTimeMillis();
			
			long timetaken = stoptime-starttime;
			
			
			if(triangle!=null){
				Utility.printGraph(Utility.makeGraphFromVertexSet(graph,triangle));
			}
			else{
				System.out.println("Triangle not found");
			}

	}
	
	public Collection<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		long starttime = System.currentTimeMillis();
		Collection<Graph.Vertex<Integer>> triangle= find(graph);
		long stoptime = System.currentTimeMillis();
		time = ""+(stoptime-starttime);
		
		if(triangle==null)
			found = "not found";
		System.out.println(getResult());
		
		return triangle;
	}
	
	public static Collection<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){
		
		//for each edge in graph, check if each vertex has an edge between it and the 
		//vertices at the edge
		Iterator<Graph.Edge<Integer>> edges = graph.edges();
		while(edges.hasNext()){
			Graph.Edge<Integer> edge = edges.next();
			//get the vertices
			Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
			
			//check whether an edge exist between vertices at the ends of the edge and
			//the third vertex
			while(vertices.hasNext()){
				Graph.Vertex<Integer> vertex = vertices.next();
				
				//get the end vertices
				Graph.Vertex<Integer> source = edge.getSource();
				Graph.Vertex<Integer> destination = edge.getDestination();
				
				if(graph.containsEdge(source, vertex) && graph.containsEdge(destination, vertex)){
					List<Graph.Vertex<Integer>> triangleVertices = new ArrayList<Graph.Vertex<Integer>>();
					triangleVertices.add(source);
					triangleVertices.add(destination);
					triangleVertices.add(vertex);				
					
					return triangleVertices;					
				}
			}
			
		}
		
		return null;
	}
	
	public String getResult(){
		String result = String.format("%-10s%-10s", time,found);
		return result;
	}
}
