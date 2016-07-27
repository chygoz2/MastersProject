import java.awt.EventQueue;
import java.io.File;
import java.util.Iterator;
import java.util.Scanner;

import detectsubgraphs.*;
import general.*;

public class RunMe {
	public static void main(String[] args){
		String commands = "help, detect, generate, quit, triangle, claw, k4, kl, diamond, simplicial, gui";

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
							Graph<Integer,Integer> triangle = DetectTriangle.detect(graph);
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
			}
	    	
	    	System.out.print("> ");
			input = sc.nextLine();
			words = input.split("[ ]+");
	    }
//	    String command = sc.next();
//
//	    while(!command.equals("quit")){
//	    	
//			if (command.equals("help")){
//				System.out.println(commands);
//			}
//	
//			if (command.equals("detect")) {
//				command = sc.next();
//				
//				String path = sc.next();
//				File fr= new File(path);
//				if(!fr.exists())
//					System.out.println("File not found");
//				else{	
//					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(path);
//					if(graph!=null){
//						if (command.equals("triangle")) {
//							Graph<Integer,Integer> triangle = DetectTriangle.detect(graph);
//							if(triangle!=null){
//								Iterator<Graph.Vertex<Integer>> vertices = triangle.vertices();
//								String out = "";
//								while(vertices.hasNext()){
//									out+=vertices.next().getElement()+",";
//								}
//								out = String.format("Triangle found %n%s", out.substring(0,out.length()-1));
//								System.out.println(out);
//							}else
//								System.out.println("Triangle not found");
//						}
//						
//						else if (command.equals("claw")) {
//							UndirectedGraph<Integer,Integer> claw = DetectClaw.detect(graph);
//							if(claw!=null){
//								Iterator<Graph.Vertex<Integer>> vertices = claw.vertices();
//								String out = "";
//								
//								while(vertices.hasNext()){
//									out+=vertices.next().getElement()+","; 
//								}
//								out = String.format("Claw found %n%s", out.substring(0,out.length()-1));
//								System.out.println(out);
//							}else
//								System.out.println("Claw not found");
//						}
//						
//						else if (command.equals("diamond")) {
//							UndirectedGraph<Integer,Integer> diamond = DetectDiamond.detect(graph);
//							if(diamond!=null){
//								Iterator<Graph.Vertex<Integer>> vertices = diamond.vertices();
//								String out = "";
//								
//								while(vertices.hasNext()){
//									out+=vertices.next().getElement()+","; 
//								}
//								out = String.format("Diamond found %n%s", out.substring(0,out.length()-1));
//								System.out.println(out);
//							}else
//								System.out.println("Diamond not found");
//						}
//						
//						else if (command.equals("simplicial")) {
//							Graph.Vertex<Integer> simpVertex = DetectSimplicialVertex.detect(graph);
//							if(simpVertex!=null){
//								String out = simpVertex.getElement()+"";
//								out = String.format("Simplicial vertex found %n%s", out);
//								System.out.println(out);
//							}else
//								System.out.println("Simplicial vertex not found");
//						}	
//						
//						else if (command.equals("k4")) {
//							UndirectedGraph<Integer,Integer> k4 = DetectK4.detect(graph);
//							if(k4!=null){
//								Iterator<Graph.Vertex<Integer>> vertices = k4.vertices();
//								String out = "";
//								while(vertices.hasNext()){
//									out+=vertices.next().getElement()+",";
//								}
//								out = String.format("K4 found %n%s", out.substring(0,out.length()-1));
//								System.out.println(out);
//							}else
//								System.out.println("K4 not found");
//						}
//						
//					}	
//				}
//			}
//			
//			if (command.equals("generate")) {
//				
//			}
//			
//			if (command.equals("quit")) {
//				System.exit(0);
//			}
//			
//			if (command.equals("gui")) {
//				EventQueue.invokeLater(new Runnable() {
//					public void run() {
//						try {
//							GUI frame = new GUI();
//							frame.setVisible(true);
//							frame.setAlwaysOnTop(true);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				});
//			}
//			
//			if (command.equals("k4")) {
//				
//			}
//			
//			if (command.equals("kl")) {
//				
//			}
//	
//		}
	    sc.close();
	}
}
