package efficientdetection;
import java.util.*;

import general.Graph;
import general.MatrixException;
import general.UndirectedGraph;
import general.Utility;

public class DetectTriangle {
	
	private static String time = "-";
	private static String found = "found";
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph;
//		for(int a=0;a<15;a++){
//			String fileName = "matrix3.txt";
//			String fileName = "generated_graphs\\size_7\\graph_7_0.2_2.txt";
//			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
//			String fileName = "generated_graphs\\size_15\\graph_15_1.0_1.txt";
			String fileName = "test\\testdata\\triangletestdata.txt";
			graph = Utility.makeGraphFromFile(fileName);
//			int[][] A = {{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1},{1,0,1,0,0},{1,0,1,0,0}};
//			graph = Utility.makeGraphFromAdjacencyMatrix(A);
			
			long starttime = System.currentTimeMillis();
			Collection<Graph.Vertex<Integer>> triangle = detect(graph);
//			List<UndirectedGraph<Integer,Integer>> triangles = DetectKL.detect(graph,3);
			long stoptime = System.currentTimeMillis();
			
			long timetaken = stoptime-starttime;
			
			
			if(triangle!=null){
				Utility.printGraph(Utility.makeGraphFromVertexSet(graph, triangle));
				System.out.println("Time taken in milliseconds: "+timetaken);
			}
			else{
				System.out.println("Triangle not found");
			}

	}
	
	public static Collection<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		long starttime = System.currentTimeMillis();
		Collection<Graph.Vertex<Integer>> triangle= find(graph);
		long stoptime = System.currentTimeMillis();
		time = ""+(stoptime-starttime);
		
		if(triangle==null)
			found = "not found";
		System.out.println(getResult());
		resetResult();
		
		return triangle;
	}
	
//	/**
//	 * method that detects the presence of a triangle in a graph via Alon et al.'s method.
//	 * Has complexity of O(m^1.41) where m is the number of edges in the graph
//	 * @param graph
//	 * @return
//	 */
//	public static Collection<Graph.Vertex<Integer>> detect3(UndirectedGraph<Integer,Integer> graph){
//		
//		List<Graph.Vertex<Integer>>[] verticesPartition = Utility.partitionVertices(graph);
//		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
//		
//		long starttime = System.currentTimeMillis();
//		Collection<Graph.Vertex<Integer>> triangle = null;
//		triangle = phaseOne(graph, lowDegreeVertices);
//		long stoptime = System.currentTimeMillis();
//		time += "phase1("+(stoptime-starttime)+")_";
//		if(triangle == null){
//			starttime = System.currentTimeMillis();
//			triangle = phaseTwo(graph, lowDegreeVertices);
//			stoptime = System.currentTimeMillis();
//			time += "phase2("+(stoptime-starttime)+")_";
//		}
//		
//		if(triangle!=null)
//			time+="1";
//		else
//			time+="0";
////		System.out.println(time);
//		DetectTriangle.resetTime();
//		
//		return triangle;
//	}
	
