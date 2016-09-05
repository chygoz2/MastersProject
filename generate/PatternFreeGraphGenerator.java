package generate;

import java.util.List;
import java.util.Random;

import efficient.detection.DetectClaw;
import efficient.detection.DetectDiamond;
import efficient.detection.DetectK4;
import efficient.detection.DetectKL;
import efficient.detection.DetectTriangle;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class PatternFreeGraphGenerator {
	
	private long time;
	
	/**
	 * main method which allows direct access to the class via the command line terminal.
	 * @param args		command line arguments
	 */
	public static void main(String[] args){
		PatternFreeGraphGenerator g = new PatternFreeGraphGenerator();
		String type = "";
		int size=0, l=0;
		if(args.length>0){
			try{
				type = args[0]; //type of subgraph to be absent
				size = Integer.parseInt(args[1]); //graph size to be generated
				if(size<1){
					System.out.println("The second argument should be greater than 0");
				}else{
					if(type.equals("triangle")){ //for triangle-free generation
						String filename = g.generateTriangleFreeGraph(size);
						System.out.printf("Name of graph file: %s%n", filename);
						System.out.printf("CPU time taken: %d milliseconds", g.getTime());
					}
					else if(type.equals("claw")){//for claw-free generation
						String filename = g.generateClawFreeGraph(size);
						System.out.printf("Name of graph file: %s%n", filename);
						System.out.printf("CPU time taken: %d milliseconds", g.getTime());
					}
					else if(type.equals("diamond")){ //for diamond-free generation
						String filename = g.generateDiamondFreeGraph(size);
						System.out.printf("Name of graph file: %s%n", filename);
						System.out.printf("CPU time taken: %d milliseconds", g.getTime());
					}
					else if(type.equals("k4")){ //for K4-free generation
						String filename = g.generateK4FreeGraph(size);
						System.out.printf("Name of graph file: %s%n", filename);
						System.out.printf("CPU time taken: %d milliseconds", g.getTime());
					}
					else if(type.equals("kl")){ //for KL-free generation
						try{
							l = Integer.parseInt(args[2]); //size of complete subgraph to be absent
							if(l<2){ //size of complete subgraph should be greater than one
								System.out.println("The third argument should be greater than 1");
							}else{
								String filename = g.generateKLFreeGraph(size, l);
								System.out.printf("Name of graph file: %s%n", filename);
								System.out.printf("CPU time taken: %d milliseconds", g.getTime());
							}
						}catch(NumberFormatException e){
							System.out.println("The third argument should be an integer");
						}
					}
					else{
						System.out.println("The first argument should be triangle, claw, diamond, k4 or kl");
					}
				}
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("Arguments should be in the form [generation_type] [graph_size] "
						+ "[complete_subgraph_size].\nThe 3rd argument is needed only for complete subgraphs"
						+ " whose size is greater than 4");
			}catch(NumberFormatException e){
				System.out.println("The second argument should be an integer");
			}
		}
	}
	
	/**
	 * method to generate graph that is diamond-free
	 * @param n		the size of the graph to be generated
	 * @return		the name of the graph file
	 */
	public String generateDiamondFreeGraph(int n){		
		
		long starttime = System.currentTimeMillis();
		int[][] adjMatrix = new int[n][n];
		Random random = new Random(System.currentTimeMillis());
		//for each edge added, check if the resulting graph is diamond-free
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					List<Graph.Vertex<Integer>> diamond = new DetectDiamond().detect(graph);
					if(diamond!=null){
						adjMatrix[i][j] = 0;
						adjMatrix[j][i] = 0;
					}
				}
			}
		}
		time = System.currentTimeMillis()-starttime; //calculate time taken
		String file = Utility.saveGraphToFile(adjMatrix, System.currentTimeMillis(), "diamond");//save generated graph to file
		return file;
	}
	
	/**
	 * method to generate graph that is triangle-free
	 * @param n		the size of the graph to be generated
	 * @return		the name of the graph file
	 */
	public String generateTriangleFreeGraph(int n){
		long starttime = System.currentTimeMillis();
		int[][] adjMatrix = new int[n][n];
		
		Random random = new Random(System.currentTimeMillis());
		//for each edge added, check if the resulting graph is triangle-free
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					List<Graph.Vertex<Integer>> triangle = new DetectTriangle().detect(graph);
					if(triangle!=null){
						adjMatrix[i][j] = 0;
						adjMatrix[j][i] = 0;
					}
				}
			}
		}
		
		time = System.currentTimeMillis()-starttime; //calculate time taken
		String file = Utility.saveGraphToFile(adjMatrix, System.currentTimeMillis(), "triangle");//save generated graph to file
		return file;
	}
	
	/**
	 * method to generate graph that is k4-free
	 * @param n		the size of the graph to be generated
	 * @return		the name of the graph file
	 */
	public String generateK4FreeGraph(int n){
		long starttime = System.currentTimeMillis();
		int[][] adjMatrix = new int[n][n];
		
		Random random = new Random(System.currentTimeMillis());
		//for each edge added, check if the resulting graph is k4-free
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					List<Graph.Vertex<Integer>> k4 = new DetectK4().detect(graph);
					if(k4!=null){
						adjMatrix[i][j] = 0;
						adjMatrix[j][i] = 0;
					}
				}
			}
		}
		
		time = System.currentTimeMillis()-starttime;//calculate time taken
		String file = Utility.saveGraphToFile(adjMatrix, System.currentTimeMillis(), "k4");//save generated graph to file
		return file;
	}
	
	/**
	 * method to generate graph that is claw-free
	 * @param n		the size of the graph to be generated
	 * @return		the name of the graph file
	 */
	public String generateClawFreeGraph(int n){
		long starttime = System.currentTimeMillis();
		int[][] adjMatrix = new int[n][n];
		
		Random random = new Random(System.currentTimeMillis());
		//for each edge added, check if the resulting graph is claw-free
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					List<Graph.Vertex<Integer>> claw = new DetectClaw().detect(graph);
					if(claw!=null){
						adjMatrix[i][j] = 0;
						adjMatrix[j][i] = 0;
					}
				}
			}
		}
		
		time = System.currentTimeMillis()-starttime;//calculate time taken
		String file = Utility.saveGraphToFile(adjMatrix, System.currentTimeMillis(), "claw");//save generated graph to file
		return file;
	}
	
	/**
	 * method to generate graphs that are kl-free
	 * @param n		the size of the graph to be generated
	 * @param l		the size of the complete subgraph that should be absent in generated graph
	 * @return		the name of the graph file
	 */
	public String generateKLFreeGraph(int n, int l){
		long starttime = System.currentTimeMillis();
		int[][] adjMatrix = new int[n][n];
		
		Random random = new Random(System.currentTimeMillis());
		//for each edge added, check if the resulting graph is kl-free
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					List<Graph.Vertex<Integer>> kl = new DetectKL().detect(graph,l);
					if(kl!=null){
						adjMatrix[i][j] = 0;
						adjMatrix[j][i] = 0;
					}
				}
			}
		}
		
		time = System.currentTimeMillis()-starttime;//calculate time taken
		String file = Utility.saveGraphToFile(adjMatrix, System.currentTimeMillis(), "k"+l);//save generated graph to file
		return file;
	}
	
	public long getTime(){
		return time;
	}
	
}
