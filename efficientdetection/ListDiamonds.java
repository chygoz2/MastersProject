package efficientdetection;
import java.util.*;

import general.Graph;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;
import java.io.*;

/**
 * class that detects a diamond in a graph if any and returns it
 * @author Chigozie Ekwonu
 *
 */
public class ListDiamonds {
	private static String time = "";
	
	public static void main(String [] args) throws IOException{
//		String fileName = "generated_graphs\\size_150\\graph_150_0.7_4.txt";
//		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
//		
//		Graph.Vertex<Integer> v1 = graph.addVertex(0);
//		Graph.Vertex<Integer> v2 = graph.addVertex(1);
//		Graph.Vertex<Integer> v3 = graph.addVertex(2);
//		Graph.Vertex<Integer> v4 = graph.addVertex(3);
//		Graph.Vertex<Integer> v5 = graph.addVertex(4);
//		graph.addEdge(v1, v2);
//		graph.addEdge(v3, v2);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v1, v3);
//		graph.addEdge(v1, v5);
//		graph.addEdge(v3, v5);
		
		
//		while(true){
		UndirectedGraph<Integer,Integer> graph;
		for(int a=0;a<1;a++){
//			String fileName = "matrix2.txt";
//			String fileName = "generated_graphs\\size_5\\graph_5_0.7_4.txt";
//			String fileName = "generated_graphs\\size_6\\graph_6_0.6_3.txt";
//			String fileName = "test\\testdata\\diamondtestdata.txt";
			String fileName = "generated_graphs\\size_300\\graph_300_0.9_1.txt";
//			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
//			String fileName = "test\\testdata\\diamondtestdata.txt";
//			UndirectedGraph<Integer,Integer> graphs[a] = Utility.makeGraphFromFile(fileName);
			graph = Utility.makeGraphFromFile(fileName);
			List<Collection<Graph.Vertex<Integer>>> diamonds = detect(graph);
//			
//			if(diamond!=null){
//				Utility.printGraph(diamond);
//			}
//			else{
//				System.out.println("Diamond not found");
//				//Utility.printGraph(graph);
//			}
			
//			int[][] A = {{0,1,1,0,0},{1,0,1,0,1},{1,1,0,1,1,},{0,0,1,0,1},{0,1,1,1,0}};
//			graph=Utility.makeGraphFromAdjacencyMatrix(A);
//			getP3(graph);
			
			if(!diamonds.isEmpty())
				time+="1";
			else
				time+="0";
			
			for(Collection<Graph.Vertex<Integer>> d: diamonds){
				Utility.printGraph(Utility.makeGraphFromVertexSet(graph, d));
			}
			System.out.println(time);
			System.out.println("No of diamonds found = "+diamonds.size());
			resetTime();
		}
		
	}
	
	/**
	 * method to detect and return all induced subgraphs isomorphic with a diamond
	 * @param graph 		the graph to be checked
	 * @return  			the list of sets of vertices which induce diamonds
	 */
	public static List<Collection<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		time+="size_"+graph.size()+"_";
		//list of diamond vertices found
		List<Collection<Graph.Vertex<Integer>>> diamonds = new ArrayList<Collection<Graph.Vertex<Integer>>>(); 
		
		//partition graph vertices into low and high degree vertices
		List[] verticesPartition = Utility.partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		//phase one
		long starttime = System.currentTimeMillis();
		Map phase1Results = phaseOne(lowDegreeVertices, graph);
		long stoptime = System.currentTimeMillis();
		
		List<Collection<Graph.Vertex<Integer>>> p1diamonds = (List<Collection<Vertex<Integer>>>) phase1Results.get("diamonds");
		diamonds.addAll(p1diamonds);
		time += "phase1("+(stoptime-starttime)+")_";
		
		//phase two
		Map cli = (HashMap)phase1Results.get("cliques");
		starttime = System.currentTimeMillis();
		List<Collection<Graph.Vertex<Integer>>> p2diamonds = phaseTwo(cli,graph);
		stoptime = System.currentTimeMillis();
		diamonds.addAll(p2diamonds);
		time += "phase2("+(stoptime-starttime)+")_";
		
		//phase three
		starttime = System.currentTimeMillis();
		List<Collection<Graph.Vertex<Integer>>> p3diamonds = phaseThree(graph, lowDegreeVertices);
		stoptime = System.currentTimeMillis();
		diamonds.addAll(p3diamonds);
		time += "phase3("+(stoptime-starttime)+")_";
		
