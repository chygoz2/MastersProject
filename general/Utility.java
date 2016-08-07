package general;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

import efficientdetection.MatrixException;

public final class Utility {
	
	public static UndirectedGraph<Integer,Integer> makeGraphFromFile(String fileName){
		FileReader reader = null;
		try {
			reader = new FileReader(fileName);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "Input file not found");
			return null;
		}
		Scanner scanner;
		int vertexCount = -1;
		scanner = new Scanner(reader);
		//get number of vertices
		if(!scanner.hasNextLine()){
			JOptionPane.showMessageDialog(null, "Input file is empty");
			scanner.close();
			return null;
		}
		
		
		List<String> lines = new ArrayList<String>();
		while(scanner.hasNextLine()){
			lines.add(scanner.nextLine());
		}
		
		//get vertex count from the size of first row of file
		vertexCount = lines.get(0).split("[ ]+").length;
		int[][] adjMatrix = new int[vertexCount][vertexCount];
		
		//read adjacency matrix 
		for(int i=0; i<vertexCount; i++){
			String[] stringI = lines.get(i).split("[ ]+");
			if(stringI.length != vertexCount){
				JOptionPane.showMessageDialog(null, "The number of columns in file do not match the required Vertex count");
				scanner.close();
				return null;
			}
			for(int j=i+1; j<vertexCount; j++){
				adjMatrix[i][j] = Integer.parseInt(stringI[j]);
				adjMatrix[j][i] = Integer.parseInt(stringI[j]);
			}
		}
		
		//create graph and add vertices
		UndirectedGraph<Integer,Integer> graph = makeGraphFromAdjacencyMatrix(adjMatrix);
		
		try {
			scanner.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return graph;
	}
	
	public static UndirectedGraph<Integer,Integer> makeRandomGraph(int v, double p){
		if(p<0.0 || p>1.0){
			JOptionPane.showMessageDialog(null, "Edge probability should be between 0 and 1");
			System.exit(0);
		}
		
		int[][] adjMatrix = new int[v][v];
		
		//add the edges with given edge probability
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for(int i=0; i<v; i++){
			for(int j=i+1; j<v; j++){
				double rand = random.nextDouble();
				if(rand<p){
//					graph.addEdge(vertices[i], vertices[j]);
					adjMatrix[i][j] = 1;
					adjMatrix[j][i] = 1;
				}
			}
		}
		
		UndirectedGraph<Integer,Integer> graph = makeGraphFromAdjacencyMatrix(adjMatrix);
		//printGraph(graph);
		//graph.printAdjacencyMatrix();
		return graph;
	}
	
	public static UndirectedGraph<Integer,Integer> makeGraphFromAdjacencyMatrix(int[][] adjMatrix){
		UndirectedGraph<Integer,Integer> graph = new UndirectedGraph<Integer,Integer>();
		Graph.Vertex<Integer>[] vertices = new Graph.Vertex[adjMatrix.length];
		
		//add vertices
		for(int i=0; i<adjMatrix.length; i++){
			vertices[i] = graph.addVertex(i);
		}
		
		
		//add the edges
		for(int i=0; i<adjMatrix.length; i++){
			for(int j=i+1; j<adjMatrix.length; j++){
				if(adjMatrix[i][j] == 1){
					graph.addEdge(vertices[i], vertices[j]);
				}
			}
		}
		
		return graph;
	}
	
