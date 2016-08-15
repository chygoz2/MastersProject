package efficientdetection;
import java.util.*;

import general.Graph;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;

public class DetectClaw {
	
	private  String p1time = "-";
	private  String p2time = "-";
	private  String found = "found";
	
	public static void main(String [] args){
		
		UndirectedGraph<Integer,Integer> graph = null;
		graph = Utility.makeGraphFromFile(args[0]);
		
		DetectClaw d = new DetectClaw();
		Collection<Graph.Vertex<Integer>> claw = d.detect(graph);
		System.out.print(d.getResult());
	}
	
	public  Collection<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		Collection<Graph.Vertex<Integer>> claw = null;
		long starttime = System.currentTimeMillis();
		claw = phaseOne(graph);
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime);
		
		if(claw==null){
			starttime = System.currentTimeMillis();
			claw = phaseTwo(graph);
			stoptime = System.currentTimeMillis();
			p2time = ""+(stoptime-starttime);
		}
		
		if(claw==null)
			found = "not found";
		return claw;
	}
	
	public  Collection<Graph.Vertex<Integer>> phaseOne(UndirectedGraph<Integer,Integer> graph){
		
		int edgeCount = 0;
		Iterator<Graph.Edge<Integer>> edgeIt = graph.edges();
		while(edgeIt.hasNext()){
			edgeIt.next();
			edgeCount++;
		}
		
		double maxEdgeCount = 2*Math.sqrt(edgeCount);
		Iterator<Graph.Vertex<Integer>> vertices = (Iterator<Graph.Vertex<Integer>>)graph.vertices();
		
		while(vertices.hasNext()){
			Graph.Vertex<Integer> v = vertices.next();

			//look for claw with v as central vertex
			if(graph.degree(v) > maxEdgeCount){ 		
				UndirectedGraph<Integer,Integer> vNeighGraph = Utility.getNeighbourGraph(graph, v);
				
				Collection<Graph.Vertex<Integer>> claw = getClawVerticesFromNeighbourGraph(vNeighGraph);
					
				//add the central vertex to complete claw
				claw.add(v);
				
				return claw;
				
			}
		}
		return null;
	}
	
	public  Collection<Graph.Vertex<Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph){		
		//for each vertex, check if the complement of its neighbour contains a triangle
		
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		while(vertices.hasNext()){
			//get neighbourhood graph
			Graph.Vertex<Integer> v = vertices.next();
			UndirectedGraph<Integer,Integer> vNeighGraph = Utility.getNeighbourGraph(graph, v);
			
			if(vNeighGraph.size()<3)
				continue;
			
			Collection<Graph.Vertex<Integer>> claw = getClawVerticesFromNeighbourGraph(vNeighGraph);
			if(claw!=null){
				//add v to the collection
				claw.add(v);
				
				return claw;
			}
			
		}
		return null;
	}
	
	/**
	 * method to check if a neighbourhood contains non-central vertices of a claw by checking
	 * if the complement of the neighbourhood contains a triangle
	 * @param vNeighGraph		the neighbourhood graph to be checked
	 * @return					the vertices if found
	 */
	private  Collection<Graph.Vertex<Integer>> getClawVerticesFromNeighbourGraph(UndirectedGraph<Integer,Integer> vNeighGraph){
		//get the complement matrix of the neighbour graph
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
		DetectTriangle d = new DetectTriangle();
		List<Graph.Vertex<Integer>> tri = (List<Vertex<Integer>>) d.detect(vncompgraph);
		if(tri!=null){
			//get the vertices of the main graph that correspond to the vertices of the triangle found
			Collection<Graph.Vertex<Integer>> claw = new ArrayList<Graph.Vertex<Integer>>();
			
			claw.add(vimap.get(tri.get(0).getElement()));
			claw.add(vimap.get(tri.get(1).getElement()));
			claw.add(vimap.get(tri.get(2).getElement()));
			
			return claw;
		}
		
		return null;
	}
	
	public  String getResult(){
		String result = String.format("%-10s%-10s%-10s", p1time,p2time,found);
		return result;
	}
}
