package efficientdetection;
import java.util.*;

import general.*;

public class DetectKL2 {
	
	public static void main(String [] args){
//		UndirectedGraph<Integer, Integer> graph = new UndirectedGraph<Integer,Integer>();
//		
//		Graph.Vertex<Integer> v0 = graph.addVertex(0);
//		Graph.Vertex<Integer> v1 = graph.addVertex(1);
//		Graph.Vertex<Integer> v2 = graph.addVertex(2);
//		Graph.Vertex<Integer> v3 = graph.addVertex(3);
//		Graph.Vertex<Integer> v4 = graph.addVertex(4);
//		Graph.Vertex<Integer> v5 = graph.addVertex(5);
//		graph.addEdge(v0, v1);
//		graph.addEdge(v2, v1);
//		graph.addEdge(v2, v3);
//		graph.addEdge(v0, v2);
//		graph.addEdge(v0, v4);
//		graph.addEdge(v2, v4);
//		graph.addEdge(v1, v4);
//		graph.addEdge(v3, v4);
//		graph.addEdge(v1, v3);
//		graph.addEdge(v0, v3);43
	
		String fileName = "matrix5.txt";
		UndirectedGraph<Integer, Integer> graph = Utility.makeGraphFromFile(fileName);
		List<UndirectedGraph<Integer,Integer>> klList = detect(graph, 6);
		if(!klList.isEmpty()){
			for(UndirectedGraph<Integer,Integer> kl: klList){
				Utility.printGraph(kl);
			}
		}else{
			System.out.println("Not found");
		}
	}
	
