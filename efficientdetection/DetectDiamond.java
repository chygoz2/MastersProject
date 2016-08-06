package efficientdetection;
import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import java.io.*;

public class DetectDiamond {
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
			String fileName = "generated_graphs\\size_5\\graph_5_0.7_4.txt";
//			String fileName = "generated_graphs\\size_6\\graph_6_0.6_3.txt";
//			String fileName = "test\\testdata\\diamondtestdata.txt";
//			UndirectedGraph<Integer,Integer> graphs[a] = Utility.makeGraphFromFile(fileName);
			graph = Utility.makeGraphFromFile(fileName);
			UndirectedGraph<Integer,Integer> diamond = detect(graph);
			if(diamond!=null)
				Utility.printGraph(diamond);
			else{
				System.out.println("Diamond not found");
				//Utility.printGraph(graph);
			}
		}
			//String input = (new BufferedReader(new InputStreamReader(System.in))).readLine();
//		}
	}
	
	public static UndirectedGraph<Integer,Integer> detect(UndirectedGraph<Integer,Integer> graph){
		
//		UndirectedGraph graph = Utility.makeGraphFromFile("genmatrix.txt");
//		String fileName = "generated_graphs\\size_5\\graph_5_0.7_4.txt";
		time+="size_"+graph.size()+"_";
//		Utility.printGraph(graph);
//		UndirectedGraph graph = Utility.makeRandomGraph(10, 0.4);
		
		List[] verticesPartition = Utility.partitionVertices(graph);
		
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		long starttime = System.currentTimeMillis();
		Map phase1Results = DetectDiamond.phaseOne(lowDegreeVertices, graph);
		long stoptime = System.currentTimeMillis();
		time += "phase1("+(stoptime-starttime)+")_";
		
		Map cli = (HashMap)phase1Results.get("cliques");
		
		UndirectedGraph<Integer,Integer> diamond = (UndirectedGraph<Integer,Integer>)phase1Results.get("diamond");
		if(diamond==null){	
			starttime = System.currentTimeMillis();
			diamond = DetectDiamond.phaseTwo(cli, graph);
			stoptime = System.currentTimeMillis();
			time += "phase2("+(stoptime-starttime)+")_";
			
			if(diamond==null){
				starttime = System.currentTimeMillis();
				diamond = DetectDiamond.phaseThree(graph, lowDegreeVertices);
				stoptime = System.currentTimeMillis();
				time += "phase3("+(stoptime-starttime)+")_";
				
			}
		}
		if(diamond!=null)
			time+="1";
		else
			time+="0";
		System.out.println(time);
		DetectDiamond.resetTime();
		return diamond;
	}
	
	public static Map phaseOne(Collection<Graph.Vertex<Integer>> lowDegreeVertices, UndirectedGraph<Integer,Integer> graph){
		
		Map phaseOneResults = new HashMap();
		//boolean diamondFound = false;
		
		//create a map for storing cliques and which vertex's neighbourhood they are in
		Map<Integer, List<UndirectedGraph<Integer,Integer>>> vertexCliques = new HashMap<Integer, List<UndirectedGraph<Integer,Integer>>>();
		
		here:
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
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
						//if has P3 is true, then the graph contains a diamond
						
						//produce one such diamond 
						if(phaseOneResults.get("diamond") == null){ //check if a diamond was previously stored
							resultList.add(v);
							UndirectedGraph<Integer,Integer> result = Utility.makeGraphFromVertexSet(graph, resultList);
							phaseOneResults.put("diamond", result);
							break here;
						}
					}
				}
			}
			
			vertexCliques.put(v.getElement(), cliques);
		}
		
		phaseOneResults.put("cliques", vertexCliques);
		return phaseOneResults;
		
	}
	
	public static UndirectedGraph<Integer,Integer> phaseTwo(Map cliques, UndirectedGraph<Integer,Integer> graph){
		UndirectedGraph<Integer,Integer> diamond = null;
		
		//get adjacency matrix of graph
		double[][] A = graph.getAdjacencyMatrix();
		double[][] squareA;
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
						List<Graph.Vertex<Integer>> dList = new ArrayList<Graph.Vertex<Integer>>();
						dList.add(graph.getVertexWithElement(lowVertex));dList.add(y);dList.add(z);dList.add((Graph.Vertex) yNeigh.get(0));
						diamond = Utility.makeGraphFromVertexSet(graph, dList);
						break here;
					}
				}
			}
		}
		return diamond;
	}
	
	
	public static UndirectedGraph<Integer,Integer> phaseThree(UndirectedGraph<Integer,Integer> graph, List<Graph.Vertex<Integer>> lowDegreeVertices){
		//remove low degree vertices from graph G
		
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			graph.removeVertex(graph.getVertexWithElement(v.getElement()));
		}
		
		UndirectedGraph<Integer,Integer> diamond = null;
		//look for a diamond in graph using the method listed in phase 1 applied on all vertices 
		//of the graph
		
		//get the vertices into a list
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		List<Graph.Vertex<Integer>> vSet = new ArrayList<Graph.Vertex<Integer>>();
		while(vIt.hasNext())
			vSet.add(vIt.next());
		
		//create a map to store results of phase one
		Map phase1Results = phaseOne(vSet, graph);
		
		if(phase1Results.get("diamond")!= null){
			diamond =  (UndirectedGraph<Integer,Integer>)phase1Results.get("diamond");
		}
		return diamond;
	}
	
	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
	
	public static boolean checkIfClique(UndirectedGraph<Integer,Integer> graph){
		double[][] A = graph.getAdjacencyMatrix();
		
		for(int i=0; i<A.length; i++){
			for(int j=i+1; j<A.length; j++){
				if(A[i][j] != 1)
					return false;
			}
		}
		return true;
	}
	
	//checks if a P3 exists in component and returns one
