package easylisting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class ListK4 {
	
	public static void main(String [] args){
//		String fileName = "matrix4.txt";
		String fileName = "generated_graphs\\size_20\\graph_20_1.0_9.txt";
		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(fileName);
		
		long starttime = System.currentTimeMillis();
		List<Collection<Graph.Vertex<Integer>>> k4List = detect(graph);
		long stoptime = System.currentTimeMillis();
		
		long timetaken = stoptime-starttime;
		
		for(Collection<Graph.Vertex<Integer>> k4: k4List){
			Utility.printGraph(Utility.makeGraphFromVertexSet(graph, k4));
		}
		System.out.println("Time taken in milliseconds: "+timetaken);
		System.out.println(k4List.size());
	}
	
	public static List<Collection<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same k4 more than once 
		List<Collection<Graph.Vertex<Integer>>> k4List = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		
		//get all the vertices
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		//check if each vertex has a triangle in its neighbourhood
		while(vertices.hasNext()){
			Graph.Vertex<Integer> vertex = vertices.next();
			
			//get the neighbourhood graph of vertex
			UndirectedGraph<Integer,Integer> neighbourhood = Utility.getNeighbourGraph(graph, vertex);
			//use triangle detection algorithm to get the triangles in the neighbourhood
			List<Collection<Graph.Vertex<Integer>>> tris = new ListTriangles().detect(neighbourhood);
			if(!tris.isEmpty()){
				//if list is not empty, then a triangle is found
				//get the vertices of each triangle 
				for(Collection<Graph.Vertex<Integer>> triangle: tris){
					List<Graph.Vertex<Integer>> k4VertexList = new ArrayList<Graph.Vertex<Integer>>();
					
					Set<Integer> k4VerticesElem = new HashSet<Integer>(); //list to store k4 vertices
					
					for(Graph.Vertex<Integer> v: triangle){
						k4VertexList.add(v);
						k4VerticesElem.add(v.getElement());
					}
					
					//add final vertex to list
					k4VertexList.add(vertex);
					k4VerticesElem.add(vertex.getElement());
					
					if(marked.add(k4VerticesElem)){
						k4List.add(k4VertexList);
					}
				}
			}
		}
		return k4List;
	}
}
