import java.util.List;

import efficient.listing.ListClaws;
import efficient.listing.ListDiamonds;
import efficient.listing.ListK4;
import efficient.listing.ListKL;
import efficient.listing.ListSimplicialVertices;
import efficient.listing.ListTriangles;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;
import general.Graph.Vertex;

/**
 * thread class that lists the vertices of induced subgraphs and simplicial vertices
 * in a graph. It calls on the individual listing classes for each subgraph type.
 * @author Chigozie Ekwonu
 */
public class ListThread extends Thread{

	//instance variables
	private String type; //type of subgraph to be listed
	private UndirectedGraph<Integer,Integer> graph; //graph to be checked
	private String sizeString; //size of induced complete subgraph to be listed. Only needed for KL detection
	
	/**
	 * constructor to initialize the instance variables
	 * @param type				the type of subgraph to be listed
	 * @param graph				the graph to be checked
	 * @param sizeString		size of induced complete subgraph to be listed
	 */
	public ListThread(String type, UndirectedGraph<Integer,Integer> graph, String sizeString){
		this.type = type;
		this.graph = graph;
		this.sizeString = sizeString;
	}
	
	/**
	 * method containing main code to be executed
	 */
	public void run(){
		String out = ""; 
		if (type.equals("triangle")) { //for triangle listing
			ListTriangles d = new ListTriangles();
			System.out.println("Listing...");
			System.out.print("> ");
			List<List<Vertex<Integer>>> triangles = d.detect(graph);
			if(!triangles.isEmpty()){
				out = "";
				for(List<Graph.Vertex<Integer>> triangle: triangles){
					out += Utility.printList(triangle)+"\n";
				}
				out = String.format("Triangle found%nVertices:%n%s", out);
				out += String.format("Number of triangles found: %d%n", triangles.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				
			}else{
				out = String.format("Triangle not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				
			}
		}

		else if (type.equals("claw")) { //for claw listing
			ListClaws d = new ListClaws();
			System.out.println("Listing...");
			System.out.print("> ");
			List<List<Vertex<Integer>>> claws = d.detect(graph);
			if(!claws.isEmpty()){
				out = "";
				for(List<Graph.Vertex<Integer>> claw: claws){
					out += Utility.printList(claw)+"\n";
				}
				out = String.format("Claw found%nVertices:%n%s", out);
				out += String.format("Number of claws found: %d%n", claws.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				
			}else{
				out = String.format("Claw not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}

		else if (type.equals("diamond")) { //for diamond listing
			ListDiamonds d = new ListDiamonds();
			System.out.println("Listing...");
			System.out.print("> ");
			List<List<Vertex<Integer>>> diamonds = d.detect(graph);
			if(!diamonds.isEmpty()){
				out = "";
				for(List<Graph.Vertex<Integer>> diamond: diamonds){
					out += Utility.printList(diamond)+"\n";
				}
				out = String.format("Diamond found%nVertices:%n%s", out);
				out += String.format("Number of diamonds found: %d%n", diamonds.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				
			}else{
				out = String.format("Diamond not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}

		else if (type.equals("simplicial")) { //for simplicial vertices listing
			ListSimplicialVertices d = new ListSimplicialVertices();
			System.out.println("Listing...");
			System.out.print("> ");
			List<Graph.Vertex<Integer>> simpVertex = d.detect(graph);
			if(!simpVertex.isEmpty()){
				out = Utility.printList(simpVertex);
				out = String.format("Simplicial vertices found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				
			}else{
				out = String.format("Simplicial vertices not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}	

		else if (type.equals("k4")) { //for K4 listing
			ListK4 d = new ListK4();
			System.out.println("Listing...");
			System.out.print("> ");
			List<List<Vertex<Integer>>> k4s = d.detect(graph);
			if(!k4s.isEmpty()){
				out = "";
				for(List<Graph.Vertex<Integer>> k4: k4s){
					out += Utility.printList(k4)+"\n";
				}
				out = String.format("K4 found%nVertices:%n%s", out);
				out += String.format("Number of K4s found: %d%n", k4s.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				
			}else{
				out = String.format("K4 not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
				
			}
		}
		else if (type.equals("kl")) { //for listing other complete subgraphs
			try{
				int l = Integer.parseInt(sizeString); //get size of complete subgraph
				if(l<1){
					out = "Size of graph induced subgraph should be greater than zero";
				}
				else{
					ListKL d = new ListKL();
					System.out.println("Listing...");
					System.out.print("> ");
					List<List<Vertex<Integer>>> kls = d.detect(graph, l);
					if(!kls.isEmpty()){
						out = "";
						for(List<Graph.Vertex<Integer>> kl: kls){
							out += Utility.printList(kl)+"\n";
						}
						out = String.format("K"+l+" found%nVertices:%n%s", out);
						out += String.format("Number of K"+l+"s found: %d%n", kls.size());
						out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
						
					}else{
						out = String.format("K"+l+" not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
						
					}
				}
			}catch(NumberFormatException e){ //notify user that an invalid size was entered
				out = "Complete subgraph size should be an integer";
			}
		}
		else{
			out = "For listing operation, the second argument should be "
					+ "the listing pattern, e.g. diamond, claw, triangle, k4, kl, simplicial";
		}
		
		//print out results
		System.out.println("\n"+out);
		System.out.print("> ");
	}
}
