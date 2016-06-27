import java.util.*;

public class DetectDiamond {
	public static void main(String [] args){
		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(3);
		Vertex<Integer> v3 = graph.addVertex(4);
		Vertex<Integer> v4 = graph.addVertex(2);
		Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		graph.addEdge(v3, v5);
		
		Set[] verticesPartition = partitionVertices(graph);
		
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
		
		//////////////////////////////////////////////////////////////////
		//Get find components of graphs in the neighbourhood of each of the low degree vertices.
		//also find the cliques in the process
		
		Map phase1Results = phaseOne(highDegreeVertices, graph);
		System.out.println(phase1Results.get("diamondFound"));
		Map cli = (HashMap)phase1Results.get("cliques");
		for(Object g: cli.keySet()){
			Collection c = (Collection)cli.get(g);
			for(Object z: c){
				System.out.println("Printing out cliques");
				printGraph((UndirectedGraph)z);
				System.out.println();
			}	
		}
		
		System.out.println("Print out one such diamond");
		if((boolean)phase1Results.get("diamondFound")){
			printGraph((UndirectedGraph)phase1Results.get("diamond"));
		}
		
//		System.out.println("Testing neighbourhood graph");
//		UndirectedGraph graph2 = getNeighbourGraph(graph, v1);
//		
//		printGraph(graph2);
//		
//		//print out components
//		System.out.println("Getting components graph formed by the neighbourhood of v1");
//		List<UndirectedGraph> graph2Comps = getComponents(graph2);
//		for(int i=0; i<graph2Comps.size(); i++){
//			System.out.println("Printing out component: "+(i+1));
//			printGraph(graph2Comps.get(i));
//			graph2Comps.get(i).printAdjacencyMatrix();
//			System.out.println("Is component a clique? " + checkIfClique(graph2Comps.get(i)));
//			System.out.println();
//		}
//		
//		System.out.println("Is there a P3 in graph? " + checkP3InComponent(graph));
	}
	
	public static Map phaseOne(Set<Vertex> lowDegreeVertices, UndirectedGraph graph){
		Map phaseOneResults = new HashMap();
		boolean diamondFound = false;
		
		//create a map for storing cliques and which vertex's neighbourhood they are in
		Map vertexCliques = new HashMap();
		
		for(Vertex v: lowDegreeVertices){
			UndirectedGraph graph2 = getNeighbourGraph(graph, v);
			//Create list for storing cliques in the neighbourhood of vertex v
			List<UndirectedGraph> cliques = new ArrayList<UndirectedGraph>();
			
			//System.out.println("Getting components graph formed by the neighbourhood of v");
			List<UndirectedGraph> graph2Comps = getComponents(graph2); //get components of the induced neighbour graph

			//check if each component is a clique
			for(UndirectedGraph graphC: graph2Comps){
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
	
	public static void phaseTwo(List<UndirectedGraph> cliques, UndirectedGraph graph){
		
	}
	
	//method to partition the vertices into low degree vertices and high degree vertices
	public static Set[] partitionVertices(UndirectedGraph graph){
		Set[] vertices = new Set[2];
		vertices[0] = new HashSet<Vertex>();
		vertices[1] = new HashSet<Vertex>();
		
		//get vertices
		Iterator<Vertex> vertexIterator = graph.vertices();
		
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
			Vertex<Integer> v = vertexIterator.next();
			if(graph.degree(v)>D)
				vertices[1].add(v);
			else
				vertices[0].add(v);
		}
		
		return vertices;
	}
	
	//method to create subgraph in the neighbourhood of vertex v
	public static UndirectedGraph getNeighbourGraph(UndirectedGraph graph, Vertex v){
		//get v's neighbours;
		List<Vertex> neighbours = new ArrayList<Vertex>();
		Iterator<Vertex> it = graph.neighbours(v);
		while(it.hasNext()){
			neighbours.add(it.next());
		}
		
		return makeGraphFromVertexSet(graph, neighbours);
	}
	
	
	public static List<UndirectedGraph> getComponents(UndirectedGraph graph){
		List<UndirectedGraph> components = new ArrayList<UndirectedGraph>();
		//get vertices list
		List<Vertex> vertices = new ArrayList<Vertex>();
		Iterator<Vertex> it = graph.vertices();
		while(it.hasNext()){
			vertices.add(it.next());
		}
		
		System.out.println();
		//find components
		while(!vertices.isEmpty()){
			List<Vertex> compList = graph.depthFirstTraversal(vertices.get(0));
			components.add(makeGraphFromVertexSet(graph, compList));
			vertices.removeAll(compList);
		}
		
		return components;
	}
	
	public static UndirectedGraph makeGraphFromVertexSet(UndirectedGraph graph, List<Vertex> vertices){
		UndirectedGraph g1 = new UndirectedGraph();
		Map<Vertex,Vertex> newVertices = new HashMap<Vertex,Vertex>();
		
		//add the vertices
		for(Vertex ve: vertices){
			newVertices.put(ve, g1.addVertex(ve.getElement()));
		}
		
		//add any edges
		for(Vertex one: newVertices.keySet()){
			for(Vertex two: newVertices.keySet()){
				if(graph.containsEdge(one, two))
					if(!g1.containsEdge(newVertices.get(one), newVertices.get(two)))
							g1.addEdge(newVertices.get(one), newVertices.get(two));
			}
		}
		
		return g1;
	}
	
	public static void printGraph(UndirectedGraph graph2){
		System.out.println("Graph size is "+graph2.size());
		Iterator<Vertex> it = graph2.vertices();
		
		System.out.println("Graph vertices");
		while (it.hasNext()){
			Vertex<Integer> v = it.next();
			System.out.print(v.getElement()+", ");
		}
		System.out.println("\nGraph edges:");
		Iterator<Edge> it2 = graph2.edges();
		while (it2.hasNext()){
			UndirectedGraph.UnEdge edge = (UndirectedGraph.UnEdge)it2.next();
			System.out.print("{"+edge.getSource().getElement()+", "+ edge.getDestination().getElement()+"},");
		}
		System.out.println();
	}
	
	public static boolean checkIfClique(UndirectedGraph graph){
		int[][] A = graph.getAdjacencyMatrix();
		
		for(int i=0; i<A.length; i++){
			for(int j=0; j<A.length; j++){
				if(i!=j && A[i][j] != 1)
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
				result.put("p3Graph", makeGraphFromVertexSet(graph, vert));
				break;
			}
		}
	
		return result;
	}
	
}
