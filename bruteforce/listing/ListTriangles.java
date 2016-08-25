package bruteforce.listing;

import java.util.*;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class ListTriangles {
	
	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListTriangles d = new ListTriangles();
			List<Collection<Vertex<Integer>>> tris = d.detect(graph);
			System.out.println("Number of triangles found: "+tris.size());
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	public List<Collection<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		List<Collection<Vertex<Integer>>> triangles = find(graph);
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start);
		if(triangles.isEmpty()){
			found = "not found";
		}
		return triangles;
	}
	
	public List<Collection<Graph.Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph){
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
	
	/**
	*	method to return the time taken to run the listing	
	*	and whether a triangle was found or not
	*/
	public String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
}
