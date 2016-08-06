package easydetection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectK4 {
	
	public static void main(String [] args){
		String fileName = "matrix4.txt";
		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(fileName);
		
		long starttime = System.currentTimeMillis();
		List<UndirectedGraph<Integer,Integer>> k4List = detect(graph);
		long stoptime = System.currentTimeMillis();
		
		long timetaken = stoptime-starttime;
		
		for(UndirectedGraph<Integer,Integer> k4: k4List){
			Utility.printGraph(k4);
		}
		System.out.println("Time taken in milliseconds: "+timetaken);
					
	}
	
	public static List<UndirectedGraph<Integer,Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same k4 more than once 
		List<UndirectedGraph<Integer,Integer>> k4List = new ArrayList<UndirectedGraph<Integer,Integer>>();
		
		//get all the vertices
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		//check if each vertex has a triangle in its neighbourhood
		while(vertices.hasNext()){
			Graph.Vertex<Integer> vertex = vertices.next();
			
			//get the neighbourhood graph of vertex
			UndirectedGraph<Integer,Integer> neighbourhood = Utility.getNeighbourGraph(graph, vertex);
			//use triangle detection algorithm to get the triangles in the neighbourhood
			List<UndirectedGraph<Integer,Integer>> tris = DetectTriangle.detect(neighbourhood);
			if(!tris.isEmpty()){
				//if list is not empty, then a triangle is found
				//get the vertices of each triangle 
				for(UndirectedGraph<Integer,Integer> triangle: tris){
					List<Graph.Vertex<Integer>> k4VertexList = new ArrayList<Graph.Vertex<Integer>>();
					Iterator<Graph.Vertex<Integer>> triVertices = triangle.vertices();
					
					Set<Integer> k4VerticesElem = new HashSet<Integer>(); //list to store k4 vertices
					
					while(triVertices.hasNext()){
						Graph.Vertex<Integer> v = triVertices.next();
						k4VertexList.add(v);
						k4VerticesElem.add(v.getElement());
					}
					
					//add final vertex to list
					k4VertexList.add(vertex);
					k4VerticesElem.add(vertex.getElement());
					
					//check in the marked list for an entry that contains all 4 vertex elements
					boolean contains = false;
					
					for(Set<Integer> s: marked){
						if(s.containsAll(k4VerticesElem)){
							contains = true;
							break;
						}
					}
					
					if(!contains){
						UndirectedGraph<Integer,Integer> k4 = Utility.makeGraphFromVertexSet(graph, k4VertexList);
						k4List.add(k4);
						marked.add(k4VerticesElem);
					}
				}
			}
		}
		return k4List;
	}
}
