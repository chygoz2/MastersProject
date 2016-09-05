package efficient.listing;
import java.util.*;

import exception.GraphFileReaderException;
import general.*;
import general.Graph.Vertex;

/**
 * class that lists complete graphs found in a graph
 * @author Chigozie Ekwonu
 *
 */
public class ListKL {
	
	//instance variables
	private  String time; //stores time taken to execute operation
	private  int found;	  //stores number of Kls found
	
	/**
	 * constructor to initialize instance variables
	 */
	public ListKL(){
		this.time = "-";
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
			ListKL d = new ListKL();
			if(args.length>1){
				int l = Integer.parseInt(args[1]);
				List<List<Vertex<Integer>>> kls = d.detect(graph,l);
				String out = "";
				if(!kls.isEmpty()){
					out = "";
					for(List<Graph.Vertex<Integer>> kl: kls){
						out += Utility.printList(kl)+"\n";
					}
					out = String.format("K%d found%nVertices:%n%s",l,out);
					out += String.format("Number of K%ds found: %d%n",l, kls.size());
					out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				}else{
					out = String.format("K%d not found%nCPU time taken: %d milliseconds", l,Utility.getTotalTime(d.getResult()));
				}
				//print out results
				System.out.println(out);
			}else{
				System.out.println("Please enter the size of the complete graph to be found");
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(NumberFormatException e){
			System.out.println("The graph size entered is not valid");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	public  List<List<Graph.Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph, int l){
		long starttime = System.currentTimeMillis();
		List<List<Graph.Vertex<Integer>>> kls = find(graph, l);
		long stoptime = System.currentTimeMillis();
		time = ""+(stoptime-starttime);
		
		found = kls.size();
		return kls;
	}
	
	/**
	 * method to detect the presence of a complete graph of size l
	 * @param graph graph to be checked
	 * @param l	size of the complete subgraph to be found
	 * @return	the list of complete subgraphs found if any
	 */
	public  List<List<Graph.Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph, int l){
		List<List<Graph.Vertex<Integer>>> klList = new ArrayList<List<Graph.Vertex<Integer>>>();//list of all Kls found
		
		//if size of complete subgraph to be found is greater than the graph size, then return an empty list
		if(l>graph.size()){
			return klList;
		}
		
		//if l = 1, return list of vertices of graph
		if(l == 1){
			//create subgraphs with only one vertex
			Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
			while(vertices.hasNext()){
				List<Graph.Vertex<Integer>> temp = new ArrayList<Graph.Vertex<Integer>>();
				temp.add(vertices.next());
				klList.add(temp);
			}
		}
		//if l=2, return list of vertices of each edge of graph
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
		//if l=3, return triangles in graph
		else if(l==3){
			//get all triangles in graph
			List<List<Graph.Vertex<Integer>>> k3 = new ListTriangles().detect(graph);
			klList.addAll(k3);
		}
		else if(l>3){
			int q = l/3; //quotient after division
			int r = l%3; //remainder after division
			
			if(r==1){ //if remainder is 1
				Set<Set<Integer>> seen = new HashSet<Set<Integer>>(); //to prevent creating the same K(l-1) more than once
				//get the vertices in graph
				Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
				//for each vertex x, 
				while(vertices.hasNext()){
					Graph.Vertex<Integer> x = vertices.next();
					//get the neighbourhood graph of x
					UndirectedGraph<Integer,Integer> nx = Utility.getNeighbourGraph(graph, x);
					//check if neighbourhood of x contains a k(l-1)
					List<List<Graph.Vertex<Integer>>> kqList = find(nx,l-1);
					for(List<Graph.Vertex<Integer>> kq: kqList){
						List<Graph.Vertex<Integer>> kqPlusVertices = new ArrayList<Graph.Vertex<Integer>>();
						Set<Integer> hh = new HashSet<Integer>(); //to store elements of the K(l-1) vertices used to prevent duplication
						
						//add k(l-1) vertices
						for(Graph.Vertex<Integer> v: kq){
							kqPlusVertices.add(v);
							hh.add(v.getElement());
						}
						
						kqPlusVertices.add(x); //add x
						hh.add(x.getElement());
						
						//check if the kl was found before. "contains" is true if kl was not already found previously
						boolean contains = seen.add(hh);					
						
						if(contains){//if kl was not previously found
							klList.add(kqPlusVertices);//add kl to kl list						
						}
					}
				}
			}
			else if(r==2){ //if remainder is 2
				Set<Set<Integer>> seen = new HashSet<Set<Integer>>(); //to prevent creating the same K(l-2) more than once
				//get edges
				Iterator<Graph.Edge<Integer>> edges = graph.edges();
				while(edges.hasNext()){
					//for each edge, get the vertices at the ends
					Graph.Edge<Integer> edge = edges.next();
					Graph.Vertex<Integer> source = edge.getSource();
					Graph.Vertex<Integer> destination = edge.getDestination();
					
					//get the common neighbours of source and destination vertices
					List<Graph.Vertex<Integer>> commonNeighbours = new ArrayList<Graph.Vertex<Integer>>();
					Set<Integer> sourceNeighbours = new HashSet<Integer>();
					Iterator<Graph.Vertex<Integer>> sNIt = graph.neighbours(source);
					while(sNIt.hasNext()){
						sourceNeighbours.add(sNIt.next().getElement());
					}
					
					Iterator<Graph.Vertex<Integer>> dNIt = graph.neighbours(destination);
					while(dNIt.hasNext()){
						Graph.Vertex<Integer> nv = dNIt.next();
						if(sourceNeighbours.contains(nv.getElement())){
							commonNeighbours.add(nv);
						}
					}
					
					//make graph from common neighbours
					if(commonNeighbours.isEmpty())
						continue;
					UndirectedGraph<Integer,Integer> nXY = Utility.makeGraphFromVertexSet(graph, commonNeighbours);//graph induced by vertices common to X and Y
					
					//check if the common neighbouhood graph of X and Y has a K(l-2)
					List<List<Graph.Vertex<Integer>>> kqList = find(nXY,l-2);
					for(List<Graph.Vertex<Integer>> kq: kqList){
						Set<Integer> hh = new HashSet<Integer>(); //to store elements of the K(l-1) vertices used to prevent duplication
						
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
						
						//check if the kl was found before. "contains" is true if kl was not already found previously
						boolean contains = seen.add(hh);
						
						if(contains){//if kl wasn't previously found,
							//add kl to list
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
				//make a set of vertex sets of each k2q vertices elements found.
				Set<Set<Integer>> k2qvertexset = new HashSet<Set<Integer>>();
				for(List<Graph.Vertex<Integer>> k2q: k2qList){
					Set<Integer> vElems = new HashSet<Integer>();
					for(Graph.Vertex<Integer> v: k2q){
						vElems.add(v.getElement());
					}	
					
					k2qvertexset.add(vElems);
				}
				
				//for any two pairs of vertices in H, 
				//check if there exists a k2q made up of their corresponding vertices in G 
				Set<Set<Integer>> ssfound = new HashSet<Set<Integer>>();//since a k2q can have different permutations
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
								if(k2qvertexset.contains(corrGVertices)){
									if(!H.containsEdge(H.getVertexWithElement(vOneElement), H.getVertexWithElement(vTwoElement))){
										H.addEdge(H.getVertexWithElement(vOneElement), H.getVertexWithElement(vTwoElement));
									}
								}
							}
						}
					}
				}
				
				//look for triangles in H
				List<List<Graph.Vertex<Integer>>> triangles = find(H, 3);
				
				Set<Set<Integer>> seen = new HashSet<Set<Integer>>(); //to prevent creating the same Kl more than once
				
				//get a triangle and get its corresponding vertices in G
				for(List<Graph.Vertex<Integer>> triangle:triangles){
					Set<Integer> hh = new HashSet<Integer>(); //stores elements of the kl vertices used to prevent duplication
					for(Graph.Vertex<Integer> next: triangle){
						Integer vElem = next.getElement();
						Set<Integer> corr = hToGMapping.get(vElem);
						for(Integer i: corr){
							hh.add(i);
						}
					}
										
					//check if such kl with those vertices has been created previously. If not, add it to the Kl list
					if(seen.add(hh)){						
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
	
	/**
	*	method to return the time taken to run the listing	
	*	and the number of KLs found
	*/
	public String getResult(){
		String result = String.format("%-10s%10d", time,found);
		return result;
	}

}
