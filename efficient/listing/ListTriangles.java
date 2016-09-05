package efficient.listing;

import java.util.*;

import exception.GraphFileReaderException;
import general.*;
import general.Graph.Vertex;

/**
 * class to list all triangles in a graph
 * @author Chigozie Ekwonu
 *
 */
public class ListTriangles {
	
	//instance variables
	private  String p1time; //stores time taken to run the listing operation
	private  int found; //stores number of triangles found
	
	/**
	 * constructor to initialize the instance variables
	 */
	public ListTriangles(){
		this.p1time = "-";
		this.found = 0;
	}
	
	/**
	 * main method which allows direct access to the class via the command line terminal.
	 * @param args		command line arguments
	 */
	public static void main(String[] args) {
		try{
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);//create graph from file
			ListTriangles d = new ListTriangles();
			List<List<Vertex<Integer>>> triangles = d.detect(graph);
			String out = "";
			if(!triangles.isEmpty()){
				for(List<Graph.Vertex<Integer>> triangle: triangles){
					out += Utility.printList(triangle)+"\n";
				}
				out = String.format("Triangle found%nVertices:%n%s", out);
				out += String.format("Number of triangles found: %d%n", triangles.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Triangle not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
			//print out result
			System.out.println(out);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	/**
	 * method to list all triangles in a class. It calculates time taken and uses the 'find' helper method
	 * to find the triangles
	 * @param graph
	 * @return
	 */
	public List<List<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		List<List<Vertex<Integer>>> triangles = find(graph);
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start); //calculate time taken 
		found = triangles.size(); //store number of triangles found
		return triangles;
	}
	
	/**
	 * method to return list of triangles in a graph. Uses Chiba et al's triangle listing algorithm
	 * @param graph2		the graph to be checked
	 * @return				the list of vertices for each triangle found
	 */
	public List<List<Graph.Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph2){
		/*
		 * clone the graph. This is because vertices will be removed from the graph, and this
		 * would affect the result of other classes that may be using this class to list triangles.
		 * The cloning ensures that the graph object that the other classes are working with does not
		 * get affected by the removal of vertices
		 */
		UndirectedGraph<Integer,Integer> graph = graph2.clone(); 
		
		List<List<Graph.Vertex<Integer>>> triangles = new ArrayList<List<Graph.Vertex<Integer>>>();//list for triangles found
		
		//sort the vertices in non increasing order of degree
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		
		Collections.sort(vertices, new VertexComparator(graph));
		
		for(int i=0; i<vertices.size()-2; i++){
			Graph.Vertex<Integer> v = vertices.get(i);
			Iterator<Graph.Vertex<Integer>> nv = graph.neighbours(v);
			//mark all vertices adjacent to v
			List<Graph.Vertex<Integer>> marked = new ArrayList<Graph.Vertex<Integer>>();
			List<Graph.Vertex<Integer>> marked2 = new ArrayList<Graph.Vertex<Integer>>();
			while(nv.hasNext()){
				Graph.Vertex<Integer> vv = nv.next();
				marked.add(vv);
				marked2.add(vv);
			}
			
			//for each marked vertex u, do
			for(Graph.Vertex<Integer> u: marked){
				Iterator<Graph.Vertex<Integer>> nu = graph.neighbours(u);
				//for each of u's neighbours w, do
				while(nu.hasNext()){
					Graph.Vertex<Integer> w = nu.next();
					if(marked2.contains(w)){ //if w is marked, then (u,v,w) form a triangle
						List<Graph.Vertex<Integer>> triangle = new ArrayList<Graph.Vertex<Integer>>();
						triangle.add(v); triangle.add(w); triangle.add(u);
						triangles.add(triangle);
					}
					marked2.remove(u); //remove mark from u
				}
			}
			graph.removeVertex(v); //delete v from graph
		}
		
		return triangles;
	}
	
	/**
	*	method to return the time taken to run the listing	
	*	and the number of triangles found
	*/
	public String getResult(){
		String result = String.format("%-10s%10d", p1time,found);
		return result;
	}
	
	/**
	 * inner class to sort vertices in non increasing order of their degrees 
	 * @author Chigozie Ekwonu
	 *
	 */
	private static class VertexComparator implements Comparator<Graph.Vertex<Integer>>{
		
		private UndirectedGraph<Integer,Integer> graph; //graph whose vertices are to be sorted

		/**
		 * constructor to initialize instance variable
		 * @param g		parameter that the instance variable is to be initialized with 
		 */
		public VertexComparator(UndirectedGraph<Integer,Integer> g){
			this.graph = g;
		}

		/**
		 * method that compares two vertices and returns -1, 0 or 1 depending which of
		 * the two vertices has a higher degree
		 */
		public int compare(Graph.Vertex<Integer> v1, Graph.Vertex<Integer> v2) {
			Integer d1 = graph.degree(v1);
			Integer d2 = graph.degree(v2);
			
			return -1 *(d1.compareTo(d2));
		}
	}
}
