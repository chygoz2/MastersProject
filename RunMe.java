import java.awt.EventQueue;
import java.io.File;
import java.util.List;
import java.util.Scanner;

import efficientdetection.*;
import efficientlisting.ListSimplicialVertices;
import general.*;
import generate.GraphGenerator;
import gui.GUI;

public class RunMe {
	public static void main(String[] args){
		String commands = "help, detect, generate, quit, triangle, claw, k4, kl, diamond, simplicial, gui, test";

		System.out.println(commands);
		Scanner sc = new Scanner(System.in);

		System.out.print("> ");
		String input = sc.nextLine();

		String[] words = input.split("[ ]+");
		
		while(!words[0].equals("quit")){
			if(words.length > 3){
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
								List<Graph.Vertex<Integer>> triangle = new DetectTriangle().detect(graph);
								if(triangle!=null){
									String out = printList(triangle);
									out = String.format("Triangle found %n%s", out.substring(0,out.length()-1));
									System.out.println(out);
								}else
									System.out.println("Triangle not found");
							}

							else if (words[1].equals("claw")) {
								List<Graph.Vertex<Integer>> claw = new DetectClaw().detect(graph);
								if(claw!=null){
									String out = printList(claw);
									out = String.format("Claw found %n%s", out.substring(0,out.length()-1));
									System.out.println(out);
								}else
									System.out.println("Claw not found");
							}

							else if (words[1].equals("diamond")) {
								List<Graph.Vertex<Integer>> diamond = new DetectDiamond().detect(graph);
								if(diamond!=null){
									String out = printList(diamond);
									out = String.format("Diamond found %n%s", out.substring(0,out.length()-1));
									System.out.println(out);
								}else
									System.out.println("Diamond not found");
							}

							else if (words[1].equals("simplicial")) {
								List<Graph.Vertex<Integer>> simpVertex = new ListSimplicialVertices().detect(graph);
								if(!simpVertex.isEmpty()){
									String out = simpVertex.get(0).getElement()+"";
									out = String.format("Simplicial vertex found %n%s", out);
									System.out.println(out);
								}else
									System.out.println("Simplicial vertex not found");
							}	

							else if (words[1].equals("k4")) {
								List<Graph.Vertex<Integer>> k4 = new DetectK4().detect(graph);
								if(k4!=null){
									String out = printList(k4);
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
		if(!words[0].equals("gui") && !words[0].equals("quit") && !words[0].equals("test")
				    && !words[0].equals("help")){
			System.out.println("You entered fewer arguments than required.");
			System.out.println("Arguments should be in the form");
			System.out.println("[operation_type] [detection_type] [graph_file]");
		}
	}

	public static String printList(List<Graph.Vertex<Integer>> list){
		String out = "";

		for(Graph.Vertex<Integer> v:list){
			out+=v.getElement()+","; 
		}
		return out;
	}
}
