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
		
		Set[] verticesPartition = partitionVertices(graph);
		
		Set<Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		Set<Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		System.out.println("Printing low degree vertices degrees");
		for(Vertex v: lowDegreeVertices){
			System.out.println("Vertex: "+ v.getElement() + ", degree: " + graph.degree(v));
		}
		
		System.out.println("Printing high degree vertices degrees");
		for(Vertex v: highDegreeVertices){
			System.out.println("Vertex: "+ v.getElement() + ", degree: " + graph.degree(v));
		}
		
		
		System.out.println("Testing neighbourhood graph");
		UndirectedGraph graph2 = getNeighbourGraph(graph, v1);
		
		printGraph(graph2);
		
		//print out components
		System.out.println("Getting components graph formed by the neighbourhood of v1");
		List<UndirectedGraph> graph2Comps = getComponents(graph2);
		for(int i=0; i<graph2Comps.size(); i++){
			System.out.println("Printing out component: "+(i+1));
			printGraph(graph2Comps.get(i));
			System.out.println();
		}
		
		
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
		double alpha = 2.376;
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
		
		System.out.println("Printing result of depth first traversal");
		List<Vertex> compList2 = graph.depthFirstTraversal(vertices.get(0));
		//System.out.println(graph.containsEdge(vertices.get(0), vertices.get(1)));
		for(Vertex v: compList2){
			System.out.print(v.getElement()+",");
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
			System.out.print("{"+edge.source.getElement()+", "+ edge.destination.getElement()+"},");
		}
		System.out.println();
	}
}