	public static UndirectedGraph<Integer,Integer> makeGraphFromVertexSet(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> vertices){
		UndirectedGraph<Integer,Integer> g1 = new UndirectedGraph<Integer,Integer>();
		
		//add the vertices
		for(Graph.Vertex<Integer> ve: vertices){
			g1.addVertex(ve.getElement());
		}		
		
		for(Graph.Vertex<Integer> one: vertices){
			for(Graph.Vertex<Integer> two: vertices){
				if(two.getElement()!=one.getElement()){
					Graph.Vertex<Integer> nOne = g1.getVertexWithElement(one.getElement());
					Graph.Vertex<Integer> nTwo = g1.getVertexWithElement(two.getElement());
					
					if(graph.containsEdge(graph.getVertexWithElement(one.getElement()),graph.getVertexWithElement(two.getElement()))){
						if(!g1.containsEdge(nOne,nTwo)){
							g1.addEdge(nOne, nTwo);
						}
					}
				}
			}
		}
		
		
		
		
//		Iterator<Graph.Vertex<Integer>> vIt = g1.vertices();
//		while(vIt.hasNext()){
//			Iterator<Graph.Vertex<Integer>> vIt2 = g1.vertices();
//			Graph.Vertex<Integer> one = vIt.next();
//			while(vIt2.hasNext()){
//				Graph.Vertex<Integer> two = vIt2.next();
//				if((int)two.getElement()!=(int)(one.getElement())){
//					Graph.Vertex<Integer> nOne = graph.getVertexWithElement(one.getElement());
//					Graph.Vertex<Integer> nTwo = graph.getVertexWithElement(two.getElement());
//					
//					if(graph.containsEdge(nOne, nTwo)){
//						if(!g1.containsEdge(one,two)){
//							g1.addEdge(one, two);
//						}
//					}
//				}
//			}
//		}
		
		return g1;
	}

