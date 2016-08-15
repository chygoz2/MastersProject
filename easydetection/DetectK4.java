package easydetection;

import java.util.Collection;
import java.util.Iterator;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectK4 {

	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String [] args){
//		String fileName = "matrix4.txt";
		String fileName = "generated_graphs\\size_10\\graph_10_0.9_6.txt ";
		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(fileName);

		Collection<Graph.Vertex<Integer>> k4 = new DetectK4().detect(graph);

		if(k4!=null){
			Utility.printGraph(Utility.makeGraphFromVertexSet(graph,k4));
		}

	}

	public Collection<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		
		long starttime = System.currentTimeMillis();
		Collection<Graph.Vertex<Integer>> k4 = find(graph);
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime);
		
		if(k4==null)
			found = "not found";
		System.out.println(getResult());
		return k4;
	}
	
	public Collection<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){ 

		//get all the vertices
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		//check if each vertex has a triangle in its neighbourhood
		while(vertices.hasNext()){
			Graph.Vertex<Integer> vertex = vertices.next();

			//get the neighbourhood graph of vertex
			UndirectedGraph<Integer,Integer> neighbourhood = Utility.getNeighbourGraph(graph, vertex);
			//use triangle detection algorithm to get the triangles in the neighbourhood
			Collection<Graph.Vertex<Integer>> tri = new DetectTriangle().detect(neighbourhood);
			if(tri!=null){
				//if not null, then a triangle is found
				//add final vertex to list
				tri.add(vertex);
				return tri;
			}
		}
		return null;
	}
	
	public  String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
}
