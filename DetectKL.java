import java.util.*;

public class DetectKL {
	
	public static void main(String [] args){
		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Graph.Vertex<Integer> v1 = graph.addVertex(1);
		Graph.Vertex<Integer> v2 = graph.addVertex(2);
		Graph.Vertex<Integer> v3 = graph.addVertex(3);
		Graph.Vertex<Integer> v4 = graph.addVertex(4);
		Graph.Vertex<Integer> v5 = graph.addVertex(5);
		graph.addEdge(v1, v2);
		graph.addEdge(v3, v2);
		graph.addEdge(v3, v4);
		graph.addEdge(v1, v3);
		graph.addEdge(v1, v5);
		graph.addEdge(v3, v5);
		graph.addEdge(v2, v5);
		graph.addEdge(v4, v5);
		graph.addEdge(v2, v4);
		graph.addEdge(v1, v4);
		
		
		//graph.mapVertexToId();
		
		complete(3, graph);
	}
	
	public static void complete(int l, UndirectedGraph g){
		Map vertexLabel = new HashMap();
		Iterable<Graph.Vertex> vIt = (Iterable<Graph.Vertex>)g.vertices();
		for(Graph.Vertex v: vIt){
			vertexLabel.put(v,l);
		}
		Stack<Graph.Vertex> C = new Stack<Graph.Vertex>();
		K(l,g,C,vertexLabel);
	}
	
	public static void K(int k, UndirectedGraph gk, Stack<Graph.Vertex> C, Map vertexLabel){
		Iterable<Graph.Vertex> U = (Iterable<Graph.Vertex>)gk.vertices();
		if(k==2){
			Iterable<Graph.Edge> gkEdges = (Iterable<Graph.Edge>)gk.edges();
			UndirectedGraph kGraph = new UndirectedGraph();
			Set<Integer> vertexSet = new HashSet<Integer>();
			for(Graph.Edge ee: gkEdges){
				//line 1
				Graph.Vertex source = ee.getSource();
				Graph.Vertex destination = ee.getDestination();
				vertexSet.add((Integer) source.getElement());
				vertexSet.add((Integer) destination.getElement());
			}
			while(!C.isEmpty()){
				vertexSet.add((Integer)C.pop().getElement());
			}
			for(Integer v: vertexSet){
				Graph.Vertex vv = kGraph.addVertex(v);
				for(Integer z: vertexSet){
					if(v!=z){
						Graph.Vertex zz = kGraph.addVertex(z);
						if(!kGraph.containsEdge(vv, zz)){
							kGraph.addEdge(vv, zz);
						}
					}
				}
			}
			Utility.printGraph(kGraph);
		}else{
			//sort vertices in U (line 2)
			List<Graph.Vertex> sortedU = new ArrayList<Graph.Vertex>();
			for(Graph.Vertex v: U){
				sortedU.add(v);
			}
			Collections.sort(sortedU, new VertexComparator(gk));
			
			for(int i=0; i<sortedU.size();i++){
				Graph.Vertex vi = sortedU.get(i);
				
				//get neighbour labeled k
				Iterator<Graph.Vertex> vit = gk.neighbours(gk.getVertexWithElement((int)vi.getElement()));
				List<Graph.Vertex> list = getVerticesWithLabel(k, vertexLabel);
				List<Graph.Vertex> adjVerticesLabeledK = new ArrayList<Graph.Vertex>();
				while(vit.hasNext()){
					Graph.Vertex v = vit.next();
					if(getVertexWithElement((int)v.getElement(), list) != null){
						adjVerticesLabeledK.add(v);
						
						//relabel all vertices in adjVertricesLabeledK to k-1 (line 3)
						Collection<Graph.Vertex> mapKeys = vertexLabel.keySet();
						for(Graph.Vertex vv: mapKeys){
							if(vv.getElement() == v.getElement()){
								vertexLabel.put(vv, k-1);
								break;
							}
						}
					}
				}
				
				//line 4
				for(Graph.Vertex u: adjVerticesLabeledK){
					int j = 0;
					Iterator<Graph.Vertex> uNeigh = gk.neighbours(gk.getVertexWithElement((int)u.getElement()));
					while(uNeigh.hasNext()){
						Graph.Vertex v = uNeigh.next();
						if(adjVerticesLabeledK.contains(v)){
							adjVerticesLabeledK.remove(v);
							adjVerticesLabeledK.add(j,v);
							j++;
						}
					}
				}
				
				//line 6
				C.push(vi);
				
				//line 7
				UndirectedGraph ugraph = Utility.makeGraphFromVertexSet(gk, adjVerticesLabeledK);
				K(k-1, ugraph, C, vertexLabel);
				
				C.pop(); //line 8
				
				//line 9
				Collection<Graph.Vertex> mapKeys = vertexLabel.keySet();
				for(Graph.Vertex vv: mapKeys){
					if(checkIfContained(vv, adjVerticesLabeledK)){
						vertexLabel.put(vv, k);
						break;
					}
				}
				
				//line 10
				vertexLabel.put(vi, k+1);
				
				//line 11
				for(Graph.Vertex u: adjVerticesLabeledK){
					int j = 0;
					Iterable<Graph.Vertex> uNeigh = (Iterable<Graph.Vertex>)gk.neighbours(gk.getVertexWithElement((int)u.getElement()));
					List<Graph.Vertex> uAdjList = new ArrayList<Graph.Vertex>();
					for(Graph.Vertex a: uNeigh){
						uAdjList.add(a);
					}
					//move vi
					uAdjList.remove(gk.getVertexWithElement((int)vi.getElement()));
					for(int m=0;m<uAdjList.size();m++){
						if((int)vertexLabel.get(gk.getVertexWithElement((int)uAdjList.get(m).getElement()))!= k){
							uAdjList.add(m, gk.getVertexWithElement((int)vi.getElement()));
						}
					}
				}
			}
			
		}
	}
	
	public static boolean checkIfContained(Graph.Vertex<Integer> v, List<Graph.Vertex> list){
		int vid = v.getElement();
		for(Graph.Vertex<Integer> vv: list){
			if(vv.getElement()==vid)
				return true;
		}
		return false;
	}
	
	public static class VertexComparator implements Comparator<Graph.Vertex>{
		
		private UndirectedGraph graph;
		
		public VertexComparator(UndirectedGraph g){
			this.graph = g;
		}

		@Override
		public int compare(Graph.Vertex v1, Graph.Vertex v2) {
			Integer d1 = graph.degree(v1);
			Integer d2 = graph.degree(v2);
			
			return -1 *(d1.compareTo(d2));
		}
	}
	
	public static List<Graph.Vertex> getVerticesWithLabel(int k, Map map){
		List<Graph.Vertex> list = new ArrayList<Graph.Vertex>();
		Collection<Graph.Vertex> mapKeys = map.keySet();
		for(Graph.Vertex v: mapKeys){
			if((int)map.get(v) == k){
				list.add(v);
			}
		}
		return list;
	}
	
	public static Graph.Vertex<Integer> getVertexWithElement(int id, List<Graph.Vertex> list){
		for(Graph.Vertex<Integer> v: list){
			if(v.getElement()==id)
				return v;
		}
		return null;
	}
}
