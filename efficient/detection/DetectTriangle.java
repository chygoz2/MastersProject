package efficient.detection;
import java.util.*;

import exception.GraphFileReaderException;
import exception.MatrixException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

/**
 * method to detect a triangle in a graph
 * @author Chigozie Ekwonu
 *
 */
public class DetectTriangle {

	//instance variables
	private String time; //stores time taken to execute the operation
	private String found;  //stores whether a triangle was found
	
	public DetectTriangle(){
		this.time = "-";
		this.found = "found";
	}
	
	public static void main(String[] args) {
		try{
			UndirectedGraph<Integer,Integer> graph = null;
			graph = Utility.makeGraphFromFile(args[0]); //create graph from input file
			
			//create a DetectTriangle object to detect a triangle in a graph
			DetectTriangle d = new DetectTriangle();
			List<Graph.Vertex<Integer>> triangle = d.detect(graph);
			String out = "";
			
			//print out the result of the detection
			if(triangle!=null){
				out = Utility.printList(triangle);
				out = String.format("Triangle found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Triangle not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
			System.out.println(out);
		}catch(ArrayIndexOutOfBoundsException e){ //inform user to provide the name of the input file
			System.out.println("Please provide the graph file as a command line argument");
		}
		catch(GraphFileReaderException e){ //notify user if something goes wrong while reading input file
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
	
	/**
	 * method that detects triangle via the matrix multiplication method
	 * Has a time complexity of O(n^a) where 2<a<=3 depending on which matrix multiplication
	 * algorithm is used. a is currently 3 as standard matrix multiplication was used 
	 * @param graph 		the graph to be checked
	 * @return				the triangle vertices found if any
	 */
	public List<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){
		//get the adjacency matrix
		int[][] A = graph.getAdjacencyMatrix();
		int[][] aSquared = null; 
		try{
			aSquared = Utility.multiplyMatrix(A, A);
		}catch(MatrixException e){
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
	 * method to return the time taken for the detection and the result
	 * @return		the result for analysis
	 */
	public String getResult(){
		String result = String.format("%-10s%-10s", time,found);
		return result;
	}

}
