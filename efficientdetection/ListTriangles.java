package efficientdetection;

import java.util.*;
import general.*;


public class ListTriangles {
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph;
//		for(int a=0;a<15;a++){
//			String fileName = "matrix3.txt";
//			String fileName = "generated_graphs\\size_7\\graph_7_0.2_2.txt";
//			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
			String fileName = "generated_graphs\\size_15\\graph_15_1.0_1.txt";
			graph = Utility.makeGraphFromFile(fileName);
//			int[][] A = {{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1},{1,0,1,0,0},{1,0,1,0,0}};
//			graph = Utility.makeGraphFromAdjacencyMatrix(A);
			
			long starttime = System.currentTimeMillis();
			List<Collection<Graph.Vertex<Integer>>> triangles = detect(graph);
//			List<UndirectedGraph<Integer,Integer>> triangles = DetectKL.detect(graph,3);
			long stoptime = System.currentTimeMillis();
			
			long timetaken = stoptime-starttime;
			
			
			if(!triangles.isEmpty()){
				for(Collection<Graph.Vertex<Integer>> triangle: triangles){
					Utility.printGraph(Utility.makeGraphFromVertexSet(graph, triangle));
				}
				System.out.println("Time taken in milliseconds: "+timetaken);
				System.out.println(triangles.size());
			}
			else{
				System.out.println("Triangle not found");
			}

	}
	
	public static List<Collection<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph2){
		UndirectedGraph<Integer,Integer> graph = graph2.clone();
		List<Collection<Graph.Vertex<Integer>>> triangles = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		
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
				if(!vv.equals(v)){
					marked.add(vv);
					marked2.add(vv);
				}
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
				}
				
				marked2.remove(u);
			}
			
			graph.removeVertex(v);
		}
		
		return triangles;
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
