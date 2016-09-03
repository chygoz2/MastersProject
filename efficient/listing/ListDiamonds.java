package efficient.listing;
import java.util.*;

import exception.GraphFileReaderException;
import exception.MatrixException;
import general.Graph;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;
import java.io.*;

/**
 * class that lists diamonds found in a graph
 * @author Chigozie Ekwonu
 *
 */
public class ListDiamonds {
	private  String p1time = "-";
	private  int found = 0;
	
	public static void main(String [] args) throws IOException{
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListDiamonds d = new ListDiamonds();
			List<List<Vertex<Integer>>> diamonds = d.detect(graph);
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	/**
	 * method to detect and return all induced diamonds
	 * @param graph 		the graph to be checked
	 * @return  			the list of sets of vertices which induce diamonds
	 */
	public List<List<Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		List<List<Vertex<Integer>>> diamonds = find(graph);
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start);
		found = diamonds.size();
		return diamonds;
	}
	
	public List<List<Graph.Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph){ 
		
		//partition graph vertices into low and high degree vertices
		List[] verticesPartition = partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		//phase one
		long starttime = System.currentTimeMillis();
		List<List<Graph.Vertex<Integer>>> diamonds = phaseOne(graph);
		long stoptime = System.currentTimeMillis();
		
		return diamonds;
	}
	
	/**
	 * method that checks a graph for a diamond by checking if the diamond has a low degree vertex in it
	 * @param lowDegreeVertices 	a list of low degree vertices of the graph
	 * @param graph 				the graph to be checked
	 * @return 						a map object containing a diamond if found, as well as a collection of cliques mapped to each low degree
	 * 								vertex
	 */
	public List<List<Vertex<Integer>>> phaseOne(UndirectedGraph<Integer,Integer> graph){
		
		List<List<Graph.Vertex<Integer>>> diamonds = new ArrayList<List<Graph.Vertex<Integer>>>();
		Set<Set<Integer>> seen = new HashSet<Set<Integer>>(); //to prevent creating the same diamond more than once
		
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		while(vertices.hasNext()){
			Graph.Vertex<Integer> v = vertices.next();

			//get the neighbourhood graph of vertex v
			UndirectedGraph<Integer,Integer> graph2 = Utility.getNeighbourGraph(graph, v);
			
			List<UndirectedGraph<Integer,Integer>> graph2Comps = Utility.getComponents(graph2); //get components of the induced neighbour graph
			
			//check if each component is a clique
			for(UndirectedGraph<Integer,Integer> graphC: graph2Comps){
				boolean isClique = checkIfClique(graphC);
				if(!isClique){
					//if component is not a clique, check if it contains a P3 and thus a diamond
					List<List<Graph.Vertex<Integer>>> p3s = getP3(graphC);
					
					//if a P3 is found, then the graph contains a diamond
					for(List<Graph.Vertex<Integer>> p3: p3s){
						//produce diamonds found 
						p3.add(v);

						Set<Integer> diamondListElem = new HashSet<Integer>(); //set to store diamond vertices elements
						for(Graph.Vertex<Integer> vv: p3){
							diamondListElem.add(vv.getElement());
						}
						
						//check in the seen list for an entry that contains all 4 vertex elements
						boolean contains = seen.add(diamondListElem);
						
						//get the subgraph induced by the vertex set comprising of the P3 vertices and the current low degree vertex
						if(contains){
							diamonds.add(p3);
						}
					}
					
				}
			}
		}
		return diamonds;
	}
	
	
	/**
	 * method that checks if a graph is a clique
	 * @param graph 	the graph to be checked
	 * @return 			the result of the check. 
	 */
	private boolean checkIfClique(UndirectedGraph<Integer,Integer> graph){
		int edgeCount = 0;
		Iterator<Graph.Edge<Integer>> eit = graph.edges();
		while(eit.hasNext()){
			eit.next();
			edgeCount++;
		}
		int reqCount = (graph.size()*(graph.size()-1))/2;
		if(edgeCount==reqCount){
			return true;
		}
		return false;
	}

	/**
	 * method that checks if a P3 is present in a graph and returns the vertices of the P3s
	 * @param graph 	the graph to be checked
	 * @return 			the vertex list if found
	 */
	public List<List<Graph.Vertex<Integer>>> getP3(UndirectedGraph<Integer,Integer> graph){
		//if the no of vertices in graph is less than 3, then graph cannot have a p3
		
		if(graph.size()<3){
			return null;
		}
		
		List<List<Graph.Vertex<Integer>>> p3s = new ArrayList<List<Graph.Vertex<Integer>>>(); //stores vertices of p3s found
		Set<Set<Integer>> seen = new HashSet<Set<Integer>>(); //to prevent creating the same p3 more than once
	
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
							
							if(seen.add(hh)){
								p3s.add(vert);
							}
						}
					}
				}
			}
		}
	
		return p3s;
	}
	
	//method to partition the vertices into low degree vertices and high degree vertices
	public List<Graph.Vertex<Integer>>[] partitionVertices(UndirectedGraph<Integer,Integer> graph){
		List<Graph.Vertex<Integer>>[] vertices = new List[2];
		vertices[0] = new ArrayList<Graph.Vertex<Integer>>();
		vertices[1] = new ArrayList<Graph.Vertex<Integer>>();
		
		//get vertices
		Iterator<Graph.Vertex<Integer>> vertexIterator = graph.vertices();
		
		//get number of edges
		int noOfEdges = graph.getEdgeCount();

		//calculate D for Graph.Vertex partitioning
		double D = Math.sqrt(noOfEdges);
		
		while(vertexIterator.hasNext()){
			Graph.Vertex<Integer> v = vertexIterator.next();
			if(graph.degree(v)>D)
				vertices[1].add(v);
			else
				vertices[0].add(v);
		}
		
		return vertices;
	}
	
	/**
	*	method to return the time taken to run the listing	
	*	and the number of diamonds found
	*/
	public String getResult(){
		String result = String.format("%-10s%10d", p1time,found);
		return result;
	}
}
