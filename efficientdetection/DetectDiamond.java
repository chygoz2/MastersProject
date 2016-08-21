package efficientdetection;
import java.util.*;

import general.Graph;
import general.MatrixException;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;

import java.io.*;

/**
 * class that detects a diamond in a graph if any and returns it
 * @author Chigozie Ekwonu
 *
 */
public class DetectDiamond {
	private String p1time = "-";
	private String p2time = "-";
	private String p3time = "-";
	private String found = "found";
	
	public static void main(String [] args) throws IOException{
		try{
			UndirectedGraph<Integer,Integer> graph = null;
			graph = Utility.makeGraphFromFile(args[0]);
			
			DetectDiamond d = new DetectDiamond();
			List<Graph.Vertex<Integer>> diamond = d.detect(graph);
			if(diamond!=null){
				Utility.printGraph(Utility.makeGraphFromVertexSet(graph,diamond));
			}
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}
	}
	
	/**
	 * method to detect and return an induced subgraph isomorphic with a diamond
	 * @param graph 		the graph to be checked
	 * @return  			the diamond subgraph
	 */
	public List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		
		//partition graph vertices into low and high degree vertices
		List<Graph.Vertex<Integer>>[] verticesPartition = partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		
		//check for a diamond in phase one and measure time taken
		long starttime = System.currentTimeMillis();
		Map<String,Object> phase1Results = phaseOne(lowDegreeVertices, graph);
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime);
		
		//get cliques found in phase one
		Map<Integer,UndirectedGraph<Integer,Integer>> cli = (HashMap<Integer,UndirectedGraph<Integer,Integer>>)phase1Results.get("cliques");
		 

		List<Graph.Vertex<Integer>> diamond = (List<Vertex<Integer>>) phase1Results.get("diamond");
		if(diamond==null){ //if no diamond found in phase one, check for diamond in phase two and measure time taken
			starttime = System.currentTimeMillis();
			diamond = phaseTwo(cli, graph);
			stoptime = System.currentTimeMillis();
			p2time = ""+(stoptime-starttime);
			
			if(diamond==null){ //if no diamond found in phase two, check for diamond in phase three and measure time taken
				starttime = System.currentTimeMillis();
				diamond = phaseThree(graph, lowDegreeVertices);
				stoptime = System.currentTimeMillis();
				p3time = ""+(stoptime-starttime);
				
			}
		}

		if(diamond==null)
			found = "not found";

		return diamond;
	}
	
	/**
	 * method that checks a graph for a diamond by checking if the diamond has a low degree vertex in it
	 * @param lowDegreeVertices 	a list of low degree vertices of the graph
	 * @param graph 				the graph to be checked
	 * @return 						a map object containing a diamond if found, as well as a collection of cliques mapped to each low degree
	 * 								vertex
	 */
	public  Map<String,Object> phaseOne(Collection<Graph.Vertex<Integer>> lowDegreeVertices, UndirectedGraph<Integer,Integer> graph){
		
		Map<String,Object> phaseOneResults = new HashMap<String,Object>(); //map for storing the results of the phase
		
		//create a map for storing cliques and which vertex's neighbourhood they are in
		Map<Integer, List<UndirectedGraph<Integer,Integer>>> vertexCliques = new HashMap<Integer, List<UndirectedGraph<Integer,Integer>>>();
		
		here:
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			//get the neighbourhood graph of vertex v
			UndirectedGraph<Integer,Integer> graph2 = Utility.getNeighbourGraph(graph, v);
			
			//Create list for storing cliques in the neighbourhood of vertex v
			List<UndirectedGraph<Integer,Integer>> cliques = new ArrayList<UndirectedGraph<Integer,Integer>>();
			
			List<UndirectedGraph<Integer,Integer>> graph2Comps = Utility.getComponents(graph2); //get components of the induced neighbour graph
			
			//check if each component is a clique
			for(UndirectedGraph<Integer,Integer> graphC: graph2Comps){
				boolean isClique = checkIfClique(graphC);
				//System.out.println("Is clique? "+isClique);
				if(isClique){
					cliques.add(graphC); //add component to cliques list to be used in phase 2
				}else{
					//if component is not a clique, check if it contains a P3 and thus a diamond
					List<Graph.Vertex<Integer>> resultList = checkP3InComponent(graphC);
					
					if(resultList!=null){
						//if a P3 is found, then the graph contains a diamond
						
						//produce one such diamond 
						resultList.add(v);
						//get the subgraph induced by the vertex set comprising of the P3 vertices and the current low degree vertex
						phaseOneResults.put("diamond", resultList);
						break here;
					}
				}
			}
			
			vertexCliques.put(v.getElement(), cliques); //map cliques found to the low degree vertex
		}
		
		phaseOneResults.put("cliques", vertexCliques);
		return phaseOneResults;
		
	}
	
	/**
	 * method that checks for the presence of a diamond subgraph in a graph.
	 * Method works by checking each clique of each low degree vertex to see if the condition
	 * described by Kloks et al. is satisfied.
	 * @param cliques 		the mapping of cliques to low degree vertices
	 * @param graph 		the graph to be checked
	 * @return 				a diamond subgraph vertices if found
	 */
	public  List<Graph.Vertex<Integer>> phaseTwo(Map<Integer,UndirectedGraph<Integer,Integer>> cliques, UndirectedGraph<Integer,Integer> graph){
		List<Graph.Vertex<Integer>> diamond = null;
		
		//get adjacency matrix of graph
		int[][] A = graph.getAdjacencyMatrix();
		int[][] squareA;
		try {
			squareA = Utility.multiplyMatrix(A, A);
		} catch (MatrixException e) {
			return null;
		}
//	
		Set<Integer> vertexKeys = cliques.keySet();

		here:
		for(Integer lowVertex: vertexKeys){
			//for each low degree vertex, get its cliques
			List<UndirectedGraph<Integer,Integer>> cliqs = (List<UndirectedGraph<Integer,Integer>>)cliques.get(lowVertex);
			for(UndirectedGraph<Integer,Integer> cliq: cliqs){
				//for each clique, perform the check
				Iterator<Graph.Edge<Integer>> edgeIt = cliq.edges();
				while(edgeIt.hasNext()){
					//get pair in clique(as vertices at edges)
					UndirectedGraph.UnEdge ee = (UndirectedGraph.UnEdge)edgeIt.next();
					Graph.Vertex<Integer> y = (UndirectedGraph.UnVertex) ee.getSource();
					Graph.Vertex<Integer> z = (UndirectedGraph.UnVertex) ee.getDestination();
					
					//get ids of y and z
					int yId = (int) y.getElement();
					int zId = (int) z.getElement();
					
					//perform check 
					if(squareA[yId][zId] > cliq.size()-1){ //then y and z have a common neighbor
						//outside the closed neighbourhood of x and hence a diamond can found
						//get y's and z's neighbours from the main graph and put them in a set
						Iterator<Graph.Vertex<Integer>> yIt = graph.neighbours(graph.getVertexWithElement(yId));
						List<Graph.Vertex<Integer>> yNeigh = new ArrayList<Graph.Vertex<Integer>>();
						while(yIt.hasNext()){
							Graph.Vertex<Integer> s = yIt.next();
							if(s.getElement() != lowVertex)
								yNeigh.add(s);
						}
						
						Iterator<Graph.Vertex<Integer>> zIt = graph.neighbours(graph.getVertexWithElement(zId));
						List<Graph.Vertex<Integer>> zNeigh = new ArrayList<Graph.Vertex<Integer>>();
						while(zIt.hasNext()){
							Graph.Vertex<Integer> s = zIt.next();
							if(s.getElement() != lowVertex)
								zNeigh.add(s);
						}
						 
						//and then find the intersection of both sets to find vertices common in y and z's neighbourhood
						yNeigh.retainAll(zNeigh); //yNeigh now contains vertices common to both y and z 
						
						//create a diamond and return it
						diamond = new ArrayList<Graph.Vertex<Integer>>();
						diamond.add(graph.getVertexWithElement(lowVertex));diamond.add(y);diamond.add(z);diamond.add((Graph.Vertex<Integer>) yNeigh.get(0));
						break here;
					}
				}
			}
		}
		return diamond;
	}
	
	/**
	 * method that checks if a graph contains a diamond subgraph. It works by removing all low
	 * degree vertices from the graph and then applying phaseOne method to the resulting graph
	 * @param graph					the graph to be checked
	 * @param lowDegreeVertices 	a list of low degree vertices
	 * @return						a diamond subgraph if found
	 */
	public  List<Graph.Vertex<Integer>> phaseThree(UndirectedGraph<Integer,Integer> graph2, List<Graph.Vertex<Integer>> lowDegreeVertices){
		//remove low degree vertices from graph G
		
		UndirectedGraph<Integer,Integer> graph = graph2.clone();
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			graph.removeVertex(graph.getVertexWithElement(v.getElement()));
		}
		
		List<Graph.Vertex<Integer>> diamond = null;
		//look for a diamond in graph using the method listed in phase 1 applied on all vertices 
		//of the graph
		
		//get the vertices into a list
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		List<Graph.Vertex<Integer>> vSet = new ArrayList<Graph.Vertex<Integer>>();
		while(vIt.hasNext())
			vSet.add(vIt.next());
		
		//create a map to store results of phase one
		Map<String,Object> phase1Results = phaseOne(vSet, graph);
		
		if(phase1Results.get("diamond")!= null){
			diamond =  (List<Vertex<Integer>>) phase1Results.get("diamond");
		}
		return diamond;
	}
	
	/**
	 * method that checks if a graph is a clique
	 * @param graph 	the graph to be checked
	 * @return 			the result of the check. 
	 */
	private  boolean checkIfClique(UndirectedGraph<Integer,Integer> graph){
		int edgeCount = graph.getEdgeCount();
		int reqCount = (graph.size()*(graph.size()-1))/2;
		if(edgeCount==reqCount){
			return true;
		}
		return false;
	}
	
	/**
	 * method that checks if a P3 is present in a graph and returns the vertices of the P3
	 * @param graph 	the graph to be checked
	 * @return 			the vertex list if found
	 */
	public  List<Graph.Vertex<Integer>> checkP3InComponent(UndirectedGraph<Integer,Integer> graph){
		//if the no of vertices in graph is less than 3, then graph cannot have a p3
		if(graph.size()<3){
			return null;
		}
		
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices(); //gets the vertex iterator
		
		while(vIt.hasNext()){
			Graph.Vertex<Integer> v = vIt.next();
			//do a breadth first search on each vertex of graph in search for a P3
			//stop once a p3 has been found
			List<Graph.Vertex<Integer>> bfVertices = graph.breadthFirstTraversal(v);
			Graph.Vertex<Integer> v1 = bfVertices.get(0);
			Graph.Vertex<Integer> v2 = bfVertices.get(1);
			Graph.Vertex<Integer> v3 = bfVertices.get(2);
			
			if(!(graph.containsEdge(v1, v2) && graph.containsEdge(v2, v3) &&
					graph.containsEdge(v1, v3))){
				List<Graph.Vertex<Integer>> vert = new ArrayList<Graph.Vertex<Integer>>();
				vert.add(v1); vert.add(v2); vert.add(v3);
				return vert;
			}
		}
	
		return null;
	}
	
	/**
	 * method that checks if a P3 is present in a graph and returns the vertices of the P3
	 * @param graph 	the graph to be checked
	 * @return 			the vertex list if found
	 */
