package bruteforce.detection;

import java.io.IOException;
import java.util.*;

import exception.GraphFileReaderException;
import exception.MatrixException;
import general.*;

public class DetectDiamond {
	
	private String p1time = "-";
	private String found = "found";
	
	public static void main(String [] args) throws IOException{
		
		try{			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);
			DetectDiamond d = new DetectDiamond();
			long a = System.currentTimeMillis();
			Thread t = new Thread(new Runnable(){
				public void run(){
					Collection<Graph.Vertex<Integer>> diamond = d.detect(graph);
				}
			});
			t.start();
			while(!t.isInterrupted() && t.isAlive()){
				long timeout = 30000; //timeout of 30 seconds
				long b = System.currentTimeMillis();
				if((b-a)>timeout){
					d.found = "timed out";
					t.interrupt();
				}else{
					Thread.sleep(500);
				}
			}
			
			System.out.print(d.getResult());
			System.exit(0);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(InterruptedException e){}
		catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	public List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		long starttime = System.currentTimeMillis();
		List<Graph.Vertex<Integer>> diamond = find(graph);
		long stoptime = System.currentTimeMillis();
		
		p1time = ""+(stoptime-starttime);
		if(diamond==null)
			found = "not found";
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
}
