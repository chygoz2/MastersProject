package efficient.listing;
import java.util.*;

import exception.GraphFileReaderException;
import exception.MatrixException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

/**
 * class to list simplicial vertices in a graph
 * @author Chigozie Ekwonu
 *
 */
public class ListSimplicialVertices {
	
	//instance variables
	private  String p1time; //records time taken to execute
	private  int found;		//records number of simplicial vertices found
	
	/**
	 * constructor to initialize instance variables
	 */
	public ListSimplicialVertices(){
		this.p1time = "-";
		this.found = 0;
	}
	
	/**
	 * main method which allows direct access to the class via the command line terminal.
	 * @param args		command line arguments
	 */
	public static void main(String [] args){
		try{
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);//create graph from file
			//run the listing operation
			ListSimplicialVertices d = new ListSimplicialVertices();
			List<Vertex<Integer>> simpVertex = d.detect(graph);
			String out = "";
			if(!simpVertex.isEmpty()){
				out = Utility.printList(simpVertex);
				out = String.format("Simplicial vertices found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Simplicial vertices not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
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
	 * method that combines simplicial vertices found in phases one and two and returns list of
	 * simpplicial vertices found
	 * @param graph		the graph to be checked
	 * @return			the list of simplicial vertices
	 */
	public List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		//partition vertices into low and high degree vertices
		List[] verticesPartition = partitionVertices(graph);
		List<Graph.Vertex<Integer>> simplicialVertices = new ArrayList<Graph.Vertex<Integer>>();
		
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		long starttime = System.currentTimeMillis();
		simplicialVertices.addAll(phaseOne(graph, lowDegreeVertices)); //add simplicial vertices from phase one
		simplicialVertices.addAll(phaseTwo(graph, lowDegreeVertices, highDegreeVertices));//add simplicial vertices from phase two
		long stoptime = System.currentTimeMillis();
		p1time = ""+(stoptime-starttime); //calculate time taken
		
		found = simplicialVertices.size(); //record number of simplicial vertices found
		return simplicialVertices;
		
	}
	
	/**
	 * method that returns all simplicial vertices of low degree
	 * @param graph					the graph to be checked
	 * @param lowDegreeVertices		the list of low degree vertices
	 * @return						the list of simplicial vertices found
	 */
	public List<Graph.Vertex<Integer>> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		List<Graph.Vertex<Integer>> simplicialVertices = new ArrayList<Graph.Vertex<Integer>>();//list of simplicial vertices found
		//for each low degree vertex v,
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			if(graph.degree(v) > 0){
				//get the neighbours of v
				Iterator<Graph.Vertex<Integer>> vNeigh1 = graph.neighbours(v);
				
				boolean isSimplicial = true;
				//check if the neighbours of v are adjacent with each other
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
			
				//if v is simplicial, add it to the list of simplicial vertices
				if(isSimplicial){ 
					simplicialVertices.add(v);
				}
			}
		}
		
		return simplicialVertices;
	}
	
	/**
	 * method that returns all simplicial vertices of high degree
	 * @param graph					the graph to be checked
	 * @param lowDegreeVertices		the list of low degree vertices
	 * @param highDegreeVertices	the list of high degree vertices
	 * @return						the list of simplicial vertices found
	 */
	public List<Graph.Vertex<Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices, Collection<Graph.Vertex<Integer>> highDegreeVertices){
		
		//mark all high degree vertices that have a low degree neighbour
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
		
		List<Graph.Vertex<Integer>> simplicialVertices = new ArrayList<Graph.Vertex<Integer>>();
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
		
		int[][] A = graph.getAdjacencyMatrix(); //get the adjacency matrix of graph
		
		//put 1's on the diagonal of the adjacency matrix
		for(int i=0;i<A.length;i++){
			A[i][i] = 1;
		}
		
		//square the resulting adjacency matrix
		int[][] aSquared;
		try {
			aSquared = Utility.multiplyMatrix(A, A);
		} catch (MatrixException e) {
			return simplicialVertices;
		}
		
		vIt1 = graph.vertices();
		/*
		 *for every unmarked vertex from earlier, check if A^2(x,y)=A^2(x,x) from theorem 1 of Kloks et al.. if condition
		 *is satisfied, then vertex is simplicial 
		 */
		while(vIt1.hasNext()){
			//get v's neighbours

			Graph.Vertex<Integer> x = vIt1.next();
			if(!markedVertices.contains(graph.getVertexWithElement(x.getElement()))) //do check only on unmarked vertices 
			{
				Iterator<Graph.Vertex<Integer>> vNeigh = graph.neighbours(x);
				
				boolean isSimplicial = true;
				
				//perform simplicial vertices check of theorem 1 of Kloks et al.
				while(vNeigh.hasNext()){
					Graph.Vertex<Integer> y = vNeigh.next();
					int i = (int)aSquared[vertexIndexMap.get(x.getElement())][vertexIndexMap.get(y.getElement())];
					int j = (int)aSquared[vertexIndexMap.get(x.getElement())][vertexIndexMap.get(x.getElement())];
					
					if(i!=j){
						isSimplicial = false;
						break;
					}
				}
				
				if(isSimplicial){
					simplicialVertices.add(x);
				}
			}
		}
		
		//get simplicial vertices
		return simplicialVertices;
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
		//double alpha = 3; //constant is exponent of matrix multiplication for standard matrix multiplication
		double alpha = 3;
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
	*	method to return the time taken to run the listing	
	*	and the number of simplicial vertices found
	*/
	public String getResult(){
		String result = String.format("%-10s%10d", p1time,found);
		return result;
	}
	
}
