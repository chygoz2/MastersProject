package listing;
import java.util.*;

import general.Graph;
import general.MatrixException;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class ListSimplicialVertex {
	
	private static String time = "";
	
	public static void main(String [] args){
//		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
//		
//		Graph.Vertex<Integer> v1 = graph.addVertex(1);
//		Graph.Vertex<Integer> v2 = graph.addVertex(2);
//		Graph.Vertex<Integer> v3 = graph.addVertex(3);
//		Graph.Vertex<Integer> v4 = graph.addVertex(4);
//		Graph.Vertex<Integer> v5 = graph.addVertex(5);
//		graph.addEdge(v1, v2);
//		graph.addEdge(v3, v2);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v1, v3);
////		graph.addEdge(v1, v5);
////		graph.addEdge(v3, v5);
//		graph.addEdge(v2, v5);
//		graph.addEdge(v2, v4);
//		graph.addEdge(v1, v4);
//		graph.addEdge(v4, v5);
//		
//		Utility.saveGraphToFile(graph, 0.7, 10);
//		String fileName = "generated_graphs\\size_5\\graph_5_0.7_10.txt";
//		String fileName = "generated_graphs\\size_6\\graph_6_0.6_2.txt";
		String fileName = "generated_graphs\\size_150\\graph_150_1.0_1.txt";

		for(int i=0; i<1; i++){
			UndirectedGraph<Integer, Integer> graph = Utility.makeGraphFromFile(fileName);
			long starttime = System.currentTimeMillis();
			List<Graph.Vertex<Integer>> simpVertex = detect(graph);
			long stoptime = System.currentTimeMillis();
			
			long timetaken = stoptime-starttime;
			
			if(!simpVertex.isEmpty()){
				for(Graph.Vertex<Integer> s: simpVertex)
					System.out.print(s.getElement()+", ");
			}else{
				System.out.println("Simplicial vertex not found");
			}
			System.out.println("\nTime taken in milliseconds: "+timetaken);
		}
	}
	
	public static List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		List[] verticesPartition = partitionVertices(graph);
		List<Graph.Vertex<Integer>> simplicialVertices = new ArrayList<Graph.Vertex<Integer>>();
		
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		long starttime = System.currentTimeMillis();
		simplicialVertices.addAll(phaseOne(graph, lowDegreeVertices));
		long stoptime = System.currentTimeMillis();
		time += "phase1("+(stoptime-starttime)+")_";

		starttime = System.currentTimeMillis();
		simplicialVertices.addAll(phaseTwo(graph, lowDegreeVertices, highDegreeVertices));
		stoptime = System.currentTimeMillis();
		time += "phase2("+(stoptime-starttime)+")_";
		
		if(!simplicialVertices.isEmpty())
			time+="1";
		else
			time+="0";
//		System.out.println(time);
		ListSimplicialVertex.resetTime();
		
		return simplicialVertices;
		
	}
	
	public static List<Graph.Vertex<Integer>> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		List<Graph.Vertex<Integer>> simplicialVertices = new ArrayList<Graph.Vertex<Integer>>();
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			if(graph.degree(v) > 0){
				//get the neighbours of v
				Iterator<Graph.Vertex<Integer>> vNeigh1 = graph.neighbours(v);
				
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
					simplicialVertices.add(v);
				}
			}
		}
		
		return simplicialVertices;
	}
	
	public static List<Graph.Vertex<Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices, Collection<Graph.Vertex<Integer>> highDegreeVertices){
		
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
			graph.removeVertex(v);
		}	
		
		List<Graph.Vertex<Integer>> simplicialVertices = new ArrayList<Graph.Vertex<Integer>>();
		//create a map between vertices and matrix indices
		Map<Integer, Integer> vertexIndexMap = new HashMap<Integer, Integer>();
		int a = 0;
		
		Iterator<Graph.Vertex<Integer>> vIt1 = graph.vertices();
		
		while(vIt1.hasNext()){
			Graph.Vertex<Integer> v = vIt1.next();
			vertexIndexMap.put(v.getElement(), a);
			a++;
		}
		
		int[][] A = graph.getAdjacencyMatrix();
		
		//put 1's on the diagonal
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
		while(vIt1.hasNext()){
			//get v's neighbours

			Graph.Vertex<Integer> x = vIt1.next();
			if(!markedVertices.contains(graph.getVertexWithElement(x.getElement()))) //do check only on unmarked vertices from phase 2
			{
				Iterator<Graph.Vertex<Integer>> vNeigh = graph.neighbours(x);
				
				boolean isSimplicial = true;
				
				//perform simplicial vertices check of theorem 1
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
		//double alpha = 2.376; //constant from Coppersmith-Winograd matrix multiplication algorithm
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
	
	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
}
