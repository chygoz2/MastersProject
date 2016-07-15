import java.util.*;

import Jama.Matrix;

public class ListSimplicialVertices {
	
	public static void main(String [] args){
		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(2);
		Graph.Vertex<Integer> v3 = graph.addVertex(3);
		Graph.Vertex<Integer> v4 = graph.addVertex(4);
		Graph.Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		graph.addEdge(v3, v5);
		graph.addEdge(v2, v5);
//		graph.addEdge(v2, v4);
//		graph.addEdge(v1, v4);
//		graph.addEdge(v4, v5);
		
		
		//graph.mapVertexToId();
		
		Set[] verticesPartition = Utility.partitionVertices(graph);
		
		Set<Graph.Vertex> lowDegreeVertices = verticesPartition[0];
		Set<Graph.Vertex> highDegreeVertices = verticesPartition[1];
		
//		System.out.print("Low degree vertices: ");
//		for(Graph.Vertex v: lowDegreeVertices)
//			System.out.print(v.getElement()+",");
//		System.out.println();
//		
//		System.out.print("High degree vertices: ");
//		for(Graph.Vertex v: highDegreeVertices)
//			System.out.print(v.getElement()+",");
//		System.out.println();
		
		Map phase1Results = phaseOne(graph, lowDegreeVertices);
		if((boolean)phase1Results.get("simplicialFound")){
			System.out.println("Simplicial vertices found");
			List<Graph.Vertex> vs = (List)phase1Results.get("simplicialVertices");
			for(Graph.Vertex v: vs){
				System.out.print(v.getElement()+"," );
			}
			System.out.println();
		}
		
		List<Graph.Vertex> markedVertices = phaseTwo(graph, highDegreeVertices);
		phaseThree(graph, lowDegreeVertices);
		Map phase4Results = phaseFour(graph, markedVertices);
		
		if((boolean)phase4Results.get("simplicialFound")){
			System.out.println("Simplicial vertices found");
			List<Graph.Vertex> vs = (List)phase4Results.get("simplicialVertices");
			for(Graph.Vertex v: vs){
				System.out.print(v.getElement()+"," );
			}
			System.out.println();
		}
	}
	
	public static Map phaseOne(UndirectedGraph graph, Set<Graph.Vertex> lowDegreeVertices){
		Map results = new HashMap();
		
		boolean simplicialFound = false;
		List<Graph.Vertex> simplicialVertices = new ArrayList<Graph.Vertex>();
		
		for(Graph.Vertex v: lowDegreeVertices){
			if(graph.degree(v) > 0){
				//get the neighbours of v
				Iterator<Graph.Vertex> vNeigh1 = graph.neighbours(v);
				Iterator<Graph.Vertex> vNeigh2 = graph.neighbours(v);
				
				boolean isSimplicial = true;
				
				here:
					while(vNeigh1.hasNext()){
//						if(!isSimplicial)
//							break;
						Graph.Vertex one = vNeigh1.next();
						while(vNeigh2.hasNext()){
							Graph.Vertex two = vNeigh2.next();
							if(!one.equals(two)){ //prevent checking if a vertex has an edge with itself
								if(!graph.containsEdge(one, two)){
									isSimplicial = false;
									break here;
								}
							}
						}
					}
				if(isSimplicial){
					simplicialFound = true;
					simplicialVertices.add(v);
				}
			}
		}
		results.put("simplicialFound", simplicialFound);
		results.put("simplicialVertices", simplicialVertices);
		
		return results;
	}
	
	public static List<Graph.Vertex> phaseTwo(UndirectedGraph graph, Set<Graph.Vertex> highDegreeVertices){
		List<Graph.Vertex> markedVertices = new ArrayList<Graph.Vertex>();
		
		for(Graph.Vertex v: highDegreeVertices){
			Iterator<Graph.Vertex> vNeigh = graph.neighbours(v);
			boolean allContained = true;
			while(vNeigh.hasNext()){
				Graph.Vertex vv = vNeigh.next();
				if(!highDegreeVertices.contains(vv)){
					markedVertices.add(v);
					break;
				}
			}	
		}
		
		return markedVertices;
	}
	
	public static void phaseThree(UndirectedGraph graph, Set<Graph.Vertex> lowDegreeVertices){
		//remove all low degree vertices from graph
		for(Graph.Vertex v: lowDegreeVertices){
			graph.removeVertex(v);
		}	
	}

	public static Map phaseFour(UndirectedGraph graph, List<Graph.Vertex> markedVertices){
		Map phaseFourResults = new HashMap();
		
		//create a map between vertices and matrix indices
		Map<Graph.Vertex, Integer> vertexIndexMap = new HashMap<Graph.Vertex, Integer>();
		int a = 0;
		
		for(Graph.Vertex v: (Iterable<Graph.Vertex>)graph.vertices()){
			vertexIndexMap.put(v, a);
			a++;
		}
		
		double[][] adj = graph.getAdjacencyMatrix();
		
		//put 1's on the diagonal
		for(int i=0;i<adj.length;i++){
			adj[i][i] = 1;
		}
		
		//square the resulting adjacency matrix
		Matrix A = new Matrix(adj);
		Matrix aSquared = A.times(A);
		
		//aSquared.print(3, 0);
		
//		Utility.printGraph(graph);
		
		List<Graph.Vertex> simplicialVertices = new ArrayList<Graph.Vertex>();
		
		for(Graph.Vertex x: (Iterable<Graph.Vertex>)graph.vertices()){
			//get v's neighbours
			//System.out.println("Contains: "+markedVertices.contains(x));
			if(!markedVertices.contains(x)) //do check only on unmarked vertices from phase 2
			{
				Iterator<Graph.Vertex> vNeigh = graph.neighbours(x);
				
				boolean isSimplicial = true;
				
				//perform simplicial vertices check of theorem 1
				while(vNeigh.hasNext()){
					Graph.Vertex y = vNeigh.next();
					double i = aSquared.get(vertexIndexMap.get(x),vertexIndexMap.get(y));
					double j = aSquared.get(vertexIndexMap.get(x),vertexIndexMap.get(x));
					if(Math.abs(i-j)>1e-6){
						isSimplicial = false;
						break;
					}
				}
				
				if(isSimplicial){
					simplicialVertices.add(x);
				}
			}
		}
		
		//get simplicial vertices
		phaseFourResults.put("simplicialFound", !simplicialVertices.isEmpty());
		phaseFourResults.put("simplicialVertices", simplicialVertices);
		
		return phaseFourResults;
		
		
	}
}
