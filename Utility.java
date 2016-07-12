import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

public final class Utility {
	
	//class shouldn't be instantiated
	private Utility(){}
	
	public static UndirectedGraph<Integer,Integer> makeGraphFromFile(String fileName){
		FileReader reader = null;
		try {
			reader = new FileReader(fileName);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Input file not found");
			System.exit(0);
		}
		Scanner scanner;
		int vertexCount = -1;
		int[][] adjMatrix;
		scanner = new Scanner(reader);
		//get number of vertices
		if(!scanner.hasNextLine()){
			JOptionPane.showMessageDialog(null, "Input file is empty");
			System.exit(0);
		}
		
		try{
			String vertexCountLine = scanner.nextLine();
			vertexCount = Integer.parseInt(vertexCountLine);
		}catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Invalid entry for vertex count");
			System.exit(0);
		}
		
		if(vertexCount<1){
			JOptionPane.showMessageDialog(null, "Vertex count should be greater than zero");
			System.exit(0);
		}
		
		//if vertex count is valid, create empty adjacency matrix array
		adjMatrix = new int[vertexCount][vertexCount];
		
		String[] lines = new String[vertexCount];
		int c = 0;
		while(scanner.hasNextLine()){
			//validate if the number of lines is equal to the specified number of vertices
			if(c>=vertexCount){
				JOptionPane.showMessageDialog(null, "The number of lines in file do not match the required vertex count");
				System.exit(0);
			}
			lines[c++] = scanner.nextLine();
		}
		
		//create graph and add vertices
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		Vertex<Integer>[] vertices = new Vertex[vertexCount];
		for(int i=0; i<vertexCount; i++){
			vertices[i] = graph.addVertex(i);
		}
		
		//read adjacency matrix for edges
		for(int i=0; i<vertexCount; i++){
			String[] stringI = lines[i].split("[ ]+");
			if(stringI.length != vertexCount){
				JOptionPane.showMessageDialog(null, "The number of columns in file do not match the required vertex count");
				System.exit(0);
			}
			for(int j=i+1; j<vertexCount; j++){
				adjMatrix[i][j] = Integer.parseInt(stringI[j]);
				adjMatrix[j][i] = Integer.parseInt(stringI[j]);
			}
		}
		
		//add the edges
		for(int i=0; i<vertexCount; i++){
			for(int j=i+1; j<vertexCount; j++){
				if(adjMatrix[i][j] == 1){
					graph.addEdge(vertices[i], vertices[j]);
				}
			}
		}
		
		//do the id mapping
		//graph.mapVertexToId();
		
		//prints out the graph created
		//printGraph(graph);
		
