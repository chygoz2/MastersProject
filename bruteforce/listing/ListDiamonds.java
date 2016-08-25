package bruteforce.listing;

import java.io.IOException;
import java.util.*;

import exception.GraphFileReaderException;
import exception.MatrixException;
import general.*;
import general.Graph.Vertex;

public class ListDiamonds {

	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String [] args) throws IOException{
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListDiamonds d = new ListDiamonds();
			List<Collection<Vertex<Integer>>> diamonds = d.detect(graph);
			System.out.println("Number of diamonds found: "+diamonds.size());
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	/**
	 * method to detect and return all induced diamonds found
	 * @param graph 		the graph to be checked
	 * @return  			the list of sets of vertices which induce diamonds
	 */
	public List<Collection<Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		List<Collection<Vertex<Integer>>> diamonds = find(graph);
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start);
		if(diamonds.isEmpty()){
			found = "not found";
		}
		return diamonds;
	}
	
	public List<Collection<Graph.Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph){
		List<Collection<Graph.Vertex<Integer>>> diamonds = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same diamond more than once
		
		//get the vertices
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		//for each vertex, get its neighbourhood and get a P3 in its neighbourhood
		while(vertices.hasNext()){
			Graph.Vertex<Integer> x = vertices.next();
			UndirectedGraph<Integer,Integer> xn = Utility.getNeighbourGraph(graph, x);

			List<Collection<Graph.Vertex<Integer>>> p3s = checkP3InComponent(xn);
			
			//if p3 is found, then the p3 along with x produce a diamond
			for(Collection<Graph.Vertex<Integer>> p3:p3s){
				Set<Integer> hh = new HashSet<Integer>();
				List<Graph.Vertex<Integer>> diamond = new ArrayList<Graph.Vertex<Integer>>();
				
				for(Graph.Vertex<Integer> v: p3){
					diamond.add(v);
					hh.add(v.getElement());
				}
				diamond.add(x);	hh.add(x.getElement());
				
				if(marked.add(hh)){
					diamonds.add(diamond);
				}
			}
		}
		
		return diamonds;
	}
	
	/**
	 * method that checks if a P3 is present in a graph and returns the vertices of the P3s
	 * @param graph 	the graph to be checked
	 * @return 			the vertex list if found
	 */
	public List<Collection<Graph.Vertex<Integer>>> checkP3InComponent(UndirectedGraph<Integer,Integer> graph){
		//if the no of vertices in graph is less than 3, then graph cannot have a p3
		
		if(graph.size()<3){
			return null;
		}
		
		List<Collection<Graph.Vertex<Integer>>> p3s = new ArrayList<Collection<Graph.Vertex<Integer>>>(); //stores vertices of p3s found
		Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same p3 more than once
	
		//create mapping between matrix indices and vertex
		List<Graph.Vertex<Integer>> vertexIndexMap = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices(); //gets the vertex iterator
		while(vIt.hasNext()){
			vertexIndexMap.add(vIt.next());
		}
		
		int[][] A = graph.getAdjacencyMatrix();
		int[][] A2;
		try {
			A2 = Utility.multiplyMatrix(A, A);
		} catch (MatrixException e) {
			return null;
		}
		
		//look for a path of length 2 in A2
		for(int i=0; i<A2.length;i++){
			for(int j=i+1; j<A2.length;j++){
				if((int)A2[i][j]>0 && (int)A[i][j]==0){ //then a P3 is found
					//look for third vertex
					for(int k=0; k<A.length;k++){
						if(k!=i && k!= j && ((int)A[k][i] == 1) && ((int)A[k][j] == 1)){
							List<Graph.Vertex<Integer>> vert = new ArrayList<Graph.Vertex<Integer>>();
							Set<Integer> hh = new HashSet<Integer>();
							Graph.Vertex<Integer> u = vertexIndexMap.get(i);
							Graph.Vertex<Integer> v = vertexIndexMap.get(j);
							Graph.Vertex<Integer> w = vertexIndexMap.get(k); 
							
							vert.add(u); vert.add(v); vert.add(w);
							hh.add(u.getElement()); hh.add(v.getElement()); hh.add(w.getElement());
							
							if(marked.add(hh)){
								p3s.add(vert);
							}
						}
					}
				}
			}
		}
	
		return p3s;
	}
	
	/**
	*	method to return the time taken to run the listing	
	*	and whether a diamond was found or not
	*/
	public String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
}
