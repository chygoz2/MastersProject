package easylisting;

import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class ListTriangles {
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph;

//			String fileName = "matrix3.txt";
//			String fileName = "generated_graphs\\size_7\\graph_7_0.2_2.txt";
//			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
			String fileName = "generated_graphs\\size_15\\graph_15_1.0_1.txt";
			graph = Utility.makeGraphFromFile(fileName);
//			int[][] A = {{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1},{1,0,1,0,0},{1,0,1,0,0}};
//			graph = Utility.makeGraphFromAdjacencyMatrix(A);
			
			long starttime = System.currentTimeMillis();
			List<Collection<Graph.Vertex<Integer>>> triangles = new ListTriangles().detect(graph);
			long stoptime = System.currentTimeMillis();
			
			long timetaken = stoptime-starttime;
			
			
			if(!triangles.isEmpty()){
				for(Collection<Graph.Vertex<Integer>> triangle: triangles)
					Utility.printGraph(Utility.makeGraphFromVertexSet(graph,triangle));
				System.out.println("Time taken in milliseconds: "+timetaken);
				System.out.println(triangles.size());
			}
			else{
				System.out.println("Triangle not found");
			}

	}
	
	public List<Collection<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		List<Collection<Graph.Vertex<Integer>>> triangles = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same triangle more than once
		
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
					
					Set<Integer> triListElem = new HashSet<Integer>(); //list to store triangle vertices elements
					
					triListElem.add(source.getElement()); triListElem.add(destination.getElement());
					triListElem.add(vertex.getElement());
					
					//check in the marked list for an entry that contains all 3 vertex elements
					if(marked.add(triListElem)){
						triangles.add(triangleVertices);
					}
				}
			}
			
		}
		
		return triangles;
	}
}
