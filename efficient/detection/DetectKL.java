package efficient.detection;
import java.util.*;

import efficient.listing.ListKL;
import efficient.listing.ListTriangles;
import exception.GraphFileReaderException;
import general.*;
import general.Graph.Vertex;

/**
 * class that detects the presence of a complete subgraph of a given size
 * @author Chigozie Ekwonu
 *
 */
public class DetectKL {
	
	//instance variables
	private String p1time; //measures time taken for phase one to execute
	private String p2time; //measures time taken for phase two to execute
	private  String found; //stores whether the complete subgraph was found
	
	public DetectKL(){
		this.p1time = "-";
		this.p2time = "-";
		this.found = "found";
	}
	
	public static void main(String [] args){
		try{
			UndirectedGraph<Integer,Integer> graph = null;
			graph = Utility.makeGraphFromFile(args[0]);
			
			DetectKL d = new DetectKL();
			if(args.length>1){
				int l = Integer.parseInt(args[1]);
				List<Graph.Vertex<Integer>> kl = d.detect(graph,l);
				String out = "";
				if(kl!=null){
					out = Utility.printList(kl);
					out = String.format("K"+l+" found%nVertices: %s%n", out);
					out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				}else{
					out = String.format("K"+l+" not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				}
				System.out.println(out);
			}else{
				System.out.println("Please enter the size of the complete graph to be found");
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}
		catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	/**
	 * method that calls phase one and phase two methods to detect for a Kl subgraph
	 * @param graph			the graph to be checked
	 * @param l				the size of the complete subgraph to be found
	 * @return				the vertices of the complete subgraph if found
	 */
	public  List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph, int l){
		List<Graph.Vertex<Integer>> kl=null;
		if(l<=graph.size()){
			//partition vertices into low and high degree vertices
			List<Graph.Vertex<Integer>>[] verticesPartition = partitionVertices(graph, l);
			List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
			List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
			
			//measure time taken for phase one
			long starttime = System.currentTimeMillis();
			kl = phaseOne(graph, l, lowDegreeVertices);
			long stoptime = System.currentTimeMillis();
			p1time = ""+(stoptime-starttime);
			
			if(kl==null){
				//measure time taken for phase two
				starttime = System.currentTimeMillis();
				kl = phaseTwo(graph, l, highDegreeVertices);
				stoptime = System.currentTimeMillis();
				p2time = ""+(stoptime-starttime);
			}
		}
		
		if(kl==null)
			found = "not found";
		
		return kl;
	}
	
	/**
	 * method to detect a Kl in a graph. Looks for a Kl that has at least one low degree vertex
	 * @param graph					the graph to be checked
	 * @param l						the size of the subgraph to be detected
	 * @param lowDegreeVertices		the list of low degree vertices
	 * @return						the vertices of the complete subgraph if found
	 */
	public List<Graph.Vertex<Integer>> phaseOne(UndirectedGraph<Integer,Integer> graph, int l, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		List<Graph.Vertex<Integer>> kl = null;
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			//if l = 1, return the vertex
			if (l==1){
				kl = new ArrayList<Graph.Vertex<Integer>>();
				kl.add(v);
				return kl;
			}
			//get the neighbour graph of V and check if it contains a K(l-1)
			UndirectedGraph<Integer,Integer> vn = Utility.getNeighbourGraph(graph, v);
			List<List<Graph.Vertex<Integer>>> kls = find(vn, (l-1));
			if(!kls.isEmpty()){
				kl = kls.get(0);
				kl.add(v);
				break;
			}
		}
		return kl;
	}
	
	/**
	 * method to detect a Kl. Looks for a Kl that consists of only high degree vertices
	 * @param graph					the graph to be checked
	 * @param l						the size of the complete subgraph to be detected
	 * @param highDegreeVertices	the list of high degree vertices
	 * @return						the vertices of the complete subgraph if found	
	 */	
	public List<Graph.Vertex<Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph, int l, Collection<Graph.Vertex<Integer>> highDegreeVertices){
		//make a graph from all high degree vertices
		UndirectedGraph<Integer,Integer> graph2 = Utility.makeGraphFromVertexSet(graph, highDegreeVertices);
		
		//check if the graph induced by the high degree vertices contains a kl
		List<List<Graph.Vertex<Integer>>> kls = find(graph2, l);
		
		if(!kls.isEmpty())
			return kls.get(0);
		
		return null;
	}
	
	/**
	 * method to detect the presence of a complete graph of size l
	 * @param graph graph to be checked
	 * @param l	size of the complete subgraph to be found
	 * @return	the list of complete subgraphs
	 */
	public  List<List<Graph.Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph, int l){
		return new ListKL().detect(graph,l);
	}
	
	/**
	 * method to partition the vertices into low degree vertices and high degree vertices
	 * @param graph			the graph to be partitioned
	 * @param l				the size of the complete subgraph to be detected
	 * @return				the vertices of the subgraph if found
	 */
	public  List<Graph.Vertex<Integer>>[] partitionVertices(UndirectedGraph<Integer,Integer> graph, int l){
		List<Graph.Vertex<Integer>>[] vertices = new List[2];
		vertices[0] = new ArrayList<Graph.Vertex<Integer>>();
		vertices[1] = new ArrayList<Graph.Vertex<Integer>>();
		
		//get vertices
		Iterator<Graph.Vertex<Integer>> vertexIterator = graph.vertices();
		
		//get number of edges
		int noOfEdges = graph.getEdgeCount();
					
		//calculate D for Vertex partitioning
		double D = 0;
		if(l%3==0)
			D = Math.sqrt(noOfEdges);
		else{
			double alpha = 3; //exponent of matrix multiplication
			double beta = (alpha * l) / 3;
			
			//calculate D for Vertex partitioning
			D = Math.pow(noOfEdges, ((beta - 1)*(2*beta-alpha+1)));
		}
		
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
