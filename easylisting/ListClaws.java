package easylisting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import efficientdetection.DetectTriangle;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class ListClaws {
	
	public static void main(String [] args){
		
		UndirectedGraph<Integer,Integer> graph = null;
//		String fileName = "test\\testdata\\clawtestdata.txt";
		String fileName = "generated_graphs\\size_100\\graph_100_0.8_14.txt";
		graph = Utility.makeGraphFromFile(fileName);
		
		long starttime = System.currentTimeMillis();
		List<Collection<Graph.Vertex<Integer>>> claws = new ListClaws().detect(graph);
		long stoptime = System.currentTimeMillis();
		if(!claws.isEmpty()){
//			for(Collection<Graph.Vertex<Integer>> claw: claws)
//				Utility.printGraph(Utility.makeGraphFromVertexSet(graph,claw));
		}else
			System.out.println("Claw not found");
		System.out.println("Time taken in milliseconds: " + (stoptime-starttime));
		System.out.println(claws.size());
	}

	public List<Collection<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){		
		List<Collection<Graph.Vertex<Integer>>> claws = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		//for each vertex, check if the complement of its neighbour contains a triangle
		
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		while(vertices.hasNext()){
			//get neighbourhood graph
			Graph.Vertex<Integer> v = vertices.next();
			UndirectedGraph<Integer,Integer> vNeighGraph = Utility.getNeighbourGraph(graph, v);
			
			if(vNeighGraph.size()<3)
				continue;
			
			List<Collection<Graph.Vertex<Integer>>> tris = getClawVerticesFromNeighbourGraph(vNeighGraph);
			for(Collection<Graph.Vertex<Integer>> tri: tris){
				//add v to the collection
				tri.add(v);
				claws.add(tri);
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
		int[][] vncomp = vNeighGraph.getComplementMatrix();
		List<Collection<Graph.Vertex<Integer>>> claws = new ArrayList<Collection<Graph.Vertex<Integer>>>(); 
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
		List<Collection<Graph.Vertex<Integer>>> triList = new ListTriangles().detect(vncompgraph);
		for(Collection<Graph.Vertex<Integer>> tri: triList){
			//get the vertices of the main graph that correspond to the vertices of the triangle found
			Collection<Graph.Vertex<Integer>> claw = new ArrayList<Graph.Vertex<Integer>>();
			
			claw.add(vimap.get(((List<Vertex<Integer>>) tri).get(0).getElement()));
			claw.add(vimap.get(((List<Vertex<Integer>>) tri).get(1).getElement()));
			claw.add(vimap.get(((List<Vertex<Integer>>) tri).get(2).getElement()));
			
			claws.add(claw);
		}
		
		return claws;
	}
}
