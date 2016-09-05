package efficient.detection;
import java.util.*;

import exception.GraphFileReaderException;
import exception.MatrixException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

/**
 * class to detect the presence of a simplicial vertex in a graph
 * @author Chigozie Ekwonu
 *
 */
public class DetectSimplicialVertex {
	
	//instance variables
	private  String p1time; //measures time taken to execute phase one
	private  String p2time; //measures time taken to execute phase two
	private  String found; //stores results of the operation
	
	/**
	 * constructor to initialize instance variables
	 */
	public DetectSimplicialVertex(){
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
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);//create graph from file
			//run detection on graph
			DetectSimplicialVertex d = new DetectSimplicialVertex();
			Graph.Vertex<Integer> simpVertex = d.detect(graph);
			String out = "";
			if(simpVertex!=null){
				out = String.format("Simplicial vertex found%nOne such vertex: %s%n", simpVertex.getElement());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Simplicial vertex not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
			//print out result
			System.out.println(out);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	/**
	 * method that combines phase one and phase two to detect a simplicial vertex in a graph
	 * @param graph		the graph to be checked
	 * @return			a simplicial vertex if found
	 */
	public  Graph.Vertex<Integer> detect(UndirectedGraph<Integer,Integer> graph){
		
		//partition vertices into low and high degree
		List[] verticesPartition = partitionVertices(graph);
		Graph.Vertex<Integer> simplicialVertex = null;
		
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		//measure execution time of phase one
		long starttime = System.currentTimeMillis();
		simplicialVertex = phaseOne(graph, lowDegreeVertices);
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime);
		
		if(simplicialVertex==null){ //if no simplicial vertex was found in phase one
			//measure execution time of phase two
			starttime = System.currentTimeMillis();
			simplicialVertex = phaseTwo(graph, lowDegreeVertices, highDegreeVertices);
			stoptime = System.currentTimeMillis();
			p2time = ""+(stoptime-starttime);
		}
		
		if(simplicialVertex==null)
			found = "not found";
		
		return simplicialVertex;
		
	}
	
	/**
	 * method to detect a simplicial vertex in a graph. Looks for simplicial vertices of low degree
	 * @param graph					the graph to be checked
	 * @param lowDegreeVertices		list of low degree vertices
	 * @return						a simplicial vertex if found
	 */
	public  Graph.Vertex<Integer> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			if(graph.degree(v) > 0){
				//get the neighbours of v
				Iterator<Graph.Vertex<Integer>> vNeigh1 = graph.neighbours(v);
				
				//check if every pair of its neighbours is adjacent
				boolean isSimplicial = true;
				
				here:
					while(vNeigh1.hasNext()){
						Graph.Vertex<Integer> one = vNeigh1.next();
						Iterator<Graph.Vertex<Integer>> vNeigh2 = graph.neighbours(v);
						while(vNeigh2.hasNext()){
							Graph.Vertex<Integer> two = vNeigh2.next();
							if(!one.equals(two)){ //prevent checking if a vertex has an edge with itself
								if(!graph.containsEdge(one, two)){
									isSimplicial = false;
									break here;
								}
							}
						}
					}
			
				if(isSimplicial){
					return v;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * method to check the presence of a simplicial vertices of high degree whose neighbours
	 * are also high degree vertices in a graph
	 * @param graph2				the graph to be checked
	 * @param lowDegreeVertices		the list of low degree vertices
	 * @param highDegreeVertices	the list of high degree vertices
	 * @return						a simplicial vertex if found
	 */
	public  Graph.Vertex<Integer> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices, Collection<Graph.Vertex<Integer>> highDegreeVertices){
		
		//mark high degree vertices that have a low degree neighbour
		List<Graph.Vertex<Integer>> markedVertices = new ArrayList<Graph.Vertex<Integer>>();
		
		for(Graph.Vertex<Integer> v: highDegreeVertices){
			Iterator<Graph.Vertex<Integer>> vNeigh = graph.neighbours(v);
			while(vNeigh.hasNext()){
				Graph.Vertex<Integer> vv = vNeigh.next();
				if(!highDegreeVertices.contains(vv)){
					markedVertices.add(v);
					break;
				}
			}	
		}
		
		//remove all low degree vertices from graph
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			graph.removeVertex(graph.getVertexWithElement(v.getElement()));
		}	
		
		int[][] A = graph.getAdjacencyMatrix(); //get adjacency matrix of graph
		
		//put 1's on the diagonal of the adjacency matrix
		for(int i=0;i<A.length;i++){
			A[i][i] = 1;
		}
		
		/*create a mapping between vertices and matrix indices. Useful in knowing which matrix index
		 * belongs to which vertex*/ 
		Map<Integer, Integer> vertexIndexMap = new HashMap<Integer, Integer>();
		int a = 0;
		
		Iterator<Graph.Vertex<Integer>> vIt1 = graph.vertices();
		
		while(vIt1.hasNext()){
			Graph.Vertex<Integer> v = vIt1.next();
			vertexIndexMap.put(v.getElement(), a);
			a++;
		}
		
		//square the resulting adjacency matrix
		int[][] aSquared;
		try {
			aSquared = Utility.multiplyMatrix(A, A);
		} catch (MatrixException e) {
			return null;
		}
		
		vIt1 = graph.vertices();
		/*
		 *for every unmarked vertex from earlier, check if A^2(x,y)=A^2(x,x) from theorem 1 of Kloks et al. if condition
		 *is satisfied, then vertex is simplicial 
		 */
		while(vIt1.hasNext()){
			Graph.Vertex<Integer> x = vIt1.next();
			if(!markedVertices.contains(graph.getVertexWithElement(x.getElement()))) //do check only on unmarked vertices
			{
				Iterator<Graph.Vertex<Integer>> vNeigh = graph.neighbours(x);
				
				boolean isSimplicial = true;
				
				//perform simplicial vertices check of theorem 1
				while(vNeigh.hasNext()){
					Graph.Vertex<Integer> y = vNeigh.next();
					int xx = vertexIndexMap.get(x.getElement());
					int yy = vertexIndexMap.get(y.getElement());
					int i = aSquared[xx][yy];
					int j = aSquared[xx][xx];
					
					if(i!=j){
						isSimplicial = false;
						break;
					}
				}
				
				if(isSimplicial){
					return x;
				}
			}
		}
		
		return null;
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
		
		//calculate D for Vertex partitioning
		double alpha = 3; //exponent of matrix multiplication from standard matrix multiplication
		double pow = (alpha-1)/(alpha+1);
		double D = Math.pow(noOfEdges, pow);
		
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
