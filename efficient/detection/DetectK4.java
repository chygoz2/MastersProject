package efficient.detection;
import java.util.*;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

/**
 * class that detects the presence of a K4 in a graph
 * @author Chigozie Ekwonu
 *
 */
public class DetectK4 {
	
	//instance variables
	private  String p1time; //stores time taken for phase one to execute
	private  String p2time; //stores time taken for phase two to execute
	private  String found; //stores outcome of the test
	
	/**
	 * constructor to initialize instance variables
	 */
	public DetectK4(){
		this.p1time = "-";
		this.p2time = "-";
		this.found = "found";
	}
	
	/**
	 * main method which allows direct access to the class via the command line terminal.
	 * @param args		command line arguments
	 */
	public static void main(String [] args){
		try{
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]); //create graph from file
			
			//run K4 detection on graph
			DetectK4 d = new DetectK4();
			List<Graph.Vertex<Integer>> k4 = d.detect(graph);
			String out = "";
			if(k4!=null){
				out = Utility.printList(k4);
				out = String.format("K4 found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("K4 not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
			//print out results
			System.out.println(out);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}
		catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	/**
	 * method to detect a K4. Calls phaseOne and phaseTwo methods 
	 * @param graph			the graph to be checked
	 * @return				the vertices of the k4 if found
	 */
	public  List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){	
		//partition vertices into low and high degree vertices
		List<Graph.Vertex<Integer>>[] verticesPartition = partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		List<Graph.Vertex<Integer>> k4 = null;
		
		//measure time taken for phase one to execute
		long starttime = System.currentTimeMillis();
		k4 = phaseOne(graph, highDegreeVertices);
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime);
		
		if(k4==null){ //if k4 was not found in phase one
			//measure time taken for phase two to execute
			starttime = System.currentTimeMillis();
			k4 = phaseTwo(graph, lowDegreeVertices);
			stoptime = System.currentTimeMillis();
			p2time = ""+(stoptime-starttime);
		}
		
		if(k4==null)
			found = "not found";
		return k4;
	}
	
	/**
	 * method to check for a k4 in a graph by checking for a k4 made up of high degree vertices only
	 * @param graph					the graph to be checked
	 * @param highDegreeVertices	the list of high degree vertices
	 * @return						the vertices of the K4 if found
	 */
	public  List<Graph.Vertex<Integer>> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> highDegreeVertices){
			
		/*check the intersection of the neighbourhood vertices of each high degree vertex and the
		high degree vertices*/ 
		for(Graph.Vertex<Integer> x: highDegreeVertices){
			//get x's neighbourhood graph
			Iterator<Graph.Vertex<Integer>> nXIter = graph.neighbours(x);

			//get the intersection of the neighbourhood vertices of x with the high degree vertices
			List<Graph.Vertex<Integer>> nXList = new ArrayList<Graph.Vertex<Integer>>();
			while(nXIter.hasNext()){
				Graph.Vertex<Integer> v = nXIter.next();
				if(highDegreeVertices.contains(v))
					nXList.add(v);
			}
			
			//if x's neighbourhood does not have any high degree vertex, then check the next high degree vertex
			if(nXList.isEmpty())
				continue;
			
			//make graph from new list of vertices
			UndirectedGraph<Integer,Integer> graph2 = Utility.makeGraphFromVertexSet(graph, nXList);

			//get a triangle in the neighbourhood
			DetectTriangle d = new DetectTriangle();
			List<Graph.Vertex<Integer>> triangle = d.detect(graph2);
			if(triangle!=null){ //if triangle is found
				List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
				k4Vertices.addAll(triangle); //add triangles vertices
				k4Vertices.add(x); //add the last vertex to complete the k4
				
				return k4Vertices; //return the vertices of the k4
			}	
		}
		
		return null; //no k4 was found
	}
	
	/**
	 * method to check for a k4 by looking for a k4 with at least one low degree vertex
	 * @param graph					the graph to be checked	
	 * @param lowDegreeVertices		the list of low degree vertices
	 * @return						the vertices of the k4 if found
	 */
	public  List<Graph.Vertex<Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
			
		for(Graph.Vertex<Integer> x: lowDegreeVertices){
			//get x's neighbourhood graph
			UndirectedGraph<Integer,Integer> graph2 = Utility.getNeighbourGraph(graph, x);

			//check for triangle in the neighbourhood
			DetectTriangle d = new DetectTriangle();
			List<Graph.Vertex<Integer>> triangle = d.detect(graph2);
			if(triangle!=null){ //if triangle is found, create a k4 from the triangles vertices and the low degree vertex 
				List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
				k4Vertices.addAll(triangle);
				k4Vertices.add(x);
				
				return k4Vertices;
			}
			
		}
		return null; //no k4 was found
	}

	/**
	 * method to partition the vertices into low degree vertices and high degree vertices
	 * @param graph		the graph whose vertices are to be partitioned
	 * @return			the partitions
	 */
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
	
	/**
	 * method to return the time taken for the detection and the result
	 * @return		the result for analysis
	 */
	public  String getResult(){
		String result = String.format("%-10s%-10s%-10s", p1time,p2time,found);
		return result;
	}
	
}
