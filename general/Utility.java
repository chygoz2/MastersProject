package general;
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import exception.GraphFileReaderException;
import exception.MatrixException;
import generate.RandomGraphGenerator;


public final class Utility {
	
	public static String validateInput(String fileName){		
		FileReader reader = null;
		try {
			reader = new FileReader(fileName);
		} catch (FileNotFoundException e1) {
			return "Input file not found";
		}
		Scanner scanner;
		scanner = new Scanner(reader);

		if(!scanner.hasNextLine()){
			scanner.close();
			return "Input file is empty";
		}
		scanner.close();
		return null;
	}
	
	public static UndirectedGraph<Integer,Integer> makeGraphFromFile(String fileName) throws GraphFileReaderException{
	
		String r = validateInput(fileName);
		if(r!=null){
			throw new GraphFileReaderException(r);
		}
		
		FileReader reader = null;
		try {
			reader = new FileReader(fileName);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Scanner scanner = new Scanner(reader);
		int vertexCount = -1;
		
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
				scanner.close();
				throw new GraphFileReaderException("The number of columns in file do not match the required Vertex count");
			}
			for(int j=i+1; j<vertexCount; j++){
				try{
					int val = Integer.parseInt(stringI[j]);
					if(val>1 || val<0)
						throw new GraphFileReaderException("Entries in the graph file should consist of zeros and ones only");
					adjMatrix[i][j] = val;
					adjMatrix[j][i] = val;
				}catch(NumberFormatException e){
					throw new GraphFileReaderException("Entries in the graph file should consist of zeros and ones only");
				}
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
						if(nOne.getElement()< nTwo.getElement())
							g1.addEdge(nOne, nTwo);
					}
				}
			}
		}
		
		return g1;
	}
	
	public static void saveGraphToFile(int[][] A, double p, int no){
		StringBuilder out = new StringBuilder();
		String pp = String.format("%.1f", p); 
		
		for(int i=0; i<A.length; i++){
			for(int j=0; j<A[i].length; j++){
				out.append(A[i][j]+" ");
			}
			out.append("\n");
		}
	
		//get graph size
		int size = A.length;
		
		//create folder for saving generated graphs if none exists
		File f = new File("");
		String path = f.getAbsolutePath();
		String ggFolder = "generated_graphs2";
		File dir = new File(path+File.separator+ggFolder);
		dir.mkdir();
		
		//create folder for that size if not existing
		File dir2 = new File(path+File.separator+ggFolder+File.separator+"size_"+size);
		dir2.mkdir();
		
		//select suitable file name for the generated graph
		String graphFileName = dir2.getAbsolutePath()+File.separator+"graph_"+size+"_"+pp+"_"+no+".txt";
		
		try {
			FileWriter writer = new FileWriter(graphFileName);
			writer.write(out.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String saveGraphToFile(int[][] A, long no, String type){
		StringBuilder out = new StringBuilder();
		
		for(int i=0; i<A.length; i++){
			for(int j=0; j<A[i].length; j++){
				out.append(A[i][j]+" ");
			}
			out.append("\n");
		}
	
		//get graph size
		int size = A.length;
		
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
			writer.write(out.toString());
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
		Set<Integer> verticesElem = new HashSet<Integer>();
		Iterator<Graph.Vertex<Integer>> it = graph.vertices();
		while(it.hasNext()){
			verticesElem.add(it.next().getElement());
		}
		
		//find components
		while(!verticesElem.isEmpty()){
			Integer next = 0;
			for(Integer i: verticesElem){
				next = i;
				break;
			}
			List<Graph.Vertex<Integer>> compList = graph.depthFirstTraversal(graph.getVertexWithElement(next));
			components.add(makeGraphFromVertexSet(graph, compList));
			for(Graph.Vertex<Integer> v: compList){
				verticesElem.remove(v.getElement());
			}
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
	
	public static int[][] multiplyMatrix(int[][] a, int[][] b) throws MatrixException{
		if (a.length==0 || b.length==0) 
			throw new MatrixException(0);
		
		if (a[0].length != b.length) 
			throw new MatrixException(1);
       
        int[][] result = new int[a.length][b[0].length];		
		for(int i = 0; i < a.length; i++) {
		    int[] ira = a[i];
		    int[] irc = result[i];
		    for(int k = 0; k < a.length; k++) {
		        int[] krb = b[k];
		        int ikA = ira[k];
		        for(int j = 0; j < a.length; j++) {
		            irc[j] += ikA * krb[j];
		        }
		    }
		}
        return result;
	}
	
	public static void main(String [] args){
//		String fileName = "matrix.txt";
//		Utility.makeGraphFromFile(fileName);
//		generateRandomGraphFile(10,0.9,1);
		
//		int[][] a = {{1,2,3,3},{4,5,6,6},{7,8,9,9},{10,11,12,13}};
		Random r = new Random(System.currentTimeMillis());
		int n = 1000;
		int[][] a = new int[n][n];
		int[][] b = new int[n][n];
		for(int i=0; i<a.length; i++){
			for(int j=0; j<a.length; j++){
				a[i][j] = r.nextInt(10000);
				b[i][j] = r.nextInt(10000);
			}
		}
		int[][] res = null;
		try {
			long start = System.currentTimeMillis();
			res = multiplyMatrix(a,b);
			long stop= System.currentTimeMillis();
			System.out.println("Time taken: " + (stop-start));
		} catch (MatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		for(int i=0; i<res.length; i++){
//			for(int j=0; j<res[i].length; j++){
//				System.out.print((int)res[i][j]+" ");
//			}
//			System.out.println();
//		}
		
	}
}
