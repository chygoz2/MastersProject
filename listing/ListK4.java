package listing;
import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class ListK4 {
	
	private static String time = "";
	
	public static void main(String [] args){
		
		String fileName = "matrix4.txt";
		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(fileName);
		
		long starttime = System.currentTimeMillis();
		List<Collection<Graph.Vertex<Integer>>> k4List = detect(graph);
		long stoptime = System.currentTimeMillis();
		
		long timetaken = stoptime-starttime;
		
		for(Collection<Graph.Vertex<Integer>> k4: k4List){
			Utility.printGraph(Utility.makeGraphFromVertexSet(graph, k4));
		}
		System.out.println("Time taken in milliseconds: "+timetaken);
					
	}
	
	public static List<Collection<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		//partition vertices
		List<Graph.Vertex<Integer>>[] verticesPartition = partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		List<Collection<Graph.Vertex<Integer>>> k4List = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		k4List.addAll(phaseOne(graph, highDegreeVertices));
		k4List.addAll(phaseTwo(graph, lowDegreeVertices));
	
		return k4List;
	}
	
	public static List<Collection<Graph.Vertex<Integer>>> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> highDegreeVertices){
		Hashtable<Integer,List<Set<Integer>>> marked = new Hashtable<Integer,List<Set<Integer>>>(); //to prevent creating the same k4 more than once
		List<Collection<Graph.Vertex<Integer>>> k4List = new ArrayList<Collection<Graph.Vertex<Integer>>>();
			
		for(Graph.Vertex<Integer> x: highDegreeVertices){
			//get x's neighbourhood graph
			Iterator<Graph.Vertex<Integer>> nXIter = graph.neighbours(x);
			List<Graph.Vertex<Integer>> nXList = new ArrayList<Graph.Vertex<Integer>>();

			//get the intersection of the neighbourhood vertices of x with the high degree vertices
			while(nXIter.hasNext()){
				Graph.Vertex<Integer> v = nXIter.next();
				if(highDegreeVertices.contains(v))
					nXList.add(v);
			}
			
			//if x's neighbourhood does not have any high degree vertex, then continue
			if(nXList.isEmpty())
				continue;
			
			//make graph from new list of vertices
			UndirectedGraph<Integer,Integer> graph2 = Utility.makeGraphFromVertexSet(graph, nXList);

			//get the triangles in the neighbourhood
			List<Collection<Graph.Vertex<Integer>>> triangles = ListTriangles.detect(graph2);
			
			for(Collection<Graph.Vertex<Integer>> triangle: triangles){
				List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
				k4Vertices.addAll(triangle);
				k4Vertices.add(x);
				
				Set<Integer> k4VerticesElem = new HashSet<Integer>(); //set to store k4 vertices elements
				for(Graph.Vertex<Integer> ke: k4Vertices){
					k4VerticesElem.add(ke.getElement());
				}
				
				//check in the marked list for an entry that contains all 4 vertex elements
				boolean contains = false;		
				
				Integer key = null;
				for(Integer k:k4VerticesElem){
					key = k;
					break;
				}
				
				List<Set<Integer>> list = marked.get(key);
				if(list!=null){
					for(Set<Integer> s: list){
						if(s.containsAll(k4VerticesElem)){
							contains = true;
							break;
						}
					}
				}
				
				
				if(!contains){
					k4List.add(k4Vertices);
					
					if(list==null){
						list = new ArrayList<Set<Integer>>();
						marked.put(key,list);
					}
					list.add(k4VerticesElem);
				}
				
			}
		}
		
		return k4List;
	}
	
	public static List<Collection<Graph.Vertex<Integer>>> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		Hashtable<Integer,List<Set<Integer>>> marked = new Hashtable<Integer,List<Set<Integer>>>(); //to prevent creating the same k4 more than once
		List<Collection<Graph.Vertex<Integer>>> k4List = new ArrayList<Collection<Graph.Vertex<Integer>>>();
			
		for(Graph.Vertex<Integer> x: lowDegreeVertices){
			//get x's neighbourhood graph
			Iterator<Graph.Vertex<Integer>> nXIter = graph.neighbours(x);
			List<Graph.Vertex<Integer>> nXList = new ArrayList<Graph.Vertex<Integer>>();

			while(nXIter.hasNext()){
				Graph.Vertex<Integer> v = nXIter.next();
				nXList.add(v);
			}
			
			//make graph from new list of vertices
			UndirectedGraph<Integer,Integer> graph2 = Utility.makeGraphFromVertexSet(graph, nXList);

			//get the triangles in the neighbourhood
			List<Collection<Graph.Vertex<Integer>>> triangles = ListTriangles.detect(graph2);
			
			for(Collection<Graph.Vertex<Integer>> triangle: triangles){
				List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
				k4Vertices.addAll(triangle);
				k4Vertices.add(x);
				
				Set<Integer> k4VerticesElem = new HashSet<Integer>(); //set to store k4 vertices elements
				for(Graph.Vertex<Integer> ke: k4Vertices){
					k4VerticesElem.add(ke.getElement());
				}
				
				//check in the marked list for an entry that contains all 4 vertex elements
				boolean contains = false;
				
				Integer key = null;
				for(Integer k:k4VerticesElem){
					key = k;
					break;
				}
				
				List<Set<Integer>> list = marked.get(key);
				if(list!=null){
					for(Set<Integer> s: list){
						if(s.containsAll(k4VerticesElem)){
							contains = true;
							break;
						}
					}
				}
				
				if(!contains){
					k4List.add(k4Vertices);
					if(list==null){
						list = new ArrayList<Set<Integer>>();
						marked.put(key,list);
					}
					list.add(k4VerticesElem);
				}
				
			}
		}
		
		return k4List;
	}

	//method to partition the vertices into low degree vertices and high degree vertices
	public static List<Graph.Vertex<Integer>>[] partitionVertices(UndirectedGraph<Integer,Integer> graph){
		List<Graph.Vertex<Integer>>[] vertices = new List[2];
		vertices[0] = new ArrayList<Graph.Vertex<Integer>>();
		vertices[1] = new ArrayList<Graph.Vertex<Integer>>();
		
		//get vertices
		Iterator<Graph.Vertex<Integer>> vertexIterator = graph.vertices();
		
		//get edges
		Iterator<Graph.Edge<Integer>> edgeIterator = graph.edges();
		
		//get number of edges
		int noOfEdges = 0;
		while(edgeIterator.hasNext()){
			edgeIterator.next();
			noOfEdges++;
		}
		

		//calculate D for Graph.Vertex partitioning
		double D = Math.sqrt(noOfEdges);
		
		while(vertexIterator.hasNext()){
			Graph.Vertex<Integer> v = vertexIterator.next();
			if(graph.degree(v)>D)
				vertices[1].add(v);
			else
				vertices[0].add(v);
		}
		
		return vertices;
	}
	
	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
}
