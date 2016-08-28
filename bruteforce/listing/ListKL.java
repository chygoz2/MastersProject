package bruteforce.listing;

import java.util.*;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class ListKL {
	
	private  String p1time = "-";
	private  int found = 0;
	
	public static void main(String[] args){		
		try{			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);
			ListKL d = new ListKL();
			int l = Integer.parseInt(args[1]);
			long a = System.currentTimeMillis();
			Thread t = new Thread(new Runnable(){
				public void run(){
					List<List<Graph.Vertex<Integer>>> kls = d.detect(graph,l);	
				}
			});
			t.start();
			while(!t.isInterrupted() && t.isAlive()){
				long timeout = Long.MAX_VALUE; //timeout of 2 minutes
				long b = System.currentTimeMillis();
				if((b-a)>timeout){
					d.found = -1;
					t.interrupt();
				}else{
					Thread.sleep(500);
				}
			}
			
			System.out.print(d.getResult());
			System.exit(0);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file and complete graph size as command line arguments");
		}
		catch(InterruptedException e){}
		catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	public List<List<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph, int l){
		long starttime = System.currentTimeMillis();
		List<List<Graph.Vertex<Integer>>> kls = find(graph,l);
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime);
		found = kls.size();
		return kls;
	}
	
	public List<List<Graph.Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph, int l){
		if(l<1)
			return null;
		
		List<List<Graph.Vertex<Integer>>> kls = new ArrayList<List<Graph.Vertex<Integer>>>();
		Set<Set<Integer>> seen = new HashSet<Set<Integer>>();
		
		//get the vertices of graph
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		
		if(l==1){
			//return one vertex graphs
			while(vIt.hasNext()){
				List<Graph.Vertex<Integer>> kl = new ArrayList<Graph.Vertex<Integer>>();
				kl.add(vIt.next());
				kls.add(kl);
			}
			return kls;
		}
	
		//look for a k(l-1) in the neighbourhood of each vertex
		while(vIt.hasNext()){
			Graph.Vertex<Integer> x = vIt.next();
			//get the neighbourhood graph of x
			UndirectedGraph<Integer,Integer> xn = Utility.getNeighbourGraph(graph, x);
			List<List<Graph.Vertex<Integer>>> knminusList = find(xn, l-1);
			for(List<Graph.Vertex<Integer>> knminus: knminusList){
				Set<Integer> hh = new HashSet<Integer>();
				for(Graph.Vertex<Integer> v: knminus){
					hh.add(v.getElement());
				}
				hh.add(x.getElement());
				if(seen.add(hh)){
					knminus.add(x);
					kls.add(knminus);
				}
			}
		}
		
		return kls;
	}
	
	/**
	*	method to return the time taken to run the listing	
	*	and the number of K4s found
	*/
	public String getResult(){
		String result = String.format("%-10s%10d", p1time,found);
		return result;
	}
	
}
