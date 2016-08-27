package efficient.detection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import exception.GraphFileReaderException;
import exception.MatrixException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectTriangle2 {
	//instance variables
	private String time = "-"; //stores time taken to execute the operation
	private String found = "found";  //stores whether a triangle was found
	
	public static void main(String[] args) {
		try{
			UndirectedGraph<Integer,Integer> graph = null;
			graph = Utility.makeGraphFromFile(args[0]);
			
			DetectTriangle2 d = new DetectTriangle2();
			List<Graph.Vertex<Integer>> triangle = d.detect(graph);
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}
		catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	/**
	 * method that detects triangle in a graph 
	 * @param graph 		the graph to be checked
	 * @return				the triangle vertices found if any
	 */
	public List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		//measure time taken to detect a triangle
		long starttime = System.currentTimeMillis();
		List<Graph.Vertex<Integer>> triangle= find(graph);
		long stoptime = System.currentTimeMillis();
		time = ""+(stoptime-starttime);
		
		if(triangle==null)
			found = "not found";
		
		return triangle;
	}
	
	public List<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){
		//look for a P3 whose intermediate vertex is of low degree
		List<Graph.Vertex<Integer>>[] partitionedVertices = partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = partitionedVertices[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = partitionedVertices[1];
		
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			Iterator<Graph.Edge<Integer>> eIt = graph.connectingEdges(v);
			while(eIt.hasNext()){
				Graph.Edge<Integer> e1 = eIt.next();
				Graph.Vertex<Integer> other1 = e1.getSource().equals(v)? e1.getDestination(): e1.getSource();
				
				Iterator<Graph.Edge<Integer>> eIt2 = graph.connectingEdges(v);
				while(eIt2.hasNext()){
					Graph.Edge<Integer> e2 = eIt2.next();
					if(e2.equals(e1))
						continue;
					Graph.Vertex<Integer> other2 = e2.getSource().equals(v)? e2.getDestination(): e2.getSource();
					if(graph.containsEdge(other1, other2)){
						List<Graph.Vertex<Integer>> triangle = new ArrayList<Graph.Vertex<Integer>>();
						triangle.add(v);
						triangle.add(other1);
						triangle.add(other2);
						return triangle;
					}
				}
			}
		}
		
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			graph.removeVertex(v);
		}
		
		return findUsingMatrixMultiplication(graph);
	}
	
	/**
	 * method that detects triangle via the matrix multiplication method
	 * Has a time complexity of O(n^a) where 2<a<=3 depending on which matrix multiplication
	 * algorithm is used. a is currently 3 as standard matrix multiplication was used 
	 * @param graph 		the graph to be checked
	 * @return				the triangle vertices found if any
	 */
	public List<Graph.Vertex<Integer>> findUsingMatrixMultiplication(UndirectedGraph<Integer,Integer> graph){
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
	
	/**
	 * method to partition the vertices into low degree vertices and high degree vertices
	 * @param graph		the graph whose vertices are to be partitioned
	 * @return			the partitions
	 */
	public  List<Graph.Vertex<Integer>>[] partitionVertices(UndirectedGraph<Integer,Integer> graph){
		List<Graph.Vertex<Integer>>[] vertices = new List[2];
		vertices[0] = new ArrayList<Graph.Vertex<Integer>>();
		vertices[1] = new ArrayList<Graph.Vertex<Integer>>();
		
		//get vertices
		Iterator<Graph.Vertex<Integer>> vertexIterator = graph.vertices();
		
		//get number of edges
		int noOfEdges = graph.getEdgeCount();
		
		//calculate D for Vertex partitioning
		double alpha = 3; //from standard matrix multiplication
		double pow = (alpha-1)/(alpha+1);
		double D = Math.pow(noOfEdges, pow);
		
		while(vertexIterator.hasNext()){
			Graph.Vertex<Integer> v = vertexIterator.next();
			if(graph.degree(v)>D)
				vertices[1].add(v);
			else
				vertices[0].add(v);
		}
		
		return vertices;
	}
	
	/**
	 * method to return the time taken for the detection and the result
	 * @return		the result for analysis
	 */
	public String getResult(){
		String result = String.format("%-10s%-10s", time,found);
		return result;
	}
}
