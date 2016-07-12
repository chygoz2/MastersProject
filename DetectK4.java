import java.util.*;

import Jama.Matrix;

public class DetectK4 {
	public static void main(String [] args){
		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(2);
		Vertex<Integer> v3 = graph.addVertex(3);
		Vertex<Integer> v4 = graph.addVertex(4);
		Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		graph.addEdge(v3, v5);
		graph.addEdge(v2, v5);
		graph.addEdge(v4, v5);
		graph.addEdge(v2, v4);
		graph.addEdge(v1, v4);
		
		
		//graph.mapVertexToId();
		
		Set[] verticesPartition = Utility.partitionVertices(graph);
		
		Set<Vertex> lowDegreeVertices = verticesPartition[0];
		Set<Vertex> highDegreeVertices = verticesPartition[1];
		
		Map phase1Results = phaseOne(graph, highDegreeVertices);
		if((boolean)phase1Results.get("k4Found")){
			UndirectedGraph k4 = (UndirectedGraph)phase1Results.get("k4");
			Utility.printGraph(k4);
		}else{
			Map phase2Results = phaseTwo(graph, lowDegreeVertices);
			if((boolean)phase2Results.get("k4Found")){
				UndirectedGraph k4 = (UndirectedGraph)phase2Results.get("k4");
				Utility.printGraph(k4);
			}
		}
		
	}
	
	public static Map phaseOne(UndirectedGraph graph, Set<Vertex> highDegreeVertices){
		Map phase1Results = new HashMap();
		List<Vertex> k4Vertices = new ArrayList<Vertex>();
		
		here:
			
		for(Vertex x: highDegreeVertices){
			//get x's neighbourhood graph
			Iterator<Vertex> nXIter = graph.neighbours(x);
			List<Vertex> nXList = new ArrayList<Vertex>();

			//get the intersection of the neighbourhood vertices of x with the high degree vertices
			while(nXIter.hasNext()){
				Vertex v = nXIter.next();
				if(highDegreeVertices.contains(v))
					nXList.add(v);
			}
			//make graph from new list of vertices
			UndirectedGraph graph2 = Utility.makeGraphFromVertexSet(graph, nXList);
			double [][] adj = graph2.getAdjacencyMatrix();
			Matrix adjMatrix = new Matrix(adj);
			
			//get square of adjacency matrix
			Matrix adjMatrixSquare = adjMatrix.times(adjMatrix);
			
			//create map of indices to vertex
			List<Vertex> indexVertexMap = new ArrayList<Vertex>();
			Iterable<Vertex> vIt = (Iterable<Vertex>)graph2.vertices();
			for(Vertex v: vIt){
				indexVertexMap.add(v);
			}
			
			//look for a triangle in the adjacency matrix
			for(int j=0; j<adjMatrix.getColumnDimension(); j++){
				for(int k=0; k<adjMatrix.getRowDimension(); k++){
					if(j!=k && adjMatrix.get(j,k)==1 && adjMatrixSquare.get(j,k)>0){
						//triangle found with j and k as indices of two ends of the triangle.
						//find the third one
						
						for(int m=0; m<adjMatrix.getRowDimension(); m++){
							if(m!=j && m!= k && (adjMatrix.get(m, j) == 1) && (adjMatrix.get(m, k) == 1)){
								//third vertex index found.
								//get the vertex that corresponds to all 3 indices
								
								k4Vertices.add(x);
								k4Vertices.add(indexVertexMap.get(j));
								k4Vertices.add(indexVertexMap.get(k));
								k4Vertices.add(indexVertexMap.get(m));
								
								UndirectedGraph k4Graph = Utility.makeGraphFromVertexSet(graph, k4Vertices);
								phase1Results.put("k4", k4Graph);
								break here;
								
							}
						}
					}
				}
			}
		}
		phase1Results.put("k4Found", !k4Vertices.isEmpty());
		return phase1Results;
	}
	
	public static Map phaseTwo(UndirectedGraph graph, Set<Vertex> lowDegreeVertices){
		Map phase2Results = new HashMap();
		List<Vertex> k4Vertices = new ArrayList<Vertex>();
		
		here:
		for(Vertex x: lowDegreeVertices){
			//get x's neighbourhood graph
			Iterable<Vertex> nXIter = (Iterable<Vertex>)graph.neighbours(x);
			List<Vertex> nXList = new ArrayList<Vertex>();

			//make neighbourhood graph of x from list of vertices
			for(Vertex v: nXIter){
				nXList.add(v);
			}
			
			UndirectedGraph graph2 = Utility.makeGraphFromVertexSet(graph, nXList);
			double [][] adj = graph2.getAdjacencyMatrix();
			Matrix adjMatrix = new Matrix(adj);
			
			//get square of adjacency matrix
			Matrix adjMatrixSquare = adjMatrix.times(adjMatrix);
			
			//create map of indices to vertex
			List<Vertex> indexVertexMap = new ArrayList<Vertex>();
			Iterable<Vertex> vIt = (Iterable<Vertex>)graph2.vertices();
			for(Vertex v: vIt){
				indexVertexMap.add(v);
			}
			
			//look for a triangle in the adjacency matrix
			for(int j=0; j<adjMatrix.getColumnDimension(); j++){
				for(int k=0; k<adjMatrix.getRowDimension(); k++){
					if(j!=k && adjMatrix.get(j,k)==1 && adjMatrixSquare.get(j,k)>0){
						//triangle found with j and k as indices of two ends of the triangle.
						//find the third one
						
						for(int m=0; m<adjMatrix.getRowDimension(); m++){
							if(m!=j && m!= k && (adjMatrix.get(m, j) == 1) && (adjMatrix.get(m, k) == 1)){
								//third vertex index found.
								//get the vertex that corresponds to all 3 indices
															
								k4Vertices.add(x);
								k4Vertices.add(indexVertexMap.get(j));
								k4Vertices.add(indexVertexMap.get(k));
								k4Vertices.add(indexVertexMap.get(m));
								
								UndirectedGraph k4Graph = Utility.makeGraphFromVertexSet(graph, k4Vertices);
								phase2Results.put("k4", k4Graph);
								break here;
								
							}
						}
					}
				}
			}
		}
		
		phase2Results.put("k4Found", !k4Vertices.isEmpty());
		return phase2Results;
	}
}
