import java.util.*;

public class DetectKL {
	
	public static void main(String [] args){
		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
		
		Vertex<Integer> v1 = graph.addVertex(1);
		Vertex<Integer> v2 = graph.addVertex(2);
		Vertex<Integer> v3 = graph.addVertex(3);
		Vertex<Integer> v4 = graph.addVertex(4);
		Vertex<Integer> v5 = graph.addVertex(5);
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
		
		
		graph.mapVertexToId();
		
		complete(3, graph);
	}
	
	public static void complete(int l, UndirectedGraph g){
		Map vertexLabel = new HashMap();
		Iterable<Vertex> vIt = (Iterable<Vertex>)g.vertices();
		for(Vertex v: vIt){
			vertexLabel.put(v,l);
		}
		Stack<Vertex> C = new Stack<Vertex>();
		K(l,g,C,vertexLabel);
	}
	
	public static void K(int k, UndirectedGraph gk, Stack<Vertex> C, Map vertexLabel){
		Iterable<Vertex> U = (Iterable<Vertex>)gk.vertices();
		if(k==2){
			Iterable<Edge> gkEdges = (Iterable<Edge>)gk.edges();
			UndirectedGraph kGraph = new UndirectedGraph();
			Set<Integer> vertexSet = new HashSet<Integer>();
			for(Edge ee: gkEdges){
				//line 1
				Vertex source = ee.getSource();
				Vertex destination = ee.getDestination();
				vertexSet.add((Integer) source.getElement());
				vertexSet.add((Integer) destination.getElement());
			}
			while(!C.isEmpty()){
				vertexSet.add((Integer)C.pop().getElement());
			}
			for(Integer v: vertexSet){
				Vertex vv = kGraph.addVertex(v);
				for(Integer z: vertexSet){
					if(v!=z){
						Vertex zz = kGraph.addVertex(z);
						if(!kGraph.containsEdge(vv, zz)){
							kGraph.addEdge(vv, zz);
						}
					}
				}
			}
			Utility.printGraph(kGraph);
		}else{
			//sort vertices in U (line 2)
			List<Vertex> sortedU = new ArrayList<Vertex>();
			for(Vertex v: U){
				sortedU.add(v);
			}
			Collections.sort(sortedU, new VertexComparator(gk));
			
			for(int i=0; i<sortedU.size();i++){
				Vertex vi = sortedU.get(i);
				
				//get neighbour labeled k
				Iterator<Vertex> vit = gk.neighbours(gk.getVertexWithId(vi.getId()));
				List<Vertex> list = getVerticesWithLabel(k, vertexLabel);
				List<Vertex> adjVerticesLabeledK = new ArrayList<Vertex>();
				while(vit.hasNext()){
					Vertex v = vit.next();
					if(getVertexWithId(v.getId(), list) != null){
						adjVerticesLabeledK.add(v);
						
						//relabel all vertices in adjVertricesLabeledK to k-1 (line 3)
						Collection<Vertex> mapKeys = vertexLabel.keySet();
						for(Vertex vv: mapKeys){
							if(vv.getId() == v.getId()){
								vertexLabel.put(vv, k-1);
								break;
							}
						}
					}
				}
				
				//line 4
				for(Vertex u: adjVerticesLabeledK){
					int j = 0;
					Iterator<Vertex> uNeigh = gk.neighbours(gk.getVertexWithId(u.getId()));
					while(uNeigh.hasNext()){
						Vertex v = uNeigh.next();
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
				Collection<Vertex> mapKeys = vertexLabel.keySet();
				for(Vertex vv: mapKeys){
					if(checkIfContained(vv, adjVerticesLabeledK)){
						vertexLabel.put(vv, k);
						break;
					}
				}
				
				//line 10
				vertexLabel.put(vi, k+1);
				
				//line 11
				for(Vertex u: adjVerticesLabeledK){
					int j = 0;
					Iterable<Vertex> uNeigh = (Iterable<Vertex>)gk.neighbours(gk.getVertexWithId(u.getId()));
					List<Vertex> uAdjList = new ArrayList<Vertex>();
					for(Vertex a: uNeigh){
						uAdjList.add(a);
					}
					//move vi
					uAdjList.remove(gk.getVertexWithId(vi.getId()));
					for(int m=0;m<uAdjList.size();m++){
						if((int)vertexLabel.get(gk.getVertexWithId(uAdjList.get(m).getId()))!= k){
							uAdjList.add(m, gk.getVertexWithId(vi.getId()));
						}
					}
				}
			}
			
		}
	}
	
	public static boolean checkIfContained(Vertex v, List<Vertex> list){
		int vid = v.getId();
		for(Vertex vv: list){
			if(vv.getId()==vid)
				return true;
		}
		return false;
	}
	
	public static class VertexComparator implements Comparator<Vertex>{
		
		private UndirectedGraph graph;
		
		public VertexComparator(UndirectedGraph g){
			this.graph = g;
		}

		@Override
		public int compare(Vertex v1, Vertex v2) {
			Integer d1 = graph.degree(v1);
			Integer d2 = graph.degree(v2);
			
			return -1 *(d1.compareTo(d2));
		}
	}
	
	public static List<Vertex> getVerticesWithLabel(int k, Map map){
		List<Vertex> list = new ArrayList<Vertex>();
		Collection<Vertex> mapKeys = map.keySet();
		for(Vertex v: mapKeys){
			if((int)map.get(v) == k){
				list.add(v);
			}
		}
		return list;
	}
	
	public static Vertex getVertexWithId(int id, List<Vertex> list){
		for(Vertex v: list){
			if(v.getId()==id)
				return v;
		}
		return null;
	}
}
