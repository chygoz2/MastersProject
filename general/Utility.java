package general;
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import exception.GraphFileReaderException;
import exception.MatrixException;
import generate.RandomGraphGenerator;

/**
 * class containing methods that are commonly used by the other classes
 * @author Chigozie Ekwonu
 *
 */
public final class Utility {
	
	/**
	 * method that creates a graph object from adjacency matrix present in a file
	 * @param fileName						the name of the file
	 * @return								the adjacency matrix created
	 * @throws GraphFileReaderException		exception thrown if the graph file is not valid
	 */
	public static UndirectedGraph<Integer,Integer> makeGraphFromFile(String fileName) throws GraphFileReaderException{
	
		//open file
		FileReader reader = null;
		try {
			reader = new FileReader(fileName);
		} catch (FileNotFoundException e1) {
			throw new GraphFileReaderException("Input file not found");
		}
		Scanner scanner;
		scanner = new Scanner(reader);

		if(!scanner.hasNextLine()){
			scanner.close();
			throw new GraphFileReaderException("Input file is empty");
		}
	
		int vertexCount = -1; //initial default vertex size
		
		//read each line of file into a list
		List<String> lines = new ArrayList<String>();
		while(scanner.hasNextLine()){
			lines.add(scanner.nextLine());
		}
		
		//get vertex count from the size of first row of file
		vertexCount = lines.get(0).split("[ ]+").length;
		int[][] adjMatrix = new int[vertexCount][vertexCount];
		
		//create adjacency matrix from read data 
		for(int i=0; i<vertexCount; i++){
			String[] stringI = lines.get(i).split("[ ]+");
			if(stringI.length != vertexCount){
				scanner.close();
				throw new GraphFileReaderException("The number of columns in file do not match the required Vertex count");
			}
			for(int j=i; j<vertexCount; j++){
				try{
					int val = Integer.parseInt(stringI[j]);
					if(val>1 || val<0)
						throw new GraphFileReaderException("Entries in the graph file should consist of zeros and ones only");
					adjMatrix[i][j] = val;
					adjMatrix[j][i] = val;
				}catch(NumberFormatException e){
					scanner.close();
					throw new GraphFileReaderException("Entries in the graph file should consist of zeros and ones only");
				}
			}
		}
		
		//create graph and add vertices
		UndirectedGraph<Integer,Integer> graph = makeGraphFromAdjacencyMatrix(adjMatrix);
		
		//close scanner and file reader objects
		try {
			scanner.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return graph;
	}
	
	/**
	 * method that creates a graph from adjacency matrix
	 * @param adjMatrix		the adjacency matrix
	 * @return				the created graph
	 */
	public static UndirectedGraph<Integer,Integer> makeGraphFromAdjacencyMatrix(int[][] adjMatrix){
		//create empty graph
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
	
	/**
	 * method that creates a graph induced by a vertex set
	 * @param graph			the original graph or supergraph
	 * @param vertices		the vertex set
	 * @return				the induced subgraph created
	 */
	public static UndirectedGraph<Integer,Integer> makeGraphFromVertexSet(UndirectedGraph<Integer,Integer> graph, Collection<Graph.Vertex<Integer>> vertices){
		UndirectedGraph<Integer,Integer> g1 = new UndirectedGraph<Integer,Integer>();
		
		//add the vertices
		for(Graph.Vertex<Integer> ve: vertices){
			g1.addVertex(ve.getElement());
		}		
		
		//add the edges
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
	
	/**
	 * method that saves a graph to file as an adjacency matrix. 
	 * The file is named according to its size, density and an index
	 * @param A		the adjacency matrix
	 * @param p		the edge probability
	 * @param no	the graph index
	 */
	public static void saveGraphToFile(int[][] A, double p, int no){
		StringBuilder out = new StringBuilder(); //create StringBuilder object
		String pp = String.format("%.1f", p); 
		
		//convert adjacency matrix to a StringBuilder object representation
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
		String ggFolder = "generated_graphs";
		File dir = new File(path+File.separator+ggFolder);
		dir.mkdir();
		
		//create folder for that size if not existing
		File dir2 = new File(path+File.separator+ggFolder+File.separator+"size_"+size);
		dir2.mkdir();
		
		//select suitable file name for the generated graph
		String graphFileName = dir2.getAbsolutePath()+File.separator+"graph_"+size+"_"+pp+"_"+no+".txt";
		
		//write the stringbuilder object to file
		try {
			FileWriter writer = new FileWriter(graphFileName);
			writer.write(out.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * method that saves a randomly generated patter-free graph to file as an adjacency matrix depending on graph size, type 
	 * of pattern-free graph it is
	 * @param A			the adjacency matrix
	 * @param no		a random number to make graph file names unique
	 * @param type		the type of pattern-free graph it is
	 * @return			the name of the file where the graph is saved to
	 */
	public static String saveGraphToFile(int[][] A, long no, String type){
		StringBuilder out = new StringBuilder(); //create a stringbuilder object
		
		//convert adjacency matrix to a StringBuilder object representation
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
		
		//write stringbuilder object to file
		try {
			FileWriter writer = new FileWriter(graphFileName);
			writer.write(out.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return graphFileName;
	}
	
	/**
	 * method to get the neighbourhood graph of a vertex
	 * @param graph			the original graph/supergraph that the neighbourhood graph is to be created from
	 * @param v				the vertex whose neighbourhood graph is to be computed
	 * @return				the neighbourhood graph
	 */
	public static UndirectedGraph<Integer,Integer> getNeighbourGraph(UndirectedGraph<Integer,Integer> graph, Graph.Vertex<Integer> v){
		//get v's neighbours;
		List<Graph.Vertex<Integer>> neighbours = new ArrayList<Graph.Vertex<Integer>>(); //list of the neighbours of the graph
		Iterator<Graph.Vertex<Integer>> it = graph.neighbours(v);
		while(it.hasNext()){
			neighbours.add(it.next());
		}
		return makeGraphFromVertexSet(graph, neighbours); //get the graph induced by the neighbour list
	}
	
	/**
	 * method that gets the components of a graph. The components are computed using depth first traversal
	 * @param graph		the graph whose components are required
	 * @return			list containing the components of the graph
	 */
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
			//find list of vertices reachable from the first vertex in the vertex set
			List<Graph.Vertex<Integer>> compList = graph.depthFirstTraversal(graph.getVertexWithElement(next));
			components.add(makeGraphFromVertexSet(graph, compList)); //create a graph from the list and add to the components list

			//remove the vertices that have already been used in order to get another component if any
			for(Graph.Vertex<Integer> v: compList){
				verticesElem.remove(v.getElement());
			}
		}
		
		return components;
	}
	
	/**
	 * method to multiply two matrices 
	 * idea was obtained from https://www.daniweb.com/programming/software-development/code/355645/optimizing-matrix-multiplication
	 * @param a 				two-dimensional array representing first matrix to be multiplied
	 * @param b					two-dimensional array representing second matrix to be multiplied
	 * @return					two-dimensional array containing the results
	 * @throws MatrixException	exception thrown if the dimensions of the matrices are not valid 
	 */
	public static int[][] multiplyMatrix(int[][] a, int[][] b) throws MatrixException{
		if (a.length==0 || b.length==0) 
			throw new MatrixException("Matrix empty");
		
		if (a[0].length != b.length) 
			throw new MatrixException("Invalid matrix dimensions found");
       
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
	
	/**
	 * method that returns a string containing the elements of vertices in a list
	 * @param list		the list of vertices
	 * @return			the string representation of the vertices' elements
	 */
	public static String printList(List<Graph.Vertex<Integer>> list){
		String out = "";

		for(Graph.Vertex<Integer> v:list){
			out+=v.getElement()+","; 
		}
		out = out.substring(0,out.length()-1);
		return out;
	}
	
	/**
	 * method that extracts the total time taken for a detection operation to run from a string
	 * returned by the detection class
	 * @param s		the string containing the results
	 * @return		the time taken for the detection or listing operation to run
	 */
	public static int getTotalTime(String s){
		String[] tokens = s.split("[ ]+");
		int time = 0;
		int limit = tokens[tokens.length-2].equals("not") ? tokens.length-2: tokens.length-1;
		for(int i=0; i<limit; i++){
			if(!tokens[i].equals("-"))
				time += Integer.parseInt(tokens[i]);
		}
		return time;
	}
}
