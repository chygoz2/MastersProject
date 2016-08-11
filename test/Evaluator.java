package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

import general.UndirectedGraph;
import general.Utility;

public class Evaluator {

	static boolean done;
	static List<String> graphs;
	
	public static void main(String[] args) throws InterruptedException {
		//generate graphs
		done = false;
		new Thread(new Runnable(){

			@Override
			public void run() {
				generateRandomGraphs(20, 15);
			}
			
		}).start();
		System.out.print("Generating graphs");
//		while(!done){
//			System.out.print(".");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println("Done");
		
		//get graphs
		done = false;
		new Thread(new Runnable(){

			@Override
			public void run() {
				graphs = getGeneratedFiles();
			}
			
		}).start();
		System.out.println("Reading graphs");
		while(!done){
			System.out.print(".");
			Thread.sleep(1000);
		}
		System.out.println("Done");		
		
		//run efficient triangle detection
		done = false;
		Thread t1 = new Thread(new Runnable(){

			@Override
			public void run() {
				runEfficientTriangleDetection();
			}
			
		});
		t1.start();
		System.out.println("Running efficient triangle detection");
		while(!done){
			System.out.print(".");
		
			Thread.sleep(1000);
		}
		System.out.println("Done");
		
		//run efficient diamond detection
		done = false;
		Thread t2 = new Thread(new Runnable(){

			@Override
			public void run() {
				runEfficientDiamondDetection();
			}
			
		});
		t2.start();
		
		System.out.println("Running efficient diamond detection");
		while(!done){
			System.out.print(".");
			Thread.sleep(1000);
		}
		System.out.println("Done");
		
		//run efficient K4 detection
		done = false;
		Thread t3 = new Thread(new Runnable(){

			@Override
			public void run() {
				runEfficientK4Detection();
			}
			
		});
		t3.start();
		
		System.out.println("Running efficient K4 detection");
		while(!done){
			System.out.print(".");
			Thread.sleep(1000);
		}
		System.out.println("Done");
		
		//run efficient claw detection
		done = false;
		Thread t4 = new Thread(new Runnable(){

			@Override
			public void run() {
				runEfficientClawDetection();
			}
			
		});
		t4.start();
		
		System.out.println("Running efficient claw detection");
		while(!done){
			System.out.print(".");
			Thread.sleep(1000);
		}
		System.out.println("Done");
		
		//run simplicial vertex detection
		done = false;
		new Thread(new Runnable(){

			@Override
			public void run() {
				runSimplicialVertexDetection();
			}
			
		}).start();
		
		System.out.println("Running simplicial vertex detection");
		while(!done){
			System.out.print(".");
			Thread.sleep(1000);
		}
		System.out.println("Done");
		
		//run Kl listing
//		done = false;
//		new Thread(new Runnable(){
//
//			@Override
//			public void run() {
//				runKLListing(Integer.parseInt(args[0]));
//			}
//			
//		}).start();
//		
//		System.out.println("Running KL listing");
//		while(!done){
//			System.out.print(".");
//			Thread.sleep(1000);
//		}
//		System.out.println("Done");
		
	}
	
	/**
	 * method to generate random graphs
	 * @param vmax		the maximum number of vertices 
	 * @param no		the number of graphs for each graph size
	 */
	public static void generateRandomGraphs(int vmax, int no){
		for(int v=1; v<=vmax; v++){
			for(double p=0.1; p<=1; p+=0.1){
				Utility.generateRandomGraphFile(v, Math.round(p*10)/10.0, no);
			}
		}
		done = true;
	}
	
	public static List<String> getGeneratedFiles(){
		List<String> graphs = new ArrayList<String>();
		File file = new File("");
		String path = file.getAbsolutePath();
		
		//enter generated graphs folder
		String ggFolder = "generated_graphs";
		File dir = new File(path+File.separator+ggFolder);
		
		String[] graphFolders = dir.list();
		for(String s: graphFolders){
			//enter each graph size folder and print out the graph files in it
			File dir2 = new File(path+File.separator+ggFolder+File.separator+s);
			String[] graphFiles = dir2.list();
			for(String t: graphFiles){
				String graphFileName = dir2.getAbsolutePath()+File.separator+t;
				graphs.add(graphFileName);
			}
		}
		
		done = true;
		return graphs;
	}
	
	public static void runEfficientTriangleDetection(){
		String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
		for(String gname: graphs){
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			efficientdetection.DetectTriangle.detect(graph);
			String result = efficientdetection.DetectTriangle.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			efficientdetection.DetectTriangle.resetResult();
		}
		saveResultToFile(output, "efficient_triangle_detection_result");
		done = true;
	}
	
	public static void runEfficientDiamondDetection(){
		String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
		for(String gname: graphs){
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			efficientdetection.DetectDiamond.detect(graph);
			String result = efficientdetection.DetectDiamond.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			efficientdetection.DetectDiamond.resetResult();
		}
		saveResultToFile(output, "efficient_diamond_detection_result");
		done = true;
	}
	
	public static void runEfficientK4Detection(){
		String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
		for(String gname: graphs){
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			efficientdetection.DetectK4.detect(graph);
			String result = efficientdetection.DetectK4.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			efficientdetection.DetectK4.resetResult();
		}
		saveResultToFile(output, "efficient_K4_detection_result");
		done = true;
	}
	
	public static void runEfficientClawDetection(){
		String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
		for(String gname: graphs){
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			efficientdetection.DetectClaw.detect(graph);
			String result = efficientdetection.DetectClaw.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			efficientdetection.DetectClaw.resetResult();
		}
		saveResultToFile(output, "efficient_Claw_detection_result");
		done = true;
	}
	
	public static void runSimplicialVertexDetection(){
		String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
		for(String gname: graphs){
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			efficientdetection.DetectSimplicialVertex.detect(graph);
			String result = efficientdetection.DetectSimplicialVertex.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			efficientdetection.DetectSimplicialVertex.resetResult();
		}
		saveResultToFile(output, "efficient_Simplicial_vertex_detection_result");
		done = true;
	}
	
	public static void runKLListing(int l){
		String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
		for(String gname: graphs){
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			listing.ListKL3.detect(graph,l);
			String result = listing.ListKL3.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			listing.ListKL3.resetResult();
		}
		saveResultToFile(output, "efficient_KL_listing_result");
		done = true;
	}
	
	public static void saveResultToFile(String result, String filename){
		File f = new File("");
		String path = f.getAbsolutePath();
		File dir = new File(path+File.separator+"results");
		dir.mkdir();
		
		String graphFileName = dir.getAbsolutePath()+File.separator+filename+".txt";
		try {
			PrintWriter writer = new PrintWriter(graphFileName);
			writer.write(result);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
