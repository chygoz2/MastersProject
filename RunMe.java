import java.awt.EventQueue;
import java.io.File;
import java.util.Iterator;
import java.util.Scanner;

import detectsubgraphs.*;
import general.*;

public class RunMe {
	public static void main(String[] args){
		String commands = "help, detect, generate, quit, triangle, claw, k4, kl, diamond, simplicial, gui, test";

	    System.out.println(commands);
	    Scanner sc = new Scanner(System.in);
	   
	    System.out.print("> ");
	    String input = sc.nextLine();
	    
	    String[] words = input.split("[ ]+");
	    
	    while(!words[0].equals("quit")){
	    	if(words[0].equals("help")){
	    		System.out.println(commands);
	    	}
	    	
	    	if (words[0].equals("detect")) {
				
				String path = words[2];
				File fr= new File(path);
				if(!fr.exists())
					System.out.println("File not found");
				else{	
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(path);
					if(graph!=null){
						if (words[1].equals("triangle")) {
							Graph<Integer,Integer> triangle = DetectTriangle.detect(graph).get(0);
							if(triangle!=null){
								Iterator<Graph.Vertex<Integer>> vertices = triangle.vertices();
								String out = "";
								while(vertices.hasNext()){
									out+=vertices.next().getElement()+",";
								}
								out = String.format("Triangle found %n%s", out.substring(0,out.length()-1));
								System.out.println(out);
							}else
								System.out.println("Triangle not found");
						}
						
						else if (words[1].equals("claw")) {
							UndirectedGraph<Integer,Integer> claw = DetectClaw.detect(graph);
							if(claw!=null){
								Iterator<Graph.Vertex<Integer>> vertices = claw.vertices();
								String out = "";
								
								while(vertices.hasNext()){
									out+=vertices.next().getElement()+","; 
								}
								out = String.format("Claw found %n%s", out.substring(0,out.length()-1));
								System.out.println(out);
							}else
								System.out.println("Claw not found");
						}
						
						else if (words[1].equals("diamond")) {
							UndirectedGraph<Integer,Integer> diamond = DetectDiamond.detect(graph);
							if(diamond!=null){
								Iterator<Graph.Vertex<Integer>> vertices = diamond.vertices();
								String out = "";
								
								while(vertices.hasNext()){
									out+=vertices.next().getElement()+","; 
								}
								out = String.format("Diamond found %n%s", out.substring(0,out.length()-1));
								System.out.println(out);
							}else
								System.out.println("Diamond not found");
						}
						
						else if (words[1].equals("simplicial")) {
							Graph.Vertex<Integer> simpVertex = DetectSimplicialVertex.detect(graph);
							if(simpVertex!=null){
								String out = simpVertex.getElement()+"";
								out = String.format("Simplicial vertex found %n%s", out);
								System.out.println(out);
							}else
								System.out.println("Simplicial vertex not found");
						}	
						
						else if (words[1].equals("k4")) {
							UndirectedGraph<Integer,Integer> k4 = DetectK4.detect(graph);
							if(k4!=null){
								Iterator<Graph.Vertex<Integer>> vertices = k4.vertices();
								String out = "";
								while(vertices.hasNext()){
									out+=vertices.next().getElement()+",";
								}
								out = String.format("K4 found %n%s", out.substring(0,out.length()-1));
								System.out.println(out);
							}else
								System.out.println("K4 not found");
						}
						
					}	
				}
			}else if(words[0].equals("gui")){
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							GUI frame = new GUI();
							frame.setVisible(true);
							frame.setAlwaysOnTop(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}else if(words[0].equals("quit")){
				System.exit(0);
			}else if(words[0].equals("generate")){
				try{
					int vertexCount = Integer.parseInt(words[2]);
					if(vertexCount<0)
						System.out.println("The value entered for vertex count is not a valid number");
					else{
						int graphCount = 0;
						try{
							graphCount = Integer.parseInt(words[3]);
						}catch(ArrayIndexOutOfBoundsException e){
							System.out.println("The arguments are not complete");
						}
						if(graphCount<1)
							System.out.println("The value entered for graph count should be greater than zero");
						else{
							String name = null;
							if (words[1].equals("triangle")) {
								String n = "";
								for(int i=1; i<=graphCount; i++){
									int[][] A = GraphGenerator.generateTriangleFreeGraph(vertexCount);
									UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(A);
									n += Utility.saveGraphToFile(graph, i, "triangle")+System.lineSeparator();
								}
								name = n;
							}
							
							else if (words[1].equals("claw")) {
								String n = "";
								for(int i=1; i<=graphCount; i++){
									int[][] A = GraphGenerator.generateClawFreeGraph(vertexCount);
									UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(A);
									n += Utility.saveGraphToFile(graph, i, "claw")+System.lineSeparator();
								}
								name = n;
							}
							
							else if (words[1].equals("diamond")) {
								String n = "";
								for(int i=1; i<=graphCount; i++){
									int[][] A = GraphGenerator.generateDiamondFreeGraph(vertexCount);
									UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(A);
									n+=Utility.saveGraphToFile(graph, i, "diamond")+System.lineSeparator();
								}
								name = n;
							}
											
							else if (words[1].equals("k4")) {
								String n = "";
								for(int i=1; i<=graphCount; i++){
									int[][] A = GraphGenerator.generateK4FreeGraph(vertexCount);
									UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(A);
									n+=Utility.saveGraphToFile(graph, i, "k4")+System.lineSeparator();
								}
								name = n;
							}
							System.out.println(name);
						}
					}
						
				}catch(NumberFormatException e){
					System.out.println("The value entered for vertex count is not a valid number");
				}
				
			}
	    	
	    	System.out.print("> ");
			input = sc.nextLine();
			words = input.split("[ ]+");
	    }
	    sc.close();
	}
}
