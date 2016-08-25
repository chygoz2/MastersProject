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
	private  String found = "found";
	
	public static void main(String [] args) throws IOException{
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListDiamonds d = new ListDiamonds();
			List<List<Vertex<Integer>>> diamonds = d.detect(graph);
			System.out.println("Number of diamonds found: "+diamonds.size());
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
		if(diamonds.isEmpty()){
			found = "not found";
		}
		return diamonds;
	}
	
	public List<List<Graph.Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph){
		//list of diamond vertices found
		List<List<Graph.Vertex<Integer>>> diamonds = new ArrayList<List<Graph.Vertex<Integer>>>(); 
		
		//partition graph vertices into low and high degree vertices
		List[] verticesPartition = partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		//phase one
		long starttime = System.currentTimeMillis();
		Map phase1Results = phaseOne(graph);
		long stoptime = System.currentTimeMillis();
		
		List<List<Graph.Vertex<Integer>>> p1diamonds = (List<List<Vertex<Integer>>>) phase1Results.get("diamonds");
		diamonds.addAll(p1diamonds);
//		
//		//phase two
//		Map cli = (HashMap)phase1Results.get("cliques");
//		starttime = System.currentTimeMillis();
//		List<Collection<Graph.Vertex<Integer>>> p2diamonds = phaseTwo(cli,graph);
//		stoptime = System.currentTimeMillis();
//		diamonds.addAll(p2diamonds);
//		time += "phase2("+(stoptime-starttime)+")_";
//		
//		//phase three
//		starttime = System.currentTimeMillis();
//		List<Collection<Graph.Vertex<Integer>>> p3diamonds = phaseThree(graph, lowDegreeVertices);
//		stoptime = System.currentTimeMillis();
//		diamonds.addAll(p3diamonds);
//		time += "phase3("+(stoptime-starttime)+")_";
		
		return diamonds;
	}
	
	/**
	 * method that checks a graph for a diamond by checking if the diamond has a low degree vertex in it
	 * @param lowDegreeVertices 	a list of low degree vertices of the graph
	 * @param graph 				the graph to be checked
	 * @return 						a map object containing a diamond if found, as well as a collection of cliques mapped to each low degree
	 * 								vertex
	 */
	public Map phaseOne(UndirectedGraph<Integer,Integer> graph){
		
		Map phaseOneResults = new HashMap(); //map for storing the results of the phase
		List<List<Graph.Vertex<Integer>>> diamonds = new ArrayList<List<Graph.Vertex<Integer>>>();
		Set<Set<Integer>> seen = new HashSet<Set<Integer>>(); //to prevent creating the same diamond more than once
		
		//create a map for storing cliques and which vertex's neighbourhood they are in
		Map<Integer, List<UndirectedGraph<Integer,Integer>>> vertexCliques = new HashMap<Integer, List<UndirectedGraph<Integer,Integer>>>();
		
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		while(vertices.hasNext()){
			Graph.Vertex<Integer> v = vertices.next();

			//get the neighbourhood graph of vertex v
			UndirectedGraph<Integer,Integer> graph2 = Utility.getNeighbourGraph(graph, v);
			
			//Create list for storing cliques in the neighbourhood of vertex v
			List<UndirectedGraph<Integer,Integer>> cliques = new ArrayList<UndirectedGraph<Integer,Integer>>();
			
			List<UndirectedGraph<Integer,Integer>> graph2Comps = Utility.getComponents(graph2); //get components of the induced neighbour graph
			
			//check if each component is a clique
			for(UndirectedGraph<Integer,Integer> graphC: graph2Comps){
				boolean isClique = checkIfClique(graphC);
				if(isClique){
					cliques.add(graphC); //add component to cliques list to be used in phase 2
				}else{
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
			
			vertexCliques.put(v.getElement(), cliques); //map cliques found to the low degree vertex
		}
		phaseOneResults.put("diamonds", diamonds);
		phaseOneResults.put("cliques", vertexCliques);
		return phaseOneResults;
		
	}
	
//	/**
//	 * method that checks for the presence of a diamond subgraph in a graph.
//	 * Method works by checking each clique of each low degree vertex to see if the condition
//	 * described by Kloks et al. is satisfied.
//	 * @param cliques 		the mapping of cliques to low degree vertices
//	 * @param graph 		the graph to be checked
//	 * @return 				a diamond subgraph if found
//	 */
//	public List<Collection<Graph.Vertex<Integer>>> phaseTwo(Map cliques, UndirectedGraph<Integer,Integer> graph){
//		List<Collection<Graph.Vertex<Integer>>> diamonds = new ArrayList<Collection<Graph.Vertex<Integer>>>();
//		Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same diamond more than once
//		
//		//get adjacency matrix of graph
//		int[][] A = graph.getAdjacencyMatrix();
//		int[][] squareA;
//		try {
//			squareA = Utility.multiplyMatrix(A, A);
//		} catch (MatrixException e) {
//			return diamonds;
//		}
//
//		Set<Integer> vertexKeys = cliques.keySet();
//
//		for(Integer lowVertex: vertexKeys){
////			System.out.println("low vertex is "+lowVertex);
//			//for each low degree vertex, get its cliques
//			List<UndirectedGraph<Integer,Integer>> cliqs = (List<UndirectedGraph<Integer,Integer>>)cliques.get(lowVertex);
//			for(UndirectedGraph<Integer,Integer> cliq: cliqs){
//				//for each clique, perform the check
//				Iterator<Graph.Edge<Integer>> edgeIt = cliq.edges();
//				while(edgeIt.hasNext()){
//					//get pair in clique(as vertices at edges)
//					UndirectedGraph.UnEdge ee = (UndirectedGraph.UnEdge)edgeIt.next();
//					Graph.Vertex<Integer> y = (UndirectedGraph.UnVertex) ee.getSource();
//					Graph.Vertex<Integer> z = (UndirectedGraph.UnVertex) ee.getDestination();
//					
//					//get elements (adjacency matrix indices) of y and z
//					int yId = y.getElement();
//					int zId = z.getElement();
//					
//					//perform check 
//					if(squareA[yId][zId] > cliq.size()-1){ //then y and z have a common neighbor
//						//outside the closed neighbourhood of x and hence a diamond can found
//						//get y's and z's neighbours from the main graph and put them in a list
//						Iterator<Graph.Vertex<Integer>> yIt = graph.neighbours(graph.getVertexWithElement(yId));
//						List<Graph.Vertex<Integer>> commNeigh = new ArrayList<Graph.Vertex<Integer>>(); //list of vertices
//											//adjacent to y and z, excluding x
//						while(yIt.hasNext()){
//							Graph.Vertex<Integer> s = yIt.next();
//							if(s.getElement() != lowVertex){
//								Iterator<Graph.Vertex<Integer>> zIt = graph.neighbours(graph.getVertexWithElement(zId));
//								while(zIt.hasNext()){
//									Graph.Vertex<Integer> t = zIt.next();
//									if(t.getElement() != lowVertex)
//										if(s.getElement()==t.getElement()){
//											commNeigh.add(s);
//											break;
//										}
//								}
//							}
//						}
//						
//						Set<Integer> diamondListElem = new HashSet<Integer>(); //set to store diamond vertices elements
//						
//						//add diamond vertices from x,y,z and each vertex in the common neighbours list
//						for(Graph.Vertex<Integer> c: commNeigh){
//							List<Graph.Vertex<Integer>> diamond = new ArrayList<Graph.Vertex<Integer>>();
//							diamond.add(graph.getVertexWithElement(lowVertex));
//							diamondListElem.add(graph.getVertexWithElement(lowVertex).getElement());
//							diamond.add(y);	diamond.add(z); diamond.add(c);
//							diamondListElem.add(y.getElement()); diamondListElem.add(z.getElement());
//							diamondListElem.add(c.getElement());
//							
//							//check in the marked list for an entry that contains all 4 vertex elements
//							boolean contains = marked.add(diamondListElem);
//							
//							if(contains){
//								diamonds.add(diamond);
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		return diamonds;
//	}
	
//	/**
//	 * method that checks if a graph contains a diamond subgraph. It works by removing all low
//	 * degree vertices from the graph and then applying phaseOne method to the resulting graph
//	 * @param graph					the graph to be checked
//	 * @param lowDegreeVertices 	a list of low degree vertices
//	 * @return						a diamond subgraph if found
//	 */
//	public List<Collection<Graph.Vertex<Integer>>> phaseThree(UndirectedGraph<Integer,Integer> graph2, List<Graph.Vertex<Integer>> lowDegreeVertices){
//		
//		//remove low degree vertices from graph G
//		UndirectedGraph<Integer,Integer> graph = graph2.clone();
//		
//		for(Graph.Vertex<Integer> v: lowDegreeVertices){
//			graph.removeVertex(graph.getVertexWithElement(v.getElement()));
//		}
//		
//		//look for diamonds in graph using the method listed in phase 1 applied on all vertices 
//		//of the graph
//		
//		//get the vertices into a list
//		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
//		List<Graph.Vertex<Integer>> vSet = new ArrayList<Graph.Vertex<Integer>>();
//		while(vIt.hasNext())
//			vSet.add(vIt.next());
//		
//		//create a map to store results of phase one
//		Map phase1Results = phaseOne(vSet, graph);
//		List<Collection<Graph.Vertex<Integer>>> diamonds = (List<Collection<Vertex<Integer>>>) phase1Results.get("diamonds");
//		
//		return diamonds;
//	}
	
	
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
	
//	private class VertexComparator implements Comparator<Graph.Vertex<Integer>>{
//		
//		private UndirectedGraph<Integer,Integer> graph;
//		
//		public VertexComparator(UndirectedGraph<Integer,Integer> g){
//			this.graph = g;
//		}
//
//		@Override
//		public int compare(Graph.Vertex<Integer> v1, Graph.Vertex<Integer> v2) {
//			Integer d1 = graph.degree(v1);
//			Integer d2 = graph.degree(v2);
//			
//			return -1 *(d1.compareTo(d2));
//		}
//	}
	
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
	*	and whether a diamond was found or not
	*/
	public String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
}