//	public static Map checkP3InComponent(UndirectedGraph graph){
//		//if the no of vertices in graph is less than 3, then graph cannot have a p3
//		Map result = new HashMap();
//		if(graph.size()<3){
//			result.put("hasP3", false);
//			return result;
//		}
//		
//		Iterator<Graph.Vertex> vIt = graph.vertices(); //gets the vertex iterator
//		
//		while(vIt.hasNext()){
//			Graph.Vertex v = vIt.next();
//			//do a breadth first search on each vertex of graph in search for a P3
//			//stop once a p3 has been found
//			List<Graph.Vertex> bfVertices = graph.breadthFirstTraversal(v);
//			Graph.Vertex v1 = bfVertices.get(0);
//			Graph.Vertex v2 = bfVertices.get(1);
//			Graph.Vertex v3 = bfVertices.get(2);
//			
//			if(!(graph.containsEdge(v1, v2) && graph.containsEdge(v2, v3) &&
//					graph.containsEdge(v1, v3))){
//				result.put("hasP3", true);
//				List<Graph.Vertex> vert = new ArrayList<Graph.Vertex>();
//				vert.add(v1); vert.add(v2); vert.add(v3);
//				result.put("p3Graph", Utility.makeGraphFromVertexSet(graph, vert));
//				break;
//			}
//		}
//	
//		return result;
//	}
	
	public static List<Graph.Vertex<Integer>> checkP3InComponent(UndirectedGraph<Integer,Integer> graph){
		//if the no of vertices in graph is less than 3, then graph cannot have a p3
		
		if(graph.size()<3){
			return null;
		}
		
		//create mapping between matrix indices and vertex
		List<Graph.Vertex<Integer>> vertexIndexMap = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices(); //gets the vertex iterator
		while(vIt.hasNext()){
			vertexIndexMap.add(vIt.next());
		}
		
		double[][] A = graph.getAdjacencyMatrix();
		double[][] A2;
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
							vert.add(vertexIndexMap.get(i)); vert.add(vertexIndexMap.get(j)); vert.add(vertexIndexMap.get(k));
							return vert;
						}
					}
				}
			}
		}
	
		return null;
	}
	
}