		scanner.close();
		return graph;
	}
	
	public static UndirectedGraph<Integer,Integer> makeRandomGraph(int v, double p){
		if(p<0.0 || p>1.0){
			JOptionPane.showMessageDialog(null, "Edge probability should be between 0 and 1");
			System.exit(0);
		}
		
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		Vertex<Integer>[] vertices = new Vertex[v];
		//add the vertices
		for(int i=0; i<v; i++){
			vertices[i] = graph.addVertex(i);
		}
		
		//add the edges with given edge probability
		for(int i=0; i<v; i++){
			for(int j=i+1; j<v; j++){
				Random random = new Random();
				double rand = random.nextDouble();
				if(rand<p){
					graph.addEdge(vertices[i], vertices[j]);
				}
			}
		}
		
		//printGraph(graph);
		//graph.printAdjacencyMatrix();
		saveGraphToFile(graph);
		return graph;
	}
	
	public static void saveGraphToFile(UndirectedGraph graph){
		String out = "";
		out += String.format("%d%n", graph.size());
		Iterator<Vertex> vertices = graph.vertices();
		
		while(vertices.hasNext()){
			Vertex v1 = vertices.next();
			Iterator<Vertex> vertices2 = graph.vertices();
			while(vertices2.hasNext()){
				Vertex v2 = vertices2.next();
				if(graph.containsEdge(v1, v2)){
					out += "1 ";
				}else{
					out += "0 ";
				}
			}
			out+=String.format("%n");
		}
		
		//get graph size
		int size = graph.size();
		
		//create folder for saving generated graphs if none exists
		File f = new File("");
		String path = f.getAbsolutePath();
		String ggFolder = "generated_graphs";
		File dir = new File(path+File.separator+ggFolder);
		dir.mkdir();
		
		
		//create folder for that size if not existing
		File dir2 = new File(path+File.separator+ggFolder+File.separator+"size_"+size);
		dir2.mkdir();
		
		//select suitable file name for the generated graph
		String graphFileName = dir2.getAbsolutePath()+File.separator+"graph_"+size+"_"+System.currentTimeMillis()+".txt";
//		System.out.println(graphFileName);
		
		try {
			FileWriter writer = new FileWriter(graphFileName);
			writer.write(out);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//method to create subgraph in the neighbourhood of vertex v
	public static UndirectedGraph<Integer,Integer> getNeighbourGraph(UndirectedGraph graph, Vertex v){
		//get v's neighbours;
		List<Vertex> neighbours = new ArrayList<Vertex>();
		Iterator<Vertex> it = graph.neighbours(v);
		while(it.hasNext()){
			neighbours.add(it.next());
		}
		return makeGraphFromVertexSet(graph, neighbours);
	}
	
	public static UndirectedGraph<Integer,Integer> makeGraphFromVertexSet(UndirectedGraph graph, Collection<Vertex> vertices){
		UndirectedGraph<Integer,Integer> g1 = new UndirectedGraph<Integer,Integer>();
		
		//add the vertices
		for(Vertex<Integer> ve: vertices){
			g1.addVertex(ve.getElement());
		}		
		
		Iterator<Vertex<Integer>> vIt = g1.vertices();
		while(vIt.hasNext()){
			Iterator<Vertex<Integer>> vIt2 = g1.vertices();
			Vertex<Integer> one = vIt.next();
			while(vIt2.hasNext()){
				Vertex<Integer> two = vIt2.next();
				Vertex<Integer> nOne = graph.getVertexWithElement((int) one.getElement());
				Vertex<Integer> nTwo = graph.getVertexWithElement((int)two.getElement());
				
				if(graph.containsEdge(nOne, nTwo)){
					if(!g1.containsEdge(one,two)){
						g1.addEdge(one, two);
					}
				}
			}
		}
		
		return g1;
	}
	
	public static List<UndirectedGraph> getComponents(UndirectedGraph graph){
		List<UndirectedGraph> components = new ArrayList<UndirectedGraph>();
		//get vertices list
		List<Vertex> vertices = new ArrayList<Vertex>();
		Iterator<Vertex> it = graph.vertices();
		while(it.hasNext()){
			vertices.add(it.next());
		}
		
		//find components
		while(!vertices.isEmpty()){
			List<Vertex> compList = graph.depthFirstTraversal(vertices.get(0));
//			System.out.println("Vertices in component found are ");
//			for(Vertex v: compList){
//				System.out.print(v.getElement()+", ");
//			}
			components.add(makeGraphFromVertexSet(graph, compList));
			vertices.removeAll(compList);
		}
		
		return components;
	}
	
	public static void printGraph(UndirectedGraph graph2){
		System.out.println("Graph size is "+graph2.size());
		Iterator<Vertex> it = graph2.vertices();
		
		System.out.println("Graph vertices");
		while (it.hasNext()){
			Vertex<Integer> v = it.next();
			System.out.print(v.getElement()+", ");
		}
		System.out.println("\nGraph edges:");
		Iterator<Edge> it2 = graph2.edges();
		while (it2.hasNext()){
			UndirectedGraph.UnEdge edge = (UndirectedGraph.UnEdge)it2.next();
			System.out.print("{"+edge.getSource().getElement()+", "+ edge.getDestination().getElement()+"},");
		}
		System.out.println();
	}
	
	//method to partition the vertices into low degree vertices and high degree vertices
		public static Set[] partitionVertices(UndirectedGraph graph){
			Set[] vertices = new Set[2];
			vertices[0] = new HashSet<Vertex>();
			vertices[1] = new HashSet<Vertex>();
			
			//get vertices
			Iterator<Vertex> vertexIterator = graph.vertices();
			
			//get edges
			Iterator<Edge> edgeIterator = graph.edges();
			
			//get number of edges
			int noOfEdges = 0;
			while(edgeIterator.hasNext()){
				edgeIterator.next();
				noOfEdges++;
			}
			

			//calculate D for vertex partitioning
			//double alpha = 2.376; //constant from Coppersmith-Winograd matrix multiplication algorithm
			double alpha = 3;
			double pow = (alpha-1)/(alpha+1);
			double D = Math.pow(noOfEdges, pow);
			
			while(vertexIterator.hasNext()){
				Vertex<Integer> v = vertexIterator.next();
				if(graph.degree(v)>D)
					vertices[1].add(v);
				else
					vertices[0].add(v);
			}
			
			return vertices;
		}
	
	public static void main(String [] args){
		String fileName = "matrix.txt";
		//Utility.makeGraphFromFile(fileName);
		makeRandomGraph(8,0.8);
	}
}
