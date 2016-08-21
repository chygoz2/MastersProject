package efficientdetection;
import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectK4 {
	
	private  String p1time = "-";
	private  String p2time = "-";
	private  String found = "found";
	
	public static void main(String [] args){
		try{
			UndirectedGraph<Integer,Integer> graph = null;
			graph = Utility.makeGraphFromFile(args[0]);
			
			DetectK4 d = new DetectK4();
			List<Graph.Vertex<Integer>> k4 = d.detect(graph);
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}
	}
	
	public  List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){		
		List<Graph.Vertex<Integer>>[] verticesPartition = partitionVertices(graph);
		
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		List<Graph.Vertex<Integer>> k4 = null;
		
		long starttime = System.currentTimeMillis();
		k4 = phaseOne(graph, highDegreeVertices);
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime);
		
		if(k4==null){
			starttime = System.currentTimeMillis();
			k4 = phaseTwo(graph, lowDegreeVertices);
			stoptime = System.currentTimeMillis();
			p2time = ""+(stoptime-starttime);
		}
		
		if(k4==null)
			found = "not found";
		return k4;
	}
	
	public  List<Graph.Vertex<Integer>> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> highDegreeVertices){
			
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

			//get a triangle in the neighbourhood
			DetectTriangle d = new DetectTriangle();
			List<Graph.Vertex<Integer>> triangle = d.detect(graph2);
			if(triangle!=null){
				List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
				k4Vertices.addAll(triangle);
				k4Vertices.add(x);
				
				return k4Vertices;
			}	
		}
		
		return null;
	}
	
	public  List<Graph.Vertex<Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
			
		for(Graph.Vertex<Integer> x: lowDegreeVertices){
			//get x's neighbourhood graph
			UndirectedGraph<Integer,Integer> graph2 = Utility.getNeighbourGraph(graph, x);

			//check for triangle in the neighbourhood
			DetectTriangle d = new DetectTriangle();
			List<Graph.Vertex<Integer>> triangle = d.detect(graph2);
			if(triangle!=null){
				List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
				k4Vertices.addAll(triangle);
				k4Vertices.add(x);
				
				return k4Vertices;
			}
			
		}
		return null;
	}

	//method to partition the vertices into low degree vertices and high degree vertices
	public  List<Graph.Vertex<Integer>>[] partitionVertices(UndirectedGraph<Integer,Integer> graph){
		List<Graph.Vertex<Integer>>[] vertices = new List[2];
		vertices[0] = new ArrayList<Graph.Vertex<Integer>>();
		vertices[1] = new ArrayList<Graph.Vertex<Integer>>();
		
		//get vertices
		Iterator<Graph.Vertex<Integer>> vertexIterator = graph.vertices();
				
		//get number of edges
		int noOfEdges = graph.getEdgeCount();
		

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
	
	public  String getResult(){
		String result = String.format("%-10s%-10s%-10s", p1time,p2time,found);
		return result;
	}
	
}
