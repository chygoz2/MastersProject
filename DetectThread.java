import java.util.List;

import efficient.detection.DetectClaw;
import efficient.detection.DetectDiamond;
import efficient.detection.DetectK4;
import efficient.detection.DetectKL;
import efficient.detection.DetectSimplicialVertex;
import efficient.detection.DetectTriangle;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

/**
 * thread class that detects the presence of induced subgraphs and simplicial vertices
 * in a graph. It calls on the individual detection classes for each subgraph type.
 * @author Chigozie Ekwonu
 */
public class DetectThread extends Thread {
	
	//instance variables
	private String type; //type of subgraph to be detected
	private UndirectedGraph<Integer,Integer> graph; //graph to be checked
	private String sizeString; //size of complete induced subgraph to be checked. Only needed for KL detection
	
	/**
	 * constructor to initialize the instance variables
	 * @param type				the type of subgraph to be detected
	 * @param graph				the graph to be checked
	 * @param sizeString		size of complete induced subgraph to be checked
	 */
	public DetectThread(String type, UndirectedGraph<Integer,Integer> graph, String sizeString){
		this.type = type;
		this.graph = graph;
		this.sizeString = sizeString;
	}
	
	/**
	 * run method that contains code to be executed 
	 */
	public void run(){
		String out = ""; 
		if (type.equals("triangle")) { //for triangle detection, 
			DetectTriangle d = new DetectTriangle();
			System.out.println("Detecting...");
			System.out.print("> ");
			List<Graph.Vertex<Integer>> triangle = d.detect(graph);
			//print out result of detection
			if(triangle!=null){
				out = Utility.printList(triangle);
				out = String.format("Triangle found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Triangle not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}

		else if (type.equals("claw")) { //for claw detection
			DetectClaw d = new DetectClaw();
			System.out.println("Detecting...");
			System.out.print("> ");
			List<Graph.Vertex<Integer>> claw = d.detect(graph);
			if(claw!=null){
				out = Utility.printList(claw);
				out = String.format("Claw found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Claw not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}

		else if (type.equals("diamond")) {  //for diamond detection
			DetectDiamond d = new DetectDiamond();
			System.out.println("Detecting...");
			System.out.print("> ");
			List<Graph.Vertex<Integer>> diamond = d.detect(graph);
			if(diamond!=null){
				out = Utility.printList(diamond);
				out = String.format("Diamond found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Diamond not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}

		else if (type.equals("simplicial")) { //for simplicial vertex detection
			DetectSimplicialVertex d = new DetectSimplicialVertex();
			System.out.println("Detecting...");
			System.out.print("> ");
			Graph.Vertex<Integer> simpVertex = d.detect(graph);
			if(simpVertex!=null){
				out = String.format("Simplicial vertex found%nOne such vertex: %s%n", simpVertex.getElement());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));							
			}else{
				out = String.format("Simplicial vertex not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}	

		else if (type.equals("k4")) { //for K4 detection
			DetectK4 d = new DetectK4();
			System.out.println("Detecting...");
			System.out.print("> ");
			List<Graph.Vertex<Integer>> k4 = d.detect(graph);
			if(k4!=null){
				out = Utility.printList(k4);
				out = String.format("K4 found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("K4 not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
		}
		else if (type.equals("kl")) { //for other complete subgraph size detection
			try{
				int l = Integer.parseInt(sizeString); //get size of complete subgraph to be detection
				if(l<1){
					out = "Size of induced subgraph should be greater than zero";
				}else{
					DetectKL d = new DetectKL();
					System.out.println("Detecting...");
					System.out.print("> ");
					List<Graph.Vertex<Integer>> kl = d.detect(graph, l);
					if(kl!=null){
						out = Utility.printList(kl);
						out = String.format("K"+l+" found%nVertices: %s%n", out);
						out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
					}else{
						out = String.format("K"+l+" not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
					}
				}
			}catch(NumberFormatException e){ //notify user that an invalid size was entered
				out = "Complete subgraph size should be an integer";
			}
		}
		else{ //Notify user of invalid detection type selection
			out = "For detection operation, the second argument should be "
					+ "the detection pattern, e.g. diamond, claw, triangle, k4, kl, simplicial";
		}
		System.out.println("\n"+out);
		System.out.print("> ");
	}
}