//	private List<Graph.Vertex<Integer>> checkP3InComponent(UndirectedGraph<Integer,Integer> graph){
//		//if the no of vertices in graph is less than 3, then graph cannot have a p3
//		
//		if(graph.size()<3){
//			return null;
//		}
//		
//		//create mapping between matrix indices and vertex
//		List<Graph.Vertex<Integer>> vertexIndexMap = new ArrayList<Graph.Vertex<Integer>>();
//		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices(); //gets the vertex iterator
//		while(vIt.hasNext()){
//			vertexIndexMap.add(vIt.next());
//		}
//		
//		int[][] A = graph.getAdjacencyMatrix();
//		int[][] A2;
//		try {
//			A2 = Utility.multiplyMatrix(A, A);
//		} catch (MatrixException e) {
//			return null;
//		}
//		
//		//look for a path of length 2 in A2
//		for(int i=0; i<A2.length;i++){
//			for(int j=i+1; j<A2.length;j++){
//				if((int)A2[i][j]>0 && (int)A[i][j]==0){ //then a P3 is found
//					//look for third vertex
//					for(int k=0; k<A.length;k++){
//						if(k!=i && k!= j && ((int)A[k][i] == 1) && ((int)A[k][j] == 1)){
//							List<Graph.Vertex<Integer>> vert = new ArrayList<Graph.Vertex<Integer>>();
//							vert.add(vertexIndexMap.get(i)); vert.add(vertexIndexMap.get(j)); vert.add(vertexIndexMap.get(k));
//							return vert;
//						}
//					}
//				}
//			}
//		}
//	
//		return null;
//	}
	
	//method to partition the vertices into low degree vertices and high degree vertices
	public  List<Graph.Vertex<Integer>>[] partitionVertices(UndirectedGraph<Integer,Integer> graph){
		List<Graph.Vertex<Integer>>[] vertices = new List[2];
		vertices[0] = new ArrayList<Graph.Vertex<Integer>>();
		vertices[1] = new ArrayList<Graph.Vertex<Integer>>();
		
		//get vertices
		Iterator<Graph.Vertex<Integer>> vertexIterator = graph.vertices();
		
		//get number of edges
		int noOfEdges = graph.getEdgeCount();
		

		//calculate D for Vertex partitioning
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
	
	public  String getResult(){
		String result = String.format("%-10s%-10s%-10s%-10s", p1time,p2time,p3time,found);
		return result;
	}

}
