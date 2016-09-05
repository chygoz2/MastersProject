package test;

import java.io.FileWriter;
import java.io.IOException;

import generate.PatternFreeGraphGenerator;

public class EvaluateGenerator {
	public static void main(String[] args){
		System.out.println("Running...");
		PatternFreeGraphGenerator g = new PatternFreeGraphGenerator();
		int n = 510;
		
		//for triangle-free graphs
		StringBuilder out1 = new StringBuilder();
		out1.append("Triangle-free\r\n");
		for(int i=200; i<n; i+=10){
			g.generateTriangleFreeGraph(i);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		//for diamond-free graphs
		out1.append("Diamond-free\r\n");
		for(int i=200; i<n; i+=10){
			g.generateDiamondFreeGraph(i);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		//for claw-free graphs
		out1.append("Claw-free\r\n");
		for(int i=200; i<n; i+=10){
			g.generateClawFreeGraph(i);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		//for k4-free graphs
		out1.append("K4-free\r\n");
		for(int i=200; i<n; i+=10){
			g.generateK4FreeGraph(i);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		n = 50;
		//for k5-free graphs
		out1.append("K5-free\r\n");
		for(int i=21; i<=n; i+=1){
			g.generateKLFreeGraph(i,5);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		//for k6-free graphs
		out1.append("K6-free\r\n");
		for(int i=21; i<=n; i+=1){
			g.generateKLFreeGraph(i,6);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		//for k6-free graphs
		out1.append("K7-free\r\n");
		for(int i=21; i<=n; i+=1){
			g.generateKLFreeGraph(i,7);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		//for k6-free graphs
		out1.append("K8-free\r\n");
		for(int i=21; i<=n; i+=1){
			g.generateKLFreeGraph(i,8);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		//for k6-free graphs
		out1.append("K9-free\r\n");
		for(int i=21; i<=n; i+=1){
			g.generateKLFreeGraph(i,9);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		//for k6-free graphs
		out1.append("K10-free\r\n");
		for(int i=10; i<=n; i+=1){
			g.generateKLFreeGraph(i,10);
			out1.append(String.format("Graph size: %d, Time taken: %d%n", i, g.getTime()));
		}
		out1.append("\r\n");
		
		System.out.println("Completed...");
		try {
			FileWriter writer = new FileWriter("graph generator times.txt");
			writer.write(out1.toString());
			writer.close();
		} catch (IOException e) {
			
		}
	}
}
