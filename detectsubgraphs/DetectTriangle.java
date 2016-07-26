package detectsubgraphs;
import java.util.*;

import general.Graph;
import general.MatrixOperation;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class DetectTriangle {
	
	private static String time = "";
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph;
//		for(int a=0;a<15;a++){
//			String fileName = "matrix2.txt";
//			String fileName = "generated_graphs\\size_7\\graph_7_0.2_2.txt";
//			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
//			graph = Utility.makeGraphFromFile(fileName);
			int[][] A = {{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1},{1,0,1,0,0},{1,0,1,0,0}};
			graph = Utility.makeGraphFromAdjacencyMatrix(A);
			
			UndirectedGraph<Integer,Integer> triangle = detect(graph);
			
			if(triangle!=null)
				Utility.printGraph(triangle);
			else{
				System.out.println("Triangle not found");
			}

	}
	
	public static UndirectedGraph<Integer,Integer> detect(UndirectedGraph<Integer,Integer> graph){
		UndirectedGraph<Integer,Integer> triangle = null;
		
		List[] verticesPartition = Utility.partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		
		long starttime = System.currentTimeMillis();
		triangle = phaseOne(graph, lowDegreeVertices);
		long stoptime = System.currentTimeMillis();
		time += "phase1("+(stoptime-starttime)+")_";
		
		if(triangle==null){
			starttime = System.currentTimeMillis();
			triangle = phaseTwo(graph, lowDegreeVertices);
			stoptime = System.currentTimeMillis();
			time += "phase2("+(stoptime-starttime)+")_";
		}
		
		if(triangle!=null)
			time+="1";
		else
			time+="0";
		System.out.println(time);
		DetectTriangle.resetTime();
		
		return triangle;
	}
	
	public static UndirectedGraph<Integer,Integer> phaseOne(UndirectedGraph<Integer, Integer> graph, List<Graph.Vertex<Integer>> lowDegreeVertices){
		UndirectedGraph<Integer,Integer> triangle = null;
		
		List<List<Graph.Vertex<Integer>>> paths = new ArrayList<List<Graph.Vertex<Integer>>>();
		
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			List<Graph.Vertex<Integer>> path = customBFS(v,graph);
			if(path!=null){
				Graph.Vertex<Integer> first = path.get(1);
				Graph.Vertex<Integer> last = path.get(2);
				if(graph.containsEdge(first, last)){
					triangle = Utility.makeGraphFromVertexSet(graph, path);
					return triangle;
				}
			}
		}

		return null;
	}
	
	public static UndirectedGraph<Integer,Integer> phaseTwo(UndirectedGraph<Integer,Integer> graph, List<Graph.Vertex<Integer>> lowDegreeVertices){
		
		//if no triangle was found
		//remove all low degree vertices and get the adjacency matrix of resulting graph. 
		//Then detect a triangle from the square of the adjacency matrix
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			graph.removeVertex(v);
		}
		
		UndirectedGraph<Integer,Integer> triangle = null;
		
		//get the adjacency matrix
		double[][] A = graph.getAdjacencyMatrix();
		double[][] aSquared = null; 
		try{
			aSquared = MatrixOperation.multiply(A, A);
		}catch(RuntimeException e){
			return null;
		}
		
		//create mapping of matrix index to graph vertex
		List<Graph.Vertex<Integer>> vertexIndexMap = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext()){
			vertexIndexMap.add(vIt.next());
		}
		
		//look for end vertices of a triangle from the square of the adjacency matrix
		here:
			for(int i=0; i<aSquared.length; i++){
				for(int j=i+1; j<aSquared.length; j++){
					if((int)aSquared[i][j]>0 && (int)A[i][j]==1){ //end vertices found
						//look for the intermediate index to make up the P3
						for(int k=0; k<A.length; k++){
							if(k!=i && k!=j && (int)A[k][i]==1 && (int)A[k][j]==1){
								//at this point, i, j and k represent matrix indices of the vertices which form the triangle
								//get the actual vertices and create a list of them
								List<Graph.Vertex<Integer>> tVertices = new ArrayList<Graph.Vertex<Integer>>();
								tVertices.add(vertexIndexMap.get(i));
								tVertices.add(vertexIndexMap.get(j));
								tVertices.add(vertexIndexMap.get(k));
								
								triangle = Utility.makeGraphFromVertexSet(graph, tVertices);
								break here;
							}
						}
					}
				}
			}
		
		return triangle;
	}
	
	public static List<Graph.Vertex<Integer>> customBFS(Graph.Vertex<Integer> start, UndirectedGraph<Integer,Integer> graph){
		Queue<Graph.Vertex<Integer>> vertexQueue = new LinkedList<Graph.Vertex<Integer>>();
		List<Graph.Vertex<Integer>> list = new ArrayList<Graph.Vertex<Integer>>();
		List<Graph.Vertex<Integer>> visited = new ArrayList<Graph.Vertex<Integer>>();
		
		vertexQueue.add(start);
		visited.add(start);
		
		while(!vertexQueue.isEmpty()){
			Graph.Vertex<Integer> v = vertexQueue.remove();
			list.add(v);
			if(list.size()==3)
				return list;
			//get vertex v's neighbours
			Iterator<Graph.Vertex<Integer>> vNeighbours = graph.neighbours(v);
			while(vNeighbours.hasNext()){
				Graph.Vertex<Integer> w = vNeighbours.next();
				if(!visited.contains(w)){
					vertexQueue.add(w);
					visited.add(w);
				}
			}
			
		}
		
		return null;
	}
	
//	public static UndirectedGraph<Integer,Integer> detect(UndirectedGraph<Integer, Integer> graph){
//		UndirectedGraph<Integer,Integer> triangle = null;
//		
//		List[] verticesPartition = Utility.partitionVertices(graph);
//		
//		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
//		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
//		
//		List<List<Graph.Vertex<Integer>>> paths = new ArrayList<List<Graph.Vertex<Integer>>>();
//		for(Graph.Vertex<Integer> v: lowDegreeVertices){
//			List<Graph.Vertex<Integer>> path = customBFS(v,graph);
//			if(path!=null){
//				Graph.Vertex<Integer> first = path.get(1);
//				Graph.Vertex<Integer> last = path.get(2);
//				if(graph.containsEdge(first, last)){
//					triangle = Utility.makeGraphFromVertexSet(graph, path);
//					return triangle;
//				}
//			}
//		}
//		
//		for(Graph.Vertex<Integer> v: highDegreeVertices){
//			List<Graph.Vertex<Integer>> path = customBFS(v,graph);
//			Graph.Vertex<Integer> first = path.get(1);
//			Graph.Vertex<Integer> last = path.get(2);
//			
//			if(highDegreeVertices.contains(first) && highDegreeVertices.contains(last)){
//				if(graph.containsEdge(first, last)){
//					triangle = Utility.makeGraphFromVertexSet(graph, path);
//					return triangle;
//				}
//			}
//		}
//		
//		return null;
//	}
	
	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
}
