package detectsubgraphs;
import java.util.*;

import general.Graph;
import general.MatrixOperation;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class DetectK4 {
	
	private static String time = "";
	
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
		//graph.addEdge(v3, v5);
		graph.addEdge(v2, v5);
		graph.addEdge(v4, v5);
		graph.addEdge(v2, v4);
		graph.addEdge(v1, v4);
		
		Utility.saveGraphToFile(graph, 1.0, 1);
		UndirectedGraph<Integer,Integer> k4 = detect(graph);
		if(k4!=null)
			Utility.printGraph(k4);
		//graph.mapVertexToId();
		
		
		
	}
	
	public static UndirectedGraph<Integer,Integer> detect(UndirectedGraph<Integer,Integer> graph){
		time+="size_"+graph.size()+"_";
		
		List<Graph.Vertex<Integer>>[] verticesPartition = Utility.partitionVertices(graph);
		
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		UndirectedGraph<Integer,Integer> k4 = null;
		long starttime = System.currentTimeMillis();
		k4 = phaseOne(graph, highDegreeVertices);
		long stoptime = System.currentTimeMillis();
		time += "phase1("+(stoptime-starttime)+")_";
		if(k4==null){
			starttime = System.currentTimeMillis();
			k4 = phaseTwo(graph, lowDegreeVertices);
			stoptime = System.currentTimeMillis();
			time += "phase2("+(stoptime-starttime)+")_";
		}
		
		if(k4!=null)
			time+="1";
		else
			time+="0";
		//System.out.println(time);
		DetectK4.resetTime();
		return k4;
	}
	
	public static UndirectedGraph<Integer,Integer> phaseOne(UndirectedGraph graph, Collection<Graph.Vertex<Integer>> highDegreeVertices){
		UndirectedGraph<Integer,Integer> k4 = null;
		List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>();
		
		here:
			
		for(Graph.Vertex x: highDegreeVertices){
			//get x's neighbourhood graph
			Iterator<Graph.Vertex> nXIter = graph.neighbours(x);
			List<Graph.Vertex<Integer>> nXList = new ArrayList<Graph.Vertex<Integer>>();

			//get the intersection of the neighbourhood vertices of x with the high degree vertices
			while(nXIter.hasNext()){
				Graph.Vertex v = nXIter.next();
				if(highDegreeVertices.contains(v))
					nXList.add(v);
			}
			
			//if x's neighbourhood does not have any high degree vertex as its neighbour, then continue
			if(nXList.isEmpty())
				continue;
			
			//make graph from new list of vertices
			UndirectedGraph graph2 = Utility.makeGraphFromVertexSet(graph, nXList);
			double [][] adjMatrix = graph2.getAdjacencyMatrix();
			
			//get square of adjacency matrix
			double[][] adjMatrixSquare;
			try {
				adjMatrixSquare = MatrixOperation.multiply(adjMatrix, adjMatrix);
			} catch (MatrixException e) {
				continue;
			}
			
			//create map of indices to vertex
			List<Graph.Vertex<Integer>> indexVertexMap = new ArrayList<Graph.Vertex<Integer>>();
			Iterator<Graph.Vertex<Integer>> vIt = graph2.vertices();
			while(vIt.hasNext()){
				Graph.Vertex<Integer> v = vIt.next();
				indexVertexMap.add(v);
			}
			
			//look for a triangle in the adjacency matrix
			for(int j=0; j<adjMatrix.length; j++){
				for(int k=0; k<adjMatrix.length; k++){
					if(j!=k && (int)adjMatrix[j][k]==1 && (int)adjMatrixSquare[j][k]>0){
						//triangle found with j and k as indices of two ends of the triangle.
						//find the third one
						
						for(int m=0; m<adjMatrix.length; m++){
							if(m!=j && m!= k && ((int)adjMatrix[m][j] == 1) && ((int)adjMatrix[m][k] == 1)){
								//third vertex index found.
								//get the vertex that corresponds to all 3 indices
								
								k4Vertices.add(x);
								k4Vertices.add(indexVertexMap.get(j));
								k4Vertices.add(indexVertexMap.get(k));
								k4Vertices.add(indexVertexMap.get(m));
								
								k4 = Utility.makeGraphFromVertexSet(graph, k4Vertices);
								break here;
								
							}
						}
					}
				}
			}
		}
		
		return k4;
	}
	
	public static UndirectedGraph<Integer,Integer> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		UndirectedGraph<Integer,Integer> k4 = null;
		List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>();
		
		here:
		for(Graph.Vertex x: lowDegreeVertices){
			//get x's neighbourhood graph
			Iterator<Graph.Vertex<Integer>> nXIter = graph.neighbours(x);
			List<Graph.Vertex<Integer>> nXList = new ArrayList<Graph.Vertex<Integer>>();

			//make neighbourhood graph of x from list of vertices
			while(nXIter.hasNext()){
				nXList.add(nXIter.next());
			}
			
			//if a vertex is isolated, then it won't have any neighbours
			if(nXList.isEmpty())
				continue;
			
			UndirectedGraph<Integer,Integer> graph2 = Utility.makeGraphFromVertexSet(graph, nXList);
			double [][] adjMatrix = graph2.getAdjacencyMatrix();
			
			//get square of adjacency matrix
			double[][] adjMatrixSquare;
			try {
				adjMatrixSquare = MatrixOperation.multiply(adjMatrix, adjMatrix);
			} catch (MatrixException e) {
				continue;
			}
			
			//create map of indices to vertex
			List<Graph.Vertex<Integer>> indexVertexMap = new ArrayList<Graph.Vertex<Integer>>();
			Iterator<Graph.Vertex<Integer>> vIt = graph2.vertices();
			while(vIt.hasNext()){
				Graph.Vertex<Integer> v = vIt.next();
				indexVertexMap.add(v);
			}
			
			//look for a triangle in the adjacency matrix
			for(int j=0; j<adjMatrix.length; j++){
				for(int k=0; k<adjMatrix.length; k++){
					if(j!=k && (int)adjMatrix[j][k]==1 && (int)adjMatrixSquare[j][k]>0){
						//triangle found with j and k as indices of two ends of the triangle.
						//find the third one
						
						for(int m=0; m<adjMatrix.length; m++){
							if(m!=j && m!= k && ((int)adjMatrix[m][j] == 1) && ((int)adjMatrix[m][k] == 1)){
								//third vertex index found.
								//get the vertex that corresponds to all 3 indices
															
								k4Vertices.add(x);
								k4Vertices.add(indexVertexMap.get(j));
								k4Vertices.add(indexVertexMap.get(k));
								k4Vertices.add(indexVertexMap.get(m));
								
								k4 = Utility.makeGraphFromVertexSet(graph, k4Vertices);
								break here;
								
							}
						}
					}
				}
			}
		}
		
		return k4;
	}
	
	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
}
