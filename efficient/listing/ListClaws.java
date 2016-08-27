package efficient.listing;
import java.util.*;

import exception.GraphFileReaderException;
import general.Graph;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;

public class ListClaws {
	
	private  String p1time = "-";
	private  int found = 0;
	
	public static void main(String [] args){		
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListClaws d = new ListClaws();
			List<List<Vertex<Integer>>> claws = d.detect(graph);
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}	
	
	public List<List<Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		List<List<Vertex<Integer>>> claws = find(graph);
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start);
		found = claws.size();
		return claws;
	}

	public List<List<Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph){
		//for each vertex, check if the complement of its neighbour contains a triangle
		List<List<Graph.Vertex<Integer>>> claws = new ArrayList<List<Graph.Vertex<Integer>>>();
		
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		while(vertices.hasNext()){
			//get neighbourhood graph
			Graph.Vertex<Integer> v = vertices.next();
			UndirectedGraph<Integer,Integer> vNeighGraph = Utility.getNeighbourGraph(graph, v);
			
			if(vNeighGraph.size()<3)
				continue;
			
			List<List<Graph.Vertex<Integer>>> cls = getClawVerticesFromNeighbourGraph(vNeighGraph);
			if(!cls.isEmpty()){
				
				for(List<Graph.Vertex<Integer>> claw: cls){
					//add v to the collection
					claw.add(v);
					
					claws.add((List<Vertex<Integer>>) claw);
				}
			}
			
		}
		return claws;
	}
	
	/**
	 * method to check if a neighbourhood contains non-central vertices of a claw by checking
	 * if the complement of the neighbourhood contains a triangle
	 * @param vNeighGraph		the neighbourhood graph to be checked
	 * @return					the vertices if found
	 */
	private List<List<Graph.Vertex<Integer>>> getClawVerticesFromNeighbourGraph(UndirectedGraph<Integer,Integer> vNeighGraph){
		//get the complement matrix of the neighbour graph
		List<List<Graph.Vertex<Integer>>> claws = new ArrayList<List<Graph.Vertex<Integer>>>();
		int[][] vncomp = vNeighGraph.getComplementMatrix();
		
		//map the neighbour graph vertices to the complement matrix indices
		List<Graph.Vertex<Integer>> vimap = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vnIt = vNeighGraph.vertices();
		while(vnIt.hasNext()){
			vimap.add(vnIt.next());
		}
		
		//create complement graph from complement matrix
		UndirectedGraph<Integer,Integer> vncompgraph = Utility.makeGraphFromAdjacencyMatrix(vncomp); 
		
		//look for a triangle in the complement graph. Such a triangle forms the remaining vertices
		//of the claw
		List<List<Graph.Vertex<Integer>>> tris =  new ListTriangles().detect(vncompgraph);
		if(!tris.isEmpty()){
			for(List<Graph.Vertex<Integer>> tri: tris){
				//get the vertices of the main graph that correspond to the vertices of the triangle found
				List<Graph.Vertex<Integer>> claw = new ArrayList<Graph.Vertex<Integer>>();
				
				for(Graph.Vertex<Integer> v: tri){
					claw.add(vimap.get(v.getElement()));
				}
				
				claws.add(claw);
			}
		}
		
		return claws;
	}
	
	/**
	*	method to return the time taken to run the listing	
	*	and the number of claws found
	*/
	public String getResult(){
		String result = String.format("%-10s%10d", p1time,found);
		return result;
	}

}