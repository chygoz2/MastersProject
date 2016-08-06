package efficientdetection;
import java.util.*;

import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectK4 {
	
	private static String time = "";
	
	public static void main(String [] args){
//		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
//		
//		Graph.Vertex<Integer> v0 = graph.addVertex(0);
//		Graph.Vertex<Integer> v1 = graph.addVertex(1);
//		Graph.Vertex<Integer> v2 = graph.addVertex(2);
//		Graph.Vertex<Integer> v3 = graph.addVertex(3);
//		Graph.Vertex<Integer> v4 = graph.addVertex(4);
//		graph.addEdge(v0, v1);
//		graph.addEdge(v2, v1);
//		graph.addEdge(v2, v3);
//		graph.addEdge(v0, v2);
//		graph.addEdge(v0, v4);
////		graph.addEdge(v2, v4);
//		graph.addEdge(v1, v4);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v1, v3);
//		graph.addEdge(v0, v3);
//		
////		Utility.saveGraphToFile(graph, 1.0, 1);
		String fileName = "matrix4.txt";
		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(fileName);
		
		long starttime = System.currentTimeMillis();
		List<UndirectedGraph<Integer,Integer>> k4List = detect(graph);
		long stoptime = System.currentTimeMillis();
		
		long timetaken = stoptime-starttime;
		
		for(UndirectedGraph<Integer,Integer> k4: k4List){
			Utility.printGraph(k4);
		}
		System.out.println("Time taken in milliseconds: "+timetaken);
					
	}
	
	public static List<UndirectedGraph<Integer,Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		time+="size_"+graph.size()+"_";
		
		List<Graph.Vertex<Integer>>[] verticesPartition = Utility.partitionVertices(graph);
		
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		List<UndirectedGraph<Integer,Integer>> k4List = new ArrayList<UndirectedGraph<Integer,Integer>>();
		long starttime = System.currentTimeMillis();
		k4List.addAll(phaseOne(graph, highDegreeVertices));
		long stoptime = System.currentTimeMillis();
		time += "phase1("+(stoptime-starttime)+")_";
//		if(k4==null){
		starttime = System.currentTimeMillis();
		k4List.addAll(phaseTwo(graph, lowDegreeVertices));
		stoptime = System.currentTimeMillis();
		time += "phase2("+(stoptime-starttime)+")_";
//		}
		
//		starttime = System.currentTimeMillis();
//		k4List.addAll(phaseThree(graph, lowDegreeVertices));
//		stoptime = System.currentTimeMillis();
//		time += "phase3("+(stoptime-starttime)+")_";
//		
//		starttime = System.currentTimeMillis();
//		k4List.addAll(phaseFour(graph, lowDegreeVertices, highDegreeVertices));
//		stoptime = System.currentTimeMillis();
//		time += "phase4("+(stoptime-starttime)+")_";
		
		if(!k4List.isEmpty())
			time+="1";
		else
			time+="0";
		//System.out.println(time);
		DetectK4.resetTime();
		return k4List;
	}
	
	public static List<UndirectedGraph<Integer,Integer>> phaseOne(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> highDegreeVertices){
		List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same k4 more than once
		List<UndirectedGraph<Integer,Integer>> k4List = new ArrayList<UndirectedGraph<Integer,Integer>>();
			
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
			double [][] adjMatrix = graph2.getAdjacencyMatrix();
			
			//get square of adjacency matrix
			double[][] adjMatrixSquare;
			try {
				adjMatrixSquare = Utility.multiplyMatrix(adjMatrix, adjMatrix);
			} catch (MatrixException e) {
				continue;
			}
			
			//create map of indices to vertex
			List<Graph.Vertex<Integer>> indexVertexMap = new ArrayList<Graph.Vertex<Integer>>();
			Iterator<Graph.Vertex<Integer>> vIt = graph2.vertices();
			while(vIt.hasNext()){
				Graph.Vertex<Integer> v = vIt.next();
				indexVertexMap.add(v);
			}
			
			//look for a triangle in the adjacency matrix
			for(int j=0; j<adjMatrix.length; j++){
				for(int k=0; k<adjMatrix.length; k++){
					if(j!=k && (int)adjMatrix[j][k]==1 && (int)adjMatrixSquare[j][k]>0){
						
						//triangle found with j and k as indices of two ends of the triangle.
						//find the third one
						
						for(int m=0; m<adjMatrix.length; m++){
							if(m!=j && m!= k && ((int)adjMatrix[m][j] == 1) && ((int)adjMatrix[m][k] == 1)){
								List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
								Set<Integer> k4VerticesElem = new HashSet<Integer>(); //list to store k4 vertices
								//third vertex index found.
								//get the vertex that corresponds to all 3 indices
								
								k4Vertices.add(x);						k4VerticesElem.add(x.getElement());
								k4Vertices.add(indexVertexMap.get(j));	k4VerticesElem.add(indexVertexMap.get(j).getElement());
								k4Vertices.add(indexVertexMap.get(k));	k4VerticesElem.add(indexVertexMap.get(k).getElement());
								k4Vertices.add(indexVertexMap.get(m));	k4VerticesElem.add(indexVertexMap.get(m).getElement());
								
								//check in the marked list for an entry that contains all 4 vertex elements
								boolean contains = false;
								
								for(Set<Integer> s: marked){
									if(s.containsAll(k4VerticesElem)){
										contains = true;
										break;
									}
								}
								
								
								if(!contains){
									UndirectedGraph<Integer,Integer> k4 = Utility.makeGraphFromVertexSet(graph, k4Vertices);
									k4List.add(k4);
									marked.add(k4VerticesElem);
								}
							}
						}
					}
				}
			}
		}
		
		return k4List;
	}
	
	public static List<UndirectedGraph<Integer,Integer>> phaseTwo(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same k4 more than once
		List<UndirectedGraph<Integer,Integer>> k4List = new ArrayList<UndirectedGraph<Integer,Integer>>();
		
		for(Graph.Vertex<Integer> x: lowDegreeVertices){
			//get x's neighbourhood graph
			Iterator<Graph.Vertex<Integer>> nXIter = graph.neighbours(x);
			List<Graph.Vertex<Integer>> nXList = new ArrayList<Graph.Vertex<Integer>>();

			//make neighbourhood graph of x from list of vertices
			while(nXIter.hasNext()){
				nXList.add(nXIter.next());
			}
			
			//if a vertex is isolated, then it won't have any neighbours
			if(nXList.isEmpty())
				continue;
			
			UndirectedGraph<Integer,Integer> graph2 = Utility.makeGraphFromVertexSet(graph, nXList);
			double [][] adjMatrix = graph2.getAdjacencyMatrix();
			
			//get square of adjacency matrix
			double[][] adjMatrixSquare;
			try {
				adjMatrixSquare = Utility.multiplyMatrix(adjMatrix, adjMatrix);
			} catch (MatrixException e) {
				continue;
			}
			
			//create map of indices to vertex
			List<Graph.Vertex<Integer>> indexVertexMap = new ArrayList<Graph.Vertex<Integer>>();
			Iterator<Graph.Vertex<Integer>> vIt = graph2.vertices();
			while(vIt.hasNext()){
				Graph.Vertex<Integer> v = vIt.next();
				indexVertexMap.add(v);
			}
			
			//look for a triangle in the adjacency matrix
			for(int j=0; j<adjMatrix.length; j++){
				for(int k=0; k<adjMatrix.length; k++){
					if(j!=k && (int)adjMatrix[j][k]==1 && (int)adjMatrixSquare[j][k]>0){
						//triangle found with j and k as indices of two ends of the triangle.
						//find the third one
						
						for(int m=0; m<adjMatrix.length; m++){
							if(m!=j && m!= k && ((int)adjMatrix[m][j] == 1) && ((int)adjMatrix[m][k] == 1)){
								//third vertex index found.
								//get the vertex that corresponds to all 3 indices
								List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
								
								Set<Integer> k4VerticesElem = new HashSet<Integer>(); //list to store k4 vertices
								//third vertex index found.
								//get the vertex that corresponds to all 3 indices
								
								k4Vertices.add(x);						k4VerticesElem.add(x.getElement());
								k4Vertices.add(indexVertexMap.get(j));	k4VerticesElem.add(indexVertexMap.get(j).getElement());
								k4Vertices.add(indexVertexMap.get(k));	k4VerticesElem.add(indexVertexMap.get(k).getElement());
								k4Vertices.add(indexVertexMap.get(m));	k4VerticesElem.add(indexVertexMap.get(m).getElement());
								
								//check in the marked list for an entry that contains all 4 vertex elements
								boolean contains = false;
								
								for(Set<Integer> s: marked){
									if(s.containsAll(k4VerticesElem)){
										contains = true;
										break;
									}
								}
								
								if(!contains){
									UndirectedGraph<Integer,Integer> k4 = Utility.makeGraphFromVertexSet(graph, k4Vertices);
									k4List.add(k4);
									marked.add(k4VerticesElem);
								}
							}
						}
					}
				}
			}
		}
		
		return k4List;
	}
	
