package efficient.listing;
import java.io.IOException;
import java.util.*;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

/**
 * class that lists complete graphs of size 4 found in a graph
 * @author Chigozie Ekwonu
 *
 */
public class ListK4 {
	
	//instance variables
	private  String p1time; //stores time taken to execute operation
	private  int found;		//stores number of k4s found
	
	/**
	 * constructor to initialize instance variables
	 */
	public ListK4(){
		this.p1time = "-";
		this.found = 0;
	}
	
	/**
	 * main method which allows direct access to the class via the command line terminal.
	 * @param args		command line arguments
	 */
	public static void main(String [] args) throws IOException{
		try{
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);//create graph from file
			//run the listing operation
			ListK4 d = new ListK4();
			List<List<Vertex<Integer>>> k4s = d.detect(graph);
			String out = "";
			if(!k4s.isEmpty()){
				for(List<Graph.Vertex<Integer>> k4: k4s){
					out += Utility.printList(k4)+"\n";
				}
				out = String.format("K4 found%nVertices:%n%s", out);
				out += String.format("Number of K4s found: %d%n", k4s.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("K4 not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
			//print out results
			System.out.println(out);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	/**
	 * method to list k4s found in a graph
	 * @param graph		the graph to be checked
	 * @return			the vertices of each k4 found
	 */
	public List<List<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		//partition vertices into low and high degree vertices
		long start = System.currentTimeMillis();
		List<Graph.Vertex<Integer>>[] verticesPartition = partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		List<List<Graph.Vertex<Integer>>> k4List = new ArrayList<List<Graph.Vertex<Integer>>>();//list to store k4s found
		k4List.addAll(phaseOne(graph, highDegreeVertices));//add k4s found in phase one to k4 list
		k4List.addAll(phaseTwo(graph, lowDegreeVertices));//add k4s found in phase two to k4 list
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start); //calculate time taken
		found = k4List.size(); //store number of k4s found
		return k4List;
	}
	
	/**
	 * method to get k4s in a graph by checking for a k4 made up of high degree vertices only 
	 * @param graph					the graph to be checked
	 * @param highDegreeVertices	list of high degree vertices
	 * @return						list of vertices for each k4 found
	 */
	public List<List<Graph.Vertex<Integer>>> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> highDegreeVertices){
		Set<Set<Integer>> seen = new HashSet<Set<Integer>>(); //to prevent creating the same k4 more than once
		List<List<Graph.Vertex<Integer>>> k4List = new ArrayList<List<Graph.Vertex<Integer>>>();//list to store k4s found
			
		//for each high degree vertex, check its neighbourhood for triangles made up of high degree vertices only
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
			List<List<Graph.Vertex<Integer>>> triangles = new ListTriangles().detect(graph2);
			
			for(List<Graph.Vertex<Integer>> triangle: triangles){
				List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
				k4Vertices.addAll(triangle);
				k4Vertices.add(x);
				
				Set<Integer> k4VerticesElem = new HashSet<Integer>(); //set to store k4 vertices elements used to prevent duplication
				for(Graph.Vertex<Integer> ke: k4Vertices){
					k4VerticesElem.add(ke.getElement());
				}
				
				//check if this k4 was previously found. "contains" is true if k4 was not already found previously 
				boolean contains = seen.add(k4VerticesElem);						
				
				if(contains){  //if k4 was not previously found
					k4List.add(k4Vertices); //add k4 to list of k4s
				}
			}
		}
		
		return k4List;
	}
	
	/**
	 * method to look for k4s with at least a low degree vertex
	 * @param graph					the graph to be checked
	 * @param lowDegreeVertices		the list of low degree vertices
	 * @return						the vertices of each k4 found
	 */
	public List<List<Graph.Vertex<Integer>>> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same k4 more than once
		List<List<Graph.Vertex<Integer>>> k4List = new ArrayList<List<Graph.Vertex<Integer>>>();//list to store k4s found
			
		//for each low degree vertex
		for(Graph.Vertex<Integer> x: lowDegreeVertices){
			//get x's neighbourhood graph
			UndirectedGraph<Integer,Integer> graph2 = Utility.getNeighbourGraph(graph, x);

			//get the triangles in the neighbourhood
			List<List<Graph.Vertex<Integer>>> triangles = new ListTriangles().detect(graph2);
			
			for(List<Graph.Vertex<Integer>> triangle: triangles){
				List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
				k4Vertices.addAll(triangle);
				k4Vertices.add(x);
				
				Set<Integer> k4VerticesElem = new HashSet<Integer>(); //set to store k4 vertices elements used to prevent duplication
				for(Graph.Vertex<Integer> ke: k4Vertices){
					k4VerticesElem.add(ke.getElement());
				}
				
				//check if this k4 was previously found. "contains" is true if k4 was not already found previously
				boolean contains = marked.add(k4VerticesElem);
				
				//if k4 wasn't already previously found, add it to the list of k4s
				if(contains){ 
					k4List.add(k4Vertices);
				}
			}
		}
		
		return k4List;
	}

	/**
	 * method to partition the vertices into low degree vertices and high degree vertices
	 * @param graph		the graph whose vertices are to be partitioned
	 * @return			the partitions
	 */
	public List<Graph.Vertex<Integer>>[] partitionVertices(UndirectedGraph<Integer,Integer> graph){
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
	*	method to return the time taken to run the listing	
	*	and the number of K4s found
	*/
	public String getResult(){
		String result = String.format("%-10s%10d", p1time,found);
		return result;
	}
}
