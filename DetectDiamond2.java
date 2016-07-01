import java.util.*;

import Jama.Matrix;

public class DetectDiamond2 {
	public static void main(String [] args){
		AdjacencyListGraph graph = new AdjacencyListGraph(10);
		
		graph.addVertex(1);
		graph.addVertex(3);
		graph.addVertex(4);
		graph.addVertex(2);
		graph.addVertex(5);
		graph.addEdge(1, 2);
		graph.addEdge(3, 2);
		graph.addEdge(3, 4);
		graph.addEdge(1, 3);
		graph.addEdge(1, 5);
		graph.addEdge(3, 5);
		
		graph.mapVertexToId();
		
		Set[] verticesPartition = partitionVertices(graph);
		
		Set<Integer> lowDegreeVertices = verticesPartition[0];
		Set<Integer> highDegreeVertices = verticesPartition[1];
		
		System.out.println("Printing low degree vertices degrees");
		for(Integer v: lowDegreeVertices){
			System.out.println("Integer: "+ v.getElement() + ", degree: " + graph.degree(v));
		}
		
		System.out.println("Printing high degree vertices degrees");
		for(Integer v: highDegreeVertices){
			System.out.println("Integer: "+ v.getElement() + ", degree: " + graph.degree(v));
		}
		
		System.out.println();
		//////////////////////////////////////////////////////////////////
		//Get find components of graphs in the neighbourhood of each of the low degree vertices.
		//also find the cliques in the process
		
		Map phase1Results = phaseOne(lowDegreeVertices, graph);
		System.out.println(phase1Results.get("diamondFound"));
		Map cli = (HashMap)phase1Results.get("cliques");
		for(Object g: cli.keySet()){
			Collection c = (Collection)cli.get(g);
			for(Object z: c){
				System.out.println("Printing out cliques");
				printGraph((AdjacencyListGraph)z);
				System.out.println();
			}	
		}
		
		if((boolean)phase1Results.get("diamondFound")){
			System.out.println("Print out one such diamond");
			printGraph((AdjacencyListGraph)phase1Results.get("diamond"));
		} else{
			//////////////////////////////////////////////////////////////////
						
			Map phase2Results = phaseTwo(cli, graph);
			
			if((boolean)phase2Results.get("diamondFound")){
				System.out.println("Print out one such diamond");
				printGraph((AdjacencyListGraph)phase2Results.get("diamond"));
			}else{
				//phase three
				phaseThree(graph, lowDegreeVertices);
				
				Map phase4Results = phaseFour(graph);
				
				if((boolean)phase4Results.get("diamondFound")){
					System.out.println("Print out one such diamond");
					printGraph((AdjacencyListGraph)phase4Results.get("diamond"));
				}
			}
		}
		
		phaseThree(graph, lowDegreeVertices);
	}
	
