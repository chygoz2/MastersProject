package efficient.listing;
import java.io.IOException;
import java.util.*;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class ListK4 {
	
	private  String p1time = "-";
	private  String found = "found";
	
	public static void main(String [] args) throws IOException{
		UndirectedGraph<Integer,Integer> graph = null;
		try{
			graph = Utility.makeGraphFromFile(args[0]);
			ListK4 d = new ListK4();
			List<List<Vertex<Integer>>> k4s = d.detect(graph);
			System.out.println("Number of k4 found: "+k4s.size());
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	public List<List<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		//partition vertices
		long start = System.currentTimeMillis();
		List<Graph.Vertex<Integer>>[] verticesPartition = partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		List<List<Graph.Vertex<Integer>>> k4List = new ArrayList<List<Graph.Vertex<Integer>>>();
		k4List.addAll(phaseOne(graph, highDegreeVertices));
		k4List.addAll(phaseTwo(graph, lowDegreeVertices));
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start);
		if(k4List.isEmpty()){
			found = "not found";
		}
		return k4List;
	}
	
	public List<List<Graph.Vertex<Integer>>> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> highDegreeVertices){
		Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same k4 more than once
		List<List<Graph.Vertex<Integer>>> k4List = new ArrayList<List<Graph.Vertex<Integer>>>();
			
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
				
				Set<Integer> k4VerticesElem = new HashSet<Integer>(); //set to store k4 vertices elements
				for(Graph.Vertex<Integer> ke: k4Vertices){
					k4VerticesElem.add(ke.getElement());
				}
				
				//check in the marked list for an entry that contains all 4 vertex elements
				boolean contains = marked.add(k4VerticesElem);						
				
				if(contains){
					k4List.add(k4Vertices);
				}
			}
		}
		
		return k4List;
	}
	
	public List<List<Graph.Vertex<Integer>>> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same k4 more than once
		List<List<Graph.Vertex<Integer>>> k4List = new ArrayList<List<Graph.Vertex<Integer>>>();
			
		for(Graph.Vertex<Integer> x: lowDegreeVertices){
			//get x's neighbourhood graph
			UndirectedGraph<Integer,Integer> graph2 = Utility.getNeighbourGraph(graph, x);

			//get the triangles in the neighbourhood
			List<List<Graph.Vertex<Integer>>> triangles = new ListTriangles().detect(graph2);
			
			for(List<Graph.Vertex<Integer>> triangle: triangles){
				List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
				k4Vertices.addAll(triangle);
				k4Vertices.add(x);
				
				Set<Integer> k4VerticesElem = new HashSet<Integer>(); //set to store k4 vertices elements
				for(Graph.Vertex<Integer> ke: k4Vertices){
					k4VerticesElem.add(ke.getElement());
				}
				
				//check in the marked list for an entry that contains all 4 vertex elements
				boolean contains = marked.add(k4VerticesElem);
				
				if(contains){
					k4List.add(k4Vertices);
				}
			}
		}
		
		return k4List;
	}

	//method to partition the vertices into low degree vertices and high degree vertices
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
	*	and whether a k4 was found or not
	*/
	public String getResult(){
		String result = String.format("%-10s%-10s", p1time,found);
		return result;
	}
}