	/**
	 * method to detect the presence of a complete graph of size l
	 * @param graph graph to be checked
	 * @param l	size of the complete subgraph to be found
	 * @return	the complete subgraph if found
	 */
	public static List<UndirectedGraph<Integer,Integer>> detect(UndirectedGraph<Integer,Integer> graph, int k){
		List<UndirectedGraph<Integer,Integer>> klList = new ArrayList<UndirectedGraph<Integer,Integer>>();
		List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same Kl more than once
		
		//construct auxiliary graph H 
		UndirectedGraph<Integer,Integer> H = new UndirectedGraph<Integer,Integer>();
		int vCount = 0;
		
		HashMap hVertexMapToGVertices = new HashMap();
		
		k = 6; //assuming k = 6 for coding sake
		int l = k/3; //l = 2
		
		//look for k2's in G, ie the edges in G
		Iterator<Graph.Edge<Integer>> edgeIt = graph.edges();
		while(edgeIt.hasNext()){
			Graph.Edge<Integer> edge = (Graph.Edge<Integer>)edgeIt.next();
			int[] edgeVerticesArray = new int[2];
			edgeVerticesArray[0] = (int)edge.getSource().getElement();
			edgeVerticesArray[1] = (int)edge.getDestination().getElement();
			hVertexMapToGVertices.put(vCount, edgeVerticesArray);
			H.addVertex(vCount);
			vCount++;
		}
		
		System.out.println(H.size() + " is size of H");
		
		//H will contain an edge between 2 vertices if G contains a K4 among the corresponding vertices
		//look for K4's in G
		List<UndirectedGraph<Integer,Integer>> k4List = DetectK4.detect(graph);
		System.out.println("No of k4's found is "+k4List.size());
		
		List<Set<Integer>> k4vertexset = new ArrayList<Set<Integer>>();
		for(UndirectedGraph<Integer,Integer> g: k4List){
			//get the vertex set of each k4 found.
			Iterator<Graph.Vertex<Integer>> vIt = g.vertices();
			Set<Integer> vElems = new HashSet<Integer>();
			while(vIt.hasNext()){
				vElems.add(vIt.next().getElement());
			}	
			
			k4vertexset.add(vElems);
		}
		
		//for any two pairs of vertices in H, 
		//check if there exists a k4 made up of their corresponding vertices in G 
		Iterator<Graph.Vertex<Integer>> vIt1 = H.vertices();
		while(vIt1.hasNext()){
			Graph.Vertex<Integer> vOne = vIt1.next();
			Integer vOneElement = vOne.getElement();
			Iterator<Graph.Vertex<Integer>> vIt2 = H.vertices();
			
			while(vIt2.hasNext()){
				Graph.Vertex<Integer> vTwo = vIt2.next();
				Integer vTwoElement = vTwo.getElement();
				if(vOneElement!=vTwoElement){
					Set<Integer> corrGVertices = new HashSet<Integer>();
					int[] vOneCorrVertices = (int[]) hVertexMapToGVertices.get(vOneElement);
					int[] vTwoCorrVertices = (int[]) hVertexMapToGVertices.get(vTwoElement);
					corrGVertices.add(vOneCorrVertices[0]); corrGVertices.add(vOneCorrVertices[1]);
					corrGVertices.add(vTwoCorrVertices[0]); corrGVertices.add(vTwoCorrVertices[1]);
					
					for(Set<Integer> innerSet: k4vertexset){
						if(innerSet.size()==corrGVertices.size() && innerSet.containsAll(corrGVertices)){
							if(!H.containsEdge(H.getVertexWithElement(vOneElement), H.getVertexWithElement(vTwoElement))){
								if(vOneElement<vTwoElement)
									H.addEdge(H.getVertexWithElement(vOneElement), H.getVertexWithElement(vTwoElement));
								else
									H.addEdge(H.getVertexWithElement(vTwoElement), H.getVertexWithElement(vOneElement));
							}
						}
					}
				}
			}
		}
		
//		Utility.printGraph(H);
		
//		int edgeCount = 0;
//		Iterator<Graph.Edge<Integer>> edges = H.edges();
//		while(edges.hasNext()){
//			edges.next();
//			edgeCount++;
//		}
//		
//		System.out.println("no of edges in H is "+edgeCount);
		
		List<UndirectedGraph<Integer,Integer>> triangles = DetectTriangle.detect(H);
		//get a triangle and get its corresponding vertices in G
		for(UndirectedGraph<Integer,Integer> triangle:triangles){
			Iterator<Graph.Vertex<Integer>> tVertices = triangle.vertices();
			Set<Integer> hh = new HashSet<Integer>();
			while(tVertices.hasNext()){
				Graph.Vertex<Integer> next = tVertices.next();
				Integer vElem = next.getElement();
				int[] corr = (int[]) hVertexMapToGVertices.get(vElem);
				for(int i: corr){
					hh.add(i);
				}
			}
			
			//check in the marked list for an entry that contains all kl vertex elements
			boolean contains = false;
			
			for(Set<Integer> s: marked){
				if(s.containsAll(hh)){
					contains = true;
					break;
				}
			}
			
			//check if such kl with those vertices has been created previously
			if(!contains){						
				List<Graph.Vertex<Integer>> klVertices = new ArrayList<Graph.Vertex<Integer>>();
				for(Integer i: hh)
					klVertices.add(graph.getVertexWithElement(i));
				
				UndirectedGraph<Integer, Integer> kl = Utility.makeGraphFromVertexSet(graph, klVertices);
				klList.add(kl);
				marked.add(hh);
			}
			
			//print out corresponding vertices
//			for(Integer i: hh)
//				System.out.print(i+",");
//			System.out.println();
		}
		
//		List<UndirectedGraph<Integer,Integer>> triangles = DetectK4.detect(graph);
//		//get a triangle and get its corresponding vertices in G
//		for(UndirectedGraph triangle:triangles){
//			Iterator<Graph.Vertex<Integer>> tVertices = triangle.vertices();
//			Set<Integer> hh = new HashSet<Integer>();
//			while(tVertices.hasNext()){
//				Graph.Vertex<Integer> next = tVertices.next();
//				Integer vElem = next.getElement();
//				hh.add(vElem);
//			}
//			
//			//print out corresponding vertices
//			for(Integer i: hh)
//				System.out.print(i+",");
//			System.out.println();
//		}
		
		
		
//		System.out.println("Printing out triangles");
//		for(UndirectedGraph triangle: triangles)
//			Utility.printGraph(triangle);
//		System.out.println(triangles.size());
		return klList;
	}
}