//	/**
//	 * phase one of Alon et al's algorithm. Works by looking for P3s whose intermediate vertex 
//	 * is of low degree and then checking if the endpoints of the P3 are adjacent
//	 * @param graph					the graph to be checked
//	 * @param lowDegreeVertices		the list of low degree vertices
//	 * @return						the triangle vertices if found
//	 */
//	public static Collection<Graph.Vertex<Integer>> phaseOne(UndirectedGraph<Integer, Integer> graph, List<Graph.Vertex<Integer>> lowDegreeVertices){
//		Collection<Graph.Vertex<Integer>> triangle = new ArrayList<Graph.Vertex<Integer>>();
//		
//		for(Graph.Vertex<Integer> v: lowDegreeVertices){
//			//get connecting edges to v
//			Iterator<Graph.Edge<Integer>> eIt = graph.connectingEdges(v);
//			while(eIt.hasNext()){
//				UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge)eIt.next();
//				//get the other vertex of the edge
//				Graph.Vertex<Integer> other;
//				if(e.getSource().equals(v))
//					other = e.getDestination();
//				else
//					other = e.getSource();
//				//get other vertices neighbours
//				Iterator<Graph.Vertex<Integer>> otherIt = graph.neighbours(other);
//				while(otherIt.hasNext()){
//					Graph.Vertex<Integer> next = otherIt.next();
//					//check for presence of edge between v and next
//					//presence of an edge indicates a triangle
//					if(graph.containsEdge(v, next)){
//						triangle.add(v); triangle.add(other); triangle.add(next);
//						return triangle;
//					}
//				}
//			}
//		}
//		return null;
//	}
//	
//	/**
//	 * according to Alon et al., this phase removes all low degree vertices and applies the 
//	 * matrix multiplication method to detect a triangle in a graph 
//	 * @param graph2				the graph to be checked
//	 * @param lowDegreeVertices		the list of low degree vertices
//	 * @return						the triangle vertices if found
//	 */
//	public static Collection<Graph.Vertex<Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph2, List<Graph.Vertex<Integer>> lowDegreeVertices){
//		//if no triangle was found
//		//remove all low degree vertices and get the adjacency matrix of resulting graph. 
//		//Then detect a triangle from the square of the adjacency matrix
//		UndirectedGraph<Integer,Integer> graph = graph2.clone();
//		for(Graph.Vertex<Integer> v: lowDegreeVertices){
//			graph.removeVertex(v);
//		}
//		
//		Collection<Graph.Vertex<Integer>> triangle = detect2(graph);
//		
//		return triangle;
//	}
	
	/**
	 * method that detects triangle via the matrix multiplication method
	 * Has a time complexity of O(n^a) where 2<a<=3 depending on which matrix multiplication
	 * algorithm is used. a is currently 3 as standard matrix multiplication was used 
	 * @param graph 		the graph to be checked
	 * @return				the triangle vertices found if any
	 */
	public static Collection<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){
		//get the adjacency matrix
		int[][] A = graph.getAdjacencyMatrix();
		int[][] aSquared = null; 
		try{
			aSquared = Utility.multiplyMatrix(A, A);
		}catch(MatrixException e){
			if(e.getStatus()==1)
				System.out.println("Invalid matrix dimensions found");
			return null;
		}
		
		//create mapping of matrix index to graph vertex
		List<Graph.Vertex<Integer>> vertexIndexMap = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext()){
			vertexIndexMap.add(vIt.next());
		}
		
		//look for end vertices of a triangle from the square of the adjacency matrix
		for(int i=0; i<aSquared.length; i++){
			for(int j=i+1; j<aSquared.length; j++){
				if((int)aSquared[i][j]>0 && (int)A[i][j]==1){ //end vertices found
					//look for the intermediate index to make up the P3
					for(int k=0; k<A.length; k++){
						if(k!=i && k!=j && (int)A[k][i]==1 && (int)A[k][j]==1){
							//at this point, i, j and k represent matrix indices of the vertices which form the triangle
							//get the actual vertices and create a list of them
							List<Graph.Vertex<Integer>> tVertices = new ArrayList<Graph.Vertex<Integer>>();
							
							Graph.Vertex<Integer> v1 = vertexIndexMap.get(i);
							Graph.Vertex<Integer> v2 = vertexIndexMap.get(j);
							Graph.Vertex<Integer> v3 = vertexIndexMap.get(k);
							tVertices.add(v1);	
							tVertices.add(v2);	
							tVertices.add(v3);	
							
							return tVertices;
						}
					}
				}
			}
		}

		return null;
	}
	
	public static String getResult(){
		String result = String.format("%-10s%-10s", time,found);
		return result;
	}
	
	public static void resetResult(){
		time = "-";
		found = "found";
	}
}
