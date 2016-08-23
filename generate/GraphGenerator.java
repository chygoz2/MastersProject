package generate;

import java.util.List;
import java.util.Random;

import efficientdetection.*;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class GraphGenerator {
	
	public String generateDiamondFreeGraph(int n){		
		int[][] adjMatrix = new int[n][n];
		
		Random random = new Random(System.currentTimeMillis());
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
		
		String file = new Utility().saveGraphToFile(adjMatrix, System.currentTimeMillis(), "diamond");
		return file;
	}
	
	public String generateTriangleFreeGraph(int n){
		int[][] adjMatrix = new int[n][n];
		
		Random random = new Random(System.currentTimeMillis());
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
		
		String file = new Utility().saveGraphToFile(adjMatrix, System.currentTimeMillis(), "triangle");
		return file;
	}
	
	public String generateK4FreeGraph(int n){
		int[][] adjMatrix = new int[n][n];
		
		Random random = new Random(System.currentTimeMillis());
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
		
		String file = new Utility().saveGraphToFile(adjMatrix, System.currentTimeMillis(), "k4");
		return file;
	}
	
	public String generateClawFreeGraph(int n){
		int[][] adjMatrix = new int[n][n];
		
		Random random = new Random(System.currentTimeMillis());
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
		
		String file = new Utility().saveGraphToFile(adjMatrix, System.currentTimeMillis(), "claw");
		return file;
	}
	
	public String generateKLFreeGraph(int n, int l){
		int[][] adjMatrix = new int[n][n];
		
		Random random = new Random(System.currentTimeMillis());
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
		
		String file = new Utility().saveGraphToFile(adjMatrix, System.currentTimeMillis(), "k"+l);
		return file;
	}
	
//	public int[][] generateSimplicialFreeGraph(int n){
//		if(n<1){
//			JOptionPane.showMessageDialog(null, "n should be greater than zero");
//			System.exit(0);
//		}
//		
//		int[][] adjMatrix = new int[n][n];
//		
//		ThreadLocalRandom random = ThreadLocalRandom.current();
//		for(int i=0; i<n; i++){
//			for(int j=i+1; j<n; j++){
//				int rand = random.nextInt(10);
//				if(rand>4){
//					adjMatrix[i][j] = 1;
//					adjMatrix[j][i] = 1;
//					
//					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
//					List<Graph.Vertex<Integer>> v = ListSimplicialVertex.detect(graph);
//					if(!v.isEmpty()){
//						adjMatrix[i][j] = 0;
//						adjMatrix[j][i] = 0;
//					}
//				}
//			}
//		}
//		
//		return adjMatrix;
//	}
	
}
