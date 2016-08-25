package bruteforce.detection;

import java.util.Iterator;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectSimplicialVertex {
	
	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String [] args){		
		try{			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);
			
			DetectSimplicialVertex d = new DetectSimplicialVertex();
			long a = System.currentTimeMillis();
			Thread t = new Thread(new Runnable(){
				public void run(){
					Graph.Vertex<Integer> s = d.detect(graph);
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
					Thread.sleep(100);
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
	
	public Graph.Vertex<Integer> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		Graph.Vertex<Integer> s = find(graph);
		long end = System.currentTimeMillis();
		p1time = ""+(end-start);
		
		if(s==null)
			found = "not found";
		
		return s;
	}
	
	public Graph.Vertex<Integer> find(UndirectedGraph<Integer,Integer> graph){
		
		//get the vertices
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		while(vertices.hasNext()){
			Graph.Vertex<Integer> vertex = vertices.next();
			
			//get the neighbourhood graph of vertex
			UndirectedGraph<Integer,Integer> vertexNeighbourhood = Utility.getNeighbourGraph(graph, vertex);
			
			if(vertexNeighbourhood.size()>0){
				//check if each pair of vertices in the neighbourhood are adjacent
				Iterator<Graph.Vertex<Integer>> vIt1 = vertexNeighbourhood.vertices();
				
				boolean contains = true;
				
				here:
				while(vIt1.hasNext()){
					Graph.Vertex<Integer> v1 = vIt1.next();
					
					Iterator<Graph.Vertex<Integer>> vIt2 = vertexNeighbourhood.vertices();
					while(vIt2.hasNext()){
						Graph.Vertex<Integer> v2 = vIt2.next();
						
						if(v1!=v2 && !vertexNeighbourhood.containsEdge(v1, v2)){
							contains = false;
							break here;
						}
					}
				}
				
				if(contains){
					return vertex;
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
