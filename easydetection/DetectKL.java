package easydetection;

import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectKL {
	
	public Collection<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph, int l){
		
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
		}
		
		return kl;
	}
	
}
