package efficientdetection;
import java.util.*;

import efficientlisting.ListTriangles;
import general.*;

public class DetectKL {
	
	private String p1Time = "-";
	private String p2Time = "-";
	private  String found = "found";
	
	public static void main(String [] args){
		try{
			UndirectedGraph<Integer,Integer> graph = null;
			graph = Utility.makeGraphFromFile(args[0]);
			
			DetectKL d = new DetectKL();
			List<Graph.Vertex<Integer>> kl = d.detect(graph,7);
			System.out.print(d.getResult());
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}
	}
	
	public  List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph, int l){
		
		List<Graph.Vertex<Integer>>[] verticesPartition = partitionVertices(graph, l);
		List<Graph.Vertex<Integer>> lowDegreeVertices = verticesPartition[0];
		List<Graph.Vertex<Integer>> highDegreeVertices = verticesPartition[1];
		
		long starttime = System.currentTimeMillis();
		List<Graph.Vertex<Integer>> kl = phaseOne(graph, l, lowDegreeVertices);
		long stoptime = System.currentTimeMillis();
		p1Time = ""+(stoptime-starttime);
		
		if(kl==null){
			starttime = System.currentTimeMillis();
			kl = phaseTwo(graph, l, highDegreeVertices);
			stoptime = System.currentTimeMillis();
			p2Time = ""+(stoptime-starttime);
		}
		
		if(kl==null)
			found = "not found";
		
		return kl;
	}
	
	public List<Graph.Vertex<Integer>> phaseOne(UndirectedGraph<Integer,Integer> graph, int l, Collection<Graph.Vertex<Integer>> lowDegreeVertices){
		List<Graph.Vertex<Integer>> kl = null;
		for(Graph.Vertex<Integer> v: lowDegreeVertices){
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
		List<List<Graph.Vertex<Integer>>> klList = new ArrayList<List<Graph.Vertex<Integer>>>();
		
		if(l == 1){
			//create subgraphs with only one vertex
			Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
			while(vertices.hasNext()){
				List<Graph.Vertex<Integer>> temp = new ArrayList<Graph.Vertex<Integer>>();
				temp.add(vertices.next());
				klList.add(temp);
			}
		}
		else if(l==2){
			//create subgraphs formed by each edge in graph
			Iterator<Graph.Edge<Integer>> edges = graph.edges();
			while(edges.hasNext()){
				Graph.Edge<Integer> edge = edges.next();
				List<Graph.Vertex<Integer>> temp = new ArrayList<Graph.Vertex<Integer>>();
				temp.add(edge.getSource());
				temp.add(edge.getDestination());
				klList.add(temp);
			}
		}

		else if(l==3){
			//get all triangles in graph
			List<List<Graph.Vertex<Integer>>> k3 = new ListTriangles().detect(graph);
			klList.addAll(k3);
		}
		else if(l>3){
			int q = l/3;
			int r = l%3;
			
			if(r==1){
				Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same K(l-1) more than once
				//get the vertices in graph
				Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
				while(vertices.hasNext()){
					Graph.Vertex<Integer> x = vertices.next();
					//get the neighbourhood graph of x
					UndirectedGraph<Integer,Integer> nx = Utility.getNeighbourGraph(graph, x);
					//check if nx contains a k(l-1)
					List<List<Graph.Vertex<Integer>>> kqList = find(nx,l-1);
					for(List<Graph.Vertex<Integer>> kq: kqList){
						List<Graph.Vertex<Integer>> kqPlusVertices = new ArrayList<Graph.Vertex<Integer>>();
						kqPlusVertices.add(x); //add x
						
						Set<Integer> hh = new HashSet<Integer>(); //to store elements of the K(l-1) vertices
						//add k(l-1) vertices
						for(Graph.Vertex<Integer> v: kq){
							kqPlusVertices.add(v);
							hh.add(v.getElement());
						}
						
						hh.add(x.getElement());
						
						//check in the marked list for an entry that contains all 3 vertex elements
						boolean contains = marked.add(hh);					
						
						if(contains){
							//create Kl from kqPlusVertices list
							klList.add(kqPlusVertices);						
						}
					}
				}
			}
			else if(r==2){
				Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same K(l-2) more than once
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
					List<List<Graph.Vertex<Integer>>> kqList = find(nXY,l-2);
					for(List<Graph.Vertex<Integer>> kq: kqList){
						Set<Integer> hh = new HashSet<Integer>(); //to store elements of the K(l-1) vertices
						
						List<Graph.Vertex<Integer>> kqPlusVertices = new ArrayList<Graph.Vertex<Integer>>();
						kqPlusVertices.add(source); //add source
						hh.add(source.getElement());
						
						kqPlusVertices.add(destination); //add destination
						hh.add(destination.getElement());
						
						//add kq vertices
						for(Graph.Vertex<Integer> v: kq){
							kqPlusVertices.add(v);
							hh.add(v.getElement());
						}
						
						//check in the marked list for an entry that contains all 3 vertex elements
						boolean contains = marked.add(hh);
						
						if(contains){
							//create Kl from kqPlusVertices list
							klList.add(kqPlusVertices);
						}
					}
				}
			}
			else if(r==0){				
				//create auxiliary graph H
				UndirectedGraph<Integer,Integer> H = new UndirectedGraph<Integer,Integer>();
				//get Kq graphs
				List<List<Graph.Vertex<Integer>>> kqList = find(graph,q);
				
				//need a means of mapping which vertex of H corresponds to which set of Kq vertices in G
				HashMap<Integer, Set<Integer>> hToGMapping = new HashMap<Integer, Set<Integer>>();
				
				//add vertices to H such that the number of vertices is same as the number of Kq found
				for(int i=0; i<kqList.size();i++){
					H.addVertex(i);

					//do the mapping
					Set<Integer> kqVertices = new HashSet<Integer>();
					List<Graph.Vertex<Integer>> kqvit = kqList.get(i);
					for(Graph.Vertex<Integer> v: kqvit){
						kqVertices.add(v.getElement());
					}
					hToGMapping.put(i, kqVertices);
				}
				
				//for each K2q found in G, add edges between corresponding vertices in H
				List<List<Graph.Vertex<Integer>>> k2qList = find(graph,(2*q));
				
				//make a list of vertex sets of each k2q found.
				List<Set<Integer>> k2qvertexset = new ArrayList<Set<Integer>>();
				for(List<Graph.Vertex<Integer>> k2q: k2qList){
					Set<Integer> vElems = new HashSet<Integer>();
					for(Graph.Vertex<Integer> v: k2q){
						vElems.add(v.getElement());
					}	
					
					k2qvertexset.add(vElems);
				}
				
				//for any two pairs of vertices in H, 
				//check if there exists a k2q made up of their corresponding vertices in G 
				Set<Set<Integer>> ssfound = new HashSet<Set<Integer>>(); //since a k2q can have different permutations
							//of its vertices, which would then result in many edges in H, this set will be used to ensure
							//that only one edge is added to H for each K2q found in G.
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
							Set<Integer> vOneCorrVertices = hToGMapping.get(vOneElement);
							Set<Integer> vTwoCorrVertices = hToGMapping.get(vTwoElement);
							corrGVertices.addAll(vOneCorrVertices);
							corrGVertices.addAll(vTwoCorrVertices);
							
							if(ssfound.add(corrGVertices)){
								for(Set<Integer> innerSet: k2qvertexset){
									if(innerSet.size()==corrGVertices.size() && innerSet.containsAll(corrGVertices)){
										if(!H.containsEdge(H.getVertexWithElement(vOneElement), H.getVertexWithElement(vTwoElement))){
											H.addEdge(H.getVertexWithElement(vOneElement), H.getVertexWithElement(vTwoElement));
										}
									}
								}
							}
						}
					}
				}
				
				//look for triangles in H
				List<List<Graph.Vertex<Integer>>> triangles = find(H, 3);
				
				Set<Set<Integer>> marked = new HashSet<Set<Integer>>(); //to prevent creating the same Kl more than once
				
				//get a triangle and get its corresponding vertices in G
				for(List<Graph.Vertex<Integer>> triangle:triangles){
					Set<Integer> hh = new HashSet<Integer>();
					for(Graph.Vertex<Integer> next: triangle){
						Integer vElem = next.getElement();
						Set<Integer> corr = hToGMapping.get(vElem);
						for(Integer i: corr){
							hh.add(i);
						}
					}
					
					//check in the marked list for an entry that contains all kl vertex elements
					boolean contains = marked.add(hh);
										
					//check if such kl with those vertices has been created previously
					if(contains){						
						List<Graph.Vertex<Integer>> klVertices = new ArrayList<Graph.Vertex<Integer>>();
						for(Integer i: hh)
							klVertices.add(graph.getVertexWithElement(i));
						
						klList.add(klVertices);
					}
				}
			}
		}
			
		return klList;
	}
	
	//method to partition the vertices into low degree vertices and high degree vertices
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
	
	public  String getResult(){
		String result = String.format("%-10s%-10s%-10s", p1Time,p2Time,found);
		return result;
	}
	
}
