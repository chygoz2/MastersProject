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
//		UndirectedGraph graph = new UndirectedGraph();
//		Graph.Vertex v1 = graph.addVertex(1);
//		Graph.Vertex v2 = graph.addVertex(2);
//		Graph.Vertex v3 = graph.addVertex(3);
//		Graph.Vertex v4 = graph.addVertex(4);
//		
//		graph.addEdge(v1, v2);
//		graph.addEdge(v3, v2);
//		graph.addEdge(v4, v2);
//		
//		graph.mapVertexToId();
		
		
//		UndirectedGraph graph = new UndirectedGraph();
//		Graph.Vertex v1 = graph.addVertex(0);
//		Graph.Vertex v2 = graph.addVertex(1);
//		Graph.Vertex v3 = graph.addVertex(2);
//		Graph.Vertex v4 = graph.addVertex(3);
//		Graph.Vertex v5 = graph.addVertex(4);
//		Graph.Vertex v6 = graph.addVertex(5);
//		Graph.Vertex v7 = graph.addVertex(6);
//		
//		graph.addEdge(v1, v5);
//		graph.addEdge(v1, v3);
//		graph.addEdge(v2, v3);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v4, v5);
//		graph.addEdge(v4, v6);
//		graph.addEdge(v4, v7);
//		graph.addEdge(v6, v7);
//		graph.addEdge(v6, v5);
//		graph.addEdge(v5, v7);
//		
//		//graph.mapVertexToId();
		
		//UndirectedGraph<Integer,Integer> graph = Utility.makeRandomGraph(7, 0.4);
		
		UndirectedGraph<Integer,Integer> graph = null;
		String fileName = "test\\testdata\\clawtestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
		
		long starttime = System.currentTimeMillis();
		DetectClaw d = new DetectClaw();
		Collection<Graph.Vertex<Integer>> claw = d.detect(graph);
		long stoptime = System.currentTimeMillis();
		if(claw!=null){
			Utility.printGraph(Utility.makeGraphFromVertexSet(graph,claw));
		}else
			System.out.println("Claw not found");
		System.out.println("Time taken in milliseconds: " + (stoptime-starttime));
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
	
	public  void resetResult(){
		p1time = "-";
		p2time = "-";
		found = "found";
	}
}
