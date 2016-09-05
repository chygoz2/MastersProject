import java.util.ArrayList;
import java.util.List;

import generate.PatternFreeGraphGenerator;

/**
 * Thread class that generates random graph that are free from induced subgraph types.
 * It calls on the respective methods of the PatternFreeGraphGenerator class
 * @author Chigozie Ekwonu
 *
 */
public class GenerateThread extends Thread{

	//instance variables
	private String type; //type of subgraph that should be absent from generated graph
	private String sizeString; //size of the graph to be generated
	private String klSize; //size of the complete induced subgraph type that should be absent from
							//the generated graph. Only needed for Kl free graph generation
	
	/**
	 * constructor to initialize instance variables
	 * @param type			the type of subgraph that should be absent from generated graph
	 * @param sizeString	size of the graph to be generated
	 * @param klSize		size of the complete induced subgraph type that should be absent from 
	 * 						the generated graph. Only needed for Kl free graph generation
	 */
	public GenerateThread(String type, String sizeString, String klSize){
		this.type = type;
		this.sizeString = sizeString;
		this.klSize = klSize;
	}
	
	/**
	 * run method that contains code to be executed 
	 */
	public void run(){
		String out = "";
		try{
			PatternFreeGraphGenerator g = new PatternFreeGraphGenerator(); //create instance of class for the generation
			List<String> allowed = new ArrayList<String>(); //list for storing allowed subgraph types
			allowed.add("triangle"); allowed.add("claw"); allowed.add("diamond");
			allowed.add("k4"); allowed.add("kl");
			
			if(allowed.contains(type)){
				try{
					int n = Integer.parseInt(sizeString); //get size of graph to be generated
					if(n<1){ //this size should be greater than 0
						out = "Size of graph should be greater than zero";
					}else{
						if (type.equals("triangle")) { //for generating triangle-free graphs
							System.out.println("Generating...");
							System.out.print("> ");
							String filename = g.generateTriangleFreeGraph(n);
							out = String.format("Name of graph file: %s", filename);
						}
		
						else if (type.equals("claw")) { //for generating claw-free graphs
							System.out.println("Generating...");
							System.out.print("> ");
							String filename = g.generateClawFreeGraph(n);
							out = String.format("Name of graph file: %s", filename);
						}
		
						else if (type.equals("diamond")) { //for generating diamond-free graphs
							System.out.println("Generating...");
							System.out.print("> ");
							String filename = g.generateDiamondFreeGraph(n);
							out = String.format("Name of graph file: %s", filename);
						}
		
						else if (type.equals("k4")) { //for generating k4-free graphs
							System.out.println("Generating...");
							System.out.print("> ");
							String filename = g.generateK4FreeGraph(n);
							out = String.format("Name of graph file: %s", filename);
						}
						else if (type.equals("kl")) { //for generating graphs free of other complete subgraphs
							try{
								int l = Integer.parseInt(klSize); //get size of the complete subgraph that should not be contained in graph
								if(l<2){ //this size must be greater than one
									out = "Size of induced subgraph should be greater than one";
								}else{
									System.out.println("Generating...");
									System.out.print("> ");
									String filename = g.generateKLFreeGraph(n,l);
									out = String.format("Name of graph file: %s", filename);
								}
							}catch(NumberFormatException e){//notify user if invalid size of complete subgraph is entered
								out = "Value entered for size of complete induced subgraph is not an integer";
							}
						}
					}
					out += String.format("%nCPU time taken: %d milliseconds", g.getTime());
				}catch(NumberFormatException e){ //notify user if an invalid size of graph to be generated is provided
					out = "Value entered for graph size to be generated is not an integer";
				}
			}else{ //notify user if invalid operation type entered
				out = "For generation operation, the second argument should be among: "
						+ "diamond, claw, triangle, k4, kl";
			}
		}catch(OutOfMemoryError e){ //if graph size to be generated is too large, the program
			//runs out of memory. Notify the user of this
			out = "System ran out of memory. Please choose a smaller "
					+ "graph size to be generated";
		} 
		//print out results
		System.out.println("\n"+out);
		System.out.print("> ");
	}
}
