package efficientdetection;
import java.util.*;

import general.*;

public class DetectKL3 {
	
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
//		graph.addEdge(v0, v3);
	
		String fileName = "matrix5.txt";
		UndirectedGraph<Integer, Integer> graph = Utility.makeGraphFromFile(fileName);
		int l = 2;
		List<UndirectedGraph<Integer,Integer>> klList = detect(graph, l);
		System.out.println("No of k"+l+" found is "+klList.size()+"\n");
		if(!klList.isEmpty()){
			for(UndirectedGraph<Integer,Integer> kl: klList){
				Utility.printGraph(kl);
				System.out.println();
			}
		}else{
			System.out.println("Not found");
		}
	}
	
	/**
	 * method to detect the presence of a complete graph of size l
	 * @param graph graph to be checked
	 * @param l	size of the complete subgraph to be found
	 * @return	the list of complete subgraphs
	 */
	public static List<UndirectedGraph<Integer,Integer>> detect(UndirectedGraph<Integer,Integer> graph, int l){
		List<UndirectedGraph<Integer,Integer>> klList = new ArrayList<UndirectedGraph<Integer,Integer>>();
		
		if(l == 1){
			//create subgraphs with only one vertex
			Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
			while(vertices.hasNext()){
				UndirectedGraph<Integer,Integer> k1 = new UndirectedGraph<Integer,Integer>();
				k1.addVertex(vertices.next().getElement());
				klList.add(k1);
			}
		}
		else if(l==2){
			//create subgraphs formed by each edge in graph
			Iterator<Graph.Edge<Integer>> edges = graph.edges();
			while(edges.hasNext()){
				Graph.Edge<Integer> edge = edges.next();
				UndirectedGraph<Integer,Integer> k2 = new UndirectedGraph<Integer,Integer>();
				k2.addVertex((Integer) edge.getSource().getElement());
				k2.addVertex((Integer) edge.getDestination().getElement());
				klList.add(k2);
			}
		}
//		else if(l==4){
//			List<UndirectedGraph<Integer,Integer>> k4 = DetectK4.detect(graph);
//			klList.addAll(k4);
//		}
		else if(l==3){
			//get all triangles in graph
			List<UndirectedGraph<Integer,Integer>> k3 = detectTriangle(graph);
			klList.addAll(k3);
			
			
		}
		else if(l>3){
			int q = l/3;
			int r = l%3;
			
			if(r==1){
				List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same K(l-1) more than once
				//get the vertices in graph
				Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
				while(vertices.hasNext()){
					Graph.Vertex<Integer> x = vertices.next();
					//get the neighbourhood graph of x
					UndirectedGraph<Integer,Integer> nx = Utility.getNeighbourGraph(graph, x);
					//check if nx contains a k(l-1)
					List<UndirectedGraph<Integer,Integer>> kqList = detect(nx,l-1);
					for(UndirectedGraph<Integer,Integer> kq: kqList){
						List<Graph.Vertex<Integer>> kqPlusVertices = new ArrayList<Graph.Vertex<Integer>>();
						kqPlusVertices.add(x); //add x
						Iterator<Graph.Vertex<Integer>> kqVertices = kq.vertices();
						
						Set<Integer> hh = new HashSet<Integer>(); //to store elements of the K(l-1) vertices
						//add k(l-1) vertices
						while(kqVertices.hasNext()){
							Graph.Vertex<Integer> v = kqVertices.next();
							kqPlusVertices.add(v);
							hh.add(v.getElement());
						}
						
						hh.add(x.getElement());
						
						//check in the marked list for an entry that contains all 3 vertex elements
						boolean contains = false;
						
						for(Set<Integer> s: marked){
							if(s.containsAll(hh)){
								contains = true;
								break;
							}
						}
						
						if(!contains){
							//create Kl from kqPlusVertices list
							UndirectedGraph<Integer,Integer> kl = Utility.makeGraphFromVertexSet(graph, kqPlusVertices);
							klList.add(kl);
							marked.add(hh);
						}
					}
				}
			}
			else if(r==2){
				List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same K(l-2) more than once
				//get edges
				Iterator<Graph.Edge<Integer>> edges = graph.edges();
				while(edges.hasNext()){
					//for each edge, get the vertices at the ends
					Graph.Edge<Integer> edge = edges.next();
					Graph.Vertex<Integer> source = edge.getSource();
					Graph.Vertex<Integer> destination = edge.getDestination();
					
					//get the common neighbours of source and destination
					List<Graph.Vertex<Integer>> commonNeighbours = new ArrayList<Graph.Vertex<Integer>>();
					List<Graph.Vertex<Integer>> sourceNeighbours = new ArrayList<Graph.Vertex<Integer>>();
					Iterator<Graph.Vertex<Integer>> sNIt = graph.neighbours(source);
					while(sNIt.hasNext()){
						sourceNeighbours.add(sNIt.next());
					}
					
					Iterator<Graph.Vertex<Integer>> dNIt = graph.neighbours(destination);
					while(dNIt.hasNext()){
						Graph.Vertex<Integer> nv = dNIt.next();
						if(sourceNeighbours.contains(nv)){
							commonNeighbours.add(nv);
						}
					}
					
					//make graph from common neighbours
					if(commonNeighbours.isEmpty())
						continue;
					UndirectedGraph<Integer,Integer> nXY = Utility.makeGraphFromVertexSet(graph, commonNeighbours);
					
					//check if nXY has a K(l-2)
					List<UndirectedGraph<Integer,Integer>> kqList = detect(nXY,l-2);
					for(UndirectedGraph<Integer,Integer> kq: kqList){
						Set<Integer> hh = new HashSet<Integer>(); //to store elements of the K(l-1) vertices
						
						List<Graph.Vertex<Integer>> kqPlusVertices = new ArrayList<Graph.Vertex<Integer>>();
						kqPlusVertices.add(source); //add source
						hh.add(source.getElement());
						
						kqPlusVertices.add(destination); //add destination
						hh.add(destination.getElement());
						
						Iterator<Graph.Vertex<Integer>> kqVertices = kq.vertices();
						//add kq vertices
						while(kqVertices.hasNext()){
							Graph.Vertex<Integer> v = kqVertices.next();
							kqPlusVertices.add(v);
							hh.add(v.getElement());
						}
						
						//check in the marked list for an entry that contains all 3 vertex elements
						boolean contains = false;
						
						for(Set<Integer> s: marked){
							if(s.containsAll(hh)){
								contains = true;
								break;
							}
						}
						
						if(!contains){
							//create Kl from kqPlusVertices list
							UndirectedGraph<Integer,Integer> kl = Utility.makeGraphFromVertexSet(graph, kqPlusVertices);
							klList.add(kl);
							marked.add(hh);
						}
					}
				}
			}
			else if(r==0){				
				//create auxiliary graph H
				UndirectedGraph<Integer,Integer> H = new UndirectedGraph<Integer,Integer>();
				//get Kq graphs
				List<UndirectedGraph<Integer,Integer>> kqList = detect(graph,q);
				
				//need a means of mapping which vertex of H corresponds to which set of Kq vertices in G
				HashMap<Integer, Collection<Integer>> hToGMapping = new HashMap<Integer, Collection<Integer>>();
				
				//add vertices to H such that the number of vertices is same as the number of Kq found
				for(int i=0; i<kqList.size();i++){
					H.addVertex(i);

					//do the mapping
					Set<Integer> kqVertices = new HashSet<Integer>();
					Iterator<Graph.Vertex<Integer>> kgvit = kqList.get(i).vertices();
					while(kgvit.hasNext()){
						Graph.Vertex<Integer> v = kgvit.next();
						kqVertices.add(v.getElement());
					}
					hToGMapping.put(i, kqVertices);
				}
//				System.out.println(H.size() + " is size of H");
				
				//for each K2q found in G, add edges between corresponding vertices in H
				List<UndirectedGraph<Integer,Integer>> k2qList = detect(graph,(2*q));
				
				//make a list of vertex sets of each k2q found.
				List<Set<Integer>> k2qvertexset = new ArrayList<Set<Integer>>();
				for(UndirectedGraph<Integer,Integer> k2q: k2qList){
					Iterator<Graph.Vertex<Integer>> vIt = k2q.vertices();
					Set<Integer> vElems = new HashSet<Integer>();
					while(vIt.hasNext()){
						vElems.add(vIt.next().getElement());
					}	
					
					k2qvertexset.add(vElems);
				}
				
				//for any two pairs of vertices in H, 
				//check if there exists a k2q made up of their corresponding vertices in G 
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
							Collection<Integer> vOneCorrVertices = hToGMapping.get(vOneElement);
							Collection<Integer> vTwoCorrVertices = hToGMapping.get(vTwoElement);
							corrGVertices.addAll(vOneCorrVertices);
							corrGVertices.addAll(vTwoCorrVertices);
							
							for(Set<Integer> innerSet: k2qvertexset){
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
				
				//look for triangles in H
				List<UndirectedGraph<Integer,Integer>> triangles = detect(H, 3);
//				Utility.printGraph(H);
//				System.out.println(triangles.size());
				
				List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same Kl more than once
				
				//get a triangle and get its corresponding vertices in G
				for(UndirectedGraph<Integer,Integer> triangle:triangles){
					Iterator<Graph.Vertex<Integer>> tVertices = triangle.vertices();
					Set<Integer> hh = new HashSet<Integer>();
					while(tVertices.hasNext()){
						Graph.Vertex<Integer> next = tVertices.next();
						Integer vElem = next.getElement();
						Collection<Integer> corr = hToGMapping.get(vElem);
						for(Integer i: corr){
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
				}
			}
		}
			
		return klList;
	}
	
	private static List<UndirectedGraph<Integer,Integer>> detectTriangle(UndirectedGraph<Integer,Integer> graph){
		List<UndirectedGraph<Integer,Integer>> triangles = new ArrayList<UndirectedGraph<Integer,Integer>>();
		List<Set<Integer>> marked = new ArrayList<Set<Integer>>(); //to prevent creating the same triangle more than once
		
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
								UndirectedGraph<Integer, Integer> triangle = Utility.makeGraphFromVertexSet(graph, tVertices);
								triangles.add(triangle);
								marked.add(triListElem);
							}
						}
					}
				}
			}
		}
		return triangles;
	}
}
