package bruteforce.listing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class ListK4 {
	
	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String [] args) throws IOException{
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListK4 d = new ListK4();
			List<Collection<Vertex<Integer>>> k4s = d.detect(graph);
			System.out.println("Number of k4 found: "+k4s.size());
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	public List<Collection<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
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
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start);
		if(k4List.isEmpty()){
			found = "not found";
		}
		return k4List;
	}
	
	/**
	*	method to return the time taken to run the listing	
	*	and whether a k4 was found or not
	*/
	public String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
}