		return diamonds;
	}
	
	/**
	 * method that checks a graph for a diamond by checking if the diamond has a low degree vertex in it
	 * @param lowDegreeVertices 	a list of low degree vertices of the graph
	 * @param graph 				the graph to be checked
	 * @return 						a map object containing a diamond if found, as well as a collection of cliques mapped to each low degree
	 * 								vertex
	 */
	public static Map phaseOne(Collection<Graph.Vertex<Integer>> lowDegreeVertices, UndirectedGraph<Integer,Integer> graph){
		
		Map phaseOneResults = new HashMap(); //map for storing the results of the phase
		List<Collection<Graph.Vertex<Integer>>> diamonds = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		List<Set<Integer>> seen = new ArrayList<Set<Integer>>(); //to prevent creating the same diamond more than once
		
		//create a map for storing cliques and which vertex's neighbourhood they are in
		Map<Integer, List<UndirectedGraph<Integer,Integer>>> vertexCliques = new HashMap<Integer, List<UndirectedGraph<Integer,Integer>>>();
		
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
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
					List<Collection<Graph.Vertex<Integer>>> p3s = getP3(graphC);
					
					if(!p3s.isEmpty()){
						//if a P3 is found, then the graph contains a diamond
						for(Collection<Graph.Vertex<Integer>> p3: p3s){
							//produce diamonds found 
							p3.add(v);
							Set<Integer> diamondListElem = new HashSet<Integer>(); //set to store diamond vertices elements
							for(Graph.Vertex<Integer> vv: p3){
								diamondListElem.add(vv.getElement());
							}
							
							//check in the seen list for an entry that contains all 4 vertex elements
							boolean contains = false;
							
							for(Set<Integer> s: seen){
								if(s.containsAll(diamondListElem)){
									contains = true;
									break;
								}
							}
							
							//get the subgraph induced by the vertex set comprising of the P3 vertices and the current low degree vertex
							if(!contains){
								UndirectedGraph<Integer,Integer> diamond = Utility.makeGraphFromVertexSet(graph, p3);
								diamonds.add(p3);
								seen.add(diamondListElem);
							}
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
	
	/**
	 * method that checks for the presence of a diamond subgraph in a graph.
	 * Method works by checking each clique of each low degree vertex to see if the condition
	 * described by Kloks et al. is satisfied.
	 * @param cliques 		the mapping of cliques to low degree vertices
	 * @param graph 		the graph to be checked
	 * @return 				a diamond subgraph if found
	 */
	public static List<Collection<Graph.Vertex<Integer>>> phaseTwo(Map cliques, UndirectedGraph<Integer,Integer> graph){
		List<Collection<Graph.Vertex<Integer>>> diamonds = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		List<Set<Integer>> seen = new ArrayList<Set<Integer>>(); //to prevent creating the same diamond more than once
		
		//get adjacency matrix of graph
		int[][] A = graph.getAdjacencyMatrix();
		int[][] squareA;
		try {
			squareA = Utility.multiplyMatrix(A, A);
		} catch (MatrixException e) {
			return diamonds;
		}

		Set<Integer> vertexKeys = cliques.keySet();

		for(Integer lowVertex: vertexKeys){
//			System.out.println("low vertex is "+lowVertex);
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
					
					//get elements (adjacency matrix indices) of y and z
					int yId = (int) y.getElement();
					int zId = (int) z.getElement();
					
					//perform check 
					if(squareA[yId][zId] > cliq.size()-1){ //then y and z have a common neighbor
						//outside the closed neighbourhood of x and hence a diamond can found
						//get y's and z's neighbours from the main graph and put them in a list
						Iterator<Graph.Vertex<Integer>> yIt = graph.neighbours(graph.getVertexWithElement(yId));
						List<Graph.Vertex<Integer>> commNeigh = new ArrayList<Graph.Vertex<Integer>>(); //list of vertices
											//adjacent to y and z, excluding x
						while(yIt.hasNext()){
							Graph.Vertex<Integer> s = yIt.next();
							if(s.getElement() != lowVertex){
								Iterator<Graph.Vertex<Integer>> zIt = graph.neighbours(graph.getVertexWithElement(zId));
								while(zIt.hasNext()){
									Graph.Vertex<Integer> t = zIt.next();
									if(t.getElement() != lowVertex)
										if(s.getElement()==t.getElement()){
											commNeigh.add(s);
										}
								}
							}
						}
						
						Set<Integer> diamondListElem = new HashSet<Integer>(); //set to store diamond vertices elements
						
						//add diamond vertices from x,y,z and each vertex in the common neighbours list
						for(Graph.Vertex<Integer> c: commNeigh){
							List<Graph.Vertex<Integer>> diamond = new ArrayList<Graph.Vertex<Integer>>();
							diamond.add(graph.getVertexWithElement(lowVertex));
							diamondListElem.add(graph.getVertexWithElement(lowVertex).getElement());
							diamond.add(y);	diamond.add(z); diamond.add(c);
							diamondListElem.add(y.getElement()); diamondListElem.add(z.getElement());
							diamondListElem.add(c.getElement());
							
							//check in the marked list for an entry that contains all 4 vertex elements
							boolean contains = false;
							
							for(Set<Integer> s: seen){
								if(s.containsAll(diamondListElem)){
									contains = true;
									break;
								}
							}
							
							if(!contains){
								diamonds.add(diamond);
								seen.add(diamondListElem);
							}
						}
					}
				}
			}
		}
		return diamonds;
	}
	
	/**
	 * method that checks if a graph contains a diamond subgraph. It works by removing all low
	 * degree vertices from the graph and then applying phaseOne method to the resulting graph
	 * @param graph					the graph to be checked
	 * @param lowDegreeVertices 	a list of low degree vertices
	 * @return						a diamond subgraph if found
	 */
	public static List<Collection<Graph.Vertex<Integer>>> phaseThree(UndirectedGraph<Integer,Integer> graph2, List<Graph.Vertex<Integer>> lowDegreeVertices){
		
		//remove low degree vertices from graph G
		UndirectedGraph<Integer,Integer> graph = graph2.clone();
		
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			graph.removeVertex(graph.getVertexWithElement(v.getElement()));
		}
		
		List<Collection<Graph.Vertex<Integer>>> diamonds = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		
		//look for diamonds in graph using the method listed in phase 1 applied on all vertices 
		//of the graph
		
		//get the vertices into a list
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		List<Graph.Vertex<Integer>> vSet = new ArrayList<Graph.Vertex<Integer>>();
		while(vIt.hasNext())
			vSet.add(vIt.next());
		
		//create a map to store results of phase one
		Map phase1Results = phaseOne(vSet, graph);
		List<Collection<Graph.Vertex<Integer>>> p1diamonds = (List<Collection<Vertex<Integer>>>) phase1Results.get("diamonds");
		if(!p1diamonds.isEmpty()){
			diamonds.addAll(p1diamonds);
		}
		return diamonds;
	}
	
	/**
	 * method to return a report of the time taken to execute the detection
	 * @return		the report string
	 */
	public static String getTime(){
		return time;
	}
	
	/**
	 * method to reset the report string
	 */
	public static void resetTime(){
		time = "";
	}
	
	/**
	 * method that checks if a graph is a clique
	 * @param graph 	the graph to be checked
	 * @return 			the result of the check. 
	 */
	private static boolean checkIfClique(UndirectedGraph<Integer,Integer> graph){
		int[][] A = graph.getAdjacencyMatrix();
		
		for(int i=0; i<A.length; i++){
			for(int j=i+1; j<A.length; j++){
				if(A[i][j] != 1)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * method that lists all P3s in a graph
	 * @param graph2 	the graph to be checked
	 * @return 			the P3 list if found
	 */
	public static List<Collection<Graph.Vertex<Integer>>> getP3(UndirectedGraph<Integer,Integer> graph2){
		List<Collection<Graph.Vertex<Integer>>> p3s = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		List<Set<Integer>> seen = new ArrayList<Set<Integer>>(); //to prevent creating the same P3 more than once
		UndirectedGraph<Integer,Integer> graph = graph2.clone();
		
		//sort the vertices in non increasing order of degree
		List<Graph.Vertex<Integer>> vertices = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext())
			vertices.add(vIt.next());
		Collections.sort(vertices, new VertexComparator(graph));
		
		//get P3 list via a modified form of Chiba et al.'s triangle listing algorithm
		for(int i=0; i<vertices.size(); i++){
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
					if(!marked2.contains(w) && !w.equals(v)){
						List<Graph.Vertex<Integer>> p3 = new ArrayList<Graph.Vertex<Integer>>();
						Set<Integer> p3ListElem = new HashSet<Integer>(); //set to store P3 vertices elements
						
						p3.add(v); p3.add(w); p3.add(u);
						p3ListElem.add(v.getElement()); p3ListElem.add(w.getElement()); p3ListElem.add(u.getElement());
						
						//check in the marked list for an entry that contains all 3 vertex elements
						boolean contains = false;
						
						for(Set<Integer> s: seen){
							if(s.containsAll(p3ListElem)){
								contains = true;
								break;
							}
						}
						
						if(!contains){
//							System.out.print(v.getElement()+","+u.getElement()+","+w.getElement());
//							System.out.println();
							p3s.add(p3);
							seen.add(p3ListElem);
	//						Utility.printGraph(Utility.makeGraphFromVertexSet(graph2, p3));
						}
					}	
				}
			}
		}
		
		return p3s;
		
	}
	
	private static class VertexComparator implements Comparator<Graph.Vertex<Integer>>{
		
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
