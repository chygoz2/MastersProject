package easydetection;

import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectKL {
	
	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String[] args){		
		try{			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);
			DetectKL d = new DetectKL();
			int l = Integer.parseInt(args[1]);
			long a = System.currentTimeMillis();
			Thread t = new Thread(new Runnable(){
				public void run(){
					Collection<Graph.Vertex<Integer>> kl = d.detect(graph,l);	
					if(kl!=null){
						Utility.printGraph(Utility.makeGraphFromVertexSet(graph, kl));
					}
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
			System.out.println("Please provide the graph file and complete graph size as command line arguments");
		}catch(InterruptedException e){}
	}
	
	public Collection<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph, int l){
		long starttime = System.currentTimeMillis();
		Collection<Graph.Vertex<Integer>> kl = find(graph,l);
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime);
		
		if(kl==null)
			found = "not found";
		return kl;
	}
	
	public Collection<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph, int l){
		Collection<Graph.Vertex<Integer>> kl = new ArrayList<Graph.Vertex<Integer>>();
		
		if(l<1)
			return null;
		
		//get the vertices of graph
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		
		if(l==1){
			//return one vertex
			while(vIt.hasNext()){
				kl.add(vIt.next());
				return kl;
			}
		}
	
		//look for a k(l-1) in the neighbourhood of each vertex
		while(vIt.hasNext()){
			Graph.Vertex<Integer> x = vIt.next();
			//get the neighbourhood graph of x
			UndirectedGraph<Integer,Integer> xn = Utility.getNeighbourGraph(graph, x);
			Collection<Graph.Vertex<Integer>> knminus = find(xn, l-1);
			if(knminus != null && !knminus.isEmpty()){
				kl.addAll(knminus);
				kl.add(x);
				return kl;
			}
		}
		
		return null;
	}
	
	public  String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
	
}
