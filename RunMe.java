import java.awt.EventQueue;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import efficient.detection.DetectClaw;
import efficient.detection.DetectDiamond;
import efficient.detection.DetectK4;
import efficient.detection.DetectKL;
import efficient.detection.DetectSimplicialVertex;
import efficient.detection.DetectTriangle;
import efficient.listing.ListClaws;
import efficient.listing.ListDiamonds;
import efficient.listing.ListK4;
import efficient.listing.ListKL;
import efficient.listing.ListSimplicialVertices;
import efficient.listing.ListTriangles;
import exception.GraphFileReaderException;
import general.*;
import general.Graph.Vertex;
import generate.PatternFreeGraphGenerator;
import gui.GUI;

public class RunMe {
	public static void main(String[] args){
		String operationvalues = "Possible values for [operation_class]: help, detect, list, generate, quit, gui, test";
		String detectionValues = "Possible values for [operation_class_type]: triangle, claw, k4, kl, simplicial, diamond";
		String optionValues = "[option] could be the graph file relative location (for detect and list operations) "
				+ "				or graph size (for generate operation)";
		String info = "Please enter commands in the form: "
				+ "[operation_class] [operation_class_type] [option]";
		String errorMessage = "You entered fewer arguments than required.\n"
				+ "Arguments should be in the form\n"
				+ "[operation_class] [operation_class_type] [option]";
		System.out.println(info);
		System.out.println(operationvalues);
		System.out.println(detectionValues);
		Scanner sc = new Scanner(System.in);

		System.out.print("> ");
		String input = sc.nextLine();

		String[] words = input.split("[ ]+");
		while(!words[0].equals("quit")){
			if(words[0].equals("detect")){
				if(words.length < 3){
					System.out.println(errorMessage);
				}else{
					String path = words[2];
					File fr= new File(path);
					if(!fr.exists())
						System.out.println("File not found");
					else{	
						UndirectedGraph<Integer, Integer> graph = null;
						try {
							graph = Utility.makeGraphFromFile(path);
						} catch (GraphFileReaderException e1) {
							System.out.println(e1.getError());
						}
						if(graph!=null){
							if (words[1].equals("triangle")) {
								DetectTriangle d = new DetectTriangle();
								List<Graph.Vertex<Integer>> triangle = d.detect(graph);
								if(triangle!=null){
									String out = printList(triangle);
									out = String.format("Triangle found%nVertices: %s%n", out);
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("Triangle not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}
	
							else if (words[1].equals("claw")) {
								DetectClaw d = new DetectClaw();
								List<Graph.Vertex<Integer>> claw = d.detect(graph);
								if(claw!=null){
									String out = printList(claw);
									out = String.format("Claw found%nVertices: %s%n", out);
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("Claw not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}
	
							else if (words[1].equals("diamond")) {
								DetectDiamond d = new DetectDiamond();
								List<Graph.Vertex<Integer>> diamond = d.detect(graph);
								if(diamond!=null){
									String out = printList(diamond);
									out = String.format("Diamond found%nVertices: %s%n", out);
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("Diamond not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}
	
							else if (words[1].equals("simplicial")) {
								DetectSimplicialVertex d = new DetectSimplicialVertex();
								Graph.Vertex<Integer> simpVertex = d.detect(graph);
								if(simpVertex!=null){
									String out = String.format("Simplicial vertex found%nOne such vertex: %s%n", simpVertex.getElement());
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("Simplicial vertex not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}	
	
							else if (words[1].equals("k4")) {
								DetectK4 d = new DetectK4();
								List<Graph.Vertex<Integer>> k4 = d.detect(graph);
								if(k4!=null){
									String out = printList(k4);
									out = String.format("K4 found%nVertices: %s%n", out);
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("K4 not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}
							else if (words[1].equals("kl")) {
								try{
									System.out.println("Please enter size of complete subgraph to be detected");
									System.out.print("> ");
									input = sc.nextLine();
									int l = Integer.parseInt(input);
									if(l<1){
										System.out.println("Size of induced subgraph should be greater than zero");
									}else{
										DetectKL d = new DetectKL();
										List<Graph.Vertex<Integer>> kl = d.detect(graph, l);
										if(kl!=null){
											String out = printList(kl);
											out = String.format("K"+l+" found%nVertices: %s%n", out);
											out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
											System.out.println(out);
										}else{
											String out = String.format("K"+l+" not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
											System.out.println(out);
										}
									}
								}catch(NumberFormatException e){
									System.out.println("Value entered is not an integer");
								}
							}
							else{
								System.out.println("For detection operation, the second argument should be "
										+ "the detection pattern, e.g. diamond, claw, triangle, k4, kl, simplicial");
							}
						}	
					}
				}
			}
			else if(words[0].equals("list")){
				if(words.length < 3){
					System.out.println(errorMessage);
				}else{
					String path = words[2];
					File fr= new File(path);
					if(!fr.exists())
						System.out.println("File not found");
					else{	
						UndirectedGraph<Integer, Integer> graph = null;
						try {
							graph = Utility.makeGraphFromFile(path);
						} catch (GraphFileReaderException e1) {
							e1.getError();
						}
						if(graph!=null){
							if (words[1].equals("triangle")) {
								ListTriangles d = new ListTriangles();
								List<List<Vertex<Integer>>> triangles = d.detect(graph);
								if(!triangles.isEmpty()){
									String out = "";
									for(List<Graph.Vertex<Integer>> triangle: triangles){
										out += printList(triangle)+"\n";
									}
									out = String.format("Triangle found%nVertices:%n%s", out);
									out += String.format("Number of triangles found: %d%n", triangles.size());
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("Triangle not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}
	
							else if (words[1].equals("claw")) {
								ListClaws d = new ListClaws();
								List<List<Vertex<Integer>>> claws = d.detect(graph);
								if(!claws.isEmpty()){
									String out = "";
									for(List<Graph.Vertex<Integer>> claw: claws){
										out += printList(claw)+"\n";
									}
									out = String.format("Claw found%nVertices:%n%s", out);
									out += String.format("Number of claws found: %d%n", claws.size());
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("Claw not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}
	
							else if (words[1].equals("diamond")) {
								ListDiamonds d = new ListDiamonds();
								List<List<Vertex<Integer>>> diamonds = d.detect(graph);
								if(!diamonds.isEmpty()){
									String out = "";
									for(List<Graph.Vertex<Integer>> diamond: diamonds){
										out += printList(diamond)+"\n";
									}
									out = String.format("Diamond found%nVertices:%n%s", out);
									out += String.format("Number of diamonds found: %d%n", diamonds.size());
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("Diamond not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}
	
							else if (words[1].equals("simplicial")) {
								ListSimplicialVertices d = new ListSimplicialVertices();
								List<Graph.Vertex<Integer>> simpVertex = d.detect(graph);
								if(!simpVertex.isEmpty()){
									String out = printList(simpVertex);
									out = String.format("Simplicial vertices found%nVertices: %s%n", out);
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("Simplicial vertices not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}	
	
							else if (words[1].equals("k4")) {
								ListK4 d = new ListK4();
								List<List<Vertex<Integer>>> k4s = d.detect(graph);
								if(!k4s.isEmpty()){
									String out = "";
									for(List<Graph.Vertex<Integer>> k4: k4s){
										out += printList(k4)+"\n";
									}
									out = String.format("K4 found%nVertices:%n%s", out);
									out += String.format("Number of K4s found: %d%n", k4s.size());
									out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}else{
									String out = String.format("K4 not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
									System.out.println(out);
								}
							}
							else if (words[1].equals("kl")) {
								try{
									System.out.println("Please enter size of complete subgraph to be listed");
									System.out.print("> ");
									input = sc.nextLine();
									int l = Integer.parseInt(input);
									if(l<1){
										System.out.println("Size of graph induced subgraph should be greater than zero");
									}
									else{
										ListKL d = new ListKL();
										List<List<Vertex<Integer>>> kls = d.detect(graph, l);
										if(!kls.isEmpty()){
											String out = "";
											for(List<Graph.Vertex<Integer>> kl: kls){
												out += printList(kl)+"\n";
											}
											out = String.format("K"+l+" found%nVertices:%n%s", out);
											out += String.format("Number of K"+l+"s found: %d%n", kls.size());
											out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
											System.out.println(out);
										}else{
											String out = String.format("K"+l+" not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
											System.out.println(out);
										}
									}
								}catch(NumberFormatException e){
									System.out.println("Value entered is not an integer");
								}
							}
							else{
								System.out.println("For listing operation, the second argument should be "
										+ "the listing pattern, e.g. diamond, claw, triangle, k4, kl, simplicial");
							}
						}	
					}
				}
			}
			else if(words[0].equals("generate")){
				if(words.length < 3){
					System.out.println(errorMessage);
				}else{
					PatternFreeGraphGenerator g = new PatternFreeGraphGenerator();
					List<String> allowed = new ArrayList<String>();
					allowed.add("triangle"); allowed.add("claw"); allowed.add("diamond");
					allowed.add("k4"); allowed.add("kl");
					
					if(allowed.contains(words[1])){
						try{
							int n = Integer.parseInt(words[2]);
							if(n<1){
								System.out.println("Size of graph should be greater than zero");
							}else{
								if (words[1].equals("triangle")) {
									String filename = g.generateTriangleFreeGraph(n);
									String out = String.format("Name of graph file: %s", filename);
									System.out.println(out);
								}
				
								else if (words[1].equals("claw")) {
									String filename = g.generateClawFreeGraph(n);
									String out = String.format("Name of graph file: %s", filename);
									System.out.println(out);
								}
				
								else if (words[1].equals("diamond")) {
									String filename = g.generateDiamondFreeGraph(n);
									String out = String.format("Name of graph file: %s", filename);
									System.out.println(out);
								}
				
								else if (words[1].equals("k4")) {
									String filename = g.generateK4FreeGraph(n);
									String out = String.format("Name of graph file: %s", filename);
									System.out.println(out);
								}
								else if (words[1].equals("kl")) {
									try{
										System.out.println("Please enter size of complete subgraph");
										System.out.print("> ");
										input = sc.nextLine();
										int l = Integer.parseInt(input);
										if(l<1){
											System.out.println("Size of graph induced subgraph should be greater than zero");
										}else{
											String filename = g.generateKLFreeGraph(n,l);
											String out = String.format("Name of graph file: %s", filename);
											System.out.println(out);
										}
									}catch(NumberFormatException e){
										System.out.println("Value entered is not an integer");
									}
								}
							}
						}catch(NumberFormatException e){
							System.out.println("Value entered is not an integer");
						}
					}else{
						System.out.println("For generation operation, the second argument should be among: "
								+ "diamond, claw, triangle, k4, kl");
					}
				}
			}
			else if(words[0].equals("test")){
				
			}
			else if(words[0].equals("gui")){
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							GUI frame = new GUI();
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			else if(words[0].equals("help")){
				System.out.println(info);
				System.out.println(operationvalues);
				System.out.println(detectionValues);
			}
			else{
				System.out.println("Invalid keyword entered.");
				System.out.println("The first argument should be one of the following: "
						+ "detect, generate, quit, list, test, gui ");
			}
			System.out.print("> ");
			input = sc.nextLine();
			words = input.split("[ ]+");
		}
		sc.close();
		if(words[0].equals("quit")){
			System.exit(0);
		}
	}

	public static String printList(List<Graph.Vertex<Integer>> list){
		String out = "";

		for(Graph.Vertex<Integer> v:list){
			out+=v.getElement()+","; 
		}
		out = out.substring(0,out.length()-1);
		return out;
	}
	
	public static int getTotalTime(String s){
		String[] tokens = s.split("[ ]+");
		int time = 0;
		int limit = tokens[tokens.length-2].equals("not") ? tokens.length-2: tokens.length-1;
		for(int i=0; i<limit; i++){
			if(!tokens[i].equals("-"))
				time += Integer.parseInt(tokens[i]);
		}
		return time;
	}
}
