package listing;
import java.util.*;

import general.Graph;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;

public class ListClaws {
	
	private static String time = "";
	
	public static void main(String [] args){		
		
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
		
		
//		UndirectedGraph<Integer,Integer> graph = Utility.makeRandomGraph(7, 0.4);
		
		UndirectedGraph<Integer,Integer> graph = null;
		String fileName = "test\\testdata\\clawtestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
		
		long starttime = System.currentTimeMillis();
		List<Collection<Vertex<Integer>>> claws = detect(graph);
		long stoptime = System.currentTimeMillis();
		if(!claws.isEmpty()){
			for(Collection<Graph.Vertex<Integer>> claw: claws)
				Utility.printGraph(Utility.makeGraphFromVertexSet(graph,claw));
		}else
			System.out.println("Claw not found");
		System.out.println("Time taken in milliseconds: " + (stoptime-starttime));
	}	
	
	public static List<Collection<Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){		
		//for each vertex, check if the complement of its neighbour contains a triangle
		List<Collection<Graph.Vertex<Integer>>> claws = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same diamond more than once
		
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
					
					Set<Integer> clawListElem = new HashSet<Integer>(); //set to store diamond vertices elements
					for(Graph.Vertex<Integer> cv: claw){
						clawListElem.add(cv.getElement());
					}
					
					//check in the seen list for an entry that contains all 4 vertex elements
					boolean contains = marked.add(clawListElem);
					
					if(contains){
						claws.add((List<Vertex<Integer>>) claw);
					}
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
	private static List<Collection<Graph.Vertex<Integer>>> getClawVerticesFromNeighbourGraph(UndirectedGraph<Integer,Integer> vNeighGraph){
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
		List<Collection<Graph.Vertex<Integer>>> tris =  ListTriangles.detect(vncompgraph);
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
	
	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
}
