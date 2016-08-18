package efficientlisting;
import java.util.*;

import general.Graph;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;

public class ListClaws {
	
	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String [] args){		
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListClaws d = new ListClaws();
			List<Collection<Vertex<Integer>>> claws = d.detect(graph);
			System.out.println("Number of claws found: "+claws.size());
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}
	}	
	
	public List<Collection<Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		List<Collection<Vertex<Integer>>> claws = find(graph);
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start);
		if(claws.isEmpty()){
			found = "not found";
		}
		return claws;
	}

	public List<Collection<Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph){
		//for each vertex, check if the complement of its neighbour contains a triangle
		List<Collection<Graph.Vertex<Integer>>> claws = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		while(vertices.hasNext()){
			//get neighbourhood graph
			Graph.Vertex<Integer> v = vertices.next();
			UndirectedGraph<Integer,Integer> vNeighGraph = Utility.getNeighbourGraph(graph, v);
			
			if(vNeighGraph.size()<3)
				continue;
			
			List<Collection<Graph.Vertex<Integer>>> cls = getClawVerticesFromNeighbourGraph(vNeighGraph);
			if(!cls.isEmpty()){
				
				for(Collection<Graph.Vertex<Integer>> claw: cls){
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
	private List<Collection<Graph.Vertex<Integer>>> getClawVerticesFromNeighbourGraph(UndirectedGraph<Integer,Integer> vNeighGraph){
		//get the complement matrix of the neighbour graph
		List<Collection<Graph.Vertex<Integer>>> claws = new ArrayList<Collection<Graph.Vertex<Integer>>>();
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
		List<Collection<Graph.Vertex<Integer>>> tris =  new ListTriangles().detect(vncompgraph);
		if(!tris.isEmpty()){
			for(Collection<Graph.Vertex<Integer>> tri: tris){
				//get the vertices of the main graph that correspond to the vertices of the triangle found
				Collection<Graph.Vertex<Integer>> claw = new ArrayList<Graph.Vertex<Integer>>();
				
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
	*	and whether a claw was found or not
	*/
	public String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}

}