	public static void generateRandomGraphFile(int v, double p, int no){
		for(int i=1; i<=no; i++){
			UndirectedGraph<Integer,Integer> g = makeRandomGraph(v,p);
			saveGraphToFile(g,p,i);
		}
	}
	public static void saveGraphToFile(UndirectedGraph<Integer,Integer> graph, double p, int no){
		String out = "";
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		while(vertices.hasNext()){
			Graph.Vertex<Integer> v1 = vertices.next();
			Iterator<Graph.Vertex<Integer>> vertices2 = graph.vertices();
			while(vertices2.hasNext()){
				Graph.Vertex<Integer> v2 = vertices2.next();
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
		String graphFileName = dir2.getAbsolutePath()+File.separator+"graph_"+size+"_"+p+"_"+no+".txt";
		
		try {
			FileWriter writer = new FileWriter(graphFileName);
			writer.write(out);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String saveGraphToFile(UndirectedGraph<Integer,Integer> graph, int no, String type){
		String out = "";
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
		
		while(vertices.hasNext()){
			Graph.Vertex<Integer> v1 = vertices.next();
			Iterator<Graph.Vertex<Integer>> vertices2 = graph.vertices();
			while(vertices2.hasNext()){
				Graph.Vertex<Integer> v2 = vertices2.next();
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
		String ggFolder = "generated_patternfree_graphs";
		File dir = new File(path+File.separator+ggFolder);
		dir.mkdir();
		
		//create folder for the pattern type if not existing
		File dir3 = new File(path+File.separator+ggFolder+File.separator+type+"_free");
		dir3.mkdir();
		
		//create folder for that size if not existing
		File dir2 = new File(path+File.separator+ggFolder+File.separator+type+"_free"+File.separator+"size_"+size);
		dir2.mkdir();
		
		//select suitable file name for the generated graph
		String graphFileName = dir2.getAbsolutePath()+File.separator+"graph_"+size+"_"+no+".txt";
		
		try {
			FileWriter writer = new FileWriter(graphFileName);
			writer.write(out);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return graphFileName;
	}
	
	//method to create subgraph in the neighbourhood of Graph.Vertex v
	public static UndirectedGraph<Integer,Integer> getNeighbourGraph(UndirectedGraph<Integer,Integer> graph, Graph.Vertex<Integer> v){
		//get v's neighbours;
		List<Graph.Vertex<Integer>> neighbours = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> it = graph.neighbours(v);
		while(it.hasNext()){
			neighbours.add(it.next());
		}
		return makeGraphFromVertexSet(graph, neighbours);
	}
	
	public static List<UndirectedGraph<Integer,Integer>> getComponents(UndirectedGraph<Integer,Integer> graph){
		List<UndirectedGraph<Integer,Integer>> components = new ArrayList<UndirectedGraph<Integer,Integer>>();
		//get vertices list
		List<Integer> vertices = new ArrayList<Integer>();
		Iterator<Graph.Vertex<Integer>> it = graph.vertices();
		while(it.hasNext()){
			vertices.add(it.next().getElement());
		}
		
		//find components
		while(!vertices.isEmpty()){
			List<Graph.Vertex<Integer>> compList = graph.depthFirstTraversal(graph.getVertexWithElement(vertices.get(0)));
//			System.out.println("Vertices in component found are ");
//			for(Graph.Vertex v: compList){
//				System.out.print(v.getElement()+", ");
//			}
			components.add(makeGraphFromVertexSet(graph, compList));
			for(Graph.Vertex<Integer> v: compList){
				vertices.remove(v.getElement());
			}
//			vertices.removeAll(compList);
		}
		
		return components;
	}
	
	public static void printGraph(UndirectedGraph graph2){
//		System.out.println("Graph size is "+graph2.size());
		Iterator<Graph.Vertex> it = graph2.vertices();
		
//		System.out.println("Graph vertices");
		while (it.hasNext()){
			Graph.Vertex<Integer> v = it.next();
			System.out.print(v.getElement()+", ");
		}
//		System.out.println("\nGraph edges:");
//		Iterator<Graph.Edge> it2 = graph2.edges();
//		while (it2.hasNext()){
//			UndirectedGraph.UnEdge edge = (UndirectedGraph.UnEdge)it2.next();
//			System.out.print("{"+edge.getSource().getElement()+", "+ edge.getDestination().getElement()+"},");
//		}
		System.out.println();
	}
	
	//method to partition the vertices into low degree vertices and high degree vertices
	public static List<Graph.Vertex<Integer>>[] partitionVertices(UndirectedGraph<Integer,Integer> graph){
		List<Graph.Vertex<Integer>>[] vertices = new List[2];
		vertices[0] = new ArrayList<Graph.Vertex<Integer>>();
		vertices[1] = new ArrayList<Graph.Vertex<Integer>>();
		
		//get vertices
		Iterator<Graph.Vertex<Integer>> vertexIterator = graph.vertices();
		
		//get edges
		Iterator<Graph.Edge<Integer>> edgeIterator = graph.edges();
		
		//get number of edges
		int noOfEdges = 0;
		while(edgeIterator.hasNext()){
			edgeIterator.next();
			noOfEdges++;
		}
		

		//calculate D for Graph.Vertex partitioning
		//double alpha = 2.376; //constant from Coppersmith-Winograd matrix multiplication algorithm
		double alpha = 3;
		double pow = (alpha-1)/(alpha+1);
		double D = Math.pow(noOfEdges, pow);
		
		while(vertexIterator.hasNext()){
			Graph.Vertex<Integer> v = vertexIterator.next();
			if(graph.degree(v)>D)
				vertices[1].add(v);
			else
				vertices[0].add(v);
		}
		
		return vertices;
	}
	
	public static double[][] multiplyMatrix(double[][] a, double[][] b) throws MatrixException{
		if (a.length==0 || b.length==0) 
			throw new MatrixException(0);
		
		if (a[0].length != b.length) 
			throw new MatrixException(1);
       
        double[][] result = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b[i].length; j++)
                for (int k = 0; k < a[i].length; k++)
                    result[i][j] += a[i][k] * b[k][j];
        return result;
	}
	
	public static void main(String [] args){
//		String fileName = "matrix.txt";
//		Utility.makeGraphFromFile(fileName);
		generateRandomGraphFile(15,1.0,1);
		
//		double[][] a = {{1,2,3},{4,5,6},{7,8,9}};
//		double[][] res = matrixMultiply(a,a);
//		for(int i=0; i<res.length; i++){
//			for(int j=0; j<res[i].length; j++){
//				System.out.print((int)res[i][j]+" ");
//			}
//			System.out.println();
//		}
		
	}
}
