package detectsubgraphs;
import java.util.*;

import general.Graph;
import general.MatrixOperation;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

public class DetectTriangle {
	
	private static String time = "";
	
	public static void main(String[] args) {
		UndirectedGraph<Integer,Integer> graph;
//		for(int a=0;a<15;a++){
			String fileName = "matrix3.txt";
//			String fileName = "generated_graphs\\size_7\\graph_7_0.2_2.txt";
//			String fileName = "generated_graphs\\size_15\\graph_15_0.7_3.txt";
			graph = Utility.makeGraphFromFile(fileName);
//			int[][] A = {{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1},{1,0,1,0,0},{1,0,1,0,0}};
//			graph = Utility.makeGraphFromAdjacencyMatrix(A);
			
			List<UndirectedGraph<Integer,Integer>> triangles = detect(graph);
			
			if(!triangles.isEmpty())
				for(UndirectedGraph<Integer, Integer> triangle: triangles)
					Utility.printGraph(triangle);
			else{
				System.out.println("Triangle not found");
			}

	}
	
	public static List<UndirectedGraph<Integer,Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		List<UndirectedGraph<Integer,Integer>> triangles = new ArrayList<UndirectedGraph<Integer,Integer>>();
		
		List[] verticesPartition = Utility.partitionVertices(graph);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		
		long starttime = System.currentTimeMillis();
		List<UndirectedGraph<Integer, Integer>> p1Triangles = phaseOne(graph, lowDegreeVertices);
		long stoptime = System.currentTimeMillis();
		if(!p1Triangles.isEmpty())
			triangles.addAll(p1Triangles);
		time += "phase1("+(stoptime-starttime)+")_";
		
		//if(triangle==null){
			starttime = System.currentTimeMillis();
			List<UndirectedGraph<Integer, Integer>> p2Triangles = phaseTwo(graph, lowDegreeVertices);
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
	
	public static List<UndirectedGraph<Integer,Integer>> phaseOne(UndirectedGraph<Integer, Integer> graph, List<Graph.Vertex<Integer>> lowDegreeVertices){
		Set<Integer> marked = new HashSet<Integer>(); //to prevent creating the same triangle more than once
		List<UndirectedGraph<Integer,Integer>> triangles = new ArrayList<UndirectedGraph<Integer, Integer>>();
		
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
						List<Graph.Vertex<Integer>> triList = new ArrayList<Graph.Vertex<Integer>>();
						boolean r1=false,r2=false,r3=false;
						triList.add(v); triList.add(other); triList.add(next);
						
						if(!marked.contains(v.getElement()))
							r1=marked.add(v.getElement());
						if(!marked.contains(other.getElement()))
							r2=marked.add(other.getElement());
						if(!marked.contains(next.getElement()))
							r2=marked.add(next.getElement());
						
						//check if such triangle with those vertices has been created previously
						if(!(!r1&&!r2&&!r3)){						
							UndirectedGraph<Integer,Integer> triangle = Utility.makeGraphFromVertexSet(graph,triList);
							triangles.add(triangle);
						}
					}
				}
					
			}
		}

		return triangles;
	}
	
	public static List<UndirectedGraph<Integer,Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph, List<Graph.Vertex<Integer>> lowDegreeVertices){
		Set<Integer> marked = new HashSet<Integer>(); //to prevent creating the same triangle more than once
		//if no triangle was found
		//remove all low degree vertices and get the adjacency matrix of resulting graph. 
		//Then detect a triangle from the square of the adjacency matrix
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
			graph.removeVertex(v);
		}
		
		List<UndirectedGraph<Integer,Integer>> triangles = new ArrayList<UndirectedGraph<Integer,Integer>>();
		 
		//get the adjacency matrix
		double[][] A = graph.getAdjacencyMatrix();
		double[][] aSquared = null; 
		try{
			aSquared = MatrixOperation.multiply(A, A);
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
							Graph.Vertex<Integer> v1 = vertexIndexMap.get(i);
							Graph.Vertex<Integer> v2 = vertexIndexMap.get(j);
							Graph.Vertex<Integer> v3 = vertexIndexMap.get(k);
							tVertices.add(v1);
							tVertices.add(v2);
							tVertices.add(v3);
							
							boolean r1=false,r2=false,r3=false;
							if(!marked.contains(v1.getElement()))
								r1=marked.add(v1.getElement());
							if(!marked.contains(v2.getElement()))
								r2=marked.add(v2.getElement());
							if(!marked.contains(v3.getElement()))
								r2=marked.add(v3.getElement());
							
							//check if such triangle with those vertices has been created previously
							if(!(!r1&&!r2&&!r3)){						
								UndirectedGraph<Integer, Integer> triangle = Utility.makeGraphFromVertexSet(graph, tVertices);
								triangles.add(triangle);
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