//	public static List<UndirectedGraph<Integer,Integer>> phaseThree(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
//		System.out.println("In phase three");
//		Set<Integer> marked = new HashSet<Integer>(); //to prevent creating the same k4 more than once
//		List<UndirectedGraph<Integer,Integer>> k4List = new ArrayList<UndirectedGraph<Integer,Integer>>();
//			
//		for(Graph.Vertex<Integer> x: lowDegreeVertices){
//			//get x's neighbourhood graph
//			Iterator<Graph.Vertex<Integer>> nXIter = graph.neighbours(x);
//			List<Graph.Vertex<Integer>> nXList = new ArrayList<Graph.Vertex<Integer>>();
//
//			//get the intersection of the neighbourhood vertices of x with the low degree vertices
//			while(nXIter.hasNext()){
//				Graph.Vertex<Integer> v = nXIter.next();
//				if(lowDegreeVertices.contains(v))
//					nXList.add(v);
//			}
//			
//			//if x's neighbourhood does not have any high degree vertex, then continue
//			if(nXList.isEmpty())
//				continue;
//			
//			//make graph from new list of vertices
//			UndirectedGraph<Integer,Integer> graph2 = Utility.makeGraphFromVertexSet(graph, nXList);
//			double [][] adjMatrix = graph2.getAdjacencyMatrix();
//			
//			//get square of adjacency matrix
//			double[][] adjMatrixSquare;
//			try {
//				adjMatrixSquare = MatrixOperation.multiply(adjMatrix, adjMatrix);
//			} catch (MatrixException e) {
//				continue;
//			}
//			
//			//create map of indices to vertex
//			List<Graph.Vertex<Integer>> indexVertexMap = new ArrayList<Graph.Vertex<Integer>>();
//			Iterator<Graph.Vertex<Integer>> vIt = graph2.vertices();
//			while(vIt.hasNext()){
//				Graph.Vertex<Integer> v = vIt.next();
//				indexVertexMap.add(v);
//			}
//			
//			//look for a triangle in the adjacency matrix
//			for(int j=0; j<adjMatrix.length; j++){
//				for(int k=0; k<adjMatrix.length; k++){
//					if(j!=k && (int)adjMatrix[j][k]==1 && (int)adjMatrixSquare[j][k]>0){
//						
//						//triangle found with j and k as indices of two ends of the triangle.
//						//find the third one
//						
//						for(int m=0; m<adjMatrix.length; m++){
//							if(m!=j && m!= k && ((int)adjMatrix[m][j] == 1) && ((int)adjMatrix[m][k] == 1)){
//								List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
//								//third vertex index found.
//								//get the vertex that corresponds to all 3 indices
//								
//								k4Vertices.add(x);
//								k4Vertices.add(indexVertexMap.get(j));
//								k4Vertices.add(indexVertexMap.get(k));
//								k4Vertices.add(indexVertexMap.get(m));
//								
//								boolean r1=false,r2=false,r3=false,r4=false;
//								
//								if(!marked.contains(x.getElement()))
//									r1=marked.add(x.getElement());
//								if(!marked.contains(indexVertexMap.get(j).getElement()))
//									r2=marked.add(indexVertexMap.get(j).getElement());
//								if(!marked.contains(indexVertexMap.get(k).getElement()))
//									r3=marked.add(indexVertexMap.get(k).getElement());
//								if(!marked.contains(indexVertexMap.get(m).getElement()))
//									r4=marked.add(indexVertexMap.get(m).getElement());
//								
//								if(!(!r1&&!r2&&!r3&&!r4)){
//									UndirectedGraph<Integer,Integer> k4 = Utility.makeGraphFromVertexSet(graph, k4Vertices);
//									k4List.add(k4);
//									Utility.printGraph(k4);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		return k4List;
//	}
//	
//	public static List<UndirectedGraph<Integer,Integer>> phaseFour(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> lowDegreeVertices,
//			Collection<Graph.Vertex<Integer>> highDegreeVertices){
//		System.out.println("In phase four");
//		Set<Integer> marked = new HashSet<Integer>(); //to prevent creating the same k4 more than once
//		List<UndirectedGraph<Integer,Integer>> k4List = new ArrayList<UndirectedGraph<Integer,Integer>>();
//			
//		for(Graph.Vertex<Integer> x: lowDegreeVertices){
//			//get x's neighbourhood graph
//			Iterator<Graph.Vertex<Integer>> nXIter = graph.neighbours(x);
//			List<Graph.Vertex<Integer>> nXList = new ArrayList<Graph.Vertex<Integer>>();
//
//			//get the intersection of the neighbourhood vertices of x with the high degree vertices
//			while(nXIter.hasNext()){
//				Graph.Vertex<Integer> v = nXIter.next();
//				if(highDegreeVertices.contains(v))
//					nXList.add(v);
//			}
//			
//			//if x's neighbourhood does not have any high degree vertex, then continue
//			if(nXList.isEmpty())
//				continue;
//			
//			//make graph from new list of vertices
//			UndirectedGraph<Integer,Integer> graph2 = Utility.makeGraphFromVertexSet(graph, nXList);
//			double [][] adjMatrix = graph2.getAdjacencyMatrix();
//			
//			//get square of adjacency matrix
//			double[][] adjMatrixSquare;
//			try {
//				adjMatrixSquare = MatrixOperation.multiply(adjMatrix, adjMatrix);
//			} catch (MatrixException e) {
//				continue;
//			}
//			
//			//create map of indices to vertex
//			List<Graph.Vertex<Integer>> indexVertexMap = new ArrayList<Graph.Vertex<Integer>>();
//			Iterator<Graph.Vertex<Integer>> vIt = graph2.vertices();
//			while(vIt.hasNext()){
//				Graph.Vertex<Integer> v = vIt.next();
//				indexVertexMap.add(v);
//			}
//			
//			//look for a triangle in the adjacency matrix
//			for(int j=0; j<adjMatrix.length; j++){
//				for(int k=0; k<adjMatrix.length; k++){
//					if(j!=k && (int)adjMatrix[j][k]==1 && (int)adjMatrixSquare[j][k]>0){
//						
//						//triangle found with j and k as indices of two ends of the triangle.
//						//find the third one
//						
//						for(int m=0; m<adjMatrix.length; m++){
//							if(m!=j && m!= k && ((int)adjMatrix[m][j] == 1) && ((int)adjMatrix[m][k] == 1)){
//								List<Graph.Vertex<Integer>> k4Vertices = new ArrayList<Graph.Vertex<Integer>>(); //list to store k4 vertices
//								//third vertex index found.
//								//get the vertex that corresponds to all 3 indices
//								
//								k4Vertices.add(x);
//								k4Vertices.add(indexVertexMap.get(j));
//								k4Vertices.add(indexVertexMap.get(k));
//								k4Vertices.add(indexVertexMap.get(m));
//								
//								boolean r1=false,r2=false,r3=false,r4=false;
//								
//								if(!marked.contains(x.getElement()))
//									r1=marked.add(x.getElement());
//								if(!marked.contains(indexVertexMap.get(j).getElement()))
//									r2=marked.add(indexVertexMap.get(j).getElement());
//								if(!marked.contains(indexVertexMap.get(k).getElement()))
//									r3=marked.add(indexVertexMap.get(k).getElement());
//								if(!marked.contains(indexVertexMap.get(m).getElement()))
//									r4=marked.add(indexVertexMap.get(m).getElement());
//								
//								if(!(!r1&&!r2&&!r3&&!r4)){
//									UndirectedGraph<Integer,Integer> k4 = Utility.makeGraphFromVertexSet(graph, k4Vertices);
//									k4List.add(k4);
//									Utility.printGraph(k4);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		return k4List;
//	}
	
	
	public static String getTime(){
		return time;
	}
	
	public static void resetTime(){
		time = "";
	}
}