	public static Map phaseOne(Set<Integer> lowDegreeVertices, AdjacencyListGraph graph){
		System.out.println("In Phase One");
		
		Map phaseOneResults = new HashMap();
		boolean diamondFound = false;
		
		//create a map for storing cliques and which vertex's neighbourhood they are in
		Map vertexCliques = new HashMap();
		
		for(Integer v: lowDegreeVertices){
			AdjacencyListGraph graph2 = getNeighbourGraph(graph, v);
			//Create list for storing cliques in the neighbourhood of vertex v
			List<AdjacencyListGraph> cliques = new ArrayList<AdjacencyListGraph>();
			
			//System.out.println("Getting components graph formed by the neighbourhood of v");
			List<AdjacencyListGraph> graph2Comps = getComponents(graph2); //get components of the induced neighbour graph

			//check if each component is a clique
			for(AdjacencyListGraph graphC: graph2Comps){
				boolean isClique = checkIfClique(graphC);
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
							AdjacencyListGraph dTemp = (AdjacencyListGraph)result.get("p3Graph");
							Iterator<Integer> dIt = dTemp.vertices();
							Integer nVertex = dTemp.addVertex(v.getElement());
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
	
	public static Map phaseTwo(Map cliques, AdjacencyListGraph graph){
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
		Set<Integer> vertexKeys = cliques.keySet();
		for(Integer lowVertex: vertexKeys){
			if(diamondFound)
				break;
			//for each low degree vertex, get its cliques
			List<AdjacencyListGraph> cliqs = (List<AdjacencyListGraph>)cliques.get(lowVertex);
			for(AdjacencyListGraph cliq: cliqs){
				if(diamondFound)
					break;
				//for each clique, perform the check
				Iterator<Edge> edgeIt = cliq.edges();
				while(edgeIt.hasNext()){
					if(diamondFound)
						break;
					//get pair in clique(as vertices at edges)
					AdjacencyListGraph.UnEdge ee = (AdjacencyListGraph.UnEdge)edgeIt.next();
					AdjacencyListGraph.UnVertex y = (AdjacencyListGraph.UnVertex) ee.getSource();
					AdjacencyListGraph.UnVertex z = (AdjacencyListGraph.UnVertex) ee.getDestination();
					
					//get ids of y and z
					int yId = y.getId();
					int zId = z.getId();
					
					//perform check 
					if(squareA.get(yId, zId) > cliq.order()-1){ //then y and z have a common neighbor
						//outside the closed neighbourhood of x and hence a diamond can found
						//get y's and z's neighbours from the main graph and put them in a set
						Iterator yIt = graph.neighbours(graph.getVertexWithId(y.getId()));
						List yNeigh = new ArrayList<Integer>();
						while(yIt.hasNext()){
							yNeigh.add(yIt.next());
						}
						
						Iterator zIt = graph.neighbours(graph.getVertexWithId(z.getId()));
						List zNeigh = new ArrayList<Integer>();
						while(zIt.hasNext()){
							zNeigh.add(zIt.next());
						}
						
//						//remove y from z's neighbourhood and remove z from y's neighbourhood
//						yNeigh.remove(graph.getVertexWithId(z.getId()));
//						zNeigh.remove(graph.getVertexWithId(y.getId()));
						
						 
						//and then find the union of both sets to find vertices common in y and z's neighbourhood
						yNeigh.retainAll(zNeigh); //yNeigh now contains vertices common to both y and z 
						//remove x from the neighbourhood 
						yNeigh.remove(graph.getVertexWithId(((AdjacencyListGraph.UnVertex)lowVertex).getId()));
				
						
						//create a diamond and return it
						List<Integer> dList = new ArrayList<Integer>();
						dList.add(lowVertex);dList.add(y);dList.add(z);dList.add((Integer) yNeigh.get(0));
						AdjacencyListGraph diamond = makeGraphFromVertexSet(graph, dList);
						phaseTwoResults.put("diamond", diamond);
						diamondFound = true;
					}
				}
			}
		}
		phaseTwoResults.put("diamondFound", diamondFound);
		return phaseTwoResults;
	}
	
	
	public static void phaseThree(AdjacencyListGraph graph, Set<Integer> lowDegreeVertices){
		//remove low degree vertices from graph G
		for(Integer v: lowDegreeVertices){
			graph.removeVertex(v);
		}
	}
	
	public static Map phaseFour(AdjacencyListGraph graph){
		System.out.println("In Phase Four");
		//look for a diamond in graph using the method listed in phase 1 applied on all vertices 
		//of the graph
		
		//get the vertices into a set
		Iterator vIt = graph.vertices();
		Set<Integer> vSet = new HashSet<Integer>();
		while(vIt.hasNext())
			vSet.add((Integer)vIt.next());
		
		//create a map to store results of phase four
		Map results = new HashMap();
		Map phase1Results = phaseOne(vSet, graph);
		
		boolean diamondFound = (boolean)phase1Results.get("diamondFound");
		results.put("diamondFound", diamondFound);
		if(diamondFound){
			results.put("diamond", (AdjacencyListGraph)phase1Results.get("diamond"));
		}
		
		return results;
	}
	
	//method to partition the vertices into low degree vertices and high degree vertices
	public static Set[] partitionVertices(AdjacencyListGraph graph){
		Set[] vertices = new Set[2];
		vertices[0] = new HashSet<Integer>();
		vertices[1] = new HashSet<Integer>();
		
		//get vertices
		Iterator<Integer> vertexIterator = graph.vertices();
		
		//get edges
		Iterator<Edge> edgeIterator = graph.edges();
		
		//get number of edges
		int noOfEdges = 0;
		while(edgeIterator.hasNext()){
			edgeIterator.next();
			noOfEdges++;
		}
		

		//calculate D for vertex partitioning
		double alpha = 2.376; //constant from Coppersmith-Winograd matrix multiplication algorithm
		double pow = (alpha-1)/(alpha+1);
		double D = Math.pow(noOfEdges, pow);
		
		while(vertexIterator.hasNext()){
			Integer<Integer> v = vertexIterator.next();
			if(graph.degree(v)>D)
				vertices[1].add(v);
			else
				vertices[0].add(v);
		}
		
		return vertices;
	}
	
	//method to create subgraph in the neighbourhood of vertex v
	public static AdjacencyListGraph getNeighbourGraph(AdjacencyListGraph graph, Integer v){
		//get v's neighbours;
		List<Integer> neighbours = new ArrayList<Integer>();
		Iterator<Integer> it = graph.neighbours(v);
		while(it.hasNext()){
			neighbours.add(it.next());
		}
		
		return makeGraphFromVertexSet(graph, neighbours);
	}
	
	
	public static List<AdjacencyListGraph> getComponents(AdjacencyListGraph graph){
		List<AdjacencyListGraph> components = new ArrayList<AdjacencyListGraph>();
		//get vertices list
		List<Integer> vertices = new ArrayList<Integer>();
		Iterator<Integer> it = graph.vertices();
		while(it.hasNext()){
			vertices.add(it.next());
		}
		
		//find components
		while(!vertices.isEmpty()){
			List<Integer> compList = graph.depthFirstTraversal(vertices.get(0));
			components.add(makeGraphFromVertexSet(graph, compList));
			vertices.removeAll(compList);
		}
		
		return components;
	}
	
	public static AdjacencyListGraph makeGraphFromVertexSet(AdjacencyListGraph graph, List<Integer> vertices){
		AdjacencyListGraph g1 = new AdjacencyListGraph();
		
		//add the vertices
		for(Integer ve: vertices){
			AdjacencyListGraph.UnVertex v = (AdjacencyListGraph.UnVertex)ve;
			int id = v.getId();
			AdjacencyListGraph.UnVertex v1 = (AdjacencyListGraph.UnVertex)g1.addVertex(ve.getElement());
			g1.setVertexId(v1,id);
		}		
		
		Iterator vIt = g1.vertices();
		while(vIt.hasNext()){
			Iterator vIt2 = g1.vertices();
			AdjacencyListGraph.UnVertex one = (AdjacencyListGraph.UnVertex)vIt.next();
			while(vIt2.hasNext()){
				AdjacencyListGraph.UnVertex two = (AdjacencyListGraph.UnVertex)vIt2.next();
				AdjacencyListGraph.UnVertex nOne = (AdjacencyListGraph.UnVertex)graph.getVertexWithId(one.getId());
				AdjacencyListGraph.UnVertex nTwo = (AdjacencyListGraph.UnVertex)graph.getVertexWithId(two.getId());
				if(graph.containsEdge(nOne, nTwo)){
					if(!g1.containsEdge(one,two)){
						AdjacencyListGraph.UnEdge e = (AdjacencyListGraph.UnEdge) g1.addEdge(one, two);
					}
				}
			}
		}
		
		return g1;
	}
	
	public static void printGraph(AdjacencyListGraph graph2){
		System.out.println("Graph size is "+graph2.order());
		Iterator<Integer> it = graph2.vertices();
		
		System.out.println("Graph vertices");
		while (it.hasNext()){
			Integer<Integer> v = it.next();
			System.out.print(v.getElement()+", ");
		}
		System.out.println("\nGraph edges:");
		Iterator<Edge> it2 = graph2.edges();
		while (it2.hasNext()){
			AdjacencyListGraph.UnEdge edge = (AdjacencyListGraph.UnEdge)it2.next();
			System.out.print("{"+edge.getSource().getElement()+", "+ edge.getDestination().getElement()+"},");
		}
		System.out.println();
	}
	
	public static boolean checkIfClique(AdjacencyListGraph graph){
		double[][] A = graph.getAdjacencyMatrix();
		
		for(int i=0; i<A.length; i++){
			for(int j=0; j<A.length; j++){
				if(i!=j && A[i][j] != 1)
					return false;
			}
		}
		return true;
	}
	
	//checks if a P3 exists in component and returns one
	public static Map checkP3InComponent(AdjacencyListGraph graph){
		//if the no of vertices in graph is less than 3, then graph cannot have a p3
		Map result = new HashMap();
		if(graph.order()<3){
			result.put("hasP3", false);
			return result;
		}
		
		Iterator<Integer> vIt = graph.vertices(); //gets the vertex iterator
		
		while(vIt.hasNext()){
			Integer v = vIt.next();
			//do a breadth first search on each vertex of graph in search for a P3
			//stop once a p3 has been found
			List<Integer> bfVertices = graph.breadthFirstTraversal(v);
			Integer v1 = bfVertices.get(0);
			Integer v2 = bfVertices.get(1);
			Integer v3 = bfVertices.get(2);
			
			if(!(graph.containsEdge(v1, v2) && graph.containsEdge(v2, v3) &&
					graph.containsEdge(v1, v3))){
				result.put("hasP3", true);
				List<Integer> vert = new ArrayList<Integer>();
				vert.add(v1); vert.add(v2); vert.add(v3);
				result.put("p3Graph", makeGraphFromVertexSet(graph, vert));
				break;
			}
		}
	
		return result;
	}
	
}
