import java.util.*;

import Jama.Matrix;

public class ListSimplicialVertices {
	
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
		
		
		graph.mapVertexToId();
		
		Set[] verticesPartition = DetectDiamond.partitionVertices(graph);
		
		Set<Vertex> lowDegreeVertices = verticesPartition[0];
		Set<Vertex> highDegreeVertices = verticesPartition[1];
		
//		System.out.print("Low degree vertices: ");
//		for(Vertex v: lowDegreeVertices)
//			System.out.print(v.getElement()+",");
//		System.out.println();
//		
//		System.out.print("High degree vertices: ");
//		for(Vertex v: highDegreeVertices)
//			System.out.print(v.getElement()+",");
//		System.out.println();
		
		Map phase1Results = phaseOne(graph, lowDegreeVertices);
		if((boolean)phase1Results.get("simplicialFound")){
			System.out.println("Simplicial vertices found");
			List<Vertex> vs = (List)phase1Results.get("simplicialVertices");
			for(Vertex v: vs){
				System.out.print(v.getElement()+"," );
			}
			System.out.println();
		}
		
		List<Vertex> markedVertices = phaseTwo(graph, highDegreeVertices);
		phaseThree(graph, lowDegreeVertices);
		Map phase4Results = phaseFour(graph, markedVertices);
		
		if((boolean)phase4Results.get("simplicialFound")){
			System.out.println("Simplicial vertices found");
			List<Vertex> vs = (List)phase4Results.get("simplicialVertices");
			for(Vertex v: vs){
				System.out.print(v.getElement()+"," );
			}
			System.out.println();
		}
	}
	
	public static Map phaseOne(UndirectedGraph graph, Set<Vertex> lowDegreeVertices){
		Map results = new HashMap();
		
		boolean simplicialFound = false;
		List<Vertex> simplicialVertices = new ArrayList<Vertex>();
		
		for(Vertex v: lowDegreeVertices){
			if(graph.degree(v) > 0){
				//get the neighbours of v
				Iterable<Vertex> vNeigh1 = (Iterable<Vertex>) graph.neighbours(v);
				Iterable<Vertex> vNeigh2 = (Iterable<Vertex>) graph.neighbours(v);
				
				boolean isSimplicial = true;
				
				for(Vertex one: vNeigh1){
					if(!isSimplicial)
						break;
					for(Vertex two: vNeigh2){
						if(!one.equals(two)){ //prevent checking if a vertex has an edge with itself
							if(!graph.containsEdge(one, two)){
								isSimplicial = false;
								break;
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
	
	public static List<Vertex> phaseTwo(UndirectedGraph graph, Set<Vertex> highDegreeVertices){
		List<Vertex> markedVertices = new ArrayList<Vertex>();
		
		for(Vertex v: highDegreeVertices){
			Iterable<Vertex> vNeigh = (Iterable<Vertex>)graph.neighbours(v);
			boolean allContained = true;;
			for(Vertex vv: vNeigh){
				if(!highDegreeVertices.contains(vv)){
					markedVertices.add(v);
					break;
				}
			}	
		}
		
		return markedVertices;
	}
	
	public static void phaseThree(UndirectedGraph graph, Set<Vertex> lowDegreeVertices){
		//remove all low degree vertices from graph
		for(Vertex v: lowDegreeVertices){
			graph.removeVertex(v);
		}	
	}

	public static Map phaseFour(UndirectedGraph graph, List<Vertex> markedVertices){
//		Map phaseFourResults = new HashMap();
//		
//		double[][] adj = graph.getAdjacencyMatrix();
//		
//		//put 1's on the diagonal
//		for(int i=0;i<adj.length;i++){
//			adj[i][i] = 1;
//		}
//		
//		//square the resulting adjacency matrix
//		Matrix A = new Matrix(adj);
//		Matrix aSquared = A.times(A);
//		
//		aSquared.print(3, 0);
		
		//get vertices of the new graph
		Set<Vertex> gSet = new HashSet<Vertex>();
		for(Vertex v: (Iterable<Vertex>)graph.vertices()){
			gSet.add(v);
		}
		
		//Remove vertices which have been marked from the set
		for(Vertex v: markedVertices){
			if(gSet.contains(v)){
				gSet.remove(v);
			}
		}
		
		//get simplicial vertices
		Map phase1Results = phaseOne(graph, gSet);	
		return phase1Results;
		
		
	}
}
