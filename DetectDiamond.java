import java.util.*;

import Jama.Matrix;

public class DetectDiamond {
	public static void main(String [] args){
//		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
		
//		Vertex<Integer> v1 = graph.addVertex(0);
//		Vertex<Integer> v2 = graph.addVertex(1);
//		Vertex<Integer> v3 = graph.addVertex(2);
//		Vertex<Integer> v4 = graph.addVertex(3);
//		Vertex<Integer> v5 = graph.addVertex(4);
//		graph.addEdge(v1, v2);
//		graph.addEdge(v3, v2);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v1, v3);
//		graph.addEdge(v1, v5);
//		graph.addEdge(v3, v5);
//		
//		graph.mapVertexToId();
		
		UndirectedGraph graph = Utility.makeGraphFromFile("genmatrix.txt");
//		Utility.printGraph(graph);
//		UndirectedGraph graph = Utility.makeRandomGraph(10, 0.4);
		
		Set[] verticesPartition = Utility.partitionVertices(graph);
		
		Set<Vertex> lowDegreeVertices = verticesPartition[0];
		Set<Vertex> highDegreeVertices = verticesPartition[1];
		
		System.out.println("Printing low degree vertices degrees");
		for(Vertex v: lowDegreeVertices){
			System.out.println("Vertex: "+ v.getElement() + ", degree: " + graph.degree(v));
		}
		
		System.out.println("Printing high degree vertices degrees");
		for(Vertex v: highDegreeVertices){
			System.out.println("Vertex: "+ v.getElement() + ", degree: " + graph.degree(v));
		}
		
		System.out.println();
		//////////////////////////////////////////////////////////////////
		//Get find components of graphs in the neighbourhood of each of the low degree vertices.
		//also find the cliques in the process
		
		Map phase1Results = phaseOne(lowDegreeVertices, graph);
//		System.out.println(phase1Results.get("diamondFound"));
		Map cli = (HashMap)phase1Results.get("cliques");
		for(Object g: cli.keySet()){
			Collection c = (Collection)cli.get(g);
			for(Object z: c){
				System.out.println("Printing out cliques");
				Utility.printGraph((UndirectedGraph)z);
				System.out.println();
			}	
		}
		
		if((boolean)phase1Results.get("diamondFound")){
			System.out.println("Print out one such diamond");
			Utility.printGraph((UndirectedGraph)phase1Results.get("diamond"));
		} else{
			//////////////////////////////////////////////////////////////////
						
			Map phase2Results = phaseTwo(cli, graph);
			
			if((boolean)phase2Results.get("diamondFound")){
				System.out.println("Print out one such diamond");
				Utility.printGraph((UndirectedGraph)phase2Results.get("diamond"));
			}else{
				//phase three
				phaseThree(graph, lowDegreeVertices);
				
				Map phase4Results = phaseFour(graph);
				
				if((boolean)phase4Results.get("diamondFound")){
					System.out.println("Print out one such diamond");
					Utility.printGraph((UndirectedGraph)phase4Results.get("diamond"));
				}
			}
		}
		
		phaseThree(graph, lowDegreeVertices);
	}
	
	public static Map phaseOne(Set<Vertex> lowDegreeVertices, UndirectedGraph graph){
		System.out.println("In Phase One");
		
		Map phaseOneResults = new HashMap();
		boolean diamondFound = false;
		
		//create a map for storing cliques and which vertex's neighbourhood they are in
		Map vertexCliques = new HashMap();
		
		for(Vertex v: lowDegreeVertices){
			System.out.println("Low degree vertex is "+v.getElement());
			UndirectedGraph graph2 = Utility.getNeighbourGraph(graph, v);
			
			//Create list for storing cliques in the neighbourhood of vertex v
			List<UndirectedGraph> cliques = new ArrayList<UndirectedGraph>();
			
			//System.out.println("Getting components graph formed by the neighbourhood of v");
			List<UndirectedGraph> graph2Comps = Utility.getComponents(graph2); //get components of the induced neighbour graph
			
			//check if each component is a clique
			for(UndirectedGraph graphC: graph2Comps){
				boolean isClique = checkIfClique(graphC);
				//System.out.println("Is clique? "+isClique);
				if(isClique){
					cliques.add(graphC); //add component to cliques list to be used in phase 2
				}else{
					//if component is not a clique, check if it contains a P3 and thus a diamond
					Map result = checkP3InComponent(graphC);
					boolean hasP3 = (boolean) result.get("hasP3");
					if(hasP3){
						//if has P3 is true, then the graph contains a diamond
						diamondFound = true;
						
						//produce one such diamond 
						if(phaseOneResults.get("diamond") == null){ //check if a diamond was previously stored
							UndirectedGraph dTemp = (UndirectedGraph)result.get("p3Graph");
							Iterator<Vertex> dIt = dTemp.vertices();
							Vertex nVertex = dTemp.addVertex(v.getElement());
							while(dIt.hasNext()){
								dTemp.addEdge(nVertex, dIt.next());
							}
							phaseOneResults.put("diamond", dTemp);
						}
					}
				}
			}
			
			vertexCliques.put(v, cliques);
		}
		
		phaseOneResults.put("diamondFound", diamondFound);
		phaseOneResults.put("cliques", vertexCliques);
		
		return phaseOneResults;
		
	}
	
