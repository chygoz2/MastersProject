package efficient.listing;

import java.util.*;

import exception.GraphFileReaderException;
import general.*;
import general.Graph.Vertex;


public class ListTriangles {
	
	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListTriangles d = new ListTriangles();
			List<List<Vertex<Integer>>> tris = d.detect(graph);
			System.out.println("Number of triangles found: "+tris.size());
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	public List<List<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		List<List<Vertex<Integer>>> triangles = find(graph);
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start);
		if(triangles.isEmpty()){
			found = "not found";
		}
		return triangles;
	}
	
	public List<List<Graph.Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph2){
		UndirectedGraph<Integer,Integer> graph = graph2.clone();
		List<List<Graph.Vertex<Integer>>> triangles = new ArrayList<List<Graph.Vertex<Integer>>>();
		
		//sort the vertices in non increasing order of degree
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		
		Collections.sort(vertices, new VertexComparator(graph));
		
		for(int i=0; i<vertices.size()-2; i++){
			Graph.Vertex<Integer> v = vertices.get(i);
			Iterator<Graph.Vertex<Integer>> nv = graph.neighbours(v);
			List<Graph.Vertex<Integer>> marked = new ArrayList<Graph.Vertex<Integer>>();
			List<Graph.Vertex<Integer>> marked2 = new ArrayList<Graph.Vertex<Integer>>();
			while(nv.hasNext()){
				Graph.Vertex<Integer> vv = nv.next();
				marked.add(vv);
				marked2.add(vv);
			}
			
			for(Graph.Vertex<Integer> u: marked){
				Iterator<Graph.Vertex<Integer>> nu = graph.neighbours(u);
				while(nu.hasNext()){
					Graph.Vertex<Integer> w = nu.next();
					if(marked2.contains(w)){
						List<Graph.Vertex<Integer>> triangle = new ArrayList<Graph.Vertex<Integer>>();
						triangle.add(v); triangle.add(w); triangle.add(u);
						triangles.add(triangle);
					}
					marked2.remove(u);
				}
				
				graph.removeVertex(v);
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
	
	public static class VertexComparator implements Comparator<Graph.Vertex<Integer>>{
		
		private UndirectedGraph<Integer,Integer> graph;
		
		public VertexComparator(UndirectedGraph<Integer,Integer> g){
			this.graph = g;
		}

		@Override
		public int compare(Graph.Vertex<Integer> v1, Graph.Vertex<Integer> v2) {
			Integer d1 = graph.degree(v1);
			Integer d2 = graph.degree(v2);
			
			return -1 *(d1.compareTo(d2));
		}
	}
}
