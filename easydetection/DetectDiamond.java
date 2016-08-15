package easydetection;

import java.io.IOException;
import java.util.*;

import general.*;

public class DetectDiamond {
	
	private String p1time = "-";
	private String found = "found";
	
	public static void main(String [] args) throws IOException{
		UndirectedGraph<Integer,Integer> graph;
		for(int a=0;a<1;a++){
//			String fileName = "matrix2.txt";
			String fileName = "generated_graphs\\size_5\\graph_5_0.7_4.txt";
//			String fileName = "generated_graphs\\size_6\\graph_6_0.6_3.txt";
//			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
//			String fileName = "test\\testdata\\diamondtestdata.txt";
//			String fileName = "generated_graphs\\size_300\\graph_300_0.9_1.txt";
//			String fileName = "generated_graphs\\size_150\\graph_150_1.0_1.txt";
//			UndirectedGraph<Integer,Integer> graphs[a] = Utility.makeGraphFromFile(fileName);
			graph = Utility.makeGraphFromFile(fileName);
			
			List<Graph.Vertex<Integer>> diamond = new DetectDiamond().detect(graph);
			
			if(diamond!=null){
				Utility.printGraph(Utility.makeGraphFromVertexSet(graph,diamond));
			}
			else{
				System.out.println("Diamond not found");
			}
			
		}
	}
	
	public List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		long starttime = System.currentTimeMillis();
		List<Graph.Vertex<Integer>> diamond = find(graph);
		long stoptime = System.currentTimeMillis();
		
		p1time = ""+(stoptime-starttime);
		if(diamond==null)
			found = "not found";
		
		System.out.println(getResult());
		return diamond;
	}

	public List<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){
		//get the vertices
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		//for each vertex, get its neighbourhood and get a P3 in its neighbourhood
		while(vertices.hasNext()){
			Graph.Vertex<Integer> x = vertices.next();
			UndirectedGraph<Integer,Integer> xn = Utility.getNeighbourGraph(graph, x);

			List<Graph.Vertex<Integer>> p3 = checkP3InComponent(xn);
			
			//if p3 is found, then the p3 along with x produce a diamond
			if(p3!=null){
				p3.add(x);
				return p3;
			}
		}
		
		return null;
	}
	
	/**
	 * method that checks if a P3 is present in a graph and returns the vertices of the P3
	 * @param graph 	the graph to be checked
	 * @return 			the vertex list if found
	 */
	public List<Graph.Vertex<Integer>> checkP3InComponent(UndirectedGraph<Integer,Integer> graph){
		//if the no of vertices in graph is less than 3, then graph cannot have a p3
		
		if(graph.size()<3){
			return null;
		}
		
		//create mapping between matrix indices and vertex
		List<Graph.Vertex<Integer>> vertexIndexMap = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices(); //gets the vertex iterator
		while(vIt.hasNext()){
			vertexIndexMap.add(vIt.next());
		}
		
		int[][] A = graph.getAdjacencyMatrix();
		int[][] A2;
		try {
			A2 = Utility.multiplyMatrix(A, A);
		} catch (MatrixException e) {
			return null;
		}
		
		//look for a path of length 2 in A2
		for(int i=0; i<A2.length;i++){
			for(int j=i+1; j<A2.length;j++){
				if((int)A2[i][j]>0 && (int)A[i][j]==0){ //then a P3 is found
					//look for third vertex
					for(int k=0; k<A.length;k++){
						if(k!=i && k!= j && ((int)A[k][i] == 1) && ((int)A[k][j] == 1)){
							List<Graph.Vertex<Integer>> vert = new ArrayList<Graph.Vertex<Integer>>();
							vert.add(vertexIndexMap.get(i)); vert.add(vertexIndexMap.get(j)); vert.add(vertexIndexMap.get(k));
							return vert;
						}
					}
				}
			}
		}
	
		return null;
	}
	
	public  String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
	
	public  void resetResult(){
		p1time = "-";
		found = "found";
	}
}