	public static Map phaseTwo(Map cliques, UndirectedGraph graph){
		System.out.println("In Phase Two");
		
		Map phaseTwoResults = new HashMap();
		boolean diamondFound = false;
		
		//get adjacency matrix of graph
		double[][] aa = graph.getAdjacencyMatrix();
		Matrix A = new Matrix(aa);
		Matrix squareA = A.times(A);
//		System.out.println("Printing A");
//		A.print(3, 0);
//		System.out.println("Printing A squared");
//		squareA.print(3, 0);
		Set<Vertex> vertexKeys = cliques.keySet();
		
		here:
		for(Vertex lowVertex: vertexKeys){
			//for each low degree vertex, get its cliques
			List<UndirectedGraph> cliqs = (List<UndirectedGraph>)cliques.get(lowVertex);
			for(UndirectedGraph cliq: cliqs){
				//for each clique, perform the check
				Iterator<Edge> edgeIt = cliq.edges();
				while(edgeIt.hasNext()){
					//get pair in clique(as vertices at edges)
					UndirectedGraph.UnEdge ee = (UndirectedGraph.UnEdge)edgeIt.next();
					UndirectedGraph.UnVertex y = (UndirectedGraph.UnVertex) ee.getSource();
					UndirectedGraph.UnVertex z = (UndirectedGraph.UnVertex) ee.getDestination();
					
					//get ids of y and z
					int yId = (int) y.getElement();
					int zId = (int) z.getElement();
					
					//perform check 
					if(squareA.get(yId, zId) > cliq.size()-1){ //then y and z have a common neighbor
						//outside the closed neighbourhood of x and hence a diamond can found
						//get y's and z's neighbours from the main graph and put them in a set
						Iterator yIt = graph.neighbours(graph.getVertexWithElement(yId));
						List yNeigh = new ArrayList<Vertex>();
						while(yIt.hasNext()){
							yNeigh.add(yIt.next());
						}
						
						Iterator zIt = graph.neighbours(graph.getVertexWithElement(zId));
						List zNeigh = new ArrayList<Vertex>();
						while(zIt.hasNext()){
							zNeigh.add(zIt.next());
						}
						
//						//remove y from z's neighbourhood and remove z from y's neighbourhood
//						yNeigh.remove(graph.getVertexWithId(z.getId()));
//						zNeigh.remove(graph.getVertexWithId(y.getId()));
						
						 
						//and then find the union of both sets to find vertices common in y and z's neighbourhood
						yNeigh.retainAll(zNeigh); //yNeigh now contains vertices common to both y and z 
						//remove x from the neighbourhood 
						yNeigh.remove(graph.getVertexWithElement((int)lowVertex.getElement()));
				
						
						//create a diamond and return it
						List<Vertex> dList = new ArrayList<Vertex>();
						dList.add(lowVertex);dList.add(y);dList.add(z);dList.add((Vertex) yNeigh.get(0));
						UndirectedGraph diamond = Utility.makeGraphFromVertexSet(graph, dList);
						phaseTwoResults.put("diamond", diamond);
						diamondFound = true;
						break here;
					}
				}
			}
		}
		phaseTwoResults.put("diamondFound", diamondFound);
		return phaseTwoResults;
	}
	
	
	public static void phaseThree(UndirectedGraph graph, Set<Vertex> lowDegreeVertices){
		//remove low degree vertices from graph G
		for(Vertex v: lowDegreeVertices){
			graph.removeVertex(v);
		}
	}
	
	public static Map phaseFour(UndirectedGraph graph){
		System.out.println("In Phase Four");
		//look for a diamond in graph using the method listed in phase 1 applied on all vertices 
		//of the graph
		
		//get the vertices into a set
		Iterator vIt = graph.vertices();
		Set<Vertex> vSet = new HashSet<Vertex>();
		while(vIt.hasNext())
			vSet.add((Vertex)vIt.next());
		
		//create a map to store results of phase four
		Map results = new HashMap();
		Map phase1Results = phaseOne(vSet, graph);
		
		boolean diamondFound = (boolean)phase1Results.get("diamondFound");
		results.put("diamondFound", diamondFound);
		if(diamondFound){
			results.put("diamond", (UndirectedGraph)phase1Results.get("diamond"));
		}
		
		return results;
	}
	
	public static boolean checkIfClique(UndirectedGraph graph){
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
	public static Map checkP3InComponent(UndirectedGraph graph){
		//if the no of vertices in graph is less than 3, then graph cannot have a p3
		Map result = new HashMap();
		if(graph.size()<3){
			result.put("hasP3", false);
			return result;
		}
		
		Iterator<Vertex> vIt = graph.vertices(); //gets the vertex iterator
		
		while(vIt.hasNext()){
			Vertex v = vIt.next();
			//do a breadth first search on each vertex of graph in search for a P3
			//stop once a p3 has been found
			List<Vertex> bfVertices = graph.breadthFirstTraversal(v);
			Vertex v1 = bfVertices.get(0);
			Vertex v2 = bfVertices.get(1);
			Vertex v3 = bfVertices.get(2);
			
			if(!(graph.containsEdge(v1, v2) && graph.containsEdge(v2, v3) &&
					graph.containsEdge(v1, v3))){
				result.put("hasP3", true);
				List<Vertex> vert = new ArrayList<Vertex>();
				vert.add(v1); vert.add(v2); vert.add(v3);
				result.put("p3Graph", Utility.makeGraphFromVertexSet(graph, vert));
				break;
			}
		}
	
		return result;
	}
	
}
