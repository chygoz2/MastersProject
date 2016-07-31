package general;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

import detectsubgraphs.*;

public class GraphGenerator {
	
	public static int[][] generateDiamondFreeGraph(int n){
		if(n<1){
			JOptionPane.showMessageDialog(null, "n should be greater than zero");
			System.exit(0);
		}
		
		int[][] adjMatrix = new int[n][n];
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					UndirectedGraph<Integer,Integer> diamond = DetectDiamond.detect(graph);
					if(diamond!=null){
						adjMatrix[i][j] = 0;
						adjMatrix[j][i] = 0;
					}
				}
			}
		}
		
		return adjMatrix;
	}
	
	public static int[][] generateTriangleFreeGraph(int n){
		if(n<1){
			JOptionPane.showMessageDialog(null, "n should be greater than zero");
			System.exit(0);
		}
		
		int[][] adjMatrix = new int[n][n];
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					List<UndirectedGraph<Integer,Integer>> triangle = DetectTriangle.detect(graph);
					if(!triangle.isEmpty()){
						adjMatrix[i][j] = 0;
						adjMatrix[j][i] = 0;
					}
				}
			}
		}
		
		return adjMatrix;
	}
	
	public static int[][] generateK4FreeGraph(int n){
		if(n<1){
			JOptionPane.showMessageDialog(null, "n should be greater than zero");
			System.exit(0);
		}
		
		int[][] adjMatrix = new int[n][n];
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					List<UndirectedGraph<Integer,Integer>> k4 = DetectK4.detect(graph);
					if(!k4.isEmpty()){
						adjMatrix[i][j] = 0;
						adjMatrix[j][i] = 0;
					}
				}
			}
		}
		
		return adjMatrix;
	}
	
	public static int[][] generateClawFreeGraph(int n){
		if(n<1){
			JOptionPane.showMessageDialog(null, "n should be greater than zero");
			System.exit(0);
		}
		
		int[][] adjMatrix = new int[n][n];
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					UndirectedGraph<Integer,Integer> claw = DetectClaw.detect(graph);
					if(claw!=null){
						adjMatrix[i][j] = 0;
						adjMatrix[j][i] = 0;
					}
				}
			}
		}
		
		return adjMatrix;
	}
	
	public static int[][] generateSimplicialFreeGraph(int n){
		if(n<1){
			JOptionPane.showMessageDialog(null, "n should be greater than zero");
			System.exit(0);
		}
		
		int[][] adjMatrix = new int[n][n];
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				int rand = random.nextInt(10);
				if(rand>4){
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
					
					UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(adjMatrix);
					Graph.Vertex<Integer> v = DetectSimplicialVertex.detect(graph);
					if(v!=null){
						if(j+1!=n){
							j++;
							adjMatrix[i][j] = 1;
							adjMatrix[j][i] = 1;
						}else{
							adjMatrix[i][j] = 0;
							adjMatrix[j][i] = 0;
						}
					}
				}
			}
		}
		
		return adjMatrix;
	}
	
	public static void main(String [] args){
//		int[][] A = generateDiamondFreeGraph(20);
//		
//		for(int i=0;i<A.length;i++){
//			for(int j=0; j<A.length; j++){
//				System.out.print(A[i][j]+" ");
//			}
//			System.out.println();
//		}
//		
//		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(A);
//		Utility.saveGraphToFile(graph, 0.5, 20);
		
		int[][] A = generateTriangleFreeGraph(20);
		
		for(int i=0;i<A.length;i++){
			for(int j=0; j<A.length; j++){
				System.out.print(A[i][j]+" ");
			}
			System.out.println();
		}
		
		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(A);
		Utility.saveGraphToFile(graph, 21, "triangle");
		
//		int[][] A = generateClawFreeGraph(20);
//		
//		for(int i=0;i<A.length;i++){
//			for(int j=0; j<A.length; j++){
//				System.out.print(A[i][j]+" ");
//			}
//			System.out.println();
//		}
//		
//		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(A);
//		Utility.saveGraphToFile(graph, 0.5, 22);
		
//		int[][] A = generateK4FreeGraph(20);
//		
//		for(int i=0;i<A.length;i++){
//			for(int j=0; j<A.length; j++){
//				System.out.print(A[i][j]+" ");
//			}
//			System.out.println();
//		}
//		
//		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(A);
//		Utility.saveGraphToFile(graph, 0.5, 23);
		
//		int[][] A = generateSimplicialFreeGraph(5);
//		
//		for(int i=0;i<A.length;i++){
//			for(int j=0; j<A.length; j++){
//				System.out.print(A[i][j]+" ");
//			}
//			System.out.println();
//		}
//		
//		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromAdjacencyMatrix(A);
//		Utility.saveGraphToFile(graph, 0.5, 24);
//		
		
	}
}
