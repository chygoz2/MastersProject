package efficientdetection;
import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectTriangle {
	
	private static String time = "";
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph;
//		for(int a=0;a<15;a++){
//			String fileName = "matrix3.txt";
//			String fileName = "generated_graphs\\size_7\\graph_7_0.2_2.txt";
//			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
			String fileName = "generated_graphs\\size_15\\graph_15_1.0_1.txt";
			graph = Utility.makeGraphFromFile(fileName);
//			int[][] A = {{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1},{1,0,1,0,0},{1,0,1,0,0}};
//			graph = Utility.makeGraphFromAdjacencyMatrix(A);
			
			long starttime = System.currentTimeMillis();
			List<Collection<Graph.Vertex<Integer>>> triangles = detect(graph);
//			List<UndirectedGraph<Integer,Integer>> triangles = DetectKL.detect(graph,3);
			long stoptime = System.currentTimeMillis();
			
			long timetaken = stoptime-starttime;
			
			
			if(!triangles.isEmpty()){
				for(Collection<Graph.Vertex<Integer>> triangle: triangles){
					Utility.printGraph(Utility.makeGraphFromVertexSet(graph, triangle));
				}
				System.out.println("Time taken in milliseconds: "+timetaken);
				System.out.println(triangles.size());
			}
			else{
				System.out.println("Triangle not found");
			}

	}
	
	public static List<Collection<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		List<Collection<Graph.Vertex<Integer>>> triangles = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		
		List<Graph.Vertex<Integer>>[] verticesPartition = Utility.partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		
		long starttime = System.currentTimeMillis();
		List<Collection<Graph.Vertex<Integer>>> p1Triangles = phaseOne(graph, lowDegreeVertices);
		long stoptime = System.currentTimeMillis();
		if(!p1Triangles.isEmpty())
			triangles.addAll(p1Triangles);
		time += "phase1("+(stoptime-starttime)+")_";
		
		//if(triangle==null){
			starttime = System.currentTimeMillis();
			List<Collection<Graph.Vertex<Integer>>> p2Triangles = phaseTwo(graph, lowDegreeVertices);
			stoptime = System.currentTimeMillis();
			
			if(!p2Triangles.isEmpty())
				triangles.addAll(p2Triangles);
			time += "phase2("+(stoptime-starttime)+")_";
		//}
		
		if(!triangles.isEmpty())
			time+="1";
		else
			time+="0";
		//System.out.println(time);
		DetectTriangle.resetTime();
		
		return triangles;
	}
	
	public static List<Collection<Graph.Vertex<Integer>>> phaseOne(UndirectedGraph<Integer, Integer> graph, List<Graph.Vertex<Integer>> lowDegreeVertices){
		List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same triangle more than once
		List<Collection<Graph.Vertex<Integer>>> triangles = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			//get connecting edges to v
			Iterator<Graph.Edge<Integer>> eIt = graph.connectingEdges(v);
			while(eIt.hasNext()){
				UndirectedGraph.UnEdge e = (UndirectedGraph.UnEdge)eIt.next();
				//get the other vertex of the edge
				Graph.Vertex<Integer> other;
				if(e.getSource().equals(v))
					other = e.getDestination();
				else
					other = e.getSource();
				//get other vertices neighbours
				Iterator<Graph.Vertex<Integer>> otherIt = graph.neighbours(other);
				while(otherIt.hasNext()){
					Graph.Vertex<Integer> next = otherIt.next();
					//check for presence of edge between v and next
					//presence of an edge indicates a triangle
					if(graph.containsEdge(v, next)){
						List<Graph.Vertex<Integer>> triList = new ArrayList<Graph.Vertex<Integer>>(); //list to store triangle vertices
						Set<Integer> triListElem = new HashSet<Integer>(); //set to store triangle vertices elements
						
						triList.add(v); triList.add(other); triList.add(next);
						triListElem.add(v.getElement()); triListElem.add(other.getElement());
						triListElem.add(next.getElement());
						
						//check in the marked list for an entry that contains all 3 vertex elements
						boolean contains = false;
						
						for(Set<Integer> s: marked){
							if(s.containsAll(triListElem)){
								contains = true;
								break;
							}
						}
						
						//check if such triangle with those vertices has been created previously
						if(!contains){						
							triangles.add(triList);
							marked.add(triListElem);
						}
					}
				}
					
			}
		}
		return triangles;
	}
	
	public static List<Collection<Graph.Vertex<Integer>>> phaseTwo(UndirectedGraph<Integer,Integer> graph2, List<Graph.Vertex<Integer>> lowDegreeVertices){
		List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same triangle more than once
		//if no triangle was found
		//remove all low degree vertices and get the adjacency matrix of resulting graph. 
		//Then detect a triangle from the square of the adjacency matrix
		UndirectedGraph<Integer,Integer> graph = graph2.clone();
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			graph.removeVertex(v);
		}
		
		List<Collection<Graph.Vertex<Integer>>> triangles = new ArrayList<Collection<Graph.Vertex<Integer>>>();
		 
		//get the adjacency matrix
		double[][] A = graph.getAdjacencyMatrix();
		double[][] aSquared = null; 
		try{
			aSquared = Utility.multiplyMatrix(A, A);
		}catch(MatrixException e){
			if(e.getStatus()==1)
				System.out.println("Invalid matrix dimensions found");
			return triangles;
		}
		
		//create mapping of matrix index to graph vertex
		List<Graph.Vertex<Integer>> vertexIndexMap = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vIt = graph.vertices();
		while(vIt.hasNext()){
			vertexIndexMap.add(vIt.next());
		}
		
		//look for end vertices of a triangle from the square of the adjacency matrix
		for(int i=0; i<aSquared.length; i++){
			for(int j=i+1; j<aSquared.length; j++){
				if((int)aSquared[i][j]>0 && (int)A[i][j]==1){ //end vertices found
					//look for the intermediate index to make up the P3
					for(int k=0; k<A.length; k++){
						if(k!=i && k!=j && (int)A[k][i]==1 && (int)A[k][j]==1){
							//at this point, i, j and k represent matrix indices of the vertices which form the triangle
							//get the actual vertices and create a list of them
							List<Graph.Vertex<Integer>> tVertices = new ArrayList<Graph.Vertex<Integer>>();
							Set<Integer> triListElem = new HashSet<Integer>(); //list to store triangle vertices elements
							
							Graph.Vertex<Integer> v1 = vertexIndexMap.get(i);
							Graph.Vertex<Integer> v2 = vertexIndexMap.get(j);
							Graph.Vertex<Integer> v3 = vertexIndexMap.get(k);
							tVertices.add(v1);	triListElem.add(v1.getElement());
							tVertices.add(v2);	triListElem.add(v2.getElement());
							tVertices.add(v3);	triListElem.add(v3.getElement());
							
							//check in the marked list for an entry that contains all 3 vertex elements
							boolean contains = false;
							
							for(Set<Integer> s: marked){
								if(s.containsAll(triListElem)){
									contains = true;
									break;
								}
							}
							
							//check if such triangle with those vertices has been created previously
							if(!contains){						
								triangles.add(tVertices);
								marked.add(triListElem);
							}
						}
					}
				}
			}
		}
		
		return triangles;
	}
	
	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
}
