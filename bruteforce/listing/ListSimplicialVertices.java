package bruteforce.listing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import efficient.listing.ListK4;
import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class ListSimplicialVertices {
	
	private  String p1time = "-";
	private  int found = 0;
	
	public static void main(String [] args){
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListSimplicialVertices d = new ListSimplicialVertices();
			Collection<Vertex<Integer>> svs = d.detect(graph);
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	public List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		List<Graph.Vertex<Integer>> s = find(graph);
		long end = System.currentTimeMillis();
		p1time = ""+(end-start);
		
		found = s.size();
		return s;
	}
	
	public List<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
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
	
	/**
	*	method to return the time taken to run the listing	
	*	and the number of simplicial vertices found
	*/
	public String getResult(){
		String result = String.format("%-10s%10d", p1time,found);
		return result;
	}
}
