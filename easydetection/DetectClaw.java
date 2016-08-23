package easydetection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import efficientdetection.DetectTriangle;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class DetectClaw {
	
	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String [] args){		
		try{			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);
			DetectClaw d = new DetectClaw();
			long a = System.currentTimeMillis();
			Thread t = new Thread(new Runnable(){
				public void run(){
					Collection<Graph.Vertex<Integer>> claw = d.detect(graph);	
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
	}

	public Collection<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		Collection<Graph.Vertex<Integer>> s = find(graph);
		long end = System.currentTimeMillis();
		p1time = ""+(end-start);
		
		if(s==null)
			found = "not found";
		return s;
	}
	
	public Collection<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){		
		//for each vertex, check if the complement of its neighbour contains a triangle
		
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		while(vertices.hasNext()){
			//get neighbourhood graph
			Graph.Vertex<Integer> v = vertices.next();
			UndirectedGraph<Integer,Integer> vNeighGraph = Utility.getNeighbourGraph(graph, v);
			
			if(vNeighGraph.size()<3)
				continue;
			
			Collection<Graph.Vertex<Integer>> claw = getClawVerticesFromNeighbourGraph(vNeighGraph);
			if(claw!=null){
				//add v to the collection
				claw.add(v);
				return claw;
			}
			
		}
		return null;
	}
	
	/**
	 * method to check if a neighbourhood contains non-central vertices of a claw by checking
	 * if the complement of the neighbourhood contains a triangle
	 * @param vNeighGraph		the neighbourhood graph to be checked
	 * @return					the vertices if found
	 */
	private Collection<Graph.Vertex<Integer>> getClawVerticesFromNeighbourGraph(UndirectedGraph<Integer,Integer> vNeighGraph){
		//get the complement matrix of the neighbour graph
		int[][] vncomp = vNeighGraph.getComplementMatrix();
		
		//map the neighbour graph vertices to the complement matrix indices
		List<Graph.Vertex<Integer>> vimap = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vnIt = vNeighGraph.vertices();
		while(vnIt.hasNext()){
			vimap.add(vnIt.next());
		}
		
		//create complement graph from complement matrix
		UndirectedGraph<Integer,Integer> vncompgraph = Utility.makeGraphFromAdjacencyMatrix(vncomp); 
		
		//look for a triangle in the complement graph. Such a triangle forms the remaining vertices
		//of the claw
		List<Graph.Vertex<Integer>> tri = new DetectTriangle().detect(vncompgraph);
		if(tri!=null){
			//get the vertices of the main graph that correspond to the vertices of the triangle found
			Collection<Graph.Vertex<Integer>> claw = new ArrayList<Graph.Vertex<Integer>>();
			
			claw.add(vimap.get(tri.get(0).getElement()));
			claw.add(vimap.get(tri.get(1).getElement()));
			claw.add(vimap.get(tri.get(2).getElement()));
			
			return claw;
		}
		
		return null;
	}
	
	public  String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
}
