package bruteforce.detection;

import java.util.Collection;
import java.util.Iterator;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectK4 {

	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String [] args){
		try{			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);
			DetectK4 d = new DetectK4();
			long a = System.currentTimeMillis();
			Thread t = new Thread(new Runnable(){
				public void run(){
					Collection<Graph.Vertex<Integer>> k4 = d.detect(graph);	
				}
			});
			t.start();
			while(!t.isInterrupted() && t.isAlive()){
				long timeout = 120000; //timeout of 2 minutes
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

	public Collection<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		
		long starttime = System.currentTimeMillis();
		Collection<Graph.Vertex<Integer>> k4 = find(graph);
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime);
		
		if(k4==null)
			found = "not found";
		return k4;
	}
	
	public Collection<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){ 

		//get all the vertices
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		//check if each vertex has a triangle in its neighbourhood
		while(vertices.hasNext()){
			Graph.Vertex<Integer> vertex = vertices.next();

			//get the neighbourhood graph of vertex
			UndirectedGraph<Integer,Integer> neighbourhood = Utility.getNeighbourGraph(graph, vertex);
			//use triangle detection algorithm to get the triangles in the neighbourhood
			Collection<Graph.Vertex<Integer>> tri = new DetectTriangle().detect(neighbourhood);
			if(tri!=null){
				//if not null, then a triangle is found
				//add final vertex to list
				tri.add(vertex);
				return tri;
			}
		}
		return null;
	}
	
	public  String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
}
