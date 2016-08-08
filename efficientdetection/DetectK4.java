package efficientdetection;
import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectK4 {
	
	private static String time = "";
	
	public static void main(String [] args){
//		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
//		
//		Graph.Vertex<Integer> v0 = graph.addVertex(0);
//		Graph.Vertex<Integer> v1 = graph.addVertex(1);
//		Graph.Vertex<Integer> v2 = graph.addVertex(2);
//		Graph.Vertex<Integer> v3 = graph.addVertex(3);
//		Graph.Vertex<Integer> v4 = graph.addVertex(4);
//		graph.addEdge(v0, v1);
//		graph.addEdge(v2, v1);
//		graph.addEdge(v2, v3);
//		graph.addEdge(v0, v2);
//		graph.addEdge(v0, v4);
////		graph.addEdge(v2, v4);
//		graph.addEdge(v1, v4);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v1, v3);
//		graph.addEdge(v0, v3);
//		
////		Utility.saveGraphToFile(graph, 1.0, 1);
		String fileName = "matrix4.txt";
		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(fileName);
		
		long starttime = System.currentTimeMillis();
		Collection<Graph.Vertex<Integer>> k4 = detect(graph);
		long stoptime = System.currentTimeMillis();
		
		long timetaken = stoptime-starttime;
		
		if(k4!=null){
			Utility.printGraph(Utility.makeGraphFromVertexSet(graph, k4));
		}
		System.out.println("Time taken in milliseconds: "+timetaken);
					
	}
	
	public static Collection<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		time+="size_"+graph.size()+"_";
		
		List<Graph.Vertex<Integer>>[] verticesPartition = Utility.partitionVertices(graph);
		
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		Collection<Graph.Vertex<Integer>> k4 = null;
		
		long starttime = System.currentTimeMillis();
		k4 = phaseOne(graph, highDegreeVertices);
		long stoptime = System.currentTimeMillis();
		time += "phase1("+(stoptime-starttime)+")_";
		
		if(k4==null){
			starttime = System.currentTimeMillis();
			k4 = phaseTwo(graph, lowDegreeVertices);
			stoptime = System.currentTimeMillis();
			time += "phase2("+(stoptime-starttime)+")_";
		}
		
		if(k4 != null)
			time+="1";
		else
			time+="0";
		//System.out.println(time);
		DetectK4.resetTime();
		return k4;
	}
	
	public static Collection<Graph.Vertex<Integer>> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> highDegreeVertices){
			
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
			Collection<Graph.Vertex<Integer>> triangle = DetectTriangle.detect2(graph2);
			
			List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
			k4Vertices.addAll(triangle);
			k4Vertices.add(x);
			
			return k4Vertices;
				
		}
		
		return null;
	}
	
	public static Collection<Graph.Vertex<Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
			
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
			Collection<Graph.Vertex<Integer>> triangle = DetectTriangle.detect2(graph2);
			
			List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
			k4Vertices.addAll(triangle);
			k4Vertices.add(x);
			
			return k4Vertices;
			
		}
		return null;
	}

	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
}
