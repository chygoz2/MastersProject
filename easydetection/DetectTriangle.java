package easydetection;

import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectTriangle {
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph;

//			String fileName = "matrix3.txt";
//			String fileName = "generated_graphs\\size_7\\graph_7_0.2_2.txt";
			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
			graph = Utility.makeGraphFromFile(fileName);
//			int[][] A = {{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1},{1,0,1,0,0},{1,0,1,0,0}};
//			graph = Utility.makeGraphFromAdjacencyMatrix(A);
			
			long starttime = System.currentTimeMillis();
			List<UndirectedGraph<Integer,Integer>> triangles = detect(graph);
			long stoptime = System.currentTimeMillis();
			
			long timetaken = stoptime-starttime;
			
			
			if(!triangles.isEmpty()){
				for(UndirectedGraph<Integer, Integer> triangle: triangles)
					Utility.printGraph(triangle);
				System.out.println("Time taken in milliseconds: "+timetaken);
				System.out.println(triangles.size());
			}
			else{
				System.out.println("Triangle not found");
			}

	}
	
	public static List<UndirectedGraph<Integer,Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		List<UndirectedGraph<Integer,Integer>> triangles = new ArrayList<UndirectedGraph<Integer,Integer>>();
		List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same triangle more than once
		
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
					boolean contains = false;
					
					for(Set<Integer> s: marked){
						if(s.containsAll(triListElem)){
							contains = true;
							break;
						}
					}
					if(!contains){
						UndirectedGraph<Integer,Integer> triangle = Utility.makeGraphFromVertexSet(graph, triangleVertices);
						triangles.add(triangle);
						marked.add(triListElem);
					}
				}
			}
			
		}
		
		return triangles;
	}
}
